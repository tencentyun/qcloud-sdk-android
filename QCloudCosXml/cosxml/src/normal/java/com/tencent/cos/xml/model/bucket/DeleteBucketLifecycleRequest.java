package com.tencent.cos.xml.model.bucket;





import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * 删除Bucket的生命周期规则
 * </p>
 *
 */
final public class DeleteBucketLifecycleRequest extends BucketRequest {

    public DeleteBucketLifecycleRequest(String bucket) {
        super(bucket);
    }

    public DeleteBucketLifecycleRequest() {
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.DELETE ;
    }



    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("lifecycle", null);
        return queryParameters;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

}
