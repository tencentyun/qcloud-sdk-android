package com.tencent.cos.xml;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.bucket.GetBucketACLRequest;
import com.tencent.cos.xml.model.bucket.GetBucketACLResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by bradyxiao on 2018/11/15.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */

@RunWith(AndroidJUnit4.class)
public class ServiceTest {

     private static Context appContext;

     @BeforeClass
     public static void setUp(){
         appContext = InstrumentationRegistry.getContext();
         QServer.init(appContext);
     }

     @Test
     public void testGetService() throws Exception{
         GetServiceRequest getServiceRequest = new GetServiceRequest();
         GetServiceResult result = QServer.cosXml.getService(getServiceRequest);
         Log.d(QServer.TAG, result.printResult());
     }

     @Test
    public void testSetDomain() {

         QServer.cosXml.setDomain("otherRequestHost");
         QServer.cosXml.setServiceDomain("getServiceRequestHost");

         try {
             GetServiceRequest getServiceRequest = new GetServiceRequest();
             GetServiceResult result = QServer.cosXml.getService(getServiceRequest);
         } catch (CosXmlClientException e) {
             e.printStackTrace();
         } catch (CosXmlServiceException e) {
             e.printStackTrace();
         }

         try {
             GetBucketACLRequest getBucketACLRequest = new GetBucketACLRequest("bucket");
             GetBucketACLResult getBucketACLResult = QServer.cosXml.getBucketACL(getBucketACLRequest);
         } catch (CosXmlClientException e) {
             e.printStackTrace();
         } catch (CosXmlServiceException e) {
             e.printStackTrace();
         }


         QServer.cosXml.setDomain(null);
         QServer.cosXml.setServiceDomain(null);

         try {
             GetServiceRequest getServiceRequest = new GetServiceRequest();
             GetServiceResult result = QServer.cosXml.getService(getServiceRequest);
         } catch (CosXmlClientException e) {
             e.printStackTrace();
         } catch (CosXmlServiceException e) {
             e.printStackTrace();
         }

         try {
             GetBucketACLRequest getBucketACLRequest = new GetBucketACLRequest("bucket");
             GetBucketACLResult getBucketACLResult = QServer.cosXml.getBucketACL(getBucketACLRequest);
         } catch (CosXmlClientException e) {
             e.printStackTrace();
         } catch (CosXmlServiceException e) {
             e.printStackTrace();
         }

     }

     @Test
     public void testTrafficLimit() {

         try {
             GetObjectRequest getObjectRequest = new GetObjectRequest(QServer.persistBucket, "cosPath", Environment.getExternalStorageDirectory().toString() + "res");
             getObjectRequest.setTrafficLimit(819200);
             GetObjectResult result = QServer.cosXml.getObject(getObjectRequest);
         } catch (CosXmlClientException e) {
             e.printStackTrace();
         } catch (CosXmlServiceException e) {
             e.printStackTrace();
         }
     }

}
