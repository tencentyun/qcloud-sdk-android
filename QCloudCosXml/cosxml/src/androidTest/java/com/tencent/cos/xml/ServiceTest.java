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

package com.tencent.cos.xml;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.bucket.GetBucketACLRequest;
import com.tencent.cos.xml.model.bucket.GetBucketACLResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

;

@RunWith(AndroidJUnit4.class)
public class ServiceTest {

     private static Context appContext;

     @BeforeClass
     public static void setUp(){
         appContext = TestUtils.getContext();
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
