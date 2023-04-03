package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.IntelligentTieringConfiguration;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * <p>
 * Created by rickenwang on 2020/11/10.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class GetBucketIntelligentTieringResult extends CosXmlResult {

    private IntelligentTieringConfiguration configuration;

    public IntelligentTieringConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
        configuration = QCloudXmlUtils.fromXml(response.byteStream(), IntelligentTieringConfiguration.class);
    }
}
