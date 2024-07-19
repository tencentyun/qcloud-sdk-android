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

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;


/**
 * 查询AI内容识别队列
 * <a href="https://cloud.tencent.com/document/product/460/79394">查询AI内容识别队列</a>
 * @see com.tencent.cos.xml.CIService#getAIQueue(GetAIQueueRequest)
 * @see com.tencent.cos.xml.CIService#getAIQueueAsync(GetAIQueueRequest, CosXmlResultListener)
 */
public class GetAIQueueRequest extends BucketRequest {
    

    /**
     * 队列 ID，以“,”符号分割字符串;是否必传：否
     */
    public String queueIds;
    /**
     * Active 表示队列内的作业会被调度执行Paused 表示队列暂停，作业不再会被调度执行，队列内的所有作业状态维持在暂停状态，已经执行中的任务不受影响;是否必传：否
     */
    public String state;
    /**
     * 第几页，默认值1;是否必传：否
     */
    public int pageNumber;
    /**
     * 每页个数，默认值10;是否必传：否
     */
    public int pageSize;

    /**
     * 本接口用于查询AI 内容识别（异步）队列
     *
     * @param bucket  存储桶名
     */
    public GetAIQueueRequest(@NonNull String bucket ) {
        super(bucket);
        
    }
    
    @Override
    public Map<String, String> getQueryString() {
        if(queueIds != null) {
            queryParameters.put("queueIds", queueIds);
        }
        if(state != null) {
            queryParameters.put("state", state);
        }
        queryParameters.put("pageNumber", String.valueOf(pageNumber));
        queryParameters.put("pageSize", String.valueOf(pageSize));
        return super.getQueryString();
    }
    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/ai_queue";
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
