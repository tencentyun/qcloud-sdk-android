package com.tencent.qcloud.quic;

import okhttp3.*;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuicResponse {

    protected Map<String, String> headers = new LinkedHashMap<>();
    protected int code;
    protected String message;
    protected String contentType;
    protected Buffer buffer = new Buffer();
    protected BufferedSink fileSink;
    protected ProgressCallback progressCallback;
    protected long currentLength = 0L;
    protected long totalLength = -1L;


    public QuicResponse(){}

    protected void setContentLength(long contentLength){
        this.totalLength = contentLength;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public void setProgressCallback(ProgressCallback progressCallback){
        this.progressCallback = progressCallback;
    }

    //设置下载文件到本地地址
    public void setOutputStream(OutputStream outputStream){
        fileSink = Okio.buffer(Okio.sink(outputStream));
    }

    protected void updateProgress(int dataLength){
        currentLength += dataLength;
        if(progressCallback != null){
            progressCallback.onProgress(currentLength, totalLength);
        }
    }

    /**
     * 构造 Okhttp3 Response {@link Response}
     * @param request
     * @return
     */
    public Response covertResponse(Request request){
        Headers headers = Headers.of(this.headers);
        Response response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message(message)
                .headers(headers)
                .body(new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        if(contentType != null){
                            return MediaType.parse(contentType);
                        }else {
                            return null;
                        }
                    }

                    @Override
                    public long contentLength() {
                        return totalLength;
                    }

                    @Override
                    public BufferedSource source() {
                        return buffer;
                    }
                })
                .build();
        return response;
    }

}
