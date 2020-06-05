package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * 获取某个 Bucket 下的某个 Object 的访问权限ACL。
 * </p>
 *
 */
final public class GetObjectACLRequest extends ObjectRequest{

    public GetObjectACLRequest(String bucket, String cosPath){
        super(bucket, cosPath);
    }

    public GetObjectACLRequest(){
        super(null, null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("acl", null);
        return queryParameters;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }
}
