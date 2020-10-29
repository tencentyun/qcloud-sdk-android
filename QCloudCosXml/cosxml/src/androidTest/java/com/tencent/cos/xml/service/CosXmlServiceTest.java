package com.tencent.cos.xml.service;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.qcloud.core.http.HttpRequest;

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


    @Test public void testSignInUrl() {


        CosXmlService cosXmlService = ServiceFactory.INSTANCE.newSignInUrlService();
        GetServiceRequest getServiceRequest = new GetServiceRequest();
        try {
            GetServiceResult getServiceResult = cosXmlService.getService(getServiceRequest);
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

    }

}
