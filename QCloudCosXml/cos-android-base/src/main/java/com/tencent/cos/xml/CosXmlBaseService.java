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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.VersionInfo;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.BasePutObjectRequest;
import com.tencent.cos.xml.model.object.BasePutObjectResult;
import com.tencent.cos.xml.model.object.GetObjectBytesRequest;
import com.tencent.cos.xml.model.object.GetObjectBytesResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.SaveLocalRequest;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.cos.xml.model.object.UploadRequest;
import com.tencent.cos.xml.transfer.ResponseBytesConverter;
import com.tencent.cos.xml.transfer.ResponseFileBodySerializer;
import com.tencent.cos.xml.transfer.ResponseXmlS3BodySerializer;
import com.tencent.cos.xml.utils.StringUtils;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudCredentials;
import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.auth.ScopeLimitCredentialProvider;
import com.tencent.qcloud.core.auth.SignerFactory;
import com.tencent.qcloud.core.auth.StaticCredentialProvider;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudResultListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpResult;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.http.NetworkClient;
import com.tencent.qcloud.core.http.OkHttpClientImpl;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.http.QCloudHttpRequest;
import com.tencent.qcloud.core.http.QCloudHttpRetryHandler;
import com.tencent.qcloud.core.logger.COSLogger;
import com.tencent.qcloud.core.task.QCloudTask;
import com.tencent.qcloud.core.task.RetryStrategy;
import com.tencent.qcloud.core.task.TaskExecutors;
import com.tencent.qcloud.core.util.ContextHolder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;

import okhttp3.HttpUrl;


/**
 * 提供用于访问Tencent Cloud COS服务的基础服务类。
 * <br>
 * 在调用 COS 服务前，都必须初始化一个该类的对象。
 * <p>
 * 更详细的使用方式请参考：<a href="https://cloud.tencent.com/document/product/436/12159#.E5.88.9D.E5.A7.8B.E5.8C.96.E6.9C.8D.E5.8A.A1">入门文档</a>
 */
public class CosXmlBaseService implements BaseCosXml {
    private static final String TAG = "CosXmlBaseService";

    /**
     * 是否关闭上报
     */
    public static boolean IS_CLOSE_REPORT;
    /**
     * 桥接来源
     */
    public static String BRIDGE;

    protected volatile QCloudHttpClient client;
    protected QCloudCredentialProvider credentialProvider;
    protected String tag = "CosXml";
    protected String signerType = "CosXmlSigner";
    protected CosXmlServiceConfig config;
    public static String appCachePath;      // 用于缓存临时文件

    protected String requestDomain;
    protected QCloudSelfSigner selfSigner;

