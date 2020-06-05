package com.tencent.cos.xml.transfer;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static com.tencent.cos.xml.QServer.TAG;

/**
 * Created by bradyxiao on 2018/9/14.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class COSXMLUploadTaskTest {

    TransferManager transferManager;

    @Before
    public void init() throws Exception{
        QServer.init(InstrumentationRegistry.getContext());
        transferManager = new TransferManager((CosXmlSimpleService) QServer.cosXml, new TransferConfig.Builder().build());
    }

    CountDownLatch countDownLatch1;

    @Test
    public void testPause()throws Exception{
        countDownLatch1 = new CountDownLatch(1);
        String cosPath = "uploadTask_pause" + System.currentTimeMillis();
        final String srcPath = QServer.createFile(InstrumentationRegistry.getContext(), 1024 * 1024);
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(QServer.persistBucket, cosPath, srcPath, null);
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
                if((int)progress > 50){
                    cosxmlUploadTask.pause();
                    countDownLatch1.countDown();
                }
            }
        });
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                QServer.deleteLocalFile(srcPath);
                countDownLatch1.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                QServer.deleteLocalFile(srcPath);
                countDownLatch1.countDown();
            }
        });
        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });
        countDownLatch1.await();
        QServer.deleteLocalFile(srcPath);
        Thread.sleep(500);
        assert cosxmlUploadTask.getTaskState() == TransferState.PAUSED;
    }

    CountDownLatch countDownLatch2;
    @Test
    public void cancel() throws Exception{
        countDownLatch2 = new CountDownLatch(1);
        String cosPath = "uploadTask_cancel" + System.currentTimeMillis();
        final String srcPath = QServer.createFile(InstrumentationRegistry.getContext(), 1024 * 1024);
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(QServer.persistBucket, cosPath, srcPath, null);
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
                if((int)progress > 50){
                    cosxmlUploadTask.cancel();
                }
            }
        });
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                QServer.deleteLocalFile(srcPath);
                countDownLatch2.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                QServer.deleteLocalFile(srcPath);
                countDownLatch2.countDown();
            }
        });
        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });

        countDownLatch2.await();
        assert cosxmlUploadTask.getTaskState() == TransferState.CANCELED;
    }

    private volatile boolean isResume = false;
    CountDownLatch countDownLatch3;
    @Test
    public void resume() throws Exception{
        countDownLatch3 = new CountDownLatch(1);
        String cosPath = "uploadTask_resume" + System.currentTimeMillis();
        final String srcPath = QServer.createFile(InstrumentationRegistry.getContext(), 2 * 1024 * 1024);
        final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(QServer.persistBucket, cosPath, srcPath, null);
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
                if((int)progress > 50 && !isResume){
                    cosxmlUploadTask.pause();
                    countDownLatch3.countDown();
                }
            }
        });
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                QServer.deleteLocalFile(srcPath);
                countDownLatch3.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                QServer.deleteLocalFile(srcPath);
                countDownLatch3.countDown();
            }
        });
        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });

        countDownLatch3.await();
        isResume = true;
        cosxmlUploadTask.resume();
        countDownLatch3.await();
    }

    @Test
    public void testUploadSequence() throws Exception{
        countDownLatch2 = new CountDownLatch(3);
        for (int i = 0 ; i < 4; i++) {
            String cosPath = "uploadTask_cancel" + i;
            final String srcPath = QServer.createFile(InstrumentationRegistry.getContext(), 10 * 1024 * 1024);
            final COSXMLUploadTask cosxmlUploadTask = transferManager.upload(QServer.persistBucket, cosPath, srcPath, null);
            cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    Log.d(TAG,  "Task onProgress " + Thread.currentThread());
                }
            });
            cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    Log.d(TAG,  result.printResult());
                    QServer.deleteLocalFile(srcPath);
                    Log.d(TAG,  "Task onSuccess " + Thread.currentThread());
                    countDownLatch2.countDown();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                    QServer.deleteLocalFile(srcPath);
                    countDownLatch2.countDown();
                }
            });
            cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
                @Override
                public void onStateChanged(TransferState state) {
                    Log.d(TAG,  state.name());
                }
            });
        }

        countDownLatch2.await();
        assert true;
    }
}