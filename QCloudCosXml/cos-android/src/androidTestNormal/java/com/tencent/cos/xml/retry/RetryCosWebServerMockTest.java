package com.tencent.cos.xml.retry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.qcloud.core.http.interceptor.RetryInterceptor;
import com.tencent.qcloud.core.util.DomainSwitchUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * <p>
 * Created by rickenwang on 2021/11/12.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class RetryCosWebServerMockTest {
    private static final String HOST_MYQCLOUD = "cos-sdk-err-retry-1253960454.cos.ap-chengdu.myqcloud.com";
    private static final String HOST_TENCENTCOS = "cos-sdk-err-retry-1253960454.cos.ap-chengdu.tencentcos.cn";

    private CosXmlSimpleService serviceMyqcloud;
    private CosXmlSimpleService serviceTencentCos;
    private CosXmlSimpleService serviceMyqcloudNoSwitch;
    private CosXmlSimpleService serviceTencentCosNoSwitch;

    private int port = 8090;
    @Before
    public void init() {
        serviceMyqcloud = ServiceFactory.INSTANCE.newRetryServiceMyqcloud(true, port);
        serviceTencentCos = ServiceFactory.INSTANCE.newRetryServiceTencentCos(true, port);
        serviceMyqcloudNoSwitch = ServiceFactory.INSTANCE.newRetryServiceMyqcloud(false, port);
        serviceTencentCosNoSwitch = ServiceFactory.INSTANCE.newRetryServiceTencentCos(false, port);
        try {
            serviceMyqcloud.addCustomerDNS(HOST_MYQCLOUD, new String[]{"127.0.0.1"});
            serviceMyqcloud.addCustomerDNS(HOST_TENCENTCOS, new String[]{"127.0.0.1"});
            serviceMyqcloudNoSwitch.addCustomerDNS(HOST_MYQCLOUD, new String[]{"127.0.0.1"});
            serviceMyqcloudNoSwitch.addCustomerDNS(HOST_TENCENTCOS, new String[]{"127.0.0.1"});
            serviceTencentCos.addCustomerDNS(HOST_TENCENTCOS, new String[]{"127.0.0.1"});
            serviceTencentCosNoSwitch.addCustomerDNS(HOST_TENCENTCOS, new String[]{"127.0.0.1"});
        } catch (CosXmlClientException e) {
            throw new RuntimeException(e);
        }
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
    public void testSwitch3xx() {
        getObject(serviceMyqcloud, "301r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "301r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "301", HOST_TENCENTCOS, 0);
        getObject(serviceTencentCos, "301", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "302r", HOST_MYQCLOUD, 0);
        getObject(serviceTencentCos, "302r", HOST_TENCENTCOS, 0);

        getObject(serviceMyqcloud, "302", HOST_TENCENTCOS, 0);
        getObject(serviceTencentCos, "302", HOST_TENCENTCOS, 0);
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
        getObject(serviceMyqcloudNoSwitch, "500r", HOST_MYQCLOUD, 2);
        getObject(serviceTencentCosNoSwitch, "500r", HOST_TENCENTCOS, 2);

        getObject(serviceMyqcloudNoSwitch, "500", HOST_MYQCLOUD, 2);
        getObject(serviceTencentCosNoSwitch, "500", HOST_TENCENTCOS, 2);

        getObject(serviceMyqcloudNoSwitch, "503r", HOST_MYQCLOUD, 2);
        getObject(serviceTencentCosNoSwitch, "503r", HOST_TENCENTCOS, 2);

        getObject(serviceMyqcloudNoSwitch, "503", HOST_MYQCLOUD, 2);
        getObject(serviceTencentCosNoSwitch, "503", HOST_TENCENTCOS, 2);

        getObject(serviceMyqcloudNoSwitch, "504r", HOST_MYQCLOUD, 2);
        getObject(serviceTencentCosNoSwitch, "504r", HOST_TENCENTCOS, 2);

        getObject(serviceMyqcloudNoSwitch, "504", HOST_MYQCLOUD, 2);
        getObject(serviceTencentCosNoSwitch, "504", HOST_TENCENTCOS, 2);
    }

    @Test
    public void testSwitch5xx() {
        getObject(serviceMyqcloud, "500r", HOST_MYQCLOUD, 2);
        // 切换域名后重试次数重置为0，但其实重试了3次
        getObject(serviceMyqcloud, "500", HOST_TENCENTCOS, 0);
        getObject(serviceMyqcloud, "503r", HOST_MYQCLOUD, 2);
        getObject(serviceMyqcloud, "503", HOST_TENCENTCOS, 0);
        getObject(serviceMyqcloud, "504r", HOST_MYQCLOUD, 2);
        getObject(serviceMyqcloud, "504", HOST_TENCENTCOS, 0);

        getObject(serviceTencentCos, "500r", HOST_TENCENTCOS, 2);
        getObject(serviceTencentCos, "500", HOST_TENCENTCOS, 2);
        getObject(serviceTencentCos, "503r", HOST_TENCENTCOS, 2);
        getObject(serviceTencentCos, "503", HOST_TENCENTCOS, 2);
        getObject(serviceTencentCos, "504r", HOST_TENCENTCOS, 2);
        getObject(serviceTencentCos, "504", HOST_TENCENTCOS, 2);
    }

    @Test
    public void testNoSwitchTimeout() {
        getObject(serviceMyqcloudNoSwitch, "timeout", HOST_MYQCLOUD, 2);
        getObject(serviceTencentCosNoSwitch, "timeout", HOST_TENCENTCOS, 2);
    }

    @Test
    public void testSwitchTimeout() {
        // 切换域名后重试次数重置为0，但其实重试了3次
        getObject(serviceMyqcloud, "timeout", HOST_TENCENTCOS, 0);
        getObject(serviceTencentCos, "timeout", HOST_TENCENTCOS, 2);
    }

    @Test
    public void testNoSwitchShutdown() {
        getObject(serviceMyqcloudNoSwitch, "shutdown", HOST_MYQCLOUD, 2);
        getObject(serviceTencentCosNoSwitch, "shutdown", HOST_TENCENTCOS, 2);
    }

    @Test
    public void testSwitchShutdown() {
        // 切换域名后重试次数重置为0，但其实重试了3次
        getObject(serviceMyqcloud, "shutdown", HOST_TENCENTCOS, 0);
        getObject(serviceTencentCos, "shutdown", HOST_TENCENTCOS, 2);
    }

    private void mockResponse(MockWebServer server, String xCiCode) {
        try {
            server.start(InetAddress.getByName("127.0.0.1"), port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 设置 mock 响应
        MockResponse mockResponse = new MockResponse();
        if (xCiCode.endsWith("r")) {
            mockResponse.addHeader("x-ci-request-id", "x-ci-request-id-xxxxx");
            mockResponse.addHeader("x-cos-request-id", "x-cos-request-id-xxxxx");
            xCiCode = xCiCode.substring(0, xCiCode.length() - 1);
        }
        // 超时
        if(xCiCode.equals("timeout")){
            mockResponse.setHeadersDelay(10, TimeUnit.SECONDS);
        }
        // shutdown
        if(xCiCode.equals("shutdown")){
            mockResponse.setHeadersDelay(3, TimeUnit.SECONDS);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        server.close();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
        try {
            int mockCode = Integer.parseInt(xCiCode);
            mockResponse.setResponseCode(mockCode);
        } catch (NumberFormatException e){
        }
        for (int i = 0; i < 10; i++){
            server.enqueue(mockResponse);
        }
    }

    public void getObject(CosXmlSimpleService cosXmlSimpleService, String cosKey, String hostParam, int retryCountParam)   {
        MockWebServer server = new MockWebServer();
        mockResponse(server, cosKey);

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
        RetryInterceptor.resetHostReliable(HOST_MYQCLOUD);
        RetryInterceptor.resetHostReliable(HOST_TENCENTCOS);
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
