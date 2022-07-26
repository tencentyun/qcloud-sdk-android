package com.tencent.cos.xml;

import android.content.Context;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
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
import com.tencent.cos.xml.model.ci.asr.CreateSpeechJobsRequest;
import com.tencent.cos.xml.model.ci.asr.CreateSpeechJobsResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobsRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechJobsResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechBucketsRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechBucketsResult;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechQueuesRequest;
import com.tencent.cos.xml.model.ci.asr.DescribeSpeechQueuesResult;
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

    private static class CISigner extends COSXmlSigner {
        @Override
        protected String getSessionTokenKey() {
            return "x-ci-security-token";
        }
    }

    public CIService(Context context, CosXmlServiceConfig configuration, QCloudCredentialProvider qCloudCredentialProvider) {
        super(context, configuration, qCloudCredentialProvider);
        this.signerType = "CISigner";
        SignerFactory.registerSigner(signerType, new CISigner());
    }

    public GetBucketDPStateResult getBucketDocumentPreviewState(GetBucketDPStateRequest request)
            throws CosXmlClientException, CosXmlServiceException{
        return execute(request, new GetBucketDPStateResult());
    }

    public void getBucketDocumentPreviewStateAsync(GetBucketDPStateRequest request, CosXmlResultListener listener) {
        schedule(request, new GetBucketDPStateResult(), listener);
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

    public void deleteBucketDocumentPreviewStateAsync(DeleteBucketDPStateRequest request, CosXmlResultListener listener)
            throws CosXmlClientException, CosXmlServiceException {
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46228">https://cloud.tencent.com/document/product/460/46228.</a>
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46228">https://cloud.tencent.com/document/product/460/46228.</a>
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46229">https://cloud.tencent.com/document/product/460/46229.</a>
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46229">https://cloud.tencent.com/document/product/460/46229.</a>
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46230">https://cloud.tencent.com/document/product/460/46230.</a>
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/460/46230">https://cloud.tencent.com/document/product/460/46230.</a>
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
}
