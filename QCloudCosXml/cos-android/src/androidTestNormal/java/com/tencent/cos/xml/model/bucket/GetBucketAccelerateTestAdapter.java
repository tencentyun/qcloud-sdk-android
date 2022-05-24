 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class GetBucketAccelerateTestAdapter extends NormalRequestTestAdapter<GetBucketAccelerateRequest, GetBucketAccelerateResult> {
    @Override
    protected GetBucketAccelerateRequest newRequestInstance() {
        return new GetBucketAccelerateRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketAccelerateResult exeSync(GetBucketAccelerateRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketAccelerate(request);
    }

    @Override
    protected void exeAsync(GetBucketAccelerateRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketAccelerateAsync(request, resultListener);
    }
}  