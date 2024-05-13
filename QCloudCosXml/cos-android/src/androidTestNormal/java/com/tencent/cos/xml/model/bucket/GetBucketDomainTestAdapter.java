 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class GetBucketDomainTestAdapter extends NormalRequestTestAdapter<GetBucketDomainRequest, GetBucketDomainResult> {
    @Override
    protected GetBucketDomainRequest newRequestInstance() {
        return new GetBucketDomainRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketDomainResult exeSync(GetBucketDomainRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        TestUtils.sleep(1000);
        return cosXmlService.getBucketDomain(request);
    }

    @Override
    protected void exeAsync(GetBucketDomainRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        TestUtils.sleep(1000);
        cosXmlService.getBucketDomainAsync(request, resultListener);
    }
}  