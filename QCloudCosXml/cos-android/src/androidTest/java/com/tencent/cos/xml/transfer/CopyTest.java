package com.tencent.cos.xml.transfer;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * Created by rickenwang on 2020/10/20.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class CopyTest {
    @Test
    public void testCopyObject() throws InterruptedException {

        final TestLocker testLocker = new TestLocker();
        final String cosPath = "copy_object_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        final TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);

        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                CosXmlSimpleService cosXmlService = transferManager.getCosXmlService();
                try {
                    DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(TestConst.PERSIST_BUCKET, cosPath);
                    cosXmlService.deleteObject(deleteObjectRequest);
                } catch (CosXmlClientException e) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                } catch (CosXmlServiceException e) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                }

                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
                testLocker.release();
            }
        });

        String uploadId = cosxmlCopyTask.getUploadId();
        // 上传 2s 后暂停
        Thread.sleep(2000);
        if (cosxmlCopyTask.getTaskState() == TransferState.COMPLETED) {
            Assert.assertTrue(true);
            return;
        } else if (cosxmlCopyTask.getTaskState() == TransferState.IN_PROGRESS) {
            cosxmlCopyTask.pause();
            Thread.sleep(2000);
            cosxmlCopyTask.resume();
        } else {
            Assert.fail();
            return;
        }

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlCopyTask);
    }

    @Test
    public void testCancelTask() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final String cosPath = "copy_object_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        final COSXMLCopyTask cosxmlCopyTask = transferManager.copy(copyObjectRequest);
        final TestLocker testLocker = new TestLocker();
        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
                TestUtils.assertCOSXMLTaskSuccess(cosxmlCopyTask);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                testLocker.release();
                Assert.assertEquals("UserCancelled", clientException.getMessage());
            }
        });

        TestUtils.sleep(1000);
        cosxmlCopyTask.cancel();
        TestUtils.sleep(200);
        testLocker.lock();
    }


    @Test
    public void testCopySmallObject() {

        final TestLocker testLocker = new TestLocker();
        final String cosPath = "copy_object_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(copyObjectRequest);

        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
                try {
                    DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(TestConst.PERSIST_BUCKET, cosPath);
                    cosXmlService.deleteObject(deleteObjectRequest);
                } catch (CosXmlClientException e) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                } catch (CosXmlServiceException e) {
                    Assert.fail(TestUtils.getCosExceptionMessage(e));
                }

                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlCopyTask);
    }
}
