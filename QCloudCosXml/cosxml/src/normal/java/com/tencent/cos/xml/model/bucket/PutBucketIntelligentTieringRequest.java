package com.tencent.cos.xml.model.bucket;

import android.util.Xml;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration;
import com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration$$XmlAdapter;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * <p>
 * Created by rickenwang on 2020/10/9.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class PutBucketIntelligentTieringRequest extends BucketRequest {

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
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        XmlSerializer xmlSerializer = Xml.newSerializer();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            xmlSerializer.setOutput(byteArrayOutputStream, "utf-8");
            new IntelligentTieringConfiguration$$XmlAdapter().toXml(xmlSerializer, configuration);
            xmlSerializer.flush();
            return RequestBodySerializer.bytes("text/plain", byteArrayOutputStream.toByteArray(),
                    0, byteArrayOutputStream.size());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), "", e.getCause());
        }
    }
}
