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

package com.tencent.cos.xml.model.object;

import android.content.Context;
import android.os.Environment;

import com.tencent.cos.xml.core.TestUtils;;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.core.TestUtils;
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

@RunWith(AndroidJUnit4.class)
public class SelectObjectContentTest {

    private SelectObjectContentEvent lastEvent;

    @BeforeClass public static void init() {

        Context appContext = TestUtils.getContext();
        QServer.init(appContext);
    }

    @Test public void selectObjectContentTest() throws Exception {

        if (QServer.isForeign()) { //
            Assert.assertTrue(true);
            return;
        }

        Context context = TestUtils.getContext();

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
