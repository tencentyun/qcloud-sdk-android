package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/6/17.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class GetBucketDPStateRequest extends BucketRequest {

    public GetBucketDPStateRequest(String bucket, String region) {
        this(bucket);
        this.region = region;
    }

    public GetBucketDPStateRequest(String bucket) {
        super(bucket);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/docbucket";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, CosXmlServiceConfig.CI_HOST_FORMAT);
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("bucketNames", bucket);
        return queryParameters;
    }
}
