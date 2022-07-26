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


import android.text.TextUtils;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 拉取符合条件的语音识别任务的请求.
 * @see com.tencent.cos.xml.CIService#describeSpeechJobs(DescribeSpeechJobsRequest) 
 * @see com.tencent.cos.xml.CIService#describeSpeechJobsAsync(DescribeSpeechJobsRequest, CosXmlResultListener) 
 */
final public class DescribeSpeechJobsRequest extends BucketRequest {
    private final String queueId;
    public DescribeSpeechJobsRequest(String bucket, String queueId) {
        super(bucket);
        this.queueId = queueId;
        queryParameters.put("queueId", queueId);
        queryParameters.put("tag", "SpeechRecognition");
    }

    public DescribeSpeechJobsRequest(String bucket, String region, String queueId) {
        this(bucket, queueId);
        this.region = region;
    }

    /**
     * Desc 或者 Asc。默认为 Desc。
     */
    public void setOrderByTime(String orderByTime) {
        queryParameters.put("orderByTime", orderByTime);
    }

    /**
     * 请求的上下文，用于翻页。上次返回的值。
     */
    public void setNextToken(String nextToken) {
        queryParameters.put("nextToken", nextToken);
    }

    /**
     * 拉取的最大任务数。默认为10。最大为100。
     */
    public void setSize(int size) {
        queryParameters.put("size", String.valueOf(size));
    }

    /**
     * 拉取该状态的任务，以,分割，支持多状态：All、Submitted、Running、Success、Failed、Pause、Cancel。默认为 All。
     */
    public void setStates(String states) {
        queryParameters.put("states", states);
    }

    /**
     * 拉取创建时间大于该时间的任务。格式为：%Y-%m-%dT%H:%m:%S%z
     */
    public void setStartCreationTime(String startCreationTime) {
        queryParameters.put("startCreationTime", startCreationTime);
    }

    /**
     * 拉取创建时间小于该时间的任务。格式为：%Y-%m-%dT%H:%m:%S%z
     */
    public void setEndCreationTime(String endCreationTime) {
        queryParameters.put("endCreationTime", endCreationTime);
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
        return "/asr_jobs";
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(TextUtils.isEmpty(queueId)){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "queueId must be non-empty");
        }
    }
}
