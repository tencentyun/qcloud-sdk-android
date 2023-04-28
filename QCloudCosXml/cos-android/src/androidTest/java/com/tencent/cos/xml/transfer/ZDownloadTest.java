package com.tencent.cos.xml.transfer;

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
import com.tencent.cos.xml.model.object.GetObjectRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * Created by jordanqin on 2023/4/28 12:30.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class ZDownloadTest {
    /**
     * 下载 1s 后点击暂停，并等待 1s 后恢复上传
     */
    @Test
    public void testPauseAndResume() {
        TestUtils.sleep(20000);
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath());

        final TestLocker testLocker = new TestLocker();
        final AtomicBoolean checkNotZero = new AtomicBoolean(false);
        final AtomicLong currentProgress = new AtomicLong(0);

        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
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

        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

                currentProgress.set(complete);
                if (checkNotZero.get()) {
                    checkNotZero.set(false);
                    if (complete < currentProgress.get()) {
                        testLocker.release();
                    }
                }
                TestUtils.print("download task " + complete + "/" + target);
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (downloadTask.getTaskState() == TransferState.COMPLETED) {
            TestUtils.assertCOSXMLTaskSuccess(downloadTask);
            return;
        }

        downloadTask.pause();
        TestUtils.sleep(1000);
        checkNotZero.set(true);
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
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
        downloadTask.resume();

        testLocker.lock(30000);
        TestUtils.sleep(10000);
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test
    public void testCancelTask() {
        TestUtils.sleep(20000);
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        TestUtils.sleep(2000);
        downloadTask.cancel();
        TestUtils.sleep(10000);
        Assert.assertTrue(downloadTask.getTaskState().name(), downloadTask.getTaskState() == TransferState.CANCELED ||
                downloadTask.getTaskState() == TransferState.COMPLETED);
    }
}
