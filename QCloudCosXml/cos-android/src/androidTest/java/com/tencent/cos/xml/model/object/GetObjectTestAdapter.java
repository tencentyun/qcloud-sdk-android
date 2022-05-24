package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

import org.junit.Assert;

// Generate by auto
public class GetObjectTestAdapter extends RequestTestAdapter<GetObjectRequest, GetObjectResult> {
    @Override
    protected GetObjectRequest newRequestInstance() {
        GetObjectRequest request = new GetObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.localParentPath(), TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        request.setCosPath(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        Assert.assertEquals(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH, request.getCosPath());
        request.setVersionId(null);
        request.setRange(0);
        request.setSavePath(TestUtils.localParentPath());
        request.setSaveFileName(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        Assert.assertNotNull(request.getRange());
        request.setRspContentType("multipart/form-data; boundary=something");
        Assert.assertEquals("multipart/form-data; boundary=something", request.getRspContentType());
        request.setRspContentLanguage("en-US");
        Assert.assertEquals("en-US", request.getRspContentLanguage());
        request.setRspExpires("0");
        Assert.assertEquals("0", request.getRspExpires());
        request.setRspCacheControl("no-cache");
        Assert.assertEquals("no-cache", request.getRspCacheControl());
        request.setRspContentDispositon("inline");
        Assert.assertEquals("inline", request.getRspContentDispositon());
        request.setRspContentEncoding("deflate");
        Assert.assertEquals("deflate", request.getRspContentEncoding());
        request.setIfModifiedSince("Wed, 21 Oct 2009 07:28:00 GMT");
        request.setIfUnmodifiedSince("Wed, 21 Oct 2052 07:28:00 GMT");
        request.setIfMatch(null);
        request.setIfNONEMatch("none_match_etag");
        request.setTrafficLimit(838860800);
        return request;
    }

    @Override
    protected GetObjectResult exeSync(GetObjectRequest request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getObject(request);
    }

    @Override
    protected void exeAsync(GetObjectRequest request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getObjectAsync(request, resultListener);
    }
}