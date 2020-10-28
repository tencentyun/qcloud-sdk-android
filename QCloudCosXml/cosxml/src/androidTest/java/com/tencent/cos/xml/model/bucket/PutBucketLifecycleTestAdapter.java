package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;

import java.util.Random;

// Generate by auto
public class PutBucketLifecycleTestAdapter extends RequestTestAdapter<PutBucketLifecycleRequest, PutBucketLifecycleResult> {
    @Override
    protected PutBucketLifecycleRequest newRequestInstance() {
        PutBucketLifecycleRequest putBucketLifecycleRequest = new PutBucketLifecycleRequest(TestConst.PERSIST_BUCKET);
        LifecycleConfiguration.Rule rule = new LifecycleConfiguration.Rule();
        rule.id = "lifecycle_" + new Random(System.currentTimeMillis()).nextInt();
        rule.status = "Enabled";
        rule.transition = new LifecycleConfiguration.Transition();
        rule.transition.days = 1;
        rule.transition.storageClass = COSStorageClass.STANDARD_IA.getStorageClass();
        putBucketLifecycleRequest.setRuleList(rule);
        return putBucketLifecycleRequest;
    }

    @Override
    protected PutBucketLifecycleResult exeSync(PutBucketLifecycleRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketLifecycle(request);
    }

    @Override
    protected void exeAsync(PutBucketLifecycleRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketLifecycleAsync(request, resultListener);
    }
}