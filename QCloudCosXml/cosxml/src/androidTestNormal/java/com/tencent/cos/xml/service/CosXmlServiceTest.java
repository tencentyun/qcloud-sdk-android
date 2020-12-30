package com.tencent.cos.xml.service;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * Created by rickenwang on 2020/10/29.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class CosXmlServiceTest {
    @Test public void testGetService() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newSignInUrlService();
        GetServiceRequest getServiceRequest = new GetServiceRequest();
        try {
            GetServiceResult getServiceResult = cosXmlService.getService(getServiceRequest);
            TestUtils.parseBadResponseBody(getServiceResult);
            Assert.assertNotNull(getServiceResult.printResult());
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void testGetServiceAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newSignInUrlService();
        GetServiceRequest getServiceRequest = new GetServiceRequest();
        cosXmlService.getServiceAsync(getServiceRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TestUtils.parseBadResponseBody(result);
                Assert.assertNotNull(result.printResult());
                Assert.assertTrue(true);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(exception, serviceException));
            }
        });
    }

}
