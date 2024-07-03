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

package com.tencent.cos.xml.common;

import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_BIG_OBJECT_SIZE;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.listener.CosXmlResultSimpleListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.PresignedUrlRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlBytesRequest;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.model.object.PreBuildConnectionRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLCopyTask;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

/**
 * 单次临时密钥测试
 */
@RunWith(AndroidJUnit4.class)
public class OneTimeCredentialTest {
    private SessionQCloudCredentials sessionQCloudCredentials;
    private ShortTimeCredentialProvider shortTimeCredentialProvider;
    @Before
    public void getSessionQCloudCredentials() {
//        final TestLocker testLocker = new TestLocker();
//        new Thread(() -> {
//            try {
//                sessionQCloudCredentials = MySessionCredentialProvider.getSessionQCloudCredentials();
//            } catch (QCloudClientException e) {
//                e.printStackTrace();
//                Assert.fail(e.getMessage());
//            }
//            testLocker.release();
//        }).start();
//        testLocker.lock();

        shortTimeCredentialProvider = new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,60000);
    }

    @Test
    public void testUploadSmallFile() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.filePath("N好.", 1024));
        if(sessionQCloudCredentials != null){
            putObjectRequest.setCredential(sessionQCloudCredentials);
        } else {
            putObjectRequest.setCredentialProvider(shortTimeCredentialProvider);
        }

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i("QCloudTest", result.headers.toString());
                QCloudLogger.i("QCloudTest", result.printResult());
                QCloudLogger.i("QCloudTest", result.accessUrl);
                TestUtils.parseBadResponseBody(result);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                String errMsg = TestUtils.getCosExceptionMessage(clientException, serviceException);
                TestUtils.printError(errMsg);
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test
    public void testUploadBigFile() {
        String localPath = TestUtils.localPath("1642166999131.m4a");
        try {
            TestUtils.createFile(localPath, PERSIST_BUCKET_BIG_OBJECT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                localPath);
        if(sessionQCloudCredentials != null){
            putObjectRequest.setCredential(sessionQCloudCredentials);
        } else {
            putObjectRequest.setCredentialProvider(shortTimeCredentialProvider);
        }

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i("QCloudTest", result.headers.toString());
                QCloudLogger.i("QCloudTest", result.printResult());
                QCloudLogger.i("QCloudTest", result.accessUrl);
                TestUtils.parseBadResponseBody(result);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                String errMsg = TestUtils.getCosExceptionMessage(clientException, serviceException);
                TestUtils.printError(errMsg);
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test public void testSmallDownload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
        if(sessionQCloudCredentials != null){
            getObjectRequest.setCredential(sessionQCloudCredentials);
        } else {
            getObjectRequest.setCredentialProvider(shortTimeCredentialProvider);
        }
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                File file = new File(getObjectRequest.getDownloadPath());
                TestUtils.print("download file size:"+file.length());
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testBigDownload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath());
        if(sessionQCloudCredentials != null){
            getObjectRequest.setCredential(sessionQCloudCredentials);
        } else {
            getObjectRequest.setCredentialProvider(shortTimeCredentialProvider);
        }
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test
    public void testCopySmallObject() {
        final TestLocker testLocker = new TestLocker();
        final String cosPath = "copy_object";
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        if(sessionQCloudCredentials != null){
            copyObjectRequest.setCredential(sessionQCloudCredentials);
        } else {
            copyObjectRequest.setCredentialProvider(shortTimeCredentialProvider);
        }
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(copyObjectRequest);
        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newAnonymousService();
                try {
                    DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(TestConst.PERSIST_BUCKET, cosPath);
                    if(sessionQCloudCredentials != null){
                        deleteObjectRequest.setCredential(sessionQCloudCredentials);
                    } else {
                        deleteObjectRequest.setCredentialProvider(shortTimeCredentialProvider);
                    }
                    cosXmlService.deleteObject(deleteObjectRequest);
                } catch (CosXmlClientException e) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                } catch (CosXmlServiceException e) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                }

                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlCopyTask);
    }

    @Test
    public void testCopyBigObject() {
        final TestLocker testLocker = new TestLocker();
        final String cosPath = "copy_object_big";
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        if(sessionQCloudCredentials != null){
            copyObjectRequest.setCredential(sessionQCloudCredentials);
        } else {
            copyObjectRequest.setCredentialProvider(shortTimeCredentialProvider);
        }
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(copyObjectRequest);
        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newAnonymousService();
                try {
                    DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(TestConst.PERSIST_BUCKET, cosPath);
                    if(sessionQCloudCredentials != null){
                        deleteObjectRequest.setCredential(sessionQCloudCredentials);
                    } else {
                        deleteObjectRequest.setCredentialProvider(shortTimeCredentialProvider);
                    }
                    cosXmlService.deleteObject(deleteObjectRequest);
                } catch (CosXmlClientException e) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                } catch (CosXmlServiceException e) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                }

                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlCopyTask);
    }

    @Test
    public void testPreBuildConnection() {
        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newAnonymousService();
        PreBuildConnectionRequest request = new PreBuildConnectionRequest(TestConst.PERSIST_BUCKET);
        if(sessionQCloudCredentials != null){
            request.setCredential(sessionQCloudCredentials);
        } else {
            request.setCredentialProvider(shortTimeCredentialProvider);
        }
        boolean s = cosXmlService.preBuildConnection(request);
        Assert.assertTrue(s);

        final TestLocker testLocker = new TestLocker();
        cosXmlService.preBuildConnectionAsync(request, new CosXmlResultSimpleListener() {
            @Override
            public void onSuccess() {
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.fail();
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test
    public void testPreBuildConnectionFail() {
        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newAnonymousService();
        PreBuildConnectionRequest request = new PreBuildConnectionRequest(TestConst.PERSIST_BUCKET);
        boolean b1 = cosXmlService.preBuildConnection(request);

        PreBuildConnectionRequest request1 = new PreBuildConnectionRequest("fail-1253960454");
        boolean b2 = cosXmlService.preBuildConnection(request1);

        final boolean[] b3 = {false};
        final TestLocker testLocker = new TestLocker();
        cosXmlService.preBuildConnectionAsync(request, new CosXmlResultSimpleListener() {
            @Override
            public void onSuccess() {
                b3[0] = true;
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                b3[0] = false;
                testLocker.release();
            }
        });
        testLocker.lock();

        final boolean[] b4 = {false};
        final TestLocker testLocker1 = new TestLocker();
        PreBuildConnectionRequest request2 = new PreBuildConnectionRequest("fail-1253960454");
        cosXmlService.preBuildConnectionAsync(request2, new CosXmlResultSimpleListener() {
            @Override
            public void onSuccess() {
                b4[0] = false;
                testLocker1.release();
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                b4[0] = true;
                testLocker1.release();
            }
        });
        Assert.assertTrue(b1 && !b2 && b3[0] &&!b4[0]);
        testLocker1.lock();
    }

    @Test
    public void testHeadObject() {
        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newAnonymousService();
        HeadObjectRequest request = new HeadObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        if(sessionQCloudCredentials != null){
            request.setCredential(sessionQCloudCredentials);
        } else {
            request.setCredentialProvider(shortTimeCredentialProvider);
        }
        try {
            HeadObjectResult result = cosXmlService.headObject(request);
        } catch (CosXmlClientException e) {
            Assert.fail(e.getMessage());
        } catch (CosXmlServiceException e) {
            Assert.fail(e.getMessage());
        }

        final TestLocker testLocker = new TestLocker();
        cosXmlService.headObjectAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                Assert.fail();
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test public void testPresignedDownload() {
        PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        presignedUrlRequest.setRequestMethod("GET");
        presignedUrlRequest.addNoSignHeader("Host");
        if(sessionQCloudCredentials != null){
            presignedUrlRequest.setCredential(sessionQCloudCredentials);
        } else {
            presignedUrlRequest.setCredentialProvider(shortTimeCredentialProvider);
        }
        CosXmlSimpleService defaultService = ServiceFactory.INSTANCE.newAnonymousService();
        try {
            String signUrl = defaultService.getPresignedURL(presignedUrlRequest);
            QCloudLogger.i("QCloudTest", signUrl);
        } catch (CosXmlClientException clientException) {
            QCloudLogger.i("QCloudTest", clientException.getMessage());
            clientException.printStackTrace();
        }
    }
}