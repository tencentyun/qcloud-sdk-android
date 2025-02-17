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

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.interceptor.QCloudRetryInterceptor;
import com.tencent.qcloud.core.util.OkhttpInternalUtils;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public abstract class NetworkProxy<T> {
    protected HttpTaskMetrics metrics;

    protected String identifier;
    protected QCloudProgressListener mProgressListener;

    private QCloudRetryInterceptor mRetryInterceptor;
    public void setRetryInterceptor(QCloudRetryInterceptor qCloudRetryInterceptor) {
        this.mRetryInterceptor = qCloudRetryInterceptor;
    }

    protected abstract void cancel();

    protected HttpResult<T> executeHttpRequest(HttpRequest<T> httpRequest) throws QCloudClientException,
            QCloudServiceException{
        QCloudClientException clientException = null;
        QCloudServiceException serviceException = null;
        Response response = null;
        HttpResult<T> httpResult = null;
        boolean selfCloseConverter = httpRequest.getResponseBodyConverter() instanceof SelfCloseConverter;
        try {
            httpRequest.setOkHttpRequestTag(identifier);
            Request okHttpRequest = httpRequest.buildRealRequest();

            if(mRetryInterceptor != null){
                response = mRetryInterceptor.intercept(this, okHttpRequest);
            } else {
                response = callHttpRequest(okHttpRequest);
            }

            if (response != null) {
                httpResult = convertResponse(httpRequest, response);
            } else {
                serviceException = new QCloudServiceException("http response is null");
            }
        } catch (IOException e) {
            if (e.getCause() instanceof QCloudClientException) {
                clientException = (QCloudClientException) e.getCause();
            } else if (e.getCause() instanceof QCloudServiceException) {
                serviceException = (QCloudServiceException) e.getCause();
            } else {
                clientException = new QCloudClientException(e);
            }
        } finally {
            if(response != null && !selfCloseConverter) {
                OkhttpInternalUtils.closeQuietly(response);
            }
            try {
                disconnect();
            } catch (Exception ignore) {}
        }

        if (clientException != null) {
            throw clientException;
        } else if (serviceException != null) {
            throw serviceException;
        } else {
            return httpResult;
        }
    }

    protected HttpResult<T> convertResponse(HttpRequest<T> request, Response response) throws QCloudClientException,
            QCloudServiceException{
        HttpResponse<T> httpResponse = new HttpResponse<>(request, response);
        ResponseBodyConverter<T> converter = request.getResponseBodyConverter();
        if (converter instanceof ProgressBody) {
            ((ProgressBody) converter).setProgressListener(mProgressListener);
        }
        T content = converter.convert(httpResponse);
        return new HttpResult<T>(httpResponse, content);
    }

    // TODO: 2025/1/6 去除okhttp的Response和Request类型依赖

    /**
     * 发送网络请求
     * @param okHttpRequest 请求
     * @return 响应
     * @throws IOException iO异常
     */
    public abstract Response callHttpRequest(Request okHttpRequest) throws IOException;

    /**
     * 断开网络连接(有必要的话可以重写此方法)
     */
    protected void disconnect(){}
}
