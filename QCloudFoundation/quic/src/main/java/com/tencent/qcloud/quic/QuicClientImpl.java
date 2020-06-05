package com.tencent.qcloud.quic;

import com.tencent.qcloud.core.http.HttpLogger;
import com.tencent.qcloud.core.http.NetworkClient;
import com.tencent.qcloud.core.http.NetworkProxy;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import okhttp3.Dns;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public class QuicClientImpl extends NetworkClient {

    QuicManager quicManager;

    @Override
    public void init(QCloudHttpClient.Builder b, HostnameVerifier hostnameVerifier,
                     Dns dns, HttpLogger httpLogger) {
        super.init(b, hostnameVerifier, dns, httpLogger);
        quicManager = new QuicManager();
        quicManager.init(enableDebugLog, retryStrategy, dns, httpLogger);
    }

    @Override
    public NetworkProxy getNetworkProxy() {
        QuicProxy quicProxy = new QuicProxy(quicManager);
        return quicProxy;
    }
}
