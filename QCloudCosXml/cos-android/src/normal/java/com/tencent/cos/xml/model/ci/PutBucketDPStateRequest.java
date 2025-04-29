package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/6/18.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class PutBucketDPStateRequest extends BucketRequest {
    public PutBucketDPStateRequest(String bucket) {
        this(bucket, null);
    }

    public PutBucketDPStateRequest(String bucket, String region) {
        super(bucket);
        this.region = region;
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
    protected RequestBodySerializer xmlBuilder() throws XmlPullParserException, IOException {
        return RequestBodySerializer.bytes(COSRequestHeaderKey.TEXT_PLAIN, "".getBytes("utf-8"));
    }
}
