 
package com.tencent.cos.xml.model.bucket;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.model.tag.DomainConfiguration;

import org.junit.Assert;

// Generate by auto
public class PutBucketDomainTestAdapter extends RequestTestAdapter<PutBucketDomainRequest, PutBucketDomainResult> {
    @Override
    protected PutBucketDomainRequest newRequestInstance() {

        PutBucketDomainRequest putBucketDomainRequest = new PutBucketDomainRequest(TestConst.PERSIST_BUCKET);
        DomainConfiguration.DomainRule domainRule = new DomainConfiguration.DomainRule(
                DomainConfiguration.STATUS_ENABLED,
                "www.example.com",
                DomainConfiguration.TYPE_REST
        );
        domainRule.forcedReplacement = DomainConfiguration.REPLACE_CNAME;
        putBucketDomainRequest.addDomainRule(domainRule);
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

        if (serviceException != null && "DomainAuditFailed".equalsIgnoreCase(serviceException.getErrorCode())) {
            Assert.assertTrue(true);
        } else {
            super.assertException(clientException, serviceException);
        }
    }
}  