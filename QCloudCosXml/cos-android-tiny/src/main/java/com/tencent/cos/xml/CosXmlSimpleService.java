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

package com.tencent.cos.xml;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudResultListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpResult;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.http.OkHttpClientImpl;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.http.QCloudHttpRequest;
import com.tencent.qcloud.core.http.QCloudHttpRetryHandler;
import com.tencent.qcloud.core.parser.ResponseFileBodySerializer;
import com.tencent.qcloud.core.parser.ResponseXmlS3BodySerializer;
import com.tencent.qcloud.core.task.RetryStrategy;
import com.tencent.qcloud.core.task.TaskExecutors;
import com.tencent.qcloud.core.util.ContextHolder;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;


/**
 * 提供用于访问Tencent Cloud COS服务的简单服务类。
 * <br>
 * 在调用 COS 服务前，都必须初始化一个该类的对象。
 * <p>
 * 更详细的使用方式请参考：<a href="https://cloud.tencent.com/document/product/436/12159#.E5.88.9D.E5.A7.8B.E5.8C.96.E6.9C.8D.E5.8A.A1">入门文档</a>
 */
public class CosXmlSimpleService implements SimpleCosXml {
    private static final String TAG = "CosXmlSimpleService";

    protected static volatile QCloudHttpClient client;
    protected QCloudCredentialProvider credentialProvider;
    protected String tag = "CosXml";
    protected String signerType = "CosXmlSigner";
    protected CosXmlServiceConfig config;
    public static String appCachePath;      // 用于缓存临时文件

    private String requestDomain;

    /**
     * cos android SDK 服务
     *
     * @param context                  Application 上下文{@link android.app.Application}
     * @param configuration            cos android SDK 服务配置{@link CosXmlServiceConfig}
     * @param qCloudCredentialProvider cos android SDK 证书提供者 {@link QCloudCredentialProvider}
     */
    public CosXmlSimpleService(Context context, CosXmlServiceConfig configuration,
                               QCloudCredentialProvider qCloudCredentialProvider) {
        this(context, configuration);
        credentialProvider = qCloudCredentialProvider;
    }

   

    /**
     * cos android SDK 服务
     *
     * @param context       Application 上下文{@link android.app.Application}
     * @param configuration cos android SDK 服务配置{@link CosXmlServiceConfig}
     */
    public CosXmlSimpleService(Context context, CosXmlServiceConfig configuration) {
        appCachePath = context.getApplicationContext().getFilesDir().getPath();
        if (client == null) {
            synchronized (CosXmlSimpleService.class) {
                if (client == null) {
                    QCloudHttpClient.Builder builder = new QCloudHttpClient.Builder();
                    init(builder, configuration);
                    client = builder.build();
                }
            }
        }
        config = configuration;
        client.addVerifiedHost("*." + configuration.getEndpointSuffix());
        client.addVerifiedHost("*." + configuration.getEndpointSuffix(
                configuration.getRegion(), true));
        ContextHolder.setContext(context);
    }
    
    private void init(QCloudHttpClient.Builder builder, CosXmlServiceConfig configuration){
        builder.setConnectionTimeout(configuration.getConnectionTimeout())
                .setSocketTimeout(configuration.getSocketTimeout());
        RetryStrategy retryStrategy = configuration.getRetryStrategy();
        if (retryStrategy != null) {
            builder.setRetryStrategy(retryStrategy);
        }
        QCloudHttpRetryHandler qCloudHttpRetryHandler = configuration.getQCloudHttpRetryHandler();
        if(qCloudHttpRetryHandler != null){
            builder.setQCloudHttpRetryHandler(qCloudHttpRetryHandler);
        }
        builder.enableDebugLog(configuration.isDebuggable());
        builder.setNetworkClient(new OkHttpClientImpl());
    }


