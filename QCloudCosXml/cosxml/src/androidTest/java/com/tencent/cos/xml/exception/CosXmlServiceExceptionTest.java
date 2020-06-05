package com.tencent.cos.xml.exception;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/3/14.
 */
@RunWith(AndroidJUnit4.class)
public class CosXmlServiceExceptionTest {

    @Test
    public void test() throws Exception{
        CosXmlServiceException cosXmlServiceException = new CosXmlServiceException("NOT FOUND");
        assertEquals("NOT FOUND", cosXmlServiceException.getHttpMessage());

        CosXmlServiceException cosXmlServiceException1 = new CosXmlServiceException("no authorization", null);
        cosXmlServiceException1.setRequestId("requestId");
        cosXmlServiceException1.setStatusCode(403);
        cosXmlServiceException1.setErrorCode("Access Deny");
        cosXmlServiceException1.setServiceName("tencent.com");

        assertEquals(403, cosXmlServiceException1.getStatusCode());
        assertEquals("Access Deny", cosXmlServiceException1.getErrorCode());
        assertEquals("no authorization", cosXmlServiceException1.getErrorMessage());
        assertEquals("requestId", cosXmlServiceException1.getRequestId());
        assertEquals("tencent.com", cosXmlServiceException1.getServiceName());
    }
}