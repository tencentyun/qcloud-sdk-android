 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class GetBucketTestAdapter extends RequestTestAdapter<GetBucketRequest, GetBucketResult> {
    @Override
    protected GetBucketRequest newRequestInstance() {
        return new GetBucketRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketResult exeSync(GetBucketRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucket(request);
    }

    @Override
    protected void exeAsync(GetBucketRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketAsync(request, resultListener);
    }
}  