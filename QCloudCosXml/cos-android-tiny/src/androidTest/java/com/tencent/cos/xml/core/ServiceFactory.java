package com.tencent.cos.xml.core;

import static com.tencent.cos.xml.core.TestUtils.getContext;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class ServiceFactory {

    public static ServiceFactory INSTANCE = new ServiceFactory();

    public CosXmlSimpleService newDefaultService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setConnectionTimeout(4000)
                .setSocketTimeout(4000)
                .setTransferThreadControl(false)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return newService(cosXmlServiceConfig);
    }

//    public CosXmlSimpleService newSignerService(QCloudSigner signer) {
//
//        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
//                .isHttps(true)
//                .setDebuggable(true)
//                .setConnectionTimeout(4000)
//                .setSocketTimeout(4000)
//                .setRegion(TestConst.PERSIST_BUCKET_REGION)
//                .builder();
//
//        return newSignerService(cosXmlServiceConfig, signer);
//    }

    public CosXmlSimpleService newCDNService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .setDebuggable(true)
                .setHostFormat("${bucket}.file.myqcloud.com")
                .builder();

        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig);
    }

    private CosXmlSimpleService newService(CosXmlServiceConfig cosXmlServiceConfig) {
        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,600) );

    }

//    private CosXmlSimpleService newSignerService(CosXmlServiceConfig cosXmlServiceConfig, QCloudSigner signer) {
//        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig, signer);
//
//    }
}
