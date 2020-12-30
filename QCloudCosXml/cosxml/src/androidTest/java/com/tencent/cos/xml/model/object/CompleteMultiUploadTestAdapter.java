package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class CompleteMultiUploadTestAdapter extends RequestTestAdapter<CompleteMultiUploadRequest, CompleteMultiUploadResult> {
    @Override
    protected CompleteMultiUploadRequest newRequestInstance() {
        return null;
    }

    @Override
    protected CompleteMultiUploadResult exeSync(CompleteMultiUploadRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.completeMultiUpload(request);
    }

    @Override
    protected void exeAsync(CompleteMultiUploadRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.completeMultiUploadAsync(request, resultListener);
    }
}