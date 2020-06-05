package com.tencent.qcloud.quic;

import okhttp3.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuicRequest {

    protected String host;
    protected String ip;
    protected int port;
    protected int tcpPort = 80;

    protected Map<String, String> headers = new LinkedHashMap<>(); //有序

    protected RequestBody requestBody;

    public QuicRequest(String host, String ip, int port, int tcpPort){
        this.host = host;
        this.ip = ip;
        this.port = port;
        this.tcpPort = tcpPort;
    }

    public void addHeader(String key, String value){
        headers.put(key, value);
    }

    public void setRequestBody(RequestBody requestBody){
        this.requestBody = requestBody;
    }

}
