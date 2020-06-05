package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * Created by bradyxiao on 2019/9/3.
 * Copyright (c) 2016-2020 Tencent QCloud All rights reserved.
 */
public class GetBucketDomainRequest extends BucketRequest {

    public GetBucketDomainRequest(){
        this(null);
    }

    public GetBucketDomainRequest(String bucket) {
        super(bucket);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("domain", null);
        return super.getQueryString();
    }
}
