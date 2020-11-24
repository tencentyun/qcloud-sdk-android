package com.tencent.cos.xml.model.bucket;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.model.RequestTestAdapter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BucketTest {

    private RequestTestAdapter[] adapters = new RequestTestAdapter[] {
            new PutBucketTestAdapter(), new HeadBucketTestAdapter(),
            new PutBucketIntelligentTieringTestAdapter(), new GetBucketIntelligentTieringTestAdapter(),
            new GetBucketTestAdapter(), new DeleteBucketTestAdapter(),
            new PutBucketACLTestAdapter(), new GetBucketACLTestAdapter(),
            new PutBucketInventoryTestAdapter(), new GetBucketInventoryTestAdapter(), new DeleteBucketInventoryTestAdapter(),
            new PutBucketLoggingTestAdapter(), new GetBucketLoggingTestAdapter(),
            new PutBucketTaggingTestAdapter(), new GetBucketTaggingTestAdapter(),
            new PutBucketWebsiteTestAdapter(), new GetBucketWebsiteTestAdapter(), new DeleteBucketWebsiteTestAdapter(),
            new PutBucketDomainTestAdapter(), new GetBucketDomainTestAdapter()
    };

    @Test public void testAsync() {

        for (RequestTestAdapter adapter : adapters) {
            adapter.testAsyncRequest();
        }
    }

    @Test public void testSync() {

        for (RequestTestAdapter adapter : adapters) {
            adapter.testSyncRequest();
        }
    }

}
