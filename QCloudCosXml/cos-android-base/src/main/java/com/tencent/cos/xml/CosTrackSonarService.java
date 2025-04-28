package com.tencent.cos.xml;

import static com.tencent.cos.xml.CosTrackService.EVENT_CODE_TRACK_COS_SDK_SONAR;
import static com.tencent.cos.xml.CosTrackService.EVENT_CODE_TRACK_COS_SDK_SONAR_FAILURE;

import android.content.Context;

import com.tencent.cos.xml.base.BuildConfig;
import com.tencent.qcloud.core.logger.COSLogger;
import com.tencent.qcloud.network.sonar.NetworkSonar;
import com.tencent.qcloud.network.sonar.NetworkSonarCallback;
import com.tencent.qcloud.network.sonar.SonarRequest;
import com.tencent.qcloud.network.sonar.SonarResult;
import com.tencent.qcloud.network.sonar.SonarType;
import com.tencent.qcloud.network.sonar.dns.DnsResult;
import com.tencent.qcloud.network.sonar.ping.PingResult;
import com.tencent.qcloud.network.sonar.traceroute.TracerouteResult;
import com.tencent.qcloud.track.QCloudTrackService;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Created by jordanqin on 2025/2/8 19:42.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class CosTrackSonarService {
    private static final String TAG = "CosTrackSonarService";
    private Context applicationContext;
    // 常规探测
    private final ScheduledExecutorService sonarScheduler = Executors.newScheduledThreadPool(1);

    public void setContext(Context context) {
        this.applicationContext = context;
    }

    public static boolean isIncludeSonar() {
        try {
            Class.forName("com.tencent.qcloud.network.sonar.NetworkSonar");
            return true;
        } catch (ClassNotFoundException e){
            return false;
        }
    }

    public void periodicSonar(){
        final Runnable sonar = () -> {
            CosTrackService.SonarHost sonarHost = CosTrackService.getInstance().getSonarHosts().get();
            if(sonarHost == null || sonarHost.getHost() == null) return;

            long diffInMinutes = (System.currentTimeMillis() - CosTrackService.getInstance().getSonarHosts().getSonarHostsAddTimestamp()) / (1000 * 60);
            // 检查是否超过了15分钟
            if (diffInMinutes > 15) return;

            Map<String, String> extra = new HashMap<>();
            extra.put("region", sonarHost.getRegion());
            extra.put("bucket", sonarHost.getBucket());
            sonar(EVENT_CODE_TRACK_COS_SDK_SONAR, sonarHost.getHost(), extra, true);
        };
        // 探测策略：起始延迟3分钟，每10分钟执行一次
        sonarScheduler.scheduleWithFixedDelay(sonar, 3, 10, TimeUnit.MINUTES);
    }


    public void failSonar(String host, String region, String bucket, String clientTraceId){
        if(host == null) return;
        Map<String, String> extra = new HashMap<>();
        extra.put("region", region);
        extra.put("bucket", bucket);
        extra.put("client_trace_id", clientTraceId);
        sonar(EVENT_CODE_TRACK_COS_SDK_SONAR_FAILURE, host, extra, false);
    }

    private void sonar(String eventCode, String host, Map<String, String> extra, boolean periodic){
        // 不做无谓的探测
        if (CosTrackService.getInstance().isCloseReport() || !isIncludeSonar()) {
            return;
        }

        try {
            Map<String, String> params = new HashMap<>(extra);
            params.put("host", host);

            SonarRequest sonarRequest;
            List<SonarType> types = new ArrayList<>();
            if(periodic){
                types.add(SonarType.PING);
                long startTime = System.currentTimeMillis();
                InetAddress address = InetAddress.getByName(host);
                String dnsIp = address.getHostAddress();
                params.put("dns_ip", dnsIp);
                params.put("dns_lookupTime", String.valueOf(System.currentTimeMillis() - startTime));
                sonarRequest = new SonarRequest(host, dnsIp);
            } else {
                types.add(SonarType.DNS);
                types.add(SonarType.PING);
                types.add(SonarType.TRACEROUTE);
                sonarRequest = new SonarRequest(host);
            }
            NetworkSonar.sonar(applicationContext, sonarRequest, types, new NetworkSonarCallback() {
                @Override
                public void onStart(SonarType type) {

                }

                @Override
                public void onSuccess(SonarResult result) {
                }

                @Override
                public void onFail(SonarResult result) {
                }

                @Override
                public void onFinish(List<SonarResult> results) {
                    if(results != null && !results.isEmpty()){
                        for(SonarResult sonarResult : results){
                            if(sonarResult == null || !sonarResult.isSuccess() || sonarResult.getResult() == null) continue;
                            switch (sonarResult.getType()) {
                                case DNS:
                                    DnsResult dnsResult = (DnsResult) sonarResult.getResult();
                                    params.put("dns_ip", dnsResult.ip);
                                    params.put("dns_lookupTime", String.valueOf(dnsResult.lookupTime));
                                    params.put("dns_a", dnsResult.a);
                                    params.put("dns_cname", dnsResult.cname);
                                    params.put("dns_result", dnsResult.response);
                                    break;
                                case PING:
                                    PingResult pingResult = (PingResult) sonarResult.getResult();
                                    params.put("ping_ip", pingResult.ip);
                                    params.put("ping_size", String.valueOf(pingResult.size));
                                    params.put("ping_interval", String.valueOf(pingResult.interval));
                                    params.put("ping_count", String.valueOf(pingResult.count));
                                    params.put("ping_loss", String.valueOf(pingResult.getLoss()));
                                    params.put("ping_response_num", String.valueOf(pingResult.getResponseNum()));
                                    params.put("ping_avg", String.valueOf(pingResult.avg));
                                    params.put("ping_max", String.valueOf(pingResult.max));
                                    params.put("ping_min", String.valueOf(pingResult.min));
                                    params.put("ping_stddev", String.valueOf(pingResult.stddev));
                                    break;
                                case TRACEROUTE:
                                    TracerouteResult tracerouteResult = (TracerouteResult) sonarResult.getResult();
                                    params.put("traceroute_ip", tracerouteResult.getTargetIp());
                                    params.put("traceroute_status", tracerouteResult.getCommandStatus().getName());
                                    params.put("traceroute_hop_count", String.valueOf(tracerouteResult.getHopCount()));
                                    params.put("traceroute_total_delay", String.valueOf(tracerouteResult.getTotalDelay()));
                                    params.put("traceroute_avg_loss_rate", String.valueOf(tracerouteResult.getLossRate()));
                                    params.put("traceroute_nodes", tracerouteResult.getNodeResultsString());
                                    break;
                            }
                        }
                        // 至少探测到一种网络情况才上报
                        if(params.containsKey("dns_ip") || params.containsKey("ping_ip") || params.containsKey("traceroute_ip")){
                            QCloudTrackService.getInstance().report(eventCode, params);
                            if(!periodic){
                                COSLogger.iProbe("FailSonar", params.toString());
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            if(BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
