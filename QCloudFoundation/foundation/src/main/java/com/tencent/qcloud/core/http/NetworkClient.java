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

import com.tencent.qcloud.core.http.interceptor.QCloudRetryInterceptor;
import com.tencent.qcloud.core.http.interceptor.QCloudTrafficControlInterceptor;
import com.tencent.qcloud.core.task.RetryStrategy;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Dns;

public abstract class NetworkClient {

    protected RetryStrategy retryStrategy;
    protected HttpLogger httpLogger;
    protected boolean enableDebugLog;
    protected Dns dns;

    private QCloudRetryInterceptor retryInterceptor;

    /**
     * 开启业务拦截器：重试、流量控制
     */
    public void enableQCloudInterceptor() {
        retryInterceptor = new QCloudRetryInterceptor(retryStrategy, new QCloudTrafficControlInterceptor());
    }

    public void init(QCloudHttpClient.Builder b, HostnameVerifier hostnameVerifier,
                     Dns dns, HttpLogger httpLogger){
        this.retryStrategy = b.retryStrategy;
        this.httpLogger = httpLogger;
        this.enableDebugLog = b.enableDebugLog;
        this.dns = dns;
    }

    public abstract NetworkProxy getNetworkProxy();

    public NetworkProxy getNetworkProxyWrapper(){
        NetworkProxy networkProxy = getNetworkProxy();
        networkProxy.setRetryInterceptor(retryInterceptor);
        return networkProxy;
    }
}
