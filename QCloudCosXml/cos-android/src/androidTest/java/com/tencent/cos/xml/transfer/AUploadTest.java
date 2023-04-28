package com.tencent.cos.xml.transfer;

import static com.tencent.cos.xml.core.TestUtils.big60mFilePath;

import android.util.Log;

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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * Created by jordanqin on 2023/4/28 12:27.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class AUploadTest {
    private final String UPLOAD_FOLDER="/upload/";

    /**
     * 上传进度 100% 后点击暂停，并等待 5s 后恢复上传
     */
    @Test
    public void testPauseAndResumeTask() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                big60mFilePath(), null);
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
            }, 3000);
        }

        testLocker.lock(20000);
        TestUtils.sleep(10000);
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test
    public void testUploadIdCancel() throws Exception{
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final TestLocker uploadLocker = new TestLocker();
        String cosPath = UPLOAD_FOLDER+"uploadTask_resume";
        final String srcPath = big60mFilePath();
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, null);
        AtomicReference<String> uploadId = new AtomicReference<>(null);
        cosxmlUploadTask.setInitMultipleUploadListener(initiateMultipartUpload -> {
            uploadId.set(initiateMultipartUpload.uploadId);
        });

        COSXMLUploadTask cosxmlUploadTask1 = null;
        // 上传 1s 后暂停
        Thread.sleep(2000);
        if (cosxmlUploadTask.getTaskState() == TransferState.COMPLETED) {
            uploadLocker.release();
            Assert.assertTrue(true);
            return;
        } else if (cosxmlUploadTask.getTaskState() == TransferState.IN_PROGRESS) {
            cosxmlUploadTask.pauseSafely();
            Thread.sleep(10000);
            cosxmlUploadTask1 = transferManager.upload(TestConst.PERSIST_BUCKET, cosPath, srcPath, uploadId.get());
            cosxmlUploadTask1.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    TestUtils.sleep(10000);
                    uploadLocker.release();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    TestUtils.sleep(10000);
                    uploadLocker.release();

                }
            });
            Thread.sleep(2000);
            cosxmlUploadTask1.cancel();
        } else {
            uploadLocker.lock();
            Assert.fail(cosxmlUploadTask.getTaskState().name());
            return;
        }

        uploadLocker.lock();
        TestUtils.sleep(10000);
        Assert.assertTrue(cosxmlUploadTask1.getTaskState().name(), cosxmlUploadTask1.getTaskState() == TransferState.CANCELED);
    }

    @Test
    public void testPauseTask() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final String srcPath = big60mFilePath();
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH, srcPath, null);
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
        TestUtils.sleep(10000);
        Assert.assertTrue(cosxmlUploadTask.getTaskState().name(), cosxmlUploadTask.getTaskState() == TransferState.PAUSED);
    }

    @Test
    public void testCancelTask() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        final String srcPath = big60mFilePath();
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH, srcPath, null);
        TestUtils.sleep(2000);
        cosxmlUploadTask.cancel();
        TestUtils.sleep(10000);
        Assert.assertTrue(cosxmlUploadTask.getTaskState().name(), cosxmlUploadTask.getTaskState() == TransferState.CANCELED);
    }
}
