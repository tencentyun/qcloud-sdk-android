package com.tencent.cos.xml.ci;

import static com.tencent.cos.xml.core.TestConst.AUDIT_BUCKET_PORN_IMAGE;

import android.util.Base64;

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
import com.tencent.cos.xml.model.ci.audit.AddAuditTextlibKeyword;
import com.tencent.cos.xml.model.ci.audit.AddAuditTextlibKeywordRequest;
import com.tencent.cos.xml.model.ci.audit.AddAuditTextlibKeywordResult;
import com.tencent.cos.xml.model.ci.audit.CreateAuditTextlib;
import com.tencent.cos.xml.model.ci.audit.CreateAuditTextlibRequest;
import com.tencent.cos.xml.model.ci.audit.CreateAuditTextlibResult;
import com.tencent.cos.xml.model.ci.audit.CreateStrategy;
import com.tencent.cos.xml.model.ci.audit.CreateStrategyRequest;
import com.tencent.cos.xml.model.ci.audit.CreateStrategyResult;
import com.tencent.cos.xml.model.ci.audit.DeleteAuditTextlibKeywordRequest;
import com.tencent.cos.xml.model.ci.audit.DeleteAuditTextlibKeywordResult;
import com.tencent.cos.xml.model.ci.audit.DeleteAuditTextlibRequest;
import com.tencent.cos.xml.model.ci.audit.DeleteAuditTextlibResult;
import com.tencent.cos.xml.model.ci.audit.GetAudioAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetAudioAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetAuditTextlibKeywordListRequest;
import com.tencent.cos.xml.model.ci.audit.GetAuditTextlibKeywordListResult;
import com.tencent.cos.xml.model.ci.audit.GetAuditTextlibListRequest;
import com.tencent.cos.xml.model.ci.audit.GetAuditTextlibListResponse;
import com.tencent.cos.xml.model.ci.audit.GetAuditTextlibListResult;
import com.tencent.cos.xml.model.ci.audit.GetDocumentAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetDocumentAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetImageAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetImageAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetStrategyDetailRequest;
import com.tencent.cos.xml.model.ci.audit.GetStrategyDetailResult;
import com.tencent.cos.xml.model.ci.audit.GetStrategyListRequest;
import com.tencent.cos.xml.model.ci.audit.GetStrategyListResponse;
import com.tencent.cos.xml.model.ci.audit.GetStrategyListResult;
import com.tencent.cos.xml.model.ci.audit.GetTextAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetVideoAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetVideoAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetWebPageAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetWebPageAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostAudioAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostDocumentAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostImageAuditReport;
import com.tencent.cos.xml.model.ci.audit.PostImageAuditReportRequest;
import com.tencent.cos.xml.model.ci.audit.PostImageAuditReportResult;
import com.tencent.cos.xml.model.ci.audit.PostImagesAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostImagesAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditReport;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditReportRequest;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditReportResult;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostVideoAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostWebPageAuditRequest;
import com.tencent.cos.xml.model.ci.audit.TextAuditResult;
import com.tencent.cos.xml.model.ci.audit.UpdateAuditTextlib;
import com.tencent.cos.xml.model.ci.audit.UpdateAuditTextlibRequest;
import com.tencent.cos.xml.model.ci.audit.UpdateAuditTextlibResult;
import com.tencent.cos.xml.model.ci.audit.UpdateStrategy;
import com.tencent.cos.xml.model.ci.audit.UpdateStrategyRequest;
import com.tencent.cos.xml.model.ci.audit.UpdateStrategyResult;
import com.tencent.cos.xml.model.tag.audit.bean.AuditConf;
import com.tencent.cos.xml.model.tag.audit.bean.AuditEncryption;
import com.tencent.cos.xml.model.tag.audit.bean.AuditUserInfo;
import com.tencent.cos.xml.model.tag.audit.post.PostImagesAudit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
        SensitiveContentRecognitionRequest request = new SensitiveContentRecognitionRequest(TestConst.CI_BUCKET, cosPath);
