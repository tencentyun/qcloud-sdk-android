package com.tencent.cos.xml.core;

import static com.tencent.cos.xml.core.TestUtils.getContext;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.crypto.KMSEncryptionMaterialsProvider;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferService;
import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.QCloudHttpRequest;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class ServiceFactory {

    public static ServiceFactory INSTANCE = new ServiceFactory();

    public CosXmlService newDefaultService() {

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

    public CosXmlService newDefaultServiceBySessionCredentials() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return newServiceBySessionCredentials(cosXmlServiceConfig);
    }

    public CosXmlService newQuicService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .enableQuic(true)
                .setPort(443)
                .setRegion(TestConst.QUIC_BUCKET_REGION)
                .builder();

        CosXmlService cosXmlSimpleService = newService(cosXmlServiceConfig);
        try {
            cosXmlSimpleService.addCustomerDNS("{bucket}.cos.{region}.myqcloud.com"
                    .replace("{bucket}", "mobile-ut-1253960454")
                    .replace("{region}", TestConst.QUIC_BUCKET_REGION),
                    new String[]{TestConst.QUIC_TEST_IP});
        } catch (CosXmlClientException clientException) {
            clientException.printStackTrace();
        }
        return cosXmlSimpleService;
    }


    public CosXmlService newSelfService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setConnectionTimeout(4000)
                .setSocketTimeout(4000)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return new CosXmlService(getContext(), cosXmlServiceConfig,
                new QCloudSelfSigner() {

                    /**
                     * 对请求进行签名
                     *
                     * @param request 需要签名的请求
                     * @throws QCloudClientException 客户端异常
                     */
                    @Override
                    public void sign(QCloudHttpRequest request) throws QCloudClientException {

                        // 1. 把 request 的请求参数传给服务端计算签名
                        String auth = "get auth from server";

                        // 2. 给请求添加签名
                        request.addHeader(HttpConstants.Header.AUTHORIZATION, auth);
                    }
                });
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

    public CosXmlService newSignerService(QCloudSigner signer) {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setConnectionTimeout(4000)
                .setSocketTimeout(4000)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return newSignerService(cosXmlServiceConfig, signer);
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

    public TransferManager newDefaultTransferManager() {

        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)

                .build();
        return new TransferManager(newDefaultService(), transferConfig);
    }

    public TransferManager newDefaultTransferManagerBySessionCredentials() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .build();
        return new TransferManager(newDefaultServiceBySessionCredentials(), transferConfig);
    }

    public TransferService newDefaultTransferService() {

        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .build();
        return new TransferService(newDefaultService(), transferConfig);
    }

    public TransferManager newSingerTransferManager(QCloudSigner signer) {

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferManager(newSignerService(signer), transferConfig);
    }

    public TransferService newQuicTransferService() {

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferService(newQuicService(), transferConfig);
    }

    public TransferService newCesTransferService() {
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        return new TransferService(newDefaultService(), transferConfig,
                new KMSEncryptionMaterialsProvider("kms-8xy4m0eb"));
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

        return new TransferManager(newService(cosXmlServiceConfig), new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)
                .build());
    }

    public TransferManager newCdnTransferManager() {
        return new TransferManager(newCDNService(), new TransferConfig.Builder().build());
    }

    private CosXmlService newServiceBySessionCredentials(CosXmlServiceConfig cosXmlServiceConfig) {
        return new CosXmlService(getContext(), cosXmlServiceConfig,
                new MySessionCredentialProvider());
    }

    private CosXmlService newService(CosXmlServiceConfig cosXmlServiceConfig) {
        return new CosXmlService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,600) );

    }

    private CosXmlService newSignerService(CosXmlServiceConfig cosXmlServiceConfig, QCloudSigner signer) {
        return new CosXmlService(getContext(), cosXmlServiceConfig, signer);

    }
}
