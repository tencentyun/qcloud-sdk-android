package com.tencent.qcloud.core.http;

import com.tencent.qcloud.core.common.QCloudServiceException;

import java.util.List;
import java.util.Map;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public final class HttpResult<T> {

    private final int code;
    private final String message;
    private final Map<String, List<String>> headers;
    private final HttpRequest<T> request;

    private final T content;

    public HttpResult(HttpResponse<T> response, T content) {
        this.code = response.code();
        this.message = response.message();
        this.headers = response.response.headers().toMultimap();
        this.content = content;
        this.request = response.request;
    }

    public T content() {
        return content;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public HttpRequest<T> request() {
        return request;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public final boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public QCloudServiceException asException() {
        QCloudServiceException exception = new QCloudServiceException(message);
        exception.setStatusCode(code);
        return exception;
    }

    public String header(String name) {
        List<String> values = headers.get(name);
        if (values != null && values.size() > 0) {
            return values.get(0);
        }

        return null;
    }
}
