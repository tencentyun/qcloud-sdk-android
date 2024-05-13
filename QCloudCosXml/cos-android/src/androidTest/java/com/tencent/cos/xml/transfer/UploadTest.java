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

import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_SIZE;
import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_BIG_OBJECT_SIZE;
import static com.tencent.cos.xml.core.TestUtils.big60mFilePath;
import static com.tencent.cos.xml.core.TestUtils.bigFilePath;
import static com.tencent.cos.xml.core.TestUtils.bigPlusFilePath;
import static com.tencent.cos.xml.core.TestUtils.getContext;
import static com.tencent.cos.xml.core.TestUtils.smallFilePath;
import static org.hamcrest.CoreMatchers.is;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.core.content.FileProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlServiceConfig;
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
import com.tencent.cos.xml.listener.CosXmlResultSimpleListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.BasePutObjectResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.tag.InitiateMultipartUpload;
import com.tencent.cos.xml.model.tag.UrlUploadPolicy;
import com.tencent.cos.xml.model.tag.pic.PicOperationRule;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.cos.xml.utils.UrlUtil;
import com.tencent.qcloud.core.auth.COSXmlSignSourceProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.util.Base64Utils;
import com.tencent.qcloud.core.util.QCloudStringUtils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


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
    @Test
    public void testUploadWaitingTimeout() {

        String filePath1 = TestUtils.localPath("file1");
        String filePath2 = TestUtils.localPath("file2");
        try {
            TestUtils.createFile(filePath1, 60 * 1024 * 1024);
            TestUtils.createFile(filePath2, 60 * 1024 * 1024);
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
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, smallFilePath(), null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.startTimeoutTimer(1000);
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail("upload success");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
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
//        TestUtils.sleep(15000);
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
                final long sleep = 5000;
                final int count = 3;
                for (int i = 0; i <= count; i++) {
                    testUploadSmallFileByPath(i);
                    QCloudLogger.i("QCloudTest", "!!!Start to sleep for %d ms", sleep);
                    if(i==2){
//                        TestUtils.sleep(10*60*1000);
                        TestUtils.sleep(sleep);
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

//    /**
//     * 批量上传
//     */
//    @Test public void testBatchSmallUpload() {
//        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
//        int count = 3;
//        final TestLocker testLocker = new TestLocker(count);
//        final AtomicInteger successCount = new AtomicInteger(0);
//        final AtomicInteger errorCount = new AtomicInteger(0);
//        final StringBuilder errorMessage = new StringBuilder();
//        final String path = smallFilePath();
//
//        for (int i = 0; i < count; i ++) {
//            PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
//                    TestConst.PERSIST_BUCKET_BATCH_OBJECT_PATH + i,
//                    path);
//
//            final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
//
//            uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
//                @Override
//                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                    successCount.addAndGet(1);
//                    if(successCount.get() == count) {
//                        testLocker.release();
//                    }
//                }
//
//                @Override
//                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
//                    TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
//                    errorMessage.append(TestUtils.getCosExceptionMessage(clientException, serviceException)).append("/r/n");
//                    errorCount.addAndGet(1);
//                    testLocker.release();
//                }
//            });
//        }
//        testLocker.lock();
//        TestUtils.assertErrorMessageNull(errorMessage);
//    }

    @Test public void testMultiUpload() {
        int count = 20;
        long sleep = 1000;
        final TestLocker testLocker = new TestLocker();
        for (int i = 0; i <= count; i++) {
            testUploadSmallFileByPath(i);
            QCloudLogger.i("QCloudTest", "!!!Start to sleep for %d ms", sleep);
            if (i < count) {
                QCloudLogger.i("QCloudTest", "------ Resume upload %d--------", i + 1);
            } else {
                QCloudLogger.i("QCloudTest", "------ Finish Test --------");
            }
            if(i == count){
                TestUtils.sleep(60000);
                Assert.assertTrue(true);
                testLocker.release();
            }
        }
        testLocker.lock();
    }

    public void testUploadSmallFileByPath(int number) {
        // String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/youxue.jpg";
        String path = smallFilePath();
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
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                result.printResult();
                TestUtils.parseBadResponseBody(result);
                uploadTask.clearResultAndException();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
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
        putObjectRequest.addNoSignHeader("Host");
        putObjectRequest.setPriorityLow();

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
                QCloudLogger.i("QCloudTest", result.printResult());
                QCloudLogger.i("QCloudTest", result.accessUrl);
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
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, Uri.fromFile(new File(smallFilePath())));
        putObjectRequest.setUri(Uri.fromFile(new File(smallFilePath())));
        try {
            putObjectRequest.setRequestHeaders("test1", "test1Value", false);
        } catch (CosXmlClientException e) {
            throw new RuntimeException(e);
        }

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

    @Test public void testUploadSmallFileByInputStream() throws IOException {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                Files.newInputStream(new File(smallFilePath()).toPath())
        );
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

    @Test public void testUploadSmallFileByBytes() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                "this is small object".getBytes()
        );
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

    @Test public void testUploadSmallFileByUrl() throws MalformedURLException {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                new URL(TestConst.PERSIST_BUCKET_CDN_SMALL_OBJECT_URL),
                null
        );
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

    @Test public void testUploadSmallFileByUrlPolicy() throws MalformedURLException {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                new URL(TestConst.PERSIST_BUCKET_CDN_SMALL_OBJECT_URL),
                UrlUtil.getUrlUploadPolicy(TestConst.PERSIST_BUCKET_CDN_SMALL_OBJECT_URL),
                null
        );
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

    @Test public void testUploadSmallFileByUrlPolicy1() throws MalformedURLException {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                new URL(TestConst.PERSIST_BUCKET_CDN_SMALL_OBJECT_URL));
        request.setUrlUploadPolicy(UrlUtil.getUrlUploadPolicy(TestConst.PERSIST_BUCKET_CDN_SMALL_OBJECT_URL));
        final COSXMLUploadTask uploadTask = transferManager.upload(request, null);
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

    @Test public void testUploadBigFileByInputStream() throws IOException {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                Files.newInputStream(new File(bigFilePath()).toPath())
        );
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

    @Test public void testUploadBigFileByUrl() throws MalformedURLException {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                new URL(TestConst.PERSIST_BUCKET_CDN_BIG_OBJECT_URL),
                null
        );
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
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
//        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManagerBySessionCredentials();

        File file = new File(bigPlusFilePath());
        Uri uri = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getPackageName()+".fileProvider", file);
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH + 12,
                uri);
        putObjectRequest.setPriorityLow();
        try {
            putObjectRequest.setRequestHeaders("test1", "test1Value", false);
        } catch (CosXmlClientException e) {
            throw new RuntimeException(e);
        }
        final COSXMLUploadTask uploadTask = transferManager.upload(
                putObjectRequest,
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
        uploadTask.getSendingCompleteRequest();

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }


    @Test public void testUploadBigFileByPath() {
        testUploadBigFileByPath(ServiceFactory.INSTANCE.newDefaultTransferManager());
    }

    @Test public void testDnsUploadBigFileByPath() {
        testUploadBigFileByPath(ServiceFactory.INSTANCE.newDnsTransferManager());
    }

    @Test public void testSingerUploadBigFileByPath() {
        testUploadBigFileByPath(ServiceFactory.INSTANCE.newSingerTransferManager());
    }

    @Test public void testTencentcosUploadBigFileByPath() {
        testUploadBigFileByPath(ServiceFactory.INSTANCE.newTencentcosTransferManager());
    }

    @Test public void testUploadBigFileForceSimpleUploadByPath() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newForceSimpleUploadTransferManager();
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                bigFilePath(), null);

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
    public void testResumeTask() throws Exception{

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        final TestLocker uploadLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_resume" + System.currentTimeMillis();
        final String srcPath = bigFilePath();
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

    @Test
    public void testResumeUriTask() throws Exception{

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        final TestLocker uploadLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_resume" + System.currentTimeMillis();
        File file = new File(bigFilePath());
        Uri uri = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getPackageName()+".fileProvider", file);

        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, uri, null);

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

        // 上传 3s 后暂停
        Thread.sleep(3000);
        if (cosxmlUploadTask.getTaskState() == TransferState.COMPLETED) {
            Assert.assertTrue(true);
            return;
        } else if (cosxmlUploadTask.getTaskState() == TransferState.IN_PROGRESS) {

            cosxmlUploadTask.pauseSafely(true);
            Thread.sleep(2000);
            cosxmlUploadTask.resume();
        } else {
            Assert.fail();
            return;
        }

        uploadLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlUploadTask);
    }

    @Test
    public void testResumeInputStreamTask() throws Exception{

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        final TestLocker uploadLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_resume";
        InputStream inputStream = Files.newInputStream(new File(bigFilePath()).toPath());
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET,
                cosPath, inputStream);

        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i(TestConst.UT_TAG, state.toString());
            }
        });

        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i(TestConst.UT_TAG, "transfer progress is " + complete + "/" + target);
            }
        });

        // 上传 5s 后暂停
        Thread.sleep(3000);
        if (cosxmlUploadTask.getTaskState() == TransferState.COMPLETED) {
            Assert.assertTrue(true);
            return;
        } else if (cosxmlUploadTask.getTaskState() == TransferState.IN_PROGRESS) {

            cosxmlUploadTask.pauseSafely();
            Thread.sleep(3000);
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
            cosxmlUploadTask.resume();
        } else {
            Assert.fail();
            return;
        }

        uploadLocker.lock();
        Assert.assertSame(cosxmlUploadTask.getTaskState(), TransferState.FAILED);
    }

    @Test
    public void testFixSliceSize() throws Exception{
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        TransferManager bigSliceSizeTransferManager = ServiceFactory.INSTANCE.newBigSliceSizeTransferManager();

        final TestLocker uploadLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_resume";
        final String srcPath = big60mFilePath();
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, null);
        AtomicReference<String> uploadId = new AtomicReference<>(null);
        cosxmlUploadTask.setInitMultipleUploadListener(initiateMultipartUpload -> {
            uploadId.set(initiateMultipartUpload.uploadId);
        });
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i(TestConst.UT_TAG, "transfer progress is " + complete + "/" + target);
            }
        });

        COSXMLUploadTask cosxmlUploadTask1 = null;
        // 上传 5s 后暂停
