package com.tencent.qcloud.track;

import android.content.Context;
import android.os.Build;

import com.tencent.qcloud.track.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 隐私合规服务类
 * <p>
 * Created by jordanqin on 2024/1/30 11:14.
 * Copyright 2010-2024 Tencent Cloud. All Rights Reserved.
 */
public class PrivacyService {
    /**
     * 获取可能采集的参数
     * 这些参数用于分析系统运行状态，优化系统性能和稳定性，不会用于其他用途
     * os_version： 系统版本
     * client_local_ip： 本地IP地址
     * network_type： 网络状态
     */
    public static Map<String, String> getPrivacyParams(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("os_version", Build.VERSION.RELEASE);
        params.put("client_local_ip", NetworkUtils.getLocalMachineIP());
        params.put("network_type", NetworkUtils.getNetworkType(context));
        return params;
    }

    /**
     * 关闭采集数据上报
     * 这些参数用于分析系统运行状态，优化系统性能和稳定性，不会用于其他用途
     */
    public static void closeReport(){
        QCloudTrackService.getInstance().setIsCloseReport(true);
    }
}
