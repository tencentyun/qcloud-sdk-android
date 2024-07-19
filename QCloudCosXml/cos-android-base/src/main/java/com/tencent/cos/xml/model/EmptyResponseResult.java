package com.tencent.cos.xml.model;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * 空响应结果
 * <p>
 * Created by jordanqin on 2024/7/18 14:52.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class EmptyResponseResult extends CosXmlResult {
    /**
     * @see CosXmlResult#parseResponseBody(HttpResponse)
     */
    @Override
    public void parseResponseBody(HttpResponse response)  throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
    }
}
