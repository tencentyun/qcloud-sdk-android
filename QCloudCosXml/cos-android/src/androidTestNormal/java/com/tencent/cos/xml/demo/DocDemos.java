package com.tencent.cos.xml.demo;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.bucket.GetBucketRequest;
import com.tencent.cos.xml.model.bucket.GetBucketResult;
import com.tencent.cos.xml.model.object.DeleteMultiObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.tag.ListBucket;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * <p>
 * Created by rickenwang on 2021/4/27.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class DocDemos {


    private CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();

    @Test public void testCreateDirectory() {
        createDirectory(TestConst.PERSIST_BUCKET);
    }

    @Test public void testDeleteDirectory() {
        deleteDirectory(TestConst.PERSIST_BUCKET);
    }

    /**
     * 创建目录
     */
    public void createDirectory(String bucket) {

        // 在 parent 目录下创建一个 directory 目录，需要以 / 结尾
        String directory = "parent/directory/";
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, directory, new byte[]{});
        cosXmlService.putObjectAsync(putObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // 创建成功
                PutObjectResult putObjectResult = (PutObjectResult) result;
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                // 创建失败，打印异常信息
                if (clientException != null) {
                    clientException.printStackTrace();
                } else {
                    serviceException.printStackTrace();
                }
            }
        });
    }

    public void deleteDirectory(String bucket) {

        // 在 parent 目录下创建一个 directory 目录，需要以 / 结尾
        String directory = "parent/directory/";

        GetBucketRequest getBucketRequest = new GetBucketRequest(bucket);
        getBucketRequest.setPrefix(directory);

        // prefix表示要删除的文件夹
        getBucketRequest.setPrefix(directory);
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        getBucketRequest.setMaxKeys(2);
        GetBucketResult getBucketResult = null;

        do {

            try {
                getBucketResult = cosXmlService.getBucket(getBucketRequest);
                List<ListBucket.Contents> contents = getBucketResult.listBucket.contentsList;
                DeleteMultiObjectRequest deleteMultiObjectRequest = new DeleteMultiObjectRequest(bucket);
                for (ListBucket.Contents content : contents) {
                    deleteMultiObjectRequest.setObjectList(content.key);
                }
                cosXmlService.deleteMultiObject(deleteMultiObjectRequest);
                getBucketRequest.setMarker(getBucketResult.listBucket.nextMarker);
            } catch (CosXmlClientException e) {
                e.printStackTrace();
                return;
            } catch (CosXmlServiceException e) {
                e.printStackTrace();
                return;
            }
        } while (getBucketResult.listBucket.isTruncated);
    }

}
