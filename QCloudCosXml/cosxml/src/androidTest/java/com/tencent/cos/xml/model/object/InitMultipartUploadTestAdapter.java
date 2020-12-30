package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class InitMultipartUploadTestAdapter extends RequestTestAdapter<InitMultipartUploadRequest, InitMultipartUploadResult> {
    @Override
    protected InitMultipartUploadRequest newRequestInstance() {
        return null;
    }

    @Override
    protected InitMultipartUploadResult exeSync(InitMultipartUploadRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.initMultipartUpload(request);
    }

    @Override
    protected void exeAsync(InitMultipartUploadRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.initMultipartUploadAsync(request, resultListener);
    }
}