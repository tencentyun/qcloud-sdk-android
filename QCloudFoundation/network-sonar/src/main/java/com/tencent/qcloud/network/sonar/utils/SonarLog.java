package com.tencent.qcloud.network.sonar.utils;

import android.util.Log;

public final class SonarLog {
    public static boolean openLog = false;
    private static final String TAG = "NETWORK-SONAR";

    public static void v(String module, Object msg) {
        if (!openLog) {
            return;
        }
        Log.v(TAG, format(module, msg));
    }

    public static void d(String module, Object msg) {
        if (!openLog) {
            return;
        }
        Log.d(TAG, format(module, msg));
    }

    public static void w(String module, Object msg) {
        if (!openLog) {
            return;
        }
        Log.w(TAG, format(module, msg));
    }

    public static void e(String module, Object msg) {
        if (!openLog) {
            return;
        }
        Log.e(TAG, format(module, msg));
    }

    private static String format(String module, Object msg) {
        return String.format("module: %s, %s", module, toString(msg));
    }

    public static String format(String format, Object... args) {
        return String.format(format, args);
    }

    public static String toString(Object obj) {
        if (null == obj) {
            return "null";
        }

        if (obj instanceof String) {
            return (String)obj;
        }

        if (obj instanceof Number) {
            return obj.toString();
        }

        if (obj instanceof Boolean) {
            return String.valueOf(obj);
        }

        return obj.toString();
    }

}
