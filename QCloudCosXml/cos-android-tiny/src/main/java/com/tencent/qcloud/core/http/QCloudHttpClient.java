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


import androidx.annotation.NonNull;

import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.task.QCloudTask;
import com.tencent.qcloud.core.task.RetryStrategy;
import com.tencent.qcloud.core.task.TaskManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import okhttp3.*;

public final class QCloudHttpClient {

    public static final String HTTP_LOG_TAG = "QCloudHttp";
    public static final String QUIC_LOG_TAG = "QCloudQuic";

    // private NetworkClient networkClient;
    private String networkClientType = OkHttpClientImpl.class.getName();
    private static Map<Integer, NetworkClient> networkClientMap = new HashMap<>(2);
    private final TaskManager taskManager;

    private final Set<String> verifiedHost;
    private final Map<String, List<InetAddress>> dnsMap;

    private boolean dnsCache = true;

    private static volatile QCloudHttpClient gDefault;

    private HostnameVerifier mHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            if (verifiedHost.size() > 0) {
                for (String host : verifiedHost) {
                    if (HttpsURLConnection.getDefaultHostnameVerifier().verify(host, session)) {
                        return true;
                    }
                }
            }
            return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
        }
    };


    private Dns mDns = new Dns() {

        @Override
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {

            List<InetAddress> dns = null;

            // 首先使用用户设置的 dns
            if (dnsMap.containsKey(hostname)) {
                dns = dnsMap.get(hostname);
            }

            // 然后使用系统的 dns
            if (dns == null) {
                try {
                    dns = Dns.SYSTEM.lookup(hostname);
                } catch (UnknownHostException e) {
                    // e.printStackTrace();
                    //QCloudLogger.w(HTTP_LOG_TAG, "system dns failed, retry cache dns records.");
                }
            }

            if (dns == null && !dnsCache) {
                throw new UnknownHostException("can not resolve host name " + hostname);
            }

            // 最后使用缓存的 dns
            if (dns == null) {
                throw new UnknownHostException(hostname);
            }

            return dns;
        }
    };

    public static QCloudHttpClient getDefault() {
        if (gDefault == null) {
            synchronized (QCloudHttpClient.class) {
                if (gDefault == null) {
                    gDefault = new QCloudHttpClient.Builder().build();
                }
            }
        }

        return gDefault;
    }

    public void addVerifiedHost(String hostname) {
        if (hostname != null) {
            verifiedHost.add(hostname);
        }
    }

    public void addDnsRecord(@NonNull String hostName, @NonNull String[] ipAddress) throws UnknownHostException {
        if (ipAddress.length > 0) {
            List<InetAddress> addresses = new ArrayList<>(ipAddress.length);
            for (String ip : ipAddress) {
                addresses.add(InetAddress.getByName(ip));
            }
            dnsMap.put(hostName, addresses);
        }
    }


    private QCloudHttpClient(Builder b) {
        this.verifiedHost = new HashSet<>(5);
        this.dnsMap = new HashMap<>(3);
        this.taskManager = TaskManager.getInstance();


        NetworkClient networkClient = b.networkClient;
        if(networkClient == null){
            networkClient = new OkHttpClientImpl();
        }
        networkClientType = networkClient.getClass().getName();
        int hashCode = networkClientType.hashCode();
        if(!networkClientMap.containsKey(hashCode)){
            networkClient.init(b, hostnameVerifier(), mDns);
            networkClientMap.put(hashCode, networkClient);
        }
    }

    public void setNetworkClientType(Builder b){
        NetworkClient networkClient = b.networkClient;
        if(networkClient != null){
            String name = networkClient.getClass().getName();
            int hashCode = name.hashCode();
            if(!networkClientMap.containsKey(hashCode)){
                networkClient.init(b, hostnameVerifier(), mDns);
                networkClientMap.put(hashCode, networkClient);
            }
            this.networkClientType = name;
        }
    }

    public List<HttpTask> getTasksByTag(String tag) {
        List<HttpTask> tasks = new ArrayList<>();
        if (tag == null) {
            return tasks;
        }

        List<QCloudTask> taskManagerSnapshot = taskManager.snapshot();
        for (QCloudTask task : taskManagerSnapshot) {
            if (task instanceof HttpTask && tag.equals(task.getTag())) {
                tasks.add((HttpTask) task);
            }
        }

        return tasks;
    }

    public <T> HttpTask<T> resolveRequest(HttpRequest<T> request) {
        return handleRequest(request, null);
    }

    public <T> HttpTask<T> resolveRequest(QCloudHttpRequest<T> request,
                                          QCloudCredentialProvider credentialProvider) {
        return handleRequest(request, credentialProvider);
    }

    private HostnameVerifier hostnameVerifier() {
        return mHostnameVerifier;
    }


    private <T> HttpTask<T> handleRequest(HttpRequest<T> request,
                                          QCloudCredentialProvider credentialProvider) {
        return new HttpTask<T>(request, credentialProvider, networkClientMap.get(networkClientType.hashCode()));
    }

    public final static class Builder {
        int connectionTimeout = 15 * 1000;  //in milliseconds
        int socketTimeout = 30 * 1000;  //in milliseconds
        RetryStrategy retryStrategy;
        QCloudHttpRetryHandler qCloudHttpRetryHandler;
        OkHttpClient.Builder mBuilder;
        NetworkClient networkClient;
        boolean enableDebugLog = false;
        List<String> prefetchHost = new LinkedList<>();
        boolean dnsCache = false;

        public Builder() {
        }

        public Builder setConnectionTimeout(int connectionTimeout) {
            if (connectionTimeout < 3 * 1000) {
                throw new IllegalArgumentException("connection timeout must be larger than 3 seconds.");
            }
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setSocketTimeout(int socketTimeout) {
            if (socketTimeout < 3 * 1000) {
                throw new IllegalArgumentException("socket timeout must be larger than 3 seconds.");
            }
            this.socketTimeout = socketTimeout;
            return this;
        }

        public Builder setRetryStrategy(RetryStrategy retryStrategy) {
            this.retryStrategy = retryStrategy;
            return this;
        }

        public Builder setQCloudHttpRetryHandler(QCloudHttpRetryHandler qCloudHttpRetryHandler) {
            this.qCloudHttpRetryHandler = qCloudHttpRetryHandler;
            return this;
        }

        public Builder setInheritBuilder(OkHttpClient.Builder builder) {
            mBuilder = builder;
            return this;
        }

        public Builder setNetworkClient(NetworkClient networkClient){
            this.networkClient = networkClient;
            return this;
        }

        public Builder enableDebugLog(boolean enableDebugLog){
            this.enableDebugLog = enableDebugLog;
            return this;
        }

        public Builder addPrefetchHost(String host) {
            prefetchHost.add(host);
            return this;
        }

        public Builder dnsCache(boolean dnsCache) {
            this.dnsCache = dnsCache;
            return this;
        }

        public QCloudHttpClient build() {
            if (retryStrategy == null) {
                retryStrategy = RetryStrategy.DEFAULT;
            }
            if(qCloudHttpRetryHandler != null){
                retryStrategy.setRetryHandler(qCloudHttpRetryHandler);
            }
            if (mBuilder == null) {
                mBuilder = new OkHttpClient.Builder();
            }

            return new QCloudHttpClient(this);
        }
    }
}
