package com.tencent.qcloud.network.sonar.dns;

import com.tencent.qcloud.network.sonar.Sonar;
import com.tencent.qcloud.network.sonar.SonarRequest;
import com.tencent.qcloud.network.sonar.SonarResult;
import com.tencent.qcloud.network.sonar.SonarType;
import com.tencent.qcloud.network.sonar.utils.SonarLog;

import org.minidns.dnsmessage.Question;
import org.minidns.hla.DnssecResolverApi;
import org.minidns.hla.ResolverResult;
import org.minidns.record.CNAME;
import org.minidns.record.Data;
import org.minidns.record.Record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created by jordanqin on 2024/8/19 11:04.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class DnsSonar implements Sonar<DnsResult> {
    public SonarResult<DnsResult> start(SonarRequest request) {
        DnsResult dnsResult = new DnsResult();
        try {
            long startTime = System.currentTimeMillis();
            DnssecResolverApi.INSTANCE.getDnssecClient().setUseHardcodedDnsServers(false);
            Question question = new Question(request.getHost(), Record.TYPE.A);
            ResolverResult<Data> result = DnssecResolverApi.INSTANCE.resolve(question);
            List<Record<? extends Data>> answerSection = result.getRawAnswer().answerSection;
            List<String> aList = new ArrayList<>();
            List<String> cnameList = new ArrayList<>();
            for (Record<? extends Data> record : answerSection) {
                if(record.type == Record.TYPE.A){
                    aList.add(record.getPayload().toString());
                } else if(record.type == Record.TYPE.CNAME){
                    cnameList.add(((CNAME)record.getPayload()).target.toString());
                }
            }
            dnsResult.a = String.join(",", aList);
            dnsResult.cname = String.join(",", cnameList);
            dnsResult.response = result.getDnsQueryResult().response.toString();

            long endTime = System.currentTimeMillis();
            dnsResult.ip = aList.get(aList.size() - 1);
            dnsResult.lookupTime = endTime - startTime;
            return new SonarResult<>(SonarType.DNS, dnsResult);
        } catch (IOException e) {
            if(SonarLog.openLog){
                e.printStackTrace();
            }
            return new SonarResult<>(SonarType.DNS, e);
        }
    }

    @Override
    public void stop() {

    }
}
