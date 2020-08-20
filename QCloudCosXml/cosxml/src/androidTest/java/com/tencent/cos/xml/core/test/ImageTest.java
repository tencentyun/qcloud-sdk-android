package com.tencent.cos.xml.core.test;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConfigs;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PostObjectRequest;
import com.tencent.cos.xml.model.object.PostObjectResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.tag.pic.PicOperationRule;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Created by rickenwang on 2020/8/14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class ImageTest {


    @Test public void testCloudImageProcess() {

        final TestLocker testLocker = new TestLocker(1);

        QCloudLogger.i("QCloud", "start test");
        CosXmlService cosXmlService = TestUtils.newDefaultTerminalService();
        PostObjectRequest postObjectRequest = new PostObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_PIC_PATH,
                new byte[]{1});

        List< PicOperationRule > rules = new LinkedList<>();
        rules.add(new PicOperationRule("ut_files/test.jpg", "imageView2/format/jpg"));
        PicOperations picOperations = new PicOperations(false, rules);

        postObjectRequest.setHeader("Pic-Operations", picOperations.toJsonStr());

        cosXmlService.postObjectAsync(postObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i("QCloud", "onSuccess");
                PostObjectResult postObjectResult = (PostObjectResult) result;
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                QCloudLogger.i("QCloud", "onFail");
                testLocker.release();
            }
        });

        testLocker.lock();
    }
}
