package com.tencent.qcloud.track.service;

import android.content.Context;
import android.util.Log;

import com.tencent.qcloud.track.cls.ClsAuthenticationException;
import com.tencent.qcloud.track.cls.ClsLifecycleCredentialProvider;
import com.tencent.qcloud.track.cls.ClsSessionCredentials;
import com.tencentcloudapi.cls.android.producer.AsyncProducerClient;
import com.tencentcloudapi.cls.android.producer.AsyncProducerConfig;
import com.tencentcloudapi.cls.android.producer.common.LogItem;
import com.tencentcloudapi.cls.android.producer.errors.ProducerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CLS数据上报
 * <p>
 * Created by jordanqin on 2023/9/4 19:36.
 * Copyright 2010-2023 Tencent Cloud. All Rights Reserved.
 */
public class ClsTrackService extends ATrackService {
    private static final String TAG = "ClsTrackService";

    private AsyncProducerClient clsClient;
    // 主题id
    private String topicId;
    // 临时秘钥提供器
    private ClsLifecycleCredentialProvider clsLifecycleCredentialProvider;
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    /**
     * 初始化
     * @param context context
     * @param topicId 主题Id
     * @param endpoint 接入点
     */
    public void init(Context context, String topicId, String endpoint) {
        isInit = true;
        this.topicId = topicId;
        //获取本地网卡ip，如果不指定，默认填充服务端接收到的网络出口ip
        // NetworkUtils.getLocalMachineIP();
        // cls默认字段source ip为网络出口IP，自定义公共字段client_local_ip为网卡ip
        // 先用secretId和secretKey字符串占位（cls sdk设计缺陷）
        AsyncProducerConfig config = new AsyncProducerConfig(context.getApplicationContext(), endpoint, "secretId", "secretKey", "", null);
        // 构建一个客户端实例
        this.clsClient = new AsyncProducerClient(config);
    }

    /**
     * 设置固定API密钥
     * @param secretId API密钥 secretId
     * @param secretKey API密钥 secretKey
     */
    public void setSecurityCredential(String secretId, String secretKey){
        this.clsClient.getProducerConfig().resetSecurityToken(secretId, secretKey, "");
    }

    /**
     * 设置CLS临时秘钥提供器
     * @param lifecycleCredentialProvider CLS临时秘钥提供器
     */
    public void setCredentialProvider(ClsLifecycleCredentialProvider lifecycleCredentialProvider){
        this.clsLifecycleCredentialProvider = lifecycleCredentialProvider;
    }

    @Override
    public void report(String eventCode, Map<String, String> params) {
        if (isCloseReport() || !isInit || !isInclude()) return;

        if(this.clsLifecycleCredentialProvider == null){
            // 固定秘钥
            putLogs(eventCode, params);
        } else {
           // 临时秘钥
            singleThreadExecutor.submit(() -> {
                ClsSessionCredentials credentials = null;
                try {
                    credentials = clsLifecycleCredentialProvider.getCredentials();
                } catch (ClsAuthenticationException e) {
                    e.printStackTrace();
                }
                if (credentials != null) {
                    clsClient.getProducerConfig().resetSecurityToken(credentials.getSecretId(), credentials.getSecretKey(), credentials.getToken());
                    putLogs(eventCode, params);
                }
            });
        }
    }

    private void putLogs(String eventCode, Map<String, String> params){
        List<LogItem> logItems = new ArrayList<>();
        LogItem logItem = new LogItem();
        for (String key : params.keySet()){
            logItem.PushBack(key, params.get(key));
        }
        logItems.add(logItem);
        try {
            clsClient.putLogs(this.topicId, logItems,
                    result -> {
                        if (isDebug()) {
                            StringBuilder mapAsString = new StringBuilder("{");
                            for (String key : params.keySet()) {
                                mapAsString.append(key).append("=").append(params.get(key)).append(", ");
                            }
                            mapAsString.delete(mapAsString.length() - 2, mapAsString.length()).append("}");
                            Log.i(TAG, String.format("eventCode: %s, topicId: %s, params: %s => result: %s", eventCode, this.topicId, mapAsString, result.toString()));
                        }
                    }
            );
        } catch (InterruptedException | ProducerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取是否包含CLS模块
     * @return 是否包含CLS模块
     */
    public static boolean isInclude() {
        try {
            Class.forName("com.tencentcloudapi.cls.android.producer.AsyncProducerClient");
            return true;
        } catch (ClassNotFoundException e){
            return false;
        }
    }
}
