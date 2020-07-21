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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Response;

public final class HttpResponse<T> {

    final HttpRequest<T> request;
    final Response response;

    public HttpResponse(HttpRequest<T> request, Response response) {
        this.request = request;
        this.response = response;
    }

    public HttpRequest<T> request() {
        return request;
    }

    public int code() {
        return response.code();
    }

    public String message() {
        return response.message();
    }

    public String header(String name) {
        return response.header(name);
    }

    public Map<String, List<String>> headers() {
        return response.headers().toMultimap();
    }

    public final long contentLength() {
        return response.body() == null ? 0 : response.body().contentLength();
    }

    public final InputStream byteStream() {
        return response.body() == null ? null : response.body().byteStream();
    }

    public final byte[] bytes() throws IOException {
        return response.body() == null ? null : response.body().bytes();
    }

    public final String string() throws IOException {
        return response.body() == null ? null : response.body().string();
    }

    public final boolean isSuccessful() {
        return response != null && response.isSuccessful();
    }

    public static void checkResponseSuccessful(HttpResponse response) throws QCloudServiceException {
        if (response == null) {
            throw new QCloudServiceException("response is null");
        }
        if (!response.isSuccessful()) {
            QCloudServiceException exception = new QCloudServiceException(response.message());
            exception.setStatusCode(response.code());
            throw exception;
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "http code = %d, http message = %s %nheader is %s",
                code(), message(), response.headers().toMultimap());
    }
}
