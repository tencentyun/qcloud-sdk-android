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

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultSimpleListener;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

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
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
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
}