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

package com.tencent.cos.xml.quic;

import android.net.Uri;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.quic.QuicClientImpl;
import com.tencent.tquic.impl.TnetConfig;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
public class QuicTest {

    private final boolean testQuic = TestConst.QUIC_TEST;

    @Test
    public void testUploadSmallFileByPath() {

        if (!testQuic) {
            return;
        }

        QuicClientImpl.setTnetConfig(
                new TnetConfig.Builder()
//                        .enableCongetionOptimization(true)
//                        .setTotalTimeoutMillis(1000)
                        .build()
        );

        TransferManager transferManager = ServiceFactory.INSTANCE.newQuicTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.QUIC_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, TestUtils.smallFilePath());


        COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }


    @Test
    public void testUploadSmallFileByUri() {

        if (!testQuic) {
            return;
        }

        TransferManager transferManager = ServiceFactory.INSTANCE.newQuicTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.QUIC_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, Uri.fromFile(new File(TestUtils.smallFilePath())));

        COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test
    public void testUploadBigFileByUri() {

        if (!testQuic) {
            return;
        }

        TransferManager transferManager = ServiceFactory.INSTANCE.newQuicTransferManager();
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.QUIC_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                Uri.fromFile(new File(TestUtils.bigFilePath())), null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test
    public void testUploadBigFileByPath() {

        if (!testQuic) {
            return;
        }

        TransferManager transferManager = ServiceFactory.INSTANCE.newQuicTransferManager();
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.QUIC_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.bigFilePath(), null);

        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i("QCloudTest", "transfer progress is %d/%d", complete, target);
            }
        });
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test
    public void testUploadFileConcurrent() throws Exception{

        if (!testQuic) {
            return;
        }

        final int fileCount = 4;
        TransferManager transferManager = ServiceFactory.INSTANCE.newQuicTransferManager();
        final TestLocker testLocker = new TestLocker(fileCount);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final StringBuilder errorMessage = new StringBuilder();
        for (int i = 0 ; i < fileCount; i++) {

            String cosPath = "uploadTask_cancel" + i;
            final String srcPath = TestUtils.localPath("upload" + i);
            TestUtils.createFile(srcPath, 10 * 1024 * 1024);
            final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.QUIC_BUCKET, cosPath, srcPath, null);
            cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    Log.d(TestConst.UT_TAG,  "Task onProgress " + Thread.currentThread());
                }
            });
            cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    Log.d(TestConst.UT_TAG,  result.printResult());
                    TestUtils.removeLocalFile(srcPath);
                    Log.d(TestConst.UT_TAG,  "Task onSuccess " + Thread.currentThread());
                    testLocker.release();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    Log.d(TestConst.UT_TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                    errorMessage.append(TestUtils.getCosExceptionMessage(exception, serviceException)).append("/r/n");;
                    TestUtils.removeLocalFile(srcPath);
                    errorCount.addAndGet(1);
                    testLocker.release();
                }
            });
            cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
                @Override
                public void onStateChanged(TransferState state) {
                    Log.d(TestConst.UT_TAG,  state.name());
                }
            });
        }
        testLocker.lock();
        TestUtils.assertErrorMessageNull(errorMessage);
    }

    @Test
    public void testHeadQuicObject() {

        if (!testQuic) {
            return;
        }

        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        HeadObjectRequest headObjectRequest = new HeadObjectRequest(TestConst.QUIC_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        try {
            cosXmlService.headObject(headObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBigDownload() {

        if (!testQuic) {
            return;
        }

        TransferManager transferManager = ServiceFactory.INSTANCE.newQuicTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.QUIC_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localPath("downloadobject"));
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
//                QCloudLogger.i("QCloudTest", "transfer progress is %d/%d", complete, target);
            }
        });
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

}
