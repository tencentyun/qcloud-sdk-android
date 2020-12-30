package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class UploadPartTestAdapter extends RequestTestAdapter<UploadPartRequest, UploadPartResult> {
    @Override
    protected UploadPartRequest newRequestInstance() {
        return null;
    }

    @Override
    protected UploadPartResult exeSync(UploadPartRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.uploadPart(request);
    }

    @Override
    protected void exeAsync(UploadPartRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.uploadPartAsync(request, resultListener);
    }
}