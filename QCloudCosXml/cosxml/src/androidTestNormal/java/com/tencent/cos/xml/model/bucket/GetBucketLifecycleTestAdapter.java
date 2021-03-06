package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class GetBucketLifecycleTestAdapter extends NormalRequestTestAdapter<GetBucketLifecycleRequest, GetBucketLifecycleResult> {
    @Override
    protected GetBucketLifecycleRequest newRequestInstance() {
        return new GetBucketLifecycleRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketLifecycleResult exeSync(GetBucketLifecycleRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketLifecycle(request);
    }

    @Override
    protected void exeAsync(GetBucketLifecycleRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketLifecycleAsync(request, resultListener);
    }
}