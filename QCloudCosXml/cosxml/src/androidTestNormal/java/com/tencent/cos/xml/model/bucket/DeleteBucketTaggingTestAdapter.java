 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class DeleteBucketTaggingTestAdapter extends NormalRequestTestAdapter<DeleteBucketTaggingRequest, DeleteBucketTaggingResult> {
    @Override
    protected DeleteBucketTaggingRequest newRequestInstance() {
        return new DeleteBucketTaggingRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected DeleteBucketTaggingResult exeSync(DeleteBucketTaggingRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteBucketTagging(request);
    }

    @Override
    protected void exeAsync(DeleteBucketTaggingRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteBucketTaggingAsync(request, resultListener);
    }
}  