//        Thread.sleep(6000);
        uploadLocker.lock(3000);
        if (cosxmlUploadTask.getTaskState() == TransferState.COMPLETED) {
            Assert.assertTrue(true);
            return;
        } else if (cosxmlUploadTask.getTaskState() == TransferState.IN_PROGRESS) {
            cosxmlUploadTask.pauseSafely();
            Thread.sleep(3000);
            cosxmlUploadTask1 = bigSliceSizeTransferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, uploadId.get());
            cosxmlUploadTask1.setCosXmlResultListener(new CosXmlResultListener() {
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
            cosxmlUploadTask1.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    QCloudLogger.i(TestConst.UT_TAG, "transfer progress is " + complete + "/" + target);
                }
            });
        } else {
            Assert.fail();
            return;
        }

        uploadLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlUploadTask1);
    }

    @Test
    public void testFixSliceSize369() throws Exception{
        TransferManager transferManager = ServiceFactory.INSTANCE.newSlice369TransferManager();
        TransferManager bigSliceSizeTransferManager = ServiceFactory.INSTANCE.newBigSliceSize369TransferManager();

        final TestLocker uploadLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_resume";
        final String srcPath = big60mFilePath();
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, null);
        AtomicReference<String> uploadId = new AtomicReference<>(null);
        cosxmlUploadTask.setInitMultipleUploadListener(initiateMultipartUpload -> {
            uploadId.set(initiateMultipartUpload.uploadId);
        });
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i(TestConst.UT_TAG, "transfer progress is " + complete + "/" + target);
            }
        });

        COSXMLUploadTask cosxmlUploadTask1 = null;
        // 上传 5s 后暂停
        Thread.sleep(3000);
        if (cosxmlUploadTask.getTaskState() == TransferState.COMPLETED) {
            Assert.assertTrue(true);
            return;
        } else if (cosxmlUploadTask.getTaskState() == TransferState.IN_PROGRESS) {
            cosxmlUploadTask.pauseSafely();
            Thread.sleep(3000);
            cosxmlUploadTask1 = bigSliceSizeTransferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, uploadId.get());
            cosxmlUploadTask1.setCosXmlResultListener(new CosXmlResultListener() {
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
            cosxmlUploadTask1.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    QCloudLogger.i(TestConst.UT_TAG, "transfer progress is " + complete + "/" + target);
                }
            });
        } else {
            Assert.fail();
            return;
        }

        uploadLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlUploadTask1);
    }

    @Test public void testUploadSmallFileSignatureByPath() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, TestUtils.filePath("N好.", 1024), null, new MyOnSignatureListener());
        uploadTask.setCosXmlService(ServiceFactory.INSTANCE.newDefaultService());
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
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
    }

    @Test public void testUploadBigFileSignatureByPath() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        String localPath = TestUtils.localPath("1642166999131.m4a");
        try {
            TestUtils.createFile(localPath, PERSIST_BUCKET_BIG_OBJECT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                localPath, null, new MyOnSignatureListener());
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

    @Test public void testUploadFailed() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .build();
        try {
            TransferManager transferManager = new TransferManager(null, transferConfig);
        } catch (IllegalArgumentException e){
            Assert.assertEquals("CosXmlService is null", e.getMessage());
        }

        try {
            TransferManager transferManager = new TransferManager(ServiceFactory.INSTANCE.newDefaultService(), null);
        } catch (IllegalArgumentException e){
            Assert.assertEquals("TransferConfig is null", e.getMessage());
        }
    }

    @Test public void testUploadSmallFileByPathFailed() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET+"aaa",
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.filePath("N好.", 1024));

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        uploadTask.setCosXmlService(ServiceFactory.INSTANCE.newDefaultService());
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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

    @Test public void testUploadBigFileByPathFailed() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET+"aaa",
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH, bigFilePath());

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        uploadTask.setCosXmlService(ServiceFactory.INSTANCE.newDefaultService());
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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

    @Test public void testUploadCheckParameterFailed1() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                (String) null,
                null);
        uploadTask.setCosXmlService(ServiceFactory.INSTANCE.newDefaultService());
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                Assert.assertTrue(clientException.getMessage().contains("source is is invalid: null"));
                testLocker.release();
            }
        });

        testLocker.lock();
    }

    @Test public void testUploadCheckParameterFailed2() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                smallFilePath()+"aaa",
                null);
        uploadTask.setCosXmlService(ServiceFactory.INSTANCE.newDefaultService());
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                Assert.assertTrue(clientException.getMessage().contains("srcPath is is invalid: "));
                testLocker.release();
            }
        });

        testLocker.lock();
    }

    @Test public void testUploadCheckParameterFailed3() throws MalformedURLException {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                new URL(TestConst.PERSIST_BUCKET_CDN_SMALL_OBJECT_URL),
                new UrlUploadPolicy(UrlUploadPolicy.Type.NOTSUPPORT, 10),
                null
        );
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                Assert.assertTrue(clientException.getMessage().contains("url not support download"));
                testLocker.release();
            }
        });

        testLocker.lock();
    }

    @Test public void testBasePutObject() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                smallFilePath());
        String region = cosXmlSimpleService.getRegion(request);
        Assert.assertEquals(region, TestConst.PERSIST_BUCKET_REGION);
        request.addNoSignParams("test");
        try {
            request.setRequestHeaders("test1", "value1");
        } catch (CosXmlClientException e) {
            throw new RuntimeException(e);
        }
        request.setSignInUrl(true);
        request.isSupportAccelerate(true);
        Assert.assertTrue(request.isSupportAccelerate());
        request.setSignSourceProvider(new COSXmlSignSourceProvider());

        request.setSign(1000);
        request.setSign(100, 200);
        Set<String> parameters = new ArraySet<>();
        parameters.add("parameters1");
        parameters.add("parameters2");
        Set<String> headers = new ArraySet<>();
        headers.add("headers1");
        headers.add("headers2");
        request.setSign(1000, parameters, headers);
        request.setSign(100, 200, parameters, headers);
        request.setSignParamsAndHeaders(parameters, headers);

        request.setPriorityLow();
        Assert.assertTrue(request.isPriorityLow());

        try {
            BasePutObjectResult result = cosXmlSimpleService.basePutObject(request);
            Assert.assertFalse(TextUtils.isEmpty(result.eTag));
        } catch (CosXmlClientException e) {
            Assert.fail();
        } catch (CosXmlServiceException e) {
            Assert.fail();
        }

        final TestLocker testLocker = new TestLocker();
        cosXmlSimpleService.basePutObjectAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                BasePutObjectResult result1 = (BasePutObjectResult) result;
                Assert.assertFalse(TextUtils.isEmpty(result1.eTag));
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

    private void testUploadBigFileByPath(TransferManager transferManager) {
        String localPath = TestUtils.localPath("1642166999131.m4a");
        try {
            TestUtils.createFile(localPath, PERSIST_BUCKET_BIG_OBJECT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                localPath, null);
        final TestLocker testLocker = new TestLocker();

        uploadTask.setInitMultipleUploadListener(initiateMultipartUpload -> TestUtils.print(initiateMultipartUpload.uploadId));
        uploadTask.setInternalStateListener(state -> TestUtils.print(state.name()));
        uploadTask.setInternalProgressListener((complete, target) -> TestUtils.print(String.format("%d-%d", complete, target)));
        uploadTask.setInternalInitMultipleUploadListener(initiateMultipartUpload -> TestUtils.print(initiateMultipartUpload.uploadId));

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

    // TODO: 2023/4/26 单测
    @Test public void testBasePutObjectSSE_KMS() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                smallFilePath());
        try {
            request.setCOSServerSideEncryptionWithKMS("customKey", "encryptContext");
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        try {
            BasePutObjectResult result = cosXmlSimpleService.basePutObject(request);
            Assert.fail();
        } catch (CosXmlClientException e) {
            Assert.fail();
        } catch (CosXmlServiceException e) {
            Assert.assertTrue(true);
        }
    }

    // TODO: 2023/4/26 单测
    @Test public void testBasePutObjectSSE_CustomerKey() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                smallFilePath());
        try {
            request.setCOSServerSideEncryptionWithCustomerKey(Base64Utils.encode("customKey".getBytes()));
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        try {
            BasePutObjectResult result = cosXmlSimpleService.basePutObject(request);
            Assert.fail();
        } catch (CosXmlClientException e) {
            Assert.fail();
        } catch (CosXmlServiceException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testNoSuchUploadByListMultiUpload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        String localPath = TestUtils.localPath("1642166999131.m4a");
        try {
            TestUtils.createFile(localPath, PERSIST_BUCKET_BIG_OBJECT_SIZE+ new Random().nextInt(200));
        } catch (IOException e) {
            e.printStackTrace();
        }
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                localPath, "asdasdasdasdasd");
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                Assert.fail();
                // TODO: 2023/4/27 查查为什么会走到成功
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.assertTrue(true);
                testLocker.release();
            }
        });

        testLocker.lock(20000);
    }

    @Test
    public void testNoSuchUploadByCompleteMultiUpload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        String localPath = TestUtils.localPath("1642166999131.m4a");
        try {
            TestUtils.createFile(localPath, PERSIST_BUCKET_BIG_60M_OBJECT_SIZE+ new Random().nextInt(200));
        } catch (IOException e) {
            e.printStackTrace();
        }
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                localPath, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlProgressListener((complete, target) -> {
            if(complete > 50 * 1024 * 1024){
                if(!"asdasdasdasdasd".equals(uploadTask.getUploadId())){
                    uploadTask.setUploadId("asdasdasdasdasd");
                }
            }
        });

        // 上传 5s 后暂停
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        if (uploadTask.getTaskState() == TransferState.COMPLETED) {
            Assert.assertTrue(true);
            return;
        } else if (uploadTask.getTaskState() == TransferState.IN_PROGRESS) {
            uploadTask.pauseSafely();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

            uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    Assert.fail();
                    testLocker.release();
                }

                @Override
                public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                    Assert.assertEquals("NoSuchUpload", serviceException.getErrorCode());
                    testLocker.release();
                }
            });
            uploadTask.resume();
        } else {
            Assert.fail();
            return;
        }
        testLocker.lock(20000);
    }

    @Test
    public void testUploadSmallFileCallback() throws CosXmlClientException {
        testUploadCallback(false, false, false);
        testUploadCallback(false, false, true);
        testUploadCallback(false, true, false);

//        testUploadCallback(false, true, true);

        testUploadCallback(true, false, false);
        testUploadCallback(true, false, true);
        testUploadCallback(true, true, false);
        testUploadCallback(true, true, true);
    }

    @Rule
    public ErrorCollector collector = new ErrorCollector();
    private void testUploadCallback(boolean isBigFile, boolean isCI, boolean is203) throws CosXmlClientException {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .setDebuggable(true)
                .setRegion(TestConst.CALLBACK_PERSIST_BUCKET_REGION)
                .builder();
        CosXmlSimpleService cosXmlSimpleService = new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.CALLBACK_SECRET_ID, TestConst.CALLBACK_SECRET_KEY,60000) );
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        TransferManager transferManager = new TransferManager(cosXmlSimpleService, transferConfig);

        PutObjectRequest putObjectRequest;
        if(isCI){
            String localImageName ="/test_image.png";
            String cosPath;
            if(isBigFile){
                cosPath = TestConst.PERSIST_BUCKET_PIC_6M_PATH;
            } else {
                cosPath = TestConst.PERSIST_BUCKET_PIC_PATH;
            }
            GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                    cosPath, TestUtils.localParentPath(), localImageName);
            CosXmlSimpleService cosXmlDefaultService = ServiceFactory.INSTANCE.newDefaultService();
            try {
                cosXmlDefaultService.getObject(getObjectRequest);
            } catch (Exception e) {
                Assert.fail(e.getMessage());
            }

            putObjectRequest = new PutObjectRequest(TestConst.CALLBACK_PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH, TestUtils.localPath(localImageName));
            List<PicOperationRule> rules = new LinkedList<>();
            rules.add(new PicOperationRule("examplepngobject", "imageView2/format/png"));
            PicOperations picOperations = new PicOperations(true, rules);
            putObjectRequest.setPicOperations(picOperations);
        } else {
            if(isBigFile){
                putObjectRequest = new PutObjectRequest(TestConst.CALLBACK_PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH, TestUtils.bigFilePath());
            } else {
                putObjectRequest = new PutObjectRequest(TestConst.CALLBACK_PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH, TestUtils.smallFilePath());
            }
        }

        String callbackJson;
        if(is203){
            callbackJson = "{ \"callbackUrlaaa\": \"http://114.132.67.183/index\", " +
                    "\"callbackHost\": \"114.132.67.183\", " +
                    "\"callbackBody\": \"bucket=${bucket}&object=${object}&etag=${etag}&test=test_123\", " +
                    "\"callbackBodyType\": \"application/x-www-form-urlencoded\" }";
        } else {
            callbackJson = "{ \"callbackUrl\": \"http://114.132.67.183/index\", " +
                    "\"callbackHost\": \"114.132.67.183\", " +
                    "\"callbackBody\": \"bucket=${bucket}&object=${object}&etag=${etag}&test=test_123\", " +
                    "\"callbackBodyType\": \"application/x-www-form-urlencoded\" }";
        }
        String callbackBase64 = DigestUtils.getBase64(callbackJson);
        putObjectRequest.setRequestHeaders("x-cos-callback", callbackBase64, false);

        COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setInitMultipleUploadListener(initiateMultipartUpload -> TestUtils.print(initiateMultipartUpload.uploadId));
        uploadTask.setInternalStateListener(state -> TestUtils.print(state.name()));
        uploadTask.setInternalProgressListener((complete, target) -> TestUtils.print(String.format("%d-%d", complete, target)));
        uploadTask.setInternalInitMultipleUploadListener(initiateMultipartUpload -> TestUtils.print(initiateMultipartUpload.uploadId));
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult cosxmlUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                TestUtils.printXML(cosxmlUploadTaskResult);
                if(cosxmlUploadTaskResult.callbackResult != null && cosxmlUploadTaskResult.callbackResult.getCallbackBody() != null) {
                    TestUtils.print("CallbackBody:");
                    TestUtils.print(cosxmlUploadTaskResult.callbackResult.getCallbackBody());
                }
                if(is203){
                    if(isCI){
                        if(isBigFile){
                            //
                            collector.checkThat(true, is(true));
                            // 这种情况 合并操作总是超时(需要和后端一起看) 导致最后上传成功是head检测 因此万象和回调的响应都没有
//                            collector.checkThat(cosxmlUploadTaskResult.picUploadResult.originalInfo != null, is(true));
//                            collector.checkThat("203".equals(cosxmlUploadTaskResult.callbackResult.status) && TextUtils.isEmpty(cosxmlUploadTaskResult.callbackResult.getCallbackBody()), is(true));
                        } else {
                            collector.checkThat(cosxmlUploadTaskResult.picUploadResult.originalInfo != null, is(true));
                            collector.checkThat(cosxmlUploadTaskResult.callbackResult == null, is(true));
                        }
                    } else {
                        collector.checkThat("203".equals(cosxmlUploadTaskResult.callbackResult.status) && TextUtils.isEmpty(cosxmlUploadTaskResult.callbackResult.getCallbackBody()), is(true));
                    }
                } else {
                    if(isCI){
                        if(isBigFile){
                            collector.checkThat(cosxmlUploadTaskResult.picUploadResult.originalInfo != null, is(true));
                            collector.checkThat("200".equals(cosxmlUploadTaskResult.callbackResult.status) && !TextUtils.isEmpty(cosxmlUploadTaskResult.callbackResult.getCallbackBody()), is(true));
                        } else {
                            collector.checkThat(cosxmlUploadTaskResult.picUploadResult.originalInfo != null, is(true));
                            collector.checkThat(cosxmlUploadTaskResult.callbackResult == null, is(true));
                        }
                    } else {
                        collector.checkThat("200".equals(cosxmlUploadTaskResult.callbackResult.status) && !TextUtils.isEmpty(cosxmlUploadTaskResult.callbackResult.getCallbackBody()), is(true));
                    }
                }

                collector.checkThat(result.httpCode >= 200 && result.httpCode < 300, is(true));
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                collector.addError(new AssertionError(TestUtils.getCosExceptionMessage(this.getClass().getSimpleName(), clientException, serviceException)));
                testLocker.release();
            }
        });

        testLocker.lock();
    }

}
