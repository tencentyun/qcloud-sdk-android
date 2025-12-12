package com.tencent.cos.xml.retry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.tencent.cos.xml.model.ci.media.TemplateConcatResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.tag.audit.post.PostTextAudit;
import com.tencent.qcloud.core.util.DomainSwitchUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.Charset;

/**
 * <p>
 * Created by rickenwang on 2021/11/12.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class RetryTest {
    private static final String HOST_MYQCLOUD = "cos-sdk-err-retry-1253960454.cos.ap-chengdu.myqcloud.com";
    private static final String HOST_TENCENTCOS = "cos-sdk-err-retry-1253960454.cos.ap-chengdu.tencentcos.cn";

//    private static final String CI_HOST_MYQCLOUD = "cos-sdk-citest-1253960454.ci.ap-beijing.myqcloud.com";
//    private static final String CI_HOST_TENCENTCI = "cos-sdk-citest-1253960454.ci.ap-beijing.tencentci.cn";
    private static final String CI_HOST_MYQCLOUD = "ci.ap-beijing.myqcloud.com";
    private static final String CI_HOST_TENCENTCI = "ci.ap-beijing.tencentci.cn";

    private CosXmlSimpleService serviceMyqcloud;
    private CosXmlSimpleService serviceTencentCos;
    private CosXmlSimpleService serviceMyqcloudNoSwitch;
    private CosXmlSimpleService serviceTencentCosNoSwitch;

    private CIService ciServiceMyqcloud;
    private CIService ciServiceTencentCI;
    private CIService ciServiceMyqcloudNoSwitch;
    private CIService ciServiceTencentCINoSwitch;
    @Before
    public void init() {
        serviceMyqcloud = ServiceFactory.INSTANCE.newRetryServiceMyqcloud(true);
        serviceTencentCos = ServiceFactory.INSTANCE.newRetryServiceTencentCos(true);
        serviceMyqcloudNoSwitch = ServiceFactory.INSTANCE.newRetryServiceMyqcloud(false);
        serviceTencentCosNoSwitch = ServiceFactory.INSTANCE.newRetryServiceTencentCos(false);

        ciServiceMyqcloud = NormalServiceFactory.INSTANCE.newRetryCIAuditServiceMyqcloud(true);
        ciServiceTencentCI = NormalServiceFactory.INSTANCE.newRetryCIAuditServiceTencentCI(true);
        ciServiceMyqcloudNoSwitch = NormalServiceFactory.INSTANCE.newRetryCIAuditServiceMyqcloud(false);
        ciServiceTencentCINoSwitch = NormalServiceFactory.INSTANCE.newRetryCIAuditServiceTencentCI(false);
    }

    @Test
    public void testIsMyqcloudUrl(){
        String[] testUrls = {
                "ut-1257101689.cos.ap-chengdu.myqcloud.com",
                "ut-1257101689.cos.ap-guangzhou.myqcloud.com",
                "examplebucket-1250000000.file.myqcloud.com",
                "ut-1257101689.cos.accelerate.myqcloud.com",
                "service.cos.myqcloud.com",
                "cos.ap-guangzhou.myqcloud.com",
                "exampledomain.com",
                "bucket-1250000000.ci.ap-beijing.myqcloud.com",
                "ci.ap-beijing.myqcloud.com",
                "ap-beijing.ci.myqcloud.com"
        };
        assertTrue(DomainSwitchUtils.isMyqcloudUrl(testUrls[0]));
        assertTrue(DomainSwitchUtils.isMyqcloudUrl(testUrls[1]));
        assertFalse(DomainSwitchUtils.isMyqcloudUrl(testUrls[2]));
        assertFalse(DomainSwitchUtils.isMyqcloudUrl(testUrls[3]));
        assertFalse(DomainSwitchUtils.isMyqcloudUrl(testUrls[4]));
        assertFalse(DomainSwitchUtils.isMyqcloudUrl(testUrls[5]));
        assertFalse(DomainSwitchUtils.isMyqcloudUrl(testUrls[6]));
        assertTrue(DomainSwitchUtils.isMyqcloudUrl(testUrls[7]));
        assertTrue(DomainSwitchUtils.isMyqcloudUrl(testUrls[8]));
        assertFalse(DomainSwitchUtils.isMyqcloudUrl(testUrls[9]));
    }

    @Test
    public void testNoSwitch2xx() {
        getObject(serviceMyqcloudNoSwitch, "200r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "200r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "200", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "200", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "204r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "204r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "204", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "204", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "206r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "206r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "206", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "206", HOST_TENCENTCOS, 0);
    }

    @Test
    public void testNoSwitch3xx() {
        getObject(serviceMyqcloudNoSwitch, "301r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "301r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "301", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "301", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "302r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "302r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "302", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "302", HOST_TENCENTCOS, 0);
    }

    @Test
    public void testNoSwitch4xx() {
        getObject(serviceMyqcloudNoSwitch, "400r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "400r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "400", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "400", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "403r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "403r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "403", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "403", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "404r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "404r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloudNoSwitch, "404", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCosNoSwitch, "404", HOST_TENCENTCOS, 0);
    }

    @Test
    public void testNoSwitch500() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceMyqcloudNoSwitch, "500r", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "500r", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloudNoSwitch, "500", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "500", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitch503() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceMyqcloudNoSwitch, "503r", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "503r", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloudNoSwitch, "503", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "503", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitch504() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceMyqcloudNoSwitch, "504r", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "504r", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloudNoSwitch, "504", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "504", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitchTimeout() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceMyqcloudNoSwitch, "timeout", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "timeout", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitchShutdown() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是400了  不再重试
        getObject(serviceMyqcloudNoSwitch, "shutdown", HOST_MYQCLOUD, 1);
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
        templateConcat(ciServiceMyqcloudNoSwitch, "200r", CI_HOST_MYQCLOUD, 0);
//        describeDocProcessBuckets(ciServiceMyqcloudNoSwitch, "200r", CI_HOST_TENCENTCI, 0);

//        describeDocProcessBuckets(ciServiceMyqcloudNoSwitch, "200", CI_HOST_MYQCLOUD, 0);
//        describeDocProcessBuckets(ciServiceTencentCINoSwitch, "200", CI_HOST_TENCENTCI, 0);
//
//        describeDocProcessBuckets(ciServiceMyqcloudNoSwitch, "204r", CI_HOST_MYQCLOUD, 0);
//        describeDocProcessBuckets(ciServiceTencentCINoSwitch, "204r", CI_HOST_TENCENTCI, 0);
//
//        describeDocProcessBuckets(ciServiceMyqcloudNoSwitch, "204", CI_HOST_MYQCLOUD, 0);
//        describeDocProcessBuckets(ciServiceTencentCINoSwitch, "204", CI_HOST_TENCENTCI, 0);
//
//        describeDocProcessBuckets(ciServiceMyqcloudNoSwitch, "206r", CI_HOST_MYQCLOUD, 0);
//        describeDocProcessBuckets(ciServiceTencentCINoSwitch, "206r", CI_HOST_TENCENTCI, 0);
//
//        describeDocProcessBuckets(ciServiceMyqcloudNoSwitch, "206", CI_HOST_MYQCLOUD, 0);
//        describeDocProcessBuckets(ciServiceTencentCINoSwitch, "206", CI_HOST_TENCENTCI, 0);
    }

    public void describeDataset(CIService ciService, String xCiCode, String hostParam, int retryCountParam) {
        DescribeDatasetRequest request = new DescribeDatasetRequest(TestConst.CI_BUCKET_APPID);
        request.datasetname = "datasetnametest1";// 设置数据集名称，同一个账户下唯一。
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
            Assert.fail(e.getMessage());
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
            Assert.fail(e.getMessage());
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
            Assert.fail(e.getMessage());
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
