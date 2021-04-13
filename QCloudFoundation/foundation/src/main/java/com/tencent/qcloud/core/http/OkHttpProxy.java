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

package com.tencent.qcloud.core.http;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.qcloud.core.BuildConfig;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.logger.QCloudLogger;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.List;

public class OkHttpProxy<T> extends NetworkProxy<T> {

    private Call httpCall;
    private Field eventListenerFiled;
    private OkHttpClient okHttpClient;

    public OkHttpProxy(OkHttpClient okHttpClient){
        this.okHttpClient = okHttpClient;
    }

    @Override
    public void cancel(){
        if (httpCall != null) {
            httpCall.cancel();
        }
    }

    @Override
    protected HttpResult<T> executeHttpRequest(HttpRequest<T> httpRequest) throws QCloudClientException,
            QCloudServiceException {
        QCloudClientException clientException = null;
        QCloudServiceException serviceException = null;
        Response response = null;
        CallMetricsListener eventListener = null;
        HttpResult<T> httpResult = null;

        try {
            httpRequest.setOkHttpRequestTag(identifier);

            //测试 cos-stgw
//            httpRequest.addHeader("Vod-Forward-Cos", httpRequest.header("Host"));
//            httpRequest.removeHeader("Host");

            Request okHttpRequest = httpRequest.buildRealRequest();

            httpCall = okHttpClient.newCall(okHttpRequest);

            if (eventListenerFiled == null) {
                try {
                    eventListenerFiled = httpCall.getClass().getDeclaredField("eventListener");
                    eventListenerFiled.setAccessible(true);
                    eventListener = (CallMetricsListener) eventListenerFiled.get(httpCall);
                } catch (NoSuchFieldException ignore) {
                } catch (IllegalAccessException ignore) {
                }
            }

            response = httpCall.execute();

            if (response != null) {
                if (httpResult == null) {
                    httpResult = convertResponse(httpRequest, response);
                }

            } else {
                serviceException = new QCloudServiceException("http response is null");
            }

        } catch (IOException e) {
            if (e.getCause() instanceof QCloudClientException) {
                clientException = (QCloudClientException) e.getCause();
            } else if (e.getCause() instanceof QCloudServiceException) {
                serviceException = (QCloudServiceException) e.getCause();
            } else {
                clientException = new QCloudClientException(e);
            }
        } finally {
            if(response != null) Util.closeQuietly(response);
        }

        if (eventListener != null) {
            eventListener.dumpMetrics(metrics);
        }

        if (clientException != null) {
            throw clientException;
        } else if (serviceException != null) {
            throw serviceException;
        } else {
            if (isCosResponse(response)) {
                recordDns(httpRequest.host(), eventListener);
            }
            return httpResult;
        }
    }

    private void recordDns(String host, CallMetricsListener eventListener) {

        List<InetAddress> dnsRecord = null;
        if (eventListener != null && (dnsRecord = eventListener.dumpDns()) != null) {
            DnsRepository.getInstance().insertDnsRecordCache(host, dnsRecord);
        }
    }

    private boolean isCosResponse(Response response) {

        return response != null && "tencent-cos".equalsIgnoreCase(response.header("Server"));
    }

    @Override
    protected HttpResult<T> convertResponse(HttpRequest<T> request, Response response) throws QCloudClientException, QCloudServiceException {
        HttpResponse<T> httpResponse = new HttpResponse<>(request, response);
        ResponseBodyConverter<T> converter = request.getResponseBodyConverter();
        if (converter instanceof ProgressBody) {
            ((ProgressBody) converter).setProgressListener(mProgressListener);
        }
        T content = converter.convert(httpResponse);
        return new HttpResult<T>(httpResponse, content);
    }
}
