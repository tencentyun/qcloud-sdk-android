package com.tencent.cos.xml.model.bucket;

import android.app.Instrumentation;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration;

// Generate by auto
public class PutBucketIntelligentTieringTestAdapter extends RequestTestAdapter<PutBucketIntelligentTieringRequest, PutBucketIntelligentTieringResult> {
    @Override
    protected PutBucketIntelligentTieringRequest newRequestInstance() {
        PutBucketIntelligentTieringRequest request = new PutBucketIntelligentTieringRequest(TestConst.TEMP_BUCKET);
        request.setConfiguration(new IntelligentTieringConfiguration(PutBucketIntelligentTieringRequest.STATUS_ENABLED,
                30));
        return request;
    }

    @Override
    protected PutBucketIntelligentTieringResult exeSync(PutBucketIntelligentTieringRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketIntelligentTiering(request);
    }

    @Override
    protected void exeAsync(PutBucketIntelligentTieringRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketIntelligentTieringAsync(request, resultListener);
    }
}