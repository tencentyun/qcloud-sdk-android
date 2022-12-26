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

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
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
import com.tencent.cos.xml.model.bucket.GetBucketRequest;
import com.tencent.cos.xml.model.bucket.GetBucketResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.tag.ListBucket;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(AndroidJUnit4.class)
public class DownloadTest {

    @After public void clearDownloadFiles() {
        //TestUtils.clearDir(new File(TestUtils.localParentPath()));
    }

    @Test public void testListDirectory() {

        String region = TestConst.PERSIST_BUCKET_REGION;
        String bucket = TestConst.PERSIST_BUCKET;
        String cosPath = "do_not_remove/";
        Context context = TestUtils.getContext();
        String localDir = context.getExternalCacheDir().getAbsolutePath();


        CosXmlService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        boolean isTruncated = true;
        String marker = null;
        try {
            while (isTruncated) {
                GetBucketRequest getBucketRequest = new GetBucketRequest(region, bucket, cosPath);
                // 设置分页信息
                getBucketRequest.setMarker(marker);
                // 设置不查询子目录
                getBucketRequest.setDelimiter("/");
                getBucketRequest.setMaxKeys(2);
                GetBucketResult getBucketResult = cosXmlService.getBucket(getBucketRequest);
                // 批量下载
                for (ListBucket.Contents content : getBucketResult.listBucket.contentsList) {
                    GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, content.key, localDir);
                    transferManager.download(context,getObjectRequest);
                }
                isTruncated = getBucketResult.listBucket.isTruncated;
                marker = getBucketResult.listBucket.nextMarker;
            }
        } catch (CosXmlServiceException serviceException) {
            serviceException.printStackTrace();
        } catch (CosXmlClientException clientException) {
            clientException.printStackTrace();
        }


    }

    @Test public void testSmallDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
       // getObjectRequest.setRange(100, 200);

        /// getObjectRequest.addNoSignHeader("Range");
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        // downloadTask.startTimeoutTimer(100);
        downloadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {
                InetAddress socketAddress = httpTaskMetrics.getConnectAddress();
                if (socketAddress != null) {
                    QCloudLogger.i(TestConst.UT_TAG, "connect ip is " + socketAddress.getHostAddress());
                }
            }
        });

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
        downloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i("QCloudTest", "transfer state is " + state);
            }
        });



        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testFailSmallDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+"notexist",
                TestUtils.localParentPath());
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
                Assert.assertTrue(true);
            }
        });

        testLocker.lock();

        COSXMLDownloadTask downloadTask1 = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath()+"notexist");
        downloadTask1.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
                Assert.assertTrue(true);
            }
        });
        testLocker.lock();
    }

    @Test public void testBigDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath());
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

            final String localName = "1M_" + i + ".txt";
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

            downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    TestUtils.print("download " + localName + ": " + complete + "/" + target);
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

    @Test public void testAnonymousDownload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_ANONYMOUS_DOWNLOAD_OBJECT_PATH,
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        downloadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {
                InetAddress socketAddress = httpTaskMetrics.getConnectAddress();
                if (socketAddress != null) {
                    QCloudLogger.i(TestConst.UT_TAG, "connect ip is " + socketAddress.getHostAddress());
                }
            }
        });

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
        downloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i("QCloudTest", "transfer state is " + state);
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

}
