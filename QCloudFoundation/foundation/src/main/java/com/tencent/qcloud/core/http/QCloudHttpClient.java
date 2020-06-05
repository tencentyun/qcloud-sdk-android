package com.tencent.qcloud.core.http;


import android.support.annotation.NonNull;

import com.tencent.qcloud.core.R;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.QCloudTask;
import com.tencent.qcloud.core.task.RetryStrategy;
import com.tencent.qcloud.core.task.TaskManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.*;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public final class QCloudHttpClient {

    public static final String HTTP_LOG_TAG = "QCloudHttp";

   // private NetworkClient networkClient;
    private String networkClientType = OkHttpClientImpl.class.getName();
    private static Map<Integer, NetworkClient> networkClientMap = new HashMap<>(2);
    private final TaskManager taskManager;
    private final HttpLogger httpLogger;

    private final Set<String> verifiedHost;
    private final Map<String, List<InetAddress>> dnsMap;

    private final DnsRepository dnsRepository;

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
            if (dnsMap.containsKey(hostname)) {
                return dnsMap.get(hostname);
            }

            try {
                return Dns.SYSTEM.lookup(hostname);
            } catch (UnknownHostException e) {
                // e.printStackTrace();
                QCloudLogger.w(HTTP_LOG_TAG, "system dns failed, retry cache dns records.");
            }

            if (!dnsCache) {
                throw new UnknownHostException("can not resolve host name " + hostname);
            }

            return dnsRepository.getDnsRecord(hostname);
        }
    };

    private okhttp3.EventListener.Factory mEventListenerFactory = new okhttp3.EventListener.Factory() {
        @Override
        public okhttp3.EventListener create(Call call) {
            return new CallMetricsListener(call);
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

    public void setDebuggable(boolean debuggable) {
        httpLogger.setDebug(debuggable || QCloudLogger.isLoggableOnLogcat(QCloudLogger.DEBUG, HTTP_LOG_TAG));
    }

    private QCloudHttpClient(Builder b) {
        this.verifiedHost = new HashSet<>(5);
        this.dnsMap = new HashMap<>(3);
        this.taskManager = TaskManager.getInstance();
        this.dnsRepository = DnsRepository.getInstance();
        httpLogger = new HttpLogger(false);
        setDebuggable(false);
        NetworkClient networkClient = b.networkClient;
        if(networkClient == null){
            networkClient = new OkHttpClientImpl();
        }
        networkClientType = networkClient.getClass().getName();
        int hashCode = networkClientType.hashCode();
        if(!networkClientMap.containsKey(hashCode)){
            networkClient.init(b, hostnameVerifier(), mDns, httpLogger);
            networkClientMap.put(hashCode, networkClient);
        }
        dnsRepository.addPrefetchHosts(b.prefetchHost);
        dnsRepository.init(); // 启动 dns 缓存
    }

    public void setNetworkClientType(Builder b){
        NetworkClient networkClient = b.networkClient;
        if(networkClient != null){
            String name = networkClient.getClass().getName();
            int hashCode = name.hashCode();
            if(!networkClientMap.containsKey(hashCode)){
                networkClient.init(b, hostnameVerifier(), mDns, httpLogger);
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
                throw new IllegalArgumentException("connection timeout must be larger than 10 seconds.");
            }
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setSocketTimeout(int socketTimeout) {
            if (socketTimeout < 3 * 1000) {
                throw new IllegalArgumentException("socket timeout must be larger than 10 seconds.");
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
