package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * <p>
 * 确认该 Bucket 是否存在，以及是否有权限访问。
 * </p>
 *
 */
final public class HeadBucketRequest extends BucketRequest {

    public HeadBucketRequest(String bucket){
        super(bucket);
    }

    public HeadBucketRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.HEAD;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }
}
