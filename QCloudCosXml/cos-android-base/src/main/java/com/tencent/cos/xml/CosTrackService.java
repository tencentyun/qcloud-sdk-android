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

import androidx.annotation.Nullable;

import com.tencent.cos.xml.base.BuildConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.object.ObjectRequest;
import com.tencent.cos.xml.transfer.TransferTaskMetrics;
import com.tencent.cos.xml.utils.ThrowableUtils;
import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.track.Constants;
import com.tencent.qcloud.track.QCloudTrackService;
import com.tencent.qcloud.track.cls.ClsLifecycleCredentialProvider;
import com.tencent.qcloud.track.service.BeaconTrackService;
import com.tencent.qcloud.track.service.ClsTrackService;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * COS数据上报服务
 */
public class CosTrackService {
    private static final String TAG = "CosTrackService";

    private static final boolean IS_DEBUG = BuildConfig.DEBUG;
//    private static final boolean IS_DEBUG = true;

    private static final String SDK_NAME = "cos";

    private static final String EVENT_CODE_QCLOUD_TRACK_COS_SDK = "qcloud_track_cos_sdk";
    private static final String EVENT_CODE_NEW_TRANSFER = "qcloud_track_cos_sdk_transfer";

    private static final String EVENT_PARAMS_SUCCESS = "Success";
    private static final String EVENT_PARAMS_FAILURE = "Failure";
    private static final String EVENT_PARAMS_SERVER = "Server";
    private static final String EVENT_PARAMS_CLIENT = "Client";

    private static CosTrackService instance;

    //上报桥接来源
    private String bridge;

    private CosTrackService() {
    }

    /**
     * 初始化
     */
    public static void init(Context applicationContext, boolean isCloseReport, String bridge) {
        synchronized (CosTrackService.class) {
            if (instance == null) {
                instance = new CosTrackService();
                instance.bridge = bridge;
                if (BeaconTrackService.isInclude()) {
                    // 添加全部上报灯塔上报器
                    BeaconTrackService beaconTrackService = new BeaconTrackService();
                    beaconTrackService.setContext(applicationContext);
                    beaconTrackService.init("0AND05O9HW5YY29Z");
                    QCloudTrackService.getInstance().addTrackService(EVENT_CODE_QCLOUD_TRACK_COS_SDK, beaconTrackService);
                    // 添加全部上报灯塔上报器(新传输)
                    BeaconTrackService newTransferBeaconTrackService = new BeaconTrackService();
                    newTransferBeaconTrackService.setContext(applicationContext);
                    newTransferBeaconTrackService.init("0AND05O9HW5YY29Z");
                    QCloudTrackService.getInstance().addTrackService(EVENT_CODE_NEW_TRANSFER, newTransferBeaconTrackService);
//                // 添加云控灯塔上报器(不初始化)
//                BeaconTrackService ccBeaconTrackService = new BeaconTrackService();
//                ccBeaconTrackService.setContext(applicationContext);
//                QCloudTrackService.getInstance().addTrackService(EVENT_CODE_QCLOUD_TRACK_COS_SDK, ccBeaconTrackService);
                }

                // 初始化QCloudTrack
                QCloudTrackService.getInstance().init(applicationContext);
                QCloudTrackService.getInstance().setDebug(IS_DEBUG);
                QCloudTrackService.getInstance().setIsCloseReport(isCloseReport);

                CosTrackService.getInstance().reportSdkStart();

//                if (BeaconTrackService.isInclude()) {
//                // 获取灯塔云控配置，决定是否要initBeacon
//                CloudControl.getInstance().getBeaconAppKey(applicationContext.getPackageName(), new CloudControl.BeaconCloudCallback() {
//                    @Override
//                    public void onSuccess(String beaconKey) {
//                        if (!TextUtils.isEmpty(beaconKey)) {
//                            try {
//                                if (!BeaconTrackService.isInclude()) {
//                                    Log.i(TAG, "The beacon library is not referenced, cancel the beacon initialization");
//                                    return;
//                                }
//                                ccBeaconTrackService.init(beaconKey);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Log.d(TAG, "getBeaconAppKey onError:" + e.getMessage());
//                        e.printStackTrace();
//                    }
//                });
//                }
            }
        }
    }

