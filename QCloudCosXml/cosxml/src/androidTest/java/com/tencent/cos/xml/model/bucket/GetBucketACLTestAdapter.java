 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class GetBucketACLTestAdapter extends RequestTestAdapter<GetBucketACLRequest, GetBucketACLResult> {
    @Override
    protected GetBucketACLRequest newRequestInstance() {
        return new GetBucketACLRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketACLResult exeSync(GetBucketACLRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketACL(request);
    }

    @Override
    protected void exeAsync(GetBucketACLRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketACLAsync(request, resultListener);
    }
}  