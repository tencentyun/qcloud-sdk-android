package com.tencent.cos.xml.model.object;

import androidx.test.ext.junit.runners.AndroidJUnit4;

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
            new PutObjectTestAdapter.PutObjectSrcPathTestAdapter(),
            new PutObjectTestAdapter.PutObjectByteTestAdapter(),
            new PutObjectTestAdapter.PutObjectInputStreamTestAdapter(),
            new GetObjectTestAdapter(),
            // 上传小文件供之后使用
            new PutObjectTestAdapter.PutObjectSrcPathTestAdapter(),
    };


    @Test
    public void testAsync() {
        for (RequestTestAdapter adapter : simpleTestAdapters) {
            adapter.testAsyncRequest();
        }
    }
}
