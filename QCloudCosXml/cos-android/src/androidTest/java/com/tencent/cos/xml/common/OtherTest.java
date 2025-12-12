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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.os.Environment;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultSimpleListener;
import com.tencent.cos.xml.model.PresignedUrlRequest;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.util.Base64Utils;
import com.tencent.qcloud.core.util.DomainSwitchUtils;
import com.tencent.qcloud.core.util.OkhttpInternalUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * <p>
 * Created by jordanqin on 2020/12/29.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class OtherTest {
    @Test
    public void testServerEncryptType(){
        assertEquals("SSE-C", ServerEncryptType.SSE_C.getType());
        assertEquals(ServerEncryptType.SSE_COS, ServerEncryptType.fromString("SSE-COS"));
        Assert.assertNull(ServerEncryptType.fromString("test"));
    }

    @Test
    public void testClientErrorCode(){
        assertEquals(ClientErrorCode.to(-10000), ClientErrorCode.UNKNOWN);
        ClientErrorCode clientErrorCode = ClientErrorCode.UNKNOWN;
        clientErrorCode.setErrorMsg("UNKNOWN_TEST");
        assertEquals("UNKNOWN_TEST", clientErrorCode.getErrorMsg());
    }

    @Test
    public void testPreBuildConnection() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        boolean b = cosXmlSimpleService.preBuildConnection(TestConst.PERSIST_BUCKET);
        Assert.assertTrue(b);
    }

    @Test
    public void testPreBuildConnectionFailed() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        boolean b = cosXmlSimpleService.preBuildConnection(TestConst.PERSIST_BUCKET+"aaaaaaa");
        Assert.assertFalse(b);
    }

    @Test
    public void testPreBuildConnectionAsync() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        final TestLocker testLocker = new TestLocker();
        cosXmlSimpleService.preBuildConnectionAsync(TestConst.PERSIST_BUCKET, new CosXmlResultSimpleListener() {
            @Override
            public void onSuccess() {
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(exception, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test
    public void testPreBuildConnectionAsyncFailed() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        final TestLocker testLocker = new TestLocker();
        cosXmlSimpleService.preBuildConnectionAsync(TestConst.PERSIST_BUCKET+"aaaaa", new CosXmlResultSimpleListener() {
            @Override
            public void onSuccess() {
                Assert.fail();
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.assertTrue(true);
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test public void testCRC64() {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "12345.zip");
        QCloudLogger.i(TestConst.UT_TAG, "file path is " + file.getAbsolutePath());

        BigInteger bi = new BigInteger("9668868296014390415");
        QCloudLogger.i(TestConst.UT_TAG, "remote crc64 is " + bi.longValue());

        try {
            QCloudLogger.i(TestConst.UT_TAG, "file crc64 is " + DigestUtils.getCRC64(new FileInputStream(file)));
//            QCloudLogger.i(TestConst.UT_TAG, "file crc64 is " + DigestUtils.getCRC64(new ByteArrayInputStream(
//                    "123456789".getBytes()
//            )));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void testGetObjectUrl() {

        CosXmlSimpleService defaultService = ServiceFactory.INSTANCE.newDefaultService();
        String bucket = "bucket";
        String region = "ap-shanghai";
        String key = "objectexample";
        String url = defaultService.getObjectUrl(bucket, region, key);
        QCloudLogger.i("QCloudTest", url);
        Assert.assertEquals("https://bucket.cos.ap-shanghai.myqcloud.com/objectexample", url);
    }

    public String onGetMd5(String filePath) throws IOException {
        InputStream inputStream = null;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            File file = new File(filePath);
            inputStream = new FileInputStream(file);
            byte[] buff = new byte[8 * 1024];
            int readLen;
            long remainLength = file.length();
            while (remainLength > 0L && (readLen = inputStream.read(buff, 0,
                    (buff.length > remainLength ? (int) remainLength : buff.length)))!= -1){
                messageDigest.update(buff, 0, readLen);
                remainLength -= readLen;
            }
            return Base64Utils.encode(messageDigest.digest());
        } catch (IOException e) {
            throw e;
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("unSupport Md5 algorithm", e);
        } finally {
            if(inputStream != null) OkhttpInternalUtils.closeQuietly(inputStream);
        }
    }

    @Test public void testPresignedRequest() {
        String filePath = TestUtils.smallFilePath();
        PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH) {
            @Override
            public RequestBodySerializer getRequestBody() throws CosXmlClientException {
                return RequestBodySerializer.file("image/png", new File(filePath));
            }
        };
        presignedUrlRequest.setRequestMethod("PUT");
        presignedUrlRequest.setSignKeyTime(3600);
        presignedUrlRequest.addNoSignHeader("Host");
        String md5;
//        try {
//            md5 = DigestUtils.getMD5(filePath);
//        } catch (CosXmlClientException e) {
//            throw new RuntimeException(e);
//        }

        try {
            md5 = onGetMd5(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        try {
//            presignedUrlRequest.setRequestHeaders(HttpConstants.Header.CONTENT_MD5, md5);
//        } catch (CosXmlClientException e) {
//            throw new RuntimeException(e);
//        }
        CosXmlSimpleService defaultService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            String signUrl = defaultService.getPresignedURL(presignedUrlRequest);
            QCloudLogger.i("QCloudTest", signUrl);
            MediaType imageType = MediaType.parse("image/png");
            Request request = new Request.Builder()
                    .url(signUrl)
//                    .header(HttpConstants.Header.CONTENT_MD5, md5)
                    .put(RequestBody.create(imageType, new File(TestUtils.smallFilePath())))
                    .build();
            Response response = new OkHttpClient().newCall(request).execute();
            String result = response.body().string();
            QCloudLogger.i("QCloudTest", result);
        } catch (CosXmlClientException | IOException clientException) {
            QCloudLogger.i("QCloudTest", clientException.getMessage());
            clientException.printStackTrace();
        }
    }

    public void uploadFile(String targetUrl, String filePath) {
        int retryCount = 0;
        boolean success = false;

        while (!success && retryCount < 3) {
            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            FileInputStream fileInputStream = null;

            try {
                fileInputStream = new FileInputStream(filePath);

                URL url = new URL(targetUrl);
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                connection.setRequestMethod("POST");

                outputStream = new DataOutputStream(connection.getOutputStream());
                int bytesRead;
                byte[] buffer = new byte[8192];
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    if (connection.getResponseCode() >= 500) {
                        retryCount++;
                        continue;
                    } else {
                        throw new RuntimeException("Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage());
                    }
                }

                success = true;
            } catch (Exception e) {
                retryCount++;
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                    if (fileInputStream != null)
                        fileInputStream.close();
                } catch (Exception ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
        }

        if (!success) {
            throw new RuntimeException("Failed to upload file after 3 attempts");
        }
    }

    @Test public void testPresignedDownload() {
        PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(TestConst.PERSIST_BUCKET, "wechat.png");
        presignedUrlRequest.setCosPath("wechat.png");
        presignedUrlRequest.setRequestMethod("GET");
        presignedUrlRequest.setSignKeyTime(3600);
        presignedUrlRequest.addQuery(URLEncoder.encode("imageMogr2/rotate/90"), null);
        // presignedUrlRequest.addNoSignHeader("Host");
        CosXmlSimpleService defaultService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            String signUrl = defaultService.getPresignedURL(presignedUrlRequest);
            Log.i("QCloudTest", signUrl);
            new Thread(() -> {
                String localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/wechat.png";
                downloadFile(signUrl, localPath);
            }).start();
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

    @Test public void testPresignedDownloadFail1() {
        PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest("", "wechat.png");
        CosXmlSimpleService defaultService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            String signUrl = defaultService.getPresignedURL(presignedUrlRequest);
            QCloudLogger.i("QCloudTest", signUrl);
        } catch (CosXmlClientException clientException) {
            Assert.assertTrue(clientException.getMessage().contains("bucket must not be null"));
        }
    }

    @Test public void testPresignedDownloadFail2() {
        PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(TestConst.PERSIST_BUCKET, "");
        CosXmlSimpleService defaultService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            String signUrl = defaultService.getPresignedURL(presignedUrlRequest);
            QCloudLogger.i("QCloudTest", signUrl);
        } catch (CosXmlClientException clientException) {
            Assert.assertTrue(clientException.getMessage().contains("cosPath must not be null"));
        }
    }


    @Test public void testDownload() {
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                "wechat.png",
                TestUtils.localParentPath());
        getObjectRequest.addQuery("imageMogr2/rotate/90", null);
        CosXmlSimpleService defaultService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            defaultService.getObject(getObjectRequest);
        } catch (CosXmlClientException clientException) {
            clientException.printStackTrace();
        } catch (CosXmlServiceException serviceException) {
            serviceException.printStackTrace();
        }

    }

    @Test public void testSessionCredentialsExpired() {
        CosXmlSimpleService service = ServiceFactory.INSTANCE.newDefaultServiceBySessionCredentials();
        PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.smallFilePath());
//        GetObjectRequest request = new GetObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH, TestUtils.localParentPath(), "test");
        request.addNoSignHeader("Content-Length");
        request.addNoSignHeader("User-Agent");
        request.addNoSignHeader("Host");
        request.addNoSignHeader("Content-MD5");
        request.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                TestUtils.print("Progress test" + ": " + complete + "/" + target);
            }
        });
        for (int i=0; i<5;i++){
            try {
                service.putObject(request);
//                service.getObject(request);
            } catch (CosXmlClientException e) {
                e.printStackTrace();
                break;
            } catch (CosXmlServiceException e) {
                e.printStackTrace();
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertTrue(true);
    }

//    @Test
//    public void testMultiThreadedCosXmlService() {
//        try {
//            // 线程数常量
//            int  THREAD_COUNT = 10000;
//            Thread[] threads = new Thread[THREAD_COUNT];
//            ArrayList<Throwable> exceptions = new ArrayList<>();
//            for (int i = 0; i < THREAD_COUNT; i++) {
//                threads[i] = new Thread(() -> {
//                    CosXmlSimpleService service = ServiceFactory.INSTANCE.newDefaultService();
//                    service.getConfig();
//                });
//                threads[i].setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//                    @Override
//                    public void uncaughtException(Thread t, Throwable e) {
//                        e.printStackTrace();
//                        exceptions.add(e);
//                    }
//                });
//                threads[i].start();
//            }
//            for (int i = 0; i < THREAD_COUNT; i++) {
//                threads[i].join();
//            }
//            assertTrue(exceptions.isEmpty());
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail(e.getMessage());
//        }
//    }
}