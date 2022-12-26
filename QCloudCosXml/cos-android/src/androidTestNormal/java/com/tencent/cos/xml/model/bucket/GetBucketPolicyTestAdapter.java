 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class GetBucketPolicyTestAdapter extends NormalRequestTestAdapter<GetBucketPolicyRequest, GetBucketPolicyResult> {
    @Override
    protected GetBucketPolicyRequest newRequestInstance() {
        return new GetBucketPolicyRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketPolicyResult exeSync(GetBucketPolicyRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketPolicy(request);
    }

    @Override
    protected void exeAsync(GetBucketPolicyRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketPolicyAsync(request, resultListener);
    }
}  