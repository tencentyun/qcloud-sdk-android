package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class HeadObjectTestAdapter extends RequestTestAdapter<HeadObjectRequest, HeadObjectResult> {
    @Override
    protected HeadObjectRequest newRequestInstance() {
        HeadObjectRequest request = new HeadObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        request.setVersionId(null);
        request.setIfModifiedSince("Wed, 21 Oct 2009 07:28:00 GMT");
        return request;
    }

    @Override
    protected HeadObjectResult exeSync(HeadObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.headObject(request);
    }

    @Override
    protected void exeAsync(HeadObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.headObjectAsync(request, resultListener);
    }
}