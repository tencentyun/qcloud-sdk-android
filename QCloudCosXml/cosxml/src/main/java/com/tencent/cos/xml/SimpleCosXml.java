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
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.listener.CosXmlResultSimpleListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsRequest;
import com.tencent.cos.xml.model.bucket.ListMultiUploadsResult;
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
 * 提供用于访问Tencent Cloud COS服务的简单接口。[全面接口请使用 {@link CosXml}]
 *
 * <p>
 * 对象存储（Cloud Object Storage，COS）是腾讯云提供的一种存储海量文件的分布式存储服务，用户可通过网络随时存储和查看数据。腾讯云 COS 使所有用户都能使用具备高扩展性、低成本、可靠和安全的数据存储服务。
 *
 * <p>
 * SimpleCosXml为每个接口都提供了同步和异步操作，如同步接口为{@link #putObject(PutObjectRequest)}，
 * 则对应的异步接口为{@link #putObjectAsync(PutObjectRequest, CosXmlResultListener)}.<br><br>
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

public interface SimpleCosXml {
    /**
     * <p>
     * 下载 COS 对象的同步方法.&nbsp;
     * <p>
     * 可以直接发起 GET 请求获取 COS 中完整的对象数据, 或者在 GET 请求
     * 中传入 Range 请求头部获取对象的部分内容.
     * 获取COS 对象的同时，对象的元数据将会作为 HTTP 响应头部随对象内容一同返回，COS 支持GET 请求时
     * 使用 URL 参数的方式覆盖响应的部分元数据值，例如覆盖 Content-iDisposition 的响应值.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14115">https://cloud.tencent.com/document/product/436/14115.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7753">https://cloud.tencent.com/document/product/436/7753.</a><br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E4.B8.8B.E8.BD.BD.E5.AF.B9.E8.B1.A1">下载对象示例</a>
     * 
     * @param request 获取 COS 对象的请求 {@link GetObjectRequest}
     * @return 获取 COS 对象的请求返回的结果 {@link GetObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
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
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14115">https://cloud.tencent.com/document/product/436/14115.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7753">https://cloud.tencent.com/document/product/436/7753.</a><br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E4.B8.8B.E8.BD.BD.E5.AF.B9.E8.B1.A1">下载对象示例</a>
     * 
     * @param request 获取 COS 对象的请求 {@link GetObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void getObjectAsync(GetObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 下载 COS 对象到字节数组的同步方法<br>
     * 和{@link #getObject(GetObjectRequest)}类似，只是返回结果形式不同
     * </p>
     * <p>
     * 注意：请不要通过本接口下载大文件，否则容易造成内存溢出。
     * </p>
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象远端路径，即存储到 COS 上的绝对路径
     * @return 对象的字节数据
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    byte[] getObject(String bucketName, String objectName) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 简单上传的同步方法.&nbsp;
     * <p>
     * 简单上传主要适用于在单个请求中上传一个小于 5 GB 大小的对象.
     * 对于大于 5 GB 的对象(或者在高带宽或弱网络环境中）优先使用分片上传的方式 (<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112</a>).&nbsp;
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14113">https://cloud.tencent.com/document/product/436/14113.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7749">https://cloud.tencent.com/document/product/436/7749.</a><br>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E7.AE.80.E5.8D.95.E4.B8.8A.E4.BC.A0.E5.AF.B9.E8.B1.A1">简单上传对象示例</a>
     * 
     * @param request 简单上传请求 {@link PutObjectRequest}
     * @return 简单上传请求返回的结果 {@link PutObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PutObjectResult putObject(PutObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 简单上传的异步方法.&nbsp;
     * <p>
     * 简单上传主要适用于在单个请求中上传一个小于 5 GB 大小的对象.
     * 对于大于 5 GB 的对象(或者在高带宽或弱网络环境中）优先使用分片上传的方式 (<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112</a>).&nbsp;
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14113">https://cloud.tencent.com/document/product/436/14113.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7749">https://cloud.tencent.com/document/product/436/7749.</a><br>
     * 
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E7.AE.80.E5.8D.95.E4.B8.8A.E4.BC.A0.E5.AF.B9.E8.B1.A1">简单上传对象示例</a>
     * 
     * @param request 简单上传请求 {@link PutObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void putObjectAsync(PutObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 使用表单请求上传对象的同步方法.&nbsp;
     * <p>
     * 表单上传可以将本地不超过5GB的对象（Object）以网页表单（HTML Form）的形式上传至指定存储桶中.
     * 对于大于 5 GB 的对象(或者在高带宽或弱网络环境中）优先使用分片上传的方式 (<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112</a>).&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/14690">https://cloud.tencent.com/document/product/436/14690.</a><br>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E8.A1.A8.E5.8D.95.E4.B8.8A.E4.BC.A0.E5.AF.B9.E8.B1.A1">表单上传对象示例</a>
     *
     * @param request 简单上传请求 {@link PutObjectRequest}
     * @return 简单上传请求返回的结果 {@link PutObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    PostObjectResult postObject(PostObjectRequest request)throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 使用表单请求上传对象的异步方法.&nbsp;
     * <p>
     * 表单上传可以将本地不超过5GB的对象（Object）以网页表单（HTML Form）的形式上传至指定存储桶中.
     * 对于大于 5 GB 的对象(或者在高带宽或弱网络环境中）优先使用分片上传的方式 (<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112</a>).&nbsp;
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/14690">https://cloud.tencent.com/document/product/436/14690.</a><br>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E8.A1.A8.E5.8D.95.E4.B8.8A.E4.BC.A0.E5.AF.B9.E8.B1.A1">表单上传对象示例</a>
     *
     * @param request 简单上传请求 {@link PutObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void postObjectAsync(PostObjectRequest request, final  CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 删除 COS 上单个对象的同步方法.&nbsp;
     * <p>
     * COS 支持直接删除一个或多个对象，当仅需要删除一个对象时,只需要提供对象的名称（即对象键)即可.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14119">https://cloud.tencent.com/document/product/436/14119.</a>
     * <br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7743">https://cloud.tencent.com/document/product/436/7743.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E5.88.A0.E9.99.A4.E5.8D.95.E4.B8.AA.E5.AF.B9.E8.B1.A1">删除单个对象示例</a>
     *
     * @param request 删除 COS 上单个对象请求 {@link DeleteObjectRequest}
     * @return 删除 COS 上单个对象请求返回的结果 {@link DeleteObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    DeleteObjectResult deleteObject(DeleteObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 删除 COS 上单个对象的异步方法.&nbsp;
     * <p>
     * COS 支持直接删除一个或多个对象，当仅需要删除一个对象时,只需要提供对象的名称（即对象键)即可.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14119">https://cloud.tencent.com/document/product/436/14119.</a>
     * <br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7743">https://cloud.tencent.com/document/product/436/7743.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E5.88.A0.E9.99.A4.E5.8D.95.E4.B8.AA.E5.AF.B9.E8.B1.A1">删除单个对象示例</a>
     *
     * @param request 删除 COS 上单个对象请求 {@link DeleteObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void deleteObjectAsync(DeleteObjectRequest request, final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 获取 COS 对象的元数据信息(meta data)的同步方法.&nbsp;
     * <p>
     * 获取 COS 对象的元数据信息，需要与 Get 的权限一致.且请求是不返回消息体的.若请求中需要设置If-Modified-Since
     * 头部，则统一采用 GMT(RFC822) 时间格式，例如：Tue, 22 Oct 2017 01:35:21 GMT.如果对象不存在，则
     * 返回404.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7745">https://cloud.tencent.com/document/product/436/7745.</a>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E6.9F.A5.E8.AF.A2.E5.AF.B9.E8.B1.A1.E5.85.83.E6.95.B0.E6.8D.AE">查询对象元数据示例</a>
     *
     * @param request 获取 COS 对象的元数据信息请求 {@link HeadObjectRequest}
     * @return 获取 COS 对象的元数据信息请求返回的结果 {@link HeadObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    HeadObjectResult headObject(HeadObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 获取 COS 对象的元数据信息(meta data)的异步方法.&nbsp;
     * <p>
     * 获取 COS 对象的元数据信息，需要与 Get 的权限一致.且请求是不返回消息体的.若请求中需要设置If-Modified-Since
     * 头部，则统一采用 GMT(RFC822) 时间格式，例如：Tue, 22 Oct 2017 01:35:21 GMT.如果对象不存在，则
     * 返回404.
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7745">https://cloud.tencent.com/document/product/436/7745.</a>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E6.9F.A5.E8.AF.A2.E5.AF.B9.E8.B1.A1.E5.85.83.E6.95.B0.E6.8D.AE">查询对象元数据示例</a>
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/10881">https://cloud.tencent.com/document/product/436/10881.</a>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1.E5.A4.8D.E5.88.B6">简单对象复制示例</a>
     *
     * @param request 简单复制对象请求 {@link CopyObjectRequest}
     * @return 简单复制对象请求返回的结果 {@link CopyObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/10881">https://cloud.tencent.com/document/product/436/10881.</a>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E8.AE.BE.E7.BD.AE.E5.AF.B9.E8.B1.A1.E5.A4.8D.E5.88.B6">简单对象复制示例</a>
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8287">https://cloud.tencent.com/document/product/436/8287.</a>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E5.A4.8D.E5.88.B6.E5.88.86.E5.9D.97">分块复制示例</a>
     *
     * @param request 分块复制请求 {@link UploadPartCopyRequest}
     * @return 分块复制请求返回的结果 {@link UploadPartCopyResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
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
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/8287">https://cloud.tencent.com/document/product/436/8287.</a>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E5.A4.8D.E5.88.B6.E5.88.86.E5.9D.97">分块复制示例</a>
     *
     * @param request 分块复制请求 {@link UploadPartCopyRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void copyObjectAsync(UploadPartCopyRequest request,final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 初始化分块上传的同步方法.&nbsp;
     * <p>
     * 使用分块上传对象时，首先要进行初始化分片上传操作，获取对应分块上传的 uploadId，用于后续上传操
     * 作.分块上传适合于在弱网络或高带宽环境下上传较大的对象.SDK 支持自行切分对象并分别调用{@link #uploadPart(UploadPartRequest)}或者{@link #uploadPartAsync(UploadPartRequest, CosXmlResultListener)}上传各
     * 个分块.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112. &nbsp;</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7746">https://cloud.tencent.com/document/product/436/7746.</a><br>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22init_mulit_upload.22.3E-.E5.88.9D.E5.A7.8B.E5.8C.96.E5.88.86.E5.9D.97.E4.B8.8A.E4.BC.A0-.3C.2Fspan.3E">初始化分块上传示例</a>
     *
     * @param request 初始化分块上传请求 {@link InitMultipartUploadRequest}
     * @return 初始化分块上传请求返回的结果 {@link InitMultipartUploadResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    InitMultipartUploadResult initMultipartUpload(InitMultipartUploadRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 初始化分块上传的异步方法.&nbsp;
     * <p>
     * 使用分块上传对象时，首先要进行初始化分片上传操作，获取对应分块上传的 uploadId，用于后续上传操
     * 作.分块上传适合于在弱网络或高带宽环境下上传较大的对象.SDK 支持自行切分对象并分别调用{@link #uploadPart(UploadPartRequest)}或者{@link #uploadPartAsync(UploadPartRequest, CosXmlResultListener)}上传各
     * 个分块.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112. &nbsp;</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7746">https://cloud.tencent.com/document/product/436/7746.</a><br>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22init_mulit_upload.22.3E-.E5.88.9D.E5.A7.8B.E5.8C.96.E5.88.86.E5.9D.97.E4.B8.8A.E4.BC.A0-.3C.2Fspan.3E">初始化分块上传示例</a>
     *
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
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112,&nbsp;</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7747">https://cloud.tencent.com/document/product/436/7747.</a><br>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22list_mulit_upload.22.3E-.E6.9F.A5.E8.AF.A2.E5.B7.B2.E4.B8.8A.E4.BC.A0.E5.9D.97-.3C.2Fspan.3E">查询已上传块示例</a>
     *
     * @param request 查询特定分块上传中的已上传块请求 {@link ListPartsRequest}
     * @return 查询特定分块上传中的已上传块请求返回的结果 {@link ListPartsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    ListPartsResult listParts(ListPartsRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询特定分块上传中的已上传的块的异步方法.&nbsp;
     * <p>
     * COS 支持查询特定分块上传中的已上传的块, 即可以
     * 罗列出指定 UploadId 所属的所有已上传成功的分块. 因此，基于此可以完成续传功能.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112,&nbsp;</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7747">https://cloud.tencent.com/document/product/436/7747.</a><br>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22list_mulit_upload.22.3E-.E6.9F.A5.E8.AF.A2.E5.B7.B2.E4.B8.8A.E4.BC.A0.E5.9D.97-.3C.2Fspan.3E">查询已上传块示例</a>
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
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112. &nbsp;</a>
     * <br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7750">https://cloud.tencent.com/document/product/436/7750.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22mulit_upload_part.22.3E-.E4.B8.8A.E4.BC.A0.E5.88.86.E5.9D.97-.3C.2Fspan.3E">上传分块示例</a>
     *
     * @param request 上传一个对象某个分块请求 {@link UploadPartRequest}
     * @return 上传一个对象某个分块请求返回的结果 {@link UploadPartResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
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
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112. &nbsp;</a>
     * <br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7750">https://cloud.tencent.com/document/product/436/7750.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22mulit_upload_part.22.3E-.E4.B8.8A.E4.BC.A0.E5.88.86.E5.9D.97-.3C.2Fspan.3E">上传分块示例</a>
     *
     * @param request 上传某个分块请求 {@link UploadPartRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void uploadPartAsync(UploadPartRequest request, final CosXmlResultListener cosXmlResultListener);


    /**
     * <p>
     * 终止一个分块上传操作并删除已上传的块的同步方法.&nbsp;
     * <p>
     * COS 支持终止一个分块上传操作并删除已上传的块. 注意，已上传但是未终止的分片块会占用存储空间进
     * 而产生存储费用.因此，建议及时完成分块上传 或者终止分块上传.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112.</a>
     * <br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7740">https://cloud.tencent.com/document/product/436/7740.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22abort_mulit_upload.22.3E-.E7.BB.88.E6.AD.A2.E5.88.86.E5.9D.97.E4.B8.8A.E4.BC.A0-.3C.2Fspan.3E">终止分块上传示例</a>
     *
     * @param request 终止一个分块上传且删除已上传的分片块请求 {@link AbortMultiUploadRequest}
     * @return 终止一个分块上传且删除已上传的分片块请求返回的结果 {@link AbortMultiUploadResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    AbortMultiUploadResult abortMultiUpload(AbortMultiUploadRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 终止一个分块上传操作并删除已上传的块的异步方法.&nbsp;
     * <p>
     * COS 支持终止一个分块上传操作并删除已上传的块. 注意，已上传但是未终止的分片块会占用存储空间进
     * 而产生存储费用.因此，建议及时完成分块上传 或者终止分块上传.
     * <p>
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112.</a>
     * <br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7740">https://cloud.tencent.com/document/product/436/7740.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22abort_mulit_upload.22.3E-.E7.BB.88.E6.AD.A2.E5.88.86.E5.9D.97.E4.B8.8A.E4.BC.A0-.3C.2Fspan.3E">终止分块上传示例</a>
     *
     * @param request 终止一个分块上传且删除已上传的分片块请求 {@link AbortMultiUploadRequest}
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
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7742">https://cloud.tencent.com/document/product/436/7742.</a><br>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22complete_mulit_upload.22.3E-.E5.AE.8C.E6.88.90.E5.88.86.E5.9D.97.E4.B8.8A.E4.BC.A0-.3C.2Fspan.3E">完成分块上传示例</a>
     *
     * @param request 完成整个分块上传请求 {@link CompleteMultiUploadRequest}
     * @return 完成整个分块上传请求返回的结果 {@link CompleteMultiUploadResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
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
     * 功能描述：<a href="https://cloud.tencent.com/document/product/436/14112">https://cloud.tencent.com/document/product/436/14112.</a><br>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7742">https://cloud.tencent.com/document/product/436/7742.</a><br>
     *
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.3Cspan-id-.3D-.22complete_mulit_upload.22.3E-.E5.AE.8C.E6.88.90.E5.88.86.E5.9D.97.E4.B8.8A.E4.BC.A0-.3C.2Fspan.3E">完成分块上传示例</a>
     *
     * @param request 初始化分片请求 {@link CompleteMultiUploadRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void completeMultiUploadAsync(CompleteMultiUploadRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 查询存储桶（Bucket）中正在进行中的分块上传对象的同步方法.&nbsp;
     * <p>
     * COS 支持查询 Bucket 中有哪些正在进行中的分块上传对象，单次请求操作最多列出 1000 个正在进行中的
     * 分块上传对象.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7736">
     * https://cloud.tencent.com/document/product/436/7736.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E6.9F.A5.E8.AF.A2.E5.88.86.E5.9D.97.E4.B8.8A.E4.BC.A0">查询分块上传示例</a>
     *
     * @param request 查询 Bucket 中正在进行中的分块上传对象请求 {@link ListMultiUploadsRequest}
     * @return 查询 Bucket 中正在进行中的分块上传对象请求返回的结果 {@link ListMultiUploadsResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    ListMultiUploadsResult listMultiUploads(ListMultiUploadsRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 查询存储桶（Bucket）中正在进行中的分块上传对象的异步方法.&nbsp;
     * <p>
     * COS 支持查询 Bucket 中有哪些正在进行中的分块上传对象，单次请求操作最多列出 1000 个正在进行中的
     * 分块上传对象.
     * <p>
     * API 接口：<a href="https://cloud.tencent.com/document/product/436/7736">https://cloud.tencent.com/document/product/436/7736.</a>
     * <br>
     * <p>
     * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/34536#.E6.9F.A5.E8.AF.A2.E5.88.86.E5.9D.97.E4.B8.8A.E4.BC.A0">查询分块上传示例</a>
     *
     * @param request 查询 Bucket 中正在进行中的分块上传对象请求 {@link ListMultiUploadsRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void listMultiUploadsAsync(ListMultiUploadsRequest request, CosXmlResultListener cosXmlResultListener);
    

    /**
     * <p>
     * 预连接的同步方法。
     * </p>
     *
     * @param bucket bucket 名称, 如 test-1250000000
     * @return 预连接是否成功
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    boolean preBuildConnection(String bucket) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 预连接的异步方法。
     * </p>
     *
     * @param bucket bucket 名称, 如 test-1250000000
     * @param listener 结果回调函数
     */
    void preBuildConnectionAsync(String bucket, CosXmlResultSimpleListener listener);

    /**
     * 获取对象的 url 地址
     *
     * @return 对象的 url 地址
     */
    String getObjectUrl(String bucket, String region, String key);

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
