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
 * 查询文本库关键词列表
 * @see com.tencent.cos.xml.CIService#getAuditTextlibKeywordList(GetAuditTextlibKeywordListRequest)
 * @see com.tencent.cos.xml.CIService#getAuditTextlibKeywordListAsync(GetAuditTextlibKeywordListRequest, CosXmlResultListener)
 */
public class GetAuditTextlibKeywordListRequest extends BucketRequest {
    /**
     * 自定义文本库的ID。
     */
    private final String libid;

    /**
     * 关键词内容，过滤指定的关键词，在搜索过滤场景可填写该值。
     */
    public String content;
    /**
     * 关键词的标签类型，过滤指定类型的关键词，在搜索过滤场景可填写该值。
     */
    public String label;
    /**
     * 查询的起始位置，表示从该条开始查旬后续的关键词，默认为0。
     */
    public int offset = -1;
    /**
     * 查询的最大条数，默认为20
     */
    public int limit = -1;

    /**
     * 查询文本库关键词列表
     *
     * @param bucket  存储桶名
     */
    public GetAuditTextlibKeywordListRequest(@NonNull String bucket, @NonNull String libid) {
        super(bucket);
        this.libid = libid;
    }

    @Override
    public Map<String, String> getQueryString() {
        if(!TextUtils.isEmpty(content)){
            queryParameters.put("content", content);
        }
        if(!TextUtils.isEmpty(label)){
            queryParameters.put("label", label);
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
        return String.format("/audit/textlib/%s/keyword", this.libid);
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
