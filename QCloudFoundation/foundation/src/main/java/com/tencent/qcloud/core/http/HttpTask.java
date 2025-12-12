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


import static com.tencent.qcloud.core.http.HttpConstants.Header.RANGE;

import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudCredentials;
import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.auth.ScopeLimitCredentialProvider;
import com.tencent.qcloud.core.common.DomainSwitchException;
import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudDigistListener;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.task.QCloudTask;
import com.tencent.qcloud.core.task.TaskExecutors;
import com.tencent.qcloud.core.util.DomainSwitchUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import bolts.CancellationTokenSource;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public final class HttpTask<T> extends QCloudTask<HttpResult<T>> {
    private static AtomicInteger increments = new AtomicInteger(1);

    protected final HttpRequest<T> httpRequest;
    protected final QCloudCredentialProvider credentialProvider;
    protected HttpResult<T> httpResult;
    protected HttpTaskMetrics metrics;

    private NetworkProxy<T> networkProxy;
    private boolean hasSwitchedDomain = false; // 标记是否已经切换过域名

    private QCloudProgressListener mProgressListener = new QCloudProgressListener() {
        @Override
        public void onProgress(long complete, long target) {
            HttpTask.this.onProgress(complete, target);
        }
    };

    HttpTask(HttpRequest<T> httpRequest, QCloudCredentialProvider credentialProvider,
             NetworkClient networkClient) {
        super("HttpTask-" + httpRequest.tag() + "-" + increments.getAndIncrement(), httpRequest.tag());
        this.httpRequest = httpRequest;
        this.credentialProvider = credentialProvider;
        this.networkProxy = networkClient.getNetworkProxyWrapper();
        this.networkProxy.identifier = this.getIdentifier();
        this.networkProxy.mProgressListener = mProgressListener;
    }

    public HttpTask<T> scheduleOn(Executor executor) {
        this.scheduleOn(executor, PRIORITY_NORMAL);
        return this;
    }

    public HttpTask<T> scheduleOn(Executor executor, int priority) {
        scheduleOn(executor, new CancellationTokenSource(), priority);
        return this;
    }

    public HttpTask<T> schedule() {
        schedule(PRIORITY_NORMAL);
        return this;
    }

    public HttpTask<T> schedule(int priority) {
        if (httpRequest.getRequestBody() instanceof ProgressBody) {
            this.scheduleOn(TaskExecutors.UPLOAD_EXECUTOR, priority);
        } else if (httpRequest.getResponseBodyConverter() instanceof ProgressBody) {
            this.scheduleOn(TaskExecutors.DOWNLOAD_EXECUTOR, priority);
        } else {
            this.scheduleOn(TaskExecutors.COMMAND_EXECUTOR, priority);
        }
        return this;
    }

    public boolean isSuccessful() {
        return httpResult != null && httpResult.isSuccessful();
    }

    @Override
    public HttpResult<T> getResult()  {
        return httpResult;
    }

    public HttpTask<T> attachMetric(HttpTaskMetrics httpMetric) {
        metrics = httpMetric;
        return this;
    }

    public HttpTaskMetrics metrics() {
        return metrics;
    }

    public boolean isUploadTask() {
        if (httpRequest.getRequestBody() instanceof StreamingRequestBody) {
            return ((StreamingRequestBody) httpRequest.getRequestBody()).isLargeData();
        }
        return false;
    }

    public boolean isDownloadTask() {
        return httpRequest.getResponseBodyConverter() instanceof ProgressBody;
    }

    public boolean isResponseFilePathConverter() {
        return httpRequest.getResponseBodyConverter() instanceof ResponseFileConverter
                && ((ResponseFileConverter)httpRequest.getResponseBodyConverter()).isFilePathConverter();
    }

    public HttpRequest<T> request() {
        return httpRequest;
    }

    public long getTransferBodySize() {
        ProgressBody body = null;

        if (httpRequest.getRequestBody() instanceof ProgressBody) {
            body = (ProgressBody)httpRequest.getRequestBody();
        } else if (httpRequest.getResponseBodyConverter() instanceof ProgressBody) {
            body = (ProgressBody) httpRequest.getResponseBodyConverter();
        }
        if (body != null) {
            return body.getBytesTransferred();
        }
        return 0;
    }

    public double getAverageStreamingSpeed(long networkMillsTook) {
        ProgressBody body = null;

        if (httpRequest.getRequestBody() instanceof ProgressBody) {
            body = (ProgressBody)httpRequest.getRequestBody();
        } else if (httpRequest.getResponseBodyConverter() instanceof ProgressBody) {
            body = (ProgressBody) httpRequest.getResponseBodyConverter();
        }
        if (body != null) {
            return ((double) body.getBytesTransferred() / 1024) / ((double) networkMillsTook / 1000);
        }
        return 0;
    }

    /**
     * 是否是自签名
     */
    public boolean isSelfSigner(){
        return httpRequest.getQCloudSelfSigner() != null;
    }

    /**
     * 是否已经切换过域名
     */
    public boolean hasSwitchedDomain() {
        return hasSwitchedDomain;
    }

    /**
     * 增加重试header重签名
     */
    public QCloudHttpRequest retrySignRequest() throws QCloudClientException {
        httpRequest.removeHeader(HttpConstants.Header.AUTHORIZATION);
        httpRequest.addOrReplaceHeader(HttpConstants.Header.COS_SDK_RETRY, String.valueOf(true));
        QCloudSigner signer = httpRequest.getQCloudSigner();
        if (signer != null) {
            metrics.onSignRequestStart();
            signRequest(signer, (QCloudHttpRequest) httpRequest);
            metrics.onSignRequestEnd();
        }
        QCloudSelfSigner selfSigner = httpRequest.getQCloudSelfSigner();
        if (selfSigner != null) {
            metrics.onSignRequestStart();
            selfSigner.sign((QCloudHttpRequest) httpRequest);
            metrics.onSignRequestEnd();
        }
        return (QCloudHttpRequest) httpRequest;
    }

    /**
     * 增加重试range header重签名
     */
    public QCloudHttpRequest newRangeSignRequest(String newRange) throws QCloudClientException {
        httpRequest.removeHeader(HttpConstants.Header.AUTHORIZATION);
        httpRequest.addOrReplaceHeader(RANGE, newRange);
        QCloudSigner signer = httpRequest.getQCloudSigner();
        if (signer != null) {
            metrics.onSignRequestStart();
            signRequest(signer, (QCloudHttpRequest) httpRequest);
            metrics.onSignRequestEnd();
        }
        QCloudSelfSigner selfSigner = httpRequest.getQCloudSelfSigner();
        if (selfSigner != null) {
            metrics.onSignRequestStart();
            selfSigner.sign((QCloudHttpRequest) httpRequest);
            metrics.onSignRequestEnd();
        }
        return (QCloudHttpRequest) httpRequest;
    }

    @Override
    public void cancel() {
        this.networkProxy.cancel();
        super.cancel();
    }

    /**
     * 取消任务
     * @param now 是否立即取消，true时会即可从taskManager中删除
     */
    @Override
    public void cancel(boolean now) {
        this.networkProxy.cancel();
        super.cancel(now);
    }

    private boolean isCompleteMultipartRequest(HttpRequest httpRequest) {
        Set<String> queryKeys = httpRequest.queries.keySet();
        return queryKeys != null && queryKeys.size() == 1 && queryKeys.contains("uploadId");
    }

    @Override
    protected HttpResult<T> execute() throws QCloudClientException, QCloudServiceException {
        if (metrics == null) {
            metrics = new HttpTaskMetrics();
        }
        networkProxy.metrics = metrics;
        metrics.onTaskStart();

//        if (isCompleteMultipartRequest(httpRequest)) {
//            QCloudServiceException serviceException = new QCloudServiceException("NoSuchUpload");
//            serviceException.setErrorCode("Request is expired");
//            throw serviceException;
//        }

        // 准备请求，包括计算MD5和签名
        if (httpRequest.shouldCalculateContentMD5()) {
            metrics.onCalculateMD5Start();
            calculateContentMD5();
            metrics.onCalculateMD5End();
        }
        if (httpRequest.getRequestBody() instanceof ReactiveBody){
            try {
                ((ReactiveBody) httpRequest.getRequestBody()).prepare();
            } catch (IOException e) {
                throw new QCloudClientException(e);
            }
        }
        QCloudSigner signer = httpRequest.getQCloudSigner();
        if (signer != null) {
            metrics.onSignRequestStart();
            signRequest(signer, (QCloudHttpRequest) httpRequest);
            metrics.onSignRequestEnd();
        }
        QCloudSelfSigner selfSigner = httpRequest.getQCloudSelfSigner();
        if (selfSigner != null) {
            metrics.onSignRequestStart();
            selfSigner.sign((QCloudHttpRequest) httpRequest);
            metrics.onSignRequestEnd();
        }
        
        if (httpRequest.getRequestBody() instanceof ProgressBody) {
            ((ProgressBody) httpRequest.getRequestBody()).setProgressListener(mProgressListener);
        }
        try {
            metrics.onHttpTaskStart();
            httpResult = networkProxy.executeHttpRequest(httpRequest);
            metrics.onHttpTaskEnd();
            return httpResult;
        } catch (QCloudServiceException serviceException) {
            if (isClockSkewedError(serviceException)) {
                metrics.setClockSkewedRetry(true);
                // re sign request
                if (signer != null) {
                    metrics.onSignRequestStart();
                    signRequest(signer, (QCloudHttpRequest) httpRequest);
                    metrics.onSignRequestEnd();
                }
                // try again
                metrics.onHttpTaskStart();
                httpResult = networkProxy.executeHttpRequest(httpRequest);
                metrics.onHttpTaskEnd();
                return httpResult;
            } else {
                metrics.onHttpTaskEnd();
                throw serviceException;
            }
        } catch (QCloudClientException clientException) {
            if (clientException.getCause() instanceof DomainSwitchException && isDomainSwitch() && (signer != null && selfSigner == null)) {
                // 标记已经切换过域名
                hasSwitchedDomain = true;
                // 切换域名
                String urlString = httpRequest.url.toString().replace(DomainSwitchUtils.DOMAIN_MYQCLOUD, DomainSwitchUtils.DOMAIN_TENCENTCOS);
                httpRequest.setUrl(urlString);
                try {
                    URL url = new URL(urlString);
                    httpRequest.addOrReplaceHeader(HttpConstants.Header.HOST, url.getHost());
                } catch (MalformedURLException ignored) {
                }
                // 添加重试header
                httpRequest.addOrReplaceHeader(HttpConstants.Header.COS_SDK_RETRY, String.valueOf(true));
                //重签名
                metrics.onSignRequestStart();
                signRequest(signer, (QCloudHttpRequest) httpRequest);
                metrics.onSignRequestEnd();
                // 重试
                metrics.onHttpTaskStart();
                httpResult = networkProxy.executeHttpRequest(httpRequest);
                metrics.onHttpTaskEnd();
                return httpResult;
            } else {
                metrics.onHttpTaskEnd();
                throw clientException;
            }
        } finally {
            if (httpRequest.getRequestBody() instanceof ReactiveBody){
                try {
                    ((ReactiveBody) httpRequest.getRequestBody()).end(httpResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpRequest.getRequestBody() instanceof StreamingRequestBody) {
                ((StreamingRequestBody) httpRequest.getRequestBody()).release();
            }

            metrics.onTaskEnd();
        }
    }

    private boolean isClockSkewedError(QCloudServiceException serviceException) {
        return QCloudServiceException.ERR0R_REQUEST_IS_EXPIRED.equals(serviceException.getErrorCode()) ||
                QCloudServiceException.ERR0R_REQUEST_TIME_TOO_SKEWED.equals(serviceException.getErrorCode());
    }

    private void signRequest(QCloudSigner signer, QCloudHttpRequest request) throws QCloudClientException {
        if (credentialProvider == null) {
            throw new QCloudClientException(new QCloudAuthenticationException("no credentials provider"));
        }

        QCloudCredentials credentials;
        // 根据 provider 类型判断是否需要传入 credential scope
        if (credentialProvider instanceof ScopeLimitCredentialProvider) {
            credentials = ((ScopeLimitCredentialProvider) credentialProvider).getCredentials(
                    request.getCredentialScope());
        } else {
            credentials = credentialProvider.getCredentials();
        }
        signer.sign(request, credentials);
    }
    
    

    private void calculateContentMD5() throws QCloudClientException {
        RequestBody requestBody = httpRequest.getRequestBody();
        if (requestBody == null) {
            throw new QCloudClientException(new IllegalArgumentException("get md5 canceled, request body is null."));
        }

        if(requestBody instanceof QCloudDigistListener){
            //请求 body 比较大，易触发 OOM
            try {
                if(httpRequest.getRequestBody() instanceof MultipartStreamRequestBody){
                    ((MultipartStreamRequestBody) httpRequest.getRequestBody()).addMd5();
                }else {
                    httpRequest.addHeader(HttpConstants.Header.CONTENT_MD5, ((QCloudDigistListener) requestBody).onGetMd5());
                }
            }catch (IOException e){
                throw new QCloudClientException("calculate md5 error: " + e.getMessage(), e);
            }
        }else {
            //请求 body 比较小，不会 OOM
            Buffer sink = new Buffer();
            try {
                requestBody.writeTo(sink);
            } catch (IOException e) {
                throw new QCloudClientException("calculate md5 error" + e.getMessage(), e);
            }

            String md5 = sink.md5().base64();

            httpRequest.addHeader(HttpConstants.Header.CONTENT_MD5, md5);
            sink.close();
        }
    }

    public void convertResponse(Response response) throws QCloudClientException, QCloudServiceException {
        httpResult = networkProxy.convertResponse(httpRequest, response);
    }

}
