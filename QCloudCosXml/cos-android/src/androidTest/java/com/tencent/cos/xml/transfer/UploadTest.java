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

import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_BIG_OBJECT_SIZE;
import static com.tencent.cos.xml.core.TestUtils.getContext;

import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;
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
import com.tencent.cos.xml.listener.CosXmlResultSimpleListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.tag.InitiateMultipartUpload;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.util.QCloudStringUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


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
        // PutObjectRequest putObjectRequest = new PutObjectRequest();

        final COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, TestUtils.smallFilePath(), null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.startTimeoutTimer(1000);
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail("upload success");
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                // Assert.assertTrue(TestUtils.getCosExceptionMessage(clientException, serviceException).contains("Task waiting timeout"));
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
        TestUtils.sleep(20000);
    }

    /**
     * 测试预连接后上传耗时
     */
    @Test public void testPreBuildConnection() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        final TestLocker testLocker = new TestLocker();
        cosXmlSimpleService.preBuildConnectionAsync(TestConst.PERSIST_BUCKET, new CosXmlResultSimpleListener() {
            @Override
            public void onSuccess() {
                final long sleep = 10000;
                final int count = 3;
                for (int i = 0; i <= count; i++) {
                    testUploadSmallFileByPath(i);
                    QCloudLogger.i("QCloudTest", "!!!Start to sleep for %d ms", sleep);
                    if(i==2){
                        TestUtils.sleep(10*60*1000);
                    } else {
                        TestUtils.sleep(sleep);
                    }
                    if (i < count) {
                        QCloudLogger.i("QCloudTest", "------ Resume upload %d--------", i + 1);
                    } else {
                        QCloudLogger.i("QCloudTest", "------ Finish Test --------");
                    }
                }
                testLocker.release();
            }
            
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    private void logMsTime(String desc, double ms) {
        QCloudLogger.i("QCloudTest", "%s: %.2fms", desc, 1000 * ms);
    }


    @Test public void testMultiUpload() {
        int count = 2;
        long sleep = 1000;
        for (int i = 0; i <= count; i++) {
            testUploadSmallFileByPath(i);
            QCloudLogger.i("QCloudTest", "!!!Start to sleep for %d ms", sleep);
            if (i < count) {
                QCloudLogger.i("QCloudTest", "------ Resume upload %d--------", i + 1);
            } else {
                QCloudLogger.i("QCloudTest", "------ Finish Test --------");
            }
        }
    }

    public void testUploadSmallFileByPath(int number) {
        // String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/youxue.jpg";
        String path = TestUtils.bigFilePath();
        QCloudLogger.i("QCloudTest", "upload path is " + path);

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH + number,
                path);

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        uploadTask.setCosXmlService(ServiceFactory.INSTANCE.newDefaultService());
        uploadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {

                logMsTime("calculateMD5STookTime", httpTaskMetrics.calculateMD5STookTime());
                logMsTime("signRequestTookTime", httpTaskMetrics.signRequestTookTime());
                logMsTime("dnsLookupTookTime", httpTaskMetrics.dnsLookupTookTime());
                logMsTime("connectTookTime", httpTaskMetrics.connectTookTime());
                logMsTime("secureConnectTookTime", httpTaskMetrics.secureConnectTookTime());
                logMsTime("writeRequestHeaderTookTime", httpTaskMetrics.writeRequestHeaderTookTime());
                logMsTime("writeRequestBodyTookTime", httpTaskMetrics.writeRequestBodyTookTime());
                logMsTime("readResponseHeaderTookTime", httpTaskMetrics.readResponseHeaderTookTime());
                logMsTime("readResponseBodyTookTime", httpTaskMetrics.readResponseBodyTookTime());
                //QCloudLogger.i("QCloudTest", httpTaskMetrics.getRemoteAddress().toArray().toString());
                QCloudLogger.i("QCloudTest", "host is " + httpTaskMetrics.getConnectAddress().getHostName());
                QCloudLogger.i("QCloudTest", "ip is " + httpTaskMetrics.getConnectAddress().getHostAddress());
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

        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i(TestConst.UT_TAG, "transfer state is " + state);
            }
        });
        uploadTask.setInitMultipleUploadListener(new InitMultipleUploadListener() {
            @Override
            public void onSuccess(InitiateMultipartUpload initiateMultipartUpload) {
                //用于下次续传上传的uploadId
                String uploadId = initiateMultipartUpload.uploadId;
            }
        });
        
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test public void testUpload() {

        QCloudLogger.i("QCloud", "ext is " + QCloudStringUtils.getExtension("1111"));
        QCloudLogger.i("QCloud", "ext is " + QCloudStringUtils.getExtension("1111."));
        QCloudLogger.i("QCloud", "ext is " + QCloudStringUtils.getExtension("1111.mp3"));
    }

    @Test public void testUploadSmallFileByPath() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        // String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/deviceId.mp3";
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.filePath("N好.", 1024));

