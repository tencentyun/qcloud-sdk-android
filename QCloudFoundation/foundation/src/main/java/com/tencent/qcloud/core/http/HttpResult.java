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

import com.tencent.qcloud.core.common.QCloudServiceException;

import java.util.List;
import java.util.Map;

public final class HttpResult<T> {

    private final int code;
    private final String message;
    private final Map<String, List<String>> headers;
    private final HttpRequest<T> request;
    private final long contentLength;

    private final T content;

    public HttpResult(HttpResponse<T> response, T content) {
        this.code = response.code();
        this.message = response.message();
        this.headers = response.response.headers().toMultimap();
        this.content = content;
        this.request = response.request;
        this.contentLength = response.contentLength();
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

    public long getContentLength() {
        return contentLength;
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
