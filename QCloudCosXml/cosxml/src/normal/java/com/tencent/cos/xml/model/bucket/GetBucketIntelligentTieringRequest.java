package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * Created by rickenwang on 2020/11/10.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class GetBucketIntelligentTieringRequest extends BucketRequest {

    /**
     * 存储桶相关请求基类
     *
     * @param bucket 存储桶名称
     */
    public GetBucketIntelligentTieringRequest(String bucket) {
        super(bucket);
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("intelligenttiering", null);
        return super.getQueryString();
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }
}
