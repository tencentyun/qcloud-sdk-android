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

package com.tencent.cos.xml.model.object;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.UrlUploadPolicy;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.task.QCloudTask;
import com.tencent.qcloud.core.util.ContextHolder;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * 简单上传的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#putObject(PutObjectRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#putObjectAsync(PutObjectRequest, CosXmlResultListener)
 */
public class PutObjectRequest extends UploadRequest implements TransferRequest {
    private String srcPath;
    private byte[] data;
    private InputStream inputStream;
    private String strData;
    private URL url;
    private UrlUploadPolicy urlUploadPolicy;
    private long fileLength;
    private Uri uri;
    private CosXmlProgressListener progressListener;

    private PutObjectRequest(String bucket, String cosPath){
        super(bucket, cosPath);
        setNeedMD5(true);
    }

    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param srcPath 本地文件的绝对路径
     */
    public PutObjectRequest(String bucket, String cosPath, String srcPath){
        this(bucket, cosPath);
        this.srcPath = srcPath;
    }

    public PutObjectRequest(String bucket, String cosPath, Uri uri){
        this(bucket, cosPath);
        this.uri = uri;
    }

    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param data 上传的数据
     */
    public PutObjectRequest(String bucket, String cosPath, byte[] data){
        this(bucket, cosPath);
        this.data = data;
    }

