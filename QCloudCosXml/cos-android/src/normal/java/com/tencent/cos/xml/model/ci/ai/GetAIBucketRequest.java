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

package com.tencent.cos.xml.model.ci.ai;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 查询AI内容识别服务
 * <a href="https://cloud.tencent.com/document/product/460/79594">查询AI内容识别服务</a>
 * @see com.tencent.cos.xml.CIService#getAIBucket(GetAIBucketRequest)
 * @see com.tencent.cos.xml.CIService#getAIBucketAsync(GetAIBucketRequest, CosXmlResultListener)
 */
public class GetAIBucketRequest extends CosXmlRequest {
    /**
     * 地域信息，例如 ap-shanghai、ap-beijing，若查询多个地域以“,”分隔字符串，详情请参见 地域与域名;是否必传：否
     */
    public String regions;
    /**
     * 存储桶名称，以“,”分隔，支持多个存储桶，精确搜索;是否必传：否
     */
    public String bucketNames;
    /**
     * 存储桶名称前缀，前缀搜索;是否必传：否
     */
    public String bucketName;
    /**
     * 第几页;是否必传：是
     */
    public int pageNumber;
    /**
     * 每页个数，大于0且小于等于100的整数;是否必传：否
     */
    public int pageSize;

    /**
     * 本接口用于查询已经开通AI 内容识别（异步）服务的存储桶
     */
    public GetAIBucketRequest() {
    }
    
    @Override
    public Map<String, String> getQueryString() {
        if(regions != null) {
            queryParameters.put("regions", regions);
        }
        if(bucketNames != null) {
            queryParameters.put("bucketNames", bucketNames);
        }
        if(bucketName != null) {
            queryParameters.put("bucketName", bucketName);
        }
        queryParameters.put("pageNumber", String.valueOf(pageNumber));
        queryParameters.put("pageSize", String.valueOf(pageSize));
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/ai_bucket";
    }
    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }
    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.GET;
    }
    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, CosXmlServiceConfig.CI_REGION_HOST_FORMAT);
    }

}
