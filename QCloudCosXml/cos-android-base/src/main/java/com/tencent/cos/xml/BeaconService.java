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

import com.tencent.beacon.core.info.BeaconPubParams;
import com.tencent.beacon.event.open.BeaconReport;
import com.tencent.cos.xml.base.BuildConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.object.ObjectRequest;
import com.tencent.cos.xml.transfer.TransferTaskMetrics;
import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.ConnectionRepository;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.track.TrackService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLHandshakeException;

/**
 * 灯塔服务
 */
public class BeaconService {
    private static final String TAG = "BeaconProxy";
    private static final String APP_KEY = "0AND0VEVB24UBGDU";
    private static final boolean IS_DEBUG = BuildConfig.DEBUG;

    private static final String EVENT_CODE_BASE_SERVICE = "base_service";
    private static final String EVENT_CODE_DOWNLOAD = "cos_download";
    private static final String EVENT_CODE_UPLOAD = "cos_upload";
    private static final String EVENT_CODE_COPY = "cos_copy";
    private static final String EVENT_CODE_ERROR = "cos_error";

    private static final String EVENT_PARAMS_SUCCESS = "Success";
    private static final String EVENT_PARAMS_FAILURE = "Failure";
    private static final String EVENT_PARAMS_SERVER = "Server";
    private static final String EVENT_PARAMS_CLIENT = "Client";

    public static final String EVENT_PARAMS_NODE_HEAD = "HeadObjectRequest";
    public static final String EVENT_PARAMS_NODE_GET = "GetObjectRequest";

    private Context applicationContext;
    private static BeaconService instance;

    //是否关闭灯塔上报
    private boolean isCloseBeacon;
    //上报桥接来源
    private String bridge;

