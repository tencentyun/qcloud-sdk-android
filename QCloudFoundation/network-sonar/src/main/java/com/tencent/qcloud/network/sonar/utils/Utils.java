package com.tencent.qcloud.network.sonar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

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
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network nw = connectivityManager.getActiveNetwork();
                if (nw == null) return false;
                NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
                return actNw != null && (
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
                );
            } else {
                NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
                return nwInfo != null && nwInfo.isConnected();
            }
        } catch (Exception e) {
            //ignore
        }
        return false;
    }
}
