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

import com.tencent.beacon.core.info.BeaconPubParams;
import com.tencent.beacon.event.open.BeaconConfig;
import com.tencent.beacon.event.open.BeaconEvent;
import com.tencent.beacon.event.open.BeaconReport;
import com.tencent.beacon.event.open.EventResult;
import com.tencent.beacon.event.open.EventType;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.DnsRepository;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

/**
 * 灯塔服务
 */
public class BeaconService {
    private static final String TAG = "BeaconProxy";
    //联调APP Key：LOGDEBUGKEY00247
//    private static final String APP_KEY = "LOGDEBUGKEY00247";
        private static final String APP_KEY = "0AND0VEVB24UBGDU";
    private static final boolean IS_DEBUG = false;

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
    private CosXmlServiceConfig config;
    private static BeaconService instance;

    private BeaconService(Context applicationContext, CosXmlServiceConfig config) {
        this.applicationContext = applicationContext;
        this.config = config;
    }

    /**
     * 初始化
     */
    public static void init(Context applicationContext, CosXmlServiceConfig serviceConfig) {
        synchronized (BeaconService.class) {
            if (instance == null) {
                instance = new BeaconService(applicationContext, serviceConfig);

                BeaconConfig config = BeaconConfig.builder()
                        .auditEnable(false)
                        .bidEnable(false)
                        .collectMACEnable(false)
                        .collectIMEIEnable(false)
                        .pagePathEnable(false)
                        .build();
                BeaconReport beaconReport = BeaconReport.getInstance();
                beaconReport.setLogAble(IS_DEBUG);//是否打开日志
                beaconReport.start(applicationContext, APP_KEY, config);
            }
        }
    }

    public static BeaconService getInstance() {
        return instance;
    }

    private void report(String eventcode, Map<String, String> params) {
        BeaconEvent.Builder builder = BeaconEvent.builder()
                .withAppKey(APP_KEY)
                .withCode(eventcode)
                .withType(EventType.NORMAL)
                .withParams(params);
        try {
            builder.withIsSimpleParams(true);
        } catch (NoSuchMethodError error){
            //APP使用了标准版的灯塔SDK 不支持withIsSimpleParams
        }
        EventResult result = BeaconReport.getInstance().report(builder.build());
        QCloudLogger.d(TAG, "EventResult{ eventID:" + result.eventID + ", errorCode: " + result.errorCode + ", errorMsg: " + result.errMsg + "}");
    }

    /*--------------------------reportBaseService-----------------------------*/
    /**
     * 上报基础服务成功事件
     *
     * @param cosXmlRequest cos请求
     * @param tookTime      耗时
     */
    public void reportBaseService(CosXmlRequest cosXmlRequest, long tookTime) {
        if(isReport(cosXmlRequest)) {
            Map<String, String> params = getBaseServiceParams(cosXmlRequest, tookTime, true);
            report(EVENT_CODE_BASE_SERVICE, params);
        }
    }

    /**
     * 上报基础服务客户端异常事件
     *
     * @param cosXmlRequest cos请求
     * @param tookTime      耗时
     * @param e             客户端异常
     * @return 客户端异常
     */
    public CosXmlClientException reportBaseService(CosXmlRequest cosXmlRequest, long tookTime, QCloudClientException e) {
        ReturnClientException returnClientException = getClientExceptionParams(e);
        if (isReport(returnClientException.exception) && isReport(cosXmlRequest)) {
            Map<String, String> params = getBaseServiceParams(cosXmlRequest, tookTime, false);
            params.putAll(returnClientException.params);
            report(EVENT_CODE_BASE_SERVICE, params);
        }
        return returnClientException.exception;
    }

    /**
     * 上报基础服务服务端异常事件
     *
     * @param cosXmlRequest cos请求
     * @param tookTime      耗时
     * @param e             服务端异常
     * @return 服务端异常
     */
    public CosXmlServiceException reportBaseService(CosXmlRequest cosXmlRequest, long tookTime, QCloudServiceException e) {
        ReturnServiceException returnServiceException = getServiceExceptionParams(e);
        if (isReport(returnServiceException.exception) && isReport(cosXmlRequest)) {
            Map<String, String> params = getBaseServiceParams(cosXmlRequest, tookTime, false);
            params.putAll(returnServiceException.params);
            report(EVENT_CODE_BASE_SERVICE, params);
        }
        return returnServiceException.exception;
    }
    /*--------------------------reportBaseService-----------------------------*/