//        try {
//            // 设置 content-type
//            // putObjectRequest.setRequestHeaders("Content-Type", "image/png", false);
//            // PicOperationRule rule = new PicOperationRule("imageMogr2/iradius/200", "process/");
//            // LinkedList<PicOperationRule> rules = new LinkedList<>();
//            // rules.add(rule);
//            // PicOperations picOperations = new PicOperations(true, rules);
//            // putObjectRequest.setPicOperations(picOperations);
//        } catch (CosXmlClientException clientException) {
//            clientException.printStackTrace();
//        }

        try {
            putObjectRequest.setRequestHeaders("test", "value", false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }

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
                QCloudLogger.i("QCloudTest", result.headers.toString());
                QCloudLogger.i("QCloudTest", result.printResult());;
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
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH, Uri.fromFile(new File(TestUtils.smallFilePath())));

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
//        uploadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
//            @Override
//            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {
//                QCloudLogger.i(TestConst.UT_TAG, "connect ip is " + httpTaskMetrics.getConnectAddress());
//            }
//        });
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError("失败回调 " + TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                String uploadId = uploadTask.getUploadId();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }


    @Test public void testUploadBigFileByUri() {

//        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManagerBySessionCredentials();

        File file = new File(TestUtils.bigFilePath());
        Uri uri = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getPackageName()+".fileProvider", file);

        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH + 12,
                uri,
                null);

        final TestLocker testLocker = new TestLocker();
        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {

            }
        });
        uploadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {
                QCloudLogger.i(TestConst.UT_TAG, "connect ip is " + httpTaskMetrics.getConnectAddress());
            }
        });
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                // QCloudLogger.i(TestConst.UT_TAG, "transfer state is " + state);
                QCloudLogger.i(TestConst.UT_TAG, "upload id is " + uploadTask.getUploadId());
            }
        });
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i("QCloudTest", "upload success!!");
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
        String localPath = TestUtils.localPath("1642166999131.m4a");
        try {
            TestUtils.createFile(localPath, PERSIST_BUCKET_BIG_OBJECT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                localPath, null);
        final TestLocker testLocker = new TestLocker();

        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TestUtils.printError("upload success");
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
    public void testPauseTask() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final TestLocker testLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_pause" + System.currentTimeMillis();
        final String srcPath = TestUtils.bigFilePath();
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, null);
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.i("QCloudTest", "upload progress: " + complete + "/" + target);
            }
        });
        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i("QCloudTest", "upload state: " + state);
            }
        });
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.e("QCloudTest", "upload success");
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Log.e("QCloudTest", "upload failed: " + (clientException != null ? clientException.getMessage() : "") + " / "
                        + (serviceException != null ? serviceException.getErrorMessage() : ""));
            }
        });
        TestUtils.sleep(2000);
        cosxmlUploadTask.pauseSafely();
        TestUtils.sleep(1000);
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
        final String srcPath = TestUtils.bigFilePath();
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