    /**
     * 初始化CLS（固定密钥）
     * 请在调用cos sdk接口之前调用该方法
     * @param applicationContext Context
     * @param topicId 日志主题ID
     * @param endpoint 接入点
     * @param secretId 密钥 secretId
     * @param secretKey 密钥 secretKey
     */
    public static void initCLs(Context applicationContext,String topicId, String endpoint,
                               String secretId, String secretKey){
        if (!ClsTrackService.isInclude()) {
            throw new IllegalStateException("Please quote the cls library first: com.tencentcloudapi.cls:tencentcloud-cls-sdk-android:x.x.x");
        }
        ClsTrackService clsTrackService = new ClsTrackService();
        clsTrackService.init(applicationContext, topicId, endpoint);
        clsTrackService.setSecurityCredential(secretId, secretKey);
        QCloudTrackService.getInstance().addTrackService(EVENT_CODE_QCLOUD_TRACK_COS_SDK, clsTrackService);
    }

    /**
     * 初始化CLS（临时密钥）
     * 请在调用cos sdk接口之前调用该方法
     * @param applicationContext Context
     * @param topicId 日志主题ID
     * @param endpoint 接入点
     * @param lifecycleCredentialProvider CLS临时秘钥提供器
     */
    public static void initCLs(Context applicationContext,String topicId, String endpoint,
                               ClsLifecycleCredentialProvider lifecycleCredentialProvider){
        if (!ClsTrackService.isInclude()) {
            throw new IllegalStateException("Please quote the cls library first: com.tencentcloudapi.cls:tencentcloud-cls-sdk-android:x.x.x");
        }
        ClsTrackService clsTrackService = new ClsTrackService();
        clsTrackService.init(applicationContext, topicId, endpoint);
        clsTrackService.setCredentialProvider(lifecycleCredentialProvider);
        QCloudTrackService.getInstance().addTrackService(EVENT_CODE_QCLOUD_TRACK_COS_SDK, clsTrackService);
    }

    public static CosTrackService getInstance() {
        return instance;
    }

