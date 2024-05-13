package com.tencent.cos.xml.ci;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CIService;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.ci.GetDescribeMediaBucketsRequest;
import com.tencent.cos.xml.model.ci.GetDescribeMediaBucketsResult;
import com.tencent.cos.xml.model.ci.GetMediaInfoRequest;
import com.tencent.cos.xml.model.ci.GetMediaInfoResult;
import com.tencent.cos.xml.model.ci.QRCodeUploadRequest;
import com.tencent.cos.xml.model.ci.QRCodeUploadResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.tag.pic.QRCodeInfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

/**
 * <p>
 * Created by jordanqin on 2020/12/5.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class CosXmlCITest {
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
        request.isPicInfo = true;
        qRCodeUpload(ciService, request);
    }

    @Test
    public void testQRCodeUploadBtUri() {
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
                Uri.fromFile(new File(TestUtils.localPath("qr.png"))));
        request.setCover(0);
        qRCodeUpload(ciService, request);
    }

    @Test
    public void testQRCodeUploadByInputStream() throws IOException {
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
                Files.newInputStream(new File(TestUtils.localPath("qr.png")).toPath()));
        request.isPicInfo = true;
        qRCodeUpload(ciService, request);
    }

    @Test
    public void testQRCodeUploadByBytes() throws IOException {
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
                TestUtils.inputStreamToByteArray(Files.newInputStream(new File(TestUtils.localPath("qr.png")).toPath()))
                );
        request.isPicInfo = true;
        qRCodeUpload(ciService, request);
    }

    @Test
    public void testQRCodeUploadByUrl() throws MalformedURLException {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        QRCodeUploadRequest request = new QRCodeUploadRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_QR_PATH,
                new URL(TestConst.PERSIST_BUCKET_QR_URL));
        qRCodeUpload(ciService, request);
    }

    private void qRCodeUpload(CIService ciService, QRCodeUploadRequest request){
        try {
            QRCodeUploadResult result = ciService.qrCodeUpload(request);
            result.getPicUploadResult();
            Assert.assertNotNull(result.picUploadResult);
            for (QRCodeUploadResult.QrPicObject qrPicObject : result.picUploadResult.processResults){
                for (QRCodeInfo qrCodeInfo : qrPicObject.qrCodeInfo){
                    for (QRCodeInfo.QRCodePoint qrCodePoint : qrCodeInfo.codeLocation){
                        TestUtils.print(qrCodePoint.point);
                        TestUtils.print(qrCodePoint.point().toString());
                    }
                }
            }
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void testQRCodeUploadAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultServiceBySessionCredentials();
        CIService ciService = NormalServiceFactory.INSTANCE.newCIServiceBySessionCredentials();
        try {
            cosXmlService.getObject(new GetObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_QR_PATH,
                    TestUtils.localParentPath()));
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        final TestLocker testLocker = new TestLocker();
        QRCodeUploadRequest request = new QRCodeUploadRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_QR_PATH,
                TestUtils.localPath("qr.png"));
        ciService.qrCodeUploadAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                QRCodeUploadResult result = (QRCodeUploadResult)resultArg;
                Assert.assertNotNull(result.picUploadResult);
                for (QRCodeUploadResult.QrPicObject qrPicObject : result.picUploadResult.processResults){
                    for (QRCodeInfo qrCodeInfo : qrPicObject.qrCodeInfo){
                        for (QRCodeInfo.QRCodePoint qrCodePoint : qrCodeInfo.codeLocation){
                            TestUtils.print(qrCodePoint.point);
                            TestUtils.print(qrCodePoint.point().toString());
                        }
                    }
                }
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test
    public void getDescribeMediaBuckets() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetDescribeMediaBucketsRequest request = new GetDescribeMediaBucketsRequest();
        request.setRegions(TestConst.PERSIST_BUCKET_REGION);
        request.setPageNumber(2);
        request.setPageSize(10);
        try {
            GetDescribeMediaBucketsResult result = ciService.getDescribeMediaBuckets(request);
            Assert.assertNotNull(result.describeMediaBucketsResult);
            TestUtils.printXML(result.describeMediaBucketsResult);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void getDescribeMediaBuckets1() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetDescribeMediaBucketsRequest request = new GetDescribeMediaBucketsRequest();
        request.setBucketNames(TestConst.PERSIST_BUCKET);
        try {
            GetDescribeMediaBucketsResult result = ciService.getDescribeMediaBuckets(request);
            Assert.assertNotNull(result.describeMediaBucketsResult);
            TestUtils.printXML(result.describeMediaBucketsResult);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void getDescribeMediaBuckets2() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetDescribeMediaBucketsRequest request = new GetDescribeMediaBucketsRequest();
        request.setBucketName("mobile-");
        try {
            GetDescribeMediaBucketsResult result = ciService.getDescribeMediaBuckets(request);
            Assert.assertNotNull(result.describeMediaBucketsResult);
            TestUtils.printXML(result.describeMediaBucketsResult);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void getDescribeMediaBucketsAsync() {
//        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditServiceBySessionCredentials();
        CosXmlService ciService = NormalServiceFactory.INSTANCE.newDefaultServiceBySessionCredentials();
        final TestLocker locker = new TestLocker();
        GetDescribeMediaBucketsRequest request = new GetDescribeMediaBucketsRequest();
        request.setRegions(TestConst.PERSIST_BUCKET_REGION+",ap-beijing");
        request.setPageNumber(1);
        request.setPageSize(100);
        ciService.getDescribeMediaBucketsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                GetDescribeMediaBucketsResult result = (GetDescribeMediaBucketsResult) cosResult;
                Assert.assertNotNull(result.describeMediaBucketsResult);
                TestUtils.printXML(result.describeMediaBucketsResult);
                locker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                locker.release();
            }
        });
        locker.lock();
    }

    @Test
    public void getMediaInfo() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetMediaInfoRequest request = new GetMediaInfoRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_VIDEO_PATH);
        try {
            GetMediaInfoResult result = ciService.getMediaInfo(request);
            TestUtils.parseBadResponseBody(result);
            TestUtils.printXML(result.mediaInfo);
            Assert.assertNotNull(result.mediaInfo);
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void getMediaInfoAsync() {
        final TestLocker locker = new TestLocker();
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        ciService.getMediaInfoAsync(new GetMediaInfoRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_VIDEO_PATH), new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                GetMediaInfoResult result = (GetMediaInfoResult) cosResult;
                TestUtils.parseBadResponseBody(result);
                TestUtils.printXML(result.mediaInfo);
                Assert.assertNotNull(result.mediaInfo);
                Assert.assertTrue(true);
                locker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                locker.release();
            }
        });
        locker.lock();
    }
}
