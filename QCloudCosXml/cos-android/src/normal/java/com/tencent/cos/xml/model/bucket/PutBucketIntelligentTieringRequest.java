package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * <p>
 * Created by rickenwang on 2020/10/9.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class PutBucketIntelligentTieringRequest extends BucketRequest {

    public static final String STATUS_SUSPEND = "Suspended";
    public static final String STATUS_ENABLED = "Enabled";

    private IntelligentTieringConfiguration configuration;

    public PutBucketIntelligentTieringRequest(String bucket) {
        super(bucket);
    }

    @Override
    public String getMethod() {
        return "PUT";
    }

    public void setConfiguration(IntelligentTieringConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("intelligenttiering", null);
        return super.getQueryString();
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(configuration == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "configuration must not be null");
        }
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        String body = QCloudXmlUtils.toXml(configuration);
        return RequestBodySerializer.bytes("text/plain", body.getBytes(),
                0, body.length());
    }
}