    /**
     * cos android SDK 服务
     *
     * @param context                  Application 上下文{@link android.app.Application}
     * @param configuration            cos android SDK 服务配置{@link CosXmlServiceConfig}
     * @param qCloudCredentialProvider cos android SDK 证书提供者 {@link QCloudCredentialProvider}
     */
    public CosXmlBaseService(Context context, CosXmlServiceConfig configuration,
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
    public CosXmlBaseService(Context context, CosXmlServiceConfig configuration) {
        ContextHolder.setContext(context);

        // 为了保持之前configuration.isDebuggable()对日志的控制
        COSLogger.enableLogcat(configuration.isDebuggable());
        COSLogger.enableLogFile(configuration.isDebuggable());

        CosTrackService.init(context.getApplicationContext(), IS_CLOSE_REPORT, BRIDGE);

        appCachePath = context.getApplicationContext().getFilesDir().getPath();

        TaskExecutors.initExecutor(configuration.getUploadMaxThreadCount(), configuration.getDownloadMaxThreadCount());

        setNetworkClient(configuration);
    }

    public QCloudCredentialProvider getCredentialProvider() {
        return credentialProvider;
    }

    /**
     * cos android SDK 服务
     *
     * @param context       Application 上下文{@link android.app.Application}
     * @param configuration cos android SDK 服务配置 {@link CosXmlServiceConfig}
     * @param qCloudSigner  cos android SDK 签名提供者 {@link QCloudSigner}
     */
    public CosXmlBaseService(Context context, CosXmlServiceConfig configuration,
                             QCloudSigner qCloudSigner) {
        this(context, configuration);
        credentialProvider = new StaticCredentialProvider(null);
        signerType = "UserCosXmlSigner";
        SignerFactory.registerSigner(signerType, qCloudSigner);
    }

    /**
     * cos android SDK 服务
     *
     * @param context       Application 上下文{@link android.app.Application}
     * @param configuration cos android SDK 服务配置 {@link CosXmlServiceConfig}
     * @param selfSigner   cos android SDK 签名提供者 {@link QCloudSelfSigner}
     */
    public CosXmlBaseService(Context context, CosXmlServiceConfig configuration,
                             QCloudSelfSigner selfSigner) {
        this(context, configuration);
        this.selfSigner = selfSigner;
    }

    /**
     * 兼容某些request的signerType（比如SensitiveContentRecognitionRequest应该是cos 而不是ci）
     * @return 新的正确的signerType
     */
    protected String signerTypeCompat(String signerType, CosXmlRequest cosXmlRequest){
        return signerType;
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
        if(configuration.getCustomizeNetworkClient() != null){
            builder.setNetworkClient(configuration.getCustomizeNetworkClient());
        } else {
            if(configuration.isEnableQuic()){
                try {
                    Class clazz = Class.forName("com.tencent.qcloud.quic.QuicClientImpl");
                    builder.setNetworkClient((NetworkClient) clazz.newInstance());
                } catch (Exception e) {
                    IllegalStateException illegalStateException = new IllegalStateException(e.getMessage(), e);
                    CosTrackService.getInstance().reportError(TAG, illegalStateException);
                    throw illegalStateException;
                }
            }else {
                builder.setNetworkClient(new OkHttpClientImpl());
            }
        }
        builder.dnsCache(configuration.isDnsCache());
        builder.addPrefetchHost(configuration.getEndpointSuffix());
        builder.setVerifySSLEnable(configuration.isVerifySSLEnable());
        builder.setClientCertificate(configuration.getClientCertificateBytes(), configuration.getClientCertificatePassword());
        builder.setRedirectEnable(configuration.isRedirectEnable());
    }

    public void setNetworkClient(CosXmlServiceConfig configuration){

        QCloudHttpClient.Builder builder = new QCloudHttpClient.Builder();
        init(builder, configuration);
        client = builder.build();
        client.setNetworkClientType(builder);
        config = configuration;
        client.addVerifiedHost("*." + configuration.getEndpointSuffix());
        client.addVerifiedHost("*." + configuration.getEndpointSuffix(
                configuration.getRegion(), true));
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
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    /**
     * 添加自定义 DNS 解析器
     * 支持添加多个，会顺序解析获取
     * @param dnsFetch DNS 解析器
     */
    public void addCustomerDNSFetch(@NonNull QCloudHttpClient.QCloudDnsFetch dnsFetch){
        client.addDnsFetch(dnsFetch);
    }

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
        if (!TextUtils.isEmpty(request.getHost())) {
            return request.getHost();
        }

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
    (T1 cosXmlRequest, T2 cosXmlResult, boolean isNetworkSwitch, String networkClientType) throws CosXmlClientException {
        QCloudHttpRequest.Builder<T2> httpRequestBuilder = new QCloudHttpRequest.Builder<T2>()
                .method(cosXmlRequest.getMethod())
                .userAgent(getUserAgent(networkClientType))
                .tag(tag);

        httpRequestBuilder.addNoSignHeaderKeys(config.getNoSignHeaders());
        httpRequestBuilder.addNoSignHeaderKeys(cosXmlRequest.getNoSignHeaders());
        httpRequestBuilder.addNoSignParamKeys(cosXmlRequest.getNoSignParams());

        //add url
        String requestURL = cosXmlRequest.getRequestURL();
        String host = getRequestHost(cosXmlRequest);
        if(TextUtils.isEmpty(cosXmlRequest.getHost())){
            if(config.networkSwitchStrategy() == CosXmlServiceConfig.RequestNetworkStrategy.Aggressive){
                if(isNetworkSwitch){
                    host = config.getDefaultRequestHost(cosXmlRequest.getRegion(), cosXmlRequest.getBucket());
                }
            } else if(config.networkSwitchStrategy() == CosXmlServiceConfig.RequestNetworkStrategy.Conservative){
                if(!isNetworkSwitch){
                    host = config.getDefaultRequestHost(cosXmlRequest.getRegion(), cosXmlRequest.getBucket());
                }
            }
        }
        if (requestURL != null) {
            try {
                httpRequestBuilder.url(new URL(requestURL));
            } catch (MalformedURLException e) {
                throw new CosXmlClientException(ClientErrorCode.BAD_REQUEST.getCode(), e);
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

        // CopyObjectRequest 请求添加 x-cos-copy-source Header
        setCopySource(cosXmlRequest);

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

        if(cosXmlRequest.headersHasUnsafeNonAscii()){
            httpRequestBuilder.addHeadersUnsafeNonAscii(extraHeaders);
        } else {
            httpRequestBuilder.addHeaders(extraHeaders);
        }
        if (cosXmlRequest.isNeedMD5()) {
            httpRequestBuilder.contentMD5();
        }

        httpRequestBuilder.setKeyTime(cosXmlRequest.getKeyTime());


        // add sign
        if (credentialProvider == null) {
            if(cosXmlRequest.getCredentialProvider() != null) {
                httpRequestBuilder.signer(signerTypeCompat(signerType, cosXmlRequest), cosXmlRequest.getSignSourceProvider());
            } else {
                httpRequestBuilder.signer(null, null);
            }
        } else {
            httpRequestBuilder.signer(signerTypeCompat(signerType, cosXmlRequest), cosXmlRequest.getSignSourceProvider());
        }
        
        if (selfSigner != null) {
            httpRequestBuilder.selfSigner(selfSigner);
        }
        
        
        // add credential scope
        httpRequestBuilder.credentialScope(cosXmlRequest.getSTSCredentialScope(config));

        if (cosXmlRequest.getRequestBody() != null) {
            httpRequestBuilder.body(cosXmlRequest.getRequestBody());
        }

        if (cosXmlRequest instanceof SaveLocalRequest) {
            SaveLocalRequest saveLocalRequest = (SaveLocalRequest) cosXmlRequest;
            if (!TextUtils.isEmpty(saveLocalRequest.getSaveLocalPath())) {
                httpRequestBuilder.converter(new ResponseFileBodySerializer<T2>((GetObjectResult) cosXmlResult,
                        saveLocalRequest.getSaveLocalPath(), saveLocalRequest.getSaveLocalOffset()));
            } else if (saveLocalRequest.getSaveLocalUri() != null) {
                httpRequestBuilder.converter(new ResponseFileBodySerializer<T2>((GetObjectResult) cosXmlResult,
                        saveLocalRequest.getSaveLocalUri(),
                        ContextHolder.getAppContext().getContentResolver(),
                        saveLocalRequest.getSaveLocalOffset()));
            }
        } else if (cosXmlRequest instanceof GetObjectBytesRequest) {
            httpRequestBuilder.converter(new ResponseBytesConverter<T2>((GetObjectBytesResult) cosXmlResult));
        } else if (buildHttpRequestBodyConverter(cosXmlRequest, cosXmlResult, httpRequestBuilder)) {

        } else {
            httpRequestBuilder.converter(new ResponseXmlS3BodySerializer<T2>(cosXmlResult));
        }
        httpRequestBuilder.signInUrl(cosXmlRequest.isSignInUrl() || config.isSignInUrl());
        QCloudHttpRequest httpRequest = httpRequestBuilder.build();
        return httpRequest;
    }

    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> boolean buildHttpRequestBodyConverter(T1 cosXmlRequest, T2 cosXmlResult, QCloudHttpRequest.Builder<T2> httpRequestBuilder) {
        return false;
    }

    private  <T1 extends CosXmlRequest, T2 extends CosXmlResult> HttpTask buildHttpTask
            (T1 cosXmlRequest, T2 cosXmlResult, boolean isNetworkSwitch) throws CosXmlClientException {
        // 如果有值 说明是task传入的 则以task为准
        if(TextUtils.isEmpty(cosXmlRequest.getClientTraceId())){
            cosXmlRequest.setClientTraceId(UUID.randomUUID().toString());
        }

        if(TextUtils.isEmpty(cosXmlRequest.getRegion()) && config != null){
            cosXmlRequest.setRegion(config.getRegion());
        }

        if (cosXmlRequest.getMetrics() == null) {
            cosXmlRequest.attachMetrics(new HttpTaskMetrics());
        }

        String networkClientType = null;
        if(config.getCustomizeNetworkClient() != null){
            networkClientType = config.getCustomizeNetworkClient().getClass().getName();
        } else {
            if(config.isEnableQuic()) {
                if(cosXmlRequest.getNetworkType() == null){
                    if(config.networkSwitchStrategy() == CosXmlServiceConfig.RequestNetworkStrategy.Aggressive){
                        if(isNetworkSwitch){
                            networkClientType = OkHttpClientImpl.class.getName();
                        } else {
                            networkClientType = "com.tencent.qcloud.quic.QuicClientImpl";
                        }
                    } else if(config.networkSwitchStrategy() == CosXmlServiceConfig.RequestNetworkStrategy.Conservative){
                        if(isNetworkSwitch){
                            networkClientType = "com.tencent.qcloud.quic.QuicClientImpl";
                        } else {
                            networkClientType = OkHttpClientImpl.class.getName();
                        }
                    }
                } else {
                    if(cosXmlRequest.getNetworkType() == CosXmlRequest.RequestNetworkType.OKHTTP){
                        networkClientType = OkHttpClientImpl.class.getName();
                    } else if(cosXmlRequest.getNetworkType() == CosXmlRequest.RequestNetworkType.QUIC){
                        networkClientType = "com.tencent.qcloud.quic.QuicClientImpl";
                    }
                }
            } else {
                networkClientType = OkHttpClientImpl.class.getName();
            }
        }

        QCloudHttpRequest<T2> httpRequest = buildHttpRequest(cosXmlRequest, cosXmlResult, isNetworkSwitch, networkClientType);
        HttpTask<T2> httpTask;

        // 单次临时密钥优先级比service中的密钥提供器高
        if(cosXmlRequest.getCredentialProvider() != null){
            httpTask = client.resolveRequest(httpRequest, cosXmlRequest.getCredentialProvider(), networkClientType);
        } else {
            httpTask = client.resolveRequest(httpRequest, credentialProvider, networkClientType);
        }

        httpTask.setTransferThreadControl(config.isTransferThreadControl());
        httpTask.setUploadMaxThreadCount(config.getUploadMaxThreadCount());
        httpTask.setDownloadMaxThreadCount(config.getDownloadMaxThreadCount());
        httpTask.setDomainSwitch(config.isDomainSwitch());
        cosXmlRequest.setTask(httpTask);

        return httpTask;
    }

    /**
     * 同步执行
     */
    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> T2 execute(T1 cosXmlRequest, T2 cosXmlResult)
            throws CosXmlClientException, CosXmlServiceException {
        return this.execute(cosXmlRequest, cosXmlResult, false);
    }
    /**
     * 同步执行
     */
    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> T2 execute(T1 cosXmlRequest, T2 cosXmlResult, boolean internal)
            throws CosXmlClientException, CosXmlServiceException {
        return this.execute(cosXmlRequest, cosXmlResult, internal, false);
    }

    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> T2 execute(T1 cosXmlRequest, T2 cosXmlResult,
                                                                             boolean internal, boolean isNetworkSwitch)
            throws CosXmlClientException, CosXmlServiceException {
        try {
            HttpTask<T2> httpTask = buildHttpTask(cosXmlRequest, cosXmlResult, isNetworkSwitch);
            setProgressListener(cosXmlRequest, httpTask, false);

            HttpResult<T2> httpResult = httpTask.executeNow();

            CosTrackService.getInstance().reportRequestSuccess(cosXmlRequest, internal, config.getTrackParams());
            CosTrackService.getInstance().reportHttpMetrics(cosXmlRequest);
            logRequestMetrics(cosXmlRequest);

            return httpResult != null ? httpResult.content() : null;
        } catch (QCloudServiceException e) {
            CosTrackService.getInstance().reportHttpMetrics(cosXmlRequest);
            CosXmlServiceException cosXmlServiceException = CosTrackService.getInstance().convertServerException(e);
            // 网络异常
            if(config.networkSwitchStrategy() != null && !isNetworkSwitch && ("RequestTimeout".equals(cosXmlServiceException.getErrorCode()) || "UserNetworkTooSlow".equals(cosXmlServiceException.getErrorCode()))){
                return this.execute(cosXmlRequest, cosXmlResult, internal, true);
            }
            throw CosTrackService.getInstance().reportRequestServiceException(cosXmlRequest, e, internal, config.getTrackParams());
        } catch (QCloudClientException e) {
            CosTrackService.getInstance().reportHttpMetrics(cosXmlRequest);
            CosXmlClientException cosXmlClientException = CosTrackService.getInstance().convertClientException(e);
            // 网络异常
            if(config.networkSwitchStrategy() != null && !isNetworkSwitch && (cosXmlClientException.errorCode == ClientErrorCode.POOR_NETWORK.getCode() ||
                    cosXmlClientException.errorCode == ClientErrorCode.IO_ERROR.getCode())){
                return this.execute(cosXmlRequest, cosXmlResult, internal, true);
            }
            throw CosTrackService.getInstance().reportRequestClientException(cosXmlRequest, e, internal, config.getTrackParams());
        }
    }

    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> void schedule(final T1 cosXmlRequest, T2 cosXmlResult,
                                                                                final CosXmlResultListener cosXmlResultListener) {
        this.schedule(cosXmlRequest, cosXmlResult, cosXmlResultListener, false);
    }
    /**
     * 异步执行
     */
    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> void schedule(final T1 cosXmlRequest, T2 cosXmlResult,
                                                                                final CosXmlResultListener cosXmlResultListener, boolean internal) {
        this.schedule(cosXmlRequest, cosXmlResult, cosXmlResultListener, internal, false);
    }

    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> void schedule(final T1 cosXmlRequest, T2 cosXmlResult,
                                                                                final CosXmlResultListener cosXmlResultListener,
                                                                                boolean internal, boolean isNetworkSwitch) {
        QCloudResultListener<HttpResult<T2>> qCloudResultListener = new QCloudResultListener<HttpResult<T2>>() {
            @Override
            public void onSuccess(HttpResult<T2> result) {
                CosTrackService.getInstance().reportRequestSuccess(cosXmlRequest, internal, config.getTrackParams());
                CosTrackService.getInstance().reportHttpMetrics(cosXmlRequest);
                logRequestMetrics(cosXmlRequest);
                cosXmlResultListener.onSuccess(cosXmlRequest, result.content());
            }

            @Override
            public void onFailure(QCloudClientException clientException, QCloudServiceException serviceException) {
                CosTrackService.getInstance().reportHttpMetrics(cosXmlRequest);
                logRequestMetrics(cosXmlRequest);
                if (clientException != null) {
                    CosXmlClientException xmlClientException = CosTrackService.getInstance().convertClientException(clientException);
                    // 网络异常
                    if(config.networkSwitchStrategy() != null && !isNetworkSwitch && (xmlClientException.errorCode == ClientErrorCode.POOR_NETWORK.getCode() ||
                            xmlClientException.errorCode == ClientErrorCode.IO_ERROR.getCode())){
                        schedule(cosXmlRequest, cosXmlResult, cosXmlResultListener, internal, true);
                    }
                    cosXmlResultListener.onFail(cosXmlRequest,
                            CosTrackService.getInstance().reportRequestClientException(cosXmlRequest, clientException, internal, config.getTrackParams()),
                            null);
                } else if (serviceException != null) {
                    CosXmlServiceException xmlServiceException = CosTrackService.getInstance().convertServerException(serviceException);
                    // 网络异常
                    if(config.networkSwitchStrategy() != null && !isNetworkSwitch && ("RequestTimeout".equals(xmlServiceException.getErrorCode()) || "UserNetworkTooSlow".equals(xmlServiceException.getErrorCode()))){
                        schedule(cosXmlRequest, cosXmlResult, cosXmlResultListener, internal, true);
                    }
                    cosXmlResultListener.onFail(cosXmlRequest, null,
                            CosTrackService.getInstance().reportRequestServiceException(cosXmlRequest, serviceException, internal, config.getTrackParams()));
                }
            }
        };

        try {
            HttpTask<T2> httpTask = buildHttpTask(cosXmlRequest, cosXmlResult, isNetworkSwitch);
            setProgressListener(cosXmlRequest, httpTask, true);

            Executor executor = config.getExecutor();
            Executor observeExecutor = config.getObserveExecutor();
            if (observeExecutor != null) {
                httpTask.observeOn(observeExecutor);
            }
            httpTask.addResultListener(qCloudResultListener);
            if(executor != null){
                httpTask.scheduleOn(executor);
            }else if(cosXmlRequest instanceof UploadRequest){
                httpTask.scheduleOn(TaskExecutors.UPLOAD_EXECUTOR, cosXmlRequest.getPriority());
            }
            else {
                httpTask.schedule();
            }

        } catch (QCloudClientException e) {
            CosXmlClientException clientException = CosTrackService.getInstance().convertClientException(e);
            // 网络异常
            if(config.networkSwitchStrategy() != null && !isNetworkSwitch && (clientException.errorCode == ClientErrorCode.POOR_NETWORK.getCode() ||
                    clientException.errorCode == ClientErrorCode.IO_ERROR.getCode())){
                schedule(cosXmlRequest, cosXmlResult, cosXmlResultListener, internal, true);
            }
            cosXmlResultListener.onFail(cosXmlRequest,
                    CosTrackService.getInstance().reportRequestClientException(cosXmlRequest, e, internal, config.getTrackParams()),null);
        }
    }

    protected <T1 extends CosXmlRequest> void setCopySource(T1 cosXmlRequest) throws CosXmlClientException{

    }

    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> void setProgressListener(
            final T1 cosXmlRequest, HttpTask<T2> httpTask, boolean isSchedule){
        if (cosXmlRequest instanceof BasePutObjectRequest) {
            httpTask.addProgressListener(((BasePutObjectRequest) cosXmlRequest).getProgressListener());
        } else if (cosXmlRequest instanceof UploadPartRequest) {
            httpTask.addProgressListener(((UploadPartRequest) cosXmlRequest).getProgressListener());
            if(isSchedule) {
                httpTask.setOnRequestWeightListener(new QCloudTask.OnRequestWeightListener() {
                    @Override
                    public int onWeight() {
                        return cosXmlRequest.getWeight();
                    }
                });
            }
        } else if (cosXmlRequest instanceof GetObjectRequest) {
            httpTask.addProgressListener(((GetObjectRequest) cosXmlRequest).getProgressListener());
        }
    }

    /**
     * 获取请求的访问地址
     *
     * @param cosXmlRequest 请求
     * @return String 访问地址
     */
    public String getAccessUrl(CosXmlRequest cosXmlRequest) {
        String requestURL = cosXmlRequest.getRequestURL();
        if (requestURL != null) {
            int index = requestURL.indexOf("?");
            return index > 0 ? requestURL.substring(0, index) : requestURL;
        }
        HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder();
        httpUrlBuilder.scheme(config.getProtocol());
        String host = null;
        try {
            // host = cosXmlRequest.getHost(config, false);
            host = getRequestHost(cosXmlRequest);
        } catch (CosXmlClientException e) {
            CosTrackService.getInstance().reportError(TAG, e);
            e.printStackTrace();
        }
        httpUrlBuilder.host(host);

        String path = cosXmlRequest.getPath(config);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.length() > 0) {
            httpUrlBuilder.addPathSegments(path);
        }
        return httpUrlBuilder.build().toString();
    }

    /**
     * 获取预签名文件URL
     *
     * @param cosXmlRequest 请求
     * @return String 签名 URL
     * @throws CosXmlClientException 客户端异常
     */
    public String getPresignedURL(CosXmlRequest cosXmlRequest) throws CosXmlClientException {
        try {
            //step1: obtain sign, contain token if it exist.
            // 根据 provider 类型判断是否需要传入 credential scope
            QCloudCredentials credentials;
            // 单次临时密钥优先级比service中的密钥提供器高
            if(cosXmlRequest.getCredentialProvider() != null){
                credentials = cosXmlRequest.getCredentialProvider().getCredentials();
            } else {
                if (credentialProvider instanceof ScopeLimitCredentialProvider) {
                    credentials = ((ScopeLimitCredentialProvider) credentialProvider).getCredentials(
                            cosXmlRequest.getSTSCredentialScope(config));
                } else {
                    credentials = credentialProvider.getCredentials();
                }
            }

            QCloudSigner signer = SignerFactory.getSigner(signerTypeCompat(signerType, cosXmlRequest));

            QCloudHttpRequest request = buildHttpRequest(cosXmlRequest, null, false, null);
            signer.sign(request, credentials);
            String sign = request.header(HttpConstants.Header.AUTHORIZATION);
            String token = request.header("x-cos-security-token");
            if(!TextUtils.isEmpty(token)){
                sign = sign + "&x-cos-security-token=" + token;
            }
            // step2: obtain host and path
            String url = getAccessUrl(cosXmlRequest);

            //step3: return url + ? + sign
            String query = StringUtils.flat(cosXmlRequest.getQueryString());
            return TextUtils.isEmpty(query) ? url + "?" + sign :
                    url + "?" + query + "&" + sign;
        } catch (QCloudClientException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_CREDENTIALS.getCode(), e);
        }
    }

    /**
     * <p>
     * 获取 COS 对象的同步方法.&nbsp;
     * <p>
     * 详细介绍，请查看:{@link  BaseCosXml#getObject(GetObjectRequest)}
     */
    @Override
    public GetObjectResult getObject(GetObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetObjectResult());
    }

    /**
     * <p>
     * 获取 COS 对象的异步方法.&nbsp;
     * <p>
     * 详细介绍，请查看:{@link  BaseCosXml#getObjectAsync(GetObjectRequest, CosXmlResultListener)}
     */
    @Override
    public void getObjectAsync(GetObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectResult(), cosXmlResultListener);
    }
    public void internalGetObjectAsync(GetObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectResult(), cosXmlResultListener, true);
    }

    /**
     * <p>
     * 将 COS 对象下载为字节数组
     * <p>
     * 详细介绍，请查看:{@link BaseCosXml#getObject(String, String)}
     */
    @Override
    public byte[] getObject(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException {

        GetObjectBytesRequest getObjectBytesRequest = new GetObjectBytesRequest(bucketName, objectName);
        GetObjectBytesResult getObjectBytesResult = execute(getObjectBytesRequest, new GetObjectBytesResult());
        return getObjectBytesResult != null ? getObjectBytesResult.data : new byte[0];
    }

    /**
     * <p>
     * 将 COS 对象下载为字节数组
     * <p>
     * 详细介绍，请查看:{@link BaseCosXml#getObject(GetObjectBytesRequest)}
     */
    @Override
    public byte[] getObject(GetObjectBytesRequest request) throws CosXmlClientException, CosXmlServiceException {
        GetObjectBytesResult getObjectBytesResult = execute(request, new GetObjectBytesResult());
        return getObjectBytesResult != null ? getObjectBytesResult.data : new byte[0];
    }

    /**
     * <p>
     * 基础简单上传的同步方法.&nbsp;
     * <p>
     * 详细介绍，请查看:{@link  BaseCosXml#basePutObject(BasePutObjectRequest)}
     */
    @Override
    public BasePutObjectResult basePutObject(BasePutObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        BasePutObjectResult basePutObjectResult = new BasePutObjectResult();
        basePutObjectResult.accessUrl = getAccessUrl(request);
        return execute(request, basePutObjectResult);
    }

    /**
     * <p>
     * 基础简单上传的异步方法.&nbsp;
     * <p>
     * 详细介绍，请查看:{@link  BaseCosXml#basePutObjectAsync(BasePutObjectRequest, CosXmlResultListener)}
     */
    @Override
    public void basePutObjectAsync(BasePutObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        BasePutObjectResult basePutObjectResult = new BasePutObjectResult();
        basePutObjectResult.accessUrl = getAccessUrl(request);
        schedule(request, basePutObjectResult, cosXmlResultListener);
    }

    /**
     * <p>
     * 上传一个对象某个分片块的同步方法.&nbsp;
     * <p>
     * 详细介绍，请查看:{@link  BaseCosXml#uploadPart(UploadPartRequest)}
     */
    @Override
    public UploadPartResult uploadPart(UploadPartRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UploadPartResult());
    }

    /**
     * <p>
     * 上传一个对象某个分片块的异步方法.&nbsp;
     * <p>
     * 详细介绍，请查看:{@link  BaseCosXml#uploadPartAsync(UploadPartRequest, CosXmlResultListener)}
     */
    @Override
    public void uploadPartAsync(UploadPartRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UploadPartResult(), cosXmlResultListener);
    }

    @Override
    public String getObjectUrl(String bucket, String region, String key) {
        BasePutObjectRequest putObjectRequest = new BasePutObjectRequest(bucket, key, "");
        putObjectRequest.setRegion(region);
        return getAccessUrl(putObjectRequest);
    }

    /**
     * 通用接口同步方法
     */
    @Override
    public <T1 extends CosXmlRequest, T2 extends CosXmlResult> T2 commonInterface(T1 request, Class<T2> resultClass) throws CosXmlClientException, CosXmlServiceException {
        try {
            return execute(request, resultClass.newInstance());
        } catch (IllegalAccessException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                    "Failed to create result instance", e);
        } catch (InstantiationException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                    "Failed to create result instance", e);
        }
    }

    /**
     * 通用接口异步方法
     */
    @Override
    public <T1 extends CosXmlRequest, T2 extends CosXmlResult> void commonInterfaceAsync(T1 request, Class<T2> resultClass, CosXmlResultListener cosXmlResultListener) {
        try {
            schedule(request, resultClass.newInstance(), cosXmlResultListener);
        } catch (IllegalAccessException e) {
            cosXmlResultListener.onFail(request,
                    new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                            "Failed to create result instance", e), null);
        } catch (InstantiationException e) {
            cosXmlResultListener.onFail(request,
                    new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                            "Failed to create result instance", e), null);
        }
    }

    /**
     * 取消请求任务.&nbsp;
     * 详细介绍，请查看:{@link  BaseCosXml#cancel(CosXmlRequest)}
     */
    @Override
    public void cancel(CosXmlRequest cosXmlRequest) {
        if (cosXmlRequest != null && cosXmlRequest.getHttpTask() != null) {
            cosXmlRequest.getHttpTask().cancel();
        }
    }

    /**
     * 取消请求任务.&nbsp;
     * 详细介绍，请查看:{@link  BaseCosXml#cancel(CosXmlRequest)}
     */
    @Override
    public void cancel(CosXmlRequest cosXmlRequest, boolean now) {
        if (cosXmlRequest != null && cosXmlRequest.getHttpTask() != null) {
            cosXmlRequest.getHttpTask().cancel(now);
        }
    }

    /**
     * 取消所有的请求任务.&nbsp;
     * 详细介绍，请查看:{@link  BaseCosXml#cancelAll()}
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
     * 详细介绍，请查看:{@link  BaseCosXml#release()}
     */
    @Override
    public void release() {
        cancelAll();
    }



    @Deprecated
    public String getAppid() {
        return config.getAppid();
    }

    @Deprecated
    public String getRegion() {
        return config.getRegion();
    }

    @Deprecated
    public String getRegion(CosXmlRequest cosXmlRequest) {
        return cosXmlRequest.getRegion() == null ? config.getRegion() : cosXmlRequest.getRegion();
    }

    public CosXmlServiceConfig getConfig() {
        return config;
    }

    /**
     * 获取UserAgent
     * @return UserAgent
     */
    public String getUserAgent(@Nullable String networkClientType) {
        if(config == null){
            return VersionInfo.getUserAgent();
        }

        String userAgent;
        if(config.isEnableQuic()) {
            if(OkHttpClientImpl.class.getName().equals(networkClientType)){
                userAgent = VersionInfo.getUserAgent();
            } else if("com.tencent.qcloud.quic.QuicClientImpl".equals(networkClientType)) {
                userAgent = VersionInfo.getQuicUserAgent();
            } else {
                userAgent = VersionInfo.getQuicUserAgent();
            }
        } else {
            userAgent = VersionInfo.getUserAgent();
        }
        if(TextUtils.isEmpty(config.getUserAgentExtended())){
            return userAgent;
        } else {
            return userAgent + "-" + config.getUserAgentExtended();
        }
    }

    /**
     * 获取 SDK 日志信息
     */
    public File[] getLogFiles(int limit) {
        return COSLogger.getLogFiles(limit);
    }

    private void logRequestMetrics(CosXmlRequest cosXmlRequest){
        if (cosXmlRequest.getMetrics() != null) {
            COSLogger.iNetwork(
                    QCloudHttpClient.HTTP_LOG_TAG,
                    cosXmlRequest.getMetrics().toString()
            );
        }
    }
}
