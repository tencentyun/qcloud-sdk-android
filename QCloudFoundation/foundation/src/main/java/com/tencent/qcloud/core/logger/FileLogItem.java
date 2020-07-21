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

package com.tencent.qcloud.core.logger;

import android.util.Log;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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