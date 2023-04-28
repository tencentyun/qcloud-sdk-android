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
import static com.tencent.cos.xml.crypto.Headers.COS_HASH_CRC64_ECMA;

import android.net.Uri;

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
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.tag.pic.PicOperationRule;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.LinkedList;


@RunWith(AndroidJUnit4.class)
public class COSUploadTaskTest {

    // newQuicTransferService
//    @Test public void testQuicUpload() {
//        QuicConfig quicConfig = new QuicConfig();
//        quicConfig.setCustomProtocol(true);
//        quicConfig.setRaceType(QuicConfig.RACE_TYPE_ONLY_QUIC);
//        quicConfig.setTotalTimeoutSec(1000);
//        QuicProxy.setTnetConfig(quicConfig);
//        testUploadFile(ServiceFactory.INSTANCE.newQuicTransferService(), "mobile-ut-bj-1253960454",
//                TestUtils.localPath("shanshui.jpg"), TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
//    }

    // 测试分块上传
    @Test public void testMultipartUpload() {
        testUploadFile(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.bigFilePath(), TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
    }

    // 测试简单上传
    @Test public void testSimpleUpload() {
        testUploadFile(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.smallFilePath(), TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    // 测试暂停和恢复
    @Test public void testPauseAndResume() {
        testPauseAndResume(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.bigFilePath(), TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
    }

    // 测试加密分块上传
    @Test public void testCesMultipartUpload() {
        testUploadFile(ServiceFactory.INSTANCE.newCesTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.bigFilePath(), TestConst.PERSIST_BUCKET_CSE_BIG_OBJECT_PATH);
    }

    // 测试加密简单上传
    @Test public void testCesSimpleUpload() {
        testUploadFile(ServiceFactory.INSTANCE.newCesTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.smallFilePath(), TestConst.PERSIST_BUCKET_CSE_SMALL_OBJECT_PATH);
    }


    @Test public void testCesPauseAndResume() {
        testPauseAndResume(ServiceFactory.INSTANCE.newCesTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.bigFilePath(), TestConst.PERSIST_BUCKET_CSE_BIG_OBJECT_PATH);
    }

    @Test public void testMultipartUploadByUri() {
        testUploadFile(ServiceFactory.INSTANCE.newDefaultTransferService(),
                new PutObjectRequest(
                        TestConst.PERSIST_BUCKET,
                        TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                        Uri.fromFile(new File(TestUtils.bigFilePath())))
        );
    }

    @Test public void testSimpleUploadByUri() {
        testUploadFile(ServiceFactory.INSTANCE.newDefaultTransferService(),
                new PutObjectRequest(
                        TestConst.PERSIST_BUCKET,
                        TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                        Uri.fromFile(new File(smallFilePath())))
        );
    }

    @Test public void testSimpleUploadByBytes() {
        testUploadFile(ServiceFactory.INSTANCE.newDefaultTransferService(),
                new PutObjectRequest(
                        TestConst.PERSIST_BUCKET,
                        TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                        "this is small object".getBytes())
        );
    }

    @Test public void testSimpleUploadByStr() {
        testUploadFile(ServiceFactory.INSTANCE.newDefaultTransferService(),
                new PutObjectRequest(
                        TestConst.PERSIST_BUCKET,
                        TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                        new StringBuilder("this is small object"))
        );
    }

    @Test public void testCancel() {
        TransferService transferService = ServiceFactory.INSTANCE.newDefaultTransferService();
        String bucket = TestConst.PERSIST_BUCKET;
        String filePath = TestUtils.bigFilePath();
        String cosKey = TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH;

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
                cosKey, filePath);

        final COSUploadTask uploadTask = transferService.upload(putObjectRequest);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
                TestUtils.assertCOSXMLTaskSuccess(uploadTask);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                testLocker.release();
                Assert.assertEquals("UserCancelled", clientException.getMessage());
            }
        });
        TestUtils.sleep(1000);
        uploadTask.cancel();
        TestUtils.sleep(200);

        testLocker.lock();
    }

    private PutObjectRequest getFilePathRequest(String bucket, String filePath, String cosKey){
        QCloudLogger.i("QCloudTest", "upload path is " + filePath);
        return new PutObjectRequest(bucket,
                cosKey, filePath);
    }

