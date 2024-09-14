package com.tencent.qcloud.network.sonar;

import android.content.Context;

import com.tencent.qcloud.network.sonar.dns.AndroidDnsServerLookup;
import com.tencent.qcloud.network.sonar.dns.DnsResult;
import com.tencent.qcloud.network.sonar.dns.DnsSonar;
import com.tencent.qcloud.network.sonar.info.InfoResult;
import com.tencent.qcloud.network.sonar.info.InfoSonar;
import com.tencent.qcloud.network.sonar.ping.PingResult;
import com.tencent.qcloud.network.sonar.ping.PingSonar;
import com.tencent.qcloud.network.sonar.traceroute.TracerouteResult;
import com.tencent.qcloud.network.sonar.traceroute.TracerouteSonar;
import com.tencent.qcloud.network.sonar.utils.Utils;

import org.minidns.DnsClient;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created by jordanqin on 2024/8/16 17:10.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class NetworkSonar {
    private static Context appContext;

    public static void sonar(final Context appContext, final SonarRequest request, final List<SonarType> types, final NetworkSonarCallback callback) {
        synchronized (NetworkSonar.class) {
            if (NetworkSonar.appContext == null) {
                NetworkSonar.appContext = appContext;
                DnsClient.addDnsServerLookupMechanism(new AndroidDnsServerLookup(appContext));
            }
        }

        SonarResult result = null;
        List<SonarResult> results = new ArrayList<>();
        // 如果断网则直接完成 除NET_INFO
        boolean isNetworkAvailable = Utils.isNetworkAvailable(appContext);
        for (int i = 0; i < types.size(); i++) {
            SonarType type = types.get(i);
            switch (type) {
                case NET_INFO:
                    result = sonarInfo(appContext, request);
                    break;
                case DNS:
                    if (isNetworkAvailable) {
                        result = sonarDns(request);
                        // 拿到dns结果后 给request赋值ip
                        if (result.isSuccess()) {
                            DnsResult dnsResult = (DnsResult) result.getResult();
                            request.setIp(dnsResult.ip);
                        }
                    }
                    break;
                case PING:
                    if (isNetworkAvailable) {
                        result = sonarPing(request);
                    }
                    break;
                case TRACEROUTE:
                    if (isNetworkAvailable) {
                        result = sonarTraceroute(request);
                    }
                    break;
            }
            if (result == null) {
                callback.onFail(new SonarResult(type, new Exception("result is null")));
                continue;
            }
            if (result.isSuccess()) {
                callback.onSuccess(result);
            } else {
                callback.onFail(result);
            }
            results.add(result);
            if (i == types.size() - 1) {
                callback.onFinish(results);
            }
        }
    }

    public static SonarResult<InfoResult> sonarInfo(Context appContext, SonarRequest request) {
        return (new InfoSonar(appContext)).start(request);
    }

    public static SonarResult<DnsResult> sonarDns(SonarRequest request) {
        return (new DnsSonar()).start(request);
    }

    public static SonarResult<PingResult> sonarPing(SonarRequest request) {
        return (new PingSonar()).start(request);
    }

    public static SonarResult<TracerouteResult> sonarTraceroute(SonarRequest request) {
        return (new TracerouteSonar()).start(request);
    }

    public static Context getAppContext() {
        return appContext;
    }
}
