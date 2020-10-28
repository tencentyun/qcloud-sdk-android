package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class GetObjectTestAdapter extends RequestTestAdapter<GetObjectRequest, GetObjectResult> {
    @Override
    protected GetObjectRequest newRequestInstance() {
        return new GetObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath(), TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    @Override
    protected GetObjectResult exeSync(GetObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getObject(request);
    }

    @Override
    protected void exeAsync(GetObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getObjectAsync(request, resultListener);
    }
}