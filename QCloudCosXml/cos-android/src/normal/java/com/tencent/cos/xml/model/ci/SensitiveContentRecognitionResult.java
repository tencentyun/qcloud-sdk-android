package com.tencent.cos.xml.model.ci;


import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.RecognitionResult;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

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
        try {
            recognitionResult = QCloudXml.fromXml(response.byteStream(), RecognitionResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    


   

}
