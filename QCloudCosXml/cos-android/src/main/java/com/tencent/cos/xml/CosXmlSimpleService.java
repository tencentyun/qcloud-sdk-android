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

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.listener.CosXmlResultSimpleListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsRequest;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.AbortMultiUploadResult;
import com.tencent.cos.xml.model.object.AppendObjectRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.CopyObjectResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.PostObjectRequest;
import com.tencent.cos.xml.model.object.PostObjectResult;
import com.tencent.cos.xml.model.object.PreBuildConnectionRequest;
import com.tencent.cos.xml.model.object.PreBuildConnectionResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.object.UploadPartCopyRequest;
import com.tencent.cos.xml.model.object.UploadPartCopyResult;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.auth.QCloudSigner;
import com.tencent.qcloud.core.http.HttpTask;


/**
 * 提供用于访问Tencent Cloud COS服务的简单服务类。[全面服务类请使用 {@link CosXmlService}]
 * <br>
 * 在调用 COS 服务前，都必须初始化一个该类的对象。
 * <p>
 * 更详细的使用方式请参考：<a href="https://cloud.tencent.com/document/product/436/12159#.E5.88.9D.E5.A7.8B.E5.8C.96.E6.9C.8D.E5.8A.A1">入门文档</a>
 */
public class CosXmlSimpleService extends CosXmlBaseService implements SimpleCosXml {
    private static final String TAG = "CosXmlSimpleService";

    public CosXmlSimpleService(Context context, CosXmlServiceConfig configuration, QCloudCredentialProvider qCloudCredentialProvider) {
        super(context, configuration, qCloudCredentialProvider);
    }

    public CosXmlSimpleService(Context context, CosXmlServiceConfig configuration) {
        super(context, configuration);
    }

    public CosXmlSimpleService(Context context, CosXmlServiceConfig configuration, QCloudSigner qCloudSigner) {
        super(context, configuration, qCloudSigner);
    }

    public CosXmlSimpleService(Context context, CosXmlServiceConfig configuration, QCloudSelfSigner selfSigner) {
        super(context, configuration, selfSigner);
    }

    @Override
    protected <T1 extends CosXmlRequest> void setCopySource(T1 cosXmlRequest) throws CosXmlClientException{
        super.setCopySource(cosXmlRequest);
        if (cosXmlRequest instanceof CopyObjectRequest) {
            ((CopyObjectRequest) cosXmlRequest).setCopySource(((CopyObjectRequest) cosXmlRequest).getCopySource(), config);
        }
    }

    @Override
    protected <T1 extends CosXmlRequest, T2 extends CosXmlResult> void setProgressListener(
            final T1 cosXmlRequest, HttpTask<T2> httpTask, boolean isSchedule){
        super.setProgressListener(cosXmlRequest, httpTask, isSchedule);
        if (cosXmlRequest instanceof AppendObjectRequest) {
            httpTask.addProgressListener(((AppendObjectRequest) cosXmlRequest).getProgressListener());
        } else if (cosXmlRequest instanceof PostObjectRequest) {
            httpTask.addProgressListener(((PostObjectRequest) cosXmlRequest).getProgressListener());
        }
    }

    /**
     * <p>
     * 简单上传的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#putObject(PutObjectRequest)}
     */
    @Override
    public PutObjectResult putObject(PutObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        PutObjectResult putObjectResult = new PutObjectResult();
        putObjectResult.accessUrl = getAccessUrl(request);
        return execute(request, putObjectResult);
    }

    /**
     * <p>
     * 简单上传的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#putObjectAsync(PutObjectRequest, CosXmlResultListener)}
     */
    @Override
    public void putObjectAsync(PutObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        PutObjectResult putObjectResult = new PutObjectResult();
        putObjectResult.accessUrl = getAccessUrl(request);
        schedule(request, putObjectResult, cosXmlResultListener);
    }

    /**
     * <p>
     * 以网页表单的形式上传文件的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link SimpleCosXml#postObject(PostObjectRequest)}
     */
    @Override
    public PostObjectResult postObject(PostObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        PostObjectResult postObjectResult = new PostObjectResult();
        return execute(request, postObjectResult);
    }

    /**
     * <p>
     * 以网页表单的形式上传文件的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link SimpleCosXml#postObjectAsync(PostObjectRequest, CosXmlResultListener)}
     */
    @Override
    public void postObjectAsync(PostObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        PostObjectResult postObjectResult = new PostObjectResult();
        schedule(request, postObjectResult, cosXmlResultListener);
    }

