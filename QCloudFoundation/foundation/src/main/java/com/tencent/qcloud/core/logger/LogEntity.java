package com.tencent.qcloud.core.logger;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;

/**
 * 日志实体类，用于封装日志的各个属性信息
 * <p>
 * Created by jordanqin on 2025/3/21 21:07.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class LogEntity {
    // 日志时间戳（毫秒）
    private final long timestamp;
    // 日志级别
    private final LogLevel level;
    // 日志分类
    private final LogCategory category;
    // 日志标签
    private final String tag;
    // 日志消息内容
    private final String message;
    // 记录日志的线程名称
    private final String threadName;
    // 额外信息（可选）
    private final @Nullable Map<String, String> extras;
    // 异常信息（可选）
    private final @Nullable Throwable throwable;

    /**
     * 构造方法
     * @param level 日志级别
     * @param category 日志分类
     * @param tag 日志标签
     * @param message 日志消息内容
     * @param extras 额外信息（可为null）
     */
    public LogEntity(LogLevel level, LogCategory category, String tag, String message, @Nullable Map<String, String> extras, @Nullable Throwable throwable) {
        this.timestamp = System.currentTimeMillis();
        this.threadName = Thread.currentThread().getName();
        this.level = level;
        this.category = category;
        this.tag = tag;
        this.message = message;
        this.extras = extras;
        this.throwable = throwable;
    }

    /**
     * 获取日志时间戳
     * @return 时间戳（毫秒）
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 获取日志级别
     * @return 日志级别枚举值
     */
    public LogLevel getLevel() {
        return level;
    }

    /**
     * 获取日志分类
     * @return 日志分类枚举值
     */
    public LogCategory getCategory() {
        return category;
    }

    /**
     * 获取日志标签
     * @return 日志标签字符串
     */
    public String getTag() {
        return tag;
    }

    /**
     * 获取日志消息内容
     * @return 日志消息字符串
     */
    public String getMessage() {
        return message;
    }

    /**
     * 获取记录日志的线程名称
     * @return 线程名称字符串
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * 获取日志额外信息
     * @return 额外信息字符串（可能为null）
     */
    @Nullable
    public Map<String, String> getExtras() {
        return extras;
    }

    /**
     * 获取日志异常信息
     * @return 异常信息对象（可能为null）
     */
    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }

    @NonNull
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(level).append("/");
        builder.append(timeUtils(timestamp,"yyyy-MM-dd HH:mm:ss"));
        builder.append("[").append(threadName).append("]");
        builder.append("[").append(category).append(" ").append(tag).append("]");
        builder.append("[").append(message).append("]");
        builder.append("[").append(extras).append("]");
        if(throwable != null){
            builder.append(" * Exception :\n").append(Log.getStackTraceString(throwable));
        }
        builder.append("\n");
        return  builder.toString();
    }

    public String getLogcatString(){
        String message;
        if (getExtras() == null || getExtras().isEmpty()) {
            message = String.format("[%s][%s] %s",
                    getCategory().name(),
                    getThreadName(),
                    getMessage());
        } else {
            message = String.format("[%s][%s] %s - %s",
                    getCategory().name(),
                    getThreadName(),
                    getMessage(),
                    getExtras());
        }
        return message;
    }

    public long getLength(){
        return (message != null ? message.length() : 0) + 40;
    }

    private static String timeUtils(long timestamp, String dateFormat){
        Date dat=new Date(timestamp);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFormat, Locale.CHINA);
        return format.format(gc.getTime());
    }

}