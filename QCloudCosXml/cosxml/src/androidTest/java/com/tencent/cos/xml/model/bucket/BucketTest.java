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

package com.tencent.cos.xml.model.bucket;

import android.content.Context;

import com.tencent.cos.xml.core.TestUtils;;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlBooleanListener;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
public class BucketTest {


    @BeforeClass
    public static void init() {

        Context appContext = TestUtils.getContext();
        QServer.init(appContext);
    }

    @Test public void doesBucketExistTest() throws  Exception{
        Assert.assertTrue(QServer.cosXml.doesBucketExist(QServer.persistBucket));
        Assert.assertFalse(QServer.cosXml.doesBucketExist("android-demo-ap-guangzhou-not-exist"));
    }

    @Test
    public void doesBucketExistAsyncTest() {

        final CountDownLatch countDownLatch = new CountDownLatch(2);


        QServer.cosXml.doesBucketExistAsync(QServer.persistBucket, new CosXmlBooleanListener() {
            @Override
            public void onSuccess(boolean bool) {

                Assert.assertTrue(bool);
                countDownLatch.countDown();
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {

                Assert.assertFalse(false);
                countDownLatch.countDown();
            }
        });


        QServer.cosXml.doesBucketExistAsync("android-demo-ap-guangzhou_not_exist", new CosXmlBooleanListener() {
            @Override
            public void onSuccess(boolean bool) {


                Assert.assertTrue(bool);
                countDownLatch.countDown();
            }

            @Override
            public void onFail(CosXmlClientException exception, CosXmlServiceException serviceException) {


                Assert.assertFalse(false);
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
