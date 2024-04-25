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

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.core.task.QCloudTask;

import java.util.Map;

/**
 * 查询特定分块上传中的已上传的块的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#listParts(ListPartsRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#listPartsAsync(ListPartsRequest, CosXmlResultListener)
 */
final public class ListPartsRequest extends BaseMultipartUploadRequest {

    private String uploadId;
    private String maxParts;
    private String partNumberMarker;
    private String encodingType;

    /**
     *
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param uploadId 初始化分片返回的 uploadId
     */
    public ListPartsRequest(String bucket, String cosPath, String uploadId){
        super(bucket, cosPath);
        this.uploadId = uploadId;
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        if(uploadId != null){
            queryParameters.put("uploadId",uploadId);
        }
        if(maxParts != null){
            queryParameters.put("max-parts",maxParts);
        }
        if(partNumberMarker != null){
            queryParameters.put("part-number-marker",partNumberMarker);
        }
        if(encodingType != null){
            queryParameters.put("Encoding-type",encodingType);
        }
        return queryParameters;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
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

    /**
     * 设置查询分片上传的UploadId
     * @param uploadId 分片上传的UploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * 获取分片上传的UploadId
     * @return 分片上传的UploadId
     */
    public String getUploadId() {
        return uploadId;
    }

    /**
     * 设置单次返回的最大条目数
     * @param maxParts 查询返回的最大条目数
     */
    public void setMaxParts(int maxParts) {
        maxParts = maxParts <= 0 ? 1: maxParts;
        this.maxParts = String.valueOf(maxParts);
    }

    /**
     * 获取用户设置的单次返回最大条目数
     * @return 查询返回的最大条目数
     */
    public int getMaxParts() {
        return Integer.parseInt(maxParts);
    }

    /**
     * 设置列出分片的起点。
     * @param partNumberMarker 列出分片的起点，可从ListPartsResult中读取。
     */
    public void setPartNumberMarker(int partNumberMarker) {
        this.partNumberMarker = String.valueOf(partNumberMarker);
    }

    /**
     * 设置列出分片的起点。
     * @param partNumberMarker 列出分片的起点，可从ListPartsResult中读取。
     */
    public void setPartNumberMarker(String partNumberMarker) {
        this.partNumberMarker = partNumberMarker;
    }

    /**
     * 获取设置的NumberMarker
     * @return 用户设置的NumberMarker
     */
    public int getPartNumberMarker() {
        return Integer.parseInt(partNumberMarker);
    }

    /**
     * 设置返回值的编码方式
     * @param encodingType 返回值的编码方式
     */
    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    /**
     * 获取设置的返回值编码方式
     * @return 返回值编码方式
     */
    public String getEncodingType() {
        return encodingType;
    }

    @Override
    public int getPriority() {
        return QCloudTask.PRIORITY_HIGH;
    }
}
