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

package com.tencent.cos.xml.weak_network;

import android.content.Context;

import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;


public class WeakNetwrokPraticsTest {

    CountDownLatch countDownLatch_1;
    Exception exception_1;

    CountDownLatch countDownLatch_2;
    Exception exception_2;


    public void test_100k() throws Exception{

        if (!TestConst.WEAK_NETWORK_TEST) {
            Assert.assertTrue(true);
            return;
        }

        exception_1 = null;
        countDownLatch_1 = new CountDownLatch(1);
        Context appContext = TestUtils.getContext();
        String cosPath = "100k";
        String srcPath = TestUtils.localPath(cosPath);
        TestUtils.createFile(srcPath, 1024 * 1024);
        String bucket = TestConst.PERSIST_BUCKET;
        CosXmlService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        String uploadId = null;
        /** 设置分片上传时，分片块的大小 */
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(1024 * 1024) // 1M
                .setSliceSizeForUpload(100 * 1024) // 100k
                .build();
        /** 初始化TransferManager */
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);
        /** 开始上传: 若 uploadId != null,则可以进行续传 */
        final COSXMLUploadTask uploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);
        /** 显示任务状态信息 */
        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TestConst.UT_TAG, "upload task state: " + state.name());
            }
        });
        /** 显示任务上传进度 */
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TestConst.UT_TAG, "upload task progress: " + complete + "/" + target);
            }
        });
        /** 显示任务上传结果 */
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            /** 任务上传成功 */
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult uploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                Log.d(TestConst.UT_TAG, "upload task success: " + uploadTaskResult.printResult());
                countDownLatch_1.countDown();
            }

            /** 任务上传失败 */
            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TestConst.UT_TAG, "upload task failed: " + (exception == null ? serviceException.getMessage() :
                        (exception.errorCode + "," + exception.getMessage())));
                exception_1 = exception == null ? serviceException : exception;
                countDownLatch_1.countDown();
            }
        });
        countDownLatch_1.await();
        if(exception_1 != null) throw exception_1;
        TestUtils.removeLocalFile(srcPath);
    }

    public void test_100k_resume() throws Exception{

        if (!TestConst.WEAK_NETWORK_TEST) {
            Assert.assertTrue(true);
            return;
        }

        exception_2 = null;
        countDownLatch_2 = new CountDownLatch(1);
        Context appContext = TestUtils.getContext();
        String cosPath = "100k_resume";
        String srcPath = TestUtils.localPath(cosPath);
        TestUtils.createFile(srcPath, 1024 * 1024 * 3);
        String bucket = TestConst.PERSIST_BUCKET;
        CosXmlService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        String uploadId = null;


        InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, cosPath);
        InitMultipartUploadResult initMultipartUploadResult = cosXmlService.initMultipartUpload(initMultipartUploadRequest);
        uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;

        int partNumber;
        UploadPartRequest uploadPartRequest;
        UploadPartResult uploadPartResult;
        //upload part 1
        partNumber = 1;
        uploadPartRequest = new UploadPartRequest(bucket, cosPath, partNumber, srcPath, 0, 1024 * 1024, uploadId);
        uploadPartResult = cosXmlService.uploadPart(uploadPartRequest);

        //upload part 3
        partNumber = 3;
        uploadPartRequest = new UploadPartRequest(bucket, cosPath, partNumber, srcPath, 2 * 1024 * 1024, 1024 * 1024, uploadId);
        uploadPartResult = cosXmlService.uploadPart(uploadPartRequest);


        ListPartsRequest listPartsRequest = new ListPartsRequest(bucket, cosPath, uploadId);
        ListPartsResult listPartsResult = cosXmlService.listParts(listPartsRequest);
        if(listPartsResult.listParts.parts.size() != 2){
            throw new Exception("parts upload error");
        }


        /** 设置分片上传时，分片块的大小 */
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(1024 * 1024) // 1M
                .setSliceSizeForUpload(100 * 1024) // 100k
                .build();
        /** 初始化TransferManager */
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);
        /** 开始上传: 若 uploadId != null,则可以进行续传 */
        final COSXMLUploadTask uploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);
        /** 显示任务状态信息 */
        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TestConst.UT_TAG, "upload task state: " + state.name());
            }
        });
        /** 显示任务上传进度 */
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TestConst.UT_TAG, "upload task progress: " + complete + "/" + target);
            }
        });
        /** 显示任务上传结果 */
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            /** 任务上传成功 */
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult uploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                Log.d(TestConst.UT_TAG, "upload task success: " + uploadTaskResult.printResult());
                countDownLatch_2.countDown();
            }

            /** 任务上传失败 */
            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TestConst.UT_TAG, "upload task failed: " + (exception == null ? serviceException.getMessage() :
                        (exception.errorCode + "," + exception.getMessage())));
                exception_2 = exception == null ? serviceException : exception;
                countDownLatch_2.countDown();
            }
        });
        countDownLatch_2.await();
        if(exception_2 != null) throw exception_2;
        TestUtils.removeLocalFile(srcPath);
    }
}
