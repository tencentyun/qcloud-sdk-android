package com.tencent.cos.xml.utils;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.http.HttpResponse;

import java.io.IOException;

/**
 * <p>
 * Created by jordanqin on 2024/6/5 17:25.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class QCloudJsonUtils {
    public static <T> T fromJson(HttpResponse httpResponse, Class<T> clazz) throws CosXmlClientException {
        try {
            return GsonSingleton.getInstance().fromJson(httpResponse.string(), clazz);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        }
    }

    public static String toJson(Object value) {
        return GsonSingleton.getInstance().toJson(value);
    }
}
