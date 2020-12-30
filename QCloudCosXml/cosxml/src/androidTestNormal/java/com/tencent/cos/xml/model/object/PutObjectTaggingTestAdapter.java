 
package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class PutObjectTaggingTestAdapter extends NormalRequestTestAdapter<PutObjectTaggingRequest, PutObjectTaggingResult> {
    @Override
    protected PutObjectTaggingRequest newRequestInstance() {
        PutObjectTaggingRequest putObjectTaggingRequest = new PutObjectTaggingRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        putObjectTaggingRequest.addTag("key", "value");
        putObjectTaggingRequest.addTag("hello", "world");
        putObjectTaggingRequest.setVersionId(null);
        return putObjectTaggingRequest;
    }

    @Override
    protected PutObjectTaggingResult exeSync(PutObjectTaggingRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putObjectTagging(request);
    }

    @Override
    protected void exeAsync(PutObjectTaggingRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putObjectTaggingAsync(request, resultListener);
    }
}  