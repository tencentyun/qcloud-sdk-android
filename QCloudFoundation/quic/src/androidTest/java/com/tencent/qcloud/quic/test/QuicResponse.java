package com.tencent.qcloud.quic.test;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuicResponse {

    protected Map<String, String> headers = new LinkedHashMap<>();
    protected OutputStream outputStream;
    protected ProgressCallback progressCallback;
    private long currentLength = 0L;
    private long totalLength = -1L;

    public QuicResponse(){}

    public void setProgressCallback(ProgressCallback progressCallback){
        this.progressCallback = progressCallback;
    }

    public void setSink(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    protected void setContentLength(long contentLength){
        this.totalLength = contentLength;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    protected void updateProgress(int dataLength){
        currentLength += dataLength;
        if(progressCallback != null){
            progressCallback.onProgress(currentLength, totalLength);
        }
    }

}
