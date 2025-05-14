package com.tencent.cos.xml.server_sign;

import static com.tencent.cos.xml.core.TestUtils.bigPlusFilePath;
import static com.tencent.cos.xml.core.TestUtils.getContext;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.crypto.Headers;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Created by jordanqin on 2025/5/12.
 * Copyright 2010-2023 Tencent Cloud. All Rights Reserved.
 */
public class ServerSignTest {
    CosXmlSimpleService singleCosXmlSimpleService;
    TransferManager singleTransferManager;
    CosXmlSimpleService callbackCosXmlSimpleService;
    TransferManager callbackTransferManager;

    @Before
    public void before() {
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET)
                .builder();
        TransferConfig transferConfig = new TransferConfig.Builder()
                .build();

        singleCosXmlSimpleService = new CosXmlSimpleService(getContext(), cosXmlServiceConfig);
        singleTransferManager = new TransferManager(singleCosXmlSimpleService, transferConfig);

        callbackCosXmlSimpleService = new CosXmlSimpleService(getContext(), cosXmlServiceConfig, new MySelfSigner());
        callbackTransferManager = new TransferManager(callbackCosXmlSimpleService, transferConfig);
    }

    @Test
    public void testSingleServerSign() {
        uploadSmallFile(false);
        testUploadBigFile(false);
    }

    @Test
    public void testCallbackServerSign() {
        uploadSmallFile(true);
        testUploadBigFile(true);
    }

    private void uploadSmallFile(boolean isCallback)  {
        TransferManager transferManager = isCallback ? callbackTransferManager : singleTransferManager;

        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.filePath("N好.", 1024));

        if(!isCallback){
            MySelfSigner.SignResult signResult;
            try {
                signResult = MySelfSigner.getSignResult(putObjectRequest.getMethod(), putObjectRequest.getHost(), putObjectRequest.getCosPath(),
                        putObjectRequest.getRequestHeaders(), putObjectRequest.getQueryString());
            } catch (QCloudClientException e) {
                throw new RuntimeException(e);
            }
            putObjectRequest.setSign(signResult.authorization, signResult.securityToken);
            if(signResult.securityToken != null) {
                try {
                    putObjectRequest.setRequestHeaders(Headers.SECURITY_TOKEN, signResult.securityToken, false);
                } catch (CosXmlClientException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i("QCloudTest", result.headers.toString());
                QCloudLogger.i("QCloudTest", result.printResult());
                TestUtils.parseBadResponseBody(result);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    private void testUploadBigFile(boolean isCallback) {
        TransferManager transferManager = isCallback ? callbackTransferManager : singleTransferManager;

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                bigPlusFilePath());

        if(!isCallback){
            MySelfSigner.SignResult signResult;
            try {
                // 分块上传由多个不同的请求组成，所以很多参数无法在开始时确定，这些参数后续要进行免签，或者使用回调的方式一一签名
                signResult = MySelfSigner.getSignResult(null, putObjectRequest.getHost(), putObjectRequest.getCosPath(),
                       null, null);
            } catch (QCloudClientException e) {
                throw new RuntimeException(e);
            }
            putObjectRequest.setSign(signResult.authorization, signResult.securityToken);
            if(signResult.securityToken != null) {
                try {
                    putObjectRequest.setRequestHeaders(Headers.SECURITY_TOKEN, signResult.securityToken, false);
                } catch (CosXmlClientException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i(TestConst.UT_TAG, "upload id is " + uploadTask.getUploadId());
            }
        });
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i("QCloudTest", "upload success!!");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }
}
