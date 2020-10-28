package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class DeleteBucketLifecycleTestAdapter extends RequestTestAdapter<DeleteBucketLifecycleRequest, DeleteBucketLifecycleResult> {
    @Override
    protected DeleteBucketLifecycleRequest newRequestInstance() {
        return new DeleteBucketLifecycleRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected DeleteBucketLifecycleResult exeSync(DeleteBucketLifecycleRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteBucketLifecycle(request);
    }

    @Override
    protected void exeAsync(DeleteBucketLifecycleRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteBucketLifecycleAsync(request, resultListener);
    }
}