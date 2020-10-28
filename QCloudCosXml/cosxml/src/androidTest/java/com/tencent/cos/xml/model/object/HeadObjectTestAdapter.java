package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class HeadObjectTestAdapter extends RequestTestAdapter<HeadObjectRequest, HeadObjectResult> {
    @Override
    protected HeadObjectRequest newRequestInstance() {
        return new HeadObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    @Override
    protected HeadObjectResult exeSync(HeadObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.headObject(request);
    }

    @Override
    protected void exeAsync(HeadObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.headObjectAsync(request, resultListener);
    }
}