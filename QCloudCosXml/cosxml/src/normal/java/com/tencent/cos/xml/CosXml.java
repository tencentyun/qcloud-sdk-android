package com.tencent.cos.xml;


import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlBooleanListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
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
import com.tencent.cos.xml.model.object.DeleteMultiObjectRequest;
import com.tencent.cos.xml.model.object.DeleteMultiObjectResult;
import com.tencent.cos.xml.model.object.GetObjectACLRequest;
import com.tencent.cos.xml.model.object.GetObjectACLResult;
import com.tencent.cos.xml.model.object.OptionObjectRequest;
import com.tencent.cos.xml.model.object.OptionObjectResult;
import com.tencent.cos.xml.model.object.PutObjectACLRequest;
import com.tencent.cos.xml.model.object.PutObjectACLResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.RestoreRequest;
import com.tencent.cos.xml.model.object.RestoreResult;
import com.tencent.cos.xml.model.object.SelectObjectContentRequest;
import com.tencent.cos.xml.model.object.SelectObjectContentResult;
import com.tencent.cos.xml.model.object.UploadPartCopyRequest;
import com.tencent.cos.xml.model.object.UploadPartCopyResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.cos.xml.model.tag.COSMetaData;

/**
 * <p>
 * 对象存储（Cloud Object Storage，简称：COS）是腾讯云提供的一种存储海量文件的分布式存储服务，用户可
 * 通过网络随时存储和查看数据.COS 介绍网址:<a href="https://cloud.tencent.com/document/product/436/6222">https://cloud.tencent.com/document/product/436/6222</a>
 * <p>
 * cos Android SDK 提供访问腾讯云COS服务的SDK接口，注意这里封装的是COS XML接口，COS JSON接口相关的SDK请参见<a
 * href="https://cloud.tencent.com/document/product/436/6517">COS V4</a>，由于COS XML接口相比JSON接口
 * 有更丰富的特性，因此我们更推荐您使用XML SDK。
 *
 * <p>
 * {@link SimpleCosXml}为每个接口都提供了同步和异步操作，如同步接口为{@link SimpleCosXml#putObject(PutObjectRequest)}，
 * 则对应的异步接口为{@link SimpleCosXml#putObjectAsync(PutObjectRequest, CosXmlResultListener)}.<br>
 *
 * COS XML SDK中一共有三种类型的接口：<br>
 * <ul>
 * <li>Service接口 ：用户账户相关接口，目前仅有list bucket功能</li>
 * <li>Bucket接口 ：和bucket相关接口，主要用于设置、删除和获取bucket属性</li>
 * <li>Object接口 ：和Object相关接口，主要用于上传下载文件以及设置和删除文件属性</li>
 * </ul><br>
 * 更多信息请参见<a
 * href="https://cloud.tencent.com/document/product/436/7751"> 腾讯云COS XML API文档</a>
 * @see SimpleCosXml
 * @see CosXmlService
 */

public interface CosXml extends SimpleCosXml {

