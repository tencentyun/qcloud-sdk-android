package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.CORSConfiguration;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Generate by auto
public class PutBucketCORSTestAdapter extends NormalRequestTestAdapter<PutBucketCORSRequest, PutBucketCORSResult> {
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
        corsRule.allowedHeader.add("Access-Control-Test");
        corsRule.allowedHeader.add("Access-Control-Request-Headers");
        corsRule.exposeHeader = new ArrayList<>();
        corsRule.exposeHeader.add("x-cos-meta-1");
        corsRule.exposeHeader.add("Access-Control-Test");
        corsRule.exposeHeader.add("Access-Control-Request-Header");
        List<CORSConfiguration.CORSRule> corsRules = new ArrayList<>();
        corsRules.add(corsRule);
        putBucketCORSRequest.addCORSRules(corsRules);

        CORSConfiguration.CORSRule corsRule1 = new CORSConfiguration.CORSRule();
        corsRule1.id = "cors1" + new Random(System.currentTimeMillis()).nextInt();
        corsRule1.maxAgeSeconds = 3000;
        corsRule1.allowedOrigin = "cloud.tencent.com";
        corsRule1.allowedMethod = new ArrayList<>();
        corsRule1.allowedMethod.add("PUT");
        corsRule1.allowedMethod.add("GET");
        corsRule1.allowedHeader = new ArrayList<>();
        corsRule1.allowedHeader.add("Host");
        corsRule1.allowedHeader.add("Except");
        corsRule1.exposeHeader = new ArrayList<>();
        corsRule1.exposeHeader.add("x-cos-meta-1");
        putBucketCORSRequest.addCORSRule(corsRule1);

        Assert.assertNotNull(putBucketCORSRequest.getCorsConfiguration());
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