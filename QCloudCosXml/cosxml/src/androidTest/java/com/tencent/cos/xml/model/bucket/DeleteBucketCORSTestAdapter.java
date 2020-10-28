package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class DeleteBucketCORSTestAdapter extends RequestTestAdapter<DeleteBucketCORSRequest, DeleteBucketCORSResult> {
    @Override
    protected DeleteBucketCORSRequest newRequestInstance() {
        return new DeleteBucketCORSRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected DeleteBucketCORSResult exeSync(DeleteBucketCORSRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteBucketCORS(request);
    }

    @Override
    protected void exeAsync(DeleteBucketCORSRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteBucketCORSAsync(request, resultListener);
    }
}