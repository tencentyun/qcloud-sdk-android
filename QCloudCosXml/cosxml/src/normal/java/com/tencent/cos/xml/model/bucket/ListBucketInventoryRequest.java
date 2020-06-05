package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

public class ListBucketInventoryRequest extends BucketRequest {

    private String continuationToken;

    public ListBucketInventoryRequest(String bucket) {
        super(bucket);
    }

    public ListBucketInventoryRequest(){
        this(null);
    }

    public void setContinuationToken(String continuationToken){
        this.continuationToken = continuationToken;
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("inventory", null);
        if(continuationToken != null){
            queryParameters.put("continuation-token", continuationToken);
        }
        return super.getQueryString();
    }
}
