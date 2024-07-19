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
import com.tencent.cos.xml.model.ci.common.AudioMix;
import com.tencent.cos.xml.model.ci.common.EffectConfig;
import com.tencent.cos.xml.model.ci.common.PicProcess;
import com.tencent.cos.xml.model.ci.media.TemplateAnimation;
import com.tencent.cos.xml.model.ci.media.TemplateAnimationRequest;
import com.tencent.cos.xml.model.ci.media.TemplateAnimationResult;
import com.tencent.cos.xml.model.ci.media.TemplateConcat;
import com.tencent.cos.xml.model.ci.media.TemplateConcatRequest;
import com.tencent.cos.xml.model.ci.media.TemplateConcatResult;
import com.tencent.cos.xml.model.ci.media.TemplatePicProcess;
import com.tencent.cos.xml.model.ci.media.TemplatePicProcessRequest;
import com.tencent.cos.xml.model.ci.media.TemplatePicProcessResult;
import com.tencent.cos.xml.model.ci.media.TemplateSmartCover;
import com.tencent.cos.xml.model.ci.media.TemplateSmartCoverRequest;
import com.tencent.cos.xml.model.ci.media.TemplateSmartCoverResult;
import com.tencent.cos.xml.model.ci.media.TemplateSnapshot;
import com.tencent.cos.xml.model.ci.media.TemplateSnapshotRequest;
import com.tencent.cos.xml.model.ci.media.TemplateSnapshotResult;
import com.tencent.cos.xml.model.ci.media.TemplateTranscode;
import com.tencent.cos.xml.model.ci.media.TemplateTranscodeRequest;
import com.tencent.cos.xml.model.ci.media.TemplateTranscodeResult;
import com.tencent.cos.xml.model.ci.media.TemplateVideoMontage;
import com.tencent.cos.xml.model.ci.media.TemplateVideoMontageRequest;
import com.tencent.cos.xml.model.ci.media.TemplateVideoMontageResult;
import com.tencent.cos.xml.model.ci.media.TemplateVoiceSeparate;
import com.tencent.cos.xml.model.ci.media.TemplateVoiceSeparateRequest;
import com.tencent.cos.xml.model.ci.media.TemplateVoiceSeparateResult;
import com.tencent.cos.xml.model.ci.media.TemplateWatermark;
import com.tencent.cos.xml.model.ci.media.TemplateWatermarkRequest;
import com.tencent.cos.xml.model.ci.media.TemplateWatermarkResult;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * <p>
 * Created by jordanqin on 2024/4/28 12:00.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class TemplateTest {
    private static final String TAG = "TemplateTest";

    @Test
    public void templatePicProcess() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        TemplatePicProcessRequest request = new TemplatePicProcessRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        TemplatePicProcess templatePicProcess = new TemplatePicProcess();
        templatePicProcess.name = "TestPicProcessTemplate"+System.currentTimeMillis();
        PicProcess picProcess = new PicProcess();
        picProcess.isPicInfo = true;
        picProcess.processRule = "imageMogr2/rotate/90";
        templatePicProcess.picProcess = picProcess;
        request.setTemplatePicProcess(templatePicProcess);
        try {
            TemplatePicProcessResult result = ciService.templatePicProcess(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void templatePicProcessAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        TemplatePicProcessRequest request = new TemplatePicProcessRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        TemplatePicProcess templatePicProcess = new TemplatePicProcess();
        templatePicProcess.name = "TestPicProcessTemplate"+System.currentTimeMillis();
        PicProcess picProcess = new PicProcess();
        picProcess.isPicInfo = true;
        picProcess.processRule = "imageMogr2/rotate/90";
        templatePicProcess.picProcess = picProcess;
        request.setTemplatePicProcess(templatePicProcess);
        final TestLocker testLocker = new TestLocker();
        ciService.templatePicProcessAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TemplatePicProcessResult templatePicProcessResult = (TemplatePicProcessResult)result;
                Assert.assertNotNull(templatePicProcessResult.response);
                TestUtils.printXML(templatePicProcessResult.response);
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
    public void templateAnimation() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateAnimationRequest request = new TemplateAnimationRequest(TestConst.CI_BUCKET);
        TemplateAnimation templateAnimation = new TemplateAnimation();
        templateAnimation.name = "TestAnimationTemplate"+System.currentTimeMillis();
        templateAnimation.container = new TemplateAnimation.TemplateAnimationContainer();
        templateAnimation.container.format = "gif";
        templateAnimation.video = new TemplateAnimation.TemplateAnimationVideo();
        templateAnimation.video.codec = "gif";
        templateAnimation.video.width = "1280";
        templateAnimation.video.fps = "15";
        templateAnimation.video.animateOnlyKeepKeyFrame = "true";
        templateAnimation.video.quality = "80";
        templateAnimation.timeInterval = new TemplateAnimation.TemplateAnimationTimeInterval();
        templateAnimation.timeInterval.start = "0";
        templateAnimation.timeInterval.duration = "60";
        request.setTemplateAnimation(templateAnimation);
        try {
            TemplateAnimationResult result = ciService.templateAnimation(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void templateAnimationAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateAnimationRequest request = new TemplateAnimationRequest(TestConst.CI_BUCKET);
        TemplateAnimation templateAnimation = new TemplateAnimation();
        templateAnimation.name = "TestAnimationTemplate"+System.currentTimeMillis();
        templateAnimation.container = new TemplateAnimation.TemplateAnimationContainer();
        templateAnimation.container.format = "gif";
        templateAnimation.video = new TemplateAnimation.TemplateAnimationVideo();
        templateAnimation.video.codec = "gif";
        templateAnimation.video.width = "1280";
        templateAnimation.video.fps = "15";
        templateAnimation.video.animateOnlyKeepKeyFrame = "true";
        templateAnimation.video.quality = "80";
        templateAnimation.timeInterval = new TemplateAnimation.TemplateAnimationTimeInterval();
        templateAnimation.timeInterval.start = "0";
        templateAnimation.timeInterval.duration = "60";
        request.setTemplateAnimation(templateAnimation);
        final TestLocker testLocker = new TestLocker();
        ciService.templateAnimationAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TemplateAnimationResult templateAnimationResult = (TemplateAnimationResult)result;
                Assert.assertNotNull(templateAnimationResult.response);
                TestUtils.printXML(templateAnimationResult.response);
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
    public void templateSnapshot() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateSnapshotRequest request = new TemplateSnapshotRequest(TestConst.CI_BUCKET);
        TemplateSnapshot templateSnapshot = new TemplateSnapshot();
        templateSnapshot.name = "TestSnapshotTemplate"+System.currentTimeMillis();
        templateSnapshot.snapshot = new TemplateSnapshot.TemplateSnapshotSnapshot();
        templateSnapshot.snapshot.mode = "Interval";
        templateSnapshot.snapshot.width = "1280";
        templateSnapshot.snapshot.height = "960";
        templateSnapshot.snapshot.start = "0";
        templateSnapshot.snapshot.timeInterval = "0.5";
        templateSnapshot.snapshot.count = "10";
        templateSnapshot.snapshot.snapshotOutMode = "OnlySprite";
        templateSnapshot.snapshot.spriteSnapshotConfig = new TemplateSnapshot.TemplateSnapshotSpriteSnapshotConfig();
        templateSnapshot.snapshot.spriteSnapshotConfig.color = "Aquamarine";
        templateSnapshot.snapshot.spriteSnapshotConfig.columns = "3";
        templateSnapshot.snapshot.spriteSnapshotConfig.lines = "3";
        request.setTemplateSnapshot(templateSnapshot);
        try {
            TemplateSnapshotResult result = ciService.templateSnapshot(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void templateSnapshotAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateSnapshotRequest request = new TemplateSnapshotRequest(TestConst.CI_BUCKET);
        TemplateSnapshot templateSnapshot = new TemplateSnapshot();
        templateSnapshot.name = "TestSnapshotTemplate"+System.currentTimeMillis();
        templateSnapshot.snapshot = new TemplateSnapshot.TemplateSnapshotSnapshot();
        templateSnapshot.snapshot.mode = "Interval";
        templateSnapshot.snapshot.width = "1280";
        templateSnapshot.snapshot.height = "960";
        templateSnapshot.snapshot.start = "0";
        templateSnapshot.snapshot.timeInterval = "0.5";
        templateSnapshot.snapshot.count = "10";
        templateSnapshot.snapshot.snapshotOutMode = "OnlySprite";
        templateSnapshot.snapshot.spriteSnapshotConfig = new TemplateSnapshot.TemplateSnapshotSpriteSnapshotConfig();
        templateSnapshot.snapshot.spriteSnapshotConfig.color = "Aquamarine";
        templateSnapshot.snapshot.spriteSnapshotConfig.columns = "3";
        templateSnapshot.snapshot.spriteSnapshotConfig.lines = "3";
        request.setTemplateSnapshot(templateSnapshot);
        final TestLocker testLocker = new TestLocker();
        ciService.templateSnapshotAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TemplateSnapshotResult templateSnapshotResult = (TemplateSnapshotResult)result;
                Assert.assertNotNull(templateSnapshotResult.response);
                TestUtils.printXML(templateSnapshotResult.response);
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
    public void templateWatermark() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateWatermarkRequest request = new TemplateWatermarkRequest(TestConst.CI_BUCKET);
        TemplateWatermark templateWatermark = new TemplateWatermark();
        templateWatermark.name = "TestWatermarkTemplate"+System.currentTimeMillis();
        templateWatermark.watermark = new TemplateWatermark.Watermark();
        templateWatermark.watermark.type = "Text";
        templateWatermark.watermark.locMode = "Absolute";
        templateWatermark.watermark.dx = "3";
        templateWatermark.watermark.dy = "3";
        templateWatermark.watermark.pos = "TopRight";
        templateWatermark.watermark.startTime = "0";
        templateWatermark.watermark.endTime = "100";
        templateWatermark.watermark.text = new TemplateWatermark.TemplateWatermarkText();
        templateWatermark.watermark.text.text = "水印内容";
        templateWatermark.watermark.text.fontSize = "30";
        templateWatermark.watermark.text.fontType = "simfang.ttf";
        templateWatermark.watermark.text.fontColor = "0x000000";
        templateWatermark.watermark.text.transparency = "30";
        templateWatermark.watermark.slideConfig = new TemplateWatermark.TemplateWatermarkSlideConfig();
        templateWatermark.watermark.slideConfig.slideMode = "Default";
        templateWatermark.watermark.slideConfig.xSlideSpeed = "0";
        templateWatermark.watermark.slideConfig.ySlideSpeed = "0";
        request.setTemplateWatermark(templateWatermark);
        try {
            TemplateWatermarkResult result = ciService.templateWatermark(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void templateWatermarkAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateWatermarkRequest request = new TemplateWatermarkRequest(TestConst.CI_BUCKET);
        TemplateWatermark templateWatermark = new TemplateWatermark();
        templateWatermark.name = "TestWatermarkTemplate"+System.currentTimeMillis();
        templateWatermark.watermark = new TemplateWatermark.Watermark();
        templateWatermark.watermark.type = "Image";
        templateWatermark.watermark.locMode = "Absolute";
        templateWatermark.watermark.dx = "3";
        templateWatermark.watermark.dy = "3";
        templateWatermark.watermark.pos = "TopRight";
        templateWatermark.watermark.startTime = "0";
        templateWatermark.watermark.endTime = "100";
        templateWatermark.watermark.image = new TemplateWatermark.TemplateWatermarkImage();
        templateWatermark.watermark.image.url = "http://test-1234567890.ci.ap-chongqing.myqcloud.com/shuiyin_2.png";
        templateWatermark.watermark.image.mode = "Proportion";
        templateWatermark.watermark.image.width = "10";
        templateWatermark.watermark.image.height = "10";
        templateWatermark.watermark.image.transparency = "100";
        templateWatermark.watermark.slideConfig = new TemplateWatermark.TemplateWatermarkSlideConfig();
        templateWatermark.watermark.slideConfig.slideMode = "Default";
        templateWatermark.watermark.slideConfig.xSlideSpeed = "0";
        templateWatermark.watermark.slideConfig.ySlideSpeed = "0";
        request.setTemplateWatermark(templateWatermark);
        final TestLocker testLocker = new TestLocker();
        ciService.templateWatermarkAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TemplateWatermarkResult templateWatermarkResult = (TemplateWatermarkResult)result;
                Assert.assertNotNull(templateWatermarkResult.response);
                TestUtils.printXML(templateWatermarkResult.response);
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
    public void templateConcat() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateConcatRequest request = new TemplateConcatRequest(TestConst.CI_BUCKET);
        TemplateConcat templateConcat = new TemplateConcat();
        templateConcat.name = "TestConcatTemplate"+System.currentTimeMillis();
        templateConcat.concatTemplate = new TemplateConcat.TemplateConcatConcatTemplate();
        templateConcat.concatTemplate.concatFragment = new ArrayList<>();
        TemplateConcat.TemplateConcatConcatFragment templateConcatConcatFragment = new TemplateConcat.TemplateConcatConcatFragment();
        templateConcatConcatFragment.mode = "Start";
        templateConcatConcatFragment.url = String.format("http://%s.cos.%s.myqcloud.com/%s", TestConst.CI_BUCKET, TestConst.CI_BUCKET_REGION, TestConst.AUDIT_BUCKET_VIDEO);
        templateConcatConcatFragment.startTime = "0";
        templateConcatConcatFragment.endTime = "30";
        templateConcat.concatTemplate.concatFragment.add(templateConcatConcatFragment);
        templateConcat.concatTemplate.audio = new TemplateConcat.TemplateConcatAudio();
        templateConcat.concatTemplate.audio.codec = "mp3";
        templateConcat.concatTemplate.audio.samplerate = "11025";
        templateConcat.concatTemplate.audio.bitrate = "100";
        templateConcat.concatTemplate.audio.channels = "1";
        templateConcat.concatTemplate.video = new TemplateConcat.TemplateConcatVideo();
        templateConcat.concatTemplate.video.codec = "H.264";
        templateConcat.concatTemplate.video.bitrate = "1000";
        templateConcat.concatTemplate.video.width = "1280";
        templateConcat.concatTemplate.video.height = "960";
        templateConcat.concatTemplate.video.fps = "30";
        templateConcat.concatTemplate.video.crf = "25";
        templateConcat.concatTemplate.video.remove = "false";
        templateConcat.concatTemplate.video.rotate = "10";
        templateConcat.concatTemplate.container = new TemplateConcat.TemplateConcatContainer();
        templateConcat.concatTemplate.container.format = "mp4";
        templateConcat.concatTemplate.audioMixArray = new ArrayList<>();
        AudioMix audioMix = new AudioMix();
        audioMix.audioSource = String.format("http://%s.cos.%s.myqcloud.com/%s", TestConst.CI_BUCKET, TestConst.CI_BUCKET_REGION, TestConst.AUDIT_BUCKET_AUDIO);
        audioMix.mixMode = "Once";
        audioMix.replace = "true";
        audioMix.effectConfig = new EffectConfig();
        audioMix.effectConfig.enableStartFadein = "true";
        audioMix.effectConfig.startFadeinTime = "3";
        audioMix.effectConfig.enableEndFadeout = "false";
        audioMix.effectConfig.endFadeoutTime = "0";
        audioMix.effectConfig.enableBgmFade = "true";
        audioMix.effectConfig.bgmFadeTime = "1.7";
        templateConcat.concatTemplate.audioMixArray.add(audioMix);
        request.setTemplateConcat(templateConcat);
        try {
            TemplateConcatResult result = ciService.templateConcat(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void templateConcatAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateConcatRequest request = new TemplateConcatRequest(TestConst.CI_BUCKET);
        TemplateConcat templateConcat = new TemplateConcat();
        templateConcat.name = "TestConcatTemplate"+System.currentTimeMillis();
        templateConcat.concatTemplate = new TemplateConcat.TemplateConcatConcatTemplate();
        templateConcat.concatTemplate.concatFragment = new ArrayList<>();
        TemplateConcat.TemplateConcatConcatFragment templateConcatConcatFragment = new TemplateConcat.TemplateConcatConcatFragment();
        templateConcatConcatFragment.mode = "Start";
        templateConcatConcatFragment.url = String.format("http://%s.cos.%s.myqcloud.com/%s", TestConst.CI_BUCKET, TestConst.CI_BUCKET_REGION, TestConst.AUDIT_BUCKET_VIDEO);
        templateConcatConcatFragment.startTime = "0";
        templateConcatConcatFragment.endTime = "30";
        templateConcat.concatTemplate.concatFragment.add(templateConcatConcatFragment);
        templateConcat.concatTemplate.audio = new TemplateConcat.TemplateConcatAudio();
        templateConcat.concatTemplate.audio.codec = "mp3";
        templateConcat.concatTemplate.audio.samplerate = "11025";
        templateConcat.concatTemplate.audio.bitrate = "100";
        templateConcat.concatTemplate.audio.channels = "1";
        templateConcat.concatTemplate.video = new TemplateConcat.TemplateConcatVideo();
        templateConcat.concatTemplate.video.codec = "H.264";
        templateConcat.concatTemplate.video.bitrate = "1000";
        templateConcat.concatTemplate.video.width = "1280";
        templateConcat.concatTemplate.video.height = "960";
        templateConcat.concatTemplate.video.fps = "30";
        templateConcat.concatTemplate.video.crf = "25";
        templateConcat.concatTemplate.video.remove = "false";
        templateConcat.concatTemplate.video.rotate = "10";
        templateConcat.concatTemplate.container = new TemplateConcat.TemplateConcatContainer();
        templateConcat.concatTemplate.container.format = "mp4";
        templateConcat.concatTemplate.audioMixArray = new ArrayList<>();
        AudioMix audioMix = new AudioMix();
        audioMix.audioSource = String.format("http://%s.cos.%s.myqcloud.com/%s", TestConst.CI_BUCKET, TestConst.CI_BUCKET_REGION, TestConst.AUDIT_BUCKET_AUDIO);
        audioMix.mixMode = "Once";
        audioMix.replace = "true";
        audioMix.effectConfig = new EffectConfig();
        audioMix.effectConfig.enableStartFadein = "true";
        audioMix.effectConfig.startFadeinTime = "3";
        audioMix.effectConfig.enableEndFadeout = "false";
        audioMix.effectConfig.endFadeoutTime = "0";
        audioMix.effectConfig.enableBgmFade = "true";
        audioMix.effectConfig.bgmFadeTime = "1.7";
        templateConcat.concatTemplate.audioMixArray.add(audioMix);
        request.setTemplateConcat(templateConcat);
        final TestLocker testLocker = new TestLocker();
        ciService.templateConcatAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TemplateConcatResult templateConcatResult = (TemplateConcatResult)result;
                Assert.assertNotNull(templateConcatResult.response);
                TestUtils.printXML(templateConcatResult.response);
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
    public void templateVoiceSeparate() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        TemplateVoiceSeparateRequest request = new TemplateVoiceSeparateRequest(TestConst.ASR_BUCKET);
        TemplateVoiceSeparate templateVoiceSeparate = new TemplateVoiceSeparate();
        templateVoiceSeparate.name = "TestVoiceSeparateTemplate"+System.currentTimeMillis();
        templateVoiceSeparate.audioMode = "IsAudio";
        templateVoiceSeparate.audioConfig = new TemplateVoiceSeparate.AudioConfig();
        templateVoiceSeparate.audioConfig.codec = "aac";
        templateVoiceSeparate.audioConfig.samplerate = "44100";
        templateVoiceSeparate.audioConfig.bitrate = "128";
        templateVoiceSeparate.audioConfig.channels = "4";
        request.setTemplateVoiceSeparate(templateVoiceSeparate);
        try {
            TemplateVoiceSeparateResult result = ciService.templateVoiceSeparate(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void templateVoiceSeparateAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        TemplateVoiceSeparateRequest request = new TemplateVoiceSeparateRequest(TestConst.ASR_BUCKET);
        TemplateVoiceSeparate templateVoiceSeparate = new TemplateVoiceSeparate();
        templateVoiceSeparate.name = "TestVoiceSeparateTemplate"+System.currentTimeMillis();
        templateVoiceSeparate.audioMode = "IsAudio";
        templateVoiceSeparate.audioConfig = new TemplateVoiceSeparate.AudioConfig();
        templateVoiceSeparate.audioConfig.codec = "aac";
        templateVoiceSeparate.audioConfig.samplerate = "44100";
        templateVoiceSeparate.audioConfig.bitrate = "128";
        templateVoiceSeparate.audioConfig.channels = "4";
        request.setTemplateVoiceSeparate(templateVoiceSeparate);
        final TestLocker testLocker = new TestLocker();
        ciService.templateVoiceSeparateAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TemplateVoiceSeparateResult templateVoiceSeparateResult = (TemplateVoiceSeparateResult)result;
                Assert.assertNotNull(templateVoiceSeparateResult.response);
                TestUtils.printXML(templateVoiceSeparateResult.response);
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
    public void templateTranscode() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateTranscodeRequest request = new TemplateTranscodeRequest(TestConst.CI_BUCKET);
        TemplateTranscode templateTranscode = new TemplateTranscode();
        templateTranscode.name = "TestTranscodeTemplate"+System.currentTimeMillis();
        templateTranscode.container = new TemplateTranscode.TemplateTranscodeContainer();
        templateTranscode.container.format = "mp4";
        templateTranscode.video = new TemplateTranscode.TemplateTranscodeVideo();
        templateTranscode.video.codec = "H.264";
        templateTranscode.video.profile = "high";
        templateTranscode.video.bitrate = "1000";
        templateTranscode.video.width = "1280";
        templateTranscode.video.fps = "30";
        templateTranscode.video.preset = "medium";
        templateTranscode.audio = new TemplateTranscode.TemplateTranscodeAudio();
        templateTranscode.audio.codec = "aac";
        templateTranscode.audio.samplerate = "44100";
        templateTranscode.audio.bitrate = "128";
        templateTranscode.audio.channels = "4";
        templateTranscode.transConfig = new TemplateTranscode.TemplateTranscodeTransConfig();
        templateTranscode.transConfig.adjDarMethod = "scale";
        templateTranscode.transConfig.isCheckReso = "false";
        templateTranscode.transConfig.resoAdjMethod = "1";
        templateTranscode.timeInterval = new TemplateTranscode.TemplateTranscodeTimeInterval();
        templateTranscode.timeInterval.start = "0";
        templateTranscode.timeInterval.duration = "60";
        templateTranscode.audioMixArray = new ArrayList<>();
        AudioMix audioMix = new AudioMix();
        audioMix.audioSource = String.format("http://%s.cos.%s.myqcloud.com/%s", TestConst.CI_BUCKET, TestConst.CI_BUCKET_REGION, TestConst.AUDIT_BUCKET_AUDIO);
        audioMix.mixMode = "Once";
        audioMix.replace = "true";
        audioMix.effectConfig = new EffectConfig();
        audioMix.effectConfig.enableStartFadein = "true";
        audioMix.effectConfig.startFadeinTime = "3";
        audioMix.effectConfig.enableEndFadeout = "false";
        audioMix.effectConfig.endFadeoutTime = "0";
        audioMix.effectConfig.enableBgmFade = "true";
        audioMix.effectConfig.bgmFadeTime = "1.7";
        templateTranscode.audioMixArray.add(audioMix);
        request.setTemplateTranscode(templateTranscode);
        try {
            TemplateTranscodeResult result = ciService.templateTranscode(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void templateTranscodeAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateTranscodeRequest request = new TemplateTranscodeRequest(TestConst.CI_BUCKET);
        TemplateTranscode templateTranscode = new TemplateTranscode();
        templateTranscode.name = "TestTranscodeTemplate"+System.currentTimeMillis();
        templateTranscode.container = new TemplateTranscode.TemplateTranscodeContainer();
        templateTranscode.container.format = "mp4";
        templateTranscode.video = new TemplateTranscode.TemplateTranscodeVideo();
        templateTranscode.video.codec = "H.264";
        templateTranscode.video.profile = "high";
        templateTranscode.video.bitrate = "1000";
        templateTranscode.video.width = "1280";
        templateTranscode.video.fps = "30";
        templateTranscode.video.preset = "medium";
        templateTranscode.audio = new TemplateTranscode.TemplateTranscodeAudio();
        templateTranscode.audio.codec = "aac";
        templateTranscode.audio.samplerate = "44100";
        templateTranscode.audio.bitrate = "128";
        templateTranscode.audio.channels = "4";
        templateTranscode.transConfig = new TemplateTranscode.TemplateTranscodeTransConfig();
        templateTranscode.transConfig.adjDarMethod = "scale";
        templateTranscode.transConfig.isCheckReso = "false";
        templateTranscode.transConfig.resoAdjMethod = "1";
        templateTranscode.timeInterval = new TemplateTranscode.TemplateTranscodeTimeInterval();
        templateTranscode.timeInterval.start = "0";
        templateTranscode.timeInterval.duration = "60";
        templateTranscode.audioMixArray = new ArrayList<>();
        AudioMix audioMix = new AudioMix();
        audioMix.audioSource = String.format("http://%s.cos.%s.myqcloud.com/%s", TestConst.CI_BUCKET, TestConst.CI_BUCKET_REGION, TestConst.AUDIT_BUCKET_AUDIO);
        audioMix.mixMode = "Once";
        audioMix.replace = "true";
        audioMix.effectConfig = new EffectConfig();
        audioMix.effectConfig.enableStartFadein = "true";
        audioMix.effectConfig.startFadeinTime = "3";
        audioMix.effectConfig.enableEndFadeout = "false";
        audioMix.effectConfig.endFadeoutTime = "0";
        audioMix.effectConfig.enableBgmFade = "true";
        audioMix.effectConfig.bgmFadeTime = "1.7";
        templateTranscode.audioMixArray.add(audioMix);
        request.setTemplateTranscode(templateTranscode);
        final TestLocker testLocker = new TestLocker();
        ciService.templateTranscodeAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TemplateTranscodeResult templateTranscodeResult = (TemplateTranscodeResult)result;
                Assert.assertNotNull(templateTranscodeResult.response);
                TestUtils.printXML(templateTranscodeResult.response);
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
    public void templateSmartCover() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateSmartCoverRequest request = new TemplateSmartCoverRequest(TestConst.CI_BUCKET);
        TemplateSmartCover templateSmartCover = new TemplateSmartCover();
        templateSmartCover.name = "TestSmartCoverTemplate"+System.currentTimeMillis();
        templateSmartCover.smartCover = new TemplateSmartCover.TemplateSmartCoverSmartCover();
        templateSmartCover.smartCover.format = "jpg";
        templateSmartCover.smartCover.width = "1280";
        templateSmartCover.smartCover.height ="960";
        templateSmartCover.smartCover.count = "10";
        templateSmartCover.smartCover.deleteDuplicates = "true";
        request.setTemplateSmartCover(templateSmartCover);
        try {
            TemplateSmartCoverResult result = ciService.templateSmartCover(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void templateSmartCoverAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateSmartCoverRequest request = new TemplateSmartCoverRequest(TestConst.CI_BUCKET);
        TemplateSmartCover templateSmartCover = new TemplateSmartCover();
        templateSmartCover.name = "TestSmartCoverTemplate"+System.currentTimeMillis();
        templateSmartCover.smartCover = new TemplateSmartCover.TemplateSmartCoverSmartCover();
        templateSmartCover.smartCover.format = "jpg";
        templateSmartCover.smartCover.width = "1280";
        templateSmartCover.smartCover.height ="960";
        templateSmartCover.smartCover.count = "10";
        templateSmartCover.smartCover.deleteDuplicates = "true";
        request.setTemplateSmartCover(templateSmartCover);
        final TestLocker testLocker = new TestLocker();
        ciService.templateSmartCoverAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TemplateSmartCoverResult templateSmartCoverResult = (TemplateSmartCoverResult)result;
                Assert.assertNotNull(templateSmartCoverResult.response);
                TestUtils.printXML(templateSmartCoverResult.response);
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
    public void templateVideoMontage() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateVideoMontageRequest request = new TemplateVideoMontageRequest(TestConst.CI_BUCKET);
        TemplateVideoMontage templateVideoMontage = new TemplateVideoMontage();
        templateVideoMontage.name = "TestVideoMontageTemplate"+System.currentTimeMillis();
        templateVideoMontage.container = new TemplateVideoMontage.TemplateVideoMontageContainer();
        templateVideoMontage.container.format = "mp4";
        templateVideoMontage.video = new TemplateVideoMontage.TemplateVideoMontageVideo();
        templateVideoMontage.video.codec = "H.264";
        templateVideoMontage.video.bitrate = "1000";
        templateVideoMontage.video.width = "1280";
        templateVideoMontage.video.fps = "30";
        templateVideoMontage.audio = new TemplateVideoMontage.TemplateVideoMontageAudio();
        templateVideoMontage.audio.codec = "aac";
        templateVideoMontage.audio.samplerate = "44100";
        templateVideoMontage.audio.bitrate = "128";
        templateVideoMontage.audio.channels = "4";
        templateVideoMontage.audio.remove = "false";
        templateVideoMontage.audioMixArray = new ArrayList<>();
        AudioMix audioMix = new AudioMix();
        audioMix.audioSource = String.format("http://%s.cos.%s.myqcloud.com/%s", TestConst.CI_BUCKET, TestConst.CI_BUCKET_REGION, TestConst.AUDIT_BUCKET_AUDIO);
        audioMix.mixMode = "Once";
        audioMix.replace = "true";
        audioMix.effectConfig = new EffectConfig();
        audioMix.effectConfig.enableStartFadein = "true";
        audioMix.effectConfig.startFadeinTime = "3";
        audioMix.effectConfig.enableEndFadeout = "false";
        audioMix.effectConfig.endFadeoutTime = "0";
        audioMix.effectConfig.enableBgmFade = "true";
        audioMix.effectConfig.bgmFadeTime = "1.7";
        templateVideoMontage.audioMixArray.add(audioMix);
        request.setTemplateVideoMontage(templateVideoMontage);
        try {
            TemplateVideoMontageResult result = ciService.templateVideoMontage(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void templateVideoMontageAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditService();
        TemplateVideoMontageRequest request = new TemplateVideoMontageRequest(TestConst.CI_BUCKET);
        TemplateVideoMontage templateVideoMontage = new TemplateVideoMontage();
        templateVideoMontage.name = "TestVideoMontageTemplate"+System.currentTimeMillis();
        templateVideoMontage.container = new TemplateVideoMontage.TemplateVideoMontageContainer();
        templateVideoMontage.container.format = "mp4";
        templateVideoMontage.video = new TemplateVideoMontage.TemplateVideoMontageVideo();
        templateVideoMontage.video.codec = "H.264";
        templateVideoMontage.video.bitrate = "1000";
        templateVideoMontage.video.width = "1280";
        templateVideoMontage.video.fps = "30";
        templateVideoMontage.audio = new TemplateVideoMontage.TemplateVideoMontageAudio();
        templateVideoMontage.audio.codec = "aac";
        templateVideoMontage.audio.samplerate = "44100";
        templateVideoMontage.audio.bitrate = "128";
        templateVideoMontage.audio.channels = "4";
        templateVideoMontage.audio.remove = "false";
        templateVideoMontage.audioMixArray = new ArrayList<>();
        AudioMix audioMix = new AudioMix();
        audioMix.audioSource = String.format("http://%s.cos.%s.myqcloud.com/%s", TestConst.CI_BUCKET, TestConst.CI_BUCKET_REGION, TestConst.AUDIT_BUCKET_AUDIO);
        audioMix.mixMode = "Once";
        audioMix.replace = "true";
        audioMix.effectConfig = new EffectConfig();
        audioMix.effectConfig.enableStartFadein = "true";
        audioMix.effectConfig.startFadeinTime = "3";
        audioMix.effectConfig.enableEndFadeout = "false";
        audioMix.effectConfig.endFadeoutTime = "0";
        audioMix.effectConfig.enableBgmFade = "true";
        audioMix.effectConfig.bgmFadeTime = "1.7";
        templateVideoMontage.audioMixArray.add(audioMix);
        request.setTemplateVideoMontage(templateVideoMontage);
        final TestLocker testLocker = new TestLocker();
        ciService.templateVideoMontageAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TemplateVideoMontageResult templateVideoMontageResult = (TemplateVideoMontageResult)result;
                Assert.assertNotNull(templateVideoMontageResult.response);
                TestUtils.printXML(templateVideoMontageResult.response);
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


}
