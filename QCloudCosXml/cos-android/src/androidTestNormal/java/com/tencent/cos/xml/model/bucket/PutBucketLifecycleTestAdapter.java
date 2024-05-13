package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.LifecycleConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Generate by auto
public class PutBucketLifecycleTestAdapter extends NormalRequestTestAdapter<PutBucketLifecycleRequest, PutBucketLifecycleResult> {
    @Override
    protected PutBucketLifecycleRequest newRequestInstance() {
        PutBucketLifecycleRequest putBucketLifecycleRequest = new PutBucketLifecycleRequest(TestConst.PERSIST_BUCKET);
        LifecycleConfiguration.Rule rule = new LifecycleConfiguration.Rule();
        rule.id = "lifecycle_" + new Random(System.currentTimeMillis()).nextInt();
        LifecycleConfiguration.Filter filter = new LifecycleConfiguration.Filter();
        filter.prefix = "do_not_remove/";
        rule.filter = filter;
        rule.status = "Enabled";
        rule.transition = new LifecycleConfiguration.Transition();
        rule.transition.days = 1;
//        rule.transition.date = "2021-12-01T00:00:00+08:00";
        rule.transition.storageClass = COSStorageClass.STANDARD_IA.getStorageClass();
        LifecycleConfiguration.NoncurrentVersionTransition noncurrentVersionTransition = new LifecycleConfiguration.NoncurrentVersionTransition();
        noncurrentVersionTransition.noncurrentDays = 1;
        noncurrentVersionTransition.storageClass = "STANDARD_IA";
        rule.noncurrentVersionTransition = noncurrentVersionTransition;
        LifecycleConfiguration.AbortIncompleteMultiUpload  abortIncompleteMultiUpload = new LifecycleConfiguration.AbortIncompleteMultiUpload();
        abortIncompleteMultiUpload.daysAfterInitiation = 1;
        rule.abortIncompleteMultiUpload = abortIncompleteMultiUpload;

        List<LifecycleConfiguration.Rule> ruleList = new ArrayList<>();
        LifecycleConfiguration.Rule rule1 = new LifecycleConfiguration.Rule();
        rule1.id = "lifecycle1_" + new Random(System.currentTimeMillis()).nextInt();
        rule1.status = "Enabled";
        rule1.transition = new LifecycleConfiguration.Transition();
        rule1.transition.days = 1;
        rule1.transition.storageClass = COSStorageClass.STANDARD_IA.getStorageClass();
        LifecycleConfiguration.NoncurrentVersionExpiration noncurrentVersionExpiration = new LifecycleConfiguration.NoncurrentVersionExpiration();
        noncurrentVersionExpiration.noncurrentDays = 1;
        rule1.noncurrentVersionExpiration = noncurrentVersionExpiration;

        LifecycleConfiguration.Rule rule2 = new LifecycleConfiguration.Rule();
        rule2.id = "lifecycle2_" + new Random(System.currentTimeMillis()).nextInt();
        rule2.status = "Enabled";
        LifecycleConfiguration.Expiration expiration = new LifecycleConfiguration.Expiration();
//        expiration.date = "2029-12-01T12:00:00.000Z";
        expiration.days = 1;
//        expiration.expiredObjectDeleteMarker = "true";
        rule2.expiration = expiration;

        ruleList.add(rule1);
        ruleList.add(rule2);
        putBucketLifecycleRequest.getLifecycleConfiguration();

        putBucketLifecycleRequest.setRuleList(rule);
        putBucketLifecycleRequest.setRuleList(ruleList);
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