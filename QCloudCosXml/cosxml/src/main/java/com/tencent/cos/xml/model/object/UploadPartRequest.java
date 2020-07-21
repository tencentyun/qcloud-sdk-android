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

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.util.ContextHolder;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * 上传一个分片块的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#uploadPart(UploadPartRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#uploadPartAsync(UploadPartRequest, CosXmlResultListener)
 */
final public class UploadPartRequest extends BaseMultipartUploadRequest implements TransferRequest {
    private int partNumber;
    private String uploadId;
    private Uri uri;
    private String srcPath;
    private byte[] data;
    private InputStream inputStream;
    private long fileOffset = -1L;
    private long fileContentLength = -1L;

    private CosXmlProgressListener progressListener;

    private UploadPartRequest(String bucket, String cosPath){
        super(bucket, cosPath);
        setNeedMD5(true);
    }

    public UploadPartRequest(String bucket, String cosPath, int partNumber, String srcPath, String uploadId){
        this(bucket, cosPath);
        this.partNumber = partNumber;
        this.srcPath = srcPath;
        this.uploadId = uploadId;
        fileOffset = -1L;
        fileContentLength = -1L;
    }

    public UploadPartRequest(String bucket, String cosPath, int partNumber, Uri uri, String uploadId) {
        this(bucket, cosPath);
        this.partNumber = partNumber;
        this.uri = uri;
        this.uploadId = uploadId;
        fileOffset = -1L;
        fileContentLength = -1L;
    }

    public UploadPartRequest(String bucket, String cosPath, int partNumber, String srcPath, long offset, long length,
                             String uploadId){
        this(bucket, cosPath);
        this.partNumber = partNumber;
        setSrcPath(srcPath, offset, length);
        this.uploadId = uploadId;
    }

    public UploadPartRequest(String bucket, String cosPath, int partNumber, Uri uri, long offset, long length,
                             String uploadId){
        this(bucket, cosPath);
        this.partNumber = partNumber;
        this.uri = uri;
        this.fileOffset = offset;
        this.fileContentLength = length;
        this.uploadId = uploadId;
    }

    public UploadPartRequest(String bucket, String cosPath, int partNumber, byte[] data, String uploadId){
        this(bucket, cosPath);
        this.partNumber = partNumber;
        this.data = data;
        this.uploadId = uploadId;
        fileOffset = -1L;
        fileContentLength = -1L;
    }

    public UploadPartRequest(String bucket, String cosPath, int partNumber, InputStream inputStream, String uploadId) throws CosXmlClientException {
        this(bucket, cosPath);
        this.partNumber = partNumber;
        this.inputStream = inputStream;
        //this.srcPath = FileUtils.tempCache(inputStream);
        this.uploadId = uploadId;
        fileOffset = -1L;
        fileContentLength = -1L;
    }

    /**
     * 设置上传的分块编号
     * @param partNumber 上传的分块数
     */
    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    /**
     * 获取用户设置的上传分块编号
     * @return 上传的分块数
     */
    public int getPartNumber() {
        return partNumber;
    }

    /**
     * 设置分块上传的UploadId
     * @param uploadId 分块上传的UploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * 获取用户设置分块上传的UploadId号
     * @return 分块上传的UploadId
     */
    public String getUploadId() {
        return uploadId;
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT ;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("partNumber", String.valueOf(partNumber));
        queryParameters.put("uploadId", uploadId);
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        if(srcPath != null){
            if(fileOffset != -1){
                return RequestBodySerializer.file(getContentType(), new File(srcPath), fileOffset, fileContentLength);
            }else {
               return RequestBodySerializer.file(getContentType(), new File(srcPath));
            }
        }else if(data != null){
            return RequestBodySerializer.bytes(null, data);
        }else if(inputStream != null){
            return RequestBodySerializer.stream(null, new File(CosXmlSimpleService.appCachePath),
                    inputStream);
        } else if (uri != null && ContextHolder.getAppContext() != null) {
            return RequestBodySerializer.uri(null, uri, ContextHolder.getAppContext(), fileOffset, fileContentLength);
        }
        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(requestURL == null){
            if(partNumber <= 0){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "partNumber must be >= 1");
            }
            if(uploadId == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "uploadID must not be null");
            }
        }
        if(srcPath == null && data == null && inputStream == null && uri == null){
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
     * <p>
     * 设置上传的本地文件路径
     * </p>
     * 可以设置上传本地文件、字节数组或者输入流。每次只能上传一种类型，若同时设置，
     * 则优先级为 本地文件&gt;字节数组&gt;输入流
     * @param srcPath 本地文件路径
     */
    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    /**
     * 设置上传的本地文件路径和上传范围
     * @see UploadPartRequest#setSrcPath(String)
     */
    public void setSrcPath(String srcPath, long fileOffset, long contentLength) {
        this.srcPath = srcPath;
        this.fileOffset = fileOffset;
        this.fileContentLength = contentLength;
    }

    /**
     * 获取设置的本地文件路径
     * @return String
     */
    public String getSrcPath() {
        return srcPath;
    }

    /**
     * <p>
     * 设置上传的字节数组
     * </p>
     * 可以设置上传本地文件、字节数组或者输入流。每次只能上传一种类型，若同时设置，
     * 则优先级为 本地文件&gt;字节数组&gt;输入流
     * @param data 需要上传的字节数组
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * 获取用户设置的字节数组
     * @return byte[]
     */
    public byte[] getData() {
        return data;
    }

    /**
     * 获取用户设置的输入流读取的字节长度
     * @return long
     */
    public long getFileLength() {
        if(data != null){
            fileContentLength =  data.length;
        }else if(srcPath != null && fileContentLength == -1L){
            fileContentLength = new File(srcPath).length();
        }
        return fileContentLength;
    }

    /**
     * 设置上传进度回调
     * @param progressListener {@link CosXmlProgressListener}
     */
    public void setProgressListener(CosXmlProgressListener progressListener){
        this.progressListener = progressListener;
    }

    public CosXmlProgressListener getProgressListener() {
        return progressListener;
    }

    @Override
    public void setTrafficLimit(long limit) {
        addHeader("x-cos-traffic-limit", String.valueOf(limit));
    }
}
