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

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.*;
import com.tencent.qcloud.core.task.RetryStrategy;

import okhttp3.*;
import okhttp3.internal.Util;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class QuicProxy<T> extends NetworkProxy<T> {

    private QuicImpl quic;
    private QuicManager quicManager;
    private RetryStrategy retryStrategy;
    private Dns dns;
    private HttpLogger httpLogger;

    public QuicProxy(QuicManager quicManager, Dns dns, HttpLogger httpLogger, RetryStrategy retryStrategy){
        this.quicManager = quicManager;
        this.retryStrategy = retryStrategy;
        this.dns = dns;
        this.httpLogger = httpLogger;
    }

    @Override
    public void cancel(){
        if(quic != null){
            quic.cancelConnect();
        }
    }

    @Override
    protected HttpResult<T> executeHttpRequest(HttpRequest<T> httpRequest) throws QCloudClientException,
            QCloudServiceException {
        Response response = null;
        HttpResult<T> httpResult = null;
        QCloudClientException clientException = null;
        QCloudServiceException serviceException = null;
        int attempt = 0;
        long startTime = System.nanoTime();
        while (true){
            CallMetricsListener callMetricsListener;
            try {
                httpRequest.setOkHttpRequestTag(identifier);
                final Request okHttpRequest = httpRequest.buildRealRequest();

                //设置url
                HttpUrl httpUrl = okHttpRequest.url();
                String host = httpUrl.host();
                boolean isHttps = httpUrl.isHttps();
                String url = httpUrl.toString();
                int index = url.indexOf("/", isHttps? 8 : 7); // https:// or http://
                String path = "/";
                if(index > 0) path = url.substring(index);
                String ip = null;
                callMetricsListener = new CallMetricsListener(null);
                callMetricsListener.dnsStart(null, host);

                List<InetAddress> inetAddresses = dns.lookup(host);

                if(inetAddresses != null && inetAddresses.size() > 0){
                    ip = inetAddresses.get(0).getHostAddress();
                    // QLog.d("dns ip: " + ip);
                }
                callMetricsListener.dnsEnd(null, host, inetAddresses);

                int port = httpUrl.port();
                int tcpPort = 80;

                String method = okHttpRequest.method().toUpperCase();

                QuicRequest quicRequest = new QuicRequest(host, ip, port, tcpPort);
                quicRequest.addHeader(":scheme", isHttps? "https" :"http");
                quicRequest.addHeader(":method", method);
                quicRequest.addHeader(":path",  path);
                quicRequest.addHeader(":authority", host);


                Headers headers = okHttpRequest.headers();

                //设置 header
                for(int i = 0, size = headers.size(); i < size; i ++){
                    String headerKey = headers.name(i);
                    if("Host".equalsIgnoreCase(headerKey)){
                        quicRequest.addHeader("Vod-Forward-Cos".toLowerCase(), headers.value(i));
                    }
                    // 不要修改 ua，否则签名会不一致
//                    else if("User-Agent".equalsIgnoreCase(headerKey)){
//                        String headerValue = headers.value(i);
//                        int pos = headerValue.lastIndexOf('-');
//                        StringBuilder stringBuilder = new StringBuilder();
//                        stringBuilder.append(headerValue.substring(0, pos))
//                                .append("-quic-")
//                                .append(headerValue.substring(pos + 1));
//                        quicRequest.addHeader("User-Agent".toLowerCase(), stringBuilder.toString());
//                    }

                    else {
                        quicRequest.addHeader(headers.name(i).toLowerCase(), headers.value(i));
                    }
                }

                RequestBody requestBody = httpRequest.getRequestBody();
                if (httpRequest.getRequestBody() != null) {
                    String contentType = requestBody.contentType() != null ?
                            requestBody.contentType().toString() : "application/octet-stream";
                    quicRequest.addHeader("Content-Type".toLowerCase(), contentType);
                    quicRequest.addHeader("Content-Length".toLowerCase(), String.valueOf(requestBody.contentLength()));
                }

                // 打印 request
                String requestStartMessage = "--> " + okHttpRequest.method() + ' ' + okHttpRequest.url() + ' ' + Protocol.QUIC;
                OkHttpLoggingUtils.logMessage(requestStartMessage, httpLogger);
                OkHttpLoggingUtils.logQuicRequestHeaders(quicRequest.headers, httpLogger);

                //设置 body
                quicRequest.setRequestBody(okHttpRequest.body());

                //创建 quic
                quic = quicManager.newQuicImpl(quicRequest);
                quic.setCallMetricsListener(callMetricsListener);

                //若是下载，需要指定输出位置，否则就是默认文本输出
                final ResponseBodyConverter<T> converter = httpRequest.getResponseBodyConverter();
                if(converter instanceof ResponseFileConverter){
                    ((ResponseFileConverter<T>) converter).enableQuic(true);
                    ((ResponseFileConverter<T>) converter).setProgressListener(mProgressListener);
                    final QCloudProgressListener progressListener = ((ResponseFileConverter<T>) converter).getProgressListener();
                    quic.setProgressCallback(new ProgressCallback() {
                        @Override
                        public void onProgress(long current, long total) {
                            progressListener.onProgress(current, total);
                        }
                    });

                    quic.setOutputDestination(((ResponseFileConverter<T>) converter).getOutputStream());
                }

                long startNs = System.nanoTime();
                //执行请求 获取响应
                QuicResponse quicResponse = quic.call();

//                quic.clear();

                //转化为Response
                response = quicResponse.covertResponse(okHttpRequest);

                long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
                // 打印 response
                OkHttpLoggingUtils.logResponse(response, tookMs, HttpLoggingInterceptor.Level.HEADERS, httpLogger);

                httpResult = convertResponse(httpRequest, response);
                callMetricsListener.dumpMetrics(metrics);
                break;
            } catch (Exception e) {
                if (e.getCause() instanceof QCloudServiceException) {
                    serviceException = (QCloudServiceException) e.getCause();
                    break;
                }else{
                    //是否需要重试
                    if(retryStrategy.shouldRetry(attempt++, System.nanoTime() - startTime, 0)){
                        // QLog.d("%s failed for %s, %d", httpRequest.url().toString(), e.getMessage(), attempt);
                    }else {
                        if (e.getCause() instanceof QCloudClientException) {
                            clientException = (QCloudClientException) e.getCause();
                        } else {
                            clientException = new QCloudClientException(e);
                        }
                        break;
                    }
                }
            }finally {
                Util.closeQuietly(response);
            }
        }

        if (clientException != null) {
            throw clientException;
        } else if (serviceException != null) {
            throw serviceException;
        } else {
            return httpResult;
        }
    }

    @Override
    protected HttpResult<T> convertResponse(HttpRequest<T> httpRequest, Response response) throws QCloudClientException, QCloudServiceException {
        HttpResponse<T> httpResponse = new HttpResponse<>(httpRequest, response);
        ResponseBodyConverter<T> converter = httpRequest.getResponseBodyConverter();
        T content = converter.convert(httpResponse);
        return new HttpResult<>(httpResponse, content);
    }


}