    /**
     * <p>
     * 删除 COS 上单个对象的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#deleteObject(DeleteObjectRequest)}
     */
    @Override
    public DeleteObjectResult deleteObject(DeleteObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteObjectResult());
    }

    /**
     * <p>
     * 删除 COS 上单个对象的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#deleteObjectAsync(DeleteObjectRequest, CosXmlResultListener)}
     */
    @Override
    public void deleteObjectAsync(DeleteObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new DeleteObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 获取 COS 对象的元数据的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link SimpleCosXml#headObject(HeadObjectRequest)}
     */
    @Override
    public HeadObjectResult headObject(HeadObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        HeadObjectResult headObjectResult = new HeadObjectResult();
        headObjectResult.accessUrl = getAccessUrl(request);
        return execute(request, headObjectResult);
    }

    /**
     * <p>
     * 获取 COS 对象的元数据的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link SimpleCosXml#headObjectAsync(HeadObjectRequest, CosXmlResultListener)}
     */
    @Override
    public void headObjectAsync(HeadObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        HeadObjectResult headObjectResult = new HeadObjectResult();
        headObjectResult.accessUrl = getAccessUrl(request);
        schedule(request, headObjectResult, cosXmlResultListener);
    }

    /**
     * <p>
     * 拷贝对象的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link SimpleCosXml#copyObject(CopyObjectRequest)}
     */
    @Override
    public CopyObjectResult copyObject(CopyObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new CopyObjectResult());
    }

    /**
     * <p>
     * 拷贝对象的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link SimpleCosXml#copyObjectAsync(CopyObjectRequest, CosXmlResultListener)}
     */
    @Override
    public void copyObjectAsync(CopyObjectRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new CopyObjectResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 拷贝对象的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link SimpleCosXml#copyObject(UploadPartCopyRequest)}
     */
    @Override
    public UploadPartCopyResult copyObject(UploadPartCopyRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new UploadPartCopyResult());
    }

    /**
     * <p>
     * 拷贝对象的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link SimpleCosXml#copyObjectAsync(UploadPartCopyRequest, CosXmlResultListener)}
     */
    @Override
    public void copyObjectAsync(UploadPartCopyRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new UploadPartCopyResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 初始化分块上传的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#initMultipartUpload(InitMultipartUploadRequest)}
     */
    @Override
    public InitMultipartUploadResult initMultipartUpload(InitMultipartUploadRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new InitMultipartUploadResult());
    }

    /**
     * <p>
     * 初始化分块上传的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#initMultipartUploadAsync(InitMultipartUploadRequest, CosXmlResultListener)}
     */
    @Override
    public void initMultipartUploadAsync(InitMultipartUploadRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new InitMultipartUploadResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 查询特定分块上传中的已上传的块的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#listParts(ListPartsRequest)}
     */
    @Override
    public ListPartsResult listParts(ListPartsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new ListPartsResult());
    }

    /**
     * <p>
     * 查询特定分块上传中的已上传的块的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#listPartsAsync(ListPartsRequest, CosXmlResultListener)}
     */
    @Override
    public void listPartsAsync(ListPartsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new ListPartsResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 舍弃一个分块上传且删除已上传的分片块的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#abortMultiUpload(AbortMultiUploadRequest)}
     */
    @Override
    public AbortMultiUploadResult abortMultiUpload(AbortMultiUploadRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new AbortMultiUploadResult());
    }

    /**
     * <p>
     * 舍弃一个分块上传且删除已上传的分片块的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#abortMultiUploadAsync(AbortMultiUploadRequest, CosXmlResultListener)}
     */
    @Override
    public void abortMultiUploadAsync(AbortMultiUploadRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new AbortMultiUploadResult(), cosXmlResultListener);
    }

    /**
     * <p>
     * 完成整个分块上传的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#completeMultiUpload(CompleteMultiUploadRequest)}
     */
    @Override
    public CompleteMultiUploadResult completeMultiUpload(CompleteMultiUploadRequest request) throws CosXmlClientException, CosXmlServiceException {
        CompleteMultiUploadResult completeMultiUploadResult = new CompleteMultiUploadResult();
        completeMultiUploadResult.accessUrl = getAccessUrl(request);
        return execute(request, completeMultiUploadResult);
    }

    /**
     * <p>
     * 完成整个分块上传的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  SimpleCosXml#completeMultiUploadAsync(CompleteMultiUploadRequest, CosXmlResultListener)}
     */
    @Override
    public void completeMultiUploadAsync(CompleteMultiUploadRequest request, CosXmlResultListener cosXmlResultListener) {
        CompleteMultiUploadResult completeMultiUploadResult = new CompleteMultiUploadResult();
        completeMultiUploadResult.accessUrl = getAccessUrl(request);
        schedule(request, completeMultiUploadResult, cosXmlResultListener);
    }


    /**
     * <p>
     * 预连接的同步方法.&nbsp。
     * </p>
     */
    @Override
    public boolean preBuildConnection(String bucket) {
        PreBuildConnectionRequest preBuildConnectionRequest = new PreBuildConnectionRequest(bucket);
        try {
            execute(preBuildConnectionRequest, new PreBuildConnectionResult());
            return true;
        } catch (CosXmlClientException e) {
            return false;
        } catch (CosXmlServiceException e) {
            return e.getStatusCode() != 404;
        }
    }

    /**
     * <p>
     * 预连接的异步方法.&nbsp。
     * </p>
     */
    @Override
    public void preBuildConnectionAsync(String bucket, final CosXmlResultSimpleListener listener) {
        PreBuildConnectionRequest preBuildConnectionRequest = new PreBuildConnectionRequest(bucket);
        schedule(preBuildConnectionRequest, new PreBuildConnectionResult(), new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                listener.onSuccess();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if(serviceException != null && serviceException.getStatusCode() != 404){
                    listener.onSuccess();
                } else {
                    listener.onFail(clientException, serviceException);
                }
            }
        });
    }

    /**
     * <p>
     * 查询存储桶（Bucket）中正在进行中的分块上传对象的同步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  CosXml#listMultiUploads(ListMultiUploadsRequest request)}
     */
    @Override
    public ListMultiUploadsResult listMultiUploads(ListMultiUploadsRequest request) throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new ListMultiUploadsResult());
    }

    /**
     * <p>
     * 查询存储桶（Bucket）中正在进行中的分块上传对象的异步方法.&nbsp;
     * </p>
     * 详细介绍，请查看:{@link  CosXml#listMultiUploadsAsync(ListMultiUploadsRequest request, CosXmlResultListener cosXmlResultListener)}
     */
    @Override
    public void listMultiUploadsAsync(ListMultiUploadsRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new ListMultiUploadsResult(), cosXmlResultListener);
    }
}
