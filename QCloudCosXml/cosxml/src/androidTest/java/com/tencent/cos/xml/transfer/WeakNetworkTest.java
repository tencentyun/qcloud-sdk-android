package com.tencent.cos.xml.transfer;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.cos.xml.BuildConfig;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
public class WeakNetworkTest {

    String error = null;

    private CosXmlSimpleService cosXmlSimpleService;

    Context context;

    private final String tag = "QCloudHttp";

    @Before
    public void setUp() {

        context = InstrumentationRegistry.getContext();

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig
                .Builder()
                .isHttps(true)
                .setConnectionTimeout(3000)
                .setSocketTimeout(3000)
                .setAppidAndRegion(BuildConfig.COS_APPID, BuildConfig.REGION)
                .builder();
        cosXmlSimpleService = new CosXmlSimpleService(context, cosXmlServiceConfig,
                new ShortTimeCredentialProvider(BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, 600));

    }


    private String errorMessage(Exception e) {

        String errorMessage = e.getMessage();
        if (TextUtils.isEmpty(errorMessage)) {
            return "unknown";
        }
        if (errorMessage.contains("SocketTimeoutException")) {
            return "SocketTimeoutException";
        } else if (errorMessage.contains("ConnectException")) {
            return "ConnectException";
        } else if (errorMessage.contains("UnknownHostException")) {
            return "UnknownHostException";
        } else if (errorMessage.contains("Connection reset")) { // 代理错误
            return "ConnectionReset";
        } else {
            return errorMessage;
        }
    }

    @Test public void testWeakNetwork() {

        List<Integer> fileSizes = new LinkedList<>();
        //fileSizes.add(4);
        //fileSizes.add(100);
        fileSizes.add(200);

        List<Integer> sliceSizes = new LinkedList<>();
        sliceSizes.add(1);
        //sliceSizes.add(2);
       //sliceSizes.add(4);

        List<Integer> testCounts = new LinkedList<>();
        testCounts.add(200);

        for (int fileSize = 0; fileSize < fileSizes.size(); fileSize++) {
            for (int sliceSize = 0; sliceSize < sliceSizes.size(); sliceSize++) {
                for (int testCount = 0; testCount < testCounts.size(); testCount++) {
                    testUploadFile(fileSizes.get(fileSize), sliceSizes.get(sliceSize), testCounts.get(testCount));
                }
            }

        }
    }

    private void testUploadFile(int fileSize, int sliceSize, int testCount) {

        File uploadFile = createTempFile(fileSize, "upload-test-" + fileSize + "M.txt");

        System.out.println("file path is " + uploadFile.getAbsolutePath());

        int successCount = 0;
        long startTime = System.currentTimeMillis();
        int retryCount = 0;
        File logFile = createTempFile(0, "test-" + uploadFile.getName() + "-" + startTime + ".log");

        StringBuilder logs = new StringBuilder("start test " + uploadFile.getName() + " with slice size " + sliceSize  + " M, file count is " + testCount + "\r\n");

        for (int i = 0; i < testCount; i++) {
            long startIndexTime = System.currentTimeMillis();
            String uploadError = upload(uploadFile, sliceSize * 1024 * 1024);
            boolean success = TextUtils.isEmpty(uploadError);

            if (success) {
                logs.insert(0, String.format(Locale.ENGLISH, "upload %d complete: cost time %.2f s, success\r\n", i+1, 1.0 * (System.currentTimeMillis() - startIndexTime) / 100));
                successCount++;
            } else {
                logs.insert(0, String.format(Locale.ENGLISH, "upload %d complete: cost time %.2f s, failed %s\r\n",
                        i+1, 1.0 * (System.currentTimeMillis() - startIndexTime) / 100, uploadError));
            }

            Log.i(tag, "test upload file index of " + i + ", status " + success);
        }

        logs.insert(0, String.format(Locale.ENGLISH, "upload %d files, success %d, success rate %.2f\r\n", testCount, successCount, 1.0 * successCount / testCount));
        logs.insert(0, String.format(Locale.ENGLISH, "retry count is %d, %.2f counts per upload\r\n", retryCount, 1.0 * retryCount / testCount));
        long timeCount = System.currentTimeMillis() - startTime;
        logs.insert(0, String.format(Locale.ENGLISH, "total time %.2f s, %.2f per upload\r\n", 0.01 * timeCount , 0.01 * timeCount / testCount));

        appendFile(logFile, logs.toString());
    }

    private String upload(File file, int sliceSize) {

        TransferManager transferManager = new TransferManager(cosXmlSimpleService, new TransferConfig.Builder()
                .setSliceSizeForUpload(sliceSize)
                .build());
        COSXMLUploadTask uploadTask = transferManager.upload(BuildConfig.BUCKET_PERSIST, file.getName(), file.getAbsolutePath(), null);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                error = "";
                countDownLatch.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                error = errorMessage(exception != null ? exception : serviceException);
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return error;
    }




    private void appendFile(File logFile, String line) {

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(logFile);
            fileOutputStream.write(line.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建临时文件，大小为 M
     * @param fileSize
     * @param fileName
     * @return
     */
    private File createTempFile(int fileSize, String fileName) {

        File parentDir = new File(Environment.getExternalStorageDirectory(), "weakNetwork/");
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

        File file = new File(parentDir, fileName);
        if (file.exists() && file.length() == fileSize) {
            return file;
        }

        FileOutputStream fileOutputStream = null;
        try {
            file.delete();
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            for (int i = 0; i < fileSize * 16; i++) {
                fileOutputStream.write(new byte[1024 * 64]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;
    }



}
