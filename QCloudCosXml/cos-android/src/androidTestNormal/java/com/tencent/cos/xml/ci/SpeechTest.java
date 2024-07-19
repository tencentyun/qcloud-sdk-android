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
import com.tencent.cos.xml.model.ci.BaseDescribeQueuesResponse;
import com.tencent.cos.xml.model.ci.ai.CloseAsrBucketRequest;
import com.tencent.cos.xml.model.ci.ai.CloseAsrBucketResult;
import com.tencent.cos.xml.model.ci.ai.OpenAsrBucketRequest;
import com.tencent.cos.xml.model.ci.ai.OpenAsrBucketResult;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReduction;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReductionRequest;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReductionResult;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReductionTemplete;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReductionTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReductionTempleteResult;
import com.tencent.cos.xml.model.ci.ai.PostSoundHound;
import com.tencent.cos.xml.model.ci.ai.PostSoundHoundRequest;
import com.tencent.cos.xml.model.ci.ai.PostSoundHoundResult;
import com.tencent.cos.xml.model.ci.ai.PostSpeechRecognitionTemplete;
import com.tencent.cos.xml.model.ci.ai.PostSpeechRecognitionTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.PostSpeechRecognitionTempleteResult;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesis;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesisRequest;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesisResult;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesisTemplete;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesisTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesisTempleteResult;
import com.tencent.cos.xml.model.ci.ai.UpdateAsrQueue;
import com.tencent.cos.xml.model.ci.ai.UpdateAsrQueueRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateAsrQueueResult;
import com.tencent.cos.xml.model.ci.ai.UpdateNoiseReductionTemplete;
import com.tencent.cos.xml.model.ci.ai.UpdateNoiseReductionTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateNoiseReductionTempleteResult;
import com.tencent.cos.xml.model.ci.ai.UpdateSpeechRecognitionTemplete;
import com.tencent.cos.xml.model.ci.ai.UpdateSpeechRecognitionTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateSpeechRecognitionTempleteResult;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSeparateTemplete;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSeparateTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSeparateTempleteResult;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSynthesisTemplete;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSynthesisTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSynthesisTempleteResult;
import com.tencent.cos.xml.model.ci.ai.VocalScore;
import com.tencent.cos.xml.model.ci.ai.VocalScoreRequest;
import com.tencent.cos.xml.model.ci.ai.VocalScoreResult;
import com.tencent.cos.xml.model.ci.asr.CreateSpeechJobsRequest;
import com.tencent.cos.xml.model.ci.asr.CreateSpeechJobsResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechBucketsRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechBucketsResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobsRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobsResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechQueuesRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechQueuesResult;
import com.tencent.cos.xml.model.ci.common.AudioConfig;
import com.tencent.cos.xml.model.ci.common.NoiseReduction;
import com.tencent.cos.xml.model.ci.common.NotifyConfig;
import com.tencent.cos.xml.model.ci.common.SpeechRecognition;
import com.tencent.cos.xml.model.ci.media.SubmitVoiceSeparateJob;
import com.tencent.cos.xml.model.ci.media.SubmitVoiceSeparateJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitVoiceSeparateJobResult;
import com.tencent.cos.xml.model.ci.media.TemplateVoiceSeparate;
import com.tencent.cos.xml.model.ci.media.TemplateVoiceSeparateRequest;
import com.tencent.cos.xml.model.ci.media.TemplateVoiceSeparateResult;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * Created by jordanqin on 2023/2/21 14:37.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpeechTest {
    private static String queueId;
    private static String queueName;
    private static String templateId;
    private static String templateName;
    private static String jobId;
    private static String flashJobId;
    private static String voiceSynthesisTemplateId;
    private static String noiseReductionTempleteId;
    private static String voiceSeparateTempleteId;

    @Test
    public void a0() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        OpenAsrBucketRequest request = new OpenAsrBucketRequest(TestConst.ASR_BUCKET);

        try {
            OpenAsrBucketResult result = ciService.openAsrBucket(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage1_describeSpeechBuckets() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechBucketsRequest request = new DescribeSpeechBucketsRequest();
        request.setPageNumber(1);
        request.setPageSize(10);
        request.setRegions("All");
        try {
            DescribeSpeechBucketsResult result = ciService.describeSpeechBuckets(request);
            Assert.assertNotNull(result.describeSpeechBucketsResponse);
            TestUtils.printXML(result.describeSpeechBucketsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage1_describeSpeechBuckets1() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechBucketsRequest request = new DescribeSpeechBucketsRequest();
        request.setBucketNames(TestConst.PERSIST_BUCKET);
        try {
            DescribeSpeechBucketsResult result = ciService.describeSpeechBuckets(request);
            Assert.assertNotNull(result.describeSpeechBucketsResponse);
            TestUtils.printXML(result.describeSpeechBucketsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage1_describeSpeechBuckets2() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechBucketsRequest request = new DescribeSpeechBucketsRequest();
        request.setBucketName("mobile-");
        try {
            DescribeSpeechBucketsResult result = ciService.describeSpeechBuckets(request);
            Assert.assertNotNull(result.describeSpeechBucketsResponse);
            TestUtils.printXML(result.describeSpeechBucketsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage1_describeSpeechBucketsAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIAuditServiceBySessionCredentials();
        final TestLocker locker = new TestLocker();
        DescribeSpeechBucketsRequest request = new DescribeSpeechBucketsRequest();
        request.setPageNumber(1);
        request.setPageSize(10);
        request.setRegions("All");
        ciService.describeSpeechBucketsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeSpeechBucketsResult result = (DescribeSpeechBucketsResult) cosResult;
                Assert.assertNotNull(result.describeSpeechBucketsResponse);
                TestUtils.printXML(result.describeSpeechBucketsResponse);
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
    public void stage2_closeAsrBucket() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        CloseAsrBucketRequest request = new CloseAsrBucketRequest(TestConst.ASR_BUCKET);

        try {
            CloseAsrBucketResult result = ciService.closeAsrBucket(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            if(e.getErrorMessage().contains("Asr processing service is not activated")){
                Assert.assertTrue(true);
            } else {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        }
    }

    @Test
    public void stage2_closeAsrBucketAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        CloseAsrBucketRequest request = new CloseAsrBucketRequest(TestConst.ASR_BUCKET);

        final TestLocker testLocker = new TestLocker();
        ciService.closeAsrBucketAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                CloseAsrBucketResult result = (CloseAsrBucketResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(serviceException != null && serviceException.getErrorMessage().contains("Asr processing service is not activated")){
                    Assert.assertTrue(true);
                } else {
                    Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                }
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    @Test
    public void stage3_openAsrBucket() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        OpenAsrBucketRequest request = new OpenAsrBucketRequest(TestConst.ASR_BUCKET);

        try {
            OpenAsrBucketResult result = ciService.openAsrBucket(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage3_openAsrBucketAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        OpenAsrBucketRequest request = new OpenAsrBucketRequest(TestConst.ASR_BUCKET);

        final TestLocker testLocker = new TestLocker();
        ciService.openAsrBucketAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                OpenAsrBucketResult result = (OpenAsrBucketResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

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
    public void stage4_describeSpeechQueues() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechQueuesRequest request = new DescribeSpeechQueuesRequest(TestConst.ASR_BUCKET);
        request.setPageNumber(1);
        request.setPageSize(10);
        request.setState("Active");
        try {
            DescribeSpeechQueuesResult result = ciService.describeSpeechQueues(request);
            Assert.assertNotNull(result.describeSpeechQueuesResponse);
            for (BaseDescribeQueuesResponse.Queue queue : result.describeSpeechQueuesResponse.queueList){
                if(queue.state.equals("Active")){
                    SpeechTest.queueId = queue.queueId;
                    SpeechTest.queueName = queue.name;
                }
            }
            TestUtils.printXML(result.describeSpeechQueuesResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage4_describeSpeechQueuesAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        DescribeSpeechQueuesRequest request = new DescribeSpeechQueuesRequest(TestConst.ASR_BUCKET, TestConst.PERSIST_BUCKET_REGION);
        request.setQueueIds(SpeechTest.queueId+",ashjdaosdhjiasodj12312"+",ashjdaosdasdashjiasodj12312");
        ciService.describeSpeechQueuesAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeSpeechQueuesResult result = (DescribeSpeechQueuesResult) cosResult;
                Assert.assertNotNull(result.describeSpeechQueuesResponse);
                TestUtils.printXML(result.describeSpeechQueuesResponse);
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
    public void stage5_updateAsrQueue() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateAsrQueueRequest request = new UpdateAsrQueueRequest(TestConst.ASR_BUCKET, SpeechTest.queueId);
        UpdateAsrQueue updateAsrQueue = new UpdateAsrQueue();// 更新智能语音队列请求体
        request.setUpdateAsrQueue(updateAsrQueue);// 设置请求
        // 设置队列名称，仅支持中文、英文、数字、_、-和*，长度不超过128;是否必传：是
        updateAsrQueue.name = SpeechTest.queueName;
        // 设置Active 表示队列内的作业会被调度执行Paused 表示队列暂停，作业不再会被调度执行，队列内的所有作业状态维持在暂停状态，已经执行中的任务不受影响;是否必传：是
        updateAsrQueue.state = "Active";
        NotifyConfig notifyConfig = new NotifyConfig();
        updateAsrQueue.notifyConfig = notifyConfig;
        // 设置回调开关OffOn;是否必传：否
        notifyConfig.state = "On";
        // 设置回调事件TaskFinish：任务完成WorkflowFinish：工作流完成;是否必传：否
        notifyConfig.event = "TaskFinish";
        // 设置回调格式XML JSON;是否必传：否
        notifyConfig.resultFormat = "JSON";
        // 设置回调类型Url TDMQ;是否必传：否
        notifyConfig.type = "Url";
        // 设置回调地址，不能为内网地址。;是否必传：否
        notifyConfig.url = "https://github.com/tencentyun/qcloud-sdk-android";

        try {
            UpdateAsrQueueResult result = ciService.updateAsrQueue(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage5_updateAsrQueueAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateAsrQueueRequest request = new UpdateAsrQueueRequest(TestConst.ASR_BUCKET, SpeechTest.queueId);
        UpdateAsrQueue updateAsrQueue = new UpdateAsrQueue();// 更新智能语音队列请求体
        request.setUpdateAsrQueue(updateAsrQueue);// 设置请求
        // 设置队列名称，仅支持中文、英文、数字、_、-和*，长度不超过128;是否必传：是
        updateAsrQueue.name = SpeechTest.queueName;
        // 设置Active 表示队列内的作业会被调度执行Paused 表示队列暂停，作业不再会被调度执行，队列内的所有作业状态维持在暂停状态，已经执行中的任务不受影响;是否必传：是
        updateAsrQueue.state = "Active";NotifyConfig notifyConfig = new NotifyConfig();
        updateAsrQueue.notifyConfig = notifyConfig;
        // 设置回调开关OffOn;是否必传：否
        notifyConfig.state = "On";
        // 设置回调事件TaskFinish：任务完成WorkflowFinish：工作流完成;是否必传：否
        notifyConfig.event = "TaskFinish";
        // 设置回调格式XML JSON;是否必传：否
        notifyConfig.resultFormat = "JSON";
        // 设置回调类型Url TDMQ;是否必传：否
        notifyConfig.type = "Url";
        // 设置回调地址，不能为内网地址。;是否必传：否
        notifyConfig.url = "https://github.com/tencentyun/qcloud-sdk-android";

        final TestLocker testLocker = new TestLocker();
        ciService.updateAsrQueueAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                UpdateAsrQueueResult result = (UpdateAsrQueueResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

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
    public void stage6_postSpeechRecognitionTemplete() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostSpeechRecognitionTempleteRequest request = new PostSpeechRecognitionTempleteRequest(TestConst.ASR_BUCKET);
        PostSpeechRecognitionTemplete postSpeechRecognitionTemplete = new PostSpeechRecognitionTemplete();// 创建模板请求体
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        postSpeechRecognitionTemplete.name = "testqjd369"+System.currentTimeMillis();
        SpeechRecognition speechRecognition = new SpeechRecognition();
        speechRecognition.flashAsr = "false";
        speechRecognition.engineModelType = "8k_zh";
        speechRecognition.channelNum = "1";
        postSpeechRecognitionTemplete.speechRecognition = speechRecognition;
        request.setPostSpeechRecognitionTemplete(postSpeechRecognitionTemplete);// 设置请求

        try {
            PostSpeechRecognitionTempleteResult result = ciService.postSpeechRecognitionTemplete(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
            SpeechTest.templateId = result.response.template.templateId;
            SpeechTest.templateName = result.response.template.name;
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage6_postSpeechRecognitionTempleteAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostSpeechRecognitionTempleteRequest request = new PostSpeechRecognitionTempleteRequest(TestConst.ASR_BUCKET);
        PostSpeechRecognitionTemplete postSpeechRecognitionTemplete = new PostSpeechRecognitionTemplete();// 创建模板请求体
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        postSpeechRecognitionTemplete.name = "testqjd369"+System.currentTimeMillis();
        SpeechRecognition speechRecognition = new SpeechRecognition();
        speechRecognition.flashAsr = "false";
        speechRecognition.engineModelType = "8k_zh";
        speechRecognition.channelNum = "1";
        postSpeechRecognitionTemplete.speechRecognition = speechRecognition;
        request.setPostSpeechRecognitionTemplete(postSpeechRecognitionTemplete);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.postSpeechRecognitionTempleteAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                PostSpeechRecognitionTempleteResult result = (PostSpeechRecognitionTempleteResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);
                SpeechTest.templateId = result.response.template.templateId;
                SpeechTest.templateName = result.response.template.name;
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
    public void stage7_updateSpeechRecognitionTemplete() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateSpeechRecognitionTempleteRequest request = new UpdateSpeechRecognitionTempleteRequest(TestConst.ASR_BUCKET, SpeechTest.templateId);
        UpdateSpeechRecognitionTemplete updateSpeechRecognitionTemplete = new UpdateSpeechRecognitionTemplete();// 更新模板请求体
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        updateSpeechRecognitionTemplete.name = SpeechTest.templateName;
        SpeechRecognition speechRecognition = new SpeechRecognition();
        updateSpeechRecognitionTemplete.speechRecognition = speechRecognition;
// 设置引擎模型类型，分为电话场景和非电话场景。电话场景：8k_zh：电话 8k 中文普通话通用（可用于双声道音频）8k_zh_s：电话 8k 中文普通话话者分离（仅适用于单声道音频）8k_en：电话 8k 英语 非电话场景： 6k_zh：16k 中文普通话通用16k_zh_video：16k 音视频领域16k_en：16k 英语16k_ca：16k 粤语16k_ja：16k 日语16k_zh_edu：中文教育16k_en_edu：英文教育16k_zh_medical：医疗16k_th：泰语16k_zh_dialect：多方言，支持23种方言极速 ASR 支持8k_zh、16k_zh、16k_en、16k_zh_video、16k_zh_dialect、16k_ms（马来语）、16k_zh-PY（中英粤）;是否必传：是
        speechRecognition.engineModelType = "16k_zh";
// 设置语音声道数：1 表示单声道。EngineModelType为非电话场景仅支持单声道2 表示双声道（仅支持 8k_zh 引擎模型 双声道应分别对应通话双方）仅���持非极速ASR，为非极速ASR时，该参数必填;是否必传：否
        speechRecognition.channelNum = "1";
// 设置识别结果返回形式：0：识别结果文本（含分段时间戳）1：词级别粒度的详细识别结果，不含标点，含语速值（词时间戳列表，一般用于生成字幕场景）2：词级别粒度的详细识别结果（包含标点、语速值）3：标点符号分段，包含每段时间戳，特别适用于字幕场景（包含词级时间、标点、语速值）仅支持非极速ASR;是否必传：否
        speechRecognition.resTextFormat = "1";
// 设置是否过滤脏词（目前支持中文普通话引擎）0：不过滤脏词1：过滤脏词2：将脏词替换为 *;是否必传：否
        speechRecognition.filterDirty = "0";
// 设置是否过滤语气词（目前支持中文普通话引擎）：0 表示不过滤语气词1 表示部分过滤2 表示严格过滤 ;是否必传：否
        speechRecognition.filterModal = "1";
// 设置是否进行阿拉伯数字智能转换（目前支持中文普通话引擎）0：不转换，直接输出中文数字1：根据场景智能转换为阿拉伯数字3 ：打开数学相关数字转换仅支持非极速ASR;是否必传：否
        speechRecognition.convertNumMode = "0";
// 设置是否开启说话人分离0：不开启1：开启(仅支持8k_zh，16k_zh，16k_zh_video，单声道音频)8k电话场景建议使用双声道来区分通话双方，设置ChannelNum=2即可，不用开启说话人分离。;是否必传：否
        speechRecognition.speakerDiarization = "1";
// 设置说话人分离人数（需配合开启说话人分离使用），取值范围：[0, 10]0 代表自动分离（目前仅支持≤6个人）1-10代表指定说话人数分离仅支持非极速ASR;是否必传：否
        speechRecognition.speakerNumber = "0";
// 设置是否过滤标点符号（目前支持中文普通话引擎）0：不过滤。1：过滤句末标点2：过滤所有标点;是否必传：否
        speechRecognition.filterPunc = "0";
// 设置输出文件类型，可选txt、srt极速ASR仅支持txt非极速Asr且ResTextFormat为3时仅支持txt;是否必传：否
        speechRecognition.outputFileType = "txt";
        request.setUpdateSpeechRecognitionTemplete(updateSpeechRecognitionTemplete);// 设置请求

        try {
            UpdateSpeechRecognitionTempleteResult result = ciService.updateSpeechRecognitionTemplete(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage7_updateSpeechRecognitionTempleteAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateSpeechRecognitionTempleteRequest request = new UpdateSpeechRecognitionTempleteRequest(TestConst.ASR_BUCKET, SpeechTest.templateId);
        UpdateSpeechRecognitionTemplete updateSpeechRecognitionTemplete = new UpdateSpeechRecognitionTemplete();// 更新模板请求体
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        updateSpeechRecognitionTemplete.name = SpeechTest.templateName;
        SpeechRecognition speechRecognition = new SpeechRecognition();
        updateSpeechRecognitionTemplete.speechRecognition = speechRecognition;
// 设置引擎模型类型，分为电话场景和非电话场景。电话场景：8k_zh：电话 8k 中文普通话通用（可用于双声道音频）8k_zh_s：电话 8k 中文普通话话者分离（仅适用于单声道音频）8k_en：电话 8k 英语 非电话场景： 6k_zh：16k 中文普通话通用16k_zh_video：16k 音视频领域16k_en：16k 英语16k_ca：16k 粤语16k_ja：16k 日语16k_zh_edu：中文教育16k_en_edu：英文教育16k_zh_medical：医疗16k_th：泰语16k_zh_dialect：多方言，支持23种方言极速 ASR 支持8k_zh、16k_zh、16k_en、16k_zh_video、16k_zh_dialect、16k_ms（马来语）、16k_zh-PY（中英粤）;是否必传：是
        speechRecognition.engineModelType = "16k_zh";
// 设置语音声道数：1 表示单声道。EngineModelType为非电话场景仅支持单声道2 表示双声道（仅支持 8k_zh 引擎模型 双声道应分别对应通话双方）仅���持非极速ASR，为非极速ASR时，该参数必填;是否必传：否
        speechRecognition.channelNum = "1";
// 设置识别结果返回形式：0：识别结果文本（含分段时间戳）1：词级别粒度的详细识别结果，不含标点，含语速值（词时间戳列表，一般用于生成字幕场景）2：词级别粒度的详细识别结果（包含标点、语速值）3：标点符号分段，包含每段时间戳，特别适用于字幕场景（包含词级时间、标点、语速值）仅支持非极速ASR;是否必传：否
        speechRecognition.resTextFormat = "1";
// 设置是否过滤脏词（目前支持中文普通话引擎）0：不过滤脏词1：过滤脏词2：将脏词替换为 *;是否必传：否
        speechRecognition.filterDirty = "0";
// 设置是否过滤语气词（目前支持中文普通话引擎）：0 表示不过滤语气词1 表示部分过滤2 表示严格过滤 ;是否必传：否
        speechRecognition.filterModal = "1";
// 设置是否进行阿拉伯数字智能转换（目前支持中文普通话引擎）0：不转换，直接输出中文数字1：根据场景智能转换为阿拉伯数字3 ：打开数学相关数字转换仅支持非极速ASR;是否必传：否
        speechRecognition.convertNumMode = "0";
// 设置是否开启说话人分离0：不开启1：开启(仅支持8k_zh，16k_zh，16k_zh_video，单声道音频)8k电话场景建议使用双声道来区分通话双方，设置ChannelNum=2即可，不用开启说话人分离。;是否必传：否
        speechRecognition.speakerDiarization = "1";
// 设置说话人分离人数（需配合开启说话人分离使用），取值范围：[0, 10]0 代表自动分离（目前仅支持≤6个人）1-10代表指定说话人数分离仅支持非极速ASR;是否必传：否
        speechRecognition.speakerNumber = "0";
// 设置是否过滤标点符号（目前支持中文普通话引擎）0：不过滤。1：过滤句末标点2：过滤所有标点;是否必传：否
        speechRecognition.filterPunc = "0";
// 设置输出文件类型，可选txt、srt极速ASR仅支持txt非极速Asr且ResTextFormat为3时仅支持txt;是否必传：否
        speechRecognition.outputFileType = "txt";
        request.setUpdateSpeechRecognitionTemplete(updateSpeechRecognitionTemplete);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.updateSpeechRecognitionTempleteAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                UpdateSpeechRecognitionTempleteResult result = (UpdateSpeechRecognitionTempleteResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

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
    public void stage8_createSpeechJobs() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        CreateSpeechJobsRequest request = new CreateSpeechJobsRequest(TestConst.ASR_BUCKET);
        request.setInputObject(TestConst.ASR_OBJECT_LONG);
        request.setQueueId(SpeechTest.queueId);
        request.setOutput(TestConst.ASR_BUCKET_REGION, TestConst.ASR_BUCKET, TestConst.ASR_OBJECT_OUTPUT);
        request.setTemplateId(SpeechTest.templateId);
        request.setEngineModelType("8k_zh");
        request.setChannelNum(1);
        request.setResTextFormat(1);
        request.setFilterDirty(0);
        request.setFilterModal(0);
        request.setConvertNumMode(1);
        request.setSpeakerDiarization(0);
        request.setSpeakerNumber(0);
        request.setFilterPunc(0);
        request.setOutputFileType("txt");
        request.setFirstChannelOnly(1);
        request.setWordInfo(0);
        request.setUserData("userdata");
        request.setJobLevel(0);
        request.setCallBack("no");
        request.setCallBackFormat("XML");
        request.setCallBackType("Url");
//        request.setCallBackMqConfig(new CallBackMqConfig());

        try {
            CreateSpeechJobsResult result = ciService.createSpeechJobs(request);
            Assert.assertNotNull(result.createSpeechJobsResponse);
            SpeechTest.jobId = result.createSpeechJobsResponse.jobsDetail.jobId;
            TestUtils.printXML(result.createSpeechJobsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage8_createSpeechJobsAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        CreateSpeechJobsRequest request = new CreateSpeechJobsRequest(TestConst.ASR_BUCKET);
        request.setInputUrl(TestConst.ASR_OBJECT_GS_2C_URL);
        request.setQueueId(SpeechTest.queueId);
        request.setOutput(TestConst.ASR_BUCKET_REGION, TestConst.ASR_BUCKET, TestConst.ASR_OBJECT_OUTPUT);
        request.setEngineModelType("8k_zh");
        request.setChannelNum(2);
        request.setResTextFormat(0);
        ciService.createSpeechJobsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                CreateSpeechJobsResult result = (CreateSpeechJobsResult) cosResult;
                Assert.assertNotNull(result.createSpeechJobsResponse);
                SpeechTest.jobId = result.createSpeechJobsResponse.jobsDetail.jobId;
                TestUtils.printXML(result.createSpeechJobsResponse);
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
    public void stage9_createFlashSpeechJobs() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        CreateSpeechJobsRequest request = new CreateSpeechJobsRequest(TestConst.ASR_BUCKET);
        request.setInputObject(TestConst.ASR_OBJECT_GS_2C);
        request.setQueueId(SpeechTest.queueId);
        request.setOutput(TestConst.ASR_BUCKET_REGION, TestConst.ASR_BUCKET, TestConst.ASR_OBJECT_OUTPUT);
        request.setEngineModelType("8k_zh");
        request.setChannelNum(1);
        request.setResTextFormat(1);
        request.setFlashAsr(true);
        request.setFormat("m4a");
        try {
            CreateSpeechJobsResult result = ciService.createSpeechJobs(request);
            Assert.assertNotNull(result.createSpeechJobsResponse);
            SpeechTest.flashJobId = result.createSpeechJobsResponse.jobsDetail.jobId;
            TestUtils.printXML(result.createSpeechJobsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void t_stage1_describeSpeechJob() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechJobRequest request = new DescribeSpeechJobRequest(TestConst.ASR_BUCKET, SpeechTest.jobId);
        try {
            DescribeSpeechJobResult result = ciService.describeSpeechJob(request);
            Assert.assertNotNull(result.describeSpeechJobResponse);
            TestUtils.printXML(result.describeSpeechJobResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void t_stage1_describeSpeechJobAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        DescribeSpeechJobRequest request = new DescribeSpeechJobRequest(TestConst.ASR_BUCKET, TestConst.PERSIST_BUCKET_REGION, SpeechTest.jobId);
        ciService.describeSpeechJobAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeSpeechJobResult result = (DescribeSpeechJobResult) cosResult;
                Assert.assertNotNull(result.describeSpeechJobResponse);
                TestUtils.printXML(result.describeSpeechJobResponse);
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
    public void t_stage2_describeFlashSpeechJob() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechJobRequest request = new DescribeSpeechJobRequest(TestConst.ASR_BUCKET, SpeechTest.flashJobId);
        try {
            DescribeSpeechJobResult result = ciService.describeSpeechJob(request);
            Assert.assertNotNull(result.describeSpeechJobResponse);
            TestUtils.printXML(result.describeSpeechJobResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void t_stage3_describeSpeechJobs() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechJobsRequest request = new DescribeSpeechJobsRequest(TestConst.ASR_BUCKET);
        request.setQueueId(SpeechTest.queueId);
        request.setOrderByTime("Desc");
        request.setNextToken(null);
        request.setSize(50);
        request.setStates("All");
        try {
            DescribeSpeechJobsResult result = ciService.describeSpeechJobs(request);
            Assert.assertNotNull(result.describeSpeechJobsResponse);
            TestUtils.printXML(result.describeSpeechJobsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage7_describeSpeechJobs1() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        DescribeSpeechJobsRequest request = new DescribeSpeechJobsRequest(TestConst.ASR_BUCKET);
        request.setQueueId(SpeechTest.queueId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String oneDayBefore = sdf.format(cal.getTime());
        request.setStartCreationTime(oneDayBefore);
        request.setEndCreationTime(sdf.format(new Date()));
        try {
            DescribeSpeechJobsResult result = ciService.describeSpeechJobs(request);
            Assert.assertNotNull(result.describeSpeechJobsResponse);
            TestUtils.printXML(result.describeSpeechJobsResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void t_stage3_describeSpeechJobsAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        DescribeSpeechJobsRequest request = new DescribeSpeechJobsRequest(TestConst.ASR_BUCKET, TestConst.PERSIST_BUCKET_REGION);
        request.setQueueId(SpeechTest.queueId);
        ciService.describeSpeechJobsAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                DescribeSpeechJobsResult result = (DescribeSpeechJobsResult) cosResult;
                Assert.assertNotNull(result.describeSpeechJobsResponse);
                TestUtils.printXML(result.describeSpeechJobsResponse);
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
    public void noiceSeparateTemplete1_post() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        TemplateVoiceSeparateRequest request = new TemplateVoiceSeparateRequest(TestConst.ASR_BUCKET);
        TemplateVoiceSeparate templateVoiceSeparate = new TemplateVoiceSeparate();// 模板请求体
        request.setTemplateVoiceSeparate(templateVoiceSeparate);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        templateVoiceSeparate.name = "VoiceSeparateTempleteTest"+System.currentTimeMillis();
        // 设置输出音频IsAudio：输出人声IsBackground：输出背景声AudioAndBackground：输出人声和背景声MusicMode：输出人声、背景声、Bass声、鼓声;是否必传：是
        templateVoiceSeparate.audioMode = "IsAudio";
        TemplateVoiceSeparate.AudioConfig audioConfig = new TemplateVoiceSeparate.AudioConfig();
        templateVoiceSeparate.audioConfig = audioConfig;
        // 设置编解码格式，取值 aac、mp3、flac、amr。当 Request.AudioMode 为 MusicMode 时，仅支持 mp3、wav、acc;是否必传：否
        audioConfig.codec = "aac";
        // 设置采样率单位：Hz可选 8000、11025、22050、32000、44100、48000、96000当 Codec 设置为 aac/flac 时，不支持 8000当 Codec 设置为 mp3 时，不支持 8000 和 96000当 Codec 设置为 amr 时，只支持 8000当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.samplerate = "44100";
        // 设置音频码率单位：Kbps值范围：[8，1000]当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.bitrate = "128";
        // 设置声道数当 Codec 设置为 aac/flac，支持1、2、4、5、6、8当 Codec 设置为 mp3，支持1、2 当 Codec 设置为 amr，只支持1当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.channels = "4";

        try {
            TemplateVoiceSeparateResult result = ciService.templateVoiceSeparate(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
            voiceSeparateTempleteId = result.response.template.templateId;
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void noiceSeparateTemplete1_postAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        TemplateVoiceSeparateRequest request = new TemplateVoiceSeparateRequest(TestConst.ASR_BUCKET);
        TemplateVoiceSeparate templateVoiceSeparate = new TemplateVoiceSeparate();// 模板请求体
        request.setTemplateVoiceSeparate(templateVoiceSeparate);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        templateVoiceSeparate.name = "VoiceSeparateTempleteTest"+System.currentTimeMillis();
        // 设置输出音频IsAudio：输出人声IsBackground：输出背景声AudioAndBackground：输出人声和背景声MusicMode：输出人声、背景声、Bass声、鼓声;是否必传：是
        templateVoiceSeparate.audioMode = "IsAudio";
        TemplateVoiceSeparate.AudioConfig audioConfig = new TemplateVoiceSeparate.AudioConfig();
        templateVoiceSeparate.audioConfig = audioConfig;
        // 设置编解码格式，取值 aac、mp3、flac、amr。当 Request.AudioMode 为 MusicMode 时，仅支持 mp3、wav、acc;是否必传：否
        audioConfig.codec = "aac";
        // 设置采样率单位：Hz可选 8000、11025、22050、32000、44100、48000、96000当 Codec 设置为 aac/flac 时，不支持 8000当 Codec 设置为 mp3 时，不支持 8000 和 96000当 Codec 设置为 amr 时，只支持 8000当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.samplerate = "44100";
        // 设置音频码率单位：Kbps值范围：[8，1000]当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.bitrate = "128";
        // 设置声道数当 Codec 设置为 aac/flac，支持1、2、4、5、6、8当 Codec 设置为 mp3，支持1、2 当 Codec 设置为 amr，只支持1当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.channels = "4";

        final TestLocker testLocker = new TestLocker();
        ciService.templateVoiceSeparateAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                TemplateVoiceSeparateResult result = (TemplateVoiceSeparateResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);
                voiceSeparateTempleteId = result.response.template.templateId;
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
    public void noiceSeparateTemplete2_update() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateVoiceSeparateTempleteRequest request = new UpdateVoiceSeparateTempleteRequest(TestConst.ASR_BUCKET, voiceSeparateTempleteId);
        UpdateVoiceSeparateTemplete updateVoiceSeparateTemplete = new UpdateVoiceSeparateTemplete();// 更新模板请求体
        request.setUpdateVoiceSeparateTemplete(updateVoiceSeparateTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        updateVoiceSeparateTemplete.name = "VoiceSeparateTempleteTest"+System.currentTimeMillis();
        // 设置输出音频IsAudio：输出人声IsBackground：输出背景声AudioAndBackground：输出人声和背景声MusicMode：输出人声、背景声、Bass声、鼓声;是否必传：是
        updateVoiceSeparateTemplete.audioMode = "IsBackground";
        AudioConfig audioConfig = new AudioConfig();
        updateVoiceSeparateTemplete.audioConfig = audioConfig;
        // 设置编解码格式，取值 aac、mp3、flac、amr。当 Request.AudioMode 为 MusicMode 时，仅支持 mp3、wav、acc;是否必传：否
        audioConfig.codec = "aac";
        // 设置采样率单位：Hz可选 8000、11025、22050、32000、44100、48000、96000当 Codec 设置为 aac/flac 时，不支持 8000当 Codec 设置为 mp3 时，不支持 8000 和 96000当 Codec 设置为 amr 时，只支持 8000当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.samplerate = "44100";
        // 设置音频码率单位：Kbps值范围：[8，1000]当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.bitrate = "128";
        // 设置声道数当 Codec 设置为 aac/flac，支持1、2、4、5、6、8当 Codec 设置为 mp3，支持1、2 当 Codec 设置为 amr，只支持1当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.channels = "2";

        try {
            UpdateVoiceSeparateTempleteResult result = ciService.updateVoiceSeparateTemplete(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void noiceSeparateTemplete2_updateAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateVoiceSeparateTempleteRequest request = new UpdateVoiceSeparateTempleteRequest(TestConst.ASR_BUCKET, voiceSeparateTempleteId);
        UpdateVoiceSeparateTemplete updateVoiceSeparateTemplete = new UpdateVoiceSeparateTemplete();// 更新模板请求体
        request.setUpdateVoiceSeparateTemplete(updateVoiceSeparateTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        updateVoiceSeparateTemplete.name = "VoiceSeparateTempleteTest"+System.currentTimeMillis();
        // 设置输出音频IsAudio：输出人声IsBackground：输出背景声AudioAndBackground：输出人声和背景声MusicMode：输出人声、背景声、Bass声、鼓声;是否必传：是
        updateVoiceSeparateTemplete.audioMode = "IsBackground";
        AudioConfig audioConfig = new AudioConfig();
        updateVoiceSeparateTemplete.audioConfig = audioConfig;
        // 设置编解码格式，取值 aac、mp3、flac、amr。当 Request.AudioMode 为 MusicMode 时，仅支持 mp3、wav、acc;是否必传：否
        audioConfig.codec = "aac";
        // 设置采样率单位：Hz可选 8000、11025、22050、32000、44100、48000、96000当 Codec 设置为 aac/flac 时，不支持 8000当 Codec 设置为 mp3 时，不支持 8000 和 96000当 Codec 设置为 amr 时，只支持 8000当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.samplerate = "44100";
        // 设置音频码率单位：Kbps值范围：[8，1000]当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.bitrate = "128";
        // 设置声道数当 Codec 设置为 aac/flac，支持1、2、4、5、6、8当 Codec 设置为 mp3，支持1、2 当 Codec 设置为 amr，只支持1当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
        audioConfig.channels = "2";

        final TestLocker testLocker = new TestLocker();
        ciService.updateVoiceSeparateTempleteAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                UpdateVoiceSeparateTempleteResult result = (UpdateVoiceSeparateTempleteResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

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
    public void noiceSeparateTemplete3_submitVoiceSeparateJob() {
        TestUtils.sleep(2000);
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
    public void noiceSeparateTemplete3_submitVoiceSeparateJobAsync() {
        TestUtils.sleep(2000);
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
    public void noiseReductionTemplete1_post() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostNoiseReductionTempleteRequest request = new PostNoiseReductionTempleteRequest(TestConst.ASR_BUCKET);
        PostNoiseReductionTemplete postNoiseReductionTemplete = new PostNoiseReductionTemplete();// 创建模板请求体
        request.setPostNoiseReductionTemplete(postNoiseReductionTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64。;是否必传：是
        postNoiseReductionTemplete.name = "NoiseReductionTempleteTest"+System.currentTimeMillis();
        NoiseReduction noiseReduction = new NoiseReduction();
        // 封装格式，支持 mp3、m4a、wav;是否必传：否
        noiseReduction.format = "mp3";
        // 采样率单位：Hz可选 8000、12000、16000、24000、32000、44100、48000;是否必传：否
        noiseReduction.samplerate = "8000";
        // 设置降噪参数
        postNoiseReductionTemplete.noiseReduction = noiseReduction;

        try {
            PostNoiseReductionTempleteResult result = ciService.postNoiseReductionTemplete(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
            noiseReductionTempleteId = result.response.template.templateId;
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void noiseReductionTemplete1_postAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostNoiseReductionTempleteRequest request = new PostNoiseReductionTempleteRequest(TestConst.ASR_BUCKET);
        PostNoiseReductionTemplete postNoiseReductionTemplete = new PostNoiseReductionTemplete();// 创建模板请求体
        request.setPostNoiseReductionTemplete(postNoiseReductionTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64。;是否必传：是
        postNoiseReductionTemplete.name = "NoiseReductionTempleteTest"+System.currentTimeMillis();
        NoiseReduction noiseReduction = new NoiseReduction();
        // 封装格式，支持 mp3、m4a、wav;是否必传：否
        noiseReduction.format = "mp3";
        // 采样率单位：Hz可选 8000、12000、16000、24000、32000、44100、48000;是否必传：否
        noiseReduction.samplerate = "8000";
        // 设置降噪参数
        postNoiseReductionTemplete.noiseReduction = noiseReduction;

        final TestLocker testLocker = new TestLocker();
        ciService.postNoiseReductionTempleteAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                PostNoiseReductionTempleteResult result = (PostNoiseReductionTempleteResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);
                noiseReductionTempleteId = result.response.template.templateId;
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
    public void noiseReductionTemplete2_update() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateNoiseReductionTempleteRequest request = new UpdateNoiseReductionTempleteRequest(TestConst.ASR_BUCKET, noiseReductionTempleteId);
        UpdateNoiseReductionTemplete updateNoiseReductionTemplete = new UpdateNoiseReductionTemplete();// 更新模板请求体
        request.setUpdateNoiseReductionTemplete(updateNoiseReductionTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64。;是否必传：是
        updateNoiseReductionTemplete.name = "NoiseReductionTempleteTest"+System.currentTimeMillis();
        NoiseReduction noiseReduction = new NoiseReduction();
        // 封装格式，支持 mp3、m4a、wav;是否必传：否
        noiseReduction.format = "mp3";
        // 采样率单位：Hz可选 8000、12000、16000、24000、32000、44100、48000;是否必传：否
        noiseReduction.samplerate = "8000";
        // 设置降噪参数
        updateNoiseReductionTemplete.noiseReduction = noiseReduction;

        try {
            UpdateNoiseReductionTempleteResult result = ciService.updateNoiseReductionTemplete(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void noiseReductionTemplete2_updateAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateNoiseReductionTempleteRequest request = new UpdateNoiseReductionTempleteRequest(TestConst.ASR_BUCKET, noiseReductionTempleteId);
        UpdateNoiseReductionTemplete updateNoiseReductionTemplete = new UpdateNoiseReductionTemplete();// 更新模板请求体
        request.setUpdateNoiseReductionTemplete(updateNoiseReductionTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64。;是否必传：是
        updateNoiseReductionTemplete.name = "NoiseReductionTempleteTest"+System.currentTimeMillis();
        NoiseReduction noiseReduction = new NoiseReduction();
        // 封装格式，支持 mp3、m4a、wav;是否必传：否
        noiseReduction.format = "mp3";
        // 采样率单位：Hz可选 8000、12000、16000、24000、32000、44100、48000;是否必传：否
        noiseReduction.samplerate = "8000";
        // 设置降噪参数
        updateNoiseReductionTemplete.noiseReduction = noiseReduction;

        final TestLocker testLocker = new TestLocker();
        ciService.updateNoiseReductionTempleteAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                UpdateNoiseReductionTempleteResult result = (UpdateNoiseReductionTempleteResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

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
    public void noiseReductionTemplete3_postNoiseReduction() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostNoiseReductionRequest request = new PostNoiseReductionRequest(TestConst.ASR_BUCKET);
        PostNoiseReduction postNoiseReduction = new PostNoiseReduction();// 提交任务请求体
        request.setPostNoiseReduction(postNoiseReduction);// 设置请求
        PostNoiseReduction.PostNoiseReductionInput postNoiseReductionInput = new PostNoiseReduction.PostNoiseReductionInput();
        postNoiseReduction.input = postNoiseReductionInput;
        // 设置执行音频降噪任务的文件路径目前只支持文件大小在10M之内的音频 如果输入为视频文件或者多通道的音频，只会保留单通道的音频流 目前暂不支持m3u8格式输入;是否必传：是
        postNoiseReductionInput.object = TestConst.ASR_OBJECT_GS_2C;
        PostNoiseReduction.PostNoiseReductionOperation postNoiseReductionOperation = new PostNoiseReduction.PostNoiseReductionOperation();
        postNoiseReduction.operation = postNoiseReductionOperation;
        // 设置降噪模板ID;是否必传：否
        postNoiseReductionOperation.templateId = noiseReductionTempleteId;
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postNoiseReductionOperation.jobLevel = "0";
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postNoiseReductionOperation.userData = "aaa";
        PostNoiseReduction.PostNoiseReductionOutput postNoiseReductionOutput = new PostNoiseReduction.PostNoiseReductionOutput();
        postNoiseReductionOperation.output = postNoiseReductionOutput;
        // 设置存储桶的地域;是否必传：是
        postNoiseReductionOutput.region = TestConst.PERSIST_BUCKET_REGION;
        // 设置存储结果的存储桶;是否必传：是
        postNoiseReductionOutput.bucket = TestConst.AI_RECOGNITION_BUCKET;
        // 设置结果文件名;是否必传：是
        postNoiseReductionOutput.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"NoiseReduction.mp3";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postNoiseReduction.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postNoiseReduction.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postNoiseReduction.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        try {
            PostNoiseReductionResult result = ciService.postNoiseReduction(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void noiseReductionTemplete3_postNoiseReductionAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostNoiseReductionRequest request = new PostNoiseReductionRequest(TestConst.ASR_BUCKET);
        PostNoiseReduction postNoiseReduction = new PostNoiseReduction();// 提交任务请求体
        request.setPostNoiseReduction(postNoiseReduction);// 设置请求
        PostNoiseReduction.PostNoiseReductionInput postNoiseReductionInput = new PostNoiseReduction.PostNoiseReductionInput();
        postNoiseReduction.input = postNoiseReductionInput;
        // 设置执行音频降噪任务的文件路径目前只支持文件大小在10M之内的音频 如果输入为视频文件或者多通道的音频，只会保留单通道的音频流 目前暂不支持m3u8格式输入;是否必传：是
        postNoiseReductionInput.object = TestConst.ASR_OBJECT_GS_2C;
        PostNoiseReduction.PostNoiseReductionOperation postNoiseReductionOperation = new PostNoiseReduction.PostNoiseReductionOperation();
        postNoiseReduction.operation = postNoiseReductionOperation;
        // 设置降噪模板ID;是否必传：否
        postNoiseReductionOperation.templateId = noiseReductionTempleteId;
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postNoiseReductionOperation.jobLevel = "0";
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postNoiseReductionOperation.userData = "aaa";
        PostNoiseReduction.PostNoiseReductionOutput postNoiseReductionOutput = new PostNoiseReduction.PostNoiseReductionOutput();
        postNoiseReductionOperation.output = postNoiseReductionOutput;
        // 设置存储桶的地域;是否必传：是
        postNoiseReductionOutput.region = TestConst.PERSIST_BUCKET_REGION;
        // 设置存储结果的存储桶;是否必传：是
        postNoiseReductionOutput.bucket = TestConst.AI_RECOGNITION_BUCKET;
        // 设置结果文件名;是否必传：是
        postNoiseReductionOutput.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"NoiseReduction.mp3";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postNoiseReduction.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postNoiseReduction.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postNoiseReduction.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        final TestLocker testLocker = new TestLocker();
        ciService.postNoiseReductionAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                PostNoiseReductionResult result = (PostNoiseReductionResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

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
    public void voiceSynthesisTemplete1_post() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostVoiceSynthesisTempleteRequest request = new PostVoiceSynthesisTempleteRequest(TestConst.ASR_BUCKET);
        PostVoiceSynthesisTemplete postVoiceSynthesisTemplete = new PostVoiceSynthesisTemplete();// 创建模板请求体
        request.setPostVoiceSynthesisTemplete(postVoiceSynthesisTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        postVoiceSynthesisTemplete.name = "VoiceSynthesisTest"+System.currentTimeMillis();
        // 设置处理模式Asyc（异步合成）Sync（同步合成）;是否必传：否
        postVoiceSynthesisTemplete.mode = "Asyc";
        // 设置音频格式，支持 wav、mp3、pcm ;是否必传：否
        postVoiceSynthesisTemplete.codec = "pcm";
        // 设置音色，取值和限制介绍请见下表;是否必传：否
        postVoiceSynthesisTemplete.voiceType = "aixiaoxing";
        // 设置音量，取值范围 [-10,10];是否必传：否
        postVoiceSynthesisTemplete.volume = "0";
        // 设置语速，取值范围 [50,200];是否必传：否
        postVoiceSynthesisTemplete.speed = "100";
        // 设置情绪，不同音色支持的情绪不同，详见下表;是否必传：否
        postVoiceSynthesisTemplete.emotion = "arousal";

        try {
            PostVoiceSynthesisTempleteResult result = ciService.postVoiceSynthesisTemplete(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
            voiceSynthesisTemplateId = result.response.template.templateId;
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void voiceSynthesisTemplete1_postAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostVoiceSynthesisTempleteRequest request = new PostVoiceSynthesisTempleteRequest(TestConst.ASR_BUCKET);
        PostVoiceSynthesisTemplete postVoiceSynthesisTemplete = new PostVoiceSynthesisTemplete();// 创建模板请求体
        request.setPostVoiceSynthesisTemplete(postVoiceSynthesisTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        postVoiceSynthesisTemplete.name = "VoiceSynthesisTest"+System.currentTimeMillis();
        // 设置处理模式Asyc（异步合成）Sync（同步合成）;是否必传：否
        postVoiceSynthesisTemplete.mode = "Asyc";
        // 设置音频格式，支持 wav、mp3、pcm ;是否必传：否
        postVoiceSynthesisTemplete.codec = "pcm";
        // 设置音色，取值和限制介绍请见下表;是否必传：否
        postVoiceSynthesisTemplete.voiceType = "aixiaoxing";
        // 设置音量，取值范围 [-10,10];是否必传：否
        postVoiceSynthesisTemplete.volume = "0";
        // 设置语速，取值范围 [50,200];是否必传：否
        postVoiceSynthesisTemplete.speed = "100";
        // 设置情绪，不同音色支持的情绪不同，详见下表;是否必传：否
        postVoiceSynthesisTemplete.emotion = "arousal";

        final TestLocker testLocker = new TestLocker();
        ciService.postVoiceSynthesisTempleteAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                PostVoiceSynthesisTempleteResult result = (PostVoiceSynthesisTempleteResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);
                voiceSynthesisTemplateId = result.response.template.templateId;
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
    public void voiceSynthesisTemplete2_update() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateVoiceSynthesisTempleteRequest request = new UpdateVoiceSynthesisTempleteRequest(TestConst.ASR_BUCKET, voiceSynthesisTemplateId);
        UpdateVoiceSynthesisTemplete updateVoiceSynthesisTemplete = new UpdateVoiceSynthesisTemplete();// 更新模板请求体
        request.setUpdateVoiceSynthesisTemplete(updateVoiceSynthesisTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        updateVoiceSynthesisTemplete.name = "VoiceSynthesisTest"+System.currentTimeMillis();
        // 设置处理模式Asyc（异步合成）Sync（同步合成）;是否必传：否
        updateVoiceSynthesisTemplete.mode = "Asyc";
        // 设置音频格式，支持 wav、mp3、pcm ;是否必传：否
        updateVoiceSynthesisTemplete.codec = "pcm";
        // 设置音色，取值和限制介绍请见下表;是否必传：否
        updateVoiceSynthesisTemplete.voiceType = "aixiaoxing";
        // 设置音量，取值范围 [-10,10];是否必传：否
        updateVoiceSynthesisTemplete.volume = "0";
        // 设置语速，取值范围 [50,200];是否必传：否
        updateVoiceSynthesisTemplete.speed = "100";
        // 设置情绪，不同音色支持的情绪不同，详见下表;是否必传：否
        updateVoiceSynthesisTemplete.emotion = "arousal";

        try {
            UpdateVoiceSynthesisTempleteResult result = ciService.updateVoiceSynthesisTemplete(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void voiceSynthesisTemplete2_updateAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateVoiceSynthesisTempleteRequest request = new UpdateVoiceSynthesisTempleteRequest(TestConst.ASR_BUCKET, voiceSynthesisTemplateId);
        UpdateVoiceSynthesisTemplete updateVoiceSynthesisTemplete = new UpdateVoiceSynthesisTemplete();// 更新模板请求体
        request.setUpdateVoiceSynthesisTemplete(updateVoiceSynthesisTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        updateVoiceSynthesisTemplete.name = "VoiceSynthesisTest"+System.currentTimeMillis();
        // 设置处理模式Asyc（异步合成）Sync（同步合成）;是否必传：否
        updateVoiceSynthesisTemplete.mode = "Asyc";
        // 设置音频格式，支持 wav、mp3、pcm ;是否必传：否
        updateVoiceSynthesisTemplete.codec = "pcm";
        // 设置音色，取值和限制介绍请见下表;是否必传：否
        updateVoiceSynthesisTemplete.voiceType = "aixiaoxing";
        // 设置音量，取值范围 [-10,10];是否必传：否
        updateVoiceSynthesisTemplete.volume = "0";
        // 设置语速，取值范围 [50,200];是否必传：否
        updateVoiceSynthesisTemplete.speed = "100";
        // 设置情绪，不同音色支持的情绪不同，详见下表;是否必传：否
        updateVoiceSynthesisTemplete.emotion = "arousal";

        final TestLocker testLocker = new TestLocker();
        ciService.updateVoiceSynthesisTempleteAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                UpdateVoiceSynthesisTempleteResult result = (UpdateVoiceSynthesisTempleteResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

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
    public void voiceSynthesisTemplete3_postVoiceSynthesis() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostVoiceSynthesisRequest request = new PostVoiceSynthesisRequest(TestConst.ASR_BUCKET);
        PostVoiceSynthesis postVoiceSynthesis = new PostVoiceSynthesis();// 提交任务请求体
        request.setPostVoiceSynthesis(postVoiceSynthesis);// 设置请求
        PostVoiceSynthesis.PostVoiceSynthesisOperation postVoiceSynthesisOperation = new PostVoiceSynthesis.PostVoiceSynthesisOperation();
        postVoiceSynthesis.operation = postVoiceSynthesisOperation;
        PostVoiceSynthesis.PostVoiceSynthesisTtsTpl postVoiceSynthesisTtsTpl = new PostVoiceSynthesis.PostVoiceSynthesisTtsTpl();
        postVoiceSynthesisOperation.ttsTpl = postVoiceSynthesisTtsTpl;
        // 设置处理模式Asyc（异步合成）Sync（同步合成）;是否必传：否
        postVoiceSynthesisTtsTpl.mode = "Asyc";
        // 设置音频格式，支持 wav、mp3、pcm ;是否必传：否
        postVoiceSynthesisTtsTpl.codec = "pcm";
        // 设置音色，取值和限制介绍请见下表;是否必传：否
        postVoiceSynthesisTtsTpl.voiceType = "aixiaoxing";
        // 设置音量，取值范围 [-10,10];是否必传：否
        postVoiceSynthesisTtsTpl.volume = "0";
        // 设置语速，取值范围 [50,200];是否必传：否
        postVoiceSynthesisTtsTpl.speed = "100";
        // 设置情绪，不同音色支持的情绪不同，详见下表;是否必传：否
        postVoiceSynthesisTtsTpl.emotion = "arousal";
        PostVoiceSynthesis.PostVoiceSynthesisTtsConfig postVoiceSynthesisTtsConfig = new PostVoiceSynthesis.PostVoiceSynthesisTtsConfig();
        postVoiceSynthesisOperation.ttsConfig = postVoiceSynthesisTtsConfig;
        // 设置输入类型，Url/Text;是否必传：是
        postVoiceSynthesisTtsConfig.inputType = "Text";
        // 设置当 InputType 为 Url 时， 必须是合法的 COS 地址，文件必须是utf-8编码，且大小不超过 10M。如果合成方式为同步处理，则文件内容不超过 300 个 utf-8 字符；如果合成方式为异步处理，则文件内容不超过 10000 个 utf-8 字符。当 InputType 为 Text 时, 输入必须是 utf-8 字符, 且不超过 300 个字符。;是否必传：是
        postVoiceSynthesisTtsConfig.input = "哈哈哈哈哈";
        PostVoiceSynthesis.PostVoiceSynthesisOutput postVoiceSynthesisOutput = new PostVoiceSynthesis.PostVoiceSynthesisOutput();
        postVoiceSynthesisOperation.output = postVoiceSynthesisOutput;
        // 设置存储桶的地域;是否必传：是
        postVoiceSynthesisOutput.region = TestConst.PERSIST_BUCKET_REGION;
        // 设置存储结果的存储桶;是否必传：是
        postVoiceSynthesisOutput.bucket = TestConst.AI_RECOGNITION_BUCKET;
        // 设置结果文件名;是否必传：是
        postVoiceSynthesisOutput.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"voiceSynthesis.pcm";
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postVoiceSynthesisOperation.userData = "aaa";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postVoiceSynthesisOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postVoiceSynthesis.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postVoiceSynthesis.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postVoiceSynthesis.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        try {
            PostVoiceSynthesisResult result = ciService.postVoiceSynthesis(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void voiceSynthesisTemplete3_postVoiceSynthesisAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostVoiceSynthesisRequest request = new PostVoiceSynthesisRequest(TestConst.ASR_BUCKET);
        PostVoiceSynthesis postVoiceSynthesis = new PostVoiceSynthesis();// 提交任务请求体
        request.setPostVoiceSynthesis(postVoiceSynthesis);// 设置请求
        PostVoiceSynthesis.PostVoiceSynthesisOperation postVoiceSynthesisOperation = new PostVoiceSynthesis.PostVoiceSynthesisOperation();
        postVoiceSynthesis.operation = postVoiceSynthesisOperation;
        PostVoiceSynthesis.PostVoiceSynthesisTtsTpl postVoiceSynthesisTtsTpl = new PostVoiceSynthesis.PostVoiceSynthesisTtsTpl();
        postVoiceSynthesisOperation.ttsTpl = postVoiceSynthesisTtsTpl;
        // 设置处理模式Asyc（异步合成）Sync（同步合成）;是否必传：否
        postVoiceSynthesisTtsTpl.mode = "Asyc";
        // 设置音频格式，支持 wav、mp3、pcm ;是否必传：否
        postVoiceSynthesisTtsTpl.codec = "pcm";
        // 设置音色，取值和限制介绍请见下表;是否必传：否
        postVoiceSynthesisTtsTpl.voiceType = "aixiaoxing";
        // 设置音量，取值范围 [-10,10];是否必传：否
        postVoiceSynthesisTtsTpl.volume = "0";
        // 设置语速，取值范围 [50,200];是否必传：否
        postVoiceSynthesisTtsTpl.speed = "100";
        // 设置情绪，不同音色支持的情绪不同，详见下表;是否必传：否
        postVoiceSynthesisTtsTpl.emotion = "arousal";
        PostVoiceSynthesis.PostVoiceSynthesisTtsConfig postVoiceSynthesisTtsConfig = new PostVoiceSynthesis.PostVoiceSynthesisTtsConfig();
        postVoiceSynthesisOperation.ttsConfig = postVoiceSynthesisTtsConfig;
        // 设置输入类型，Url/Text;是否必传：是
        postVoiceSynthesisTtsConfig.inputType = "Text";
        // 设置当 InputType 为 Url 时， 必须是合法的 COS 地址，文件必须是utf-8编码，且大小不超过 10M。如果合成方式为同步处理，则文件内容不超过 300 个 utf-8 字符；如果合成方式为异步处理，则文件内容不超过 10000 个 utf-8 字符。当 InputType 为 Text 时, 输入必须是 utf-8 字符, 且不超过 300 个字符。;是否必传：是
        postVoiceSynthesisTtsConfig.input = "哈哈哈哈哈";
        PostVoiceSynthesis.PostVoiceSynthesisOutput postVoiceSynthesisOutput = new PostVoiceSynthesis.PostVoiceSynthesisOutput();
        postVoiceSynthesisOperation.output = postVoiceSynthesisOutput;
        // 设置存储桶的地域;是否必传：是
        postVoiceSynthesisOutput.region = TestConst.PERSIST_BUCKET_REGION;
        // 设置存储结果的存储桶;是否必传：是
        postVoiceSynthesisOutput.bucket = TestConst.AI_RECOGNITION_BUCKET;
        // 设置结果文件名;是否必传：是
        postVoiceSynthesisOutput.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"voiceSynthesis.pcm";
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postVoiceSynthesisOperation.userData = "aaa";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postVoiceSynthesisOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postVoiceSynthesis.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postVoiceSynthesis.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postVoiceSynthesis.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        final TestLocker testLocker = new TestLocker();
        ciService.postVoiceSynthesisAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                PostVoiceSynthesisResult result = (PostVoiceSynthesisResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

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
    public void postSoundHound() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostSoundHoundRequest request = new PostSoundHoundRequest(TestConst.ASR_BUCKET);
        PostSoundHound postSoundHound = new PostSoundHound();// 提交任务请求体
        PostSoundHound.PostSoundHoundInput postSoundHoundInput = new PostSoundHound.PostSoundHoundInput();
        postSoundHound.input = postSoundHoundInput;
        // 设置文件路径;是否必传：是
        postSoundHoundInput.object = TestConst.ASR_OBJECT_GS_2C;
        PostSoundHound.PostSoundHoundOperation postSoundHoundOperation = new PostSoundHound.PostSoundHoundOperation();
        postSoundHound.operation = postSoundHoundOperation;
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postSoundHoundOperation.userData = "";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postSoundHoundOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postSoundHound.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postSoundHound.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postSoundHound.callBack = "https://github.com/tencentyun/qcloud-sdk-android";
        com.tencent.cos.xml.model.ci.common.CallBackMqConfig callBackMqConfig = new com.tencent.cos.xml.model.ci.common.CallBackMqConfig();
        callBackMqConfig.mqRegion = "bj";
        callBackMqConfig.mqMode = "Queue";
        callBackMqConfig.mqName = "test";
        postSoundHound.callBackMqConfig = callBackMqConfig;
        request.setPostSoundHound(postSoundHound);// 设置请求

        try {
            PostSoundHoundResult result = ciService.postSoundHound(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void postSoundHoundAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostSoundHoundRequest request = new PostSoundHoundRequest(TestConst.ASR_BUCKET);
        PostSoundHound postSoundHound = new PostSoundHound();// 提交任务请求体
        PostSoundHound.PostSoundHoundInput postSoundHoundInput = new PostSoundHound.PostSoundHoundInput();
        postSoundHound.input = postSoundHoundInput;
        // 设置文件路径;是否必传：是
        postSoundHoundInput.object = TestConst.ASR_OBJECT_GS_2C;
        PostSoundHound.PostSoundHoundOperation postSoundHoundOperation = new PostSoundHound.PostSoundHoundOperation();
        postSoundHound.operation = postSoundHoundOperation;
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postSoundHoundOperation.userData = "";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postSoundHoundOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postSoundHound.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postSoundHound.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postSoundHound.callBack = "https://github.com/tencentyun/qcloud-sdk-android";
        com.tencent.cos.xml.model.ci.common.CallBackMqConfig callBackMqConfig = new com.tencent.cos.xml.model.ci.common.CallBackMqConfig();
        callBackMqConfig.mqRegion = "bj";
        callBackMqConfig.mqMode = "Queue";
        callBackMqConfig.mqName = "test";
        postSoundHound.callBackMqConfig = callBackMqConfig;
        request.setPostSoundHound(postSoundHound);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.postSoundHoundAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                PostSoundHoundResult result = (PostSoundHoundResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

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
    public void vocalScore() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        VocalScoreRequest request = new VocalScoreRequest(TestConst.ASR_BUCKET);
        VocalScore vocalScore = new VocalScore();// 提交任务请求体
        request.setVocalScore(vocalScore);// 设置请求
        VocalScore.VocalScoreInput vocalScoreInput = new VocalScore.VocalScoreInput();
        vocalScore.input = vocalScoreInput;
        // 设置文件路径;是否必传：否
        vocalScoreInput.object = TestConst.ASR_OBJECT_GS_2C;
        VocalScore.VocalScoreOperation vocalScoreOperation = new VocalScore.VocalScoreOperation();
        vocalScore.operation = vocalScoreOperation;
        VocalScore.VocalScoreVocalScore vocalScoreVocalScore = new VocalScore.VocalScoreVocalScore();
        vocalScoreOperation.vocalScore = vocalScoreVocalScore;
        // 设置比对基准文件路径;是否必传：否
        vocalScoreVocalScore.standardObject = TestConst.ASR_OBJECT_GS_2C;
        // 设置透传用户信息, 可打印的 ASCII 码, 长度不超过1024;是否必传：否
        vocalScoreOperation.userData = "This is my data.";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        vocalScoreOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        vocalScore.callBackFormat = "JSON";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        vocalScore.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        try {
            VocalScoreResult result = ciService.vocalScore(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void vocalScoreAsync() {
        TestUtils.sleep(2000);
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        VocalScoreRequest request = new VocalScoreRequest(TestConst.ASR_BUCKET);
        VocalScore vocalScore = new VocalScore();// 提交任务请求体
        request.setVocalScore(vocalScore);// 设置请求
        VocalScore.VocalScoreInput vocalScoreInput = new VocalScore.VocalScoreInput();
        vocalScore.input = vocalScoreInput;
        // 设置文件路径;是否必传：否
        vocalScoreInput.object = TestConst.ASR_OBJECT_GS_2C;
        VocalScore.VocalScoreOperation vocalScoreOperation = new VocalScore.VocalScoreOperation();
        vocalScore.operation = vocalScoreOperation;
        VocalScore.VocalScoreVocalScore vocalScoreVocalScore = new VocalScore.VocalScoreVocalScore();
        vocalScoreOperation.vocalScore = vocalScoreVocalScore;
        // 设置比对基准文件路径;是否必传：否
        vocalScoreVocalScore.standardObject = TestConst.ASR_OBJECT_GS_2C;
        // 设置透传用户信息, 可打印的 ASCII 码, 长度不超过1024;是否必传：否
        vocalScoreOperation.userData = "This is my data.";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        vocalScoreOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        vocalScore.callBackFormat = "JSON";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        vocalScore.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        final TestLocker testLocker = new TestLocker();
        ciService.vocalScoreAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                // result 提交任务的结果
                // 详细字段请查看api文档或者SDK源码
                VocalScoreResult result = (VocalScoreResult) cosResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

                testLocker.release();
            }
            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }
}
