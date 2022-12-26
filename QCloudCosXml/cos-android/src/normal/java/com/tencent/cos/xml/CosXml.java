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

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlBooleanListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
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
import com.tencent.cos.xml.model.ci.GetDescribeMediaBucketsRequest;
import com.tencent.cos.xml.model.ci.GetDescribeMediaBucketsResult;
import com.tencent.cos.xml.model.ci.GetMediaInfoRequest;
import com.tencent.cos.xml.model.ci.GetMediaInfoResult;
import com.tencent.cos.xml.model.ci.GetSnapshotRequest;
import com.tencent.cos.xml.model.ci.GetSnapshotResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlLinkRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlLinkResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentResult;
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

/**
 * <p>
 * 提供用于访问Tencent Cloud COS服务的全面接口。
 *
 * <p>
 * 对象存储（Cloud Object Storage，COS）是腾讯云提供的一种存储海量文件的分布式存储服务，用户可通过网络随时存储和查看数据。腾讯云 COS 使所有用户都能使用具备高扩展性、低成本、可靠和安全的数据存储服务。
 *
 * <p>
 * CosXml为每个接口都提供了同步和异步操作，如同步接口为{@link #headBucket(HeadBucketRequest)}，
 * 则对应的异步接口为{@link #headBucketAsync(HeadBucketRequest, CosXmlResultListener)}.<br><br>
 *
 * <p>
 * 更多信息请参见：
 * <a href="https://cloud.tencent.com/document/product/436/6222"> 腾讯云COS文档</a>&nbsp;&nbsp;&nbsp;
 * <a href="https://cloud.tencent.com/document/product/436/7751"> 腾讯云COS XML API文档</a>
 *
 * <p>
 * 注意：这里封装的是COS XML接口，COS JSON接口相关的SDK请参见<a
 * href="https://cloud.tencent.com/document/product/436/6517">COS V4</a>，由于COS XML接口相比JSON接口
 * 有更丰富的特性，因此我们更推荐您使用XML SDK。
 */

public interface CosXml extends SimpleCosXml {

