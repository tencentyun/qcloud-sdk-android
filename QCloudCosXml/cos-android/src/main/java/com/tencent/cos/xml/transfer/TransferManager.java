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

package com.tencent.cos.xml.transfer;


import android.content.Context;
import android.net.Uri;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.model.object.CopyObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.tag.UrlUploadPolicy;

import java.io.InputStream;
import java.net.URL;

/**
 * 传输管理器
 */

public class TransferManager{

    protected CosXmlSimpleService cosXmlService;
    protected TransferConfig transferConfig;

    /**
     * TransferManager 构造器，
     *
     * @param cosXmlService CosXmlSimpleService 对象，用于真正发起传输请求
     * @param transferConfig 传输配置类
     */
    public TransferManager(CosXmlSimpleService cosXmlService, TransferConfig transferConfig){
        if(cosXmlService == null){
            throw new IllegalArgumentException("CosXmlService is null");
        }
        if(transferConfig == null){
            throw new IllegalArgumentException("TransferConfig is null");
        }
        this.cosXmlService = cosXmlService;
        this.transferConfig = transferConfig;
    }
    

    /**
     * 通过初始化 {@link PutObjectRequest} 来上传文件
     *
     * <p>
     * 支持给 pubObjectRequest 设置 Header，后续所有的上传相关的请求都会带上对应的 Header
     *
     * @param putObjectRequest 上传请求Request封装类
     * @param uploadId 是否分片续传的uploadId
     * @return COSXMLUploadTask 上传任务
     */
    public COSXMLUploadTask upload(PutObjectRequest putObjectRequest, String uploadId){
        COSXMLUploadTask cosxmlUploadTask = new COSXMLUploadTask(cosXmlService, putObjectRequest, uploadId);
        cosxmlUploadTask.multiUploadSizeDivision = transferConfig.divisionForUpload; // 分片上传的界限
        cosxmlUploadTask.sliceSize = transferConfig.sliceSizeForUpload; // 分片上传的分片大小
        cosxmlUploadTask.forceSimpleUpload = transferConfig.isForceSimpleUpload();
        cosxmlUploadTask.upload();
        return cosxmlUploadTask;
    }


    /**
     * 通过本地文件绝对路径来上传文件
     *
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param srcPath 文件本地路径
     * @param uploadId 是否分片续传的uploadId，如果为空，则从头开始上传文件
     *
     * @return COSXMLUploadTask 上传任务
     */
    public COSXMLUploadTask upload(String bucket, String cosPath, String srcPath, String uploadId){
        COSXMLUploadTask cosxmlUploadTask = new COSXMLUploadTask(cosXmlService, null, bucket, cosPath, srcPath, uploadId);
        cosxmlUploadTask.multiUploadSizeDivision = transferConfig.divisionForUpload; // 分片上传的界限
        cosxmlUploadTask.sliceSize = transferConfig.sliceSizeForUpload; // 分片上传的分片大小
        cosxmlUploadTask.forceSimpleUpload = transferConfig.isForceSimpleUpload();
        cosxmlUploadTask.upload();
        return cosxmlUploadTask;
    }

    /**
     * 通过本地文件 Uri 路径来上传文件
     *
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param uri 文件本地 Uri 路径
     * @param uploadId 是否分片续传的uploadId，如果为空，则从头开始上传文件
     *
     * @return COSXMLUploadTask 上传任务
     */
    public COSXMLUploadTask upload(String bucket, String cosPath, Uri uri, String uploadId) {

        COSXMLUploadTask cosxmlUploadTask = new COSXMLUploadTask(cosXmlService, null, bucket, cosPath, uri, uploadId);
        cosxmlUploadTask.multiUploadSizeDivision = transferConfig.divisionForUpload; // 分片上传的界限
        cosxmlUploadTask.sliceSize = transferConfig.sliceSizeForUpload; // 分片上传的分片大小
        cosxmlUploadTask.forceSimpleUpload = transferConfig.isForceSimpleUpload();
        cosxmlUploadTask.upload();
        return cosxmlUploadTask;

    }

    /**
     * 通过远程 URL 来上传文件
     *
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param url 远程 URL
     * @param uploadId 是否分片续传的uploadId，如果为空，则从头开始上传文件
     *
     * @return COSXMLUploadTask 上传任务
     */
    public COSXMLUploadTask upload(String bucket, String cosPath, URL url, String uploadId) {

        COSXMLUploadTask cosxmlUploadTask = new COSXMLUploadTask(cosXmlService, null, bucket, cosPath, url, uploadId);
        cosxmlUploadTask.multiUploadSizeDivision = transferConfig.divisionForUpload; // 分片上传的界限
        cosxmlUploadTask.sliceSize = transferConfig.sliceSizeForUpload; // 分片上传的分片大小
        cosxmlUploadTask.forceSimpleUpload = transferConfig.isForceSimpleUpload();
        cosxmlUploadTask.upload();
        return cosxmlUploadTask;
    }

