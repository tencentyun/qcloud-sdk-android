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

package com.tencent.cos.xml.common;

import android.os.Environment;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

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
import com.tencent.cos.xml.model.PresignedUrlRequest;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * <p>
 * Created by jordanqin on 2020/12/29.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class SpecialCharactersTest {
    private String bucket = "0-a-1253960454";
    private String cosKey = "sentences/Cambridge_IELTS_Series/0+Cambridge A16~IETLS_A16_T1_Part_2.mp3";

    private CosXmlSimpleService getCosXmlService() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setConnectionTimeout(4000)
                .setSocketTimeout(4000)
                .setTransferThreadControl(false)
                .setUploadMaxThreadCount(10)
                .setDownloadMaxThreadCount(36)
                .setRegion("ap-nanjing")
                .builder();
        return ServiceFactory.INSTANCE.newService(cosXmlServiceConfig);
    }

    private TransferManager getTransferManager() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .setVerifyCRC64(true)
                .setSliceSizeForCopy(5242880)
                .setDividsionForCopy(5242880)
                .build();
        Log.d(TestConst.UT_TAG, String.valueOf(transferConfig.getDivisionForCopy()));
        return new TransferManager(getCosXmlService(), transferConfig);
    }

    @Test public void testPresignedDownload() {
        PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(bucket, cosKey);
        presignedUrlRequest.setRequestMethod("GET");
        presignedUrlRequest.setSignKeyTime(3600);
         presignedUrlRequest.addNoSignHeader("Host");
        try {
            String signUrl = getCosXmlService().getPresignedURL(presignedUrlRequest);
            Log.i("QCloudTest", signUrl);
            new Thread(() -> {
                String localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/wechat.png";
                downloadFile(signUrl, localPath);
            }).start();
            TestUtils.sleep(5000);
        } catch (CosXmlClientException clientException) {
            QCloudLogger.i("QCloudTest", clientException.getMessage());
            clientException.printStackTrace();
        }
    }
    @Test public void testPresignedDownload1() {
        PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(bucket, "test.avif");
        presignedUrlRequest.setRequestMethod("GET");
        presignedUrlRequest.setSignKeyTime(3600);
        presignedUrlRequest.addNoSignHeader("Host");
        try {
            String signUrl = getCosXmlService().getPresignedURL(presignedUrlRequest);
            Log.i("QCloudTest", signUrl);
            new Thread(() -> {
                String localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/wechat.png";
                downloadFile(signUrl, localPath);
            }).start();
            TestUtils.sleep(5000);
        } catch (CosXmlClientException clientException) {
            QCloudLogger.i("QCloudTest", clientException.getMessage());
            clientException.printStackTrace();
        }
    }
    public void downloadFile(String fileUrl, String localPath) {
        int retryCount = 0;
        boolean success = false;
        while (!success && retryCount < 3) {
            HttpURLConnection connection = null;
            InputStream input = null;
            FileOutputStream output = null;
            try {
                URL url = new URL(fileUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    if (connection.getResponseCode() >= 500) {
                        retryCount++;
                        continue;
                    } else {
                        throw new RuntimeException("Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage());
                    }
                }
                input = connection.getInputStream();
                output = new FileOutputStream(localPath);
                byte data[] = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
                success = true;
            } catch (Exception e) {
                retryCount++;
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (Exception ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
        }
        if (!success) {
            throw new RuntimeException("Failed to download file after 3 attempts");
        }
    }

    @Test public void testSmallDownload() {
        TransferManager transferManager = getTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket,
                cosKey,
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                File file = new File(getObjectRequest.getDownloadPath());
                TestUtils.print("download file size:"+file.length());
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
}