    /**
     * <p>
     * 获取所属账户下所有存储桶列表的同步方法.&nbsp;
     * <p>
     * 通过使用帯 Authorization 签名认证的请求，可以获取签名中 APPID 所属账户的所有存储空间列表
     * (Bucket list).
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8291">https://cloud.tencent.com/document/product/436/8291.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34535#.E6.9F.A5.E8.AF.A2.E5.AD.98.E5.82.A8.E6.A1.B6.E5.88.97.E8.A1.A8">获取存储桶列表示例</a>
     * 
     * @param request 获取所属账户的所有存储空间列表请求 {@link GetServiceRequest}
     * @return 获取所属账户的所有存储空间列表请求返回的结果 {@link GetServiceResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetServiceResult getService(GetServiceRequest request) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 获取所属账户下所有存储桶列表的异步方法.&nbsp;
     * <p>
     * 通过使用帯 Authorization 签名认证的请求，可以获取签名中 APPID 所属账户的所有存储空间列表
     * (Bucket list).
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8291">https://cloud.tencent.com/document/product/436/8291.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34535#.E6.9F.A5.E8.AF.A2.E5.AD.98.E5.82.A8.E6.A1.B6.E5.88.97.E8.A1.A8">获取存储桶列表示例</a>
     * 
     * @param request 获取所属账户的所有存储空间列表请求 {@link GetServiceRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getServiceAsync(GetServiceRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 追加上传对象的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31929">https://cloud.tencent.com/document/product/436/31929.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E6.9F.A5.E8.AF.A2.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">追加上传对象示例</a>
     *
     * @param request 追加上传对象的请求 {@link AppendObjectRequest}
     * @return 追加上传对象的返回结果 {@link AppendObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    AppendObjectResult appendObject(AppendObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 追加上传对象的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31929">https://cloud.tencent.com/document/product/436/31929.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E6.9F.A5.E8.AF.A2.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">追加上传对象示例</a>
     *
     * @param request 追加上传对象的请求 {@link AppendObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void appendObjectAsync(AppendObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * COS 对象的跨域访问配置预请求的同步方法.&nbsp;
     * <p>
     * 跨域访问配置的预请求是指在发送跨域请求之前会发送一个 OPTIONS 请求并带上特定的来源域，HTTP 方法
     * 和 header 信息等给 COS，以决定是否可以发送真正的跨域请求.
     * 当跨域访问配置不存在时，请求返回403 Forbidden.
     * 跨域访问配置可以通过 {@link #putBucketCORS(PutBucketCORSRequest)} 或者 {@link
     * #putBucketCORSAsync(PutBucketCORSRequest, CosXmlResultListener)} 方法来开启 Bucket 的跨域访问
     * 支持.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8288">https://cloud.tencent.com/document/product/436/8288.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E9.A2.84.E8.AF.B7.E6.B1.82.E8.B7.A8.E5.9F.9F.E9.85.8D.E7.BD.AE">预请求跨域配置示例</a>
     *
     * @param request COS 对象的跨域访问配置预请求 {@link OptionObjectRequest}
     * @return COS 对象的跨域访问配置预请求返回的结果 {@link OptionObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    OptionObjectResult optionObject(OptionObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * COS 对象的跨域访问配置预请求的异步方法.&nbsp;
     * <p>
     * 跨域访问配置的预请求是指在发送跨域请求之前会发送一个 OPTIONS 请求并带上特定的来源域，HTTP 方法
     * 和 header 信息等给 COS，以决定是否可以发送真正的跨域请求.
     * 当跨域访问配置不存在时，请求返回403 Forbidden.
     * 跨域访问配置可以通过 {@link #putBucketCORS(PutBucketCORSRequest)} 或者 {@link
     * #putBucketCORSAsync(PutBucketCORSRequest, CosXmlResultListener)} 方法来开启 Bucket 的跨域访问
     * 支持.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8288">https://cloud.tencent.com/document/product/436/8288.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E9.A2.84.E8.AF.B7.E6.B1.82.E8.B7.A8.E5.9F.9F.E9.85.8D.E7.BD.AE">预请求跨域配置示例</a>
     *
     * @param request COS 对象的跨域访问配置预请求 {@link OptionObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void optionObjectAsync(OptionObjectRequest request, final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 批量删除 COS 对象的同步方法.&nbsp;
     * <p>
     * COS 支持批量删除指定 Bucket 中 对象，单次请求最大支持批量删除 1000 个 对象.<br>
     * 请求中删除一个不存在的对象，仍然认为是成功的.<br>
     * 对于响应结果，COS提供 Verbose 和 Quiet 两种模式：Verbose 模式将返回每个对象的删除结果;Quiet 模式只返回删除报错的对象信息. 可以通过{@link DeleteMultiObjectRequest#setQuiet(boolean)}设置
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8289">https://cloud.tencent.com/document/product/436/8289.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E5.88.A0.E9.99.A4.E5.A4.9A.E4.B8.AA.E5.AF.B9.E8.B1.A1">删除多个对象示例</a>
     * 
     * @param request 批量删除 COS 对象请求 {@link DeleteMultiObjectRequest}
     * @return 批量删除 COS 对象请求返回的结果 {@link DeleteMultiObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteMultiObjectResult deleteMultiObject(DeleteMultiObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 批量删除 COS 对象的异步方法.&nbsp;
     * <p>
     * COS 支持批量删除指定 Bucket 中 对象，单次请求最大支持批量删除 1000 个 对象.<br>
     * 请求中删除一个不存在的对象，仍然认为是成功的.<br>
     * 对于响应结果，COS提供 Verbose 和 Quiet 两种模式：Verbose 模式将返回每个对象的删除结果;Quiet 模式只返回删除报错的对象信息. 可以通过{@link DeleteMultiObjectRequest#setQuiet(boolean)}设置
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8289">https://cloud.tencent.com/document/product/436/8289.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E5.88.A0.E9.99.A4.E5.A4.9A.E4.B8.AA.E5.AF.B9.E8.B1.A1">删除多个对象示例</a>
     * 
     * @param request 批量删除 COS 对象请求 {@link DeleteMultiObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteMultiObjectAsync(DeleteMultiObjectRequest request, final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 恢复归档对象的同步方法.&nbsp;
     * <p>
     * 该请求用于将归档类型的对象取回。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/12633">https://cloud.tencent.com/document/product/436/12633.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E6.81.A2.E5.A4.8D.E5.BD.92.E6.A1.A3.E5.AF.B9.E8.B1.A1">恢复归档对象示例</a>
     *
     * @param request 恢复归档对象请求 {@link RestoreRequest}
     * @return 恢复归档对象返回结果 {@link RestoreResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    RestoreResult restoreObject(RestoreRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 恢复归档对象的异步方法.&nbsp;
     * <p>
     * 该请求用于将归档类型的对象取回。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/12633">https://cloud.tencent.com/document/product/436/12633.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E6.81.A2.E5.A4.8D.E5.BD.92.E6.A1.A3.E5.AF.B9.E8.B1.A1">恢复归档对象示例</a>
     *
     * @param request 恢复归档对象请求 {@link RestoreRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void restoreObjectAsync(RestoreRequest request,final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 查询存储桶（Bucket) 下的部分或者全部对象的同步方法.&nbsp;
     * <p>
     * COS 支持列出指定 Bucket 下的部分或者全部对象.
     * <ul>
     * <li>每次默认返回的最大条目数为 1000 条.</li>
     * <li>如果无法一次返回所有的对象，则返回结果中的 IsTruncated 为 true，同时会附加一个 NextMarker 字段，提示下
     * 一个条目的起点.</li>
     * <li>若一次请求，已经返回了全部对象，则不会有 NextMarker 这个字段，同时 IsTruncated
     * 为 false.</li>
     * <li>若把 prefix 设置为某个文件夹的全路径名，则可以列出以此 prefix 为开头的文件，即该文件
     * 夹下递归的所有文件和子文件夹.</li>
     * <li>如果再设置 delimiter 定界符为 “/”，则只列出该文件夹下的文件，子文件夹下递归的文件和文件夹名
     * 将不被列出.而子文件夹名将会以 CommonPrefix 的形式给出.</ul>
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7734">https://cloud.tencent.com/document/product/436/7734.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E6.9F.A5.E8.AF.A2.E5.AF.B9.E8.B1.A1.E5.88.97.E8.A1.A8">查询对象列表示例</a>
     *
     * @param request 查询 Bucket 下的部分或者全部对象请求 {@link GetBucketRequest}
     * @return 查询 Bucket 下的部分或者全部对象请求返回的结果 {@link GetBucketResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketResult getBucket(GetBucketRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询存储桶（Bucket) 下的部分或者全部对象的异步方法.&nbsp;
     * <p>
     * COS 支持列出指定 Bucket 下的部分或者全部对象.
     * <ul>
     * <li>每次默认返回的最大条目数为 1000 条.</li>
     * <li>如果无法一次返回所有的对象，则返回结果中的 IsTruncated 为 true，同时会附加一个 NextMarker 字段，提示下
     * 一个条目的起点.</li>
     * <li>若一次请求，已经返回了全部对象，则不会有 NextMarker 这个字段，同时 IsTruncated
     * 为 false.</li>
     * <li>若把 prefix 设置为某个文件夹的全路径名，则可以列出以此 prefix 为开头的文件，即该文件
     * 夹下递归的所有文件和子文件夹.</li>
     * <li>如果再设置 delimiter 定界符为 “/”，则只列出该文件夹下的文件，子文件夹下递归的文件和文件夹名
     * 将不被列出.而子文件夹名将会以 CommonPrefix 的形式给出.</ul>
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7734">https://cloud.tencent.com/document/product/436/7734.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E6.9F.A5.E8.AF.A2.E5.AF.B9.E8.B1.A1.E5.88.97.E8.A1.A8">查询对象列表示例</a>
     *
     * @param request 查询 Bucket 下的部分或者全部对象请求 {@link GetBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketAsync(GetBucketRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 创建存储桶（Bucket）的同步方法.&nbsp;
     * <p>
     * 在开始使用 COS 时，需要在指定的账号下先创建一个 Bucket 以便于对象的使用和管理. 并指定 Bucket
     * 所属的地域.创建 Bucket 的用户默认成为 Bucket 的持有者.若创建 Bucket 时没有指定访问权限，则默认
     * 为私有读写（private）权限.
     * <p>
     * 可用地域，可以查看<a href="https://cloud.tencent.com/document/product/436/6224">https://cloud.tencent.com/document/product/436/6224.</a><br>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14106">
     *  https://cloud.tencent.com/document/product/436/14106</a>.<br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7738">
     *  https://cloud.tencent.com/document/product/436/7738.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34535#.E5.88.9B.E5.BB.BA.E5.AD.98.E5.82.A8.E6.A1.B6">创建存储桶示例</a>
     *
     * @param request 创建 Bucket请求 {@link PutBucketRequest}
     * @return 创建 Bucket 请求返回的结果 {@link PutBucketResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketResult putBucket(PutBucketRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 创建存储桶（Bucket）的异步方法.&nbsp;
     * <p>
     * 在开始使用 COS 时，需要在指定的账号下先创建一个 Bucket 以便于对象的使用和管理. 并指定 Bucket
     * 所属的地域.创建 Bucket 的用户默认成为 Bucket 的持有者.若创建 Bucket 时没有指定访问权限，则默认
     * 为私有读写（private）权限.
     * <p>
     * 可用地域，可以查看<a href="https://cloud.tencent.com/document/product/436/6224">https://cloud.tencent.com/document/product/436/6224.</a><br>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14106">
     *  https://cloud.tencent.com/document/product/436/14106</a>.<br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7738">
     *  https://cloud.tencent.com/document/product/436/7738.</a>
     * <p>
     * <br>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34535#.E5.88.9B.E5.BB.BA.E5.AD.98.E5.82.A8.E6.A1.B6">创建存储桶示例</a>
     *
     * @param request 创建 Bucket 请求 {@link PutBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketAsync(PutBucketRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 存储桶（Bucket） 是否存在的同步方法.&nbsp;
     * <p>
     * 在开始使用 Bucket 时，需要确认该 Bucket 是否存在，是否有权限访问.若不存在，则可以调用{@link #putBucket(PutBucketRequest)}
     * 创建.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7735">https://cloud.tencent.com/document/product/436/7735.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34535#.E6.A3.80.E7.B4.A2.E5.AD.98.E5.82.A8.E6.A1.B6.E5.8F.8A.E5.85.B6.E6.9D.83.E9.99.90">查询对象列表示例</a>
     *
     * @param request Bucket 是否存在请求 {@link HeadBucketRequest}
     * @return Bucket 是否存在请求返回的结果 {@link HeadBucketResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    HeadBucketResult headBucket(HeadBucketRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 存储桶（Bucket） 是否存在的异步方法.&nbsp;
     * <p>
     * 在开始使用 Bucket 时，需要确认该 Bucket 是否存在，是否有权限访问.若不存在，则可以调用{@link #putBucket(PutBucketRequest)}
     * 创建.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7735">https://cloud.tencent.com/document/product/436/7735.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34535#.E6.A3.80.E7.B4.A2.E5.AD.98.E5.82.A8.E6.A1.B6.E5.8F.8A.E5.85.B6.E6.9D.83.E9.99.90">查询对象列表示例</a>
     *
     * @param request Bucket 是否存在请求 {@link HeadBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void headBucketAsync(HeadBucketRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 删除存储桶 (Bucket)的同步方法.&nbsp;
     * <p>
     * COS 目前仅支持删除已经清空的 Bucket，如果 Bucket 中仍有对象，将会删除失败. 因此，在执行删除 Bucket
     * 前，需确保 Bucket 内已经没有对象. 删除 Bucket 时，还需要确保操作的身份已被授权该操作，并确认
     * 传入了正确的存储桶名称和地域参数, 请参阅 {@link #putBucket(PutBucketRequest)}.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14105">https://cloud.tencent.com/document/product/436/14105.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7732">https://cloud.tencent.com/document/product/436/7732.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34535#.E5.88.A0.E9.99.A4.E5.AD.98.E5.82.A8.E6.A1.B6">删除存储桶示例</a>
     *
     * @param request 删除 Bucket 请求 {@link DeleteBucketRequest}
     * @return 删除 Bucket 请求返回的结果 {@link DeleteBucketResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteBucketResult deleteBucket(DeleteBucketRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除存储桶 (Bucket)的异步方法.&nbsp;
     * <p>
     * COS 目前仅支持删除已经清空的 Bucket，如果 Bucket 中仍有对象，将会删除失败. 因此，在执行删除 Bucket
     * 前，需确保 Bucket 内已经没有对象. 删除 Bucket 时，还需要确保操作的身份已被授权该操作，并确认
     * 传入了正确的存储桶名称和地域参数, 请参阅 {@link #putBucket(PutBucketRequest)}.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14105">https://cloud.tencent.com/document/product/436/14105.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7732">https://cloud.tencent.com/document/product/436/7732.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34535#.E5.88.A0.E9.99.A4.E5.AD.98.E5.82.A8.E6.A1.B6">删除存储桶示例</a>
     *
     * @param request 删除 Bucket 请求 {@link GetServiceRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketAsync(DeleteBucketRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 获取 COS 对象的访问权限信息（Access Control List, ACL）的同步方法.&nbsp;
     * <p>
     * Bucket 的持有者可获取该 Bucket 下的某个对象的 ACL 信息，如被授权者以及被授权的信息. ACL
     * 权限包括读、写、读写权限.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7744">https://cloud.tencent.com/document/product/436/7744.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E6.9F.A5.E8.AF.A2.E5.AF.B9.E8.B1.A1-acl">查询对象ACL示例</a>
     * 
     * @param request 获取 COS 对象的 ACL 请求 {@link GetObjectACLRequest}
     * @return 获取 COS 对象的 ACL 请求返回的结果 {@link GetObjectACLResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetObjectACLResult getObjectACL(GetObjectACLRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * 获取 COS 对象的访问权限信息（Access Control List, ACL）的异步方法.&nbsp;
     * <p>
     * Bucket 的持有者可获取该 Bucket 下的某个对象的 ACL 信息，如被授权者以及被授权的信息. ACL
     * 权限包括读、写、读写权限.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7744">https://cloud.tencent.com/document/product/436/7744.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E6.9F.A5.E8.AF.A2.E5.AF.B9.E8.B1.A1-acl">查询对象ACL示例</a>
     * 
     * @param request 获取 COS 对象的 ACL 请求 {@link GetObjectACLRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getObjectACLAsync(GetObjectACLRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 设置 COS 对象的访问权限信息（Access Control List, ACL）的同步方法.&nbsp;
     * <p>
     * ACL权限包括读、写、读写权限. COS 对象的 ACL 可以通过 header头部："x-cos-acl"，"x-cos-grant-read"，"x-cos-grant-write"，
     * "x-cos-grant-full-control" 传入 ACL 信息，或者通过 Body 以 XML 格式传入 ACL 信息.这两种方式只
     * 能选择其中一种，否则引起冲突.
     * 传入新的 ACL 将覆盖原有 ACL信息.ACL策略数上限1000，建议用户不要每个上传文件都设置 ACL.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7748">https://cloud.tencent.com/document/product/436/7748.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1-acl">设置对象ACL示例</a>
     *
     * @param request 设置 COS 对象的 ACL 请求 {@link PutObjectACLRequest}
     * @return 设置 COS 对象的 ACL 请求返回的结果 {@link PutObjectACLResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutObjectACLResult putObjectACL(PutObjectACLRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 设置 COS 对象的访问权限信息（Access Control List, ACL）的异步方法.&nbsp;
     * <p>
     * ACL权限包括读、写、读写权限. COS 对象的 ACL 可以通过 header头部："x-cos-acl"，"x-cos-grant-read"，"x-cos-grant-write"，
     * "x-cos-grant-full-control" 传入 ACL 信息，或者通过 Body 以 XML 格式传入 ACL 信息.这两种方式只
     * 能选择其中一种，否则引起冲突.
     * 传入新的 ACL 将覆盖原有 ACL信息.ACL策略数上限1000，建议用户不要每个上传文件都设置 ACL.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7748">https://cloud.tencent.com/document/product/436/7748.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1-acl">设置对象ACL示例</a>
     *
     * @param request 设置COS 对象的ACL请求 {@link PutObjectACLRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putObjectACLAsync(PutObjectACLRequest request, final CosXmlResultListener cosXmlResultListener);

//    /**
//     * <p>
//     * 获取 COS 对象的元数据信息(meta data)的同步方法.&nbsp;
//     * <p>
//     * 获取 COS 对象的元数据信息，需要与 Get 的权限一致.且请求是不返回消息体的.若请求中需要设置If-Modified-Since
//     * 头部，则统一采用 GMT(RFC822) 时间格式，例如：Tue, 22 Oct 2017 01:35:21 GMT.如果对象不存在，则
//     * 返回404.
//     * <p>
//     * 关于获取 COS 对象的元数据信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7745">https://cloud.tencent.com/document/product/436/7745.</a>
//     *
//     * <p>
//     * cos Android SDK 中获取 COS 对象的元数据信息的同步方法具体步骤如下：<br>
//     * 1、通过调用 {@link HeadObjectRequest} 构造方法，实例化 HeadObjectRequest 对象;<br>
//     * 2、通过调用 {@link #headObject(HeadObjectRequest)} 同步方法，传入 HeadObjectRequest，返回 {@link HeadObjectResult} 对象.
//     *
//
//     *
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
//     * HeadObjectRequest request = new HeadObjectRequest(bucket, cosPath);
//     
//     * try {
//     *     HeadObjectResult result = cosXml.headObject(request);
//     *     Log.w("TEST","success");
//     * } catch (CosXmlClientException e) {
//     *     Log.w("TEST","CosXmlClientException =" + e.toString());
//     * } catch (CosXmlServiceException e) {
//     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
//     * }
//     *</pre></blockquote>
//     *
//     * @param request 获取 COS 对象的元数据信息请求 {@link HeadObjectRequest}
//     * @return HeadObjectResult 获取 COS 对象的元数据信息请求返回的结果 {@link HeadObjectResult}
//     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
//     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
//     */
//    HeadObjectResult headObject(HeadObjectRequest request) throws CosXmlClientException, CosXmlServiceException;
//
//    /**
//     * <p>
//     * 获取 COS 对象的元数据信息(meta data)的异步方法.&nbsp;
//     * <p>
//     * 获取 COS 对象的元数据信息，需要与 Get 的权限一致.且请求是不返回消息体的.若请求中需要设置If-Modified-Since
//     * 头部，则统一采用 GMT(RFC822) 时间格式，例如：Tue, 22 Oct 2017 01:35:21 GMT.如果对象不存在，则
//     * 返回404.
//     * 关于获取 COS 对象的元数据信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7745">https://cloud.tencent.com/document/product/436/7745.</a>
//     *
//     * <p>
//     * cos Android SDK 中获取 COS 对象的元数据信息的异步方法具体步骤如下：<br>
//     * 1、通过调用 {@link HeadObjectRequest} 构造方法，实例化 HeadObjectRequest 对象;<br>
//     * 2、通过调用 {@link #headObjectAsync(HeadObjectRequest, CosXmlResultListener)} 异步方法，传入 HeadObjectRequest 和 CosXmlResultListener 进行异步回调操作.
//     *
//
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
//     * HeadObjectRequest request = new HeadObjectRequest(bucket, cosPath);
//     
//     * cosXml.headObjectAsync(request, new CosXmlResultListener() {
//     *    &nbsp;@Override
//     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
//     *         Log.w("TEST","success");
//     *     }
//     *    &nbsp;@Override
//     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
//     *             serviceException)  {
//     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
//     *         Log.w("TEST",errorMsg);
//     *     }
//     * })
//     *</pre></blockquote>
//     *
//     * @param request 获取 COS 对象的元数据信息请求 {@link HeadObjectRequest}
//     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
//     */
//    void headObjectAsync(HeadObjectRequest request, final CosXmlResultListener cosXmlResultListener);
//

