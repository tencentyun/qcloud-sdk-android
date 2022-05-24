 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.RefererConfiguration;

import java.util.ArrayList;

public class PutBucketRefererTestAdapter extends NormalRequestTestAdapter<PutBucketRefererRequest, PutBucketRefererResult> {
    private final boolean enabled;
    private final RefererConfiguration.RefererType refererType;
    private final boolean allowEmptyRefer;

    public PutBucketRefererTestAdapter(boolean enabled, RefererConfiguration.RefererType refererType, boolean allowEmptyRefer) {
        this.enabled = enabled;
        this.refererType = refererType;
        this.allowEmptyRefer = allowEmptyRefer;
    }

    @Override
    protected PutBucketRefererRequest newRequestInstance() {
        PutBucketRefererRequest putBucketRefererRequest = new PutBucketRefererRequest(
                TestConst.PERSIST_BUCKET, enabled, refererType);
        putBucketRefererRequest.setAllowEmptyRefer(allowEmptyRefer);
        ArrayList<RefererConfiguration.Domain> domainList = new ArrayList<>();
        domainList.add(new RefererConfiguration.Domain("*.qq.com"));
        domainList.add(new RefererConfiguration.Domain("*.qcloud.com"));
        domainList.add(new RefererConfiguration.Domain("*.google.com"));
        putBucketRefererRequest.setDomainList(domainList);
        return putBucketRefererRequest;
    }

    @Override
    protected PutBucketRefererResult exeSync(PutBucketRefererRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketReferer(request);
    }

    @Override
    protected void exeAsync(PutBucketRefererRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketRefererAsync(request, resultListener);
    }
}  