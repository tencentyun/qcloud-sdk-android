/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.qcloud.quic;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

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
    public void setOutputStream(OutputStream outputStream) {
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
    public Response covertResponse(Request request) throws QuicException {
        if(code == 0 && message == null){
            throw new QuicException("no response received or response is empty");
        }

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
