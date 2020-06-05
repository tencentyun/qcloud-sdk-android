package com.tencent.qcloud.core.logger;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * File Log Printer.
 * <p>
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 * <p>
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class FileLogAdapter implements LogAdapter {

    private String alias;
    private int minPriority;

    private static final String LOG_DIR = "QCloudLogs";

    private static final int MAX_FILE_SIZE = 3 * 1024 * 1024; // Log文件大小
    private static final long LOG_FLUSH_DURATION = 10 * 1000; // Log Buffer的flush周期
    private static final long BUFFER_SIZE = 32 * 1024; // Log内存缓冲大小
    private static final int MAX_FILE_COUNT = 30; // 最多日志文件

    //log根目录
    private File logRootDir;
    private File latestLogFile;

    private static final int MSG_FLUSH_ALL = 0;
    private static final int MSG_FLUSH_CONTENT = 1;
    // 处理log者
    private Handler handler;
    //buffer
    private List<FileLogItem> bufferRecord = Collections.synchronizedList(new ArrayList<FileLogItem>());
    private volatile long mBufferSize = 0;

    private static final byte[] object = new byte[0];

    private static FileLogAdapter instance;

    /**
     * 实例化一个文件日志记录器。默认写入 {@link QCloudLogger#INFO} 级别以上的日志。
     *
     * @param context 上下文
     * @param alias   logger 文件别名
     */
    public static FileLogAdapter getInstance(Context context, String alias){
        return getInstance(context, alias, QCloudLogger.INFO);
    }

    public static FileLogAdapter getInstance(Context context, String alias, int minPriority){
        synchronized (FileLogAdapter.class){
            if(instance == null){
                instance = new FileLogAdapter(context, alias, minPriority);
            }
        }
        return instance;
    }
    /**
     * 实例化一个文件日志记录器
     *
     * @param context     上下文
     * @param alias       logger 文件别名
     * @param minPriority 最小的日志级别，低于这个级别的日志将不会被保存。
     */
    private FileLogAdapter(Context context, String alias, int minPriority) {
        this.alias = alias;
        this.minPriority = minPriority;
        this.logRootDir = new File(context.getExternalCacheDir() + File.separator + LOG_DIR);

        HandlerThread handlerThread = new HandlerThread("log_handlerThread", Thread.MIN_PRIORITY);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_FLUSH_ALL:
                        flush();
                        sendEmptyMessageDelayed(MSG_FLUSH_ALL, LOG_FLUSH_DURATION);
                        break;
                    case MSG_FLUSH_CONTENT:
                        input();
                        break;
                }
            }
        };
        Message message = handler.obtainMessage();
        message.what = MSG_FLUSH_ALL;
        handler.sendMessage(message);
    }

    @Override
    public boolean isLoggable(int priority, @Nullable String tag) {
        return priority >= minPriority;
    }

    @Override
    public synchronized void log(int priority, @NonNull String tag, @NonNull String message, @Nullable Throwable tr) {
        FileLogItem r = new FileLogItem(tag, priority, message, tr);
        bufferRecord.add(r);
        mBufferSize += r.getLength();
        //有消息进入，发送一个通知
        handler.removeMessages(MSG_FLUSH_CONTENT);
        handler.sendEmptyMessageDelayed(MSG_FLUSH_CONTENT, 500);
    }

    private String formatDateString(long times) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss", Locale.getDefault());
        return simpleDateFormat.format(times);
    }

    private boolean isSameDay(String timesString, long dateTime) {
        SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss", Locale.getDefault());
        SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            String string1 = fmt2.format(fmt1.parse(timesString));
            String string2 = fmt2.format(dateTime);
            return string1.equals(string2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public File[] getLogFilesDesc(int limit) {
        if (logRootDir.listFiles() != null && logRootDir.listFiles().length > 0) {
            File[] logFiles = logRootDir.listFiles();
            Arrays.sort(logFiles, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    return Long.valueOf(rhs.lastModified()).compareTo(Long.valueOf(lhs.lastModified()));
                }
            });
            File[] required = new File[Math.min(limit, logFiles.length)];
            System.arraycopy(logFiles, 0, required, 0, required.length);
            return required;
        }
        return null;
    }

    //log 文件(最新的）
    private File getLogFile(long times) {
        File[] logFiles = logRootDir.listFiles();
        if (latestLogFile == null) {
            if (!logRootDir.exists() && !logRootDir.mkdirs()) {
                return null;
            }
            if (logFiles != null && logFiles.length > 0) {
                Arrays.sort(logFiles, new Comparator<File>() {
                    @Override
                    public int compare(File lhs, File rhs) {
                        return Long.valueOf(rhs.lastModified()).compareTo(Long.valueOf(lhs.lastModified()));
                    }
                });
                latestLogFile = logFiles[0];
            }
        }
        // 如果是同一天且文件没有超过最大大小，返回最新日志文件
        if (latestLogFile != null && latestLogFile.length() < MAX_FILE_SIZE) {
            String fileLogDate = latestLogFile.getName().replace(".log", "");
            if (isSameDay(fileLogDate, times)) {
                return latestLogFile;
            }
        }

        // 创建一个新文件
        latestLogFile = new File(logRootDir + File.separator + formatDateString(times) + ".log");
        // 清除过时文件
        cleanFilesIfNecessary(logFiles);
        return latestLogFile;
    }

    private void cleanFilesIfNecessary(File[] logFiles) {
        if (logFiles != null && logFiles.length >= MAX_FILE_COUNT) {
            logFiles[logFiles.length - 1].delete();
        }
    }

    //写入日志(同步)
    private void write(List<FileLogItem> listInfo) {
        synchronized (object) {
            if (listInfo == null) return;
            FileOutputStream fos = null;
            //noinspection TryWithIdenticalCatches
            try {
                File file = getLogFile(System.currentTimeMillis());
                if (file != null) {
                    fos = new FileOutputStream(file, true);
                    for (int i = 0; i < listInfo.size(); i++) {
                        fos.write(listInfo.get(i).toString().getBytes("UTF-8"));
                    }
                    fos.flush();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private synchronized void flush() {
        if (mBufferSize <= 0) return;
        write(bufferRecord);
        bufferRecord.clear();
        mBufferSize = 0;
    }

    private synchronized void input() {
        if (mBufferSize > BUFFER_SIZE) {
            flush();
        }
    }
}
