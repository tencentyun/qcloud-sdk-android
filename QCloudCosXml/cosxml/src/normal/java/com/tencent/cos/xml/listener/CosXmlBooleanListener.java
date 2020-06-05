package com.tencent.cos.xml.listener;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;

/**
 * <p>
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public interface CosXmlBooleanListener {

    void onSuccess(boolean bool);

    void onFail(CosXmlClientException exception, CosXmlServiceException serviceException);

}
