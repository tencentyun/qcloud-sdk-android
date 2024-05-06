package com.tencent.cos.xml.model.bucket;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.RefererConfiguration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BucketTest {
    @Rule
    public ErrorCollector collector = new ErrorCollector();
    private NormalRequestTestAdapter[] adapters = new NormalRequestTestAdapter[] {
            new PutBucketTestAdapter(), new HeadBucketTestAdapter(),
            new PutBucketIntelligentTieringTestAdapter(), new GetBucketIntelligentTieringTestAdapter(),
            new GetBucketTestAdapter(), new DeleteBucketTestAdapter(),

            new PutBucketACLTestAdapter.PutBucketACL1TestAdapter(), new PutBucketACLTestAdapter.PutBucketACL2TestAdapter(), new GetBucketACLTestAdapter(),
            new PutBucketLoggingTestAdapter(), new GetBucketLoggingTestAdapter(),
            new PutBucketTaggingTestAdapter(), new GetBucketTaggingTestAdapter(), new DeleteBucketTaggingTestAdapter(),

            new GetBucketObjectVersionsTestAdapter(),
            new GetBucketLocationTestAdapter(),
            new GetBucketVersionsTestAdapter(),
            new ListMultiUploadsTestAdapter(),

            new PutBucketVersioningTestAdapter(true), new GetBucketVersioningTestAdapter(),
            new PutBucketReplicationTestAdapter(), new GetBucketReplicationTestAdapter(), new DeleteBucketReplicationTestAdapter(),
            new PutBucketVersioningTestAdapter(false),

            new PutBucketInventoryTestAdapter(), new GetBucketInventoryTestAdapter(),
            new ListBucketInventoryTestAdapter(), new DeleteBucketInventoryTestAdapter(),
            new PutBucketPolicyTestAdapter(), new GetBucketPolicyTestAdapter(), new DeleteBucketPolicyTestAdapter(),
            new PutBucketCORSTestAdapter(), new GetBucketCORSTestAdapter(), new DeleteBucketCORSTestAdapter(),
            new PutBucketAccelerateTestAdapter(), new GetBucketAccelerateTestAdapter(),
            new PutBucketWebsiteTestAdapter(), new GetBucketWebsiteTestAdapter(), new DeleteBucketWebsiteTestAdapter(),
            new PutBucketLifecycleTestAdapter(), new GetBucketLifecycleTestAdapter(), new DeleteBucketLifecycleTestAdapter(),

            new GetBucketRefererTestAdapter(),
            new PutBucketRefererTestAdapter(true, RefererConfiguration.RefererType.Black, true),
            new GetBucketRefererTestAdapter(),
            new PutBucketRefererTestAdapter(true, RefererConfiguration.RefererType.White, false),
            new GetBucketRefererTestAdapter(),
            new PutBucketRefererTestAdapter(false, RefererConfiguration.RefererType.White, true),
            new GetBucketRefererTestAdapter(),
    };

    @Before
    public void setCollector(){
        for (NormalRequestTestAdapter adapter : adapters) {
            adapter.setCollector(collector);
        }
    }

    @Test public void testAsync() {

        for (NormalRequestTestAdapter adapter : adapters) {
            adapter.testAsyncRequest();
        }
    }

    @Test public void testSync() {

        for (NormalRequestTestAdapter adapter : adapters) {
            adapter.testSyncRequest();
        }
    }

    @Test public void bucketDomainTestAsync() {
        NormalRequestTestAdapter[] adapters = new NormalRequestTestAdapter[] {
                new PutBucketDomainTestAdapter(), new GetBucketDomainTestAdapter(), new DeleteBucketDomainTestAdapter(),
        };
        for (NormalRequestTestAdapter adapter : adapters) {
            adapter.setCollector(collector);
            adapter.testAsyncRequest();
        }
    }

    @Test public void bucketDomainTestSync() {
        NormalRequestTestAdapter[] adapters = new NormalRequestTestAdapter[] {
                new PutBucketDomainTestAdapter(), new GetBucketDomainTestAdapter(), new DeleteBucketDomainTestAdapter(),
        };
        for (NormalRequestTestAdapter adapter : adapters) {
            adapter.setCollector(collector);
            adapter.testSyncRequest();
        }
    }

}
