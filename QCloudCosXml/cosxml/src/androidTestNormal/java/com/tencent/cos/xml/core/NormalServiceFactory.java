package com.tencent.cos.xml.core;

import com.tencent.cos.xml.CIService;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import static com.tencent.cos.xml.core.TestUtils.getContext;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class NormalServiceFactory {

    public static NormalServiceFactory INSTANCE = new NormalServiceFactory();

    public CosXmlService newDefaultService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setAppidAndRegion(TestConst.COS_APPID, TestConst.PERSIST_BUCKET_REGION)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return newService(cosXmlServiceConfig);
    }

    public CosXmlService newSignInUrlService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .setDebuggable(true)
                .setSignInUrl(true)
                .builder();

        return newService(cosXmlServiceConfig);
    }

    public CosXmlService newCDNService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .setDebuggable(true)
                .setHostFormat("${bucket}.file.myqcloud.com")
                .builder();

        return new CosXmlService(getContext(), cosXmlServiceConfig);
    }

    public CIService newCIService() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setAppidAndRegion(TestConst.COS_APPID, TestConst.PERSIST_BUCKET_REGION)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return new CIService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,600) );
    }

    public TransferManager newDefaultTransferManager() {

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferManager(newDefaultService(), transferConfig);
    }

    public TransferManager newForceSimpleUploadTransferManager() {

        TransferConfig transferConfig = new TransferConfig.Builder()
                .setForceSimpleUpload(true)
                .build();
        return new TransferManager(newDefaultService(), transferConfig);
    }

    public TransferManager newQuicTransferManager() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setRegion(TestConst.QUIC_BUCKET_REGION)
                .setDebuggable(true)
                .enableQuic(true)
                .builder();

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        CosXmlService cosXmlService = newService(cosXmlServiceConfig);
        String host = TestConst.QUIC_BUCKET + ".cos." + TestConst.QUIC_BUCKET_REGION + ".myqcloud.com";
        try {
            cosXmlService.addCustomerDNS(host, new String[] {TestConst.QUIC_TEST_IP});
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        return new TransferManager(cosXmlService, transferConfig);
    }

    public TransferManager newAccelerateTransferManager() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setAccelerate(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return new TransferManager(newService(cosXmlServiceConfig), new TransferConfig.Builder().build());
    }

    public TransferManager newCdnTransferManager() {
        return new TransferManager(newCDNService(), new TransferConfig.Builder().build());
    }

    private CosXmlService newService(CosXmlServiceConfig cosXmlServiceConfig) {
        return new CosXmlService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,600) );

    }

}
