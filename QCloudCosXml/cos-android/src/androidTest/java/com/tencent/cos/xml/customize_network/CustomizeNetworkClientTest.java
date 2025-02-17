package com.tencent.cos.xml.customize_network;

import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_BIG_OBJECT_SIZE;
import static com.tencent.cos.xml.core.TestUtils.getContext;
import static com.tencent.cos.xml.core.TestUtils.smallFilePath;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.BasePutObjectResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jordanqin on 2024/12/30 21:38.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class CustomizeNetworkClientTest {
    private CosXmlSimpleService newCustomizeNetworkService() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .setCustomizeNetworkClient(new CustomizeNetworkClient())
//                .setCustomizeNetworkClient(new CustomizeOkHttpNetworkClient())
                .builder();

        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,60000) );
    }

    private TransferManager newCustomizeNetworkTransferManager() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .build();
        Log.d(TestConst.UT_TAG, String.valueOf(transferConfig.getDivisionForCopy()));
        return new TransferManager(newCustomizeNetworkService(), transferConfig);
    }

    @Test
    public void testBasePutObject() {
        CosXmlSimpleService cosXmlSimpleService = newCustomizeNetworkService();
        PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                smallFilePath());
        final TestLocker testLocker = new TestLocker();
        cosXmlSimpleService.basePutObjectAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                BasePutObjectResult result1 = (BasePutObjectResult) result;
                TestUtils.printXML(result1);
                Assert.assertFalse(TextUtils.isEmpty(result1.eTag));
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(clientException != null) {
                    clientException.printStackTrace();
                }
                if(serviceException != null) {
                    serviceException.printStackTrace();
                }
                Assert.fail();
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test public void testUploadBigFileByPath() {
        testUploadBigFileByPath(newCustomizeNetworkTransferManager());
    }

    private void testUploadBigFileByPath(TransferManager transferManager) {
        String localPath = TestUtils.localPath("1642166999131.m4a");
        try {
            TestUtils.createFile(localPath, PERSIST_BUCKET_BIG_OBJECT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                localPath, null);
        final TestLocker testLocker = new TestLocker();

        uploadTask.setInitMultipleUploadListener(initiateMultipartUpload -> TestUtils.print(initiateMultipartUpload.uploadId));
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i(TestConst.UT_TAG, "transfer progress is " + complete + "/" + target);
            }
        });
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TestUtils.printXML(result);
                TestUtils.printError("upload success");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if(clientException != null) {
                    clientException.printStackTrace();
                }
                if(serviceException != null) {
                    serviceException.printStackTrace();
                }
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock(500000);
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }
}
