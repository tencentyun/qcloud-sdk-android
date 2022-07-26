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

package com.tencent.cos.xml.model.ci.asr;


import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.ci.GetDescribeMediaBucketsRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 查询已经开通语音识别功能的存储桶的请求.
 * @see com.tencent.cos.xml.CosXml#getDescribeMediaBuckets(GetDescribeMediaBucketsRequest) 
 * @see com.tencent.cos.xml.CosXml#getDescribeMediaBucketsAsync(GetDescribeMediaBucketsRequest, CosXmlResultListener) 
 */
final public class DescribeSpeechBucketsRequest extends CosXmlRequest {
    /**
     * 地域信息，以“,”分隔字符串，支持 All、ap-shanghai、ap-beijing
     * 详情请参见 地域与域名
     * @param regions 地域信息
     */
    public void setRegions(@NonNull String regions) {
        queryParameters.put("regions", regions);
    }

    /**
     * 存储桶名称，以“,”分隔，支持多个存储桶，精确搜索
     * @param bucketNames 存储桶名称
     */
    public void setBucketNames(@NonNull String bucketNames) {
        queryParameters.put("bucketNames", bucketNames);
    }

    /**
     * 存储桶名称前缀，前缀搜索
     * @param bucketName 存储桶名称前缀
     */
    public void setBucketName(@NonNull String bucketName) {
        queryParameters.put("bucketName", bucketName);
    }

    /**
     * 第几页
     * @param pageNumber 第几页
     */
    public void setPageNumber(int pageNumber) {
        queryParameters.put("pageNumber", String.valueOf(pageNumber));
    }

    /**
     * 每页个数
     * @param pageSize 每页个数
     */
    public void setPageSize(int pageSize) {
        queryParameters.put("pageSize", String.valueOf(pageSize));
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public String getPath(CosXmlServiceConfig config) {
        return  "/asrbucket";
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return String.format("ci.%s.myqcloud.com", config.getRegion());
    }
}
