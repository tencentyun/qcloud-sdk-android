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
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.BasePutObjectRequest;
import com.tencent.cos.xml.model.object.BasePutObjectResult;
import com.tencent.cos.xml.model.object.GetObjectBytesRequest;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;

/**
 * <p>
 * 提供用于访问Tencent Cloud COS服务的基础接口。
 *
 * <p>
 * 对象存储（Cloud Object Storage，COS）是腾讯云提供的一种存储海量文件的分布式存储服务，用户可通过网络随时存储和查看数据。腾讯云 COS 使所有用户都能使用具备高扩展性、低成本、可靠和安全的数据存储服务。
 *
 * <p>
 * SimpleCosXml为每个接口都提供了同步和异步操作，如同步接口为{@link #basePutObject(BasePutObjectRequest)}，
 * 则对应的异步接口为{@link #basePutObjectAsync(BasePutObjectRequest, CosXmlResultListener)}.<br><br>
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

public interface BaseCosXml {
    /**
     * 通用接口同步方法
     */
    <T1 extends CosXmlRequest, T2 extends CosXmlResult> T2 commonInterface(T1 request, Class<T2> resultClass) throws CosXmlClientException, CosXmlServiceException;

    /**
     * 通用接口异步方法
     */
    <T1 extends CosXmlRequest, T2 extends CosXmlResult> void commonInterfaceAsync(T1 request, Class<T2> resultClass, final CosXmlResultListener cosXmlResultListener);

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
     * 下载 COS 对象到字节数组的同步方法<br>
     * 和{@link #getObject(GetObjectRequest)}类似，只是返回结果形式不同
     * </p>
     * <p>
     * 注意：请不要通过本接口下载大文件，否则容易造成内存溢出。
     * </p>
     *
     * @param request 获取 COS 对象的请求 {@link GetObjectBytesRequest}
     * @return 对象的字节数据
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    byte[] getObject(GetObjectBytesRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 基础简单上传的同步方法.&nbsp;
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
     * @param request 简单上传请求 {@link BasePutObjectRequest}
     * @return 简单上传请求返回的结果 {@link BasePutObjectResult}
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    BasePutObjectResult basePutObject(BasePutObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    /**
     * <p>
     * 基础简单上传的异步方法.&nbsp;
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
     * @param request 简单上传请求 {@link BasePutObjectRequest}
     * @param cosXmlResultListener 请求回调结果 {@link CosXmlResultListener}
     */
    void basePutObjectAsync(BasePutObjectRequest request, final CosXmlResultListener cosXmlResultListener);

    /**
     * <p>
     * 上传一个分片块的同步方法.&nbsp;
     * <p>
     * 使用分块上传时，可将对象切分成一个个分块的方式上传到 COS，每个分块上传需要携带分块号（partNumber） 和 uploadId（{SimpleCosXml#initMultipartUpload(InitMultipartUploadRequest)}），
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
     * 使用分块上传时，可将对象切分成一个个分块的方式上传到 COS，每个分块上传需要携带分块号（partNumber） 和 uploadId（{SimpleCosXml#initMultipartUpload(InitMultipartUploadRequest)}），
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
     * 获取对象的 url 地址
     *
     * @return 对象的 url 地址
     */
    String getObjectUrl(String bucket, String region, String key);

    /**
     * 立即取消请求任务
     * @param cosXmlRequest 请求request {@link CosXmlRequest}
     */
    void cancel(CosXmlRequest cosXmlRequest, boolean now);

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
