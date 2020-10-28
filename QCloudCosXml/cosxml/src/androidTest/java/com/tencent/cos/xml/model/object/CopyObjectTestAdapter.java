package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.common.MetaDataDirective;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class CopyObjectTestAdapter extends RequestTestAdapter<CopyObjectRequest, CopyObjectResult> {

    @Override
    protected CopyObjectRequest newRequestInstance() {
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        String copyObjectCosPath = TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH.concat("_copy_" + System.currentTimeMillis());
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(TestConst.PERSIST_BUCKET,
                copyObjectCosPath, copySourceStruct);

        copyObjectRequest.setCopyMetaDataDirective(MetaDataDirective.COPY);
        try {
            copyObjectRequest.setRequestHeaders("x-cos-xml", "cos", false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        return copyObjectRequest;
    }

    @Override
    protected CopyObjectResult exeSync(CopyObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.copyObject(request);
    }

    @Override
    protected void exeAsync(CopyObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.copyObjectAsync(request, resultListener);
    }
}