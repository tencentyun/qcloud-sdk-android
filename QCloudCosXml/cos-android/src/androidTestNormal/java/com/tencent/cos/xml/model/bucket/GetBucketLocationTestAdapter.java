package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class GetBucketLocationTestAdapter extends NormalRequestTestAdapter<GetBucketLocationRequest, GetBucketLocationResult> {
    @Override
    protected GetBucketLocationRequest newRequestInstance() {
        return new GetBucketLocationRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketLocationResult exeSync(GetBucketLocationRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketLocation(request);
    }

    @Override
    protected void exeAsync(GetBucketLocationRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketLocationAsync(request, resultListener);
    }
}