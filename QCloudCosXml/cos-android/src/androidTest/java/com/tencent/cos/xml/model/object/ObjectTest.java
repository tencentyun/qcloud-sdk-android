package com.tencent.cos.xml.model.object;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.model.RequestTestAdapter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

/**
 *
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class ObjectTest {
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    /**
     * 简单测试
     */
    private RequestTestAdapter[] simpleTestAdapters = new RequestTestAdapter[] {
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

    @Before
    public void setCollector(){
        for (RequestTestAdapter adapter : simpleTestAdapters) {
            adapter.setCollector(collector);
        }
    }

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
}
