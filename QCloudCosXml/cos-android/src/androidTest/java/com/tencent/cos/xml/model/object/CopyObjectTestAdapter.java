package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.common.MetaDataDirective;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.model.tag.ACLAccount;

// Generate by auto
public class CopyObjectTestAdapter extends RequestTestAdapter<CopyObjectRequest, CopyObjectResult> {

    @Override
    protected CopyObjectRequest newRequestInstance() {
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        String copyObjectCosPath = "/copy/"+"small_object_copy_" + System.currentTimeMillis();
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(TestConst.PERSIST_BUCKET,
                copyObjectCosPath, copySourceStruct);

        copyObjectRequest.setCopyMetaDataDirective(MetaDataDirective.COPY);
        try {
            copyObjectRequest.setRequestHeaders("x-cos-xml", "cos", false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        copyObjectRequest.setCosStorageClass(COSStorageClass.STANDARD);
        copyObjectRequest.setXCOSACL(COSACL.DEFAULT);
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(TestConst.OWNER_UIN);
        aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
        copyObjectRequest.setXCOSGrantRead(aclAccount);
//        copyObjectRequest.setXCOSGrantWrite(aclAccount);
        copyObjectRequest.setXCOSReadWrite(aclAccount);
//        copyObjectRequest.setCopyIfNoneMatch("test etag");
//        copyObjectRequest.setCopyIfUnmodifiedSince("Wed, 21 Oct 2052 07:28:00 GMT");
        return copyObjectRequest;
    }

    @Override
    protected CopyObjectResult exeSync(CopyObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.copyObject(request);
    }

    @Override
    protected void exeAsync(CopyObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.copyObjectAsync(request, resultListener);
    }
}