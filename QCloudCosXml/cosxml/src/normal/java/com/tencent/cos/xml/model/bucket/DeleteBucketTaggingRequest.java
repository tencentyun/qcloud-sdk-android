package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * 删除Bucket的Tag信息。
 * </p>
 * <p>
 * 暂时不可用。
 * </p>
 *
 */
final public class DeleteBucketTaggingRequest extends BucketRequest {

    public DeleteBucketTaggingRequest(String bucket) {
       super(bucket);
    }

    public DeleteBucketTaggingRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.DELETE ;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("tagging", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }


}
