package com.tencent.cos.xml.model.ci;


import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.RecognitionResult;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

/**
 * <p>
 * Created by rickenwang on 2021/5/17.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */

public class SensitiveContentRecognitionResult extends CosXmlResult {

    @XmlElement(name = "RecognitionResult")
    public RecognitionResult recognitionResult;
    
    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
        recognitionResult = QCloudXmlUtils.fromXml(response.byteStream(), RecognitionResult.class);
    }
}
