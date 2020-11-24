package com.tencent.cos.xml.model.object;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class ObjectTest {

    /**
     * 简单测试
     */
    private RequestTestAdapter[] simpleTestAdapters = new RequestTestAdapter[] {
            new PutObjectTestAdapter(),
            new GetObjectTestAdapter(),
            new PutObjectDeepArchiveTestAdapter(),
            new HeadObjectTestAdapter(),
            new OptionObjectTestAdapter(),
            new PutObjectACLTestAdapter(),
            new GetObjectACLTestAdapter(),
            new CopyObjectTestAdapter(),
            new DeleteObjectTestAdapter(),

            // 上传小文件供之后使用
            new PutObjectTestAdapter()
    };

    /**
     * 一次分片上传
     */
    private RequestTestAdapter[] multiUploadTestAdapters = new RequestTestAdapter[] {

            new InitMultipartUploadTestAdapter(),
            new UploadPartTestAdapter(),
            new CompleteMultiUploadTestAdapter()
    };


    @Test
    public void testAsync() {

        for (RequestTestAdapter adapter : simpleTestAdapters) {
            adapter.testAsyncRequest();
        }
    }


    @Test public void testSync() {

        for (RequestTestAdapter adapter : simpleTestAdapters) {
            adapter.testSyncRequest();
        }
    }

    /**
     * Test initMultipartUpload、ListParts、UploadPart、CompleteMultiUpload
     *
     * @throws CosXmlServiceException
     * @throws CosXmlClientException
     */
    private void multiUploadObject() {

        String fileName = TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH;
        String bucketName = TestConst.PERSIST_BUCKET;
        CosXmlService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();

        // 初始化分片上传
        InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucketName, fileName);
        InitMultipartUploadResult initMultipartUploadResult = null;
        try {
            initMultipartUploadResult = cosXmlService.initMultipartUpload(initMultipartUploadRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(initMultipartUploadResult);
        Assert.assertEquals(200, initMultipartUploadResult.httpCode);
        Assert.assertNotNull(initMultipartUploadResult.initMultipartUpload.uploadId);

        // 查询分片上传
        String uploadId = initMultipartUploadResult.initMultipartUpload.uploadId;
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, fileName, uploadId);
        ListPartsResult listPartsResult = null;
        try {
            listPartsResult = cosXmlService.listParts(listPartsRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(listPartsResult);
        Assert.assertEquals(200, listPartsResult.httpCode);
        Assert.assertNotNull(listPartsResult.listParts);

        // 上传分片
        int partNumber = 1;
        String localFilePath = TestUtils.bigFilePath();
        UploadPartRequest uploadPartRequest1 = new UploadPartRequest(bucketName, fileName, partNumber, localFilePath, uploadId);
        uploadPartRequest1.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d(TestConst.UT_TAG, complete + "/" + target);
            }
        });
        UploadPartResult uploadPartResult1 = null;
        try {
            uploadPartResult1 = cosXmlService.uploadPart(uploadPartRequest1);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(uploadPartResult1);
        Assert.assertEquals(200, uploadPartResult1.httpCode);
        Assert.assertNotNull(uploadPartResult1.eTag);

        // 完成分片上传
        CompleteMultiUploadRequest completeMultiUploadRequest = new CompleteMultiUploadRequest(bucketName, fileName, uploadId, null);
        completeMultiUploadRequest.setPartNumberAndETag(1, uploadPartResult1.eTag);
        CompleteMultiUploadResult completeMultiUploadResult = null;
        try {
            completeMultiUploadResult = cosXmlService.completeMultiUpload(completeMultiUploadRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(completeMultiUploadResult);
        Assert.assertEquals(completeMultiUploadResult.httpCode, 200);
        Assert.assertNotNull(completeMultiUploadResult.completeMultipartUpload);
    }

}
