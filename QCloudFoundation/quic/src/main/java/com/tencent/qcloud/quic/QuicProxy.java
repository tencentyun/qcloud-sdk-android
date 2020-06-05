package com.tencent.qcloud.quic;



import android.util.Log;

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.*;
import okhttp3.*;
import okhttp3.internal.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;


public class QuicProxy<T> extends NetworkProxy<T> {

    private QuicImpl quic;
    private QuicManager quicManager;
    public QuicProxy(QuicManager quicManager){
        this.quicManager = quicManager;
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

                List<InetAddress> inetAddresses = quicManager.dns.lookup(host);

                if(inetAddresses != null && inetAddresses.size() > 0){
                    ip = inetAddresses.get(0).getHostAddress();
                    QLog.d("dns ip: " + ip);
                }
                callMetricsListener.dnsEnd(null, host, inetAddresses);

                int port = httpUrl.port();
                int tcpPort = 80;

                String method = okHttpRequest.method().toUpperCase();

                QuicRequest quicRequest = new QuicRequest(host, ip, port, tcpPort);
                quicRequest.addHeader(":scheme", isHttps? "https" :"http");
                quicRequest.addHeader(":method", method);
                quicRequest.addHeader(":path",  path);

                Headers headers = okHttpRequest.headers();

                //设置 header
                for(int i = 0, size = headers.size(); i < size; i ++){
                    String headerKey = headers.name(i);
                    if("Host".equalsIgnoreCase(headerKey)){
                        quicRequest.addHeader("Vod-Forward-Cos".toLowerCase(), headers.value(i));
                    } else if("User-Agent".equalsIgnoreCase(headerKey)){
                        String headerValue = headers.value(i);
                        int pos = headerValue.lastIndexOf('-');
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(headerValue.substring(0, pos))
                                .append("-")
                                .append("quic")
                                .append(headerValue.substring(pos + 1));
                        quicRequest.addHeader("User-Agent".toLowerCase(), stringBuilder.toString());
                    } else {
                        quicRequest.addHeader(headers.name(i).toLowerCase(), headers.value(i));
                    }
                }
                quicRequest.addHeader(":authority", host);

                //设置 body
                quicRequest.setRequestBody(okHttpRequest.body());

                String requestStartMessage = "--> " + okHttpRequest.method() + ' ' + okHttpRequest.url() + ' ' + "quic";
                quicManager.httpLogger.logRequest(requestStartMessage);

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

                //执行请求 获取响应
                QuicResponse quicResponse = quic.call();

//                quic.clear();

                //转化为Response
                response = quicResponse.covertResponse(okHttpRequest);
                quicManager.httpLogger.logResponse(response, "<-- " + response.code() + ' ' + response.message() + ' '
                        + response.request().url());
                httpResult = convertResponse(httpRequest, response);
                callMetricsListener.dumpMetrics(metrics);
                break;
            } catch (Exception e) {
                if (e.getCause() instanceof QCloudServiceException) {
                    serviceException = (QCloudServiceException) e.getCause();
                    break;
                }else{
                    //是否需要重试
                    if(quicManager.retryStrategy.shouldRetry(attempt++, System.nanoTime() - startTime, 0)){
                        QLog.d("%s failed for %s, %d", httpRequest.url().toString(), e.getMessage(), attempt);
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