    /**
     * <p>
     * 获取所属账户的所有存储空间列表的同步方法.&nbsp;
     * <p>
     * 通过使用帯 Authorization 签名认证的请求，可以获取签名中 APPID 所属账户的所有存储空间列表
     * (Bucket list).
     * <p>
     * 关于获取所有存储空间列表接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8291">https://cloud.tencent.com/document/product/436/8291.</a>
     * 
     * <p>
     * cos Android SDK 中获取所属账户的所有存储空间列表的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetServiceRequest} 构造方法，实例化 GetServiceRequest 对象;<br>
     * 2、通过调用 {@link #getService(GetServiceRequest)} 同步方法，传入 GetServiceRequest，返回 {@link GetServiceResult} 对象.<br>
     *
     *<p>
     * 示例：
     * <blockquote><pre>
     * GetServiceRequest request = new GetServiceRequest();
     * try {
     *     GetServiceResult result = cosXml.getService(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 获取所属账户的所有存储空间列表请求 {@link GetServiceRequest}
     * @return GetServiceResult 获取所属账户的所有存储空间列表请求返回的结果 {@link GetServiceResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    GetServiceResult getService(GetServiceRequest request) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 获取所属账户的所有存储空间列表的异步方法.&nbsp;
     * <p>
     * 通过使用帯 Authorization 签名认证的请求，可以获取签名中 APPID 所属账户的所有存储空间列表
     * (Bucket list).
     * <p>
     * 关于获取所有存储空间列表接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8291">https://cloud.tencent.com/document/product/436/8291.</a>
     *
     * <p>
     * cos Android SDK 中获取所属账户的所有存储空间列表的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetServiceRequest} 构造方法，实例化 GetServiceRequest 对象;<br>
     * 2、通过调用 {@link #getServiceAsync(GetServiceRequest, CosXmlResultListener)} 异步方法，传入 GetServiceRequest 和 CosXmlResultListener 进行异步回调操作.<br>
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * GetServiceRequest request = new GetServiceRequest();
     
     * cosXml.getServiceAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 获取所属账户的所有存储空间列表请求 {@link GetServiceRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getServiceAsync(GetServiceRequest request, final CosXmlResultListener cosXmlResultListener);


    //COS Object API

//    /**
//     * <p>
//     * 追加上传.&nbsp;
//     * 已被放弃支持.
//     * 
//     * @param request 追加上传请求 {@link AppendObjectRequest}
//     * @return AppendObjectResult 追加上传请求返回的结果 {@link AppendObjectResult}
//     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
//     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
//     */
//    @Deprecated
//    AppendObjectResult appendObject(AppendObjectRequest request) throws CosXmlClientException, CosXmlServiceException;
//
//    /**
//     * <p>
//     * 追加上传.&nbsp;
//     * 已被放弃支持.
//     * 
//     * @param request 追加上传请求 {@link AppendObjectRequest}
//     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
//     */
//    @Deprecated
//    void appendObjectAsync(AppendObjectRequest request, final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 批量删除 COS 对象的同步方法.&nbsp;
     * <p>
     * COS 支持批量删除指定 Bucket 中 对象，单次请求最大支持批量删除 1000 个 对象.
     * 请求中删除一个不存在的对象，仍然认为是成功的.
     * 对于响应结果，COS提供 Verbose 和 Quiet 两种模式：Verbose 模式将返回每个对象的删除结果;Quiet 模式只返回删除报错的对象信息.
     * 请求必须携带 Content-MD5 用来校验请求Body 的完整性.
     * <p>
     * 关于批量删除 COS 对象接口的描述，请查看<a href="https://cloud.tencent.com/document/product/436/8289">https://cloud.tencent.com/document/product/436/8289.</a>
     * 
     * <p>
     * cos Android SDK 中批量删除 COS 对象的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteMultiObjectRequest} 构造方法，实例化 DeleteMultiObjectRequest 对象;<br>
     * 2、通过调用 {@link #deleteMultiObject(DeleteMultiObjectRequest)} 同步方法，传入 DeleteMultiObjectRequest，返回 {@link DeleteMultiObjectResult} 对象.
     * 

     *
     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * List&lt;String&gt; listObject = new ArrayList&lt;&gt;();
     * listObject.add("/xml_test_copy.txt");
     * listObject.add("/1511858966419.txt");
     * DeleteMultiObjectRequest request = new DeleteMultiObjectRequest(bucket, null);
     
     * request.setQuiet(false);
     * request.setObjectList(listObject);
     * try {
     *     DeleteMultiObjectResult result = cosXml.deleteMultiObject(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 批量删除 COS 对象请求 {@link DeleteMultiObjectRequest}
     * @return DeleteMultiObjectResult 批量删除 COS 对象请求返回的结果 {@link DeleteMultiObjectResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    DeleteMultiObjectResult deleteMultiObject(DeleteMultiObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 批量删除 COS 对象的异步方法.&nbsp;
     * <p>
     * COS 支持批量删除指定 Bucket 中 对象，单次请求最大支持批量删除 1000 个 对象.
     * 请求中删除一个不存在的对象，仍然认为是成功的.
     * 对于响应结果，COS提供 Verbose 和 Quiet 两种模式：Verbose 模式将返回每个对象的删除结果;Quiet 模式只返回删除报错的对象信息.
     * 请求必须携带 Content-MD5 用来校验请求Body 的完整性.
     * <p>
     * 关于批量删除 COS 对象接口的描述，请查看<a href="https://cloud.tencent.com/document/product/436/8289">https://cloud.tencent.com/document/product/436/8289.</a>
     *
     * <p>
     * cos Android SDK 中批量删除 COS 对象的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteMultiObjectRequest} 构造方法，实例化 DeleteMultiObjectRequest 对象;<br>
     * 2、通过调用 {@link #deleteMultiObjectAsync(DeleteMultiObjectRequest, CosXmlResultListener)} 异步方法，传入 DeleteMultiObjectRequest 和 CosXmlResultListener 进行异步回调操作.
     *
     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * List&lt;String&gt;listObject = new ArrayList&lt;&gt;();
     * listObject.add("/xml_test_copy.txt");
     * listObject.add("/1511858966419.txt");
     * DeleteMultiObjectRequest request = new DeleteMultiObjectRequest(bucket, null);
     
     * request.setQuiet(false);
     * request.setObjectList(listObject);
     * cosXml.deleteMultiObjectAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 批量删除 COS 对象请求 {@link DeleteMultiObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteMultiObjectAsync(DeleteMultiObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取 COS 对象的访问权限信息（Access Control List, ACL）的同步方法.&nbsp;
     * <p>
     * Bucket 的持有者可获取该 Bucket 下的某个对象的 ACL 信息，如被授权者以及被授权的信息. ACL
     * 权限包括读、写、读写权限.
     * <p>
     * 关于获取 COS 对象的 ACL 接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7744">https://cloud.tencent.com/document/product/436/7744.</a>
     * 
     * <p>
     * cos Android SDK 中获取 COS 对象的 ACL 的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetObjectACLRequest} 构造方法，实例化 GetObjectACLRequest 对象;<br>
     * 2、通过调用 {@link #getObjectACL(GetObjectACLRequest)} 同步方法，传入 GetObjectACLRequest，返回 {@link GetObjectACLResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * GetObjectACLRequest request = new GetObjectACLRequest(bucket, cosPath);
     
     * try {
     *     GetObjectACLResult result = cosXml.getObjectACL(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 获取 COS 对象的 ACL 请求 {@link GetObjectACLRequest}
     * @return GetObjectACLResult 获取 COS 对象的 ACL 请求返回的结果 {@link GetObjectACLResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    GetObjectACLResult getObjectACL(GetObjectACLRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * 获取 COS 对象的访问权限信息（Access Control List, ACL）的异步方法.&nbsp;
     * <p>
     * Bucket 的持有者可获取该 Bucket 下的某个对象的 ACL 信息，如被授权者以及被授权的信息. ACL
     * 权限包括读、写、读写权限.
     * <p>
     * 关于获取 COS 对象的 ACL 接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7744">https://cloud.tencent.com/document/product/436/7744.</a>
     *
     * cos Android SDK 中获取 COS 对象的 ACL 的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetObjectACLRequest} 构造方法，实例化 GetObjectACLRequest 对象;<br>
     * 2、通过调用 {@link #getObjectACLAsync(GetObjectACLRequest, CosXmlResultListener)} 异步方法，传入 GetObjectACLRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * GetObjectACLRequest request = new GetObjectACLRequest(bucket, cosPath);
     
     * cosXml.getObjectACLAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 获取 COS 对象的 ACL 请求 {@link GetObjectACLRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getObjectACLAsync(GetObjectACLRequest request, final CosXmlResultListener cosXmlResultListener);

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
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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
     * 关于COS 对象的跨域访问配置预请求接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8288">https://cloud.tencent.com/document/product/436/8288.</a>
     * 
     * <p>
     * cos Android SDK 中发起COS 对象的跨域访问配置预请求的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link OptionObjectRequest} 构造方法，实例化 OptionObjectRequest 对象;<br>
     * 2、通过调用 {@link #optionObject(OptionObjectRequest)} 同步方法，传入 OptionObjectRequest，返回 {@link OptionObjectResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String origin = "cloud.tencent.com";
     * String method = "GET";
     * OptionObjectRequest request = new OptionObjectRequest(bucket, cosPath, origin, method);
     
     * try {
     *     OptionObjectResult result = cosXml.optionObject(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request COS 对象的跨域访问配置预请求 {@link OptionObjectRequest}
     * @return OptionObjectResult COS 对象的跨域访问配置预请求返回的结果 {@link OptionObjectResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于COS 对象的跨域访问配置预请求接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8288">https://cloud.tencent.com/document/product/436/8288.</a>
     *
     * <p>
     * cos Android SDK 中发起COS 对象的跨域访问配置预请求的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link OptionObjectRequest} 构造方法，实例化 OptionObjectRequest 对象;<br>
     * 2、通过调用 {@link #optionObjectAsync(OptionObjectRequest, CosXmlResultListener)} 异步方法，传入 OptionObjectRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String origin = "cloud.tencent.com";
     * String method = "GET";
     * OptionObjectRequest request = new OptionObjectRequest(bucket, cosPath, origin, method);
     
     * cosXml.optionObjectAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request COS 对象的跨域访问配置预请求 {@link OptionObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void optionObjectAsync(OptionObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 设置 COS 对象的访问权限信息（Access Control List, ACL）的同步方法.&nbsp;
     * <p>
     * ACL权限包括读、写、读写权限. COS 对象的 ACL 可以通过 header头部："x-cos-acl"，"x-cos-grant-read"，"x-cos-grant-write"，
     * "x-cos-grant-full-control" 传入 ACL 信息，或者通过 Body 以 XML 格式传入 ACL 信息.这两种方式只
     * 能选择其中一种，否则引起冲突.
     * 传入新的 ACL 将覆盖原有 ACL信息.ACL策略数上限1000，建议用户不要每个上传文件都设置 ACL.
     * <p>
     * 关于设置 COS 对象的ACL接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7748">https://cloud.tencent.com/document/product/436/7748.</a>
     * 
     * <p>
     * cos Android SDK 中设置 COS 对象的 ACL 的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutObjectACLRequest} 构造方法，实例化 PutObjectACLRequest 对象;<br>
     * 2、通过调用 {@link #putObjectACL(PutObjectACLRequest)} 同步方法，传入 PutObjectACLRequest，返回 {@link PutObjectACLResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * PutObjectACLRequest request = new PutObjectACLRequest(bucket, cosPath);
     * request.setXCOSACL(COSACL.PRIVATE);
     * ACLAccount aclAccount = new ACLAccount();
     * aclAccount.addAccount("1131975903", "1131975903");
     * request.setXCOSGrantRead(aclAccount);
     * request.setXCOSGrantWrite(aclAccount);
     
     * try {
     *     PutBucketACLResult result = cosXml.putObjectACL(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 设置 COS 对象的 ACL 请求 {@link PutObjectACLRequest}
     * @return PutObjectACLResult 设置 COS 对象的 ACL 请求返回的结果 {@link PutObjectACLResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于设置 COS 对象的ACL接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7748">https://cloud.tencent.com/document/product/436/7748.</a>
     *
     * <p>
     * cos Android SDK 中设置COS 对象的ACL的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutObjectACLRequest} 构造方法，实例化 PutObjectACLRequest 对象;<br>
     * 2、通过调用 {@link #putObjectACLAsync(PutObjectACLRequest, CosXmlResultListener)} 异步方法，传入 PutObjectACLRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * PutObjectACLRequest request = new PutObjectACLRequest(bucket, cosPath);
     * request.setXCOSACL(COSACL.PRIVATE);
     * ACLAccount aclAccount = new ACLAccount();
     * aclAccount.addAccount("1131975903", "1131975903");
     * request.setXCOSGrantRead(aclAccount);
     * request.setXCOSGrantWrite(aclAccount);
     
     * cosXml.putObjectACLAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 设置COS 对象的ACL请求 {@link PutObjectACLRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putObjectACLAsync(PutObjectACLRequest request, final CosXmlResultListener cosXmlResultListener);

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
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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
     * COS 归档(archive) 类型的对象恢复.&nbsp;
     * <p>
     * COS 中复制对象可以完成如下功能:
     * <ul>
     * <li>创建一个新的对象副本.</li>
     * <li>复制对象并更名，删除原始对象，实现重命名</li>
     * <li>修改对象的存储类型，在复制时选择相同的源和目标对象键，修改存储类型.</li>
     * <li>在不同的腾讯云 COS 地域复制对象.</li>
     * <li>修改对象的元数据，在复制时选择相同的源和目标对象键，并修改其中的元数据,复制对象时，默认将继承原对象的元数据，但创建日期将会按新对象的时间计算.</li>
     * </ul>
     * <p>
     * 当复制的对象小于等于 5 GB ，可以使用简单复制（<a href="https://cloud.tencent.com/document/product/436/14117">https://cloud.tencent.com/document/product/436/14117</a>).<br>
     * 当复制对象超过 5 GB 时，必须使用分块复制（<a href="https://cloud.tencent.com/document/product/436/14118">https://cloud.tencent.com/document/product/436/14118 </a>）
     * 来实现复制.<br>
     * 关于分块复制接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8287">https://cloud.tencent.com/document/product/436/8287.</a>
     *
     * <p>
     * cos Android SDK 中分块复制的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link UploadPartCopyRequest} 构造方法，实例化 UploadPartCopyRequest 对象;<br>
     * 2、通过调用 {@link #copyObject(UploadPartCopyRequest)} 同步方法，传入 UploadPartCopyRequest，返回 {@link UploadPartCopyResult} 对象.
     *

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String srcPath = "srcPath"; //本地文件的绝对路径
     * PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
     * putObjectRequest.setSign(signDuration,null,null); //签名
     * putObjectRequest.setProgressListener(new CosXmlProgressListener() {// 进度回调
     *    &nbsp;@Override
     *     public void onProgress(long progress, long max) {
     *         float result = (float) (progress * 100.0/max);
     *         Log.w("TEST","progress =" + (long)result + "%");
     *    }
     * });
     * String eTag = null; //上传返回的文件 md5
     * String accessUrl = null; //访问文件的地址
     * try {
     *     UploadPartCopyResult result = cosXml.copyObject(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     *
     * @param request 分块复制请求 {@link UploadPartCopyRequest}
     * @return UploadPartCopyResult 分块复制请求返回的结果 {@link UploadPartCopyResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    RestoreResult restoreObject(RestoreRequest request) throws CosXmlClientException, CosXmlServiceException;

    void restoreObjectAsync(RestoreRequest request,final CosXmlResultListener cosXmlResultListener);

    //COS Bucket API

    /**
     * <p>
     * 删除跨域访问配置信息的同步方法.&nbsp;
     * <p>
     * 若是 Bucket 不需要支持跨域访问配置，可以调用此接口删除已配置的跨域访问信息.
     * 跨域访问配置可以通过 {@link #putBucketCORS(PutBucketCORSRequest)} 或者 {@link
     * #putBucketCORSAsync(PutBucketCORSRequest, CosXmlResultListener)} 方法来开启 Bucket 的跨域访问
     * 支持.
     * <p>
     * 关于删除跨域访问配置信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8283">https://cloud.tencent.com/document/product/436/8283.</a>
     * 
     * <p>
     * cos Android SDK 中删除跨域访问配置信息的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteBucketCORSRequest} 构造方法，实例化 DeleteBucketCORSRequest 对象;<br>
     * 2、通过调用 {@link #deleteBucketCORS(DeleteBucketCORSRequest)} 同步方法，传入 DeleteBucketCORSRequest，返回 {@link DeleteBucketCORSResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * DeleteBucketCORSRequest request = new DeleteBucketCORSRequest(bucket);
     
     * try {
     *     DeleteBucketCORSResult result = cosXml.deleteBucketCORS(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 删除跨域访问配置信息请求 {@link DeleteBucketCORSRequest}
     * @return DeleteBucketCORSResult 删除跨域访问配置信息请求返回的结果 {@link DeleteBucketCORSResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于删除跨域访问配置信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8283">https://cloud.tencent.com/document/product/436/8283.</a>
     *
     *
     * <p>
     * cos Android SDK 中删除跨域访问配置信息的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteBucketCORSRequest} 构造方法，实例化 DeleteBucketCORSRequest 对象;<br>
     * 2、通过调用 {@link #deleteBucketCORSAsync(DeleteBucketCORSRequest, CosXmlResultListener)} 异步方法，传入 DeleteBucketCORSRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * DeleteBucketCORSRequest request = new DeleteBucketCORSRequest(bucket);
     
     * cosXml.deleteBucketCORSAsync(request,new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 删除跨域访问配置信息请求 {@link DeleteBucketCORSRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketCORSAsync(DeleteBucketCORSRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 删除存储桶（Bucket） 的生命周期配置的同步方法.&nbsp;
     * <p>
     * COS 支持删除已配置的 Bucket 的生命周期列表.
     * COS 支持以生命周期配置的方式来管理 Bucket 中 对象的生命周期，生命周期配置包含一个或多个将
     * 应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)，请参阅 {@link #putBucketLifecycle(PutBucketLifecycleRequest)}.
     * <p>
     * 关于删除 Bucket 的生命周期配置接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8284">https://cloud.tencent.com/document/product/436/8284.</a>
     * 
     * <p>
     * cos Android SDK 中删除 Bucket 的生命周期配置的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteBucketLifecycleRequest} 构造方法，实例化 DeleteBucketLifecycleRequest 对象;<br>
     * 2、通过调用 {@link #deleteBucketLifecycle(DeleteBucketLifecycleRequest)} 同步方法，传入 DeleteBucketLifecycleRequest，返回 {@link DeleteBucketLifecycleResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * DeleteBucketLifecycleRequest request = new DeleteBucketLifecycleRequest(bucket);
     
     * try {
     *     DeleteBucketLifecycleResult result = cosXml.deleteBucketLifecycle(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 删除 Bucket 的生命周期配置请求 {@link GetServiceRequest}
     * @return DeleteBucketLifecycleResult 删除 Bucket 的生命周期配置请求返回的结果 {@link DeleteBucketLifecycleResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于删除 Bucket 的生命周期配置接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8284">https://cloud.tencent.com/document/product/436/8284.</a>
     *
     * <p>
     * cos Android SDK 中删除 Bucket 的生命周期配置的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteBucketLifecycleRequest} 构造方法，实例化 DeleteBucketLifecycleRequest 对象;<br>
     * 2、通过调用 {@link #deleteBucketLifecycleAsync(DeleteBucketLifecycleRequest, CosXmlResultListener)} 异步方法，传入 DeleteBucketLifecycleRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     * <p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * DeleteBucketLifecycleRequest request = new DeleteBucketLifecycleRequest(bucket);
     
     * cosXml.deleteBucketLifecycleAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 删除 Bucket 的生命周期配置请求 {@link DeleteBucketLifecycleRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketLifecycleAsync(DeleteBucketLifecycleRequest request,CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 删除存储桶 (Bucket)的同步方法.&nbsp;
     * <p>
     * COS 目前仅支持删除已经清空的 Bucket，如果 Bucket 中仍有对象，将会删除失败. 因此，在执行删除 Bucket
     * 前，需确保 Bucket 内已经没有对象. 删除 Bucket 时，还需要确保操作的身份已被授权该操作，并确认
     * 传入了正确的存储桶名称和地域参数, 请参阅 {@link #putBucket(PutBucketRequest)}.
     * <p>
     * 关于删除 Bucket 的描述,请查看 <a href="https://cloud.tencent.com/document/product/436/14105">https://cloud.tencent.com/document/product/436/14105.</a><br>
     * 关于删除 Bucket 接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7732">https://cloud.tencent.com/document/product/436/7732.</a>
     * 
     * <p>
     * cos Android SDK 中删除 Bucket 的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteBucketRequest} 构造方法，实例化 DeleteBucketRequest 对象;<br>
     * 2、通过调用 {@link #deleteBucket(DeleteBucketRequest)} 同步方法，传入 GetServiceRequest，返回 {@link DeleteBucketResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * DeleteBucketRequest request = new DeleteBucketRequest(bucket);
     
     * try {
     *     DeleteBucketResult result = cosXml.deleteBucket(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 删除 Bucket 请求 {@link DeleteBucketRequest}
     * @return DeleteBucketResult 删除 Bucket 请求返回的结果 {@link DeleteBucketResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于删除 Bucket 的描述,请查看 <a href="https://cloud.tencent.com/document/product/436/14105">https://cloud.tencent.com/document/product/436/14105.</a><br>
     * 关于删除 Bucket 接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7732">https://cloud.tencent.com/document/product/436/7732.</a>
     *
     * <p>
     * cos Android SDK 中删除 Bucket 的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteBucketRequest} 构造方法，实例化 DeleteBucketRequest 对象;<br>
     * 2、通过调用 {@link #deleteBucketAsync(DeleteBucketRequest, CosXmlResultListener)} 异步方法，传入 DeleteBucketRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * DeleteBucketRequest request = new DeleteBucketRequest(bucket);
     
     * cosXml.deleteBucketAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 删除 Bucket 请求 {@link GetServiceRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketAsync(DeleteBucketRequest request, CosXmlResultListener cosXmlResultListener);
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
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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
     * 关于获取 Bucket 的 ACL 接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7733"> https://cloud.tencent.com/document/product/436/7733.</a>
     * 
     * <p>
     * cos Android SDK 中获取 Bucket 的 ACL 的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketACLRequest} 构造方法，实例化 GetBucketACLRequest 对象;<br>
     * 2、通过调用 {@link #getBucketACL(GetBucketACLRequest)} 同步方法，传入 GetBucketACLRequest，返回 {@link GetBucketACLResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketACLRequest request = new GetBucketACLRequest(bucket);
     
     * try {
     *     GetBucketACLResult result = cosXml.getBucketACL(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 获取 Bucket 的 ACL 请求 {@link GetBucketACLRequest}
     * @return GetBucketACLResult 获取 Bucket 的 ACL 请求返回的结果 {@link GetBucketACLResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    GetBucketACLResult getBucketACL(GetBucketACLRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶（Bucket) 的访问权限信息（Access Control List, ACL）的异步方法.&nbsp;
     * <p>
     * ACL 权限包括读、写、读写权限.COS 中 Bucket 是有访问权限控制的.可以通过获取 Bucket 的 ACL 表({@link #putBucketACL(PutBucketACLRequest)})，来查看那些用户拥有 Bucket 访
     * 问权限.
     * <p>
     * 关于获取 Bucket 的 ACL 接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7733"> https://cloud.tencent.com/document/product/436/7733.</a>
     *
     * <p>
     * cos Android SDK 中获取 Bucket 的 ACL 的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketACLRequest} 构造方法，实例化 GetBucketACLRequest 对象;<br>
     * 2、通过调用 {@link #getBucketACLAsync(GetBucketACLRequest, CosXmlResultListener)} 异步方法，传入 GetBucketACLRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketACLRequest request = new GetBucketACLRequest(bucket);
     
     * cosXml.getBucketACLAsync(request,  new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 获取所有存储空间列表请求 {@link GetBucketACLRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketACLAsync(GetBucketACLRequest request, CosXmlResultListener cosXmlResultListener);

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
     * 关于查询 Bucket 跨域访问配置信息接口的具体描述，
     * 请查看 <a href="https://cloud.tencent.com/document/product/436/8274">https://cloud.tencent.com/document/product/436/8274.</a>
     * 
     * <p>
     * cos Android SDK 中查询 Bucket 跨域访问配置信息的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketCORSRequest} 构造方法，实例化 GetBucketCORSRequest 对象;<br>
     * 2、通过调用 {@link #getBucketCORS(GetBucketCORSRequest)} 同步方法，传入 GetBucketCORSRequest，返回 {@link GetBucketCORSResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketCORSRequest request = new GetBucketCORSRequest(bucket);
     
     * try {
     *     GetBucketCORSResult result = cosXml.getBucketCORS(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 查询 Bucket 跨域访问配置信息请求 {@link GetBucketCORSRequest}
     * @return GetBucketCORSResult 查询 Bucket 跨域访问配置信息请求返回的结果 {@link GetBucketCORSResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于查询 Bucket 跨域访问配置信息接口的具体描述，
     * 请查看 <a href="https://cloud.tencent.com/document/product/436/8274">https://cloud.tencent.com/document/product/436/8274.</a>
     *
     * <p>
     * cos Android SDK 中查询 Bucket 跨域访问配置信息的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketCORSRequest} 构造方法，实例化 GetBucketCORSRequest 对象;<br>
     * 2、通过调用 {@link #getBucketCORSAsync(GetBucketCORSRequest, CosXmlResultListener)} 异步方法，传入 GetBucketCORSRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketCORSRequest request = new GetBucketCORSRequest(bucket);
     
     * cosXml.getBucketCORSAsync(request,  new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 查询 Bucket 跨域访问配置信息请求 {@link GetBucketCORSRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketCORSAsync(GetBucketCORSRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 查询存储桶（Bucket) 的生命周期配置的同步方法.&nbsp;
     * <p>
     * COS 支持以生命周期配置的方式来管理 Bucket 中对象的生命周期，生命周期配置包含一个或多个将
     * 应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)，请参阅 {@link #putBucketLifecycle(PutBucketLifecycleRequest)}.
     * <p>
     * 关于查询 Bucket 的生命周期配置接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8278">https://cloud.tencent.com/document/product/436/8278.</a>
     * 
     * <p>
     * cos Android SDK 中查询 Bucket 的生命周期配置的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketLifecycleRequest} 构造方法，实例化 GetBucketLifecycleRequest 对象;<br>
     * 2、通过调用 {@link #getBucketLifecycle(GetBucketLifecycleRequest)} 同步方法，传入 GetServiceRequest，返回 {@link GetBucketLifecycleResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketLifecycleRequest request = new GetBucketLifecycleRequest(bucket);
     
     * try {
     *     GetBucketLifecycleResult result = cosXml.getBucketLifecycle(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 查询 Bucket 的生命周期配置请求 {@link GetBucketLifecycleRequest}
     * @return GetBucketLifecycleResult 查询 Bucket 的生命周期配置请求返回的结果 {@link GetBucketLifecycleResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    GetBucketLifecycleResult getBucketLifecycle(GetBucketLifecycleRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询存储桶（Bucket) 的生命周期配置的异步方法.&nbsp;
     * <p>
     * COS 支持以生命周期配置的方式来管理 Bucket 中对象的生命周期，生命周期配置包含一个或多个将
     * 应用于一组对象规则的规则集 (其中每个规则为 COS 定义一个操作)，请参阅 {@link #putBucketLifecycle(PutBucketLifecycleRequest)}.
     * <p>
     * 关于查询 Bucket 的生命周期配置接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8278">https://cloud.tencent.com/document/product/436/8278.</a>
     *
     * <p>
     * cos Android SDK 中查询 Bucket 的生命周期配置的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketLifecycleRequest} 构造方法，实例化 GetBucketLifecycleRequest 对象;<br>
     * 2、通过调用 {@link #getBucketLifecycleAsync(GetBucketLifecycleRequest, CosXmlResultListener)} 异步方法，传入 GetBucketLifecycleRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketLifecycleRequest request = new GetBucketLifecycleRequest(bucket);
     
     * cosXml.getBucketLifecycleAsync(request,  new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 查询 Bucket 的生命周期配置请求 {@link GetBucketLifecycleRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketLifecycleAsync(GetBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取存储桶（Bucket) 所在的地域信息的同步方法.&nbsp;
     * <p>
     * 在创建 Bucket 时，需要指定所属该 Bucket 所属地域信息.
     * <p>
     * COS 支持的地域信息，可查看<a href="https://cloud.tencent.com/document/product/436/6224">https://cloud.tencent.com/document/product/436/6224.</a><br>
     * 关于获取 Bucket 所在的地域信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8275">https://cloud.tencent.com/document/product/436/8275.</a>
     * 
     * <p>
     * cos Android SDK 中获取 Bucket 所在的地域信息的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketLocationRequest} 构造方法，实例化 GetBucketLocationRequest 对象;<br>
     * 2、通过调用 {@link #getBucketLocation(GetBucketLocationRequest)} 同步方法，传入 GetBucketLocationRequest，返回 {@link GetBucketLocationResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketLocationRequest request = new GetBucketLocationRequest(bucket);
     
     * try {
     *     GetBucketLocationResult result = cosXml.getBucketLocation(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 获取所有存储空间列表请求 {@link GetBucketLocationRequest}
     * @return GetBucketLocationResult 获取所有存储空间列表请求返回的结果 {@link GetBucketLocationResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    GetBucketLocationResult getBucketLocation(GetBucketLocationRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶（Bucket) 所在的地域信息的异步方法.&nbsp;
     * <p>
     * 在创建 Bucket 时，需要指定所属该 Bucket 所属地域信息.
     * <p>
     * COS 支持的地域信息，可查看<a href="https://cloud.tencent.com/document/product/436/6224">https://cloud.tencent.com/document/product/436/6224.</a><br>
     * 关于获取 Bucket 所在的地域信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8275">https://cloud.tencent.com/document/product/436/8275.</a>
     *
     * <p>
     * cos Android SDK 中获取 Bucket 所在的地域信息的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketLocationRequest} 构造方法，实例化 GetBucketLocationRequest 对象;<br>
     * 2、通过调用 {@link #getBucketLocationAsync(GetBucketLocationRequest, CosXmlResultListener)} 异步方法，传入 GetBucketLocationRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketLocationRequest request = new GetBucketLocationRequest(bucket);
     
     * cosXml.getBucketLocationAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 获取 Bucket 所在的地域信息请求 {@link GetBucketLocationRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketLocationAsync(GetBucketLocationRequest request, CosXmlResultListener cosXmlResultListener);

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
     * 关于查询Bucket 下的部分或者全部对象接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7734">https://cloud.tencent.com/document/product/436/7734.</a>
     * 
     * <p>
     * cos Android SDK 中查询 Bucket 下的部分或者全部对象的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketRequest} 构造方法，实例化 GetBucketRequest 对象;<br>
     * 2、通过调用 {@link #getBucket(GetBucketRequest)} 同步方法，传入 GetBucketRequest，返回 {@link GetBucketResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketRequest request = new GetBucketRequest(bucket);
     
     * try {
     *     GetBucketResult result = cosXml.getBucket(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 查询 Bucket 下的部分或者全部对象请求 {@link GetBucketRequest}
     * @return GetBucketResult 查询 Bucket 下的部分或者全部对象请求返回的结果 {@link GetBucketResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于查询Bucket 下的部分或者全部对象接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7734">https://cloud.tencent.com/document/product/436/7734.</a>
     *
     * <p>
     * cos Android SDK 中查询 Bucket 下的部分或者全部对象的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketRequest} 构造方法，实例化 GetBucketRequest 对象;<br>
     * 2、通过调用 {@link #getBucketAsync(GetBucketRequest, CosXmlResultListener)} 异步方法，传入 GetBucketRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketRequest request = new GetBucketRequest(bucket);
     
     * cosXml.getBucketAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 查询 Bucket 下的部分或者全部对象请求 {@link GetBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketAsync(GetBucketRequest request, CosXmlResultListener cosXmlResultListener);

//    /**
//     * 获取存储桶（Bucket）的标签的同步方法.&nbsp;
//
//     *<p>
//     * 示例：
//     * <blockquote><pre>
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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
//     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
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


    /**
     * <p>
     * 存储桶（Bucket） 是否存在的同步方法.&nbsp;
     * <p>
     * 在开始使用 COS 时，需要确认该 Bucket 是否存在，是否有权限访问.若不存在，则可以调用{@link #putBucket(PutBucketRequest)}
     * 创建.
     * <p>
     * 关于确认该 Bucket 是否存在，是否有权限访问接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7735">https://cloud.tencent.com/document/product/436/7735.</a>
     * 
     * <p>
     * cos Android SDK 中Bucket 是否存在的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link HeadBucketRequest} 构造方法，实例化 HeadBucketRequest 对象;<br>
     * 2、通过调用 {@link #headBucket(HeadBucketRequest)} 同步方法，传入 HeadBucketRequest，返回 {@link HeadBucketResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * HeadBucketRequest request = new HeadBucketRequest(bucket);
     
     * try {
     *     HeadBucketResult result = cosXml.headBucket(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request Bucket 是否存在请求 {@link HeadBucketRequest}
     * @return HeadBucketResult Bucket 是否存在请求返回的结果 {@link HeadBucketResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    HeadBucketResult headBucket(HeadBucketRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 存储桶（Bucket） 是否存在的异步方法.&nbsp;
     * <p>
     * 在开始使用 COS 时，需要确认该 Bucket 是否存在，是否有权限访问.若不存在，则可以调用{@link #putBucket(PutBucketRequest)}
     * 创建.
     * <p>
     * 关于确认该 Bucket 是否存在，是否有权限访问接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7735">https://cloud.tencent.com/document/product/436/7735.</a>
     *
     * <p>
     * cos Android SDK 中 Bucket 是否存在的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link HeadBucketRequest} 构造方法，实例化 HeadBucketRequest 对象;<br>
     * 2、通过调用 {@link #headBucketAsync(HeadBucketRequest, CosXmlResultListener)} 异步方法，传入 HeadBucketRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * HeadBucketRequest request = new HeadBucketRequest(bucket);
     
     * cosXml.headBucketAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request Bucket 是否存在请求 {@link HeadBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void headBucketAsync(HeadBucketRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 查询存储桶（Bucket）中正在进行中的分块上传对象的同步方法.&nbsp;
     * <p>
     * COS 支持查询 Bucket 中有哪些正在进行中的分块上传对象，单次请求操作最多列出 1000 个正在进行中的
     * 分块上传对象.
     * <p>
     * 关于查询 Bucket 中正在进行中的分块上传对象接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7736">
     * https://cloud.tencent.com/document/product/436/7736.</a>
     * 
     * <p>
     * cos Android SDK 中查询 Bucket 中正在进行中的分块上传对象的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link ListMultiUploadsRequest} 构造方法，实例化 ListMultiUploadsRequest 对象;<br>
     * 2、通过调用 {@link #listMultiUploads(ListMultiUploadsRequest)} 同步方法，传入 ListMultiUploadsRequest，返回 {@link ListMultiUploadsResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * ListMultiUploadsRequest request = new ListMultiUploadsRequest(bucket);
     
     * try {
     *     ListMultiUploadsResult result = cosXml.listMultiUploads(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 查询 Bucket 中正在进行中的分块上传对象请求 {@link ListMultiUploadsRequest}
     * @return ListMultiUploadsResult 查询 Bucket 中正在进行中的分块上传对象请求返回的结果 {@link ListMultiUploadsResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    ListMultiUploadsResult listMultiUploads(ListMultiUploadsRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询存储桶（Bucket）中正在进行中的分块上传对象的异步方法.&nbsp;
     * <p>
     * COS 支持查询 Bucket 中有哪些正在进行中的分块上传对象，单次请求操作最多列出 1000 个正在进行中的
     * 分块上传对象.
     * <p>
     * 关于查询 Bucket 中正在进行中的分块上传对象接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7736">
     * https://cloud.tencent.com/document/product/436/7736.</a>
     *
     * <p>
     * cos Android SDK 中查询 Bucket 中正在进行中的分块上传对象的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link ListMultiUploadsRequest} 构造方法，实例化 ListMultiUploadsRequest 对象;<br>
     * 2、通过调用 {@link #listMultiUploadsAsync(ListMultiUploadsRequest, CosXmlResultListener)} 异步方法，传入 ListMultiUploadsRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * ListMultiUploadsRequest request = new ListMultiUploadsRequest(bucket);
     
     * cosXml.listMultiUploadsAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 查询 Bucket 中正在进行中的分块上传对象请求 {@link ListMultiUploadsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void listMultiUploadsAsync(ListMultiUploadsRequest request, CosXmlResultListener cosXmlResultListener);

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
     * 关于设置 Bucket 的ACL接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7737">
     * https://cloud.tencent.com/document/product/436/7737.</a>
     *
     * <p>
     * cos Android SDK 中设置 Bucket 的ACL的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketACLRequest} 构造方法，实例化 PutBucketACLRequest 对象;<br>
     * 2、通过调用 {@link #putBucketACL(PutBucketACLRequest)} 同步方法，传入 PutBucketACLRequest，返回 {@link PutBucketACLResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketACLRequest request = new PutBucketACLRequest(bucket);
     * request.setXCOSACL(COSACL.PRIVATE);
     * ACLAccount aclAccount = new ACLAccount();
     * aclAccount.addAccount("1131975903", "1131975903");
     * request.setXCOSGrantRead(aclAccount);
     * request.setXCOSGrantWrite(aclAccount);
     
     * try {
     *     PutBucketACLResult result = cosXml.putBucketACL(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 设置 Bucket 的ACL请求 {@link PutBucketACLRequest}
     * @return PutBucketACLResult 设置 Bucket 的ACL请求返回的结果 {@link PutBucketACLResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于设置 Bucket 的ACL接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7737">
     * https://cloud.tencent.com/document/product/436/7737.</a>
     *
     * <p>
     * cos Android SDK 中设置 Bucket 的ACL的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketACLRequest} 构造方法，实例化 PutBucketACLRequest 对象;<br>
     * 2、通过调用 {@link #putBucketACLAsync(PutBucketACLRequest, CosXmlResultListener)} 异步方法，传入 PutBucketACLRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketACLRequest request = new PutBucketACLRequest(bucket);
     * request.setXCOSACL(COSACL.PRIVATE);
     * ACLAccount aclAccount = new ACLAccount();
     * aclAccount.addAccount("1131975903", "1131975903");
     * request.setXCOSGrantRead(aclAccount);
     * request.setXCOSGrantWrite(aclAccount);
     
     * cosXml.putBucketACLAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 设置 Bucket 的ACL请求 {@link PutBucketACLRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketACLAsync(PutBucketACLRequest request, CosXmlResultListener cosXmlResultListener);

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
     * 关于设置 Bucket 的跨域配置信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8279">
     * https://cloud.tencent.com/document/product/436/8279</a>.
     * 
     * <p>
     * cos Android SDK 中设置 Bucket 的跨域配置信息的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketCORSRequest} 构造方法，实例化 PutBucketCORSRequest 对象;<br>
     * 2、通过调用 {@link #putBucketCORS(PutBucketCORSRequest)} 同步方法，传入 PutBucketCORSRequest，返回 {@link PutBucketCORSResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketCORSRequest request = new PutBucketCORSRequest(bucket);
     * CORSConfiguration.CORSRule corsRule = new CORSConfiguration.CORSRule();
     * corsRule.allowedOrigin = "http://cloud.tencent.com";
     * corsRule.allowedHeader = new ArrayList&lt;&gt;();
     * corsRule.allowedHeader.add("Host");
     * corsRule.allowedHeader.add("Authorization");
     * corsRule.allowedMethod = new ArrayList&lt;&gt;();
     * corsRule.allowedMethod.add("PUT");
     * corsRule.allowedMethod.add("GET");
     * corsRule.exposeHeader = new ArrayList&lt;&gt;();
     * corsRule.exposeHeader.add("x-cos-meta");
     * corsRule.exposeHeader.add("x-cos-meta-2");
     * corsRule.id = "CORSID";
     * corsRule.maxAgeSeconds = 5000;
     * request.addCORSRule(corsRule);
     
     * try {
     *     PutBucketCORSResult result = cosXml.putObject(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 设置 Bucket 的跨域配置信息请求 {@link PutBucketCORSRequest}
     * @return PutBucketCORSResult 设置 Bucket 的跨域配置信息请求返回的结果 {@link PutBucketCORSResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于设置 Bucket 的跨域配置信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8279">
     * https://cloud.tencent.com/document/product/436/8279</a>.
     *
     * <p>
     * cos Android SDK 中设置 Bucket 的跨域配置信息的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketCORSRequest} 构造方法，实例化 PutBucketCORSRequest 对象;<br>
     * 2、通过调用 {@link #putBucketCORSAsync(PutBucketCORSRequest, CosXmlResultListener)} 异步方法，传入 PutBucketCORSRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketCORSRequest request = new PutBucketCORSRequest(bucket);
     * CORSConfiguration.CORSRule corsRule = new CORSConfiguration.CORSRule();
     * corsRule.allowedOrigin = "http://cloud.tencent.com";
     * corsRule.allowedHeader = new ArrayList&lt;&gt;();
     * corsRule.allowedHeader.add("Host");
     * corsRule.allowedHeader.add("Authorization");
     * corsRule.allowedMethod = new ArrayList&lt;&gt;();
     * corsRule.allowedMethod.add("PUT");
     * corsRule.allowedMethod.add("GET");
     * corsRule.exposeHeader = new ArrayList&lt;&gt;();
     * corsRule.exposeHeader.add("x-cos-meta");
     * corsRule.exposeHeader.add("x-cos-meta-2");
     * corsRule.id = "CORSID";
     * corsRule.maxAgeSeconds = 5000;
     * request.addCORSRule(corsRule);
     
     * cosXml.putBucketCORSAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 设置 Bucket 的跨域配置信息请求 {@link PutBucketCORSRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketCORSAsync(PutBucketCORSRequest request, CosXmlResultListener cosXmlResultListener);

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
     * 关于Bucket 生命周期配置接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8280">
     * https://cloud.tencent.com/document/product/436/8280</a>
     * 
     * <p>
     * cos Android SDK 中Bucket 生命周期配置的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketLifecycleRequest} 构造方法，实例化 PutBucketLifecycleRequest 对象;<br>
     * 2、通过调用 {@link #putBucketLifecycle(PutBucketLifecycleRequest)} 同步方法，传入 GetServiceRequest，返回 {@link PutBucketLifecycleResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketLifecycleRequest request = new PutBucketLifecycleRequest(bucket);
     * LifecycleConfiguration.Rule rule = new LifecycleConfiguration.Rule();
     * rule.id = "LifeID";
     * rule.status = "Enabled";
     * rule.filter = new LifecycleConfiguration.Filter();
     * rule.filter.prefix = "aws";
     * rule.expiration = new LifecycleConfiguration.Expiration();
     * rule.expiration.days = 1;
     * request.setRuleList(rule);
     
     * try {
     *     PutBucketLifecycleResult result = cosXml.putObject(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request Bucket 生命周期配置请求 {@link GetServiceRequest}
     * @return PutBucketLifecycleResult Bucket 生命周期配置请求返回的结果 {@link PutBucketLifecycleResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于Bucket 生命周期配置接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/8280">
     * https://cloud.tencent.com/document/product/436/8280</a>
     *
     * <p>
     * cos Android SDK 中Bucket 生命周期配置的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketLifecycleRequest} 构造方法，实例化 PutBucketLifecycleRequest 对象;<br>
     * 2、通过调用 {@link #putBucketLifecycleAsync(PutBucketLifecycleRequest, CosXmlResultListener)} 异步方法，传入 GetServiceRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketLifecycleRequest request = new PutBucketLifecycleRequest(bucket);
     * LifecycleConfiguration.Rule rule = new LifecycleConfiguration.Rule();
     * rule.id = "LifeID";
     * rule.status = "Enabled";
     * rule.filter = new LifecycleConfiguration.Filter();
     * rule.filter.prefix = "aws";
     * rule.expiration = new LifecycleConfiguration.Expiration();
     * rule.expiration.days = 1;
     * request.setRuleList(rule);
     
     * cosXml.putBucketLifecycleAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 生命周期配置请求 {@link PutBucketLifecycleRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketLifecycleAsync(PutBucketLifecycleRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 创建存储桶（Bucket）的同步方法.&nbsp;
     * <p>
     * 在开始使用 COS 时，需要在指定的账号下先创建一个 Bucket 以便于对象的使用和管理. 并指定 Bucket
     * 所属的地域.创建 Bucket 的用户默认成为 Bucket 的持有者.若创建 Bucket 时没有指定访问权限，则默认
     * 为私有读写（private）权限.
     * <p>
     * 可用地域，可以查看<a href="https://cloud.tencent.com/document/product/436/6224">https://cloud.tencent.com/document/product/436/6224.</a><br>
     * 关于创建 Bucket 描述，请查看<a href="https://cloud.tencent.com/document/product/436/14106">
     *  https://cloud.tencent.com/document/product/436/14106</a>.<br>
     * 关于创建存储桶（Bucket）接口的具体 描述，请查看<a href="https://cloud.tencent.com/document/product/436/7738">
     *  https://cloud.tencent.com/document/product/436/7738.</a>
     * <p>
     * cos Android SDK 中创建 Bucket的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketRequest} 构造方法，实例化 PutBucketRequest 对象;<br>
     * 2、通过调用 {@link #putBucket(PutBucketRequest)} 同步方法，传入 PutBucketRequest，返回 {@link PutBucketResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketRequest request = new PutBucketRequest(bucket);
     * request.setXCOSACL(COSACL.PUBLIC_READ_WRITE);
     * ACLAccount aclAccount = new ACLAccount();
     * aclAccount.addAccount("2832742109", "2832742109");
     * request.setXCOSGrantRead(aclAccount);
     
     * try {
     *     PutBucketResult result = cosXml.putObject(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 创建 Bucket请求 {@link PutBucketRequest}
     * @return PutBucketResult 创建 Bucket 请求返回的结果 {@link PutBucketResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
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
     * 关于创建 Bucket 描述，请查看<a href="https://cloud.tencent.com/document/product/436/14106">
     *  https://cloud.tencent.com/document/product/436/14106</a>.<br>
     * 关于创建存储桶（Bucket）接口的具体 描述，请查看<a href="https://cloud.tencent.com/document/product/436/7738">
     *  https://cloud.tencent.com/document/product/436/7738.</a>
     * <p>
     * cos Android SDK 中创建 Bucket的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketRequest} 构造方法，实例化 PutBucketRequest 对象;<br>
     * 2、通过调用 {@link #putBucketAsync(PutBucketRequest, CosXmlResultListener)} 异步方法，传入 PutBucketRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketRequest request = new PutBucketRequest(bucket);
     * request.setXCOSACL(COSACL.PUBLIC_READ_WRITE);
     * ACLAccount aclAccount = new ACLAccount();
     * aclAccount.addAccount("2832742109", "2832742109");
     * request.setXCOSGrantRead(aclAccount);
     
     * cosXml.putBucketAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 创建 Bucket 请求 {@link PutBucketRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketAsync(PutBucketRequest request, CosXmlResultListener cosXmlResultListener);

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
     *
     * <p>
     * cos Android SDK 中获取 Bucket 版本控制信息的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketVersioningRequest} 构造方法，实例化 GetBucketVersioningRequest 对象;<br>
     * 2、通过调用 {@link #getBucketVersioning(GetBucketVersioningRequest)} 同步方法，传入 GetBucketVersioningRequest，返回 {@link GetBucketVersioningResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketVersioningRequest request = new GetBucketVersioningRequest(bucket);
     
     * try {
     *     GetBucketVersioningResult result = cosXml.getBucketVersioning(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 获取 Bucket 版本控制信息请求 {@link GetBucketVersioningRequest}
     * @return GetBucketVersioningResult 获取 Bucket 版本控制信息请求返回的结果 {@link GetBucketVersioningResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    GetBucketVersioningResult getBucketVersioning(GetBucketVersioningRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶（Bucket）版本控制信息的异步方法.&nbsp;
     * <p>
     * 通过查询版本控制信息，可以得知该 Bucket 的版本控制功能是处于禁用状态还是启用状态（Enabled 或者 Suspended）,
     * 开启版本控制功能，可参考{@link #putBucketVersioning(PutBucketVersioningRequest)}.
     *
     * <p>
     * cos Android SDK 中获取 Bucket 版本控制信息的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketVersioningRequest} 构造方法，实例化 GetBucketVersioningRequest 对象;<br>
     * 2、通过调用 {@link #getBucketVersioningAsync(GetBucketVersioningRequest, CosXmlResultListener)} 异步方法，传入 GetBucketVersioningRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketVersioningRequest request = new GetBucketVersioningRequest(bucket);
     
     * cosXml.getBucketVersioningAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 获取 Bucket 版本控制信息请求 {@link GetBucketVersioningRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketVersioningAsync(GetBucketVersioningRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 存储桶（Bucket）版本控制的同步方法.&nbsp;
     * <p>
     * 版本管理功能一经打开，只能暂停，不能关闭.
     * 通过版本控制，可以在一个 Bucket 中保留一个对象的多个版本.
     * 版本控制可以防止意外覆盖和删除对象，以便检索早期版本的对象.
     * 默认情况下，版本控制功能处于禁用状态，需要主动去启用或者暂停（Enabled 或者 Suspended）.
     *
     * <p>
     * cos Android SDK 中 Bucket 版本控制启用或者暂停的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketVersioningRequest} 构造方法，实例化 PutBucketVersioningRequest 对象;<br>
     * 2、通过调用 {@link #putBucketVersioning(PutBucketVersioningRequest)} 同步方法，传入 PutBucketVersioningRequest，返回 {@link PutBucketVersioningResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketVersioningRequest request = new PutBucketVersioningRequest(bucket);
     
     * try {
     *     PutBucketVersioningResult result = cosXml.putBucketVersioning(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request  Bucket 版本控制启用或者暂停请求 {@link PutBucketVersioningRequest}
     * @return PutBucketVersioningResult  Bucket 版本控制启用或者暂停请求返回的结果 {@link PutBucketVersioningResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    PutBucketVersioningResult putBucketVersioning(PutBucketVersioningRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 存储桶（Bucket）版本控制的异步方法.&nbsp;
     * <p>
     * 版本管理功能一经打开，只能暂停，不能关闭.
     * 通过版本控制，可以在一个 Bucket 中保留一个对象的多个版本.
     * 版本控制可以防止意外覆盖和删除对象，以便检索早期版本的对象.
     * 默认情况下，版本控制功能处于禁用状态，需要主动去启用或者暂停（Enabled 或者 Suspended）.
     *
     * <p>
     * cos Android SDK 中 Bucket 版本控制启用或者暂停的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketVersioningRequest} 构造方法，实例化 PutBucketVersioningRequest 对象;<br>
     * 2、通过调用 {@link #putBucketVersionAsync(PutBucketVersioningRequest, CosXmlResultListener)} 异步方法，传入 PutBucketVersioningRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketVersioningRequest request = new PutBucketVersioningRequest(bucket);
     
     * cosXml.putBucketVersionAsync(putObjectRequest, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request  Bucket 版本控制启用或者暂停请求 {@link PutBucketVersioningRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putBucketVersionAsync(PutBucketVersioningRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取跨区域复制配置信息的同步方法.&nbsp;
     * <p>
     * 跨区域复制是支持不同区域 Bucket 自动异步复制对象, 请查阅{@link #putBucketReplication(PutBucketReplicationRequest)}.
     *
     * <p>
     * cos Android SDK 中获取跨区域复制配置信息的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketReplicationRequest} 构造方法，实例化 GetBucketReplicationRequest 对象;<br>
     * 2、通过调用 {@link #getBucketReplication(GetBucketReplicationRequest)} 同步方法，传入 GetBucketReplicationRequest，返回 {@link GetBucketReplicationResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketReplicationRequest request = new GetBucketReplicationRequest(bucket);
     
     * try {
     *     GetBucketReplicationResult result = cosXml.putObject(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 获取跨区域复制配置信息请求 {@link GetServiceRequest}
     * @return GetBucketReplicationResult 获取跨区域复制配置信息请求返回的结果 {@link GetBucketReplicationResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    GetBucketReplicationResult getBucketReplication(GetBucketReplicationRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取跨区域复制配置信息的异步方法.&nbsp;
     * <p>
     * 跨区域复制是支持不同区域 Bucket 自动异步复制对象, 请查阅{@link #putBucketReplication(PutBucketReplicationRequest)}.
     * 
     * <p>
     * cos Android SDK 中获取跨区域复制配置信息的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetBucketReplicationRequest} 构造方法，实例化 GetBucketReplicationRequest 对象;<br>
     * 2、通过调用 {@link #getBucketReplicationAsync(GetBucketReplicationRequest, CosXmlResultListener)} 异步方法，传入 GetBucketReplicationRequest 和 CosXmlResultListener 进行异步回调操作..
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * GetBucketReplicationRequest request = new GetBucketReplicationRequest(bucket);
     
     * cosXml.getBucketReplicationAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 获取跨区域复制配置信息请求 {@link GetServiceRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getBucketReplicationAsync(GetBucketReplicationRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 配置跨区域复制的同步方法.&nbsp;
     * <p>
     * 跨区域复制是支持不同区域 Bucket 自动异步复制对象.注意，不能是同区域的 Bucket, 且源 Bucket 和目
     * 标 Bucket 必须已启用版本控制{@link #putBucketVersioning(PutBucketVersioningRequest)}.
     *
     * <p>
     * cos Android SDK 中配置跨区域复制的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketReplicationRequest} 构造方法，实例化 PutBucketReplicationRequest 对象;<br>
     * 2、通过调用 {@link #putBucketReplication(PutBucketReplicationRequest)} 同步方法，传入 PutBucketReplicationRequest，返回 {@link PutBucketReplicationResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketReplicationRequest request = new PutBucketReplicationRequest(bucket);
     * PutBucketReplicationRequest.RuleStruct ruleStruct = new PutBucketReplicationRequest.RuleStruct();
     * ruleStruct.id = "replication_id";
     * ruleStruct.isEnable = true;
     * ruleStruct.appid = "1253960454";
     * ruleStruct.bucket = "replicationtest";
     * ruleStruct.region = "ap-beijing";
     * request.setReplicationConfigurationWithRule(ruleStruct);
     
     * try {
     *     PutBucketReplicationResult result = cosXml.putBucketReplication(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 配置跨区域复制请求 {@link GetServiceRequest}
     * @return PutBucketReplicationResult 配置跨区域复制请求返回的结果 {@link PutBucketReplicationResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    PutBucketReplicationResult putBucketReplication(PutBucketReplicationRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 配置跨区域复制的异步方法.&nbsp;
     * <p>
     * 跨区域复制是支持不同区域 Bucket 自动异步复制对象.注意，不能是同区域的 Bucket, 且源 Bucket 和目
     * 标 Bucket 必须已启用版本控制{@link #putBucketVersioning(PutBucketVersioningRequest)}.
     * 
     * <p>
     * cos Android SDK 中配置跨区域复制的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutBucketReplicationRequest} 构造方法，实例化 PutBucketReplicationRequest 对象;<br>
     * 2、通过调用 {@link #putBucketReplicationAsync(PutBucketReplicationRequest, CosXmlResultListener)} 异步方法，传入 PutBucketReplicationRequest 和 CosXmlResultListener 进行异步回调操作.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * PutBucketReplicationRequest request = new PutBucketReplicationRequest(bucket);
     * PutBucketReplicationRequest.RuleStruct ruleStruct = new PutBucketReplicationRequest.RuleStruct();
     * ruleStruct.id = "replication_id";
     * ruleStruct.isEnable = true;
     * ruleStruct.appid = "1253960454";
     * ruleStruct.bucket = "replicationtest";
     * ruleStruct.region = "ap-beijing";
     * request.setReplicationConfigurationWithRule(ruleStruct);
     
     * cosXml.putBucketReplicationAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
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
     *
     * <p>
     * cos Android SDK 中删除跨区域复制配置的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteBucketReplicationRequest} 构造方法，实例化 DeleteBucketReplicationRequest 对象;<br>
     * 2、通过调用 {@link #deleteBucketReplication(DeleteBucketReplicationRequest)} 同步方法，传入 DeleteBucketReplicationRequest，返回 {@link DeleteBucketReplicationResult} 对象.
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * DeleteBucketReplicationRequest request = new DeleteBucketReplicationRequest("androidtest");
     
     * try {
     *     DeleteBucketReplicationResult result = cosXml.deleteBucketReplication(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 删除跨区域复制配置请求 {@link DeleteBucketReplicationRequest}
     * @return DeleteBucketReplicationResult 删除跨区域复制配置请求返回的结果 {@link DeleteBucketReplicationResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    DeleteBucketReplicationResult deleteBucketReplication(DeleteBucketReplicationRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除跨区域复制配置的异步方法.&nbsp;
     * <p>
     * 当不需要进行跨区域复制时，可以删除 Bucket 的跨区域复制配置. 跨区域复制，可以查阅{@link #putBucketReplication(PutBucketReplicationRequest)}
     * 
     * <p>
     * cos Android SDK 中删除跨区域复制配置的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteBucketReplicationRequest} 构造方法，实例化 DeleteBucketReplicationRequest 对象;<br>
     * 2、通过调用 {@link #deleteBucketReplicationAsync(DeleteBucketReplicationRequest, CosXmlResultListener)} 异步方法，传入 DeleteBucketReplicationRequest 和 CosXmlResultListener 进行异步回调操作.
     * 
     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * DeleteBucketReplicationRequest request = new DeleteBucketReplicationRequest("androidtest");
     
     * cosXml.deleteBucketReplicationAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 删除跨区域复制配置请求 {@link DeleteBucketReplicationRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteBucketReplicationAsync(DeleteBucketReplicationRequest request, CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 获取存储桶（Bucket）所有或者部分对象的版本信息的同步方法.&nbsp;
     * <p>
     * 通过查看对象的版本信息，可以得知对象存在哪些版本,便于管理对象.如检索或者删除某个特定版本的对象.
     * 版本管理功能，请查阅{@link #putBucketVersioning(PutBucketVersioningRequest)}
     *
     * <p>
     * cos Android SDK 中获取 Bucket 所有或者部分对象的版本信息的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link ListBucketVersionsRequest} 构造方法，实例化 ListBucketVersionsRequest 对象;<br>
     * 2、通过调用 {@link #listBucketVersions(ListBucketVersionsRequest)} 同步方法，传入 ListBucketVersionsRequest，返回 {@link ListBucketVersionsResult} 对象.
     * 
     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * ListBucketVersionsRequest request = new ListBucketVersionsRequest(bucket);
     
     * try {
     *      ListBucketVersionsResult result =  cosXml.listBucketVersions(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 获取 Bucket 所有或者部分对象的版本信息请求 {@link ListBucketVersionsRequest}
     * @return ListBucketVersionsResult获取 Bucket 所有或者部分对象的版本信息请求返回的结果 {@link ListBucketVersionsResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    ListBucketVersionsResult listBucketVersions(ListBucketVersionsRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取存储桶（Bucket）所有或者部分对象的版本信息的异步方法.&nbsp;
     * <p>
     * 通过查看对象的版本信息，可以得知对象存在哪些版本,便于管理对象.如检索或者删除某个特定版本的对象.
     * 版本管理功能，请查阅{@link #putBucketVersioning(PutBucketVersioningRequest)}
     *
     * <p>
     * cos Android SDK 中获取 Bucket 所有或者部分对象的版本信息的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link ListBucketVersionsRequest} 构造方法，实例化 ListBucketVersionsRequest 对象;<br>
     * 2、通过调用 {@link #listBucketVersionsAsync(ListBucketVersionsRequest, CosXmlResultListener)} 异步方法，传入 ListBucketVersionsRequest 和 CosXmlResultListener 进行异步回调操作.
     * 
     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * ListBucketVersionsRequest request = new ListBucketVersionsRequest(bucket);
     
     * cosXml.listBucketVersionsAsync(request, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *     }
     *    &nbsp;@Override
     *     public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException
     *             serviceException)  {
     *         String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
     *         Log.w("TEST",errorMsg);
     *     }
     * })
     *</pre></blockquote>
     * 
     * @param request 获取 Bucket 所有或者部分对象的版本信息请求 {@link ListBucketVersionsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void listBucketVersionsAsync(ListBucketVersionsRequest request, CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 判断指定的 bucket 是否存在。
     * </p>
     *
     * @param bucketName bucket 名称，如 test 等
     * @return true/false
     * @throws CosXmlClientException 抛出客户端异常
     * @throws CosXmlServiceException 抛出服务端异常
     */
    boolean doesBucketExist(String bucketName) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 判断指定的 bucket 是否存在的异步方法。
     * </p>
     *
     * @param bucketName bucket 名称，如 test 等
     * @param booleanListener 结果回调函数
     */
    void doesBucketExistAsync(String bucketName, CosXmlBooleanListener booleanListener);


    /**
     * <p>
     * 判断指定的对象是否存在，注意这里只能判断 {@link com.tencent.cos.xml.common.Region} 地域下是否存在。
     * </p>
     *
     * @param bucketName bucket 名称
     * @param objectName 对象的路径
     * @return
     * @throws CosXmlClientException 抛出的客户端异常
     * @throws CosXmlServiceException 抛出的服务端异常
     */
    boolean doesObjectExist(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 判断对象是否存在的异步方法。
     * </p>
     *
     * @param bucketName bucket 名称
     * @param objectName 对象的路径
     * @param booleanListener 结果回调函数
     */
    void doesObjectExistAsync(String bucketName, String objectName, CosXmlBooleanListener booleanListener);


    /**
     * <p>
     * 删除对象。
     * </p>
     *
     * @param bucketName bucket 名称
     * @param objectName 需要删除的对象的路径
     * @return 删除是否成功
     * @throws CosXmlClientException 抛出客户端异常
     * @throws CosXmlServiceException 抛出服务端异常
     */
    boolean deleteObject(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除对象的异步接口。
     * </p>
     *
     * @param bucketName bucket 名称
     * @param objectName 需要删除的对象路径
     * @param booleanListener 删除结果回调函数
     */
    void deleteObjectAsync(String bucketName, String objectName, CosXmlBooleanListener booleanListener);

    /**
     *
     * @param bucketName
     * @param objectName
     * @param metaData
     * @return
     * @throws CosXmlClientException
     * @throws CosXmlServiceException
     */
    boolean updateObjectMeta(String bucketName, String objectName, COSMetaData metaData) throws CosXmlClientException, CosXmlServiceException;


    /**
     *
     *
     * @param bucketName
     * @param objectName
     * @param metaData
     * @param booleanListener
     */
    void updateObjectMetaAsync(String bucketName, String objectName, COSMetaData metaData, final CosXmlBooleanListener booleanListener);


    PutBucketWebsiteResult putBucketWebsite(PutBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException;

    void putBucketWebsiteAsync(PutBucketWebsiteRequest request,  CosXmlResultListener cosXmlResultListener);

    GetBucketWebsiteResult getBucketWebsite(GetBucketWebsiteRequest request)throws CosXmlClientException, CosXmlServiceException;

    void getBucketWebsiteAsync(GetBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener);

    DeleteBucketWebsiteResult deleteBucketWebsite(DeleteBucketWebsiteRequest request) throws CosXmlClientException, CosXmlServiceException;

    void deleteBucketWebsiteAsync(DeleteBucketWebsiteRequest request, CosXmlResultListener cosXmlResultListener);

    PutBucketLoggingResult putBucketLogging(PutBucketLoggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    void putBucketLoggingAsync(PutBucketLoggingRequest request, CosXmlResultListener cosXmlResultListener);

    PutBucketTaggingResult putBucketTagging(PutBucketTaggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    void putBucketTaggingAsync(PutBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener);

    GetBucketTaggingResult getBucketTagging(GetBucketTaggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    void getBucketTaggingAsync(GetBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener);

    DeleteBucketTaggingResult deleteBucketTagging(DeleteBucketTaggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    void deleteBucketTaggingAsync(DeleteBucketTaggingRequest request, CosXmlResultListener cosXmlResultListener);

    GetBucketLoggingResult getBucketLogging(GetBucketLoggingRequest request)throws CosXmlClientException, CosXmlServiceException;

    void getBucketLoggingAsync(GetBucketLoggingRequest request, CosXmlResultListener cosXmlResultListener);

    PutBucketInventoryResult putBucketInventory(PutBucketInventoryRequest request)throws CosXmlClientException, CosXmlServiceException;

    void putBucketInventoryAsync(PutBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener);

    GetBucketInventoryResult getBucketInventory(GetBucketInventoryRequest request)throws CosXmlClientException, CosXmlServiceException;

    void getBucketInventoryAsync(GetBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener);

    DeleteBucketInventoryResult deleteBucketInventory(DeleteBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException;

    void deleteBucketInventoryAsync(DeleteBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener);

    ListBucketInventoryResult listBucketInventory(ListBucketInventoryRequest request) throws CosXmlClientException, CosXmlServiceException;

    void listBucketInventoryAsync(ListBucketInventoryRequest request, CosXmlResultListener cosXmlResultListener);

    GetBucketDomainResult getBucketDomain(GetBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException;

    void getBucketDomainAsync(GetBucketDomainRequest request, CosXmlResultListener cosXmlResultListener);

    PutBucketDomainResult putBucketDomain(PutBucketDomainRequest request) throws CosXmlClientException, CosXmlServiceException;

    void putBucketDomainAsync(PutBucketDomainRequest request, CosXmlResultListener cosXmlResultListener);

    SelectObjectContentResult selectObjectContent(SelectObjectContentRequest request) throws CosXmlClientException, CosXmlServiceException;

    void selectObjectContentAsync(SelectObjectContentRequest request, CosXmlResultListener cosXmlResultListener);

    GetBucketObjectVersionsResult getBucketObjectVersions(GetBucketObjectVersionsRequest getBucketObjectVersionsRequest) throws CosXmlClientException, CosXmlServiceException;

    void getBucketObjectVersionsAsync(GetBucketObjectVersionsRequest getBucketObjectVersionsRequest, CosXmlResultListener cosXmlResultListener);

}
