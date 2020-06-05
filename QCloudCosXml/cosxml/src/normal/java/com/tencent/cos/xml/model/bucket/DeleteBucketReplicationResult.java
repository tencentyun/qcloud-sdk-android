package com.tencent.cos.xml.model.bucket;


import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * Created by bradyxiao on 2017/11/6.
 */

public class DeleteBucketReplicationResult extends CosXmlResult {
    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
    }
}
