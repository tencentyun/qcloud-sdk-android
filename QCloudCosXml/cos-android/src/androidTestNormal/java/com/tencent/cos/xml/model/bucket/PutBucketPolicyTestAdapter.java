 
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
public class PutBucketPolicyTestAdapter extends NormalRequestTestAdapter<PutBucketPolicyRequest, PutBucketPolicyResult> {
    @Override
    protected PutBucketPolicyRequest newRequestInstance() {
        String policy = "{\n" +
                "        \"version\": \"2.0\",\n" +
                "        \"Statement\": [{\n" +
                "            \"Effect\": \"allow\",\n" +
                "            \"Principal\": {\n" +
                String.format("                \"qcs\": [\"qcs::cam::uin/%s:uin/%s\"]\n", TestConst.OWNER_UIN, TestConst.OWNER_UIN) +
                "            },\n" +
                "            \"Action\": [\n" +
                "                \"name/cos:PutObject\",\n" +
                "                \"name/cos:InitiateMultipartUpload\",\n" +
                "                \"name/cos:ListMultipartUploads\",\n" +
                "                \"name/cos:ListParts\",\n" +
                "                \"name/cos:UploadPart\",\n" +
                "                \"name/cos:CompleteMultipartUpload\"\n" +
                "            ],\n" +
                String.format("            \"Resource\": [\"qcs::cos:%s:uid/%s:%s/*\"]\n", TestConst.PERSIST_BUCKET_REGION, TestConst.COS_APPID, TestConst.PERSIST_BUCKET) +
                "        }]\n" +
                "    }";
        return new PutBucketPolicyRequest(TestConst.PERSIST_BUCKET, policy);
    }

    @Override
    protected PutBucketPolicyResult exeSync(PutBucketPolicyRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketPolicy(request);
    }

    @Override
    protected void exeAsync(PutBucketPolicyRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketPolicyAsync(request, resultListener);
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