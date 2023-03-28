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

import android.os.Environment;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;

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
    }

    @Test
    public void testGetService() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newSelfService();
        boolean b = cosXmlSimpleService.preBuildConnection(TestConst.PERSIST_BUCKET);
        Assert.assertTrue(b);
    }

    @Test
    public void testGetServiceAsync() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        cosXmlSimpleService.preBuildConnectionAsync(TestConst.PERSIST_BUCKET, new CosXmlResultSimpleListener() {
            @Override
            public void onSuccess() {
                Assert.assertTrue(true);
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(exception, serviceException));
            }
        });
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
    
    @Test public void testPresignedRequest() {

        PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH) {
            @Override
            public RequestBodySerializer getRequestBody() throws CosXmlClientException {
                return RequestBodySerializer.file("image/png", new File(TestUtils.smallFilePath()));
            }
        };
        presignedUrlRequest.setRequestMethod("PUT");
        presignedUrlRequest.setSignKeyTime(3600);
        presignedUrlRequest.addNoSignHeader("Host");
        CosXmlSimpleService defaultService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            String signUrl = defaultService.getPresignedURL(presignedUrlRequest);
            QCloudLogger.i("QCloudTest", signUrl);
            MediaType imageType = MediaType.parse("image/png");
            Request request = new Request.Builder()
                    .url(signUrl)
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

    @Test public void testPresignedDownload() {
        PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(TestConst.PERSIST_BUCKET, "wechat.png");
        presignedUrlRequest.setRequestMethod("GET");
        presignedUrlRequest.setSignKeyTime(3600);
        presignedUrlRequest.addQuery(URLEncoder.encode("imageMogr2/rotate/90"), null);
        // presignedUrlRequest.addNoSignHeader("Host");
        CosXmlSimpleService defaultService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            String signUrl = defaultService.getPresignedURL(presignedUrlRequest);
            QCloudLogger.i("QCloudTest", signUrl);
        } catch (CosXmlClientException clientException) {
            QCloudLogger.i("QCloudTest", clientException.getMessage());
            clientException.printStackTrace();
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
}