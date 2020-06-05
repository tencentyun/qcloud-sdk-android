package com.tencent.cos.xml.temp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.STSUnitTest;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.qcloud.core.auth.SessionCredentialProvider;
import com.tencent.qcloud.core.http.HttpRequest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rickenwang on 2019-12-25.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@RunWith(AndroidJUnit4.class)
public class SessionCredentialProviderTest {

    private final boolean test = false;
    private final String authUrl = "https://ricken-public-1253653367.cos.ap-guangzhou.myqcloud.com/auth.json";

    public void testSts3() {


        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(false)
                .setAppidAndRegion(QServer.appid, QServer.region)
                .setDebuggable(true)
                .builder();
        Context context = InstrumentationRegistry.getContext();

        SessionCredentialProvider sts2Provider = null;
        try {
            sts2Provider = new SessionCredentialProvider(new HttpRequest.Builder<String>()
                    .url(new URL(authUrl))
                    .method("GET"),
                    SessionCredentialProvider.StsVersion.VERSION_3);
        } catch (MalformedURLException e) {
            System.out.println("malformedURLException");
            e.printStackTrace();
        }

        CosXmlService cosXmlService = new CosXmlService(context, cosXmlServiceConfig, sts2Provider);
        try {
            GetServiceResult getServiceResult = cosXmlService.getService(new GetServiceRequest());
            System.out.println("xx");
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
    }

    public void testSts2() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(false)
                .setAppidAndRegion(QServer.appid, QServer.region)
                .setDebuggable(true)
                .builder();
        Context context = InstrumentationRegistry.getContext();

        SessionCredentialProvider sts2Provider = null;
        try {
            sts2Provider = new SessionCredentialProvider(new HttpRequest.Builder<String>()
                    .url(new URL(authUrl))
                    .method("GET"));
        } catch (MalformedURLException e) {
            System.out.println("malformedURLException");
            e.printStackTrace();
        }

        CosXmlService cosXmlService = new CosXmlService(context, cosXmlServiceConfig, sts2Provider);
        try {
            GetServiceResult getServiceResult = cosXmlService.getService(new GetServiceRequest());
            System.out.println("xx");
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
    }

    @Test public void test() {

        if (test) {
            testSts2();
            testSts3();
        }
    }
}
