/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.cos.xml.common.Constants;
import com.tencent.cos.xml.common.MetaDataDirective;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlBooleanListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.EmptyResponseResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketCORSResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketDomainRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketDomainResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketInventoryRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketInventoryResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketPolicyRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketPolicyResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketReplicationRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketReplicationResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketTaggingRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketTaggingResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketWebsiteRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketWebsiteResult;
import com.tencent.cos.xml.model.bucket.GetBucketACLRequest;
import com.tencent.cos.xml.model.bucket.GetBucketACLResult;
import com.tencent.cos.xml.model.bucket.GetBucketAccelerateRequest;
import com.tencent.cos.xml.model.bucket.GetBucketAccelerateResult;
import com.tencent.cos.xml.model.bucket.GetBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.GetBucketCORSResult;
import com.tencent.cos.xml.model.bucket.GetBucketDomainRequest;
import com.tencent.cos.xml.model.bucket.GetBucketDomainResult;
import com.tencent.cos.xml.model.bucket.GetBucketIntelligentTieringRequest;
import com.tencent.cos.xml.model.bucket.GetBucketIntelligentTieringResult;
import com.tencent.cos.xml.model.bucket.GetBucketInventoryRequest;
import com.tencent.cos.xml.model.bucket.GetBucketInventoryResult;
import com.tencent.cos.xml.model.bucket.GetBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.GetBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.GetBucketLocationRequest;
import com.tencent.cos.xml.model.bucket.GetBucketLocationResult;
import com.tencent.cos.xml.model.bucket.GetBucketLoggingRequest;
import com.tencent.cos.xml.model.bucket.GetBucketLoggingResult;
import com.tencent.cos.xml.model.bucket.GetBucketObjectVersionsRequest;
import com.tencent.cos.xml.model.bucket.GetBucketObjectVersionsResult;
import com.tencent.cos.xml.model.bucket.GetBucketPolicyRequest;
import com.tencent.cos.xml.model.bucket.GetBucketPolicyResult;
import com.tencent.cos.xml.model.bucket.GetBucketRefererRequest;
import com.tencent.cos.xml.model.bucket.GetBucketRefererResult;
import com.tencent.cos.xml.model.bucket.GetBucketReplicationRequest;
import com.tencent.cos.xml.model.bucket.GetBucketReplicationResult;
import com.tencent.cos.xml.model.bucket.GetBucketRequest;
import com.tencent.cos.xml.model.bucket.GetBucketResult;
import com.tencent.cos.xml.model.bucket.GetBucketTaggingRequest;
import com.tencent.cos.xml.model.bucket.GetBucketTaggingResult;
import com.tencent.cos.xml.model.bucket.GetBucketVersioningRequest;
import com.tencent.cos.xml.model.bucket.GetBucketVersioningResult;
import com.tencent.cos.xml.model.bucket.GetBucketWebsiteRequest;
import com.tencent.cos.xml.model.bucket.GetBucketWebsiteResult;
import com.tencent.cos.xml.model.bucket.HeadBucketRequest;
import com.tencent.cos.xml.model.bucket.HeadBucketResult;
import com.tencent.cos.xml.model.bucket.ListBucketInventoryRequest;
import com.tencent.cos.xml.model.bucket.ListBucketInventoryResult;
import com.tencent.cos.xml.model.bucket.ListBucketVersionsRequest;
import com.tencent.cos.xml.model.bucket.ListBucketVersionsResult;
import com.tencent.cos.xml.model.bucket.PutBucketACLRequest;
import com.tencent.cos.xml.model.bucket.PutBucketACLResult;
import com.tencent.cos.xml.model.bucket.PutBucketAccelerateRequest;
import com.tencent.cos.xml.model.bucket.PutBucketAccelerateResult;
import com.tencent.cos.xml.model.bucket.PutBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.PutBucketCORSResult;
import com.tencent.cos.xml.model.bucket.PutBucketDomainRequest;
import com.tencent.cos.xml.model.bucket.PutBucketDomainResult;
import com.tencent.cos.xml.model.bucket.PutBucketIntelligentTieringRequest;
import com.tencent.cos.xml.model.bucket.PutBucketIntelligentTieringResult;
import com.tencent.cos.xml.model.bucket.PutBucketInventoryRequest;
import com.tencent.cos.xml.model.bucket.PutBucketInventoryResult;
import com.tencent.cos.xml.model.bucket.PutBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.PutBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.PutBucketLoggingRequest;
import com.tencent.cos.xml.model.bucket.PutBucketLoggingResult;
import com.tencent.cos.xml.model.bucket.PutBucketPolicyRequest;
import com.tencent.cos.xml.model.bucket.PutBucketPolicyResult;
import com.tencent.cos.xml.model.bucket.PutBucketRefererRequest;
import com.tencent.cos.xml.model.bucket.PutBucketRefererResult;
import com.tencent.cos.xml.model.bucket.PutBucketReplicationRequest;
import com.tencent.cos.xml.model.bucket.PutBucketReplicationResult;
import com.tencent.cos.xml.model.bucket.PutBucketRequest;
import com.tencent.cos.xml.model.bucket.PutBucketResult;
import com.tencent.cos.xml.model.bucket.PutBucketTaggingRequest;
import com.tencent.cos.xml.model.bucket.PutBucketTaggingResult;
import com.tencent.cos.xml.model.bucket.PutBucketVersioningRequest;
import com.tencent.cos.xml.model.bucket.PutBucketVersioningResult;
import com.tencent.cos.xml.model.bucket.PutBucketWebsiteRequest;
import com.tencent.cos.xml.model.bucket.PutBucketWebsiteResult;
import com.tencent.cos.xml.model.ci.FormatConversionRequest;
import com.tencent.cos.xml.model.ci.FormatConversionResult;
import com.tencent.cos.xml.model.ci.GetDescribeMediaBucketsRequest;
import com.tencent.cos.xml.model.ci.GetDescribeMediaBucketsResult;
import com.tencent.cos.xml.model.ci.GetMediaInfoRequest;
import com.tencent.cos.xml.model.ci.GetMediaInfoResult;
import com.tencent.cos.xml.model.ci.GetSnapshotRequest;
import com.tencent.cos.xml.model.ci.GetSnapshotResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlBytesRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlBytesResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlLinkRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlLinkResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentResult;
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
import com.tencent.cos.xml.model.ci.ai.AISuperResolutionRequest;
import com.tencent.cos.xml.model.ci.ai.AddImageSearchRequest;
import com.tencent.cos.xml.model.ci.ai.AssessQualityRequest;
import com.tencent.cos.xml.model.ci.ai.AssessQualityResult;
import com.tencent.cos.xml.model.ci.ai.AutoTranslationBlockRequest;
import com.tencent.cos.xml.model.ci.ai.AutoTranslationBlockResult;
import com.tencent.cos.xml.model.ci.ai.COSOCRRequest;
import com.tencent.cos.xml.model.ci.ai.COSOCRResult;
import com.tencent.cos.xml.model.ci.ai.CreateCRcodeRequest;
import com.tencent.cos.xml.model.ci.ai.CreateCRcodeResult;
import com.tencent.cos.xml.model.ci.ai.DeleteImageSearchRequest;
import com.tencent.cos.xml.model.ci.ai.DetectLabelRequest;
import com.tencent.cos.xml.model.ci.ai.DetectLabelResult;
import com.tencent.cos.xml.model.ci.ai.GetActionSequenceRequest;
import com.tencent.cos.xml.model.ci.ai.GetActionSequenceResult;
import com.tencent.cos.xml.model.ci.ai.GetLiveCodeRequest;
import com.tencent.cos.xml.model.ci.ai.GetLiveCodeResult;
import com.tencent.cos.xml.model.ci.ai.GetSearchImageRequest;
import com.tencent.cos.xml.model.ci.ai.GetSearchImageResult;
import com.tencent.cos.xml.model.ci.ai.GoodsMattingRequest;
import com.tencent.cos.xml.model.ci.ai.ImageRepairRequest;
import com.tencent.cos.xml.model.ci.ai.LivenessRecognitionRequest;
import com.tencent.cos.xml.model.ci.ai.LivenessRecognitionResult;
import com.tencent.cos.xml.model.ci.ai.RecognitionQRcodeRequest;
import com.tencent.cos.xml.model.ci.ai.RecognitionQRcodeResult;
import com.tencent.cos.xml.model.ci.ai.RecognizeLogoRequest;
import com.tencent.cos.xml.model.ci.ai.RecognizeLogoResult;
import com.tencent.cos.xml.model.ci.media.GetPrivateM3U8Request;
import com.tencent.cos.xml.model.ci.media.GetPrivateM3U8Result;
import com.tencent.cos.xml.model.ci.media.GetWorkflowDetailRequest;
import com.tencent.cos.xml.model.ci.media.GetWorkflowDetailResult;
import com.tencent.cos.xml.model.object.AppendObjectRequest;
import com.tencent.cos.xml.model.object.AppendObjectResult;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.DeleteMultiObjectRequest;
import com.tencent.cos.xml.model.object.DeleteMultiObjectResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectTaggingRequest;
import com.tencent.cos.xml.model.object.DeleteObjectTaggingResult;
import com.tencent.cos.xml.model.object.GetObjectACLRequest;
import com.tencent.cos.xml.model.object.GetObjectACLResult;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.GetObjectTaggingRequest;
import com.tencent.cos.xml.model.object.GetObjectTaggingResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.OptionObjectRequest;
import com.tencent.cos.xml.model.object.OptionObjectResult;
import com.tencent.cos.xml.model.object.PutObjectACLRequest;
import com.tencent.cos.xml.model.object.PutObjectACLResult;
import com.tencent.cos.xml.model.object.PutObjectTaggingRequest;
import com.tencent.cos.xml.model.object.PutObjectTaggingResult;
import com.tencent.cos.xml.model.object.RestoreRequest;
import com.tencent.cos.xml.model.object.RestoreResult;
import com.tencent.cos.xml.model.object.SelectObjectContentRequest;
import com.tencent.cos.xml.model.object.SelectObjectContentResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.cos.xml.model.tag.COSMetaData;
import com.tencent.cos.xml.transfer.SelectObjectContentConverter;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.http.QCloudHttpRequest;

/**
 * 提供用于访问Tencent Cloud COS服务的全面服务类。<br>
 * 在调用 COS 服务前，都必须初始化一个该类的对象。<br>
 * 该类只存在于 cosxml 包中，cosxml-lite 包中，只包含了 {@link CosXmlSimpleService} 类。
 * <p>
 * 更详细的使用方式请参考：<a href="https://cloud.tencent.com/document/product/436/12159#.E5.88.9D.E5.A7.8B.E5.8C.96.E6.9C.8D.E5.8A.A1">入门文档</a>
 * <p>
 */
public class CosXmlService extends CosXmlSimpleService implements CosXml {

    private String getServiceRequestDomain;

    /**
     * cos android SDK 服务
     * @param context Application 上下文{@link android.app.Application}
     * @param configuration cos android SDK 服务配置{@link CosXmlServiceConfig}
     * @param qCloudCredentialProvider cos android SDK 证书提供者 {@link QCloudCredentialProvider}
     */
    public CosXmlService(Context context, CosXmlServiceConfig configuration, QCloudCredentialProvider qCloudCredentialProvider){
        super(context, configuration, qCloudCredentialProvider);
    }

