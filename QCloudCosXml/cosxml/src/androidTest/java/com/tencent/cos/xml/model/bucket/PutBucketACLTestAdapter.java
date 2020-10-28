 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.model.tag.ACLAccount;

// Generate by auto
public class PutBucketACLTestAdapter extends RequestTestAdapter<PutBucketACLRequest, PutBucketACLResult> {
    @Override
    protected PutBucketACLRequest newRequestInstance() {

        PutBucketACLRequest putBucketACLRequest = new PutBucketACLRequest(TestConst.PERSIST_BUCKET);
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
        putBucketACLRequest.setXCOSGrantRead(aclAccount);
        putBucketACLRequest.setXCOSGrantWrite(aclAccount);
        putBucketACLRequest.setXCOSACL(COSACL.PRIVATE);
        return putBucketACLRequest;
    }

    @Override
    protected PutBucketACLResult exeSync(PutBucketACLRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketACL(request);
    }

    @Override
    protected void exeAsync(PutBucketACLRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketACLAsync(request, resultListener);
    }
}  