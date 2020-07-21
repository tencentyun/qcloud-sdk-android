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

package com.tencent.cos.xml.temp;

import android.content.Context;

import com.tencent.cos.xml.core.TestUtils;;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.QServer;
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
        Context context = TestUtils.getContext();

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
        Context context = TestUtils.getContext();

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
