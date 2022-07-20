package com.tencent.cos.xml.model.object;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.bucket.PutBucketCORSTestAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class NormalObjectTest {

    /**
     * 简单测试
     */
    private NormalRequestTestAdapter[] simpleTestAdapters = new NormalRequestTestAdapter[] {
            new PutBucketCORSTestAdapter(), new OptionObjectTestAdapter(),
            new PutObjectACLTestAdapter.PutObjectACL1TestAdapter(), new PutObjectACLTestAdapter.PutObjectACL2TestAdapter(), new GetObjectACLTestAdapter(),
            new PutObjectTaggingTestAdapter(), new GetObjectTaggingTestAdapter(), new DeleteObjectTaggingTestAdapter(),

            new NormalPutObjectTestAdapter(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+1), new NormalPutObjectTestAdapter(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+2),
            new NormalPutObjectTestAdapter(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+3),new NormalPutObjectTestAdapter(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+4),
            new NormalPutObjectTestAdapter(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+5),new NormalPutObjectTestAdapter(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+6),
            new DeleteMultiObjectTestAdapter(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+1, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+2),

            new SelectObjectContentJsonTestAdapter(), new SelectObjectContentCsvTestAdapter(),
            new RestoreObjectTestAdapter(), new PreviewDocumentTestAdapter(),

            new GetMediaInfoTestAdapter(), new GetSnapshotTestAdapter(),

            new AppendObjectTestAdapter.AppendObjectByteTestAdapter(),
            new AppendObjectTestAdapter.AppendObjectSrcPathTestAdapter(),
            new AppendObjectTestAdapter.AppendObjectStreamTestAdapter(),
            new AppendObjectTestAdapter.DeleteAppendObjectTestAdapter()

//            new RestoreObjectTestAdapter()
    };

    @Test public void testTagging() {
//        new PutObjectTaggingTestAdapter().testAsyncRequest();
        new GetObjectTaggingTestAdapter().testAsyncRequest();
    }

    @Test
    public void testAsync() {

        for (NormalRequestTestAdapter adapter : simpleTestAdapters) {
            adapter.testAsyncRequest();
        }
    }


    @Test public void testSync() {

        for (NormalRequestTestAdapter adapter : simpleTestAdapters) {
            adapter.testSyncRequest();
        }
    }
}
