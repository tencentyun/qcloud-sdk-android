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

package com.tencent.cos.xml.signer;

import android.content.Context;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.bucket.GetBucketRequest;
import com.tencent.cos.xml.model.bucket.GetBucketResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.STSCredentialScope;
import com.tencent.qcloud.core.auth.STSScopeLimitCredentialProvider;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;


@RunWith(AndroidJUnit4.class)
public class STSUnitTest {

    Context context;

    CosXmlService scopeService;
    CosXmlService skewwdService;

    TrackedSTSScopeLimitCredentialProvider trackedSTSScopeLimitCredentialProvider;
    SkewedSessionCredentialProvider skewedSessionCredentialProvider;

    final boolean testSTS = false;

    private class SkewedSessionCredentialProvider extends STSLocalProvider {
        private int i = 0;

        public SkewedSessionCredentialProvider(String secretId, String secretKey, String appId, String region) {
            super(secretId, secretKey, appId, region);
        }

        @Override
        protected SessionQCloudCredentials parseServerResponse(String jsonContent) throws QCloudClientException {
            SessionQCloudCredentials credentials = super.parseServerResponse(jsonContent);
            i++;
            if (i < 2) {
                return new SessionQCloudCredentials(
                        credentials.getSecretId(),
                        credentials.getSecretKey(),
                        credentials.getToken(),
                        credentials.getStartTime() - 3000,
                        credentials.getExpiredTime() - 3000
                );
            } else {
                return credentials;
            }
        }
    }

    private class TrackedSTSScopeLimitCredentialProvider extends STSScopeLimitCredentialProvider {

        private int fetchNewCount;

        void reset () {
            fetchNewCount = 0;
        }

        public int getFetchNewCount() {
            return fetchNewCount;
        }

        public TrackedSTSScopeLimitCredentialProvider(HttpRequest.Builder<String> requestBuilder) {
            super(requestBuilder);
        }

        @Override
        public SessionQCloudCredentials fetchNewCredentials(STSCredentialScope[] credentialScope)
                throws QCloudClientException {
            QCloudLogger.i("QCloudHttp", "fetch new credentials : " + STSCredentialScope.jsonify(credentialScope));
            fetchNewCount++;
            return super.fetchNewCredentials(credentialScope);
        }
    }

    @Before
    public void setup() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(false)
                .setAppidAndRegion(TestConst.COS_APPID, TestConst.PERSIST_BUCKET_REGION)
                .setDebuggable(true)
                .builder();
        context = TestUtils.getContext();

        trackedSTSScopeLimitCredentialProvider = new TrackedSTSScopeLimitCredentialProvider(new HttpRequest.Builder<String>()
                .scheme("http")
                .host("10.0.2.2")
                .port(3000)
                .path("/sts"));
        skewedSessionCredentialProvider = new SkewedSessionCredentialProvider(
                TestConst.SECRET_ID,
                TestConst.SECRET_KEY,
                TestConst.COS_APPID,
                TestConst.PERSIST_BUCKET_REGION
        );

        scopeService = new CosXmlService(context, cosXmlServiceConfig, trackedSTSScopeLimitCredentialProvider);
        skewwdService = new CosXmlService(context, cosXmlServiceConfig, skewedSessionCredentialProvider);
    }

    // @Test

    /**
     * 服务器时间和本地时间差值很小，不太好测试
     */
    public void testClockSkewed() throws QCloudClientException, QCloudServiceException {
        GetBucketRequest getBucketRequest = new GetBucketRequest(TestConst.PERSIST_BUCKET);
        HttpTaskMetrics metrics = new HttpTaskMetrics();
        getBucketRequest.attachMetrics(metrics);
        GetBucketResult getBucketResult =  skewwdService.getBucket(getBucketRequest);
        Assert.assertTrue(getBucketResult.httpCode == 200);
        QCloudLogger.i("QCloudHttp", metrics.toString());
    }

    @Test
    public void testGetBucket() throws QCloudClientException, QCloudServiceException {
        if (!testSTS) {
            return;
        }

        GetBucketRequest getBucketRequest = new GetBucketRequest(TestConst.PERSIST_BUCKET);
        GetBucketResult getBucketResult =  scopeService.getBucket(getBucketRequest);
        Assert.assertTrue(getBucketResult.httpCode == 200);
    }

    @Test
    public void testPutObject() throws IOException, QCloudClientException, QCloudServiceException {
        if (!testSTS) {
            return;
        }

        String srcPath = TestUtils.localPath("1m.txt");
        TestUtils.createFile(srcPath, 1024 * 1024);
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET, "ip.txt", srcPath);
        PutObjectResult putObjectResult = scopeService.putObject(putObjectRequest);
        Assert.assertTrue(putObjectResult.httpCode == 200);
    }

    @Test
    public void testGetService() throws QCloudClientException, QCloudServiceException {
        if (!testSTS) {
            return;
        }

        GetServiceRequest getServiceRequest = new GetServiceRequest();
        GetServiceResult getServiceResult = scopeService.getService(getServiceRequest);
        Assert.assertTrue(getServiceResult.httpCode == 200);
    }

    @Test
    public void testMultiUpload() throws IOException, InterruptedException {
        if (!testSTS) {
            return;
        }
        
        TransferManager transferManager = new TransferManager(scopeService,
                new TransferConfig.Builder().build());

        final String srcPath = TestUtils.localPath("1m.txt");
        TestUtils.createFile(srcPath, 5 * 1024 * 1024);
        String cosPath = "uploadTask_myMultiUploads";
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath,
                srcPath, null);
        final Object lock = new Object();

        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.assertTrue(result.httpCode == 200);
                TestUtils.removeLocalFile(srcPath);
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TestConst.UT_TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                Assert.assertNull(exception);
                Assert.assertNull(serviceException);
                synchronized (lock) {
                    lock.notify();
                }
            }
        });

        synchronized (lock) {
            lock.wait();
        }
    }

    @Test
    public void testPolicySwitch() throws QCloudServiceException, QCloudClientException, IOException,
    InterruptedException{
        trackedSTSScopeLimitCredentialProvider.reset();
        testPutObject();
        testMultiUpload();
        testPutObject();
        testMultiUpload();
        testGetBucket();
        if(testSTS){
            Assert.assertEquals(trackedSTSScopeLimitCredentialProvider.getFetchNewCount(), 3);
        }else {
            Assert.assertEquals(trackedSTSScopeLimitCredentialProvider.getFetchNewCount(), 0);
        }

    }


}
