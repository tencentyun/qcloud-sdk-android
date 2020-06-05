package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * <p>
 * 删除Bucket。
 * </p>
 * <p>
 * 删除的Bucket必须是一个空的Bucket，否则会删除失败。
 * </p>
 *
 */
final public class DeleteBucketRequest extends BucketRequest {

    public DeleteBucketRequest(String bucket) {
        super(bucket);
    }

    public DeleteBucketRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.DELETE ;
    }


    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

}