    /**
     * 自定义 DNS 解析
     *
     * @param domainName dns 解析的 domain
     * @param ipList 解析的 ip 地址列表
     * @throws CosXmlClientException 客户端异常
     */
    public void addCustomerDNS(String domainName, String[] ipList) throws CosXmlClientException {
        try {
            client.addDnsRecord(domainName, ipList);
        } catch (UnknownHostException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK, e);
        }
    }

    private CosXmlClientException convertClientException(QCloudClientException clientException) {
        return new CosXmlClientException(ClientErrorCode.UNKNOWN, clientException);
    }

//    private CosXmlClientException convertClientException(QCloudClientException clientException) {
//        CosXmlClientException xmlClientException;
//        if (clientException instanceof CosXmlClientException) {
//            xmlClientException = (CosXmlClientException) clientException;
//            if (clientException.getCause() instanceof IOException) {
//                xmlClientException = new CosXmlClientException(subdivisionIOException(clientException.getCause()), clientException);
//            }
//        } else {
//            Throwable causeException = clientException.getCause();
//            if (causeException instanceof IllegalArgumentException) {
//                xmlClientException = new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.code, clientException);
//            } else if (causeException instanceof QCloudAuthenticationException) {
//                xmlClientException = new CosXmlClientException(ClientErrorCode.INVALID_CREDENTIALS.code, clientException);
//            } else if (causeException instanceof IOException) {
//                xmlClientException = new CosXmlClientException(subdivisionIOException(causeException), clientException);
//            } else {
//                xmlClientException = new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.code, clientException);
//            }
//        }
//        return xmlClientException;
//    }
//
//    private int subdivisionIOException(Throwable causeException) {
//        //细分其他IO异常
//        if (causeException instanceof FileNotFoundException) {
//            return ClientErrorCode.SINK_SOURCE_NOT_FOUND.code;
//        } else if (causeException instanceof UnknownHostException) {
//            return PoorNetworkCode.UnknownHostException;
//        } else if (causeException instanceof SocketTimeoutException) {
//            return PoorNetworkCode.SocketTimeoutException;
//        } else if (causeException instanceof ConnectException) {
//            return PoorNetworkCode.ConnectException;
//        } else if (causeException instanceof HttpRetryException) {
//            return PoorNetworkCode.HttpRetryException;
//        } else if (causeException instanceof NoRouteToHostException) {
//            return PoorNetworkCode.NoRouteToHostException;
//        } else if (causeException instanceof SSLHandshakeException && !(causeException.getCause() instanceof CertificateException)) {
//            return PoorNetworkCode.SSLHandshakeException;
//        } else {
//            return ClientErrorCode.IO_ERROR.code;
//        }
//    }
//
//    private static class PoorNetworkCode {
//        private static final int UnknownHostException = 200032;
//        private static final int SocketTimeoutException = 200033;
//        private static final int ConnectException = 200034;
//        private static final int HttpRetryException = 200035;
//        private static final int NoRouteToHostException = 200036;
//        private static final int SSLHandshakeException = 200037;
//    }

    @Deprecated
    public void addVerifiedHost(String hostName) {
        client.addVerifiedHost(hostName);
    }

    /**
     * 设置除 get service 请求外其他所有请求的域名
     *
     * @param domain 域名
     */
    public void setDomain(String domain) {
        this.requestDomain = domain;
    }

    protected String getRequestHost(CosXmlRequest request) throws CosXmlClientException {
        if (!TextUtils.isEmpty(requestDomain)) {
            return requestDomain;
        }
        return request.getRequestHost(config);
    }


    /**
     * 构建请求
     *
     * Header 数据优先级：request、config、默认
     */
    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> QCloudHttpRequest buildHttpRequest
    (T1 cosXmlRequest, T2 cosXmlResult) throws CosXmlClientException {

        QCloudHttpRequest.Builder<T2> httpRequestBuilder = new QCloudHttpRequest.Builder<T2>()
                .method(cosXmlRequest.getMethod())
                .userAgent(config.getUserAgent())
                .tag(tag);

        httpRequestBuilder.addNoSignHeaderKeys(config.getNoSignHeaders());
        httpRequestBuilder.addNoSignHeaderKeys(cosXmlRequest.getNoSignHeaders());
        //add url
        String requestURL = cosXmlRequest.getRequestURL();
        final String host = getRequestHost(cosXmlRequest);
        if (requestURL != null) {
            try {
                httpRequestBuilder.url(new URL(requestURL));
            } catch (MalformedURLException e) {
                throw new CosXmlClientException(ClientErrorCode.BAD_REQUEST, e);
            }
        } else {
            cosXmlRequest.checkParameters();
            httpRequestBuilder.scheme(config.getProtocol())
                    .host(host)
                    .path(cosXmlRequest.getPath(config));
                    // .addHeader(HttpConstants.Header.HOST, hostHeader);
            if(config.getPort() != -1)httpRequestBuilder.port(config.getPort());
            httpRequestBuilder.query(cosXmlRequest.getQueryString());
            if (cosXmlRequest.getQueryEncodedString() != null) {
                httpRequestBuilder.encodedQuery(cosXmlRequest.getQueryEncodedString());
            }
        }

        // 添加 header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.addAll(config.getCommonHeaders().keySet()); // 添加公共 header
        headerKeys.addAll(cosXmlRequest.getRequestHeaders().keySet()); // 添加 request header

        Map<String, List<String>> extraHeaders = new HashMap<>();
        for (String headerKey : headerKeys) {
            List<String> headerValue = cosXmlRequest.getRequestHeaders().get(headerKey);
            if (headerValue == null) {
                headerValue = config.getCommonHeaders().get(headerKey);
            }
            if (headerValue != null) {
                extraHeaders.put(headerKey, headerValue);
            }
        }

        if (!extraHeaders.containsKey(HttpConstants.Header.HOST)) {
            List<String> hostHeaderValue = new LinkedList<>();
            hostHeaderValue.add(host);
            extraHeaders.put(HttpConstants.Header.HOST, hostHeaderValue);
        }

        httpRequestBuilder.addHeaders(extraHeaders);

        if (cosXmlRequest.isNeedMD5) {
            httpRequestBuilder.contentMD5();
        }


        // add sign
        if (credentialProvider == null) {
            httpRequestBuilder.signer(null, null);
        } else {
            httpRequestBuilder.signer(signerType, cosXmlRequest.getSignSourceProvider());
        }

        if (cosXmlRequest.getRequestBody() != null) {
            httpRequestBuilder.body(cosXmlRequest.getRequestBody());
        }

        if (cosXmlRequest instanceof GetObjectRequest) {
            GetObjectRequest getObjectRequest = (GetObjectRequest) cosXmlRequest;
            if (!TextUtils.isEmpty(getObjectRequest.getDownloadPath())) {
                httpRequestBuilder.converter(new ResponseFileBodySerializer<T2>((GetObjectResult) cosXmlResult,
                        getObjectRequest.getDownloadPath(), getObjectRequest.fileOffset));
            } else if (getObjectRequest.fileContentUri != null) {
                httpRequestBuilder.converter(new ResponseFileBodySerializer<T2>((GetObjectResult) cosXmlResult,
                        getObjectRequest.fileContentUri,
                        ContextHolder.getAppContext().getContentResolver(),
                        getObjectRequest.fileOffset));
            }
        } else if (buildHttpRequestBodyConverter(cosXmlRequest, cosXmlResult, httpRequestBuilder)) {

        } else {
            httpRequestBuilder.converter(new ResponseXmlS3BodySerializer<T2>(cosXmlResult));
        }
        QCloudHttpRequest httpRequest = httpRequestBuilder.build();
        return httpRequest;
    }

    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> boolean buildHttpRequestBodyConverter(T1 cosXmlRequest, T2 cosXmlResult, QCloudHttpRequest.Builder<T2> httpRequestBuilder) {
        return false;
    }

    /**
     * 异步执行
     */
    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> void schedule(final T1 cosXmlRequest, T2 cosXmlResult,
                                                                                final CosXmlResultListener cosXmlResultListener) {
        QCloudResultListener<HttpResult<T2>> qCloudResultListener = new QCloudResultListener<HttpResult<T2>>() {
            @Override
            public void onSuccess(HttpResult<T2> result) {
                cosXmlResultListener.onSuccess(cosXmlRequest, result.content());
            }

            @Override
            public void onFailure(QCloudClientException clientException, QCloudServiceException serviceException) {
                if (clientException != null) {
                    CosXmlClientException xmlClientException = convertClientException(clientException);
                    cosXmlResultListener.onFail(cosXmlRequest, xmlClientException,null);
                } else if (serviceException != null) {
                    CosXmlServiceException xmlServiceException = new CosXmlServiceException(serviceException);
                    cosXmlResultListener.onFail(cosXmlRequest, null, xmlServiceException);
                }
            }
        };

        try {
            QCloudHttpRequest<T2> httpRequest = buildHttpRequest(cosXmlRequest, cosXmlResult);

            HttpTask<T2> httpTask;
//            if (cosXmlRequest instanceof PostObjectRequest) {
//                httpTask = client.resolveRequest(httpRequest, null);
//            } else {
//                httpTask = client.resolveRequest(httpRequest, credentialProvider);
//            }
            httpTask = client.resolveRequest(httpRequest, credentialProvider);
            
            httpTask.setTransferThreadControl(config.isTransferThreadControl());

            cosXmlRequest.setTask(httpTask);

            if (cosXmlRequest instanceof PutObjectRequest) {
                httpTask.addProgressListener(((PutObjectRequest) cosXmlRequest).progressListener);
            } else if (cosXmlRequest instanceof GetObjectRequest) {
                httpTask.addProgressListener(((GetObjectRequest) cosXmlRequest).progressListener);
            }

            Executor executor = config.getExecutor();
            Executor observeExecutor = config.getObserveExecutor();
            if (observeExecutor != null) {
                httpTask.observeOn(observeExecutor);
            }
            httpTask.addResultListener(qCloudResultListener);
            if(executor != null){
                httpTask.scheduleOn(executor);
            }else if(cosXmlRequest instanceof PutObjectRequest){
                httpTask.scheduleOn(TaskExecutors.UPLOAD_EXECUTOR, cosXmlRequest.getPriority());
            } else {
                httpTask.schedule();
            }

        } catch (QCloudClientException e) {
            CosXmlClientException clientException = convertClientException(e);
            cosXmlResultListener.onFail(cosXmlRequest, clientException,null);
        }
    }

    /**
     * <p>
     * 获取 COS 对象的异步方法.&nbsp;
     * <p>
     * 详细介绍，请查看:{@link  SimpleCosXml#getObjectAsync(GetObjectRequest, CosXmlResultListener)}
     * </p>
     */
    @Override
    public void getObjectAsync(GetObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectResult(), cosXmlResultListener);
    }


    /**
     * <p>
     * 简单上传的异步方法.&nbsp;
     * <p>
     * 详细介绍，请查看:{@link  SimpleCosXml#putObjectAsync(PutObjectRequest, CosXmlResultListener)}
     * </p>
     */
    @Override
    public void putObjectAsync(PutObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        PutObjectResult putObjectResult = new PutObjectResult();
        schedule(request, putObjectResult, cosXmlResultListener);
    }


    /**
     * 取消请求任务.&nbsp;
     * 详细介绍，请查看:{@link  SimpleCosXml#cancel(CosXmlRequest)}
     */
    @Override
    public void cancel(CosXmlRequest cosXmlRequest) {
        if (cosXmlRequest != null && cosXmlRequest.getHttpTask() != null) {
            cosXmlRequest.getHttpTask().cancel();
        }
    }

    /**
     * 取消所有的请求任务.&nbsp;
     * 详细介绍，请查看:{@link  SimpleCosXml#cancelAll()}
     */
    @Override
    public void cancelAll() {
        List<HttpTask> tasks = client.getTasksByTag(tag);
        for (HttpTask task : tasks) {
            task.cancel();
        }
    }

    /**
     * 释放所有的请求.&nbsp;
     * 详细介绍，请查看:{@link  SimpleCosXml#release()}
     */
    @Override
    public void release() {
        cancelAll();
    }
}
