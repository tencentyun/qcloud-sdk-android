 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class GetBucketLoggingTestAdapter extends RequestTestAdapter<GetBucketLoggingRequest, GetBucketLoggingResult> {
    @Override
    protected GetBucketLoggingRequest newRequestInstance() {
        return new GetBucketLoggingRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketLoggingResult exeSync(GetBucketLoggingRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketLogging(request);
    }

    @Override
    protected void exeAsync(GetBucketLoggingRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketLoggingAsync(request, resultListener);
    }
}  