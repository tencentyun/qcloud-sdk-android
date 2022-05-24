 
package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class DeleteObjectTaggingTestAdapter extends NormalRequestTestAdapter<DeleteObjectTaggingRequest, DeleteObjectTaggingResult> {
    @Override
    protected DeleteObjectTaggingRequest newRequestInstance() {
        DeleteObjectTaggingRequest request = new DeleteObjectTaggingRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        request.setVersionId(null);
        return request;
    }

    @Override
    protected DeleteObjectTaggingResult exeSync(DeleteObjectTaggingRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteObjectTagging(request);
    }

    @Override
    protected void exeAsync(DeleteObjectTaggingRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteObjectTaggingAsync(request, resultListener);
    }
}  