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
public class RetryCosTest {
    private static final String HOST_MYQCLOUD = "cos-sdk-err-retry-1253960454.cos.ap-chengdu.myqcloud.com";
    private static final String HOST_TENCENTCOS = "cos-sdk-err-retry-1253960454.cos.ap-chengdu.tencentcos.cn";

    private CosXmlSimpleService serviceMyqcloud;
    private CosXmlSimpleService serviceTencentCos;
    private CosXmlSimpleService serviceMyqcloudNoSwitch;
    private CosXmlSimpleService serviceTencentCosNoSwitch;

    @Before
    public void init() {
        serviceMyqcloud = ServiceFactory.INSTANCE.newRetryServiceMyqcloud(true, 80);
        serviceTencentCos = ServiceFactory.INSTANCE.newRetryServiceTencentCos(true, 80);
        serviceMyqcloudNoSwitch = ServiceFactory.INSTANCE.newRetryServiceMyqcloud(false, 80);
        serviceTencentCosNoSwitch = ServiceFactory.INSTANCE.newRetryServiceTencentCos(false, 80);
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
    public void testSwitch2xx() {
        getObject(serviceMyqcloud, "200r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "200r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "200", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "200", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "204r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "204r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "204", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "204", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "206r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "206r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "206", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "206", HOST_TENCENTCOS, 0);
    }

    @Test
    public void testNoSwitch3xx() {
        // 测试服务器重定向到cloud.tencent.com
        getObject(serviceMyqcloudNoSwitch, "301r", "cloud.tencent.com", 0);
        getObject(serviceTencentCosNoSwitch, "301r", "cloud.tencent.com", 0);

        getObject(serviceMyqcloudNoSwitch, "301", "cloud.tencent.com", 0);
        getObject(serviceTencentCosNoSwitch, "301", "cloud.tencent.com", 0);

        getObject(serviceMyqcloudNoSwitch, "302r", "cloud.tencent.com", 0);
        getObject(serviceTencentCosNoSwitch, "302r", "cloud.tencent.com", 0);

        getObject(serviceMyqcloudNoSwitch, "302", "cloud.tencent.com", 0);
        getObject(serviceTencentCosNoSwitch, "302", "cloud.tencent.com", 0);
    }

    @Test
    public void testSwitch3xx() {
        getObject(serviceMyqcloud, "301r", "cloud.tencent.com", 0);
        getObject(serviceTencentCos, "301r", "cloud.tencent.com", 0);

        // 第一次就切换域名，说明重试次数为0
        // 是因为测试服务器带重试header 响应的就是200了 不再重定向 所以host不是cloud.tencent.com
        getObject(serviceMyqcloud, "301", HOST_TENCENTCOS, 0);
        getObject(serviceTencentCos, "301", "cloud.tencent.com", 0);

        getObject(serviceMyqcloud, "302r", "cloud.tencent.com", 0);
        getObject(serviceTencentCos, "302r", "cloud.tencent.com", 0);
//
        getObject(serviceMyqcloud, "302", HOST_TENCENTCOS, 0);
        getObject(serviceTencentCos, "302", "cloud.tencent.com", 0);
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
    public void testSwitch4xx() {
        getObject(serviceMyqcloud, "400r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "400r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "400", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "400", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "403r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "403r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "403", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "403", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "404r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "404r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "404", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "404", HOST_TENCENTCOS, 0);
    }

    @Test
    public void testNoSwitch5xx() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是200了  不再重试
        getObject(serviceMyqcloudNoSwitch, "500r", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "500r", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloudNoSwitch, "500", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "500", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloudNoSwitch, "503r", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "503r", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloudNoSwitch, "503", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "503", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloudNoSwitch, "504r", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "504r", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloudNoSwitch, "504", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "504", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testSwitch5xx() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是200了  不再重试
        getObject(serviceMyqcloud, "500r", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCos, "500r", HOST_TENCENTCOS, 1);
        // 为什么域名没有切换，因为测试服务器带重试header 响应的就是200了  不再重试
        getObject(serviceMyqcloud, "500", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCos, "500", HOST_TENCENTCOS, 1);
//
        getObject(serviceMyqcloud, "503r", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCos, "503r", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloud, "503", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCos, "503", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloud, "504r", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCos, "504r", HOST_TENCENTCOS, 1);

        getObject(serviceMyqcloud, "504", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCos, "504", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitchTimeout() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是200了  不再重试
        getObject(serviceMyqcloudNoSwitch, "timeout", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "timeout", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testSwitchTimeout() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是200了  不再重试
        // 为什么域名没有切换，因为测试服务器带重试header 响应的就是200了  不再重试
        getObject(serviceMyqcloud, "timeout", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCos, "timeout", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testNoSwitchShutdown() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是200了  不再重试
        getObject(serviceMyqcloudNoSwitch, "shutdown", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCosNoSwitch, "shutdown", HOST_TENCENTCOS, 1);
    }

    @Test
    public void testSwitchShutdown() {
        // 为什么重试1次  是因为测试服务器带重试header 响应的就是200了  不再重试
        // 为什么域名没有切换，因为测试服务器带重试header 响应的就是200了  不再重试
        getObject(serviceMyqcloud, "shutdown", HOST_MYQCLOUD, 1);
        getObject(serviceTencentCos, "shutdown", HOST_TENCENTCOS, 1);
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
            int retryCount = request.getMetrics().getRetryCount();
            Log.d("RetryTest_"+cosKey, "retryCount: " + retryCount + "--- retryCountParam: " + retryCountParam);
            Assert.assertEquals(retryCount, retryCountParam);
            String host = request.getHttpTask().request().header("Host");
            Log.d("RetryTest_"+cosKey, "host: " + host + "--- hostParam: " + hostParam);
            Assert.assertEquals(host, hostParam);
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
}
