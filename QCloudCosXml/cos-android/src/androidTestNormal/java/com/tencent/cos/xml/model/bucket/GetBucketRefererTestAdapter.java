 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class GetBucketRefererTestAdapter extends NormalRequestTestAdapter<GetBucketRefererRequest, GetBucketRefererResult> {
    @Override
    protected GetBucketRefererRequest newRequestInstance() {
        return new GetBucketRefererRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketRefererResult exeSync(GetBucketRefererRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketReferer(request);
    }

    @Override
    protected void exeAsync(GetBucketRefererRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketRefererAsync(request, resultListener);
    }
}  