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

import com.tencent.cos.xml.base.BuildConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
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
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.cos.xml.model.object.UploadRequest;
import com.tencent.cos.xml.transfer.ResponseBytesConverter;
import com.tencent.cos.xml.transfer.ResponseFileBodySerializer;
import com.tencent.cos.xml.transfer.ResponseXmlS3BodySerializer;
import com.tencent.cos.xml.utils.StringUtils;
import com.tencent.cos.xml.utils.URLEncodeUtils;
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
import com.tencent.qcloud.core.logger.AndroidLogcatAdapter;
import com.tencent.qcloud.core.logger.FileLogAdapter;
import com.tencent.qcloud.core.logger.QCloudLogger;
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
import java.util.concurrent.Executor;


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
     * 是否关闭灯塔上报
     */
    public static boolean IS_CLOSE_BEACON;
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
        if(configuration.isDebuggable() && !BuildConfig.DEBUG){
            FileLogAdapter fileLogAdapter = FileLogAdapter.getInstance(context, "QLog");
            QCloudLogger.addAdapter(fileLogAdapter);
        }
        if(configuration.isDebuggable()){
            AndroidLogcatAdapter logcatAdapter = new AndroidLogcatAdapter();
            QCloudLogger.addAdapter(logcatAdapter);
        }

        BeaconService.init(context.getApplicationContext(), IS_CLOSE_BEACON, BRIDGE);
        appCachePath = context.getApplicationContext().getFilesDir().getPath();

        TaskExecutors.initExecutor(configuration.getUploadMaxThreadCount(), configuration.getDownloadMaxThreadCount());

        setNetworkClient(configuration);
        ContextHolder.setContext(context);
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
        builder.enableDebugLog(configuration.isDebuggable());
        if(configuration.isEnableQuic()){
            try {
                Class clazz = Class.forName("com.tencent.qcloud.quic.QuicClientImpl");
                builder.setNetworkClient((NetworkClient) clazz.newInstance());
            } catch (Exception e) {
                IllegalStateException illegalStateException = new IllegalStateException(e.getMessage(), e);
                BeaconService.getInstance().reportError(TAG, illegalStateException);
                throw illegalStateException;
            }
        }else {
            builder.setNetworkClient(new OkHttpClientImpl());
        }
        builder.dnsCache(configuration.isDnsCache());
        builder.addPrefetchHost(configuration.getEndpointSuffix());
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
        client.setDebuggable(configuration.isDebuggable());
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
        httpRequestBuilder.addNoSignParamKeys(cosXmlRequest.getNoSignParams());

        //add url
        String requestURL = cosXmlRequest.getRequestURL();
        final String host = getRequestHost(cosXmlRequest);
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
            httpRequestBuilder.signer(null, null);
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

        if (cosXmlRequest instanceof GetObjectRequest) {
            GetObjectRequest getObjectRequest = (GetObjectRequest) cosXmlRequest;
            if (!TextUtils.isEmpty(getObjectRequest.getDownloadPath())) {
                httpRequestBuilder.converter(new ResponseFileBodySerializer<T2>((GetObjectResult) cosXmlResult,
                        getObjectRequest.getDownloadPath(), getObjectRequest.getFileOffset()));
            } else if (getObjectRequest.getFileContentUri() != null) {
                httpRequestBuilder.converter(new ResponseFileBodySerializer<T2>((GetObjectResult) cosXmlResult,
                        getObjectRequest.getFileContentUri(),
                        ContextHolder.getAppContext().getContentResolver(),
                        getObjectRequest.getFileOffset()));
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

    /**
     * 同步执行
     */
    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> T2 execute(T1 cosXmlRequest, T2 cosXmlResult)
            throws CosXmlClientException, CosXmlServiceException {
        try {
            if (cosXmlRequest.getMetrics() == null) {
                cosXmlRequest.attachMetrics(new HttpTaskMetrics());
            }
            QCloudHttpRequest<T2> httpRequest = buildHttpRequest(cosXmlRequest, cosXmlResult);
            HttpTask<T2> httpTask;

            httpTask = client.resolveRequest(httpRequest, credentialProvider);
            httpTask.setTransferThreadControl(config.isTransferThreadControl());
            httpTask.setUploadMaxThreadCount(config.getUploadMaxThreadCount());
            httpTask.setDownloadMaxThreadCount(config.getDownloadMaxThreadCount());
            cosXmlRequest.setTask(httpTask);

            setProgressListener(cosXmlRequest, httpTask, false);

            HttpResult<T2> httpResult = httpTask.executeNow();

            BeaconService.getInstance().reportRequestSuccess(cosXmlRequest);

            logRequestMetrics(cosXmlRequest);

            return httpResult != null ? httpResult.content() : null;
        } catch (QCloudServiceException e) {
            throw BeaconService.getInstance().reportRequestServiceException(cosXmlRequest, e);
        } catch (QCloudClientException e) {
            throw BeaconService.getInstance().reportRequestClientException(cosXmlRequest, e);
        }
    }

    /**
     * 异步执行
     */
    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> void schedule(final T1 cosXmlRequest, T2 cosXmlResult,
                                                                                final CosXmlResultListener cosXmlResultListener) {
        QCloudResultListener<HttpResult<T2>> qCloudResultListener = new QCloudResultListener<HttpResult<T2>>() {
            @Override
            public void onSuccess(HttpResult<T2> result) {
                BeaconService.getInstance().reportRequestSuccess(cosXmlRequest);
                logRequestMetrics(cosXmlRequest);
                cosXmlResultListener.onSuccess(cosXmlRequest, result.content());
            }

            @Override
            public void onFailure(QCloudClientException clientException, QCloudServiceException serviceException) {
                logRequestMetrics(cosXmlRequest);
                if (clientException != null) {
                    CosXmlClientException xmlClientException = BeaconService.getInstance().reportRequestClientException(cosXmlRequest, clientException);
                    cosXmlResultListener.onFail(cosXmlRequest, xmlClientException,null);
                } else if (serviceException != null) {
                    CosXmlServiceException xmlServiceException = BeaconService.getInstance().reportRequestServiceException(cosXmlRequest, serviceException);
                    cosXmlResultListener.onFail(cosXmlRequest, null, xmlServiceException);
                }
            }
        };

        try {
            if (cosXmlRequest.getMetrics() == null) {
                cosXmlRequest.attachMetrics(new HttpTaskMetrics());
            }
            QCloudHttpRequest<T2> httpRequest = buildHttpRequest(cosXmlRequest, cosXmlResult);

            HttpTask<T2> httpTask;
//            if (cosXmlRequest instanceof PostObjectRequest) {
//                httpTask = client.resolveRequest(httpRequest, null);
//            } else {
//                httpTask = client.resolveRequest(httpRequest, credentialProvider);
//            }
            httpTask = client.resolveRequest(httpRequest, credentialProvider);
            
            httpTask.setTransferThreadControl(config.isTransferThreadControl());
            httpTask.setUploadMaxThreadCount(config.getUploadMaxThreadCount());
            httpTask.setDownloadMaxThreadCount(config.getDownloadMaxThreadCount());

            cosXmlRequest.setTask(httpTask);

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
            CosXmlClientException clientException = BeaconService.getInstance().reportRequestClientException(cosXmlRequest, e);
            cosXmlResultListener.onFail(cosXmlRequest, clientException,null);
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
        String host = null;
        try {
            // host = cosXmlRequest.getHost(config, false);
            host = getRequestHost(cosXmlRequest);
        } catch (CosXmlClientException e) {
            BeaconService.getInstance().reportError(TAG, e);
            e.printStackTrace();
        }
        String path = "/";
        try {
            path = URLEncodeUtils.cosPathEncode(cosXmlRequest.getPath(config));
        } catch (CosXmlClientException e) {
            BeaconService.getInstance().reportError(TAG, e);
            e.printStackTrace();
        }
        return config.getProtocol() + "://" + host + path;
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
            if (credentialProvider instanceof ScopeLimitCredentialProvider) {
                credentials = ((ScopeLimitCredentialProvider) credentialProvider).getCredentials(
                        cosXmlRequest.getSTSCredentialScope(config));
            } else {
                credentials = credentialProvider.getCredentials();
            }

            QCloudSigner signer = SignerFactory.getSigner(signerTypeCompat(signerType, cosXmlRequest));

            QCloudHttpRequest request = buildHttpRequest(cosXmlRequest, null);
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
     * 获取 SDK 日志信息
     */
    public File[] getLogFiles(int limit) {
        FileLogAdapter fileLogAdapter = QCloudLogger.getAdapter(FileLogAdapter.class);
        if (fileLogAdapter != null) {
            return fileLogAdapter.getLogFilesDesc(limit);
        }
        return null;
    }

    private void logRequestMetrics(CosXmlRequest cosXmlRequest){
        if(config.isDebuggable()) {
            if (cosXmlRequest.getMetrics() != null) {
                QCloudLogger.i(
                        QCloudHttpClient.HTTP_LOG_TAG,
                        cosXmlRequest.getMetrics().toString()
                );
            }
        }
    }
}
