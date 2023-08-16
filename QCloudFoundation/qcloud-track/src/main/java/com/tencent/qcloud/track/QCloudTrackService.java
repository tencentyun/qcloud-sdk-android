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

package com.tencent.qcloud.track;

import android.content.Context;
import android.util.Log;

import com.tencentcloudapi.cls.android.CLSAdapter;
import com.tencentcloudapi.cls.android.CLSConfig;
import com.tencentcloudapi.cls.android.producer.AsyncProducerClient;
import com.tencentcloudapi.cls.android.producer.AsyncProducerConfig;
import com.tencentcloudapi.cls.android.producer.common.LogItem;
import com.tencentcloudapi.cls.android.producer.errors.ProducerException;
import com.tencentcloudapi.cls.android.producer.util.NetworkUtils;
import com.tencentcloudapi.cls.plugin.network_diagnosis.CLSNetDiagnosisPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据追踪服务
 */
public class QCloudTrackService {
    private static final String TAG = "TrackService";

    private static final String NET_DIAGNOSIS_TOPIC_ID = "net_diagnosis";

    private static boolean debug = false;
    private static boolean isCloseTrack = false;
    private static AsyncProducerClient clsClient;

    /**
     * 主题前缀
     */
    private static String topicIdPrefix = "qcloud_track";

    /**
     * 初始化
     * @param context context
     * @param endpoint 接入点
     * @param secretId API密钥 secretId
     * @param secretKey API密钥 secretKey
     * @param secretToken API密钥 secretToken
     * @param debug 是否是调试模式 会输出日志
     * @param isCloseTrack 是否关闭数据上报
     */
    public static void init(Context context,
                            String topicIdPrefix,
                            String endpoint, String secretId, String secretKey, String secretToken,
                            boolean debug, boolean isCloseTrack) {
        QCloudTrackService.topicIdPrefix = topicIdPrefix;
        QCloudTrackService.debug = debug;
        QCloudTrackService.isCloseTrack = isCloseTrack;
        // NetworkUtils.getLocalMachineIP() 获取本地网卡ip，如果不指定，默认填充服务端接收到的网络出口ip
        AsyncProducerConfig config = new AsyncProducerConfig(context.getApplicationContext(), endpoint, secretId, secretKey, secretToken, NetworkUtils.getLocalMachineIP());
        // 构建一个客户端实例
        QCloudTrackService.clsClient = new AsyncProducerClient(config);

        // 添加网络探测插件
        CLSAdapter adapter = CLSAdapter.getInstance();
        adapter.addPlugin(new CLSNetDiagnosisPlugin());
        CLSConfig clsConfig = new CLSConfig(context.getApplicationContext());
        clsConfig.endpoint = endpoint;
        clsConfig.accessKeyId = secretId;
        clsConfig.accessKeySecret = secretKey;
        clsConfig.securityToken = secretToken;
        clsConfig.topicId = topicIdPrefix + "_" + NET_DIAGNOSIS_TOPIC_ID;
        // 发布时，建议关闭，即配置为config.debuggable = false。
        clsConfig.debuggable = debug;
        adapter.init(clsConfig);
    }

    public static void track(String eventCode, Map<String, String> params) {
        if (QCloudTrackService.isCloseTrack) return;

        if (clsClient == null) {
            throw new IllegalArgumentException("Please call the init method of TrackService first");
        }

        for (int i = 0; i < 10000; ++i) {
            List<LogItem> logItems = new ArrayList<>();
            LogItem logItem = new LogItem();
            for (String key : params.keySet()){
                logItem.PushBack(key, params.get(key));
            }
            logItems.add(logItem);
            try {
                clsClient.putLogs(QCloudTrackService.topicIdPrefix + "_" + eventCode, logItems,
                        result -> {
                            if (debug) {
                                StringBuilder mapAsString = new StringBuilder("{");
                                for (String key : params.keySet()) {
                                    mapAsString.append(key).append("=").append(params.get(key)).append(", ");
                                }
                                mapAsString.delete(mapAsString.length() - 2, mapAsString.length()).append("}");
                                Log.i(TAG, String.format("eventCode: %s, params: %s => result: %s", QCloudTrackService.topicIdPrefix + "_" + eventCode, mapAsString, result.toString()));
                            }
                        }
                );
            } catch (InterruptedException | ProducerException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: 2023/6/21 添加适合sdk用的网络探测方法

    /**
     * 设置是否关闭数据上报
     * @param isCloseTrack 是否关闭数据上报
     */
    public static void setIsCloseTrack(boolean isCloseTrack) {
        QCloudTrackService.isCloseTrack = isCloseTrack;
    }
}
