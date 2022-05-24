package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class GetBucketIntelligentTieringTestAdapter extends NormalRequestTestAdapter<GetBucketIntelligentTieringRequest, GetBucketIntelligentTieringResult> {
    @Override
    protected GetBucketIntelligentTieringRequest newRequestInstance() {
        return new GetBucketIntelligentTieringRequest(TestConst.TEMP_BUCKET);
    }

    @Override
    protected GetBucketIntelligentTieringResult exeSync(GetBucketIntelligentTieringRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketIntelligentTiering(request);
    }

    @Override
    protected void exeAsync(GetBucketIntelligentTieringRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketIntelligentTieringAsync(request, resultListener);
    }

    @Override
    protected void assertResult(GetBucketIntelligentTieringResult result) {
        super.assertResult(result);
    }
}