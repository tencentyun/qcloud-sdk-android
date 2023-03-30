package com.tencent.cos.xml.ci;

import static com.tencent.cos.xml.core.TestConst.AUDIT_BUCKET_PORN_IMAGE;

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
import com.tencent.cos.xml.model.tag.audit.post.PostImagesAudit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * Created by jordanqin on 2023/2/21 14:34.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class AuditTest {
    @Test
    public void testSensitiveContentRecognition() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        String cosPath = String.format(TestConst.AUDIT_BUCKET_IMAGE_GIF, 1);
        SensitiveContentRecognitionRequest request = new SensitiveContentRecognitionRequest(TestConst.AUDIT_BUCKET, cosPath);
//        request.setCosPath(String.format(TestConst.AUDIT_BUCKET_IMAGE, 1));
        request.setCosPath(cosPath);
        Assert.assertEquals(cosPath, request.getCosPath());
        request.addType("porn");
        request.addType("ads");
        request.setInterval(2);
        request.setMaxFrames(5);
        request.setLargeImageDetect(true);
        request.setBizType("");

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
    public void testSensitiveContentRecognitionAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        String cosPath = String.format(TestConst.AUDIT_BUCKET_IMAGE_GIF, 1);
        SensitiveContentRecognitionRequest request = new SensitiveContentRecognitionRequest(TestConst.AUDIT_BUCKET, cosPath);
        request.setCosPath(cosPath);
        Assert.assertEquals(cosPath, request.getCosPath());
        request.addType("porn");
        request.addType("ads");
        request.setInterval(2);
        request.setMaxFrames(5);
        request.setLargeImageDetect(true);
        request.setBizType("");
        final TestLocker testLocker = new TestLocker();
        ciService.sensitiveContentRecognitionAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SensitiveContentRecognitionResult sensitiveContentRecognitionResult = (SensitiveContentRecognitionResult)result;
                Assert.assertNotNull(sensitiveContentRecognitionResult.recognitionResult);
                TestUtils.printXML(sensitiveContentRecognitionResult.recognitionResult);
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
    public void testSensitiveContentRecognitionDetectUrl() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        SensitiveContentRecognitionRequest request = new SensitiveContentRecognitionRequest(TestConst.AUDIT_BUCKET);
        request.addType("porn");
        request.addType("ads");
        request.setInterval(2);
        request.setMaxFrames(5);
        request.setLargeImageDetect(true);
        request.setDetectUrl("https://p0.meituan.net/travelcube/5793e41eeb57c1653782793b353852b7190953.png");
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
        image1.object = AUDIT_BUCKET_PORN_IMAGE;
        image1.dataId = "DataIdQJD1";
        image1.interval = 2;
        image1.maxFrames = 5;
        PostImagesAudit.ImagesAuditInput image2 = new PostImagesAudit.ImagesAuditInput();
        image2.object = String.format(TestConst.AUDIT_BUCKET_IMAGE_GIF, 1);
        image2.dataId = "DataIdQJD2";
        image2.interval = 2;
        image2.maxFrames = 5;
        PostImagesAudit.ImagesAuditInput image3 = new PostImagesAudit.ImagesAuditInput();
        image3.url = "https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/%205image.jpg";
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

            TestUtils.sleep(20000);

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
    public void imagesAuditAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostImagesAuditRequest postRequest = new PostImagesAuditRequest(TestConst.AUDIT_BUCKET);
        PostImagesAudit.ImagesAuditInput image1 = new PostImagesAudit.ImagesAuditInput();
        image1.object = AUDIT_BUCKET_PORN_IMAGE;
        image1.dataId = "DataIdQJD1";
        image1.interval = 2;
        image1.maxFrames = 5;
        PostImagesAudit.ImagesAuditInput image2 = new PostImagesAudit.ImagesAuditInput();
        image2.object = String.format(TestConst.AUDIT_BUCKET_IMAGE_GIF, 1);
        image2.dataId = "DataIdQJD2";
        image2.interval = 2;
        image2.maxFrames = 5;
        PostImagesAudit.ImagesAuditInput image3 = new PostImagesAudit.ImagesAuditInput();
        image3.url = "https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/%205image.jpg";
        image3.dataId = "DataIdQJD3";
        image3.interval = 2;
        image3.maxFrames = 5;
        postRequest.addImage(image1);
        postRequest.addImage(image2);
        postRequest.addImage(image3);
        postRequest.setDetectType("Porn,Terrorism");
        final TestLocker testLocker = new TestLocker();
        ciService.postImagesAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                PostImagesAuditResult result = (PostImagesAuditResult) resultArg;
                TestUtils.printXML(result.postImagesAuditJobResponse);

                TestUtils.sleep(20000);

                GetImageAuditRequest getAuditRequest = new GetImageAuditRequest(TestConst.AUDIT_BUCKET, result.postImagesAuditJobResponse.jobsDetail.get(0).jobId);
                ciService.getImageAuditAsync(getAuditRequest, new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        GetImageAuditResult getResult = (GetImageAuditResult) result;
                        Assert.assertNotNull(getResult.getImageAuditJobResponse);
                        TestUtils.printXML(getResult.getImageAuditJobResponse);
                        testLocker.release();
                    }

                    @Override
                    public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                        Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                        testLocker.release();
                    }
                });
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
    public void videoAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostVideoAuditRequest postRequest = new PostVideoAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_VIDEO);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/test.mp4");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setCount(3);
        postRequest.setTimeInterval(10);
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setDetectContent(1);
        try {
            PostAuditResult result = ciService.postVideoAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(20000);

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
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/test.mp4");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setCount(3);
        postRequest.setTimeInterval(10);
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setDetectContent(1);
        ciService.postVideoAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                PostAuditResult postAuditResult = (PostAuditResult) result;
                TestUtils.printXML(postAuditResult.postAuditJobResponse);

                TestUtils.sleep(20000);

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
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/16k_ch_and_en.mp3");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        try {
            PostAuditResult result = ciService.postAudioAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(20000);

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
    public void audioAuditAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostAudioAuditRequest postRequest = new PostAudioAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_AUDIO);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/16k_ch_and_en.mp3");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");

        final TestLocker testLocker = new TestLocker();
        ciService.postAudioAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                PostAuditResult result = (PostAuditResult) resultArg;
                TestUtils.printXML(result.postAuditJobResponse);

                TestUtils.sleep(20000);

                GetAudioAuditRequest getAuditRequest = new GetAudioAuditRequest(TestConst.AUDIT_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
                ciService.getAudioAuditAsync(getAuditRequest, new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        GetAudioAuditResult getResult = (GetAudioAuditResult) result;
                        Assert.assertNotNull(getResult.getAudioAuditJobResponse);
                        TestUtils.printXML(getResult.getAudioAuditJobResponse);
                        testLocker.release();
                    }

                    @Override
                    public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                        Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                        testLocker.release();
                    }
                });
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
    public void textAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostTextAuditRequest postRequest = new PostTextAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_TEXT);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/test.txt");
