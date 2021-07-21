package com.tencent.cos.xml.ci;

import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CIService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateResult;
import com.tencent.cos.xml.model.ci.GetBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.GetBucketDPStateResult;
import com.tencent.cos.xml.model.ci.PutBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.PutBucketDPStateResult;
import com.tencent.cos.xml.model.ci.QRCodeUploadRequest;
import com.tencent.cos.xml.model.ci.QRCodeUploadResult;
import com.tencent.cos.xml.model.ci.SensitiveContentRecognitionRequest;
import com.tencent.cos.xml.model.ci.SensitiveContentRecognitionResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * Created by jordanqin on 2020/12/5.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class CosXmlCITest {
    @Test
    public void putBucketDocumentPreviewState() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PutBucketDPStateRequest request = new PutBucketDPStateRequest(TestConst.PERSIST_BUCKET);
        try {
            Assert.assertNotNull(ciService.getPresignedURL(request));
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        try {
            PutBucketDPStateResult result = ciService.putBucketDocumentPreviewState(request);
            TestUtils.parseBadResponseBody(result);
            Assert.assertNotNull(result.printResult());
            Assert.assertNotNull(result.getDocumentPreviewState());
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void getBucketDocumentPreviewState() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetBucketDPStateRequest request = new GetBucketDPStateRequest(TestConst.PERSIST_BUCKET);
        try {
            GetBucketDPStateResult result = ciService.getBucketDocumentPreviewState(request);
            TestUtils.parseBadResponseBody(result);
            Assert.assertNotNull(result.printResult());
            Assert.assertNotNull(result.getDocumentPreviewState());
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void deleteBucketDocumentPreviewState() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DeleteBucketDPStateRequest request = new DeleteBucketDPStateRequest(TestConst.PERSIST_BUCKET);
        try {
            DeleteBucketDPStateResult result = ciService.deleteBucketDocumentPreviewState(request);
            TestUtils.parseBadResponseBody(result);
            Assert.assertNotNull(result.printResult());
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void testQRCodeUpload() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        try {
            ciService.getObject(new GetObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_QR_PATH,
                    TestUtils.localParentPath()));
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

        QRCodeUploadRequest request = new QRCodeUploadRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_QR_PATH,
                TestUtils.localPath("qr.png"));
        try {
            QRCodeUploadResult result = ciService.qrCodeUpload(request);
            Assert.assertNotNull(result.picUploadResult);
            Assert.assertNotNull(result.picUploadResult.processResults.get(0).qrCodeInfo.location.points.get(0));
            Assert.assertNotNull(result.picUploadResult.processResults.get(0).qrCodeInfo.location.points.get(1));
            Assert.assertNotNull(result.picUploadResult.processResults.get(0).qrCodeInfo.location.points.get(2));
            Assert.assertNotNull(result.picUploadResult.processResults.get(0).qrCodeInfo.location.points.get(3));
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void testSensitiveContentRecognition() {

        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SensitiveContentRecognitionRequest request = new SensitiveContentRecognitionRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_PIC_PATH);
        request.addType("porn"); 
        request.addType("terrorist");
        request.addType("ads");
        request.addType("politics");

        try {
            SensitiveContentRecognitionResult result = ciService.sensitiveContentRecognition(request);
            Assert.assertNotNull(result.recognitionResult);
            Assert.assertNotNull(result.recognitionResult.adsInfo);
            Assert.assertNotNull(result.recognitionResult.politicsInfo);
            Assert.assertNotNull(result.recognitionResult.pornInfo);
            Assert.assertNotNull(result.recognitionResult.terroristInfo);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
        
    }

}
