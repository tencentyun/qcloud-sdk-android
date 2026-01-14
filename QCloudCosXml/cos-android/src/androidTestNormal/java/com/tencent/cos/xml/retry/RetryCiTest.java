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
public class RetryCiTest {
//    private static final String CI_HOST_MYQCLOUD = "cos-sdk-citest-1253960454.ci.ap-beijing.myqcloud.com";
//    private static final String CI_HOST_TENCENTCI = "cos-sdk-citest-1253960454.ci.ap-beijing.tencentci.cn";
    private static final String CI_HOST_MYQCLOUD = "1253960454.ci.ap-beijing.myqcloud.com";
    private static final String CI_HOST_TENCENTCI = "1253960454.ci.ap-beijing.tencentci.cn";

    private CIService ciServiceMyqcloud;
    private CIService ciServiceMyqcloudNoSwitch;
    private CIService ciServiceTencentCI;
    private CIService ciServiceTencentCINoSwitch;
    @Before
    public void init() {
        ciServiceMyqcloud = NormalServiceFactory.INSTANCE.newRetryCIServiceMyqcloud(true, 80);
        ciServiceMyqcloudNoSwitch = NormalServiceFactory.INSTANCE.newRetryCIServiceMyqcloud(false, 80);
        ciServiceTencentCI = NormalServiceFactory.INSTANCE.newRetryCIServiceTencentCI(true, 80);
        ciServiceTencentCINoSwitch = NormalServiceFactory.INSTANCE.newRetryCIServiceTencentCI(false, 80);
    }

    @Test
    public void testCiNoSwitch2xx() {
        describeDataset(ciServiceMyqcloudNoSwitch, "200r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "200", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "204r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "204", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "206r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "206", CI_HOST_MYQCLOUD, 0);

        describeDataset(ciServiceTencentCINoSwitch, "200r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "200", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "204r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "204", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "206r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "206", CI_HOST_TENCENTCI, 0);
    }

    @Test
    public void testCiSwitch2xx() {
        describeDataset(ciServiceMyqcloud, "200r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "200", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "204r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "204", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "206r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "206", CI_HOST_MYQCLOUD, 0);

        describeDataset(ciServiceTencentCI, "200r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "200", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "204r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "204", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "206r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "206", CI_HOST_TENCENTCI, 0);
    }

    @Test
    public void testCiNoSwitch3xx() {
        describeDataset(ciServiceMyqcloudNoSwitch, "301r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "301", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "302r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "302", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "307r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "307", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "308r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "308", CI_HOST_MYQCLOUD, 0);

        describeDataset(ciServiceTencentCINoSwitch, "301r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "301", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "302r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "302", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "307r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "307", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "308r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "308", CI_HOST_TENCENTCI, 0);
    }

    @Test
    public void testCi3xx() {
        describeDataset(ciServiceMyqcloud, "301r", CI_HOST_MYQCLOUD, 0);
        // 第一次就切换域名，说明重试次数为0
        describeDataset(ciServiceMyqcloud, "301", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceMyqcloud, "302r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "302", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceMyqcloud, "307r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "307", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceMyqcloud, "308r", CI_HOST_MYQCLOUD, 0);
        // 308不切换域名
        describeDataset(ciServiceMyqcloud, "308", CI_HOST_MYQCLOUD, 0);

        describeDataset(ciServiceTencentCI, "301r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "301", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "302r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "302", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "307r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "307", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "308r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "308", CI_HOST_TENCENTCI, 0);
    }

    @Test
    public void testCiNoSwitch4xx() {
        describeDataset(ciServiceMyqcloudNoSwitch, "400r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "400", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "403r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "403", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "404r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloudNoSwitch, "404", CI_HOST_MYQCLOUD, 0);

        describeDataset(ciServiceTencentCINoSwitch, "400r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "400", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "403r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "403", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "404r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCINoSwitch, "404", CI_HOST_TENCENTCI, 0);
    }

    @Test
    public void testCi4xx() {
        describeDataset(ciServiceMyqcloud, "400r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "400", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "403r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "403", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "404r", CI_HOST_MYQCLOUD, 0);
        describeDataset(ciServiceMyqcloud, "404", CI_HOST_MYQCLOUD, 0);

        describeDataset(ciServiceTencentCI, "400r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "400", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "403r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "403", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "404r", CI_HOST_TENCENTCI, 0);
        describeDataset(ciServiceTencentCI, "404", CI_HOST_TENCENTCI, 0);
    }

    @Test
    public void testCiNoSwitch5xx() {
        describeDataset(ciServiceMyqcloudNoSwitch, "500r", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceMyqcloudNoSwitch, "500", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceMyqcloudNoSwitch, "503r", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceMyqcloudNoSwitch, "503", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceMyqcloudNoSwitch, "504r", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceMyqcloudNoSwitch, "504", CI_HOST_MYQCLOUD, 2);

        describeDataset(ciServiceTencentCINoSwitch, "500r", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCINoSwitch, "500", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCINoSwitch, "503r", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCINoSwitch, "503", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCINoSwitch, "504r", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCINoSwitch, "504", CI_HOST_TENCENTCI, 2);
    }

    @Test
    public void testCi5xx() {
        describeDataset(ciServiceMyqcloud, "500r", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceMyqcloud, "500", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceMyqcloud, "503r", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceMyqcloud, "503", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceMyqcloud, "504r", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceMyqcloud, "504", CI_HOST_TENCENTCI, 2);

        describeDataset(ciServiceTencentCI, "500r", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCI, "500", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCI, "503r", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCI, "503", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCI, "504r", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCI, "504", CI_HOST_TENCENTCI, 2);
    }

    @Test
    public void testCiNoSwitchTimeout() {
        describeDataset(ciServiceMyqcloudNoSwitch, "timeout", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceTencentCINoSwitch, "timeout", CI_HOST_TENCENTCI, 2);
    }

    @Test
    public void testCiTimeout() {
        describeDataset(ciServiceMyqcloud, "timeout", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCI, "timeout", CI_HOST_TENCENTCI, 2);
    }

    @Test
    public void testCiNoSwitchShutdown() {
        describeDataset(ciServiceMyqcloudNoSwitch, "shutdown", CI_HOST_MYQCLOUD, 2);
        describeDataset(ciServiceTencentCINoSwitch, "shutdown", CI_HOST_TENCENTCI, 2);
    }

    @Test
    public void testCiShutdown() {
        describeDataset(ciServiceMyqcloud, "shutdown", CI_HOST_TENCENTCI, 2);
        describeDataset(ciServiceTencentCI, "shutdown", CI_HOST_TENCENTCI, 2);
    }

    public void describeDataset(CIService ciService, String xCiCode, String hostParam, int retryCountParam) {
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
            String host = request.getHttpTask().request().header("Host");
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
            String host = request.getHttpTask().request().header("Host");
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
            String host = postRequest.getHttpTask().request().header("Host");
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
