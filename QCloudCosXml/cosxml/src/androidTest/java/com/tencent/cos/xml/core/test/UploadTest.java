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

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.core.TestConfigs;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tencent.cos.xml.core.TestUtils.mergeExceptionMessage;
import static com.tencent.cos.xml.core.TestUtils.newDefaultTerminalTransferManager;

@RunWith(AndroidJUnit4.class)
public class UploadTest {

    /**
     * 上传 1M 小文件测试
     */
    @Test public void testSimpleUpload() {

        TransferManager transferManager = newDefaultTerminalTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_1M_PATH, TestConfigs.LOCAL_TXT_1M_PATH);

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

    /**
     * 上传 1M 小文件测试
     */
    @Test public void testSimpleUploadRequestUri() {

        TransferManager transferManager = newDefaultTerminalTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_1M_PATH, Uri.fromFile(new File(TestConfigs.LOCAL_TXT_1M_PATH)));

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

    /**
     * 上传 1M 小文件测试
     */
    @Test public void testSimpleUploadUri() {

        TransferManager transferManager = newDefaultTerminalTransferManager();
        COSXMLUploadTask uploadTask = transferManager.upload(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_1M_PATH,
                Uri.fromFile(new File(TestConfigs.LOCAL_TXT_1M_PATH)), null);
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


    /**
     * 上传 100M 大文件测试
     */
    @Test public void testMultiUpload() {

        TransferManager transferManager = newDefaultTerminalTransferManager();
        COSXMLUploadTask uploadTask = transferManager.upload(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_100M_PATH,
                TestConfigs.LOCAL_TXT_100M_PATH, null);

        File localFile = new File(TestConfigs.LOCAL_TXT_100M_PATH);
        if (localFile.exists()) {
            Log.i(TestConfigs.UNIT_TEST_TAG, "file size is " + localFile.length());
        }

        final TestLocker testLocker = new TestLocker();

        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "upload success");
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "upload failed");
                Assert.fail(mergeExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "upload progress : " + complete + "/" + target);
            }
        });

        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "upload state : " + state);
            }
        });

        testLocker.lock();
    }

    /**
     * 上传 100M 大文件测试
     */
    @Test public void testMultiUploadUri() {

        TransferManager transferManager = newDefaultTerminalTransferManager();
        COSXMLUploadTask uploadTask = transferManager.upload(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_100M_PATH,
                Uri.fromFile(new File(TestConfigs.LOCAL_TXT_100M_PATH)), null);

        File localFile = new File(TestConfigs.LOCAL_TXT_100M_PATH);
        if (localFile.exists()) {
            Log.i(TestConfigs.UNIT_TEST_TAG, "file size is " + localFile.length());
        }

        final TestLocker testLocker = new TestLocker();

        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "upload success");
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "upload failed");
                Assert.fail(mergeExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "upload progress : " + complete + "/" + target);
            }
        });

        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "upload state : " + state);
            }
        });

        testLocker.lock();
    }


    // TODO: 2020-04-29 上传超大文件 10G


    /**
     * 批量小文件上传
     */
    @Test public void testBatchSimpleUpload() {

        TransferManager transferManager = newDefaultTerminalTransferManager();
        int count = 10;
        long size = 1024 * 1024;
        final TestLocker testLocker = new TestLocker(count);

        for (int i = 0; i < count; i ++) {

            String cosPath = "/ut_files/1M_" + i + ".txt";
            String localPath = Environment.getExternalStorageDirectory().getAbsolutePath() + cosPath;
            try {
                TestUtils.createFile(localPath, size);
            } catch (IOException e) {
                e.printStackTrace();
            }

            COSXMLUploadTask uploadTask = transferManager.upload(TestConfigs.TERMINAL_PERSIST_BUCKET, cosPath,
                    localPath, null);
            uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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
     * 批量大文件上传
     */
    @Test public void testBatchMultiUpload() {

        TransferManager transferManager = newDefaultTerminalTransferManager();
        int count = 10;
        long size = 100 * 1024 * 1024;
        final TestLocker testLocker = new TestLocker(count);

        for (int i = 0; i < count; i ++) {

            String cosPath = "/ut_files/100M_" + i + ".txt";
            String localPath = Environment.getExternalStorageDirectory().getAbsolutePath() + cosPath;
            try {
                TestUtils.createFile(localPath, size);
            } catch (IOException e) {
                e.printStackTrace();
            }

            COSXMLUploadTask uploadTask = transferManager.upload(TestConfigs.TERMINAL_PERSIST_BUCKET, cosPath,
                    localPath, null);
            uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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
     * 上传进度 100% 后点击暂停，并等待 5s 后恢复上传
     */
    @Test public void testPauseAndResume() {

        TransferManager transferManager = newDefaultTerminalTransferManager();
        final COSXMLUploadTask uploadTask = transferManager.upload(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_100M_PATH,
                TestConfigs.LOCAL_TXT_100M_PATH, null);
        final TestLocker testLocker = new TestLocker();
        final TestLocker waitPauseLocker = new TestLocker();

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

        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "state " + state);

            }
        });


        final AtomicBoolean pauseSuccess = new AtomicBoolean(false);
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

                Log.i(TestConfigs.UNIT_TEST_TAG, "progress  = " + 1.0 * complete / target);

                if (complete == target && !pauseSuccess.get()) {
                    if (uploadTask.pauseSafely()) {
                        Log.i(TestConfigs.UNIT_TEST_TAG, "pause success!!");
                        pauseSuccess.set(true);
                    } else {
                        Log.i(TestConfigs.UNIT_TEST_TAG, "pause failed!!");
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
                    Log.i(TestConfigs.UNIT_TEST_TAG, "start resume");
                    uploadTask.resume();
                }
            }, 5000);
        }

        testLocker.lock();
    }

    @Test public void testMultiSliceSizeUpload() {

        String localPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Ttt.mp4");
        StringHolder uploadId = new StringHolder();
        uploadAndPause(TestConfigs.TERMINAL_PERSIST_BUCKET, localPath, localPath, uploadId, 1024 * 1024, 20 * 1024 * 1024);
        TestUtils.sleep(2000);
        uploadAndPause(TestConfigs.TERMINAL_PERSIST_BUCKET, localPath, localPath, uploadId, 512 * 1024, 40 * 1024 * 1024);
        TestUtils.sleep(2000);
        uploadAndPause(TestConfigs.TERMINAL_PERSIST_BUCKET, localPath, localPath, uploadId, 2 * 1024 * 1024, 100 * 1024 * 1024);
    }

    private void uploadAndPause(String bucket, String cosPath, String localFile, final StringHolder uploadId, long sliceSize, final long pauseSize) {

        TransferManager transferManager = newDefaultTerminalTransferManager(sliceSize);
        final COSXMLUploadTask uploadTask = transferManager.upload(bucket, cosPath, localFile, uploadId.getValue());
        final TestLocker testLocker = new TestLocker();

        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.assertTrue(true);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(mergeExceptionMessage(clientException, serviceException));
            }
        });

        uploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                Log.i(TestConfigs.UNIT_TEST_TAG, "state " + state);
            }
        });

        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

                Log.i(TestConfigs.UNIT_TEST_TAG, "progress  = " + complete + "/" + target);

                if (complete > pauseSize) {
                    uploadId.setValue(uploadTask.getUploadId());
                    uploadTask.pauseSafely();
                    testLocker.release();
                }
            }
        });
        testLocker.lock();
    }

    static class StringHolder {

        String value;

        public StringHolder() {}

        public StringHolder(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
