package com.tencent.qcloud.track.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * <p>
 * Created by jordanqin on 2023/9/14 17:14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class NetworkUtils {
    public static boolean isIPAddr(String ipAddress) {
        if (ipAddress != null && !ipAddress.isEmpty()) {
            try {
                String[] tokens = ipAddress.split("\\.");
                if (tokens.length != 4) {
                    return false;
                } else {
                    String[] var2 = tokens;
                    int var3 = tokens.length;

                    for(int var4 = 0; var4 < var3; ++var4) {
                        String token = var2[var4];
                        int i = Integer.parseInt(token);
                        if (i < 0 || i > 255) {
                            return false;
                        }
                    }

                    return true;
                }
            } catch (Exception var7) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取本机网卡ip
     */
    public static String getLocalMachineIP() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if(networkInterfaces == null){
                return null;
            }
            while(true) {
                NetworkInterface ni;
                do {
                    if (!networkInterfaces.hasMoreElements()) {
                        return null;
                    }

                    ni = (NetworkInterface)networkInterfaces.nextElement();
                } while(!ni.isUp());

                Enumeration<InetAddress> addresses = ni.getInetAddresses();

                while(addresses.hasMoreElements()) {
                    InetAddress address = (InetAddress)addresses.nextElement();
                    if (!address.isLinkLocalAddress() && address.getHostAddress() != null) {
                        String ipAddress = address.getHostAddress();
                        if (!ipAddress.equals("127.0.0.1") && isIPAddr(ipAddress)) {
                            return ipAddress;
                        }
                    }
                }
            }
        } catch (SocketException var5) {
            return null;
        }
    }

    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "-"; // not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:     // api< 8: replace by 11
                case TelephonyManager.NETWORK_TYPE_GSM:      // api<25: replace by 16
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:   // api< 9: replace by 12
                case TelephonyManager.NETWORK_TYPE_EHRPD:    // api<11: replace by 14
                case TelephonyManager.NETWORK_TYPE_HSPAP:    // api<13: replace by 15
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA: // api<25: replace by 17
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:      // api<11: replace by 13
                case TelephonyManager.NETWORK_TYPE_IWLAN:    // api<25: replace by 18
                case 19: // LTE_CA
                    return "4G";
                case TelephonyManager.NETWORK_TYPE_NR:       // api<29: replace by 20
                    return "5G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    /**
     * 判断设备 是否使用代理上网
     */
    public static boolean isProxy() {
        String proxyAddress;
        proxyAddress = System.getProperty("http.proxyHost");
        String portStr = System.getProperty("http.proxyPort");

        int proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

}
