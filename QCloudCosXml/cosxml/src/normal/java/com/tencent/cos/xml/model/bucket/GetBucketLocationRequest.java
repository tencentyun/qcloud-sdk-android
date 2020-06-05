package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * 获取Bucket的地域信息
 * </p>
 *
 */
final public class GetBucketLocationRequest extends BucketRequest {

    public GetBucketLocationRequest(String bucket){
        super(bucket);
    }

    public GetBucketLocationRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }


    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("location", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }
}