    //todo jordan 补充SDK示例url
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1-acl">获取对象标签示例</a>
    /**
     * <p>
     * 获取对象标签的同步方法.&nbsp;
     * <p>
     * 该请求用于获取对象设置的标签。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/42998">https://cloud.tencent.com/document/product/436/42998.</a>
     *
     * @param request 获取对象标签请求 {@link GetObjectTaggingRequest}
     * @return 获取对象标签返回结果 {@link GetObjectTaggingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetObjectTaggingResult getObjectTagging(GetObjectTaggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    //todo jordan 补充SDK示例url
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1-acl">获取对象标签示例</a>
    /**
     * <p>
     * 获取对象标签的异步方法.&nbsp;
     * <p>
     * 该请求用于获取对象设置的标签。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/42998">https://cloud.tencent.com/document/product/436/42998.</a>
     *
     * @param request 获取对象标签请求 {@link GetObjectTaggingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getObjectTaggingAsync(GetObjectTaggingRequest request, CosXmlResultListener cosXmlResultListener);


    //todo jordan 修改示例url
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1-acl">设置对象标签示例</a>
    /**
     * <p>
     * 设置对象标签的同步方法.&nbsp;
     * <p>
     * 该请求用于为对象设置键值对作为对象标签，可以协助您管理已有的对象资源，并通过标签进行成本管理。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/42997">https://cloud.tencent.com/document/product/436/42997.</a>
     *
     * @param request 设置对象标签请求 {@link PutObjectTaggingRequest}
     * @return 设置对象标签返回结果 {@link PutObjectTaggingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutObjectTaggingResult putObjectTagging(PutObjectTaggingRequest request)throws CosXmlClientException, CosXmlServiceException;


    //todo jordan 修改示例url
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1-acl">设置对象标签示例</a>
    /**
     * <p>
     * 设置对象标签的异步方法.&nbsp;
     * <p>
     * 该请求用于为对象设置键值对作为对象标签，可以协助您管理已有的对象资源，并通过标签进行成本管理。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/42997">https://cloud.tencent.com/document/product/436/42997.</a>
     *
     * @param request 设置对象标签请求 {@link PutObjectTaggingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putObjectTaggingAsync(PutObjectTaggingRequest request, CosXmlResultListener cosXmlResultListener);


    //todo jordan 修改示例url
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1-acl">删除对象标签示例</a>
    /**
     * <p>
     * 删除对象标签的同步方法.&nbsp;
     * <p>
     * 该请求用于删除对象设置的标签。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/42999">https://cloud.tencent.com/document/product/436/42999.</a>
     *
     * @param request 删除对象标签请求 {@link DeleteObjectTaggingRequest}
     * @return 删除对象标签返回结果 {@link DeleteObjectTaggingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteObjectTaggingResult deleteObjectTagging(DeleteObjectTaggingRequest request)throws CosXmlClientException, CosXmlServiceException;


    //todo jordan 修改示例url
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1-acl">删除对象标签示例</a>
    /**
     * <p>
     * 删除对象标签的异步方法.&nbsp;
     * <p>
     * 该请求用于删除对象设置的标签。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/42999">https://cloud.tencent.com/document/product/436/42999.</a>
     *
     * @param request 删除对象标签请求 {@link DeleteObjectTaggingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteObjectTaggingAsync(DeleteObjectTaggingRequest request, CosXmlResultListener cosXmlResultListener);

//    /**
//     * <p>
//     * 简单复制对象的同步方法.&nbsp;
//     * <p>
//     * COS 中复制对象可以完成如下功能:
//     * <ul>
//     * <li>创建一个新的对象副本.</li>
//     * <li>复制对象并更名，删除原始对象，实现重命名</li>
//     * <li>修改对象的存储类型，在复制时选择相同的源和目标对象键，修改存储类型.</li>
//     * <li>在不同的腾讯云 COS 地域复制对象.</li>
//     * <li>修改对象的元数据，在复制时选择相同的源和目标对象键，并修改其中的元数据,复制对象时，默认将继承原对象的元数据，但创建日期将会按新对象的时间计算.</li>
//     * </ul>
//     * <p>
//     * 当复制的对象小于等于 5 GB ，可以使用简单复制（<a href="https://cloud.tencent.com/document/product/436/14117">https://cloud.tencent.com/document/product/436/14117</a>).<br>
//     * 当复制对象超过 5 GB 时，必须使用分块复制（<a href="https://cloud.tencent.com/document/product/436/14118">https://cloud.tencent.com/document/product/436/14118 </a>）
//     * 来实现复制.<br>
//     * 关于简单复制接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/10881">https://cloud.tencent.com/document/product/436/10881.</a>
//     *
//     * <p>
//     * cos Android SDK 中简单复制对象的同步方法具体步骤如下：<br>
//     * 1、通过调用 {@link CopyObjectRequest} 构造方法，实例化 CopyObjectRequest 对象;<br>
//     * 2、通过调用 {@link #copyObject(CopyObjectRequest)} 同步方法，传入 CopyObjectRequest，返回 {@link CopyObjectResult} 对象.
//     *
//
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
//     * CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
//     * "appid", "*source_bucket", "source_region", "source");
//     
//     * try {
//     *     CopyObjectResult result = cosXml.copyObject(request);
//     *     Log.w("TEST","success");
//     * } catch (CosXmlClientException e) {
//     *     Log.w("TEST","CosXmlClientException =" + e.toString());
//     * } catch (CosXmlServiceException e) {
//     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
//     * }
//     *</pre></blockquote>
//     *
//     * @param request 简单复制对象请求 {@link CopyObjectRequest}
//     * @return CopyObjectResult 简单复制对象请求返回的结果 {@link CopyObjectResult}
//     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
//     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
//     */
//    CopyObjectResult copyObject(CopyObjectRequest request) throws CosXmlClientException, CosXmlServiceException;
//
//    /**
//     * <p>
//     * 简单复制对象的异步方法.&nbsp;<br>
//     * <p>
//     * COS 中复制对象可以完成如下功能:
//     * <ul>
//     * <li>创建一个新的对象副本.</li>
//     * <li>复制对象并更名，删除原始对象，实现重命名</li>
//     * <li>修改对象的存储类型，在复制时选择相同的源和目标对象键，修改存储类型.</li>
//     * <li>在不同的腾讯云 COS 地域复制对象.</li>
//     * <li>修改对象的元数据，在复制时选择相同的源和目标对象键，并修改其中的元数据,复制对象时，默认将继承原对象的元数据，但创建日期将会按新对象的时间计算.</li>
//     * </ul>
//     * <p>
//     * 当复制的对象小于等于 5 GB ，可以使用简单复制（<a href="https://cloud.tencent.com/document/product/436/14117">https://cloud.tencent.com/document/product/436/14117</a>).<br>
//     * 当复制对象超过 5 GB 时，必须使用分块复制（<a href="https://cloud.tencent.com/document/product/436/14118">https://cloud.tencent.com/document/product/436/14118 </a>）
//     * 来实现复制.<br>
//     * 关于简单复制接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/10881">https://cloud.tencent.com/document/product/436/10881.</a>
//     *
//     * <p>
//     * cos Android SDK 中简单复制对象的异步方法具体步骤如下：<br>
//     * 1、通过调用 {@link CopyObjectRequest} 构造方法，实例化 CopyObjectRequest 对象;<br>
//     * 2、通过调用 {@link #copyObjectAsync(CopyObjectRequest, CosXmlResultListener)} 异步方法，传入 CopyObjectRequest 和 CosXmlResultListener 进行异步回调操作.
//     *
//
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
//     * CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
//     * "appid", "*source_bucket", "source_region", "source");
//     * CopyObjectRequest request = new CopyObjectRequest(bucket, cosPath, copySourceStruct);
//     
//     * cosXml.copyObjectAsync(request, new CosXmlResultListener() {
//     *    &nbsp;@Override
//     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
//     *         Log.w("TEST","success");
//     *     }
//     *    &nbsp;@Override
//     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
//     *             serviceException)  {
//     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
//     *         Log.w("TEST",errorMsg);
//     *     }
//     * })
//     *</pre></blockquote>
//     *
//     * @param request 简单复制对象请求 {@link CopyObjectRequest}
//     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
//     */
//    void copyObjectAsync(CopyObjectRequest request, final CosXmlResultListener cosXmlResultListener);
//
//
//    /**
//     * <p>
//     * 分块复制的同步方法.&nbsp;
//     * <p>
//     * COS 中复制对象可以完成如下功能:
//     * <ul>
//     * <li>创建一个新的对象副本.</li>
//     * <li>复制对象并更名，删除原始对象，实现重命名</li>
//     * <li>修改对象的存储类型，在复制时选择相同的源和目标对象键，修改存储类型.</li>
//     * <li>在不同的腾讯云 COS 地域复制对象.</li>
//     * <li>修改对象的元数据，在复制时选择相同的源和目标对象键，并修改其中的元数据,复制对象时，默认将继承原对象的元数据，但创建日期将会按新对象的时间计算.</li>
//     * </ul>
//     * <p>
//     * 当复制的对象小于等于 5 GB ，可以使用简单复制（<a href="https://cloud.tencent.com/document/product/436/14117">https://cloud.tencent.com/document/product/436/14117</a>).<br>
//     * 当复制对象超过 5 GB 时，必须使用分块复制（<a href="https://cloud.tencent.com/document/product/436/14118">https://cloud.tencent.com/document/product/436/14118 </a>）
//     * 来实现复制.<br>
//     * 关于分块复制接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8287">https://cloud.tencent.com/document/product/436/8287.</a>
//     *
//     * <p>
//     * cos Android SDK 中分块复制的同步方法具体步骤如下：<br>
//     * 1、通过调用 {@link UploadPartCopyRequest} 构造方法，实例化 UploadPartCopyRequest 对象;<br>
//     * 2、通过调用 {@link #copyObject(UploadPartCopyRequest)} 同步方法，传入 UploadPartCopyRequest，返回 {@link UploadPartCopyResult} 对象.
//     *
//
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
//     * String srcPath = "srcPath"; //本地文件的绝对路径
//     * PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
//     * putObjectRequest.setSign(signDuration,null,null); //签名
//     * putObjectRequest.setProgressListener(new CosXmlProgressListener() {// 进度回调
//     *    &nbsp;@Override
//     *     public void onProgress(long progress, long max) {
//     *         float result = (float) (progress * 100.0/max);
//     *         Log.w("TEST","progress =" + (long)result + "%");
//     *    }
//     * });
//     * String eTag = null; //上传返回的文件 md5
//     * String accessUrl = null; //访问文件的地址
//     * try {
//     *     UploadPartCopyResult result = cosXml.copyObject(request);
//     *     Log.w("TEST","success");
//     * } catch (CosXmlClientException e) {
//     *     Log.w("TEST","CosXmlClientException =" + e.toString());
//     * } catch (CosXmlServiceException e) {
//     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
//     * }
//     *</pre></blockquote>
//     *
//     * @param request 分块复制请求 {@link UploadPartCopyRequest}
//     * @return UploadPartCopyResult 分块复制请求返回的结果 {@link UploadPartCopyResult}
//     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
//     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
//     */
//    UploadPartCopyResult copyObject(UploadPartCopyRequest request) throws CosXmlClientException, CosXmlServiceException;
//
//    /**
//     * <p>
//     * 分块复制的异步方法.&nbsp;
//     * <p>
//     * COS 中复制对象可以完成如下功能:
//     * <ul>
//     * <li>创建一个新的对象副本.</li>
//     * <li>复制对象并更名，删除原始对象，实现重命名</li>
//     * <li>修改对象的存储类型，在复制时选择相同的源和目标对象键，修改存储类型.</li>
//     * <li>在不同的腾讯云 COS 地域复制对象.</li>
//     * <li>修改对象的元数据，在复制时选择相同的源和目标对象键，并修改其中的元数据,复制对象时，默认将继承原对象的元数据，但创建日期将会按新对象的时间计算.</li>
//     * </ul>
//     * <p>
//     * 当复制的对象小于等于 5 GB ，可以使用简单复制（<a href="https://cloud.tencent.com/document/product/436/14117">https://cloud.tencent.com/document/product/436/14117</a>).<br>
//     * 当复制对象超过 5 GB 时，必须使用分块复制（<a href="https://cloud.tencent.com/document/product/436/14118">https://cloud.tencent.com/document/product/436/14118 </a>）
//     * 来实现复制.<br>
//     * 关于分块复制接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8287">https://cloud.tencent.com/document/product/436/8287.</a>
//     *
//     * <p>
//     * cos Android SDK 中分块复制的异步方法具体步骤如下：<br>
//     * 1、通过调用 {@link UploadPartCopyRequest} 构造方法，实例化 UploadPartCopyRequest 对象;<br>
//     * 2、通过调用 {@link #copyObjectAsync(UploadPartCopyRequest, CosXmlResultListener)} 异步方法，传入 UploadPartCopyRequest 和 CosXmlResultListener 进行异步回调操作.
//     *
//
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
//     * String srcPath = "srcPath"; //本地文件的绝对路径
//     * PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
//     * putObjectRequest.setSign(signDuration,null,null); //签名
//     * putObjectRequest.setProgressListener(new CosXmlProgressListener() { // 进度回调
//     *    &nbsp;@Override
//     *     public void onProgress(long progress, long max) {
//     *         float result = (float) (progress * 100.0/max);
//     *         Log.w("TEST","progress =" + (long)result + "%");
//     *    }
//     * });
//     * cosXml.copyObjectAsync(request,  new CosXmlResultListener() {
//     *    &nbsp;@Override
//     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
//     *         Log.w("TEST","success");
//     *     }
//     *    &nbsp;@Override
//     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
//     *             serviceException)  {
//     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
//     *         Log.w("TEST",errorMsg);
//     *     }
//     * })
//     *</pre></blockquote>
//     *
//     * @param request 分块复制请求 {@link UploadPartCopyRequest}
//     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
//     */
//    void copyObjectAsync(UploadPartCopyRequest request,final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 查询存储桶（Bucket) 跨域访问配置信息的同步方法.&nbsp;
     * <p>
     * COS 支持查询当前 Bucket 跨域访问配置信息，以确定是否配置跨域信息.当跨域访问配置不存在时，请求
     * 返回403 Forbidden.
     * 跨域访问配置可以通过 {@link #putBucketCORS(PutBucketCORSRequest)} 或者 {@link
     * #putBucketCORSAsync(PutBucketCORSRequest, CosXmlResultListener)} 方法来开启 Bucket 的跨域访问
     * 支持.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8274">https://cloud.tencent.com/document/product/436/8274.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41909#.E6.9F.A5.E8.AF.A2.E8.B7.A8.E5.9F.9F.E9.85.8D.E7.BD.AE">查询跨域配置示例</a>
     *
     * @param request 查询 Bucket 跨域访问配置信息请求 {@link GetBucketCORSRequest}
     * @return 查询 Bucket 跨域访问配置信息请求返回的结果 {@link GetBucketCORSResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketCORSResult getBucketCORS(GetBucketCORSRequest request) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 查询存储桶（Bucket) 跨域访问配置信息的异步方法.&nbsp;
     * <p>
     * COS 支持查询当前 Bucket 跨域访问配置信息，以确定是否配置跨域信息.当跨域访问配置不存在时，请求
     * 返回403 Forbidden.
     * 跨域访问配置可以通过 {@link #putBucketCORS(PutBucketCORSRequest)} 或者 {@link
     * #putBucketCORSAsync(PutBucketCORSRequest, CosXmlResultListener)} 方法来开启 Bucket 的跨域访问
     * 支持.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8274">https://cloud.tencent.com/document/product/436/8274.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41909#.E6.9F.A5.E8.AF.A2.E8.B7.A8.E5.9F.9F.E9.85.8D.E7.BD.AE">查询跨域配置示例</a>
     *
     * @param request 查询 Bucket 跨域访问配置信息请求 {@link GetBucketCORSRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketCORSAsync(GetBucketCORSRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 设置存储桶（Bucket） 的跨域配置信息的同步方法.&nbsp;
     * <p>
     * 跨域访问配置的预请求是指在发送跨域请求之前会发送一个 OPTIONS 请求并带上特定的来源域，HTTP 方
     * 法和 header 信息等给 COS，以决定是否可以发送真正的跨域请求.
     * 当跨域访问配置不存在时，请求返回403 Forbidden.
     * <p>
     * 默认情况下，Bucket的持有者可以直接配置 Bucket的跨域信息 ，Bucket 持有者也可以将配置权限授予其他用户.新的配置是覆盖当前的所有配置信
     * 息，而不是新增一条配置.可以通过传入 XML 格式的配置文件来实现配置，文件大小限制为64 KB.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8279">
     * https://cloud.tencent.com/document/product/436/8279</a>.
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41909#.E8.AE.BE.E7.BD.AE.E8.B7.A8.E5.9F.9F.E9.85.8D.E7.BD.AE">设置跨域配置示例</a>
     *
     * @param request 设置 Bucket 的跨域配置信息请求 {@link PutBucketCORSRequest}
     * @return 设置 Bucket 的跨域配置信息请求返回的结果 {@link PutBucketCORSResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketCORSResult putBucketCORS(PutBucketCORSRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 设置存储桶（Bucket） 的跨域配置信息的异步方法.&nbsp;
     * <p>
     * 跨域访问配置的预请求是指在发送跨域请求之前会发送一个 OPTIONS 请求并带上特定的来源域，HTTP 方
     * 法和 header 信息等给 COS，以决定是否可以发送真正的跨域请求.
     * 当跨域访问配置不存在时，请求返回403 Forbidden.
     * <p>
     * 默认情况下，Bucket的持有者可以直接配置 Bucket的跨域信息 ，Bucket 持有者也可以将配置权限授予其他用户.新的配置是覆盖当前的所有配置信
     * 息，而不是新增一条配置.可以通过传入 XML 格式的配置文件来实现配置，文件大小限制为64 KB.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8279">
     * https://cloud.tencent.com/document/product/436/8279</a>.
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41909#.E8.AE.BE.E7.BD.AE.E8.B7.A8.E5.9F.9F.E9.85.8D.E7.BD.AE">设置跨域配置示例</a>
     *
     * @param request 设置 Bucket 的跨域配置信息请求 {@link PutBucketCORSRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketCORSAsync(PutBucketCORSRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 删除跨域访问配置信息的同步方法.&nbsp;
     * <p>
     * 若是 Bucket 不需要支持跨域访问配置，可以调用此接口删除已配置的跨域访问信息.
     * 跨域访问配置可以通过 {@link #putBucketCORS(PutBucketCORSRequest)} 或者 {@link
     * #putBucketCORSAsync(PutBucketCORSRequest, CosXmlResultListener)} 方法来开启 Bucket 的跨域访问
     * 支持.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8283">https://cloud.tencent.com/document/product/436/8283.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41909#.E5.88.A0.E9.99.A4.E8.B7.A8.E5.9F.9F.E9.85.8D.E7.BD.AE">删除跨域配置示例</a>
     * 
     * @param request 删除跨域访问配置信息请求 {@link DeleteBucketCORSRequest}
     * @return 删除跨域访问配置信息请求返回的结果 {@link DeleteBucketCORSResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteBucketCORSResult deleteBucketCORS(DeleteBucketCORSRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除跨域访问配置信息的异步方法.&nbsp;
     * <p>
     * 若是 Bucket 不需要支持跨域访问配置，可以调用此接口删除已配置的跨域访问信息.
     * 跨域访问配置可以通过 {@link #putBucketCORS(PutBucketCORSRequest)} 或者 {@link
     * #putBucketCORSAsync(PutBucketCORSRequest, CosXmlResultListener)} 方法来开启 Bucket 的跨域访问
     * 支持.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8283">https://cloud.tencent.com/document/product/436/8283.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41909#.E5.88.A0.E9.99.A4.E8.B7.A8.E5.9F.9F.E9.85.8D.E7.BD.AE">删除跨域配置示例</a>
     * 
     * @param request 删除跨域访问配置信息请求 {@link DeleteBucketCORSRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketCORSAsync(DeleteBucketCORSRequest request, final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 查询存储桶（Bucket) 的生命周期配置的同步方法.&nbsp;
     * <p>
     * COS 支持以生命周期配置的方式来管理 Bucket 中对象的生命周期，生命周期配置包含一个或多个将
     * 应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)，请参阅 {@link #putBucketLifecycle(PutBucketLifecycleRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8278">https://cloud.tencent.com/document/product/436/8278.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41904#.E6.9F.A5.E8.AF.A2.E7.94.9F.E5.91.BD.E5.91.A8.E6.9C.9F">查询生命周期示例</a>
     *
     * @param request 查询 Bucket 的生命周期配置请求 {@link GetBucketLifecycleRequest}
     * @return 查询 Bucket 的生命周期配置请求返回的结果 {@link GetBucketLifecycleResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketLifecycleResult getBucketLifecycle(GetBucketLifecycleRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询存储桶（Bucket) 的生命周期配置的异步方法.&nbsp;
     * <p>
     * COS 支持以生命周期配置的方式来管理 Bucket 中对象的生命周期，生命周期配置包含一个或多个将
     * 应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)，请参阅 {@link #putBucketLifecycle(PutBucketLifecycleRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8278">https://cloud.tencent.com/document/product/436/8278.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41904#.E6.9F.A5.E8.AF.A2.E7.94.9F.E5.91.BD.E5.91.A8.E6.9C.9F">查询生命周期示例</a>
     *
     * @param request 查询 Bucket 的生命周期配置请求 {@link GetBucketLifecycleRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketLifecycleAsync(GetBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 设置存储桶（Bucket) 生命周期配置的同步方法.&nbsp;
     * <p>
     * COS 支持以生命周期配置的方式来管理 Bucket 中对象的生命周期.
     * 如果该 Bucket 已配置生命周期，新的配置的同时则会覆盖原有的配置.
     * 生命周期配置包含一个或多个将应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)。这些操作分为以下两种：转换操作，过期操作.
     * <ul>
     * <li>转换操作,定义对象转换为另一个存储类的时间(例如，您可以选择在对象创建 30 天后将其转换为低频存储类别，同
     * 时也支持将数据沉降到归档存储类别）.</li>
     * <li>过期操作，指定 Object 的过期时间，COS 将会自动为用户删除过期的 Object.</li>
     * </ul>
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8280">
     * https://cloud.tencent.com/document/product/436/8280</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41904#.E8.AE.BE.E7.BD.AE.E7.94.9F.E5.91.BD.E5.91.A8.E6.9C.9F">设置生命周期示例</a>
     *
     * @param request Bucket 生命周期配置请求 {@link PutBucketLifecycleRequest}
     * @return Bucket 生命周期配置请求返回的结果 {@link PutBucketLifecycleResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketLifecycleResult putBucketLifecycle(PutBucketLifecycleRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 设置存储桶（Bucket) 生命周期配置的异步方法.&nbsp;
     * <p>
     * COS 支持以生命周期配置的方式来管理 Bucket 中对象的生命周期.
     * 如果该 Bucket 已配置生命周期，新的配置的同时则会覆盖原有的配置.
     * 生命周期配置包含一个或多个将应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)。这些操作分为以下两种：转换操作，过期操作.
     * <ul>
     * <li>转换操作,定义对象转换为另一个存储类的时间(例如，您可以选择在对象创建 30 天后将其转换为低频存储类别，同
     * 时也支持将数据沉降到归档存储类别）.</li>
     * <li>过期操作，指定 Object 的过期时间，COS 将会自动为用户删除过期的 Object.</li>
     * </ul>
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8280">
     * https://cloud.tencent.com/document/product/436/8280</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41904#.E8.AE.BE.E7.BD.AE.E7.94.9F.E5.91.BD.E5.91.A8.E6.9C.9F">设置生命周期示例</a>
     *
     * @param request Bucket 生命周期配置请求 {@link PutBucketLifecycleRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketLifecycleAsync(PutBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 删除存储桶（Bucket） 的生命周期配置的同步方法.&nbsp;
     * <p>
     * COS 支持删除已配置的 Bucket 的生命周期列表.
     * COS 支持以生命周期配置的方式来管理 Bucket 中 对象的生命周期，生命周期配置包含一个或多个将
     * 应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)，请参阅 {@link #putBucketLifecycle(PutBucketLifecycleRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8284">https://cloud.tencent.com/document/product/436/8284.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41904#.E5.88.A0.E9.99.A4.E7.94.9F.E5.91.BD.E5.91.A8.E6.9C.9F">删除生命周期示例</a>
     * 
     * @param request 删除 Bucket 的生命周期配置请求 {@link GetServiceRequest}
     * @return 删除 Bucket 的生命周期配置请求返回的结果 {@link DeleteBucketLifecycleResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteBucketLifecycleResult deleteBucketLifecycle(DeleteBucketLifecycleRequest request) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 删除存储桶（Bucket） 的生命周期配置的异步方法.&nbsp;
     * <p>
     * COS 支持删除已配置的 Bucket 的生命周期列表.
     * COS 支持以生命周期配置的方式来管理 Bucket 中 对象的生命周期，生命周期配置包含一个或多个将
     * 应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)，请参阅 {@link #putBucketLifecycle(PutBucketLifecycleRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8284">https://cloud.tencent.com/document/product/436/8284.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41904#.E5.88.A0.E9.99.A4.E7.94.9F.E5.91.BD.E5.91.A8.E6.9C.9F">删除生命周期示例</a>
     * 
     * @param request 删除 Bucket 的生命周期配置请求 {@link DeleteBucketLifecycleRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketLifecycleAsync(DeleteBucketLifecycleRequest request,CosXmlResultListener cosXmlResultListener);


//
//    /**
//     * <p>
//     * 删除存储桶（Bucket) 标签的同步方法.&nbsp;
//     * 
//     * <p>
//     * cos Android SDK 中删除 Bucket 标签的同步方法具体步骤如下：<br>
//     * 1、通过调用 {@link DeleteBucketTaggingRequest} 构造方法，实例化 DeleteBucketTaggingRequest 对象;<br>
//     * 2、通过调用 {@link #deleteBucketTagging(DeleteBucketTaggingRequest)} 同步方法，传入 DeleteBucketTaggingRequest，返回 {@link DeleteBucketTaggingResult} 对象.
//     * 
//
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * DeleteBucketTaggingRequest request = new DeleteBucketTaggingRequest(bucket);
//     
//     * try {
//     *     DeleteBucketTaggingResult result = cosXml.deleteBucketTagging(request);
//     *     Log.w("TEST","success");
//     * } catch (CosXmlClientException e) {
//     *     Log.w("TEST","CosXmlClientException =" + e.toString());
//     * } catch (CosXmlServiceException e) {
//     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
//     * }
//     *</pre></blockquote>
//     * 
//     * @param request 删除 Bucket 标签请求 {@link DeleteBucketTaggingRequest}
//     * @return DeleteBucketTaggingResult 删除 Bucket 标签请求返回的结果 {@link DeleteBucketTaggingResult}
//     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
//     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
//     */
//    DeleteBucketTaggingResult deleteBucketTagging(DeleteBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException;
//
//    /**
//     * <p>
//     * 删除存储桶（Bucket) 标签的异步方法.&nbsp;
//     * 
//     * <p>
//     * cos Android SDK 中删除 Bucket 标签的异步方法具体步骤如下：<br>
//     * 1、通过调用 {@link DeleteBucketTaggingRequest} 构造方法，实例化 DeleteBucketTaggingRequest 对象;<br>
//     * 2、通过调用 {@link #deleteBucketTaggingAsync(DeleteBucketTaggingRequest, CosXmlResultListener)} 异步方法，传入 DeleteBucketTaggingRequest 和 CosXmlResultListener 进行异步回调操作.
//     * 
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * DeleteBucketTaggingRequest request = new DeleteBucketTaggingRequest(bucket);
//     
//     * cosXml.deleteBucketTaggingAsync(request,  new CosXmlResultListener() {
//     *    &nbsp;@Override
//     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
//     *         Log.w("TEST","success");
//     *     }
//     *    &nbsp;@Override
//     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
//     *             serviceException)  {
//     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
//     *         Log.w("TEST",errorMsg);
//     *     }
//     * })
//     *</pre></blockquote>
//     * 
//     * @param request 删除 Bucket 标签请求 {@link DeleteBucketTaggingRequest}
//     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
//     */
//    void deleteBucketTaggingAsync(DeleteBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取存储桶（Bucket) 的访问权限信息（Access Control List, ACL）的同步方法.&nbsp;
     * <p>
     * ACL 权限包括读、写、读写权限. COS 中 Bucket 是有访问权限控制的.可以通过获取 Bucket 的 ACL 表({@link #putBucketACL(PutBucketACLRequest)})，来查看那些用户拥有 Bucket 访
     * 问权限.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7733"> https://cloud.tencent.com/document/product/436/7733.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E6.9F.A5.E8.AF.A2.E5.AD.98.E5.82.A8.E6.A1.B6-acl">查询存储桶ACL示例</a>
     * 
     * @param request 获取 Bucket 的 ACL 请求 {@link GetBucketACLRequest}
     * @return 获取 Bucket 的 ACL 请求返回的结果 {@link GetBucketACLResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketACLResult getBucketACL(GetBucketACLRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶（Bucket) 的访问权限信息（Access Control List, ACL）的异步方法.&nbsp;
     * <p>
     * ACL 权限包括读、写、读写权限.COS 中 Bucket 是有访问权限控制的.可以通过获取 Bucket 的 ACL 表({@link #putBucketACL(PutBucketACLRequest)})，来查看那些用户拥有 Bucket 访
     * 问权限.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7733"> https://cloud.tencent.com/document/product/436/7733.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E6.9F.A5.E8.AF.A2.E5.AD.98.E5.82.A8.E6.A1.B6-acl">查询存储桶ACL示例</a>
     * 
     * @param request 获取 Bucket 的 ACL 请求 {@link GetBucketACLRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketACLAsync(GetBucketACLRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 设置存储桶（Bucket） 的访问权限（Access Control List, ACL)的同步方法.&nbsp;
     * <p>
     * ACL 权限包括读、写、读写权限.
     * 写入 Bucket 的 ACL 可以通过 header头部："x-cos-acl"，"x-cos-grant-read"，"x-cos-grant-write"，
     * "x-cos-grant-full-control" 传入 ACL 信息，或者通过 Body 以 XML 格式传入 ACL 信息.这两种方式只
     * 能选择其中一种，否则引起冲突.
     * 传入新的 ACL 将覆盖原有 ACL信息.
     * 私有 Bucket 可以下可以给某个文件夹设置成公有，那么该文件夹下的文件都是公有；但是把文件夹设置成私有后，在该文件夹下的文件设置
     * 的公有属性，不会生效.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7737">https://cloud.tencent.com/document/product/436/7737.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AD.98.E5.82.A8.E6.A1.B6-acl">设置存储桶ACL示例</a>
     *
     * @param request 设置 Bucket 的ACL请求 {@link PutBucketACLRequest}
     * @return 设置 Bucket 的ACL请求返回的结果 {@link PutBucketACLResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketACLResult putBucketACL(PutBucketACLRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 设置存储桶（Bucket） 的访问权限（Access Control List, ACL)的异步方法.&nbsp;
     * <p>
     * ACL 权限包括读、写、读写权限.
     * 写入 Bucket 的 ACL 可以通过 header头部："x-cos-acl"，"x-cos-grant-read"，"x-cos-grant-write"，
     * "x-cos-grant-full-control" 传入 ACL 信息，或者通过 Body 以 XML 格式传入 ACL 信息.这两种方式只
     * 能选择其中一种，否则引起冲突.
     * 传入新的 ACL 将覆盖原有 ACL信息.
     * 私有 Bucket 可以下可以给某个文件夹设置成公有，那么该文件夹下的文件都是公有；但是把文件夹设置成私有后，在该文件夹下的文件设置
     * 的公有属性，不会生效.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7737">https://cloud.tencent.com/document/product/436/7737.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41912#.E8.AE.BE.E7.BD.AE.E5.AD.98.E5.82.A8.E6.A1.B6-acl">设置存储桶ACL示例</a>
     *
     * @param request 设置 Bucket 的ACL请求 {@link PutBucketACLRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketACLAsync(PutBucketACLRequest request, CosXmlResultListener cosXmlResultListener);


    GetBucketAccelerateResult getBucketAccelerate(GetBucketAccelerateRequest request) throws CosXmlClientException, CosXmlServiceException;

    void getBucketAccelerateAsync(GetBucketAccelerateRequest request, CosXmlResultListener cosXmlResultListener);

    PutBucketAccelerateResult putBucketAccelerate(PutBucketAccelerateRequest request) throws CosXmlClientException, CosXmlServiceException;

    void putBucketAccelerateAsync(PutBucketAccelerateRequest request, CosXmlResultListener cosXmlResultListener);

    //todo jordan 缺少该示例文档
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41904#.E6.9F.A5.E8.AF.A2.E7.94.9F.E5.91.BD.E5.91.A8.E6.9C.9F">获取存储桶地域示例</a>
    /**
     * <p>
     * 获取存储桶（Bucket) 所在的地域信息的同步方法.&nbsp;
     * <p>
     * 在创建 Bucket 时，需要指定所属该 Bucket 所属地域信息.
     * <p>
     * COS 支持的地域信息，可查看<a href="https://cloud.tencent.com/document/product/436/6224">https://cloud.tencent.com/document/product/436/6224.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8275">https://cloud.tencent.com/document/product/436/8275.</a>
     *
     * @param request 获取 Bucket 所在的地域信息请求 {@link GetBucketLocationRequest}
     * @return 获取 Bucket 所在的地域信息请求返回的结果 {@link GetBucketLocationResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketLocationResult getBucketLocation(GetBucketLocationRequest request) throws CosXmlClientException, CosXmlServiceException;


    //todo jordan 缺少该示例文档
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41904#.E6.9F.A5.E8.AF.A2.E7.94.9F.E5.91.BD.E5.91.A8.E6.9C.9F">获取存储桶地域示例</a>
    /**
     * <p>
     * 获取存储桶（Bucket) 所在的地域信息的异步方法.&nbsp;
     * <p>
     * 在创建 Bucket 时，需要指定所属该 Bucket 所属地域信息.
     * <p>
     * COS 支持的地域信息，可查看<a href="https://cloud.tencent.com/document/product/436/6224">https://cloud.tencent.com/document/product/436/6224.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8275">https://cloud.tencent.com/document/product/436/8275.</a>
     *
     * @param request 获取 Bucket 所在的地域信息请求 {@link GetBucketLocationRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketLocationAsync(GetBucketLocationRequest request, CosXmlResultListener cosXmlResultListener);


//    /**
//     * 获取存储桶（Bucket）的标签的同步方法.&nbsp;
//
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * GetBucketTaggingRequest request = new GetBucketTaggingRequest(bucket);
//     
//     * try {
//     *     GetBucketTaggingResult result = cosXml.getBucketTagging(request);
//     *     Log.w("TEST","success");
//     * } catch (CosXmlClientException e) {
//     *     Log.w("TEST","CosXmlClientException =" + e.toString());
//     * } catch (CosXmlServiceException e) {
//     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
//     * }
//     *</pre></blockquote>
//     * 
//     * @param request 获取 Bucket 的标签请求 {@link GetBucketTaggingRequest}
//     * @return GetBucketTaggingResult 获取 Bucket 的标签请求返回的结果 {@link GetBucketTaggingResult}
//     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
//     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
//     */
//    GetBucketTaggingResult getBucketTagging(GetBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException;
//
//    /**
//     * 获取存储桶（Bucket）的标签的异步方法.&nbsp;
//
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
//     * GetBucketTaggingRequest request = new GetBucketTaggingRequest(bucket);
//     
//     * cosXml.getBucketTaggingAsync(request, new CosXmlResultListener() {
//     *    &nbsp;@Override
//     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
//     *         Log.w("TEST","success");
//     *     }
//     *    &nbsp;@Override
//     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
//     *             serviceException)  {
//     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
//     *         Log.w("TEST",errorMsg);
//     *     }
//     * })
//     *</pre></blockquote>
//     * 
//     * @param request 获取存储桶 Bucket 的标签请求 {@link GetBucketTaggingRequest}
//     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
//     */
//    void getBucketTaggingAsync(GetBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener);


//    /**
//     * <p>
//     * 暂时不支持
//     * 
//     *
//     * @param request {@link PutBucketTaggingRequest}
//     * @return PutBucketTaggingResult {@link PutBucketTaggingResult}
//     * @throws CosXmlClientException {@link CosXmlClientException}
//     * @throws CosXmlServiceException {@link CosXmlServiceException}
//     */
//    PutBucketTaggingResult putBucketTagging(PutBucketTaggingRequest request) throws CosXmlClientException, CosXmlServiceException;
//
//    /**
//     * {@link CosXml#putBucketTagging(PutBucketTaggingRequest)} 的异步接口。
//     *
//     * @param request {@link PutBucketTaggingRequest}
//     * @param cosXmlResultListener {@link CosXmlResultListener}
//     */
//    void putBucketTaggingAsync(PutBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 获取存储桶（Bucket）版本控制信息的同步方法.&nbsp;
     * <p>
     * 通过查询版本控制信息，可以得知该 Bucket 的版本控制功能是处于禁用状态还是启用状态（Enabled 或者 Suspended）,
     * 开启版本控制功能，可参考{@link #putBucketVersioning(PutBucketVersioningRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19888">https://cloud.tencent.com/document/product/436/19888.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41901#.E6.9F.A5.E8.AF.A2.E7.89.88.E6.9C.AC.E6.8E.A7.E5.88.B6">查询存储桶版本控制示例</a>
     * 
     * @param request 获取 Bucket 版本控制信息请求 {@link GetBucketVersioningRequest}
     * @return 获取 Bucket 版本控制信息请求返回的结果 {@link GetBucketVersioningResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketVersioningResult getBucketVersioning(GetBucketVersioningRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶（Bucket）版本控制信息的异步方法.&nbsp;
     * <p>
     * 通过查询版本控制信息，可以得知该 Bucket 的版本控制功能是处于禁用状态还是启用状态（Enabled 或者 Suspended）,
     * 开启版本控制功能，可参考{@link #putBucketVersioning(PutBucketVersioningRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19888">https://cloud.tencent.com/document/product/436/19888.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41901#.E6.9F.A5.E8.AF.A2.E7.89.88.E6.9C.AC.E6.8E.A7.E5.88.B6">查询存储桶版本控制示例</a>
     * 
     * @param request 获取 Bucket 版本控制信息请求 {@link GetBucketVersioningRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketVersioningAsync(GetBucketVersioningRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 设置存储桶（Bucket）版本控制的同步方法.&nbsp;
     * <p>
     * 版本管理功能一经打开，只能暂停，不能关闭.
     * 通过版本控制，可以在一个 Bucket 中保留一个对象的多个版本.
     * 版本控制可以防止意外覆盖和删除对象，以便检索早期版本的对象.
     * 默认情况下，版本控制功能处于禁用状态，需要主动去启用或者暂停（Enabled 或者 Suspended）.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19888">https://cloud.tencent.com/document/product/436/19888.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41901#.E8.AE.BE.E7.BD.AE.E7.89.88.E6.9C.AC.E6.8E.A7.E5.88.B6">设置存储桶版本控制示例</a>
     *
     * @param request  Bucket 版本控制启用或者暂停请求 {@link PutBucketVersioningRequest}
     * @return Bucket 版本控制启用或者暂停请求返回的结果 {@link PutBucketVersioningResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketVersioningResult putBucketVersioning(PutBucketVersioningRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 设置存储桶（Bucket）版本控制的异步方法.&nbsp;
     * <p>
     * 版本管理功能一经打开，只能暂停，不能关闭.
     * 通过版本控制，可以在一个 Bucket 中保留一个对象的多个版本.
     * 版本控制可以防止意外覆盖和删除对象，以便检索早期版本的对象.
     * 默认情况下，版本控制功能处于禁用状态，需要主动去启用或者暂停（Enabled 或者 Suspended）.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19888">https://cloud.tencent.com/document/product/436/19888.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41901#.E8.AE.BE.E7.BD.AE.E7.89.88.E6.9C.AC.E6.8E.A7.E5.88.B6">设置存储桶版本控制示例</a>
     *
     * @param request Bucket 版本控制启用或者暂停请求 {@link PutBucketVersioningRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketVersionAsync(PutBucketVersioningRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取跨区域复制配置信息的同步方法.&nbsp;
     * <p>
     * 跨区域复制是支持不同区域 Bucket 自动异步复制对象, 请查阅{@link #putBucketReplication(PutBucketReplicationRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19222">https://cloud.tencent.com/document/product/436/19222.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41902#.E6.9F.A5.E8.AF.A2.E8.B7.A8.E5.9C.B0.E5.9F.9F.E5.A4.8D.E5.88.B6">查询跨地域复制示例</a>
     * 
     * @param request 获取跨区域复制配置信息请求 {@link GetBucketReplicationRequest}
     * @return 获取跨区域复制配置信息请求返回的结果 {@link GetBucketReplicationResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketReplicationResult getBucketReplication(GetBucketReplicationRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取跨区域复制配置信息的异步方法.&nbsp;
     * <p>
     * 跨区域复制是支持不同区域 Bucket 自动异步复制对象, 请查阅{@link #putBucketReplication(PutBucketReplicationRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19222">https://cloud.tencent.com/document/product/436/19222.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41902#.E6.9F.A5.E8.AF.A2.E8.B7.A8.E5.9C.B0.E5.9F.9F.E5.A4.8D.E5.88.B6">查询跨地域复制示例</a>
     * 
     * @param request 获取跨区域复制配置信息请求 {@link GetBucketReplicationRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketReplicationAsync(GetBucketReplicationRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 配置跨区域复制的同步方法.&nbsp;
     * <p>
     * 跨区域复制是支持不同区域 Bucket 自动异步复制对象.注意，不能是同区域的 Bucket, 且源 Bucket 和目
     * 标 Bucket 必须已启用版本控制{@link #putBucketVersioning(PutBucketVersioningRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19223">https://cloud.tencent.com/document/product/436/19223.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41902#.E8.AE.BE.E7.BD.AE.E8.B7.A8.E5.9C.B0.E5.9F.9F.E5.A4.8D.E5.88.B6">设置跨地域复制示例</a>
     *
     * @param request 配置跨区域复制请求 {@link PutBucketReplicationRequest}
     * @return 配置跨区域复制请求返回的结果 {@link PutBucketReplicationResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketReplicationResult putBucketReplication(PutBucketReplicationRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 配置跨区域复制的异步方法.&nbsp;
     * <p>
     * 跨区域复制是支持不同区域 Bucket 自动异步复制对象.注意，不能是同区域的 Bucket, 且源 Bucket 和目
     * 标 Bucket 必须已启用版本控制{@link #putBucketVersioning(PutBucketVersioningRequest)}.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19223">https://cloud.tencent.com/document/product/436/19223.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41902#.E8.AE.BE.E7.BD.AE.E8.B7.A8.E5.9C.B0.E5.9F.9F.E5.A4.8D.E5.88.B6">设置跨地域复制示例</a>
     *
     * @param request 配置跨区域复制请求 {@link PutBucketReplicationRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketReplicationAsync(PutBucketReplicationRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 删除跨区域复制配置的同步方法.&nbsp;
     * <p>
     * 当不需要进行跨区域复制时，可以删除 Bucket 的跨区域复制配置. 跨区域复制，可以查阅{@link #putBucketReplication(PutBucketReplicationRequest)}
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19221">https://cloud.tencent.com/document/product/436/19221.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41902#.E5.88.A0.E9.99.A4.E8.B7.A8.E5.9C.B0.E5.9F.9F.E5.A4.8D.E5.88.B6">删除跨地域复制示例</a>
     *
     * @param request 删除跨区域复制配置请求 {@link DeleteBucketReplicationRequest}
     * @return 删除跨区域复制配置请求返回的结果 {@link DeleteBucketReplicationResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteBucketReplicationResult deleteBucketReplication(DeleteBucketReplicationRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除跨区域复制配置的异步方法.&nbsp;
     * <p>
     * 当不需要进行跨区域复制时，可以删除 Bucket 的跨区域复制配置. 跨区域复制，可以查阅{@link #putBucketReplication(PutBucketReplicationRequest)}
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/19221">https://cloud.tencent.com/document/product/436/19221.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41902#.E5.88.A0.E9.99.A4.E8.B7.A8.E5.9C.B0.E5.9F.9F.E5.A4.8D.E5.88.B6">删除跨地域复制示例</a>
     *
     * @param request 删除跨区域复制配置请求 {@link DeleteBucketReplicationRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketReplicationAsync(DeleteBucketReplicationRequest request, CosXmlResultListener cosXmlResultListener);

    //todo jordan 需要补充此示例吗？ 改方法在SDK中废弃了吗?
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41902#.E5.88.A0.E9.99.A4.E8.B7.A8.E5.9C.B0.E5.9F.9F.E5.A4.8D.E5.88.B6">获取存储桶对象版本信息示例</a>
    /**
     * <p>
     * 获取存储桶（Bucket）所有或者部分对象的版本信息的同步方法.&nbsp;
     * <p>
     * 通过查看对象的版本信息，可以得知对象存在哪些版本,便于管理对象.如检索或者删除某个特定版本的对象.
     * 版本管理功能，请查阅{@link #putBucketVersioning(PutBucketVersioningRequest)}
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/35521">https://cloud.tencent.com/document/product/436/35521.</a>
     *
     * @param request 获取 Bucket 所有或者部分对象的版本信息请求 {@link ListBucketVersionsRequest}
     * @return Bucket 所有或者部分对象的版本信息请求返回的结果 {@link ListBucketVersionsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    ListBucketVersionsResult listBucketVersions(ListBucketVersionsRequest request) throws CosXmlClientException, CosXmlServiceException;


    //todo jordan 需要补充此示例吗？ 改方法在SDK中废弃了吗?
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41902#.E5.88.A0.E9.99.A4.E8.B7.A8.E5.9C.B0.E5.9F.9F.E5.A4.8D.E5.88.B6">获取存储桶对象版本信息示例</a>
    /**
     * <p>
     * 获取存储桶（Bucket）所有或者部分对象的版本信息的异步方法.&nbsp;
     * <p>
     * 通过查看对象的版本信息，可以得知对象存在哪些版本,便于管理对象.如检索或者删除某个特定版本的对象.
     * 版本管理功能，请查阅{@link #putBucketVersioning(PutBucketVersioningRequest)}
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/35521">https://cloud.tencent.com/document/product/436/35521.</a>
     *
     * @param request 获取 Bucket 所有或者部分对象的版本信息请求 {@link ListBucketVersionsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void listBucketVersionsAsync(ListBucketVersionsRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 查询与存储桶关联的静态网站配置信息的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31929">https://cloud.tencent.com/document/product/436/31929.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E6.9F.A5.E8.AF.A2.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">查询静态网站配置示例</a>
     *
     * @param request 查询与存储桶关联的静态网站配置信息的请求 {@link GetBucketWebsiteRequest}
     * @return 查询与存储桶关联的静态网站配置信息的返回结果 {@link GetBucketWebsiteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketWebsiteResult getBucketWebsite(GetBucketWebsiteRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询与存储桶关联的静态网站配置信息的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31929">https://cloud.tencent.com/document/product/436/31929.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E6.9F.A5.E8.AF.A2.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">查询静态网站配置示例</a>
     *
     * @param request 查询与存储桶关联的静态网站配置信息的请求 {@link GetBucketWebsiteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketWebsiteAsync(GetBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 设置存储桶静态网站的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31930">https://cloud.tencent.com/document/product/436/31930.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">设置静态网站示例</a>
     *
     * @param request 设置存储桶静态网站请求 {@link PutBucketWebsiteRequest}
     * @return 设置存储桶静态网站返回结果 {@link PutBucketWebsiteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketWebsiteResult putBucketWebsite(PutBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 设置存储桶静态网站的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31930">https://cloud.tencent.com/document/product/436/31930.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E8.AE.BE.E7.BD.AE.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99">设置静态网站示例</a>
     *
     * @param request 设置存储桶静态网站请求 {@link PutBucketWebsiteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketWebsiteAsync(PutBucketWebsiteRequest request,  CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 删除存储桶中静态网站配置的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31928">https://cloud.tencent.com/document/product/436/31928.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E5.88.A0.E9.99.A4.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">删除静态网站配置示例</a>
     *
     * @param request 删除存储桶中静态网站配置的请求 {@link DeleteBucketWebsiteRequest}
     * @return 删除存储桶中静态网站配置的返回结果 {@link DeleteBucketWebsiteResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteBucketWebsiteResult deleteBucketWebsite(DeleteBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除存储桶中静态网站配置的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31928">https://cloud.tencent.com/document/product/436/31928.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E5.88.A0.E9.99.A4.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">删除静态网站配置示例</a>
     *
     * @param request 删除存储桶中静态网站配置的请求 {@link DeleteBucketWebsiteRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketWebsiteAsync(DeleteBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 用于查询指定存储桶日志配置信息的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/17053">https://cloud.tencent.com/document/product/436/17053.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41905#.E6.9F.A5.E8.AF.A2.E6.97.A5.E5.BF.97.E7.AE.A1.E7.90.86">查询日志管理示例</a>
     *
     * @param request 用于查询指定存储桶日志配置信息的请求 {@link GetBucketLoggingRequest}
     * @return 用于查询指定存储桶日志配置信息的的返回结果 {@link GetBucketLoggingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketLoggingResult getBucketLogging(GetBucketLoggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 用于查询指定存储桶日志配置信息的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/17053">https://cloud.tencent.com/document/product/436/17053.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41905#.E6.9F.A5.E8.AF.A2.E6.97.A5.E5.BF.97.E7.AE.A1.E7.90.86">查询日志管理示例</a>
     *
     * @param request 用于查询指定存储桶日志配置信息的请求 {@link GetBucketLoggingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketLoggingAsync(GetBucketLoggingRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 为源存储桶开启日志记录的同步方法.&nbsp;
     * <p>
     * 该请求用于为源存储桶开启日志记录，将源存储桶的访问日志保存到指定的目标存储桶中
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/17054">https://cloud.tencent.com/document/product/436/17054.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41905#.E8.AE.BE.E7.BD.AE.E6.97.A5.E5.BF.97.E7.AE.A1.E7.90.86">设置日志管理示例</a>
     *
     * @param request 为源存储桶开启日志记录的请求 {@link PutBucketLoggingRequest}
     * @return 为源存储桶开启日志记录的返回结果 {@link PutBucketLoggingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketLoggingResult putBucketLogging(PutBucketLoggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 为源存储桶开启日志记录的异步方法.&nbsp;
     * <p>
     * 该请求用于为源存储桶开启日志记录，将源存储桶的访问日志保存到指定的目标存储桶中
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/17054">https://cloud.tencent.com/document/product/436/17054.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41905#.E8.AE.BE.E7.BD.AE.E6.97.A5.E5.BF.97.E7.AE.A1.E7.90.86">设置日志管理示例</a>
     *
     * @param request 为源存储桶开启日志记录的请求 {@link PutBucketLoggingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketLoggingAsync(PutBucketLoggingRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 获取存储桶标签的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/34837">https://cloud.tencent.com/document/product/436/34837.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41906#.E6.9F.A5.E8.AF.A2.E5.AD.98.E5.82.A8.E6.A1.B6.E6.A0.87.E7.AD.BE">查询存储桶标签示例</a>
     *
     * @param request 查询存储桶标签的请求 {@link GetBucketTaggingRequest}
     * @return 查询存储桶标签的返回结果 {@link GetBucketTaggingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketTaggingResult getBucketTagging(GetBucketTaggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶标签的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/34837">https://cloud.tencent.com/document/product/436/34837.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41906#.E6.9F.A5.E8.AF.A2.E5.AD.98.E5.82.A8.E6.A1.B6.E6.A0.87.E7.AD.BE">查询存储桶标签示例</a>
     *
     * @param request 查询存储桶标签的请求 {@link GetBucketTaggingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketTaggingAsync(GetBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 为已存在的存储桶设置标签的同步方法.&nbsp;
     * <p>
     * 该请求用于为存储桶设置键值对作为存储桶标签，可以协助您管理已有的存储桶资源，并通过标签进行成本管理。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/34838">https://cloud.tencent.com/document/product/436/34838.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41906#.E8.AE.BE.E7.BD.AE.E5.AD.98.E5.82.A8.E6.A1.B6.E6.A0.87.E7.AD.BE">设置存储桶标签示例</a>
     *
     * @param request 为已存在的存储桶设置标签的请求 {@link PutBucketTaggingRequest}
     * @return 为已存在的存储桶设置标签的返回结果 {@link PutBucketTaggingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketTaggingResult putBucketTagging(PutBucketTaggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 为已存在的存储桶设置标签的异步方法.&nbsp;
     * <p>
     * 该请求用于为存储桶设置键值对作为存储桶标签，可以协助您管理已有的存储桶资源，并通过标签进行成本管理。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/34838">https://cloud.tencent.com/document/product/436/34838.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41906#.E8.AE.BE.E7.BD.AE.E5.AD.98.E5.82.A8.E6.A1.B6.E6.A0.87.E7.AD.BE">设置存储桶标签示例</a>
     *
     * @param request 为已存在的存储桶设置标签的请求 {@link PutBucketTaggingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketTaggingAsync(PutBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 删除存储桶标签的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/34836">https://cloud.tencent.com/document/product/436/34836.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41906#.E5.88.A0.E9.99.A4.E5.AD.98.E5.82.A8.E6.A1.B6.E6.A0.87.E7.AD.BE">删除存储桶标签示例</a>
     *
     * @param request 删除存储桶标签的请求 {@link DeleteBucketTaggingRequest}
     * @return 删除存储桶标签的返回结果 {@link DeleteBucketTaggingResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteBucketTaggingResult deleteBucketTagging(DeleteBucketTaggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除存储桶标签的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/34836">https://cloud.tencent.com/document/product/436/34836.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41906#.E5.88.A0.E9.99.A4.E5.AD.98.E5.82.A8.E6.A1.B6.E6.A0.87.E7.AD.BE">删除存储桶标签示例</a>
     *
     * @param request 删除存储桶标签的请求 {@link DeleteBucketTaggingRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketTaggingAsync(DeleteBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 获取存储桶中指定清单任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/33705">https://cloud.tencent.com/document/product/436/33705.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41911#.E6.9F.A5.E8.AF.A2.E6.B8.85.E5.8D.95.E4.BB.BB.E5.8A.A1">查询清单任务示例</a>
     *
     * @param request 获取存储桶中清单任务的请求 {@link GetBucketInventoryRequest}
     * @return 获取存储桶中清单任务的返回结果 {@link GetBucketInventoryResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketInventoryResult getBucketInventory(GetBucketInventoryRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶中指定清单任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/33705">https://cloud.tencent.com/document/product/436/33705.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41911#.E6.9F.A5.E8.AF.A2.E6.B8.85.E5.8D.95.E4.BB.BB.E5.8A.A1">查询清单任务示例</a>
     *
     * @param request 获取存储桶中清单任务的请求 {@link GetBucketInventoryRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketInventoryAsync(GetBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 在存储桶中创建清单任务的同步方法.&nbsp;
     * <p>
     * 在存储桶中创建清单任务，您可以对清单任务命名后，使用该请求创建清单任务。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/33707">https://cloud.tencent.com/document/product/436/33707.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41911#.E8.AE.BE.E7.BD.AE.E6.B8.85.E5.8D.95.E4.BB.BB.E5.8A.A1">设置清单任务示例</a>
     *
     * @param request 在存储桶中创建清单任务的请求 {@link PutBucketInventoryRequest}
     * @return 在存储桶中创建清单任务的返回结果 {@link PutBucketInventoryResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketInventoryResult putBucketInventory(PutBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 在存储桶中创建清单任务的异步方法.&nbsp;
     * <p>
     * 在存储桶中创建清单任务，您可以对清单任务命名后，使用该请求创建清单任务。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/33707">https://cloud.tencent.com/document/product/436/33707.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41911#.E8.AE.BE.E7.BD.AE.E6.B8.85.E5.8D.95.E4.BB.BB.E5.8A.A1">设置清单任务示例</a>
     *
     * @param request 在存储桶中创建清单任务的请求 {@link PutBucketInventoryRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketInventoryAsync(PutBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 删除存储桶中指定清单任务的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/33704">https://cloud.tencent.com/document/product/436/33704.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41911#.E5.88.A0.E9.99.A4.E6.B8.85.E5.8D.95.E4.BB.BB.E5.8A.A1">删除清单任务示例</a>
     *
     * @param request 删除存储桶中指定清单任务的请求 {@link DeleteBucketInventoryRequest}
     * @return 删除存储桶中指定清单任务的返回结果 {@link DeleteBucketInventoryResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteBucketInventoryResult deleteBucketInventory(DeleteBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除存储桶中指定清单任务的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/33704">https://cloud.tencent.com/document/product/436/33704.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41911#.E5.88.A0.E9.99.A4.E6.B8.85.E5.8D.95.E4.BB.BB.E5.8A.A1">删除清单任务示例</a>
     *
     * @param request 删除存储桶中指定清单任务的请求 {@link DeleteBucketInventoryRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketInventoryAsync(DeleteBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener);

    //todo jordan 无此示例 待补充
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41911#.E5.88.A0.E9.99.A4.E6.B8.85.E5.8D.95.E4.BB.BB.E5.8A.A1">查询所有清单任务示例</a>
    /**
     * <p>
     * 查询存储桶中所有清单任务的同步方法.&nbsp;
     * <p>
     * 用于请求返回一个存储桶中的所有清单任务。每一个存储桶中最多配置1000条清单任务。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/33706">https://cloud.tencent.com/document/product/436/33706.</a>
     *
     * @param request 查询存储桶中所有清单任务的请求 {@link ListBucketInventoryRequest}
     * @return 查询存储桶中所有清单任务的返回结果 {@link ListBucketInventoryResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    ListBucketInventoryResult listBucketInventory(ListBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException;


    //todo jordan 无此示例 待补充
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41911#.E5.88.A0.E9.99.A4.E6.B8.85.E5.8D.95.E4.BB.BB.E5.8A.A1">查询所有清单任务示例</a>
    /**
     * <p>
     * 查询存储桶中所有清单任务的异步方法.&nbsp;
     * <p>
     * 用于请求返回一个存储桶中的所有清单任务。每一个存储桶中最多配置1000条清单任务。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/33706">https://cloud.tencent.com/document/product/436/33706.</a>
     *
     * @param request 查询存储桶中所有清单任务的请求 {@link ListBucketInventoryRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void listBucketInventoryAsync(ListBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 为存储桶配置自定义域名的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/43882">https://cloud.tencent.com/document/product/436/43882.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E8.AE.BE.E7.BD.AE.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">设置自定义域名示例</a>
     *
     * @param request 为存储桶配置自定义域名的请求 {@link PutBucketDomainRequest}
     * @return 为存储桶配置自定义域名的返回结果 {@link PutBucketDomainResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketDomainResult putBucketDomain(PutBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 为存储桶配置自定义域名的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/43882">https://cloud.tencent.com/document/product/436/43882.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E8.AE.BE.E7.BD.AE.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">设置自定义域名示例</a>
     *
     * @param request 为存储桶配置自定义域名的请求 {@link PutBucketDomainRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketDomainAsync(PutBucketDomainRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 查询存储桶自定义域名信息的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/43883">https://cloud.tencent.com/document/product/436/43883.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">查询自定义域名示例</a>
     *
     * @param request 查询存储桶自定义域名信息的请求 {@link GetBucketDomainRequest}
     * @return 查查询存储桶自定义域名信息的返回结果 {@link GetBucketDomainResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketDomainResult getBucketDomain(GetBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询存储桶自定义域名信息的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/43883">https://cloud.tencent.com/document/product/436/43883.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">查询自定义域名示例</a>
     *
     * @param request 查询存储桶自定义域名信息的请求 {@link GetBucketDomainRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketDomainAsync(GetBucketDomainRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 删除存储桶自定义域名信息的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/43885">https://cloud.tencent.com/document/product/436/43885.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">删除自定义域名示例</a>
     *
     * @param request 删除存储桶自定义域名信息的请求 {@link DeleteBucketDomainRequest}
     * @return 查删除存储桶自定义域名信息的返回结果 {@link DeleteBucketDomainResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteBucketDomainResult deleteBucketDomain(DeleteBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除存储桶自定义域名信息的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/43885">https://cloud.tencent.com/document/product/436/43885.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">删除自定义域名示例</a>
     *
     * @param request 删除存储桶自定义域名信息的请求 {@link DeleteBucketDomainRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketDomainAsync(DeleteBucketDomainRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取存储桶防盗链配置的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/32493">https://cloud.tencent.com/document/product/436/32493.</a>
     * <br>
     * <p>
     *
     * @param request 获取存储桶防盗链配置的请求 {@link GetBucketRefererRequest}
     * @return 获取存储桶防盗链配置的返回结果 {@link GetBucketRefererResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketRefererResult getBucketReferer(GetBucketRefererRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶防盗链配置的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/32493">https://cloud.tencent.com/document/product/436/32493.</a>
     * <br>
     * <p>
     *
     * @param request 查询与存储桶关联的静态网站配置信息的请求 {@link GetBucketRefererRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketRefererAsync(GetBucketRefererRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 设置存储桶防盗链的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/32492">https://cloud.tencent.com/document/product/436/32492.</a>
     * <br>
     * <p>
     * @param request 设置存储桶防盗链请求 {@link PutBucketRefererRequest}
     * @return 设置存储桶防盗链返回结果 {@link PutBucketRefererResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketRefererResult putBucketReferer(PutBucketRefererRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 设置存储桶防盗链的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/32492">https://cloud.tencent.com/document/product/436/32492.</a>
     * <br>
     * <p>
     * @param request 设置存储桶防盗链请求 {@link PutBucketRefererRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketRefererAsync(PutBucketRefererRequest request,  CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取存储桶权限策略配置的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8276">https://cloud.tencent.com/document/product/436/8276.</a>
     * <br>
     * <p>
     * @param request 获取存储桶权限策略配置的请求 {@link GetBucketPolicyRequest}
     * @return 获取存储桶权限策略配置的返回结果 {@link GetBucketPolicyResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketPolicyResult getBucketPolicy(GetBucketPolicyRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶权限策略配置的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8276">https://cloud.tencent.com/document/product/436/8276.</a>
     * <br>
     * <p>
     * @param request 获取存储桶权限策略配置的请求 {@link GetBucketPolicyRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketPolicyAsync(GetBucketPolicyRequest request, CosXmlResultListener cosXmlResultListener);
    
    /**
     * <p>
     * 设置存储桶权限策略的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8282">https://cloud.tencent.com/document/product/436/8282.</a>
     * <br>
     * <p>
     * @param request 设置存储桶权限策略请求 {@link PutBucketPolicyRequest}
     * @return 设置存储桶权限策略返回结果 {@link PutBucketPolicyResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketPolicyResult putBucketPolicy(PutBucketPolicyRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 设置存储桶权限策略的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8282">https://cloud.tencent.com/document/product/436/8282.</a>
     * <br>
     * <p>
     * @param request 设置存储桶权限策略请求 {@link PutBucketPolicyRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketPolicyAsync(PutBucketPolicyRequest request,  CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 删除存储桶权限策略的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8285">https://cloud.tencent.com/document/product/436/8285.</a>
     * <br>
     * <p>
     * @param request 删除存储桶权限策略请求 {@link DeleteBucketPolicyRequest}
     * @return 删除存储桶权限策略返回结果 {@link DeleteBucketPolicyResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteBucketPolicyResult deleteBucketPolicy(DeleteBucketPolicyRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除存储桶权限策略的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8285">https://cloud.tencent.com/document/product/436/8285.</a>
     * <br>
     * <p>
     * @param request 删除存储桶权限策略请求 {@link DeleteBucketPolicyRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketPolicyAsync(DeleteBucketPolicyRequest request,  CosXmlResultListener cosXmlResultListener);

    //todo jordan 缺少示例
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">结构化查询对象示例</a>
    /**
     * <p>
     * 使用结构化查询语句从指定对象（CSV 格式或者 JSON 格式）中检索内容的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/37641">https://cloud.tencent.com/document/product/436/37641.</a>
     *
     * @param request 使用结构化查询语句从指定对象中检索内容的请求 {@link SelectObjectContentRequest}
     * @return 使用结构化查询语句从指定对象中检索内容的返回结果 {@link SelectObjectContentResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    SelectObjectContentResult selectObjectContent(SelectObjectContentRequest request) throws CosXmlClientException, CosXmlServiceException;


    //todo jordan 缺少示例
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">结构化查询对象示例</a>
    /**
     * <p>
     * 使用结构化查询语句从指定对象（CSV 格式或者 JSON 格式）中检索内容的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/37641">https://cloud.tencent.com/document/product/436/37641.</a>
     *
     * @param request 使用结构化查询语句从指定对象中检索内容的请求 {@link SelectObjectContentRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void selectObjectContentAsync(SelectObjectContentRequest request, CosXmlResultListener cosXmlResultListener);

    //todo jordan 缺少示例
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">获取所有对象及历史版本示例</a>
    /**
     * <p>
     * 拉取存储桶内的所有对象及其历史版本信息的同步方法.&nbsp;
     * <p>
     * 用于拉取存储桶内的所有对象及其历史版本信息，您可以通过指定参数筛选出存储桶内部分对象及其历史版本信息。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/35521">https://cloud.tencent.com/document/product/436/35521.</a>
     *
     * @param request 拉取存储桶内的所有对象及其历史版本信息的请求 {@link GetBucketObjectVersionsRequest}
     * @return 拉取存储桶内的所有对象及其历史版本信息的返回结果 {@link GetBucketObjectVersionsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketObjectVersionsResult getBucketObjectVersions(GetBucketObjectVersionsRequest request) throws CosXmlClientException, CosXmlServiceException;

    //todo jordan 缺少示例
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">获取所有对象及历史版本示例</a>
    /**
     * <p>
     * 拉取存储桶内的所有对象及其历史版本信息的异步方法.&nbsp;
     * <p>
     * 用于拉取存储桶内的所有对象及其历史版本信息，您可以通过指定参数筛选出存储桶内部分对象及其历史版本信息。
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/35521">https://cloud.tencent.com/document/product/436/35521.</a>
     *
     * @param request 拉取存储桶内的所有对象及其历史版本信息的请求 {@link GetBucketObjectVersionsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketObjectVersionsAsync(GetBucketObjectVersionsRequest request, CosXmlResultListener cosXmlResultListener);

    //todo rickenwang 缺少示例和 API 接口
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">获取所有对象及历史版本示例</a>
    /**
     * <p>
     * 启用存储桶智能分层存储配置能力的同步方法.&nbsp;
     * <p>
     * 启用智能分层能力后，只能进行参数编辑，无法关闭。
     * <p>
     * API 接口：
     *
     * @param request 启用存储桶智能分层存储配置能力的请求 {@link PutBucketIntelligentTieringRequest}
     * @return 启用存储桶智能分层存储配置能力的返回结果 {@link PutBucketIntelligentTieringResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutBucketIntelligentTieringResult putBucketIntelligentTiering(PutBucketIntelligentTieringRequest request) throws CosXmlClientException, CosXmlServiceException;

    //todo rickenwang 缺少示例和 API 接口
    //SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41910#.E6.9F.A5.E8.AF.A2.E8.87.AA.E5.AE.9A.E4.B9.89.E5.9F.9F.E5.90.8D">获取所有对象及历史版本示例</a>
    /**
     * <p>
     * 启用存储桶智能分层存储配置能力的异步方法.&nbsp;
     * <p>
     * 启用智能分层能力后，只能进行参数编辑，无法关闭。
     * <p>
     * API 接口：
     *
     * @param request 启用存储桶智能分层存储配置能力的请求 {@link PutBucketIntelligentTieringRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketIntelligentTieringAsync(PutBucketIntelligentTieringRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取存储桶智能分层存储配置能力的同步方法.&nbsp;
     * <p>
     * API 接口：
     *
     * @param request 启用存储桶智能分层存储配置能力的请求 {@link GetBucketIntelligentTieringRequest}
     * @return 启用存储桶智能分层存储配置能力的返回结果 {@link GetBucketIntelligentTieringResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetBucketIntelligentTieringResult getBucketIntelligentTiering(GetBucketIntelligentTieringRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 取存储桶智能分层存储配置能力的异步方法.&nbsp;
     * <p>
     * API 接口：
     *
     * @param request 启用存储桶智能分层存储配置能力的请求 {@link GetBucketIntelligentTieringRequest}
     * @param resultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketIntelligentTieringAsync(GetBucketIntelligentTieringRequest request, CosXmlResultListener resultListener);

    /**
     * <p>
     * 预览文档的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54058">https://cloud.tencent.com/document/product/436/54058.</a>
     *
     * @param request 预览文档的请求 {@link PreviewDocumentRequest}
     * @return 预览文档的返回结果 {@link PreviewDocumentResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PreviewDocumentResult previewDocument(PreviewDocumentRequest request) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 预览文档的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54058">https://cloud.tencent.com/document/product/436/54058.</a>
     *
     * @param request 预览文档的请求 {@link PreviewDocumentRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void previewDocumentAsync(PreviewDocumentRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 以HTML格式预览文档的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54059">https://cloud.tencent.com/document/product/436/54059.</a>
     *
     * @param request 以HTML格式预览文档的请求 {@link PreviewDocumentInHtmlRequest}
     * @return 以HTML格式预览文档的返回结果 {@link PreviewDocumentInHtmlResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PreviewDocumentInHtmlResult previewDocumentInHtml(PreviewDocumentInHtmlRequest request) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 以HTML格式预览文档的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54059">https://cloud.tencent.com/document/product/436/54059.</a>
     *
     * @param request 以HTML格式预览文档的请求 {@link PreviewDocumentInHtmlRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void previewDocumentInHtmlAsync(PreviewDocumentInHtmlRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 以HTML格式链接预览文档的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54060#3.-.E8.8E.B7.E5.8F.96.E5.9C.A8.E7.BA.BF.E6.96.87.E6.A1.A3.E9.A2.84.E8.A7.88.E5.9C.B0.E5.9D.80">https://cloud.tencent.com/document/product/436/54060.</a>
     *
     * @param request 以HTML格式链接预览文档的请求 {@link PreviewDocumentInHtmlLinkRequest}
     * @return 以HTML格式链接预览文档的返回结果 {@link PreviewDocumentInHtmlLinkResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PreviewDocumentInHtmlLinkResult previewDocumentInHtmlLink(PreviewDocumentInHtmlLinkRequest request) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 以HTML格式链接预览文档的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/54060#3.-.E8.8E.B7.E5.8F.96.E5.9C.A8.E7.BA.BF.E6.96.87.E6.A1.A3.E9.A2.84.E8.A7.88.E5.9C.B0.E5.9D.80">https://cloud.tencent.com/document/product/436/54060.</a>
     *
     * @param request 以HTML格式链接预览文档的请求 {@link PreviewDocumentInHtmlLinkRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void previewDocumentInHtmlLinkAsync(PreviewDocumentInHtmlLinkRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 以HTML格式直出内容预览文档到字节数组的同步方法<br>
     * 和{@link #previewDocumentInHtml(PreviewDocumentInHtmlRequest)}类似，只是返回结果形式不同
     * </p>
     * <p>
     * 注意：请不要通过本接口预览大文件，否则容易造成内存溢出。
     * </p>
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象远端路径，即存储到 COS 上的绝对路径
     * @return 预览结果的字节数据
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    byte[] previewDocumentInHtmlBytes(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取截图的同步方法
     * </p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/55671">https://cloud.tencent.com/document/product/436/55671.</a>
     *
     * @param request 获取截图的同步方法 {@link GetSnapshotRequest}
     * @return 获取截图的返回结果 {@link GetSnapshotResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetSnapshotResult getSnapshot(GetSnapshotRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取截图的异步方法
     * </p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/55671">https://cloud.tencent.com/document/product/436/55671.</a>
     *
     * @param request 获取截图的异步方法 {@link GetSnapshotRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getSnapshotAsync(GetSnapshotRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取媒体文件信息的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31929">https://cloud.tencent.com/document/product/436/31929.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E6.9F.A5.E8.AF.A2.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">获取媒体文件信息的示例</a>
     *
     * @param request 获取媒体文件信息的请求 {@link GetMediaInfoRequest}
     * @return 获取媒体文件信息的返回结果 {@link GetMediaInfoResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetMediaInfoResult getMediaInfo(GetMediaInfoRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取媒体文件信息的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31929">https://cloud.tencent.com/document/product/436/31929.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E6.9F.A5.E8.AF.A2.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">获取媒体文件信息的示例</a>
     *
     * @param request 获取媒体文件信息的请求 {@link GetMediaInfoRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getMediaInfoAsync(GetMediaInfoRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 查询已经开通媒体处理功能的存储桶的同步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31929">https://cloud.tencent.com/document/product/436/31929.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E6.9F.A5.E8.AF.A2.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">查询已经开通媒体处理功能存储桶的示例</a>
     *
     * @param request 获取存储桶防盗链配置的请求 {@link GetDescribeMediaBucketsRequest}
     * @return 获取存储桶防盗链配置的返回结果 {@link GetDescribeMediaBucketsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    GetDescribeMediaBucketsResult getDescribeMediaBuckets(GetDescribeMediaBucketsRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询已经开通媒体处理功能的存储桶的异步方法.&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/31929">https://cloud.tencent.com/document/product/436/31929.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/41907#.E6.9F.A5.E8.AF.A2.E9.9D.99.E6.80.81.E7.BD.91.E7.AB.99.E9.85.8D.E7.BD.AE">查询已经开通媒体处理功能存储桶的示例</a>
     *
     * @param request 查询已经开通媒体处理功能的存储桶的请求 {@link GetDescribeMediaBucketsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getDescribeMediaBucketsAsync(GetDescribeMediaBucketsRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 判断指定的 bucket 是否存在的同步方法。
     * </p>
     * <p>
     * 基于 {@link #getBucketACL(GetBucketACLRequest)} 实现
     * </p>
     *
     * @param bucketName bucket 名称，如 bucket-1250000000 等
     * @return true/false
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    boolean doesBucketExist(String bucketName) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 判断指定的 bucket 是否存在的异步方法。
     * </p>
     * <p>
     * 基于 {@link #getBucketACLAsync(GetBucketACLRequest, CosXmlResultListener)} 实现
     * </p>
     *
     * @param bucketName bucket 名称，如 bucket-1250000000 等
     * @param booleanListener 结果回调函数
     */
    void doesBucketExistAsync(String bucketName, CosXmlBooleanListener booleanListener);

    /**
     * <p>
     * 判断存储桶下指定对象是否存在的同步方法。
     * </p>
     * <p>
     * 基于 {@link SimpleCosXml#headObject(HeadObjectRequest)} 实现
     * </p>
     *
     * @param bucketName bucket 名称, 如 test-1250000000
     * @param objectName 对象在 COS 上的路径，比如 image/me.png
     * @return true/false
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    boolean doesObjectExist(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 判断存储桶下指定对象是否存在的异步方法。
     * </p>
     * <p>
     * 基于 {@link SimpleCosXml#headObjectAsync(HeadObjectRequest, CosXmlResultListener)} 实现
     * </p>
     *
     * @param bucketName bucket 名称, 如 test-1250000000
     * @param objectName 对象在 COS 上的路径，比如 image/me.png
     * @param booleanListener 结果回调函数
     */
    void doesObjectExistAsync(String bucketName, String objectName, CosXmlBooleanListener booleanListener);


    /**
     * <p>
     * 删除 COS 上单个对象的同步方法。
     * </p>
     * <p>
     * 基于 {@link SimpleCosXml#deleteObject(DeleteObjectRequest)} 实现
     * </p>
     *
     * @param bucketName bucket 名称, 如 test-1250000000
     * @param objectName 对象在 COS 上的路径，比如 image/me.png
     * @return 删除是否成功
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    boolean deleteObject(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除 COS 上单个对象的异步方法。
     * </p>
     * <p>
     * 基于 {@link SimpleCosXml#deleteObjectAsync(DeleteObjectRequest, CosXmlResultListener)} 实现
     * </p>
     *
     * @param bucketName bucket 名称, 如 test-1250000000
     * @param objectName 对象在 COS 上的路径，比如 image/me.png
     * @param booleanListener 结果回调函数
     */
    void deleteObjectAsync(String bucketName, String objectName, CosXmlBooleanListener booleanListener);

    /**
     * <p>
     * 更新对象元数据的同步方法。 建议使用{@link CosXml#updateObjectMetaData}
     * {@link CosXml#updateObjectMeta}：bucketName不含appid，比如：test
     * {@link CosXml#updateObjectMetaData}：bucketName包含appid，比如：test-1250000000
     *
     * </p>
     * <p>
     * 基于 {@link SimpleCosXml#copyObject(CopyObjectRequest)} 实现
     * </p>
     *
     * @param bucketName bucket 名称, 如 test
     * @param objectName 对象在 COS 上的路径，比如 image/me.png
     * @param metaData 对象元数据
     * @return 更新对象元数据是否成功
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    @Deprecated
    boolean updateObjectMeta(String bucketName, String objectName, COSMetaData metaData) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 更新对象元数据的异步方法。 建议使用{@link CosXml#updateObjectMetaDataAsync}
     * {@link CosXml#updateObjectMetaAsync}：bucketName不含appid，比如：test
     * {@link CosXml#updateObjectMetaDataAsync}：bucketName包含appid，比如：test-1250000000
     *
     * </p>
     * <p>
     * 基于 {@link SimpleCosXml#copyObjectAsync(CopyObjectRequest, CosXmlResultListener)} 实现
     * </p>
     *
     * @param bucketName bucket 名称, 如 test
     * @param objectName 对象在 COS 上的路径，比如 image/me.png
     * @param metaData 对象元数据
     * @param booleanListener 结果回调函数
     */
    @Deprecated
    void updateObjectMetaAsync(String bucketName, String objectName, COSMetaData metaData, final CosXmlBooleanListener booleanListener);

    /**
     * <p>
     * 更新对象元数据的同步方法。
     * </p>
     * <p>
     * 基于 {@link SimpleCosXml#copyObject(CopyObjectRequest)} 实现
     * </p>
     *
     * @param bucketName bucket 名称, 如 test-1250000000
     * @param objectName 对象在 COS 上的路径，比如 image/me.png
     * @param metaData 对象元数据
     * @return 更新对象元数据是否成功
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    boolean updateObjectMetaData(String bucketName, String objectName, COSMetaData metaData) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 更新对象元数据的异步方法。
     * </p>
     * <p>
     * 基于 {@link SimpleCosXml#copyObjectAsync(CopyObjectRequest, CosXmlResultListener)} 实现
     * </p>
     *
     * @param bucketName bucket 名称, 如 test-1250000000
     * @param objectName 对象在 COS 上的路径，比如 image/me.png
     * @param metaData 对象元数据
     * @param booleanListener 结果回调函数
     */
    void updateObjectMetaDataAsync(String bucketName, String objectName, COSMetaData metaData, final CosXmlBooleanListener booleanListener);
}
