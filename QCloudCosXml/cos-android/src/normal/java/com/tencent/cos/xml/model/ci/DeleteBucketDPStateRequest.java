package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/6/18.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class DeleteBucketDPStateRequest extends BucketRequest {

    public DeleteBucketDPStateRequest(String bucket) {
        super(bucket);
    }

    public DeleteBucketDPStateRequest(String bucket, String region) {
        super(bucket);
        this.region = region;
    }

    @Override
    public String getMethod() {
        return RequestMethod.DELETE;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/docbucket";
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, CosXmlServiceConfig.CI_HOST_FORMAT);
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }
}
