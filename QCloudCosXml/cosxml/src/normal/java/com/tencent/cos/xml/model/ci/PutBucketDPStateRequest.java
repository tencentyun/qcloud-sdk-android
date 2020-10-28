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

public class PutBucketDPStateRequest extends BucketRequest {

    private boolean enable = false;

    public PutBucketDPStateRequest(String bucket) {
        this(bucket, null);
    }

    public PutBucketDPStateRequest(String bucket, String region) {
        super(bucket);
        this.region = region;
        addNoSignHeader("Content-Type");
        addNoSignHeader("Content-Length");
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
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
        return RequestBodySerializer.string(HttpConstants.ContentType.TEXT_PLAIN, "");
    }
}
