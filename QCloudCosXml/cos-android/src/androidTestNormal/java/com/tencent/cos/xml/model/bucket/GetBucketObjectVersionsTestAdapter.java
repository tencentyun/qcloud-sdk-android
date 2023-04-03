 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

import org.junit.Assert;

public class GetBucketObjectVersionsTestAdapter extends NormalRequestTestAdapter<GetBucketObjectVersionsRequest, GetBucketObjectVersionsResult> {
    @Override
    protected GetBucketObjectVersionsRequest newRequestInstance() {
        GetBucketObjectVersionsRequest request = new GetBucketObjectVersionsRequest(TestConst.PERSIST_BUCKET);
        request.setPrefix("do_not_remove/");
        request = new GetBucketObjectVersionsRequest(TestConst.PERSIST_BUCKET, "do_not_remove/", "/", null, null);
        request.setKeyMarker(null);
        request = new GetBucketObjectVersionsRequest(TestConst.PERSIST_BUCKET, "/", null, null);
        request.setVersionIdMarker(null);
        request.setDelimiter("/");
        request.setEncodingType("url");
        request.setMaxKeys(1000);
        Assert.assertEquals(request.getDelimiter(), "/");
        Assert.assertNull(request.getKeyMarker());
        Assert.assertNull(request.getVersionIdMarker());
        return request;
    }

    @Override
    protected GetBucketObjectVersionsResult exeSync(GetBucketObjectVersionsRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getBucketObjectVersions(request);
    }

    @Override
    protected void exeAsync(GetBucketObjectVersionsRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getBucketObjectVersionsAsync(request, resultListener);
    }
}  