package com.tencent.cos.xml.utils;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;

/**
 * <p>
 * Created by rickenwang on 2021/4/20.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class COSUtils {


    public static Exception mergeException(CosXmlClientException clientException, CosXmlServiceException serviceException) {
        return clientException != null ? clientException : serviceException;
    }
}
