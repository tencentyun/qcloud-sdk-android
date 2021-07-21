package com.tencent.cos.xml.model.object;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.model.RequestTestAdapter;

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
            new PostObjectTestAdapter.PostObjectByteTestAdapter(),
            new PostObjectTestAdapter.PostObjectSrcPathTestAdapter(),
            new PostObjectTestAdapter.PostObjectStreamTestAdapter(),

            new PutObjectTestAdapter.PutObjectSrcPathTestAdapter(),
            new PutObjectTestAdapter.PutObjectByteTestAdapter(),
            new PutObjectTestAdapter.PutObjectSbTestAdapter(),
            new PutObjectTestAdapter.PutObjectInputStreamTestAdapter(),
            new PutObjectTestAdapter.PutObjectUrlTestAdapter(),

            new GetObjectTestAdapter(),
            new PutObjectDeepArchiveTestAdapter(),
            new HeadObjectTestAdapter(),
            new CopyObjectTestAdapter(),
            new DeleteObjectTestAdapter(),

            // 上传小文件供之后使用
            new PutObjectTestAdapter.PutObjectSrcPathTestAdapter(),
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

    @Test public void testGetSnapshot() {


    }
}
