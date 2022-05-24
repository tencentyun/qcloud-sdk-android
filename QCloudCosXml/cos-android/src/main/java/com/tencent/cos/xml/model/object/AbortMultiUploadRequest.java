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
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * 终止一个分块上传操作并删除已上传的块的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#abortMultiUpload(AbortMultiUploadRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#abortMultiUploadAsync(AbortMultiUploadRequest, CosXmlResultListener)
*/
final public class AbortMultiUploadRequest extends BaseMultipartUploadRequest {

    /** 初始化分片返回的 uploadId */
    private String uploadId;

    /**
     * 舍弃一个分块上传且删除已上传的分片块构造方法
     * @param bucket 存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 bucket-1250000000)
     * @param cosPath 远端路径，即存储到 COS 上的绝对路径
     * @param uploadId 初始化分片返回的 uploadId
     */
    public AbortMultiUploadRequest(String bucket, String cosPath, String uploadId) {
        super(bucket, cosPath);
        this.uploadId = uploadId;
    }

    /**
     * @see CosXmlRequest#getMethod()
     */
    @Override
    public String getMethod() {
        return RequestMethod.DELETE;
    }

    /**
     * @see CosXmlRequest#getQueryString()
     */
    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("uploadId",uploadId);
        return queryParameters;
    }

    /**
     * @see CosXmlRequest#getRequestBody()
     */
    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    /**
     * @see CosXmlRequest#checkParameters()
     */
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
     * 舍弃一个上传分块且删除已上传的分块，需要指定是那个分块上传对象的uploadId.
     * @param uploadId 设置分片上传的uploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * 获取设置的分片上传的uploadId
     */
    public String getUploadId() {
        return uploadId;
    }

}
