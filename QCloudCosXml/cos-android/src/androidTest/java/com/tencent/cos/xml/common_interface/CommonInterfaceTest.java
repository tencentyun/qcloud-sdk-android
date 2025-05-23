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

package com.tencent.cos.xml.common_interface;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * <p>
 * Created by jordanqin on 2020/12/29.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class CommonInterfaceTest {
    CosXmlSimpleService defaultService;
    @Before
    public void before() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.CI_BUCKET_REGION)
                .builder();
        defaultService = ServiceFactory.INSTANCE.newService(cosXmlServiceConfig);
    }

    @Test public void commonInterface() {
        PostVirusDetectRequest postRequest = new PostVirusDetectRequest(TestConst.CI_BUCKET);
        PostVirusDetect.VirusDetectInput input = new PostVirusDetect.VirusDetectInput();
        input.object = TestConst.AUDIT_BUCKET_AUDIO;
        postRequest.setInput(input);
        try {
            PostVirusDetectResult result = defaultService.commonInterface(postRequest, PostVirusDetectResult.class);
            TestUtils.printXML(result.response);
            Assert.assertNotNull(result.response);
        } catch (CosXmlClientException | CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void commonInterfaceAsync() {
        PostVirusDetectRequest postRequest = new PostVirusDetectRequest(TestConst.CI_BUCKET);
        PostVirusDetect.VirusDetectInput input = new PostVirusDetect.VirusDetectInput();
        input.object = TestConst.AUDIT_BUCKET_AUDIO;
        postRequest.setInput(input);

        final TestLocker testLocker = new TestLocker();
        defaultService.commonInterfaceAsync(postRequest, PostVirusDetectResult.class, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                PostVirusDetectResult result = (PostVirusDetectResult) resultArg;
                TestUtils.printXML(result.response);
                Assert.assertNotNull(result.response);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }
}