    /**
     * PutObject 构造方法
     *
     * @param bucket 存储桶名称
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param stringBuilder 上传的字符串
     */
    public PutObjectRequest(String bucket, String cosPath, StringBuilder stringBuilder) {
        this(bucket, cosPath);
        strData = stringBuilder.toString();
    }

    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param inputStream 上传的数据流
     */
    public PutObjectRequest(String bucket, String cosPath, InputStream inputStream){
        this(bucket, cosPath);
        this.inputStream = inputStream;
    }


    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param url 上传的url
     */
    public PutObjectRequest(String bucket, String cosPath, URL url) {
        this(bucket, cosPath);
        this.url = url;
        setNeedMD5(false);
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    /**
     *
     */
    public void setPriorityLow() {
        this.priority = QCloudTask.PRIORITY_LOW;
    }

    public boolean isPriorityLow() {
        return this.priority == QCloudTask.PRIORITY_LOW;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        if(srcPath != null){
            return RequestBodySerializer.file(getContentType(), new File(srcPath));
        } else if(data != null){
            return RequestBodySerializer.bytes(null, data);
        } else if(inputStream != null){
            return RequestBodySerializer.stream(null, new File(CosXmlSimpleService.appCachePath, String.valueOf(System.currentTimeMillis())),
                    inputStream);
        } else if (strData != null) {
            return RequestBodySerializer.bytes(null, strData.getBytes());
        } else if (url != null) {
            return RequestBodySerializer.url(null, url);
        } else if (uri != null && ContextHolder.getAppContext() != null) {
            return RequestBodySerializer.uri(null, uri, ContextHolder.getAppContext());
        }

        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(srcPath == null && data == null && inputStream == null && strData == null && uri == null && url == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "Data Source must not be null");
        }
        if(srcPath != null){
            File file = new File(srcPath);
            if(!file.exists()){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "upload file does not exist");
            }
        }
    }


    /**
     * 上传进度回调
     * @param progressListener 进度监听器 {@link CosXmlProgressListener}
     */
    public void setProgressListener(CosXmlProgressListener progressListener){
        this.progressListener = progressListener;
    }

    public CosXmlProgressListener getProgressListener(){
        return progressListener;
    }

    /**
     * <p>
     * 设置上传的本地文件路径.<br>
     * 可以设置上传本地文件、字节数组或者输入流。每次只能上传一种类型，若同时设置，
     * 则优先级为 本地文件&gt;字节数组&gt;输入流
     * </p>
     * @param srcPath 本地文件路径
     */
    public void setSrcPath(String srcPath){
        this.srcPath = srcPath;
    }

    /**
     * 获取设置的本地文件路径
     * @return String
     */
    public String getSrcPath(){
       return srcPath;
    }

    /**
     * <p>
     * 设置上传的字节数组.<br>
     * 可以设置上传本地文件、字节数组或者输入流。每次只能上传一种类型，若同时设置，
     * 则优先级为 本地文件&gt;字节数组&gt;输入流
     * </p>
     * @param data 需要上传的字节数组
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public String getStrData() {
        return strData;
    }

    public URL getUrl() {
        return url;
    }

    public UrlUploadPolicy getUrlUploadPolicy() {
        return urlUploadPolicy;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 获取用户设置的字节数组
     * @return byte[]
     */
    public byte[] getData() {
        return data;
    }


    /**
     * 获取待上传数据的总长度
     * @return long
     */
    public long getFileLength() {
        if(srcPath != null){
            fileLength = new File(srcPath).length();
        }else if(data != null){
            fileLength =  data.length;
        } else if (strData != null) {
            fileLength = strData.getBytes().length;
        }
        return fileLength;
    }

    /**
     * <p>
     * 设置Cache-Control头部
     * </p>
     * @param cacheControl Cache-Control头部
     */
    public void setCacheControl(String cacheControl) {
        if(cacheControl == null)return;
        addHeader(COSRequestHeaderKey.CACHE_CONTROL,cacheControl);
    }

    /**
     * <p>
     * 设置Content-Disposition头部部
     * </p>
     * @param contentDisposition Content-Disposition头部
     */
    public void setContentDisposition(String contentDisposition) {
        if(contentDisposition == null)return;
       addHeader(COSRequestHeaderKey.CONTENT_DISPOSITION,contentDisposition);
    }

    /**
     * <p>
     * 设置Content-Encoding头部
     * </p>
     * @param contentEncoding Content-Encoding头部
     */
    public void setContentEncodeing(String contentEncoding) {
        if(contentEncoding == null)return;
        addHeader(COSRequestHeaderKey.CONTENT_ENCODING,contentEncoding);
    }

    /**
     * <p>
     * 设置Expires头部
     * </p>
     * @param expires Expires头部
     */
    public void setExpires(String expires) {
        if(expires == null)return;
        addHeader(COSRequestHeaderKey.EXPIRES,expires);
    }

    /**
     * 设置用户自定义头部信息
     * @param key 自定义头部信息的key值，需要以 x-cos-meta- 开头
     * @param value 自定义头部信息的value值。
     */
    public void setXCOSMeta(String key, String value){
        if(key != null && value != null){
            addHeader(key,value);
        }
    }

    /**
     * 定义对象的访问控制列表（ACL）属性。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/30752#.E9.A2.84.E8.AE.BE.E7.9A.84-acl">ACL 概述</a> 文档中对象的预设 ACL 部分，例如 default，private，public-read 等，默认为 default
     * @param cosacl COS 访问权限
     */
    public void setXCOSACL(COSACL cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl.getAcl());
        }
    }

    /**
     * 同{@link #setXCOSACL(COSACL)}
     * @param cosacl COS 访问权限
     */
    public void setXCOSACL(String cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl);
        }
    }

    /**
     * <p>
     * 赋予被授权者读权限
     * </p>
     * @param aclAccount 读权限用户列表 {@link ACLAccount}
     */
    public void setXCOSGrantRead(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_READ, aclAccount.getAccount());
        }
    }


    /**
     * 赋予被授权者操作对象的读取权限
     * @param aclAccount ACL授权账号列表
     */
    public void setXCOSGrantWrite(ACLAccount aclAccount){

        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_WRITE, aclAccount.getAccount());
        }
    }

    /**
     * 赋予被授权者操作对象的写入权限
     * @param aclAccount ACL授权账号列表
     */
    public void setXCOSReadWrite(ACLAccount aclAccount){

        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_FULL_CONTROL, aclAccount.getAccount());
        }
    }

    /**
     * 设置对象的存储类型。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/33417">存储类型</a> 文档，例如 STANDARD_IA，ARCHIVE。默认值：STANDARD
     * @param stroageClass COS存储类型
     */
    public void setStroageClass(COSStorageClass stroageClass)
    {
        addHeader(COSRequestHeaderKey.X_COS_STORAGE_CLASS_, stroageClass.getStorageClass());
    }

    @Override
    public void setTrafficLimit(long limit) {
        addHeader("x-cos-traffic-limit", String.valueOf(limit));
    }

    /**
     * 设置启动盲水印参数
     * @param operations 盲水印参数
     */
    public void setPicOperations(@NonNull PicOperations operations) {
        addHeader("Pic-Operations", operations.toJsonStr());
    }

    /**
     * 设置URL上传策略
     * @param urlUploadPolicy url上传策略
     */
    public void setUrlUploadPolicy(UrlUploadPolicy urlUploadPolicy) {
        this.urlUploadPolicy = urlUploadPolicy;
    }
}
