 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class PutBucketVersioningTestAdapter extends NormalRequestTestAdapter<PutBucketVersioningRequest, PutBucketVersioningResult> {
    @Override
    protected PutBucketVersioningRequest newRequestInstance() {
        PutBucketVersioningRequest request = new PutBucketVersioningRequest(TestConst.PERSIST_BUCKET);
        request.setEnableVersion(true);
        return request;
    }

    @Override
    protected PutBucketVersioningResult exeSync(PutBucketVersioningRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketVersioning(request);
    }

    @Override
    protected void exeAsync(PutBucketVersioningRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketVersionAsync(request, resultListener);
    }
}  