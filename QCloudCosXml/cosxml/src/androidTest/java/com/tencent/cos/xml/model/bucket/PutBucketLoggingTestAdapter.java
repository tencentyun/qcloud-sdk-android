 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class PutBucketLoggingTestAdapter extends RequestTestAdapter<PutBucketLoggingRequest, PutBucketLoggingResult> {
    @Override
    protected PutBucketLoggingRequest newRequestInstance() {
        PutBucketLoggingRequest putBucketLoggingRequest = new PutBucketLoggingRequest(TestConst.PERSIST_BUCKET);
        putBucketLoggingRequest.setTargetBucket(TestConst.PERSIST_BUCKET);
        putBucketLoggingRequest.setTargetPrefix("objectPrefix/");
        return putBucketLoggingRequest;
    }

    @Override
    protected PutBucketLoggingResult exeSync(PutBucketLoggingRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketLogging(request);
    }

    @Override
    protected void exeAsync(PutBucketLoggingRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketLoggingAsync(request, resultListener);
    }
}  