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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.tencent.beacon.event.open.BeaconConfig;
import com.tencent.beacon.event.open.BeaconReport;
import com.tencent.qcloud.track.service.ATrackService;
import com.tencent.qcloud.track.service.BeaconTrackService;
import com.tencent.qcloud.track.service.ClsTrackService;
import com.tencent.qcloud.track.utils.NetworkUtils;
import com.tencent.qimei.sdk.IQimeiSDK;
import com.tencent.qimei.sdk.QimeiSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据追踪服务
 */
public class QCloudTrackService {
    private static final String TAG = "TrackService";
    private static final String EVENT_KEY_CLS_REPORT = "cls_report";

    private Context context;
    // 详细数据上报渠道
    private final Map<String, List<ATrackService>> trackServiceMap;
    // 简单数据上报渠道
    private BeaconTrackService simpleDataTrackService;

    private static volatile QCloudTrackService instance;
    private boolean isInitialized = false;

    private Map<String, String> businessParams;
    private Map<String, String> commonParams;

    private QCloudTrackService() {
        trackServiceMap = new HashMap<>();
    }

    public static QCloudTrackService getInstance() {
        if (instance == null) {
            synchronized (QCloudTrackService.class) {
                if (instance == null) {
                    instance = new QCloudTrackService();
                }
            }
        }
        return instance;
    }

    /**
     * 添加数据上报器
     * @param trackService 数据上报器
     */
    public synchronized void addTrackService(String eventCode, ATrackService trackService) {
        if (trackServiceMap.get(eventCode) == null) {
            trackServiceMap.put(eventCode, new ArrayList<>());
        }
        trackServiceMap.get(eventCode).add(trackService);
    }

