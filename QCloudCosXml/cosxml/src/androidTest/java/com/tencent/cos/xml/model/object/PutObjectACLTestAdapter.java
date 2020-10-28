package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.model.tag.ACLAccount;

// Generate by auto
public class PutObjectACLTestAdapter extends RequestTestAdapter<PutObjectACLRequest, PutObjectACLResult> {
    @Override
    protected PutObjectACLRequest newRequestInstance() {
        PutObjectACLRequest putObjectACLRequest = new PutObjectACLRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        putObjectACLRequest.setXCOSACL(COSACL.PRIVATE);
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
        putObjectACLRequest.setXCOSGrantRead(aclAccount);
        return putObjectACLRequest;
    }

    @Override
    protected PutObjectACLResult exeSync(PutObjectACLRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putObjectACL(request);
    }

    @Override
    protected void exeAsync(PutObjectACLRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putObjectACLAsync(request, resultListener);
    }
}