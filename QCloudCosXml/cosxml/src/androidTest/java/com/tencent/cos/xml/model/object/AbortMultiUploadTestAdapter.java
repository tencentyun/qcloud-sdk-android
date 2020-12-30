package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class AbortMultiUploadTestAdapter extends RequestTestAdapter<AbortMultiUploadRequest, AbortMultiUploadResult> {
    @Override
    protected AbortMultiUploadRequest newRequestInstance() {
        return null;
    }

    @Override
    protected AbortMultiUploadResult exeSync(AbortMultiUploadRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.abortMultiUpload(request);
    }

    @Override
    protected void exeAsync(AbortMultiUploadRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.abortMultiUploadAsync(request, resultListener);
    }
}