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

package com.tencent.cos.xml.model.ci.media;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 查询工作流
 * <a href="https://cloud.tencent.com/document/product/460/76857">查询工作流</a>
 * @see com.tencent.cos.xml.CIService#getWorkflowList(GetWorkflowListRequest)
 * @see com.tencent.cos.xml.CIService#getWorkflowListAsync(GetWorkflowListRequest, CosXmlResultListener)
 */
public class GetWorkflowListRequest extends BucketRequest {
    /**
     * 工作流 ID，以,符号分割字符串
     */
    public String ids;
    /**
     * 工作流名称
     */
    public String name;
    /**
     * 第几页
     */
    public String pageNumber;
    /**
     * 每页个数
     */
    public String pageSize;

    /**
     * 查询工作流
     *
     * @param bucket  存储桶名
     */
    public GetWorkflowListRequest(@NonNull String bucket ) {
        super(bucket);
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("ids", ids);
        queryParameters.put("name", name);
        queryParameters.put("pageNumber", pageNumber);
        queryParameters.put("pageSize", pageSize);
        return super.getQueryString();
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/workflow";
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
        return config.getRequestHost(region, bucket, CosXmlServiceConfig.CI_HOST_FORMAT);
    }

}
