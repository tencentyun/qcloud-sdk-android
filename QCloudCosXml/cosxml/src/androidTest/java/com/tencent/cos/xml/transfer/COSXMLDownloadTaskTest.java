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

import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.tencent.cos.xml.QServer.TAG;

@RunWith(AndroidJUnit4.class)
public class COSXMLDownloadTaskTest {

    TransferManager transferManager;

    @Before
    public void init() throws Exception{
        QServer.init(TestUtils.getContext());
        transferManager = new TransferManager((CosXmlSimpleService) QServer.cosXml, new TransferConfig.Builder().build());
    }

    CountDownLatch countDownLatch1;

    @Test
    public void pause() throws Exception{
        countDownLatch1 = new CountDownLatch(1);
        String cosPath = "uploadTask_download";
        String srcPath = QServer.createFile(TestUtils.getContext(), 1024 * 1024);
        PutObjectRequest putObjectRequest = new PutObjectRequest(QServer.persistBucket, cosPath, srcPath);
        QServer.cosXml.putObject(putObjectRequest);
        QServer.deleteLocalFile(srcPath);
        final String localDir = QServer.localParentDirectory(TestUtils.getContext()).getPath();
        final String localFileName = cosPath;
        final COSXMLDownloadTask cosxmlDownloadTask = transferManager.download(TestUtils.getContext(), QServer.persistBucket, cosPath, localDir, localFileName);
        cosxmlDownloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
                if((int)progress > 50){
                    cosxmlDownloadTask.pause();
                    countDownLatch1.countDown();
                }
            }
        });
        cosxmlDownloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                countDownLatch1.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                countDownLatch1.countDown();
            }
        });
        cosxmlDownloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });

        countDownLatch1.await();
        assert cosxmlDownloadTask.getTaskState() == TransferState.PAUSED;
    }

    CountDownLatch countDownLatch2;
    @Test
    public void cancel() throws Exception{
        countDownLatch2 = new CountDownLatch(1);
        String cosPath = "uploadTask_download";
        String srcPath = QServer.createFile(TestUtils.getContext(), 1024 * 1024);
        PutObjectRequest putObjectRequest = new PutObjectRequest(QServer.persistBucket, cosPath, srcPath);
        QServer.cosXml.putObject(putObjectRequest);
        QServer.deleteLocalFile(srcPath);
        final String localDir = QServer.localParentDirectory(TestUtils.getContext()).getPath();
        final String localFileName = cosPath;
        final COSXMLDownloadTask cosxmlDownloadTask = transferManager.download(TestUtils.getContext(), QServer.persistBucket, cosPath, localDir, localFileName);
        cosxmlDownloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
                if((int)progress > 50){
                    cosxmlDownloadTask.cancel();
                }
            }
        });
        cosxmlDownloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                countDownLatch2.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                countDownLatch2.countDown();
            }
        });
        cosxmlDownloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });
        countDownLatch2.await();
        Thread.sleep(500);
        assert cosxmlDownloadTask.getTaskState() == TransferState.CANCELED;
    }

    CountDownLatch countDownLatch3;
    private volatile boolean isResume = false;
    @Test
    public void resume() throws Exception{
        countDownLatch3 = new CountDownLatch(1);
        String cosPath = "uploadTask_download";
        String srcPath = QServer.createFile(TestUtils.getContext(), 1024 * 1024);
        PutObjectRequest putObjectRequest = new PutObjectRequest(QServer.persistBucket, cosPath, srcPath);
        QServer.cosXml.putObject(putObjectRequest);
        QServer.deleteLocalFile(srcPath);
        final String localDir = QServer.localParentDirectory(TestUtils.getContext()).getPath();
        final String localFileName = cosPath;
        final COSXMLDownloadTask cosxmlDownloadTask = transferManager.download(TestUtils.getContext(), QServer.persistBucket, cosPath, localDir, localFileName);
        cosxmlDownloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
                if((int)progress > 50 && !isResume){
                    cosxmlDownloadTask.pause();
                    countDownLatch3.countDown();
                }
            }
        });
        cosxmlDownloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                countDownLatch3.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                countDownLatch3.countDown();
            }
        });
        cosxmlDownloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });
        countDownLatch3.await();
        isResume = true;
        cosxmlDownloadTask.resume();
        countDownLatch3.await();

    }

    @Test
    public void testBatchDownload() throws IOException, QCloudClientException, QCloudServiceException,InterruptedException {
        String cosPath = "batch_download_sample";
        String srcPath = QServer.createFile(TestUtils.getContext(), 10 * 1024 * 1024);
        PutObjectRequest putObjectRequest = new PutObjectRequest(QServer.persistBucket, cosPath, srcPath);
        QServer.cosXml.putObject(putObjectRequest);

        Log.d(TAG,  "PUT success");

        final String localDir = QServer.localParentDirectory(TestUtils.getContext()).getPath();
        final int taskCount = 5;
        List<COSXMLDownloadTask> tasks = new ArrayList<>(taskCount);

        final int snapTime = 500;

        for (int i = 0; i < taskCount; i++) {
            String localFileName = cosPath + "_" + i;
            QServer.deleteLocalFile(localDir + File.separator + localFileName);
            COSXMLDownloadTask cosxmlDownloadTask = transferManager.download(TestUtils.getContext(),
                    QServer.persistBucket, cosPath, localDir, localFileName);
            cosxmlDownloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    Log.d(TAG,  "Success: " + result.printResult());
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    if (exception != null) {
                        exception.getStackTrace();
                    }
                    Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                }
            });
            tasks.add(cosxmlDownloadTask);
        }
        Log.d(TAG,  "Download ALL");

        TimeUnit.MILLISECONDS.sleep(snapTime);

        for (COSXMLDownloadTask task : tasks) {
            task.pause();
        }
        Log.d(TAG,  "PAUSE ALL");

        TimeUnit.MILLISECONDS.sleep(snapTime);

        for (COSXMLDownloadTask task : tasks) {
            task.resume();
        }
        Log.d(TAG,  "RESUME ALL");

        int randomTask = 0;

        TimeUnit.MILLISECONDS.sleep(snapTime);

        tasks.get(randomTask).pause();
        Log.d(TAG,  "PAUSE ONE");

        TimeUnit.MILLISECONDS.sleep(snapTime);

        tasks.get(randomTask).resume();

        Log.d(TAG,  "RESUME ONE");

        TimeUnit.SECONDS.sleep(10);
    }

}