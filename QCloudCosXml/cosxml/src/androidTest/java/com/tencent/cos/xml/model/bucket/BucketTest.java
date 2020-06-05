package com.tencent.cos.xml.model.bucket;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlBooleanListener;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

@RunWith(AndroidJUnit4.class)
public class BucketTest {


    @BeforeClass
    public static void init() {

        Context appContext = InstrumentationRegistry.getContext();
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
