package com.tencent.qcloud.quic;

import com.tencent.qcloud.core.http.HttpLogger;
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
