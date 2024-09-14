package com.tencent.qcloud.network.sonar;

/**
 * <p>
 * Created by jordanqin on 2024/8/16 17:14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class SonarRequest {
    private String host;
    private String ip;

    public SonarRequest(String host) {
        this.host = host;
    }

    public SonarRequest(String host, String ip) {
        this.host = host;
        this.ip = ip;
    }

    public String getHost() {
        return host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
