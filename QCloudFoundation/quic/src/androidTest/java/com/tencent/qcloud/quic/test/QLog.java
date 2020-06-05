package com.tencent.qcloud.quic.test;

import android.util.Log;

public class QLog {
    public final static String TAG = "COSQUIC";
    public static boolean isDebug = true;

    public static void d(String format, Object... objects){
        if(isDebug){
            Log.d(TAG, String.format(format, objects));
        }
    }
}
