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

import com.tencent.qcloud.core.http.HttpLogger;
import com.tencent.qcloud.core.http.NetworkClient;
import com.tencent.qcloud.core.http.NetworkProxy;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.tquic.impl.TnetConfig;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Dns;

public class QuicClientImpl extends NetworkClient {
    private QuicManager quicManager;
    private static TnetConfig quicConfig;

    @Override
    public void init(QCloudHttpClient.Builder b, HostnameVerifier hostnameVerifier,
                     Dns dns, HttpLogger httpLogger) {
        super.init(b, hostnameVerifier, dns, httpLogger);
        quicManager = new QuicManager();
        httpLogger.setTag(QCloudHttpClient.QUIC_LOG_TAG);
    }

    @Override
    public NetworkProxy getNetworkProxy() {
        QuicProxy quicProxy = new QuicProxy(quicManager, dns, httpLogger, retryStrategy);
        return quicProxy;
    }

    /**
     * 设置quic配置
     * @param quicConfig quic配置
     */
    public static void setTnetConfig(TnetConfig quicConfig) {
        QuicClientImpl.quicConfig = quicConfig;
    }

    /**
     * 获取quic配置
     * @return quic配置
     */
    public static TnetConfig getQuicConfig() {
        if(QuicClientImpl.quicConfig == null){
            QuicClientImpl.quicConfig = new TnetConfig.Builder().build();
        }
        return QuicClientImpl.quicConfig;
    }
}
