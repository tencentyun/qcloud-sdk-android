 
package com.tencent.cos.xml.model.bucket;

import static com.tencent.cos.xml.core.TestConst.TEMP_BUCKET_REGION;

import static org.hamcrest.CoreMatchers.is;

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
        request.enableMAZ(false);
        request.setRegion(TEMP_BUCKET_REGION);
        request.setXCOSACL(COSACL.DEFAULT);
        ACLAccount aclAccount = new ACLAccount();
        aclAccount.addAccount(TestConst.OWNER_UIN);
        aclAccount.addAccount(TestConst.OWNER_UIN, TestConst.OWNER_UIN);
        request.setXCOSGrantRead(aclAccount);
        request.setXCOSGrantWrite(aclAccount);
        request.setXCOSReadWrite(aclAccount);

        PutBucketRequest request1 = new PutBucketRequest(TestConst.TEMP_BUCKET);
        request1.setRegion(TEMP_BUCKET_REGION);
        request1.setXCOSACL("default");
        request1.setXCOSGrantRead("default");
        request1.setXCOSGrantWrite("default");
        request1.setXCOSReadWrite("default");

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
            if(super.collector != null) {
                this.collector.checkThat(true, is(true));
            } else  {
                Assert.assertTrue(true);
            }
        } else {
            super.assertException(clientException, serviceException);
        }
    }
}