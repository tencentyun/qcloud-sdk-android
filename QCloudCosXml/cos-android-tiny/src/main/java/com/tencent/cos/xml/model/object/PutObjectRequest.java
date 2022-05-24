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

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.util.ContextHolder;
import com.tencent.qcloud.core.util.QCloudUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * 简单上传的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#putObject(PutObjectRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#putObjectAsync(PutObjectRequest, CosXmlResultListener)
 */
public class PutObjectRequest extends ObjectRequest {
    public String srcPath;
    public byte[] data;
    public InputStream inputStream;
    public String strData;
    public URL url;
    public Uri uri;
    public CosXmlProgressListener progressListener;


    protected PutObjectRequest(String bucket, String cosPath){
        super(bucket, cosPath);
        isNeedMD5 = true;
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
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param inputStream 上传的数据流
     */
    public PutObjectRequest(String bucket, String cosPath, InputStream inputStream){
        this(bucket, cosPath);
        this.inputStream = inputStream;
    }


    @Override
    public String getMethod() {
        return "PUT";
    }


    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        if(srcPath != null){
            return RequestBodySerializer.file(getContentType(), new File(srcPath));
        } else if(data != null){
            return RequestBodySerializer.bytes(getContentType(), data);
        } else if(inputStream != null){
            return RequestBodySerializer.stream(getContentType(), new File(CosXmlSimpleService.appCachePath, String.valueOf(System.currentTimeMillis())),
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
        if (srcPath == null && data == null && inputStream == null && strData == null && uri == null && url == null) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT, "Data Source must not be null");
        }
        if (srcPath != null) {
            File file = new File(srcPath);
            if (!file.exists()) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT, "upload file does not exist");
            }
        }

        if (uri != null) {
            Context appContext = ContextHolder.getAppContext();
            if (appContext != null && !QCloudUtils.doesUriFileExist(uri, appContext.getContentResolver())) {
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT, "upload file does not exist");
            }
        }

    }
}
