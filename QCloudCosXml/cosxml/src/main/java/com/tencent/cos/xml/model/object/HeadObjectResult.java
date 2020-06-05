package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

import java.net.HttpURLConnection;

final public class HeadObjectResult extends CosXmlResult {
    public String cosObjectType;
    public String cosStorageClass;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        cosObjectType = response.header("x-cos-object-type");
        cosStorageClass = response.header("x-cos-storage-class");
    }

}
