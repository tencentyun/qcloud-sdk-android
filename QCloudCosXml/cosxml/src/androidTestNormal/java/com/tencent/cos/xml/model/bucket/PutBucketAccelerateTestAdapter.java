 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class PutBucketAccelerateTestAdapter extends NormalRequestTestAdapter<PutBucketAccelerateRequest, PutBucketAccelerateResult> {
    @Override
    protected PutBucketAccelerateRequest newRequestInstance() {
        return new PutBucketAccelerateRequest(TestConst.PERSIST_BUCKET, true);
    }

    @Override
    protected PutBucketAccelerateResult exeSync(PutBucketAccelerateRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketAccelerate(request);
    }

    @Override
    protected void exeAsync(PutBucketAccelerateRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketAccelerateAsync(request, resultListener);
    }
}  