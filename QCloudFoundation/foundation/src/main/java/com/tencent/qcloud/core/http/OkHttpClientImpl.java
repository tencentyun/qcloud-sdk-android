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

import com.tencent.qcloud.core.BuildConfig;
import com.tencent.qcloud.core.http.interceptor.RedirectInterceptor;
import com.tencent.qcloud.core.http.interceptor.RetryInterceptor;
import com.tencent.qcloud.core.http.interceptor.TrafficControlInterceptor;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Call;
import okhttp3.Dns;
import okhttp3.OkHttpClient;

public class OkHttpClientImpl extends NetworkClient {

    private okhttp3.EventListener.Factory mEventListenerFactory = new okhttp3.EventListener.Factory() {
        @Override
        public okhttp3.EventListener create(Call call) {
            return new CallMetricsListener(call);
        }
    };

    private OkHttpClient okHttpClient;

    @Override
    public void init(QCloudHttpClient.Builder b, HostnameVerifier hostnameVerifier,
                     final Dns dns, HttpLogger httpLogger) {
        super.init(b, hostnameVerifier, dns, httpLogger);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(httpLogger);
        if(BuildConfig.DEBUG){
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        }
        OkHttpClient.Builder builder = b.mBuilder;
        RedirectInterceptor redirectInterceptor = new RedirectInterceptor();
        okHttpClient = builder
                .followRedirects(false)
                .followSslRedirects(true)
                .hostnameVerifier(hostnameVerifier)
                .dns(dns)
                .connectTimeout(b.connectionTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(b.socketTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(b.socketTimeout, TimeUnit.MILLISECONDS)
                .eventListenerFactory(mEventListenerFactory)
//                .addNetworkInterceptor(new HttpMetricsInterceptor())
                .addInterceptor(logInterceptor)
                .addInterceptor(new RetryInterceptor(b.retryStrategy))
                .addInterceptor(new TrafficControlInterceptor())
                .addInterceptor(redirectInterceptor)
                .build();
        redirectInterceptor.setClient(okHttpClient);
    }

    @Override
    public NetworkProxy getNetworkProxy() {
        OkHttpProxy okHttpProxy = new OkHttpProxy(okHttpClient);
        return okHttpProxy;
    }
}
