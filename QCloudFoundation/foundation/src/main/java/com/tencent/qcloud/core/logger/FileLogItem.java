package com.tencent.qcloud.core.logger;

import android.util.Log;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

class FileLogItem {
    private String tag =null;// log 的TAG
    private String msg = null;//log 的msg
    private Throwable throwable = null;
    private int priority = 0; //log 的level
    private long timestamp;
    private long threadId;
    private String threadName = null;

    public FileLogItem(String tag, int priority, String msg, Throwable t) {
        this.priority = priority;
        this.tag = tag;
        this.msg = msg;
        this.throwable = t;

        this.timestamp = System.currentTimeMillis();
        this.threadId = Thread.currentThread().getId();
        this.threadName = Thread.currentThread().getName();
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(getPriorityString(priority)).append("/");
        builder.append(timeUtils(timestamp,"yyyy-MM-dd HH:mm:ss"));
        builder.append("[").append(threadName).append(" ").append(threadId).append("]");
        builder.append("[").append(tag).append("]");
        builder.append("[").append(msg).append("]");
        if(throwable != null){
            builder.append(" * Exception :\n").append(Log.getStackTraceString(throwable));
        }
        builder.append("\n");
        return  builder.toString();
    }

    private static String getPriorityString(int priority) {
        switch (priority) {
            case QCloudLogger.VERBOSE:
                return "VERBOSE";
            case QCloudLogger.DEBUG:
                return "DEBUG";
            case QCloudLogger.INFO:
                return "INFO";
            case QCloudLogger.WARN:
                return "WARN";
            case QCloudLogger.ERROR:
                return "ERROR";
            default:
                return "UNKNOWN";
        }
    }

    private static String timeUtils(long seconds, String dateFormat){
        Date dat=new Date(seconds);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFormat, Locale.CHINA);
        return format.format(gc.getTime());
    }

    public long getLength(){
        return (msg != null ? msg.length() : 0) + 40;
    }

}