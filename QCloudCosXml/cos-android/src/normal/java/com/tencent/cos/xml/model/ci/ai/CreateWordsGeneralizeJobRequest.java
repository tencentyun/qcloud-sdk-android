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

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.cos.xml.model.ci.ai.bean.CreateWordsGeneralizeJob;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 提交一个AI分词识别任务的请求.
 * @see com.tencent.cos.xml.CIService#createWordsGeneralizeJob(CreateWordsGeneralizeJobRequest)
 * @see com.tencent.cos.xml.CIService#createWordsGeneralizeJobAsync(CreateWordsGeneralizeJobRequest, CosXmlResultListener)
 */
public class CreateWordsGeneralizeJobRequest extends BucketRequest {
    private final CreateWordsGeneralizeJob createWordsGeneralizeJob;
    /**
     * 提交一个分词任务请求
     *
     * @param bucket  存储桶名
     */
    public CreateWordsGeneralizeJobRequest(@NonNull String bucket) {
        super(bucket);
        addNoSignHeader("Content-Type");
        addNoSignHeader("Content-Length");
        createWordsGeneralizeJob = new CreateWordsGeneralizeJob();
    }

    /**
     * 设置文件在 COS 上的 key
     */
    public void setInputObject(@NonNull String object){
        createWordsGeneralizeJob.input.object = object;
    }

    /**
     * 设置任务所在的队列 ID
     */
    public void setQueueId(@NonNull String queueId){
        createWordsGeneralizeJob.queueId = queueId;
    }

    /**
     * 设置任务回调地址
     * 优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调
     */
    public void setCallBack(@NonNull String callBack){
        createWordsGeneralizeJob.callBack = callBack;
    }

    /**
     * 设置任务回调格式，JSON 或 XML，默认 XML
     * 优先级高于队列的回调格式
     */
    public void setCallBackFormat(@NonNull String callBackFormat){
        createWordsGeneralizeJob.callBackFormat = callBackFormat;
    }

    /**
     * 设置透传用户信息, 可打印的 ASCII 码, 长度不超过1024
     */
    public void setUserData(@NonNull String userData){
        createWordsGeneralizeJob.operation.userData = userData;
    }

    /**
     * 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0
     */
    public void setJobLevel(int jobLevel){
        createWordsGeneralizeJob.operation.jobLevel = jobLevel;
    }

    /**
     * 设置ner方式, 默认值DL
     * 可选值：NerBasic或DL
     */
    public void setNerMethod(@NonNull String nerMethod){
        createWordsGeneralizeJob.operation.wordsGeneralize.nerMethod = nerMethod;
    }

    /**
     * 设置分词粒度, 默认值MIX
     * 可选值：SegBasic或MIX
     */
    public void setSegMethod(@NonNull String segMethod){
        createWordsGeneralizeJob.operation.wordsGeneralize.segMethod = segMethod;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/ai_jobs";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                QCloudXmlUtils.toXml(createWordsGeneralizeJob));
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(TextUtils.isEmpty(createWordsGeneralizeJob.input.object)){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "input must be non-empty");
        }
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.POST;
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, bucket, CosXmlServiceConfig.CI_HOST_FORMAT);
    }
}
