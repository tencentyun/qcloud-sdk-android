package com.tencent.cos.xml.utils;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 收敛fromXml和toXml
 * <p>
 * Created by jordanqin on 2023/3/24 16:48.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class QCloudXmlUtils {
    public static <T> T fromXml(InputStream inputStream, Class<T> clazz) throws CosXmlClientException {
        try {
            return QCloudXml.fromXml(inputStream, clazz);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    public static <T> String toXml(T value) throws CosXmlClientException {
        try {
            return QCloudXml.toXml(value);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }
}
