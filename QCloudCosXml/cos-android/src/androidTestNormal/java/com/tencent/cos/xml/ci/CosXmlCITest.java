package com.tencent.cos.xml.ci;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CIService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateResult;
import com.tencent.cos.xml.model.ci.GetBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.GetBucketDPStateResult;
import com.tencent.cos.xml.model.ci.GetDescribeMediaBucketsRequest;
import com.tencent.cos.xml.model.ci.GetDescribeMediaBucketsResult;
import com.tencent.cos.xml.model.ci.GetMediaInfoRequest;
import com.tencent.cos.xml.model.ci.GetMediaInfoResult;
import com.tencent.cos.xml.model.ci.PutBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.PutBucketDPStateResult;
import com.tencent.cos.xml.model.ci.QRCodeUploadRequest;
import com.tencent.cos.xml.model.ci.QRCodeUploadResult;
import com.tencent.cos.xml.model.ci.SensitiveContentRecognitionRequest;
import com.tencent.cos.xml.model.ci.SensitiveContentRecognitionResult;
import com.tencent.cos.xml.model.ci.audit.GetAudioAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetAudioAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetDocumentAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetDocumentAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetImageAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetImageAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetTextAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetVideoAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetVideoAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetWebPageAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetWebPageAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostAudioAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostDocumentAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostImagesAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostImagesAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostVideoAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostWebPageAuditRequest;
import com.tencent.cos.xml.model.ci.audit.TextAuditResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.tag.audit.post.PostImagesAudit;

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
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
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
            TestUtils.parseBadResponseBody(result);
            Assert.assertNotNull(result.printResult());
            Assert.assertNotNull(result.describeMediaBucketsResult);
            Assert.assertTrue(true);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void getDescribeMediaBucketsAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        GetDescribeMediaBucketsRequest request = new GetDescribeMediaBucketsRequest();
        request.setRegions(TestConst.PERSIST_BUCKET_REGION+",ap-beijing");
        request.setPageNumber(1);
        request.setPageSize(100);
        ciService.getDescribeMediaBucketsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                GetDescribeMediaBucketsResult result = (GetDescribeMediaBucketsResult) cosResult;
                TestUtils.parseBadResponseBody(result);
                Assert.assertNotNull(result.printResult());
                Assert.assertNotNull(result.describeMediaBucketsResult);
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

    @Test
    public void getMediaInfo() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetMediaInfoRequest request = new GetMediaInfoRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_VIDEO_PATH);
        try {
            GetMediaInfoResult result = ciService.getMediaInfo(request);
            TestUtils.parseBadResponseBody(result);
            Assert.assertNotNull(result.printResult());
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
                Assert.assertNotNull(result.printResult());
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

    @Test
    public void testSensitiveContentRecognition() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        SensitiveContentRecognitionRequest request = new SensitiveContentRecognitionRequest(TestConst.AUDIT_BUCKET);
//        request.setCosPath(String.format(TestConst.AUDIT_BUCKET_IMAGE, 1));
        request.setCosPath(String.format(TestConst.AUDIT_BUCKET_IMAGE_GIF, 1));
//        request.setDetectUrl("https://p0.meituan.net/travelcube/5793e41eeb57c1653782793b353852b7190953.png");
        request.addType("porn");
        request.addType("ads");
        request.setInterval(2);
        request.setMaxFrames(5);
        request.setLargeImageDetect(true);

        try {
            SensitiveContentRecognitionResult result = ciService.sensitiveContentRecognition(request);
            Assert.assertNotNull(result.recognitionResult);
            TestUtils.printXML(result.recognitionResult);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void imagesAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostImagesAuditRequest postRequest = new PostImagesAuditRequest(TestConst.AUDIT_BUCKET);
        PostImagesAudit.ImagesAuditInput image1 = new PostImagesAudit.ImagesAuditInput();
        image1.object = String.format(TestConst.AUDIT_BUCKET_IMAGE, 1);
        image1.dataId = "DataIdQJD1";
        image1.interval = 2;
        image1.maxFrames = 5;
        PostImagesAudit.ImagesAuditInput image2 = new PostImagesAudit.ImagesAuditInput();
        image2.object = String.format(TestConst.AUDIT_BUCKET_IMAGE_GIF, 1);
        image2.dataId = "DataIdQJD2";
        image2.interval = 2;
        image2.maxFrames = 5;
        PostImagesAudit.ImagesAuditInput image3 = new PostImagesAudit.ImagesAuditInput();
        image3.url = "https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/%205image.jpg?q-sign-algorithm=sha1&q-ak=AKIDjOsDmCNwBUedQ3EouQq7C5RdmqW2eWTL&q-sign-time=1648885158;1648892358&q-key-time=1648885158;1648892358&q-header-list=&q-url-param-list=&q-signature=e2d5e7169d5f1ff5868ae9e74f880e19f0557305";
        image3.dataId = "DataIdQJD3";
        image3.interval = 2;
        image3.maxFrames = 5;
        postRequest.addImage(image1);
        postRequest.addImage(image2);
        postRequest.addImage(image3);
        postRequest.setDetectType("Porn,Terrorism");
        try {
            PostImagesAuditResult result = ciService.postImagesAudit(postRequest);
            TestUtils.printXML(result.postImagesAuditJobResponse);

            TestUtils.sleep(10000);

            GetImageAuditRequest getAuditRequest = new GetImageAuditRequest(TestConst.AUDIT_BUCKET, result.postImagesAuditJobResponse.jobsDetail.get(0).jobId);
            try {
                GetImageAuditResult getResult = ciService.getImageAudit(getAuditRequest);
                Assert.assertNotNull(getResult.getImageAuditJobResponse);
                TestUtils.printXML(getResult.getImageAuditJobResponse);
            } catch (CosXmlClientException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            } catch (CosXmlServiceException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void videoAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostVideoAuditRequest postRequest = new PostVideoAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_VIDEO);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/test.mp4?q-sign-algorithm=sha1&q-ak=AKIDjOsDmCNwBUedQ3EouQq7C5RdmqW2eWTL&q-sign-time=1648880520;1648887720&q-key-time=1648880520;1648887720&q-header-list=&q-url-param-list=&q-signature=9f808f64f87d60dda39e965f2486d9f0f263f0a7");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setCount(3);
        postRequest.setTimeInterval(10);
        postRequest.setDetectType("Porn,Terrorism");
        postRequest.setDetectContent(1);
        try {
            PostAuditResult result = ciService.postVideoAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(10000);

            GetVideoAuditRequest getAuditRequest = new GetVideoAuditRequest(TestConst.AUDIT_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
            try {
                GetVideoAuditResult getResult = ciService.getVideoAudit(getAuditRequest);
                Assert.assertNotNull(getResult.getVideoAuditJobResponse);
                TestUtils.printXML(getResult.getVideoAuditJobResponse);
            } catch (CosXmlClientException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            } catch (CosXmlServiceException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void videoAuditAsync() {
        final CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        final TestLocker testLocker = new TestLocker();
        PostVideoAuditRequest postRequest = new PostVideoAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_VIDEO);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/test.mp4?q-sign-algorithm=sha1&q-ak=AKIDjOsDmCNwBUedQ3EouQq7C5RdmqW2eWTL&q-sign-time=1648880520;1648887720&q-key-time=1648880520;1648887720&q-header-list=&q-url-param-list=&q-signature=9f808f64f87d60dda39e965f2486d9f0f263f0a7");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setCount(3);
        postRequest.setTimeInterval(10);
        postRequest.setDetectType("Porn,Terrorism");
        postRequest.setDetectContent(1);
        ciService.postVideoAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                PostAuditResult postAuditResult = (PostAuditResult) result;
                TestUtils.printXML(postAuditResult.postAuditJobResponse);

                TestUtils.sleep(10000);

                GetVideoAuditRequest getAuditRequest = new GetVideoAuditRequest(TestConst.AUDIT_BUCKET, postAuditResult.postAuditJobResponse.jobsDetail.jobId);
                ciService.getVideoAuditAsync(getAuditRequest, new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        GetVideoAuditResult getResult = (GetVideoAuditResult) result;
                        Assert.assertNotNull(getResult.getVideoAuditJobResponse);
                        TestUtils.printXML(getResult.getVideoAuditJobResponse);
                        testLocker.release();
                    }

                    @Override
                    public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                        if(clientException != null){
                            Assert.fail(TestUtils.getCosExceptionMessage(clientException));
                        }
                        if(serviceException != null){
                            Assert.fail(TestUtils.getCosExceptionMessage(serviceException));
                        }
                        testLocker.release();
                    }
                });
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(clientException != null){
                    Assert.fail(TestUtils.getCosExceptionMessage(clientException));
                }
                if(serviceException != null){
                    Assert.fail(TestUtils.getCosExceptionMessage(serviceException));
                }
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test
    public void audioAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostAudioAuditRequest postRequest = new PostAudioAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_AUDIO);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/16k_ch_and_en.mp3?q-sign-algorithm=sha1&q-ak=AKIDjOsDmCNwBUedQ3EouQq7C5RdmqW2eWTL&q-sign-time=1648883854;1648891054&q-key-time=1648883854;1648891054&q-header-list=&q-url-param-list=&q-signature=ae787cdd17f67a6c33ae0ff416bf47302be69c17");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism");
        try {
            PostAuditResult result = ciService.postAudioAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(10000);

            GetAudioAuditRequest getAuditRequest = new GetAudioAuditRequest(TestConst.AUDIT_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
            try {
                GetAudioAuditResult getResult = ciService.getAudioAudit(getAuditRequest);
                Assert.assertNotNull(getResult.getAudioAuditJobResponse);
                TestUtils.printXML(getResult.getAudioAuditJobResponse);
            } catch (CosXmlClientException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            } catch (CosXmlServiceException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void textAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostTextAuditRequest postRequest = new PostTextAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_TEXT);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/test.txt?q-sign-algorithm=sha1&q-ak=AKIDjOsDmCNwBUedQ3EouQq7C5RdmqW2eWTL&q-sign-time=1648883822;1648891022&q-key-time=1648883822;1648891022&q-header-list=&q-url-param-list=&q-signature=a783b2cbc4defbc9992215f82f549950e149addd");
//        postRequest.setContent(Base64.encodeToString("测试文本 很黄很暴力".getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism");
        try {
            TextAuditResult result = ciService.postTextAudit(postRequest);
            TestUtils.printXML(result.textAuditJobResponse);

            TestUtils.sleep(10000);

            GetTextAuditRequest getAuditRequest = new GetTextAuditRequest(TestConst.AUDIT_BUCKET, result.textAuditJobResponse.jobsDetail.jobId);
            try {
                TextAuditResult getResult = ciService.getTextAudit(getAuditRequest);
                Assert.assertNotNull(getResult.textAuditJobResponse);
                TestUtils.printXML(getResult.textAuditJobResponse);
            } catch (CosXmlClientException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            } catch (CosXmlServiceException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void documentAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostDocumentAuditRequest postRequest = new PostDocumentAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_DOCUMENT);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/03_%E8%B7%AF%E7%94%B1.pdf?q-sign-algorithm=sha1&q-ak=AKIDjOsDmCNwBUedQ3EouQq7C5RdmqW2eWTL&q-sign-time=1648884238;1648891438&q-key-time=1648884238;1648891438&q-header-list=&q-url-param-list=&q-signature=1a950c3a6f7a8150c7a91051c13373b202828819");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setDetectType("Porn,Terrorism");
        try {
            PostAuditResult result = ciService.postDocumentAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(20000);

            GetDocumentAuditRequest getAuditRequest = new GetDocumentAuditRequest(TestConst.AUDIT_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
            try {
                GetDocumentAuditResult getResult = ciService.getDocumentAudit(getAuditRequest);
                Assert.assertNotNull(getResult.getDocumentAuditJobResponse);
                TestUtils.printXML(getResult.getDocumentAuditJobResponse);
            } catch (CosXmlClientException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            } catch (CosXmlServiceException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void webpageAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostWebPageAuditRequest postRequest = new PostWebPageAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setUrl(TestConst.AUDIT_WEBPAGE);
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setDetectType("Porn,Terrorism");
        postRequest.setReturnHighlightHtml(true);
        try {
            PostAuditResult result = ciService.postWebPageAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(10000);

            GetWebPageAuditRequest getAuditRequest = new GetWebPageAuditRequest(TestConst.AUDIT_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
            try {
                GetWebPageAuditResult getResult = ciService.getWebPageAudit(getAuditRequest);
                Assert.assertNotNull(getResult.getWebPageAuditJobResponse);
                TestUtils.printXML(getResult.getWebPageAuditJobResponse);
            } catch (CosXmlClientException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            } catch (CosXmlServiceException e) {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void webpageAuditAsync() {
        final CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        final TestLocker testLocker = new TestLocker();
        PostWebPageAuditRequest postRequest = new PostWebPageAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setUrl(TestConst.AUDIT_WEBPAGE);
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setDetectType("Porn,Terrorism");
        postRequest.setReturnHighlightHtml(true);
        ciService.postWebPageAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                PostAuditResult postAuditResult = (PostAuditResult)result;
                TestUtils.printXML(postAuditResult.postAuditJobResponse);

                TestUtils.sleep(10000);

                GetWebPageAuditRequest getAuditRequest = new GetWebPageAuditRequest(TestConst.AUDIT_BUCKET, postAuditResult.postAuditJobResponse.jobsDetail.jobId);
                ciService.getWebPageAuditAsync(getAuditRequest, new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        GetWebPageAuditResult getResult = (GetWebPageAuditResult)result;
                        Assert.assertNotNull(getResult.getWebPageAuditJobResponse);
                        TestUtils.printXML(getResult.getWebPageAuditJobResponse);
                        testLocker.release();
                    }

                    @Override
                    public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                        if(clientException != null){
                            Assert.fail(TestUtils.getCosExceptionMessage(clientException));
                        }
                        if(serviceException != null){
                            Assert.fail(TestUtils.getCosExceptionMessage(serviceException));
                        }
                        testLocker.release();
                    }
                });
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(clientException != null){
                    Assert.fail(TestUtils.getCosExceptionMessage(clientException));
                }
                if(serviceException != null){
                    Assert.fail(TestUtils.getCosExceptionMessage(serviceException));
                }
                testLocker.release();
            }
        });
        testLocker.lock();
    }
}
