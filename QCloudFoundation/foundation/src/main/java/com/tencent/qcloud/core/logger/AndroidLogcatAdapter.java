package com.tencent.qcloud.core.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Android Logcat Printer.
 *
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public final class AndroidLogcatAdapter implements LogAdapter {

    @Override
    public boolean isLoggable(int priority, @Nullable String tag) {
        if (TextUtils.isEmpty(tag) || tag.length() >= 23) {
            return false;
        }

        // default level : INFO
        // use "adb shell setprop log.tag.[tag] DEBUG" to enable DEBUG level for specific tag
        return Log.isLoggable(tag, priority);
    }

    @Override
    public void log(int priority, @NonNull String tag, @NonNull String message, @Nullable Throwable tr) {
        switch (priority) {
            case QCloudLogger.VERBOSE:
                v(tag, message, tr);
                break;
            case QCloudLogger.DEBUG:
                d(tag, message, tr);
                break;
            case QCloudLogger.INFO:
                i(tag, message, tr);
                break;
            case QCloudLogger.WARN:
                w(tag, message, tr);
                break;
            case QCloudLogger.ERROR:
                e(tag, message, tr);
                break;
        }
    }

    private int v(String tag, String message, @Nullable Throwable tr) {
        if (tr == null) {
            return Log.v(tag, message);
        } else {
            return Log.v(tag, message, tr);
        }
    }

    private int d(String tag, String message, @Nullable Throwable tr) {
        if (tr == null) {
            return Log.d(tag, message);
        } else {
            return Log.d(tag, message, tr);
        }
    }

    private int i(String tag, String message, @Nullable Throwable tr) {
        if (tr == null) {
            return Log.i(tag, message);
        } else {
            return Log.i(tag, message, tr);
        }
    }

    private int w(String tag, String message, @Nullable Throwable tr) {
        if (tr == null) {
            return Log.w(tag, message);
        } else {
            return Log.w(tag, message, tr);
        }
    }

    private int e(String tag, String message, @Nullable Throwable tr) {
        if (tr == null) {
            return Log.e(tag, message);
        } else {
            return Log.e(tag, message, tr);
        }
    }
}
