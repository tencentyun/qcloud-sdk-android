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

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
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
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


@RunWith(AndroidJUnit4.class)
public class UploadTest {

    private final String UPLOAD_FOLDER="/upload/";

    /**
     * 测试任务优先级
     *
     */
    @Test public void testUploadPriority() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final int lowPriorityTaskCount = 5;
        final int normalPriorityTaskCount = 5;
        final TestLocker testLocker = new TestLocker(lowPriorityTaskCount + normalPriorityTaskCount);
        final AtomicInteger uploadSuccessCount = new AtomicInteger();
        for (int i = 0; i < lowPriorityTaskCount; i++) {
            String filePath = TestUtils.localPath("lowPriorityFile" + i);
            try {
                TestUtils.createFile(filePath, 10 * 1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
            PutObjectRequest putObjectRequest= new PutObjectRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH + "low" + i, filePath);
            putObjectRequest.setPriorityLow();
            final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
            uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    QCloudLogger.i("QCloudTest", "upload success low ");
                    int count = uploadSuccessCount.addAndGet(1);
                    if (count <= lowPriorityTaskCount) {
                        Assert.fail("upload low priority task success, but count is " + count);
                    }
                    testLocker.release();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    testLocker.release();
                }
            });
        }

        for (int i = 0; i < normalPriorityTaskCount; i++) {
            String filePath = TestUtils.localPath("normalPriorityFile" + i);
            try {
                TestUtils.createFile(filePath, 10 * 1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
            PutObjectRequest putObjectRequest= new PutObjectRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH + "normal" + i, filePath);
            final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
            uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    uploadSuccessCount.addAndGet(1);
                    QCloudLogger.i("QCloudTest", "upload success normal ");
                    testLocker.release();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    testLocker.release();
                }
            });
        }

        testLocker.lock();
    }


    /**
     * 测试上传任务等待超时
     */
    @Test public void testUploadWaitingTimeout() {

        String filePath1 = TestUtils.localPath("file1");
        String filePath2 = TestUtils.localPath("file2");
        try {
            TestUtils.createFile(filePath1, 10 * 1024 * 1024);
            TestUtils.createFile(filePath2, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PutObjectRequest putObjectRequest1 = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH + 1, filePath1);
        PutObjectRequest putObjectRequest2= new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH + 2, filePath2);
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        // 上传两个大文件占用线程
        cosXmlSimpleService.putObjectAsync(putObjectRequest1, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {

            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {

            }
        });
        cosXmlSimpleService.putObjectAsync(putObjectRequest2, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {

            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {

            }
        });

        TestUtils.sleep(500);

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, TestUtils.smallFilePath());

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.startTimeoutTimer(1000);
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail("upload success");
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.assertTrue(TestUtils.getCosExceptionMessage(clientException, serviceException).contains("Task waiting timeout"));
                testLocker.release();
            }
        });
        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i("QCloudTest", "upload state is " + state);
            }
        });

        testLocker.lock();
    }



    @Test public void testUploadSmallFileByPath10() {
        final long sleep = 10000;
        final int count = 9;
        for (int i = 0; i <= count; i++) {
            testUploadSmallFileByPath(i);
            QCloudLogger.i("QCloudTest", "!!!Start to sleep for %d ms", sleep);
            TestUtils.sleep(sleep);
            if (i < count) {
                QCloudLogger.i("QCloudTest", "------ Resume upload %d--------", i + 1);
            } else {
                QCloudLogger.i("QCloudTest", "------ Finish Test --------");
            }
        }
    }

    private void logMsTime(String desc, double ms) {

        QCloudLogger.i("QCloudTest", "%s: %.2fms", desc, 1000 * ms);
    }

    public void testUploadSmallFileByPath(int number) {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH + number, TestUtils.smallFilePath());

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        uploadTask.setCosXmlService(ServiceFactory.INSTANCE.newDefaultService());
        uploadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {

                //logMsTime("calculateMD5STookTime", httpTaskMetrics.calculateMD5STookTime());
                //logMsTime("signRequestTookTime", httpTaskMetrics.signRequestTookTime());
                logMsTime("dnsLookupTookTime", httpTaskMetrics.dnsLookupTookTime());
                logMsTime("connectTookTime", httpTaskMetrics.connectTookTime());
                //logMsTime("secureConnectTookTime", httpTaskMetrics.secureConnectTookTime());
                //logMsTime("writeRequestHeaderTookTime", httpTaskMetrics.writeRequestHeaderTookTime());
                //logMsTime("writeRequestBodyTookTime", httpTaskMetrics.writeRequestBodyTookTime());
                //logMsTime("readResponseHeaderTookTime", httpTaskMetrics.readResponseHeaderTookTime());
                //logMsTime("readResponseBodyTookTime", httpTaskMetrics.readResponseBodyTookTime());
                logMsTime("fullTaskTookTime", httpTaskMetrics.fullTaskTookTime());
            }
        });
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                result.printResult();
                TestUtils.parseBadResponseBody(result);
                testLocker.release();
                uploadTask.clearResultAndException();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test public void testUploadSmallFileByPath() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET + "xxx",
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, TestUtils.bigFilePath());

        File file = new File(putObjectRequest.getSrcPath());
        QCloudLogger.i("QCloudTest", "upload file size is " + file.length());
        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        uploadTask.setCosXmlService(ServiceFactory.INSTANCE.newDefaultService());
        uploadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {

                //logMsTime("calculateMD5STookTime", httpTaskMetrics.calculateMD5STookTime());
                //logMsTime("signRequestTookTime", httpTaskMetrics.signRequestTookTime());
                logMsTime("dnsLookupTookTime", httpTaskMetrics.dnsLookupTookTime());
                logMsTime("connectTookTime", httpTaskMetrics.connectTookTime());
                //logMsTime("secureConnectTookTime", httpTaskMetrics.secureConnectTookTime());
                //logMsTime("writeRequestHeaderTookTime", httpTaskMetrics.writeRequestHeaderTookTime());
                //logMsTime("writeRequestBodyTookTime", httpTaskMetrics.writeRequestBodyTookTime());
                //logMsTime("readResponseHeaderTookTime", httpTaskMetrics.readResponseHeaderTookTime());
                //logMsTime("readResponseBodyTookTime", httpTaskMetrics.readResponseBodyTookTime());
                logMsTime("fullTaskTookTime", httpTaskMetrics.fullTaskTookTime());
            }
        });
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                result.printResult();
                TestUtils.parseBadResponseBody(result);
                testLocker.release();

            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
    }

    @Test public void testUploadSmallFileByUri() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
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


    @Test public void testUploadBigFileByUri() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                Uri.fromFile(new File(TestUtils.bigFilePath())), null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i("QCloudTest", "state is " + state);
            }
        });
        uploadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {
                QCloudLogger.i(TestConst.UT_TAG, "connect ip is " + httpTaskMetrics.getConnectAddress().getAddress().getHostAddress());
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




    @Test public void testUploadBigFileByPath() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.bigFilePath(), null);

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

    @Test public void testUploadBigFileForceSimpleUploadByPath() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newForceSimpleUploadTransferManager();
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.bigFilePath(), null);

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
    public void testUploadFileConcurrent() throws Exception{

        final int fileCount = 4;
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final TestLocker testLocker = new TestLocker(fileCount);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final StringBuilder errorMessage = new StringBuilder();
        for (int i = 0 ; i < fileCount; i++) {

            String cosPath = UPLOAD_FOLDER+"uploadTask_cancel" + i;
            final String srcPath = TestUtils.localPath("upload" + i);
            TestUtils.createFile(srcPath, 10 * 1024 * 1024);
            final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, null);
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
                    TestUtils.removeLocalFile(srcPath);
                    errorMessage.append(TestUtils.getCosExceptionMessage(exception, serviceException) + "/r/n");
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
    public void testCancelTask()throws Exception{

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final TestLocker testLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_pause" + System.currentTimeMillis();
        final String srcPath = TestUtils.bigFilePath();
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, null);
        TestUtils.sleep(2000);
        cosxmlUploadTask.cancel();
        TestUtils.sleep(200);
        Assert.assertTrue(cosxmlUploadTask.getTaskState() == TransferState.CANCELED);
    }



    @Test
    public void testPauseTask() throws Exception{

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final TestLocker testLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_pause" + System.currentTimeMillis();
        final String srcPath = TestUtils.bigFilePath();
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, null);
        TestUtils.sleep(2000);
        cosxmlUploadTask.pauseSafely();
        TestUtils.sleep(200);
        Assert.assertTrue(cosxmlUploadTask.getTaskState() == TransferState.PAUSED);
    }


    /**
     *
     *
     *
     * @throws Exception
     */
    @Test
    public void testResumeTask() throws Exception{

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        final TestLocker uploadLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_resume" + System.currentTimeMillis();
        final String srcPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + TestUtils.bigFilePath();
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, null);

        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i(TestConst.UT_TAG, state.toString());
            }
        });

        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                uploadLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                uploadLocker.release();

            }
        });

        // 上传 5s 后暂停
        Thread.sleep(10000);
        if (cosxmlUploadTask.getTaskState() == TransferState.COMPLETED) {
            Assert.assertTrue(true);
            return;
        } else if (cosxmlUploadTask.getTaskState() == TransferState.IN_PROGRESS) {

            cosxmlUploadTask.pauseSafely();
            Thread.sleep(2000);
            cosxmlUploadTask.resume();
        } else {
            Assert.fail();
            return;
        }

        uploadLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlUploadTask);
    }

    /**
     * 上传进度 100% 后点击暂停，并等待 5s 后恢复上传
     */
    @Test public void testPauseAndResumeTask() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.bigFilePath(), null);
        final TestLocker testLocker = new TestLocker();
        final TestLocker waitPauseLocker = new TestLocker();

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

        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i(TestConst.UT_TAG, "state " + state);
            }
        });


        final AtomicBoolean pauseSuccess = new AtomicBoolean(false);
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

                Log.i(TestConst.UT_TAG, "progress  = " + 1.0 * complete / target);

                if (complete == target && !pauseSuccess.get()) {
                    if (uploadTask.pauseSafely()) {
                        Log.i(TestConst.UT_TAG, "pause success!!");
                        pauseSuccess.set(true);
                    } else {
                        Log.i(TestConst.UT_TAG, "pause failed!!");
                    }
                    waitPauseLocker.release();
                }
            }
        });

        waitPauseLocker.lock(); // 等待暂停

        if (pauseSuccess.get()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.i(TestConst.UT_TAG, "start resume");
                    uploadTask.resume();
                }
            }, 5000);
        }

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

}
