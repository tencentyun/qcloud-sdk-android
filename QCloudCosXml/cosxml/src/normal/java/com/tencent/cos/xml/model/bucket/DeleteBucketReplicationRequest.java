package com.tencent.cos.xml.model.bucket;


import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * Created by bradyxiao on 2017/11/6.
 *
 * <p>实现删除存储桶中用户跨区域复制配置</p>
 *
 */

public class DeleteBucketReplicationRequest extends BucketRequest {

    public DeleteBucketReplicationRequest(String bucket){
       super(bucket);
    }

    public DeleteBucketReplicationRequest(){
        super(null);
    }

    @Override
    public String getMethod() {
        return RequestMethod.DELETE;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("replication", null);
        return queryParameters;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

}
