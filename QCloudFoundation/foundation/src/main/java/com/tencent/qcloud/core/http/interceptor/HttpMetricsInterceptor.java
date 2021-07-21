package com.tencent.qcloud.core.http.interceptor;

import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.TaskManager;

import java.io.IOException;
import java.net.Socket;

import okhttp3.Connection;
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

        try {
            if (chain instanceof RealInterceptorChain) {
                Connection connection = chain.connection();
                if (connection instanceof RealConnection) {
                    RealConnection realConnection = (RealConnection) connection;
                    Socket socket = realConnection.socket();
                    HttpTask task = (HttpTask) TaskManager.getInstance().get((String) request.tag());
                    HttpTaskMetrics metrics = task.metrics();
                    if (metrics != null) {
                        metrics.recordConnectAddress(socket.getInetAddress());
                    }
                }
            }
        } catch (Exception e) {
            QCloudLogger.d("HttpMetricsInterceptor", e.getMessage());
        }

        return chain.proceed(request);
    }
}
