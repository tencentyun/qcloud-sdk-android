package com.tencent.cos.xml.core;

import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.transfer.COSXMLTask;

/**
 * <p>
 * Created by jordanqin on 2023/3/29 19:18.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class MyOnSignatureListener implements COSXMLTask.OnSignatureListener{
    @Override
    public String onGetSign(CosXmlRequest cosXmlRequest) {
        // TODO: 2023/3/29 去掉 COSXMLService中的签名 用这个签名
        return "";
    }
}
