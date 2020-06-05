package com.tencent.cos.xml.model.bucket;


import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;


import java.util.Map;


/**
 * <p>
 * 删除Bucket的CORS设置。
 * </p>
 */
final public class DeleteBucketCORSRequest extends BucketRequest {

    public DeleteBucketCORSRequest(String bucket) {
        super(bucket);
    }

    public DeleteBucketCORSRequest() {
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.DELETE ;
    }


    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("cors", null);
        return queryParameters;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

}
