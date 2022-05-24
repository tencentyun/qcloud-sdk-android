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

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.CompleteMultipartUpload;
import com.tencent.cos.xml.transfer.XmlSlimBuilder;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.task.QCloudTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 完成整个分块上传的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#completeMultiUpload(CompleteMultiUploadRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#completeMultiUploadAsync(CompleteMultiUploadRequest, CosXmlResultListener)
 */
final public class CompleteMultiUploadRequest extends BaseMultipartUploadRequest{

    /**
     * 分块上传所有块的信息，用于校验块的准确性.
     */
    private CompleteMultipartUpload completeMultipartUpload;

    /** 初始化分片返回的 uploadId */
    private String uploadId;

    /**
     * 完成整个分块上传构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param uploadId 初始化分片上传，返回的 uploadId
     * @param partNumberAndETag 分片编号 和对应的分片 MD5 值
     */
    public CompleteMultiUploadRequest(String bucket, String cosPath, String uploadId, Map<Integer,String> partNumberAndETag ){
        super(bucket,cosPath);
        this.uploadId = uploadId;
        completeMultipartUpload = new CompleteMultipartUpload();
        completeMultipartUpload.parts = new ArrayList<CompleteMultipartUpload.Part>();
        setPartNumberAndETag(partNumberAndETag);
    }

    /**
     * 获取分块上传所有块的信息
     * @return 分块上传所有块的信息
     */
    public CompleteMultipartUpload getCompleteMultipartUpload() {
        return completeMultipartUpload;
    }

    /**
     * 添加单个分块的eTag值
     * @param partNumbers 分块编号
     * @param eTag 该分块的eTag值
     */
    public void setPartNumberAndETag(int partNumbers, String eTag){
        CompleteMultipartUpload.Part part = new CompleteMultipartUpload.Part();
        part.partNumber = partNumbers;
        part.eTag = eTag;
        completeMultipartUpload.parts.add(part);
    }

    /**
     * 添加多个分块的编号 和 eTag值
     */
    public void setPartNumberAndETag(Map<Integer,String> partNumberAndETag){
        if(partNumberAndETag != null){
            CompleteMultipartUpload.Part part;
            for(Map.Entry<Integer,String> entry : partNumberAndETag.entrySet()){
                part = new CompleteMultipartUpload.Part();
                part.partNumber = entry.getKey();
                part.eTag = entry.getValue();
                completeMultipartUpload.parts.add(part);
            }
        }
    }

    /**
     * 设置该分块上传的uploadId
     * @param uploadId 分块上传的UploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * 获取用户设置的该分块上传的uploadId
     * @return 分块上传的UploadId
     */
    public String getUploadId() {
        return uploadId;
    }

    @Override
    public String getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("uploadId", uploadId);
        return queryParameters;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.bytes(COSRequestHeaderKey.APPLICATION_XML, XmlSlimBuilder.buildCompleteMultipartUpload(completeMultipartUpload).getBytes("utf-8"));
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(requestURL != null){
            return;
        }
        if(uploadId == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "uploadID must not be null");
        }
    }

    @Override
    public int getPriority() {
        return QCloudTask.PRIORITY_HIGH;
    }
}
