package com.tencent.qcloud.core.logger.channel;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tencent.qcloud.core.logger.LogEntity;

/**
 * <p>
 * Created by jordanqin on 2025/3/21 21:10.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class LogcatChannel extends BaseLogChannel {
    @Override
    public void log(LogEntity entity) {
        if(!isLoggable(entity)) {
            return;
        }

        String message = entity.getLogcatString();
        switch (entity.getLevel()) {
            case VERBOSE:
                v(entity.getTag(), message, entity.getThrowable());
                break;
            case DEBUG:
                d(entity.getTag(), message, entity.getThrowable());
                break;
            case INFO:
                i(entity.getTag(), message, entity.getThrowable());
                break;
            case WARN:
                w(entity.getTag(), message, entity.getThrowable());
                break;
            case ERROR:
                e(entity.getTag(), message, entity.getThrowable());
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

    private boolean isLoggable(LogEntity entity) {
        if (!isEnabled() || entity == null) return false;

        if (!entity.getLevel().isLoggable(getMinLevel())) return false;

        if (TextUtils.isEmpty(entity.getTag()) || entity.getTag().length() >= 23) {
            return false;
        }

        // default level : INFO
        // use "adb shell setprop log.tag.[tag] DEBUG" to enable DEBUG level for specific tag
        // +1是为了转换为Android的log level
        return Log.isLoggable(entity.getTag(), entity.getLevel().getPriority()+1);
    }
}
