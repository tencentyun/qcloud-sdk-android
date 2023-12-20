package com.tencent.qcloud.track.utils;

import android.content.Context;
import android.util.Log;

import com.tencent.qcloud.track.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件文件操作 主要用于缓存事件和读取缓存
 * <p>
 * Created by jordanqin on 2023/9/6 19:37.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class EventFileUtils {
    /**
     * 将上报时间和参数写到本地文件中 最多10M
     * @param context context
     * @param eventCode 事件code
     * @param params 参数
     */
    public static void appendEventToFile(Context context, String eventCode, Map<String, String> params) {
        if(context == null) return;

        File file = new File(context.getExternalCacheDir() + File.separator + Constants.BEACON_EVENT_CACHE_FILE_NAME);

        // 检查文件所在的目录是否存在，如果不存在则创建
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                return;
            }
        }
        // 检查文件是否存在
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                Log.e("FileUtils", "Error creating new file", e);
                return;
            }
        }

        // 最多延迟上报10M 检查文件大小，如果大于10MB则返回
        long fileSizeInBytes = file.length();
        long fileSizeInMB = fileSizeInBytes / (1024 * 1024);
        if (fileSizeInMB > 10) {
            Log.e("FileUtils", "File size is larger than 10MB");
            return;
        }

        OutputStreamWriter outputStreamWriter = null;

        try {
            // 打开文件输出流，设置为追加模式
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            // 将eventCode和params转换为字符串
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Event code: ").append(eventCode).append("\n");
            stringBuilder.append("Params: ").append("\n");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            stringBuilder.append("\n");

            // 将字符串追加到文件中
            outputStreamWriter.write(stringBuilder.toString());
            outputStreamWriter.flush();
        } catch (IOException e) {
            Log.e("FileUtils", "Error appending event to file", e);
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e("FileUtils", "Error closing OutputStreamWriter", e);
                }
            }
        }
    }

    /**
     * 从本地文件中读取上报事件并删除文件
     * @param context context
     * @param eventCodes 事件code列表
     * @param paramsList 参数列表
     */
    public static void readEventFromFile(Context context, List<String> eventCodes, List<Map<String, String>> paramsList) {
        if(context == null) return;

        File file = new File(context.getExternalCacheDir() + File.separator + Constants.BEACON_EVENT_CACHE_FILE_NAME);
        BufferedReader bufferedReader = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            Map<String, String> params = new HashMap<>();
            String eventCode = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("Event code: ")) {
                    // 如果有新的eventCode，将之前的eventCode和params添加到列表中
                    if (!eventCode.isEmpty()) {
                        eventCodes.add(eventCode);
                        paramsList.add(params);
                        params = new HashMap<>();
                    }
                    eventCode = line.substring("Event code: ".length());
                } else if (line.contains(": ")) {
                    String[] parts = line.split(": ", 2);
                    params.put(parts[0], parts[1]);
                }
            }
            // 添加最后一个eventCode和params到列表中
            if (!eventCode.isEmpty()) {
                eventCodes.add(eventCode);
                paramsList.add(params);
            }
        } catch (IOException e) {
            Log.e("FileUtils", "Error reading event from file", e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e("FileUtils", "Error closing BufferedReader", e);
                }
            }

            // 删除文件
            if (file.exists()) {
                if (!file.delete()) {
                    Log.e("FileUtils", "Error deleting file");
                }
            }
        }
    }

    /**
     * 本地事件缓存文件是否存在
     * @param context context
     * @return 本地事件缓存文件是否存在
     */
    public static boolean eventFileExists(Context context){
        if(context == null) return false;

        File file = new File(context.getExternalCacheDir() + File.separator + Constants.BEACON_EVENT_CACHE_FILE_NAME);
        return file.exists();
    }
}
