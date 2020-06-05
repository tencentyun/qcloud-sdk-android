package com.tencent.qcloud.core.http;


import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudCredentials;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.auth.ScopeLimitCredentialProvider;
import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudDigistListener;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.task.QCloudTask;
import com.tencent.qcloud.core.task.TaskExecutors;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import bolts.CancellationTokenSource;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by wjielai on 2017/11/27.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public final class HttpTask<T> extends QCloudTask<HttpResult<T>> {
    private static AtomicInteger increments = new AtomicInteger(1);

    protected final HttpRequest<T> httpRequest;
    protected final QCloudCredentialProvider credentialProvider;
    protected HttpResult<T> httpResult;
    protected HttpTaskMetrics metrics;

    private NetworkProxy<T> networkProxy;

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
        this.networkProxy = networkClient.getNetworkProxy();
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

    public boolean isUploadTask() {
        if (httpRequest.getRequestBody() instanceof StreamingRequestBody) {
            return ((StreamingRequestBody) httpRequest.getRequestBody()).isLargeData();
        }
        return false;
    }

    public boolean isDownloadTask() {
        return httpRequest.getResponseBodyConverter() instanceof ProgressBody;
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

    @Override
    public void cancel() {
        this.networkProxy.cancel();
        super.cancel();
    }

    @Override
    protected HttpResult<T> execute() throws QCloudClientException, QCloudServiceException {
        if (metrics == null) {
            metrics = new HttpTaskMetrics();
        }
        networkProxy.metrics = metrics;
        metrics.onTaskStart();

        // 准备请求，包括计算MD5和签名
        if (httpRequest.shouldCalculateContentMD5()) {
            metrics.onCalculateMD5Start();
            calculateContentMD5();
            metrics.onCalculateMD5End();
        }
        QCloudSigner signer = httpRequest.getQCloudSigner();
        if (signer != null) {
            metrics.onSignRequestStart();
            signRequest(signer, (QCloudHttpRequest) httpRequest);
            metrics.onSignRequestEnd();
        }
        if (httpRequest.getRequestBody() instanceof ReactiveBody){
            try {
                ((ReactiveBody) httpRequest.getRequestBody()).prepare();
            } catch (IOException e) {
                throw new QCloudClientException(e);
            }
        }
        if (httpRequest.getRequestBody() instanceof ProgressBody) {
            ((ProgressBody) httpRequest.getRequestBody()).setProgressListener(mProgressListener);
        }
        try {
            httpResult = networkProxy.executeHttpRequest(httpRequest);
            return httpResult;
        } catch (QCloudServiceException serviceException) {
            if (isClockSkewedError(serviceException)) {
                // re sign request
                if (signer != null) {
                    metrics.onSignRequestStart();
                    signRequest(signer, (QCloudHttpRequest) httpRequest);
                    metrics.onSignRequestEnd();
                }
                // try again
                httpResult = networkProxy.executeHttpRequest(httpRequest);
                return httpResult;
            } else {
                throw serviceException;
            }
        } finally {
            if (httpRequest.getRequestBody() instanceof ReactiveBody){
                try {
                    ((ReactiveBody) httpRequest.getRequestBody()).end(httpResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                throw new QCloudClientException("calculate md5 error", e);
            }
        }else {
            //请求 body 比较小，不会 OOM
            Buffer sink = new Buffer();
            try {
                requestBody.writeTo(sink);
            } catch (IOException e) {
                throw new QCloudClientException("calculate md5 error", e);
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
