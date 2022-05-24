 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class PutBucketTaggingTestAdapter extends NormalRequestTestAdapter<PutBucketTaggingRequest, PutBucketTaggingResult> {
    @Override
    protected PutBucketTaggingRequest newRequestInstance() {
        PutBucketTaggingRequest putBucketTaggingRequest = new PutBucketTaggingRequest(TestConst.PERSIST_BUCKET);
        putBucketTaggingRequest.addTag("key", "value");
        putBucketTaggingRequest.addTag("hello", "world");
        return putBucketTaggingRequest;
    }

    @Override
    protected PutBucketTaggingResult exeSync(PutBucketTaggingRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketTagging(request);
    }

    @Override
    protected void exeAsync(PutBucketTaggingRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketTaggingAsync(request, resultListener);
    }
}  