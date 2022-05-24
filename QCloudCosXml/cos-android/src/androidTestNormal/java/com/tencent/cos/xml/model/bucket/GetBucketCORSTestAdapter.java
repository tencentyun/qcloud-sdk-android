package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class GetBucketCORSTestAdapter extends NormalRequestTestAdapter<GetBucketCORSRequest, GetBucketCORSResult> {
    @Override
    protected GetBucketCORSRequest newRequestInstance() {
        return new GetBucketCORSRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketCORSResult exeSync(GetBucketCORSRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketCORS(request);
    }

    @Override
    protected void exeAsync(GetBucketCORSRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketCORSAsync(request, resultListener);
    }
}