//        request.setCosPath(String.format(TestConst.AUDIT_BUCKET_IMAGE, 1));
        request.setCosPath(cosPath);
        Assert.assertEquals(cosPath, request.getCosPath());
        request.addType("porn");
        request.addType("ads");
        request.setInterval(2);
        request.setMaxFrames(5);
        request.setLargeImageDetect(true);
        request.setBizType("");
        request.setDataid("asdasdasdasd");
        request.setAsync(true);
        request.setCallback("https://www.callback.com");

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
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditServiceBySessionCredentials();
        String cosPath = String.format(TestConst.AUDIT_BUCKET_IMAGE_GIF, 1);
        SensitiveContentRecognitionRequest request = new SensitiveContentRecognitionRequest(TestConst.CI_BUCKET, cosPath);
        request.setCosPath(cosPath);
        Assert.assertEquals(cosPath, request.getCosPath());
        request.addType("porn");
        request.addType("ads");
        request.setInterval(2);
        request.setMaxFrames(5);
        request.setLargeImageDetect(true);
        request.setBizType("");
        request.setDataid("asdasdasdasd");
        request.setAsync(false);
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
        SensitiveContentRecognitionRequest request = new SensitiveContentRecognitionRequest(TestConst.CI_BUCKET);
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

        PostImagesAuditRequest postRequest = new PostImagesAuditRequest(TestConst.CI_BUCKET);
        PostImagesAudit.ImagesAuditInput image1 = new PostImagesAudit.ImagesAuditInput();
        image1.object = AUDIT_BUCKET_PORN_IMAGE;
        image1.dataId = "DataIdQJD1";
        image1.interval = 2;
        image1.maxFrames = 5;
        image1.largeImageDetect = 0;
        AuditUserInfo userInfo = new AuditUserInfo();
        userInfo.appId = "123";
        userInfo.deviceId = "deviceId";
        image1.userInfo = userInfo;
        PostImagesAudit.ImagesAuditInput image2 = new PostImagesAudit.ImagesAuditInput();
        image2.object = String.format(TestConst.AUDIT_BUCKET_IMAGE_GIF, 1);
        image2.dataId = "DataIdQJD2";
        image2.interval = 2;
        image2.maxFrames = 5;
        AuditEncryption encryption = new AuditEncryption();
        encryption.algorithm = "aes-256-ctr";
        encryption.key = "key";
        image2.encryption = encryption;
        PostImagesAudit.ImagesAuditInput image3 = new PostImagesAudit.ImagesAuditInput();
        image3.url = "https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/1image.jpg";
        image3.dataId = "DataIdQJD3";
        image3.interval = 2;
        image3.maxFrames = 5;
        postRequest.addImage(image1);
        postRequest.addImage(image2);
        postRequest.addImage(image3);
        postRequest.setDetectType("Porn,Terrorism");
        postRequest.setAsync(1);
        postRequest.setCallback("https://www.callback.com");
        AuditConf.Freeze freeze = new AuditConf.Freeze();
        freeze.pornScore = 80;
        freeze.adsScore = 90;
        postRequest.setFreeze(freeze);
        try {
            PostImagesAuditResult result = ciService.postImagesAudit(postRequest);
            TestUtils.printXML(result.postImagesAuditJobResponse);

            TestUtils.sleep(20000);

            GetImageAuditRequest getAuditRequest = new GetImageAuditRequest(TestConst.CI_BUCKET, result.postImagesAuditJobResponse.jobsDetail.get(0).jobId);
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

        PostImagesAuditRequest postRequest = new PostImagesAuditRequest(TestConst.CI_BUCKET);
        PostImagesAudit.ImagesAuditInput image1 = new PostImagesAudit.ImagesAuditInput();
        image1.object = AUDIT_BUCKET_PORN_IMAGE;
        image1.dataId = "DataIdQJD1";
        image1.interval = 2;
        image1.maxFrames = 5;
        PostImagesAudit.ImagesAuditInput image2 = new PostImagesAudit.ImagesAuditInput();
        image2.content = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAASABIAAD/4QBARXhpZgAATU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAAqACAAQAAAABAAAAhaADAAQAAAABAAAAbgAAAAD/4gfYSUNDX1BST0ZJTEUAAQEAAAfIYXBwbAIgAABtbnRyUkdCIFhZWiAH2QACABkACwAaAAthY3NwQVBQTAAAAABhcHBsAAAAAAAAAAAAAAAAAAAAAAAA9tYAAQAAAADTLWFwcGwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAtkZXNjAAABCAAAAG9kc2NtAAABeAAABYpjcHJ0AAAHBAAAADh3dHB0AAAHPAAAABRyWFlaAAAHUAAAABRnWFlaAAAHZAAAABRiWFlaAAAHeAAAABRyVFJDAAAHjAAAAA5jaGFkAAAHnAAAACxiVFJDAAAHjAAAAA5nVFJDAAAHjAAAAA5kZXNjAAAAAAAAABRHZW5lcmljIFJHQiBQcm9maWxlAAAAAAAAAAAAAAAUR2VuZXJpYyBSR0IgUHJvZmlsZQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAbWx1YwAAAAAAAAAfAAAADHNrU0sAAAAoAAABhGRhREsAAAAkAAABrGNhRVMAAAAkAAAB0HZpVk4AAAAkAAAB9HB0QlIAAAAmAAACGHVrVUEAAAAqAAACPmZyRlUAAAAoAAACaGh1SFUAAAAoAAACkHpoVFcAAAASAAACuGtvS1IAAAAWAAACym5iTk8AAAAmAAAC4GNzQ1oAAAAiAAADBmhlSUwAAAAeAAADKHJvUk8AAAAkAAADRmRlREUAAAAsAAADaml0SVQAAAAoAAADlnN2U0UAAAAmAAAC4HpoQ04AAAASAAADvmphSlAAAAAaAAAD0GVsR1IAAAAiAAAD6nB0UE8AAAAmAAAEDG5sTkwAAAAoAAAEMmVzRVMAAAAmAAAEDHRoVEgAAAAkAAAEWnRyVFIAAAAiAAAEfmZpRkkAAAAoAAAEoGhySFIAAAAoAAAEyHBsUEwAAAAsAAAE8HJ1UlUAAAAiAAAFHGVuVVMAAAAmAAAFPmFyRUcAAAAmAAAFZABWAWEAZQBvAGIAZQBjAG4A/QAgAFIARwBCACAAcAByAG8AZgBpAGwARwBlAG4AZQByAGUAbAAgAFIARwBCAC0AcAByAG8AZgBpAGwAUABlAHIAZgBpAGwAIABSAEcAQgAgAGcAZQBuAOgAcgBpAGMAQx6lAHUAIABoAOwAbgBoACAAUgBHAEIAIABDAGgAdQBuAGcAUABlAHIAZgBpAGwAIABSAEcAQgAgAEcAZQBuAOkAcgBpAGMAbwQXBDAEMwQwBDsETAQ9BDgEOQAgBD8EQAQ+BEQEMAQ5BDsAIABSAEcAQgBQAHIAbwBmAGkAbAAgAGcA6QBuAOkAcgBpAHEAdQBlACAAUgBWAEIAwQBsAHQAYQBsAOEAbgBvAHMAIABSAEcAQgAgAHAAcgBvAGYAaQBskBp1KABSAEcAQoJyX2ljz4/wx3y8GAAgAFIARwBCACDVBLhc0wzHfABHAGUAbgBlAHIAaQBzAGsAIABSAEcAQgAtAHAAcgBvAGYAaQBsAE8AYgBlAGMAbgD9ACAAUgBHAEIAIABwAHIAbwBmAGkAbAXkBegF1QXkBdkF3AAgAFIARwBCACAF2wXcBdwF2QBQAHIAbwBmAGkAbAAgAFIARwBCACAAZwBlAG4AZQByAGkAYwBBAGwAbABnAGUAbQBlAGkAbgBlAHMAIABSAEcAQgAtAFAAcgBvAGYAaQBsAFAAcgBvAGYAaQBsAG8AIABSAEcAQgAgAGcAZQBuAGUAcgBpAGMAb2ZukBoAUgBHAEJjz4/wZYdO9k4AgiwAIABSAEcAQgAgMNcw7TDVMKEwpDDrA5MDtQO9A7kDugPMACADwAPBA78DxgOvA7sAIABSAEcAQgBQAGUAcgBmAGkAbAAgAFIARwBCACAAZwBlAG4A6QByAGkAYwBvAEEAbABnAGUAbQBlAGUAbgAgAFIARwBCAC0AcAByAG8AZgBpAGUAbA5CDhsOIw5EDh8OJQ5MACAAUgBHAEIAIA4XDjEOSA4nDkQOGwBHAGUAbgBlAGwAIABSAEcAQgAgAFAAcgBvAGYAaQBsAGkAWQBsAGUAaQBuAGUAbgAgAFIARwBCAC0AcAByAG8AZgBpAGkAbABpAEcAZQBuAGUAcgBpAQ0AawBpACAAUgBHAEIAIABwAHIAbwBmAGkAbABVAG4AaQB3AGUAcgBzAGEAbABuAHkAIABwAHIAbwBmAGkAbAAgAFIARwBCBB4EMQRJBDgEOQAg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";
        image2.dataId = "DataIdQJD2";
        image2.interval = 2;
        image2.maxFrames = 5;
        PostImagesAudit.ImagesAuditInput image3 = new PostImagesAudit.ImagesAuditInput();
        image3.url = "https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/1image.jpg";
        image3.dataId = "DataIdQJD3";
        image3.interval = 2;
        image3.maxFrames = 5;
        postRequest.addImage(image1);
        postRequest.addImage(image2);
        postRequest.addImage(image3);
        postRequest.setDetectType("Porn,Terrorism");
        postRequest.setAsync(0);
        final TestLocker testLocker = new TestLocker();
        ciService.postImagesAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                PostImagesAuditResult result = (PostImagesAuditResult) resultArg;
                TestUtils.printXML(result.postImagesAuditJobResponse);

                TestUtils.sleep(20000);

                GetImageAuditRequest getAuditRequest = new GetImageAuditRequest(TestConst.CI_BUCKET, result.postImagesAuditJobResponse.jobsDetail.get(0).jobId);
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

        PostVideoAuditRequest postRequest = new PostVideoAuditRequest(TestConst.CI_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_VIDEO);
//        postRequest.setUrl("https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/example.mp4");
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

            GetVideoAuditRequest getAuditRequest = new GetVideoAuditRequest(TestConst.CI_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
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
        PostVideoAuditRequest postRequest = new PostVideoAuditRequest(TestConst.CI_BUCKET);
//        postRequest.setObject(TestConst.AUDIT_BUCKET_VIDEO);
        postRequest.setUrl("https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/example.mp4");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setCount(3);
        postRequest.setTimeInterval(10);
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setDetectContent(1);
        postRequest.setBizType(null);
        postRequest.setMode("Interval");
        ciService.postVideoAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                PostAuditResult postAuditResult = (PostAuditResult) result;
                TestUtils.printXML(postAuditResult.postAuditJobResponse);

                TestUtils.sleep(20000);

                GetVideoAuditRequest getAuditRequest = new GetVideoAuditRequest(TestConst.CI_BUCKET, postAuditResult.postAuditJobResponse.jobsDetail.jobId);
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

        PostAudioAuditRequest postRequest = new PostAudioAuditRequest(TestConst.CI_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_AUDIO);
//        postRequest.setUrl("https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/example1672060469857.mp3");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        try {
            PostAuditResult result = ciService.postAudioAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(20000);

            GetAudioAuditRequest getAuditRequest = new GetAudioAuditRequest(TestConst.CI_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
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

        PostAudioAuditRequest postRequest = new PostAudioAuditRequest(TestConst.CI_BUCKET);
//        postRequest.setObject(TestConst.AUDIT_BUCKET_AUDIO);
        postRequest.setUrl("https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/example1672060469857.mp3");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setBizType(null);

        final TestLocker testLocker = new TestLocker();
        ciService.postAudioAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                PostAuditResult result = (PostAuditResult) resultArg;
                TestUtils.printXML(result.postAuditJobResponse);

                TestUtils.sleep(20000);

                GetAudioAuditRequest getAuditRequest = new GetAudioAuditRequest(TestConst.CI_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
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

        PostTextAuditRequest postRequest = new PostTextAuditRequest(TestConst.CI_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_TEXT);
//        postRequest.setUrl("https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/test.txt");
//        postRequest.setContent(Base64.encodeToString("测试文本 很黄很暴力".getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        try {
            TextAuditResult result = ciService.postTextAudit(postRequest);
            TestUtils.printXML(result.textAuditJobResponse);

            TestUtils.sleep(20000);

            GetTextAuditRequest getAuditRequest = new GetTextAuditRequest(TestConst.CI_BUCKET, result.textAuditJobResponse.jobsDetail.jobId);
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

        PostTextAuditRequest postRequest = new PostTextAuditRequest(TestConst.CI_BUCKET);
//        postRequest.setObject(TestConst.AUDIT_BUCKET_TEXT);
//        postRequest.setUrl("https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/test.txt");
        postRequest.setContent(Base64.encodeToString("测试文本 很黄很暴力".getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setCallbackVersion("Detail");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setBizType(null);
        final TestLocker testLocker = new TestLocker();
        ciService.postTextAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                TextAuditResult result = (TextAuditResult) resultArg;
                TestUtils.printXML(result.textAuditJobResponse);

                TestUtils.sleep(20000);

                GetTextAuditRequest getAuditRequest = new GetTextAuditRequest(TestConst.CI_BUCKET, result.textAuditJobResponse.jobsDetail.jobId);
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

        PostDocumentAuditRequest postRequest = new PostDocumentAuditRequest(TestConst.CI_BUCKET);
        postRequest.setObject(TestConst.AUDIT_BUCKET_DOCUMENT);
//        postRequest.setUrl("https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/student.ppt");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        try {
            PostAuditResult result = ciService.postDocumentAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(20000);

            GetDocumentAuditRequest getAuditRequest = new GetDocumentAuditRequest(TestConst.CI_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
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

        PostDocumentAuditRequest postRequest = new PostDocumentAuditRequest(TestConst.CI_BUCKET);
//        postRequest.setObject(TestConst.AUDIT_BUCKET_DOCUMENT);
        postRequest.setUrl("https://cos-sdk-citest-1253960454.cos.ap-beijing.myqcloud.com/student.ppt");
        postRequest.setDataId("DataIdQJD");
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setBizType(null);
        postRequest.setType("pdf");
        final TestLocker testLocker = new TestLocker();
        ciService.postDocumentAuditAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult resultArg) {
                PostAuditResult result = (PostAuditResult) resultArg;
                TestUtils.printXML(result.postAuditJobResponse);

                TestUtils.sleep(20000);

                GetDocumentAuditRequest getAuditRequest = new GetDocumentAuditRequest(TestConst.CI_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
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

        PostWebPageAuditRequest postRequest = new PostWebPageAuditRequest(TestConst.CI_BUCKET);
        postRequest.setUrl(TestConst.AUDIT_WEBPAGE);
        postRequest.setCallback("https://github.com/jordanqin");
        postRequest.setDetectType("Porn,Terrorism,Politics,Ads,Illegal,Abuse");
        postRequest.setReturnHighlightHtml(true);
        try {
            PostAuditResult result = ciService.postWebPageAudit(postRequest);
            TestUtils.printXML(result.postAuditJobResponse);

            TestUtils.sleep(20000);

            GetWebPageAuditRequest getAuditRequest = new GetWebPageAuditRequest(TestConst.CI_BUCKET, result.postAuditJobResponse.jobsDetail.jobId);
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
        PostWebPageAuditRequest postRequest = new PostWebPageAuditRequest(TestConst.CI_BUCKET);
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

                GetWebPageAuditRequest getAuditRequest = new GetWebPageAuditRequest(TestConst.CI_BUCKET, postAuditResult.postAuditJobResponse.jobsDetail.jobId);
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

//    @Test
//    public void liveVideoAudit() {
//        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
//        PostLiveVideoAuditRequest postRequest = new PostLiveVideoAuditRequest(TestConst.CI_BUCKET);
//        PostLiveVideoAudit postLiveVideoAudit = new PostLiveVideoAudit();
//        PostLiveVideoAudit.PostLiveVideoAuditInput input = new PostLiveVideoAudit.PostLiveVideoAuditInput();
//        input.url = "rtmp://live.hkstv.hk.lxdns.com/live/hks2";
//        input.dataId = "dataId";
//        postLiveVideoAudit.input = input;
//        PostLiveVideoAudit.PostLiveVideoAuditConf conf = new PostLiveVideoAudit.PostLiveVideoAuditConf();
////        conf.bizType = "b81d45f94b91a683255e9a9506f45a11";
//        conf.callback = "https://github.com/jordanqin";
//        conf.callbackType = 1;
//        postLiveVideoAudit.conf = conf;
//        postRequest.setPostLiveVideoAudit(postLiveVideoAudit);
//        try {
//            PostLiveVideoAuditResult result = ciService.postLiveVideoAudit(postRequest);
//            TestUtils.printXML(result.response);
//
//            TestUtils.sleep(20000);
//
//            GetLiveVideoAuditRequest getAuditRequest = new GetLiveVideoAuditRequest(TestConst.CI_BUCKET, result.response.jobsDetail.jobId);
//            try {
//                GetLiveVideoAuditResult getResult = ciService.getLiveVideoAudit(getAuditRequest);
//                Assert.assertNotNull(getResult.response);
//                TestUtils.printXML(getResult.response);
//            } catch (CosXmlClientException e) {
//                Assert.fail(TestUtils.getCosExceptionMessage(e));
//            } catch (CosXmlServiceException e) {
//                Assert.fail(TestUtils.getCosExceptionMessage(e));
//            }
//
//            TestUtils.sleep(1000);
//
//            CancelLiveVideoAuditRequest cancelLiveVideoAuditRequest = new CancelLiveVideoAuditRequest(TestConst.CI_BUCKET, result.response.jobsDetail.jobId);
//            try {
//                CancelLiveVideoAuditResult getResult = ciService.cancelLiveVideoAudit(cancelLiveVideoAuditRequest);
//                Assert.assertNotNull(getResult.response);
//                TestUtils.printXML(getResult.response);
//            } catch (CosXmlClientException e) {
//                Assert.fail(TestUtils.getCosExceptionMessage(e));
//            } catch (CosXmlServiceException e) {
//                Assert.fail(TestUtils.getCosExceptionMessage(e));
//            }
//        } catch (CosXmlClientException e) {
//            Assert.fail(TestUtils.getCosExceptionMessage(e));
//        } catch (CosXmlServiceException e) {
//            Assert.fail(TestUtils.getCosExceptionMessage(e));
//        }
//    }
//
//    @Test
//    public void liveVideoAuditAsync() {
//        final CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
//        final TestLocker testLocker = new TestLocker();
//        PostLiveVideoAuditRequest postRequest = new PostLiveVideoAuditRequest(TestConst.CI_BUCKET);
//        PostLiveVideoAudit postLiveVideoAudit = new PostLiveVideoAudit();
//        PostLiveVideoAudit.PostLiveVideoAuditInput input = new PostLiveVideoAudit.PostLiveVideoAuditInput();
//        input.url = "rtmp://live.hkstv.hk.lxdns.com/live/hks2";
//        input.dataId = "dataId";
//        postLiveVideoAudit.input = input;
//        PostLiveVideoAudit.PostLiveVideoAuditConf conf = new PostLiveVideoAudit.PostLiveVideoAuditConf();
////        conf.bizType = "b81d45f94b91a683255e9a9506f45a11";
//        conf.callback = "https://github.com/jordanqin";
//        conf.callbackType = 1;
//        postLiveVideoAudit.conf = conf;
//        postRequest.setPostLiveVideoAudit(postLiveVideoAudit);
//        ciService.postLiveVideoAuditAsync(postRequest, new CosXmlResultListener() {
//            @Override
//            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                PostLiveVideoAuditResult postAuditResult = (PostLiveVideoAuditResult)result;
//                TestUtils.printXML(postAuditResult.response);
//
//                TestUtils.sleep(20000);
//
//                GetLiveVideoAuditRequest getAuditRequest = new GetLiveVideoAuditRequest(TestConst.CI_BUCKET, postAuditResult.response.jobsDetail.jobId);
//                ciService.getLiveVideoAuditAsync(getAuditRequest, new CosXmlResultListener() {
//                    @Override
//                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                        GetLiveVideoAuditResult getResult = (GetLiveVideoAuditResult)result;
//                        Assert.assertNotNull(getResult.response);
//                        TestUtils.printXML(getResult.response);
//                        testLocker.release();
//                    }
//
//                    @Override
//                    public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
//                        if(clientException != null){
//                            Assert.fail(TestUtils.getCosExceptionMessage(clientException));
//                        }
//                        if(serviceException != null){
//                            Assert.fail(TestUtils.getCosExceptionMessage(serviceException));
//                        }
//                        testLocker.release();
//                    }
//                });
//            }
//
//            @Override
//            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
//                if(clientException != null){
//                    Assert.fail(TestUtils.getCosExceptionMessage(clientException));
//                }
//                if(serviceException != null){
//                    Assert.fail(TestUtils.getCosExceptionMessage(serviceException));
//                }
//                testLocker.release();
//            }
//        });
//        testLocker.lock();
//    }
//
//
//    @Test
//    public void cancelLiveVideoAuditAsync() {
//        final CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
//        final TestLocker testLocker = new TestLocker();
//        CancelLiveVideoAuditRequest request = new CancelLiveVideoAuditRequest(TestConst.CI_BUCKET, "jobid");
//        ciService.cancelLiveVideoAuditAsync(request, new CosXmlResultListener() {
//            @Override
//            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                CancelLiveVideoAuditResult cancelLiveVideoAuditResult = (CancelLiveVideoAuditResult)result;
//                TestUtils.printXML(cancelLiveVideoAuditResult.response);
//            }
//
//            @Override
//            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
//                if(clientException != null){
//                    Assert.fail(TestUtils.getCosExceptionMessage(clientException));
//                }
//                if(serviceException != null){
//                    Assert.fail(TestUtils.getCosExceptionMessage(serviceException));
//                }
//                testLocker.release();
//            }
//        });
//        testLocker.lock();
//    }

    @Test
    public void postImageAuditReport() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostImageAuditReportRequest postRequest = new PostImageAuditReportRequest(TestConst.CI_BUCKET);
        PostImageAuditReport PostImageAuditReport = new PostImageAuditReport();
        PostImageAuditReport.url = "https://main.qcloudimg.com/raw/2d47c4f47b8f9c8eca85a3590a106e14.jpeg";
        PostImageAuditReport.label = "Porn";
        PostImageAuditReport.suggestedLabel = "Normal";
        postRequest.setPostImageAuditReport(PostImageAuditReport);
        try {
            PostImageAuditReportResult result = ciService.postImageAuditReport(postRequest);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void postImageAuditReportAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        final TestLocker testLocker = new TestLocker();

        PostImageAuditReportRequest postRequest = new PostImageAuditReportRequest(TestConst.CI_BUCKET);
        PostImageAuditReport PostImageAuditReport = new PostImageAuditReport();
        PostImageAuditReport.url = "https://main.qcloudimg.com/raw/2d47c4f47b8f9c8eca85a3590a106e14.jpeg";
        PostImageAuditReport.label = "Porn";
        PostImageAuditReport.suggestedLabel = "Normal";
        postRequest.setPostImageAuditReport(PostImageAuditReport);
        ciService.postImageAuditReportAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                PostImageAuditReportResult postImageAuditReportResult = (PostImageAuditReportResult)result;
                Assert.assertNotNull(postImageAuditReportResult.response);
                TestUtils.printXML(postImageAuditReportResult.response);
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
        testLocker.lock();
    }

    @Test
    public void postTextAuditReport() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        PostTextAuditReportRequest postRequest = new PostTextAuditReportRequest(TestConst.CI_BUCKET);
        PostTextAuditReport PostTextAuditReport = new PostTextAuditReport();
        PostTextAuditReport.text = Base64.encodeToString("texttexttexttexttextte".getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
        PostTextAuditReport.label = "Porn";
        PostTextAuditReport.suggestedLabel = "Normal";
        postRequest.setPostTextAuditReport(PostTextAuditReport);
        try {
            PostTextAuditReportResult result = ciService.postTextAuditReport(postRequest);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void postTextAuditReportAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        final TestLocker testLocker = new TestLocker();

        PostTextAuditReportRequest postRequest = new PostTextAuditReportRequest(TestConst.CI_BUCKET);
        PostTextAuditReport PostTextAuditReport = new PostTextAuditReport();
        PostTextAuditReport.text = Base64.encodeToString("texttexttexttexttextte".getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
        PostTextAuditReport.label = "Porn";
        PostTextAuditReport.suggestedLabel = "Normal";
        postRequest.setPostTextAuditReport(PostTextAuditReport);
        ciService.postTextAuditReportAsync(postRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                PostTextAuditReportResult postTextAuditReportResult = (PostTextAuditReportResult)result;
                Assert.assertNotNull(postTextAuditReportResult.response);
                TestUtils.printXML(postTextAuditReportResult.response);
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
        testLocker.lock();
    }

    @Test
    public void stage1_createStrategy() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();

        CreateStrategyRequest request = new CreateStrategyRequest(TestConst.CI_BUCKET, "Text");
        CreateStrategy createStrategy = new CreateStrategy();
        createStrategy.name = "TestTextStrategy";
//        createStrategy.textLibs = new ArrayList<String>(){};
        CreateStrategy.Labels labels = new CreateStrategy.Labels();
        CreateStrategy.LabelInfo labelInfo = new CreateStrategy.LabelInfo();
        labelInfo.porn = new ArrayList<>();
        labelInfo.porn.add("Pornography");
        labelInfo.porn.add("PornographyObscene");
        labelInfo.politics = new ArrayList<>();
        labelInfo.politics.add("NegativeContent");
        labelInfo.politics.add("PositiveContent");
        labels.text = labelInfo;
        createStrategy.labels = labels;
        request.setStrategy(createStrategy);
        try {
            CreateStrategyResult result = ciService.createAuditStrategy(request);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage1_createStrategyAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        final TestLocker testLocker = new TestLocker();

        CreateStrategyRequest request = new CreateStrategyRequest(TestConst.CI_BUCKET, "Image");
        CreateStrategy createStrategy = new CreateStrategy();
        createStrategy.name = "ImageStrategy";
//        createStrategy.textLibs = new ArrayList<String>(){};
        CreateStrategy.Labels labels = new CreateStrategy.Labels();
        CreateStrategy.LabelInfo labelInfo = new CreateStrategy.LabelInfo();
        labelInfo.porn = new ArrayList<>();
        labelInfo.porn.add("ObsceneBehaviour");
        labelInfo.porn.add("SexProducts");
        labelInfo.politics = new ArrayList<>();
        labelInfo.politics.add("NegativeFigure");
        labelInfo.politics.add("Building");
        labels.image = labelInfo;
        createStrategy.labels = labels;
        request.setStrategy(createStrategy);
        ciService.createAuditStrategyAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                CreateStrategyResult createStrategyResult = (CreateStrategyResult)result;
                Assert.assertNotNull(createStrategyResult.response);
                TestUtils.printXML(createStrategyResult.response);
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
        testLocker.lock();
    }

    @Test
    public void stage2_strategy() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        GetStrategyListRequest getStrategyListRequest = new GetStrategyListRequest(TestConst.CI_BUCKET);
        try {
            GetStrategyListResult getStrategyListResult = ciService.getAuditStrategyList(getStrategyListRequest);
            TestUtils.printXML(getStrategyListResult.response);
            if(getStrategyListResult.response.strategies.size()>0){
                GetStrategyListResponse.Strategies strategies = null;
                for (GetStrategyListResponse.Strategies item : getStrategyListResult.response.strategies){
                    if("TestTextStrategy".equalsIgnoreCase(item.name)) {
                        strategies = item;
                    }
                }
                if(strategies == null){
                    Assert.fail();
                }
                String bizType = strategies.bizType;
                String service = strategies.service;

                GetStrategyDetailRequest getStrategyDetailRequest = new GetStrategyDetailRequest(TestConst.CI_BUCKET, bizType);
                getStrategyDetailRequest.service = service;
                GetStrategyDetailResult getStrategyDetailResult = ciService.getAuditStrategyDetail(getStrategyDetailRequest);
                TestUtils.printXML(getStrategyDetailResult.response);

                UpdateStrategyRequest updateStrategyRequest = new UpdateStrategyRequest(TestConst.CI_BUCKET, service, bizType);
                UpdateStrategy updateStrategy = new UpdateStrategy();
                CreateStrategy.Labels labels = new CreateStrategy.Labels();
                CreateStrategy.LabelInfo labelInfo = new CreateStrategy.LabelInfo();
                labelInfo.porn = new ArrayList<>();
                labelInfo.porn.add("Pornography");
                labelInfo.porn.add("PornographyObscene");
                labelInfo.politics = new ArrayList<>();
                labelInfo.politics.add("NegativeContent");
                labelInfo.politics.add("PositiveContent");
                labels.text = labelInfo;
                updateStrategy.labels = labels;
                updateStrategyRequest.updateStrategy(updateStrategy);
                UpdateStrategyResult updateStrategyResult = ciService.updateAuditStrategy(updateStrategyRequest);
                TestUtils.printXML(updateStrategyResult.response);
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void textlib() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        CreateAuditTextlibRequest createAuditTextlibRequest = new CreateAuditTextlibRequest(TestConst.CI_BUCKET);
        CreateAuditTextlib createAuditTextlib = new CreateAuditTextlib();
        createAuditTextlib.libName = "textlibQ";
        createAuditTextlib.suggestion = "Review";
        createAuditTextlib.matchType = "Fuzzy";
        createAuditTextlibRequest.setAuditTextlib(createAuditTextlib);
        try {
            CreateAuditTextlibResult createAuditTextlibResult = ciService.createAuditTextlib(createAuditTextlibRequest);
            TestUtils.printXML(createAuditTextlibResult.response);

            GetAuditTextlibListRequest getAuditTextlibListRequest = new GetAuditTextlibListRequest(TestConst.CI_BUCKET);
            GetAuditTextlibListResult getAuditTextlibListResult = ciService.getAuditTextlibList(getAuditTextlibListRequest);
            TestUtils.printXML(getAuditTextlibListResult.response);

            if(getAuditTextlibListResult.response.libs.size()>0){
                GetAuditTextlibListResponse.Libs lib = getAuditTextlibListResult.response.libs.get(0);

                AddAuditTextlibKeywordRequest addAuditTextlibKeywordRequest = new AddAuditTextlibKeywordRequest(TestConst.CI_BUCKET, lib.libID);
                AddAuditTextlibKeyword addAuditTextlibKeyword = new AddAuditTextlibKeyword();
                addAuditTextlibKeyword.keywords = new ArrayList<>();
                AddAuditTextlibKeyword.Keywords keyword1 = new AddAuditTextlibKeyword.Keywords();
                keyword1.label = "Porn";
                keyword1.content = "content connt";
                keyword1.remark = "remark";
                addAuditTextlibKeyword.keywords.add(keyword1);
                AddAuditTextlibKeyword.Keywords keyword2 = new AddAuditTextlibKeyword.Keywords();
                keyword2.label = "Porn";
                keyword2.content = "content nt2";
                keyword2.remark = "remark2";
                addAuditTextlibKeyword.keywords.add(keyword2);
                addAuditTextlibKeywordRequest.setAuditTextlib(addAuditTextlibKeyword);
                AddAuditTextlibKeywordResult addAuditTextlibKeywordResult = ciService.addAuditTextlibKeyword(addAuditTextlibKeywordRequest);
                TestUtils.printXML(addAuditTextlibKeywordResult.response);

                GetAuditTextlibKeywordListRequest getAuditTextlibKeywordListRequest = new GetAuditTextlibKeywordListRequest(TestConst.CI_BUCKET, lib.libID);
                GetAuditTextlibKeywordListResult getAuditTextlibKeywordListResult = ciService.getAuditTextlibKeywordList(getAuditTextlibKeywordListRequest);
                TestUtils.printXML(getAuditTextlibKeywordListResult.response);

                if(getAuditTextlibKeywordListResult.response.keywords.size() > 0){
                    DeleteAuditTextlibKeywordRequest deleteAuditTextlibKeywordRequest = new DeleteAuditTextlibKeywordRequest(TestConst.CI_BUCKET, lib.libID);
                    deleteAuditTextlibKeywordRequest.addKeywordID(getAuditTextlibKeywordListResult.response.keywords.get(0).keywordID);
                    DeleteAuditTextlibKeywordResult deleteAuditTextlibKeywordResult = ciService.deleteAuditTextlibKeyword(deleteAuditTextlibKeywordRequest);
                    TestUtils.printXML(deleteAuditTextlibKeywordResult.response);
                }

                UpdateAuditTextlibRequest updateAuditTextlibRequest = new UpdateAuditTextlibRequest(TestConst.CI_BUCKET, lib.libID);
                UpdateAuditTextlib updateAuditTextlib = new UpdateAuditTextlib();
                updateAuditTextlib.libName = "textlibQ1";
                updateAuditTextlib.suggestion = "Review";
                updateAuditTextlibRequest.updateAuditTextlib(updateAuditTextlib);
                UpdateAuditTextlibResult updateAuditTextlibResult = ciService.updateAuditTextlib(updateAuditTextlibRequest);
                TestUtils.printXML(updateAuditTextlibResult.response);

                DeleteAuditTextlibRequest deleteAuditTextlibRequest = new DeleteAuditTextlibRequest(TestConst.CI_BUCKET, lib.libID);
                DeleteAuditTextlibResult deleteAuditTextlibResult = ciService.deleteAuditTextlib(deleteAuditTextlibRequest);
                TestUtils.printXML(deleteAuditTextlibResult.response);
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }
}
