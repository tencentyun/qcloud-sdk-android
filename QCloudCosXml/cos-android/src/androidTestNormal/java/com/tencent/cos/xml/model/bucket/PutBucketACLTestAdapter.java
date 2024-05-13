
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.AccessControlPolicy;

import java.util.ArrayList;
import java.util.List;

public class PutBucketACLTestAdapter {
    public static class PutBucketACL1TestAdapter extends NormalRequestTestAdapter<PutBucketACLRequest, PutBucketACLResult> {
        @Override
        protected PutBucketACLRequest newRequestInstance() {

            PutBucketACLRequest putBucketACLRequest = new PutBucketACLRequest(TestConst.PERSIST_BUCKET);
            ACLAccount aclAccount = new ACLAccount();
            aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
            putBucketACLRequest.setXCOSGrantRead(aclAccount);
            putBucketACLRequest.setXCOSGrantWrite(aclAccount);
            putBucketACLRequest.setXCOSGrantReadACP(aclAccount);
            putBucketACLRequest.setXCOSGrantWriteACP(aclAccount);
            putBucketACLRequest.setXCOSReadFullControl(aclAccount);
//            putBucketACLRequest.setXCOSReadWrite(aclAccount);
//            putBucketACLRequest.setXCOSReadWrite(null);
//            putBucketACLRequest.setXCOSACL(COSACL.PRIVATE.getAcl());
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

    public static class PutBucketACL2TestAdapter extends NormalRequestTestAdapter<PutBucketACLRequest, PutBucketACLResult> {
        @Override
        protected PutBucketACLRequest newRequestInstance() {

            PutBucketACLRequest putBucketACLRequest = new PutBucketACLRequest(TestConst.PERSIST_BUCKET);
            AccessControlPolicy accessControlPolicy = new AccessControlPolicy();
            AccessControlPolicy.Owner owner = new AccessControlPolicy.Owner();
            owner.id = String.format("qcs::cam::uin/%s:uin/%s", TestConst.OWNER_UIN, TestConst.OWNER_UIN);
            accessControlPolicy.owner = owner;
            AccessControlPolicy.AccessControlList accessControlList = new AccessControlPolicy.AccessControlList();
            List<AccessControlPolicy.Grant> grants = new ArrayList<>();
            AccessControlPolicy.Grant grant = new AccessControlPolicy.Grant();
            AccessControlPolicy.Grantee grantee = new AccessControlPolicy.Grantee();
            grantee.id = String.format("qcs::cam::uin/%s:uin/%s", TestConst.OWNER_UIN, TestConst.OWNER_UIN);
            grant.grantee = grantee;
            grant.permission = "FULL_CONTROL";
            grants.add(grant);
            accessControlList.grants = grants;
            accessControlPolicy.accessControlList = accessControlList;
            putBucketACLRequest.setAccessControlPolicy(accessControlPolicy);
            putBucketACLRequest.setXCOSACL("private");
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
}
