package com.tencent.qcloud.core.http;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright (C), 2016-2019, tencent
 * Date: 2019/1/11 17:54
 * Author: bradyxiao
 */
public abstract class QCloudHttpRetryHandler {
    public abstract boolean shouldRetry(Request request, Response response, Exception exception);
    public static QCloudHttpRetryHandler DEFAULT = new QCloudHttpRetryHandler() {
        @Override
        public boolean shouldRetry(Request request, Response response, Exception exception) {
            return true;
        }
    };
}