    public synchronized void init(Context context) {
        try {
            if (!isInitialized) {
                this.context = context.getApplicationContext();
                commonParams = getCommonParams();
                // 因为默认上报简单数据到灯塔，因此默认初始化灯塔和QIMEI
                if (BeaconTrackService.isInclude()) {
                    initBeaconAndQimei();
                    simpleDataTrackService = new BeaconTrackService();
                    // 设置context
                    simpleDataTrackService.setContext(context);
                    simpleDataTrackService.init(Constants.SIMPLE_DATA_BEACON_APP_KEY);
                } else {
                    Log.i(TAG, "The beacon library is not referenced, cancel the beacon initialization");
                }

                isInitialized = true;
            } else {
                Log.d(TAG, "init has been called and the initialization code will not be executed again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上报数据
     *
     * @param eventCode 事件码
     * @param params    参数
     */
    public void report(String eventCode, Map<String, String> params) {
        try {
            // 添加公共参数
            if (commonParams != null && commonParams.size() > 0) {
                params.putAll(commonParams);
            }
            // 添加业务参数
            if (businessParams != null && businessParams.size() > 0) {
                params.putAll(businessParams);
            }
            // 将value为null的值替换为字符串null
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() == null) {
                    params.put(entry.getKey(), "null");
                }
            }
            List<ATrackService> list = this.trackServiceMap.get(eventCode);
            if(list != null) {
                for (ATrackService trackService : list) {
                    trackService.report(eventCode, params);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上报简单数据
     *
     * @param eventCode 事件码
     * @param params    参数
     */
    public void reportSimpleData(String eventCode, Map<String, String> params) {
        if(simpleDataTrackService == null) return;
        try {
            // 添加公共参数
            if (commonParams != null && commonParams.size() > 0) {
                params.putAll(commonParams);
            }
            // 添加业务参数
            if (businessParams != null && businessParams.size() > 0) {
                params.putAll(businessParams);
            }
            // 添加是否包含cls上报
            params.put(EVENT_KEY_CLS_REPORT, String.valueOf(ClsTrackService.isInclude()));
            // 将value为null的值替换为字符串null
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() == null) {
                    params.put(entry.getKey(), "null");
                }
            }
            simpleDataTrackService.report(eventCode, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置业务参数，便于在数据分析时个性化使用<br/>
     * 例如 用户id等
     *
     * @param businessParamsArg 业务参数，不能超过10个
     */
    public void setBusinessParams(Map<String, String> businessParamsArg) {
        if (businessParamsArg.size() > 10) {
            throw new IllegalArgumentException("The number of businessParams cannot be greater than 10");
        }
        // 给businessParams key拼接business_前缀，防止与SDK内部参数冲突
        businessParams = new HashMap<>();
        for (Map.Entry<String, String> entry : businessParamsArg.entrySet()) {
            businessParams.put("business_" + entry.getKey(), entry.getValue());
        }
    }

    /**
     * 设置是否关闭数据上报
     *
     * @param isCloseReport 是否关闭数据上报
     */
    public void setIsCloseReport(boolean isCloseReport) {
        if(simpleDataTrackService != null) {
            simpleDataTrackService.setIsCloseReport(isCloseReport);
        }
        for (String eventCode : trackServiceMap.keySet()) {
            List<ATrackService> list = this.trackServiceMap.get(eventCode);
            if (list != null) {
                for (ATrackService trackService : list) {
                    trackService.setIsCloseReport(isCloseReport);
                }
            }
        }
    }

    /**
     * 设置是否是debug模式
     *
     * @param debug 是否是debug模式
     */
    public void setDebug(boolean debug) {
        if(simpleDataTrackService != null) {
            simpleDataTrackService.setDebug(debug);
        }
        for (String eventCode : trackServiceMap.keySet()) {
            List<ATrackService> list = this.trackServiceMap.get(eventCode);
            if (list != null) {
                for (ATrackService trackService : list) {
                    trackService.setDebug(debug);
                }
            }
        }
    }

    /**
     * 初始化灯塔和qimei
     */
    private void initBeaconAndQimei() {
        BeaconConfig.Builder builder = BeaconConfig.builder()
                .auditEnable(false)
                .bidEnable(false)
                .qmspEnable(false)
                .pagePathEnable(false)
//                            .setNeedInitQimei(false)
                .setNormalPollingTime(30000);
        BeaconConfig config = builder.build();

        BeaconReport beaconReport = BeaconReport.getInstance();
        try {
            beaconReport.setCollectProcessInfo(false); //该项设为false即关闭采集processInfo功能
        } catch (NoSuchMethodError error) {
        }
        // 多次初始化，或者初始化时传入的appkey和manifest.xml文件内不同，debug模式
        // 下会主动抛出异常
        try {
            IQimeiSDK qimeiSDK = QimeiSDK.getInstance(Constants.SIMPLE_DATA_BEACON_APP_KEY);
            qimeiSDK.getStrategy()
                    .enableOAID(false)           // 关闭oaid采集，这里设置false
                    .enableIMEI(false)           // 关闭imei采集，这里设置false，建议如用户授权，尽可能采集，便于复核问题
                    .enableIMSI(false)           // 关闭imsi采集，这里设置false
                    .enableAndroidId(false)          // 关闭android id采集，这里设置false，建议如用户授权，尽可能采集，便于复核问题
                    .enableMAC(false)            // 关闭mac采集，这里设置false
                    .enableCid(false)           // 关闭cid采集，这里设置false
                    .enableProcessInfo(false)        // 关闭应用列表枚举，这里设置false，1.0.5以上版本有效
                    .enableBuildModel(false);     // 关闭BUILD.MODEL采集，这里设置false，1.2.3以上版本有效

            beaconReport.start(context, Constants.SIMPLE_DATA_BEACON_APP_KEY, config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //某些灯塔sdk版本，start之前setCollectMac等无效
        try {
            beaconReport.setCollectProcessInfo(false); //该项设为false即关闭采集processInfo功能
        } catch (NoSuchMethodError error) {
        }
    }

    /**
     * 获取公共参数
     */
    private Map<String, String> getCommonParams() {
        Map<String, String> params = new HashMap<>();
        String hostPackageName = context.getPackageName();
        params.put("boundle_id", hostPackageName);
        try {
            // 获取宿主应用的版本信息
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(hostPackageName, 0);
            String hostVersionName = packageInfo.versionName;
            long hostVersionCode;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                hostVersionCode = packageInfo.getLongVersionCode();
            } else {
                hostVersionCode = packageInfo.versionCode;
            }
            params.put("app_version_code", String.valueOf(hostVersionCode));
            params.put("app_version_name", hostVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        params.put("os_name", "Android");
        params.put("os_version", Build.VERSION.RELEASE);

        params.put("client_local_ip", NetworkUtils.getLocalMachineIP());
        params.put("client_proxy", String.valueOf(NetworkUtils.isProxy()));
        params.put("network_type", NetworkUtils.getNetworkType(context));

        return params;
    }
}
