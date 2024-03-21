package com.tencent.qcloud.track;

import static com.tencent.qcloud.track.BuildConfig.CLS_SECRET_ID;
import static com.tencent.qcloud.track.BuildConfig.CLS_SECRET_KEY;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.tencent.qcloud.track.service.ClsTrackService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * Created by jordanqin on 2023/11/8 10:35.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class ClsCosDataInputTest {
    private static final String TAG = "ClsCosDataInputTest";
    private static final String EVENT_CODE_QCLOUD_TRACK_COS_SDK = "qcloud_track_cos_sdk";

    @Before
    public void init() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();


        ClsTrackService clsTrackService = new ClsTrackService();
//        clsTrackService.init(appContext, "9ab664b4-f657-4ef9-8b8c-305b819e477d", "ap-guangzhou.cls.tencentcs.com");
        clsTrackService.init(appContext, "d4bb2a4f-99a0-4b42-960b-87249b0f6c33", "ap-guangzhou.cls.tencentcs.com");
        // 临时秘钥
//        clsTrackService.setCredentialProvider(new MyClsLifecycleCredentialProvider());
        // 固定秘钥
        clsTrackService.setSecurityCredential(CLS_SECRET_ID, CLS_SECRET_KEY);
        QCloudTrackService.getInstance().addTrackService(EVENT_CODE_QCLOUD_TRACK_COS_SDK, clsTrackService);

        QCloudTrackService.getInstance().init(appContext);
        QCloudTrackService.getInstance().setIsCloseReport(false);
        QCloudTrackService.getInstance().setDebug(true);
    }

    @Test
    public void inputData1() {
        QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("UploadTask"));
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void inputData() {
        // 每隔100s上报一次上传、下载、其他基础接口
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i=0;i<200;i++){
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("UploadTask"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("DownloadTask"));
            }
            for (int i=0;i<40;i++){
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("CopyTask"));
            }
            for (int i=0;i<800;i++){
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("PutObjectRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("HeadObjectRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("GetObjectRequest"));
            }
            for (int i=0;i<300;i++){
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("InitMultipartUploadRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("CompleteMultiUploadRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("DeleteObjectRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("ListPartsRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("PostTextAuditRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("GetBucketRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("HeadBucketRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("GetBucketObjectVersionsRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("GetServiceRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("PostImagesAuditRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("PreviewDocumentRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("FormatConversionRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getSuccessParams("PutObjectACLRequest"));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i=0;i<5;i++) {
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("UploadTask"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("DownloadTask"));
            }
            for (int i=0;i<1;i++) {
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("CopyTask"));
            }
            for (int i=0;i<25;i++){
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("PutObjectRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("HeadObjectRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("GetObjectRequest"));
            }
            for (int i=0;i<5;i++){
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("InitMultipartUploadRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("CompleteMultiUploadRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("DeleteObjectRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("ListPartsRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("PostTextAuditRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("GetBucketRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("HeadBucketRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("GetBucketObjectVersionsRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("GetServiceRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("PostImagesAuditRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("PreviewDocumentRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("FormatConversionRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getClientExceptionParams("PutObjectACLRequest"));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i=0;i<10;i++) {
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("UploadTask"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("DownloadTask"));
            }
            for (int i=0;i<1;i++) {
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("CopyTask"));
            }
            for (int i=0;i<50;i++){
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("PutObjectRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("HeadObjectRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("GetObjectRequest"));
            }
            for (int i=0;i<10;i++){
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("InitMultipartUploadRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("CompleteMultiUploadRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("DeleteObjectRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("ListPartsRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("PostTextAuditRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("GetBucketRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("HeadBucketRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("GetBucketObjectVersionsRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("GetServiceRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("PostImagesAuditRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("PreviewDocumentRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("FormatConversionRequest"));
                QCloudTrackService.getInstance().report(EVENT_CODE_QCLOUD_TRACK_COS_SDK, getServiceExceptionParams("PutObjectACLRequest"));
            }
        }
    }

    private Map<String, String> getSuccessParams(String request_name){
        // 添加 Request 参数
        Map<String, String> params = parseCosXmlRequestParams();
        // 添加基础参数
        params.putAll(getCommonParams());
        // 添加性能参数
        params.putAll(parseHttpTaskMetricsParams());
        // 添加服务名称
        params.put("request_name", request_name);

        // 添加请求结果
        params.put("request_result", "Success");
        return params;
    }

    private Map<String, String> getClientExceptionParams(String request_name) {
//        String[] errorCodeArray = {"10001", "200032", "20002", "20004", "200033", "20001"};
//        String[] errorNameArray = {"QCloudAuthenticationException", "UnknownHostException", "SocketException", "QCloudClientException", "SocketTimeoutException", "XmlPullParserException"};
//        String[] errorMessageArray = {
//                "Credentials is null.",
//                "tmap-reflux-1251316161.cos.ap-shanghai.myqcloud.com",
//                "Connection reset",
//                "NetworkNotConnected",
//                "timeout",
//                "expected: /hr read: body (position:END_TAG </body>@6:8 in java.io.InputStreamReader@71b9af7)"
//        };
        String[] errorCodeArray = {"10001", "200032", "20002"};
        String[] errorNameArray = {"QCloudAuthenticationException", "UnknownHostException", "SocketException"};
        String[] errorMessageArray = {
                "Credentials is null.",
                "tmap-reflux-1251316161.cos.ap-shanghai.myqcloud.com",
                "Connection reset",
        };
        Random random = new Random();
        int index = random.nextInt(errorCodeArray.length);

        // 添加 Request 参数
        Map<String, String> params = parseCosXmlRequestParams();
        // 添加基础参数
        params.putAll(getCommonParams());
        // 添加错误信息
        params.put("error_code", errorCodeArray[index]);
        params.put("error_name", errorNameArray[index]);
        params.put("error_message", errorMessageArray[index]);
        params.put("error_type", "Client");
        // 添加性能参数
        params.putAll(parseHttpTaskMetricsParams());
        // 添加服务名称
        params.put("request_name", request_name);
        // 添加请求结果
        params.put("request_result", "Failure");
        return params;
    }

    /**
     * 上报服务端异常 单个请求，整体任务
     */
    private Map<String, String> getServiceExceptionParams(String request_name) {
//        String[] httpCodeArray = {"0", "403", "400", "403", "404", "404"};
//        String[] errorCodeArray = {"RequestIsExpired", "AccessDenied", "BadDigest", "InvalidAccessKeyId", "NoSuchBucket", ""};
//        String[] errorMessageArray = {
//                "client clock skewed",
//                "Request has expired",
//                "The Content-MD5 you specified is not match.",
//                "The Access Key Id you provided does not exist in our records",
//                "The specified bucket does not exist.",
//                ""
//        };
//        String[] httpMessageArray = {
//                "",
//                "Execute access forbidden",
//                "Bad Request",
//                "Invalid configuration",
//                "File extension denied.",
//                "Not found."
//        };
//        String[] serviceNameArray = {"images/2023-11-09/1.jpg", "aaa/2.mp4", "aaa", "asd.txt", "p.word", "android/qqq.apk"};
//        String[] requestIdArray = {
//                "NjU0NjQ5MjVfMWU2ZGFhMDlfMjExNDJfODkzYWQ5Ng%3D%3D",
//                "NjU0NjQ5MjVfMWU2ZGFhMDlfMjExNDJfODkzYWQ5Ng%3D%3A",
//                "NjU0NjQ5MjQQMWU2ZGFhMDlfMjExNDJfODkzYWQ5Ng%3D%3D",
//                "NjU0NjQ5MjVfMWU2ZGFhMDlfMjExND66ODkzYWQ5Ng%3D%3D",
//                "NjU0NjQ5MjVfMWU2ZGFhMWWfMjExNDJfODkzYWQ5Ng%3D%3D",
//                "NjU0NjQ5MjVfMWU2ZGFABclfMjExNDJfODkzYWQ5Ng%3D%3D"
//        };
        String[] httpCodeArray = {"0", "403", "400"};
        String[] errorCodeArray = {"RequestIsExpired", "AccessDenied", "BadDigest"};
        String[] errorMessageArray = {
                "client clock skewed",
                "Request has expired",
                "The Content-MD5 you specified is not match.",
        };
        String[] httpMessageArray = {
                "",
                "Execute access forbidden",
                "Bad Request",
        };
        String[] serviceNameArray = {"images/2023-11-09/1.jpg", "aaa/2.mp4", "aaa"};
        String[] requestIdArray = {
                "NjU0NjQ5MjVfMWU2ZGFhMDlfMjExNDJfODkzYWQ5Ng%3D%3D",
                "NjU0NjQ5MjVfMWU2ZGFhMDlfMjExNDJfODkzYWQ5Ng%3D%3A",
                "NjU0NjQ5MjQQMWU2ZGFhMDlfMjExNDJfODkzYWQ5Ng%3D%3D",
        };

        Random random = new Random();
        int index = random.nextInt(errorCodeArray.length);

        // 添加 Request 参数
        Map<String, String> params = parseCosXmlRequestParams();
        // 添加基础参数
        params.putAll(getCommonParams());
        // 添加错误信息
        params.put("error_code", errorCodeArray[index]);
        params.put("error_message", errorMessageArray[index]);
        params.put("error_http_code", httpCodeArray[index]);
        params.put("error_http_message", httpMessageArray[index]);
        params.put("error_service_name", serviceNameArray[index]);
        params.put("error_request_id", requestIdArray[index]);
        params.put("error_type", "Server");
        // 添加性能参数
        params.putAll(parseHttpTaskMetricsParams());
        // 添加服务名称
        params.put("request_name", request_name);

        // 添加请求结果
        params.put("request_result", "Failure");
        return params;
    }

    /**
     * 获取request数据字段
     */
    private Map<String, String> parseCosXmlRequestParams() {
//        String[] versionNameArray = {"5.9.15", "5.9.16", "5.9.17", "5.9.18", "5.9.19", "5.9.20"};
//        String[] networkProtocolArray = {"http", "https", "quic", "http", "https", "quic"};
//        String[] bucketArray = {"mobile-ut-1253969954", "mobile-1251230454", "xhs-1251230454", "xhs-images-1251230454", "ut-1253690454", "test-1256230454"};
//        String[] regionArray = {"ap-beijing", "ap-chengdu", "ap-guangzhou", "ap-shanghai", "ap-chongqing", "ap-nanjing"};
//        String[] requestPathArray = {"images/2023-11-09/1.jpg", "aaa/2.mp4", "aaa", "asd.txt", "p.word", "android/qqq.apk"};
//        String[] accelerateArray = {"false", "false", "false", "false", "false", "true"};
//        String[] httpMethodArray = {"get", "get", "put", "put", "put", "head"};
        String[] versionNameArray = {"5.9.18", "5.9.19", "5.9.20"};
        String[] networkProtocolArray = {"http", "https", "quic"};
        String[] bucketArray = {"xhs-images-1251230454", "ut-1253690454", "test-1256230454"};
        String[] regionArray = {"ap-shanghai", "ap-chongqing", "ap-nanjing"};
        String[] requestPathArray = {"asd.txt", "p.word", "android/qqq.apk"};
        String[] accelerateArray = {"false", "false", "true"};
        String[] httpMethodArray = {"put", "put", "get"};
        Random random = new Random();
        int index = random.nextInt(versionNameArray.length);

        Map<String, String> params = new HashMap<>();
        params.put("bucket", bucketArray[index]);
        params.put("accelerate", accelerateArray[index]);
        params.put("network_protocol", networkProtocolArray[index]);
        params.put("http_method", httpMethodArray[index]);
        params.put("url", String.format("https://%s.cos.%s.myqcloud.com/%s", bucketArray[index], regionArray[index], requestPathArray[index]));
        params.put("user_agent", "cos-android-sdk-" + versionNameArray[index]);

        params.put("request_path", requestPathArray[index]);
        params.put("host", String.format("%s.cos.%s.myqcloud.com", bucketArray[index], regionArray[index]));
        params.put("region", regionArray[index]);
        return params;
    }

    private Map<String, String> getCommonParams() {
//        String[] versionNameArray = {"5.9.15", "5.9.16", "5.9.17", "5.9.18", "5.9.19", "5.9.20"};
//        String[] versionCodeArray = {"50915", "50916", "50917", "50918", "50919", "50920"};
        String[] versionNameArray = {"5.9.18", "5.9.19", "5.9.20"};
        String[] versionCodeArray = {"50918", "50919", "50920"};
        Random random = new Random();
        int index = random.nextInt(versionNameArray.length);
        String randomVersionName = versionNameArray[index];
        String randomCodeName = versionCodeArray[index];

        Map<String, String> params = new HashMap<>();
        params.put("sdk_version_name", randomVersionName);
        params.put("sdk_version_code", randomCodeName);
        return params;
    }

    /**
     * 获取http性能数据字段
     */
    private Map<String, String> parseHttpTaskMetricsParams() {
        double[] taskTookTimeArray = {2.5, 2.8, 2.1, 2.9, 2.0, 2.7, 2.2};
        double[] httpTookTimeArray = {1.5, 1.8, 1.1, 0.9, 2.0, 2.7, 1.2};
        double[] dnsLookupTookTimeArray = {0.1, 0.11, 0.123, 0.2, 0.5, 0.8, 0.19};
        double[] connectTookTimeArray = {0.3, 0.31, 0.323, 0.4, 0.5, 1.2, 1.19};
        double[] secureConnectTookTimeArray = {0.3, 0.31, 0.323, 0.4, 0.5, 1.2, 1.19};
        double[] calculateMD5STookTimeArray = {0.03, 0.031, 0.0323, 0.04, 0.05, 0.027, 0.019};
        double[] signRequestTookTimeArray = {0.03, 0.031, 0.0323, 0.04, 0.05, 0.027, 0.019};
        double[] readResponseHeaderTookTimeArray = {0.1, 0.11, 0.09, 0.29, 0.2, 0.11, 0.19};
        double[] readResponseBodyTookTimeArray = {0.11, 0.111, 0.109, 9.129, 1.12, 2.111, 3.119};
        double[] writeRequestHeaderTookTimeArray = {0.1, 0.11, 0.09, 0.29, 0.2, 0.11, 0.19};
        double[] writeRequestBodyTookTimeArray = {0.11, 0.111, 0.109, 5.129, 1.12, 2.111, 3.119};
        int[] httpSizeArray = {1102400, 2102400, 1902400, 11102400, 102400, 1110, 9990240};
        Random random = new Random();
        int index = random.nextInt(taskTookTimeArray.length);

        Map<String, String> params = new HashMap<>();
        params.put("http_full_time", String.valueOf(taskTookTimeArray[index]));
        params.put("http_took_time", String.valueOf(httpTookTimeArray[index]));
        params.put("http_dns", String.valueOf(dnsLookupTookTimeArray[index]));
        params.put("http_connect", String.valueOf(connectTookTimeArray[index]));
        params.put("http_secure_connect", String.valueOf(secureConnectTookTimeArray[index]));
        params.put("http_md5", String.valueOf(calculateMD5STookTimeArray[index]));
        params.put("http_sign", String.valueOf(signRequestTookTimeArray[index]));
        params.put("http_read_header", String.valueOf(readResponseHeaderTookTimeArray[index]));
        params.put("http_read_body", String.valueOf(readResponseBodyTookTimeArray[index]));
        params.put("http_write_header", String.valueOf(writeRequestHeaderTookTimeArray[index]));
        params.put("http_write_body", String.valueOf(writeRequestBodyTookTimeArray[index]));
        params.put("http_size", String.valueOf(httpSizeArray[index]));
        params.put("http_retry_times", "0");
        params.put("http_domain", "null");
        params.put("http_connect_ip", "null");
        params.put("http_dns_ips", "null");
        return params;
    }
}
