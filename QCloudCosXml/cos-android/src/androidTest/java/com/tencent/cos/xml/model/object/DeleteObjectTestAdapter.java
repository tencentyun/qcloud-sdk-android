package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class DeleteObjectTestAdapter extends RequestTestAdapter<DeleteObjectRequest, DeleteObjectResult> {

    private String cosPath;

    public DeleteObjectTestAdapter() {
        this(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
    }

    public DeleteObjectTestAdapter(String cosPath) {
        this.cosPath = cosPath;
    }

    @Override
    protected DeleteObjectRequest newRequestInstance() {
        DeleteObjectRequest request = new DeleteObjectRequest(TestConst.PERSIST_BUCKET, cosPath);
        request.setVersionId(null);
        return request;
    }

    @Override
    protected DeleteObjectResult exeSync(DeleteObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteObject(request);
    }

    @Override
    protected void exeAsync(DeleteObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteObjectAsync(request, resultListener);
    }
}