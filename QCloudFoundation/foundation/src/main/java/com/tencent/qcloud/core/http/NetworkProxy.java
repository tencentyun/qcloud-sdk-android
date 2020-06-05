package com.tencent.qcloud.core.http;

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import okhttp3.Response;

public abstract class NetworkProxy<T> {

    protected HttpTaskMetrics metrics;

    protected String identifier;
    protected QCloudProgressListener mProgressListener;

    protected abstract void cancel();

    protected abstract HttpResult<T> executeHttpRequest(HttpRequest<T> httpRequest) throws QCloudClientException,
            QCloudServiceException;

    protected abstract HttpResult<T> convertResponse(HttpRequest<T> httpRequest, Response response) throws QCloudClientException,
            QCloudServiceException;
}
