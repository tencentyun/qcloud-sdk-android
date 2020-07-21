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

package com.tencent.cos.xml.core.transfer;

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

    public static String getMD5(String str) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }

    private String cdnSign(String path, long timestamp, String rand, String key) {

        try {
            return getMD5(String.format("%s-%d-%s-%d-%s", path, timestamp, rand, 0, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Test public void testMd5() {

        String str = "/test.jpg-1595236205-c2iglbgtdni-0-b8ejrafcq7ax6y1pn1iw84cgu6";
        try {
            QCloudLogger.i("QCloud", getMD5(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void testGetCDNObject() {

        CosXmlService cosXmlService = TestUtils.newCdnTerminalService();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_PIC_PATH, TestConfigs.LOCAL_FILE_DIRECTORY.concat("test2/"));

        String path = TestConfigs.COS_PIC_PATH;
        Long timestamp = System.currentTimeMillis() / 1000;
        String rand = String.valueOf(new Random(timestamp).nextInt(10000));
        String key = TestConfigs.COS_SUB_BUCKET_CDN_SIGN;

        Map<String, String> paras = new HashMap<>();
        String sign = cdnSign(path, timestamp, rand, key);
        paras.put("sign", String.format("%d-%s-0-%s", timestamp, rand, sign));
        getObjectRequest.setQueryParameters(paras);

        try {
            cosXmlService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

    }

    @Test public void testBigCDNDownload() {

        TransferManager transferManager = TestUtils.newCdnTerminalTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET,
                TestConfigs.COS_TXT_1G_PATH, TestConfigs.LOCAL_FILE_DIRECTORY.concat("test2/"));

        String path = TestConfigs.COS_TXT_1G_PATH;
        Long timestamp = System.currentTimeMillis() / 1000;
        String rand = String.valueOf(new Random(timestamp).nextInt(10000));
        String key = TestConfigs.COS_SUB_BUCKET_CDN_SIGN;

        Map<String, String> paras = new HashMap<>();
        String sign = cdnSign(path, timestamp, rand, key);
        paras.put("sign", String.format("%d-%s-0-%s", timestamp, rand, sign));
        getObjectRequest.setQueryParameters(paras);

        try {
            getObjectRequest.setRequestHeaders("Host", "android-ut-persist-gz-1253653367.file.myqcloud.com", false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }

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
               QCloudLogger.i("QCloudHttp", "complete = " + complete + ",target = " + target);
            }
        });

        testLocker.lock();
    }



    @Test public void testHeadCDNObject() {


        CosXmlService cosXmlService = TestUtils.newCdnTerminalService();
        HeadObjectRequest headObjectRequest = new HeadObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_PIC_PATH);

        String path = TestConfigs.COS_PIC_PATH;
        Long timestamp = System.currentTimeMillis() / 1000;
        String rand = String.valueOf(new Random(timestamp).nextInt(10000));
        String key = TestConfigs.COS_SUB_BUCKET_CDN_SIGN;

        Map<String, String> paras = new HashMap<>();
        String sign = cdnSign(path, timestamp, rand, key);
        paras.put("sign", String.format("%d-%s-0-%s", timestamp, rand, sign));
        headObjectRequest.setQueryParameters(paras);

        try {
            headObjectRequest.setRequestHeaders("Host", "android-ut-persist-gz-1253653367.file.myqcloud.com", false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        try {
            cosXmlService.headObject(headObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
    }


    @Test public void testSmallDownload() {

        TransferManager transferManager = TestUtils.newDefaultTerminalTransferManager();

        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_PIC_PATH,
                TestConfigs.LOCAL_FILE_DIRECTORY.concat("/tet"));
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


}
