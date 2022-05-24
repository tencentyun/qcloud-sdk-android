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
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.tag.pic.PicOperationRule;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.qcloud.core.logger.QCloudLogger;
//import com.tencent.qcloud.quic.QuicConfig;
//import com.tencent.qcloud.quic.QuicProxy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.LinkedList;

import static com.tencent.cos.xml.crypto.Headers.COS_HASH_CRC64_ECMA;


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
                TestUtils.localPath("shanshui.png"), TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
    }

    // 测试简单上传
    @Test public void testSimpleUpload() {
        testUploadFile(ServiceFactory.INSTANCE.newDefaultTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.localPath("shanshui.jpg"), TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
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


    @Test public void testCsePauseAndResume() {
        testPauseAndResume(ServiceFactory.INSTANCE.newCesTransferService(), TestConst.PERSIST_BUCKET,
                TestUtils.bigFilePath(), TestConst.PERSIST_BUCKET_CSE_BIG_OBJECT_PATH);
    }

    private void testUploadFile(TransferService transferService, String bucket, String filePath, String cosKey) {

        QCloudLogger.i("QCloudTest", "upload path is " + filePath);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
                cosKey, filePath);
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
        TestUtils.sleep(3000);
        uploadTask.pause();

        // 恢复
        TestUtils.sleep(4000);
        uploadTask.resume();

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }


}
