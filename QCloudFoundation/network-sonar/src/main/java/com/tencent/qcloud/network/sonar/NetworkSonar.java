package com.tencent.qcloud.network.sonar;

import android.content.Context;

import com.tencent.qcloud.network.sonar.dns.AndroidDnsServerLookup;
import com.tencent.qcloud.network.sonar.dns.DnsResult;
import com.tencent.qcloud.network.sonar.dns.DnsSonar;
import com.tencent.qcloud.network.sonar.http.HttpResult;
import com.tencent.qcloud.network.sonar.http.HttpSonar;
import com.tencent.qcloud.network.sonar.ping.PingResult;
import com.tencent.qcloud.network.sonar.ping.PingSonar;
import com.tencent.qcloud.network.sonar.traceroute.TracerouteResult;
import com.tencent.qcloud.network.sonar.traceroute.TracerouteSonar;
import com.tencent.qcloud.network.sonar.utils.Utils;

import org.minidns.DnsClient;
import org.minidns.dnsserverlookup.AndroidUsingExec;
import org.minidns.dnsserverlookup.AndroidUsingReflection;
import org.minidns.dnsserverlookup.UnixUsingEtcResolvConf;

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
                DnsClient.removeDNSServerLookupMechanism(AndroidUsingExec.INSTANCE);
                DnsClient.removeDNSServerLookupMechanism(AndroidUsingReflection.INSTANCE);
                DnsClient.removeDNSServerLookupMechanism(UnixUsingEtcResolvConf.INSTANCE);
                DnsClient.addDnsServerLookupMechanism(new AndroidDnsServerLookup(appContext));
            }
        }
        request.setNetworkAvailable(Utils.isNetworkAvailable(appContext));

        List<SonarResult> results = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            SonarResult result = null;
            SonarType type = types.get(i);
            callback.onStart(type);
            switch (type) {
                case DNS:
                    result = sonarDns(request);
                    // 拿到dns结果后 给request赋值ip
                    if (result.isSuccess()) {
                        DnsResult dnsResult = (DnsResult) result.getResult();
                        request.setIp(dnsResult.ip);
                    }
                    break;
                case PING:
                    result = sonarPing(request);
                    break;
                case TRACEROUTE:
                    result = sonarTraceroute(request);
                    break;
                case HTTP:
                    result = sonarHttp(request);
                    break;
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

    public static SonarResult<DnsResult> sonarDns(SonarRequest request) {
        return (new DnsSonar()).start(request);
    }

    public static SonarResult<PingResult> sonarPing(SonarRequest request) {
        return (new PingSonar()).start(request);
    }

    public static SonarResult<TracerouteResult> sonarTraceroute(SonarRequest request) {
        return (new TracerouteSonar()).start(request);
    }

    public static SonarResult<HttpResult> sonarHttp(SonarRequest request) {
        return sonarHttp(request, false);
    }

    public static SonarResult<HttpResult> sonarHttp(SonarRequest request, boolean bypassProxy) {
        return (new HttpSonar(bypassProxy)).start(request);
    }

    public static Context getAppContext() {
        return appContext;
    }
}
