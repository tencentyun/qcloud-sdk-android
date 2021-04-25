package com.tencent.qcloud.core.http.interceptor;

import com.tencent.qcloud.core.http.CallMetricsListener;

import java.io.IOException;
import java.net.Socket;

import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.http.RealInterceptorChain;

/**
 * <p>
 * Created by rickenwang on 2021/4/25.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class HttpMetricsInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        if (chain instanceof RealInterceptorChain) {
            RealInterceptorChain realChain = (RealInterceptorChain) chain;
            Connection connection = chain.connection();
            if (connection instanceof RealConnection) {
                RealConnection realConnection = (RealConnection) connection;
                Socket socket = realConnection.socket();
                EventListener eventListener = realChain.eventListener();
                if (eventListener instanceof CallMetricsListener && socket != null) {
                    CallMetricsListener callMetricsListener = (CallMetricsListener) eventListener;
                    callMetricsListener.recordConnection(socket.getInetAddress());
                }
            }
        }

        return chain.proceed(request);
    }
}
