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

package com.tencent.cos.xml.core.test;

import android.content.Context;
import android.os.Environment;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.core.TestConfigs;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.tag.pic.PicOriginalInfo;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tencent.cos.xml.QServer.TAG;
import static com.tencent.cos.xml.core.TestUtils.mergeExceptionMessage;
import static com.tencent.cos.xml.core.TestUtils.newDefaultTerminalTransferManager;
import static com.tencent.cos.xml.core.TestUtils.newQuicTerminalTransferManager;


/**
 * Created by bradyxiao on 2018/3/13.
 */

@RunWith(AndroidJUnit4.class)
public class QuicTest {


    // quic 上传小文件
    @Test public void testSingleQuicUpload() {

        TransferManager transferManager = newQuicTerminalTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConfigs.COS_SUB_BUCKET_QUIC, TestConfigs.COS_TXT_1M_PATH, TestConfigs.LOCAL_TXT_1M_PATH);
        putObjectRequest.setRegion(TestConfigs.COS_SUB_BUCKET_QUIC_REGION);

        COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(mergeExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
    }

    // quic 上传大文件
    @Test public void testBigQuicUpload() {

        long fileLength = 20 * 1024 * 1024;

        String srcPath = null;
        try {
            srcPath = QServer.createFile(TestUtils.getContext(), fileLength);
        } catch (IOException e) {
            e.printStackTrace();
        }


        TransferManager transferManager = newQuicTerminalTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConfigs.COS_SUB_BUCKET_QUIC, TestConfigs.COS_TXT_1G_PATH, srcPath);
        putObjectRequest.setRegion(TestConfigs.COS_SUB_BUCKET_QUIC_REGION);



        COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
                Assert.assertTrue(true);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                testLocker.release();
                Assert.fail(mergeExceptionMessage(clientException, serviceException));
            }
        });

        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i("QCloudQuic", complete + "/" + target);
            }
        });

        testLocker.lock();
    }

    // quic 并发上传
    @Test public void testMultiQuicUpload() {

        TransferManager transferManager = newQuicTerminalTransferManager();
        String[] localPaths = new String[] {
                TestConfigs.COS_TXT_1M_PATH  + "1",
                TestConfigs.COS_TXT_1M_PATH  + "2",
                TestConfigs.COS_TXT_1M_PATH  + "3",
                TestConfigs.COS_TXT_1M_PATH  + "4",
                TestConfigs.COS_TXT_1M_PATH  + "5",
                TestConfigs.COS_TXT_1M_PATH  + "6"
        };

        final AtomicInteger successCounter = new AtomicInteger(0);

        final TestLocker testLocker = new TestLocker(localPaths.length);

        for (String localPath : localPaths) {

            PutObjectRequest putObjectRequest = new PutObjectRequest(TestConfigs.COS_SUB_BUCKET_QUIC, localPath,
                    TestConfigs.LOCAL_TXT_1M_PATH);
            putObjectRequest.setRegion(TestConfigs.COS_SUB_BUCKET_QUIC_REGION);

            COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
            uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    successCounter.addAndGet(1);
                    testLocker.release();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    Assert.fail(mergeExceptionMessage(clientException, serviceException));
                    testLocker.release();
                }
            });
        }
        testLocker.lock();
        Assert.assertEquals(successCounter.get(), localPaths.length);
    }

    @Test public void testHeadQuicObject() {

        CosXmlService cosXmlService = TestUtils.newQuicTerminalService();
        HeadObjectRequest headObjectRequest = new HeadObjectRequest(TestConfigs.COS_SUB_BUCKET_QUIC, TestConfigs.COS_TXT_1M_PATH);
        try {
            cosXmlService.headObject(headObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
    }

    @Test public void testBigQuicDownload() {

        TransferManager transferManager = TestUtils.newQuicTerminalTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConfigs.COS_SUB_BUCKET_QUIC,
                TestConfigs.COS_TXT_1G_PATH, TestConfigs.LOCAL_FILE_DIRECTORY.concat("test2/"));

        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(), getObjectRequest);
        final TestLocker testLocker = new TestLocker();

        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(mergeExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i("QCloudQuic", "complete = " + complete + ",target = " + target);
            }
        });

        testLocker.lock();
    }

}
