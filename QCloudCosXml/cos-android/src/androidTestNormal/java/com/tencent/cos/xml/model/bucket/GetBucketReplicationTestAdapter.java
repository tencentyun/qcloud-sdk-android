 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class GetBucketReplicationTestAdapter extends NormalRequestTestAdapter<GetBucketReplicationRequest, GetBucketReplicationResult> {
    @Override
    protected GetBucketReplicationRequest newRequestInstance() {
        return new GetBucketReplicationRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected GetBucketReplicationResult exeSync(GetBucketReplicationRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketReplication(request);
    }

    @Override
    protected void exeAsync(GetBucketReplicationRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketReplicationAsync(request, resultListener);
    }
}  