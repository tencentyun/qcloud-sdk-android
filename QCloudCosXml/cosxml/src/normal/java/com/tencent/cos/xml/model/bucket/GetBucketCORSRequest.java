package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * 获取Bucket的跨域信息CORS。
 * </p>
 *
 */
final public class GetBucketCORSRequest extends BucketRequest {

    public GetBucketCORSRequest(String bucket){
      super(bucket);
    }

    public GetBucketCORSRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("cors", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

}
