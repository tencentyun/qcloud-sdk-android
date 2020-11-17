package com.tencent.cos.xml.image;

import android.os.Environment;

import androidx.test.ext.junit.runners.AndroidJUnit4;

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
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.tag.pic.PicOperationRule;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.cos.xml.model.tag.pic.PicUploadResult;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Created by rickenwang on 2020/8/14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class ImageTest {

    final String localImageName ="/test_image.png";

    private void downloadImageToLocalPath() {

        TestUtils.removeLocalFile(TestUtils.localPath(localImageName));
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_PIC_PATH, TestUtils.localParentPath(), localImageName);

        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            cosXmlSimpleService.getObject(getObjectRequest);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test public void testPicOperationRule()  {

        downloadImageToLocalPath();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_PIC_PATH, TestUtils.localPath(localImageName));
        try {
            putObjectRequest.setRequestHeaders("Content-Type", "image/png", false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            Assert.fail();
        }

        List<PicOperationRule> rules = new LinkedList<>();
        rules.add(new PicOperationRule("/test.png", "imageView2/format/png"));
        PicOperations picOperations = new PicOperations(false, rules);
        putObjectRequest.setPicOperations(picOperations);

        PutObjectResult putObjectResult = null;
        try {
            putObjectResult = ServiceFactory.INSTANCE.newDefaultService().putObject(putObjectRequest);
            PicUploadResult uploadResult = putObjectResult.picUploadResult();
            Assert.assertEquals(200, putObjectResult.httpCode);
        } catch (Exception exception) {
            Assert.fail(exception.getMessage());
        }
    }

    @Test public void testThumbnail() {

        final TestLocker testLocker = new TestLocker(1);
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_PIC_PATH,
                TestUtils.localParentPath(), "pic2.png");

        Map<String, String> paras = new HashMap<>();
        paras.put("imageMogr2/thumbnail/!50p", null);
        getObjectRequest.setQueryParameters(paras);

        GetObjectResult getObjectResult = null;
        try {
            getObjectResult = ServiceFactory.INSTANCE.newDefaultService().getObject(getObjectRequest);
            Assert.assertEquals(200, getObjectResult.httpCode);
        } catch (Exception exception) {
            Assert.fail(exception.getMessage());
        }

    }
}