    private void testUploadFile(TransferService transferService, String bucket, String filePath, String cosKey){
        testUploadFile(transferService, getFilePathRequest(bucket, filePath, cosKey));
    }

    private void testUploadFile(TransferService transferService, PutObjectRequest putObjectRequest) {
        try {
            // 设置 content-type
            putObjectRequest.setRequestHeaders("Content-Type", "image/png", false);
            PicOperationRule rule = new PicOperationRule("imageMogr2/iradius/200", "process/");
            LinkedList<PicOperationRule> rules = new LinkedList<>();
            rules.add(rule);
            PicOperations picOperations = new PicOperations(true, rules);
            //putObjectRequest.setPicOperations(picOperations);
        } catch (CosXmlClientException clientException) {
            clientException.printStackTrace();
        }

        final COSUploadTask uploadTask = transferService.upload(putObjectRequest);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                String crc64 = result.getHeader(COS_HASH_CRC64_ECMA);
                result.printResult();
                TestUtils.parseBadResponseBody(result);
                QCloudLogger.i(TestConst.UT_TAG, "upload success");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                // Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i(TestConst.UT_TAG, "transfer state is " + state);
            }
        });

        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i(TestConst.UT_TAG, "transfer progress is " + complete + "/" + target);
            }
        });

        testLocker.lock();
        TransferTaskMetrics transferTaskMetrics = uploadTask.getTransferTaskMetrics();
        QCloudLogger.i(TestConst.UT_TAG, transferTaskMetrics.toString());

        Assert.assertNotNull(transferTaskMetrics);
        Assert.assertNotNull(transferTaskMetrics.domain);
        // Assert.assertNotNull(transferTaskMetrics.connectAddress);
        Assert.assertTrue(transferTaskMetrics.getSize() >= 0);
        Assert.assertTrue(transferTaskMetrics.getTookTime() >= 0);
        Assert.assertTrue(transferTaskMetrics.getFirstProgressTookTime() >= 0);
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    private void testPauseAndResume(TransferService transferService, String bucket, String filePath, String cosKey) {

        QCloudLogger.i("QCloudTest", "upload path is " + filePath);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
                cosKey, filePath);

        final COSUploadTask uploadTask = transferService.upload(putObjectRequest);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i(TestConst.UT_TAG, result.printResult());
                TestUtils.parseBadResponseBody(result);
                testLocker.release();
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

        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i(TestConst.UT_TAG, "transfer progress is " + complete + "/" + target);
            }
        });

        // 暂停
        TestUtils.sleep(1000);
        uploadTask.pause();

        // 恢复
        TestUtils.sleep(4000);
        uploadTask.resume();

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test public void testMultipartUploadFailed() {
        testUploadFileFailed(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET+"aaa",
                TestUtils.bigFilePath(), TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
    }

    // 测试简单上传
    @Test public void testSimpleUploadFailed() {
        testUploadFileFailed(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET+"aaa",
                TestUtils.smallFilePath(), TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    @Test public void testMultipartUploadFailed2() {
        testUploadFileFailed(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.bigFilePath()+"aaa", TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
    }

    // 测试简单上传
    @Test public void testSimpleUploadFailed2() {
        testUploadFileFailed(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.smallFilePath()+"aaa", TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    @Test public void testSimpleUploadFailed3() {
        testUploadFileFailed(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
                null, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    @Test public void testSimpleUploadFailed4() {
        testUploadFileFailed(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.localParentPath(), TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    @Test public void testSimpleUploadFailedUri() {
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                Uri.parse("file://test.jpg"));
        testUploadFileFailed(ServiceFactory.INSTANCE.newDefaultTransferService(), putObjectRequest);
    }

    private void testUploadFileFailed(TransferService transferService, String bucket, String filePath, String cosKey) {
        QCloudLogger.i("QCloudTest", "upload path is " + filePath);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
                cosKey, filePath);
        testUploadFileFailed(transferService, putObjectRequest);
    }

    private void testUploadFileFailed(TransferService transferService, PutObjectRequest putObjectRequest) {
        final COSUploadTask uploadTask = transferService.upload(putObjectRequest);
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

}
