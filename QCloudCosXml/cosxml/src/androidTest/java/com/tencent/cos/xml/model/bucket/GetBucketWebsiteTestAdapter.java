 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class GetBucketWebsiteTestAdapter extends RequestTestAdapter<GetBucketWebsiteRequest, GetBucketWebsiteResult> {
    @Override
    protected GetBucketWebsiteRequest newRequestInstance() {
        return new GetBucketWebsiteRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketWebsiteResult exeSync(GetBucketWebsiteRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketWebsite(request);
    }

    @Override
    protected void exeAsync(GetBucketWebsiteRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketWebsiteAsync(request, resultListener);
    }
}  