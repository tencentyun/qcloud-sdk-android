package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class GetObjectACLTestAdapter extends NormalRequestTestAdapter<GetObjectACLRequest, GetObjectACLResult> {
    @Override
    protected GetObjectACLRequest newRequestInstance() {
        return new GetObjectACLRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    @Override
    protected GetObjectACLResult exeSync(GetObjectACLRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getObjectACL(request);
    }

    @Override
    protected void exeAsync(GetObjectACLRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getObjectACLAsync(request, resultListener);
    }
}