 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

// Generate by auto
public class GetBucketInventoryTestAdapter extends NormalRequestTestAdapter<GetBucketInventoryRequest, GetBucketInventoryResult> {
    @Override
    protected GetBucketInventoryRequest newRequestInstance() {
        GetBucketInventoryRequest getBucketInventoryRequest = new GetBucketInventoryRequest(TestConst.PERSIST_BUCKET);
        getBucketInventoryRequest.setInventoryId("inventoryId");
        return getBucketInventoryRequest;
    }

    @Override
    protected GetBucketInventoryResult exeSync(GetBucketInventoryRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketInventory(request);
    }

    @Override
    protected void exeAsync(GetBucketInventoryRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketInventoryAsync(request, resultListener);
    }
}  