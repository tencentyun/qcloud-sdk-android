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

import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.util.QCloudStringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest<T> {

    protected final Request.Builder requestBuilder;
    protected final Map<String, List<String>> headers;
    protected final Map<String, String> queries;
    protected final Set<String> noSignHeaders;
    protected final RequestBody requestBody;
    protected final String method;
    protected final Object tag;
    protected final URL url;
    protected final ResponseBodyConverter<T> responseBodyConverter;

    protected final boolean calculateContentMD5;

    HttpRequest(Builder<T> builder) {
        requestBuilder = builder.requestBuilder;
        responseBodyConverter = builder.responseBodyConverter;
        headers = builder.headers;
        queries = builder.queries;
        noSignHeaders = builder.noSignHeaderKeys;
        method = builder.method;
        calculateContentMD5 = builder.calculateContentMD5;
        if (builder.tag == null) {
            tag = toString();
        } else {
            tag = builder.tag;
        }
        url = builder.httpUrlBuilder.build().url();

        if (builder.requestBodySerializer != null) {
            requestBody = builder.requestBodySerializer.body();
        } else {
            requestBody = null;
        }
        requestBuilder.method(builder.method, requestBody);
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public Set<String> getNoSignHeaders() {
        return noSignHeaders;
    }

    public Map<String, String> queries() {
        return queries;
    }

    public String header(String name) {
        List<String> values = headers.get(name);
        return values != null ? values.get(0) : null;
    }

    public Object tag() {
        return tag;
    }

    public void setOkHttpRequestTag(String tag) {
        requestBuilder.tag(tag);
    }

    public void addHeader(String name, String value) {
        List<String> values = headers.get(name);
        if (values == null || values.size() < 1) {
            requestBuilder.addHeader(name, value);
            addHeaderNameValue(headers, name, value);
        }
    }

    public void setUrl(String url){
        requestBuilder.url(url);
    }

    public void addQuery(String name, String value) {
        if (name != null) {
            queries.put(name, value);
        }
    }

    public void removeHeader(String name) {
        requestBuilder.removeHeader(name);
        headers.remove(name);
    }

    public boolean shouldCalculateContentMD5() {
        return calculateContentMD5 && QCloudStringUtils.isEmpty(header(HttpConstants.Header.CONTENT_MD5));
    }

    public String method() {
        return method;
    }

    public String host() {
        return url.getHost();
    }

    public String contentType() {
        if (requestBody == null) {
            return null;
        }

        MediaType mediaType = requestBody.contentType();
        return mediaType != null ? mediaType.toString() : null;
    }

    public long contentLength() throws IOException {
        if (requestBody == null) {
            return -1;
        }

        return requestBody.contentLength();
    }

    public URL url() {
        return url;
    }

    public ResponseBodyConverter<T> getResponseBodyConverter() {
        return responseBodyConverter;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public Request buildRealRequest() {
        return requestBuilder.build();
    }

    QCloudSigner getQCloudSigner() throws QCloudClientException {
        return null;
    }

    private static void addHeaderNameValue(Map<String, List<String>> headers, String name, String value) {
        List<String> values = headers.get(name);
        if (values == null) {
            values = new ArrayList<>(2);
            headers.put(name, values);
        }
        values.add(value.trim());
    }

    public static class Builder<T> {
        Object tag;

        String method;

        Request.Builder requestBuilder;
        HttpUrl.Builder httpUrlBuilder;

        Map<String, List<String>> headers = new HashMap<>(10);
        Map<String, String> queries = new HashMap<>(10);

        Set<String> noSignHeaderKeys = new HashSet<>(); // 不签名的 header

        RequestBodySerializer requestBodySerializer;
        ResponseBodyConverter<T> responseBodyConverter;

        boolean calculateContentMD5;

        boolean isCacheEnabled = true;

        public Builder() {
            httpUrlBuilder = new HttpUrl.Builder();
            requestBuilder = new Request.Builder();
        }

        public Builder<T> url(URL url) {
            HttpUrl httpUrl = HttpUrl.get(url);
            if (httpUrl == null) {
                throw new IllegalArgumentException("url is not legal : " + url);
            }
            httpUrlBuilder = httpUrl.newBuilder();
            return this;
        }

        public Builder<T> scheme(String scheme) {
            httpUrlBuilder.scheme(scheme);
            return this;
        }

        public Builder<T> tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder<T> host(String host) {
            httpUrlBuilder.host(host);
            return this;
        }

        public Builder<T> port(int port) {
            httpUrlBuilder.port(port);
            return this;
        }

        public Builder<T> path(String path) {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
//            if (path.endsWith("/")) {
//                path = path.substring(0, path.length() - 1);
//            }
            if (path.length() > 0) {
                httpUrlBuilder.addPathSegments(path);
            }
            return this;
        }

        public Builder<T> method(String method) {
            this.method = method;
            return this;
        }

        public Builder<T> query(String key, String value) {
            if (key != null) {
                queries.put(key, value);
                httpUrlBuilder.addQueryParameter(key, value);
            }
            return this;
        }

        public Builder<T> encodedQuery(String key, String value) {
            if (key != null) {
                queries.put(key, value);
                httpUrlBuilder.addEncodedQueryParameter(key, value);
            }
            return this;
        }

        public Builder<T> encodedQuery(String queryString) {
            httpUrlBuilder.encodedQuery(queryString);
            return this;
        }

        public Builder<T> query(Map<String, String> nameValues) {
            if (nameValues != null) {
                for (Map.Entry<String, String> entry : nameValues.entrySet()) {
                    String name = entry.getKey();
                    if (name != null) {
                        queries.put(name, entry.getValue());
                        httpUrlBuilder.addQueryParameter(name, entry.getValue());
                    }
                }
            }
            return this;
        }

        public Builder<T> encodedQuery(Map<String, String> nameValues) {
            if (nameValues != null) {
                for (Map.Entry<String, String> entry : nameValues.entrySet()) {
                    String name = entry.getKey();
                    if (name != null) {
                        queries.put(name, entry.getValue());
                        httpUrlBuilder.addEncodedQueryParameter(name, entry.getValue());
                    }
                }
            }
            return this;
        }

        public Builder<T> contentMD5() {
            calculateContentMD5 = true;
            return this;
        }

        public Builder<T> addHeader(String name, String value) {
            if (name != null && value != null) {
                requestBuilder.addHeader(name, value);
                addHeaderNameValue(headers, name, value);
            }
            return this;
        }

        public Builder<T> addHeaders(Map<String, List<String>> headers) {
            if (headers != null) {
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    String name = entry.getKey();
                    for (String value : entry.getValue()) {
                        if (name != null && value != null) {
                            requestBuilder.addHeader(name, value);
                            addHeaderNameValue(this.headers, name, value);
                        }
                    }
                }
            }
            return this;
        }


        public Builder<T> removeHeader(String name) {
            requestBuilder.removeHeader(name);
            headers.remove(name);
            return this;
        }


        public Builder<T> addNoSignHeaderKeys(List<String> keys) {
            noSignHeaderKeys.addAll(keys);
            return this;
        }

        public Set<String> getNoSignHeaderKeys() {
            return noSignHeaderKeys;
        }

        public Builder<T> userAgent(String userAgent) {
            requestBuilder.addHeader(HttpConstants.Header.USER_AGENT, userAgent);
            addHeaderNameValue(this.headers, HttpConstants.Header.USER_AGENT, userAgent);
            return this;
        }

        public Builder<T> setUseCache(boolean cacheEnabled) {
            isCacheEnabled = cacheEnabled;
            return this;
        }

        public Builder<T> body(RequestBodySerializer bodySerializer) {
            requestBodySerializer = bodySerializer;
            return this;
        }

        public Builder<T> converter(ResponseBodyConverter<T> responseBodyConverter) {
            this.responseBodyConverter = responseBodyConverter;
            return this;
        }

        protected void prepareBuild() {
            requestBuilder.url(httpUrlBuilder.build());
            if (!isCacheEnabled) {
                requestBuilder.cacheControl(CacheControl.FORCE_NETWORK);
            }
            if (responseBodyConverter == null) {
                // default use string converter
                responseBodyConverter = (ResponseBodyConverter<T>) ResponseBodyConverter.string();
            }
        }

        public HttpRequest<T> build() {
            prepareBuild();
            return new HttpRequest<>(this);
        }
    }
}
