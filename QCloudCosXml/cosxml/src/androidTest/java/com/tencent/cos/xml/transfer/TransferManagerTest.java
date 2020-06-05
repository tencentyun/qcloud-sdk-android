package com.tencent.cos.xml.transfer;

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
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import static com.tencent.cos.xml.QServer.TAG;
import static com.tencent.cos.xml.QServer.appid;
import static com.tencent.cos.xml.QServer.region;

/**
 * Created by bradyxiao on 2018/9/14.
 * Copyright 2010-2018 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class TransferManagerTest {
    TransferManager transferManager;
    @Before
    public void init(){
        QServer.init(InstrumentationRegistry.getContext());
        transferManager = new TransferManager((CosXmlService) QServer.cosXml, new TransferConfig.Builder().build());
    }

    CountDownLatch countDownLatch1;

    @Test
    public void upload() throws Exception {
        countDownLatch1 = new CountDownLatch(1);
        String cosPath = "uploadTask_" + System.currentTimeMillis();
        final String srcPath = QServer.createFile(InstrumentationRegistry.getContext(), 1024 * 1024);
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(QServer.persistBucket, cosPath, srcPath, null);
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
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
    }

    CountDownLatch countDownLatch2;
    @Test
    public void upload1() throws Exception {

        countDownLatch2 = new CountDownLatch(1);
        String cosPath = "uploadTask_" + System.currentTimeMillis();
        final String srcPath = QServer.createFile(InstrumentationRegistry.getContext(), 20 * 1024 * 1024);
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(QServer.persistBucket, cosPath, srcPath, null);
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
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
    }

    CountDownLatch countDownLatch3;
    @Test
    public void uploadTask() throws Exception {
        countDownLatch3 = new CountDownLatch(1);
        String cosPath = "uploadTask_" + System.currentTimeMillis();
        final String srcPath = QServer.createFile(InstrumentationRegistry.getContext(), 20 * 1024 * 1024);
        PutObjectRequest putObjectRequest = new PutObjectRequest(QServer.persistBucket, cosPath, srcPath);
        putObjectRequest.setSign(700);
        putObjectRequest.setRequestHeaders("cos-xml-metate", "meta");
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(putObjectRequest, null);
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
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
    }

    CountDownLatch countDownLatch4;
    @Test
    public void download() throws Exception {
        countDownLatch4 = new CountDownLatch(1);
        String cosPath = "uploadTask_download";
        final String localDir = QServer.localParentDirectory(InstrumentationRegistry.getContext()).getPath();
        final String localFileName = "uploadTask_download";
        COSXMLDownloadTask cosxmlDownloadTask = transferManager.download(InstrumentationRegistry.getContext(),  QServer.persistBucket, cosPath, localDir, localFileName);
        cosxmlDownloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
            }
        });
        cosxmlDownloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                QServer.deleteLocalFile(localDir + File.separator + localFileName);
                countDownLatch4.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                countDownLatch4.countDown();
            }
        });
        cosxmlDownloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });

        countDownLatch4.await();
    }

    CountDownLatch countDownLatch5;

    @Test
    public void download1() throws Exception {
        countDownLatch5 = new CountDownLatch(1);
        String cosPath = "uploadTask_download";
        final String localDir = QServer.localParentDirectory(InstrumentationRegistry.getContext()).getPath();
        final String localFileName = "uploadTask_download";
        GetObjectRequest getObjectRequest = new GetObjectRequest(QServer.persistBucket, cosPath, localDir, localFileName);
        COSXMLDownloadTask cosxmlDownloadTask = transferManager.download(InstrumentationRegistry.getContext(), getObjectRequest);
        cosxmlDownloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Log.d(TAG,  String.format("progress = %d%%", (int)progress));
            }
        });
        cosxmlDownloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                QServer.deleteLocalFile(localDir + File.separator + localFileName);
                countDownLatch5.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                countDownLatch5.countDown();
            }
        });
        cosxmlDownloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });
        countDownLatch5.await();
    }


    CountDownLatch countDownLatch6;
    @Test
    public void copy() throws Exception{
        countDownLatch6 = new CountDownLatch(1);
        String cosPath = "uploadTask_download_copy";
        String sourceCosPath = "uploadTask_download";
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                appid, QServer.persistBucket, region, sourceCosPath);
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(QServer.persistBucket, cosPath, copySourceStruct);
        cosxmlCopyTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });
        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                countDownLatch6.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                countDownLatch6.countDown();
            }
        });

        countDownLatch6.await();

    }

    CountDownLatch countDownLatch7;
    @Test
    public void copy1()throws Exception{
        countDownLatch7 = new CountDownLatch(1);
        String cosPath = "uploadTask_download_copy2";
        String sourceCosPath = "uploadTask_download";
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                appid, QServer.persistBucket, region, sourceCosPath);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(QServer.persistBucket, cosPath, copySourceStruct);
        COSXMLCopyTask cosxmlCopyTask = transferManager.copy(copyObjectRequest);
        cosxmlCopyTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.d(TAG,  state.name());
            }
        });
        cosxmlCopyTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.d(TAG,  result.printResult());
                countDownLatch7.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.d(TAG,  exception == null ? serviceException.getMessage() : exception.toString());
                countDownLatch7.countDown();
            }
        });

        countDownLatch7.await();
    }
}