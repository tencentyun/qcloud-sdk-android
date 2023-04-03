 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class GetBucketVersionsTestAdapter extends NormalRequestTestAdapter<ListBucketVersionsRequest, ListBucketVersionsResult> {
    @Override
    protected ListBucketVersionsRequest newRequestInstance() {
        ListBucketVersionsRequest request = new ListBucketVersionsRequest(TestConst.PERSIST_BUCKET);
        request.setPrefix("do_not_remove/");
        request.setKeyMarker(null);
        request.setVersionIdMarker(null);
        request.setDelimiter("/");
        request.setEncodingType("url");
        request.setMaxKeys(1000);
        return request;
    }

    @Override
    protected ListBucketVersionsResult exeSync(ListBucketVersionsRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.listBucketVersions(request);
    }

    @Override
    protected void exeAsync(ListBucketVersionsRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.listBucketVersionsAsync(request, resultListener);
    }
}  