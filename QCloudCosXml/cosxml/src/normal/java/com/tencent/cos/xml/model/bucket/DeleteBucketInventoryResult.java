package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

public class DeleteBucketInventoryResult extends CosXmlResult {

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
    }
}
