package com.tencent.cos.xml.model.bucket;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.QServer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by bradyxiao on 2018/3/5.
 */
@RunWith(AndroidJUnit4.class)
public class ListBucketVersionsRequestTest {

    @BeforeClass
    public static void init() {

        Context appContext = InstrumentationRegistry.getContext();
        QServer.init(appContext);
    }
    @Test
    public void test() throws Exception{
        ListBucketVersionsRequest request = new ListBucketVersionsRequest(QServer.persistBucket);
        ListBucketVersionsResult result = QServer.cosXml.listBucketVersions(request);
        Log.d(QServer.TAG, result.printResult());
    }
}