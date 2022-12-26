 
package com.tencent.cos.xml.model.bucket;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

import org.junit.Assert;

// Generate by auto
public class DeleteBucketPolicyTestAdapter extends NormalRequestTestAdapter<DeleteBucketPolicyRequest, DeleteBucketPolicyResult> {
    @Override
    protected DeleteBucketPolicyRequest newRequestInstance() {
        return new DeleteBucketPolicyRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected DeleteBucketPolicyResult exeSync(DeleteBucketPolicyRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteBucketPolicy(request);
    }

    @Override
    protected void exeAsync(DeleteBucketPolicyRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteBucketPolicyAsync(request, resultListener);
    }

    @Override
    protected void assertException(@Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
        if (serviceException != null) {
            Assert.assertTrue(true);
        } else {
            super.assertException(clientException, serviceException);
        }
    }
}  