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

import static com.tencent.cos.xml.core.TestUtils.print;
import static com.tencent.cos.xml.core.TestUtils.smallFilePath;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.MyOnSignatureListener;
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
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.qcloud.core.http.HttpTaskMetrics;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.TaskManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RunWith(AndroidJUnit4.class)
public class DownloadTest {

    @After public void clearDownloadFiles() {
        //TestUtils.clearDir(new File(TestUtils.localParentPath()));
    }

    @Test public void testSmallDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
       // getObjectRequest.setRange(100, 200);

        /// getObjectRequest.addNoSignHeader("Range");
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        // downloadTask.startTimeoutTimer(100);
        downloadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {
                InetAddress socketAddress = httpTaskMetrics.getConnectAddress();
                if (socketAddress != null) {
                    QCloudLogger.i(TestConst.UT_TAG, "connect ip is " + socketAddress.getHostAddress());
                }
            }
        });

        final TestLocker testLocker = new TestLocker();
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
        downloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i("QCloudTest", "transfer state is " + state);
            }
        });



        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testFailSmallDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+"notexist",
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
                Assert.assertTrue(true);
            }
        });

        testLocker.lock();

        COSXMLDownloadTask downloadTask1 = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath()+"notexist");
        downloadTask1.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
                Assert.assertTrue(true);
            }
        });
        testLocker.lock();
    }

    @Test public void testBigDownload() {

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
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

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    /**
     * 批量文件下载
     */
    @Test public void testBatchSmallDownload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        int count = 6;
        final TestLocker testLocker = new TestLocker(count);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final AtomicInteger successCount = new AtomicInteger(0);
        final StringBuilder errorMessage = new StringBuilder();

        for (int i = 0; i < count; i ++) {

            final String localName = "1M_" + i + ".txt";
            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                    TestUtils.localParentPath(), localName);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    successCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                    }
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    errorMessage.append(TestUtils.getCosExceptionMessage(clientException, serviceException)).append("/r/n");
                    errorCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                    }
                }
            });

            downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    TestUtils.print(localName+ " download_Progress" + ": " + complete + "/" + target);
                }
            });
            downloadTask.setTransferStateListener(new TransferStateListener() {
                @Override
                public void onStateChanged(TransferState state) {
                    TestUtils.print(localName+ " download_State:" + state.name());
                }
            });
        }

        testLocker.lock();
        TestUtils.assertErrorMessageNull(errorMessage);
    }

    /**
     * 批量文件下载
     */
    @Test public void testBatchSmallGet() {
        CosXmlSimpleService simpleService = ServiceFactory.INSTANCE.newAnonymousService();
        int count = 36;
        final TestLocker testLocker = new TestLocker(count);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < count; i ++) {
            int finalI = i;
            final String localName = "image_" + i + ".jpg";
            GetObjectRequest request = new GetObjectRequest("ut-1257101689", String.format("small_images/%s.jpg", i+1),
                    TestUtils.localParentPath() + File.separator + localName);
            new Thread(() -> {
                try {
                    print("start_"+ finalI);
                    long startTime = System.nanoTime();
                    GetObjectResult result = simpleService.getObject(request);
                    print("end_"+finalI);
                    print("took_time_"+finalI+"_"+toSeconds(System.nanoTime() - startTime));
                    successCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                        testBatchSmallGet();
                    }
                } catch (CosXmlClientException e) {
                    e.printStackTrace();
                    errorCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                        testBatchSmallGet();
                    }
                } catch (CosXmlServiceException e) {
                    e.printStackTrace();
                    errorCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                        testBatchSmallGet();
                    }
                }
            }).start();
        }

        testLocker.lock();
    }

    private double toSeconds(long nanotime) {
        return (double)nanotime / 1_000_000_000.0;
    }

    /**
     * 批量文件下载
     */
    @Test public void testBatchSmallGetNotCosSdk() {
        int count = 36;
        final TestLocker testLocker = new TestLocker(count);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < count; i ++) {
            String urlString = String.format("https://mobile-ut-1253960454.cos.ap-guangzhou.myqcloud.com/small_images/%s.jpg", i+1);
            int finalI = i;
            new Thread(() -> {
                HttpURLConnection getConnection = null;
                try {
                    URL url = new URL(urlString);
                    print("start_"+ finalI);
                    long startTime = System.nanoTime();
                    getConnection = (HttpURLConnection) url.openConnection();
                    getConnection.setRequestMethod("GET");

                    int responseCode = getConnection.getResponseCode();
                    print("end_"+finalI);
                    print("took_time_"+finalI+"_"+toSeconds(System.nanoTime() - startTime));

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        successCount.addAndGet(1);
                        if(successCount.get() + errorCount.get() == count) {
                            testLocker.release();
                        }
                    } else {
                        Log.e(TestConst.UT_TAG, "HTTP error code: " + responseCode);
                        errorCount.addAndGet(1);
                        if(successCount.get() + errorCount.get() == count) {
                            testLocker.release();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TestConst.UT_TAG, "Error sending GET request: " + e.getMessage());
                    errorCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                    }
                } finally {
                    if (getConnection != null) {
                        getConnection.disconnect();
                    }
                }
            }).start();
        }

        testLocker.lock();
    }

    /**
     * 批量文件下载
     */
    @Test public void testBatchSmallGetNotCosSdkOkhttp() {
        int count = 36;
        final TestLocker testLocker = new TestLocker(count);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final AtomicInteger successCount = new AtomicInteger(0);

        OkHttpClient client = new OkHttpClient();
        Dispatcher dispatcher = client.dispatcher();
        dispatcher.setMaxRequests(64); // 设置最大并发请求数
        dispatcher.setMaxRequestsPerHost(36); // 设置每个主机的最大并发请求数

        for (int i = 0; i < count; i ++) {
            int finalI1 = i;
            String urlString = String.format("https://mobile-ut-1253960454.cos.ap-guangzhou.myqcloud.com/small_images/%s.jpg", i + 1);
            print("start_" + finalI1);
            long startTime = System.nanoTime();
            Request request = new Request.Builder()
                    .url(urlString)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    errorCount.addAndGet(1);
                    if (successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    print("end_" + finalI1);
                    print("took_time_" + finalI1 + "_" + toSeconds(System.nanoTime() - startTime));
                    successCount.addAndGet(1);
                    if (successCount.get() + errorCount.get() == count) {
                        testLocker.release();
                    }
                }
            });
        }
        testLocker.lock();
    }

    /**
     * 批量文件下载取消
     */
    @Test public void testBatchSmallDownloadCancel() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        int count = 36;
        final TestLocker testLocker = new TestLocker(count);
        List<COSXMLDownloadTask> taskList = new ArrayList<>();

        TestUtils.print("第一批开始");
        for (int i = 0; i < count; i ++) {
            final String localName = "1_" + i + ".txt";
            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    TestUtils.localParentPath(), localName);
            taskList.add(downloadTask);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    TestUtils.print("onSuccess1_"+((COSXMLDownloadTask.COSXMLDownloadTaskRequest)request).getDownloadPath());
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    TestUtils.print("onFail1_"+((COSXMLDownloadTask.COSXMLDownloadTaskRequest)request).getDownloadPath());
                }
            });
            TestUtils.print("TaskPoolCount1_"+i+"_"+TaskManager.getInstance().getPoolCount());
