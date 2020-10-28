 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class GetBucketTaggingTestAdapter extends RequestTestAdapter<GetBucketTaggingRequest, GetBucketTaggingResult> {
    @Override
    protected GetBucketTaggingRequest newRequestInstance() {
        return new GetBucketTaggingRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketTaggingResult exeSync(GetBucketTaggingRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketTagging(request);
    }

    @Override
    protected void exeAsync(GetBucketTaggingRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketTaggingAsync(request, resultListener);
    }
}  