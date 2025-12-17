package com.tencent.cos.xml.retry;

import android.util.Base64;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CIService;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.ci.DescribeDocProcessBucketsRequest;
import com.tencent.cos.xml.model.ci.DescribeDocProcessBucketsResult;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditRequest;
import com.tencent.cos.xml.model.ci.audit.TextAuditResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.tag.audit.post.PostTextAudit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;

/**
 * <p>
 * Created by rickenwang on 2021/11/12.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class RetryWebServerMockTest {
    private static final String HOST_TENCENTCOS = "cos-sdk-err-retry-1253960454.cos.ap-chengdu.tencentcos.cn";
    private static final String CI_HOST_TENCENTCI = "1253960454.ci.ap-beijing.tencentci.cn";

    private CosXmlSimpleService serviceTencentCos;
    private CosXmlSimpleService serviceTencentCosNoSwitch;

    private CIService ciServiceTencentCI;
    private CIService ciServiceTencentCINoSwitch;
    @Before
    public void init() {
        serviceTencentCos = ServiceFactory.INSTANCE.newRetryServiceTencentCos(true);
        serviceTencentCosNoSwitch = ServiceFactory.INSTANCE.newRetryServiceTencentCos(false);

        ciServiceTencentCI = NormalServiceFactory.INSTANCE.newRetryCIAuditServiceTencentCI(true);
        ciServiceTencentCINoSwitch = NormalServiceFactory.INSTANCE.newRetryCIAuditServiceTencentCI(false);
    }

    @Test
    public void testNoSwitch2xx() {
        getObject(serviceTencentCosNoSwitch, "200r", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "200", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "204r", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "204", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "206r", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "206", HOST_TENCENTCOS, 0);
    }

    @Test
    public void testNoSwitch3xx() {
        getObject(serviceTencentCosNoSwitch, "301r", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "301", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "302r", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "302", HOST_TENCENTCOS, 0);
    }

    @Test
    public void testNoSwitch4xx() {
        getObject(serviceTencentCosNoSwitch, "400r", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "400", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "403r", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "403", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "404r", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCosNoSwitch, "404", HOST_TENCENTCOS, 0);
    }

    @Test
    public void testNoSwitch500() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceTencentCosNoSwitch, "500r", HOST_TENCENTCOS, 1);

        getObject(serviceTencentCosNoSwitch, "500", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitch503() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceTencentCosNoSwitch, "503r", HOST_TENCENTCOS, 1);

        getObject(serviceTencentCosNoSwitch, "503", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitch504() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceTencentCosNoSwitch, "504r", HOST_TENCENTCOS, 1);

        getObject(serviceTencentCosNoSwitch, "504", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitchTimeout() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceTencentCosNoSwitch, "timeout", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitchShutdown() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceTencentCosNoSwitch, "shutdown", HOST_TENCENTCOS, 1);
    }

    public void getObject(CosXmlSimpleService cosXmlSimpleService, String cosKey, String hostParam, int retryCountParam)   {
        GetObjectRequest request = new GetObjectRequest(TestConst.RETRY_BUCKET, cosKey, TestUtils.localParentPath());
        try {
            GetObjectResult result = cosXmlSimpleService.getObject(request);
            int retryCount = request.getMetrics().getRetryCount();
            Log.d("RetryTest_"+cosKey, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = result.host;
            Log.d("RetryTest_"+cosKey, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            int retryCount = request.getMetrics().getRetryCount();
            Log.d("RetryTest_"+cosKey, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = e.getHost();
            Log.d("RetryTest_"+cosKey, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        }
        // 由于流水线的wetest真机无法配置代理，因此这里在流水线上不判断用例是否正确，只在本地开发专项测试时使用
//        GetObjectRequest request = new GetObjectRequest(TestConst.RETRY_BUCKET, cosKey, TestUtils.localParentPath());
//        try {
//            GetObjectResult result = cosXmlSimpleService.getObject(request);
//            int retryCount = request.getMetrics().getRetryCount();
//            Log.d("RetryTest_"+cosKey, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
//            Assert.assertTrue(true);
//        } catch (CosXmlClientException e) {
//            e.printStackTrace();
//            Assert.assertTrue(true);
//        } catch (CosXmlServiceException e) {
//            e.printStackTrace();
//            Assert.assertTrue(true);
//        }
    }

    @Test
    public void testCiNoSwitch2xx() {
        describeDataset(ciServiceTencentCINoSwitch, "200r", CI_HOST_TENCENTCI, 0, 200, true);

        describeDataset(ciServiceTencentCINoSwitch, "200", CI_HOST_TENCENTCI, 0, 200, false);

        describeDataset(ciServiceTencentCINoSwitch, "204r", CI_HOST_TENCENTCI, 0, 204, true);

        describeDataset(ciServiceTencentCINoSwitch, "204", CI_HOST_TENCENTCI, 0, 204, false);

        describeDataset(ciServiceTencentCINoSwitch, "206r", CI_HOST_TENCENTCI, 0, 206, true);

        describeDataset(ciServiceTencentCINoSwitch, "206", CI_HOST_TENCENTCI, 0, 206, false);
    }

    @Test
    public void testCiNoSwitch3xx() {
        describeDataset(ciServiceTencentCINoSwitch, "301r", CI_HOST_TENCENTCI, 0, 301, true);

        describeDataset(ciServiceTencentCINoSwitch, "301", CI_HOST_TENCENTCI, 0, 301, false);

        describeDataset(ciServiceTencentCINoSwitch, "302r", CI_HOST_TENCENTCI, 0, 302, true);

        describeDataset(ciServiceTencentCINoSwitch, "302", CI_HOST_TENCENTCI, 0, 302, false);

        describeDataset(ciServiceTencentCINoSwitch, "307r", CI_HOST_TENCENTCI, 0, 307, true);

        describeDataset(ciServiceTencentCINoSwitch, "307", CI_HOST_TENCENTCI, 0, 307, false);

        describeDataset(ciServiceTencentCINoSwitch, "308r", CI_HOST_TENCENTCI, 0, 308, true);

        describeDataset(ciServiceTencentCINoSwitch, "308", CI_HOST_TENCENTCI, 0, 308, false);
    }

    @Test
    public void testCiNoSwitch4xx() {
        describeDataset(ciServiceTencentCINoSwitch, "400r", CI_HOST_TENCENTCI, 0, 400, true);

        describeDataset(ciServiceTencentCINoSwitch, "400", CI_HOST_TENCENTCI, 0, 400, false);

        describeDataset(ciServiceTencentCINoSwitch, "403r", CI_HOST_TENCENTCI, 0, 403, true);

        describeDataset(ciServiceTencentCINoSwitch, "403", CI_HOST_TENCENTCI, 0, 403, false);

        describeDataset(ciServiceTencentCINoSwitch, "404r", CI_HOST_TENCENTCI, 0, 404, true);

        describeDataset(ciServiceTencentCINoSwitch, "404", CI_HOST_TENCENTCI, 0, 404, false);
    }

    @Test
    public void testCiNoSwitch500() {
        describeDataset(ciServiceTencentCINoSwitch, "500r", CI_HOST_TENCENTCI, 2, 500, true);

        describeDataset(ciServiceTencentCINoSwitch, "500", CI_HOST_TENCENTCI, 2, 500, false);
    }

    @Test
    public void testCiNoSwitch503() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        describeDataset(ciServiceTencentCINoSwitch, "503r", HOST_TENCENTCOS, 1, 503, true);

        describeDataset(ciServiceTencentCINoSwitch, "503", HOST_TENCENTCOS, 1, 503, false);
    }

    @Test
    public void testCiNoSwitch504() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        describeDataset(ciServiceTencentCINoSwitch, "504r", HOST_TENCENTCOS, 1, 504, true);

        describeDataset(ciServiceTencentCINoSwitch, "504", HOST_TENCENTCOS, 1, 504, false);
    }

    @Test
    public void testCiNoSwitchTimeout() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        describeDataset(ciServiceTencentCINoSwitch, "timeout", HOST_TENCENTCOS, 1, 1000, false);
    }

    @Test
    public void testCiNoSwitchShutdown() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        describeDataset(ciServiceTencentCINoSwitch, "shutdown", HOST_TENCENTCOS, 1, 2000, false);
    }

    public void describeDataset(CIService ciService, String xCiCode, String hostParam, int retryCountParam,
                                int mockCode, boolean mockHasRetryId) {
        MockWebServer server = new MockWebServer();
        // 设置 mock 响应
        MockResponse.Builder builder = new MockResponse.Builder();
        if (mockHasRetryId) {
            builder.addHeader("x-ci-request-id", "x-ci-request-id-xxxxx");
            builder.addHeader("x-cos-request-id", "x-cos-request-id-xxxxx");
        }
        // 超时
        if(mockCode == 1000){
            builder.headersDelay(10, TimeUnit.SECONDS);
        }
        // shutdown
        if(mockCode == 2000){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        server.close();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        builder.code(mockCode);
//        builder.body("hello, world!");
        server.enqueue(builder.build());

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }

        DescribeDatasetRequest request = new DescribeDatasetRequest(TestConst.CI_BUCKET_APPID);
        request.datasetname = "datasetnametestqjd";// 设置数据集名称，同一个账户下唯一。
        request.statistics = false;// 设置是否需要实时统计数据集中文件相关信息。有效值： false：不统计，返回的文件的总大小、数量信息可能不正确也可能都为0。 true：需要统计，返回数据集中当前的文件的总大小、数量信息。 默认值为false。
        try {
            request.setRequestHeaders("x-ci-code", xCiCode, false);
        } catch (CosXmlClientException e) {
            throw new RuntimeException(e);
        }

        try {
            DescribeDatasetResult result = ciService.describeDataset(request);
            int retryCount = request.getMetrics().getRetryCount();
            Log.d("RetryTest_"+xCiCode, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = result.host;
            Log.d("RetryTest_"+xCiCode, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            int retryCount = request.getMetrics().getRetryCount();
            Log.d("RetryTest_"+xCiCode, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = request.getHttpTask().request().host();
            Log.d("RetryTest_"+xCiCode, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            int retryCount = request.getMetrics().getRetryCount();
            Log.d("RetryTest_"+xCiCode, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = e.getHost();
            Log.d("RetryTest_"+xCiCode, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        }
        server.close();
        // 由于流水线的wetest真机无法配置代理，因此这里在流水线上不判断用例是否正确，只在本地开发专项测试时使用
    }
    public void describeDocProcessBuckets(CIService ciService, String xCiCode, String hostParam, int retryCountParam) {
        DescribeDocProcessBucketsRequest request = new DescribeDocProcessBucketsRequest();
        request.setPageNumber(1);
        request.setPageSize(20);
        try {
            request.setRequestHeaders("x-ci-code", xCiCode, false);
        } catch (CosXmlClientException e) {
            throw new RuntimeException(e);
        }

        try {
            DescribeDocProcessBucketsResult result = ciService.describeDocProcessBuckets(request);
            int retryCount = request.getMetrics().getRetryCount();
            Log.d("RetryTest_"+xCiCode, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = result.host;
            Log.d("RetryTest_"+xCiCode, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            int retryCount = request.getMetrics().getRetryCount();
            Log.d("RetryTest_"+xCiCode, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = request.getHttpTask().request().host();
            Log.d("RetryTest_"+xCiCode, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            int retryCount = request.getMetrics().getRetryCount();
            Log.d("RetryTest_"+xCiCode, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = e.getHost();
            Log.d("RetryTest_"+xCiCode, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        }

        // 由于流水线的wetest真机无法配置代理，因此这里在流水线上不判断用例是否正确，只在本地开发专项测试时使用
    }
    public void templateConcat(CIService ciService, String xCiCode, String hostParam, int retryCountParam) {
        PostTextAuditRequest postRequest = new PostTextAuditRequest(TestConst.CI_BUCKET);
//        postRequest.setObject(TestConst.AUDIT_BUCKET_TEXT);
        postRequest.setContent(Base64.encodeToString("测试文本 很黄很暴力".getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/tencentyun/qcloud-sdk-android");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setBizType("");
        postRequest.setConfig(new PostTextAudit.TextAuditConf());
        try {
            postRequest.setRequestHeaders("x-ci-code", xCiCode, false);
        } catch (CosXmlClientException e) {
            throw new RuntimeException(e);
        }

        try {
            TextAuditResult result = ciService.postTextAudit(postRequest);
            int retryCount = postRequest.getMetrics().getRetryCount();
            Log.d("RetryTest_"+xCiCode, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = result.host;
            Log.d("RetryTest_"+xCiCode, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            int retryCount = postRequest.getMetrics().getRetryCount();
            Log.d("RetryTest_"+xCiCode, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = postRequest.getHttpTask().request().host();
            Log.d("RetryTest_"+xCiCode, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            int retryCount = postRequest.getMetrics().getRetryCount();
            Log.d("RetryTest_"+xCiCode, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = e.getHost();
            Log.d("RetryTest_"+xCiCode, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
        }

        // 由于流水线的wetest真机无法配置代理，因此这里在流水线上不判断用例是否正确，只在本地开发专项测试时使用
    }
}
