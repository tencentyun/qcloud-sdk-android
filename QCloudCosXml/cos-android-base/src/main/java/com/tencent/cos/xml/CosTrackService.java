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

import com.tencent.cos.xml.base.BuildConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.VersionInfo;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.object.BasePutObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectBytesRequest;
import com.tencent.cos.xml.model.object.GetObjectRequest;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
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

    // 业务事件
    private static final String EVENT_CODE_TRACK_COS_SDK = "qcloud_track_cos_sdk";
    // 网络事件
    private static final String EVENT_CODE_TRACK_COS_SDK_HTTP = "qcloud_track_cos_sdk_http";
    // 常规网络探测事件
    public static final String EVENT_CODE_TRACK_COS_SDK_SONAR = "qcloud_track_cos_sdk_sonar";
    // 错误网络探测事件
    public static final String EVENT_CODE_TRACK_COS_SDK_SONAR_FAILURE = "qcloud_track_cos_sdk_sonar_failure";
    private static final String EVENT_CODE_NEW_TRANSFER = "qcloud_track_cos_sdk_transfer";

    private static final String EVENT_PARAMS_SUCCESS = "Success";
    private static final String EVENT_PARAMS_FAILURE = "Failure";
    private static final String EVENT_PARAMS_SERVER = "Server";
    private static final String EVENT_PARAMS_CLIENT = "Client";

    private static CosTrackService instance;
    //上报桥接来源
    private String bridge;
    private boolean isCloseReport;
    public boolean isCloseReport() {
        return isCloseReport;
    }

    private final CosTrackSonarService sonarService = new CosTrackSonarService();
    public CosTrackSonarService getSonarService() {
        return sonarService;
    }
    private final CosTrackService.SonarHostsRandomQueue sonarHosts = new CosTrackService.SonarHostsRandomQueue(3);
    public SonarHostsRandomQueue getSonarHosts() {
        return sonarHosts;
    }

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
                instance.isCloseReport = isCloseReport;
                if (BeaconTrackService.isInclude()) {
                    // 添加全部上报灯塔上报器
                    BeaconTrackService beaconTrackService = new BeaconTrackService();
                    beaconTrackService.setContext(applicationContext);
                    beaconTrackService.init("0AND063SCYC5856Q");
                    QCloudTrackService.getInstance().addTrackService(EVENT_CODE_TRACK_COS_SDK, beaconTrackService);
                    QCloudTrackService.getInstance().addTrackService(EVENT_CODE_TRACK_COS_SDK_HTTP, beaconTrackService);
                    QCloudTrackService.getInstance().addTrackService(EVENT_CODE_TRACK_COS_SDK_SONAR, beaconTrackService);
                    QCloudTrackService.getInstance().addTrackService(EVENT_CODE_TRACK_COS_SDK_SONAR_FAILURE, beaconTrackService);

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
                CosTrackService.getInstance().getSonarService().setContext(applicationContext);
                CosTrackService.getInstance().getSonarService().periodicSonar();
            }
        }
    }

    /**
     * 初始化CLS（匿名方式）
     * 请在调用cos sdk接口之前调用该方法
     * @param applicationContext Context
     * @param topicId 日志主题ID
     * @param endpoint 接入点
     */
    public static void initCLs(Context applicationContext,String topicId, String endpoint){
        if (!ClsTrackService.isInclude()) {
            throw new IllegalStateException("Please quote the cls library first: com.tencentcloudapi.cls:tencentcloud-cls-sdk-android:x.x.x");
        }
        ClsTrackService clsTrackService = new ClsTrackService();
        clsTrackService.init(applicationContext, topicId, endpoint);
        // 写死固定无效字符串，因为cls sdk不允许空密钥
        clsTrackService.setSecurityCredential("secretId", "secretKey");
        QCloudTrackService.getInstance().addTrackService(EVENT_CODE_TRACK_COS_SDK, clsTrackService);
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
        QCloudTrackService.getInstance().addTrackService(EVENT_CODE_TRACK_COS_SDK, clsTrackService);
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
        QCloudTrackService.getInstance().addTrackService(EVENT_CODE_TRACK_COS_SDK, clsTrackService);
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
            if(IS_DEBUG) {
                e.printStackTrace();
            }
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
            if(IS_DEBUG) {
                eee.printStackTrace();
            }
        }
    }

    /**
     * 网络事件：每次请求网络后产生
     */
    public void reportHttpMetrics(CosXmlRequest request) {
        if (request == null || request.getMetrics() == null ||
                // 判断有没有http性能，有的话上报，因为有可能是客户端非网络异常
                (request.getMetrics().httpTaskFullTime() == 0 && request.getMetrics().dnsLookupTookTime() == 0
                        && request.getMetrics().connectTookTime() == 0 && request.getMetrics().secureConnectTookTime() == 0)) {
            return;
        }

        try {
            // 添加 Request 参数
            Map<String, String> params = parseCosXmlRequestBaseParams(request);
            // 添加HTTP性能参数
            HttpTaskMetrics taskMetrics = request.getMetrics();
            params.putAll(parseHttpTaskMetricsBaseParams(taskMetrics, getRequestName(request)));
            // 添加基础参数
            params.putAll(getCommonParams());
            if(!TextUtils.isEmpty(request.getClientTraceId())){
                params.put("client_trace_id", request.getClientTraceId());
            }
            QCloudTrackService.getInstance().report(EVENT_CODE_TRACK_COS_SDK_HTTP, params);
        } catch (Exception e) {
            if(IS_DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上报base_service事件 成功
     *
     * @param request request
     */
    public void reportRequestSuccess(CosXmlRequest request, boolean internal, Map<String, String> configExtra) {
        Map<String, String> params = new HashMap<>();
        params.put("request_name", getRequestName(request));
        params.putAll(configExtra);
        reportRequestSuccess(
                request,
                params,
                internal
        );
    }

    /**
     * 上报base_service事件 ClientException
     */
    public CosXmlClientException reportRequestClientException(CosXmlRequest request, QCloudClientException clientException, boolean internal, Map<String, String> configExtra) {
        Map<String, String> params = new HashMap<>();
        params.put("request_name", getRequestName(request));
        params.putAll(configExtra);
        return reportClientException(
                request,
                clientException,
                params,
                internal
        );
    }

    /**
     * 上报base_service事件 ServiceException
     */
    public CosXmlServiceException reportRequestServiceException(CosXmlRequest request, QCloudServiceException serviceException, boolean internal, Map<String, String> configExtra) {
        Map<String, String> params = new HashMap<>();
        params.put("request_name", getRequestName(request));
        params.putAll(configExtra);
        return reportServiceException(
                request,
                serviceException,
                params,
                internal
        );
    }

    /**
     * 上报cos_upload事件 成功
     *
     * @param request request
     */
    public void reportUploadTaskSuccess(CosXmlRequest request, Map<String, String> configExtra) {
        // 只需要一个 PutObjectRequest 壳，带上 HttpTaskMetrics 信息
        Map<String, String> params = new HashMap<>();
        params.put("request_name", "UploadTask");
        params.putAll(configExtra);
        reportRequestSuccess(request, params, false);
    }

    /**
     * 上报cos_upload事件 ClientException
     */
    public void reportUploadTaskClientException(CosXmlRequest request, QCloudClientException clientException, Map<String, String> configExtra) {
        Map<String, String> params = createTransferExtra("UploadTask", request);
        params.putAll(configExtra);
        reportClientException(request, clientException, params, false);
    }

    /**
     * 上报cos_upload事件 ServiceException
     */
    public void reportUploadTaskServiceException(CosXmlRequest request, QCloudServiceException serviceException, Map<String, String> configExtra) {
        Map<String, String> params = createTransferExtra("UploadTask", request);
        params.putAll(configExtra);
        reportServiceException(request, serviceException, params, false);
    }

    /**
     * 上报cos_download事件 成功
     *
     * @param request request
     */
    public void reportDownloadTaskSuccess(CosXmlRequest request, Map<String, String> configExtra) {
        // 只需要一个 GetObjectRequest 壳，带上 HttpTaskMetrics 信息
        Map<String, String> params = new HashMap<>();
        params.put("request_name", "DownloadTask");
        params.putAll(configExtra);
        reportRequestSuccess(request,
                params, false);
    }

    /**
     * 上报cos_download事件 ClientException
     */
    public void reportDownloadTaskClientException(CosXmlRequest request, QCloudClientException clientException, Map<String, String> configExtra) {
        Map<String, String> params = createTransferExtra("DownloadTask", request);
        params.putAll(configExtra);
        reportClientException(request, clientException,
                params, false);
    }

    /**
     * 上报cos_download事件 ServiceException
     */
    public void reportDownloadTaskServiceException(CosXmlRequest request, QCloudServiceException serviceException, Map<String, String> configExtra) {
        Map<String, String> params = createTransferExtra("DownloadTask", request);
        params.putAll(configExtra);
        reportServiceException(request, serviceException,
                params, false);
    }

    /**
     * 上报cos_copy事件 成功
     *
     * @param request request
     */
    public void reportCopyTaskSuccess(CosXmlRequest request, Map<String, String> configExtra) {
        // 只需要一个 CopyObjectRequest 壳，带上 HttpTaskMetrics 信息
        Map<String, String> params = new HashMap<>();
        params.put("request_name", "CopyTask");
        params.putAll(configExtra);
        reportRequestSuccess(request,
                params, false);
    }

    /**
     * 上报cos_copy事件 ClientException
     */
    public void reportCopyTaskClientException(CosXmlRequest request, CosXmlClientException clientException, Map<String, String> configExtra) {
        Map<String, String> params = createTransferExtra("CopyTask", request);
        params.putAll(configExtra);
        reportClientException(request, clientException,
                params, false);
    }

    /**
     * 上报cos_copy事件 ServiceException
     */
    public void reportCopyTaskServiceException(CosXmlRequest request, CosXmlServiceException serviceException, Map<String, String> configExtra) {
        Map<String, String> params = createTransferExtra("CopyTask", request);
        params.putAll(configExtra);
        reportServiceException(request, serviceException,
                params, false);
    }

    /**
     * 单个请求，整体任务 成功
     */
    private void reportRequestSuccess(CosXmlRequest request, @Nullable Map<String, String> extra, boolean internal) {
        if(internal) return;

        try {
            HttpTaskMetrics taskMetrics = request.getMetrics();

            // 添加 Request 参数
            Map<String, String> params = parseCosXmlRequestParams(request);
            // 添加基础参数
            params.putAll(getCommonParams());
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
            // 添加性能参数
            params.putAll(parseHttpTaskMetricsParams(taskMetrics, params.get("request_name")));
            if(!TextUtils.isEmpty(request.getClientTraceId())){
                params.put("client_trace_id", request.getClientTraceId());
            }
            QCloudTrackService.getInstance().report(EVENT_CODE_TRACK_COS_SDK, params);

            sonarHosts.add(
                    new SonarHost(
                            params.get("host"),
                            params.get("region"),
                            params.get("bucket")
                    )
            );
        } catch (Exception e) {
            if(IS_DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上报客户端异常 单个请求，整体任务
     */
    private CosXmlClientException reportClientException(CosXmlRequest request, QCloudClientException clientException, @Nullable Map<String, String> extra, boolean internal) {
        ReturnClientException returnClientException = getClientExceptionParams(clientException);
        try {
            if (!internal && isReport(returnClientException.exception)) {
                HttpTaskMetrics taskMetrics = request.getMetrics();

                // 添加 Request 参数
                Map<String, String> params = parseCosXmlRequestParams(request);
                // 添加基础参数
                params.putAll(getCommonParams());
                // 添加错误信息
                params.putAll(returnClientException.params);
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
                // 添加性能参数
                params.putAll(parseHttpTaskMetricsParams(taskMetrics, params.get("request_name")));
                if(!TextUtils.isEmpty(request.getClientTraceId())){
                    params.put("client_trace_id", request.getClientTraceId());
                }
                QCloudTrackService.getInstance().report(EVENT_CODE_TRACK_COS_SDK, params);

                // 客户端网络异常sonar
                if(returnClientException.exception.errorCode == ClientErrorCode.POOR_NETWORK.getCode() ||
                        returnClientException.exception.errorCode == ClientErrorCode.IO_ERROR.getCode()){
                    if(sonarService != null){
                        sonarService.failSonar(
                                params.get("host"),
                                params.get("region"),
                                params.get("bucket"),
                                request.getClientTraceId()
                        );
                    }
                }
            }
        } catch (Exception e) {
            if(IS_DEBUG) {
                e.printStackTrace();
            }
        }
        return returnClientException.exception;
    }

    /**
     * 上报服务端异常 单个请求，整体任务
     */
    private CosXmlServiceException reportServiceException(CosXmlRequest request, QCloudServiceException serviceException, @Nullable Map<String, String> extra, boolean internal) {
        ReturnServiceException returnServiceException = getServiceExceptionParams(serviceException);
        try {
            if (!internal && isReport(returnServiceException.exception)) {
                // 添加 Request 参数
                Map<String, String> params = parseCosXmlRequestParams(request);
                // 添加基础参数
                params.putAll(getCommonParams());
                // 添加错误信息
                params.putAll(returnServiceException.params);
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
                // 添加性能参数
                params.putAll(parseHttpTaskMetricsParams(request.getMetrics(), params.get("request_name")));
                if(!TextUtils.isEmpty(request.getClientTraceId())){
                    params.put("client_trace_id", request.getClientTraceId());
                }
                QCloudTrackService.getInstance().report(EVENT_CODE_TRACK_COS_SDK, params);

                // 服务端网络异常sonar
                if(returnServiceException.exception != null && ("RequestTimeout".equals(returnServiceException.exception.getErrorCode()) ||
                        "UserNetworkTooSlow".equals(returnServiceException.exception.getErrorCode()))){
                    if(sonarService != null){
                        sonarService.failSonar(
                            params.get("host"),
                            params.get("region"),
                            params.get("bucket"),
                            request.getClientTraceId()
                        );
                    }
                }
            }
        } catch (Exception e){
            if(IS_DEBUG) {
                e.printStackTrace();
            }
        }
        return returnServiceException.exception;
    }

    /**
     * 获取http性能数据基础字段
     */
    private Map<String, String> parseHttpTaskMetricsBaseParams(@Nullable HttpTaskMetrics taskMetrics, String requestName) {
        Map<String, String> params = new HashMap<>();

        if (taskMetrics == null) {
            return params;
        }

        params.put("http_took_time", String.valueOf(taskMetrics.httpTaskFullTime()));
        params.put("http_dns", String.valueOf(taskMetrics.dnsLookupTookTime()));
        params.put("http_connect", String.valueOf(taskMetrics.connectTookTime()));
        params.put("http_secure_connect", String.valueOf(taskMetrics.secureConnectTookTime()));
        params.put("http_read_header", String.valueOf(taskMetrics.readResponseHeaderTookTime()));
        params.put("http_read_body", String.valueOf(taskMetrics.readResponseBodyTookTime()));
        params.put("http_write_header", String.valueOf(taskMetrics.writeRequestHeaderTookTime()));
        params.put("http_write_body", String.valueOf(taskMetrics.writeRequestBodyTookTime()));
        if("UploadTask".equalsIgnoreCase(requestName) || "CopyTask".equalsIgnoreCase(requestName)){
            params.put("http_size", String.valueOf(taskMetrics.requestBodyByteCount()));
            // 速度 每秒传输的数据大小 kb为单位
            params.put("http_speed", String.valueOf((taskMetrics.requestBodyByteCount()/1024d) / taskMetrics.httpTaskFullTime()));
        } else if("DownloadTask".equalsIgnoreCase(requestName)){
            params.put("http_size", String.valueOf(taskMetrics.responseBodyByteCount()));
            // 速度 每秒传输的数据大小 kb为单位
            params.put("http_speed", String.valueOf((taskMetrics.responseBodyByteCount()/1024d) / taskMetrics.httpTaskFullTime()));
        } else {
            params.put("http_size", String.valueOf(taskMetrics.requestBodyByteCount() + taskMetrics.responseBodyByteCount()));
            // 速度 每秒传输的数据大小 kb为单位
            params.put("http_speed", String.valueOf(((taskMetrics.requestBodyByteCount() + taskMetrics.responseBodyByteCount())/1024d) / taskMetrics.httpTaskFullTime()));
        }
        params.put("http_connect_ip", taskMetrics.getConnectAddress() != null ? taskMetrics.getConnectAddress().getHostAddress() : "null");
        params.put("http_dns_ips", taskMetrics.getRemoteAddress() != null ? taskMetrics.getRemoteAddress().toString() : "null");
        return params;
    }

    /**
     * 获取http性能数据字段
     */
    private Map<String, String> parseHttpTaskMetricsParams(@Nullable HttpTaskMetrics taskMetrics, String requestName) {
        Map<String, String> params = new HashMap<>();

        if (taskMetrics == null) {
            return params;
        }

        params.putAll(parseHttpTaskMetricsBaseParams(taskMetrics, requestName));

        params.put("http_md5", String.valueOf(taskMetrics.calculateMD5STookTime()));
        params.put("http_sign", String.valueOf(taskMetrics.signRequestTookTime()));
        params.put("http_full_time", String.valueOf(taskMetrics.fullTaskTookTime()));
        params.put("http_retry_times", String.valueOf(taskMetrics.getRetryCount()));
        return params;
    }

    /**
     * 获取request数据基础字段
     */
    private Map<String, String> parseCosXmlRequestBaseParams(CosXmlRequest request) {
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
            }
        }

        String host = parseHost(request);
        if (!TextUtils.isEmpty(host)) {
            params.put("host", host);
            try {
                Pattern pattern = Pattern.compile(".*\\.cos\\.(.*)\\.myqcloud.com");
                Matcher matcher = pattern.matcher(host);
                if (matcher.find()) {
                    String findRegion = matcher.group(1);
                    if(!TextUtils.isEmpty(findRegion)) {
                        params.put("region", findRegion);
                    }
                }
            } catch (Exception e) {
            }
        }
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

        params.putAll(parseCosXmlRequestBaseParams(request));

        if (request.getHttpTask() != null) {
            if (request.getHttpTask().request() != null) {
                String ua = request.getHttpTask().request().header(HttpConstants.Header.USER_AGENT);
                if (!TextUtils.isEmpty(ua)) {
                    params.put("user_agent", ua);
                }
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
        params.put("sdk_version_name", VersionInfo.getVersionName());
        params.put("sdk_version_code", String.valueOf(VersionInfo.getVersionCode()));
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
        return host;
    }

    private static @NonNull String getRequestName(CosXmlRequest request) {
        String request_name;
        if(request instanceof BasePutObjectRequest){
            request_name = "UploadTask";
        } else if(request instanceof GetObjectRequest || request instanceof GetObjectBytesRequest){
            request_name = "DownloadTask";
        } else if("CopyObjectRequest".equalsIgnoreCase(request.getClass().getSimpleName())){
            request_name = "CopyTask";
        } else {
            request_name = request.getClass().getSimpleName();
        }
        return request_name;
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

    public CosXmlServiceException convertServerException(QCloudServiceException e) {
        return e instanceof CosXmlServiceException ? (CosXmlServiceException) e
                : new CosXmlServiceException(e);
    }

    public CosXmlClientException convertClientException(QCloudClientException e) {
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

    public static class SonarHostsRandomQueue {
        private final List<SonarHost> list;
        private final int maxSize;
        private final Random random;
        // sonarHostsAdd时的时间戳，也就是最后一次cos成功操作的时间戳，用来计算是否停止探测
        private long sonarHostsAddTimestamp;

        public SonarHostsRandomQueue(int size) {
            this.list = new LinkedList<>();
            this.maxSize = size;
            this.random = new Random();
        }

        public void add(SonarHost sonarHost) {
            if (list.size() >= maxSize) {
                // 如果列表已满，移除最早插入的元素
                list.remove(0);
            }
            list.add(sonarHost);
            sonarHostsAddTimestamp = System.currentTimeMillis();
        }

        public long getSonarHostsAddTimestamp() {
            return sonarHostsAddTimestamp;
        }

        public SonarHost get() {
            if (list.isEmpty()) {
                return null;
            }
            // 获取一个随机元素
            int index = random.nextInt(list.size());
            return list.get(index);
        }
    }

    public static class SonarHost {
        private final String host;
        private final String region;
        private final String bucket;

        public SonarHost(String host, String region, String bucket) {
            this.host = host;
            this.region = region;
            this.bucket = bucket;
        }

        public String getHost() {
            return host;
        }

        public String getRegion() {
            return region;
        }

        public String getBucket() {
            return bucket;
        }
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
            if(IS_DEBUG) {
                e.printStackTrace();
            }
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
