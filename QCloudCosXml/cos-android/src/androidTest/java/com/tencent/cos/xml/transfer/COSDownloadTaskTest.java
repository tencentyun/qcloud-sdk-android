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

package com.tencent.cos.xml.transfer;

import androidx.test.ext.junit.runners.AndroidJUnit4;

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
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class COSDownloadTaskTest {

    @After public void clearDownloadFiles() {
         TestUtils.clearDir(new File(TestUtils.localParentPath()));
    }

    @Test public void testSmallCesDownload() {

        TransferService transferService = ServiceFactory.INSTANCE.newCesTransferService();
        downloadObject(transferService, TestConst.PERSIST_BUCKET_CSE_SMALL_OBJECT_PATH);
    }

    @Test public void testBigCesDownload() {
        TransferService transferService = ServiceFactory.INSTANCE.newCesTransferService();
        downloadObject(transferService, TestConst.PERSIST_BUCKET_CSE_BIG_OBJECT_PATH);
    }

//    @Test
    public void testCesPauseAndResume() {
        TransferService transferService = ServiceFactory.INSTANCE.newCesTransferService();
        testPauseAndResume(transferService, TestConst.PERSIST_BUCKET_CSE_BIG_OBJECT_PATH);
    }

    // 简单下载小文件
    @Test public void testSmallDownload() {

        TransferService transferService = ServiceFactory.INSTANCE.newDefaultTransferService();
        downloadObject(transferService, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    @Test public void testAnonymousDownload() {
        TransferService transferService = ServiceFactory.INSTANCE.newAnonymousTransferService();
        downloadObject(transferService, TestConst.PERSIST_BUCKET_CDN_BIG_OBJECT_PATH);
    }

    @Test public void testAnonymousPauseAndResume() {
        TestUtils.sleep(10000);
        TransferService transferService = ServiceFactory.INSTANCE.newAnonymousTransferService();
        testPauseAndResume(transferService, TestConst.PERSIST_BUCKET_CDN_BIG_60M_OBJECT_PATH);
    }

    @Test public void testAnonymousPauseAndResumeNow() {
        TestUtils.sleep(10000);
        TransferService transferService = ServiceFactory.INSTANCE.newAnonymousTransferService();
        testPauseAndResumeNow(transferService, TestConst.PERSIST_BUCKET_CDN_BIG_60M_OBJECT_PATH);
    }

//    @Test
    public void testPauseAndResume() {
        TestUtils.sleep(10000);
        TransferService transferService = ServiceFactory.INSTANCE.newDefaultTransferService();
        testPauseAndResume(transferService, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH);
    }

    private void downloadObject(TransferService transferService, String key) {

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                key,
                TestUtils.localParentPath());
//        getObjectRequest.addQuery("imageMogr2/thumbnail/!50p|watermark/2/text/5pWw5o2u5LiH6LGh/fill/I0ZGRkZGRg==/fontsize/30/dx/20/dy/20", null);
        // getObjectRequest.addNoSignHeader("Host");
        // getObjectRequest.addNoSignHeader("Range");
        // getObjectRequest.addNoSignParams("imageMogr2/thumbnail/!50p");
        try {
            getObjectRequest.setRequestHeaders("key", "value", false);
        } catch (CosXmlClientException clientException) {
            clientException.printStackTrace();
        }
        COSDownloadTask downloadTask = transferService.download(getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                if (clientException != null) {
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    serviceException.printStackTrace();
                }
                testLocker.release();
            }
        });
        downloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i("QCloudTest", "transfer state is " + state);
            }
        });
        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i("QCloudTest", "transfer progress is %d/%d", complete, target);
            }
        });

        testLocker.lock();

        TransferTaskMetrics transferTaskMetrics = downloadTask.getTransferTaskMetrics();
        QCloudLogger.i(TestConst.UT_TAG, transferTaskMetrics.toString());
        Assert.assertNotNull(transferTaskMetrics);
        Assert.assertNotNull(transferTaskMetrics.domain);
        Assert.assertNotNull(transferTaskMetrics.connectAddress);
        Assert.assertTrue(transferTaskMetrics.getSize() >= 0);
        Assert.assertTrue(transferTaskMetrics.getTookTime() >= 0);
        Assert.assertTrue(transferTaskMetrics.getFirstProgressTookTime() >= 0);
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    /**
     * 下载 1s 后点击暂停，并等待 1s 后恢复上传
     */