    /**
     * cos android SDK 服务
     *
     * @param context       Application 上下文{@link android.app.Application}
     * @param configuration cos android SDK 服务配置 {@link CosXmlServiceConfig}
     * @param qCloudSigner  cos android SDK 签名提供者 {@link QCloudSigner}
     */
    public CosXmlService(Context context, CosXmlServiceConfig configuration, QCloudSigner qCloudSigner) {
        super(context, configuration, qCloudSigner);
    }

    /**
     * cos android SDK 服务
     *
     * @param context       Application 上下文{@link android.app.Application}
     * @param configuration cos android SDK 服务配置 {@link CosXmlServiceConfig}
     * @param selfSigner  cos android SDK 签名提供者 {@link QCloudSigner}
     */
    public CosXmlService(Context context, CosXmlServiceConfig configuration, QCloudSelfSigner selfSigner) {
        super(context, configuration, selfSigner);
    }

    /**
     * cos android SDK 服务
     *
     * @param context       Application 上下文{@link android.app.Application}
     * @param configuration cos android SDK 服务配置{@link CosXmlServiceConfig}
     */
    public CosXmlService(Context context, CosXmlServiceConfig configuration) {
        super(context, configuration);
    }

    @Override
    protected String signerTypeCompat(String signerType, CosXmlRequest cosXmlRequest){
        // GetDescribeMediaBucketsRequest其实是ci域名 应该用ci签名
        if(cosXmlRequest instanceof GetDescribeMediaBucketsRequest){
            // 此判断是为了兼容自定义签名UserCosXmlSigner的情况
            if("CosXmlSigner".equals(signerType)){
                return CIService.SIGNERTYPE_CISIGNER;
            }
        }
        return signerType;
    }

    /**
     * <p>
     * 获取所属账户的所有存储空间列表的同步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link CosXml#getService(GetServiceRequest)}
     *</p>
     */
    @Override
    public GetServiceResult getService(GetServiceRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetServiceResult());
    }

    /**
     * <p>
     * 获取所属账户的所有存储空间列表的异步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link CosXml#getServiceAsync(GetServiceRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void getServiceAsync(GetServiceRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetServiceResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 追加上传对象的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#appendObject(AppendObjectRequest)}
     */
    @Override
    public AppendObjectResult appendObject(AppendObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        AppendObjectResult appendObjectResult =  new AppendObjectResult();
        appendObjectResult.accessUrl = getAccessUrl(request);
        return execute(request, appendObjectResult);
    }

    /**
     * <p>
     * 追加上传对象的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#appendObjectAsync(AppendObjectRequest, CosXmlResultListener)}
     */
    @Override
    public void appendObjectAsync(AppendObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        AppendObjectResult appendObjectResult =  new AppendObjectResult();
        appendObjectResult.accessUrl = getAccessUrl(request);
        schedule(request, appendObjectResult, cosXmlResultListener);
    }

    /**
     * <p>
     * 批量删除 COS 对象的同步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#deleteMultiObject(DeleteMultiObjectRequest)}
     *</p>
     */
    @Override
    public DeleteMultiObjectResult deleteMultiObject(DeleteMultiObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteMultiObjectResult());
    }

    /**
     * <p>
     * 批量删除 COS 对象的异步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#deleteMultiObjectAsync(DeleteMultiObjectRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void deleteMultiObjectAsync(DeleteMultiObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteMultiObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取 COS 对象的访问权限信息（Access Control List, ACL）的同步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#getObjectACL(GetObjectACLRequest)}
     *</p>
     */
    @Override
    public GetObjectACLResult getObjectACL(GetObjectACLRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetObjectACLResult());
    }

    /**
     * <p>
     * 获取 COS 对象的访问权限信息（Access Control List, ACL）的异步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#getObjectACLAsync(GetObjectACLRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void getObjectACLAsync(GetObjectACLRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectACLResult(), cosXmlResultListener);
    }

//    /**
//     * <p>
//     * 获取 COS 对象的元数据信息(meta data)的同步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#headObject(HeadObjectRequest)}
//     *</p>
//     */
//    @Override
//    public HeadObjectResult headObject(HeadObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
//        return execute(request, new HeadObjectResult());
//    }
//
//    /**
//     * <p>
//     * 获取 COS 对象的元数据信息(meta data)的异步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#headObjectAsync(HeadObjectRequest request, CosXmlResultListener cosXmlResultListener)}
//     *</p>
//     */
//    @Override
//    public void headObjectAsync(HeadObjectRequest request, CosXmlResultListener cosXmlResultListener) {
//        schedule(request, new HeadObjectResult(), cosXmlResultListener);
//    }

    /**
     * <p>
     * COS 对象的跨域访问配置预请求的同步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#optionObject(OptionObjectRequest)}
     *</p>
     */
    @Override
    public OptionObjectResult optionObject(OptionObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new OptionObjectResult());
    }

    /**
     * <p>
     * COS 对象的跨域访问配置预请求的异步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#optionObjectAsync(OptionObjectRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void optionObjectAsync(OptionObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new OptionObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置 COS 对象的访问权限信息（Access Control List, ACL）的同步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#putObjectACL(PutObjectACLRequest)}
     *</p>
     */
    @Override
    public PutObjectACLResult putObjectACL(PutObjectACLRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutObjectACLResult());
    }

    /**
     * <p>
     * 设置 COS 对象的访问权限信息（Access Control List, ACL）的异步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#putObjectACLAsync(PutObjectACLRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void putObjectACLAsync(PutObjectACLRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutObjectACLResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置对象标签的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putObjectTagging(PutObjectTaggingRequest)}
     */
    @Override
    public PutObjectTaggingResult putObjectTagging(PutObjectTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutObjectTaggingResult());
    }

    /**
     * <p>
     * 设置对象标签的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putObjectTaggingAsync(PutObjectTaggingRequest, CosXmlResultListener)}
     */
    @Override
    public void putObjectTaggingAsync(PutObjectTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutObjectTaggingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取对象标签的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getObjectTagging(GetObjectTaggingRequest)}
     */
    @Override
    public GetObjectTaggingResult getObjectTagging(GetObjectTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetObjectTaggingResult());
    }

    /**
     * <p>
     * 获取对象标签的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getObjectTaggingAsync(GetObjectTaggingRequest, CosXmlResultListener)}
     */
    @Override
    public void getObjectTaggingAsync(GetObjectTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectTaggingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除对象标签的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteObjectTagging(DeleteObjectTaggingRequest)}
     */
    @Override
    public DeleteObjectTaggingResult deleteObjectTagging(DeleteObjectTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteObjectTaggingResult());
    }

    /**
     * <p>
     * 删除对象标签的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteObjectTaggingAsync(DeleteObjectTaggingRequest, CosXmlResultListener)}
     */
    @Override
    public void deleteObjectTaggingAsync(DeleteObjectTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteObjectTaggingResult(), cosXmlResultListener);
    }

//    /**
//     * <p>
//     * 简单复制对象的同步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#copyObject(CopyObjectRequest request)}
//     *</p>
//     */
//    @Override
//    public CopyObjectResult copyObject(CopyObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
//        return execute(request, new CopyObjectResult());
//    }
//
//    /**
//     * <p>
//     * 简单复制对象的异步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#copyObjectAsync(CopyObjectRequest request, CosXmlResultListener cosXmlResultListener)}
//     *</p>
//     */
//    @Override
//    public void copyObjectAsync(CopyObjectRequest request, CosXmlResultListener cosXmlResultListener) {
//        schedule(request, new CopyObjectResult(), cosXmlResultListener);
//    }
//
//    /**
//     * <p>
//     * 分块复制的同步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#copyObject(UploadPartCopyRequest)}
//     *</p>
//     */
//    @Override
//    public UploadPartCopyResult copyObject(UploadPartCopyRequest request) throws CosXmlClientException, CosXmlServiceException {
//        return execute(request, new UploadPartCopyResult());
//    }
//
//    /**
//     * <p>
//     * 分块复制的异步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#copyObjectAsync(UploadPartCopyRequest request, CosXmlResultListener cosXmlResultListener)}
//     *</p>
//     */
//    @Override
//    public void copyObjectAsync(UploadPartCopyRequest request, CosXmlResultListener cosXmlResultListener) {
//        schedule(request, new UploadPartCopyResult(), cosXmlResultListener);
//    }

    @Override
    public RestoreResult restoreObject(RestoreRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new RestoreResult());
    }

    @Override
    public void restoreObjectAsync(RestoreRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new RestoreResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除跨域访问配置信息的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  CosXml#deleteBucketCORS(DeleteBucketCORSRequest request)}
     */
    @Override
    public DeleteBucketCORSResult deleteBucketCORS(DeleteBucketCORSRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketCORSResult());
    }

    /**
     * <p>
     * 删除跨域访问配置信息的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  CosXml#deleteBucketCORSAsync(DeleteBucketCORSRequest request, CosXmlResultListener cosXmlResultListener)}
     */
    @Override
    public void deleteBucketCORSAsync(DeleteBucketCORSRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketCORSResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除存储桶（Bucket） 的生命周期配置的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  CosXml#deleteBucketLifecycle(DeleteBucketLifecycleRequest request)}
     */
    @Override
    public DeleteBucketLifecycleResult deleteBucketLifecycle(DeleteBucketLifecycleRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketLifecycleResult());
    }

    /**
     * <p>
     * 删除存储桶（Bucket） 的生命周期配置的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  CosXml#deleteBucketLifecycleAsync(DeleteBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener)}
     */
    @Override
    public void deleteBucketLifecycleAsync(DeleteBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketLifecycleResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除存储桶 (Bucket)的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  CosXml#deleteBucket(DeleteBucketRequest request)}
     */
    @Override
    public DeleteBucketResult deleteBucket(DeleteBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketResult());
    }

    /**
     * <p>
     * 删除存储桶 (Bucket)的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  CosXml#deleteBucketAsync(DeleteBucketRequest request, CosXmlResultListener cosXmlResultListener)}
     */
    @Override
    public void deleteBucketAsync(DeleteBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketResult(), cosXmlResultListener);
    }

