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

import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.io.File;
import java.util.Map;

/**
 * 下载 COS 对象的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#getObject(GetObjectRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#getObjectAsync(GetObjectRequest, CosXmlResultListener)
 */
public class GetObjectRequest extends ObjectRequest {

    public String versionId;
    public long fileOffset = 0L;

    public CosXmlProgressListener progressListener;
    public String savePath;
    public String saveFileName;

    public Uri fileContentUri;

    /**
     * GetObjectRequest 构造函数
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param savePath 文件下载到本地文件夹的绝对路径
     */
    public GetObjectRequest(String bucket, String cosPath, String savePath){
        super(bucket, cosPath);
        this.savePath = savePath;
    }

    /**
     * GetObjectRequest 构造函数
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param savePath 文件下载到本地文件夹的绝对路径
     * @param saveFileName 保存到本地的文件名
     */
    public GetObjectRequest(String bucket, String cosPath, String savePath, String saveFileName){
        super(bucket, cosPath);
        this.savePath = savePath;
        this.saveFileName = saveFileName;
    }

    /**
     * GetObjectRequest 构造函数
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param fileContentUri 本地文件 Uri
     */
    public GetObjectRequest(String bucket, String cosPath, Uri fileContentUri) {
        super(bucket, cosPath);
        this.fileContentUri = fileContentUri;
    }

    public String getDownloadPath(){
        String path  = null;
        if(savePath != null){
            if(!savePath.endsWith("/")){
                path = savePath + "/";
            }else{
                path = savePath;
            }
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            if (saveFileName != null){
                path = path + saveFileName;
                return path;
            }
            if(cosPath != null){
                int separator = cosPath.lastIndexOf("/");
                if(separator >= 0){
                    path = path + cosPath.substring(separator + 1);
                }else{
                    path = path + cosPath;
                }
            }
        }
        return path;
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public Map<String, String> getQueryString() {
        if(versionId != null){
            queryParameters.put("versionId",versionId);
        }
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }
}