    /**
     * 通过远程 URL 来上传文件
     *
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param url 远程 URL
     * @param urlUploadPolicy url上传策略
     * @param uploadId 是否分片续传的uploadId，如果为空，则从头开始上传文件
     *
     * @return COSXMLUploadTask 上传任务
     */
    public COSXMLUploadTask upload(String bucket, String cosPath, URL url, UrlUploadPolicy urlUploadPolicy, String uploadId) {

        COSXMLUploadTask cosxmlUploadTask = new COSXMLUploadTask(cosXmlService, null, bucket, cosPath, url, urlUploadPolicy, uploadId);
        cosxmlUploadTask.multiUploadSizeDivision = transferConfig.divisionForUpload; // 分片上传的界限
        cosxmlUploadTask.sliceSize = transferConfig.sliceSizeForUpload; // 分片上传的分片大小
        cosxmlUploadTask.forceSimpleUpload = transferConfig.isForceSimpleUpload();
        cosxmlUploadTask.upload();
        return cosxmlUploadTask;
    }

    /**
     * 上传字节数组到 COS，仅仅支持简单上传
     *
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param bytes 字节数组
     *
     * @return COSXMLUploadTask 上传任务
     */
    public COSXMLUploadTask upload(String bucket, String cosPath, byte[] bytes){
        COSXMLUploadTask cosxmlUploadTask = new COSXMLUploadTask(cosXmlService, null, bucket, cosPath, bytes);
        cosxmlUploadTask.multiUploadSizeDivision = transferConfig.divisionForUpload; // 分片上传的界限
        cosxmlUploadTask.sliceSize = transferConfig.sliceSizeForUpload; // 分片上传的分片大小
        cosxmlUploadTask.forceSimpleUpload = transferConfig.isForceSimpleUpload();
        cosxmlUploadTask.upload();
        return cosxmlUploadTask;
    }

    /**
     * 通过流上传到 COS，仅仅支持简单上传。
     *
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param inputStream 输入流
     *
     * @return COSXMLUploadTask 上传任务
     */
    public COSXMLUploadTask upload(String bucket, String cosPath, InputStream inputStream){
        COSXMLUploadTask cosxmlUploadTask = new COSXMLUploadTask(cosXmlService, null, bucket, cosPath, inputStream);
        cosxmlUploadTask.multiUploadSizeDivision = transferConfig.divisionForUpload; // 分片上传的界限
        cosxmlUploadTask.sliceSize = transferConfig.sliceSizeForUpload; // 分片上传的分片大小
        cosxmlUploadTask.forceSimpleUpload = transferConfig.isForceSimpleUpload();
        cosxmlUploadTask.upload();
        return cosxmlUploadTask;
    }



    /**
     * 通过本地文件绝对路径上传文件，并自行负责签名串的生成
     *
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param srcPath 文件本地路径
     * @param uploadId 是否分片续传的uploadId
     * @param onSignatureListener 签名注册器
     * @return COSXMLUploadTask 上传任务
     */
    public COSXMLUploadTask upload(String bucket, String cosPath, String srcPath, String uploadId, COSXMLTask.OnSignatureListener onSignatureListener) {
        COSXMLUploadTask cosxmlUploadTask = new COSXMLUploadTask(cosXmlService, null, bucket, cosPath, srcPath, uploadId);
        cosxmlUploadTask.multiUploadSizeDivision = transferConfig.divisionForUpload; // 分片上传的界限
        cosxmlUploadTask.sliceSize = transferConfig.sliceSizeForUpload; // 分片上传的分片大小
        cosxmlUploadTask.setOnSignatureListener(onSignatureListener);
        cosxmlUploadTask.forceSimpleUpload = transferConfig.isForceSimpleUpload();
        cosxmlUploadTask.upload();
        return cosxmlUploadTask;
    }

    /**
     * 下载文件
     * @param context app上下文
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param savedDirPath 文件下载到本地的路径
     * @return COSXMLDownloadTask 下载任务
     */
    public COSXMLDownloadTask download(Context context, String bucket, String cosPath, String savedDirPath){
        return download(context, bucket, cosPath, savedDirPath, null);
    }

