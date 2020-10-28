package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
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
        return new DeleteObjectRequest(TestConst.PERSIST_BUCKET, cosPath);
    }

    @Override
    protected DeleteObjectResult exeSync(DeleteObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteObject(request);
    }

    @Override
    protected void exeAsync(DeleteObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteObjectAsync(request, resultListener);
    }
}