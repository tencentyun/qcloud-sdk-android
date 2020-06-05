package com.tencent.cos.xml;


import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.AbortMultiUploadResult;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.CopyObjectResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.ListPartsRequest;
import com.tencent.cos.xml.model.object.ListPartsResult;
import com.tencent.cos.xml.model.object.PostObjectRequest;
import com.tencent.cos.xml.model.object.PostObjectResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.object.UploadPartCopyRequest;
import com.tencent.cos.xml.model.object.UploadPartCopyResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;

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
 *
 * @see CosXmlSimpleService
 */

public interface SimpleCosXml {


    //COS Object API

    /**
     * <p>
     * 初始化分块上传的同步方法.&nbsp;
     * <p>
     * 使用分块上传对象时，首先要进行初始化分片上传操作，获取对应分块上传的 uploadId，用于后续上传操
     * 作.分块上传适合于在弱网络或高带宽环境下上传较大的对象.SDK 支持自行切分对象并分别调用{@link #uploadPart(UploadPartRequest)}或者{@link #uploadPartAsync(UploadPartRequest, CosXmlResultListener)}上传各
     * 个分块.
     * <p>
     * 关于分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112. &nbsp;</a><br>
     * 关于初始化分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7746">https://cloud.tencent.com/document/product/436/7746.</a><br>
     *
     * <p>
     * cos Android SDK 中初始化分块上传请求的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link InitMultipartUploadRequest} 构造方法，实例化 InitMultipartUploadRequest 对象;<br>
     * 2、通过调用 {@link #initMultipartUpload(InitMultipartUploadRequest)} 同步方法，传入 InitMultipartUploadRequest，返回 {@link InitMultipartUploadResult} 对象.<br>
     *
     * <p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, cosPath);
     * String uploadId = null;
     * try {
     *     InitMultipartUploadResult initMultipartUploadResult = simpleCosXml.initMultipartUpload(initMultipartUploadRequest);
     *     Log.w("TEST","success");
     *     uploadId =initMultipartUploadResult.initMultipartUpload.uploadId;
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     *
     * @param request 初始化分块上传请求 {@link InitMultipartUploadRequest}
     * @return InitMultipartUploadResult 初始化分块上传请求返回的结果 {@link InitMultipartUploadResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    InitMultipartUploadResult initMultipartUpload(InitMultipartUploadRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 初始化分块上传的异步方法.&nbsp;
     * <p>
     * 使用分块上传对象时，首先要进行初始化分片上传操作，获取对应分块上传的 uploadId，用于后续上传操
     * 作.分块上传适合于在弱网络或高带宽环境下上传较大的对象.SDK 支持自行切分对象并分别调用{@link #uploadPart(UploadPartRequest)}上传各
     * 个分块.
     * <p>
     * 关于分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112. &nbsp;</a><br>
     * 关于初始化分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7746">https://cloud.tencent.com/document/product/436/7746.</a><br>
     *
     * cos Android SDK 中初始化分块上传请求的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link InitMultipartUploadRequest} 构造方法，实例化 InitMultipartUploadRequest 对象;<br>
     * 2、通过调用 {@link #initMultipartUploadAsync(InitMultipartUploadRequest, CosXmlResultListener)} 异步方法，传入 InitMultipartUploadRequest 和 CosXmlResultListener 进行异步回调操作.<br>
     *
     * <p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * InitMultipartUploadRequest initMultipartUploadRequest = new InitMultipartUploadRequest(bucket, cosPath);
     * String uploadId = null;
     * simpleCosXml.initMultipartUploadAsync(initMultipartUploadRequest, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *         uploadId = ((InitMultipartUploadResult)cosXmlResult).initMultipartUpload.uploadId;
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
     * @param request 初始化分块上传请求 {@link InitMultipartUploadRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void initMultipartUploadAsync(InitMultipartUploadRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 查询特定分块上传中的已上传的块的同步方法.&nbsp;
     * <p>
     * COS 支持查询特定分块上传中的已上传的块, 即可以
     * 罗列出指定 UploadId 所属的所有已上传成功的分块. 因此，基于此可以完成续传功能.
     * <p>
     * 关于分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112,&nbsp;</a><br>
     * 关于查询特定分块上传中的已上传块接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7747">https://cloud.tencent.com/document/product/436/7747.</a>
     * <br>
     * <p>
     * cos Android SDK 中查询特定分块上传中的已上传块请求的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link ListPartsRequest} 构造方法，实例化 ListPartsRequest 对象;<br>
     * 2、通过调用 {@link #listParts(ListPartsRequest)} 同步方法，传入 ListPartsRequest，返回 {@link ListPartsResult} 对象.<br>
     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String uploadId = "初始化分片返回的 uploadId";
     * ListPartsRequest listPartsRequest = new ListPartsRequest(bucket, cosPath, uploadId);
     * try {
     *     ListPartsResult listPartsResult = simpleCosXml.listParts(listPartsRequest);
     *     Log.w("TEST","success: " + listPartsResult.listParts.toString());
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 查询特定分块上传中的已上传块请求 {@link ListPartsRequest}
     * @return ListPartsResult 查询特定分块上传中的已上传块请求返回的结果 {@link ListPartsResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    ListPartsResult listParts(ListPartsRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询特定分块上传中的已上传的块的异步方法.&nbsp;
     * <p>
     * COS 支持查询特定分块上传中的已上传的块, 即可以
     * 罗列出指定 UploadId 所属的所有已上传成功的分块. 因此，基于此可以完成续传功能.
     * <p>
     * 关于分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112,&nbsp;</a><br>
     * 关于查询特定分块上传中的已上传块接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7747">https://cloud.tencent.com/document/product/436/7747.</a>
     * <br>
     * <p>
     * cos Android SDK 中查询特定分块上传中的已上传块请求的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link ListPartsRequest} 构造方法，实例化 ListPartsRequest 对象;<br>
     * 2、通过调用 {@link #listPartsAsync(ListPartsRequest, CosXmlResultListener)} 异步方法，传入 ListPartsRequest 和 CosXmlResultListener 进行异步回调操作.<br>
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String uploadId = "初始化分片返回的 uploadId";
     * ListPartsRequest listPartsRequest = new ListPartsRequest(bucket, cosPath, uploadId);
     * simpleCosXml.listPartsAsync(listPartsRequest, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success: " + listPartsResult.listParts.toString());
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
     * @param request 查询特定分块上传中已上传的块请求 {@link ListPartsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void listPartsAsync(ListPartsRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 上传一个分片块的同步方法.&nbsp;
     * <p>
     * 使用分块上传时，可将对象切分成一个个分块的方式上传到 COS，每个分块上传需要携带分块号（partNumber） 和 uploadId（{@link #initMultipartUpload(InitMultipartUploadRequest)}），
     * 每个分块大小为 1 MB 到 5 GB ，最后一个分块可以小于 1 MB, 若传入 uploadId 和 partNumber都相同，
     * 后传入的块将覆盖之前传入的块，且支持乱序上传.
     * <p>
     * 关于分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112. &nbsp;</a>
     * <br>
     * 关于上传一个对象的分块接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7750">https://cloud.tencent.com/document/product/436/7750.</a>
     * <br>
     * <p>
     * cos Android SDK 中上传一个对象某个分片块请求的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link UploadPartRequest} 构造方法，实例化 UploadPartRequest 对象;<br>
     * 2、通过调用 {@link #uploadPart(UploadPartRequest)} 同步方法，传入 UploadPartRequest，返回 {@link UploadPartRequest} 对象.<br>
     * 

     *
     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String uploadId = "初始化分片返回的 uploadId";
     * int partNumber = 1;//此次上传分片的编号，从 1 开始
     * String srcPath = "本地文件的绝对路径";
     * UploadPartRequest uploadPartRequest = new UploadPartRequest(bucket, cosPath, partNumber, srcPath, uploadId);
     * uploadPartRequest.setProgressListener(new CosXmlProgressListener() {// 进度回调
     *    &nbsp;@Override
     *     public void onProgress(long progress, long max) {
     *         float result = (float) (progress * 100.0/max);
     *         Log.w("TEST","progress =" + (long)result + "%");
     *    }
     * });
     * String eTag = null;
     * try {
     *     UploadPartResult uploadPartResult = simpleCosXml.uploadPart(uploadPartRequest);
     *     Log.w("TEST","success");
     *     eTag = uploadPartResult.eTag;
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 上传一个对象某个分块请求 {@link UploadPartRequest}
     * @return UploadPartResult 上传一个对象某个分块请求返回的结果 {@link UploadPartResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    UploadPartResult uploadPart(UploadPartRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 上传一个分片块的异步方法.&nbsp;
     * <p>
     * 使用分块上传时，可将对象切分成一个个分块的方式上传到 COS，每个分块上传需要携带分块号（partNumber） 和 uploadId（{@link #initMultipartUpload(InitMultipartUploadRequest)}），
     * 每个分块大小为 1 MB 到 5 GB ,最后一个分块可以小于 1 MB, 若传入 uploadId 和 partNumber都相同，
     * 后传入的块将覆盖之前传入的块，且支持乱序上传.
     * <p>
     * 关于分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112. &nbsp;</a>
     * <br>
     * 关于上传一个对象的分块接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7750">https://cloud.tencent.com/document/product/436/7750.</a>
     * <br>
     * <p>
     * cos Android SDK 中上传一个对象某个分片块请求的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link UploadPartRequest} 构造方法，实例化 UploadPartRequest 对象;<br>
     * 2、通过调用 {@link #uploadPartAsync(UploadPartRequest, CosXmlResultListener)} 方法，传入 UploadPartRequest 和 CosXmlResultListener 进行异步回调操作.<br>
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String uploadId = "初始化分片返回的 uploadId";
     * int partNumber = 1;//此次上传分片的编号，从 1 开始
     * String srcPath = "本地文件的绝对路径";
     * UploadPartRequest uploadPartRequest = new UploadPartRequest(bucket, cosPath, partNumber, srcPath, uploadId);
     * uploadPartRequest.setProgressListener(new CosXmlProgressListener() {// 进度回调
     *    &nbsp;@Override
     *     public void onProgress(long progress, long max) {
     *         float result = (float) (progress * 100.0/max);
     *         Log.w("TEST","progress =" + (long)result + "%");
     *    }
     * });
     * String eTag = null;
     * simpleCosXml.uploadPartAsync(uploadPartRequest, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *         eTag = uploadPartResult.eTag;
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
     * @param request 上传某个分块请求 {@link UploadPartRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void uploadPartAsync(UploadPartRequest request, final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 舍弃一个分块上传且删除已上传的分片块的同步方法.&nbsp;
     * <p>
     * COS 支持舍弃一个分块上传且删除已上传的分片块. 注意，已上传但是未终止的分片块会占用存储空间进
     * 而产生存储费用.因此，建议及时完成分块上传 或者舍弃分块上传.
     * <p>
     * 关于分块上传的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112.</a>
     * <br>
     * 关于舍弃一个分块上传且删除已上传的分片块接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7740">https://cloud.tencent.com/document/product/436/7740.</a>
     * <br>
     * <p>
     * cos Android SDK 中舍弃一个分块上传且删除已上传的分片块请求的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link AbortMultiUploadRequest} 构造方法，实例化 AbortMultiUploadRequest 对象;<br>
     * 2、通过调用 {@link #abortMultiUpload(AbortMultiUploadRequest)} 同步方法，传入 AbortMultiUploadRequest，返回 {@link AbortMultiUploadResult} 对象.<br>
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String uploadId = "初始化分片返回的 uploadId";
     * AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucket, cosPath, uploadId);
     * try {
     *     AbortMultiUploadResult abortMultiUploadResult = simpleCosXml.abortMultiUpload(abortMultiUploadRequest);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 舍弃一个分块上传且删除已上传的分片块请求 {@link AbortMultiUploadRequest}
     * @return AbortMultiUploadResult 舍弃一个分块上传且删除已上传的分片块请求返回的结果 {@link AbortMultiUploadResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    AbortMultiUploadResult abortMultiUpload(AbortMultiUploadRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 舍弃一个分块上传且删除已上传的分片块的异步方法.&nbsp;
     * <p>
     * COS 支持舍弃一个分块上传且删除已上传的分片块. 注意，已上传但是未终止的分片块会占用存储空间进
     * 而产生存储费用.因此，建议及时完成分块上传 或者舍弃分块上传.
     * <p>
     * 关于分块上传的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112.</a>
     * <br>
     * 关于舍弃一个分块上传且删除已上传的分片块接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7740">https://cloud.tencent.com/document/product/436/7740.</a>
     * <br>
     * <p>
     * cos Android SDK 中舍弃一个分块上传且删除已上传的分片块请求的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link AbortMultiUploadRequest} 构造方法，实例化 AbortMultiUploadRequest 对象;<br>
     * 2、通过调用 {@link #abortMultiUploadAsync(AbortMultiUploadRequest, CosXmlResultListener)} 异步方法，传入 AbortMultiUploadRequest 和 CosXmlResultListener 进行异步回调操作.
     * <br>

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String uploadId = "初始化分片返回的 uploadId";
     * AbortMultiUploadRequest abortMultiUploadRequest = new AbortMultiUploadRequest(bucket, cosPath, uploadId);
     * simpleCosXml.abortMultiUploadAsync(initMultipartUploadRequest, new CosXmlResultListener() {
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
     * @param request 舍弃一个分块上传且删除已上传的分片块请求 {@link AbortMultiUploadRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void abortMultiUploadAsync(AbortMultiUploadRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 完成整个分块上传的同步方法.&nbsp;
     * <p>
     * 当使用分块上传（{@link #uploadPart(UploadPartRequest)}）完对象的所有块以后，必须调用该 {@link #completeMultiUpload(CompleteMultiUploadRequest)} 或者 {@link #completeMultiUploadAsync(CompleteMultiUploadRequest, CosXmlResultListener)}
     * 来完成整个文件的分块上传.且在该请求的 Body 中需要给出每一个块的 PartNumber 和 ETag，用来校验块的准
     * 确性.
     * <p>
     * 分块上传适合于在弱网络或高带宽环境下上传较大的对象.SDK 支持自行切分对象并分别调用{@link #uploadPart(UploadPartRequest)}上传各
     * 个分块.<br>
     * 关于分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112.</a><br>
     * 关于完成整个分片上传接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7742">https://cloud.tencent.com/document/product/436/7742.</a><br>
     *
     * <p>
     * cos Android SDK 中完成整个分块上传请求的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link CompleteMultiUploadRequest} 构造方法，实例化 CompleteMultiUploadRequest 对象;<br>
     * 2、通过调用 {@link #completeMultiUpload(CompleteMultiUploadRequest)} 同步方法，传入 CompleteMultiUploadRequest，返回 {@link CompleteMultiUploadResult} 对象.<br>
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String uploadId = "初始化分片返回的 uploadId";
     * int partNumber = 1; // 分片编号
     * String etag = "编号为 partNumber 对应分片上传结束返回的 eTag ";
     * Map&lt;Integer, String&gt; partNumberAndETag = new HashMap&lt;&gt;();
     * partNumberAndETag.put(partNumber, etag);
     * CompleteMultiUploadRequest completeMultiUploadRequest = new CompleteMultiUploadRequest(bucket, cosPath, uploudId, partNumberAndETag);
     * try {
     *     CompleteMultiUploadResult completeMultiUploadResult = simpleCosXml.completeMultiUpload(completeMultiUploadRequest);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 完成整个分块上传请求 {@link CompleteMultiUploadRequest}
     * @return CompleteMultiUploadResult 完成整个分块上传请求返回的结果 {@link CompleteMultiUploadResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    CompleteMultiUploadResult completeMultiUpload(CompleteMultiUploadRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 完成整个分块上传的异步方法.&nbsp;
     * <p>
     * 当使用分块上传（{@link #uploadPart(UploadPartRequest)}）完对象的所有块以后，必须调用该 {@link #completeMultiUpload(CompleteMultiUploadRequest)} 或者 {@link #completeMultiUploadAsync(CompleteMultiUploadRequest, CosXmlResultListener)}
     * 来完成整个文件的分块上传.且在该请求的 Body 中需要给出每一个块的 PartNumber 和 ETag，用来校验块的准
     * 确性.
     * <p>
     * 分块上传适合于在弱网络或高带宽环境下上传较大的对象.SDK 支持自行切分对象并分别调用{@link #uploadPart(UploadPartRequest)}上传各
     * 个分块.<br>
     * 关于分块上传的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112.</a><br>
     * 关于完成整个分片上传接口的描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7742">https://cloud.tencent.com/document/product/436/7742.</a><br>
     * 
     * <p>
     * cos Android SDK 中完成整个分块上传请求的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link CompleteMultiUploadRequest} 构造方法，实例化 CompleteMultiUploadRequest 对象;<br>
     * 2、通过调用 {@link #completeMultiUploadAsync(CompleteMultiUploadRequest, CosXmlResultListener)} 异步方法，传入 CompleteMultiUploadRequest 和 CosXmlResultListener 进行异步回调操作.
     * <br>

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String uploadId = "初始化分片返回的 uploadId";
     * int partNumber = 1; // 分片编号
     * String etag = "编号为 partNumber 对应分片上传结束返回的 eTag ";
     * Map&lt;Integer, String&gt; partNumberAndETag = new HashMap&lt;&gt;();
     * partNumberAndETag.put(partNumber, etag);
     * CompleteMultiUploadRequest completeMultiUploadRequest = new CompleteMultiUploadRequest(bucket, cosPath, uploudId, partNumberAndETag);
     * simpleCosXml.completeMultiUploadAsync(completeMultiUploadRequest, new CosXmlResultListener() {
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
     * @param request 初始化分片请求 {@link CompleteMultiUploadRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void completeMultiUploadAsync(CompleteMultiUploadRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 删除 COS 上单个对象的同步方法.&nbsp;
     * <p>
     * COS 支持直接删除一个或多个对象，当仅需要删除一个对象时,只需要提供对象的名称（即对象键)即可.
     * <p>
     * 关于删除 COS 上单个对象的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14119">https://cloud.tencent.com/document/product/436/14119.</a>
     * <br>
     * 关于删除 COS 上单个对象接口的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7743">https://cloud.tencent.com/document/product/436/7743.</a>
     * <br>
     * <p>
     * cos Android SDK 中删除 COS 上单个对象请求的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteObjectRequest} 构造方法，实例化 DeleteObjectRequest 对象;<br>
     * 2、通过调用 {@link #deleteObject(DeleteObjectRequest)} 同步方法，传入 DeleteObjectRequest，返回 {@link DeleteObjectResult} 对象.<br>
     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, cosPath);
     * try {
     *     DeleteObjectResult deleteObjectResult = simpleCosXml.deleteObject(deleteObjectRequest);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * @param request 删除 COS 上单个对象请求 {@link DeleteObjectRequest}
     * @return CompleteMultiUploadResult 删除 COS 上单个对象请求返回的结果 {@link DeleteObjectResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    DeleteObjectResult deleteObject(DeleteObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除 COS 上单个对象的异步方法.&nbsp;
     * <p>
     * COS 支持直接删除一个或多个对象，当仅需要删除一个对象时,只需要提供对象的名称（即对象键)即可.
     * <p>
     * 关于删除 COS 上单个对象的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14119">https://cloud.tencent.com/document/product/436/14119.</a>
     * <br>
     * 关于删除 COS 上单个对象接口的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7743">https://cloud.tencent.com/document/product/436/7743.</a>
     * <br>
     * <p>
     * cos Android SDK 中删除 COS 上单个对象请求的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link DeleteObjectRequest} 构造方法，实例化 DeleteObjectRequest 对象;<br>
     * 2、通过调用 {@link #deleteObjectAsync(DeleteObjectRequest, CosXmlResultListener)} 异步方法，传入 DeleteObjectRequest 和 CosXmlResultListener 进行异步回调操作.
     * <br>

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, cosPath);
     * simpleCosXml.deleteObjectAsync(deleteObjectRequest, new CosXmlResultListener() {
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
     * @param request 删除 COS 上单个对象请求 {@link DeleteObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteObjectAsync(DeleteObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 下载 COS 对象的同步方法.&nbsp;
     * <p>
     * 可以直接发起 GET 请求获取 COS 中完整的对象数据, 或者在 GET 请求
     * 中传入 Range 请求头部获取对象的部分内容.
     * 获取COS 对象的同时，对象的元数据将会作为 HTTP 响应头部随对象内容一同返回，COS 支持GET 请求时
     * 使用 URL 参数的方式覆盖响应的部分元数据值，例如覆盖 Content-iDisposition 的响应值.
     * <p>
     * 关于获取 COS 对象的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14115">https://cloud.tencent.com/document/product/436/14115.</a><br>
     * 关于获取 COS 对象的接口描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7753">https://cloud.tencent.com/document/product/436/7753.</a><br>
     * <p>
     * cos Android SDK 中获取 COS 对象请求的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetObjectRequest} 构造方法，实例化 GetObjectRequest 对象;<br>
     * 2、通过调用 {@link #getObject(GetObjectRequest)} 方法，传入 GetObjectRequest，返回 {@link GetObjectResult} 对象.<br>
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String savePath = "savePath"; //文件下载到本地文件夹的绝对路径
     * GetObjectRequest getObjectRequest = GetObjectRequest(bucket, cosPath, savePath);
     * getObjectRequest.setProgressListener(new CosXmlProgressListener() {// 进度回调
     *    &nbsp;@Override
     *     public void onProgress(long progress, long max) {
     *         float result = (float) (progress * 100.0/max);
     *         Log.w("TEST","progress =" + (long)result + "%");
     *    }
     * });
     * try {
     *     GetObjectResult getObjectResult =simpleCosXml.getObject(getObjectRequest);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 获取 COS 对象的请求 {@link GetObjectRequest}
     * @return GetObjectResult 获取 COS 对象的请求返回的结果 {@link GetObjectResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    GetObjectResult getObject(GetObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 下载 COS 对象的异步方法.&nbsp;
     * <p>
     * 可以直接发起 GET 请求获取 COS 中完整的对象数据, 或者在 GET 请求
     * 中传入 Range 请求头部获取对象的部分内容.
     * 获取COS 对象的同时，对象的元数据将会作为 HTTP 响应头部随对象内容一同返回，COS 支持GET 请求时
     * 使用 URL 参数的方式覆盖响应的部分元数据值，例如覆盖 Content-iDisposition 的响应值.
     * <p>
     * 关于获取 COS 对象的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14115">https://cloud.tencent.com/document/product/436/14115.</a><br>
     * 关于获取 COS 对象的接口描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7753">https://cloud.tencent.com/document/product/436/7753.</a><br>
     * <p>
     * cos Android SDK 中获取 COS 对象请求的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link GetObjectRequest} 构造方法，实例化 GetObjectRequest 对象;<br>
     * 2、通过调用 {@link #getObjectAsync(GetObjectRequest, CosXmlResultListener)} 异步方法，传入 GetObjectRequest 和 CosXmlResultListener 进行异步回调操作.
     * <br>

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String savePath = "savePath"; //文件下载到本地文件夹的绝对路径
     * GetObjectRequest getObjectRequest = GetObjectRequest(bucket, cosPath, savePath);
     * getObjectRequest.setProgressListener(new CosXmlProgressListener() {// 进度回调
     *    &nbsp;@Override
     *     public void onProgress(long progress, long max) {
     *         float result = (float) (progress * 100.0/max);
     *         Log.w("TEST","progress =" + (long)result + "%");
     *    }
     * });
     * simpleCosXml.getObjectAsync(getObjectRequest, new CosXmlResultListener() {
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
     * @param request 获取 COS 对象的请求 {@link GetObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getObjectAsync(GetObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 简单上传的同步方法.&nbsp;
     * <p>
     * 简单上传主要适用于在单个请求中上传一个小于 5 GB 大小的对象.
     * 对于大于 5 GB 的对象(或者在高带宽或弱网络环境中）优先使用分片上传的方式 (<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112</a>).&nbsp;
     * <p>
     * 关于简单上传的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14113">https://cloud.tencent.com/document/product/436/14113.</a><br>
     * 关于简单上传接口的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7749">https://cloud.tencent.com/document/product/436/7749.</a><br>
     *
     * <p>
     * cos Android SDK 中简单上传请求的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutObjectRequest} 构造方法，实例化 PutObjectRequest 对象;<br>
     * 2、通过调用 {@link #putObject(PutObjectRequest)} 同步方法，传入 PutObjectRequest，返回 {@link PutObjectResult} 对象.<br>
     * 

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String srcPath = "srcPath"; //本地文件的绝对路径
     * PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
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
     *     PutObjectResult putObjectResult = simpleCosXml.putObject(putObjectRequest);
     *     Log.w("TEST","success");
     *     eTag = putObjectResult.eTag;
     *     accessUrl = putObjectResult.accessUrl
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     * 
     * @param request 简单上传请求 {@link PutObjectRequest}
     * @return PutObjectResult 简单上传请求返回的结果 {@link PutObjectResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    PutObjectResult putObject(PutObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 简单上传的异步方法.&nbsp;
     * <p>
     * 简单上传主要适用于在单个请求中上传一个小于 5 GB 大小的对象.
     * 对于大于 5 GB 的对象(或者在高带宽或弱网络环境中）优先使用分片上传的方式 (<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112</a>).&nbsp;
     * <p>
     * 关于简单上传的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/14113">https://cloud.tencent.com/document/product/436/14113.</a><br>
     * 关于简单上传接口的具体描述，请查看 <a href="https://cloud.tencent.com/document/product/436/7749">https://cloud.tencent.com/document/product/436/7749.</a><br>
     * 
     * <p>
     * cos Android SDK 中简单上传请求的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link PutObjectRequest} 构造方法，实例化 PutObjectRequest 对象;<br>
     * 2、通过调用 {@link #putObjectAsync(PutObjectRequest, CosXmlResultListener)} 异步方法，传入 PutObjectRequest 和 CosXmlResultListener 进行异步回调操作.
     * <br>

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String srcPath = "srcPath"; //本地文件的绝对路径
     * PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
     * putObjectRequest.setProgressListener(new CosXmlProgressListener() { // 进度回调
     *    &nbsp;@Override
     *     public void onProgress(long progress, long max) {
     *         float result = (float) (progress * 100.0/max);
     *         Log.w("TEST","progress =" + (long)result + "%");
     *    }
     * });
     * String eTag = null; //上传返回的文件 md5
     * String accessUrl = null; //访问文件的地址
     * simpleCosXml.putObjectAsync(putObjectRequest, new CosXmlResultListener() {
     *    &nbsp;@Override
     *     public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
     *         Log.w("TEST","success");
     *         eTag = putObjectResult.eTag;
     *         accessUrl = putObjectResult.accessUrl
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
     * @param request 简单上传请求 {@link PutObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putObjectAsync(PutObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    PostObjectResult postObject(PostObjectRequest request)throws CosXmlClientException, CosXmlServiceException;

    void postObjectAsync(PostObjectRequest request, final  CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 下载文件并保存到字节数组中，请不要通过本接口下载大文件，否则容易造成内存溢出。
     * </p>
     *
     * <p>
     * 示例：
     * <blockquote><pre>
     * String bucketName = "bucket"; // 存储桶名称
     * String objectName = "cosPath"; // 远端路径，即存储到 COS 上的绝对路径
     *
     * byte[] data = simpleCosXml.getObject(bucketName, objectName);
     *
     *</pre></blockquote>
     *
     * @param bucketName bucket 名称
     * @param objectName object 路径
     * @return object 的字节数据
     * @throws CosXmlClientException 抛出的客户端异常
     * @throws CosXmlServiceException 抛出的服务端异常
     */
    byte[] getObject(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException;


    /**
     * <p>
     * 获取 COS 对象的元数据信息(meta data)的同步方法.&nbsp;
     * <p>
     * 获取 COS 对象的元数据信息，需要与 Get 的权限一致.且请求是不返回消息体的.若请求中需要设置If-Modified-Since
     * 头部，则统一采用 GMT(RFC822) 时间格式，例如：Tue, 22 Oct 2017 01:35:21 GMT.如果对象不存在，则
     * 返回404.
     * <p>
     * 关于获取 COS 对象的元数据信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7745">https://cloud.tencent.com/document/product/436/7745.</a>
     *
     * <p>
     * cos Android SDK 中获取 COS 对象的元数据信息的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link HeadObjectRequest} 构造方法，实例化 HeadObjectRequest 对象;<br>
     * 2、通过调用 {@link #headObject(HeadObjectRequest)} 同步方法，传入 HeadObjectRequest，返回 {@link HeadObjectResult} 对象.
     *

     *
     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * HeadObjectRequest request = new HeadObjectRequest(bucket, cosPath);
     * try {
     *     HeadObjectResult result = cosXml.headObject(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     *
     * @param request 获取 COS 对象的元数据信息请求 {@link HeadObjectRequest}
     * @return HeadObjectResult 获取 COS 对象的元数据信息请求返回的结果 {@link HeadObjectResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    HeadObjectResult headObject(HeadObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取 COS 对象的元数据信息(meta data)的异步方法.&nbsp;
     * <p>
     * 获取 COS 对象的元数据信息，需要与 Get 的权限一致.且请求是不返回消息体的.若请求中需要设置If-Modified-Since
     * 头部，则统一采用 GMT(RFC822) 时间格式，例如：Tue, 22 Oct 2017 01:35:21 GMT.如果对象不存在，则
     * 返回404.
     * 关于获取 COS 对象的元数据信息接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/7745">https://cloud.tencent.com/document/product/436/7745.</a>
     *
     * <p>
     * cos Android SDK 中获取 COS 对象的元数据信息的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link HeadObjectRequest} 构造方法，实例化 HeadObjectRequest 对象;<br>
     * 2、通过调用 {@link #headObjectAsync(HeadObjectRequest, CosXmlResultListener)} 异步方法，传入 HeadObjectRequest 和 CosXmlResultListener 进行异步回调操作.
     *

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * HeadObjectRequest request = new HeadObjectRequest(bucket, cosPath);
     * cosXml.headObjectAsync(request, new CosXmlResultListener() {
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
     * @param request 获取 COS 对象的元数据信息请求 {@link HeadObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void headObjectAsync(HeadObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 简单复制对象的同步方法.&nbsp;
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
     * 关于简单复制接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/10881">https://cloud.tencent.com/document/product/436/10881.</a>
     *
     * <p>
     * cos Android SDK 中简单复制对象的同步方法具体步骤如下：<br>
     * 1、通过调用 {@link CopyObjectRequest} 构造方法，实例化 CopyObjectRequest 对象;<br>
     * 2、通过调用 {@link #copyObject(CopyObjectRequest)} 同步方法，传入 CopyObjectRequest，返回 {@link CopyObjectResult} 对象.
     *

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
     * "appid", "*source_bucket", "source_region", "source");
     * try {
     *     CopyObjectResult result = cosXml.copyObject(request);
     *     Log.w("TEST","success");
     * } catch (CosXmlClientException e) {
     *     Log.w("TEST","CosXmlClientException =" + e.toString());
     * } catch (CosXmlServiceException e) {
     *     Log.w("TEST","CosXmlServiceException =" + e.toString());
     * }
     *</pre></blockquote>
     *
     * @param request 简单复制对象请求 {@link CopyObjectRequest}
     * @return CopyObjectResult 简单复制对象请求返回的结果 {@link CopyObjectResult}
     * @throws CosXmlClientException 抛出客户异常 {@link CosXmlClientException}
     * @throws CosXmlServiceException 抛出服务异常 {@link CosXmlServiceException}
     */
    CopyObjectResult copyObject(CopyObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 简单复制对象的异步方法.&nbsp;<br>
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
     * 关于简单复制接口的具体描述，请查看<a href="https://cloud.tencent.com/document/product/436/10881">https://cloud.tencent.com/document/product/436/10881.</a>
     *
     * <p>
     * cos Android SDK 中简单复制对象的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link CopyObjectRequest} 构造方法，实例化 CopyObjectRequest 对象;<br>
     * 2、通过调用 {@link #copyObjectAsync(CopyObjectRequest, CosXmlResultListener)} 异步方法，传入 CopyObjectRequest 和 CosXmlResultListener 进行异步回调操作.
     *

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
     * "appid", "*source_bucket", "source_region", "source");
     * CopyObjectRequest request = new CopyObjectRequest(bucket, cosPath, copySourceStruct);
     * cosXml.copyObjectAsync(request, new CosXmlResultListener() {
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
     * @param request 简单复制对象请求 {@link CopyObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void copyObjectAsync(CopyObjectRequest request, final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 分块复制的同步方法.&nbsp;
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
     * CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
     * "appid", "*source_bucket", "source_region", "source");
     * int partNumber = 1; //分片号
     * String uploadId = "uploadId"; //分片uploadId
     * UploadPartCopyRequest request = new UploadPartCopyRequest(bucket, cosPath, partNumber, uploadIdcopySourceStruct);
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
    UploadPartCopyResult copyObject(UploadPartCopyRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 分块复制的异步方法.&nbsp;
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
     * cos Android SDK 中分块复制的异步方法具体步骤如下：<br>
     * 1、通过调用 {@link UploadPartCopyRequest} 构造方法，实例化 UploadPartCopyRequest 对象;<br>
     * 2、通过调用 {@link #copyObjectAsync(UploadPartCopyRequest, CosXmlResultListener)} 异步方法，传入 UploadPartCopyRequest 和 CosXmlResultListener 进行异步回调操作.
     *

     *<p>
     * 示例：
     * <blockquote><pre>
     * String bucket = "bucket"; //存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)
     * String cosPath = "cosPath"; //远端路径，即存储到 COS 上的绝对路径
     * String srcPath = "srcPath"; //本地文件的绝对路径
     * CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
     * "appid", "*source_bucket", "source_region", "source");
     * int partNumber = 1; //分片号
     * String uploadId = "uploadId"; //分片uploadId
     * UploadPartCopyRequest request = new UploadPartCopyRequest(bucket, cosPath, partNumber, uploadIdcopySourceStruct);
     * cosXml.copyObjectAsync(request,  new CosXmlResultListener() {
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
     * @param request 分块复制请求 {@link UploadPartCopyRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void copyObjectAsync(UploadPartCopyRequest request,final CosXmlResultListener cosXmlResultListener);

    /**
     * 取消请求任务
     * @param cosXmlRequest 请求request {@link CosXmlRequest}
     */
    void cancel(CosXmlRequest cosXmlRequest);

    /**
     * 取消所有的请求任务
     */
    void cancelAll();

    /**
     * 释放所有的请求. 请参阅 {@link #cancelAll()}
     */
    void release();

}