    /*--------------------------reportDownload-----------------------------*/
    public void reportDownload(String region, long size, long tookTime) {
        Map<String, String> params = getDownloadParams(region, true);
        params.put("took_time", String.valueOf(tookTime));
        params.put("size", String.valueOf(size));
        report(EVENT_CODE_DOWNLOAD, params);
    }

    public void reportDownload(String region, String error_node, QCloudClientException e) {
        ReturnClientException returnClientException = getClientExceptionParams(e);
        if (isReport(returnClientException.exception)) {
            Map<String, String> params = getDownloadParams(region, false);
            params.putAll(returnClientException.params);
            params.put("error_node", error_node);
            report(EVENT_CODE_DOWNLOAD, params);
        }
    }

    public void reportDownload(String region, String error_node, QCloudServiceException e) {
        ReturnServiceException returnServiceException = getServiceExceptionParams(e);
        if (isReport(returnServiceException.exception)) {
            Map<String, String> params = getDownloadParams(region, false);
            params.putAll(returnServiceException.params);
            params.put("error_node", error_node);
            report(EVENT_CODE_DOWNLOAD, params);
        }
    }
    /*--------------------------reportDownload-----------------------------*/

    /*--------------------------reportUpload-----------------------------*/
    public void reportUpload(String region, long size, long tookTime) {
        Map<String, String> params = getUploadParams(region, true);
        params.put("took_time", String.valueOf(tookTime));
        params.put("size", String.valueOf(size));
        report(EVENT_CODE_UPLOAD, params);
    }

    public void reportUpload(String region, String error_node, QCloudClientException e) {
        ReturnClientException returnClientException = getClientExceptionParams(e);
        if (isReport(returnClientException.exception)) {
            Map<String, String> params = getUploadParams(region, false);
            params.putAll(returnClientException.params);
            params.put("error_node", error_node);
            report(EVENT_CODE_UPLOAD, params);
        }
    }

    public void reportUpload(String region, String error_node, QCloudServiceException e) {
        ReturnServiceException returnServiceException = getServiceExceptionParams(e);
        if (isReport(returnServiceException.exception)) {
            Map<String, String> params = getUploadParams(region, false);
            params.putAll(returnServiceException.params);
            params.put("error_node", error_node);
            report(EVENT_CODE_UPLOAD, params);
        }
    }
    /*--------------------------reportUpload-----------------------------*/

    /*--------------------------reportCopy-----------------------------*/
    public void reportCopy(String region) {
        Map<String, String> params = getUploadParams(region, true);
        report(EVENT_CODE_COPY, params);
    }

    public void reportCopy(String region, String error_node, QCloudClientException e) {
        ReturnClientException returnClientException = getClientExceptionParams(e);
        if (isReport(returnClientException.exception)) {
            Map<String, String> params = getUploadParams(region, false);
            params.putAll(returnClientException.params);
            params.put("error_node", error_node);
            report(EVENT_CODE_COPY, params);
        }
    }

    public void reportCopy(String region, String error_node, QCloudServiceException e) {
        ReturnServiceException returnServiceException = getServiceExceptionParams(e);
        if (isReport(returnServiceException.exception)) {
            Map<String, String> params = getUploadParams(region, false);
            params.putAll(returnServiceException.params);
            params.put("error_node", error_node);
            report(EVENT_CODE_COPY, params);
        }
    }
    /*--------------------------reportCopy---------------------------*/

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

