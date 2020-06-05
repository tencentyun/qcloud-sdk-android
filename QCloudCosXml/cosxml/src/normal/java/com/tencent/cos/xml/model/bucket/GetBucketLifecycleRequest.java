package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * 获取Bucket的生命周期规则
 * </p>
 */
final public class GetBucketLifecycleRequest extends BucketRequest {

    public GetBucketLifecycleRequest(String bucket){
        super(bucket);
    }

    public GetBucketLifecycleRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("lifecycle", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

}
