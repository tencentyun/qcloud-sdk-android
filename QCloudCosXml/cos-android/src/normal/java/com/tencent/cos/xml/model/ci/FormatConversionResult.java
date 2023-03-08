package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * <p>
 * Created by rickenwang on 2021/5/17.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class FormatConversionResult extends CosXmlResult {
    /**
     * @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response)  throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
    }
}
