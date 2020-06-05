package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * 获取Bucket的ACL信息。
 * </p>
 *
 */
final public class GetBucketACLRequest extends BucketRequest {

    public GetBucketACLRequest(String bucket) {
        super(bucket);
    }

    public GetBucketACLRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }


    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("acl", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }


}
