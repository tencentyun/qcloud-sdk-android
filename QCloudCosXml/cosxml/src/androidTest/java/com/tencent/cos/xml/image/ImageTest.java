package com.tencent.cos.xml.image;

import android.util.Log;

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
import com.tencent.cos.xml.transfer.COSXMLUploadTask;

import org.junit.Assert;
import org.junit.BeforeClass;
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

    final static String localImageName ="/test_image.png";

    @BeforeClass public static void downloadImageToLocalPath() {

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

    // 通过 CosXmlService#putObject() 方法简单上传，返回 PutObjectResult
    @Test public void testPicOperationRule()  {

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
            Assert.assertNotNull(uploadResult);
            // Assert.assertFalse(uploadResult.processResults.isEmpty());
            Assert.assertEquals(200, putObjectResult.httpCode);
        } catch (Exception exception) {
            Assert.fail(exception.getMessage());
        }
    }

    // 通过 TransferManager 分片上传，返回 CompleteMultiUploadResult
    @Test public void transferManagerUploadPic() {

        final TestLocker testLocker = new TestLocker();
        List<PicOperationRule> rules = new LinkedList<>();
        // 添加一条将图片转化为 png 格式的 rule，处理后的图片在存储桶中的位置标识符为
        // examplepngobject
        rules.add(new PicOperationRule("examplepngobject", "imageView2/format/png"));
        rules.add(new PicOperationRule("examplepngobject1", "imageView2/format/png"));
        PicOperations picOperations = new PicOperations(true, rules);

        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_PIC_PATH, TestUtils.localPath(localImageName));
        putObjectRequest.setPicOperations(picOperations);
        Log.i("transferManagerUploadPic", "start ");
        // 上传成功后，您将会得到 2 张图片，分别是原始图片和处理后图片
        COSXMLUploadTask cosxmlUploadTask = ServiceFactory.INSTANCE.newDefaultTransferManager().upload(putObjectRequest, "");
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.i("transferManagerUploadPic", "onSuccess ");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.i("transferManagerUploadPic", "onFail ");
                testLocker.release();
            }
        });
        Log.i("transferManagerUploadPic", "lock ");
        testLocker.lock();
        COSXMLUploadTask.COSXMLUploadTaskResult taskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) cosxmlUploadTask.getResult();
        Assert.assertNotNull(taskResult.picUploadResult);
//        Assert.assertNotNull(taskResult.picUploadResult.processResults);
//        Assert.assertFalse(taskResult.picUploadResult.processResults.isEmpty());
    }

    @Test public void testThumbnail() {

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_PIC_PATH,
                TestUtils.localParentPath());

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
