 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class GetBucketVersioningTestAdapter extends NormalRequestTestAdapter<GetBucketVersioningRequest, GetBucketVersioningResult> {
    @Override
    protected GetBucketVersioningRequest newRequestInstance() {
        return new GetBucketVersioningRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketVersioningResult exeSync(GetBucketVersioningRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketVersioning(request);
    }

    @Override
    protected void exeAsync(GetBucketVersioningRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketVersioningAsync(request, resultListener);
    }
}  