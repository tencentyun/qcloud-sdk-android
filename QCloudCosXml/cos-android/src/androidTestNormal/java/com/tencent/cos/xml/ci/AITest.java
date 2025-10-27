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
import com.tencent.cos.xml.model.EmptyResponseResult;
import com.tencent.cos.xml.model.ci.ai.AIBodyRecognitionRequest;
import com.tencent.cos.xml.model.ci.ai.AIBodyRecognitionResult;
import com.tencent.cos.xml.model.ci.ai.AIDetectCarRequest;
import com.tencent.cos.xml.model.ci.ai.AIDetectCarResult;
import com.tencent.cos.xml.model.ci.ai.AIDetectFaceRequest;
import com.tencent.cos.xml.model.ci.ai.AIDetectFaceResult;
import com.tencent.cos.xml.model.ci.ai.AIDetectPetRequest;
import com.tencent.cos.xml.model.ci.ai.AIDetectPetResult;
import com.tencent.cos.xml.model.ci.ai.AIEnhanceImageRequest;
import com.tencent.cos.xml.model.ci.ai.AIFaceEffectRequest;
import com.tencent.cos.xml.model.ci.ai.AIFaceEffectResult;
import com.tencent.cos.xml.model.ci.ai.AIGameRecRequest;
import com.tencent.cos.xml.model.ci.ai.AIGameRecResult;
import com.tencent.cos.xml.model.ci.ai.AIIDCardOCRRequest;
import com.tencent.cos.xml.model.ci.ai.AIIDCardOCRResult;
import com.tencent.cos.xml.model.ci.ai.AIImageColoringRequest;
import com.tencent.cos.xml.model.ci.ai.AIImageCropRequest;
import com.tencent.cos.xml.model.ci.ai.AILicenseRecRequest;
import com.tencent.cos.xml.model.ci.ai.AILicenseRecResult;
import com.tencent.cos.xml.model.ci.ai.AIPortraitMattingRequest;
import com.tencent.cos.xml.model.ci.ai.AISuperResolutionRequest;
import com.tencent.cos.xml.model.ci.ai.AddImageSearch;
import com.tencent.cos.xml.model.ci.ai.AddImageSearchRequest;
import com.tencent.cos.xml.model.ci.ai.AssessQualityRequest;
import com.tencent.cos.xml.model.ci.ai.AssessQualityResult;
import com.tencent.cos.xml.model.ci.ai.AutoTranslationBlockRequest;
import com.tencent.cos.xml.model.ci.ai.AutoTranslationBlockResult;
import com.tencent.cos.xml.model.ci.ai.COSOCRRequest;
import com.tencent.cos.xml.model.ci.ai.COSOCRResult;
import com.tencent.cos.xml.model.ci.ai.CloseAIBucketRequest;
import com.tencent.cos.xml.model.ci.ai.CloseAIBucketResult;
import com.tencent.cos.xml.model.ci.ai.CreateCRcodeRequest;
import com.tencent.cos.xml.model.ci.ai.CreateCRcodeResult;
import com.tencent.cos.xml.model.ci.ai.DeleteImageSearch;
import com.tencent.cos.xml.model.ci.ai.DeleteImageSearchRequest;
import com.tencent.cos.xml.model.ci.ai.DetectLabelRequest;
import com.tencent.cos.xml.model.ci.ai.DetectLabelResult;
import com.tencent.cos.xml.model.ci.ai.GetAIBucketRequest;
import com.tencent.cos.xml.model.ci.ai.GetAIBucketResult;
import com.tencent.cos.xml.model.ci.ai.GetAIQueueRequest;
import com.tencent.cos.xml.model.ci.ai.GetAIQueueResponse;
import com.tencent.cos.xml.model.ci.ai.GetAIQueueResult;
import com.tencent.cos.xml.model.ci.ai.GetActionSequenceRequest;
import com.tencent.cos.xml.model.ci.ai.GetActionSequenceResult;
import com.tencent.cos.xml.model.ci.ai.GetLiveCodeRequest;
import com.tencent.cos.xml.model.ci.ai.GetLiveCodeResult;
import com.tencent.cos.xml.model.ci.ai.GetSearchImageRequest;
import com.tencent.cos.xml.model.ci.ai.GetSearchImageResult;
import com.tencent.cos.xml.model.ci.ai.GoodsMattingRequest;
import com.tencent.cos.xml.model.ci.ai.ImageRepairRequest;
import com.tencent.cos.xml.model.ci.ai.ImageSearchBucket;
import com.tencent.cos.xml.model.ci.ai.ImageSearchBucketRequest;
import com.tencent.cos.xml.model.ci.ai.LivenessRecognitionRequest;
import com.tencent.cos.xml.model.ci.ai.LivenessRecognitionResult;
import com.tencent.cos.xml.model.ci.ai.OpenBucketAiRequest;
import com.tencent.cos.xml.model.ci.ai.OpenBucketAiResult;
import com.tencent.cos.xml.model.ci.ai.PostSegmentVideoBody;
import com.tencent.cos.xml.model.ci.ai.PostSegmentVideoBodyRequest;
import com.tencent.cos.xml.model.ci.ai.PostSegmentVideoBodyResult;
import com.tencent.cos.xml.model.ci.ai.PostTranslation;
import com.tencent.cos.xml.model.ci.ai.PostTranslationRequest;
import com.tencent.cos.xml.model.ci.ai.PostTranslationResult;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetRec;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetRecRequest;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetRecResult;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetTemplete;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetTempleteResult;
import com.tencent.cos.xml.model.ci.ai.RecognitionQRcodeRequest;
import com.tencent.cos.xml.model.ci.ai.RecognitionQRcodeResult;
import com.tencent.cos.xml.model.ci.ai.RecognizeLogoRequest;
import com.tencent.cos.xml.model.ci.ai.RecognizeLogoResult;
import com.tencent.cos.xml.model.ci.ai.UpdateAIQueue;
import com.tencent.cos.xml.model.ci.ai.UpdateAIQueueRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateAIQueueResult;
import com.tencent.cos.xml.model.ci.ai.UpdateVideoTargetTemplete;
import com.tencent.cos.xml.model.ci.ai.UpdateVideoTargetTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateVideoTargetTempleteResult;
import com.tencent.cos.xml.model.ci.common.NotifyConfig;
import com.tencent.cos.xml.model.ci.common.VideoTargetRec;
import com.tencent.cos.xml.model.object.GetObjectResult;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AITest {
    private static String aiQueueId;
    private static String aiQueueName;
    private static String videoTargetTempleteId;

    // 查询AI内容识别服务
    @Test
    public void stage1_getAIBucket() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetAIBucketRequest request = new GetAIBucketRequest();
//        request.regions = "ap-shanghai,ap-beijing";// 设置地域信息，例如 ap-shanghai、ap-beijing，若查询多个地域以“,”分隔字符串，详情请参见 地域与域名
//        request.bucketNames = "mobile-ut-1253960454,mobile-ut-temp-1253960454";// 设置存储桶名称，以“,”分隔，支持多个存储桶，精确搜索
//        request.bucketName = "mobile";// 设置存储桶名称前缀，前缀搜索
        request.pageNumber = 1;
        request.pageSize = 10;

        try {
            GetAIBucketResult result = ciService.getAIBucket(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage1_getAIBucketAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetAIBucketRequest request = new GetAIBucketRequest();
        request.regions = "ap-shanghai,ap-beijing";// 设置地域信息，例如 ap-shanghai、ap-beijing，若查询多个地域以“,”分隔字符串，详情请参见 地域与域名
        request.bucketNames = "mobile-ut-1253960454,mobile-ut-temp-1253960454";// 设置存储桶名称，以“,”分隔，支持多个存储桶，精确搜索
        request.bucketName = "ai-";// 设置存储桶名称前缀，前缀搜索
        request.pageNumber = 1;
        request.pageSize = 10;

        final TestLocker testLocker = new TestLocker();
        ciService.getAIBucketAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetAIBucketResult result = (GetAIBucketResult)cosXmlResult;
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
    public void stage2_closeAIBucket() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        CloseAIBucketRequest request = new CloseAIBucketRequest(TestConst.AI_RECOGNITION_BUCKET);

        try {
            CloseAIBucketResult result = ciService.closeAIBucket(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            if(!"AIBucketUnBinded".equals(e.getErrorCode())){
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            } else {
                Assert.assertTrue(true);
            }
        }
    }

    @Test
    public void stage2_closeAIBucketAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        CloseAIBucketRequest request = new CloseAIBucketRequest(TestConst.AI_RECOGNITION_BUCKET);

        final TestLocker testLocker = new TestLocker();
        ciService.closeAIBucketAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                CloseAIBucketResult result = (CloseAIBucketResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(serviceException != null && "AIBucketUnBinded".equals(serviceException.getErrorCode())){
                    Assert.assertTrue(true);
                    testLocker.release();
                    return;
                } else {
                    Assert.fail(TestUtils.getCosExceptionMessage(clientException, serviceException));
                    testLocker.release();
                }
            }
        });
        testLocker.lock();
    }

    @Test
    public void stage3_openBucketAi() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        OpenBucketAiRequest request = new OpenBucketAiRequest(TestConst.AI_RECOGNITION_BUCKET);
        try {
            OpenBucketAiResult result = ciService.openBucketAi(request);
            Assert.assertNotNull(result.openBucketAiResponse);
            TestUtils.printXML(result.openBucketAiResponse);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage3_openBucketAiAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        final TestLocker locker = new TestLocker();
        OpenBucketAiRequest request = new OpenBucketAiRequest(TestConst.AI_RECOGNITION_BUCKET);
        ciService.openBucketAiAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                OpenBucketAiResult result = (OpenBucketAiResult) cosResult;
                Assert.assertNotNull(result.openBucketAiResponse);
                TestUtils.printXML(result.openBucketAiResponse);
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
    public void stage4_getAIQueue() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetAIQueueRequest request = new GetAIQueueRequest(TestConst.AI_RECOGNITION_BUCKET);
//        request.queueIds = ;// 设置队列 ID，以“,”符号分割字符串
        request.state = "Active";// 设置Active 表示队列内的作业会被调度执行Paused 表示队列暂停，作业不再会被调度执行，队列内的所有作业状态维持在暂停状态，已经执行中的任务不受影响

        try {
            GetAIQueueResult result = ciService.getAIQueue(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
            for (GetAIQueueResponse.GetAIQueueResponseQueueList queue : result.response.queueList){
                if(queue.state.equals("Active")){
                    AITest.aiQueueId = queue.queueId;
                    AITest.aiQueueName = queue.name;
                }
            }
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage4_getAIQueueAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        GetAIQueueRequest request = new GetAIQueueRequest(TestConst.AI_RECOGNITION_BUCKET);
        request.queueIds = "pf9aec3578e0646b69585259b6c3082af";// 设置队列 ID，以“,”符号分割字符串
        request.state = "Active";// 设置Active 表示队列内的作业会被调度执行Paused 表示队列暂停，作业不再会被调度执行，队列内的所有作业状态维持在暂停状态，已经执行中的任务不受影响

        final TestLocker testLocker = new TestLocker();
        ciService.getAIQueueAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetAIQueueResult result = (GetAIQueueResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);
                if(result.response != null && result.response.queueList != null && result.response.queueList.size() > 0) {
                    for (GetAIQueueResponse.GetAIQueueResponseQueueList queue : result.response.queueList) {
                        if (queue.state.equals("Active")) {
                            AITest.aiQueueId = queue.queueId;
                            AITest.aiQueueName = queue.name;
                        }
                    }
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

    @Test
    public void stage5_updateAIQueue() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateAIQueueRequest request = new UpdateAIQueueRequest(TestConst.AI_RECOGNITION_BUCKET, aiQueueId);
        UpdateAIQueue updateAIQueue = new UpdateAIQueue();// 更新AI内容识别队列请求体
        // 设置队列名称，仅支持中文、英文、数字、_、-和*，长度不超过 128;是否必传：是
        updateAIQueue.name = aiQueueName;
        // 设置Active 表示队列内的作业会被调度执行Paused 表示队列暂停，作业不再会被调度执行，队列内的所有作业状态维持在暂停状态，已经执行中的任务不受影响;是否必传：是
        updateAIQueue.state = "Active";
        // 配置回调参数
        NotifyConfig notifyConfig = new NotifyConfig();
        notifyConfig.resultFormat = "XML";
        updateAIQueue.notifyConfig = notifyConfig;
        request.setUpdateAIQueue(updateAIQueue);// 设置请求

        try {
            UpdateAIQueueResult result = ciService.updateAIQueue(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void stage5_updateAIQueueAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateAIQueueRequest request = new UpdateAIQueueRequest(TestConst.AI_RECOGNITION_BUCKET, aiQueueId);
        UpdateAIQueue updateAIQueue = new UpdateAIQueue();// 更新AI内容识别队列请求体
        // 设置队列名称，仅支持中文、英文、数字、_、-和*，长度不超过 128;是否必传：是
        updateAIQueue.name = aiQueueName;
        // 设置Active 表示队列内的作业会被调度执行Paused 表示队列暂停，作业不再会被调度执行，队列内的所有作业状态维持在暂停状态，已经执行中的任务不受影响;是否必传：是
        updateAIQueue.state = "Active";
        // 配置回调参数
        NotifyConfig notifyConfig = new NotifyConfig();
        notifyConfig.resultFormat = "XML";
        updateAIQueue.notifyConfig = notifyConfig;
        request.setUpdateAIQueue(updateAIQueue);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.updateAIQueueAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                UpdateAIQueueResult result = (UpdateAIQueueResult)cosXmlResult;
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
    public void aiImageColoring() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIImageColoringRequest request = new AIImageColoringRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
        // 设置待上色图片url，与ObjectKey二选其一，如果同时存在，则默认以ObjectKey为准
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果文件保存路径
        request.saveLocalPath = TestUtils.localParentPath()+"/test.jpg";
        try {
            GetObjectResult result = cosXmlService.aiImageColoring(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiImageColoringAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIImageColoringRequest request = new AIImageColoringRequest(TestConst.AI_RECOGNITION_BUCKET);
//        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
        // 设置待上色图片url，与ObjectKey二选其一，如果同时存在，则默认以ObjectKey为准
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果保存uri
        request.saveLocalUri = Uri.fromFile(new File(TestUtils.localParentPath()+"/test.jpg"));

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiImageColoringAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetObjectResult result = (GetObjectResult)cosXmlResult;
                Assert.assertNotNull(result.headers);

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
    public void aiSuperResolution() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AISuperResolutionRequest request = new AISuperResolutionRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果文件保存路径
        request.saveLocalPath = TestUtils.localParentPath()+"/test.jpg";
        request.magnify = 2;
        try {
            GetObjectResult result = cosXmlService.aiSuperResolution(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiSuperResolutionAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AISuperResolutionRequest request = new AISuperResolutionRequest(TestConst.AI_RECOGNITION_BUCKET);
//        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果保存uri
        request.saveLocalUri = Uri.fromFile(new File(TestUtils.localParentPath()+"/test.jpg"));
        request.magnify = 2;

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiSuperResolutionAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetObjectResult result = (GetObjectResult)cosXmlResult;
                Assert.assertNotNull(result.headers);

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
    public void aiEnhanceImage() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIEnhanceImageRequest request = new AIEnhanceImageRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果文件保存路径
        request.saveLocalPath = TestUtils.localParentPath()+"/test.jpg";
        request.denoise = 3;// 设置去噪强度值，取值范围为 0 - 5 之间的整数，值为 0 时不进行去噪操作，默认值为3。
        request.sharpen = 3;// 设置锐化强度值，取值范围为 0 - 5 之间的整数，值为 0 时不进行锐化操作，默认值为3。
        request.ignoreError = 1;// 设置

        try {
            GetObjectResult result = cosXmlService.aiEnhanceImage(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiEnhanceImageAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIEnhanceImageRequest request = new AIEnhanceImageRequest(TestConst.AI_RECOGNITION_BUCKET);
        //        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果保存uri
        request.saveLocalUri = Uri.fromFile(new File(TestUtils.localParentPath()+"/test.jpg"));
        request.denoise = 3;// 设置去噪强度值，取值范围为 0 - 5 之间的整数，值为 0 时不进行去噪操作，默认值为3。
        request.sharpen = 3;// 设置锐化强度值，取值范围为 0 - 5 之间的整数，值为 0 时不进行锐化操作，默认值为3。
        request.ignoreError = 1;// 设置

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiEnhanceImageAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetObjectResult result = (GetObjectResult)cosXmlResult;
                Assert.assertNotNull(result.headers);

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
    public void aiImageCrop() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIImageCropRequest request = new AIImageCropRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果文件保存路径
        request.saveLocalPath = TestUtils.localParentPath()+"/test.jpg";
        request.width = 10;// 设置需要裁剪区域的宽度，与height共同组成所需裁剪的图片宽高比例；输入数字请大于0、小于图片宽度的像素值
        request.height = 6;// 设置需要裁剪区域的高度，与width共同组成所需裁剪的图片宽高比例；输入数字请大于0、小于图片高度的像素值；width : height建议取值在[1, 2.5]之间，超过这个范围可能会影响效果
        request.fixed = 0;// 设置是否严格按照 width 和 height 的值进行输出。取值为0时，宽高比例（width : height）会简化为最简分数，即如果width输入10、height输入20，会简化为1：2；取值为1时，输出图片的宽度等于width，高度等于height；默认值为0
        request.ignoreError = 1;// 设置当此参数为1时，针对文件过大等导致处理失败的场景，会直接返回原图而不报错

        try {
            GetObjectResult result = cosXmlService.aiImageCrop(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiImageCropAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIImageCropRequest request = new AIImageCropRequest(TestConst.AI_RECOGNITION_BUCKET);
        //        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果保存uri
        request.saveLocalUri = Uri.fromFile(new File(TestUtils.localParentPath()+"/test.jpg"));
        request.width = 10;// 设置需要裁剪区域的宽度，与height共同组成所需裁剪的图片宽高比例；输入数字请大于0、小于图片宽度的像素值
        request.height = 6;// 设置需要裁剪区域的高度，与width共同组成所需裁剪的图片宽高比例；输入数字请大于0、小于图片高度的像素值；width : height建议取值在[1, 2.5]之间，超过这个范围可能会影响效果
        request.fixed = 0;// 设置是否严格按照 width 和 height 的值进行输出。取值为0时，宽高比例（width : height）会简化为最简分数，即如果width输入10、height输入20，会简化为1：2；取值为1时，输出图片的宽度等于width，高度等于height；默认值为0
        request.ignoreError = 1;// 设置当此参数为1时，针对文件过大等导致处理失败的场景，会直接返回原图而不报错

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiImageCropAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetObjectResult result = (GetObjectResult)cosXmlResult;
                Assert.assertNotNull(result.headers);

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
    public void imageRepair() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        ImageRepairRequest request = new ImageRepairRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        // 安全base64 [[[608, 794], [1024, 794], [1024, 842], [608, 842]],[[1295, 62], [1295, 30], [1597, 32],[1597,64]]]
        request.maskPoly = "W1tbNjA4LCA3OTRdLCBbMTAyNCwgNzk0XSwgWzEwMjQsIDg0Ml0sIFs2MDgsIDg0Ml1dLFtbMTI5NSwgNjJdLCBbMTI5NSwgMzBdLCBbMTU5NywgMzJdLFsxNTk3LDY0XV1d";
        // 设置结果文件保存路径
        request.saveLocalPath = TestUtils.localParentPath()+"/test.jpg";

        try {
            GetObjectResult result = cosXmlService.imageRepair(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void imageRepairAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        ImageRepairRequest request = new ImageRepairRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 安全base64 [[[608, 794], [1024, 794], [1024, 842], [608, 842]],[[1295, 62], [1295, 30], [1597, 32],[1597,64]]]
        request.maskPoly = "W1tbNjA4LCA3OTRdLCBbMTAyNCwgNzk0XSwgWzEwMjQsIDg0Ml0sIFs2MDgsIDg0Ml1dLFtbMTI5NSwgNjJdLCBbMTI5NSwgMzBdLCBbMTU5NywgMzJdLFsxNTk3LDY0XV1d";
        // 设置结果保存uri
        request.saveLocalUri = Uri.fromFile(new File(TestUtils.localParentPath()+"/test.jpg"));

        final TestLocker testLocker = new TestLocker();
        cosXmlService.imageRepairAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetObjectResult result = (GetObjectResult)cosXmlResult;
                Assert.assertNotNull(result.headers);

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
    public void recognizeLogo() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        RecognizeLogoRequest request = new RecognizeLogoRequest(TestConst.AI_RECOGNITION_BUCKET);
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";// 设置待检查图片url

        try {
            RecognizeLogoResult result = cosXmlService.recognizeLogo(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void recognizeLogoAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        RecognizeLogoRequest request = new RecognizeLogoRequest(TestConst.AI_RECOGNITION_BUCKET);
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";// 设置待检查图片url

        final TestLocker testLocker = new TestLocker();
        cosXmlService.recognizeLogoAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                RecognizeLogoResult result = (RecognizeLogoResult)cosXmlResult;
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
    public void recognitionQRcode() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        RecognitionQRcodeRequest request = new RecognitionQRcodeRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        request.cover = 0;

        try {
            RecognitionQRcodeResult result = cosXmlService.recognitionQRcode(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void recognitionQRcodeAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        RecognitionQRcodeRequest request = new RecognitionQRcodeRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        request.cover = 0;

        final TestLocker testLocker = new TestLocker();
        cosXmlService.recognitionQRcodeAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                RecognitionQRcodeResult result = (RecognitionQRcodeResult)cosXmlResult;
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
    public void createCRcode() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        CreateCRcodeRequest request = new CreateCRcodeRequest(TestConst.AI_RECOGNITION_BUCKET);
        request.qrcodeContent = "test";// 设置可识别的二维码文本信息
        request.width = "50";// 设置指定生成的二维码或条形码的宽度，高度会进行等比压缩
        request.mode = 0;

        try {
            CreateCRcodeResult result = cosXmlService.createCRcode(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void createCRcodeAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        CreateCRcodeRequest request = new CreateCRcodeRequest(TestConst.AI_RECOGNITION_BUCKET);
        request.qrcodeContent = "test";// 设置可识别的二维码文本信息
        request.width = "50";// 设置指定生成的二维码或条形码的宽度，高度会进行等比压缩
        request.mode = 0;

        final TestLocker testLocker = new TestLocker();
        cosXmlService.createCRcodeAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                CreateCRcodeResult result = (CreateCRcodeResult)cosXmlResult;
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
    public void detectLabel() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        DetectLabelRequest request = new DetectLabelRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        request.scenes = "web";// 设置本次调用支持的识别场景，可选值如下：web，针对网络图片优化；camera，针对手机摄像头拍摄图片优化；album，针对手机相册、网盘产品优化；news，针对新闻、资讯、广电等行业优化；如果不传此参数，则默认为camera。支持多场景（scenes）一起检测，以，分隔。例如，使用 scenes=web，camera 即对一张图片使用两个模型同时检测，输出两套识别结果。

        try {
            DetectLabelResult result = cosXmlService.detectLabel(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void detectLabelAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        DetectLabelRequest request = new DetectLabelRequest(TestConst.AI_RECOGNITION_BUCKET);
//        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        request.scenes = "web";// 设置本次调用支持的识别场景，可选值如下：web，针对网络图片优化；camera，针对手机摄像头拍摄图片优化；album，针对手机相册、网盘产品优化；news，针对新闻、资讯、广电等行业优化；如果不传此参数，则默认为camera。支持多场景（scenes）一起检测，以，分隔。例如，使用 scenes=web，camera 即对一张图片使用两个模型同时检测，输出两套识别结果。

        final TestLocker testLocker = new TestLocker();
        cosXmlService.detectLabelAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                DetectLabelResult result = (DetectLabelResult)cosXmlResult;
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
    public void aiGameRec() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIGameRecRequest request = new AIGameRecRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";

        try {
            AIGameRecResult result = cosXmlService.aiGameRec(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiGameRecAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIGameRecRequest request = new AIGameRecRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
//        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiGameRecAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AIGameRecResult result = (AIGameRecResult)cosXmlResult;
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
    public void assessQuality() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AssessQualityRequest request = new AssessQualityRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);

        try {
            AssessQualityResult result = cosXmlService.assessQuality(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void assessQualityAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AssessQualityRequest request = new AssessQualityRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);

        final TestLocker testLocker = new TestLocker();
        cosXmlService.assessQualityAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AssessQualityResult result = (AssessQualityResult)cosXmlResult;
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
    public void aiDetectFace() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIDetectFaceRequest request = new AIDetectFaceRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        request.maxFaceNum = 1;

        try {
            AIDetectFaceResult result = cosXmlService.aiDetectFace(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiDetectFaceAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIDetectFaceRequest request = new AIDetectFaceRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        request.maxFaceNum = 1;

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiDetectFaceAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AIDetectFaceResult result = (AIDetectFaceResult)cosXmlResult;
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
    public void aiFaceEffect() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIFaceEffectRequest request = new AIFaceEffectRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        request.type = "face-segmentation";// 设置人脸特效类型，人脸美颜：face-beautify；人脸性别转换：face-gender-transformation；人脸年龄变化：face-age-transformation；人像分割：face-segmentation
        request.whitening = 30;// 设置type为face-beautify时生效，美白程度，取值范围[0,100]。0不美白，100代表最高程度。默认值30
        request.smoothing = 10;// 设置type为face-beautify时生效，磨皮程度，取值范围[0,100]。0不磨皮，100代表最高程度。默认值10
        request.faceLifting = 70;// 设置type为face-beautify时生效，瘦脸程度，取值范围[0,100]。0不瘦脸，100代表最高程度。默认值70
        request.eyeEnlarging = 70;// 设置type为face-beautify时生效，大眼程度，取值范围[0,100]。0不大眼，100代表最高程度。默认值70
        request.gender = 1;// 设置type为face-gender-transformation时生效，选择转换方向，0：男变女，1：女变男。无默认值，为必选项。限制：仅对图片中面积最大的人脸进行转换。
        request.age = 18;// 设置type为face-age-transformation时生效，变化到的人脸年龄,[10,80]。无默认值，为必选项。限制：仅对图片中面积最大的人脸进行转换。

        try {
            AIFaceEffectResult result = cosXmlService.aiFaceEffect(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiFaceEffectAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIFaceEffectRequest request = new AIFaceEffectRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
//        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        request.type = "face-segmentation";// 设置人脸特效类型，人脸美颜：face-beautify；人脸性别转换：face-gender-transformation；人脸年龄变化：face-age-transformation；人像分割：face-segmentation
        request.whitening = 30;// 设置type为face-beautify时生效，美白程度，取值范围[0,100]。0不美白，100代表最高程度。默认值30
        request.smoothing = 10;// 设置type为face-beautify时生效，磨皮程度，取值范围[0,100]。0不磨皮，100代表最高程度。默认值10
        request.faceLifting = 70;// 设置type为face-beautify时生效，瘦脸程度，取值范围[0,100]。0不瘦脸，100代表最高程度。默认值70
        request.eyeEnlarging = 70;// 设置type为face-beautify时生效，大眼程度，取值范围[0,100]。0不大眼，100代表最高程度。默认值70
        request.gender = 1;// 设置type为face-gender-transformation时生效，选择转换方向，0：男变女，1：女变男。无默认值，为必选项。限制：仅对图片中面积最大的人脸进行转换。
        request.age = 18;// 设置type为face-age-transformation时生效，变化到的人脸年龄,[10,80]。无默认值，为必选项。限制：仅对图片中面积最大的人脸进行转换。

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiFaceEffectAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AIFaceEffectResult result = (AIFaceEffectResult)cosXmlResult;
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
    public void aiBodyRecognition() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIBodyRecognitionRequest request = new AIBodyRecognitionRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";

        try {
            AIBodyRecognitionResult result = cosXmlService.aiBodyRecognition(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiBodyRecognitionAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIBodyRecognitionRequest request = new AIBodyRecognitionRequest(TestConst.AI_RECOGNITION_BUCKET);
        //存储桶中的位置标识符，即对象键
//        request.objectKey = TestConst.AI_RECOGNITION_IMAGE;
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiBodyRecognitionAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AIBodyRecognitionResult result = (AIBodyRecognitionResult)cosXmlResult;
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
    public void aiDetectPet() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIDetectPetRequest request = new AIDetectPetRequest(TestConst.AI_RECOGNITION_BUCKET, "image_pet.jpg");

        try {
            AIDetectPetResult result = cosXmlService.aiDetectPet(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiDetectPetAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIDetectPetRequest request = new AIDetectPetRequest(TestConst.AI_RECOGNITION_BUCKET, "image_pet.jpg");

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiDetectPetAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AIDetectPetResult result = (AIDetectPetResult)cosXmlResult;
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
    public void aiIDCardOCR() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIIDCardOCRRequest request = new AIIDCardOCRRequest(TestConst.AI_RECOGNITION_BUCKET, "IDCard.jpeg");
        request.cardSide = "FRONT";// 设置FRONT：身份证有照片的一面（人像面）BACK：身份证有国徽的一面（国徽面）该参数如果不填，将为您自动判断身份证正反面
        request.config = "{\"CropIdCard\":true,\"CropPortrait\":true}";// 设置以下可选字段均为 bool 类型，默认 false：CropIdCard，身份证照片裁剪（去掉证件外多余的边缘、自动矫正拍摄角度）CropPortrait，人像照片裁剪（自动抠取身份证头像区域）CopyWarn，复印件告警BorderCheckWarn，边框和框内遮挡告警ReshootWarn，翻拍告警DetectPsWarn，PS 检测告警TempIdWarn，临时身份证告警InvalidDateWarn，身份证有效日期不合法告警Quality，图片质量分数（评价图片的模糊程度）MultiCardDetect，是否开启多卡证检测参数设置方式参考：Config = {"CropIdCard":true,"CropPortrait":true}

        try {
            AIIDCardOCRResult result = cosXmlService.aiIDCardOCR(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiIDCardOCRAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIIDCardOCRRequest request = new AIIDCardOCRRequest(TestConst.AI_RECOGNITION_BUCKET, "IDCard.jpeg");
        request.cardSide = "FRONT";// 设置FRONT：身份证有照片的一面（人像面）BACK：身份证有国徽的一面（国徽面）该参数如果不填，将为您自动判断身份证正反面
        request.config = "{\"CropIdCard\":true,\"CropPortrait\":true}";// 设置以下可选字段均为 bool 类型，默认 false：CropIdCard，身份证照片裁剪（去掉证件外多余的边缘、自动矫正拍摄角度）CropPortrait，人像照片裁剪（自动抠取身份证头像区域）CopyWarn，复印件告警BorderCheckWarn，边框和框内遮挡告警ReshootWarn，翻拍告警DetectPsWarn，PS 检测告警TempIdWarn，临时身份证告警InvalidDateWarn，身份证有效日期不合法告警Quality，图片质量分数（评价图片的模糊程度）MultiCardDetect，是否开启多卡证检测参数设置方式参考：Config = {"CropIdCard":true,"CropPortrait":true}

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiIDCardOCRAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AIIDCardOCRResult result = (AIIDCardOCRResult)cosXmlResult;
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
    public void getLiveCode() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        GetLiveCodeRequest request = new GetLiveCodeRequest(TestConst.AI_RECOGNITION_BUCKET);

        try {
            GetLiveCodeResult result = cosXmlService.getLiveCode(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void getLiveCodeAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        GetLiveCodeRequest request = new GetLiveCodeRequest(TestConst.AI_RECOGNITION_BUCKET);

        final TestLocker testLocker = new TestLocker();
        cosXmlService.getLiveCodeAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetLiveCodeResult result = (GetLiveCodeResult)cosXmlResult;
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
    public void getActionSequence() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        GetActionSequenceRequest request = new GetActionSequenceRequest(TestConst.AI_RECOGNITION_BUCKET);

        try {
            GetActionSequenceResult result = cosXmlService.getActionSequence(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void getActionSequenceAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        GetActionSequenceRequest request = new GetActionSequenceRequest(TestConst.AI_RECOGNITION_BUCKET);

        final TestLocker testLocker = new TestLocker();
        cosXmlService.getActionSequenceAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetActionSequenceResult result = (GetActionSequenceResult)cosXmlResult;
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
    public void livenessRecognition() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        LivenessRecognitionRequest request = new LivenessRecognitionRequest(TestConst.AI_RECOGNITION_BUCKET, "IDCard.jpeg");
        request.idCard = "522530199208180048";// 设置身份证号
        request.name = "王飞";// 设置姓名
        request.livenessType = "ACTION";// 设置活体检测类型，取值：LIP/ACTION/SILENTLIP 为数字模式，ACTION 为动作模式，SILENT 为静默模式，三种模式选择一种传入
        request.validateData = "1,2";// 设置数字模式传参：数字验证码（1234），需先调用接口获取数字验证码动作模式传参：传动作顺序（2，1 or 1，2），需先调用接口获取动作顺序静默模式传参：空
        request.bestFrameNum = 1;// 设置需要返回多张最佳截图，取值范围1 - 10，不设置默认返回一张最佳截图

        try {
            LivenessRecognitionResult result = cosXmlService.livenessRecognition(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            if(e.getErrorMessage().contains("活体检测没通过")){
                Assert.assertTrue(true);
            } else {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        }
    }

    @Test
    public void livenessRecognitionAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        LivenessRecognitionRequest request = new LivenessRecognitionRequest(TestConst.AI_RECOGNITION_BUCKET, "IDCard.jpeg");
        request.idCard = "522530199208180048";// 设置身份证号
        request.name = "王飞";// 设置姓名
        request.livenessType = "ACTION";// 设置活体检测类型，取值：LIP/ACTION/SILENTLIP 为数字模式，ACTION 为动作模式，SILENT 为静默模式，三种模式选择一种传入
        request.validateData = "1,2";// 设置数字模式传参：数字验证码（1234），需先调用接口获取数字验证码动作模式传参：传动作顺序（2，1 or 1，2），需先调用接口获取动作顺序静默模式传参：空
        request.bestFrameNum = 1;// 设置需要返回多张最佳截图，取值范围1 - 10，不设置默认返回一张最佳截图

        final TestLocker testLocker = new TestLocker();
        cosXmlService.livenessRecognitionAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                LivenessRecognitionResult result = (LivenessRecognitionResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);

                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(serviceException!=null && serviceException.getErrorMessage().contains("活体检测没通过")){
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
    public void aiDetectCar() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIDetectCarRequest request = new AIDetectCarRequest(TestConst.AI_RECOGNITION_BUCKET, "car.png");

        try {
            AIDetectCarResult result = cosXmlService.aiDetectCar(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiDetectCarAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIDetectCarRequest request = new AIDetectCarRequest(TestConst.AI_RECOGNITION_BUCKET, "car.png");

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiDetectCarAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AIDetectCarResult result = (AIDetectCarResult)cosXmlResult;
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
    public void cosOCR() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        COSOCRRequest request = new COSOCRRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        request.type = "general";// 设置ocr的识别类型，有效值为general，accurate，efficient，fast，handwriting。general表示通用印刷体识别；accurate表示印刷体高精度版；efficient表示印刷体精简版；fast表示印刷体高速版；handwriting表示手写体识别。默认值为general。
        request.languageType = "auto";// 设置type值为general时有效，表示识别语言类型。支持自动识别语言类型，同时支持自选语言种类，默认中英文混合(zh)，各种语言均支持与英文混合的文字识别。可选值：zh：中英混合zh_rare：支持英文、数字、中文生僻字、繁体字，特殊符号等auto：自动mix：混合语种jap：日语kor：韩语spa：西班牙语fre：法语ger：德语por：葡萄牙语vie：越语may：马来语rus：俄语ita：意大利语hol：荷兰语swe：瑞典语fin：芬兰语dan：丹麦语nor：挪威语hun：匈牙利语tha：泰语hi：印地语ara：阿拉伯语
        request.ispdf = false;// 设置type值为general，fast时有效，表示是否开启PDF识别，有效值为true和false，默认值为false，开启后可同时支持图片和PDF的识别。
        request.pdfPagenumber = 1;
        request.isword = false;// 设置type值为general，accurate时有效，表示识别后是否需要返回单字信息，有效值为true和false，默认为false
        request.enableWordPolygon = false;// 设置type值为handwriting时有效，表示是否开启单字的四点定位坐标输出，有效值为true和false，默认值为false。

        try {
            COSOCRResult result = cosXmlService.cOSOCR(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void cosOCRAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        COSOCRRequest request = new COSOCRRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        request.type = "general";// 设置ocr的识别类型，有效值为general，accurate，efficient，fast，handwriting。general表示通用印刷体识别；accurate表示印刷体高精度版；efficient表示印刷体精简版；fast表示印刷体高速版；handwriting表示手写体识别。默认值为general。
        request.languageType = "auto";// 设置type值为general时有效，表示识别语言类型。支持自动识别语言类型，同时支持自选语言种类，默认中英文混合(zh)，各种语言均支持与英文混合的文字识别。可选值：zh：中英混合zh_rare：支持英文、数字、中文生僻字、繁体字，特殊符号等auto：自动mix：混合语种jap：日语kor：韩语spa：西班牙语fre：法语ger：德语por：葡萄牙语vie：越语may：马来语rus：俄语ita：意大利语hol：荷兰语swe：瑞典语fin：芬兰语dan：丹麦语nor：挪威语hun：匈牙利语tha：泰语hi：印地语ara：阿拉伯语
        request.ispdf = false;// 设置type值为general，fast时有效，表示是否开启PDF识别，有效值为true和false，默认值为false，开启后可同时支持图片和PDF的识别。
        request.pdfPagenumber = 1;
        request.isword = false;// 设置type值为general，accurate时有效，表示识别后是否需要返回单字信息，有效值为true和false，默认为false
        request.enableWordPolygon = false;// 设置type值为handwriting时有效，表示是否开启单字的四点定位坐标输出，有效值为true和false，默认值为false。

        final TestLocker testLocker = new TestLocker();
        cosXmlService.cOSOCRAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                COSOCRResult result = (COSOCRResult)cosXmlResult;
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
    public void aiLicenseRec() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AILicenseRecRequest request = new AILicenseRecRequest(TestConst.AI_RECOGNITION_BUCKET, "IDCard.jpeg");
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/IDCard.jpeg";// 设置您可以通过填写 detect-url 处理任意公网可访问的图片链接。不填写 detect-url 时，后台会默认处理 ObjectKey ，填写了 detect-url 时，后台会处理 detect-url 链接，无需再填写 ObjectKey detect-url 示例：http://www.example.com/abc.jpg
        request.cardType = "IDCard";// 设置卡证识别类型，有效值为IDCard，DriverLicense。<br>IDCard表示身份证；DriverLicense表示驾驶证，默认：DriverLicense

        try {
            AILicenseRecResult result = cosXmlService.aiLicenseRec(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiLicenseRecAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AILicenseRecRequest request = new AILicenseRecRequest(TestConst.AI_RECOGNITION_BUCKET, "IDCard.jpeg");
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/IDCard.jpeg";// 设置您可以通过填写 detect-url 处理任意公网可访问的图片链接。不填写 detect-url 时，后台会默认处理 ObjectKey ，填写了 detect-url 时，后台会处理 detect-url 链接，无需再填写 ObjectKey detect-url 示例：http://www.example.com/abc.jpg
        request.cardType = "IDCard";// 设置卡证识别类型，有效值为IDCard，DriverLicense。<br>IDCard表示身份证；DriverLicense表示驾驶证，默认：DriverLicense

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiLicenseRecAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AILicenseRecResult result = (AILicenseRecResult)cosXmlResult;
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
    public void autoTranslationBlock() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AutoTranslationBlockRequest request = new AutoTranslationBlockRequest(TestConst.AI_RECOGNITION_BUCKET);
        request.inputText = "测试";// 设置待翻译的文本
        request.sourceLang = "zh";// 设置输入语言，如 "zh"
        request.targetLang = "en";// 设置输出语言，如 "en"
        request.textDomain = "general";// 设置文本所属业务领域，如: "ecommerce", //缺省值为 general
        request.textStyle = "sentence";// 设置文本类型，如: "title", //缺省值为 sentence

        try {
            AutoTranslationBlockResult result = cosXmlService.autoTranslationBlock(request);
            TestUtils.print(result.response);
            Assert.assertNotNull(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void autoTranslationBlockAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AutoTranslationBlockRequest request = new AutoTranslationBlockRequest(TestConst.AI_RECOGNITION_BUCKET);
        request.inputText = "测试";// 设置待翻译的文本
        request.sourceLang = "zh";// 设置输入语言，如 "zh"
        request.targetLang = "en";// 设置输出语言，如 "en"
        request.textDomain = "general";// 设置文本所属业务领域，如: "ecommerce", //缺省值为 general
        request.textStyle = "sentence";// 设置文本类型，如: "title", //缺省值为 sentence

        final TestLocker testLocker = new TestLocker();
        cosXmlService.autoTranslationBlockAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                AutoTranslationBlockResult result = (AutoTranslationBlockResult)cosXmlResult;
                TestUtils.print(result.response);
                Assert.assertNotNull(result.response);

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
    public void postTranslation() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostTranslationRequest request = new PostTranslationRequest(TestConst.AI_RECOGNITION_BUCKET);
        PostTranslation postTranslation = new PostTranslation();// 提交任务请求体
        request.setPostTranslation(postTranslation);// 设置请求
        PostTranslation.PostTranslationInput postTranslationInput = new PostTranslation.PostTranslationInput();
        postTranslation.input = postTranslationInput;
        // 设置源文档文件名单文件（docx/xlsx/html/markdown/txt）：不超过800万字符有页数的（pdf/pptx）：不超过300页文本文件（txt）：不超过10MB二进制文件（pdf/docx/pptx/xlsx）：不超过60MB图片文件（jpg/jpeg/png/webp）：不超过10MB;是否必传：是
        postTranslationInput.object = "en.pdf";
        // 设置文档语言类型zh：简体中文zh-hk：繁体中文zh-tw：繁体中文zh-tr：繁体中文en：英语ar：阿拉伯语de：德语es：西班牙语fr：法语id：印尼语it：意大利语ja：日语pt：葡萄牙语ru：俄语ko：韩语km：高棉语lo：老挝语;是否必传：是
        postTranslationInput.lang = "en";
        // 设置文档类型pdfdocxpptxxlsxtxtxmlhtml：只能翻译 HTML 里的文本节点，需要通过 JS 动态加载的不进行翻译markdownjpgjpegpngwebp;是否必传：是
        postTranslationInput.type = "pdf";
        // 设置原始文档类型仅在 Type=pdf/jpg/jpeg/png/webp 时使用，当值为pdf时，仅支持 docx、pptx当值为jpg/jpeg/png/webp时，仅支持txt;是否必传：否
        postTranslationInput.basicType = "pptx";
        PostTranslation.PostTranslationOperation postTranslationOperation = new PostTranslation.PostTranslationOperation();
        postTranslation.operation = postTranslationOperation;
        PostTranslation.PostTranslationTranslation postTranslationTranslation = new PostTranslation.PostTranslationTranslation();
        postTranslationOperation.translation = postTranslationTranslation;
        // 设置目标语言类型源语言类型为 zh/zh-hk/zh-tw/zh-tr 时支持：en、ar、de、es、fr、id、it、ja、it、ru、ko、km、lo、pt源语言类型为 en 时支持：zh、zh-hk、zh-tw、zh-tr、ar、de、es、fr、id、it、ja、it、ru、ko、km、lo、pt其他类型时支持：zh、zh-hk、zh-tw、zh-tr、en;是否必传：是
        postTranslationTranslation.lang = "zh";
        // 设置文档类型，源文件类型与目标文件类型映射关系如下：docx：docxpptx：pptxxlsx：xlsxtxt：txtxml：xmlhtml：htmlmarkdown：markdownpdf：pdf、docxpng：txtjpg：txtjpeg：txtwebp：txt;是否必传：是
        postTranslationTranslation.type = "pdf";
        PostTranslation.PostTranslationOutput postTranslationOutput = new PostTranslation.PostTranslationOutput();
        postTranslationOperation.output = postTranslationOutput;
        // 设置存储桶的地域;是否必传：是
        postTranslationOutput.region = TestConst.PERSIST_BUCKET_REGION;
        // 设置存储结果的存储桶;是否必传：是
        postTranslationOutput.bucket = TestConst.AI_RECOGNITION_BUCKET;
        // 设置输出结果的文件名;是否必传：是
        postTranslationOutput.object = "output/zh.pdf";
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postTranslationOperation.userData = "This is my data.";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postTranslationOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postTranslation.callBackFormat = "JSON";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postTranslation.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        try {
            PostTranslationResult result = ciService.postTranslation(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void postTranslationAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostTranslationRequest request = new PostTranslationRequest(TestConst.AI_RECOGNITION_BUCKET);
        PostTranslation postTranslation = new PostTranslation();// 提交任务请求体
        request.setPostTranslation(postTranslation);// 设置请求
        PostTranslation.PostTranslationInput postTranslationInput = new PostTranslation.PostTranslationInput();
        postTranslation.input = postTranslationInput;
        // 设置源文档文件名单文件（docx/xlsx/html/markdown/txt）：不超过800万字符有页数的（pdf/pptx）：不超过300页文本文件（txt）：不超过10MB二进制文件（pdf/docx/pptx/xlsx）：不超过60MB图片文件（jpg/jpeg/png/webp）：不超过10MB;是否必传：是
        postTranslationInput.object = "en.pdf";
        // 设置文档语言类型zh：简体中文zh-hk：繁体中文zh-tw：繁体中文zh-tr：繁体中文en：英语ar：阿拉伯语de：德语es：西班牙语fr：法语id：印尼语it：意大利语ja：日语pt：葡萄牙语ru：俄语ko：韩语km：高棉语lo：老挝语;是否必传：是
        postTranslationInput.lang = "en";
        // 设置文档类型pdfdocxpptxxlsxtxtxmlhtml：只能翻译 HTML 里的文本节点，需要通过 JS 动态加载的不进行翻译markdownjpgjpegpngwebp;是否必传：是
        postTranslationInput.type = "pdf";
        // 设置原始文档类型仅在 Type=pdf/jpg/jpeg/png/webp 时使用，当值为pdf时，仅支持 docx、pptx当值为jpg/jpeg/png/webp时，仅支持txt;是否必传：否
        postTranslationInput.basicType = "pptx";
        PostTranslation.PostTranslationOperation postTranslationOperation = new PostTranslation.PostTranslationOperation();
        postTranslation.operation = postTranslationOperation;
        PostTranslation.PostTranslationTranslation postTranslationTranslation = new PostTranslation.PostTranslationTranslation();
        postTranslationOperation.translation = postTranslationTranslation;
        // 设置目标语言类型源语言类型为 zh/zh-hk/zh-tw/zh-tr 时支持：en、ar、de、es、fr、id、it、ja、it、ru、ko、km、lo、pt源语言类型为 en 时支持：zh、zh-hk、zh-tw、zh-tr、ar、de、es、fr、id、it、ja、it、ru、ko、km、lo、pt其他类型时支持：zh、zh-hk、zh-tw、zh-tr、en;是否必传：是
        postTranslationTranslation.lang = "zh";
        // 设置文档类型，源文件类型与目标文件类型映射关系如下：docx：docxpptx：pptxxlsx：xlsxtxt：txtxml：xmlhtml：htmlmarkdown：markdownpdf：pdf、docxpng：txtjpg：txtjpeg：txtwebp：txt;是否必传：是
        postTranslationTranslation.type = "pdf";
        PostTranslation.PostTranslationOutput postTranslationOutput = new PostTranslation.PostTranslationOutput();
        postTranslationOperation.output = postTranslationOutput;
        // 设置存储桶的地域;是否必传：是
        postTranslationOutput.region = TestConst.PERSIST_BUCKET_REGION;
        // 设置存储结果的存储桶;是否必传：是
        postTranslationOutput.bucket = TestConst.AI_RECOGNITION_BUCKET;
        // 设置输出结果的文件名;是否必传：是
        postTranslationOutput.object = "output/zh.pdf";
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postTranslationOperation.userData = "This is my data.";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postTranslationOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postTranslation.callBackFormat = "JSON";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postTranslation.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        final TestLocker testLocker = new TestLocker();
        ciService.postTranslationAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                // result 提交任务的结果
                // 详细字段请查看api文档或者SDK源码
                PostTranslationResult result = (PostTranslationResult) cosResult;
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

    @Test
    public void goodsMatting() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        GoodsMattingRequest request = new GoodsMattingRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
//        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果文件保存路径
        request.saveLocalPath = TestUtils.localParentPath()+"/test.jpg";

        try {
            GetObjectResult result = cosXmlService.goodsMatting(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void goodsMattingAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        GoodsMattingRequest request = new GoodsMattingRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/image.jpg";
        // 设置结果保存uri
        request.saveLocalUri = Uri.fromFile(new File(TestUtils.localParentPath()+"/test.jpg"));

        final TestLocker testLocker = new TestLocker();
        cosXmlService.goodsMattingAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetObjectResult result = (GetObjectResult)cosXmlResult;
                Assert.assertNotNull(result.headers);

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
    public void aiPortraitMatting() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIPortraitMattingRequest request = new AIPortraitMattingRequest(TestConst.AI_RECOGNITION_BUCKET, "PortraitMatting.jpg");
        request.centerLayout = true;
        request.paddingLayout = "900 x 10";
        // 设置结果文件保存路径
        request.saveLocalPath = TestUtils.localParentPath()+"/PortraitMatting.jpg";

        try {
            GetObjectResult result = cosXmlService.aiPortraitMatting(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void aiPortraitMattingAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AIPortraitMattingRequest request = new AIPortraitMattingRequest(TestConst.AI_RECOGNITION_BUCKET, "PortraitMatting.jpg");
        request.detectUrl = "https://ai-recognition-1253960454.cos.ap-guangzhou.myqcloud.com/PortraitMatting.jpg";
        // 设置结果保存uri
        request.saveLocalUri = Uri.fromFile(new File(TestUtils.localParentPath()+"/PortraitMatting.jpg"));

        final TestLocker testLocker = new TestLocker();
        cosXmlService.aiPortraitMattingAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetObjectResult result = (GetObjectResult)cosXmlResult;
                Assert.assertNotNull(result.headers);

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
    public void imageSearch_stage1_imageSearchBucket() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        ImageSearchBucketRequest request = new ImageSearchBucketRequest(TestConst.AI_RECOGNITION_BUCKET);
        ImageSearchBucket imageSearchBucket = new ImageSearchBucket();// 开通以图搜图请求体
        imageSearchBucket.maxCapacity = 10000;
        imageSearchBucket.maxQps  =10;
        request.setImageSearchBucket(imageSearchBucket);// 设置请求

        try {
            EmptyResponseResult result = ciService.imageSearchBucket(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            if(e.getErrorMessage().contains("Already Enabled")){
                Assert.assertTrue(true);
            } else {
                Assert.fail(TestUtils.getCosExceptionMessage(e));
            }
        }
    }

    @Test
    public void imageSearch_stage1_imageSearchBucketAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        ImageSearchBucketRequest request = new ImageSearchBucketRequest(TestConst.AI_RECOGNITION_BUCKET);
        ImageSearchBucket imageSearchBucket = new ImageSearchBucket();// 开通以图搜图请求体
        imageSearchBucket.maxCapacity = 10000;
        imageSearchBucket.maxQps  =10;
        request.setImageSearchBucket(imageSearchBucket);// 设置请求

        final TestLocker testLocker = new TestLocker();
        ciService.imageSearchBucketAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                Assert.assertNotNull(cosXmlResult.headers);

                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(serviceException != null && serviceException.getErrorMessage().contains("Already Enabled")){
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
    public void imageSearch_stage2_addImageSearch() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AddImageSearchRequest request = new AddImageSearchRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        AddImageSearch addImageSearch = new AddImageSearch();// 添加图库图片请求体
        request.setAddImageSearch(addImageSearch);// 设置请求
        // 设置物品 ID，最多支持64个字符。若 EntityId 已存在，则对其追加图片;是否必传：是
        addImageSearch.entityId = "apple";
        // 设置用户自定义的内容，最多支持4096个字符，查询时原样带回;是否必传：否
        addImageSearch.customContent = "myapple";
        // 设置图片自定义标签，最多不超过10个，json 字符串，格式为 key:value （例 key1>=1 key1>='aa' ）对;是否必传：否
        addImageSearch.tags = "{\"key1\":\"val1\",\"key2\":\"val2\"}";

        try {
            EmptyResponseResult result = cosXmlService.addImageSearch(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void imageSearch_stage2_addImageSearchAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        AddImageSearchRequest request = new AddImageSearchRequest(TestConst.AI_RECOGNITION_BUCKET, "car.png");
        AddImageSearch addImageSearch = new AddImageSearch();// 添加图库图片请求体
        request.setAddImageSearch(addImageSearch);// 设置请求
        // 设置物品 ID，最多支持64个字符。若 EntityId 已存在，则对其追加图片;是否必传：是
        addImageSearch.entityId = "car";
        // 设置用户自定义的内容，最多支持4096个字符，查询时原样带回;是否必传：否
        addImageSearch.customContent = "mycar";
        // 设置图片自定义标签，最多不超过10个，json 字符串，格式为 key:value （例 key1>=1 key1>='aa' ）对;是否必传：否
        addImageSearch.tags = "{\"key1\":\"val1\",\"key2\":\"val2\"}";

        final TestLocker testLocker = new TestLocker();
        cosXmlService.addImageSearchAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                EmptyResponseResult result = (EmptyResponseResult)cosXmlResult;
                Assert.assertNotNull(result.headers);

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
    public void imageSearch_stage3_getSearchImage() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        GetSearchImageRequest request = new GetSearchImageRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        request.matchThreshold = 0;// 设置出参 Score 中，只有超过 MatchThreshold 值的结果才会返回。默认为0
        request.offset = 0;// 设置起始序号，默认值为0
        request.limit = 10;// 设置返回数量，默认值为10，最大值为100
        try {
            GetSearchImageResult result = cosXmlService.getSearchImage(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void imageSearch_stage3_getSearchImageAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        GetSearchImageRequest request = new GetSearchImageRequest(TestConst.AI_RECOGNITION_BUCKET, "car.png");
        request.matchThreshold = 0;// 设置出参 Score 中，只有超过 MatchThreshold 值的结果才会返回。默认为0
        request.offset = 0;// 设置起始序号，默认值为0
        request.limit = 10;// 设置返回数量，默认值为10，最大值为100

        final TestLocker testLocker = new TestLocker();
        cosXmlService.getSearchImageAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                GetSearchImageResult result = (GetSearchImageResult)cosXmlResult;
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
    public void imageSearch_stage4_deleteImageSearch() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        DeleteImageSearchRequest request = new DeleteImageSearchRequest(TestConst.AI_RECOGNITION_BUCKET, TestConst.AI_RECOGNITION_IMAGE);
        DeleteImageSearch deleteImageSearch = new DeleteImageSearch();// 删除图库图片请求体
        request.setDeleteImageSearch(deleteImageSearch);// 设置请求
        // 设置物品 ID;是否必传：是
        deleteImageSearch.entityId = "apple";

        try {
            EmptyResponseResult result = cosXmlService.deleteImageSearch(request);
            Assert.assertNotNull(result.headers);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void imageSearch_stage4_deleteImageSearchAsync() {
        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newCosXmlService();
        DeleteImageSearchRequest request = new DeleteImageSearchRequest(TestConst.AI_RECOGNITION_BUCKET, "car.png");
        DeleteImageSearch deleteImageSearch = new DeleteImageSearch();// 删除图库图片请求体
        request.setDeleteImageSearch(deleteImageSearch);// 设置请求
        // 设置物品 ID;是否必传：是
        deleteImageSearch.entityId = "car";

        final TestLocker testLocker = new TestLocker();
        cosXmlService.deleteImageSearchAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                EmptyResponseResult result = (EmptyResponseResult)cosXmlResult;
                Assert.assertNotNull(result.headers);

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
    public void videoTargetTemplete1_post() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostVideoTargetTempleteRequest request = new PostVideoTargetTempleteRequest(TestConst.AI_RECOGNITION_BUCKET);
        PostVideoTargetTemplete postVideoTargetTemplete = new PostVideoTargetTemplete();// 创建模板请求体
        request.setPostVideoTargetTemplete(postVideoTargetTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        postVideoTargetTemplete.name = "VideoTargetTempleteTest"+System.currentTimeMillis();
        VideoTargetRec videoTargetRec = new VideoTargetRec();
        videoTargetRec.body = "true";
        videoTargetRec.car = "true";
        videoTargetRec.pet = "true";
        postVideoTargetTemplete.videoTargetRec = videoTargetRec;

        try {
            PostVideoTargetTempleteResult result = ciService.postVideoTargetTemplete(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);
            videoTargetTempleteId = result.response.template.templateId;
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void videoTargetTemplete1_postAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostVideoTargetTempleteRequest request = new PostVideoTargetTempleteRequest(TestConst.AI_RECOGNITION_BUCKET);
        PostVideoTargetTemplete postVideoTargetTemplete = new PostVideoTargetTemplete();// 创建模板请求体
        request.setPostVideoTargetTemplete(postVideoTargetTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        postVideoTargetTemplete.name = "VideoTargetTempleteTest"+System.currentTimeMillis();
        VideoTargetRec videoTargetRec = new VideoTargetRec();
        videoTargetRec.body = "true";
        videoTargetRec.car = "true";
        videoTargetRec.pet = "true";
        postVideoTargetTemplete.videoTargetRec = videoTargetRec;

        final TestLocker testLocker = new TestLocker();
        ciService.postVideoTargetTempleteAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                PostVideoTargetTempleteResult result = (PostVideoTargetTempleteResult)cosXmlResult;
                Assert.assertNotNull(result.response);
                TestUtils.printXML(result.response);
                videoTargetTempleteId = result.response.template.templateId;
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
    public void videoTargetTemplete2_update() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateVideoTargetTempleteRequest request = new UpdateVideoTargetTempleteRequest(TestConst.AI_RECOGNITION_BUCKET, videoTargetTempleteId);
        UpdateVideoTargetTemplete updateVideoTargetTemplete = new UpdateVideoTargetTemplete();// 更新模板请求体
        request.setUpdateVideoTargetTemplete(updateVideoTargetTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        updateVideoTargetTemplete.name = "VideoTargetTempleteTest"+System.currentTimeMillis();
        VideoTargetRec videoTargetRec = new VideoTargetRec();
        videoTargetRec.body = "false";
        videoTargetRec.car = "true";
        videoTargetRec.pet = "true";
        updateVideoTargetTemplete.videoTargetRec = videoTargetRec;

        try {
            UpdateVideoTargetTempleteResult result = ciService.updateVideoTargetTemplete(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void videoTargetTemplete2_updateAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        UpdateVideoTargetTempleteRequest request = new UpdateVideoTargetTempleteRequest(TestConst.AI_RECOGNITION_BUCKET, videoTargetTempleteId);
        UpdateVideoTargetTemplete updateVideoTargetTemplete = new UpdateVideoTargetTemplete();// 更新模板请求体
        request.setUpdateVideoTargetTemplete(updateVideoTargetTemplete);// 设置请求
        // 设置模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
        updateVideoTargetTemplete.name = "VideoTargetTempleteTest"+System.currentTimeMillis();
        VideoTargetRec videoTargetRec = new VideoTargetRec();
        videoTargetRec.body = "false";
        videoTargetRec.car = "true";
        videoTargetRec.pet = "true";
        updateVideoTargetTemplete.videoTargetRec = videoTargetRec;

        final TestLocker testLocker = new TestLocker();
        ciService.updateVideoTargetTempleteAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                UpdateVideoTargetTempleteResult result = (UpdateVideoTargetTempleteResult)cosXmlResult;
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
    public void videoTargetTemplete3_postVideoTargetRec() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostVideoTargetRecRequest request = new PostVideoTargetRecRequest(TestConst.AI_RECOGNITION_BUCKET);
        PostVideoTargetRec postVideoTargetRec = new PostVideoTargetRec();// 提交任务请求体
        request.setPostVideoTargetRec(postVideoTargetRec);// 设置请求
        PostVideoTargetRec.PostVideoTargetRecOperation postVideoTargetRecOperation = new PostVideoTargetRec.PostVideoTargetRecOperation();
        postVideoTargetRec.operation = postVideoTargetRecOperation;
        // 设置视频目标检测模板 ID;是否必传：否
        postVideoTargetRecOperation.templateId = videoTargetTempleteId;
        // 设置透传用户信息, 可打印的 ASCII 码, 长度不超过1024;是否必传：否
        postVideoTargetRecOperation.userData = "aaa";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postVideoTargetRecOperation.jobLevel = "0";
        PostVideoTargetRec.PostVideoTargetRecInput postVideoTargetRecInput = new PostVideoTargetRec.PostVideoTargetRecInput();
        postVideoTargetRec.input = postVideoTargetRecInput;
        // 设置媒体文件名;是否必传：否
        postVideoTargetRecInput.object = TestConst.MEDIA_BUCKET_VIDEO;
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postVideoTargetRec.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postVideoTargetRec.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postVideoTargetRec.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        try {
            PostVideoTargetRecResult result = ciService.postVideoTargetRec(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void videoTargetTemplete3_postVideoTargetRecAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostVideoTargetRecRequest request = new PostVideoTargetRecRequest(TestConst.AI_RECOGNITION_BUCKET);
        PostVideoTargetRec postVideoTargetRec = new PostVideoTargetRec();// 提交任务请求体
        request.setPostVideoTargetRec(postVideoTargetRec);// 设置请求
        PostVideoTargetRec.PostVideoTargetRecOperation postVideoTargetRecOperation = new PostVideoTargetRec.PostVideoTargetRecOperation();
        postVideoTargetRec.operation = postVideoTargetRecOperation;
        // 设置视频目标检测模板 ID;是否必传：否
        postVideoTargetRecOperation.templateId = videoTargetTempleteId;
        // 设置透传用户信息, 可打印的 ASCII 码, 长度不超过1024;是否必传：否
        postVideoTargetRecOperation.userData = "aaa";
        // 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
        postVideoTargetRecOperation.jobLevel = "0";
        PostVideoTargetRec.PostVideoTargetRecInput postVideoTargetRecInput = new PostVideoTargetRec.PostVideoTargetRecInput();
        postVideoTargetRec.input = postVideoTargetRecInput;
        // 设置媒体文件名;是否必传：否
        postVideoTargetRecInput.object = TestConst.MEDIA_BUCKET_VIDEO;
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postVideoTargetRec.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postVideoTargetRec.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postVideoTargetRec.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        final TestLocker testLocker = new TestLocker();
        ciService.postVideoTargetRecAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                PostVideoTargetRecResult result = (PostVideoTargetRecResult)cosXmlResult;
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
    public void postSegmentVideoBody() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostSegmentVideoBodyRequest request = new PostSegmentVideoBodyRequest(TestConst.AI_RECOGNITION_BUCKET);
        PostSegmentVideoBody postSegmentVideoBody = new PostSegmentVideoBody();// 提交任务请求体
        request.setPostSegmentVideoBody(postSegmentVideoBody);// 设置请求
        PostSegmentVideoBody.PostSegmentVideoBodyInput postSegmentVideoBodyInput = new PostSegmentVideoBody.PostSegmentVideoBodyInput();
        postSegmentVideoBody.input = postSegmentVideoBodyInput;
        // 设置文件路径;是否必传：是
        postSegmentVideoBodyInput.object = TestConst.MEDIA_BUCKET_VIDEO;
        PostSegmentVideoBody.PostSegmentVideoBodyOperation postSegmentVideoBodyOperation = new PostSegmentVideoBody.PostSegmentVideoBodyOperation();
        postSegmentVideoBody.operation = postSegmentVideoBodyOperation;
        PostSegmentVideoBody.PostSegmentVideoBodySegmentVideoBody postSegmentVideoBodySegmentVideoBody = new PostSegmentVideoBody.PostSegmentVideoBodySegmentVideoBody();
        postSegmentVideoBodyOperation.segmentVideoBody = postSegmentVideoBodySegmentVideoBody;
        // 设置抠图模式 Mask：输出alpha通道结果Foreground：输出前景视频Combination：输出抠图后的前景与自定义背景合成后的视频默认值：Mask;是否必传：否
        postSegmentVideoBodySegmentVideoBody.mode = "Mask";
        // 设置抠图类型HumanSeg：人像抠图GreenScreenSeg：绿幕抠图SolidColorSeg：纯色背景抠图默认值：HumanSeg;是否必传：否
        postSegmentVideoBodySegmentVideoBody.segmentType = "HumanSeg";
        // 设置mode为 Foreground 时参数生效，背景颜色为红色，取值范围 [0, 255]， 默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.backgroundRed = "0";
        // 设置mode为 Foreground 时参数生效，背景颜色为绿色，取值范围 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.backgroundGreen = "0";
        // 设置mode为 Foreground 时参数生效，背景颜色为蓝色，取值范围 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.backgroundBlue = "0";
        // 设置调整抠图的边缘位置，取值范围为[0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.binaryThreshold = "0";
        // 设置纯色背景抠图的背景色（红）, 当 SegmentType 为 SolidColorSeg 生效，取值范围为 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.removeRed = "0";
        // 设置纯色背景抠图的背景色（绿）, 当 SegmentType 为 SolidColorSeg 生效，取值范围为 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.removeGreen = "0";
        // 设置纯色背景抠图的背景色（蓝）, 当 SegmentType 为 SolidColorSeg 生效，取���范围为 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.removeBlue = "0";
        PostSegmentVideoBody.PostSegmentVideoBodyOutput postSegmentVideoBodyOutput = new PostSegmentVideoBody.PostSegmentVideoBodyOutput();
        postSegmentVideoBodyOperation.output = postSegmentVideoBodyOutput;
        // 设置存储桶的地域;是否必传：是
        postSegmentVideoBodyOutput.region = TestConst.PERSIST_BUCKET_REGION;
        // 设置存储结果的存储桶;是否必传：是
        postSegmentVideoBodyOutput.bucket = TestConst.AI_RECOGNITION_BUCKET;
        // 设置结果文件名;是否必传：是
        postSegmentVideoBodyOutput.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"SegmentVideoBody.jpg";
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postSegmentVideoBodyOperation.userData = "aaa";
        // 设置任务优先级，级别限制：0 、1 、2。级别越大任务优先级越高，默认为0;是否必传：否
        postSegmentVideoBodyOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postSegmentVideoBody.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postSegmentVideoBody.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postSegmentVideoBody.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        try {
            PostSegmentVideoBodyResult result = ciService.postSegmentVideoBody(request);
            Assert.assertNotNull(result.response);
            TestUtils.printXML(result.response);

        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }

    @Test
    public void postSegmentVideoBodyAsync() {
        CIService ciService = NormalServiceFactory.INSTANCE.newCIService();
        PostSegmentVideoBodyRequest request = new PostSegmentVideoBodyRequest(TestConst.AI_RECOGNITION_BUCKET);
        PostSegmentVideoBody postSegmentVideoBody = new PostSegmentVideoBody();// 提交任务请求体
        request.setPostSegmentVideoBody(postSegmentVideoBody);// 设置请求
        PostSegmentVideoBody.PostSegmentVideoBodyInput postSegmentVideoBodyInput = new PostSegmentVideoBody.PostSegmentVideoBodyInput();
        postSegmentVideoBody.input = postSegmentVideoBodyInput;
        // 设置文件路径;是否必传：是
        postSegmentVideoBodyInput.object = TestConst.MEDIA_BUCKET_VIDEO;
        PostSegmentVideoBody.PostSegmentVideoBodyOperation postSegmentVideoBodyOperation = new PostSegmentVideoBody.PostSegmentVideoBodyOperation();
        postSegmentVideoBody.operation = postSegmentVideoBodyOperation;
        PostSegmentVideoBody.PostSegmentVideoBodySegmentVideoBody postSegmentVideoBodySegmentVideoBody = new PostSegmentVideoBody.PostSegmentVideoBodySegmentVideoBody();
        postSegmentVideoBodyOperation.segmentVideoBody = postSegmentVideoBodySegmentVideoBody;
        // 设置抠图模式 Mask：输出alpha通道结果Foreground：输出前景视频Combination：输出抠图后的前景与自定义背景合成后的视频默认值：Mask;是否必传：否
        postSegmentVideoBodySegmentVideoBody.mode = "Mask";
        // 设置抠图类型HumanSeg：人像抠图GreenScreenSeg：绿幕抠图SolidColorSeg：纯色背景抠图默认值：HumanSeg;是否必传：否
        postSegmentVideoBodySegmentVideoBody.segmentType = "HumanSeg";
        // 设置mode为 Foreground 时参数生效，背景颜色为红色，取值范围 [0, 255]， 默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.backgroundRed = "0";
        // 设置mode为 Foreground 时参数生效，背景颜色为绿色，取值范围 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.backgroundGreen = "0";
        // 设置mode为 Foreground 时参数生效，背景颜色为蓝色，取值范围 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.backgroundBlue = "0";
        // 设置调整抠图的边缘位置，取值范围为[0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.binaryThreshold = "0";
        // 设置纯色背景抠图的背景色（红）, 当 SegmentType 为 SolidColorSeg 生效，取值范围为 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.removeRed = "0";
        // 设置纯色背景抠图的背景色（绿）, 当 SegmentType 为 SolidColorSeg 生效，取值范围为 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.removeGreen = "0";
        // 设置纯色背景抠图的背景色（蓝）, 当 SegmentType 为 SolidColorSeg 生效，取���范围为 [0, 255]，默认值为 0;是否必传：否
        postSegmentVideoBodySegmentVideoBody.removeBlue = "0";
        PostSegmentVideoBody.PostSegmentVideoBodyOutput postSegmentVideoBodyOutput = new PostSegmentVideoBody.PostSegmentVideoBodyOutput();
        postSegmentVideoBodyOperation.output = postSegmentVideoBodyOutput;
        // 设置存储桶的地域;是否必传：是
        postSegmentVideoBodyOutput.region = TestConst.PERSIST_BUCKET_REGION;
        // 设置存储结果的存储桶;是否必传：是
        postSegmentVideoBodyOutput.bucket = TestConst.AI_RECOGNITION_BUCKET;
        // 设置结果文件名;是否必传：是
        postSegmentVideoBodyOutput.object = TestConst.MEDIA_BUCKET_JOB_RESULT+"SegmentVideoBody.jpg";
        // 设置透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
        postSegmentVideoBodyOperation.userData = "aaa";
        // 设置任务优先级，级别限制：0 、1 、2。级别越大任务优先级越高，默认为0;是否必传：否
        postSegmentVideoBodyOperation.jobLevel = "0";
        // 设置任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
        postSegmentVideoBody.callBackFormat = "XML";
        // 设置任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
        postSegmentVideoBody.callBackType = "Url";
        // 设置任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
        postSegmentVideoBody.callBack = "https://github.com/tencentyun/qcloud-sdk-android";

        final TestLocker testLocker = new TestLocker();
        ciService.postSegmentVideoBodyAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult cosXmlResult) {
                PostSegmentVideoBodyResult result = (PostSegmentVideoBodyResult)cosXmlResult;
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
}