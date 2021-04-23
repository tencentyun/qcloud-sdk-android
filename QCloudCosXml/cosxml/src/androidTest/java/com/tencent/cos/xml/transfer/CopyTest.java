package com.tencent.cos.xml.transfer;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSStorageClass;
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
import com.tencent.cos.xml.model.tag.ACLAccount;

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
    private final String COPY_FOLDER="/copy/";

    @Test
    public void testCopySmallObject() {
        final TestLocker testLocker = new TestLocker();
        String cosPath = COPY_FOLDER + "copy_object_small_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        CopyObjectRequest request = new CopyObjectRequest(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        request.setCosPath(cosPath);
        Assert.assertEquals(request.getCosPath(), cosPath);
        request.setCosStorageClass(COSStorageClass.STANDARD);

        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
        request.setXCOSGrantRead(aclAccount);
        request.setXCOSGrantWrite(null);
        request.setXCOSReadWrite(aclAccount);
        request.setXCOSACL(COSACL.DEFAULT);

        request.setCopyIfModifiedSince("Wed, 21 Oct 2009 07:28:00 GMT");
//        request.setCopyIfUnmodifiedSince("Wed, 21 Oct 2052 07:28:00 GMT");
        request.setCopyIfMatch(null);
        request.setCopyIfNoneMatch("none_match_etag");
        try {
            request.setCopySourceServerSideEncryptionCustomerKey(null);
            request.setCopySourceServerSideEncryptionKMS(null, null);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(request);

        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                result.printResult();
                TestUtils.parseBadResponseBody(result);
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
    public void testFailCopySmallObject() {
        final TestLocker testLocker = new TestLocker();
        String cosPath = COPY_FOLDER + "copy_object_big_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+"notexist");
        CopyObjectRequest request = new CopyObjectRequest(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        request.setXCOSACL(COSACL.DEFAULT.getAcl());
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(request);

        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
                testLocker.release();
                Assert.assertTrue(true);
            }
        });
        testLocker.lock();
    }

    private int retry = 3;
    @Test
    public void testCopyBigObject() {
        final TestLocker testLocker = new TestLocker();
        String cosPath = COPY_FOLDER + "copy_object_big_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        CopyObjectRequest request = new CopyObjectRequest(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        request.setXCOSACL(COSACL.DEFAULT.getAcl());
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(request);

        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
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

        //重试三次 有些时候会出现QCloudClientException: com.tencent.qcloud.core.common.QCloudAuthenticationException: Credentials is null.
        if(cosxmlCopyTask.getTaskState() != TransferState.COMPLETED){
            if(retry > 0) {
                TestUtils.sleep(600);
                retry--;
                testCopyBigObject();
            } else {
                TestUtils.assertCOSXMLTaskSuccess(cosxmlCopyTask);
            }
        } else {
            Assert.assertTrue(true);
        }
    }


    @Test
    public void testCancelTask(){
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        String cosPath = COPY_FOLDER + "copy_object_cancel_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        TestUtils.sleep(2000);
        cosxmlCopyTask.cancel();
        TestUtils.sleep(200);
        Assert.assertTrue(cosxmlCopyTask.getTaskState() == TransferState.CANCELED);
    }

    @Test
    public void testPauseTask() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        String cosPath = COPY_FOLDER + "copy_object_pause_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);
        TestUtils.sleep(1000);
        cosxmlCopyTask.pause();
        TestUtils.sleep(200);
        Assert.assertTrue(cosxmlCopyTask.getTaskState() == TransferState.PAUSED);
    }

    @Test
    public void testResumeTask() throws Exception{
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        final TestLocker uploadLocker = new TestLocker();
        String cosPath = COPY_FOLDER + "copy_object_resume_" + System.currentTimeMillis();
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(TestConst.PERSIST_BUCKET, cosPath, copySourceStruct);

        cosxmlCopyTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i(TestConst.UT_TAG, state.toString());
            }
        });

        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
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
        Thread.sleep(5000);
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

        uploadLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(cosxmlCopyTask);
    }
}
