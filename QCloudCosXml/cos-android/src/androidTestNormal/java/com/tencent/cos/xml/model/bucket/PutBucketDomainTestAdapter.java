 
package com.tencent.cos.xml.model.bucket;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.DomainConfiguration;

import org.junit.Assert;

// Generate by auto
public class PutBucketDomainTestAdapter extends NormalRequestTestAdapter<PutBucketDomainRequest, PutBucketDomainResult> {
    @Override
    protected PutBucketDomainRequest newRequestInstance() {
        PutBucketDomainRequest putBucketDomainRequest = new PutBucketDomainRequest(TestConst.PERSIST_BUCKET);
        DomainConfiguration.DomainRule domainRule1 = new DomainConfiguration.DomainRule(
                DomainConfiguration.STATUS_DISABLED,
                "testDomain.com",
                DomainConfiguration.TYPE_WEBSITE
        );
        DomainConfiguration.DomainRule domainRule2 = new DomainConfiguration.DomainRule(
                DomainConfiguration.STATUS_DISABLED,
                "testDomain1.com",
                DomainConfiguration.TYPE_REST
        );
        putBucketDomainRequest.addDomainRule(domainRule1);
        putBucketDomainRequest.addDomainRule(domainRule2);
        return putBucketDomainRequest;
    }

    @Override
    protected PutBucketDomainResult exeSync(PutBucketDomainRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketDomain(request);
    }

    @Override
    protected void exeAsync(PutBucketDomainRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketDomainAsync(request, resultListener);
    }

    @Override
    protected void assertException(@Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {

        if (serviceException != null && "DNSRecordVerifyFailed".equalsIgnoreCase(serviceException.getErrorCode())) {
            Assert.assertTrue(true);
        } else {
            super.assertException(clientException, serviceException);
        }
    }
}  