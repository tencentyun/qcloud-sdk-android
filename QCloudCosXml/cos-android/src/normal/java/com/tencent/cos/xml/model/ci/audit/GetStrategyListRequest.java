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

package com.tencent.cos.xml.model.ci.audit;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 查询审核策略列表
 * @see com.tencent.cos.xml.CIService#getAuditStrategyList(GetStrategyListRequest)
 * @see com.tencent.cos.xml.CIService#getAuditStrategyListAsync(GetStrategyListRequest, CosXmlResultListener)
 */
public class GetStrategyListRequest extends BucketRequest {
    /**
     * 审核策略的服务类型，有效值：Image、Video、Audio、Text、Document、Html。不填表示查询所有审核服务类型的策略。
     */
    public String service;
    /**
     * 查询的起始位置，表示从该条开始查旬后续的策略，默认为0。
     */
    public int offset = -1;
    /**
     * 查询的最大条数，默认为20。
     */
    public int limit = -1;

    /**
     * 查询审核策略列表
     *
     * @param bucket  存储桶名
     */
    public GetStrategyListRequest(@NonNull String bucket ) {
        super(bucket);
    }

    @Override
    public Map<String, String> getQueryString() {
        if(!TextUtils.isEmpty(service)){
            queryParameters.put("service", service);
        }
        if(offset != -1) {
            queryParameters.put("offset", String.valueOf(offset));
        }
        if(limit != -1) {
            queryParameters.put("limit", String.valueOf(limit));
        }
        return super.getQueryString();
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/audit/strategy";
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
