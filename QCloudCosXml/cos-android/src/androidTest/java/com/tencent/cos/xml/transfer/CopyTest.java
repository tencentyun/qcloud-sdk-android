package com.tencent.cos.xml.transfer;

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
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.CopyObjectResult;
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
                CopyObjectResult copyObjectResult = (CopyObjectResult)result;
                TestUtils.print(copyObjectResult.printResult());
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
    public void testResumeTask() throws Exception{
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final String cosPath = "copy_object_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        copyObjectRequest.setCosPath(cosPath);
        final TestLocker testLocker = new TestLocker();
        final COSXMLCopyTask cosxmlCopyTask = transferManager.copy(copyObjectRequest);
        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
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

        Thread.sleep(2000);
        if (cosxmlCopyTask.getTaskState() == TransferState.COMPLETED) {
            Assert.assertTrue(true);
            return;
        } else if (cosxmlCopyTask.getTaskState() == TransferState.IN_PROGRESS) {
            cosxmlCopyTask.pause();
            Thread.sleep(2000);
            String uploadId = cosxmlCopyTask.getUploadId();
            if(uploadId != null) {
                TestUtils.print("uploadId:" + uploadId);
            }
            Assert.assertNotNull(uploadId);
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
        TestUtils.sleep(200);
        cosxmlCopyTask.cancel();
        TestUtils.sleep(200);
        Assert.assertTrue(cosxmlCopyTask.getTaskState() == TransferState.CANCELED);
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

    @Test
    public void testCopyObjectSignature() {
        final TestLocker testLocker = new TestLocker();
        final String cosPath = "copy_object_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        final TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct, new MyOnSignatureListener());

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

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlCopyTask);
    }

    @Test
    public void testCopySmallObjectSignature() {

        final TestLocker testLocker = new TestLocker();
        final String cosPath = "copy_object_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct, new MyOnSignatureListener());

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

    @Test
    public void testCopyObjectFail() {
        final TestLocker testLocker = new TestLocker();
        final String cosPath = "copy_object_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        final TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(TestConst.PERSIST_BUCKET+"aaa", cosPath, copySourceStruct);
        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
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

    @Test
    public void testCopySmallObjectFail() {
        final TestLocker testLocker = new TestLocker();
        final String cosPath = "copy_object_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        final TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(TestConst.PERSIST_BUCKET+"aaa", cosPath, copySourceStruct);
        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
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

    @Test
    public void testCopySmallObjectFailByCheckParameters1() {
        CosXmlSimpleService cosXmlSimpleService =  ServiceFactory.INSTANCE.newDefaultService();
        final TestLocker testLocker = new TestLocker();
        final String cosPath = "copy_object_" + System.currentTimeMillis();
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(TestConst.PERSIST_BUCKET, cosPath, null);
        cosXmlSimpleService.copyObjectAsync(copyObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                Assert.assertTrue(clientException.getMessage().contains("copy source must not be null"));
                testLocker.release();
            }
        });
        testLocker.lock();
    }
}