//        postRequest.setContent(Base64.encodeToString("测试文本 很黄很暴力".getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        try {
            TextAuditResult result = ciService.postTextAudit(postRequest);
            TestUtils.printXML(result.textAuditJobResponse);

            TestUtils.sleep(20000);

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
    public void textAuditAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostTextAuditRequest postRequest = new PostTextAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_TEXT);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/test.txt");
//        postRequest.setContent(Base64.encodeToString("测试文本 很黄很暴力".getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        final TestLocker testLocker = new TestLocker();
        ciService.postTextAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                TextAuditResult result = (TextAuditResult) resultArg;
                TestUtils.printXML(result.textAuditJobResponse);

                TestUtils.sleep(20000);

                GetTextAuditRequest getAuditRequest = new GetTextAuditRequest(TestConst.AUDIT_BUCKET, result.textAuditJobResponse.jobsDetail.jobId);
                ciService.getTextAuditAsync(getAuditRequest, new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        TextAuditResult getResult = (TextAuditResult) result;
                        Assert.assertNotNull(getResult.textAuditJobResponse);
                        TestUtils.printXML(getResult.textAuditJobResponse);
                        testLocker.release();
                    }

                    @Override
                    public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                        Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                        testLocker.release();
                    }
                });
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
    public void documentAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostDocumentAuditRequest postRequest = new PostDocumentAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_DOCUMENT);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/03_%E8%B7%AF%E7%94%B1.pdf");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
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
    public void documentAuditAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostDocumentAuditRequest postRequest = new PostDocumentAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_DOCUMENT);
//        postRequest.setUrl("https://00000000000000-1253960454.cos.ap-chengdu.myqcloud.com/03_%E8%B7%AF%E7%94%B1.pdf");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        final TestLocker testLocker = new TestLocker();
        ciService.postDocumentAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                PostAuditResult result = (PostAuditResult) resultArg;
                TestUtils.printXML(result.postAuditJobResponse);

                TestUtils.sleep(20000);

                GetDocumentAuditRequest getAuditRequest = new GetDocumentAuditRequest(TestConst.AUDIT_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
                ciService.getDocumentAuditAsync(getAuditRequest, new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        GetDocumentAuditResult getResult = (GetDocumentAuditResult) result;
                        Assert.assertNotNull(getResult.getDocumentAuditJobResponse);
                        TestUtils.printXML(getResult.getDocumentAuditJobResponse);
                        testLocker.release();
                    }

                    @Override
                    public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                        Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                        testLocker.release();
                    }
                });
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
    public void webpageAudit() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostWebPageAuditRequest postRequest = new PostWebPageAuditRequest(TestConst.AUDIT_BUCKET);
        postRequest.setUrl(TestConst.AUDIT_WEBPAGE);
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setReturnHighlightHtml(true);
        try {
            PostAuditResult result = ciService.postWebPageAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(20000);

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
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setReturnHighlightHtml(true);
        ciService.postWebPageAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                PostAuditResult postAuditResult = (PostAuditResult)result;
                TestUtils.printXML(postAuditResult.postAuditJobResponse);

                TestUtils.sleep(20000);

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
