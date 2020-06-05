package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * Created by bradyxiao on 2017/11/6.
 * <p>
 *     Get Bucket Versioning 接口实现获得存储桶的版本控制信息.
 * </p>
 */

public class GetBucketVersioningRequest extends BucketRequest {

    public GetBucketVersioningRequest(String bucket){
        super(bucket);
    }

    public GetBucketVersioningRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
       queryParameters.put("versioning", null);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }
}
