package com.tencent.cos.xml.transfer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.QServer;
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
import com.tencent.cos.xml.model.tag.ListParts;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
public class WeakNetwrokPraticsTest {

    CountDownLatch countDownLatch_1;
    Exception exception_1;

    CountDownLatch countDownLatch_2;
    Exception exception_2;

    @Test
    public void test_100k() throws Exception{

        if (QServer.isForeign()) {
            Assert.assertTrue(true);
            return;
        }

        exception_1 = null;
        countDownLatch_1 = new CountDownLatch(1);
        Context appContext = InstrumentationRegistry.getContext();
        QServer.init(appContext);
        String cosPath = "100k";
        String srcPath = QServer.createFile(appContext, 1024 * 1024);
        String bucket = QServer.persistBucket;
        CosXmlService cosXmlService = QServer.cosXml;
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
                Log.d(QServer.TAG, "upload task state: " + state.name());
            }
        });
        /** 显示任务上传进度 */
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(QServer.TAG, "upload task progress: " + complete + "/" + target);
            }
        });
        /** 显示任务上传结果 */
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            /** 任务上传成功 */
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult uploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                Log.d(QServer.TAG, "upload task success: " + uploadTaskResult.printResult());
                countDownLatch_1.countDown();
            }

            /** 任务上传失败 */
            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(QServer.TAG, "upload task failed: " + (exception == null ? serviceException.getMessage() :
                        (exception.errorCode + "," + exception.getMessage())));
                exception_1 = exception == null ? serviceException : exception;
                countDownLatch_1.countDown();
            }
        });
        countDownLatch_1.await();
        if(exception_1 != null) throw exception_1;
        QServer.deleteLocalFile(srcPath);
    }

    @Test
    public void test_100k_resume() throws Exception{

        if (QServer.isForeign()) {
            Assert.assertTrue(true);
            return;
        }

        exception_2 = null;
        countDownLatch_2 = new CountDownLatch(1);
        Context appContext = InstrumentationRegistry.getContext();
        QServer.init(appContext);
        String cosPath = "100k_resume";
        String srcPath = QServer.createFile(appContext, 1024 * 1024 * 3);
        String bucket = QServer.persistBucket;
        CosXmlService cosXmlService = QServer.cosXml;
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
                Log.d(QServer.TAG, "upload task state: " + state.name());
            }
        });
        /** 显示任务上传进度 */
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(QServer.TAG, "upload task progress: " + complete + "/" + target);
            }
        });
        /** 显示任务上传结果 */
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            /** 任务上传成功 */
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult uploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                Log.d(QServer.TAG, "upload task success: " + uploadTaskResult.printResult());
                countDownLatch_2.countDown();
            }

            /** 任务上传失败 */
            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(QServer.TAG, "upload task failed: " + (exception == null ? serviceException.getMessage() :
                        (exception.errorCode + "," + exception.getMessage())));
                exception_2 = exception == null ? serviceException : exception;
                countDownLatch_2.countDown();
            }
        });
        countDownLatch_2.await();
        if(exception_2 != null) throw exception_2;
        QServer.deleteLocalFile(srcPath);
    }
}
