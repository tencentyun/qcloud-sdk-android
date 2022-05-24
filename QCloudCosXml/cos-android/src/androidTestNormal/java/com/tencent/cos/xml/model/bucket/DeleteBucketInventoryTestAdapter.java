 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class DeleteBucketInventoryTestAdapter extends NormalRequestTestAdapter<DeleteBucketInventoryRequest, DeleteBucketInventoryResult> {
    @Override
    protected DeleteBucketInventoryRequest newRequestInstance() {
        DeleteBucketInventoryRequest deleteBucketInventoryRequest = new DeleteBucketInventoryRequest(TestConst.PERSIST_BUCKET);
        deleteBucketInventoryRequest.setInventoryId("inventoryId");
        return deleteBucketInventoryRequest;
    }

    @Override
    protected DeleteBucketInventoryResult exeSync(DeleteBucketInventoryRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteBucketInventory(request);
    }

    @Override
    protected void exeAsync(DeleteBucketInventoryRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteBucketInventoryAsync(request, resultListener);
    }
}  