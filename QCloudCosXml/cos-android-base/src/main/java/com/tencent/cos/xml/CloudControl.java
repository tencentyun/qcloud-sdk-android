package com.tencent.cos.xml;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 云控相关
 * <p>
 * Created by jordanqin on 2023/9/6 17:20.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class CloudControl {
    private static final String TAG = "CloudControl";
    public final ThreadPoolExecutor CLOUD_CONTROL_EXECUTOR;

    private final Handler handler;

    private static volatile CloudControl instance;

    // 灯塔云控配置基础url
    private final String BEACON_CLOUD_URL = "https://cos-android-sdk-doc-1253960454.cos.ap-shanghai.myqcloud.com/beacon_cc/android/";
    private final String BEACON_CLOUD_APPKEY = "appKey";

    private CloudControl() {
        CLOUD_CONTROL_EXECUTOR = new ThreadPoolExecutor(1, 1, 5L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(Integer.MAX_VALUE));
        CLOUD_CONTROL_EXECUTOR.allowCoreThreadTimeOut(true);
        handler = new Handler(Looper.getMainLooper());
    }

    public static CloudControl getInstance() {
        if (instance == null) {
            synchronized (CloudControl.class) {
                if (instance == null) {
                    instance = new CloudControl();
                }
            }
        }
        return instance;
    }

    /**
     * 获取灯塔云控数据
     *
     * @param packageName 包名
     * @param callback    回调
     */
    public void getBeaconAppKey(String packageName, BeaconCloudCallback callback) {
        CLOUD_CONTROL_EXECUTOR.execute(() -> {
            String beaconKey;
            try {
                URL url = new URL(BEACON_CLOUD_URL + packageName);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    beaconKey = new JSONObject(sb.toString()).getString(BEACON_CLOUD_APPKEY);
                } else {
                    handler.post(() -> callback.onError(new IllegalStateException("getBeaconAppKey failure:" + responseCode)));
                    return;
                }
            } catch (Exception e) {
                handler.post(() -> callback.onError(e));
                return;
            }
            String finalBeaconKey = beaconKey;
            Log.d(TAG, "getBeaconAppKey onSuccess:" + beaconKey);
            handler.post(() -> callback.onSuccess(finalBeaconKey));
        });
    }

    interface BeaconCloudCallback {
        void onSuccess(String beaconKey);

        void onError(Exception e);
    }
}