    private BeaconService(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化
     */
    public static void init(Context applicationContext, boolean isCloseBeacon, String bridge) {
        synchronized (BeaconService.class) {
            if (instance == null) {
                instance = new BeaconService(applicationContext);
                instance.isCloseBeacon = isCloseBeacon;
                instance.bridge = bridge;
                TrackService.init(applicationContext, APP_KEY, IS_DEBUG, isCloseBeacon);
            }
        }
    }

    public static BeaconService getInstance() {
        return instance;
    }

    public void reportRequestSuccess(CosXmlRequest request) {
        reportRequestSuccess(parseEventCode(request), request, null);
    }

    public CosXmlClientException reportRequestClientException(CosXmlRequest request, QCloudClientException clientException) {
        return reportClientException(parseEventCode(request), request, clientException, null);
    }

    public CosXmlServiceException reportRequestServiceException(CosXmlRequest request, QCloudServiceException serviceException) {
        return reportServiceException(parseEventCode(request), request, serviceException, null);
    }

    public void reportUploadTaskSuccess(CosXmlRequest request) {
        // 只需要一个 PutObjectRequest 壳，带上 HttpTaskMetrics 信息
        reportRequestSuccess(EVENT_CODE_UPLOAD, request,
                Collections.singletonMap("name", "UploadTask"));
    }

    public void reportUploadTaskClientException(CosXmlRequest request, QCloudClientException clientException) {
        reportClientException(EVENT_CODE_UPLOAD, request, clientException,
                createTransferExtra("UploadTask", request));
    }

    public void reportUploadTaskServiceException(CosXmlRequest request, QCloudServiceException serviceException) {
        reportServiceException(EVENT_CODE_UPLOAD, request, serviceException,
                createTransferExtra("UploadTask", request));
    }

    public void reportDownloadTaskSuccess(CosXmlRequest request) {
        // 只需要一个 GetObjectRequest 壳，带上 HttpTaskMetrics 信息
        reportRequestSuccess(EVENT_CODE_DOWNLOAD, request,
                Collections.singletonMap("name", "DownloadTask"));
    }

    public void reportDownloadTaskClientException(CosXmlRequest request, QCloudClientException clientException) {
        reportClientException(EVENT_CODE_DOWNLOAD, request, clientException,
                createTransferExtra("DownloadTask", request));
    }

    public void reportDownloadTaskServiceException(CosXmlRequest request, QCloudServiceException serviceException) {
        reportServiceException(EVENT_CODE_DOWNLOAD, request, serviceException,
                createTransferExtra("DownloadTask", request));
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
//                Collections.singletonMap("name", cosUploadName(cse)));
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
//                Collections.singletonMap("name", cosDownloadName(cse)));
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

    public void reportCopyTaskSuccess(CosXmlRequest request) {
        // 只需要一个 CopyObjectRequest 壳，带上 HttpTaskMetrics 信息
        reportRequestSuccess(EVENT_CODE_COPY, request,
                Collections.singletonMap("name", "CopyTask"));
    }

    public void reportCopyTaskClientException(CosXmlRequest request, CosXmlClientException clientException) {
        reportClientException(EVENT_CODE_COPY, request, clientException,
                createTransferExtra("CopyTask", request));
    }

    public void reportCopyTaskServiceException(CosXmlRequest request, CosXmlServiceException serviceException) {
        reportServiceException(EVENT_CODE_COPY, request, serviceException,
                createTransferExtra("CopyTask", request));
    }

    private Map<String, String> createTransferExtra(String name, CosXmlRequest request) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("error_node", request != null ? request.getClass().getSimpleName() : "null");
        return params;
    }

    public void reportTransferSuccess(CosXmlRequest request, TransferTaskMetrics taskMetrics, boolean encrypted) {

        if (isReport(request)) {
            reportTransferTask(request, taskMetrics, encrypted, true, null);
        }
    }

    /**
     * 上报客户端异常 单个请求，整体任务
     *
     * 失败：
     * 1. host
     * 2. region
     * 3. 失败原因
     * 4. errorcode
     * 5. dns解析时间，建联时间，tls建联时间，ip列表，服务端ip，读写时间，请求整体耗时
     */
    public void reportTransferClientException(CosXmlRequest request, TransferTaskMetrics taskMetrics, CosXmlClientException clientException, boolean encrypted) {

        if (isReport(clientException) && isReport(request)) {
            reportTransferTask(request, taskMetrics, encrypted, false, parseClientExceptionParams(clientException));
        }
    }

    /**
     * 上报服务端异常 单个请求，整体任务
     *
     * 失败：
     * 1. host
     * 2. region
     * 3. 失败原因
     * 4. errorcode
     * 5. dns解析时间，建联时间，tls建联时间，ip列表，服务端ip，读写时间，请求整体耗时
     * 6. requestid
     */
    public void reportTransferServiceException(CosXmlRequest request, TransferTaskMetrics taskMetrics, CosXmlServiceException serviceException, boolean encrypted) {

        if (isReport(serviceException) && isReport(request)) {
            reportTransferTask(request, taskMetrics, encrypted, false, parseServiceExceptionParams(serviceException));
        }
    }


    private void reportTransferTask(CosXmlRequest request, TransferTaskMetrics taskMetrics, boolean encrypted,
                                    boolean isSuccess, @Nullable Map<String, String> extras) {

        Map<String, String> params = parseUrlParams(request);
        // 添加服务名称
        params.put("name", request.getClass().getSimpleName());
        // 添加基础参数
        params.putAll(getCommonParams());
        // 添加性能参数
        params.putAll(parseSimplePerfParams(taskMetrics));
        // 是否加密传输
        params.put("encrypted", String.valueOf(encrypted));
        // 添加请求结果
        params.put("result", isSuccess ? EVENT_PARAMS_SUCCESS : EVENT_PARAMS_FAILURE);
        if (extras != null) {
            params.putAll(extras);
        }
        report("cos_transfer", params);
    }

    /**
     * 单个请求，整体任务
     *
     * 成功：
     *
     * 1. host
     * 2. region
     * 3. length
     * 4. time
     */
    private void reportRequestSuccess(String eventCode, CosXmlRequest request, @Nullable Map<String, String> extra) {

        if(isReport(request)) {
            HttpTaskMetrics taskMetrics = request.getMetrics();

            // 添加 host region
            Map<String, String> params = parseUrlParams(request);
            // 添加基础参数
            params.putAll(getCommonParams());
            // 添加简单性能参数
            params.putAll(parseSimplePerfParams(taskMetrics));
            // 添加服务名称
            if (extra == null || !extra.containsKey("name")) {
                params.put("name", request.getClass().getSimpleName());
            }
            // 添加请求结果
            params.put("result", EVENT_PARAMS_SUCCESS);
            // 添加额外参数
            if (extra != null) {
                params.putAll(extra);
            }
            report(eventCode, params);
        }
    }

    /**
     * 上报客户端异常 单个请求，整体任务
     *
     * 失败：
     * 1. host
     * 2. region
     * 3. 失败原因
     * 4. errorcode
     * 5. dns解析时间，建联时间，tls建联时间，ip列表，服务端ip，读写时间，请求整体耗时
     */
    private CosXmlClientException reportClientException(String eventCode, CosXmlRequest request, QCloudClientException clientException, @Nullable Map<String, String> extra) {

        ReturnClientException returnClientException = getClientExceptionParams(clientException);
        if (isReport(returnClientException.exception) && isReport(request)) {

            HttpTaskMetrics taskMetrics = request.getMetrics();

            // 添加 host region key
            Map<String, String> params = parseUrlParams(request);
            // 添加基础参数
            params.putAll(getCommonParams());
            // 添加错误信息
            params.putAll(returnClientException.params);
            // 添加性能参数
            params.putAll(parsePerfParams(taskMetrics));
            // 添加 dns 解析
            params.putAll(parseDnsParams(request));
            // 添加服务名称
            if (extra == null || !extra.containsKey("name")) {
                params.put("name", request.getClass().getSimpleName());
            }
            // 添加请求结果
            params.put("result", EVENT_PARAMS_FAILURE);

            // 添加额外参数
            if (extra != null) {
                params.putAll(extra);
            }
            report(eventCode, params);
        }


        return returnClientException.exception;
    }

    /**
     * 上报服务端异常 单个请求，整体任务
     *
     * 失败：
     * 1. host
     * 2. region
     * 3. 失败原因
     * 4. errorcode
     * 5. dns解析时间，建联时间，tls建联时间，ip列表，服务端ip，读写时间，请求整体耗时
     * 6. requestid
     */
    private CosXmlServiceException reportServiceException(String eventCode, CosXmlRequest request, QCloudServiceException serviceException, @Nullable Map<String, String> extra) {

        ReturnServiceException returnServiceException = getServiceExceptionParams(serviceException);

        if (request instanceof ObjectRequest) {
            String path = ((ObjectRequest) request).getCosPath();
        }

        if (isReport(returnServiceException.exception) && isReport(request)) {
            // 添加 host region key
            Map<String, String> params = parseUrlParams(request);
            // 添加基础参数
            params.putAll(getCommonParams());
            // 添加错误信息
            params.putAll(returnServiceException.params);
            // 添加性能参数
            params.putAll(parsePerfParams(request.getMetrics()));
            // 添加 dns 解析
            params.putAll(parseDnsParams(request));
            // 添加服务名称
            if (extra == null || !extra.containsKey("name")) {
                params.put("name", request.getClass().getSimpleName());
            }
            // 添加请求结果
            params.put("result", EVENT_PARAMS_FAILURE);

            // 添加额外参数
            if (extra != null) {
                params.putAll(extra);
            }
            report(eventCode, params);
        }
        return returnServiceException.exception;
    }

    /**
     * 如果域名从来没有解析成功过，那么这里返回的结果为空，比如一个错误的域名
     * @param request
     * @return
     */
    private Map<String, String> parseDnsParams(CosXmlRequest request) {
        Map<String, String> params = new HashMap<>();
        String host = parseHost(request);

        if (TextUtils.isEmpty(host)) {
            return params;
        }

        HttpTaskMetrics taskMetrics = request.getMetrics();
        List<InetAddress> dns = null;
        try {
            dns = ConnectionRepository.getInstance().getDnsRecord(host);
        } catch (UnknownHostException ignored) {
        }
        params.put("ips", flatDns(taskMetrics.getConnectAddress(), dns));
        return params;
    }

    private String flatDns(@Nullable InetAddress connect, @Nullable List<InetAddress> dns) {

        if (connect != null && connect.getHostAddress() != null) {
            return String.format("{%s}", connect.getHostAddress());
        }
        return flatInetAddressList(dns);
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

    private Map<String, String> parsePerfParams(@Nullable HttpTaskMetrics taskMetrics) {

        Map<String, String> params = new HashMap<>();

        if (taskMetrics == null) {
            return params;
        }

        params.put("took_time", String.valueOf(taskMetrics.httpTaskFullTime()));
        params.put("http_dns", String.valueOf(taskMetrics.dnsLookupTookTime()));
        params.put("http_connect", String.valueOf(taskMetrics.connectTookTime()));
        params.put("http_secure_connect", String.valueOf(taskMetrics.secureConnectTookTime()));
        params.put("http_md5", String.valueOf(taskMetrics.calculateMD5STookTime()));
        params.put("http_sign", String.valueOf(taskMetrics.signRequestTookTime()));
        params.put("http_read_header", String.valueOf(taskMetrics.readResponseHeaderTookTime()));
        params.put("http_read_body", String.valueOf(taskMetrics.readResponseBodyTookTime()));
        params.put("http_write_header", String.valueOf(taskMetrics.writeRequestHeaderTookTime()));
        params.put("http_write_body", String.valueOf(taskMetrics.writeRequestBodyTookTime()));
        params.put("http_full", String.valueOf(taskMetrics.fullTaskTookTime()));
        params.put("size", String.valueOf(taskMetrics.requestBodyByteCount() + taskMetrics.responseBodyByteCount()));
        params.put("retry_times", String.valueOf(taskMetrics.getRetryCount()));
        //TODO dathub已经不能编辑上报字段 待升级datahub3.0
//        params.put("is_clock_skewed_retry", String.valueOf(taskMetrics.isClockSkewedRetry()));
        return params;
    }

    private Map<String, String> parseSimplePerfParams(@Nullable HttpTaskMetrics taskMetrics) {

        Map<String, String> params = new HashMap<>();
        if (taskMetrics == null) {
            return params;
        }

        params.put("took_time", String.valueOf(taskMetrics.httpTaskFullTime()));
        params.put("size", String.valueOf(taskMetrics.requestBodyByteCount() + taskMetrics.responseBodyByteCount()));
        params.put("retry_times", String.valueOf(taskMetrics.getRetryCount()));
        //TODO dathub已经不能编辑上报字段 待升级datahub3.0
//        params.put("is_clock_skewed_retry", String.valueOf(taskMetrics.isClockSkewedRetry()));
        return params;
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

    private Map<String, String> parseUrlParams(CosXmlRequest request) {
        Map<String, String> params = new HashMap<>();
        String host = parseHost(request); // taskMetrics.getDomainName();
        if (TextUtils.isEmpty(host)) {
            return params;
        }
        params.put("host", host);
        try {
            Pattern pattern = Pattern.compile(".*\\.cos\\.(.*)\\.myqcloud.com");
            Matcher matcher = pattern.matcher(host);
            if (matcher.find()) {
                params.put("region", matcher.group(1));
            }
        } catch (Exception e) {}

        if (request instanceof ObjectRequest &&
                !TextUtils.isEmpty(((ObjectRequest) request).getCosPath())) {
            params.put("request_path", ((ObjectRequest) request).getCosPath());
        }
        if(request.getHttpTask() != null){
            if(request.getHttpTask().request() != null){
                String ua = request.getHttpTask().request().header(HttpConstants.Header.USER_AGENT);
                if(!TextUtils.isEmpty(ua)) {
                    params.put("user_agent", ua);
                }
            }
        }
        return params;
    }

//    private BasePutObjectRequest emptyPutObjectRequestWithMetrics(HttpTaskMetrics taskMetrics) {
//        BasePutObjectRequest putObjectRequest = new BasePutObjectRequest("", "", "");
//        putObjectRequest.attachMetrics(taskMetrics);
//        return putObjectRequest;
//    }

//    private CopyObjectRequest emptyCopyObjectRequestWithMetrics(HttpTaskMetrics taskMetrics) {
//        CopyObjectRequest copyObjectRequest = new CopyObjectRequest("", "", null);
//        copyObjectRequest.attachMetrics(taskMetrics);
//        return copyObjectRequest;
//    }

//    private GetObjectRequest emptyGetObjectRequestWithMetrics(HttpTaskMetrics taskMetrics) {
//        GetObjectRequest getObjectRequest = new GetObjectRequest("", "", "");
//        getObjectRequest.attachMetrics(taskMetrics);
//        return getObjectRequest;
//    }

    private String parseEventCode(CosXmlRequest request) {

        String eventCode = EVENT_CODE_BASE_SERVICE;
        if (isUploadTaskRequest(request)) {
            eventCode = EVENT_CODE_UPLOAD;
        } else if (isDownloadTaskRequest(request)) {
            eventCode = EVENT_CODE_DOWNLOAD;
        }
        return eventCode;
    }

    private void report(String eventCode, Map<String, String> params) {
        if(isCloseBeacon || !TrackService.isIncludeBeacon()) return;
        TrackService.getInstance().track(APP_KEY, eventCode, params);
    }


    /**
     * 上报异常
     *
     * @param source 异常来源
     * @param e      其他异常
     */
    public void reportError(String source, Exception e) {
        Map<String, String> params = getCommonParams();
        params.put("source", source);
        params.put("name", e.getClass().getSimpleName());
        params.put("message", e.getMessage());
        report(EVENT_CODE_ERROR, params);
    }

//    private Map<String, String> getBaseServiceParams(CosXmlRequest cosXmlRequest, long tookTime, boolean isSuccess) {
//        Map<String, String> params = getCommonParams();
//
//        params.put("result", isSuccess ? EVENT_PARAMS_SUCCESS : EVENT_PARAMS_FAILURE);
//        params.put("took_time", String.valueOf(tookTime));
//        params.put("name", cosXmlRequest.getClass().getSimpleName());
//        params.put("region", TextUtils.isEmpty(cosXmlRequest.getRegion()) ? this.config.getRegion() : cosXmlRequest.getRegion());
//        params.put("accelerate", cosXmlRequest.isSupportAccelerate() ? "Y" : "N");
//
//        if (!isSuccess) {
//            HttpTaskMetrics metrics = cosXmlRequest.getMetrics();
//            if (metrics != null) {
//                params.put("http_dns", String.valueOf(metrics.dnsLookupTookTime()));
//                params.put("http_connect", String.valueOf(metrics.connectTookTime()));
//                params.put("http_secure_connect", String.valueOf(metrics.secureConnectTookTime()));
//                params.put("http_md5", String.valueOf(metrics.calculateMD5STookTime()));
//                params.put("http_sign", String.valueOf(metrics.signRequestTookTime()));
//                params.put("http_read_header", String.valueOf(metrics.readResponseHeaderTookTime()));
//                params.put("http_read_body", String.valueOf(metrics.readResponseBodyTookTime()));
//                params.put("http_write_header", String.valueOf(metrics.writeRequestHeaderTookTime()));
//                params.put("http_write_body", String.valueOf(metrics.writeRequestBodyTookTime()));
//                params.put("http_full", String.valueOf(metrics.fullTaskTookTime()));
//            }
//
//            String host = cosXmlRequest.getRequestHost(this.config);
//            if (host != null) {
//                params.put("host", host);
//                try {
//                    List<InetAddress> ips = ConnectionRepository.getInstance().getDnsRecord(host);
//                    params.put("ips", flatInetAddressList(ips));
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return params;
//    }

    private String flatInetAddressList(@Nullable List<InetAddress> ips) {
        if (ips == null) {
            return "{}";
        }

        StringBuilder ipString = new StringBuilder("{");
        int count = 0;
        for (InetAddress ip : ips) {
            ipString.append(ip.getHostAddress());
            boolean isLast = ++count == ips.size();
            if (isLast) {
                ipString.append("}");
            } else {
                ipString.append(",");
            }
        }
        return ipString.toString();
    }

//    private String getConnectIp(@Nullable InetSocketAddress socketAddress) {
//        if (socketAddress == null || socketAddress.getAddress() == null) {
//            return "";
//        }
//        return socketAddress.getAddress().getHostAddress();
//    }
//
//    private Map<String, String> getDownloadParams(String region, boolean isSuccess) {
//        Map<String, String> params = getCommonParams();
//        params.put("result", isSuccess ? EVENT_PARAMS_SUCCESS : EVENT_PARAMS_FAILURE);
//        params.put("region", region);
//        return params;
//    }
//
//    private Map<String, String> getUploadParams(String region, boolean isSuccess) {
//        Map<String, String> params = getCommonParams();
//        params.put("result", isSuccess ? EVENT_PARAMS_SUCCESS : EVENT_PARAMS_FAILURE);
//        params.put("region", region);
//        return params;
//    }

    /**
     * 获取公共参数
     *
     * @return 公共参数
     */
    private Map<String, String> getCommonParams() {
        if(isCloseBeacon || !TrackService.isIncludeBeacon()) return new HashMap<>();

        Map<String, String> params = new HashMap<>();
        BeaconPubParams pubParams = BeaconReport.getInstance().getCommonParams(applicationContext);
        params.put("boundle_id", pubParams.getBoundleId());
        params.put("network_type", pubParams.getNetworkType());
        params.put("cossdk_version", com.tencent.cos.xml.base.BuildConfig.VERSION_NAME);
        params.put("cossdk_version_code", String.valueOf(com.tencent.cos.xml.base.BuildConfig.VERSION_CODE));
        if(!TextUtils.isEmpty(bridge)) {
            params.put("bridge", bridge);
        }
        return params;
    }

    /**
     * 获取服务端异常参数
     */
    private ReturnServiceException getServiceExceptionParams(QCloudServiceException e) {
        Map<String, String> params = new HashMap<>();
        CosXmlServiceException serviceException = convertServerException(e);
        params.put("error_request_id", serviceException.getRequestId());
        params.put("error_message", serviceException.getErrorMessage());
        params.put("error_code", serviceException.getErrorCode());
        params.put("error_status_code", String.valueOf(serviceException.getStatusCode()));
        params.put("error_service_name", serviceException.getServiceName());
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

    /**
     * 获取客户端异常参数
     */
    private ReturnClientException getClientExceptionParams(QCloudClientException e) {
        Map<String, String> params = new HashMap<>();
        CosXmlClientException xmlClientException = convertClientException(e);
        String name = xmlClientException.getCause() == null ? xmlClientException.getClass().getSimpleName() : xmlClientException.getCause().getClass().getSimpleName();
        String message = xmlClientException.getCause() == null ? xmlClientException.getMessage() : xmlClientException.getCause().getMessage();
        params.put("error_name", name);
        params.put("error_message", message);
        params.put("error_code", String.valueOf(xmlClientException.errorCode));
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
        //网络未连接的情况
        if (!TextUtils.isEmpty(e.getMessage()) &&
                e.getMessage().contains("NetworkNotConnected")) {
            return new CosXmlClientException(ClientErrorCode.NETWORK_NOT_CONNECTED.getCode(), e);
        }

        CosXmlClientException xmlClientException;
        if (e instanceof CosXmlClientException) {
            xmlClientException = (CosXmlClientException) e;
            if (e.getCause() instanceof IOException) {
                xmlClientException = new CosXmlClientException(subdivisionIOException(e.getCause()), e);
            }
        } else {
            Throwable causeException = e.getCause();
            if (causeException instanceof IllegalArgumentException) {
                xmlClientException = new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
            } else if (causeException instanceof QCloudAuthenticationException) {
                xmlClientException = new CosXmlClientException(ClientErrorCode.INVALID_CREDENTIALS.getCode(), e);
            } else if (causeException instanceof IOException) {
                xmlClientException = new CosXmlClientException(subdivisionIOException(causeException), e);
            } else {
                xmlClientException = new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
            }
        }
        return xmlClientException;
    }

    /**
     * 细分IO异常
     */
    private int subdivisionIOException(Throwable causeException) {
        //细分其他IO异常
        if (causeException instanceof FileNotFoundException) {
            return ClientErrorCode.SINK_SOURCE_NOT_FOUND.getCode();
        } else if (causeException instanceof UnknownHostException) {
            return PoorNetworkCode.UnknownHostException;
        } else if (causeException instanceof SocketTimeoutException) {
            return PoorNetworkCode.SocketTimeoutException;
        } else if (causeException instanceof ConnectException) {
            return PoorNetworkCode.ConnectException;
        } else if (causeException instanceof HttpRetryException) {
            return PoorNetworkCode.HttpRetryException;
        } else if (causeException instanceof NoRouteToHostException) {
            return PoorNetworkCode.NoRouteToHostException;
        } else if (causeException instanceof SSLHandshakeException && !(causeException.getCause() instanceof CertificateException)) {
            return PoorNetworkCode.SSLHandshakeException;
        } else {
            return ClientErrorCode.IO_ERROR.getCode();
        }
    }

    /**
     * 以下请求不计算在base service中（因为已经包含在上传 下载 复制事件中）
     * @param cosXmlRequest cos请求
     * @return 是否上报
     */
    private boolean isReport(CosXmlRequest cosXmlRequest) {
        return true;
    }


    private boolean isDownloadTaskRequest(CosXmlRequest cosXmlRequest) {

        String requestName = cosXmlRequest.getClass().getSimpleName();
        return "HeadObjectRequest".equals(requestName) ||
                "GetObjectRequest".equals(requestName);
    }

    private boolean isUploadTaskRequest(CosXmlRequest cosXmlRequest) {

        String requestName = cosXmlRequest.getClass().getSimpleName();
        return "PutObjectRequest".equals(requestName) ||
                "InitMultipartUploadRequest".equals(requestName) ||
                "ListPartsRequest".equals(requestName) ||
                "UploadPartRequest".equals(requestName) ||
                "CompleteMultiUploadRequest".equals(requestName) ||
                "AbortMultiUploadRequest".equals(requestName);
    }


//    // 这里有点问题，复制任务还包括了一部分分片上传的请求
//    private boolean isCopyTaskRequest(CosXmlRequest cosXmlRequest) {
//
//        String requestName = cosXmlRequest.getClass().getSimpleName();
//        return "UploadPartCopyRequest".equals(requestName) ||
//                "CopyObjectRequest".equals(requestName);
//    }

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
     * POOR_NETWORK细分code，用于灯塔后台统计筛选
     */
    private static class PoorNetworkCode {
        private static final int UnknownHostException = 200032;
        private static final int SocketTimeoutException = 200033;
        private static final int ConnectException = 200034;
        private static final int HttpRetryException = 200035;
        private static final int NoRouteToHostException = 200036;
        private static final int SSLHandshakeException = 200037;
    }
}
