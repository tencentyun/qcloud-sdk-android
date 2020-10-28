 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class PutBucketWebsiteTestAdapter extends RequestTestAdapter<PutBucketWebsiteRequest, PutBucketWebsiteResult> {
    @Override
    protected PutBucketWebsiteRequest newRequestInstance() {
        PutBucketWebsiteRequest putBucketWebsiteRequest = new PutBucketWebsiteRequest(TestConst.PERSIST_BUCKET);
        putBucketWebsiteRequest.setIndexDocument("index.html");
        return putBucketWebsiteRequest;
    }

    @Override
    protected PutBucketWebsiteResult exeSync(PutBucketWebsiteRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketWebsite(request);
    }

    @Override
    protected void exeAsync(PutBucketWebsiteRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketWebsiteAsync(request, resultListener);
    }
}  