    /**
     * 指定下载到本地的文件名来下载文件
     * @param context app上下文
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param savedDirPath 文件下载到本地的路径
     * @param savedFileName 文件下载本地的别名
     * @return COSXMLDownloadTask 下载任务
     */
    public COSXMLDownloadTask download(Context context, String bucket, String cosPath, String savedDirPath, String savedFileName){
        COSXMLDownloadTask cosxmlDownloadTask = new COSXMLDownloadTask(context, cosXmlService, null, bucket, cosPath, savedDirPath, savedFileName);
        cosxmlDownloadTask.download();
        return cosxmlDownloadTask;
    }

    /**
     * 通过初始化 {@link GetObjectRequest} 请求来下载文件
     * @param context app上下文
     * @param getObjectRequest 下载请求Request封装类
     * @return COSXMLDownloadTask 下载任务
     */
    public COSXMLDownloadTask download(Context context, GetObjectRequest getObjectRequest){
        COSXMLDownloadTask cosxmlDownloadTask = new COSXMLDownloadTask(context, cosXmlService, getObjectRequest);
        cosxmlDownloadTask.download();
        return cosxmlDownloadTask;
    }

    /**
     * 下载文件，并自行负责签名串的生成
     * @param context app上下文
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param savedDirPath 文件下载到本地的路径
     * @param savedFileName 文件下载本地的别名
     * @param onSignatureListener 签名注册器
     * @return COSXMLDownloadTask 下载任务
     */
    public COSXMLDownloadTask download(Context context, String bucket, String cosPath, String savedDirPath, String savedFileName, COSXMLTask.OnSignatureListener onSignatureListener){
        COSXMLDownloadTask cosxmlDownloadTask = new COSXMLDownloadTask(context, cosXmlService, null, bucket, cosPath, savedDirPath, savedFileName);
        cosxmlDownloadTask.setOnSignatureListener(onSignatureListener);
        cosxmlDownloadTask.download();
        return cosxmlDownloadTask;
    }

    /**
     * 复制文件
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param copySourceStruct 源文件存储于COS的位置
     * @return COSXMLCopyTask
     */
    public COSXMLCopyTask copy(String bucket, String cosPath, CopyObjectRequest.CopySourceStruct copySourceStruct){
        COSXMLCopyTask cosxmlCopyTask = new COSXMLCopyTask(cosXmlService, null, bucket, cosPath, copySourceStruct);
        cosxmlCopyTask.multiCopySizeDivision = transferConfig.divisionForCopy;
        cosxmlCopyTask.sliceSize = transferConfig.sliceSizeForCopy;
        cosxmlCopyTask.copy();
        return cosxmlCopyTask;
    }

    /**
     * 通过初始化 {@link CopyObjectRequest} 来复制文件
     *
     * @param copyObjectRequest 复制请求Request封装类
     * @return COSXMLCopyTask
     */
    public COSXMLCopyTask copy(CopyObjectRequest copyObjectRequest){
        COSXMLCopyTask cosxmlCopyTask = new COSXMLCopyTask(cosXmlService, copyObjectRequest);
        cosxmlCopyTask.multiCopySizeDivision = transferConfig.divisionForCopy;
        cosxmlCopyTask.sliceSize = transferConfig.sliceSizeForCopy;
        cosxmlCopyTask.copy();
        return cosxmlCopyTask;
    }

    /**
     * 复制文件，并自行负责签名串的生成
     * @param bucket 存储桶
     * @param cosPath 文件存放于存储桶上的位置
     * @param copySourceStruct 源文件存储于COS的位置
     * @return COSXMLCopyTask
     */
    public COSXMLCopyTask copy(String bucket, String cosPath, CopyObjectRequest.CopySourceStruct copySourceStruct, COSXMLTask.OnSignatureListener onSignatureListener){
        COSXMLCopyTask cosxmlCopyTask = new COSXMLCopyTask(cosXmlService, null, bucket, cosPath, copySourceStruct);
        cosxmlCopyTask.multiCopySizeDivision = transferConfig.divisionForCopy;
        cosxmlCopyTask.sliceSize = transferConfig.sliceSizeForCopy;
        cosxmlCopyTask.setOnSignatureListener(onSignatureListener);
        cosxmlCopyTask.copy();
        return cosxmlCopyTask;
    }

    /**
     * 获取对应的 {@link CosXmlSimpleService} 对象
     *
     * @return 对应的 {@link CosXmlSimpleService} 对象
     */
    public CosXmlSimpleService getCosXmlService() {
        return cosXmlService;
    }
}
