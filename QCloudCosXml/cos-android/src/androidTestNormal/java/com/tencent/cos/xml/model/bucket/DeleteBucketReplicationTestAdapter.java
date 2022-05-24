 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class DeleteBucketReplicationTestAdapter extends NormalRequestTestAdapter<DeleteBucketReplicationRequest, DeleteBucketReplicationResult> {
    @Override
    protected DeleteBucketReplicationRequest newRequestInstance() {
        return new DeleteBucketReplicationRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected DeleteBucketReplicationResult exeSync(DeleteBucketReplicationRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteBucketReplication(request);
    }

    @Override
    protected void exeAsync(DeleteBucketReplicationRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteBucketReplicationAsync(request, resultListener);
    }
}  