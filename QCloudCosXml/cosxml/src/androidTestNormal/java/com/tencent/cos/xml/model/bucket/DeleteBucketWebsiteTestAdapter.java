 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class DeleteBucketWebsiteTestAdapter extends NormalRequestTestAdapter<DeleteBucketWebsiteRequest, DeleteBucketWebsiteResult> {
    @Override
    protected DeleteBucketWebsiteRequest newRequestInstance() {
        return new DeleteBucketWebsiteRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected DeleteBucketWebsiteResult exeSync(DeleteBucketWebsiteRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteBucketWebsite(request);
    }

    @Override
    protected void exeAsync(DeleteBucketWebsiteRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteBucketWebsiteAsync(request, resultListener);
    }
}  