package com.tencent.qcloud.quic.test;


import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuicRequest {

    protected String host;
    protected String ip;
    protected int port;
    protected int tcpPort = 80;

    protected ProgressCallback progressCallback;

    protected Map<String, String> headers = new LinkedHashMap<>(); //有序
    protected InputStream inputStream;

    protected long contentLength = 0L;

    public QuicRequest(String host, String ip, int port, int tcpPort){
        this.host = host;
        this.ip = ip;
        this.port = port;
        this.tcpPort = tcpPort;
    }

    public void addHeader(String key, String value){
        headers.put(key, value);
    }

    public void setSource(InputStream inputStream, long contentLength){
        this.inputStream = inputStream;
        this.contentLength = contentLength;
    }

    public void setProgressCallback(ProgressCallback progressCallback){
        this.progressCallback = progressCallback;
    }


}
