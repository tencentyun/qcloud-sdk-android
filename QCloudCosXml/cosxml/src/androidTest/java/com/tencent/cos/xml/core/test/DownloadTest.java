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
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConfigs;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tencent.cos.xml.core.TestUtils.mergeExceptionMessage;

@RunWith(AndroidJUnit4.class)
public class DownloadTest {

    @Test public void testGetObject() {

        CosXmlService cosXmlService = TestUtils.newDefaultTerminalService();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_PIC_PATH,
                TestConfigs.LOCAL_FILE_DIRECTORY.concat("/tet"));
        final TestLocker testLocker = new TestLocker();
        cosXmlService.getObjectAsync(getObjectRequest, new CosXmlResultListener() {
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


    @Test public void testSmallDownload() {

        TransferManager transferManager = TestUtils.newDefaultTerminalTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET,
                TestConfigs.COS_PIC_PATH, TestConfigs.LOCAL_FILE_DIRECTORY.concat("/tet"));
        Map<String, String> params = new HashMap<>();
        params.put("watermark/3/type/3/text/dGVuY2VudCBjbG91ZA==", null);
        getObjectRequest.setQueryParameters(params);

        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

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

        testLocker.lock();
    }

    @Test public void testBigDownload() {

        TransferManager transferManager = TestUtils.newDefaultTerminalTransferManager();
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_1G_PATH,
                TestConfigs.LOCAL_FILE_DIRECTORY);
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
                QCloudLogger.i("QCloudHttp", "complete = " + complete + ",target = " + target);
            }
        });

        testLocker.lock();
    }

    /**
     * 批量小文件下载
     */
    @Test public void testBatchSmallUpload() {

        TransferManager transferManager = TestUtils.newDefaultTerminalTransferManager();
        int count = 10;
        final TestLocker testLocker = new TestLocker(count);

        for (int i = 0; i < count; i ++) {

            String cosPath = "/ut_files/1M_" + i + ".txt";
            String localName = "1M_" + i + ".txt";

            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConfigs.TERMINAL_PERSIST_BUCKET, cosPath,
                    TestConfigs.LOCAL_FILE_DIRECTORY, localName);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
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
        Assert.assertTrue(true);
    }

    /**
     * 批量大文件下载
     */
    @Test public void testBatchBigUpload() {

        TransferManager transferManager = TestUtils.newDefaultTerminalTransferManager();
        int count = 10;
        final TestLocker testLocker = new TestLocker(count);

        for (int i = 0; i < count; i ++) {

            String cosPath = "/ut_files/100M_" + i + ".txt";
            String localName = "100M_" + i + ".txt";

            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConfigs.TERMINAL_PERSIST_BUCKET, cosPath,
                    TestConfigs.LOCAL_FILE_DIRECTORY, localName);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
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
        Assert.assertTrue(true);
    }


    /**
     * 下载进度 50% 后点击暂停，并等待 5s 后恢复上传
     */
    @Test public void testPauseAndResume() {

        TransferManager transferManager = TestUtils.newDefaultTerminalTransferManager();
        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_100M_PATH,
                TestConfigs.LOCAL_FILE_DIRECTORY);
        final TestLocker testLocker = new TestLocker();
        final TestLocker waitPauseLocker = new TestLocker();

        TestUtils.removeLocalFile(TestConfigs.LOCAL_TXT_100M_PATH);

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

        downloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "state " + state);

            }
        });


        final AtomicBoolean hasPaused = new AtomicBoolean(false);
        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

                Log.i(TestConfigs.UNIT_TEST_TAG, "progress  = " + 1.0 * complete / target);

                if (complete >= target / 2 && !hasPaused.get()) {
                    downloadTask.pause();
                    hasPaused.set(true);
                    waitPauseLocker.release();
                }
            }
        });

        waitPauseLocker.lock(); // 等待暂停

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(TestConfigs.UNIT_TEST_TAG, "start resume");
                downloadTask.resume();
            }
        }, 5000);


        testLocker.lock();
    }


    /**
     * 测试续传
     */
    @Test public void testContinueDownload() {


        TransferManager transferManager = TestUtils.newDefaultTerminalTransferManager();
        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_100M_PATH,
                TestConfigs.LOCAL_FILE_DIRECTORY);

        final TestLocker waitFinishLocker = new TestLocker();
        final TestLocker waitPauseLocker = new TestLocker();

        TestUtils.removeLocalFile(TestConfigs.LOCAL_TXT_100M_PATH);

        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

            if (complete >= target / 2) {
                downloadTask.pause();
                waitPauseLocker.release();
            }
            }
        });

        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail("onSuccess");
                waitPauseLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(mergeExceptionMessage(clientException, serviceException));
                waitPauseLocker.release();
            }
        });

        waitPauseLocker.lock(); // 等待暂停

        COSXMLDownloadTask continueTask = transferManager.download(TestUtils.getContext(),
                TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_100M_PATH,
                TestConfigs.LOCAL_FILE_DIRECTORY);

        continueTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

                if (complete < target / 2) {
                    Assert.fail("continue download failed");
                }
            }
        });

        continueTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.assertTrue(true);
                waitFinishLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(mergeExceptionMessage(clientException, serviceException));
                waitFinishLocker.release();
            }
        });

        waitFinishLocker.lock();
    }


}
