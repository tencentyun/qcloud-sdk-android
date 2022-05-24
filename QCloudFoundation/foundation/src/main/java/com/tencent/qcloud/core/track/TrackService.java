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

package com.tencent.qcloud.core.track;

import android.content.Context;

import com.tencent.beacon.event.open.BeaconConfig;
import com.tencent.beacon.event.open.BeaconEvent;
import com.tencent.beacon.event.open.BeaconReport;
import com.tencent.beacon.event.open.EventResult;
import com.tencent.beacon.event.open.EventType;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qimei.sdk.IQimeiSDK;
import com.tencent.qimei.sdk.QimeiSDK;

import java.util.Map;

/**
 * 灯塔服务
 */
public class TrackService {

    private static final String TAG = "TrackService";
    private static String beaconKey;
    private static boolean debug = false;
    private Context context;
    private static TrackService instance;

    private TrackService(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 初始化
     */
    public static void init(Context context, String beaconKey, boolean debug) {
        synchronized (TrackService.class) {
            if (instance == null) {
                instance = new TrackService(context);
                TrackService.beaconKey = beaconKey;
                TrackService.debug = debug;
                if(isIncludeBeacon()) {
                    BeaconConfig.Builder builder = BeaconConfig.builder()
                            .auditEnable(false)
                            .bidEnable(false)
                            .qmspEnable(false)
                            .pagePathEnable(false)
//                            .setNeedInitQimei(false)
                            .setNormalPollingTime(30000);
                    BeaconConfig config = builder.build();

                    BeaconReport beaconReport = BeaconReport.getInstance();
                    beaconReport.setLogAble(debug);//是否打开日志
                    try {
                        beaconReport.setCollectProcessInfo(false); //该项设为false即关闭采集processInfo功能
                    } catch (NoSuchMethodError error) {
                    }
                    // 多次初始化，或者初始化时传入的appkey和manifest.xml文件内不同，debug模式
                    // 下会主动抛出异常
                    try {
                        IQimeiSDK qimeiSDK = QimeiSDK.getInstance(beaconKey);
                        qimeiSDK.getStrategy()
                                .enableOAID(false)           // 关闭oaid采集，这里设置false
                                .enableIMEI(false)           // 关闭imei采集，这里设置false，建议如用户授权，尽可能采集，便于复核问题
                                .enableIMSI(false)           // 关闭imsi采集，这里设置false
                                .enableAndroidId(false)          // 关闭android id采集，这里设置false，建议如用户授权，尽可能采集，便于复核问题
                                .enableMAC(false)            // 关闭mac采集，这里设置false
                                .enableCid(false)           // 关闭cid采集，这里设置false
                                .enableProcessInfo(false)        // 关闭应用列表枚举，这里设置false，1.0.5以上版本有效
                                .enableBuildModel(false);     // 关闭BUILD.MODEL采集，这里设置false，1.2.3以上版本有效

                        beaconReport.start(context, beaconKey, config);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //某些灯塔sdk版本，start之前setCollectMac等无效
                    try {
                        beaconReport.setCollectProcessInfo(false); //该项设为false即关闭采集processInfo功能
                    } catch (NoSuchMethodError error) {
                    }
                }
            }
        }
    }

    public static TrackService getInstance() {
        return instance;
    }


    public void track(String beaconKey, String eventCode, Map<String, String> params) {
        if(!isIncludeBeacon()) return;
        String trackBeaconKey = TrackService.beaconKey;
        if (beaconKey != null) {
            trackBeaconKey = beaconKey;
        }
        BeaconEvent.Builder builder = BeaconEvent.builder()
                .withAppKey(trackBeaconKey)
                .withCode(eventCode)
                .withType(EventType.NORMAL)
                .withParams(params);
        EventResult result = BeaconReport.getInstance().report(builder.build());
        if (debug) {
            StringBuilder mapAsString = new StringBuilder("{");
            for (String key : params.keySet()) {
                mapAsString.append(key + "=" + params.get(key) + ", ");
            }
            mapAsString.delete(mapAsString.length() - 2, mapAsString.length()).append("}");
            QCloudLogger.i(TAG, "eventCode: %s, params: %s => result{ eventID: %s, errorCode: %d, errorMsg: %s}",
                    eventCode, mapAsString, result.eventID, result.errorCode, result.errMsg);
        }
    }


    public static boolean isIncludeBeacon(){
        try {
            Class.forName("com.tencent.beacon.event.open.BeaconReport");
            Class.forName("com.tencent.qimei.sdk.QimeiSDK");
            return true;
        } catch (ClassNotFoundException e){
            return false;
        }
    }
}
