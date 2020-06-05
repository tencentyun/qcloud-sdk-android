package com.tencent.cos.xml.model.object;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.listener.SelectObjectContentListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.eventstreaming.CompressionType;
import com.tencent.cos.xml.model.tag.eventstreaming.InputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONInput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONOutput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONType;
import com.tencent.cos.xml.model.tag.eventstreaming.OutputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectObjectContentEvent;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

@RunWith(AndroidJUnit4.class)
public class SelectObjectContentTest {

    private SelectObjectContentEvent lastEvent;

    @BeforeClass public static void init() {

        Context appContext = InstrumentationRegistry.getContext();
        QServer.init(appContext);
    }

    @Test public void selectObjectContentTest() throws Exception {

        if (QServer.isForeign()) { //
            Assert.assertTrue(true);
            return;
        }

        Context context = InstrumentationRegistry.getContext();

        String bucket = QServer.persistBucket;
        String key = "select_object_content.json";
        final String expression = "Select * from COSObject";

        SelectObjectContentRequest selectObjectContentRequest = new SelectObjectContentRequest(
                bucket, key, expression, true, new InputSerialization(CompressionType.NONE,
                new JSONInput(JSONType.DOCUMENT)),
                new OutputSerialization(new JSONOutput())
        );

        String localPath = new File(Environment.getExternalStorageDirectory(), "select.json").getAbsolutePath();

        selectObjectContentRequest.setSelectResponseFilePath(localPath);

        selectObjectContentRequest.setSelectObjectContentProgressListener(new SelectObjectContentListener() {
            @Override
            public void onProcess(SelectObjectContentEvent event) {
                // event.toString();
                System.out.println("SelectObjectContentEvent type " + event.getClass().getSimpleName());
                lastEvent = event;
            }
        });

        try {
            SelectObjectContentResult selectObjectContentResult = QServer.cosXml.selectObjectContent(selectObjectContentRequest);
            Assert.assertTrue(lastEvent instanceof SelectObjectContentEvent.EndEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test public void selectObjectContentAsyncTest() throws Exception {

        if (QServer.isForeign()) { //
            Assert.assertTrue(true);
            return;
        }


        String bucket = QServer.persistBucket;
        String key = "select_object_content.json";
        final String expression = "Select * from COSObject";

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        SelectObjectContentRequest selectObjectContentRequest = new SelectObjectContentRequest(
                bucket, key, expression, true, new InputSerialization(CompressionType.NONE,
                new JSONInput(JSONType.DOCUMENT)),
                new OutputSerialization(new JSONOutput(","))
        );

        selectObjectContentRequest.setSelectObjectContentProgressListener(new SelectObjectContentListener() {
            @Override
            public void onProcess(SelectObjectContentEvent event) {
                 lastEvent = event;
            }
        });

        QServer.cosXml.selectObjectContentAsync(selectObjectContentRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.e("TAG", result.toString());
                countDownLatch.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
        Assert.assertTrue(lastEvent instanceof SelectObjectContentEvent.EndEvent);
    }

}
