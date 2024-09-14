package com.tencent.qcloud.network.sonar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.Closeable;
import java.io.IOException;

/**
 * <p>
 * Created by jordanqin on 2024/8/21 16:30.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class Utils {
    public static void closeAllCloseable(Closeable... closeables) {
        if (closeables == null || closeables.length <= 0)
            return;

        for (Closeable closeable : closeables) {
            if (closeable != null)
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager mgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mgr == null) {
                return false;
            }
            @SuppressLint("MissingPermission") @SuppressWarnings("deprecation") NetworkInfo[] info = mgr.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo != null && anInfo.isConnected()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return false;
    }
}
