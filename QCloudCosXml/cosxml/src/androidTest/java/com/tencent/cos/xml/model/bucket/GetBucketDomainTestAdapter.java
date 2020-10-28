 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class GetBucketDomainTestAdapter extends RequestTestAdapter<GetBucketDomainRequest, GetBucketDomainResult> {
    @Override
    protected GetBucketDomainRequest newRequestInstance() {
        return new GetBucketDomainRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketDomainResult exeSync(GetBucketDomainRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketDomain(request);
    }

    @Override
    protected void exeAsync(GetBucketDomainRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketDomainAsync(request, resultListener);
    }
}  