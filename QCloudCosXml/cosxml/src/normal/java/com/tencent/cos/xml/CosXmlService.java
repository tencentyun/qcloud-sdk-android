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
import com.tencent.cos.xml.model.bucket.DeleteBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketCORSResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketInventoryRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketInventoryResult;
import com.tencent.cos.xml.model.bucket.DeleteBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.DeleteBucketLifecycleResult;
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
import com.tencent.cos.xml.model.bucket.GetBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.GetBucketCORSResult;
import com.tencent.cos.xml.model.bucket.GetBucketDomainRequest;
import com.tencent.cos.xml.model.bucket.GetBucketDomainResult;
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
import com.tencent.cos.xml.model.bucket.ListMultiUploadsRequest;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsResult;
import com.tencent.cos.xml.model.bucket.PutBucketACLRequest;
import com.tencent.cos.xml.model.bucket.PutBucketACLResult;
import com.tencent.cos.xml.model.bucket.PutBucketCORSRequest;
import com.tencent.cos.xml.model.bucket.PutBucketCORSResult;
import com.tencent.cos.xml.model.bucket.PutBucketDomainRequest;
import com.tencent.cos.xml.model.bucket.PutBucketDomainResult;
import com.tencent.cos.xml.model.bucket.PutBucketInventoryRequest;
import com.tencent.cos.xml.model.bucket.PutBucketInventoryResult;
import com.tencent.cos.xml.model.bucket.PutBucketLifecycleRequest;
import com.tencent.cos.xml.model.bucket.PutBucketLifecycleResult;
import com.tencent.cos.xml.model.bucket.PutBucketLoggingRequest;
import com.tencent.cos.xml.model.bucket.PutBucketLoggingResult;
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
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.DeleteMultiObjectRequest;
import com.tencent.cos.xml.model.object.DeleteMultiObjectResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectACLRequest;
import com.tencent.cos.xml.model.object.GetObjectACLResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.OptionObjectRequest;
import com.tencent.cos.xml.model.object.OptionObjectResult;
import com.tencent.cos.xml.model.object.PutObjectACLRequest;
import com.tencent.cos.xml.model.object.PutObjectACLResult;
import com.tencent.cos.xml.model.object.RestoreRequest;
import com.tencent.cos.xml.model.object.RestoreResult;
import com.tencent.cos.xml.model.object.SelectObjectContentRequest;
import com.tencent.cos.xml.model.object.SelectObjectContentResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.cos.xml.model.tag.COSMetaData;
import com.tencent.cos.xml.transfer.SelectObjectContentConverter;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.http.QCloudHttpRequest;

/**
 * Created by bradyxiao on 2017/11/30.
 */

public class CosXmlService extends CosXmlSimpleService implements CosXml {

    private String getServiceRequestDomain;

    /**
     * cos android SDK 服务
     * @param context Application 上下文{@link android.app.Application}
     * @param configuration cos android SDK 服务配置{@link CosXmlServiceConfig}
     * @param qCloudCredentialProvider cos android SDK 签名提供者 {@link QCloudCredentialProvider}
     */
    public CosXmlService(Context context, CosXmlServiceConfig configuration, QCloudCredentialProvider qCloudCredentialProvider){
        super(context, configuration, qCloudCredentialProvider);
    }

    public CosXmlService(Context context, CosXmlServiceConfig configuration, QCloudSigner qCloudSigner) {
        super(context, configuration, qCloudSigner);
    }

    public CosXmlService(Context context, CosXmlServiceConfig configuration) {
        super(context, configuration);
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

//    /**
//     * <p>
//     * 追加上传.&nbsp;
//     * 已被放弃支持.
//     * </p>
//     * @param request 追加上传请求 {@link AppendObjectRequest}
//     * @return AppendObjectResult 追加上传请求返回的结果 {@link AppendObjectResult}
//     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
//     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
//     */
//    @Deprecated
//    @Override
//    public AppendObjectResult appendObject(AppendObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
//        AppendObjectResult appendObjectResult =  new AppendObjectResult();
//        appendObjectResult.accessUrl = getAccessUrl(request);
//        return execute(request, appendObjectResult);
//    }
//
//    /**
//     * <p>
//     * 追加上传.&nbsp;
//     * 已被放弃支持.
//     * </p>
//     * @param request 追加上传请求 {@link AppendObjectRequest}
//     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
//     */
//    @Deprecated
//    @Override
//    public void appendObjectAsync(AppendObjectRequest request, CosXmlResultListener cosXmlResultListener) {
//        AppendObjectResult appendObjectResult =  new AppendObjectResult();
//        appendObjectResult.accessUrl = getAccessUrl(request);
//        schedule(request, appendObjectResult, cosXmlResultListener);
//    }

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
     *
     * 详细介绍，请查看:{@link  CosXml#deleteBucketCORS(DeleteBucketCORSRequest request)}
     *</p>
     */
    @Override
    public DeleteBucketCORSResult deleteBucketCORS(DeleteBucketCORSRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketCORSResult());
    }

    /**
     * <p>
     * 删除跨域访问配置信息的异步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#deleteBucketCORSAsync(DeleteBucketCORSRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void deleteBucketCORSAsync(DeleteBucketCORSRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketCORSResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除存储桶（Bucket） 的生命周期配置的同步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#deleteBucketLifecycle(DeleteBucketLifecycleRequest request)}
     *</p>
     */
    @Override
    public DeleteBucketLifecycleResult deleteBucketLifecycle(DeleteBucketLifecycleRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketLifecycleResult());
    }

