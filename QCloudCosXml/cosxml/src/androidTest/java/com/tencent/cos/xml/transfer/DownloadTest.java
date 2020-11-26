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
import com.tencent.cos.xml.utils.StringUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(AndroidJUnit4.class)
public class DownloadTest {

    @After public void clearDownloadFiles() {
        TestUtils.clearDir(new File(TestUtils.localParentPath()));
    }

    @Test public void testSmallDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localPath(TestUtils.extractName(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH)));
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

    @Test public void testBigDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localPath(TestUtils.extractName(TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH)));
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

    /**
     * 批量文件下载
     */
    @Test public void testBatchSmallUpload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        int count = 10;
        final TestLocker testLocker = new TestLocker(count);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final StringBuilder errorMessage = new StringBuilder();

        for (int i = 0; i < count; i ++) {

            String localName = "1M_" + i + ".txt";
            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    TestUtils.localParentPath(), localName);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    testLocker.release();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    errorMessage.append(TestUtils.getCosExceptionMessage(clientException, serviceException)).append("/r/n");
                    errorCount.addAndGet(1);
                    testLocker.release();
                }
            });
        }

        testLocker.lock();
        TestUtils.assertErrorMessageNull(errorMessage);
    }


    /**
     * 下载 2s 后点击暂停，并等待 2s 后恢复上传
     */
    @Test public void testPauseAndResume() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath());

        final TestLocker testLocker = new TestLocker();
        final AtomicBoolean checkNotZero = new AtomicBoolean(false);
        final AtomicLong currentProgress = new AtomicLong(0);

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

        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

                currentProgress.set(complete);
                if (checkNotZero.get()) {
                    checkNotZero.set(false);
                    if (complete < currentProgress.get()) {
                        testLocker.release();
                    }
                }
                TestUtils.print("download task " + complete + "/" + target);
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (downloadTask.getTaskState() == TransferState.COMPLETED) {
            return;
        }

        downloadTask.pause();
        TestUtils.sleep(2000);
        checkNotZero.set(true);
        downloadTask.resume();

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }


    /**
     * 测试续传
     *
     * 下载 2s 后点击暂停，并记录当前的下载进度，等待 1s 后重新下载，进度必须大于之前记录的进度
     */
    @Test public void testContinueDownload() {

        String localFileName = TestUtils.extractName(TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);

        TestUtils.removeLocalFile(TestUtils.localPath(localFileName));

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath(), localFileName);

        TestUtils.sleep(4000);
        downloadTask.pause();
        final StringBuilder errorMessage = new StringBuilder();

        final COSXMLDownloadTask continueTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath(), localFileName);

        final TestLocker testLocker = new TestLocker();

        final long currentProgress = new File(TestUtils.localParentPath(), localFileName).length();
        continueTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

                if (complete < currentProgress) {
                    errorMessage.append("continue complete is " + complete + ", but current progress is " + currentProgress);
                    testLocker.release();
                }
            }
        });

        continueTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertErrorMessageNull(errorMessage);
        TestUtils.assertCOSXMLTaskSuccess(continueTask);
    }


}
