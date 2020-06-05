package com.tencent.cos.xml.download;

import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.core.TestConfigs;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tencent.cos.xml.core.TestUtils.mergeExceptionMessage;

@RunWith(AndroidJUnit4.class)
public class DownloadManagerTest {

    @Test public void testSmallDownload() {

        TransferManager transferManager = TestUtils.newDefaultTerminalTransferManager();
        COSXMLDownloadTask downloadTask = transferManager.download(InstrumentationRegistry.getContext(),
                TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_1M_PATH,
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

        testLocker.lock();
    }

    @Test public void testBigDownload() {

        TransferManager transferManager = TestUtils.newDefaultTerminalTransferManager();
        COSXMLDownloadTask downloadTask = transferManager.download(InstrumentationRegistry.getContext(),
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

            COSXMLDownloadTask downloadTask = transferManager.download(InstrumentationRegistry.getContext(),
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

            COSXMLDownloadTask downloadTask = transferManager.download(InstrumentationRegistry.getContext(),
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
        final COSXMLDownloadTask downloadTask = transferManager.download(InstrumentationRegistry.getContext(),
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
