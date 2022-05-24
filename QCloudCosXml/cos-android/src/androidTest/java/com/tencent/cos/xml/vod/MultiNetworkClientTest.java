package com.tencent.cos.xml.vod;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.core.ServiceFactory;
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
public class MultiNetworkClientTest {

    @Test public void testSimpleUpload() {
        TransferManager defaultTransferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        TransferManager cdnTransferManager = ServiceFactory.INSTANCE.newCdnTransferManager();

        String bucket = TestConst.PERSIST_BUCKET;
        String cosPath = TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH;
        String localPath = TestUtils.localPath("1642166999131.m4a");
        COSXMLUploadTask defaultUploadTask = defaultTransferManager.upload(bucket, cosPath,
                localPath , null);

        COSXMLUploadTask cdnUploadTask = cdnTransferManager.upload(bucket, cosPath,
                localPath , null);

        final CountDownLatch locker = new CountDownLatch(2);

        defaultUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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

        cdnUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
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

        TestUtils.assertCOSXMLTaskSuccess(cdnUploadTask);
        TestUtils.assertCOSXMLTaskSuccess(defaultUploadTask);
    }
}