    private Map<String, String> getBaseServiceParams(CosXmlRequest cosXmlRequest, long tookTime, boolean isSuccess) {
        Map<String, String> params = getCommonParams();

        params.put("result", isSuccess ? EVENT_PARAMS_SUCCESS : EVENT_PARAMS_FAILURE);
        params.put("took_time", String.valueOf(tookTime));
        params.put("name", cosXmlRequest.getClass().getSimpleName());
        params.put("region", TextUtils.isEmpty(cosXmlRequest.getRegion()) ? this.config.getRegion() : cosXmlRequest.getRegion());
        params.put("accelerate", cosXmlRequest.isSupportAccelerate() ? "Y" : "N");

        if (!isSuccess) {
            HttpTaskMetrics metrics = cosXmlRequest.getMetrics();
            if (metrics != null) {
                params.put("http_dns", String.valueOf(metrics.dnsLookupTookTime()));
                params.put("http_connect", String.valueOf(metrics.connectTookTime()));
                params.put("http_secure_connect", String.valueOf(metrics.secureConnectTookTime()));
                params.put("http_md5", String.valueOf(metrics.calculateMD5STookTime()));
                params.put("http_sign", String.valueOf(metrics.signRequestTookTime()));
                params.put("http_read_header", String.valueOf(metrics.readResponseHeaderTookTime()));
                params.put("http_read_body", String.valueOf(metrics.readResponseBodyTookTime()));
                params.put("http_write_header", String.valueOf(metrics.writeRequestHeaderTookTime()));
                params.put("http_write_body", String.valueOf(metrics.writeRequestBodyTookTime()));
                params.put("http_full", String.valueOf(metrics.fullTaskTookTime()));
            }

            String host = cosXmlRequest.getRequestHost(this.config);
            if (host != null) {
                params.put("host", host);
                try {
                    StringBuilder ipString = new StringBuilder();
                    List<InetAddress> ips = DnsRepository.getInstance().getDnsRecord(host);
                    for (InetAddress ip : ips) {
                        ipString.append(ip.getHostAddress());
                        ipString.append(",");
                    }
                    params.put("ips", ipString.toString());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        return params;
    }

    private Map<String, String> getDownloadParams(String region, boolean isSuccess) {
        Map<String, String> params = getCommonParams();
        params.put("result", isSuccess ? EVENT_PARAMS_SUCCESS : EVENT_PARAMS_FAILURE);
        params.put("region", region);
        return params;
    }

    private Map<String, String> getUploadParams(String region, boolean isSuccess) {
        Map<String, String> params = getCommonParams();
        params.put("result", isSuccess ? EVENT_PARAMS_SUCCESS : EVENT_PARAMS_FAILURE);
        params.put("region", region);
        return params;
    }

    /**
     * 获取公共参数
     *
     * @return 公共参数
     */
    private Map<String, String> getCommonParams() {
        Map<String, String> params = new HashMap<>();
        BeaconPubParams pubParams = BeaconReport.getInstance().getCommonParams(applicationContext);
        params.put("boundle_id", pubParams.getBoundleId());
        params.put("network_type", pubParams.getNetworkType());
        params.put("cossdk_version", com.tencent.cos.xml.BuildConfig.VERSION_NAME);
        return params;
    }

    /**
     * 获取服务端异常参数
     */
    private ReturnServiceException getServiceExceptionParams(QCloudServiceException e) {
        Map<String, String> params = new HashMap<>();
        CosXmlServiceException serviceException = convertServerException(e);
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
        String requestName = cosXmlRequest.getClass().getSimpleName();
        return !"InitMultipartUploadRequest".equals(requestName) &&
                !"ListPartsRequest".equals(requestName) &&
                !"UploadPartRequest".equals(requestName) &&
                !"CompleteMultiUploadRequest".equals(requestName) &&
                !"AbortMultiUploadRequest".equals(requestName) &&
                !"UploadPartCopyRequest".equals(requestName);
    }

    /**
     * 是否上报（过滤掉不需要上报的异常）
     *
     * @param e 服务端异常
     * @return 是否上报
     */
    private boolean isReport(CosXmlServiceException e) {
        return "BadDigest".equals(e.getErrorCode()) ||
                "EntitySizeNotMatch".equals(e.getErrorCode()) ||
                "IncompleteBody".equals(e.getErrorCode()) ||
                "InvalidDigest".equals(e.getErrorCode()) ||
                "InvalidSHA1Digest".equals(e.getErrorCode()) ||
                "MalformedPOSTRequest".equals(e.getErrorCode()) ||
                "MalformedXML".equals(e.getErrorCode()) ||
                "MissingRequestBodyError".equals(e.getErrorCode()) ||
                "RequestTimeout".equals(e.getErrorCode()) ||
                "XMLSizeLimit".equals(e.getErrorCode()) ||
                "SignatureDoesNotMatch".equals(e.getErrorCode()) ||
                "MissingContentLength".equals(e.getErrorCode());
    }

    /**
     * 是否上报（过滤掉不需要上报的异常）
     *
     * @param e 客户端异常
     * @return 是否上报
     */
    private boolean isReport(CosXmlClientException e) {
        return e.errorCode == ClientErrorCode.UNKNOWN.getCode() ||
                e.errorCode == ClientErrorCode.INTERNAL_ERROR.getCode() ||
                e.errorCode == ClientErrorCode.SERVERERROR.getCode() ||
                e.errorCode == ClientErrorCode.IO_ERROR.getCode() ||
                e.errorCode == PoorNetworkCode.UnknownHostException ||
                e.errorCode == PoorNetworkCode.SocketTimeoutException ||
                e.errorCode == PoorNetworkCode.ConnectException ||
                e.errorCode == PoorNetworkCode.HttpRetryException ||
                e.errorCode == PoorNetworkCode.NoRouteToHostException ||
                e.errorCode == PoorNetworkCode.SSLHandshakeException;
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
