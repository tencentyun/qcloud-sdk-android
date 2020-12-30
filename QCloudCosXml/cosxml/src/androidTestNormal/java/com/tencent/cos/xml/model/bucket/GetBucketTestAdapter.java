 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

import org.junit.Assert;

// Generate by auto
public class GetBucketTestAdapter extends NormalRequestTestAdapter<GetBucketRequest, GetBucketResult> {
    @Override
    protected GetBucketRequest newRequestInstance() {
        GetBucketRequest request = new GetBucketRequest(TestConst.PERSIST_BUCKET);
        request.setPrefix(null);
        request = new GetBucketRequest(TestConst.PERSIST_BUCKET_REGION, TestConst.PERSIST_BUCKET, null);
        Assert.assertNull(request.getPrefix());
        request.setDelimiter("/");
        Assert.assertEquals("/", request.getDelimiter());
        request.setEncodingType("url");
        Assert.assertEquals("url", request.getEncodingType());
        request.setMaxKeys(50);
        Assert.assertEquals(50, request.getMaxKeys());
        request.setMarker(null);
        Assert.assertNull(request.getMarker());
        return request;
    }

    @Override
    protected GetBucketResult exeSync(GetBucketRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucket(request);
    }

    @Override
    protected void exeAsync(GetBucketRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketAsync(request, resultListener);
    }
}  