//    /**
//     * <p>
//     * 删除存储桶（Bucket) 标签的同步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#deleteBucketTagging(DeleteBucketTaggingRequest request)}
//     *</p>
//     */
//    @Override
//    public DeleteBucketTaggingResult deleteBucketTagging(DeleteBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
//        return execute(request, new DeleteBucketTaggingResult());
//    }
//
//    /**
//     * <p>
//     * 删除存储桶（Bucket) 标签的异步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#deleteBucketTaggingAsync(DeleteBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener)}
//     *</p>
//     */
//    @Override
//    public void deleteBucketTaggingAsync(DeleteBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
//        schedule(request, new DeleteBucketTaggingResult(), cosXmlResultListener);
//    }

    /**
     * <p>
     * 获取存储桶（Bucket) 的访问权限信息（Access Control List, ACL）的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketACL(GetBucketACLRequest request)}
     *</p>
     */
    @Override
    public GetBucketACLResult getBucketACL(GetBucketACLRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketACLResult());
    }

    /**
     * <p>
     * 获取存储桶（Bucket) 的访问权限信息（Access Control List, ACL）的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketACLAsync(GetBucketACLRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void getBucketACLAsync(GetBucketACLRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketACLResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询存储桶（Bucket) 跨域访问配置信息的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketCORS(GetBucketCORSRequest request)}
     *</p>
     */
    @Override
    public GetBucketCORSResult getBucketCORS(GetBucketCORSRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketCORSResult());
    }

    /**
     * <p>
     * 查询存储桶（Bucket) 跨域访问配置信息的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketCORSAsync(GetBucketCORSRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void getBucketCORSAsync(GetBucketCORSRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketCORSResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询存储桶（Bucket) 的生命周期配置的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketLifecycle(GetBucketLifecycleRequest request)}
     *</p>
     */
    @Override
    public GetBucketLifecycleResult getBucketLifecycle(GetBucketLifecycleRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketLifecycleResult());
    }

    /**
     * <p>
     * 查询存储桶（Bucket) 的生命周期配置的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketLifecycleAsync(GetBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void getBucketLifecycleAsync(GetBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketLifecycleResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取存储桶（Bucket) 所在的地域信息的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketLocation(GetBucketLocationRequest request)}
     *</p>
     */
    @Override
    public GetBucketLocationResult getBucketLocation(GetBucketLocationRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketLocationResult());
    }

    /**
     * <p>
     * 获取存储桶（Bucket) 所在的地域信息的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketLocationAsync(GetBucketLocationRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void getBucketLocationAsync(GetBucketLocationRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketLocationResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询存储桶（Bucket) 下的部分或者全部对象的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucket(GetBucketRequest request)}
     *</p>
     */
    @Override
    public GetBucketResult getBucket(GetBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketResult());
    }

    /**
     * <p>
     * 查询存储桶（Bucket) 下的部分或者全部对象的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketAsync(GetBucketRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void getBucketAsync(GetBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketResult(), cosXmlResultListener);
    }

//    /**
//     * <p>
//     * 获取存储桶（Bucket）的标签的同步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#getBucketTagging(GetBucketTaggingRequest request)}
//     *</p>
//     */
//    @Override
//    public GetBucketTaggingResult getBucketTagging(GetBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
//        return execute(request, new GetBucketTaggingResult());
//    }
//
//    /**
//     * <p>
//     * 获取存储桶（Bucket）的标签的异步方法.&nbsp;
//     *
//     * 详细介绍，请查看:{@link  CosXml#getBucketTaggingAsync(GetBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener)}
//     *</p>
//     */
//    @Override
//    public void getBucketTaggingAsync(GetBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
//        schedule(request, new GetBucketTaggingResult(), cosXmlResultListener);
//    }

    /**
     * <p>
     * 存储桶（Bucket） 是否存在的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#headBucket(HeadBucketRequest request)}
     *</p>
     */
    @Override
    public HeadBucketResult headBucket(HeadBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new HeadBucketResult());
    }

    /**
     * <p>
     * 存储桶（Bucket）是否存在的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#headBucketAsync(HeadBucketRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void headBucketAsync(HeadBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new HeadBucketResult(), cosXmlResultListener);
    }

   
    /**
     * <p>
     * 设置存储桶（Bucket） 的访问权限（Access Control List, ACL)的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketACL(PutBucketACLRequest request)}
     *</p>
     */
    @Override
    public PutBucketACLResult putBucketACL(PutBucketACLRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketACLResult());
    }

    /**
     * <p>
     * 设置存储桶（Bucket） 的访问权限（Access Control List, ACL)的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketACLAsync(PutBucketACLRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void putBucketACLAsync(PutBucketACLRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketACLResult(), cosXmlResultListener);
    }

    @Override
    public GetBucketAccelerateResult getBucketAccelerate(GetBucketAccelerateRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketAccelerateResult());
    }

    @Override
    public void getBucketAccelerateAsync(GetBucketAccelerateRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketAccelerateResult(), cosXmlResultListener);
    }

    @Override
    public PutBucketAccelerateResult putBucketAccelerate(PutBucketAccelerateRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketAccelerateResult());
    }

    @Override
    public void putBucketAccelerateAsync(PutBucketAccelerateRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketAccelerateResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置存储桶（Bucket） 的跨域配置信息的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketCORS(PutBucketCORSRequest request)}
     *</p>
     */
    @Override
    public PutBucketCORSResult putBucketCORS(PutBucketCORSRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketCORSResult());
    }

    /**
     * <p>
     * 设置存储桶（Bucket） 的跨域配置信息的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketCORSAsync(PutBucketCORSRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void putBucketCORSAsync(PutBucketCORSRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketCORSResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置存储桶（Bucket) 生命周期配置的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketLifecycle(PutBucketLifecycleRequest request)}
     *</p>
     */
    @Override
    public PutBucketLifecycleResult putBucketLifecycle(PutBucketLifecycleRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketLifecycleResult());
    }

    /**
     * <p>
     * 设置存储桶（Bucket) 生命周期配置的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketLifecycleAsync(PutBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void putBucketLifecycleAsync(PutBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketLifecycleResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 创建存储桶（Bucket）的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucket(PutBucketRequest request)}
     *</p>
     */
    @Override
    public PutBucketResult putBucket(PutBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketResult());
    }

    /**
     * <p>
     * 创建存储桶（Bucket）的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketAsync(PutBucketRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void putBucketAsync(PutBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketResult(), cosXmlResultListener);
    }

//    @Override
//    public PutBucketTaggingResult putBucketTagging(PutBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
//        return execute(request, new PutBucketTaggingResult());
//    }
//
//    @Override
//    public void putBucketTaggingAsync(PutBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
//        schedule(request, new UploadPartResult(), cosXmlResultListener);
//    }

    /**
     * <p>
     * 获取存储桶（Bucket）版本控制信息的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketVersioning(GetBucketVersioningRequest)}
     *</p>
     */
    @Override
    public GetBucketVersioningResult getBucketVersioning(GetBucketVersioningRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketVersioningResult());
    }

    /**
     * <p>
     * 获取存储桶（Bucket）版本控制信息的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketVersioningAsync(GetBucketVersioningRequest , CosXmlResultListener)}
     *</p>
     */
    @Override
    public void getBucketVersioningAsync(GetBucketVersioningRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketVersioningResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 存储桶（Bucket）版本控制的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketVersioning(PutBucketVersioningRequest)}
     *</p>
     */
    @Override
    public PutBucketVersioningResult putBucketVersioning(PutBucketVersioningRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketVersioningResult());
    }

    /**
     * <p>
     * 存储桶（Bucket）版本控制的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketVersionAsync(PutBucketVersioningRequest, CosXmlResultListener)}
     *</p>
     */
    @Override
    public void putBucketVersionAsync(PutBucketVersioningRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketVersioningResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取跨区域复制配置信息的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketReplication(GetBucketReplicationRequest)}
     *</p>
     */
    @Override
    public GetBucketReplicationResult getBucketReplication(GetBucketReplicationRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketReplicationResult());
    }

    /**
     * <p>
     * 获取跨区域复制配置信息的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#getBucketReplicationAsync(GetBucketReplicationRequest,CosXmlResultListener)}
     *</p>
     */
    @Override
    public void getBucketReplicationAsync(GetBucketReplicationRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketReplicationResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 配置跨区域复制的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketReplication(PutBucketReplicationRequest)}
     *</p>
     */
    @Override
    public PutBucketReplicationResult putBucketReplication(PutBucketReplicationRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketReplicationResult());
    }

    /**
     * <p>
     * 配置跨区域复制的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#putBucketReplicationAsync(PutBucketReplicationRequest, CosXmlResultListener)}
     *</p>
     */
    @Override
    public void putBucketReplicationAsync(PutBucketReplicationRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketReplicationResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除跨区域复制配置的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#deleteBucketReplication(DeleteBucketReplicationRequest)}
     *</p>
     */
    @Override
    public DeleteBucketReplicationResult deleteBucketReplication(DeleteBucketReplicationRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketReplicationResult());
    }

    /**
     * <p>
     * 删除跨区域复制配置的异步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#deleteBucketReplicationAsync(DeleteBucketReplicationRequest, CosXmlResultListener)}
     *</p>
     */
    @Override
    public void deleteBucketReplicationAsync(DeleteBucketReplicationRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketReplicationResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取存储桶（Bucket）所有或者部分对象的版本信息的同步方法.&nbsp;
     * 详细介绍，请查看:{@link  CosXml#listBucketVersions(ListBucketVersionsRequest)}
     *</p>
     */
    @Override
    public ListBucketVersionsResult listBucketVersions(ListBucketVersionsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new ListBucketVersionsResult());
    }

    /**
     * <p>
     * 获取存储桶（Bucket）所有或者部分对象的版本信息的异步方法.&nbsp;
     * 详细介绍，请查看: {@link CosXml#listBucketVersionsAsync(ListBucketVersionsRequest, CosXmlResultListener)}
     *</p>
     */
    @Override
    public void listBucketVersionsAsync(ListBucketVersionsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new ListBucketVersionsResult(), cosXmlResultListener);
    }


    /**
     * <p>
     * 通过封装 HeadBucketRequest 请求来判断 bucket 是否存在。
     * </p>
     * 详细介绍，请查看: {@link CosXml#doesBucketExist(String)}
     */
    @Override
    public boolean doesBucketExist(String bucketName) throws CosXmlClientException, CosXmlServiceException {

        GetBucketACLRequest getBucketACLRequest = new GetBucketACLRequest(bucketName);
        try {
            getBucketACL(getBucketACLRequest);
            return true;
        } catch (CosXmlServiceException serviceException) {

            if ((serviceException.getStatusCode() == Constants.BUCKET_REDIRECT_STATUS_CODE)
                    || "AccessDenied".equals(serviceException.getErrorCode())) {
                return true;
            }

            if (serviceException.getStatusCode() == Constants.NO_SUCH_BUCKET_STATUS_CODE) {
                return false;
            }
            throw serviceException;
        }
    }

    /**
     * <p>
     * 通过封装 HeadBucketRequest 请求来判断 bucket 是否存在。
     * </p>
     * 详细介绍，请查看: {@link CosXml#doesBucketExistAsync(String, CosXmlBooleanListener)}
     */
    @Override
    public void doesBucketExistAsync(final String bucketName, final CosXmlBooleanListener booleanListener) {

        HeadBucketRequest headBucketResult = new HeadBucketRequest(bucketName);

        headBucketAsync(headBucketResult, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                booleanListener.onSuccess(true);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {

                boolean successCallback = false;

                if (clientException == null && serviceException != null) {

                    if ((serviceException.getStatusCode() == Constants.BUCKET_REDIRECT_STATUS_CODE)
                            || "AccessDenied".equals(serviceException.getErrorCode())) {
                        successCallback = true;
                        booleanListener.onSuccess(true);
                    }

                    if (serviceException.getStatusCode() == Constants.NO_SUCH_BUCKET_STATUS_CODE) {
                        successCallback = true;
                        booleanListener.onSuccess(false);
                    }
                }

                if (!successCallback) {
                    booleanListener.onFail(clientException, serviceException);
                }
            }
        });
    }

    /**
     * <p>
     * 通过封装 HeadObjectRequest 请求来判断对象是否存在。
     * </p>
     *
     * <p>
     * 注意只能在单个 region 下判断，无法一次性判断所有的 region。
     * 详细介绍，请查看: {@link CosXml#doesObjectExist(String, String)}
     * </p>
     */
    @Override
    public boolean doesObjectExist(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException {

        HeadObjectRequest headObjectRequest = new HeadObjectRequest(bucketName, objectName);

        try {
            headObject(headObjectRequest);
        } catch (CosXmlServiceException serviceException) {

            if (serviceException.getStatusCode() == 404) {
                return false;
            } else {
                throw serviceException;
            }
        }
        return true;
    }

    /**
     * <p>
     * 通过封装 HeadObjectRequest 请求来判断对象是否存在。
     * </p>
     *
     * <p>
     * 注意只能在单个 region 下判断，无法一次性判断所有的 region。
     * 详细介绍，请查看: {@link CosXml#doesObjectExistAsync(String, String, CosXmlBooleanListener)}
     * </p>
     */
    @Override
    public void doesObjectExistAsync(String bucketName, String objectName, final CosXmlBooleanListener booleanListener) {

        HeadObjectRequest headObjectRequest = new HeadObjectRequest(bucketName, objectName);
        headObjectAsync(headObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {

                booleanListener.onSuccess(true);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {

                if (serviceException != null && serviceException.getStatusCode() == 404) { // the object is no exist
                    booleanListener.onSuccess(false);
                } else {
                    booleanListener.onFail(clientException, serviceException);
                }
            }
        });

    }

    /**
     * <p>
     * 通过封装 DeleteObjectRequest 请求来删除对象。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteObject(String, String)}
     */
    @Override
    public boolean deleteObject(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException {

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, objectName);
        deleteObject(deleteObjectRequest);
        return true;
    }

    /**
     * <p>
     * 通过封装 DeleteObjectRequest 请求来删除对象。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteObjectAsync(String, String, CosXmlBooleanListener)}
     */
    @Override
    public void deleteObjectAsync(String bucketName, String objectName, final CosXmlBooleanListener booleanListener) {

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, objectName);
        deleteObjectAsync(deleteObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                booleanListener.onSuccess(true);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                booleanListener.onFail(exception, serviceException);
            }
        });
    }


    /**
     * <p>
     * 更新对象元数据的同步方法。 建议使用{@link CosXmlService#updateObjectMetaData}
     * {@link CosXmlService#updateObjectMeta}：bucketName不含appid，比如：test
     * {@link CosXmlService#updateObjectMetaData}：bucketName包含appid，比如：test-1250000000
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#updateObjectMeta(String, String, COSMetaData)}
     */
    @Deprecated
    @Override
    public boolean updateObjectMeta(String bucketName, String objectName, COSMetaData metaData) throws CosXmlClientException, CosXmlServiceException{

        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(config.getAppid(),
                bucketName, config.getRegion(), objectName);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, objectName, copySourceStruct);
        copyObjectRequest.setCopyMetaDataDirective(MetaDataDirective.REPLACED);
        for (String key : metaData.keySet()) {
            copyObjectRequest.setXCOSMeta(key, metaData.get(key));
        }

        copyObject(copyObjectRequest);
        return true;
    }

    /**
     * <p>
     * 更新对象元数据的异步方法。 建议使用{@link CosXmlService#updateObjectMetaDataAsync}
     * {@link CosXmlService#updateObjectMetaAsync}：bucketName不含appid，比如：test
     * {@link CosXmlService#updateObjectMetaDataAsync}：bucketName包含appid，比如：test-1250000000
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#updateObjectMetaAsync(String, String, COSMetaData, CosXmlBooleanListener)}
     */
    @Deprecated
    @Override
    public void updateObjectMetaAsync(String bucketName, String objectName, COSMetaData metaData, final CosXmlBooleanListener booleanListener) {

        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(config.getAppid(),
                bucketName, config.getRegion(), objectName);
        CopyObjectRequest copyObjectRequest = null;
        copyObjectRequest = new CopyObjectRequest(bucketName, objectName, copySourceStruct);
        copyObjectRequest.setCopyMetaDataDirective(MetaDataDirective.REPLACED);
        for (String key : metaData.keySet()) {
            copyObjectRequest.setXCOSMeta(key, metaData.get(key));
        }
        copyObjectAsync(copyObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                booleanListener.onSuccess(true);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                booleanListener.onFail(exception, serviceException);
            }
        });
    }

    /**
     * <p>
     * 更新对象元数据的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#updateObjectMetaData(String, String, COSMetaData)}
     */
    @Override
    public boolean updateObjectMetaData(String bucketName, String objectName, COSMetaData metaData) throws CosXmlClientException, CosXmlServiceException{

        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                bucketName, config.getRegion(), objectName);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, objectName, copySourceStruct);
        copyObjectRequest.setCopyMetaDataDirective(MetaDataDirective.REPLACED);
        for (String key : metaData.keySet()) {
            copyObjectRequest.setXCOSMeta(key, metaData.get(key));
        }

        copyObject(copyObjectRequest);
        return true;
    }

    /**
     * <p>
     * 更新对象元数据的异步方法
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#updateObjectMetaDataAsync(String, String, COSMetaData, CosXmlBooleanListener)}
     */
    @Override
    public void updateObjectMetaDataAsync(String bucketName, String objectName, COSMetaData metaData, final CosXmlBooleanListener booleanListener) {

        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                bucketName, config.getRegion(), objectName);
        CopyObjectRequest copyObjectRequest = null;
        copyObjectRequest = new CopyObjectRequest(bucketName, objectName, copySourceStruct);
        copyObjectRequest.setCopyMetaDataDirective(MetaDataDirective.REPLACED);
        for (String key : metaData.keySet()) {
            copyObjectRequest.setXCOSMeta(key, metaData.get(key));
        }
        copyObjectAsync(copyObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                booleanListener.onSuccess(true);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                booleanListener.onFail(exception, serviceException);
            }
        });
    }

    /**
     * <p>
     * 设置存储桶静态网站参数的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketWebsite(PutBucketWebsiteRequest)}
     */
    @Override
    public PutBucketWebsiteResult putBucketWebsite(PutBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketWebsiteResult());
    }

    /**
     * <p>
     * 设置存储桶静态网站参数的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketWebsiteAsync(PutBucketWebsiteRequest, CosXmlResultListener)}
     */
    @Override
    public void putBucketWebsiteAsync(PutBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketWebsiteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取存储桶静态网站参数的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketWebsite(GetBucketWebsiteRequest)}
     */
    @Override
    public GetBucketWebsiteResult getBucketWebsite(GetBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketWebsiteResult());
    }

    /**
     * <p>
     * 获取存储桶静态网站参数的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketWebsiteAsync(GetBucketWebsiteRequest, CosXmlResultListener)}
     */
    @Override
    public void getBucketWebsiteAsync(GetBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketWebsiteResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除存储桶静态网站参数的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketWebsite(DeleteBucketWebsiteRequest)}
     */
    @Override
    public DeleteBucketWebsiteResult deleteBucketWebsite(DeleteBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketWebsiteResult());
    }

    /**
     * <p>
     * 删除存储桶静态网站参数的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketWebsiteAsync(DeleteBucketWebsiteRequest, CosXmlResultListener)}
     */
    @Override
    public void deleteBucketWebsiteAsync(DeleteBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketWebsiteResult(), cosXmlResultListener);
    }

    /**
     * 设置存储桶日志记录的同步方法。
     * 详细介绍，请查看: {@link CosXml#putBucketLogging(PutBucketLoggingRequest)}
     */
    @Override
    public PutBucketLoggingResult putBucketLogging(PutBucketLoggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketLoggingResult());
    }

    /**
     * <p>
     * 设置存储桶日志记录的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketLoggingAsync(PutBucketLoggingRequest, CosXmlResultListener)}
     */
    @Override
    public void putBucketLoggingAsync(PutBucketLoggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketLoggingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取存储桶日志记录的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketLogging(GetBucketLoggingRequest)}
     */
    @Override
    public GetBucketLoggingResult getBucketLogging(GetBucketLoggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketLoggingResult());
    }

    /**
     * <p>
     * 获取存储桶日志记录的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketLoggingAsync(GetBucketLoggingRequest, CosXmlResultListener)}
     */
    @Override
    public void getBucketLoggingAsync(GetBucketLoggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketLoggingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置存储桶标签的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketTagging(PutBucketTaggingRequest)}
     */
    @Override
    public PutBucketTaggingResult putBucketTagging(PutBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketTaggingResult());
    }

    /**
     * <p>
     * 设置存储桶标签的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketTaggingAsync(PutBucketTaggingRequest, CosXmlResultListener)}
     */
    @Override
    public void putBucketTaggingAsync(PutBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketTaggingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取存储桶标签的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketTagging(GetBucketTaggingRequest)}
     */
    @Override
    public GetBucketTaggingResult getBucketTagging(GetBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketTaggingResult());
    }

    /**
     * <p>
     * 获取存储桶标签的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketTaggingAsync(GetBucketTaggingRequest, CosXmlResultListener)}
     */
    @Override
    public void getBucketTaggingAsync(GetBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketTaggingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除存储桶标签的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketTagging(DeleteBucketTaggingRequest)}
     */
    @Override
    public DeleteBucketTaggingResult deleteBucketTagging(DeleteBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketTaggingResult());
    }

    /**
     * <p>
     * 删除存储桶标签的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketTaggingAsync(DeleteBucketTaggingRequest, CosXmlResultListener)}
     */
    @Override
    public void deleteBucketTaggingAsync(DeleteBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketTaggingResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置存储桶清单的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketInventory(PutBucketInventoryRequest)}
     */
    @Override
    public PutBucketInventoryResult putBucketInventory(PutBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketInventoryResult());
    }

    /**
     * <p>
     * 设置存储桶清单的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketInventoryAsync(PutBucketInventoryRequest, CosXmlResultListener)}
     */
    @Override
    public void putBucketInventoryAsync(PutBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketInventoryResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取存储桶清单的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketInventory(GetBucketInventoryRequest)}
     */
    @Override
    public GetBucketInventoryResult getBucketInventory(GetBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketInventoryResult());
    }

    /**
     * <p>
     * 获取存储桶清单的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketInventoryAsync(GetBucketInventoryRequest, CosXmlResultListener)}
     */
    @Override
    public void getBucketInventoryAsync(GetBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketInventoryResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除存储桶清单的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketInventory(DeleteBucketInventoryRequest)}
     */
    @Override
    public DeleteBucketInventoryResult deleteBucketInventory(DeleteBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketInventoryResult());
    }

    /**
     * <p>
     * 删除存储桶清单的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketInventoryAsync(DeleteBucketInventoryRequest, CosXmlResultListener)}
     */
    @Override
    public void deleteBucketInventoryAsync(DeleteBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketInventoryResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询存储桶清单的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#listBucketInventory(ListBucketInventoryRequest)}
     */
    @Override
    public ListBucketInventoryResult listBucketInventory(ListBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new ListBucketInventoryResult());
    }

    /**
     * <p>
     * 查询存储桶清单的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#listBucketInventoryAsync(ListBucketInventoryRequest, CosXmlResultListener)}
     */
    @Override
    public void listBucketInventoryAsync(ListBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new ListBucketInventoryResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询存储桶自定义域名的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketDomain(GetBucketDomainRequest)}
     */
    @Override
    public GetBucketDomainResult getBucketDomain(GetBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketDomainResult());
    }

    /**
     * <p>
     * 查询存储桶自定义域名的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketDomainAsync(GetBucketDomainRequest, CosXmlResultListener)}
     */
    @Override
    public void getBucketDomainAsync(GetBucketDomainRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketDomainResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除存储桶自定义域名的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketDomain(DeleteBucketDomainRequest)}
     */
    @Override
    public DeleteBucketDomainResult deleteBucketDomain(DeleteBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketDomainResult());
    }

    /**
     * <p>
     * 删除存储桶自定义域名的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketDomainAsync(DeleteBucketDomainRequest, CosXmlResultListener)}
     */
    @Override
    public void deleteBucketDomainAsync(DeleteBucketDomainRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketDomainResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询存储桶清单的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#listBucketInventoryAsync(ListBucketInventoryRequest, CosXmlResultListener)}
     */
    @Override
    public PutBucketDomainResult putBucketDomain(PutBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketDomainResult());
    }

    /**
     * <p>
     * 查询存储桶清单的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#listBucketInventoryAsync(ListBucketInventoryRequest, CosXmlResultListener)}
     */
    @Override
    public void putBucketDomainAsync(PutBucketDomainRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketDomainResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置存储桶防盗链的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketReferer(PutBucketRefererRequest)}
     */
    @Override
    public PutBucketRefererResult putBucketReferer(PutBucketRefererRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketRefererResult());
    }

    /**
     * <p>
     * 设置存储桶防盗链的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketRefererAsync(PutBucketRefererRequest, CosXmlResultListener)}
     */
    @Override
    public void putBucketRefererAsync(PutBucketRefererRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketRefererResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取存储桶权限策略的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketPolicy(GetBucketPolicyRequest)}
     */
    @Override
    public GetBucketPolicyResult getBucketPolicy(GetBucketPolicyRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketPolicyResult());
    }

    /**
     * <p>
     * 获取存储桶权限策略的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketPolicyAsync(GetBucketPolicyRequest, CosXmlResultListener)}
     */
    @Override
    public void getBucketPolicyAsync(GetBucketPolicyRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketPolicyResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置存储桶权限策略的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketPolicy(PutBucketPolicyRequest)}
     */
    @Override
    public PutBucketPolicyResult putBucketPolicy(PutBucketPolicyRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketPolicyResult());
    }

    /**
     * <p>
     * 设置存储桶权限策略的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#putBucketPolicyAsync(PutBucketPolicyRequest, CosXmlResultListener)}
     */
    @Override
    public void putBucketPolicyAsync(PutBucketPolicyRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketPolicyResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除存储桶权限策略的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketPolicy(DeleteBucketPolicyRequest)}
     */
    @Override
    public DeleteBucketPolicyResult deleteBucketPolicy(DeleteBucketPolicyRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketPolicyResult());
    }

    /**
     * <p>
     * 删除存储桶权限策略的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#deleteBucketPolicyAsync(DeleteBucketPolicyRequest, CosXmlResultListener)}
     */
    @Override
    public void deleteBucketPolicyAsync(DeleteBucketPolicyRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketPolicyResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取存储桶防盗链配置的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketReferer(GetBucketRefererRequest)}
     */
    @Override
    public GetBucketRefererResult getBucketReferer(GetBucketRefererRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketRefererResult());
    }

    /**
     * <p>
     * 获取存储桶防盗链配置的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketRefererAsync(GetBucketRefererRequest, CosXmlResultListener)}
     */
    @Override
    public void getBucketRefererAsync(GetBucketRefererRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketRefererResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 使用 SQL 语句检索内容的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#selectObjectContent(SelectObjectContentRequest)}
     */
    @Override
    public SelectObjectContentResult selectObjectContent(SelectObjectContentRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SelectObjectContentResult());
    }

    /**
     * <p>
     * 使用 SQL 语句检索内容的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#selectObjectContentAsync(SelectObjectContentRequest, CosXmlResultListener)}
     */
    @Override
    public void selectObjectContentAsync(SelectObjectContentRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SelectObjectContentResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 拉取存储桶内的所有对象及其历史版本信息的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketObjectVersions(GetBucketObjectVersionsRequest)}
     */
    @Override
    public GetBucketObjectVersionsResult getBucketObjectVersions(GetBucketObjectVersionsRequest getBucketObjectVersionsRequest) throws CosXmlClientException, CosXmlServiceException {
        return execute(getBucketObjectVersionsRequest, new GetBucketObjectVersionsResult());
    }

    /**
     * <p>
     * 拉取存储桶内的所有对象及其历史版本信息的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketObjectVersionsAsync(GetBucketObjectVersionsRequest, CosXmlResultListener)}
     */
    @Override
    public void getBucketObjectVersionsAsync(GetBucketObjectVersionsRequest getBucketObjectVersionsRequest, CosXmlResultListener cosXmlResultListener) {
        schedule(getBucketObjectVersionsRequest, new GetBucketObjectVersionsResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 拉取存储桶内的所有对象及其历史版本信息的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketObjectVersions(GetBucketObjectVersionsRequest)}
     */
    @Override
    public PutBucketIntelligentTieringResult putBucketIntelligentTiering(PutBucketIntelligentTieringRequest putBucketIntelligentTieringRequest) throws CosXmlClientException, CosXmlServiceException {
        return execute(putBucketIntelligentTieringRequest, new PutBucketIntelligentTieringResult());
    }

    /**
     * <p>
     * 拉取存储桶内的所有对象及其历史版本信息的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getBucketObjectVersionsAsync(GetBucketObjectVersionsRequest, CosXmlResultListener)}
     */
    @Override
    public void putBucketIntelligentTieringAsync(PutBucketIntelligentTieringRequest putBucketIntelligentTieringRequest, CosXmlResultListener cosXmlResultListener) {
        schedule(putBucketIntelligentTieringRequest, new PutBucketIntelligentTieringResult(), cosXmlResultListener);
    }

    @Override
    public GetBucketIntelligentTieringResult getBucketIntelligentTiering(GetBucketIntelligentTieringRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketIntelligentTieringResult());
    }

    @Override
    public void getBucketIntelligentTieringAsync(GetBucketIntelligentTieringRequest request, CosXmlResultListener resultListener) {
        schedule(request, new GetBucketIntelligentTieringResult(), resultListener);
    }

    /**
     * <p>
     * 预览文档的同步方法。
     * </p>
     * @param request 预览文档的请求 {@link PreviewDocumentRequest}
     * @return 预览文档的返回结果 {@link PreviewDocumentResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public PreviewDocumentResult previewDocument(PreviewDocumentRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PreviewDocumentResult(request.getDownloadPath()));
    }

    /**
     * <p>
     * 预览文档的异步方法
     * </p>
     *
     * @param request 预览文档的请求 {@link PreviewDocumentRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void previewDocumentAsync(PreviewDocumentRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PreviewDocumentResult(request.getDownloadPath()), cosXmlResultListener);
    }

    /**
     * <p>
     * 以HTML格式预览文档的同步方法。
     * </p>
     * @param request 以HTML格式预览文档的请求 {@link PreviewDocumentInHtmlRequest}
     * @return 以HTML格式预览文档的返回结果 {@link PreviewDocumentInHtmlResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public PreviewDocumentInHtmlResult previewDocumentInHtml(PreviewDocumentInHtmlRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PreviewDocumentInHtmlResult(request.getDownloadPath()));
    }

    /**
     * <p>
     * 以HTML格式预览文档的异步方法
     * </p>
     *
     * @param request 以HTML格式预览文档的请求 {@link PreviewDocumentInHtmlRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void previewDocumentInHtmlAsync(PreviewDocumentInHtmlRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PreviewDocumentInHtmlResult(request.getDownloadPath()), cosXmlResultListener);
    }

    /**
     * <p>
     * 以HTML格式链接预览文档的同步方法。
     * </p>
     * @param request 以HTML格式链接预览文档的请求 {@link PreviewDocumentInHtmlLinkRequest}
     * @return 以HTML格式链接预览文档的返回结果 {@link PreviewDocumentInHtmlLinkResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public PreviewDocumentInHtmlLinkResult previewDocumentInHtmlLink(PreviewDocumentInHtmlLinkRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PreviewDocumentInHtmlLinkResult());
    }

    /**
     * <p>
     * 以HTML格式链接预览文档的异步方法
     * </p>
     *
     * @param request 以HTML格式链接预览文档的请求 {@link PreviewDocumentInHtmlLinkRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void previewDocumentInHtmlLinkAsync(PreviewDocumentInHtmlLinkRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PreviewDocumentInHtmlLinkResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 以HTML格式直出内容预览文档到字节数组
     * <p>
     * 详细介绍，请查看:{@link CosXml#previewDocumentInHtmlBytes(String, String)}
     */
    @Override
    public byte[] previewDocumentInHtmlBytes(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException {
        PreviewDocumentInHtmlBytesRequest previewDocumentInHtmlBytesRequest = new PreviewDocumentInHtmlBytesRequest(bucketName, objectName);
        PreviewDocumentInHtmlBytesResult previewDocumentInHtmlBytesResult = execute(previewDocumentInHtmlBytesRequest, new PreviewDocumentInHtmlBytesResult());
        return previewDocumentInHtmlBytesResult != null ? previewDocumentInHtmlBytesResult.getData() : new byte[0];
    }

    /**
     * <p>
     * 以HTML格式直出内容预览文档到字节数组
     * <p>
     * 详细介绍，请查看:{@link CosXml#previewDocumentInHtmlBytes(String, String)}
     */
    @Override
    public byte[] previewDocumentInHtmlBytes(PreviewDocumentInHtmlBytesRequest request) throws CosXmlClientException, CosXmlServiceException {
        PreviewDocumentInHtmlBytesResult previewDocumentInHtmlBytesResult = execute(request, new PreviewDocumentInHtmlBytesResult());
        return previewDocumentInHtmlBytesResult != null ? previewDocumentInHtmlBytesResult.getData() : new byte[0];
    }

    public void formatConversionAsync(FormatConversionRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new FormatConversionResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取截图的同步方法
     * </p>
     *
     * @param request 获取截图的同步方法 {@link GetSnapshotRequest}
     * @return 获取截图的返回结果 {@link GetSnapshotResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetSnapshotResult getSnapshot(GetSnapshotRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetSnapshotResult());
    }

    /**
     * <p>
     * 获取截图的异步方法
     * </p>
     *
     * @param request 获取截图的异步方法 {@link GetSnapshotRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void getSnapshotAsync(GetSnapshotRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetSnapshotResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取媒体文件信息的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getMediaInfo(GetMediaInfoRequest)}
     */
    @Override
    public GetMediaInfoResult getMediaInfo(GetMediaInfoRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetMediaInfoResult());
    }

    /**
     * <p>
     * 获取媒体文件信息的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getMediaInfoAsync(GetMediaInfoRequest, CosXmlResultListener)}
     */
    @Override
    public void getMediaInfoAsync(GetMediaInfoRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetMediaInfoResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询已经开通媒体处理功能的存储桶的同步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getDescribeMediaBuckets(GetDescribeMediaBucketsRequest)}
     */
    @Override
    public GetDescribeMediaBucketsResult getDescribeMediaBuckets(GetDescribeMediaBucketsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetDescribeMediaBucketsResult());
    }

    /**
     * <p>
     * 查询已经开通媒体处理功能的存储桶的异步方法。
     * </p>
     *
     * 详细介绍，请查看: {@link CosXml#getDescribeMediaBucketsAsync(GetDescribeMediaBucketsRequest, CosXmlResultListener)}
     */
    @Override
    public void getDescribeMediaBucketsAsync(GetDescribeMediaBucketsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetDescribeMediaBucketsResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * GetPrivateM3U8 接口用于获取私有 M3U8 ts 资源的下载授权。（此方式通过对象存储转发请求至数据万象）的同步方法.&nbsp;
     * </p>
     *
     * @param request GetPrivateM3U8 接口用于获取私有 M3U8 ts 资源的下载授权。（此方式通过对象存储转发请求至数据万象）请求 {@link GetPrivateM3U8Request}
     * @return GetPrivateM3U8 接口用于获取私有 M3U8 ts 资源的下载授权。（此方式通过对象存储转发请求至数据万象）返回结果 {@link GetPrivateM3U8Result}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetPrivateM3U8Result getPrivateM3U8(GetPrivateM3U8Request request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetPrivateM3U8Result());
    }

    /**
     * <p>
     * GetPrivateM3U8 接口用于获取私有 M3U8 ts 资源的下载授权。（此方式通过对象存储转发请求至数据万象）的异步方法.&nbsp;
     * </p>
     *
     * @param request GetPrivateM3U8 接口用于获取私有 M3U8 ts 资源的下载授权。（此方式通过对象存储转发请求至数据万象）请求 {@link GetPrivateM3U8Request}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void getPrivateM3U8Async(GetPrivateM3U8Request request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetPrivateM3U8Result(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取工作流实例详情的同步方法.&nbsp;
     * </p>
     *
     * @param request 获取工作流实例详情请求 {@link GetWorkflowDetailRequest}
     * @return 获取工作流实例详情返回结果 {@link GetWorkflowDetailResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetWorkflowDetailResult getWorkflowDetail(GetWorkflowDetailRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetWorkflowDetailResult());
    }

    /**
     * <p>
     * 获取工作流实例详情的异步方法.&nbsp;
     * </p>
     *
     * @param request 获取工作流实例详情请求 {@link GetWorkflowDetailRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void getWorkflowDetailAsync(GetWorkflowDetailRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetWorkflowDetailResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AIImageColoring 接口对黑白图像进行上色的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AIImageColoring 接口对黑白图像进行上色请求 {@link AIImageColoringRequest}
     * @return 腾讯云数据万象通过 AIImageColoring 接口对黑白图像进行上色返回结果 {@link GetObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetObjectResult aiImageColoring(AIImageColoringRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetObjectResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AIImageColoring 接口对黑白图像进行上色的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AIImageColoring 接口对黑白图像进行上色请求 {@link AIImageColoringRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiImageColoringAsync(AIImageColoringRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AISuperResolution 接口对图像进行超分辨率处理，当前默认超分为宽高的2倍的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AISuperResolution 接口对图像进行超分辨率处理，当前默认超分为宽高的2倍请求 {@link AISuperResolutionRequest}
     * @return 腾讯云数据万象通过 AISuperResolution 接口对图像进行超分辨率处理，当前默认超分为宽高的2倍返回结果 {@link GetObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetObjectResult aiSuperResolution(AISuperResolutionRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetObjectResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AISuperResolution 接口对图像进行超分辨率处理，当前默认超分为宽高的2倍的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AISuperResolution 接口对图像进行超分辨率处理，当前默认超分为宽高的2倍请求 {@link AISuperResolutionRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiSuperResolutionAsync(AISuperResolutionRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AIImageCrop 接口对图像进行智能裁剪，支持持久化、云上处理及下载时处理的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AIImageCrop 接口对图像进行智能裁剪，支持持久化、云上处理及下载时处理请求 {@link AIImageCropRequest}
     * @return 腾讯云数据万象通过 AIImageCrop 接口对图像进行智能裁剪，支持持久化、云上处理及下载时处理返回结果 {@link GetObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetObjectResult aiImageCrop(AIImageCropRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetObjectResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AIImageCrop 接口对图像进行智能裁剪，支持持久化、云上处理及下载时处理的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AIImageCrop 接口对图像进行智能裁剪，支持持久化、云上处理及下载时处理请求 {@link AIImageCropRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiImageCropAsync(AIImageCropRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AIEnhanceImage 接口对图像进行增强处理的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AIEnhanceImage 接口对图像进行增强处理请求 {@link AIEnhanceImageRequest}
     * @return 腾讯云数据万象通过 AIEnhanceImage 接口对图像进行增强处理返回结果 {@link GetObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetObjectResult aiEnhanceImage(AIEnhanceImageRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetObjectResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AIEnhanceImage 接口对图像进行增强处理的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AIEnhanceImage 接口对图像进行增强处理请求 {@link AIEnhanceImageRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiEnhanceImageAsync(AIEnhanceImageRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 ImageRepair 接⼝能检测并擦除图片中常见的标志,并对擦除部分进行智能修复，此功能需携带签名的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 ImageRepair 接⼝能检测并擦除图片中常见的标志,并对擦除部分进行智能修复，此功能需携带签名请求 {@link ImageRepairRequest}
     * @return 腾讯云数据万象通过 ImageRepair 接⼝能检测并擦除图片中常见的标志,并对擦除部分进行智能修复，此功能需携带签名返回结果 {@link GetObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetObjectResult imageRepair(ImageRepairRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetObjectResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 ImageRepair 接⼝能检测并擦除图片中常见的标志,并对擦除部分进行智能修复，此功能需携带签名的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 ImageRepair 接⼝能检测并擦除图片中常见的标志,并对擦除部分进行智能修复，此功能需携带签名请求 {@link ImageRepairRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void imageRepairAsync(ImageRepairRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 RecognizeLogo 接口实现对图片内电商 Logo 的识别，返回图片中 Logo 的名称、坐标、置信度分值。，返回图片中Logo的名称、坐标、置信度分值。图片Logo识别请求包属于 GET 请求，请求时需要携带签名的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 RecognizeLogo 接口实现对图片内电商 Logo 的识别，返回图片中 Logo 的名称、坐标、置信度分值。，返回图片中Logo的名称、坐标、置信度分值。图片Logo识别请求包属于 GET 请求，请求时需要携带签名请求 {@link RecognizeLogoRequest}
     * @return 腾讯云数据万象通过 RecognizeLogo 接口实现对图片内电商 Logo 的识别，返回图片中 Logo 的名称、坐标、置信度分值。，返回图片中Logo的名称、坐标、置信度分值。图片Logo识别请求包属于 GET 请求，请求时需要携带签名返回结果 {@link RecognizeLogoResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public RecognizeLogoResult recognizeLogo(RecognizeLogoRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new RecognizeLogoResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 RecognizeLogo 接口实现对图片内电商 Logo 的识别，返回图片中 Logo 的名称、坐标、置信度分值。，返回图片中Logo的名称、坐标、置信度分值。图片Logo识别请求包属于 GET 请求，请求时需要携带签名的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 RecognizeLogo 接口实现对图片内电商 Logo 的识别，返回图片中 Logo 的名称、坐标、置信度分值。，返回图片中Logo的名称、坐标、置信度分值。图片Logo识别请求包属于 GET 请求，请求时需要携带签名请求 {@link RecognizeLogoRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void recognizeLogoAsync(RecognizeLogoRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new RecognizeLogoResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 数据万象二维码识别功能可识别图片中有效二维码的位置及内容，输出图像中二维码包含的文本信息（每个二维码对应的 URL 或文本），并可对识别出的二维码添加马赛克的同步方法.&nbsp;
     * </p>
     *
     * @param request 数据万象二维码识别功能可识别图片中有效二维码的位置及内容，输出图像中二维码包含的文本信息（每个二维码对应的 URL 或文本），并可对识别出的二维码添加马赛克请求 {@link RecognitionQRcodeRequest}
     * @return 数据万象二维码识别功能可识别图片中有效二维码的位置及内容，输出图像中二维码包含的文本信息（每个二维码对应的 URL 或文本），并可对识别出的二维码添加马赛克返回结果 {@link RecognitionQRcodeResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public RecognitionQRcodeResult recognitionQRcode(RecognitionQRcodeRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new RecognitionQRcodeResult());
    }

    /**
     * <p>
     * 数据万象二维码识别功能可识别图片中有效二维码的位置及内容，输出图像中二维码包含的文本信息（每个二维码对应的 URL 或文本），并可对识别出的二维码添加马赛克的异步方法.&nbsp;
     * </p>
     *
     * @param request 数据万象二维码识别功能可识别图片中有效二维码的位置及内容，输出图像中二维码包含的文本信息（每个二维码对应的 URL 或文本），并可对识别出的二维码添加马赛克请求 {@link RecognitionQRcodeRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void recognitionQRcodeAsync(RecognitionQRcodeRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new RecognitionQRcodeResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 数据万象二维码生成功能可根据用户指定的文本信息（URL 或文本），生成对应的二维码或条形码的同步方法.&nbsp;
     * </p>
     *
     * @param request 数据万象二维码生成功能可根据用户指定的文本信息（URL 或文本），生成对应的二维码或条形码请求 {@link CreateCRcodeRequest}
     * @return 数据万象二维码生成功能可根据用户指定的文本信息（URL 或文本），生成对应的二维码或条形码返回结果 {@link CreateCRcodeResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public CreateCRcodeResult createCRcode(CreateCRcodeRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CreateCRcodeResult());
    }

    /**
     * <p>
     * 数据万象二维码生成功能可根据用户指定的文本信息（URL 或文本），生成对应的二维码或条形码的异步方法.&nbsp;
     * </p>
     *
     * @param request 数据万象二维码生成功能可根据用户指定的文本信息（URL 或文本），生成对应的二维码或条形码请求 {@link CreateCRcodeRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void createCRcodeAsync(CreateCRcodeRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CreateCRcodeResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 图片标签功能通过借助数据万象的持久化处理接口，实现对 COS 存量数据的图片标签识别，返回图片中置信度较高的主题标签。图片标签识别请求包属于 GET 请求，请求时需要携带签名的同步方法.&nbsp;
     * </p>
     *
     * @param request 图片标签功能通过借助数据万象的持久化处理接口，实现对 COS 存量数据的图片标签识别，返回图片中置信度较高的主题标签。图片标签识别请求包属于 GET 请求，请求时需要携带签名请求 {@link DetectLabelRequest}
     * @return 图片标签功能通过借助数据万象的持久化处理接口，实现对 COS 存量数据的图片标签识别，返回图片中置信度较高的主题标签。图片标签识别请求包属于 GET 请求，请求时需要携带签名返回结果 {@link DetectLabelResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public DetectLabelResult detectLabel(DetectLabelRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DetectLabelResult());
    }

    /**
     * <p>
     * 图片标签功能通过借助数据万象的持久化处理接口，实现对 COS 存量数据的图片标签识别，返回图片中置信度较高的主题标签。图片标签识别请求包属于 GET 请求，请求时需要携带签名的异步方法.&nbsp;
     * </p>
     *
     * @param request 图片标签功能通过借助数据万象的持久化处理接口，实现对 COS 存量数据的图片标签识别，返回图片中置信度较高的主题标签。图片标签识别请求包属于 GET 请求，请求时需要携带签名请求 {@link DetectLabelRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void detectLabelAsync(DetectLabelRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DetectLabelResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 游戏标签功能实现游戏图片场景的识别，返回图片中置信度较高的游戏类别标签。游戏标签识别请求包属于 GET 请求，请求时需要携带签名的同步方法.&nbsp;
     * </p>
     *
     * @param request 游戏标签功能实现游戏图片场景的识别，返回图片中置信度较高的游戏类别标签。游戏标签识别请求包属于 GET 请求，请求时需要携带签名请求 {@link AIGameRecRequest}
     * @return 游戏标签功能实现游戏图片场景的识别，返回图片中置信度较高的游戏类别标签。游戏标签识别请求包属于 GET 请求，请求时需要携带签名返回结果 {@link AIGameRecResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AIGameRecResult aiGameRec(AIGameRecRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AIGameRecResult());
    }

    /**
     * <p>
     * 游戏标签功能实现游戏图片场景的识别，返回图片中置信度较高的游戏类别标签。游戏标签识别请求包属于 GET 请求，请求时需要携带签名的异步方法.&nbsp;
     * </p>
     *
     * @param request 游戏标签功能实现游戏图片场景的识别，返回图片中置信度较高的游戏类别标签。游戏标签识别请求包属于 GET 请求，请求时需要携带签名请求 {@link AIGameRecRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiGameRecAsync(AIGameRecRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AIGameRecResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 图片质量评分功能为同步请求方式，您可以通过本接口对图片文件进行检测，从多方面评估，最终给出综合可观的清晰度评分和主观的美观度评分。该接口属于 GET 请求的同步方法.&nbsp;
     * </p>
     *
     * @param request 图片质量评分功能为同步请求方式，您可以通过本接口对图片文件进行检测，从多方面评估，最终给出综合可观的清晰度评分和主观的美观度评分。该接口属于 GET 请求请求 {@link AssessQualityRequest}
     * @return 图片质量评分功能为同步请求方式，您可以通过本接口对图片文件进行检测，从多方面评估，最终给出综合可观的清晰度评分和主观的美观度评分。该接口属于 GET 请求返回结果 {@link AssessQualityResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AssessQualityResult assessQuality(AssessQualityRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AssessQualityResult());
    }

    /**
     * <p>
     * 图片质量评分功能为同步请求方式，您可以通过本接口对图片文件进行检测，从多方面评估，最终给出综合可观的清晰度评分和主观的美观度评分。该接口属于 GET 请求的异步方法.&nbsp;
     * </p>
     *
     * @param request 图片质量评分功能为同步请求方式，您可以通过本接口对图片文件进行检测，从多方面评估，最终给出综合可观的清晰度评分和主观的美观度评分。该接口属于 GET 请求请求 {@link AssessQualityRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void assessQualityAsync(AssessQualityRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AssessQualityResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 人脸检测功能为同步请求方式，您可以通过本接口检测图片中的人脸位置。该接口属于 GET 请求的同步方法.&nbsp;
     * </p>
     *
     * @param request 人脸检测功能为同步请求方式，您可以通过本接口检测图片中的人脸位置。该接口属于 GET 请求请求 {@link AIDetectFaceRequest}
     * @return 人脸检测功能为同步请求方式，您可以通过本接口检测图片中的人脸位置。该接口属于 GET 请求返回结果 {@link AIDetectFaceResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AIDetectFaceResult aiDetectFace(AIDetectFaceRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AIDetectFaceResult());
    }

    /**
     * <p>
     * 人脸检测功能为同步请求方式，您可以通过本接口检测图片中的人脸位置。该接口属于 GET 请求的异步方法.&nbsp;
     * </p>
     *
     * @param request 人脸检测功能为同步请求方式，您可以通过本接口检测图片中的人脸位置。该接口属于 GET 请求请求 {@link AIDetectFaceRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiDetectFaceAsync(AIDetectFaceRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AIDetectFaceResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 人脸特效，提供人脸美颜、人像变换、人像分割功能的同步方法.&nbsp;
     * </p>
     *
     * @param request 人脸特效，提供人脸美颜、人像变换、人像分割功能请求 {@link AIFaceEffectRequest}
     * @return 人脸特效，提供人脸美颜、人像变换、人像分割功能返回结果 {@link AIFaceEffectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AIFaceEffectResult aiFaceEffect(AIFaceEffectRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AIFaceEffectResult());
    }

    /**
     * <p>
     * 人脸特效，提供人脸美颜、人像变换、人像分割功能的异步方法.&nbsp;
     * </p>
     *
     * @param request 人脸特效，提供人脸美颜、人像变换、人像分割功能请求 {@link AIFaceEffectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiFaceEffectAsync(AIFaceEffectRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AIFaceEffectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AIBodyRecognition 接口识别并输出画面中人体，输出其位置（矩形框）和置信度。图片人体识别请求包属于 GET 请求，请求时需要携带签名的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AIBodyRecognition 接口识别并输出画面中人体，输出其位置（矩形框）和置信度。图片人体识别请求包属于 GET 请求，请求时需要携带签名请求 {@link AIBodyRecognitionRequest}
     * @return 腾讯云数据万象通过 AIBodyRecognition 接口识别并输出画面中人体，输出其位置（矩形框）和置信度。图片人体识别请求包属于 GET 请求，请求时需要携带签名返回结果 {@link AIBodyRecognitionResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AIBodyRecognitionResult aiBodyRecognition(AIBodyRecognitionRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AIBodyRecognitionResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AIBodyRecognition 接口识别并输出画面中人体，输出其位置（矩形框）和置信度。图片人体识别请求包属于 GET 请求，请求时需要携带签名的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AIBodyRecognition 接口识别并输出画面中人体，输出其位置（矩形框）和置信度。图片人体识别请求包属于 GET 请求，请求时需要携带签名请求 {@link AIBodyRecognitionRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiBodyRecognitionAsync(AIBodyRecognitionRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AIBodyRecognitionResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 detect-pet 接口识别并输出画面中宠物，输出其位置（矩形框）和置信度。图片宠物识别请求包属于 GET 请求，请求时需要携带签名的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 detect-pet 接口识别并输出画面中宠物，输出其位置（矩形框）和置信度。图片宠物识别请求包属于 GET 请求，请求时需要携带签名请求 {@link AIDetectPetRequest}
     * @return 腾讯云数据万象通过 detect-pet 接口识别并输出画面中宠物，输出其位置（矩形框）和置信度。图片宠物识别请求包属于 GET 请求，请求时需要携带签名返回结果 {@link AIDetectPetResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AIDetectPetResult aiDetectPet(AIDetectPetRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AIDetectPetResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 detect-pet 接口识别并输出画面中宠物，输出其位置（矩形框）和置信度。图片宠物识别请求包属于 GET 请求，请求时需要携带签名的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 detect-pet 接口识别并输出画面中宠物，输出其位置（矩形框）和置信度。图片宠物识别请求包属于 GET 请求，请求时需要携带签名请求 {@link AIDetectPetRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiDetectPetAsync(AIDetectPetRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AIDetectPetResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 支持中国大陆居民二代身份证正反面所有字段的识别，包括姓名、性别、民族、出生日期、住址、公民身份证号、签发机关、有效期限；具备身份证照片、人像照片的裁剪功能和翻拍、PS、复印件告警功能，以及边框和框内遮挡告警、临时身份证告警和身份证有效期不合法告警等扩展功能的同步方法.&nbsp;
     * </p>
     *
     * @param request 支持中国大陆居民二代身份证正反面所有字段的识别，包括姓名、性别、民族、出生日期、住址、公民身份证号、签发机关、有效期限；具备身份证照片、人像照片的裁剪功能和翻拍、PS、复印件告警功能，以及边框和框内遮挡告警、临时身份证告警和身份证有效期不合法告警等扩展功能请求 {@link AIIDCardOCRRequest}
     * @return 支持中国大陆居民二代身份证正反面所有字段的识别，包括姓名、性别、民族、出生日期、住址、公民身份证号、签发机关、有效期限；具备身份证照片、人像照片的裁剪功能和翻拍、PS、复印件告警功能，以及边框和框内遮挡告警、临时身份证告警和身份证有效期不合法告警等扩展功能返回结果 {@link AIIDCardOCRResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AIIDCardOCRResult aiIDCardOCR(AIIDCardOCRRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AIIDCardOCRResult());
    }

    /**
     * <p>
     * 支持中国大陆居民二代身份证正反面所有字段的识别，包括姓名、性别、民族、出生日期、住址、公民身份证号、签发机关、有效期限；具备身份证照片、人像照片的裁剪功能和翻拍、PS、复印件告警功能，以及边框和框内遮挡告警、临时身份证告警和身份证有效期不合法告警等扩展功能的异步方法.&nbsp;
     * </p>
     *
     * @param request 支持中国大陆居民二代身份证正反面所有字段的识别，包括姓名、性别、民族、出生日期、住址、公民身份证号、签发机关、有效期限；具备身份证照片、人像照片的裁剪功能和翻拍、PS、复印件告警功能，以及边框和框内遮挡告警、临时身份证告警和身份证有效期不合法告警等扩展功能请求 {@link AIIDCardOCRRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiIDCardOCRAsync(AIIDCardOCRRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AIIDCardOCRResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 集成了活体检测和跟权威库进行比对的能力，传入一段视频和姓名、身份证号信息即可进行验证。对录制的自拍视频进行活体检测，从而确认当前用户为真人，可防止照片、视频、静态3D建模等各种不同类型的攻击。检测为真人后，再判断该视频中的人与权威库的证件照是否属于同一个人，实现用户身份信息核实的同步方法.&nbsp;
     * </p>
     *
     * @param request 集成了活体检测和跟权威库进行比对的能力，传入一段视频和姓名、身份证号信息即可进行验证。对录制的自拍视频进行活体检测，从而确认当前用户为真人，可防止照片、视频、静态3D建模等各种不同类型的攻击。检测为真人后，再判断该视频中的人与权威库的证件照是否属于同一个人，实现用户身份信息核实请求 {@link LivenessRecognitionRequest}
     * @return 集成了活体检测和跟权威库进行比对的能力，传入一段视频和姓名、身份证号信息即可进行验证。对录制的自拍视频进行活体检测，从而确认当前用户为真人，可防止照片、视频、静态3D建模等各种不同类型的攻击。检测为真人后，再判断该视频中的人与权威库的证件照是否属于同一个人，实现用户身份信息核实返回结果 {@link LivenessRecognitionResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public LivenessRecognitionResult livenessRecognition(LivenessRecognitionRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new LivenessRecognitionResult());
    }

    /**
     * <p>
     * 集成了活体检测和跟权威库进行比对的能力，传入一段视频和姓名、身份证号信息即可进行验证。对录制的自拍视频进行活体检测，从而确认当前用户为真人，可防止照片、视频、静态3D建模等各种不同类型的攻击。检测为真人后，再判断该视频中的人与权威库的证件照是否属于同一个人，实现用户身份信息核实的异步方法.&nbsp;
     * </p>
     *
     * @param request 集成了活体检测和跟权威库进行比对的能力，传入一段视频和姓名、身份证号信息即可进行验证。对录制的自拍视频进行活体检测，从而确认当前用户为真人，可防止照片、视频、静态3D建模等各种不同类型的攻击。检测为真人后，再判断该视频中的人与权威库的证件照是否属于同一个人，实现用户身份信息核实请求 {@link LivenessRecognitionRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void livenessRecognitionAsync(LivenessRecognitionRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new LivenessRecognitionResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 使用数字活体检测模式前，需调用本接口获取数字验证码的同步方法.&nbsp;
     * </p>
     *
     * @param request 使用数字活体检测模式前，需调用本接口获取数字验证码请求 {@link GetLiveCodeRequest}
     * @return 使用数字活体检测模式前，需调用本接口获取数字验证码返回结果 {@link GetLiveCodeResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetLiveCodeResult getLiveCode(GetLiveCodeRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetLiveCodeResult());
    }

    /**
     * <p>
     * 使用数字活体检测模式前，需调用本接口获取数字验证码的异步方法.&nbsp;
     * </p>
     *
     * @param request 使用数字活体检测模式前，需调用本接口获取数字验证码请求 {@link GetLiveCodeRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void getLiveCodeAsync(GetLiveCodeRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetLiveCodeResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 使用动作活体检测模式前，需调用本接口获取动作顺序的同步方法.&nbsp;
     * </p>
     *
     * @param request 使用动作活体检测模式前，需调用本接口获取动作顺序请求 {@link GetActionSequenceRequest}
     * @return 使用动作活体检测模式前，需调用本接口获取动作顺序返回结果 {@link GetActionSequenceResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetActionSequenceResult getActionSequence(GetActionSequenceRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetActionSequenceResult());
    }

    /**
     * <p>
     * 使用动作活体检测模式前，需调用本接口获取动作顺序的异步方法.&nbsp;
     * </p>
     *
     * @param request 使用动作活体检测模式前，需调用本接口获取动作顺序请求 {@link GetActionSequenceRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void getActionSequenceAsync(GetActionSequenceRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetActionSequenceResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 车辆车牌检测功能为同步请求方式，您可以通过本接口检测图片中的车辆，识别出车辆的品牌、颜色、位置、车牌位置等信息。该接口属于 GET 请求的同步方法.&nbsp;
     * </p>
     *
     * @param request 车辆车牌检测功能为同步请求方式，您可以通过本接口检测图片中的车辆，识别出车辆的品牌、颜色、位置、车牌位置等信息。该接口属于 GET 请求请求 {@link AIDetectCarRequest}
     * @return 车辆车牌检测功能为同步请求方式，您可以通过本接口检测图片中的车辆，识别出车辆的品牌、颜色、位置、车牌位置等信息。该接口属于 GET 请求返回结果 {@link AIDetectCarResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AIDetectCarResult aiDetectCar(AIDetectCarRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AIDetectCarResult());
    }

    /**
     * <p>
     * 车辆车牌检测功能为同步请求方式，您可以通过本接口检测图片中的车辆，识别出车辆的品牌、颜色、位置、车牌位置等信息。该接口属于 GET 请求的异步方法.&nbsp;
     * </p>
     *
     * @param request 车辆车牌检测功能为同步请求方式，您可以通过本接口检测图片中的车辆，识别出车辆的品牌、颜色、位置、车牌位置等信息。该接口属于 GET 请求请求 {@link AIDetectCarRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiDetectCarAsync(AIDetectCarRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AIDetectCarResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 通用文字识别功能（Optical Character Recognition，OCR）基于行业前沿的深度学习技术，将图片上的文字内容，智能识别为可编辑的文本，可应用于随手拍扫描、纸质文档电子化、电商广告审核等多种场景，大幅提升信息处理效率的同步方法.&nbsp;
     * </p>
     *
     * @param request 通用文字识别功能（Optical Character Recognition，OCR）基于行业前沿的深度学习技术，将图片上的文字内容，智能识别为可编辑的文本，可应用于随手拍扫描、纸质文档电子化、电商广告审核等多种场景，大幅提升信息处理效率请求 {@link COSOCRRequest}
     * @return 通用文字识别功能（Optical Character Recognition，OCR）基于行业前沿的深度学习技术，将图片上的文字内容，智能识别为可编辑的文本，可应用于随手拍扫描、纸质文档电子化、电商广告审核等多种场景，大幅提升信息处理效率返回结果 {@link COSOCRResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public COSOCRResult cOSOCR(COSOCRRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new COSOCRResult());
    }

    /**
     * <p>
     * 通用文字识别功能（Optical Character Recognition，OCR）基于行业前沿的深度学习技术，将图片上的文字内容，智能识别为可编辑的文本，可应用于随手拍扫描、纸质文档电子化、电商广告审核等多种场景，大幅提升信息处理效率的异步方法.&nbsp;
     * </p>
     *
     * @param request 通用文字识别功能（Optical Character Recognition，OCR）基于行业前沿的深度学习技术，将图片上的文字内容，智能识别为可编辑的文本，可应用于随手拍扫描、纸质文档电子化、电商广告审核等多种场景，大幅提升信息处理效率请求 {@link COSOCRRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void cOSOCRAsync(COSOCRRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new COSOCRResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 本接口支持中国大陆居民二代身份证正面（暂不支持背面）、驾驶证主页（暂不支持副页）所有字段的自动定位，暂不支持文本识别，用于对特定字段的抹除、屏蔽，以及进一步的文本识别的同步方法.&nbsp;
     * </p>
     *
     * @param request 本接口支持中国大陆居民二代身份证正面（暂不支持背面）、驾驶证主页（暂不支持副页）所有字段的自动定位，暂不支持文本识别，用于对特定字段的抹除、屏蔽，以及进一步的文本识别请求 {@link AILicenseRecRequest}
     * @return 本接口支持中国大陆居民二代身份证正面（暂不支持背面）、驾驶证主页（暂不支持副页）所有字段的自动定位，暂不支持文本识别，用于对特定字段的抹除、屏蔽，以及进一步的文本识别返回结果 {@link AILicenseRecResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AILicenseRecResult aiLicenseRec(AILicenseRecRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AILicenseRecResult());
    }

    /**
     * <p>
     * 本接口支持中国大陆居民二代身份证正面（暂不支持背面）、驾驶证主页（暂不支持副页）所有字段的自动定位，暂不支持文本识别，用于对特定字段的抹除、屏蔽，以及进一步的文本识别的异步方法.&nbsp;
     * </p>
     *
     * @param request 本接口支持中国大陆居民二代身份证正面（暂不支持背面）、驾驶证主页（暂不支持副页）所有字段的自动定位，暂不支持文本识别，用于对特定字段的抹除、屏蔽，以及进一步的文本识别请求 {@link AILicenseRecRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void aiLicenseRecAsync(AILicenseRecRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AILicenseRecResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 GoodsMatting 接口检测图片中的商品信息，生成只包含商品信息的图片，支持持久化、云上处理及下载时处理的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 GoodsMatting 接口检测图片中的商品信息，生成只包含商品信息的图片，支持持久化、云上处理及下载时处理请求 {@link GoodsMattingRequest}
     * @return 腾讯云数据万象通过 GoodsMatting 接口检测图片中的商品信息，生成只包含商品信息的图片，支持持久化、云上处理及下载时处理返回结果 {@link GetObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetObjectResult goodsMatting(GoodsMattingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetObjectResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 GoodsMatting 接口检测图片中的商品信息，生成只包含商品信息的图片，支持持久化、云上处理及下载时处理的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 GoodsMatting 接口检测图片中的商品信息，生成只包含商品信息的图片，支持持久化、云上处理及下载时处理请求 {@link GoodsMattingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void goodsMattingAsync(GoodsMattingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 该接口用于添加图库图片的同步方法.&nbsp;
     * </p>
     *
     * @param request 该接口用于添加图库图片请求 {@link AddImageSearchRequest}
     * @return 该接口用于添加图库图片返回结果 {@link EmptyResponseResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public EmptyResponseResult addImageSearch(AddImageSearchRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new EmptyResponseResult());
    }

    /**
     * <p>
     * 该接口用于添加图库图片的异步方法.&nbsp;
     * </p>
     *
     * @param request 该接口用于添加图库图片请求 {@link AddImageSearchRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void addImageSearchAsync(AddImageSearchRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new EmptyResponseResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 该接口用于检索图片的同步方法.&nbsp;
     * </p>
     *
     * @param request 该接口用于检索图片请求 {@link GetSearchImageRequest}
     * @return 该接口用于检索图片返回结果 {@link GetSearchImageResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public GetSearchImageResult getSearchImage(GetSearchImageRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetSearchImageResult());
    }

    /**
     * <p>
     * 该接口用于检索图片的异步方法.&nbsp;
     * </p>
     *
     * @param request 该接口用于检索图片请求 {@link GetSearchImageRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void getSearchImageAsync(GetSearchImageRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetSearchImageResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 该接口用于删除图库图片的同步方法.&nbsp;
     * </p>
     *
     * @param request 该接口用于删除图库图片请求 {@link DeleteImageSearchRequest}
     * @return 该接口用于删除图库图片返回结果 {@link EmptyResponseResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public EmptyResponseResult deleteImageSearch(DeleteImageSearchRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new EmptyResponseResult());
    }

    /**
     * <p>
     * 该接口用于删除图库图片的异步方法.&nbsp;
     * </p>
     *
     * @param request 该接口用于删除图库图片请求 {@link DeleteImageSearchRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void deleteImageSearchAsync(DeleteImageSearchRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new EmptyResponseResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AutoTranslationBlock  接口对文字块进行翻译，请求时需要携带签名的同步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AutoTranslationBlock  接口对文字块进行翻译，请求时需要携带签名请求 {@link AutoTranslationBlockRequest}
     * @return 腾讯云数据万象通过 AutoTranslationBlock  接口对文字块进行翻译，请求时需要携带签名返回结果 {@link AutoTranslationBlockResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Override
    public AutoTranslationBlockResult autoTranslationBlock(AutoTranslationBlockRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AutoTranslationBlockResult());
    }

    /**
     * <p>
     * 腾讯云数据万象通过 AutoTranslationBlock  接口对文字块进行翻译，请求时需要携带签名的异步方法.&nbsp;
     * </p>
     *
     * @param request 腾讯云数据万象通过 AutoTranslationBlock  接口对文字块进行翻译，请求时需要携带签名请求 {@link AutoTranslationBlockRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    @Override
    public void autoTranslationBlockAsync(AutoTranslationBlockRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AutoTranslationBlockResult(), cosXmlResultListener);
    }


    @Override
    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> boolean buildHttpRequestBodyConverter(T1 cosXmlRequest, T2 cosXmlResult, QCloudHttpRequest.Builder<T2> httpRequestBuilder) {

        if (cosXmlRequest instanceof SelectObjectContentRequest) {

            SelectObjectContentRequest selectObjectContentRequest = (SelectObjectContentRequest) cosXmlRequest;
            SelectObjectContentConverter<T2> selectObjectContentConverter = new SelectObjectContentConverter<T2>((SelectObjectContentResult) cosXmlResult,
                    selectObjectContentRequest.getSelectResponseFilePath());
            selectObjectContentConverter.setContentListener(selectObjectContentRequest.getSelectObjectContentProgressListener());
            httpRequestBuilder.converter(selectObjectContentConverter);

            return true;
        }
        return super.buildHttpRequestBodyConverter(cosXmlRequest, cosXmlResult, httpRequestBuilder);
    }

    @Override
    protected String getRequestHost(CosXmlRequest request) throws CosXmlClientException {

        if (request instanceof GetServiceRequest && !TextUtils.isEmpty(getServiceRequestDomain)) {
            return getServiceRequestDomain;
        } else {
            return super.getRequestHost(request);
        }
    }

    /**
     * 设置 get service 请求的域名
     *
     * @param domain GetService 请求的域名
     */
    public void setServiceDomain(String domain) {

        this.getServiceRequestDomain = domain;
    }
}
