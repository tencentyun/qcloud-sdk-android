package com.tencent.cos.xml.transfer;

import static com.tencent.cos.xml.core.TestUtils.getContext;
import static com.tencent.cos.xml.core.TestUtils.print;

import android.util.Log;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.task.TaskManager;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>
 * Created by jordanqin on 2023/10/31 18:14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class BatchDownloadTest {
    @Test
    public void testBatchUploadBigFileByPath() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        Random random = new Random();
        for (int i=0;i<60;i++) {
            String localPath = TestUtils.localPath("1642166999131.m4a");
            try {
                // 生成一个在30720到61440之间的随机数(30M-60M)
                int randomSize = 30720 + random.nextInt(61440 - 30720 + 1);
                TestUtils.print("randomSize = "+randomSize*1024);
                TestUtils.createFile(localPath, randomSize*1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                cosXmlSimpleService.putObject(new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH+i,
                        localPath));
            } catch (CosXmlClientException e) {
                e.printStackTrace();
            } catch (CosXmlServiceException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 批量文件下载
     */
    @Test
    public void testBatchSmallDownload() {
        int count = 2000;
        final TestLocker testLocker = new TestLocker(count);
        final AtomicInteger errorCount = new AtomicInteger(0);
        final AtomicInteger successCount = new AtomicInteger(0);
        final StringBuilder errorMessage = new StringBuilder();

        for (int i = 0; i < count; i ++) {
            CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                    .isHttps(true)
                    .setDebuggable(true)
                    .setConnectionTimeout(4000)
                    .setSocketTimeout(30000)
                    .setTransferThreadControl(false)
                    .setUploadMaxThreadCount(10)
                    .setDownloadMaxThreadCount(24)
                    .setRegion(TestConst.PERSIST_BUCKET_REGION)
                    .builder();
            CosXmlSimpleService cosXmlSimpleService = new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                    new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,60000) );
            TransferConfig transferConfig = new TransferConfig.Builder()
                    .setDivisionForUpload(2 * 1024 * 1024)
                    .setSliceSizeForUpload(1024 * 1024)
//                .setVerifyCRC64(true)
                    .setSliceSizeForCopy(5242880)
                    .setDividsionForCopy(5242880)
                    .build();
            TransferManager transferManager = new TransferManager(cosXmlSimpleService, transferConfig);

            final String localName = "30-60M_" + i + ".txt";
            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH+i%60,
                    TestUtils.localParentPath(), localName);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    successCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
//                        testLocker.release();
                    }
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    errorMessage.append(TestUtils.getCosExceptionMessage(clientException, serviceException)).append("/r/n");
                    errorCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count) {
//                        testLocker.release();
                    }
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
            TestUtils.print("TaskPoolCount1_"+i+"_"+ TaskManager.getInstance().getPoolCount());
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
     * 测试频繁暂停对下载的影响
     */
    @Test
    public void testBatchPauseDownload() {
        int count = 500;
        final TestLocker testLocker = new TestLocker(count);
        List<COSXMLDownloadTask> downloadTasks = new ArrayList<>();
        for (int i = 0; i < count; i ++) {
            final String localName = "1M_" + i + ".txt";
            TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                    TestUtils.localParentPath(), localName);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    TestUtils.print("pause task onSuccess");
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    TestUtils.print("pause task onFail");
                }
            });
            downloadTasks.add(downloadTask);
        }

        // 执行上述开始 暂停60s
        TestUtils.sleep(60000);
        TestUtils.print("pause task pause " + TaskManager.getInstance().getPoolCount());
        for (COSXMLDownloadTask downloadTask : downloadTasks) {
            downloadTask.pause(true);
        }
        testLocker.lock(3000);
        TestUtils.print("pause task pause " + TaskManager.getInstance().getPoolCount());

        // 开始正常下载
        int count1 = 10;
        final AtomicInteger errorCount = new AtomicInteger(0);
        final AtomicInteger successCount = new AtomicInteger(0);
        final StringBuilder errorMessage = new StringBuilder();
        TestUtils.print("download task strart");
        for (int i = 0; i < count1; i ++) {
            final String localName = "1M_" + i + ".txt";
            TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();;
            COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                    TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                    TestUtils.localParentPath(), localName);

            downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    TestUtils.print("download task onSuccess");
                    successCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count1) {
                        TestUtils.print("download task release onSuccess");
                        testLocker.release();
                    }
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    TestUtils.print("download task onFail");
                    TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    errorMessage.append(TestUtils.getCosExceptionMessage(clientException, serviceException)).append("/r/n");
                    errorCount.addAndGet(1);
                    if(successCount.get() + errorCount.get() == count1) {
                        TestUtils.print("download task release onFail");
                        testLocker.release();
                    }
                }
            });
        }

        testLocker.lock();
        TestUtils.assertErrorMessageNull(errorMessage);
    }
}
