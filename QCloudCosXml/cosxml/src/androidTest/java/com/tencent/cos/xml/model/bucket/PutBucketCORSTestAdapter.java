package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.model.tag.CORSConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Generate by auto
public class PutBucketCORSTestAdapter extends RequestTestAdapter<PutBucketCORSRequest, PutBucketCORSResult> {
    @Override
    protected PutBucketCORSRequest newRequestInstance() {

        PutBucketCORSRequest putBucketCORSRequest = new PutBucketCORSRequest(TestConst.PERSIST_BUCKET);
        CORSConfiguration.CORSRule corsRule = new CORSConfiguration.CORSRule();
        corsRule.id = "cors" + new Random(System.currentTimeMillis()).nextInt();
        corsRule.maxAgeSeconds = 5000;
        corsRule.allowedOrigin = "cloud.tencent.com";
        corsRule.allowedMethod = new ArrayList<>();
        corsRule.allowedMethod.add("PUT");
        corsRule.allowedMethod.add("GET");
        corsRule.allowedHeader = new ArrayList<>();
        corsRule.allowedHeader.add("Host");
        corsRule.allowedHeader.add("Except");
        corsRule.exposeHeader = new ArrayList<>();
        corsRule.exposeHeader.add("x-cos-meta-1");
        List<CORSConfiguration.CORSRule> corsRules = new ArrayList<>();
        corsRules.add(corsRule);
        putBucketCORSRequest.addCORSRules(corsRules);
        return putBucketCORSRequest;
    }

    @Override
    protected PutBucketCORSResult exeSync(PutBucketCORSRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketCORS(request);
    }

    @Override
    protected void exeAsync(PutBucketCORSRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketCORSAsync(request, resultListener);
    }
}