 
package com.tencent.cos.xml.model.bucket;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.ACLAccount;

import org.junit.Assert;

// Generate by auto
public class PutBucketTestAdapter extends NormalRequestTestAdapter<PutBucketRequest, PutBucketResult> {
    @Override
    protected PutBucketRequest newRequestInstance() {
        PutBucketRequest request = new PutBucketRequest(TestConst.TEMP_BUCKET);
        request.enableMAZ(true);
        request.setXCOSACL(COSACL.DEFAULT);
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(TestConst.OWNER_UIN);
        aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
        request.setXCOSGrantRead(aclAccount);
        request.setXCOSGrantWrite(aclAccount);
        request.setXCOSReadWrite(aclAccount);
        return request;
    }

    @Override
    protected PutBucketResult exeSync(PutBucketRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucket(request);
    }

    @Override
    protected void exeAsync(PutBucketRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketAsync(request, resultListener);
    }

    @Override
    protected void assertException(@Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {

        if (serviceException != null && "BucketAlreadyOwnedByYou".equalsIgnoreCase(serviceException.getErrorCode())) {
            Assert.assertTrue(true);
        } else {
            super.assertException(clientException, serviceException);
        }
    }
}