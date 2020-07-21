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


import com.tencent.cos.xml.core.TestUtils;;
import androidx.test.ext.junit.runners.AndroidJUnit4;
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

@RunWith(AndroidJUnit4.class)
public class COSXMLUploadTaskTest {

    TransferManager transferManager;

    @Before
    public void init() throws Exception{
        QServer.init(TestUtils.getContext());
        transferManager = new TransferManager((CosXmlSimpleService) QServer.cosXml, new TransferConfig.Builder().build());
    }

    CountDownLatch countDownLatch1;

    @Test
    public void testPause()throws Exception{
        countDownLatch1 = new CountDownLatch(1);
        String cosPath = "uploadTask_pause" + System.currentTimeMillis();
        final String srcPath = QServer.createFile(TestUtils.getContext(), 1024 * 1024);
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
        final String srcPath = QServer.createFile(TestUtils.getContext(), 1024 * 1024);
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
        final String srcPath = QServer.createFile(TestUtils.getContext(), 2 * 1024 * 1024);
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
            final String srcPath = QServer.createFile(TestUtils.getContext(), 10 * 1024 * 1024);
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