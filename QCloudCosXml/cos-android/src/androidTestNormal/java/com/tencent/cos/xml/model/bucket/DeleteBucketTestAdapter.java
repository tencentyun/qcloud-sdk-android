 
package com.tencent.cos.xml.model.bucket;

import static com.tencent.cos.xml.core.TestConst.TEMP_BUCKET_REGION;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class DeleteBucketTestAdapter extends NormalRequestTestAdapter<DeleteBucketRequest, DeleteBucketResult> {
    @Override
    protected DeleteBucketRequest newRequestInstance() {
        DeleteBucketRequest request = new DeleteBucketRequest(TestConst.TEMP_BUCKET);
        request.setRegion(TEMP_BUCKET_REGION);
        return request;
    }

    @Override
    protected DeleteBucketResult exeSync(DeleteBucketRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteBucket(request);
    }

    @Override
    protected void exeAsync(DeleteBucketRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteBucketAsync(request, resultListener);
    }
}  