    /**
     * 上报简单数据_SDK启动
     */
    public void reportSdkStart() {
        Map<String, String> params = new HashMap<>();
        try {
            params.put("sdk_name", SDK_NAME);
            // 添加基础参数
            params.putAll(getCommonParams());
            QCloudTrackService.getInstance().reportSimpleData(Constants.SIMPLE_DATA_EVENT_CODE_START, params);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 上报简单数据_SDK异常
     *
     * @param source 异常来源
     * @param e      其他异常
     */
    public void reportError(String source, Exception e) {
        if (e == null) return;
        Map<String, String> params = new HashMap<>();
        try {
            Throwable rootCause = ThrowableUtils.getRootCause(e);
            params.put("sdk_name", SDK_NAME);
            params.put("qcloud_error_source", source);
            params.put("qcloud_error_name", rootCause.getClass().getSimpleName());
            params.put("qcloud_error_message", rootCause.getMessage());
            // 添加基础参数
            params.putAll(getCommonParams());
            QCloudTrackService.getInstance().reportSimpleData(Constants.SIMPLE_DATA_EVENT_CODE_ERROR, params);
        } catch (Exception eee){
            eee.printStackTrace();
        }
    }

    /**
     * 上报base_service事件 成功
     *
     * @param request request
     */
    public void reportRequestSuccess(CosXmlRequest request) {
        reportRequestSuccess(request, null);
    }

    /**
     * 上报base_service事件 ClientException
     */
    public CosXmlClientException reportRequestClientException(CosXmlRequest request, QCloudClientException clientException) {
        return reportClientException(request, clientException, null);
    }

    /**
     * 上报base_service事件 ServiceException
     */
    public CosXmlServiceException reportRequestServiceException(CosXmlRequest request, QCloudServiceException serviceException) {
        return reportServiceException(request, serviceException, null);
    }

    /**
     * 上报cos_upload事件 成功
     *
     * @param request request
     */
    public void reportUploadTaskSuccess(CosXmlRequest request) {
        // 只需要一个 PutObjectRequest 壳，带上 HttpTaskMetrics 信息
        reportRequestSuccess(request,
                Collections.singletonMap("request_name", "UploadTask"));
    }

    /**
     * 上报cos_upload事件 ClientException
     */
    public void reportUploadTaskClientException(CosXmlRequest request, QCloudClientException clientException) {
        reportClientException(request, clientException,
                createTransferExtra("UploadTask", request));
    }

    /**
     * 上报cos_upload事件 ServiceException
     */
    public void reportUploadTaskServiceException(CosXmlRequest request, QCloudServiceException serviceException) {
        reportServiceException(request, serviceException,
                createTransferExtra("UploadTask", request));
    }

    /**
     * 上报cos_download事件 成功
     *
     * @param request request
     */
    public void reportDownloadTaskSuccess(CosXmlRequest request) {
        // 只需要一个 GetObjectRequest 壳，带上 HttpTaskMetrics 信息
        reportRequestSuccess(request,
                Collections.singletonMap("request_name", "DownloadTask"));
    }

    /**
     * 上报cos_download事件 ClientException
     */
    public void reportDownloadTaskClientException(CosXmlRequest request, QCloudClientException clientException) {
        reportClientException(request, clientException,
                createTransferExtra("DownloadTask", request));
    }

    /**
     * 上报cos_download事件 ServiceException
     */
    public void reportDownloadTaskServiceException(CosXmlRequest request, QCloudServiceException serviceException) {
        reportServiceException(request, serviceException,
                createTransferExtra("DownloadTask", request));
    }

    /**
     * 上报cos_copy事件 成功
     *
     * @param request request
     */
    public void reportCopyTaskSuccess(CosXmlRequest request) {
        // 只需要一个 CopyObjectRequest 壳，带上 HttpTaskMetrics 信息
        reportRequestSuccess(request,
                Collections.singletonMap("request_name", "CopyTask"));
    }

    /**
     * 上报cos_copy事件 ClientException
     */
    public void reportCopyTaskClientException(CosXmlRequest request, CosXmlClientException clientException) {
        reportClientException(request, clientException,
                createTransferExtra("CopyTask", request));
    }

    /**
     * 上报cos_copy事件 ServiceException
     */
    public void reportCopyTaskServiceException(CosXmlRequest request, CosXmlServiceException serviceException) {
        reportServiceException(request, serviceException,
                createTransferExtra("CopyTask", request));
    }

    /**
     * 单个请求，整体任务 成功
     */
    private void reportRequestSuccess(CosXmlRequest request, @Nullable Map<String, String> extra) {
        try {
            HttpTaskMetrics taskMetrics = request.getMetrics();

            // 添加 Request 参数
            Map<String, String> params = parseCosXmlRequestParams(request);
            // 添加基础参数
            params.putAll(getCommonParams());
            // 添加性能参数
            params.putAll(parseHttpTaskMetricsParams(taskMetrics));
            // 添加服务名称
            if (extra == null || !extra.containsKey("request_name")) {
                params.put("request_name", request.getClass().getSimpleName());
            }
            // 添加请求结果
            params.put("request_result", EVENT_PARAMS_SUCCESS);
            // 添加额外参数
            if (extra != null) {
                params.putAll(extra);
            }
            QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上报客户端异常 单个请求，整体任务
     */
    private CosXmlClientException reportClientException(CosXmlRequest request, QCloudClientException clientException, @Nullable Map<String, String> extra) {
        ReturnClientException returnClientException = getClientExceptionParams(clientException);
        try {
            if (isReport(returnClientException.exception)) {
                HttpTaskMetrics taskMetrics = request.getMetrics();

                // 添加 Request 参数
                Map<String, String> params = parseCosXmlRequestParams(request);
                // 添加基础参数
                params.putAll(getCommonParams());
                // 添加错误信息
                params.putAll(returnClientException.params);
                // 添加性能参数
                params.putAll(parseHttpTaskMetricsParams(taskMetrics));
                // 添加服务名称
                if (extra == null || !extra.containsKey("request_name")) {
                    params.put("request_name", request.getClass().getSimpleName());
                }
                // 添加请求结果
                params.put("request_result", EVENT_PARAMS_FAILURE);

                // 添加额外参数
                if (extra != null) {
                    params.putAll(extra);
                }
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnClientException.exception;
    }

    /**
     * 上报服务端异常 单个请求，整体任务
     */
    private CosXmlServiceException reportServiceException(CosXmlRequest request, QCloudServiceException serviceException, @Nullable Map<String, String> extra) {
        ReturnServiceException returnServiceException = getServiceExceptionParams(serviceException);
        try {
            if (isReport(returnServiceException.exception)) {
                // 添加 Request 参数
                Map<String, String> params = parseCosXmlRequestParams(request);
                // 添加基础参数
                params.putAll(getCommonParams());
                // 添加错误信息
                params.putAll(returnServiceException.params);
                // 添加性能参数
                params.putAll(parseHttpTaskMetricsParams(request.getMetrics()));
                // 添加服务名称
                if (extra == null || !extra.containsKey("request_name")) {
                    params.put("request_name", request.getClass().getSimpleName());
                }
                // 添加请求结果
                params.put("request_result", EVENT_PARAMS_FAILURE);

                // 添加额外参数
                if (extra != null) {
                    params.putAll(extra);
                }
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, params);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return returnServiceException.exception;
    }

    /**
     * 获取http性能数据字段
     */
    private Map<String, String> parseHttpTaskMetricsParams(@Nullable HttpTaskMetrics taskMetrics) {
        Map<String, String> params = new HashMap<>();

        if (taskMetrics == null) {
            return params;
        }

        params.put("http_took_time", String.valueOf(taskMetrics.httpTaskFullTime()));
        params.put("http_dns", String.valueOf(taskMetrics.dnsLookupTookTime()));
        params.put("http_connect", String.valueOf(taskMetrics.connectTookTime()));
        params.put("http_secure_connect", String.valueOf(taskMetrics.secureConnectTookTime()));
        params.put("http_md5", String.valueOf(taskMetrics.calculateMD5STookTime()));
        params.put("http_sign", String.valueOf(taskMetrics.signRequestTookTime()));
        params.put("http_read_header", String.valueOf(taskMetrics.readResponseHeaderTookTime()));
        params.put("http_read_body", String.valueOf(taskMetrics.readResponseBodyTookTime()));
        params.put("http_write_header", String.valueOf(taskMetrics.writeRequestHeaderTookTime()));
        params.put("http_write_body", String.valueOf(taskMetrics.writeRequestBodyTookTime()));
        params.put("http_full_time", String.valueOf(taskMetrics.fullTaskTookTime()));
        params.put("http_size", String.valueOf(taskMetrics.requestBodyByteCount() + taskMetrics.responseBodyByteCount()));
        params.put("http_retry_times", String.valueOf(taskMetrics.getRetryCount()));
        params.put("http_domain", taskMetrics.getDomainName() != null ? taskMetrics.getDomainName() : "null");
        params.put("http_connect_ip", taskMetrics.getConnectAddress() != null ? taskMetrics.getConnectAddress().getHostAddress() : "null");
        params.put("http_dns_ips", taskMetrics.getRemoteAddress() != null ? taskMetrics.getRemoteAddress().toString() : "null");
        // 速度 每秒传输的数据大小 kb为单位
        params.put("http_speed", String.valueOf(((taskMetrics.requestBodyByteCount() + taskMetrics.responseBodyByteCount())/1024d) / taskMetrics.httpTaskFullTime()));
        return params;
    }

    /**
     * 获取request数据字段
     */
    private Map<String, String> parseCosXmlRequestParams(CosXmlRequest request) {
        Map<String, String> params = new HashMap<>();
        if (request == null) {
            return params;
        }

        params.put("bucket", request.getBucket());
        params.put("region", request.getRegion());

        if (request.getHttpTask() != null) {
            if (request.getHttpTask().request() != null) {
                String network_protocol;
                String ua = request.getHttpTask().request().header(HttpConstants.Header.USER_AGENT);
                if(!TextUtils.isEmpty(ua) && ua.contains("android-quic-sdk")){
                    network_protocol = "quic";
                } else {
                    network_protocol = request.getHttpTask().request().url().getProtocol();
                }
                params.put("network_protocol", network_protocol);

                params.put("http_method", request.getHttpTask().request().method());
                if (request.getHttpTask().request().url() != null) {
                    params.put("url", request.getHttpTask().request().url().toString());
                }
                if (!TextUtils.isEmpty(ua)) {
                    params.put("user_agent", ua);
                }
            }
        }

        if (request instanceof ObjectRequest &&
                !TextUtils.isEmpty(((ObjectRequest) request).getCosPath())) {
            params.put("request_path", ((ObjectRequest) request).getCosPath());
        }

        String host = parseHost(request); // taskMetrics.getDomainName();
        if (!TextUtils.isEmpty(host)) {
            params.put("host", host);
            try {
                Pattern pattern = Pattern.compile(".*\\.cos\\.(.*)\\.myqcloud.com");
                Matcher matcher = pattern.matcher(host);
                if (matcher.find()) {
                    String findRegion = matcher.group(1);
                    if(!TextUtils.isEmpty(findRegion) && !"accelerate".equals(findRegion)) {
                        params.put("region", findRegion);
                    }
                }
            } catch (Exception e) {
            }
            try {
                params.put("accelerate", String.valueOf(host.endsWith("cos.accelerate.myqcloud.com")));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return params;
    }

    /**
     * 获取公共参数
     *
     * @return 公共参数
     */
    private Map<String, String> getCommonParams() {
        Map<String, String> params = new HashMap<>();
        params.put("sdk_version_name", com.tencent.cos.xml.base.BuildConfig.VERSION_NAME);
        params.put("sdk_version_code", String.valueOf(com.tencent.cos.xml.base.BuildConfig.VERSION_CODE));
        if (!TextUtils.isEmpty(bridge)) {
            params.put("sdk_bridge", bridge);
        }
        return params;
    }

    private @Nullable String parseHost(CosXmlRequest request) {
        HttpTask httpTask = request.getHttpTask();
        String host = null;
        if (httpTask != null) {
            HttpRequest httpRequest = httpTask.request();
            if (httpRequest != null) {
                host = httpRequest.host();
            }
        }
        if (host == null && request.getMetrics() != null) {
            host = request.getMetrics().getDomainName();
        }
        return host;
    }

    /**
     * 获取服务端异常参数
     */
    private ReturnServiceException getServiceExceptionParams(QCloudServiceException e) {
        Map<String, String> params = new HashMap<>();
        CosXmlServiceException serviceException = convertServerException(e);
        params.put("error_code",
                !TextUtils.isEmpty(serviceException.getErrorCode()) && !serviceException.getErrorCode().equals("null") ?
                        serviceException.getErrorCode() :
                        String.valueOf(serviceException.getStatusCode()));
        params.put("error_message",
                !TextUtils.isEmpty(serviceException.getErrorMessage()) && !serviceException.getErrorMessage().equals("null") ?
                        serviceException.getErrorMessage() :
                        String.valueOf(serviceException.getHttpMessage()));
        params.put("error_http_code", String.valueOf(serviceException.getStatusCode()));
        params.put("error_http_message", serviceException.getHttpMessage());
        params.put("error_service_name", serviceException.getServiceName());
        params.put("error_request_id", serviceException.getRequestId());
        params.put("error_type", EVENT_PARAMS_SERVER);
        return new ReturnServiceException(serviceException, params);
    }

    private static class ReturnServiceException {
        private final CosXmlServiceException exception;
        private final Map<String, String> params;

        public ReturnServiceException(CosXmlServiceException exception, Map<String, String> params) {
            this.exception = exception;
            this.params = params;
        }
    }

    /**
     * 获取客户端异常参数
     */
    private ReturnClientException getClientExceptionParams(QCloudClientException e) {
        Map<String, String> params = new HashMap<>();
        CosXmlClientException xmlClientException = convertClientException(e);
        Throwable rootCause = ThrowableUtils.getRootCause(xmlClientException);
        params.put("error_code", String.valueOf(xmlClientException.errorCode));
        params.put("error_name", rootCause.getClass().getSimpleName());
        params.put("error_message", rootCause.getMessage());
        params.put("error_type", EVENT_PARAMS_CLIENT);
        return new ReturnClientException(xmlClientException, params);
    }

    private static class ReturnClientException {
        private final CosXmlClientException exception;
        private final Map<String, String> params;

        public ReturnClientException(CosXmlClientException exception, Map<String, String> params) {
            this.exception = exception;
            this.params = params;
        }
    }

    private CosXmlServiceException convertServerException(QCloudServiceException e) {
        return e instanceof CosXmlServiceException ? (CosXmlServiceException) e
                : new CosXmlServiceException(e);
    }

    private CosXmlClientException convertClientException(QCloudClientException e) {
        CosXmlClientException xmlClientException;
        if (e instanceof CosXmlClientException) {
            xmlClientException = (CosXmlClientException) e;
        } else {
            Throwable causeException = ThrowableUtils.getRootCause(e);
            if (causeException instanceof IllegalArgumentException) {
                xmlClientException = new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
            } else if (causeException instanceof QCloudAuthenticationException) {
                xmlClientException = new CosXmlClientException(ClientErrorCode.INVALID_CREDENTIALS.getCode(), e);
            } else if (causeException instanceof IOException) {
                xmlClientException = new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(), e);
            } else {
                xmlClientException = new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
            }
        }
        return xmlClientException;
    }

    /**
     * 是否上报（过滤掉不需要上报的异常）
     *
     * @param e 服务端异常
     * @return 是否上报
     */
    private boolean isReport(CosXmlServiceException e) {
        return true;
    }

    /**
     * 是否上报（过滤掉不需要上报的异常）
     *
     * @param e 客户端异常
     * @return 是否上报
     */
    private boolean isReport(CosXmlClientException e) {
        boolean notReport = e.getMessage() != null && e.getMessage().toLowerCase(Locale.ROOT).contains("canceled");
        return !notReport;
    }

    /**
     * 返回 服务名称 和 错误节点
     */
    private Map<String, String> createTransferExtra(String name, CosXmlRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("request_name", name);
        params.put("error_node", request != null ? request.getClass().getSimpleName() : "null");
        return params;
    }

    /**
     * 上报新的传输事件cos_transfer 成功
     */
    public void reportTransferSuccess(CosXmlRequest request, TransferTaskMetrics taskMetrics, boolean encrypted) {
        reportTransferTask(request, taskMetrics, encrypted, true, null);
    }

    /**
     * 上报cos_transfer事件 客户端异常 单个请求，整体任务
     * <p>
     * 失败：
     * 1. host
     * 2. region
     * 3. 失败原因
     * 4. errorcode
     * 5. dns解析时间，建联时间，tls建联时间，ip列表，服务端ip，读写时间，请求整体耗时
     */
    public void reportTransferClientException(CosXmlRequest request, TransferTaskMetrics taskMetrics, CosXmlClientException clientException, boolean encrypted) {
        if (isReport(clientException)) {
            reportTransferTask(request, taskMetrics, encrypted, false, parseClientExceptionParams(clientException));
        }
    }

    /**
     * 上报cos_transfer事件 服务端异常 单个请求，整体任务
     * <p>
     * 失败：
     * 1. host
     * 2. region
     * 3. 失败原因
     * 4. errorcode
     * 5. dns解析时间，建联时间，tls建联时间，ip列表，服务端ip，读写时间，请求整体耗时
     * 6. requestid
     */
    public void reportTransferServiceException(CosXmlRequest request, TransferTaskMetrics taskMetrics, CosXmlServiceException serviceException, boolean encrypted) {
        if (isReport(serviceException)) {
            reportTransferTask(request, taskMetrics, encrypted, false, parseServiceExceptionParams(serviceException));
        }
    }

    private void reportTransferTask(CosXmlRequest request, TransferTaskMetrics taskMetrics, boolean encrypted,
                                    boolean isSuccess, @Nullable Map<String, String> extras) {
        try {
            Map<String, String> params = parseCosXmlRequestParams(request);
            // 添加服务名称
            params.put("request_name", request.getClass().getSimpleName());
            // 添加基础参数
            params.putAll(getCommonParams());
            // 添加性能参数
            params.putAll(parseSimplePerfParams(taskMetrics));
            // 是否加密传输
            params.put("encrypted", String.valueOf(encrypted));
            // 添加请求结果
            params.put("request_result", isSuccess ? EVENT_PARAMS_SUCCESS : EVENT_PARAMS_FAILURE);
            if (extras != null) {
                params.putAll(extras);
            }
            QCloudTrackService.getInstance().report(EVENT_CODE_NEW_TRANSFER, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> parseSimplePerfParams(@Nullable TransferTaskMetrics taskMetrics) {

        Map<String, String> params = new HashMap<>();
        if (taskMetrics == null) {
            return params;
        }

        params.put("transfer_size", String.valueOf(taskMetrics.getSize()));
        params.put("ip", taskMetrics.getConnectAddress() != null ? taskMetrics.getConnectAddress().getHostAddress() : "");
        params.put("took_time", String.valueOf(taskMetrics.getTookTime()));
        params.put("wait_took_time", String.valueOf(taskMetrics.getWaitTookTime()));
        params.put("first_progress_took_time", String.valueOf(taskMetrics.getFirstProgressTookTime()));

        return params;
    }

    private Map<String, String> parseClientExceptionParams(CosXmlClientException clientException) {
        Map<String, String> params = new HashMap<>();
        params.put("error_message", clientException.getMessage());
        params.put("error_code", String.valueOf(clientException.errorCode));
        params.put("error_type", EVENT_PARAMS_CLIENT);
        return params;
    }

    private Map<String, String> parseServiceExceptionParams(CosXmlServiceException serviceException) {
        Map<String, String> params = new HashMap<>();
        params.put("error_message", serviceException.getErrorMessage());
        params.put("error_code", serviceException.getErrorCode());
        params.put("request_id", serviceException.getRequestId());
        params.put("error_type", EVENT_PARAMS_SERVER);
        return params;
    }

    //    private String cosUploadName(boolean cse) {
//        return cse? "COSUploadTask-CSE" : "COSUploadTask";
//    }
//
//    private String cosDownloadName(boolean cse) {
//        return cse? "COSDownloadTask-CSE" : "COSDownloadTask";
//    }

//    public void reportCOSUploadTaskSuccess(CosXmlRequest request, boolean cse) {
//        // 只需要一个 PutObjectRequest 壳，带上 HttpTaskMetrics 信息
//        reportRequestSuccess(EVENT_CODE_UPLOAD, request,
//                Collections.singletonMap("request_name", cosUploadName(cse)));
//    }
//
//    public void reportCOSUploadTaskClientException(CosXmlRequest request, QCloudClientException clientException, boolean cse) {
//        reportClientException(EVENT_CODE_UPLOAD, request, clientException,
//                createTransferExtra(cosUploadName(cse), request));
//    }
//
//    public void reportCOSUploadTaskServiceException(CosXmlRequest request, QCloudServiceException serviceException, boolean cse) {
//        reportServiceException(EVENT_CODE_UPLOAD, request, serviceException,
//                createTransferExtra(cosUploadName(cse), request));
//    }
//
//    public void reportCOSDownloadTaskSuccess(CosXmlRequest request, boolean cse) {
//        // 只需要一个 GetObjectRequest 壳，带上 HttpTaskMetrics 信息
//        reportRequestSuccess(EVENT_CODE_DOWNLOAD, request,
//                Collections.singletonMap("request_name", cosDownloadName(cse)));
//    }
//
//    public void reportCOSDownloadTaskClientException(CosXmlRequest request, QCloudClientException clientException, boolean cse) {
//        reportClientException(EVENT_CODE_DOWNLOAD, request, clientException,
//                createTransferExtra(cosDownloadName(cse), request));
//    }
//
//    public void reportCOSDownloadTaskServiceException(CosXmlRequest request, QCloudServiceException serviceException, boolean cse) {
//        reportServiceException(EVENT_CODE_DOWNLOAD, request, serviceException,
//                createTransferExtra(cosDownloadName(cse), request));
//    }


//    private boolean isDownloadTaskRequest(CosXmlRequest cosXmlRequest) {
//        String requestName = cosXmlRequest.getClass().getSimpleName();
//        return "GetObjectRequest".equals(requestName);
//    }
//
//    private boolean isUploadTaskRequest(CosXmlRequest cosXmlRequest) {
//        String requestName = cosXmlRequest.getClass().getSimpleName();
//        return "PutObjectRequest".equals(requestName) ||
//                "InitMultipartUploadRequest".equals(requestName) ||
//                "ListPartsRequest".equals(requestName) ||
//                "UploadPartRequest".equals(requestName) ||
//                "CompleteMultiUploadRequest".equals(requestName) ||
//                "AbortMultiUploadRequest".equals(requestName);
//    }
//
//
//    // 因为copy task用了上传的一些request，因此此处只将UploadPartCopyRequest和CopyObjectRequest归为copy
//    private boolean isCopyTaskRequest(CosXmlRequest cosXmlRequest) {
//        String requestName = cosXmlRequest.getClass().getSimpleName();
//        return "UploadPartCopyRequest".equals(requestName) ||
//                "CopyObjectRequest".equals(requestName);
//    }
}
