 
package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;

import java.io.File;

public class GetObjectTaggingTestAdapter extends NormalRequestTestAdapter<GetObjectTaggingRequest, GetObjectTaggingResult> {
    @Override
    protected GetObjectTaggingRequest newRequestInstance() {
        GetObjectTaggingRequest request = new GetObjectTaggingRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        request.setVersionId(null);
        return request;
    }

    @Override
    protected GetObjectTaggingResult exeSync(GetObjectTaggingRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getObjectTagging(request);
    }

    @Override
    protected void exeAsync(GetObjectTaggingRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getObjectTaggingAsync(request, resultListener);
    }
}  