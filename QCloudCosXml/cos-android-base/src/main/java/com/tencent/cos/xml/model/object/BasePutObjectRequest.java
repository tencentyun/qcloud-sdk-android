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

import android.content.Context;
import android.net.Uri;

import com.tencent.cos.xml.BaseCosXml;
import com.tencent.cos.xml.CosXmlBaseService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.UrlUploadPolicy;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.task.QCloudTask;
import com.tencent.qcloud.core.util.ContextHolder;
import com.tencent.qcloud.core.util.QCloudUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * 简单上传的请求.
 * @see BaseCosXml#basePutObject(BasePutObjectRequest)
 * @see BaseCosXml#basePutObjectAsync(BasePutObjectRequest, CosXmlResultListener)
 */
public class BasePutObjectRequest extends UploadRequest implements TransferRequest {
    protected String srcPath;
    protected byte[] data;
    protected InputStream inputStream;
    protected String strData;
    protected URL url;
    protected UrlUploadPolicy urlUploadPolicy;
    protected long fileLength;
    protected Uri uri;
    protected CosXmlProgressListener progressListener;

    protected BasePutObjectRequest(String bucket, String cosPath){
        super(bucket, cosPath);
        setNeedMD5(true);
    }

    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param srcPath 本地文件的绝对路径
     */
    public BasePutObjectRequest(String bucket, String cosPath, String srcPath){
        this(bucket, cosPath);
        this.srcPath = srcPath;
    }

    public BasePutObjectRequest(String bucket, String cosPath, Uri uri){
        this(bucket, cosPath);
        this.uri = uri;
    }

    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param data 上传的数据
     */
    public BasePutObjectRequest(String bucket, String cosPath, byte[] data){
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
    public BasePutObjectRequest(String bucket, String cosPath, StringBuilder stringBuilder) {
        this(bucket, cosPath);
        strData = stringBuilder.toString();
    }

    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param inputStream 上传的数据流
     */
    public BasePutObjectRequest(String bucket, String cosPath, InputStream inputStream){
        this(bucket, cosPath);
        this.inputStream = inputStream;
    }


    /**
     * PutObject 构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param url 上传的url
     */
    public BasePutObjectRequest(String bucket, String cosPath, URL url) {
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
            return RequestBodySerializer.bytes(getContentType(), data);
        } else if(inputStream != null){
            return RequestBodySerializer.stream(getContentType(), new File(CosXmlBaseService.appCachePath, String.valueOf(System.currentTimeMillis())),
                    inputStream);
        } else if (strData != null) {
            return RequestBodySerializer.bytes(getContentType(), strData.getBytes());
        } else if (url != null) {
            return RequestBodySerializer.url(getContentType(), url);
        } else if (uri != null && ContextHolder.getAppContext() != null) {
            return RequestBodySerializer.uri(getContentType(), uri, ContextHolder.getAppContext());
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

        if (uri != null) {
            Context appContext = ContextHolder.getAppContext();
            if (appContext != null && !QCloudUtils.doesUriFileExist(uri, appContext.getContentResolver())) {
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

    @Override
    public void setTrafficLimit(long limit) {
        addHeader("x-cos-traffic-limit", String.valueOf(limit));
    }

    /**
     * 设置URL上传策略
     * @param urlUploadPolicy url上传策略
     */
    public void setUrlUploadPolicy(UrlUploadPolicy urlUploadPolicy) {
        this.urlUploadPolicy = urlUploadPolicy;
    }
}