    /**
     * <p>
     * 删除存储桶（Bucket） 的生命周期配置的异步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#deleteBucketLifecycleAsync(DeleteBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void deleteBucketLifecycleAsync(DeleteBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketLifecycleResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 删除存储桶 (Bucket)的同步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#deleteBucket(DeleteBucketRequest request)}
     *</p>
     */
    @Override
    public DeleteBucketResult deleteBucket(DeleteBucketRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketResult());
    }

    /**
     * <p>
     * 删除存储桶 (Bucket)的异步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#deleteBucketAsync(DeleteBucketRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
     * 详细介绍，请查看:{@link  CosXml#headBucketAsync(HeadBucketRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void headBucketAsync(HeadBucketRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new HeadBucketResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询存储桶（Bucket）中正在进行中的分块上传对象的同步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#listMultiUploads(ListMultiUploadsRequest request)}
     *</p>
     */
    @Override
    public ListMultiUploadsResult listMultiUploads(ListMultiUploadsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new ListMultiUploadsResult());
    }

    /**
     * <p>
     * 查询存储桶（Bucket）中正在进行中的分块上传对象的异步方法.&nbsp;
     *
     * 详细介绍，请查看:{@link  CosXml#listMultiUploadsAsync(ListMultiUploadsRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void listMultiUploadsAsync(ListMultiUploadsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new ListMultiUploadsResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置存储桶（Bucket） 的访问权限（Access Control List, ACL)的同步方法.&nbsp;
     *
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
     *
     * 详细介绍，请查看:{@link  CosXml#putBucketACLAsync(PutBucketACLRequest request, CosXmlResultListener cosXmlResultListener)}
     *</p>
     */
    @Override
    public void putBucketACLAsync(PutBucketACLRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketACLResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 设置存储桶（Bucket） 的跨域配置信息的同步方法.&nbsp;
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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

    @Override
    public void doesBucketExistAsync(final String bucketName, final CosXmlBooleanListener booleanListener) {

        GetBucketACLRequest getBucketACLRequest = new GetBucketACLRequest(bucketName);

        getBucketACLAsync(getBucketACLRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                booleanListener.onSuccess(true);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {

                if (clientException == null && serviceException != null) {

                    if ((serviceException.getStatusCode() == Constants.BUCKET_REDIRECT_STATUS_CODE)
                            || "AccessDenied".equals(serviceException.getErrorCode())) {
                        booleanListener.onSuccess(true);
                    }

                    if (serviceException.getStatusCode() == Constants.NO_SUCH_BUCKET_STATUS_CODE) {
                        booleanListener.onSuccess(false);
                    }
                }

                booleanListener.onFail(clientException, serviceException);
            }
        });
    }

    /**
     * <p>
     * 通过封装 HeadObjectRequest 请求来判断对象是否存在。
     * </p>
     *
     * <p>
     * 注意只能在单个 region 下判断，无法一次性判断所有的 region 。
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


    @Override
    public boolean deleteObject(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException {

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, objectName);
        deleteObject(deleteObjectRequest);
        return true;
    }


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
     * 利用 CopyObjectRequest 来更新对象的 meta。
     * </p>
     *
     */
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
     * 利用 CopyObjectRequest 来更新对象的 meta 的异步方法。
     * </p>
     */
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

    @Override
    public PutBucketWebsiteResult putBucketWebsite(PutBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketWebsiteResult());
    }

    @Override
    public void putBucketWebsiteAsync(PutBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketWebsiteResult(), cosXmlResultListener);
    }

    @Override
    public GetBucketWebsiteResult getBucketWebsite(GetBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketWebsiteResult());
    }

    @Override
    public void getBucketWebsiteAsync(GetBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketWebsiteResult(), cosXmlResultListener);
    }

    @Override
    public DeleteBucketWebsiteResult deleteBucketWebsite(DeleteBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketWebsiteResult());
    }

    @Override
    public void deleteBucketWebsiteAsync(DeleteBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketWebsiteResult(), cosXmlResultListener);
    }

    @Override
    public PutBucketLoggingResult putBucketLogging(PutBucketLoggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketLoggingResult());
    }

    @Override
    public void putBucketLoggingAsync(PutBucketLoggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketLoggingResult(), cosXmlResultListener);
    }

    @Override
    public PutBucketTaggingResult putBucketTagging(PutBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketTaggingResult());
    }

    @Override
    public void putBucketTaggingAsync(PutBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketTaggingResult(), cosXmlResultListener);
    }

    @Override
    public GetBucketTaggingResult getBucketTagging(GetBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketTaggingResult());
    }

    @Override
    public void getBucketTaggingAsync(GetBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketTaggingResult(), cosXmlResultListener);
    }

    @Override
    public DeleteBucketTaggingResult deleteBucketTagging(DeleteBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketTaggingResult());
    }

    @Override
    public void deleteBucketTaggingAsync(DeleteBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketTaggingResult(), cosXmlResultListener);
    }

    @Override
    public GetBucketLoggingResult getBucketLogging(GetBucketLoggingRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketLoggingResult());
    }

    @Override
    public void getBucketLoggingAsync(GetBucketLoggingRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketLoggingResult(), cosXmlResultListener);
    }

    @Override
    public PutBucketInventoryResult putBucketInventory(PutBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketInventoryResult());
    }

    @Override
    public void putBucketInventoryAsync(PutBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketInventoryResult(), cosXmlResultListener);
    }

    @Override
    public GetBucketInventoryResult getBucketInventory(GetBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketInventoryResult());
    }

    @Override
    public void getBucketInventoryAsync(GetBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketInventoryResult(), cosXmlResultListener);
    }

    @Override
    public DeleteBucketInventoryResult deleteBucketInventory(DeleteBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketInventoryResult());
    }

    @Override
    public void deleteBucketInventoryAsync(DeleteBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteBucketInventoryResult(), cosXmlResultListener);
    }

    @Override
    public ListBucketInventoryResult listBucketInventory(ListBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new ListBucketInventoryResult());
    }

    @Override
    public void listBucketInventoryAsync(ListBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new ListBucketInventoryResult(), cosXmlResultListener);
    }

    @Override
    public GetBucketDomainResult getBucketDomain(GetBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new GetBucketDomainResult());
    }

    @Override
    public void getBucketDomainAsync(GetBucketDomainRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new GetBucketDomainResult(), cosXmlResultListener);
    }

    @Override
    public PutBucketDomainResult putBucketDomain(PutBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new PutBucketDomainResult());
    }

    @Override
    public void putBucketDomainAsync(PutBucketDomainRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new PutBucketDomainResult(), cosXmlResultListener);
    }

    @Override
    public SelectObjectContentResult selectObjectContent(SelectObjectContentRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SelectObjectContentResult());
    }

    @Override
    public void selectObjectContentAsync(SelectObjectContentRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new SelectObjectContentResult(), cosXmlResultListener);
    }

    @Override
    public GetBucketObjectVersionsResult getBucketObjectVersions(GetBucketObjectVersionsRequest getBucketObjectVersionsRequest) throws CosXmlClientException, CosXmlServiceException {
        return execute(getBucketObjectVersionsRequest, new GetBucketObjectVersionsResult());
    }

    @Override
    public void getBucketObjectVersionsAsync(GetBucketObjectVersionsRequest getBucketObjectVersionsRequest, CosXmlResultListener cosXmlResultListener) {
        schedule(getBucketObjectVersionsRequest, new GetBucketObjectVersionsResult(), cosXmlResultListener);
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
    protected String getRequestHost(CosXmlRequest request, boolean isHeader) throws CosXmlClientException {

        if (request instanceof GetServiceRequest && !TextUtils.isEmpty(getServiceRequestDomain)) {
            return getServiceRequestDomain;
        } else {
            return super.getRequestHost(request, isHeader);
        }
    }

    /**
     * 设置 get service 请求的域名
     *
     * @param domain
     */
    public void setServiceDomain(String domain) {

        this.getServiceRequestDomain = domain;
    }

}
