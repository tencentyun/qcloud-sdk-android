package com.tencent.cos.xml;

import android.content.Context;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.EmptyResponseResult;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateResult;
import com.tencent.cos.xml.model.ci.DescribeDocProcessBucketsRequest;
import com.tencent.cos.xml.model.ci.DescribeDocProcessBucketsResult;
import com.tencent.cos.xml.model.ci.PutBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.PutBucketDPStateResult;
import com.tencent.cos.xml.model.ci.QRCodeUploadRequest;
import com.tencent.cos.xml.model.ci.QRCodeUploadResult;
import com.tencent.cos.xml.model.ci.SensitiveContentRecognitionRequest;
import com.tencent.cos.xml.model.ci.SensitiveContentRecognitionResult;
import com.tencent.cos.xml.model.ci.ai.CloseAIBucketRequest;
import com.tencent.cos.xml.model.ci.ai.CloseAIBucketResult;
import com.tencent.cos.xml.model.ci.ai.CloseAsrBucketRequest;
import com.tencent.cos.xml.model.ci.ai.CloseAsrBucketResult;
import com.tencent.cos.xml.model.ci.ai.CreateWordsGeneralizeJobRequest;
import com.tencent.cos.xml.model.ci.ai.CreateWordsGeneralizeJobResult;
import com.tencent.cos.xml.model.ci.ai.DescribeAiQueuesRequest;
import com.tencent.cos.xml.model.ci.ai.DescribeAiQueuesResult;
import com.tencent.cos.xml.model.ci.ai.DescribeWordsGeneralizeJobRequest;
import com.tencent.cos.xml.model.ci.ai.DescribeWordsGeneralizeJobResult;
import com.tencent.cos.xml.model.ci.ai.GetAIBucketRequest;
import com.tencent.cos.xml.model.ci.ai.GetAIBucketResult;
import com.tencent.cos.xml.model.ci.ai.GetAIQueueRequest;
import com.tencent.cos.xml.model.ci.ai.GetAIQueueResult;
import com.tencent.cos.xml.model.ci.ai.ImageSearchBucketRequest;
import com.tencent.cos.xml.model.ci.ai.OpenAsrBucketRequest;
import com.tencent.cos.xml.model.ci.ai.OpenAsrBucketResult;
import com.tencent.cos.xml.model.ci.ai.OpenBucketAiRequest;
import com.tencent.cos.xml.model.ci.ai.OpenBucketAiResult;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReductionRequest;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReductionResult;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReductionTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.PostNoiseReductionTempleteResult;
import com.tencent.cos.xml.model.ci.ai.PostSegmentVideoBodyRequest;
import com.tencent.cos.xml.model.ci.ai.PostSegmentVideoBodyResult;
import com.tencent.cos.xml.model.ci.ai.PostSoundHoundRequest;
import com.tencent.cos.xml.model.ci.ai.PostSoundHoundResult;
import com.tencent.cos.xml.model.ci.ai.PostSpeechRecognitionTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.PostSpeechRecognitionTempleteResult;
import com.tencent.cos.xml.model.ci.ai.PostTranslationRequest;
import com.tencent.cos.xml.model.ci.ai.PostTranslationResult;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetRecRequest;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetRecResult;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.PostVideoTargetTempleteResult;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesisRequest;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesisResult;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesisTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.PostVoiceSynthesisTempleteResult;
import com.tencent.cos.xml.model.ci.ai.UpdateAIQueueRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateAIQueueResult;
import com.tencent.cos.xml.model.ci.ai.UpdateAsrQueueRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateAsrQueueResult;
import com.tencent.cos.xml.model.ci.ai.UpdateNoiseReductionTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateNoiseReductionTempleteResult;
import com.tencent.cos.xml.model.ci.ai.UpdateSpeechRecognitionTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateSpeechRecognitionTempleteResult;
import com.tencent.cos.xml.model.ci.ai.UpdateVideoTargetTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateVideoTargetTempleteResult;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSeparateTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSeparateTempleteResult;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSynthesisTempleteRequest;
import com.tencent.cos.xml.model.ci.ai.UpdateVoiceSynthesisTempleteResult;
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
import com.tencent.cos.xml.model.ci.audit.AddAuditTextlibKeywordRequest;
import com.tencent.cos.xml.model.ci.audit.AddAuditTextlibKeywordResult;
import com.tencent.cos.xml.model.ci.audit.CancelLiveVideoAuditRequest;
import com.tencent.cos.xml.model.ci.audit.CancelLiveVideoAuditResult;
import com.tencent.cos.xml.model.ci.audit.CreateAuditTextlibRequest;
import com.tencent.cos.xml.model.ci.audit.CreateAuditTextlibResult;
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
import com.tencent.cos.xml.model.ci.audit.GetAuditTextlibListResult;
import com.tencent.cos.xml.model.ci.audit.GetDocumentAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetDocumentAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetImageAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetImageAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetLiveVideoAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetLiveVideoAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetStrategyDetailRequest;
import com.tencent.cos.xml.model.ci.audit.GetStrategyDetailResult;
import com.tencent.cos.xml.model.ci.audit.GetStrategyListRequest;
import com.tencent.cos.xml.model.ci.audit.GetStrategyListResult;
import com.tencent.cos.xml.model.ci.audit.GetTextAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetVideoAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetVideoAuditResult;
import com.tencent.cos.xml.model.ci.audit.GetWebPageAuditRequest;
import com.tencent.cos.xml.model.ci.audit.GetWebPageAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostAudioAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostDocumentAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostImageAuditReportRequest;
import com.tencent.cos.xml.model.ci.audit.PostImageAuditReportResult;
import com.tencent.cos.xml.model.ci.audit.PostImagesAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostImagesAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostLiveVideoAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostLiveVideoAuditResult;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditReportRequest;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditReportResult;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostVideoAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostWebPageAuditRequest;
import com.tencent.cos.xml.model.ci.audit.TextAuditResult;
import com.tencent.cos.xml.model.ci.audit.UpdateAuditTextlibRequest;
import com.tencent.cos.xml.model.ci.audit.UpdateAuditTextlibResult;
import com.tencent.cos.xml.model.ci.audit.UpdateStrategyRequest;
import com.tencent.cos.xml.model.ci.audit.UpdateStrategyResult;
import com.tencent.cos.xml.model.ci.media.GetWorkflowListRequest;
import com.tencent.cos.xml.model.ci.media.GetWorkflowListResult;
import com.tencent.cos.xml.model.ci.media.SearchMediaQueueRequest;
import com.tencent.cos.xml.model.ci.media.SearchMediaQueueResult;
import com.tencent.cos.xml.model.ci.media.SubmitAnimationJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitAnimationJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitConcatJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitConcatJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitDigitalWatermarkJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitDigitalWatermarkJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitExtractDigitalWatermarkJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitExtractDigitalWatermarkJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitMediaInfoJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitMediaInfoJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitMediaSegmentJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitMediaSegmentJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitPicProcessJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitPicProcessJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitSmartCoverJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitSmartCoverJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitSnapshotJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitSnapshotJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitTranscodeJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitTranscodeJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitVideoMontageJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitVideoMontageJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitVideoTagJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitVideoTagJobResult;
import com.tencent.cos.xml.model.ci.media.SubmitVoiceSeparateJobRequest;
import com.tencent.cos.xml.model.ci.media.SubmitVoiceSeparateJobResult;
import com.tencent.cos.xml.model.ci.media.TemplateAnimationRequest;
import com.tencent.cos.xml.model.ci.media.TemplateAnimationResult;
import com.tencent.cos.xml.model.ci.media.TemplateConcatRequest;
import com.tencent.cos.xml.model.ci.media.TemplateConcatResult;
import com.tencent.cos.xml.model.ci.media.TemplatePicProcessRequest;
import com.tencent.cos.xml.model.ci.media.TemplatePicProcessResult;
import com.tencent.cos.xml.model.ci.media.TemplateSmartCoverRequest;
import com.tencent.cos.xml.model.ci.media.TemplateSmartCoverResult;
import com.tencent.cos.xml.model.ci.media.TemplateSnapshotRequest;
import com.tencent.cos.xml.model.ci.media.TemplateSnapshotResult;
import com.tencent.cos.xml.model.ci.media.TemplateTranscodeRequest;
import com.tencent.cos.xml.model.ci.media.TemplateTranscodeResult;
import com.tencent.cos.xml.model.ci.media.TemplateVideoMontageRequest;
import com.tencent.cos.xml.model.ci.media.TemplateVideoMontageResult;
import com.tencent.cos.xml.model.ci.media.TemplateVoiceSeparateRequest;
import com.tencent.cos.xml.model.ci.media.TemplateVoiceSeparateResult;
import com.tencent.cos.xml.model.ci.media.TemplateWatermarkRequest;
import com.tencent.cos.xml.model.ci.media.TemplateWatermarkResult;
import com.tencent.cos.xml.model.ci.media.TriggerWorkflowRequest;
import com.tencent.cos.xml.model.ci.media.TriggerWorkflowResult;
import com.tencent.cos.xml.model.ci.media.UpdateMediaQueueRequest;
import com.tencent.cos.xml.model.ci.media.UpdateMediaQueueResult;
import com.tencent.cos.xml.model.ci.metainsight.CreateDatasetBindingRequest;
import com.tencent.cos.xml.model.ci.metainsight.CreateDatasetBindingResult;
import com.tencent.cos.xml.model.ci.metainsight.CreateDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.CreateDatasetResult;
import com.tencent.cos.xml.model.ci.metainsight.CreateFileMetaIndexRequest;
import com.tencent.cos.xml.model.ci.metainsight.CreateFileMetaIndexResult;
import com.tencent.cos.xml.model.ci.metainsight.DatasetFaceSearchRequest;
import com.tencent.cos.xml.model.ci.metainsight.DatasetFaceSearchResult;
import com.tencent.cos.xml.model.ci.metainsight.DatasetSimpleQueryRequest;
import com.tencent.cos.xml.model.ci.metainsight.DatasetSimpleQueryResult;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDatasetBindingRequest;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDatasetBindingResult;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.DeleteDatasetResult;
import com.tencent.cos.xml.model.ci.metainsight.DeleteFileMetaIndexRequest;
import com.tencent.cos.xml.model.ci.metainsight.DeleteFileMetaIndexResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetBindingRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetBindingResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetBindingsRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetBindingsResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetsRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeDatasetsResult;
import com.tencent.cos.xml.model.ci.metainsight.DescribeFileMetaIndexRequest;
import com.tencent.cos.xml.model.ci.metainsight.DescribeFileMetaIndexResult;
import com.tencent.cos.xml.model.ci.metainsight.SearchImageRequest;
import com.tencent.cos.xml.model.ci.metainsight.SearchImageResult;
import com.tencent.cos.xml.model.ci.metainsight.UpdateDatasetRequest;
import com.tencent.cos.xml.model.ci.metainsight.UpdateDatasetResult;
import com.tencent.cos.xml.model.ci.metainsight.UpdateFileMetaIndexRequest;
import com.tencent.cos.xml.model.ci.metainsight.UpdateFileMetaIndexResult;
import com.tencent.qcloud.core.auth.COSXmlSigner;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.SignerFactory;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/10/15.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class CIService extends CosXmlService {
    public static final String SIGNERTYPE_CISIGNER = "CISigner";
    private static class CISigner extends COSXmlSigner {
        @Override
        protected String getSessionTokenKey() {
            return "x-ci-security-token";
        }
    }

    public CIService(Context context, CosXmlServiceConfig configuration, QCloudCredentialProvider qCloudCredentialProvider) {
        super(context, configuration, qCloudCredentialProvider);
        this.signerType = SIGNERTYPE_CISIGNER;
        SignerFactory.registerSigner(signerType, new CISigner());
    }

    @Override
    protected String signerTypeCompat(String signerType, CosXmlRequest cosXmlRequest){
        // SensitiveContentRecognitionRequest和QRCodeUploadRequest其实是cos域名 应该用cos签名
        if(cosXmlRequest instanceof SensitiveContentRecognitionRequest || cosXmlRequest instanceof QRCodeUploadRequest){
            // 此判断是为了兼容自定义签名UserCosXmlSigner的情况
            if(SIGNERTYPE_CISIGNER.equals(signerType)){
                return "CosXmlSigner";
            }
        }
        return signerType;
    }

    public DescribeDocProcessBucketsResult describeDocProcessBuckets(DescribeDocProcessBucketsRequest request)
            throws CosXmlClientException, CosXmlServiceException{
        return execute(request, new DescribeDocProcessBucketsResult());
    }

    public void describeDocProcessBucketsAsync(DescribeDocProcessBucketsRequest request, CosXmlResultListener listener) {
        schedule(request, new DescribeDocProcessBucketsResult(), listener);
    }

    public PutBucketDPStateResult putBucketDocumentPreviewState(PutBucketDPStateRequest request)
            throws CosXmlClientException, CosXmlServiceException{
        return execute(request, new PutBucketDPStateResult());
    }

    public void putBucketDocumentPreviewStateAsync(PutBucketDPStateRequest request, CosXmlResultListener listener) {
        schedule(request, new PutBucketDPStateResult(), listener);
    }

    public DeleteBucketDPStateResult deleteBucketDocumentPreviewState(DeleteBucketDPStateRequest request)
            throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketDPStateResult());
    }

    public void deleteBucketDocumentPreviewStateAsync(DeleteBucketDPStateRequest request, CosXmlResultListener listener) {
        schedule(request, new DeleteBucketDPStateResult(), listener);
    }

    public QRCodeUploadResult qrCodeUpload(QRCodeUploadRequest request) 
            throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new QRCodeUploadResult());
    }

    public void qrCodeUploadAsync(QRCodeUploadRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new QRCodeUploadResult(), cosXmlResultListener);
    }
    
    public SensitiveContentRecognitionResult sensitiveContentRecognition(SensitiveContentRecognitionRequest request) 
        throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SensitiveContentRecognitionResult());
    }

    public void sensitiveContentRecognitionAsync(SensitiveContentRecognitionRequest request, CosXmlResultListener listener) {
        schedule(request, new SensitiveContentRecognitionResult(), listener);
    }

    /**
     * <p>
     * 提交批量图片审核任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/63593">https://cloud.tencent.com/document/product/436/63593.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交批量图片审核任务示例</a>
     *
     * @param request 提交批量图片审核任务请求 {@link PostImagesAuditRequest}
     * @return 提交批量图片审核任务返回结果 {@link PostImagesAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostImagesAuditResult postImagesAudit(PostImagesAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostImagesAuditResult());
    }

    /**
     * <p>
     * 提交批量图片审核任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/63593">https://cloud.tencent.com/document/product/436/63593.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交批量图片审核任务示例</a>
     *
     * @param request 提交批量图片审核任务请求 {@link PostImagesAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postImagesAuditAsync(PostImagesAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostImagesAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询图片审核任务结果的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/68904">https://cloud.tencent.com/document/product/436/68904.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询图片审核任务结果示例</a>
     *
     * @param request 查询图片审核任务结果请求 {@link GetImageAuditRequest}
     * @return 查询图片审核任务结果返回结果 {@link GetImageAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetImageAuditResult getImageAudit(GetImageAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetImageAuditResult());
    }

    /**
     * <p>
     * 查询图片审核任务结果的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/68904">https://cloud.tencent.com/document/product/436/68904.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询图片审核任务结果示例</a>
     *
     * @param request 查询图片审核任务结果请求 {@link GetImageAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getImageAuditAsync(GetImageAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetImageAuditResult(), cosXmlResultListener);
    }
    
    /**
     * <p>
     * 提交视频审核任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/47316">https://cloud.tencent.com/document/product/436/47316.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交视频审核任务示例</a>
     *
     * @param request 提交视频审核任务请求 {@link PostVideoAuditRequest}
     * @return 提交视频审核任务返回结果 {@link PostAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostAuditResult postVideoAudit(PostVideoAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostAuditResult());
    }

    /**
     * <p>
     * 提交视频审核任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/47316">https://cloud.tencent.com/document/product/436/47316.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交视频审核任务示例</a>
     *
     * @param request 提交视频审核任务请求 {@link PostVideoAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postVideoAuditAsync(PostVideoAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询视频审核任务结果的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/47317">https://cloud.tencent.com/document/product/436/47317.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询视频审核任务结果示例</a>
     *
     * @param request 查询视频审核任务结果请求 {@link GetVideoAuditRequest}
     * @return 查询视频审核任务结果返回结果 {@link GetVideoAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetVideoAuditResult getVideoAudit(GetVideoAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetVideoAuditResult());
    }

    /**
     * <p>
     * 查询视频审核任务结果的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/47317">https://cloud.tencent.com/document/product/436/47317.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询视频审核任务结果示例</a>
     *
     * @param request 查询视频审核任务结果请求 {@link GetVideoAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getVideoAuditAsync(GetVideoAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetVideoAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交音频审核任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54063">https://cloud.tencent.com/document/product/436/54063.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交音频审核任务示例</a>
     *
     * @param request 提交音频审核任务请求 {@link PostAudioAuditRequest}
     * @return 提交音频审核任务返回结果 {@link PostAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostAuditResult postAudioAudit(PostAudioAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostAuditResult());
    }

    /**
     * <p>
     * 提交音频审核任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54063">https://cloud.tencent.com/document/product/436/54063.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交音频审核任务示例</a>
     *
     * @param request 提交音频审核任务请求 {@link PostAudioAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postAudioAuditAsync(PostAudioAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询音频审核任务结果的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54064">https://cloud.tencent.com/document/product/436/54064.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询音频审核任务结果示例</a>
     *
     * @param request 查询音频审核任务结果请求 {@link GetAudioAuditRequest}
     * @return 查询音频审核任务结果返回结果 {@link GetAudioAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetAudioAuditResult getAudioAudit(GetAudioAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetAudioAuditResult());
    }

    /**
     * <p>
     * 查询音频审核任务结果的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54064">https://cloud.tencent.com/document/product/436/54064.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询音频审核任务结果示例</a>
     *
     * @param request 查询音频审核任务结果请求 {@link GetAudioAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getAudioAuditAsync(GetAudioAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetAudioAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交文本审核任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/56289">https://cloud.tencent.com/document/product/436/56289.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交文本审核任务示例</a>
     *
     * @param request 提交文本审核任务请求 {@link PostTextAuditRequest}
     * @return 提交文本审核任务返回结果 {@link TextAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TextAuditResult postTextAudit(PostTextAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TextAuditResult());
    }

    /**
     * <p>
     * 提交文本审核任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/56289">https://cloud.tencent.com/document/product/436/56289.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交文本审核任务示例</a>
     *
     * @param request 提交文本审核任务请求 {@link PostTextAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postTextAuditAsync(PostTextAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TextAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询文本审核任务结果的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/56288">https://cloud.tencent.com/document/product/436/56288.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询文本审核任务结果示例</a>
     *
     * @param request 查询文本审核任务结果请求 {@link GetTextAuditRequest}
     * @return 查询文本审核任务结果返回结果 {@link TextAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TextAuditResult getTextAudit(GetTextAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TextAuditResult());
    }

    /**
     * <p>
     * 查询文本审核任务结果的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/56288">https://cloud.tencent.com/document/product/436/56288.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询文本审核任务结果示例</a>
     *
     * @param request 查询文本审核任务结果请求 {@link GetTextAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getTextAuditAsync(GetTextAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TextAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交文档审核任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/59381">https://cloud.tencent.com/document/product/436/59381.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交文档审核任务示例</a>
     *
     * @param request 提交文档审核任务请求 {@link PostDocumentAuditRequest}
     * @return 提交文档审核任务返回结果 {@link PostAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostAuditResult postDocumentAudit(PostDocumentAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostAuditResult());
    }

    /**
     * <p>
     * 提交文档审核任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/59381">https://cloud.tencent.com/document/product/436/59381.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交文档审核任务示例</a>
     *
     * @param request 提交文档审核任务请求 {@link PostDocumentAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postDocumentAuditAsync(PostDocumentAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询文档审核任务结果的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/59382">https://cloud.tencent.com/document/product/436/59382.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询文档审核任务结果示例</a>
     *
     * @param request 查询文档审核任务结果请求 {@link GetDocumentAuditRequest}
     * @return 查询文档审核任务结果返回结果 {@link GetDocumentAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetDocumentAuditResult getDocumentAudit(GetDocumentAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetDocumentAuditResult());
    }

    /**
     * <p>
     * 查询文档审核任务结果的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/59382">https://cloud.tencent.com/document/product/436/59382.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询文档审核任务结果示例</a>
     *
     * @param request 查询文档审核任务结果请求 {@link GetDocumentAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getDocumentAuditAsync(GetDocumentAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetDocumentAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交网页审核任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/63958">https://cloud.tencent.com/document/product/436/63958.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交网页审核任务示例</a>
     *
     * @param request 提交网页审核任务请求 {@link PostWebPageAuditRequest}
     * @return 提交网页审核任务返回结果 {@link PostAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostAuditResult postWebPageAudit(PostWebPageAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostAuditResult());
    }

    /**
     * <p>
     * 提交网页审核任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/63958">https://cloud.tencent.com/document/product/436/63958.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交网页审核任务示例</a>
     *
     * @param request 提交网页审核任务请求 {@link PostWebPageAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postWebPageAuditAsync(PostWebPageAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询网页审核任务结果的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/63959">https://cloud.tencent.com/document/product/436/63959.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询网页审核任务结果示例</a>
     *
     * @param request 查询网页审核任务结果请求 {@link GetWebPageAuditRequest}
     * @return 查询网页审核任务结果返回结果 {@link GetWebPageAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetWebPageAuditResult getWebPageAudit(GetWebPageAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetWebPageAuditResult());
    }

    /**
     * <p>
     * 查询网页审核任务结果的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/63959">https://cloud.tencent.com/document/product/436/63959.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询网页审核任务结果示例</a>
     *
     * @param request 查询网页审核任务结果请求 {@link GetWebPageAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getWebPageAuditAsync(GetWebPageAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetWebPageAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询已经开通语音识别功能的存储桶的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46232">https://cloud.tencent.com/document/product/460/46232.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询已经开通语音识别功能的存储桶示例</a>
     *
     * @param request 查询已经开通语音识别功能的存储桶请求 {@link DescribeSpeechBucketsRequest}
     * @return 查询已经开通语音识别功能的存储桶返回结果 {@link DescribeSpeechBucketsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeSpeechBucketsResult describeSpeechBuckets(DescribeSpeechBucketsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeSpeechBucketsResult());
    }

    /**
     * <p>
     * 查询已经开通语音识别功能的存储桶的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46232">https://cloud.tencent.com/document/product/460/46232.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询已经开通语音识别功能的存储桶示例</a>
     *
     * @param request 查询已经开通语音识别功能的存储桶请求 {@link DescribeSpeechBucketsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeSpeechBucketsAsync(DescribeSpeechBucketsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeSpeechBucketsResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询语音识别队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46234">https://cloud.tencent.com/document/product/460/46234.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询语音识别队列示例</a>
     *
     * @param request 查询语音识别队列请求 {@link DescribeSpeechQueuesRequest}
     * @return 查询语音识别队列返回结果 {@link DescribeSpeechQueuesResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeSpeechQueuesResult describeSpeechQueues(DescribeSpeechQueuesRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeSpeechQueuesResult());
    }

    /**
     * <p>
     * 查询语音识别队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46234">https://cloud.tencent.com/document/product/460/46234.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询语音识别队列示例</a>
     *
     * @param request 查询语音识别队列请求 {@link DescribeSpeechQueuesRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeSpeechQueuesAsync(DescribeSpeechQueuesRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeSpeechQueuesResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个语音识别任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84798">https://cloud.tencent.com/document/product/460/84798.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交一个语音识别任务示例</a>
     *
     * @param request 提交一个语音识别任务请求 {@link CreateSpeechJobsRequest}
     * @return 提交一个语音识别任务返回结果 {@link CreateSpeechJobsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CreateSpeechJobsResult createSpeechJobs(CreateSpeechJobsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CreateSpeechJobsResult());
    }

    /**
     * <p>
     * 提交一个语音识别任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84798">https://cloud.tencent.com/document/product/460/84798.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交一个语音识别任务示例</a>
     *
     * @param request 提交一个语音识别任务请求 {@link CreateSpeechJobsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void createSpeechJobsAsync(CreateSpeechJobsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CreateSpeechJobsResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询指定的语音识别任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/47596">https://cloud.tencent.com/document/product/436/47596.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询指定的语音识别任务示例</a>
     *
     * @param request 查询指定的语音识别任务请求 {@link DescribeSpeechJobRequest}
     * @return 查询指定的语音识别任务返回结果 {@link DescribeSpeechJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeSpeechJobResult describeSpeechJob(DescribeSpeechJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeSpeechJobResult());
    }

    /**
     * <p>
     * 查询指定的语音识别任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/47596">https://cloud.tencent.com/document/product/436/47596.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询指定的语音识别任务示例</a>
     *
     * @param request 查询指定的语音识别任务请求 {@link DescribeSpeechJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeSpeechJobAsync(DescribeSpeechJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeSpeechJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 拉取符合条件的语音识别任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/47597">https://cloud.tencent.com/document/product/436/47597.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">拉取符合条件的语音识别任务示例</a>
     *
     * @param request 拉取符合条件的语音识别任务请求 {@link DescribeSpeechJobsRequest}
     * @return 拉取符合条件的语音识别任务返回结果 {@link DescribeSpeechJobsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeSpeechJobsResult describeSpeechJobs(DescribeSpeechJobsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeSpeechJobsResult());
    }

    /**
     * <p>
     * 拉取符合条件的语音识别任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/47597">https://cloud.tencent.com/document/product/436/47597.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">拉取符合条件的语音识别任务示例</a>
     *
     * @param request 拉取符合条件的语音识别任务请求 {@link DescribeSpeechJobsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeSpeechJobsAsync(DescribeSpeechJobsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeSpeechJobsResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 开通AI 内容识别服务并生成队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/79593">https://cloud.tencent.com/document/product/460/79593.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">开通AI 内容识别服务并生成队列示例</a>
     *
     * @param request 开通AI 内容识别服务并生成队列请求 {@link OpenBucketAiRequest}
     * @return 开通AI 内容识别服务并生成队列返回结果 {@link OpenBucketAiResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public OpenBucketAiResult openBucketAi(OpenBucketAiRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new OpenBucketAiResult());
    }

    /**
     * <p>
     * 开通AI 内容识别服务并生成队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/79593">https://cloud.tencent.com/document/product/460/79593.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">开通AI 内容识别服务并生成队列示例</a>
     *
     * @param request 开通AI 内容识别服务并生成队列请求 {@link OpenBucketAiRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void openBucketAiAsync(OpenBucketAiRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new OpenBucketAiResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询Ai识别队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46234">https://cloud.tencent.com/document/product/460/46234.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询Ai识别队列示例</a>
     *
     * @param request 查询Ai识别队列请求 {@link DescribeAiQueuesRequest}
     * @return 查询Ai识别队列返回结果 {@link DescribeAiQueuesResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeAiQueuesResult describeAiQueues(DescribeAiQueuesRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeAiQueuesResult());
    }

    /**
     * <p>
     * 查询Ai识别队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46234">https://cloud.tencent.com/document/product/460/46234.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询Ai识别队列示例</a>
     *
     * @param request 查询Ai识别队列请求 {@link DescribeAiQueuesRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeAiQueuesAsync(DescribeAiQueuesRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeAiQueuesResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个Ai分词识别任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84800">https://cloud.tencent.com/document/product/460/84800.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交一个Ai分词识别任务示例</a>
     *
     * @param request 提交一个Ai分词识别任务请求 {@link CreateWordsGeneralizeJobRequest}
     * @return 提交一个Ai分词识别任务返回结果 {@link CreateWordsGeneralizeJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CreateWordsGeneralizeJobResult createWordsGeneralizeJob(CreateWordsGeneralizeJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CreateWordsGeneralizeJobResult());
    }

    /**
     * <p>
     * 提交一个Ai分词识别任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84800">https://cloud.tencent.com/document/product/460/84800.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">提交一个Ai分词识别任务示例</a>
     *
     * @param request 提交一个Ai分词识别任务请求 {@link CreateWordsGeneralizeJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void createWordsGeneralizeJobAsync(CreateWordsGeneralizeJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CreateWordsGeneralizeJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询指定的Ai分词识别任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46229">https://cloud.tencent.com/document/product/460/46229.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询指定的Ai分词识别任务示例</a>
     *
     * @param request 查询指定的Ai分词识别任务请求 {@link DescribeWordsGeneralizeJobRequest}
     * @return 查询指定的Ai分词识别任务返回结果 {@link DescribeWordsGeneralizeJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeWordsGeneralizeJobResult describeWordsGeneralizeJob(DescribeWordsGeneralizeJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeWordsGeneralizeJobResult());
    }

    /**
     * <p>
     * 查询指定的Ai分词识别任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46229">https://cloud.tencent.com/document/product/460/46229.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">查询指定的Ai分词识别任务示例</a>
     *
     * @param request 查询指定的Ai分词识别任务请求 {@link DescribeWordsGeneralizeJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeWordsGeneralizeJobAsync(DescribeWordsGeneralizeJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeWordsGeneralizeJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 测试工作流的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76864">https://cloud.tencent.com/document/product/460/76864</a>
     *
     * @param request 测试工作流请求 {@link TriggerWorkflowRequest}
     * @return 测试工作流返回结果 {@link TriggerWorkflowResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TriggerWorkflowResult triggerWorkflow(TriggerWorkflowRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TriggerWorkflowResult());
    }

    /**
     * <p>
     * 测试工作流的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76864">https://cloud.tencent.com/document/product/460/76864</a>
     *
     * @param request 测试工作流请求 {@link TriggerWorkflowRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void triggerWorkflowAsync(TriggerWorkflowRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TriggerWorkflowResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个提取数字水印任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84786">https://cloud.tencent.com/document/product/460/84786</a>
     *
     * @param request 提交一个提取数字水印任务请求 {@link SubmitExtractDigitalWatermarkJobRequest}
     * @return 提交一个提取数字水印任务返回结果 {@link SubmitExtractDigitalWatermarkJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitExtractDigitalWatermarkJobResult submitExtractDigitalWatermarkJob(SubmitExtractDigitalWatermarkJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitExtractDigitalWatermarkJobResult());
    }

    /**
     * <p>
     * 提交一个提取数字水印任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84786">https://cloud.tencent.com/document/product/460/84786</a>
     *
     * @param request 提交一个提取数字水印任务请求 {@link SubmitExtractDigitalWatermarkJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitExtractDigitalWatermarkJobAsync(SubmitExtractDigitalWatermarkJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitExtractDigitalWatermarkJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 更新媒体处理队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/42324">https://cloud.tencent.com/document/product/460/42324</a>
     *
     * @param request 更新媒体处理队列请求 {@link UpdateMediaQueueRequest}
     * @return 更新媒体处理队列返回结果 {@link UpdateMediaQueueResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateMediaQueueResult updateMediaQueue(UpdateMediaQueueRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateMediaQueueResult());
    }

    /**
     * <p>
     * 更新媒体处理队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/42324">https://cloud.tencent.com/document/product/460/42324</a>
     *
     * @param request 更新媒体处理队列请求 {@link UpdateMediaQueueRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateMediaQueueAsync(UpdateMediaQueueRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateMediaQueueResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个动图任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84784">https://cloud.tencent.com/document/product/460/84784</a>
     *
     * @param request 提交一个动图任务请求 {@link SubmitAnimationJobRequest}
     * @return 提交一个动图任务返回结果 {@link SubmitAnimationJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitAnimationJobResult submitAnimationJob(SubmitAnimationJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitAnimationJobResult());
    }

    /**
     * <p>
     * 提交一个动图任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84784">https://cloud.tencent.com/document/product/460/84784</a>
     *
     * @param request 提交一个动图任务请求 {@link SubmitAnimationJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitAnimationJobAsync(SubmitAnimationJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitAnimationJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 搜索媒体处理队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/38913">https://cloud.tencent.com/document/product/460/38913</a>
     *
     * @param request 搜索媒体处理队列请求 {@link SearchMediaQueueRequest}
     * @return 搜索媒体处理队列返回结果 {@link SearchMediaQueueResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SearchMediaQueueResult searchMediaQueue(SearchMediaQueueRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SearchMediaQueueResult());
    }

    /**
     * <p>
     * 搜索媒体处理队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/38913">https://cloud.tencent.com/document/product/460/38913</a>
     *
     * @param request 搜索媒体处理队列请求 {@link SearchMediaQueueRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void searchMediaQueueAsync(SearchMediaQueueRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SearchMediaQueueResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个视频标签任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84779">https://cloud.tencent.com/document/product/460/84779</a>
     *
     * @param request 提交一个视频标签任务请求 {@link SubmitVideoTagJobRequest}
     * @return 提交一个视频标签任务返回结果 {@link SubmitVideoTagJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitVideoTagJobResult submitVideoTagJob(SubmitVideoTagJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitVideoTagJobResult());
    }

    /**
     * <p>
     * 提交一个视频标签任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84779">https://cloud.tencent.com/document/product/460/84779</a>
     *
     * @param request 提交一个视频标签任务请求 {@link SubmitVideoTagJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitVideoTagJobAsync(SubmitVideoTagJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitVideoTagJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个转封装任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84789">https://cloud.tencent.com/document/product/460/84789</a>
     *
     * @param request 提交一个转封装任务请求 {@link SubmitMediaSegmentJobRequest}
     * @return 提交一个转封装任务返回结果 {@link SubmitMediaSegmentJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitMediaSegmentJobResult submitMediaSegmentJob(SubmitMediaSegmentJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitMediaSegmentJobResult());
    }

    /**
     * <p>
     * 提交一个转封装任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84789">https://cloud.tencent.com/document/product/460/84789</a>
     *
     * @param request 提交一个转封装任务请求 {@link SubmitMediaSegmentJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitMediaSegmentJobAsync(SubmitMediaSegmentJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitMediaSegmentJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个智能封面任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84791">https://cloud.tencent.com/document/product/460/84791</a>
     *
     * @param request 提交一个智能封面任务请求 {@link SubmitSmartCoverJobRequest}
     * @return 提交一个智能封面任务返回结果 {@link SubmitSmartCoverJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitSmartCoverJobResult submitSmartCoverJob(SubmitSmartCoverJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitSmartCoverJobResult());
    }

    /**
     * <p>
     * 提交一个智能封面任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84791">https://cloud.tencent.com/document/product/460/84791</a>
     *
     * @param request 提交一个智能封面任务请求 {@link SubmitSmartCoverJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitSmartCoverJobAsync(SubmitSmartCoverJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitSmartCoverJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个截图任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84780">https://cloud.tencent.com/document/product/460/84780</a>
     *
     * @param request 提交一个截图任务请求 {@link SubmitSnapshotJobRequest}
     * @return 提交一个截图任务返回结果 {@link SubmitSnapshotJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitSnapshotJobResult submitSnapshotJob(SubmitSnapshotJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitSnapshotJobResult());
    }

    /**
     * <p>
     * 提交一个截图任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84780">https://cloud.tencent.com/document/product/460/84780</a>
     *
     * @param request 提交一个截图任务请求 {@link SubmitSnapshotJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitSnapshotJobAsync(SubmitSnapshotJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitSnapshotJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个获取媒体信息任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84776">https://cloud.tencent.com/document/product/460/84776</a>
     *
     * @param request 提交一个获取媒体信息任务请求 {@link SubmitMediaInfoJobRequest}
     * @return 提交一个获取媒体信息任务返回结果 {@link SubmitMediaInfoJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitMediaInfoJobResult submitMediaInfoJob(SubmitMediaInfoJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitMediaInfoJobResult());
    }

    /**
     * <p>
     * 提交一个获取媒体信息任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84776">https://cloud.tencent.com/document/product/460/84776</a>
     *
     * @param request 提交一个获取媒体信息任务请求 {@link SubmitMediaInfoJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitMediaInfoJobAsync(SubmitMediaInfoJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitMediaInfoJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个拼接任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84788">https://cloud.tencent.com/document/product/460/84788</a>
     *
     * @param request 提交一个拼接任务请求 {@link SubmitConcatJobRequest}
     * @return 提交一个拼接任务返回结果 {@link SubmitConcatJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitConcatJobResult submitConcatJob(SubmitConcatJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitConcatJobResult());
    }

    /**
     * <p>
     * 提交一个拼接任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84788">https://cloud.tencent.com/document/product/460/84788</a>
     *
     * @param request 提交一个拼接任务请求 {@link SubmitConcatJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitConcatJobAsync(SubmitConcatJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitConcatJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个添加数字水印任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84785">https://cloud.tencent.com/document/product/460/84785</a>
     *
     * @param request 提交一个添加数字水印任务请求 {@link SubmitDigitalWatermarkJobRequest}
     * @return 提交一个添加数字水印任务返回结果 {@link SubmitDigitalWatermarkJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitDigitalWatermarkJobResult submitDigitalWatermarkJob(SubmitDigitalWatermarkJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitDigitalWatermarkJobResult());
    }

    /**
     * <p>
     * 提交一个添加数字水印任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84785">https://cloud.tencent.com/document/product/460/84785</a>
     *
     * @param request 提交一个添加数字水印任务请求 {@link SubmitDigitalWatermarkJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitDigitalWatermarkJobAsync(SubmitDigitalWatermarkJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitDigitalWatermarkJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个精彩集锦任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84778">https://cloud.tencent.com/document/product/460/84778</a>
     *
     * @param request 提交一个精彩集锦任务请求 {@link SubmitVideoMontageJobRequest}
     * @return 提交一个精彩集锦任务返回结果 {@link SubmitVideoMontageJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitVideoMontageJobResult submitVideoMontageJob(SubmitVideoMontageJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitVideoMontageJobResult());
    }

    /**
     * <p>
     * 提交一个精彩集锦任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84778">https://cloud.tencent.com/document/product/460/84778</a>
     *
     * @param request 提交一个精彩集锦任务请求 {@link SubmitVideoMontageJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitVideoMontageJobAsync(SubmitVideoMontageJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitVideoMontageJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个人声分离任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84794">https://cloud.tencent.com/document/product/460/84794</a>
     *
     * @param request 提交一个人声分离任务请求 {@link SubmitVoiceSeparateJobRequest}
     * @return 提交一个人声分离任务返回结果 {@link SubmitVoiceSeparateJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitVoiceSeparateJobResult submitVoiceSeparateJob(SubmitVoiceSeparateJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitVoiceSeparateJobResult());
    }

    /**
     * <p>
     * 提交一个人声分离任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84794">https://cloud.tencent.com/document/product/460/84794</a>
     *
     * @param request 提交一个人声分离任务请求 {@link SubmitVoiceSeparateJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitVoiceSeparateJobAsync(SubmitVoiceSeparateJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitVoiceSeparateJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个图片处理任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84793">https://cloud.tencent.com/document/product/460/84793</a>
     *
     * @param request 提交一个图片处理任务请求 {@link SubmitPicProcessJobRequest}
     * @return 提交一个图片处理任务返回结果 {@link SubmitPicProcessJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitPicProcessJobResult submitPicProcessJob(SubmitPicProcessJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitPicProcessJobResult());
    }

    /**
     * <p>
     * 提交一个图片处理任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84793">https://cloud.tencent.com/document/product/460/84793</a>
     *
     * @param request 提交一个图片处理任务请求 {@link SubmitPicProcessJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitPicProcessJobAsync(SubmitPicProcessJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitPicProcessJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询工作流的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76857">https://cloud.tencent.com/document/product/460/76857</a>
     *
     * @param request 查询工作流请求 {@link GetWorkflowListRequest}
     * @return 查询工作流返回结果 {@link GetWorkflowListResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetWorkflowListResult getWorkflowList(GetWorkflowListRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetWorkflowListResult());
    }

    /**
     * <p>
     * 查询工作流的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76857">https://cloud.tencent.com/document/product/460/76857</a>
     *
     * @param request 查询工作流请求 {@link GetWorkflowListRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getWorkflowListAsync(GetWorkflowListRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetWorkflowListResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个转码任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84790">https://cloud.tencent.com/document/product/460/84790</a>
     *
     * @param request 提交一个转码任务请求 {@link SubmitTranscodeJobRequest}
     * @return 提交一个转码任务返回结果 {@link SubmitTranscodeJobResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SubmitTranscodeJobResult submitTranscodeJob(SubmitTranscodeJobRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SubmitTranscodeJobResult());
    }

    /**
     * <p>
     * 提交一个转码任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84790">https://cloud.tencent.com/document/product/460/84790</a>
     *
     * @param request 提交一个转码任务请求 {@link SubmitTranscodeJobRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void submitTranscodeJobAsync(SubmitTranscodeJobRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SubmitTranscodeJobResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口用于取消一个在进行中的直播审核任务，成功取消后将返回已终止任务的 JobID的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76262">https://cloud.tencent.com/document/product/460/76262</a>
     *
     * @param request 本接口用于取消一个在进行中的直播审核任务，成功取消后将返回已终止任务的 JobID请求 {@link CancelLiveVideoAuditRequest}
     * @return 本接口用于取消一个在进行中的直播审核任务，成功取消后将返回已终止任务的 JobID返回结果 {@link CancelLiveVideoAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CancelLiveVideoAuditResult cancelLiveVideoAudit(CancelLiveVideoAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CancelLiveVideoAuditResult());
    }

    /**
     * <p>
     * 本接口用于取消一个在进行中的直播审核任务，成功取消后将返回已终止任务的 JobID的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76262">https://cloud.tencent.com/document/product/460/76262</a>
     *
     * @param request 本接口用于取消一个在进行中的直播审核任务，成功取消后将返回已终止任务的 JobID请求 {@link CancelLiveVideoAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void cancelLiveVideoAuditAsync(CancelLiveVideoAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CancelLiveVideoAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口用于提交一个直播流审核任务。直播流审核功能为异步任务方式，您可以通过提交直播流审核任务审核您的直播流，然后通过查询直播流审核任务接口查询审核结果的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76261">https://cloud.tencent.com/document/product/460/76261</a>
     *
     * @param request 本接口用于提交一个直播流审核任务。直播流审核功能为异步任务方式，您可以通过提交直播流审核任务审核您的直播流，然后通过查询直播流审核任务接口查询审核结果请求 {@link PostLiveVideoAuditRequest}
     * @return 本接口用于提交一个直播流审核任务。直播流审核功能为异步任务方式，您可以通过提交直播流审核任务审核您的直播流，然后通过查询直播流审核任务接口查询审核结果返回结果 {@link PostLiveVideoAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostLiveVideoAuditResult postLiveVideoAudit(PostLiveVideoAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostLiveVideoAuditResult());
    }

    /**
     * <p>
     * 本接口用于提交一个直播流审核任务。直播流审核功能为异步任务方式，您可以通过提交直播流审核任务审核您的直播流，然后通过查询直播流审核任务接口查询审核结果的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76261">https://cloud.tencent.com/document/product/460/76261</a>
     *
     * @param request 本接口用于提交一个直播流审核任务。直播流审核功能为异步任务方式，您可以通过提交直播流审核任务审核您的直播流，然后通过查询直播流审核任务接口查询审核结果请求 {@link PostLiveVideoAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postLiveVideoAuditAsync(PostLiveVideoAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostLiveVideoAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口用于主动查询指定的直播审核任务结果。直播审核功能为异步任务方式，您可以通过提交直播审核任务审核您的直播流，然后通过查询直播审核任务接口轮询获取审核结果的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76265">https://cloud.tencent.com/document/product/460/76265</a>
     *
     * @param request 本接口用于主动查询指定的直播审核任务结果。直播审核功能为异步任务方式，您可以通过提交直播审核任务审核您的直播流，然后通过查询直播审核任务接口轮询获取审核结果请求 {@link GetLiveVideoAuditRequest}
     * @return 本接口用于主动查询指定的直播审核任务结果。直播审核功能为异步任务方式，您可以通过提交直播审核任务审核您的直播流，然后通过查询直播审核任务接口轮询获取审核结果返回结果 {@link GetLiveVideoAuditResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetLiveVideoAuditResult getLiveVideoAudit(GetLiveVideoAuditRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetLiveVideoAuditResult());
    }

    /**
     * <p>
     * 本接口用于主动查询指定的直播审核任务结果。直播审核功能为异步任务方式，您可以通过提交直播审核任务审核您的直播流，然后通过查询直播审核任务接口轮询获取审核结果的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/76265">https://cloud.tencent.com/document/product/460/76265</a>
     *
     * @param request 本接口用于主动查询指定的直播审核任务结果。直播审核功能为异步任务方式，您可以通过提交直播审核任务审核您的直播流，然后通过查询直播审核任务接口轮询获取审核结果请求 {@link GetLiveVideoAuditRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getLiveVideoAuditAsync(GetLiveVideoAuditRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetLiveVideoAuditResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 您可通过本接口反馈与预期不符的审核结果，例如色情图片被审核判定为正常或正常图片被判定为色情时可通过该接口直接反馈的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/75498">https://cloud.tencent.com/document/product/460/75498</a>
     *
     * @param request 您可通过本接口反馈与预期不符的审核结果，例如色情图片被审核判定为正常或正常图片被判定为色情时可通过该接口直接反馈请求 {@link PostImageAuditReportRequest}
     * @return 您可通过本接口反馈与预期不符的审核结果，例如色情图片被审核判定为正常或正常图片被判定为色情时可通过该接口直接反馈返回结果 {@link PostImageAuditReportResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostImageAuditReportResult postImageAuditReport(PostImageAuditReportRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostImageAuditReportResult());
    }

    /**
     * <p>
     * 您可通过本接口反馈与预期不符的审核结果，例如色情图片被审核判定为正常或正常图片被判定为色情时可通过该接口直接反馈的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/75498">https://cloud.tencent.com/document/product/460/75498</a>
     *
     * @param request 您可通过本接口反馈与预期不符的审核结果，例如色情图片被审核判定为正常或正常图片被判定为色情时可通过该接口直接反馈请求 {@link PostImageAuditReportRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postImageAuditReportAsync(PostImageAuditReportRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostImageAuditReportResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 您可通过本接口反馈与预期不符的审核结果，例如色情文本被审核判定为正常或正常文本被判定为色情时可通过该接口直接反馈的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/75497">https://cloud.tencent.com/document/product/460/75497</a>
     *
     * @param request 您可通过本接口反馈与预期不符的审核结果，例如色情文本被审核判定为正常或正常文本被判定为色情时可通过该接口直接反馈请求 {@link PostTextAuditReportRequest}
     * @return 您可通过本接口反馈与预期不符的审核结果，例如色情文本被审核判定为正常或正常文本被判定为色情时可通过该接口直接反馈返回结果 {@link PostTextAuditReportResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostTextAuditReportResult postTextAuditReport(PostTextAuditReportRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostTextAuditReportResult());
    }

    /**
     * <p>
     * 您可通过本接口反馈与预期不符的审核结果，例如色情文本被审核判定为正常或正常文本被判定为色情时可通过该接口直接反馈的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/75497">https://cloud.tencent.com/document/product/460/75497</a>
     *
     * @param request 您可通过本接口反馈与预期不符的审核结果，例如色情文本被审核判定为正常或正常文本被判定为色情时可通过该接口直接反馈请求 {@link PostTextAuditReportRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postTextAuditReportAsync(PostTextAuditReportRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostTextAuditReportResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询审核策略列表用于按条件查询符合的策略集合的同步方法.&nbsp;
     * <p>
     * @param request 查询审核策略列表用于按条件查询符合的策略集合请求 {@link GetStrategyListRequest}
     * @return 查询审核策略列表用于按条件查询符合的策略集合返回结果 {@link GetStrategyListResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetStrategyListResult getAuditStrategyList(GetStrategyListRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetStrategyListResult());
    }

    /**
     * <p>
     * 查询审核策略列表用于按条件查询符合的策略集合的异步方法.&nbsp;
     * <p>
     * @param request 查询审核策略列表用于按条件查询符合的策略集合请求 {@link GetStrategyListRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getAuditStrategyListAsync(GetStrategyListRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetStrategyListResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询审核策略详情的同步方法.&nbsp;
     * <p>
     * @param request 查询审核策略详情请求 {@link GetStrategyDetailRequest}
     * @return 查询审核策略详情返回结果 {@link GetStrategyDetailResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetStrategyDetailResult getAuditStrategyDetail(GetStrategyDetailRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetStrategyDetailResult());
    }

    /**
     * <p>
     * 查询审核策略详情的异步方法.&nbsp;
     * <p>
     * @param request 查询审核策略详情请求 {@link GetStrategyDetailRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getAuditStrategyDetailAsync(GetStrategyDetailRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetStrategyDetailResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建审核策略的同步方法.&nbsp;
     * <p>
     * @param request 创建审核策略请求 {@link CreateStrategyRequest}
     * @return 创建审核策略返回结果 {@link CreateStrategyResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CreateStrategyResult createAuditStrategy(CreateStrategyRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CreateStrategyResult());
    }

    /**
     * <p>
     * 创建审核策略的异步方法.&nbsp;
     * <p>
     * @param request 创建审核策略请求 {@link CreateStrategyRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void createAuditStrategyAsync(CreateStrategyRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CreateStrategyResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 修改审核策略的同步方法.&nbsp;
     * <p>
     * @param request 修改审核策略请求 {@link UpdateStrategyRequest}
     * @return 修改审核策略返回结果 {@link UpdateStrategyResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateStrategyResult updateAuditStrategy(UpdateStrategyRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateStrategyResult());
    }

    /**
     * <p>
     * 修改审核策略的异步方法.&nbsp;
     * <p>
     * @param request 修改审核策略请求 {@link UpdateStrategyRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateAuditStrategyAsync(UpdateStrategyRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateStrategyResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建自定义文本库的同步方法.&nbsp;
     * <p>
     * @param request 创建自定义文本库请求 {@link CreateAuditTextlibRequest}
     * @return 创建自定义文本库返回结果 {@link CreateAuditTextlibResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CreateAuditTextlibResult createAuditTextlib(CreateAuditTextlibRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CreateAuditTextlibResult());
    }

    /**
     * <p>
     * 创建自定义文本库的异步方法.&nbsp;
     * <p>
     * @param request 创建自定义文本库请求 {@link CreateAuditTextlibRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void createAuditTextlibAsync(CreateAuditTextlibRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CreateAuditTextlibResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 修改自定义文本库的同步方法.&nbsp;
     * <p>
     * @param request 修改自定义文本库请求 {@link UpdateAuditTextlibRequest}
     * @return 修改自定义文本库返回结果 {@link UpdateAuditTextlibResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateAuditTextlibResult updateAuditTextlib(UpdateAuditTextlibRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateAuditTextlibResult());
    }

    /**
     * <p>
     * 修改自定义文本库的异步方法.&nbsp;
     * <p>
     * @param request 修改自定义文本库请求 {@link UpdateAuditTextlibRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateAuditTextlibAsync(UpdateAuditTextlibRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateAuditTextlibResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除自定义文本库的同步方法.&nbsp;
     * <p>
     * @param request 删除自定义文本库请求 {@link DeleteAuditTextlibRequest}
     * @return 删除自定义文本库返回结果 {@link DeleteAuditTextlibResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DeleteAuditTextlibResult deleteAuditTextlib(DeleteAuditTextlibRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteAuditTextlibResult());
    }

    /**
     * <p>
     * 删除自定义文本库的异步方法.&nbsp;
     * <p>
     * @param request 删除自定义文本库请求 {@link DeleteAuditTextlibRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void deleteAuditTextlibAsync(DeleteAuditTextlibRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteAuditTextlibResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询自定义文本库的同步方法.&nbsp;
     * <p>
     * @param request 查询自定义文本库请求 {@link GetAuditTextlibListRequest}
     * @return 查询自定义文本库返回结果 {@link GetAuditTextlibListResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetAuditTextlibListResult getAuditTextlibList(GetAuditTextlibListRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetAuditTextlibListResult());
    }

    /**
     * <p>
     * 查询自定义文本库的异步方法.&nbsp;
     * <p>
     * @param request 查询自定义文本库请求 {@link GetAuditTextlibListRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getAuditTextlibListAsync(GetAuditTextlibListRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetAuditTextlibListResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 添加文本库关键词的同步方法.&nbsp;
     * <p>
     * @param request 添加文本库关键词请求 {@link AddAuditTextlibKeywordRequest}
     * @return 添加文本库关键词返回结果 {@link AddAuditTextlibKeywordResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public AddAuditTextlibKeywordResult addAuditTextlibKeyword(AddAuditTextlibKeywordRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AddAuditTextlibKeywordResult());
    }

    /**
     * <p>
     * 添加文本库关键词的异步方法.&nbsp;
     * <p>
     * @param request 添加文本库关键词请求 {@link AddAuditTextlibKeywordRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void addAuditTextlibKeywordAsync(AddAuditTextlibKeywordRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AddAuditTextlibKeywordResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询文本库关键词列表的同步方法.&nbsp;
     * <p>
     * @param request 查询文本库关键词列表请求 {@link GetAuditTextlibKeywordListRequest}
     * @return 查询文本库关键词列表返回结果 {@link GetAuditTextlibKeywordListResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetAuditTextlibKeywordListResult getAuditTextlibKeywordList(GetAuditTextlibKeywordListRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetAuditTextlibKeywordListResult());
    }

    /**
     * <p>
     * 查询文本库关键词列表的异步方法.&nbsp;
     * <p>
     * @param request 查询文本库关键词列表请求 {@link GetAuditTextlibKeywordListRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getAuditTextlibKeywordListAsync(GetAuditTextlibKeywordListRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetAuditTextlibKeywordListResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除文本库关键词的同步方法.&nbsp;
     * <p>
     * @param request 删除文本库关键词请求 {@link DeleteAuditTextlibKeywordRequest}
     * @return 删除文本库关键词返回结果 {@link DeleteAuditTextlibKeywordResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DeleteAuditTextlibKeywordResult deleteAuditTextlibKeyword(DeleteAuditTextlibKeywordRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteAuditTextlibKeywordResult());
    }

    /**
     * <p>
     * 删除文本库关键词的异步方法.&nbsp;
     * <p>
     * @param request 删除文本库关键词请求 {@link DeleteAuditTextlibKeywordRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void deleteAuditTextlibKeywordAsync(DeleteAuditTextlibKeywordRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteAuditTextlibKeywordResult(), cosXmlResultListener);
    }


    /**
     * <p>
     * 本接口用于查询已经开通AI 内容识别（异步）服务的存储桶的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/79594">https://cloud.tencent.com/document/product/460/79594</a>
     *
     * @param request 本接口用于查询已经开通AI 内容识别（异步）服务的存储桶请求 {@link GetAIBucketRequest}
     * @return 本接口用于查询已经开通AI 内容识别（异步）服务的存储桶返回结果 {@link GetAIBucketResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetAIBucketResult getAIBucket(GetAIBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetAIBucketResult());
    }

    /**
     * <p>
     * 本接口用于查询已经开通AI 内容识别（异步）服务的存储桶的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/79594">https://cloud.tencent.com/document/product/460/79594</a>
     *
     * @param request 本接口用于查询已经开通AI 内容识别（异步）服务的存储桶请求 {@link GetAIBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getAIBucketAsync(GetAIBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetAIBucketResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口用于更新AI 内容识别（异步）的队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/79397">https://cloud.tencent.com/document/product/460/79397</a>
     *
     * @param request 本接口用于更新AI 内容识别（异步）的队列请求 {@link UpdateAIQueueRequest}
     * @return 本接口用于更新AI 内容识别（异步）的队列返回结果 {@link UpdateAIQueueResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateAIQueueResult updateAIQueue(UpdateAIQueueRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateAIQueueResult());
    }

    /**
     * <p>
     * 本接口用于更新AI 内容识别（异步）的队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/79397">https://cloud.tencent.com/document/product/460/79397</a>
     *
     * @param request 本接口用于更新AI 内容识别（异步）的队列请求 {@link UpdateAIQueueRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateAIQueueAsync(UpdateAIQueueRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateAIQueueResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口用于关闭AI 内容识别（异步）服务并删除队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/95752">https://cloud.tencent.com/document/product/460/95752</a>
     *
     * @param request 本接口用于关闭AI 内容识别（异步）服务并删除队列请求 {@link CloseAIBucketRequest}
     * @return 本接口用于关闭AI 内容识别（异步）服务并删除队列返回结果 {@link CloseAIBucketResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CloseAIBucketResult closeAIBucket(CloseAIBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CloseAIBucketResult());
    }

    /**
     * <p>
     * 本接口用于关闭AI 内容识别（异步）服务并删除队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/95752">https://cloud.tencent.com/document/product/460/95752</a>
     *
     * @param request 本接口用于关闭AI 内容识别（异步）服务并删除队列请求 {@link CloseAIBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void closeAIBucketAsync(CloseAIBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CloseAIBucketResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 该接口用于开通 Bucket 搜图功能的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/63899">https://cloud.tencent.com/document/product/460/63899</a>
     *
     * @param request 该接口用于开通 Bucket 搜图功能请求 {@link ImageSearchBucketRequest}
     * @return 该接口用于开通 Bucket 搜图功能返回结果 {@link EmptyResponseResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public EmptyResponseResult imageSearchBucket(ImageSearchBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new EmptyResponseResult());
    }

    /**
     * <p>
     * 该接口用于开通 Bucket 搜图功能的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/63899">https://cloud.tencent.com/document/product/460/63899</a>
     *
     * @param request 该接口用于开通 Bucket 搜图功能请求 {@link ImageSearchBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void imageSearchBucketAsync(ImageSearchBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new EmptyResponseResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建视频目标检测模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84736">https://cloud.tencent.com/document/product/460/84736</a>
     *
     * @param request 创建视频目标检测模板请求 {@link PostVideoTargetTempleteRequest}
     * @return 创建视频目标检测模板返回结果 {@link PostVideoTargetTempleteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostVideoTargetTempleteResult postVideoTargetTemplete(PostVideoTargetTempleteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostVideoTargetTempleteResult());
    }

    /**
     * <p>
     * 创建视频目标检测模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84736">https://cloud.tencent.com/document/product/460/84736</a>
     *
     * @param request 创建视频目标检测模板请求 {@link PostVideoTargetTempleteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postVideoTargetTempleteAsync(PostVideoTargetTempleteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostVideoTargetTempleteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 更新视频目标检测模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84760">https://cloud.tencent.com/document/product/460/84760</a>
     *
     * @param request 更新视频目标检测模板请求 {@link UpdateVideoTargetTempleteRequest}
     * @return 更新视频目标检测模板返回结果 {@link UpdateVideoTargetTempleteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateVideoTargetTempleteResult updateVideoTargetTemplete(UpdateVideoTargetTempleteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateVideoTargetTempleteResult());
    }

    /**
     * <p>
     * 更新视频目标检测模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84760">https://cloud.tencent.com/document/product/460/84760</a>
     *
     * @param request 更新视频目标检测模板请求 {@link UpdateVideoTargetTempleteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateVideoTargetTempleteAsync(UpdateVideoTargetTempleteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateVideoTargetTempleteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个视频人像抠图任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/83973">https://cloud.tencent.com/document/product/460/83973</a>
     *
     * @param request 提交一个视频人像抠图任务请求 {@link PostSegmentVideoBodyRequest}
     * @return 提交一个视频人像抠图任务返回结果 {@link PostSegmentVideoBodyResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostSegmentVideoBodyResult postSegmentVideoBody(PostSegmentVideoBodyRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostSegmentVideoBodyResult());
    }

    /**
     * <p>
     * 提交一个视频人像抠图任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/83973">https://cloud.tencent.com/document/product/460/83973</a>
     *
     * @param request 提交一个视频人像抠图任务请求 {@link PostSegmentVideoBodyRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postSegmentVideoBodyAsync(PostSegmentVideoBodyRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostSegmentVideoBodyResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口用于开通智能语音服务并生成队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/95754">https://cloud.tencent.com/document/product/460/95754</a>
     *
     * @param request 本接口用于开通智能语音服务并生成队列请求 {@link OpenAsrBucketRequest}
     * @return 本接口用于开通智能语音服务并生成队列返回结果 {@link OpenAsrBucketResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public OpenAsrBucketResult openAsrBucket(OpenAsrBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new OpenAsrBucketResult());
    }

    /**
     * <p>
     * 本接口用于开通智能语音服务并生成队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/95754">https://cloud.tencent.com/document/product/460/95754</a>
     *
     * @param request 本接口用于开通智能语音服务并生成队列请求 {@link OpenAsrBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void openAsrBucketAsync(OpenAsrBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new OpenAsrBucketResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口用于关闭智能语音服务并删除队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/95755">https://cloud.tencent.com/document/product/460/95755</a>
     *
     * @param request 本接口用于关闭智能语音服务并删除队列请求 {@link CloseAsrBucketRequest}
     * @return 本接口用于关闭智能语音服务并删除队列返回结果 {@link CloseAsrBucketResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CloseAsrBucketResult closeAsrBucket(CloseAsrBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CloseAsrBucketResult());
    }

    /**
     * <p>
     * 本接口用于关闭智能语音服务并删除队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/95755">https://cloud.tencent.com/document/product/460/95755</a>
     *
     * @param request 本接口用于关闭智能语音服务并删除队列请求 {@link CloseAsrBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void closeAsrBucketAsync(CloseAsrBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CloseAsrBucketResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口用于查询AI 内容识别（异步）队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/79394">https://cloud.tencent.com/document/product/460/79394</a>
     *
     * @param request 本接口用于查询AI 内容识别（异步）队列请求 {@link GetAIQueueRequest}
     * @return 本接口用于查询AI 内容识别（异步）队列返回结果 {@link GetAIQueueResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public GetAIQueueResult getAIQueue(GetAIQueueRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetAIQueueResult());
    }

    /**
     * <p>
     * 本接口用于查询AI 内容识别（异步）队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/79394">https://cloud.tencent.com/document/product/460/79394</a>
     *
     * @param request 本接口用于查询AI 内容识别（异步）队列请求 {@link GetAIQueueRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void getAIQueueAsync(GetAIQueueRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetAIQueueResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口用于更新智能语音的队列的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46235">https://cloud.tencent.com/document/product/460/46235</a>
     *
     * @param request 本接口用于更新智能语音的队列请求 {@link UpdateAsrQueueRequest}
     * @return 本接口用于更新智能语音的队列返回结果 {@link UpdateAsrQueueResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateAsrQueueResult updateAsrQueue(UpdateAsrQueueRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateAsrQueueResult());
    }

    /**
     * <p>
     * 本接口用于更新智能语音的队列的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46235">https://cloud.tencent.com/document/product/460/46235</a>
     *
     * @param request 本接口用于更新智能语音的队列请求 {@link UpdateAsrQueueRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateAsrQueueAsync(UpdateAsrQueueRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateAsrQueueResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 更新人声分离转码模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84757">https://cloud.tencent.com/document/product/460/84757</a>
     *
     * @param request 更新人声分离转码模板请求 {@link UpdateVoiceSeparateTempleteRequest}
     * @return 更新人声分离转码模板返回结果 {@link UpdateVoiceSeparateTempleteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateVoiceSeparateTempleteResult updateVoiceSeparateTemplete(UpdateVoiceSeparateTempleteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateVoiceSeparateTempleteResult());
    }

    /**
     * <p>
     * 更新人声分离转码模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84757">https://cloud.tencent.com/document/product/460/84757</a>
     *
     * @param request 更新人声分离转码模板请求 {@link UpdateVoiceSeparateTempleteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateVoiceSeparateTempleteAsync(UpdateVoiceSeparateTempleteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateVoiceSeparateTempleteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个音频降噪任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84796">https://cloud.tencent.com/document/product/460/84796</a>
     *
     * @param request 提交一个音频降噪任务请求 {@link PostNoiseReductionRequest}
     * @return 提交一个音频降噪任务返回结果 {@link PostNoiseReductionResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostNoiseReductionResult postNoiseReduction(PostNoiseReductionRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostNoiseReductionResult());
    }

    /**
     * <p>
     * 提交一个音频降噪任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84796">https://cloud.tencent.com/document/product/460/84796</a>
     *
     * @param request 提交一个音频降噪任务请求 {@link PostNoiseReductionRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postNoiseReductionAsync(PostNoiseReductionRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostNoiseReductionResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建音频降噪模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/94315">https://cloud.tencent.com/document/product/460/94315</a>
     *
     * @param request 创建音频降噪模板请求 {@link PostNoiseReductionTempleteRequest}
     * @return 创建音频降噪模板返回结果 {@link PostNoiseReductionTempleteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostNoiseReductionTempleteResult postNoiseReductionTemplete(PostNoiseReductionTempleteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostNoiseReductionTempleteResult());
    }

    /**
     * <p>
     * 创建音频降噪模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/94315">https://cloud.tencent.com/document/product/460/94315</a>
     *
     * @param request 创建音频降噪模板请求 {@link PostNoiseReductionTempleteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postNoiseReductionTempleteAsync(PostNoiseReductionTempleteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostNoiseReductionTempleteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个语音合成任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84797">https://cloud.tencent.com/document/product/460/84797</a>
     *
     * @param request 提交一个语音合成任务请求 {@link PostVoiceSynthesisRequest}
     * @return 提交一个语音合成任务返回结果 {@link PostVoiceSynthesisResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostVoiceSynthesisResult postVoiceSynthesis(PostVoiceSynthesisRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostVoiceSynthesisResult());
    }

    /**
     * <p>
     * 提交一个语音合成任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84797">https://cloud.tencent.com/document/product/460/84797</a>
     *
     * @param request 提交一个语音合成任务请求 {@link PostVoiceSynthesisRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postVoiceSynthesisAsync(PostVoiceSynthesisRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostVoiceSynthesisResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建语音合成模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84499">https://cloud.tencent.com/document/product/460/84499</a>
     *
     * @param request 创建语音合成模板请求 {@link PostVoiceSynthesisTempleteRequest}
     * @return 创建语音合成模板返回结果 {@link PostVoiceSynthesisTempleteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostVoiceSynthesisTempleteResult postVoiceSynthesisTemplete(PostVoiceSynthesisTempleteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostVoiceSynthesisTempleteResult());
    }

    /**
     * <p>
     * 创建语音合成模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84499">https://cloud.tencent.com/document/product/460/84499</a>
     *
     * @param request 创建语音合成模板请求 {@link PostVoiceSynthesisTempleteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postVoiceSynthesisTempleteAsync(PostVoiceSynthesisTempleteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostVoiceSynthesisTempleteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 更新语音合成模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84758">https://cloud.tencent.com/document/product/460/84758</a>
     *
     * @param request 更新语音合成模板请求 {@link UpdateVoiceSynthesisTempleteRequest}
     * @return 更新语音合成模板返回结果 {@link UpdateVoiceSynthesisTempleteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateVoiceSynthesisTempleteResult updateVoiceSynthesisTemplete(UpdateVoiceSynthesisTempleteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateVoiceSynthesisTempleteResult());
    }

    /**
     * <p>
     * 更新语音合成模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84758">https://cloud.tencent.com/document/product/460/84758</a>
     *
     * @param request 更新语音合成模板请求 {@link UpdateVoiceSynthesisTempleteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateVoiceSynthesisTempleteAsync(UpdateVoiceSynthesisTempleteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateVoiceSynthesisTempleteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建语音识别模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84498">https://cloud.tencent.com/document/product/460/84498</a>
     *
     * @param request 创建语音识别模板请求 {@link PostSpeechRecognitionTempleteRequest}
     * @return 创建语音识别模板返回结果 {@link PostSpeechRecognitionTempleteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostSpeechRecognitionTempleteResult postSpeechRecognitionTemplete(PostSpeechRecognitionTempleteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostSpeechRecognitionTempleteResult());
    }

    /**
     * <p>
     * 创建语音识别模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84498">https://cloud.tencent.com/document/product/460/84498</a>
     *
     * @param request 创建语音识别模板请求 {@link PostSpeechRecognitionTempleteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postSpeechRecognitionTempleteAsync(PostSpeechRecognitionTempleteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostSpeechRecognitionTempleteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 更新语音识别模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84759">https://cloud.tencent.com/document/product/460/84759</a>
     *
     * @param request 更新语音识别模板请求 {@link UpdateSpeechRecognitionTempleteRequest}
     * @return 更新语音识别模板返回结果 {@link UpdateSpeechRecognitionTempleteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateSpeechRecognitionTempleteResult updateSpeechRecognitionTemplete(UpdateSpeechRecognitionTempleteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateSpeechRecognitionTempleteResult());
    }

    /**
     * <p>
     * 更新语音识别模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84759">https://cloud.tencent.com/document/product/460/84759</a>
     *
     * @param request 更新语音识别模板请求 {@link UpdateSpeechRecognitionTempleteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateSpeechRecognitionTempleteAsync(UpdateSpeechRecognitionTempleteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateSpeechRecognitionTempleteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个听歌识曲任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84795">https://cloud.tencent.com/document/product/460/84795</a>
     *
     * @param request 提交一个听歌识曲任务请求 {@link PostSoundHoundRequest}
     * @return 提交一个听歌识曲任务返回结果 {@link PostSoundHoundResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostSoundHoundResult postSoundHound(PostSoundHoundRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostSoundHoundResult());
    }

    /**
     * <p>
     * 提交一个听歌识曲任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84795">https://cloud.tencent.com/document/product/460/84795</a>
     *
     * @param request 提交一个听歌识曲任务请求 {@link PostSoundHoundRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postSoundHoundAsync(PostSoundHoundRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostSoundHoundResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 更新音频降噪模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/94394">https://cloud.tencent.com/document/product/460/94394</a>
     *
     * @param request 更新音频降噪模板请求 {@link UpdateNoiseReductionTempleteRequest}
     * @return 更新音频降噪模板返回结果 {@link UpdateNoiseReductionTempleteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateNoiseReductionTempleteResult updateNoiseReductionTemplete(UpdateNoiseReductionTempleteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateNoiseReductionTempleteResult());
    }

    /**
     * <p>
     * 更新音频降噪模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/94394">https://cloud.tencent.com/document/product/460/94394</a>
     *
     * @param request 更新音频降噪模板请求 {@link UpdateNoiseReductionTempleteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateNoiseReductionTempleteAsync(UpdateNoiseReductionTempleteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateNoiseReductionTempleteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个视频目标检测任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84801">https://cloud.tencent.com/document/product/460/84801</a>
     *
     * @param request 提交一个视频目标检测任务请求 {@link PostVideoTargetRecRequest}
     * @return 提交一个视频目标检测任务返回结果 {@link PostVideoTargetRecResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostVideoTargetRecResult postVideoTargetRec(PostVideoTargetRecRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostVideoTargetRecResult());
    }

    /**
     * <p>
     * 提交一个视频目标检测任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84801">https://cloud.tencent.com/document/product/460/84801</a>
     *
     * @param request 提交一个视频目标检测任务请求 {@link PostVideoTargetRecRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postVideoTargetRecAsync(PostVideoTargetRecRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostVideoTargetRecResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建图片处理模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84735">https://cloud.tencent.com/document/product/460/84735</a>
     *
     * @param request 创建图片处理模板请求 {@link TemplatePicProcessRequest}
     * @return 创建图片处理模板返回结果 {@link TemplatePicProcessResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TemplatePicProcessResult templatePicProcess(TemplatePicProcessRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TemplatePicProcessResult());
    }

    /**
     * <p>
     * 创建图片处理模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84735">https://cloud.tencent.com/document/product/460/84735</a>
     *
     * @param request 创建图片处理模板请求 {@link TemplatePicProcessRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void templatePicProcessAsync(TemplatePicProcessRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TemplatePicProcessResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建视频转动图模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84729">https://cloud.tencent.com/document/product/460/84729</a>
     *
     * @param request 创建视频转动图模板请求 {@link TemplateAnimationRequest}
     * @return 创建视频转动图模板返回结果 {@link TemplateAnimationResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TemplateAnimationResult templateAnimation(TemplateAnimationRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TemplateAnimationResult());
    }

    /**
     * <p>
     * 创建视频转动图模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84729">https://cloud.tencent.com/document/product/460/84729</a>
     *
     * @param request 创建视频转动图模板请求 {@link TemplateAnimationRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void templateAnimationAsync(TemplateAnimationRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TemplateAnimationResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建视频截图模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84727">https://cloud.tencent.com/document/product/460/84727</a>
     *
     * @param request 创建视频截图模板请求 {@link TemplateSnapshotRequest}
     * @return 创建视频截图模板返回结果 {@link TemplateSnapshotResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TemplateSnapshotResult templateSnapshot(TemplateSnapshotRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TemplateSnapshotResult());
    }

    /**
     * <p>
     * 创建视频截图模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84727">https://cloud.tencent.com/document/product/460/84727</a>
     *
     * @param request 创建视频截图模板请求 {@link TemplateSnapshotRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void templateSnapshotAsync(TemplateSnapshotRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TemplateSnapshotResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建明水印模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84725">https://cloud.tencent.com/document/product/460/84725</a>
     *
     * @param request 创建明水印模板请求 {@link TemplateWatermarkRequest}
     * @return 创建明水印模板返回结果 {@link TemplateWatermarkResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TemplateWatermarkResult templateWatermark(TemplateWatermarkRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TemplateWatermarkResult());
    }

    /**
     * <p>
     * 创建明水印模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84725">https://cloud.tencent.com/document/product/460/84725</a>
     *
     * @param request 创建明水印模板请求 {@link TemplateWatermarkRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void templateWatermarkAsync(TemplateWatermarkRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TemplateWatermarkResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建音视频拼接模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84730">https://cloud.tencent.com/document/product/460/84730</a>
     *
     * @param request 创建音视频拼接模板请求 {@link TemplateConcatRequest}
     * @return 创建音视频拼接模板返回结果 {@link TemplateConcatResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TemplateConcatResult templateConcat(TemplateConcatRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TemplateConcatResult());
    }

    /**
     * <p>
     * 创建音视频拼接模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84730">https://cloud.tencent.com/document/product/460/84730</a>
     *
     * @param request 创建音视频拼接模板请求 {@link TemplateConcatRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void templateConcatAsync(TemplateConcatRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TemplateConcatResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建人声分离模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84500">https://cloud.tencent.com/document/product/460/84500</a>
     *
     * @param request 创建人声分离模板请求 {@link TemplateVoiceSeparateRequest}
     * @return 创建人声分离模板返回结果 {@link TemplateVoiceSeparateResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TemplateVoiceSeparateResult templateVoiceSeparate(TemplateVoiceSeparateRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TemplateVoiceSeparateResult());
    }

    /**
     * <p>
     * 创建人声分离模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84500">https://cloud.tencent.com/document/product/460/84500</a>
     *
     * @param request 创建人声分离模板请求 {@link TemplateVoiceSeparateRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void templateVoiceSeparateAsync(TemplateVoiceSeparateRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TemplateVoiceSeparateResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建音视频转码模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84733">https://cloud.tencent.com/document/product/460/84733</a>
     *
     * @param request 创建音视频转码模板请求 {@link TemplateTranscodeRequest}
     * @return 创建音视频转码模板返回结果 {@link TemplateTranscodeResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TemplateTranscodeResult templateTranscode(TemplateTranscodeRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TemplateTranscodeResult());
    }

    /**
     * <p>
     * 创建音视频转码模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84733">https://cloud.tencent.com/document/product/460/84733</a>
     *
     * @param request 创建音视频转码模板请求 {@link TemplateTranscodeRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void templateTranscodeAsync(TemplateTranscodeRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TemplateTranscodeResult(), cosXmlResultListener);
    }


    /**
     * <p>
     * 创建智能封面模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84734">https://cloud.tencent.com/document/product/460/84734</a>
     *
     * @param request 创建智能封面模板请求 {@link TemplateSmartCoverRequest}
     * @return 创建智能封面模板返回结果 {@link TemplateSmartCoverResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TemplateSmartCoverResult templateSmartCover(TemplateSmartCoverRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TemplateSmartCoverResult());
    }

    /**
     * <p>
     * 创建智能封面模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84734">https://cloud.tencent.com/document/product/460/84734</a>
     *
     * @param request 创建智能封面模板请求 {@link TemplateSmartCoverRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void templateSmartCoverAsync(TemplateSmartCoverRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TemplateSmartCoverResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建精彩集锦模板的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84724">https://cloud.tencent.com/document/product/460/84724</a>
     *
     * @param request 创建精彩集锦模板请求 {@link TemplateVideoMontageRequest}
     * @return 创建精彩集锦模板返回结果 {@link TemplateVideoMontageResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public TemplateVideoMontageResult templateVideoMontage(TemplateVideoMontageRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new TemplateVideoMontageResult());
    }

    /**
     * <p>
     * 创建精彩集锦模板的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84724">https://cloud.tencent.com/document/product/460/84724</a>
     *
     * @param request 创建精彩集锦模板请求 {@link TemplateVideoMontageRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void templateVideoMontageAsync(TemplateVideoMontageRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new TemplateVideoMontageResult(), cosXmlResultListener);
    }



    /**
     * <p>
     * 本接口用于创建一个数据集（Dataset），数据集是由文件元数据构成的集合，用于存储和管理元数据。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106020">https://cloud.tencent.com/document/product/460/106020</a>
     *
     * @param request 本接口用于创建一个数据集（Dataset），数据集是由文件元数据构成的集合，用于存储和管理元数据。请求 {@link CreateDatasetRequest}
     * @return 本接口用于创建一个数据集（Dataset），数据集是由文件元数据构成的集合，用于存储和管理元数据。返回结果 {@link CreateDatasetResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CreateDatasetResult createDataset(CreateDatasetRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CreateDatasetResult());
    }

    /**
     * <p>
     * 本接口用于创建一个数据集（Dataset），数据集是由文件元数据构成的集合，用于存储和管理元数据。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106020">https://cloud.tencent.com/document/product/460/106020</a>
     *
     * @param request 本接口用于创建一个数据集（Dataset），数据集是由文件元数据构成的集合，用于存储和管理元数据。请求 {@link CreateDatasetRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void createDatasetAsync(CreateDatasetRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CreateDatasetResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本文档介绍创建数据集（Dataset）和对象存储（COS）Bucket 的绑定关系，绑定后将使用创建数据集时所指定算子对文件进行处理。
     绑定关系创建后，将对 COS 中新增的文件进行准实时的增量追踪扫描，使用创建数据集时所指定算子对文件进行处理，抽取文件元数据信息进行索引。通过此方式为文件建立索引后，您可以使用元数据查询API对元数据进行查询、管理和统计。
     的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106159">https://cloud.tencent.com/document/product/460/106159</a>
     *
     * @param request 本文档介绍创建数据集（Dataset）和对象存储（COS）Bucket 的绑定关系，绑定后将使用创建数据集时所指定算子对文件进行处理。
    绑定关系创建后，将对 COS 中新增的文件进行准实时的增量追踪扫描，使用创建数据集时所指定算子对文件进行处理，抽取文件元数据信息进行索引。通过此方式为文件建立索引后，您可以使用元数据查询API对元数据进行查询、管理和统计。
    请求 {@link CreateDatasetBindingRequest}
     * @return 本文档介绍创建数据集（Dataset）和对象存储（COS）Bucket 的绑定关系，绑定后将使用创建数据集时所指定算子对文件进行处理。
    绑定关系创建后，将对 COS 中新增的文件进行准实时的增量追踪扫描，使用创建数据集时所指定算子对文件进行处理，抽取文件元数据信息进行索引。通过此方式为文件建立索引后，您可以使用元数据查询API对元数据进行查询、管理和统计。
    返回结果 {@link CreateDatasetBindingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CreateDatasetBindingResult createDatasetBinding(CreateDatasetBindingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CreateDatasetBindingResult());
    }

    /**
     * <p>
     * 本文档介绍创建数据集（Dataset）和对象存储（COS）Bucket 的绑定关系，绑定后将使用创建数据集时所指定算子对文件进行处理。
     绑定关系创建后，将对 COS 中新增的文件进行准实时的增量追踪扫描，使用创建数据集时所指定算子对文件进行处理，抽取文件元数据信息进行索引。通过此方式为文件建立索引后，您可以使用元数据查询API对元数据进行查询、管理和统计。
     的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106159">https://cloud.tencent.com/document/product/460/106159</a>
     *
     * @param request 本文档介绍创建数据集（Dataset）和对象存储（COS）Bucket 的绑定关系，绑定后将使用创建数据集时所指定算子对文件进行处理。
    绑定关系创建后，将对 COS 中新增的文件进行准实时的增量追踪扫描，使用创建数据集时所指定算子对文件进行处理，抽取文件元数据信息进行索引。通过此方式为文件建立索引后，您可以使用元数据查询API对元数据进行查询、管理和统计。
    请求 {@link CreateDatasetBindingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void createDatasetBindingAsync(CreateDatasetBindingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CreateDatasetBindingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提取一个 COS 文件的元数据，在数据集中建立索引。会根据数据集中的算子提取不同的元数据建立索引，也支持建立自定义的元数据索引。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106022">https://cloud.tencent.com/document/product/460/106022</a>
     *
     * @param request 提取一个 COS 文件的元数据，在数据集中建立索引。会根据数据集中的算子提取不同的元数据建立索引，也支持建立自定义的元数据索引。请求 {@link CreateFileMetaIndexRequest}
     * @return 提取一个 COS 文件的元数据，在数据集中建立索引。会根据数据集中的算子提取不同的元数据建立索引，也支持建立自定义的元数据索引。返回结果 {@link CreateFileMetaIndexResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public CreateFileMetaIndexResult createFileMetaIndex(CreateFileMetaIndexRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CreateFileMetaIndexResult());
    }

    /**
     * <p>
     * 提取一个 COS 文件的元数据，在数据集中建立索引。会根据数据集中的算子提取不同的元数据建立索引，也支持建立自定义的元数据索引。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106022">https://cloud.tencent.com/document/product/460/106022</a>
     *
     * @param request 提取一个 COS 文件的元数据，在数据集中建立索引。会根据数据集中的算子提取不同的元数据建立索引，也支持建立自定义的元数据索引。请求 {@link CreateFileMetaIndexRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void createFileMetaIndexAsync(CreateFileMetaIndexRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CreateFileMetaIndexResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 从数据集中搜索与指定图片最相似的前N张图片并返回人脸坐标可对数据集内文件进行一个或多个人员的人脸识别。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106166">https://cloud.tencent.com/document/product/460/106166</a>
     *
     * @param request 从数据集中搜索与指定图片最相似的前N张图片并返回人脸坐标可对数据集内文件进行一个或多个人员的人脸识别。请求 {@link DatasetFaceSearchRequest}
     * @return 从数据集中搜索与指定图片最相似的前N张图片并返回人脸坐标可对数据集内文件进行一个或多个人员的人脸识别。返回结果 {@link DatasetFaceSearchResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DatasetFaceSearchResult datasetFaceSearch(DatasetFaceSearchRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DatasetFaceSearchResult());
    }

    /**
     * <p>
     * 从数据集中搜索与指定图片最相似的前N张图片并返回人脸坐标可对数据集内文件进行一个或多个人员的人脸识别。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106166">https://cloud.tencent.com/document/product/460/106166</a>
     *
     * @param request 从数据集中搜索与指定图片最相似的前N张图片并返回人脸坐标可对数据集内文件进行一个或多个人员的人脸识别。请求 {@link DatasetFaceSearchRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void datasetFaceSearchAsync(DatasetFaceSearchRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DatasetFaceSearchResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 可以根据已提取的文件元数据（包含文件名、标签、路径、自定义标签、文本等字段）查询和统计数据集内文件，支持逻辑关系表达方式。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106375">https://cloud.tencent.com/document/product/460/106375</a>
     *
     * @param request 可以根据已提取的文件元数据（包含文件名、标签、路径、自定义标签、文本等字段）查询和统计数据集内文件，支持逻辑关系表达方式。请求 {@link DatasetSimpleQueryRequest}
     * @return 可以根据已提取的文件元数据（包含文件名、标签、路径、自定义标签、文本等字段）查询和统计数据集内文件，支持逻辑关系表达方式。返回结果 {@link DatasetSimpleQueryResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DatasetSimpleQueryResult datasetSimpleQuery(DatasetSimpleQueryRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DatasetSimpleQueryResult());
    }

    /**
     * <p>
     * 可以根据已提取的文件元数据（包含文件名、标签、路径、自定义标签、文本等字段）查询和统计数据集内文件，支持逻辑关系表达方式。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106375">https://cloud.tencent.com/document/product/460/106375</a>
     *
     * @param request 可以根据已提取的文件元数据（包含文件名、标签、路径、自定义标签、文本等字段）查询和统计数据集内文件，支持逻辑关系表达方式。请求 {@link DatasetSimpleQueryRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void datasetSimpleQueryAsync(DatasetSimpleQueryRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DatasetSimpleQueryResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除一个数据集（Dataset）。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106157">https://cloud.tencent.com/document/product/460/106157</a>
     *
     * @param request 删除一个数据集（Dataset）。请求 {@link DeleteDatasetRequest}
     * @return 删除一个数据集（Dataset）。返回结果 {@link DeleteDatasetResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DeleteDatasetResult deleteDataset(DeleteDatasetRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteDatasetResult());
    }

    /**
     * <p>
     * 删除一个数据集（Dataset）。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106157">https://cloud.tencent.com/document/product/460/106157</a>
     *
     * @param request 删除一个数据集（Dataset）。请求 {@link DeleteDatasetRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void deleteDatasetAsync(DeleteDatasetRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteDatasetResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 解绑数据集和对象存储（COS）Bucket ，解绑会导致 COS Bucket新增的变更不会同步到数据集，请谨慎操作。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106160">https://cloud.tencent.com/document/product/460/106160</a>
     *
     * @param request 解绑数据集和对象存储（COS）Bucket ，解绑会导致 COS Bucket新增的变更不会同步到数据集，请谨慎操作。请求 {@link DeleteDatasetBindingRequest}
     * @return 解绑数据集和对象存储（COS）Bucket ，解绑会导致 COS Bucket新增的变更不会同步到数据集，请谨慎操作。返回结果 {@link DeleteDatasetBindingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DeleteDatasetBindingResult deleteDatasetBinding(DeleteDatasetBindingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteDatasetBindingResult());
    }

    /**
     * <p>
     * 解绑数据集和对象存储（COS）Bucket ，解绑会导致 COS Bucket新增的变更不会同步到数据集，请谨慎操作。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106160">https://cloud.tencent.com/document/product/460/106160</a>
     *
     * @param request 解绑数据集和对象存储（COS）Bucket ，解绑会导致 COS Bucket新增的变更不会同步到数据集，请谨慎操作。请求 {@link DeleteDatasetBindingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void deleteDatasetBindingAsync(DeleteDatasetBindingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteDatasetBindingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 从数据集内删除一个文件的元信息。无论该文件的元信息是否在数据集内存在，均会返回删除成功。

     的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106163">https://cloud.tencent.com/document/product/460/106163</a>
     *
     * @param request 从数据集内删除一个文件的元信息。无论该文件的元信息是否在数据集内存在，均会返回删除成功。

    请求 {@link DeleteFileMetaIndexRequest}
     * @return 从数据集内删除一个文件的元信息。无论该文件的元信息是否在数据集内存在，均会返回删除成功。

    返回结果 {@link DeleteFileMetaIndexResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DeleteFileMetaIndexResult deleteFileMetaIndex(DeleteFileMetaIndexRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteFileMetaIndexResult());
    }

    /**
     * <p>
     * 从数据集内删除一个文件的元信息。无论该文件的元信息是否在数据集内存在，均会返回删除成功。

     的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106163">https://cloud.tencent.com/document/product/460/106163</a>
     *
     * @param request 从数据集内删除一个文件的元信息。无论该文件的元信息是否在数据集内存在，均会返回删除成功。

    请求 {@link DeleteFileMetaIndexRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void deleteFileMetaIndexAsync(DeleteFileMetaIndexRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteFileMetaIndexResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询一个数据集（Dataset）信息。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106155">https://cloud.tencent.com/document/product/460/106155</a>
     *
     * @param request 查询一个数据集（Dataset）信息。请求 {@link DescribeDatasetRequest}
     * @return 查询一个数据集（Dataset）信息。返回结果 {@link DescribeDatasetResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeDatasetResult describeDataset(DescribeDatasetRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeDatasetResult());
    }

    /**
     * <p>
     * 查询一个数据集（Dataset）信息。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106155">https://cloud.tencent.com/document/product/460/106155</a>
     *
     * @param request 查询一个数据集（Dataset）信息。请求 {@link DescribeDatasetRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeDatasetAsync(DescribeDatasetRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeDatasetResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询数据集和对象存储（COS）Bucket 绑定关系列表。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106485">https://cloud.tencent.com/document/product/460/106485</a>
     *
     * @param request 查询数据集和对象存储（COS）Bucket 绑定关系列表。请求 {@link DescribeDatasetBindingRequest}
     * @return 查询数据集和对象存储（COS）Bucket 绑定关系列表。返回结果 {@link DescribeDatasetBindingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeDatasetBindingResult describeDatasetBinding(DescribeDatasetBindingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeDatasetBindingResult());
    }

    /**
     * <p>
     * 查询数据集和对象存储（COS）Bucket 绑定关系列表。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106485">https://cloud.tencent.com/document/product/460/106485</a>
     *
     * @param request 查询数据集和对象存储（COS）Bucket 绑定关系列表。请求 {@link DescribeDatasetBindingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeDatasetBindingAsync(DescribeDatasetBindingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeDatasetBindingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询数据集和对象存储（COS）Bucket 绑定关系列表。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106161">https://cloud.tencent.com/document/product/460/106161</a>
     *
     * @param request 查询数据集和对象存储（COS）Bucket 绑定关系列表。请求 {@link DescribeDatasetBindingsRequest}
     * @return 查询数据集和对象存储（COS）Bucket 绑定关系列表。返回结果 {@link DescribeDatasetBindingsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeDatasetBindingsResult describeDatasetBindings(DescribeDatasetBindingsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeDatasetBindingsResult());
    }

    /**
     * <p>
     * 查询数据集和对象存储（COS）Bucket 绑定关系列表。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106161">https://cloud.tencent.com/document/product/460/106161</a>
     *
     * @param request 查询数据集和对象存储（COS）Bucket 绑定关系列表。请求 {@link DescribeDatasetBindingsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeDatasetBindingsAsync(DescribeDatasetBindingsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeDatasetBindingsResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取数据集（Dataset）列表。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106158">https://cloud.tencent.com/document/product/460/106158</a>
     *
     * @param request 获取数据集（Dataset）列表。请求 {@link DescribeDatasetsRequest}
     * @return 获取数据集（Dataset）列表。返回结果 {@link DescribeDatasetsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeDatasetsResult describeDatasets(DescribeDatasetsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeDatasetsResult());
    }

    /**
     * <p>
     * 获取数据集（Dataset）列表。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106158">https://cloud.tencent.com/document/product/460/106158</a>
     *
     * @param request 获取数据集（Dataset）列表。请求 {@link DescribeDatasetsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeDatasetsAsync(DescribeDatasetsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeDatasetsResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取数据集内已完成索引的一个文件的元数据。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106164">https://cloud.tencent.com/document/product/460/106164</a>
     *
     * @param request 获取数据集内已完成索引的一个文件的元数据。请求 {@link DescribeFileMetaIndexRequest}
     * @return 获取数据集内已完成索引的一个文件的元数据。返回结果 {@link DescribeFileMetaIndexResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public DescribeFileMetaIndexResult describeFileMetaIndex(DescribeFileMetaIndexRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DescribeFileMetaIndexResult());
    }

    /**
     * <p>
     * 获取数据集内已完成索引的一个文件的元数据。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106164">https://cloud.tencent.com/document/product/460/106164</a>
     *
     * @param request 获取数据集内已完成索引的一个文件的元数据。请求 {@link DescribeFileMetaIndexRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void describeFileMetaIndexAsync(DescribeFileMetaIndexRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DescribeFileMetaIndexResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 可通过输入自然语言或图片，基于语义对数据集内文件进行图像检索。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106376">https://cloud.tencent.com/document/product/460/106376</a>
     *
     * @param request 可通过输入自然语言或图片，基于语义对数据集内文件进行图像检索。请求 {@link SearchImageRequest}
     * @return 可通过输入自然语言或图片，基于语义对数据集内文件进行图像检索。返回结果 {@link SearchImageResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public SearchImageResult searchImage(SearchImageRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SearchImageResult());
    }

    /**
     * <p>
     * 可通过输入自然语言或图片，基于语义对数据集内文件进行图像检索。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106376">https://cloud.tencent.com/document/product/460/106376</a>
     *
     * @param request 可通过输入自然语言或图片，基于语义对数据集内文件进行图像检索。请求 {@link SearchImageRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void searchImageAsync(SearchImageRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SearchImageResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 更新一个数据集（Dataset）信息。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106156">https://cloud.tencent.com/document/product/460/106156</a>
     *
     * @param request 更新一个数据集（Dataset）信息。请求 {@link UpdateDatasetRequest}
     * @return 更新一个数据集（Dataset）信息。返回结果 {@link UpdateDatasetResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateDatasetResult updateDataset(UpdateDatasetRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateDatasetResult());
    }

    /**
     * <p>
     * 更新一个数据集（Dataset）信息。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106156">https://cloud.tencent.com/document/product/460/106156</a>
     *
     * @param request 更新一个数据集（Dataset）信息。请求 {@link UpdateDatasetRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateDatasetAsync(UpdateDatasetRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateDatasetResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 更新数据集内已索引的一个文件的部分元数据。

     并非所有的元数据都允许您自定义更新，在您发起更新请求时需要填写数据集，默认会根据该数据集的算子进行元数据重新提取并更新已存在的索引，此外您也可以更新部分自定义的元数据索引，如CustomTags、CustomId等字段，具体请参考请求参数一节。的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106162">https://cloud.tencent.com/document/product/460/106162</a>
     *
     * @param request 更新数据集内已索引的一个文件的部分元数据。

    并非所有的元数据都允许您自定义更新，在您发起更新请求时需要填写数据集，默认会根据该数据集的算子进行元数据重新提取并更新已存在的索引，此外您也可以更新部分自定义的元数据索引，如CustomTags、CustomId等字段，具体请参考请求参数一节。请求 {@link UpdateFileMetaIndexRequest}
     * @return 更新数据集内已索引的一个文件的部分元数据。

    并非所有的元数据都允许您自定义更新，在您发起更新请求时需要填写数据集，默认会根据该数据集的算子进行元数据重新提取并更新已存在的索引，此外您也可以更新部分自定义的元数据索引，如CustomTags、CustomId等字段，具体请参考请求参数一节。返回结果 {@link UpdateFileMetaIndexResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public UpdateFileMetaIndexResult updateFileMetaIndex(UpdateFileMetaIndexRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UpdateFileMetaIndexResult());
    }

    /**
     * <p>
     * 更新数据集内已索引的一个文件的部分元数据。

     并非所有的元数据都允许您自定义更新，在您发起更新请求时需要填写数据集，默认会根据该数据集的算子进行元数据重新提取并更新已存在的索引，此外您也可以更新部分自定义的元数据索引，如CustomTags、CustomId等字段，具体请参考请求参数一节。的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/106162">https://cloud.tencent.com/document/product/460/106162</a>
     *
     * @param request 更新数据集内已索引的一个文件的部分元数据。

    并非所有的元数据都允许您自定义更新，在您发起更新请求时需要填写数据集，默认会根据该数据集的算子进行元数据重新提取并更新已存在的索引，此外您也可以更新部分自定义的元数据索引，如CustomTags、CustomId等字段，具体请参考请求参数一节。请求 {@link UpdateFileMetaIndexRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void updateFileMetaIndexAsync(UpdateFileMetaIndexRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UpdateFileMetaIndexResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个音乐评分任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/96095">https://cloud.tencent.com/document/product/460/96095</a>
     *
     * @param request 提交一个音乐评分任务请求 {@link VocalScoreRequest}
     * @return 提交一个音乐评分任务返回结果 {@link VocalScoreResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public VocalScoreResult vocalScore(VocalScoreRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new VocalScoreResult());
    }

    /**
     * <p>
     * 提交一个音乐评分任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/96095">https://cloud.tencent.com/document/product/460/96095</a>
     *
     * @param request 提交一个音乐评分任务请求 {@link VocalScoreRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void vocalScoreAsync(VocalScoreRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new VocalScoreResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 提交一个翻译任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84799">https://cloud.tencent.com/document/product/460/84799</a>
     *
     * @param request 提交一个翻译任务请求 {@link PostTranslationRequest}
     * @return 提交一个翻译任务返回结果 {@link PostTranslationResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public PostTranslationResult postTranslation(PostTranslationRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PostTranslationResult());
    }

    /**
     * <p>
     * 提交一个翻译任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/84799">https://cloud.tencent.com/document/product/460/84799</a>
     *
     * @param request 提交一个翻译任务请求 {@link PostTranslationRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    public void postTranslationAsync(PostTranslationRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PostTranslationResult(), cosXmlResultListener);
    }
}
