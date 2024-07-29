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
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
        builder.interceptors().clear();
        RedirectInterceptor redirectInterceptor = new RedirectInterceptor();
        builder.followRedirects(false)
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
                .addInterceptor(redirectInterceptor);
        // 绕过ssl
        if(!b.verifySSLEnable){
            try {
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                // 创建一个TrustManager，绕过证书校验
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager)trustAllCerts[0]);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        okHttpClient = builder.build();
        redirectInterceptor.setClient(okHttpClient);
    }

    @Override
    public NetworkProxy getNetworkProxy() {
        OkHttpProxy okHttpProxy = new OkHttpProxy(okHttpClient);
        return okHttpProxy;
    }
}