//    private void testPauseAndResume(TransferService transferService, String key) {
//        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
//                key,
//                TestUtils.localParentPath());
//        COSDownloadTask downloadTask = transferService.download(getObjectRequest);
//
//        final TestLocker testLocker = new TestLocker();
//        final AtomicBoolean checkNotZero = new AtomicBoolean(false);
//        final AtomicLong currentProgress = new AtomicLong(0);
//
//        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
//            @Override
//            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                testLocker.release();
//            }
//
//            @Override
//            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
//                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
//                testLocker.release();
//            }
//        });
//
//        downloadTask.setTransferStateListener(new TransferStateListener() {
//            @Override
//            public void onStateChanged(TransferState state) {
//                TestUtils.print("download task state " + state);
//            }
//        });
//
//        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
//            @Override
//            public void onProgress(long complete, long target) {
//
//                currentProgress.set(complete);
//                if (checkNotZero.get()) {
//                    checkNotZero.set(false);
//                    if (complete < currentProgress.get()) {
//                        testLocker.release();
//                    }
//                }
//                TestUtils.print("download task " + complete + "/" + target);
//            }
//        });
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        if (downloadTask.getTaskState() == TransferState.COMPLETED) {
//            TestUtils.assertCOSXMLTaskSuccess(downloadTask);
//            return;
//        }
//
//        downloadTask.pause();
//        TestUtils.sleep(3000);
//        checkNotZero.set(true);
//        downloadTask.resume();
//
//        testLocker.lock();
//        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
//    }

    private void testPauseAndResume(TransferService transferService, String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                key,
                TestUtils.localParentPath());
        COSDownloadTask downloadTask = transferService.download(getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (downloadTask.getTaskState() == TransferState.COMPLETED) {
            TestUtils.assertCOSXMLTaskSuccess(downloadTask);
            return;
        }

        downloadTask.pause();
        TestUtils.sleep(1000);
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
        downloadTask.resume();

        testLocker.lock(30000);
        TestUtils.sleep(10000);
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    private void testPauseAndResumeNow(TransferService transferService, String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                key,
                TestUtils.localParentPath());
        COSDownloadTask downloadTask = transferService.download(getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (downloadTask.getTaskState() == TransferState.COMPLETED) {
            TestUtils.assertCOSXMLTaskSuccess(downloadTask);
            return;
        }

        downloadTask.pause(true);
        TestUtils.sleep(1000);
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
        downloadTask.resume();

        testLocker.lock(30000);
        TestUtils.sleep(10000);
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testCancel() {
        TransferService transferService = ServiceFactory.INSTANCE.newDefaultTransferService();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath());
        COSDownloadTask downloadTask = transferService.download(getObjectRequest);
        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
                TestUtils.assertCOSXMLTaskSuccess(downloadTask);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                testLocker.release();
                Assert.assertEquals("UserCancelled", clientException.getMessage());
            }
        });
        TestUtils.sleep(1000);
        downloadTask.cancel();
        TestUtils.sleep(1000);
        //仅仅为了覆盖异常状态日志打印
        downloadTask.pause();
        downloadTask.resume();
        testLocker.lock();
    }

    @Test public void testCancelNow() {
        TransferService transferService = ServiceFactory.INSTANCE.newDefaultTransferService();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath());
        COSDownloadTask downloadTask = transferService.download(getObjectRequest);
        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
                TestUtils.assertCOSXMLTaskSuccess(downloadTask);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                testLocker.release();
                Assert.assertEquals("UserCancelled", clientException.getMessage());
            }
        });
        TestUtils.sleep(1000);
        downloadTask.cancel(true);
        TestUtils.sleep(1000);
        //仅仅为了覆盖异常状态日志打印
        downloadTask.pause(true);
        downloadTask.resume();
        testLocker.lock();
    }

    // TODO: 2023/4/27 新的下载 不支持range
    @Test public void testRange() {
        TransferService transferService = ServiceFactory.INSTANCE.newDefaultTransferService();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath());
        getObjectRequest.setRange(100);
        getObjectRequest.setRange(0, 1000);
        COSDownloadTask downloadTask = transferService.download(getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
                Assert.fail();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                testLocker.release();
                Assert.assertTrue(clientException.getMessage().startsWith("verify CRC64 failed"));
            }
        });
        testLocker.lock();
    }
}
