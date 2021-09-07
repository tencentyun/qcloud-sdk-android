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
import com.tencent.qcloud.core.util.QCloudStringUtils;

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
                            .setNormalPollingTime(30000);
                    try {
                        builder.collectMACEnable(false)
                                .collectIMEIEnable(false)
                                .collectAndroidIdEnable(false)
                                .collectProcessInfoEnable(false);
                    } catch (NoSuchMethodError error) {
                        //APP使用了标准版的灯塔SDK builder不支持collectXXX
                    }
                    BeaconConfig config = builder.build();

                    BeaconReport beaconReport = BeaconReport.getInstance();
                    beaconReport.setLogAble(debug);//是否打开日志
                    try {
                        beaconReport.setCollectMac(false); //该项设为false即关闭采集Mac功能
                        beaconReport.setCollectAndroidID(false); //该项设为false即关闭采集AndroidId功能
                        beaconReport.setCollectImei(false); //该项设为false即关闭采集IMEI和IMSI的功能
                        beaconReport.setCollectProcessInfo(false); //该项设为false即关闭采集processInfo功能
                    } catch (NoSuchMethodError error) {
                    }
                    beaconReport.start(context, beaconKey, config);
                    //某些灯塔sdk版本，start之前setCollectMac等无效
                    try {
                        beaconReport.setCollectMac(false); //该项设为false即关闭采集Mac功能
                        beaconReport.setCollectAndroidID(false); //该项设为false即关闭采集AndroidId功能
                        beaconReport.setCollectImei(false); //该项设为false即关闭采集IMEI和IMSI的功能
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
        try {
            builder.withIsSimpleParams(true);
        } catch (NoSuchMethodError error) {
            //APP使用了标准版的灯塔SDK 不支持withIsSimpleParams
        }
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


    private static boolean isIncludeBeacon(){
        try {
            Class.forName("com.tencent.beacon.event.open.BeaconReport");
            return true;
        } catch (ClassNotFoundException e){
            return false;
        }
    }
}
