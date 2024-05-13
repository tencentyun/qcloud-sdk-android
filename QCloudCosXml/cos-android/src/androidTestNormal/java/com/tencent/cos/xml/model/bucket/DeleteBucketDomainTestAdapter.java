 
package com.tencent.cos.xml.model.bucket;

import static org.hamcrest.CoreMatchers.is;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

import org.junit.Assert;

// Generate by auto
public class DeleteBucketDomainTestAdapter extends NormalRequestTestAdapter<DeleteBucketDomainRequest, DeleteBucketDomainResult> {
    @Override
    protected DeleteBucketDomainRequest newRequestInstance() {
        return new DeleteBucketDomainRequest(TestConst.PERSIST_BUCKET);
    }

    @Override
    protected DeleteBucketDomainResult exeSync(DeleteBucketDomainRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteBucketDomain(request);
    }

    @Override
    protected void exeAsync(DeleteBucketDomainRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteBucketDomainAsync(request, resultListener);
    }

    @Override
    protected void assertException(@Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
        if (serviceException != null && "DomainAuditFailed".equalsIgnoreCase(serviceException.getErrorCode())) {
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