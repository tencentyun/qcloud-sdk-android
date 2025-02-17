package com.tencent.qcloud.network.sonar.dns;

import java.util.List;

/**
 * <p>
 * Created by jordanqin on 2024/8/19 11:04.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class DnsResult {
    public long lookupTime;
    public String ip;
    public String a;
    public String cname;
    public String response;
    // 本地hosts
    public List<String> localHosts;

    @Override
    public String toString() {
        return "DnsResult{" +
                "lookupTime=" + lookupTime +
                ", ip='" + ip + '\'' +
                ", a='" + a + '\'' +
                ", cname='" + cname + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
