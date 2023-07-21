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

import static com.tencent.cos.xml.core.TestUtils.smallFilePath;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.MyOnSignatureListener;
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
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
public class DownloadTest {

    @After public void clearDownloadFiles() {
        //TestUtils.clearDir(new File(TestUtils.localParentPath()));
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
    @Test public void testBatchSmallDownload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        int count = 20;
        final TestLocker testLocker = new TestLocker(count);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final AtomicInteger successCount = new AtomicInteger(0);
        final StringBuilder errorMessage = new StringBuilder();

        for (int i = 0; i < count; i ++) {

            final String localName = "1M_" + i + ".txt";
            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    TestUtils.localParentPath(), localName);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    successCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                    }
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    errorMessage.append(TestUtils.getCosExceptionMessage(clientException, serviceException)).append("/r/n");
                    errorCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                    }
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
     * 测试续传
     *
     * 下载 2s 后点击暂停，并记录当前的下载进度，等待 1s 后重新下载，进度必须大于之前记录的进度
     */
    @Test public void testContinueDownload() {
        TestUtils.sleep(10000);
        String localFileName = TestUtils.extractName(TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH);

        TestUtils.removeLocalFile(TestUtils.localPath(localFileName));

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath(), localFileName);

        TestUtils.sleep(4000);
        downloadTask.pause();
        TestUtils.sleep(4000);
//        final StringBuilder errorMessage = new StringBuilder();

        final COSXMLDownloadTask continueTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath(), localFileName);

        final TestLocker testLocker = new TestLocker();

        final long currentProgress = new File(TestUtils.localParentPath(), localFileName).length();
//        continueTask.setCosXmlProgressListener(new CosXmlProgressListener() {
//            @Override
//            public void onProgress(long complete, long target) {
//
//                if (complete < currentProgress) {
//                    errorMessage.append("continue complete is " + complete + ", but current progress is " + currentProgress);
//                    testLocker.release();
//                }
//            }
//        });

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
//        TestUtils.assertErrorMessageNull(errorMessage);
        TestUtils.sleep(10000);
        TestUtils.assertCOSXMLTaskSuccess(continueTask);
    }

//    @Test public void testContinueDownloadExisted() {
//        String localFileName = TestUtils.big60mFilePath();
//        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
//        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
//                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
//                localFileName);
//
//        TestUtils.sleep(4000);
//        downloadTask.pause();
//        TestUtils.sleep(4000);
//
//        TestUtils.big60mFilePath();
//        final COSXMLDownloadTask continueTask = transferManager.download(TestUtils.getContext(),
//                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
//                localFileName);
//
//        final TestLocker testLocker = new TestLocker();
//
//        continueTask.setCosXmlResultListener(new CosXmlResultListener() {
//            @Override
//            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                testLocker.release();
//            }
//
//            @Override
//            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
//                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
//                testLocker.release();
//            }
//        });
//
//        testLocker.lock();
//        TestUtils.assertCOSXMLTaskSuccess(continueTask);
//    }

    @Test public void testAnonymousDownload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_CDN_BIG_OBJECT_PATH,
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

    @Test public void testSmallDownloadSignature() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath(), "test", new MyOnSignatureListener());

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

    @Test public void testSmallDownloadRange() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
         getObjectRequest.setRange(0, 10);
        getObjectRequest.addNoSignHeader("Range");
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

    @Test public void testSmallDownloadRangeFaild() {
        // 能通过head  但是get服务端非200-300
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
//        getObjectRequest.setIfModifiedSince("Wed, 21 Oct 2009 07:28:00 GMT");
//        getObjectRequest.setIfUnmodifiedSince("Wed, 21 Oct 2052 07:28:00 GMT");
        getObjectRequest.setIfMatch("none_match_etag");
//        getObjectRequest.setIfNONEMatch("none_match_etag");
        getObjectRequest.addNoSignHeader("Range");
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                Assert.assertTrue(true);
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test public void testDownloadClientError() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                "aaaaaaa");
        final TestLocker testLocker = new TestLocker();
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(clientException != null){
                    Assert.assertTrue(true);
                } else {
                    Assert.fail();
                }
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test public void testSmallDownloadToUri() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                Uri.fromFile(new File(smallFilePath())));
        Assert.assertNotNull(getObjectRequest.getFileContentUri());
        try {
            cosXmlSimpleService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (CosXmlServiceException e) {
            Assert.fail(e.getMessage());
            return;
        }
        Assert.assertTrue(true);
    }
}