//            downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
//                @Override
//                public void onProgress(long complete, long target) {
//                    TestUtils.print(localName+ " download_Progress" + ": " + complete + "/" + target);
//                }
//            });
//            downloadTask.setTransferStateListener(new TransferStateListener() {
//                @Override
//                public void onStateChanged(TransferState state) {
//                    TestUtils.print(localName+ " download_State:" + state.name());
//                }
//            });
        }

        TestUtils.sleep(3000);
        TestUtils.print("第一批取消");
        for (COSXMLDownloadTask task : taskList){
            task.cancel(true);
        }

        TestUtils.print("第二批开始");
        for (int i = 100; i < 136; i ++) {
            final String localName = "2_" + i + ".txt";
            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                    TestUtils.localParentPath(), localName);
//            taskList.add(downloadTask);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    TestUtils.print("onSuccess2_"+((COSXMLDownloadTask.COSXMLDownloadTaskRequest)request).getDownloadPath());
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    TestUtils.print("onFail2_"+((COSXMLDownloadTask.COSXMLDownloadTaskRequest)request).getDownloadPath());
                }
            });
            TestUtils.print("TaskPoolCount2_"+i+"_"+TaskManager.getInstance().getPoolCount());
        }

        testLocker.lock();
    }

    /**
     * 测试续传
     *
     * 下载 2s 后点击暂停，并记录当前的下载进度，等待 1s 后重新下载，进度必须大于之前记录的进度
     */
    @Test public void testContinueDownload() {
        TestUtils.sleep(10000);
        String localFileName = TestUtils.extractName(TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH);

        TestUtils.removeLocalFile(TestUtils.localPath(localFileName));

        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath(), localFileName);

        TestUtils.sleep(4000);
        downloadTask.pause();
        TestUtils.sleep(4000);
