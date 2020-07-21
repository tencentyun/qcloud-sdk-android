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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

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
