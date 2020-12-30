package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class UploadPartCopyTestAdapter extends RequestTestAdapter<UploadPartCopyRequest, UploadPartCopyResult> {

    private String uploadId;

    public UploadPartCopyTestAdapter(String uploadId) {
        this.uploadId = uploadId;
    }

    @Override
    protected UploadPartCopyRequest newRequestInstance() {

        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);

        try {
            return new UploadPartCopyRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_COPY_OBJECT_DST_PATH,
                    1, uploadId, copySourceStruct);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected UploadPartCopyResult exeSync(UploadPartCopyRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.copyObject(request);
    }

    @Override
    protected void exeAsync(UploadPartCopyRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.copyObjectAsync(request, resultListener);
    }
}