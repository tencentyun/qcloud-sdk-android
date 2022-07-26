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
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 用于查询语音识别队列的请求.
 * @see com.tencent.cos.xml.CIService#getDescribeSpeechQueues(DescribeSpeechQueuesRequest)
 * @see com.tencent.cos.xml.CIService#getDescribeSpeechQueuesAsync(DescribeSpeechQueuesRequest, CosXmlResultListener)
 */
final public class DescribeSpeechQueuesRequest extends BucketRequest {
    public DescribeSpeechQueuesRequest(String bucket) {
        super(bucket);
    }

    public DescribeSpeechQueuesRequest(String bucket, String region) {
        super(bucket);
        this.region = region;
    }

    /**
     * 队列 ID，以“,”符号分割字符串
     * @param queueIds 队列 ID
     */
    public void setQueueIds(@NonNull String queueIds) {
        queryParameters.put("queueIds", queueIds);
    }

    /**
     * 设置状态
     * Active 表示队列内的作业会被语音识别服务调度执行
     * Paused 表示队列暂停，作业不再会被语音识别服务调度执行，队列内的所有作业状态维持在暂停状态，已经处于识别中的任务将继续执行，不受影响
     */
    public void setState(@NonNull String state) {
        queryParameters.put("state", state);
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
        return HttpConstants.RequestMethod.GET;
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, CosXmlServiceConfig.CI_HOST_FORMAT);
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/asrqueue";
    }
}
