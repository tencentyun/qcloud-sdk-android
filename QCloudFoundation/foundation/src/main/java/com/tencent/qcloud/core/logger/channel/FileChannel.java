/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.qcloud.core.logger.channel;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.tencent.qcloud.core.logger.LogEntity;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileChannel extends BaseLogChannel {
    private static final String LOG_DIR = "QCloudLogs";

    private static final int MAX_FILE_SIZE = 3 * 1024 * 1024; // Log文件大小
    private static final long LOG_FLUSH_DURATION = 10 * 1000; // Log Buffer的flush周期
    private static final long BUFFER_SIZE = 32 * 1024; // Log内存缓冲大小
    private static final int MAX_FILE_COUNT = 30; // 最多日志文件

    //log根目录
    private final File logRootDir;
    private File latestLogFile;

    private static final int MSG_FLUSH_ALL = 0;
    private static final int MSG_FLUSH_CONTENT = 1;
    // 处理log者
    private final Handler handler;
    //buffer
    private final List<LogEntity> bufferRecord = Collections.synchronizedList(new ArrayList<>());
    private volatile long mBufferSize = 0;

    private static final byte[] object = new byte[0];

    private byte[] encryptionKey = null; // 默认不加密
    private byte[] ivParameter = null;

    private static FileChannel instance;

    /**
     * 实例化一个文件日志记录器。
     *
     * @param context 上下文
     */
    public static FileChannel getInstance(Context context) {
        synchronized (FileChannel.class) {
            if (instance == null) {
                instance = new FileChannel(context);
            }
        }
        return instance;
    }

    /**
     * 实例化一个文件日志记录器
     *
     * @param context 上下文
     */
    private FileChannel(Context context) {
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
    public synchronized void log(LogEntity entity) {
        if (!isLoggable(entity)) {
            return;
        }
        bufferRecord.add(entity);
        mBufferSize += entity.getLength();
        //有消息进入，发送一个通知
        handler.removeMessages(MSG_FLUSH_CONTENT);
        handler.sendEmptyMessageDelayed(MSG_FLUSH_CONTENT, 500);
    }

    public boolean isLoggable(LogEntity entity) {
        if (!isEnabled() || entity == null) return false;
        return entity.getLevel().isLoggable(getMinLevel());
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

    public String getLogRootDir() {
        return logRootDir.getAbsolutePath();
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
                // 查找匹配当前加密状态的最新文件
                for (File file : logFiles) {
                    boolean isEncryptedFile = file.getName().contains("_encrypt");
                    if ((encryptionKey != null && isEncryptedFile) ||
                        (encryptionKey == null && !isEncryptedFile)) {
                        latestLogFile = file;
                        break;
                    }
                }
            }
        }
        // 如果是同一天、相同加密状态且文件没有超过最大大小，返回最新日志文件
        if (latestLogFile != null && latestLogFile.length() < MAX_FILE_SIZE) {
            String fileName = latestLogFile.getName();
            String fileLogDate = fileName.replace("_encrypt.log", "").replace(".log", "");
            if (isSameDay(fileLogDate, times)) {
                boolean isEncryptedFile = fileName.contains("_encrypt");
                if ((encryptionKey != null && isEncryptedFile) ||
                    (encryptionKey == null && !isEncryptedFile)) {
                    return latestLogFile;
                }
            }
        }

        // 创建一个新文件，根据加密状态添加后缀
        String fileName = formatDateString(times) + (encryptionKey != null ? "_encrypt" : "") + ".log";
        latestLogFile = new File(logRootDir + File.separator + fileName);
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
    private void write(List<LogEntity> listInfo) {
        synchronized (object) {
            if (listInfo == null) return;
            DataOutputStream dos = null;
            try {
                File file = getLogFile(System.currentTimeMillis());
                if (file != null) {
                    // 使用8KB缓冲区的BufferedOutputStream包装
                    dos = new DataOutputStream(new BufferedOutputStream(
                            new FileOutputStream(file, true), 8192));
                    for (int i = 0; i < listInfo.size(); i++) {
                        byte[] logBytes = listInfo.get(i).toString().getBytes("UTF-8");
                        if (encryptionKey != null) {
                            try {
                                appendEncryptedLog(dos, logBytes);
                            } catch (Exception e) {
                                e.printStackTrace();
                                // 加密失败放弃本次日志
                            }
                        } else {
                            dos.write(logBytes);
                        }
                    }
                    // 确保缓冲区数据写入磁盘
                    dos.flush();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (dos != null) {
                    try {
                        dos.close();
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

    /**
     * 设置加密密钥
     *
     * @param key 加密密钥(16/24/32字节对应AES-128/192/256)
     * @param iv  初始化向量(16字节)
     */
    public void setEncryptionKey(byte[] key, byte[] iv) {
        encryptionKey = key != null ? key.clone() : null;
        ivParameter = iv != null ? iv.clone() : null;
    }

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    // [4字节长度头][密文数据][4字节长度头][密文数据]...
    public void appendEncryptedLog(DataOutputStream dos, byte[] logBytes) throws Exception {
        javax.crypto.spec.IvParameterSpec ivSpec = new javax.crypto.spec.IvParameterSpec(ivParameter);
        javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(encryptionKey, "AES");

        byte[] encrypted = encryptSingle(logBytes, keySpec, ivSpec);

        // 追加写入：先写长度头(4字节)，再写密文
        dos.writeInt(encrypted.length);
        dos.write(encrypted);
    }

    // 单条日志加密（私有方法）
    private byte[] encryptSingle(byte[] plaintext, SecretKeySpec keySpec, IvParameterSpec ivSpec) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(plaintext);
    }
}