//        final StringBuilder errorMessage = new StringBuilder();

        final COSXMLDownloadTask continueTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
                TestUtils.localParentPath(), localFileName);

        final TestLocker testLocker = new TestLocker();

        final long currentProgress = new File(TestUtils.localParentPath(), localFileName).length();
//        continueTask.setCosXmlProgressListener(new CosXmlProgressListener() {
//            @Override
//            public void onProgress(long complete, long target) {
//
//                if (complete < currentProgress) {
//                    errorMessage.append("continue complete is " + complete + ", but current progress is " + currentProgress);
//                    testLocker.release();
//                }
//            }
//        });

        continueTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
//        TestUtils.assertErrorMessageNull(errorMessage);
        TestUtils.sleep(10000);
        TestUtils.assertCOSXMLTaskSuccess(continueTask);
    }

//    @Test public void testContinueDownloadExisted() {
//        String localFileName = TestUtils.big60mFilePath();
//        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
//        final COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
//                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
//                localFileName);
//
//        TestUtils.sleep(4000);
//        downloadTask.pause();
//        TestUtils.sleep(4000);
//
//        TestUtils.big60mFilePath();
//        final COSXMLDownloadTask continueTask = transferManager.download(TestUtils.getContext(),
//                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH,
//                localFileName);
//
//        final TestLocker testLocker = new TestLocker();
//
//        continueTask.setCosXmlResultListener(new CosXmlResultListener() {
//            @Override
//            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                testLocker.release();
//            }
//
//            @Override
//            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
//                TestUtils.printError(TestUtils.getCosExceptionMessage(exception, serviceException));
//                testLocker.release();
//            }
//        });
//
//        testLocker.lock();
//        TestUtils.assertCOSXMLTaskSuccess(continueTask);
//    }

    @Test public void testAnonymousDownload() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newAnonymousTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_CDN_BIG_OBJECT_PATH,
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        downloadTask.setOnGetHttpTaskMetrics(new COSXMLTask.OnGetHttpTaskMetrics() {
            @Override
            public void onGetHttpMetrics(String requestName, HttpTaskMetrics httpTaskMetrics) {
                InetAddress socketAddress = httpTaskMetrics.getConnectAddress();
                if (socketAddress != null) {
                    QCloudLogger.i(TestConst.UT_TAG, "connect ip is " + socketAddress.getHostAddress());
                }
            }
        });

        final TestLocker testLocker = new TestLocker();
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
        downloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                QCloudLogger.i("QCloudTest", "transfer state is " + state);
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testSmallDownloadSignature() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath(), "test", new MyOnSignatureListener());

        final TestLocker testLocker = new TestLocker();
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
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testSmallDownloadRange() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
         getObjectRequest.setRange(0, 10);
        getObjectRequest.addNoSignHeader("Range");
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
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
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test public void testSmallDownloadRangeFaild() {
        // 能通过head  但是get服务端非200-300
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath());
//        getObjectRequest.setIfModifiedSince("Wed, 21 Oct 2009 07:28:00 GMT");
//        getObjectRequest.setIfUnmodifiedSince("Wed, 21 Oct 2052 07:28:00 GMT");
        getObjectRequest.setIfMatch("none_match_etag");
//        getObjectRequest.setIfNONEMatch("none_match_etag");
        getObjectRequest.addNoSignHeader("Range");
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);

        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                Assert.assertTrue(true);
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test public void testDownloadClientError() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                "aaaaaaa");
        final TestLocker testLocker = new TestLocker();
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(clientException != null){
                    Assert.assertTrue(true);
                } else {
                    Assert.fail();
                }
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test public void testSmallDownloadToUri() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                Uri.fromFile(new File(smallFilePath())));
        Assert.assertNotNull(getObjectRequest.getFileContentUri());
        try {
            cosXmlSimpleService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            Assert.fail(e.getMessage());
            return;
        } catch (CosXmlServiceException e) {
            Assert.fail(e.getMessage());
            return;
        }
        Assert.assertTrue(true);
    }
}
