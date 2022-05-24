package com.tencent.cos.xml.qqlive;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static com.tencent.cos.xml.core.TestUtils.getContext;

/**
 * <p>
 * Created by rickenwang on 2022/1/19.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class UploadTest {

    @Test public void testSimpleUpload() {
        TransferManager transferManager = newDefaultTransferManager();
        String bucket = TestConst.PERSIST_BUCKET;
        String cosPath = TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH;
        String localPath = TestUtils.localPath("1642166999131.m4a");
        COSXMLUploadTask uploadTask = transferManager.upload(bucket, cosPath,
                localPath , null);

        final CountDownLatch locker = new CountDownLatch(1);

        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TestUtils.printError("upload success");
                locker.countDown();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                locker.countDown();
            }
        });

        try {
            locker.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);

    }

    public TransferManager newDefaultTransferManager() {

        TransferConfig transferConfig = new TransferConfig.Builder()
                .setDivisionForUpload(2 * 1024 * 1024)
                .setSliceSizeForUpload(1024 * 1024)

                .build();
        return new TransferManager(newDefaultService(), transferConfig);
    }

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

    private CosXmlService newService(CosXmlServiceConfig cosXmlServiceConfig) {
        return new CosXmlService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,600) );

    }
}
