package com.tencent.cos.xml.model.object;

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

public class PutObjectACLTestAdapter{
    public static class PutObjectACL1TestAdapter extends NormalRequestTestAdapter<PutObjectACLRequest, PutObjectACLResult> {
        @Override
        protected PutObjectACLRequest newRequestInstance() {
            PutObjectACLRequest putObjectACLRequest = new PutObjectACLRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
            putObjectACLRequest.setXCOSACL(COSACL.PRIVATE);

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
            putObjectACLRequest.setAccessControlPolicy(accessControlPolicy);
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

    public static class PutObjectACL2TestAdapter extends NormalRequestTestAdapter<PutObjectACLRequest, PutObjectACLResult> {
        @Override
        protected PutObjectACLRequest newRequestInstance() {
            PutObjectACLRequest putObjectACLRequest = new PutObjectACLRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
            putObjectACLRequest.setXCOSACL(COSACL.PRIVATE);
            ACLAccount aclAccount = new ACLAccount();
            aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
            putObjectACLRequest.setXCOSGrantRead(aclAccount);
            putObjectACLRequest.setXCOSGrantWrite(null);
            putObjectACLRequest.setXCOSReadWrite(null);
            putObjectACLRequest.setXCOSReadFullControl(aclAccount);
            putObjectACLRequest.setXCOSGrantReadACP(aclAccount);
            putObjectACLRequest.setXCOSGrantWriteACP(aclAccount);

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
}
