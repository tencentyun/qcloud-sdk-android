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
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.task.RetryStrategy;

import okhttp3.Dns;

/**
 * 开启一个线程池跑request
 */
public class QuicManager {

    private ConnectPool connectPool = new ConnectPool();
    protected RetryStrategy retryStrategy;
    protected HttpLogger httpLogger;
    protected Dns dns;

    public void init(boolean isEnableDebugLog, RetryStrategy retryStrategy, Dns dns, HttpLogger httpLogger){
        connectPool.init(isEnableDebugLog);
        QLog.isDebug = isEnableDebugLog;
        this.retryStrategy = retryStrategy;
        this.dns = dns;
        httpLogger.setTag(QCloudHttpClient.QUIC_LOG_TAG);
        httpLogger.setDebug(isEnableDebugLog);
        this.httpLogger = httpLogger;

    }


    public QuicImpl newQuicImpl(QuicRequest quicRequest){
        QuicImpl quic = new QuicImpl(quicRequest, connectPool);
        return quic;
    }

    public void destroy(){
        connectPool.destroy();
    }

}
