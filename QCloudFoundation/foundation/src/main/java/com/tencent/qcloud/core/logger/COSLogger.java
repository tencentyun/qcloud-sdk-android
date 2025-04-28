package com.tencent.qcloud.core.logger;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.qcloud.core.logger.channel.BaseLogChannel;
import com.tencent.qcloud.core.logger.channel.ClsChannel;
import com.tencent.qcloud.core.logger.channel.CosLogListener;
import com.tencent.qcloud.core.logger.channel.FileChannel;
import com.tencent.qcloud.core.logger.channel.ListenerChannel;
import com.tencent.qcloud.core.logger.channel.LogcatChannel;
import com.tencent.qcloud.core.util.ContextHolder;
import com.tencent.qcloud.track.cls.ClsLifecycleCredentialProvider;
import com.tencent.qcloud.track.service.ClsTrackService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Created by jordanqin on 2025/3/21 21:13.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class COSLogger {
    private static COSLogger instance;
    private final List<BaseLogChannel> channels = new ArrayList<>();
    private final LogcatChannel logcatChannel;
    private final FileChannel fileChannel;
    private LogLevel minLevel;
    private final Context context;

    // 脱敏规则映射表
    private final Map<String, String> sensitivePatterns = new HashMap<>();
    // 默认token脱敏规则 密钥ID和token

    // 拓展信息
    private Map<String, String> extras = new HashMap<>();
    private String deviceID;
    private String deviceModel;
    private String appVersion;

    private COSLogger(boolean enableLogcat, boolean enableFile) {
        this.context = ContextHolder.getAppContext();
        minLevel = LogLevel.VERBOSE;

        logcatChannel = new LogcatChannel();
        logcatChannel.setEnabled(enableLogcat);
        channels.add(logcatChannel);
        fileChannel = FileChannel.getInstance(context);
        fileChannel.setEnabled(enableFile);
        channels.add(fileChannel);

        // 添加默认token脱敏规则
        sensitivePatterns.put("(q-ak=)[^&\\\\s]+", "$1***");
        sensitivePatterns.put("(x-cos-security-token:\\s*)[^\\s]+", "$1***");
    }

    protected static COSLogger getInstance() {
        synchronized (COSLogger.class) {
            if (instance == null) {
                instance = new COSLogger(false, false); // 默认关闭logcat和文件日志
            }
            return instance;
        }
    }

    public void log(LogLevel level, LogCategory category, String tag, String message, Throwable throwable) {
        try {
            if (TextUtils.isEmpty(message)) return;

            if (TextUtils.isEmpty(tag)) {
                tag = "COSLogger";
            }

            // 执行脱敏处理
            String processedMessage = desensitize(message);

            extras.put("qcloud_platform", "Android");
            if(!TextUtils.isEmpty(deviceID)) {
                extras.put("deviceID", deviceID);
            }
            if(!TextUtils.isEmpty(deviceModel)) {
                extras.put("deviceModel", deviceModel);
            }
            if(!TextUtils.isEmpty(appVersion)) {
                extras.put("appVersion", appVersion);
            }

            LogEntity entity = new LogEntity(level, category, tag, processedMessage, extras, throwable);
            for (BaseLogChannel channel : channels) {
                if (channel.isEnabled()) {
                    // 优先检查channel的minLevel，如果未设置则使用全局minLevel
                    LogLevel channelLevel = channel.getMinLevel();
                    LogLevel effectiveLevel = channelLevel != null ? channelLevel : minLevel;
                    if (level.isLoggable(effectiveLevel)) {
                        channel.log(entity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dProcess(String tag, String message) {
        getInstance().log(LogLevel.DEBUG, LogCategory.PROCESS, tag, message, null);
    }
    public static void dProcess(String tag, String format, Object... args) {
        getInstance().log(LogLevel.DEBUG, LogCategory.PROCESS, tag, formatMessage(format, args), null);
    }

    public static void dProcess(String tag, String message, Throwable throwable) {
        getInstance().log(LogLevel.DEBUG, LogCategory.PROCESS, tag, message, throwable);
    }

    public static void iProcess(String tag, String message) {
        getInstance().log(LogLevel.INFO, LogCategory.PROCESS, tag, message, null);
    }

    public static void iProcess(String tag, String format, Object... args) {
        getInstance().log(LogLevel.INFO, LogCategory.PROCESS, tag, formatMessage(format, args), null);
    }

    public static void iProcess(String tag, String message, Throwable throwable) {
        getInstance().log(LogLevel.INFO, LogCategory.PROCESS, tag, message, throwable);
    }

    public static void iNetwork(String tag, String message) {
        getInstance().log(LogLevel.INFO, LogCategory.NETWORK, tag, message, null);
    }
    public static void iNetwork(String tag, String format, Object... args) {
        getInstance().log(LogLevel.INFO, LogCategory.NETWORK, tag, formatMessage(format, args), null);
    }

    public static void iProbe(String tag, String message) {
        getInstance().log(LogLevel.INFO, LogCategory.PROBE, tag, message, null);
    }

    public static void wProcess(String tag, String message) {
        getInstance().log(LogLevel.WARN, LogCategory.PROCESS, tag, message, null);
    }
    public static void wProcess(String tag, String format, Object... args) {
        getInstance().log(LogLevel.WARN, LogCategory.PROCESS, tag, formatMessage(format, args), null);
    }
    public static void wNetwork(String tag, String message) {
        getInstance().log(LogLevel.WARN, LogCategory.NETWORK, tag, message, null);
    }
    public static void wNetwork(String tag, String format, Object... args) {
        getInstance().log(LogLevel.WARN, LogCategory.NETWORK, tag, formatMessage(format, args), null);
    }

//    public static void e(String tag, String message, String extras) {
//        getInstance().log(LogLevel.ERROR, LogCategory.ERROR, tag, message, extras, null);
//    }

    private static String formatMessage(String format, Object... args) {
        String message;
        try {
            message = args != null && args.length > 0 ? String.format(format, args) : format;
        } catch (Exception e) {
            message = format + ": !!!! Log format exception: ";
        }
        return message;
    }

    /**
     * 是否开启控制台日志
     *
     * @param enable 是否开启(默认开启)
     */
    public static void enableLogcat(boolean enable) {
        getInstance().logcatChannel.setEnabled(enable);
    }

    /**
     * 是否开启文件日志
     *
     * @param enable 是否开启(默认开启)
     */
    public static void enableLogFile(boolean enable) {
        getInstance().fileChannel.setEnabled(enable);
    }

    /**
     * 添加日志监听器
     * @param listener 日志监听器
     */
    public static void addLogListener(CosLogListener listener) {
        getInstance().channels.add(new ListenerChannel(listener));
    }

    /**
     * 删除日志监听器
     * @param listener 日志监听器
     */
    public static void removeLogListener(CosLogListener listener){
        for (BaseLogChannel channel : getInstance().channels) {
            if (channel instanceof ListenerChannel) {
                ListenerChannel listenerChannel = (ListenerChannel) channel;
                if (listenerChannel.getListener() == listener) {
                    getInstance().channels.remove(channel);
                    break;
                }
            }
        }
    }

    /**
     * 设置全局最小日志等级
     *
     * @param minLevel 最小日志等级
     */
    public static void setMinLevel(LogLevel minLevel) {
        getInstance().minLevel = minLevel;
    }

    /**
     * 设置控制台最小日志等级
     *
     * @param minLevel 最小日志等级
     */
    public static void setLogcatMinLevel(LogLevel minLevel) {
        getInstance().logcatChannel.setMinLevel(minLevel);
    }

    /**
     * 设置文件最小日志等级
     *
     * @param minLevel 最小日志等级
     */
    public static void setFileMinLevel(LogLevel minLevel) {
        getInstance().fileChannel.setMinLevel(minLevel);
    }

    /**
     * 设置cls最小日志等级
     *
     * @param minLevel 最小日志等级
     */
    public static void setClsMinLevel(LogLevel minLevel) {
        for (BaseLogChannel channel : getInstance().channels) {
            if (channel instanceof ClsChannel) {
                ClsChannel clsChannel = (ClsChannel) channel;
                clsChannel.setMinLevel(minLevel);
            }
        }
    }

    /**
     * 设置拓展字段
     * @param extras 拓展字段
     */
    public static void setExtras(Map<String, String> extras) {
        getInstance().extras = extras;
    }

    /**
     * 设置设备ID
     * @param deviceID 设备ID
     */
    public static void setDeviceID(String deviceID) {
        getInstance().deviceID = deviceID;
    }

    /**
     * 设置设备型号
     * @param deviceModel 设备型号
     */
    public static void setDeviceModel(String deviceModel) {
        getInstance().deviceModel = deviceModel;
    }

    /**
     * 设置APP版本号
     * @param appVersion app版本号
     */
    public static void setAppVersion(String appVersion) {
        getInstance().appVersion = appVersion;
    }

    /**
     * 设置文件日志加密密钥
     * @param key 加密密钥(16/24/32字节对应AES-128/192/256)
     * @param iv 初始化向量(16字节)
     */
    public static void setLogFileEncryptionKey(byte[] key, byte[] iv) {
        getInstance().fileChannel.setEncryptionKey(key, iv);
    }

    /**
     * 获取日志文件列表
     * @param limit 文件数量限制
     * @return 日志文件列表
     */
    public static File[] getLogFiles(int limit) {
        return getInstance().fileChannel.getLogFilesDesc(limit);
    }

    /**
     * 获取日志文件目录路径
     * @return 日志文件目录路径
     */
    public static String getLogRootDir() {
        return getInstance().fileChannel.getLogRootDir();
    }

    /**
     * 设置CLS渠道（匿名方式）
     * @param topicId 日志主题ID
     * @param endpoint 接入点
     */
    public static void setCLsChannel(String topicId, String endpoint){
        if (!ClsTrackService.isInclude()) {
            throw new IllegalStateException("Please quote the cls library first: com.tencentcloudapi.cls:tencentcloud-cls-sdk-android:x.x.x");
        }
        ClsTrackService clsTrackService = new ClsTrackService();
        clsTrackService.init(getInstance().context, topicId, endpoint);
        // 写死固定无效字符串，因为cls sdk不允许空密钥
        clsTrackService.setSecurityCredential("secretId", "secretKey");
        getInstance().channels.add(new ClsChannel(clsTrackService));
    }

    /**
     * 设置CLS渠道（固定密钥）
     * @param topicId 日志主题ID
     * @param endpoint 接入点
     * @param secretId 密钥 secretId
     * @param secretKey 密钥 secretKey
     */
    public static void setCLsChannel(String topicId, String endpoint,
                              String secretId, String secretKey){
        if (!ClsTrackService.isInclude()) {
            throw new IllegalStateException("Please quote the cls library first: com.tencentcloudapi.cls:tencentcloud-cls-sdk-android:x.x.x");
        }
        ClsTrackService clsTrackService = new ClsTrackService();
        clsTrackService.init(getInstance().context, topicId, endpoint);
        clsTrackService.setSecurityCredential(secretId, secretKey);
        getInstance().channels.add(new ClsChannel(clsTrackService));
    }

    /**
     * 设置CLS渠道（临时密钥）
     * @param topicId 日志主题ID
     * @param endpoint 接入点
     * @param lifecycleCredentialProvider CLS临时秘钥提供器
     */
    public static void setCLsChannel(String topicId, String endpoint,
                              ClsLifecycleCredentialProvider lifecycleCredentialProvider){
        if (!ClsTrackService.isInclude()) {
            throw new IllegalStateException("Please quote the cls library first: com.tencentcloudapi.cls:tencentcloud-cls-sdk-android:x.x.x");
        }
        ClsTrackService clsTrackService = new ClsTrackService();
        clsTrackService.init(getInstance().context, topicId, endpoint);
        clsTrackService.setCredentialProvider(lifecycleCredentialProvider);
        getInstance().channels.add(new ClsChannel(clsTrackService));
    }

    /**
     * 添加自定义脱敏规则
     * @param regex 正则表达式
     * @param replacement 替换字符串
     */
    public static void addSensitiveRule(String regex, String replacement) {
        if (TextUtils.isEmpty(regex) || TextUtils.isEmpty(replacement)) {
            return;
        }
        getInstance().sensitivePatterns.put(regex, replacement);
    }

    /**
     * 移除脱敏规则
     * @param regex 正则表达式
     */
    public static void removeSensitiveRule(String regex) {
        getInstance().sensitivePatterns.remove(regex);
    }

    /**
     * 执行脱敏处理
     * @param input 原始字符串
     * @return 脱敏后的字符串
     */
    private String desensitize(String input) {
        if (TextUtils.isEmpty(input)) {
            return input;
        }

        String result = input;
        for (String regex : sensitivePatterns.keySet()) {
            result = result.replaceAll(regex, sensitivePatterns.get(regex));
        }
        return result;
    }
}