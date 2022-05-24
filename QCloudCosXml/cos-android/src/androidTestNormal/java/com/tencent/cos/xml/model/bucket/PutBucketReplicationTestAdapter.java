 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class PutBucketReplicationTestAdapter extends NormalRequestTestAdapter<PutBucketReplicationRequest, PutBucketReplicationResult> {
    @Override
    protected PutBucketReplicationRequest newRequestInstance() {
        PutBucketReplicationRequest request = new PutBucketReplicationRequest(TestConst.PERSIST_BUCKET);
        request.setReplicationConfigurationWithRole(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
        PutBucketReplicationRequest.RuleStruct ruleStruct = new PutBucketReplicationRequest.RuleStruct();
        ruleStruct.id = "RuleId_01";
        ruleStruct.appid = TestConst.COS_APPID;
        ruleStruct.bucket = TestConst.PERSIST_BUCKET_REPLICATION;
        ruleStruct.region = TestConst.PERSIST_BUCKET_REPLICATION_REGION;
        ruleStruct.isEnable = false;
        ruleStruct.storageClass = COSStorageClass.STANDARD.getStorageClass();
        ruleStruct.prefix = TestConst.PERSIST_BUCKET_PIC_PATH;
        request.setReplicationConfigurationWithRule(ruleStruct);
        return request;
    }

    @Override
    protected PutBucketReplicationResult exeSync(PutBucketReplicationRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketReplication(request);
    }

    @Override
    protected void exeAsync(PutBucketReplicationRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketReplicationAsync(request, resultListener);
    }
}  