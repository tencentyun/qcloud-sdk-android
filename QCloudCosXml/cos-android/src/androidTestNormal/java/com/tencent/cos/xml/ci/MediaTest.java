package com.tencent.cos.xml.ci;

import androidx.annotation.Nullable;

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
import com.tencent.cos.xml.model.ci.common.DigitalWatermark;
import com.tencent.cos.xml.model.ci.common.PicProcess;
import com.tencent.cos.xml.model.ci.media.GetPrivateM3U8Request;
import com.tencent.cos.xml.model.ci.media.GetPrivateM3U8Result;
import com.tencent.cos.xml.model.ci.media.GetWorkflowListRequest;
import com.tencent.cos.xml.model.ci.media.GetWorkflowListResult;
import com.tencent.cos.xml.model.ci.media.OperationVideoTag;
import com.tencent.cos.xml.model.ci.media.SearchMediaQueueRequest;
import com.tencent.cos.xml.model.ci.media.SearchMediaQueueResult;
import com.tencent.cos.xml.model.ci.media.SubmitAnimationJob;
import com.tencent.cos.xml.model.ci.media.SubmitAnimationJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitAnimationJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitConcatJob;
import com.tencent.cos.xml.model.ci.media.SubmitConcatJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitConcatJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitDigitalWatermarkJob;
import com.tencent.cos.xml.model.ci.media.SubmitDigitalWatermarkJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitDigitalWatermarkJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitExtractDigitalWatermarkJob;
import com.tencent.cos.xml.model.ci.media.SubmitExtractDigitalWatermarkJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitExtractDigitalWatermarkJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitMediaInfoJob;
import com.tencent.cos.xml.model.ci.media.SubmitMediaInfoJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitMediaInfoJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitMediaSegmentJob;
import com.tencent.cos.xml.model.ci.media.SubmitMediaSegmentJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitMediaSegmentJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitPicProcessJob;
import com.tencent.cos.xml.model.ci.media.SubmitPicProcessJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitPicProcessJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitSmartCoverJob;
import com.tencent.cos.xml.model.ci.media.SubmitSmartCoverJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitSmartCoverJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitSnapshotJob;
import com.tencent.cos.xml.model.ci.media.SubmitSnapshotJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitSnapshotJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitTranscodeJob;
import com.tencent.cos.xml.model.ci.media.SubmitTranscodeJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitTranscodeJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitVideoMontageJob;
import com.tencent.cos.xml.model.ci.media.SubmitVideoMontageJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitVideoMontageJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitVideoTagJob;
import com.tencent.cos.xml.model.ci.media.SubmitVideoTagJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitVideoTagJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitVoiceSeparateJob;
import com.tencent.cos.xml.model.ci.media.SubmitVoiceSeparateJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitVoiceSeparateJobResult;
import com.tencent.cos.xml.model.ci.media.TemplateAnimation;
import com.tencent.cos.xml.model.ci.media.TemplateConcat;
import com.tencent.cos.xml.model.ci.media.TemplateSmartCover;
import com.tencent.cos.xml.model.ci.media.TemplateSnapshot;
import com.tencent.cos.xml.model.ci.media.TemplateTranscode;
import com.tencent.cos.xml.model.ci.media.TemplateVideoMontage;
import com.tencent.cos.xml.model.ci.media.TemplateVoiceSeparate;
import com.tencent.cos.xml.model.ci.media.TemplateWatermark;
import com.tencent.cos.xml.model.ci.media.TriggerWorkflowRequest;
import com.tencent.cos.xml.model.ci.media.TriggerWorkflowResult;
import com.tencent.cos.xml.model.ci.media.UpdateMediaQueue;
import com.tencent.cos.xml.model.ci.media.UpdateMediaQueueRequest;
import com.tencent.cos.xml.model.ci.media.UpdateMediaQueueResult;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created by jordanqin on 2023/6/20 16:31.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class MediaTest {
    @Test
    public void getPrivateM3U8() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetPrivateM3U8Request request = new GetPrivateM3U8Request(TestConst.ASR_BUCKET, TestConst.MEDIA_BUCKET_M3U8);
        request.ci_process = "pm3u8";
        // 私有 ts 资源 url 下载凭证的相对有效期，单位为秒，范围为[3600, 43200]
        request.expires = "5000";
        try {
            GetPrivateM3U8Result result = ciService.getPrivateM3U8(request);
            Assert.assertNotNull(result.response);
            TestUtils.print(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void getPrivateM3U8Async() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetPrivateM3U8Request request = new GetPrivateM3U8Request(TestConst.ASR_BUCKET, TestConst.MEDIA_BUCKET_M3U8);
        request.ci_process = "pm3u8";
        // 私有 ts 资源 url 下载凭证的相对有效期，单位为秒，范围为[3600, 43200]
        request.expires = "5000";
        final TestLocker testLocker = new TestLocker();
        ciService.getPrivateM3U8Async(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                GetPrivateM3U8Result getPrivateM3U8Result = (GetPrivateM3U8Result)result;
                Assert.assertNotNull(getPrivateM3U8Result.response);
                TestUtils.print(getPrivateM3U8Result.response);
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
    public void stage1_searchMediaQueue() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SearchMediaQueueRequest request = new SearchMediaQueueRequest(TestConst.ASR_BUCKET);
        request.queueIds="aaa,bbb,ccc";
        request.state = "Active";
        request.category = "CateAll";
        request.pageNumber = "1";
        request.pageSize = "10";
        try {
            SearchMediaQueueResult result = ciService.searchMediaQueue(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
            if(result.response.queueList!=null && result.response.queueList.size()>0) {
                updateMediaQueueId = result.response.queueList.get(0).queueId;
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage1_searchMediaQueueAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SearchMediaQueueRequest request = new SearchMediaQueueRequest(TestConst.ASR_BUCKET);
        request.queueIds="aaa,bbb,ccc";
        request.state = "Active";
        request.category = "CateAll";
        request.pageNumber = "1";
        request.pageSize = "10";
        final TestLocker testLocker = new TestLocker();
        ciService.searchMediaQueueAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SearchMediaQueueResult SearchMediaQueueResult = (SearchMediaQueueResult)result;
                Assert.assertNotNull(SearchMediaQueueResult.response);
                TestUtils.printXML(SearchMediaQueueResult.response);

                if(SearchMediaQueueResult.response.queueList!=null && SearchMediaQueueResult.response.queueList.size()>0){
                    updateMediaQueueId = SearchMediaQueueResult.response.queueList.get(0).queueId;
                }
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

    String updateMediaQueueId;
    @Test
    public void stage2_updateMediaQueue() {
        if(updateMediaQueueId == null) return;

        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateMediaQueueRequest request = new UpdateMediaQueueRequest(TestConst.ASR_BUCKET, updateMediaQueueId);
        UpdateMediaQueue updateMediaQueue = new UpdateMediaQueue();
        updateMediaQueue.name = "My-Queue-Media";
        updateMediaQueue.state = "Active";
        UpdateMediaQueue.UpdateMediaQueueNotifyConfig notifyConfig = new UpdateMediaQueue.UpdateMediaQueueNotifyConfig();
        notifyConfig.state = "On";
        notifyConfig.type = "Url";
        notifyConfig.url = "http://callback.demo.com";
        notifyConfig.event = "TaskFinish,WorkflowFinish";
        notifyConfig.resultFormat = "JSON";
        updateMediaQueue.notifyConfig = notifyConfig;
        request.setUpdateMediaQueue(updateMediaQueue);
        try {
            UpdateMediaQueueResult result = ciService.updateMediaQueue(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage2_updateMediaQueueAsync() {
        if(updateMediaQueueId == null) return;

        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateMediaQueueRequest request = new UpdateMediaQueueRequest(TestConst.ASR_BUCKET, updateMediaQueueId);
        UpdateMediaQueue updateMediaQueue = new UpdateMediaQueue();
        updateMediaQueue.name = "My-Queue-Media";
        updateMediaQueue.state = "Active";
        UpdateMediaQueue.UpdateMediaQueueNotifyConfig notifyConfig = new UpdateMediaQueue.UpdateMediaQueueNotifyConfig();
        notifyConfig.state = "On";
        notifyConfig.type = "Url";
        notifyConfig.url = "http://callback.demo.com";
        notifyConfig.event = "TaskFinish,WorkflowFinish";
        notifyConfig.resultFormat = "JSON";
        updateMediaQueue.notifyConfig = notifyConfig;
        request.setUpdateMediaQueue(updateMediaQueue);
        final TestLocker testLocker = new TestLocker();
        ciService.updateMediaQueueAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                UpdateMediaQueueResult UpdateMediaQueueResult = (UpdateMediaQueueResult)result;
                Assert.assertNotNull(UpdateMediaQueueResult.response);
                TestUtils.printXML(UpdateMediaQueueResult.response);
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
    public void submitSnapshotJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitSnapshotJobRequest request = new SubmitSnapshotJobRequest(TestConst.ASR_BUCKET);
        SubmitSnapshotJob submitSnapshotJob = new SubmitSnapshotJob();
        SubmitSnapshotJob.SubmitSnapshotJobInput input = new SubmitSnapshotJob.SubmitSnapshotJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitSnapshotJob.input = input;
        SubmitSnapshotJob.SubmitSnapshotJobOperation operation = new SubmitSnapshotJob.SubmitSnapshotJobOperation();
        TemplateSnapshot.TemplateSnapshotSnapshot snapshot = new TemplateSnapshot.TemplateSnapshotSnapshot();
        snapshot.count = "3";
        operation.snapshot = snapshot;
        SubmitSnapshotJob.SubmitSnapshotJobOutput output = new SubmitSnapshotJob.SubmitSnapshotJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_snapshot-${number}.jpg";
        output.spriteObject = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_sprite-${number}.jpg";
        operation.output = output;
        operation.jobLevel = "0";
        operation.userData = "aaaaaa";
        submitSnapshotJob.operation = operation;
        submitSnapshotJob.callBackFormat = "XML";
        submitSnapshotJob.callBackType = "Url";
//        submitSnapshotJob.callBack = "https://callback.demo.com";
        request.setSubmitSnapshotJob(submitSnapshotJob);

        try {
            SubmitSnapshotJobResult result = ciService.submitSnapshotJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitSnapshotJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitSnapshotJobRequest request = new SubmitSnapshotJobRequest(TestConst.ASR_BUCKET);
        SubmitSnapshotJob submitSnapshotJob = new SubmitSnapshotJob();
        SubmitSnapshotJob.SubmitSnapshotJobInput input = new SubmitSnapshotJob.SubmitSnapshotJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitSnapshotJob.input = input;
        SubmitSnapshotJob.SubmitSnapshotJobOperation operation = new SubmitSnapshotJob.SubmitSnapshotJobOperation();
        TemplateSnapshot.TemplateSnapshotSnapshot snapshot = new TemplateSnapshot.TemplateSnapshotSnapshot();
        snapshot.count = "3";
        operation.snapshot = snapshot;
        SubmitSnapshotJob.SubmitSnapshotJobOutput output = new SubmitSnapshotJob.SubmitSnapshotJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_snapshot-${number}.jpg";
        output.spriteObject = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_sprite-${number}.jpg";
        operation.output = output;
        operation.jobLevel = "0";
        operation.userData = "aaaaaa";
        submitSnapshotJob.operation = operation;
        submitSnapshotJob.callBackFormat = "XML";
        submitSnapshotJob.callBackType = "Url";
//        submitSnapshotJob.callBack = "https://callback.demo.com";
        request.setSubmitSnapshotJob(submitSnapshotJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitSnapshotJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitSnapshotJobResult SubmitSnapshotJobResult = (SubmitSnapshotJobResult)result;
                Assert.assertNotNull(SubmitSnapshotJobResult.response);
                TestUtils.printXML(SubmitSnapshotJobResult.response);
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
    public void submitTranscodeJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitTranscodeJobRequest request = new SubmitTranscodeJobRequest(TestConst.ASR_BUCKET);
        SubmitTranscodeJob submitTranscodeJob = new SubmitTranscodeJob();
        SubmitTranscodeJob.SubmitTranscodeJobInput input = new SubmitTranscodeJob.SubmitTranscodeJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitTranscodeJob.input = input;
        SubmitTranscodeJob.SubmitTranscodeJobOutput output = new SubmitTranscodeJob.SubmitTranscodeJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_transcode.${ext}";
        SubmitTranscodeJob.SubmitTranscodeJobOperation operation = new SubmitTranscodeJob.SubmitTranscodeJobOperation();
//        operation.templateId = "t1460606b9752148c4ab182f55163ba7cd";
        ArrayList<String> watermarkTemplateId = new ArrayList<>();
        SubmitTranscodeJob.SubmitTranscodeJobTranscode transcode = new SubmitTranscodeJob.SubmitTranscodeJobTranscode();
        TemplateTranscode.TemplateTranscodeContainer container = new TemplateTranscode.TemplateTranscodeContainer();
        container.format = "avi";
        transcode.container = container;
        operation.transcode = transcode;
        watermarkTemplateId.add("t1318c5f428d474afba1797f84091cbe22");
        watermarkTemplateId.add("t1318c5f428d474afba1797f84091cbe23");
        watermarkTemplateId.add("t1318c5f428d474afba1797f84091cbe24");
//        operation.watermarkTemplateId = watermarkTemplateId;
        List<TemplateWatermark.Watermark> watermarks = new ArrayList<>();
        TemplateWatermark.TemplateWatermarkText watermarkText = new TemplateWatermark.TemplateWatermarkText();
        watermarkText.fontSize = "10";
        watermarkText.fontType = "simfang.ttf";
        watermarkText.fontColor = "0x000000";
        watermarkText.transparency = "30";
        watermarkText.text = "水印内容";
        TemplateWatermark.Watermark watermark1 = new TemplateWatermark.Watermark();
        watermark1.type = "Text";
        watermark1.pos = "Center";
        watermark1.locMode = "Absolute";
        watermark1.dx = "10";
        watermark1.dy = "20";
        watermark1.text = watermarkText;
        watermarks.add(watermark1);
        TemplateWatermark.Watermark watermark2 = new TemplateWatermark.Watermark();
        watermark2.type = "Text";
        watermark2.pos = "Center";
        watermark2.locMode = "Absolute";
        watermark2.dx = "20";
        watermark2.dy = "10";
        watermark2.text = watermarkText;
        watermarks.add(watermark2);
        operation.watermark = watermarks;
        SubmitTranscodeJob.SubmitTranscodeJobSubtitles subtitles = new SubmitTranscodeJob.SubmitTranscodeJobSubtitles();
        List<SubmitTranscodeJob.SubmitTranscodeJobSubtitle> subtitleList = new ArrayList<>();
        SubmitTranscodeJob.SubmitTranscodeJobSubtitle subtitle1 = new SubmitTranscodeJob.SubmitTranscodeJobSubtitle();
        subtitle1.url = "https://ci-auditing-sample-1253960454.cos.ap-guangzhou.myqcloud.com/media/test1.srt";
        subtitleList.add(subtitle1);
        SubmitTranscodeJob.SubmitTranscodeJobSubtitle subtitle2 = new SubmitTranscodeJob.SubmitTranscodeJobSubtitle();
        subtitle2.url = "https://ci-auditing-sample-1253960454.cos.ap-guangzhou.myqcloud.com/media/test2.srt";
        subtitleList.add(subtitle2);
        subtitles.subtitle = subtitleList;
//        operation.subtitles = subtitles;
        operation.output = output;
        operation.jobLevel = "0";
        operation.userData = "aaaaaa";
        submitTranscodeJob.operation = operation;
        submitTranscodeJob.callBackFormat = "XML";
        submitTranscodeJob.callBackType = "Url";
//        submitTranscodeJob.callBack = "https://callback.demo.com";
        request.setSubmitTranscodeJob(submitTranscodeJob);

        try {
            SubmitTranscodeJobResult result = ciService.submitTranscodeJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitTranscodeJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitTranscodeJobRequest request = new SubmitTranscodeJobRequest(TestConst.ASR_BUCKET);
        SubmitTranscodeJob submitTranscodeJob = new SubmitTranscodeJob();
        SubmitTranscodeJob.SubmitTranscodeJobInput input = new SubmitTranscodeJob.SubmitTranscodeJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitTranscodeJob.input = input;
        SubmitTranscodeJob.SubmitTranscodeJobOutput output = new SubmitTranscodeJob.SubmitTranscodeJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_transcode.${ext}";
        SubmitTranscodeJob.SubmitTranscodeJobOperation operation = new SubmitTranscodeJob.SubmitTranscodeJobOperation();
//        operation.templateId = "t1460606b9752148c4ab182f55163ba7cd";
        ArrayList<String> watermarkTemplateId = new ArrayList<>();
        SubmitTranscodeJob.SubmitTranscodeJobTranscode transcode = new SubmitTranscodeJob.SubmitTranscodeJobTranscode();
        TemplateTranscode.TemplateTranscodeContainer container = new TemplateTranscode.TemplateTranscodeContainer();
        container.format = "avi";
        transcode.container = container;
        operation.transcode = transcode;
        watermarkTemplateId.add("t1318c5f428d474afba1797f84091cbe22");
        watermarkTemplateId.add("t1318c5f428d474afba1797f84091cbe23");
        watermarkTemplateId.add("t1318c5f428d474afba1797f84091cbe24");
//        operation.watermarkTemplateId = watermarkTemplateId;
        List<TemplateWatermark.Watermark> watermarks = new ArrayList<>();
        TemplateWatermark.TemplateWatermarkText watermarkText = new TemplateWatermark.TemplateWatermarkText();
        watermarkText.fontSize = "10";
        watermarkText.fontType = "simfang.ttf";
        watermarkText.fontColor = "0x000000";
        watermarkText.transparency = "30";
        watermarkText.text = "水印内容";
        TemplateWatermark.Watermark watermark1 = new TemplateWatermark.Watermark();
        watermark1.type = "Text";
        watermark1.pos = "Center";
        watermark1.locMode = "Absolute";
        watermark1.dx = "10";
        watermark1.dy = "20";
        watermark1.text = watermarkText;
        watermarks.add(watermark1);
        TemplateWatermark.Watermark watermark2 = new TemplateWatermark.Watermark();
        watermark2.type = "Text";
        watermark2.pos = "Center";
        watermark2.locMode = "Absolute";
        watermark2.dx = "20";
        watermark2.dy = "10";
        watermark2.text = watermarkText;
        watermarks.add(watermark2);
        operation.watermark = watermarks;
        SubmitTranscodeJob.SubmitTranscodeJobSubtitles subtitles = new SubmitTranscodeJob.SubmitTranscodeJobSubtitles();
        List<SubmitTranscodeJob.SubmitTranscodeJobSubtitle> subtitleList = new ArrayList<>();
        SubmitTranscodeJob.SubmitTranscodeJobSubtitle subtitle1 = new SubmitTranscodeJob.SubmitTranscodeJobSubtitle();
        subtitle1.url = "https://ci-auditing-sample-1253960454.cos.ap-guangzhou.myqcloud.com/media/test1.srt";
        subtitleList.add(subtitle1);
        SubmitTranscodeJob.SubmitTranscodeJobSubtitle subtitle2 = new SubmitTranscodeJob.SubmitTranscodeJobSubtitle();
        subtitle2.url = "https://ci-auditing-sample-1253960454.cos.ap-guangzhou.myqcloud.com/media/test2.srt";
        subtitleList.add(subtitle2);
        subtitles.subtitle = subtitleList;
//        operation.subtitles = subtitles;
        operation.output = output;
        operation.jobLevel = "0";
        operation.userData = "aaaaaa";
        submitTranscodeJob.operation = operation;
        submitTranscodeJob.callBackFormat = "XML";
        submitTranscodeJob.callBackType = "Url";
//        submitTranscodeJob.callBack = "https://callback.demo.com";
        request.setSubmitTranscodeJob(submitTranscodeJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitTranscodeJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitTranscodeJobResult SubmitTranscodeJobResult = (SubmitTranscodeJobResult)result;
                Assert.assertNotNull(SubmitTranscodeJobResult.response);
                TestUtils.printXML(SubmitTranscodeJobResult.response);
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
    public void submitAnimationJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitAnimationJobRequest request = new SubmitAnimationJobRequest(TestConst.ASR_BUCKET);
        SubmitAnimationJob submitAnimationJob = new SubmitAnimationJob();
        SubmitAnimationJob.SubmitAnimationJobInput input = new SubmitAnimationJob.SubmitAnimationJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitAnimationJob.input = input;
        SubmitAnimationJob.SubmitAnimationJobOutput output = new SubmitAnimationJob.SubmitAnimationJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_animation.${ext}";
        SubmitAnimationJob.SubmitAnimationJobOperation operation = new SubmitAnimationJob.SubmitAnimationJobOperation();
        operation.output = output;
        SubmitAnimationJob.SubmitAnimationJobAnimation animation = new SubmitAnimationJob.SubmitAnimationJobAnimation();
        TemplateAnimation.TemplateAnimationContainer container = new TemplateAnimation.TemplateAnimationContainer();
        container.format = "gif";
        TemplateAnimation.TemplateAnimationVideo video = new TemplateAnimation.TemplateAnimationVideo();
        video.codec = "gif";
        video.width = "300";
        video.height = "200";
        video.fps = "15";
        video.animateOnlyKeepKeyFrame = "true";
        TemplateAnimation.TemplateAnimationTimeInterval timeInterval = new TemplateAnimation.TemplateAnimationTimeInterval();
        timeInterval.start = "0";
        timeInterval.duration = "60";
        animation.timeInterval = timeInterval;
        animation.video = video;
        animation.container = container;
        operation.animation = animation;
        operation.jobLevel = "0";
        operation.userData = "aaaaaa";
        submitAnimationJob.operation = operation;
        submitAnimationJob.callBackFormat = "XML";
        submitAnimationJob.callBackType = "Url";
//        submitAnimationJob.callBack = "http://callback.demo.com";
        request.setSubmitAnimationJob(submitAnimationJob);

        try {
            SubmitAnimationJobResult result = ciService.submitAnimationJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitAnimationJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitAnimationJobRequest request = new SubmitAnimationJobRequest(TestConst.ASR_BUCKET);
        SubmitAnimationJob submitAnimationJob = new SubmitAnimationJob();
        SubmitAnimationJob.SubmitAnimationJobInput input = new SubmitAnimationJob.SubmitAnimationJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitAnimationJob.input = input;
        SubmitAnimationJob.SubmitAnimationJobOutput output = new SubmitAnimationJob.SubmitAnimationJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_animation.${ext}";
        SubmitAnimationJob.SubmitAnimationJobOperation operation = new SubmitAnimationJob.SubmitAnimationJobOperation();
        operation.output = output;
        SubmitAnimationJob.SubmitAnimationJobAnimation animation = new SubmitAnimationJob.SubmitAnimationJobAnimation();
        TemplateAnimation.TemplateAnimationContainer container = new TemplateAnimation.TemplateAnimationContainer();
        container.format = "gif";
        TemplateAnimation.TemplateAnimationVideo video = new TemplateAnimation.TemplateAnimationVideo();
        video.codec = "gif";
        video.width = "300";
        video.height = "200";
        video.fps = "15";
        video.animateOnlyKeepKeyFrame = "true";
        TemplateAnimation.TemplateAnimationTimeInterval timeInterval = new TemplateAnimation.TemplateAnimationTimeInterval();
        timeInterval.start = "0";
        timeInterval.duration = "60";
        animation.timeInterval = timeInterval;
        animation.video = video;
        animation.container = container;
        operation.animation = animation;
        operation.jobLevel = "0";
        operation.userData = "aaaaaa";
        submitAnimationJob.operation = operation;
        submitAnimationJob.callBackFormat = "XML";
        submitAnimationJob.callBackType = "Url";
//        submitAnimationJob.callBack = "http://callback.demo.com";
        request.setSubmitAnimationJob(submitAnimationJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitAnimationJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitAnimationJobResult SubmitAnimationJobResult = (SubmitAnimationJobResult)result;
                Assert.assertNotNull(SubmitAnimationJobResult.response);
                TestUtils.printXML(SubmitAnimationJobResult.response);
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
    public void submitConcatJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitConcatJobRequest request = new SubmitConcatJobRequest(TestConst.ASR_BUCKET);
        SubmitConcatJob submitConcatJob = new SubmitConcatJob();
        SubmitConcatJob.SubmitConcatJobInput input = new SubmitConcatJob.SubmitConcatJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitConcatJob.input = input;
        SubmitConcatJob.SubmitConcatJobOutput output = new SubmitConcatJob.SubmitConcatJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_concat.${ext}";
        SubmitConcatJob.SubmitConcatJobOperation operation = new SubmitConcatJob.SubmitConcatJobOperation();
        operation.output = output;
        SubmitConcatJob.SubmitConcatJobConcatTemplate concatTemplate = new SubmitConcatJob.SubmitConcatJobConcatTemplate();
        TemplateConcat.TemplateConcatContainer container = new TemplateConcat.TemplateConcatContainer();
        container.format = "flv";
        concatTemplate.container = container;
        operation.concatTemplate = concatTemplate;
        operation.jobLevel = "0";
        submitConcatJob.operation = operation;
        submitConcatJob.callBackFormat = "XML";
        submitConcatJob.callBackType = "Url";
//        submitConcatJob.callBack = "http://callback.demo.com";
        request.setSubmitConcatJob(submitConcatJob);

        try {
            SubmitConcatJobResult result = ciService.submitConcatJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitConcatJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitConcatJobRequest request = new SubmitConcatJobRequest(TestConst.ASR_BUCKET);
        SubmitConcatJob submitConcatJob = new SubmitConcatJob();
        SubmitConcatJob.SubmitConcatJobInput input = new SubmitConcatJob.SubmitConcatJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitConcatJob.input = input;
        SubmitConcatJob.SubmitConcatJobOutput output = new SubmitConcatJob.SubmitConcatJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_concat.${ext}";
        SubmitConcatJob.SubmitConcatJobOperation operation = new SubmitConcatJob.SubmitConcatJobOperation();
        operation.output = output;
        SubmitConcatJob.SubmitConcatJobConcatTemplate concatTemplate = new SubmitConcatJob.SubmitConcatJobConcatTemplate();
        TemplateConcat.TemplateConcatContainer container = new TemplateConcat.TemplateConcatContainer();
        container.format = "flv";
        concatTemplate.container = container;
        operation.concatTemplate = concatTemplate;
        operation.jobLevel = "0";
        submitConcatJob.operation = operation;
        submitConcatJob.callBackFormat = "XML";
        submitConcatJob.callBackType = "Url";
//        submitConcatJob.callBack = "http://callback.demo.com";
        request.setSubmitConcatJob(submitConcatJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitConcatJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitConcatJobResult SubmitConcatJobResult = (SubmitConcatJobResult)result;
                Assert.assertNotNull(SubmitConcatJobResult.response);
                TestUtils.printXML(SubmitConcatJobResult.response);
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
    public void submitSmartCoverJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitSmartCoverJobRequest request = new SubmitSmartCoverJobRequest(TestConst.ASR_BUCKET);
        SubmitSmartCoverJob SubmitSmartCoverJob = new SubmitSmartCoverJob();
        SubmitSmartCoverJob.SubmitSmartCoverJobInput input = new SubmitSmartCoverJob.SubmitSmartCoverJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        SubmitSmartCoverJob.input = input;
        SubmitSmartCoverJob.SubmitSmartCoverJobOutput output = new SubmitSmartCoverJob.SubmitSmartCoverJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"smartcover-${number}.png";
        SubmitSmartCoverJob.SubmitSmartCoverJobOperation operation = new SubmitSmartCoverJob.SubmitSmartCoverJobOperation();
        operation.output = output;
        TemplateSmartCover.TemplateSmartCoverSmartCover smartCover = new TemplateSmartCover.TemplateSmartCoverSmartCover();
        smartCover.format = "png";
        operation.smartCover = smartCover;
        operation.jobLevel = "0";
        SubmitSmartCoverJob.operation = operation;
        SubmitSmartCoverJob.callBackFormat = "XML";
        SubmitSmartCoverJob.callBackType = "Url";
//        SubmitSmartCoverJob.callBack = "http://callback.demo.com";
        request.setSubmitSmartCoverJob(SubmitSmartCoverJob);

        try {
            SubmitSmartCoverJobResult result = ciService.submitSmartCoverJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitSmartCoverJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitSmartCoverJobRequest request = new SubmitSmartCoverJobRequest(TestConst.ASR_BUCKET);
        SubmitSmartCoverJob SubmitSmartCoverJob = new SubmitSmartCoverJob();
        SubmitSmartCoverJob.SubmitSmartCoverJobInput input = new SubmitSmartCoverJob.SubmitSmartCoverJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        SubmitSmartCoverJob.input = input;
        SubmitSmartCoverJob.SubmitSmartCoverJobOutput output = new SubmitSmartCoverJob.SubmitSmartCoverJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"smartcover-${number}.png";
        SubmitSmartCoverJob.SubmitSmartCoverJobOperation operation = new SubmitSmartCoverJob.SubmitSmartCoverJobOperation();
        operation.output = output;
        TemplateSmartCover.TemplateSmartCoverSmartCover smartCover = new TemplateSmartCover.TemplateSmartCoverSmartCover();
        smartCover.format = "png";
        operation.smartCover = smartCover;
        operation.jobLevel = "0";
        SubmitSmartCoverJob.operation = operation;
        SubmitSmartCoverJob.callBackFormat = "XML";
        SubmitSmartCoverJob.callBackType = "Url";
//        SubmitSmartCoverJob.callBack = "http://callback.demo.com";
        request.setSubmitSmartCoverJob(SubmitSmartCoverJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitSmartCoverJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitSmartCoverJobResult SubmitSmartCoverJobResult = (SubmitSmartCoverJobResult)result;
                Assert.assertNotNull(SubmitSmartCoverJobResult.response);
                TestUtils.printXML(SubmitSmartCoverJobResult.response);
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
    public void submitVideoMontageJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitVideoMontageJobRequest request = new SubmitVideoMontageJobRequest(TestConst.PERSIST_BUCKET);
        SubmitVideoMontageJob SubmitVideoMontageJob = new SubmitVideoMontageJob();
        SubmitVideoMontageJob.SubmitVideoMontageJobInput input = new SubmitVideoMontageJob.SubmitVideoMontageJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        SubmitVideoMontageJob.input = input;
        SubmitVideoMontageJob.SubmitVideoMontageJobOutput output = new SubmitVideoMontageJob.SubmitVideoMontageJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.PERSIST_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_montage.${ext}";
        SubmitVideoMontageJob.SubmitVideoMontageJobOperation operation = new SubmitVideoMontageJob.SubmitVideoMontageJobOperation();
        operation.output = output;
        SubmitVideoMontageJob.SubmitVideoMontageJobVideoMontage videoMontage = new SubmitVideoMontageJob.SubmitVideoMontageJobVideoMontage();
        TemplateVideoMontage.TemplateVideoMontageContainer container = new TemplateVideoMontage.TemplateVideoMontageContainer();
        container.format = "mp4";
        videoMontage.container = container;
        videoMontage.video = new TemplateVideoMontage.TemplateVideoMontageVideo();
        videoMontage.video.codec = "H.264";
        videoMontage.video.width = "1280";
        operation.videoMontage = videoMontage;
        operation.jobLevel = "0";
        SubmitVideoMontageJob.operation = operation;
        SubmitVideoMontageJob.callBackFormat = "XML";
        SubmitVideoMontageJob.callBackType = "Url";
//        SubmitVideoMontageJob.callBack = "http://callback.demo.com";
        request.setSubmitVideoMontageJob(SubmitVideoMontageJob);

        try {
            SubmitVideoMontageJobResult result = ciService.submitVideoMontageJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitVideoMontageJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitVideoMontageJobRequest request = new SubmitVideoMontageJobRequest(TestConst.PERSIST_BUCKET);
        SubmitVideoMontageJob SubmitVideoMontageJob = new SubmitVideoMontageJob();
        SubmitVideoMontageJob.SubmitVideoMontageJobInput input = new SubmitVideoMontageJob.SubmitVideoMontageJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        SubmitVideoMontageJob.input = input;
        SubmitVideoMontageJob.SubmitVideoMontageJobOutput output = new SubmitVideoMontageJob.SubmitVideoMontageJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.PERSIST_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_montage.${ext}";
        SubmitVideoMontageJob.SubmitVideoMontageJobOperation operation = new SubmitVideoMontageJob.SubmitVideoMontageJobOperation();
        operation.output = output;
        SubmitVideoMontageJob.SubmitVideoMontageJobVideoMontage videoMontage = new SubmitVideoMontageJob.SubmitVideoMontageJobVideoMontage();
        TemplateVideoMontage.TemplateVideoMontageContainer container = new TemplateVideoMontage.TemplateVideoMontageContainer();
        container.format = "mp4";
        videoMontage.container = container;
        videoMontage.video = new TemplateVideoMontage.TemplateVideoMontageVideo();
        videoMontage.video.codec = "H.264";
        videoMontage.video.width = "1280";
        operation.videoMontage = videoMontage;
        operation.jobLevel = "0";
        SubmitVideoMontageJob.operation = operation;
        SubmitVideoMontageJob.callBackFormat = "XML";
        SubmitVideoMontageJob.callBackType = "Url";
//        SubmitVideoMontageJob.callBack = "http://callback.demo.com";
        request.setSubmitVideoMontageJob(SubmitVideoMontageJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitVideoMontageJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitVideoMontageJobResult SubmitVideoMontageJobResult = (SubmitVideoMontageJobResult)result;
                Assert.assertNotNull(SubmitVideoMontageJobResult.response);
                TestUtils.printXML(SubmitVideoMontageJobResult.response);
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
    public void submitVoiceSeparateJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitVoiceSeparateJobRequest request = new SubmitVoiceSeparateJobRequest(TestConst.ASR_BUCKET);
        SubmitVoiceSeparateJob submitVoiceSeparateJob = new SubmitVoiceSeparateJob();
        SubmitVoiceSeparateJob.SubmitVoiceSeparateJobInput input = new SubmitVoiceSeparateJob.SubmitVoiceSeparateJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitVoiceSeparateJob.input = input;
        SubmitVoiceSeparateJob.SubmitVoiceSeparateJobOutput output = new SubmitVoiceSeparateJob.SubmitVoiceSeparateJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_backgroud.${ext}";
        output.auObject = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_audio.${ext}";
        SubmitVoiceSeparateJob.SubmitVoiceSeparateJobOperation operation = new SubmitVoiceSeparateJob.SubmitVoiceSeparateJobOperation();
        operation.output = output;
        SubmitVoiceSeparateJob.VoiceSeparate voiceSeparate = new SubmitVoiceSeparateJob.VoiceSeparate();
        voiceSeparate.audioMode = "IsAudio";
        TemplateVoiceSeparate.AudioConfig audioConfig = new TemplateVoiceSeparate.AudioConfig();
        audioConfig.codec = "aac";
        audioConfig.samplerate = "44100";
        audioConfig.bitrate = "128";
        audioConfig.channels = "4";
        voiceSeparate.audioConfig = audioConfig;
        operation.voiceSeparate = voiceSeparate;
        operation.jobLevel = "0";
        submitVoiceSeparateJob.operation = operation;
        submitVoiceSeparateJob.callBackFormat = "XML";
        submitVoiceSeparateJob.callBackType = "Url";
//        submitVoiceSeparateJob.callBack = "http://callback.demo.com";
        request.setSubmitVoiceSeparateJob(submitVoiceSeparateJob);

        try {
            SubmitVoiceSeparateJobResult result = ciService.submitVoiceSeparateJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitVoiceSeparateJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitVoiceSeparateJobRequest request = new SubmitVoiceSeparateJobRequest(TestConst.ASR_BUCKET);
        SubmitVoiceSeparateJob submitVoiceSeparateJob = new SubmitVoiceSeparateJob();
        SubmitVoiceSeparateJob.SubmitVoiceSeparateJobInput input = new SubmitVoiceSeparateJob.SubmitVoiceSeparateJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitVoiceSeparateJob.input = input;
        SubmitVoiceSeparateJob.SubmitVoiceSeparateJobOutput output = new SubmitVoiceSeparateJob.SubmitVoiceSeparateJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_backgroud.${ext}";
        output.auObject = TestConst.MEDIA_BUCKET_JOB_RESULT+"video_audio.${ext}";
        SubmitVoiceSeparateJob.SubmitVoiceSeparateJobOperation operation = new SubmitVoiceSeparateJob.SubmitVoiceSeparateJobOperation();
        operation.output = output;
        SubmitVoiceSeparateJob.VoiceSeparate voiceSeparate = new SubmitVoiceSeparateJob.VoiceSeparate();
        voiceSeparate.audioMode = "IsAudio";
        TemplateVoiceSeparate.AudioConfig audioConfig = new TemplateVoiceSeparate.AudioConfig();
        audioConfig.codec = "aac";
        audioConfig.samplerate = "44100";
        audioConfig.bitrate = "128";
        audioConfig.channels = "4";
        voiceSeparate.audioConfig = audioConfig;
        operation.voiceSeparate = voiceSeparate;
        operation.jobLevel = "0";
        submitVoiceSeparateJob.operation = operation;
        submitVoiceSeparateJob.callBackFormat = "XML";
        submitVoiceSeparateJob.callBackType = "Url";
//        submitVoiceSeparateJob.callBack = "http://callback.demo.com";
        request.setSubmitVoiceSeparateJob(submitVoiceSeparateJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitVoiceSeparateJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitVoiceSeparateJobResult SubmitVoiceSeparateJobResult = (SubmitVoiceSeparateJobResult)result;
                Assert.assertNotNull(SubmitVoiceSeparateJobResult.response);
                TestUtils.printXML(SubmitVoiceSeparateJobResult.response);
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
    public void submitDigitalWatermarkJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitDigitalWatermarkJobRequest request = new SubmitDigitalWatermarkJobRequest(TestConst.ASR_BUCKET);
        SubmitDigitalWatermarkJob SubmitDigitalWatermarkJob = new SubmitDigitalWatermarkJob();
        SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobInput input = new SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        SubmitDigitalWatermarkJob.input = input;
        SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobOutput output = new SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"DigitalWatermark.mp4";
        SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobOperation operation = new SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobOperation();
        operation.output = output;
        DigitalWatermark digitalWatermark = new DigitalWatermark();
        digitalWatermark.message = "DigitalWatermarkTest";
        operation.digitalWatermark = digitalWatermark;
        operation.jobLevel = "0";
        SubmitDigitalWatermarkJob.operation = operation;
        SubmitDigitalWatermarkJob.callBackFormat = "XML";
        SubmitDigitalWatermarkJob.callBackType = "Url";
//        SubmitDigitalWatermarkJob.callBack = "http://callback.demo.com";
        request.setSubmitDigitalWatermarkJob(SubmitDigitalWatermarkJob);

        try {
            SubmitDigitalWatermarkJobResult result = ciService.submitDigitalWatermarkJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitDigitalWatermarkJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitDigitalWatermarkJobRequest request = new SubmitDigitalWatermarkJobRequest(TestConst.ASR_BUCKET);
        SubmitDigitalWatermarkJob SubmitDigitalWatermarkJob = new SubmitDigitalWatermarkJob();
        SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobInput input = new SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        SubmitDigitalWatermarkJob.input = input;
        SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobOutput output = new SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"DigitalWatermark.mp4";
        SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobOperation operation = new SubmitDigitalWatermarkJob.SubmitDigitalWatermarkJobOperation();
        operation.output = output;
        DigitalWatermark digitalWatermark = new DigitalWatermark();
        digitalWatermark.message = "DigitalWatermarkTest";
        operation.digitalWatermark = digitalWatermark;
        operation.jobLevel = "0";
        SubmitDigitalWatermarkJob.operation = operation;
        SubmitDigitalWatermarkJob.callBackFormat = "XML";
        SubmitDigitalWatermarkJob.callBackType = "Url";
//        SubmitDigitalWatermarkJob.callBack = "http://callback.demo.com";
        request.setSubmitDigitalWatermarkJob(SubmitDigitalWatermarkJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitDigitalWatermarkJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitDigitalWatermarkJobResult SubmitDigitalWatermarkJobResult = (SubmitDigitalWatermarkJobResult)result;
                Assert.assertNotNull(SubmitDigitalWatermarkJobResult.response);
                TestUtils.printXML(SubmitDigitalWatermarkJobResult.response);
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
    public void submitExtractDigitalWatermarkJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitExtractDigitalWatermarkJobRequest request = new SubmitExtractDigitalWatermarkJobRequest(TestConst.ASR_BUCKET);
        SubmitExtractDigitalWatermarkJob submitExtractDigitalWatermarkJob = new SubmitExtractDigitalWatermarkJob();
        SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobInput input = new SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitExtractDigitalWatermarkJob.input = input;
        SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobOperation operation = new SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobOperation();
        SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobExtractDigitalWatermark extractDigitalWatermark = new SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobExtractDigitalWatermark();
        operation.extractDigitalWatermark = extractDigitalWatermark;
        operation.jobLevel = "0";
        submitExtractDigitalWatermarkJob.operation = operation;
        submitExtractDigitalWatermarkJob.callBackFormat = "XML";
        submitExtractDigitalWatermarkJob.callBackType = "Url";
//        submitExtractDigitalWatermarkJob.callBack = "http://callback.demo.com";
        request.setSubmitExtractDigitalWatermarkJob(submitExtractDigitalWatermarkJob);

        try {
            SubmitExtractDigitalWatermarkJobResult result = ciService.submitExtractDigitalWatermarkJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitExtractDigitalWatermarkJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitExtractDigitalWatermarkJobRequest request = new SubmitExtractDigitalWatermarkJobRequest(TestConst.ASR_BUCKET);
        SubmitExtractDigitalWatermarkJob submitExtractDigitalWatermarkJob = new SubmitExtractDigitalWatermarkJob();
        SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobInput input = new SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitExtractDigitalWatermarkJob.input = input;
        SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobOperation operation = new SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobOperation();
        SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobExtractDigitalWatermark extractDigitalWatermark = new SubmitExtractDigitalWatermarkJob.SubmitExtractDigitalWatermarkJobExtractDigitalWatermark();
        operation.extractDigitalWatermark = extractDigitalWatermark;
        operation.jobLevel = "0";
        submitExtractDigitalWatermarkJob.operation = operation;
        submitExtractDigitalWatermarkJob.callBackFormat = "XML";
        submitExtractDigitalWatermarkJob.callBackType = "Url";
//        submitExtractDigitalWatermarkJob.callBack = "http://callback.demo.com";
        request.setSubmitExtractDigitalWatermarkJob(submitExtractDigitalWatermarkJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitExtractDigitalWatermarkJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitExtractDigitalWatermarkJobResult SubmitExtractDigitalWatermarkJobResult = (SubmitExtractDigitalWatermarkJobResult)result;
                Assert.assertNotNull(SubmitExtractDigitalWatermarkJobResult.response);
                TestUtils.printXML(SubmitExtractDigitalWatermarkJobResult.response);
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
    public void submitVideoTagJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitVideoTagJobRequest request = new SubmitVideoTagJobRequest(TestConst.ASR_BUCKET);
        SubmitVideoTagJob submitVideoTagJob = new SubmitVideoTagJob();
        SubmitVideoTagJob.SubmitVideoTagJobInput input = new SubmitVideoTagJob.SubmitVideoTagJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitVideoTagJob.input = input;
        SubmitVideoTagJob.SubmitVideoTagJobOperation operation = new SubmitVideoTagJob.SubmitVideoTagJobOperation();
        operation.videoTag = new OperationVideoTag();
        operation.jobLevel = "0";
        operation.userData = "aaaaaa";
        submitVideoTagJob.operation = operation;
        submitVideoTagJob.callBackFormat = "XML";
        submitVideoTagJob.callBackType = "Url";
//        submitVideoTagJob.callBack = "http://callback.demo.com";
        request.setSubmitVideoTagJob(submitVideoTagJob);

        try {
            SubmitVideoTagJobResult result = ciService.submitVideoTagJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitVideoTagJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitVideoTagJobRequest request = new SubmitVideoTagJobRequest(TestConst.ASR_BUCKET);
        SubmitVideoTagJob submitVideoTagJob = new SubmitVideoTagJob();
        SubmitVideoTagJob.SubmitVideoTagJobInput input = new SubmitVideoTagJob.SubmitVideoTagJobInput();
        input.object = TestConst.AUDIT_BUCKET_VIDEO;
        submitVideoTagJob.input = input;
        SubmitVideoTagJob.SubmitVideoTagJobOperation operation = new SubmitVideoTagJob.SubmitVideoTagJobOperation();
        operation.videoTag = new OperationVideoTag();
        submitVideoTagJob.operation = operation;
        submitVideoTagJob.callBackFormat = "XML";
        submitVideoTagJob.callBackType = "Url";
//        submitVideoTagJob.callBack = "http://callback.demo.com";
        request.setSubmitVideoTagJob(submitVideoTagJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitVideoTagJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitVideoTagJobResult SubmitVideoTagJobResult = (SubmitVideoTagJobResult)result;
                Assert.assertNotNull(SubmitVideoTagJobResult.response);
                TestUtils.printXML(SubmitVideoTagJobResult.response);
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
    public void submitPicProcessJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        SubmitPicProcessJobRequest request = new SubmitPicProcessJobRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        SubmitPicProcessJob submitPicProcessJob = new SubmitPicProcessJob();
        SubmitPicProcessJob.SubmitPicProcessJobInput input = new SubmitPicProcessJob.SubmitPicProcessJobInput();
        input.object = TestConst.MEDIA_BUCKET_IMAGE;
        submitPicProcessJob.input = input;
        SubmitPicProcessJob.SubmitPicProcessJobOperation operation = new SubmitPicProcessJob.SubmitPicProcessJobOperation();
        SubmitPicProcessJob.SubmitPicProcessJobOutput output = new SubmitPicProcessJob.SubmitPicProcessJobOutput();
        output.region = TestConst.WORDS_GENERALIZE_BUCKET_REGION;
        output.bucket = TestConst.WORDS_GENERALIZE_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"PicProcess.jpg";
        operation.output = output;
        PicProcess picProcess = new PicProcess();
        picProcess.isPicInfo = true;
        picProcess.processRule = "imageMogr2/rotate/90";
        operation.picProcess = picProcess;
        operation.jobLevel = "0";
        submitPicProcessJob.operation = operation;
        submitPicProcessJob.callBackFormat = "XML";
        submitPicProcessJob.callBackType = "Url";
//        submitPicProcessJob.callBack = "http://callback.demo.com";
        request.setSubmitPicProcessJob(submitPicProcessJob);

        try {
            SubmitPicProcessJobResult result = ciService.submitPicProcessJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitPicProcessJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newWordsGeneralizeCIService();
        SubmitPicProcessJobRequest request = new SubmitPicProcessJobRequest(TestConst.WORDS_GENERALIZE_BUCKET);
        SubmitPicProcessJob submitPicProcessJob = new SubmitPicProcessJob();
        SubmitPicProcessJob.SubmitPicProcessJobInput input = new SubmitPicProcessJob.SubmitPicProcessJobInput();
        input.object = TestConst.MEDIA_BUCKET_IMAGE;
        submitPicProcessJob.input = input;
        SubmitPicProcessJob.SubmitPicProcessJobOperation operation = new SubmitPicProcessJob.SubmitPicProcessJobOperation();
        SubmitPicProcessJob.SubmitPicProcessJobOutput output = new SubmitPicProcessJob.SubmitPicProcessJobOutput();
        output.region = TestConst.WORDS_GENERALIZE_BUCKET_REGION;
        output.bucket = TestConst.WORDS_GENERALIZE_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"PicProcess.jpg";
        operation.output = output;
        PicProcess picProcess = new PicProcess();
        picProcess.isPicInfo = true;
        picProcess.processRule = "imageMogr2/rotate/90";
        operation.picProcess = picProcess;
        operation.jobLevel = "0";
        submitPicProcessJob.operation = operation;
        submitPicProcessJob.callBackFormat = "XML";
        submitPicProcessJob.callBackType = "Url";
//        submitPicProcessJob.callBack = "http://callback.demo.com";
        request.setSubmitPicProcessJob(submitPicProcessJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitPicProcessJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitPicProcessJobResult SubmitPicProcessJobResult = (SubmitPicProcessJobResult)result;
                Assert.assertNotNull(SubmitPicProcessJobResult.response);
                TestUtils.printXML(SubmitPicProcessJobResult.response);
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
    public void submitMediaSegmentJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitMediaSegmentJobRequest request = new SubmitMediaSegmentJobRequest(TestConst.ASR_BUCKET);
        SubmitMediaSegmentJob submitMediaSegmentJob = new SubmitMediaSegmentJob();
        SubmitMediaSegmentJob.SubmitMediaSegmentJobInput input = new SubmitMediaSegmentJob.SubmitMediaSegmentJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitMediaSegmentJob.input = input;
        SubmitMediaSegmentJob.SubmitMediaSegmentJobOperation operation = new SubmitMediaSegmentJob.SubmitMediaSegmentJobOperation();
        SubmitMediaSegmentJob.SubmitMediaSegmentJobOutput output = new SubmitMediaSegmentJob.SubmitMediaSegmentJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"MediaSegment-${number}";
        operation.output = output;
        SubmitMediaSegmentJob.SubmitMediaSegmentJobSegment segment = new SubmitMediaSegmentJob.SubmitMediaSegmentJobSegment();
        segment.format = "mp4";
        segment.duration = "5";
        SubmitMediaSegmentJob.SubmitMediaSegmentJobHlsEncrypt hlsEncrypt = new SubmitMediaSegmentJob.SubmitMediaSegmentJobHlsEncrypt();
        hlsEncrypt.isHlsEncrypt = "true";
        hlsEncrypt.uriKey = "test-key";
        segment.hlsEncrypt = hlsEncrypt;
        operation.segment = segment;
        operation.jobLevel = "0";
        submitMediaSegmentJob.operation = operation;
        submitMediaSegmentJob.callBackFormat = "XML";
        submitMediaSegmentJob.callBackType = "Url";
//        submitMediaSegmentJob.callBack = "http://callback.demo.com";
        request.setSubmitMediaSegmentJob(submitMediaSegmentJob);

        try {
            SubmitMediaSegmentJobResult result = ciService.submitMediaSegmentJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitMediaSegmentJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitMediaSegmentJobRequest request = new SubmitMediaSegmentJobRequest(TestConst.ASR_BUCKET);
        SubmitMediaSegmentJob submitMediaSegmentJob = new SubmitMediaSegmentJob();
        SubmitMediaSegmentJob.SubmitMediaSegmentJobInput input = new SubmitMediaSegmentJob.SubmitMediaSegmentJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitMediaSegmentJob.input = input;
        SubmitMediaSegmentJob.SubmitMediaSegmentJobOperation operation = new SubmitMediaSegmentJob.SubmitMediaSegmentJobOperation();
        SubmitMediaSegmentJob.SubmitMediaSegmentJobOutput output = new SubmitMediaSegmentJob.SubmitMediaSegmentJobOutput();
        output.region = TestConst.PERSIST_BUCKET_REGION;
        output.bucket = TestConst.ASR_BUCKET;
        output.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"MediaSegment-${number}";
        operation.output = output;
        SubmitMediaSegmentJob.SubmitMediaSegmentJobSegment segment = new SubmitMediaSegmentJob.SubmitMediaSegmentJobSegment();
        segment.format = "mp4";
        segment.duration = "5";
        SubmitMediaSegmentJob.SubmitMediaSegmentJobHlsEncrypt hlsEncrypt = new SubmitMediaSegmentJob.SubmitMediaSegmentJobHlsEncrypt();
        hlsEncrypt.isHlsEncrypt = "true";
        hlsEncrypt.uriKey = "test-key";
        segment.hlsEncrypt = hlsEncrypt;
        operation.segment = segment;
        operation.jobLevel = "0";
        submitMediaSegmentJob.operation = operation;
        submitMediaSegmentJob.callBackFormat = "XML";
        submitMediaSegmentJob.callBackType = "Url";
//        submitMediaSegmentJob.callBack = "http://callback.demo.com";
        request.setSubmitMediaSegmentJob(submitMediaSegmentJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitMediaSegmentJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitMediaSegmentJobResult SubmitMediaSegmentJobResult = (SubmitMediaSegmentJobResult)result;
                Assert.assertNotNull(SubmitMediaSegmentJobResult.response);
                TestUtils.printXML(SubmitMediaSegmentJobResult.response);
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
    public void submitMediaInfoJob() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitMediaInfoJobRequest request = new SubmitMediaInfoJobRequest(TestConst.ASR_BUCKET);
        SubmitMediaInfoJob submitMediaInfoJob = new SubmitMediaInfoJob();
        SubmitMediaInfoJob.SubmitMediaInfoJobInput input = new SubmitMediaInfoJob.SubmitMediaInfoJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitMediaInfoJob.input = input;
        SubmitMediaInfoJob.SubmitMediaInfoJobOperation operation = new SubmitMediaInfoJob.SubmitMediaInfoJobOperation();
        operation.jobLevel = "0";
        submitMediaInfoJob.operation = operation;
        submitMediaInfoJob.callBackFormat = "XML";
        submitMediaInfoJob.callBackType = "Url";
//        submitMediaInfoJob.callBack = "http://callback.demo.com";
        request.setSubmitMediaInfoJob(submitMediaInfoJob);

        try {
            SubmitMediaInfoJobResult result = ciService.submitMediaInfoJob(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void submitMediaInfoJobAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        SubmitMediaInfoJobRequest request = new SubmitMediaInfoJobRequest(TestConst.ASR_BUCKET);
        SubmitMediaInfoJob submitMediaInfoJob = new SubmitMediaInfoJob();
        SubmitMediaInfoJob.SubmitMediaInfoJobInput input = new SubmitMediaInfoJob.SubmitMediaInfoJobInput();
        input.object = TestConst.MEDIA_BUCKET_VIDEO;
        submitMediaInfoJob.input = input;
        SubmitMediaInfoJob.SubmitMediaInfoJobOperation operation = new SubmitMediaInfoJob.SubmitMediaInfoJobOperation();
        operation.jobLevel = "0";
        submitMediaInfoJob.operation = operation;
        submitMediaInfoJob.callBackFormat = "XML";
        submitMediaInfoJob.callBackType = "Url";
//        submitMediaInfoJob.callBack = "http://callback.demo.com";
        request.setSubmitMediaInfoJob(submitMediaInfoJob);
        final TestLocker testLocker = new TestLocker();
        ciService.submitMediaInfoJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                SubmitMediaInfoJobResult SubmitMediaInfoJobResult = (SubmitMediaInfoJobResult)result;
                Assert.assertNotNull(SubmitMediaInfoJobResult.response);
                TestUtils.printXML(SubmitMediaInfoJobResult.response);
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
    public void stage3_getWorkflowList() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetWorkflowListRequest request = new GetWorkflowListRequest(TestConst.ASR_BUCKET);
        try {
            GetWorkflowListResult result = ciService.getWorkflowList(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
            if(result.response.mediaWorkflowList!=null && result.response.mediaWorkflowList.size()>0) {
                workflowId = result.response.mediaWorkflowList.get(0).workflowId;
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage3_getWorkflowListAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetWorkflowListRequest request = new GetWorkflowListRequest(TestConst.ASR_BUCKET);
        final TestLocker testLocker = new TestLocker();
        ciService.getWorkflowListAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                GetWorkflowListResult GetWorkflowListResult = (GetWorkflowListResult)result;
                Assert.assertNotNull(GetWorkflowListResult.response);
                TestUtils.printXML(GetWorkflowListResult.response);
                if(GetWorkflowListResult.response.mediaWorkflowList!=null && GetWorkflowListResult.response.mediaWorkflowList.size()>0) {
                    workflowId = GetWorkflowListResult.response.mediaWorkflowList.get(0).workflowId;
                }
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

    String workflowId;
    @Test
    public void stage4_triggerWorkflow() {
        if(workflowId == null) return;

        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        TriggerWorkflowRequest request = new TriggerWorkflowRequest(
                TestConst.ASR_BUCKET,
                workflowId,
                "aaa/测试.mp4"
        );
        request.setName("名字");
        try {
            TriggerWorkflowResult result = ciService.triggerWorkflow(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage4_triggerWorkflowAsync() {
        if(workflowId == null) return;

        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        TriggerWorkflowRequest request = new TriggerWorkflowRequest(
                TestConst.ASR_BUCKET,
                workflowId,
                "aaa/测试.mp4"
        );
        request.setName("名字");
        final TestLocker testLocker = new TestLocker();
        ciService.triggerWorkflowAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TriggerWorkflowResult TriggerWorkflowResult = (TriggerWorkflowResult)result;
                Assert.assertNotNull(TriggerWorkflowResult.response);
                TestUtils.printXML(TriggerWorkflowResult.response);
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

//    @Test
//    public void stage5_GetWorkflowDetail() {
//        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
//        GetWorkflowDetailRequest request = new GetWorkflowDetailRequest(TestConst.ASR_BUCKET, workflowId);
//        try {
//            GetWorkflowDetailResult result = ciService.getWorkflowDetail(request);
//            Assert.assertNotNull(result.response);
//            TestUtils.printXML(result.response);
//        } catch (CosXmlClientException e) {
//            Assert.fail(TestUtils.getCosExceptionMessage(e));
//        } catch (CosXmlServiceException e) {
//            Assert.fail(TestUtils.getCosExceptionMessage(e));
//        }
//    }
//
//    @Test
//    public void stage5_GetWorkflowDetailAsync() {
//        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
//        GetWorkflowDetailRequest request = new GetWorkflowDetailRequest(TestConst.ASR_BUCKET, workflowId);
//        final TestLocker testLocker = new TestLocker();
//        ciService.getWorkflowDetailAsync(request, new CosXmlResultListener() {
//            @Override
//            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
//                GetWorkflowDetailResult GetWorkflowDetailResult = (GetWorkflowDetailResult)result;
//                Assert.assertNotNull(GetWorkflowDetailResult.response);
//                TestUtils.printXML(GetWorkflowDetailResult.response);
//                testLocker.release();
//            }
//
//            @Override
//            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
//                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
//                testLocker.release();
//            }
//        });
//        testLocker.lock();
//    }
}
