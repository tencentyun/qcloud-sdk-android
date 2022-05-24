package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class GetObjectTestAdapter extends RequestTestAdapter<GetObjectRequest, GetObjectResult> {
    @Override
    protected GetObjectRequest newRequestInstance() {
        GetObjectRequest request = new GetObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath(), TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        return request;
    }

    @Override
    protected void exeAsync(GetObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getObjectAsync(request, resultListener);
    }
}