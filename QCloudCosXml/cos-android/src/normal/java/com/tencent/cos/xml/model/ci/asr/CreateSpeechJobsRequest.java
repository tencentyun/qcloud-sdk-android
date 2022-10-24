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

import androidx.annotation.NonNull;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.bucket.BucketRequest;
import com.tencent.cos.xml.model.ci.asr.bean.CreateSpeechJobs;
import com.tencent.cos.xml.model.tag.CallBackMqConfig;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 提交一个语音识别任务的请求.
 * @see com.tencent.cos.xml.CIService#createSpeechJobs(CreateSpeechJobsRequest) 
 * @see com.tencent.cos.xml.CIService#createSpeechJobsAsync(CreateSpeechJobsRequest, CosXmlResultListener) 
 */
public class CreateSpeechJobsRequest extends BucketRequest {
    private final CreateSpeechJobs createSpeechJobs;
    /**
     * 提交一个语音识别任务请求
     *
     * @param bucket  存储桶名
     */
    public CreateSpeechJobsRequest(@NonNull String bucket) {
        super(bucket);
        addNoSignHeader("Content-Type");
        addNoSignHeader("Content-Length");
        createSpeechJobs = new CreateSpeechJobs();
    }

    /**
     * 设置语音文件在 COS 上的 key
     */
    public void setInputObject(@NonNull String object){
        createSpeechJobs.input.object = object;
    }

    /**
     * 设置公网的Url，与Object必须有其中一个，且当两者都传入时，优先使用Object
     */
    public void setInputUrl(@NonNull String url){
        createSpeechJobs.input.url = url;
    }

    /**
     * 设置任务所在的队列 ID
     */
    public void setQueueId(@NonNull String queueId){
        createSpeechJobs.queueId = queueId;
    }

    /**
     * 设置结果输出地址
     */
    public void setOutput(@NonNull String region, @NonNull String bucket, @NonNull String object){
        createSpeechJobs.operation.output.region = region;
        createSpeechJobs.operation.output.bucket = bucket;
        createSpeechJobs.operation.output.object = object;
    }

    /**
     * 设置引擎模型类型。
     * 电话场景：
     * • 8k_zh：电话 8k 中文普通话通用（可用于双声道音频）；
     * • 8k_zh_s：电话 8k 中文普通话话者分离（仅适用于单声道音频）；
     * 非电话场景：
     * • 16k_zh：16k 中文普通话通用；
     * • 16k_zh_video：16k 音视频领域；
     * • 16k_en：16k 英语；
     * • 16k_ca：16k 粤语。
     */
    public void setEngineModelType(@NonNull String engineModelType){
        createSpeechJobs.operation.speechRecognition.engineModelType = engineModelType;
    }

    /**
     * 设置语音声道数。
     * 1：单声道；2：双声道（仅支持 8k_zh 引擎模型）
     */
    public void setChannelNum(int channelNum){
        createSpeechJobs.operation.speechRecognition.channelNum = channelNum;
    }

    /**
     * 设置识别结果返回形式。
     * 0： 识别结果文本(含分段时间戳)； 1：仅支持16k中文引擎，含识别结果详情(词时间戳列表，一般用于生成字幕场景)
     */
    public void setResTextFormat(int resTextFormat){
        createSpeechJobs.operation.speechRecognition.resTextFormat = resTextFormat;
    }

    /**
     * 设置是否过滤脏词（目前支持中文普通话引擎）。
     * 0：不过滤脏词；1：过滤脏词；2：将脏词替换为 * 。
     * 默认值为0
     */
    public void setFilterDirty(int filterDirty){
        createSpeechJobs.operation.speechRecognition.filterDirty = filterDirty;
    }

    /**
     * 设置是否过语气词（目前支持中文普通话引擎）。
     * 0：不过滤语气词；1：部分过滤；2：严格过滤 。
     * 默认值为 0
     */
    public void setFilterModal(int filterModal){
        createSpeechJobs.operation.speechRecognition.filterModal = filterModal;
    }

    /**
     * 是否进行阿拉伯数字智能转换（目前支持中文普通话引擎）。
     * 0：不转换，直接输出中文数字，1：根据场景智能转换为阿拉伯数字。
     * 默认值为1
     */
    public void setConvertNumMode(int convertNumMode){
        createSpeechJobs.operation.speechRecognition.convertNumMode = convertNumMode;
    }

    /**
     * 是否开启说话人分离：
     * 0 表示不开启；
     * 1 表示开启(仅支持8k_zh，16k_zh，16k_zh_video，单声道音频)。
     * 默认值为 0。
     * 注意：8k电话场景建议使用双声道来区分通话双方，设置ChannelNum=2即可，不用开启说话人分离。
     */
    public void setSpeakerDiarization(int speakerDiarization){
        createSpeechJobs.operation.speechRecognition.speakerDiarization = speakerDiarization;
    }

    /**
     * 仅支持非极速ASR
     * 说话人分离人数（需配合开启说话人分离使用），取值范围：0-10。
     * 0代表自动分离（目前仅支持≤6个人），1-10代表指定说话人数分离。默认值为 0。
     */
    public void setSpeakerNumber(int speakerNumber){
        createSpeechJobs.operation.speechRecognition.speakerNumber = speakerNumber;
    }

    /**
     * 是否过滤标点符号（目前支持中文普通话引擎）：
     * 0 表示不过滤。
     * 1 表示过滤句末标点。
     * 2 表示过滤所有标点。
     * 默认值为 0。
     */
    public void setFilterPunc(int filterPunc){
        createSpeechJobs.operation.speechRecognition.filterPunc = filterPunc;
    }

    /**
     * 输出文件类型，可选txt、srt。默认为txt
     * 极速ASR仅支持txt
     */
    public void setOutputFileType(String outputFileType){
        createSpeechJobs.operation.speechRecognition.outputFileType = outputFileType;
    }

    /**
     * 是否开启极速ASR，可选true、false。默认为false
     */
    public void setFlashAsr(boolean flashAsr){
        createSpeechJobs.operation.speechRecognition.flashAsr = flashAsr;
    }

    /**
     * 极速ASR音频格式。支持 wav、pcm、ogg-opus、speex、silk、mp3、m4a、aac。
     */
    public void setFormat(String format){
        createSpeechJobs.operation.speechRecognition.format = format;
    }

    /**
     * 极速ASR参数。表示是否只识别首个声道，默认为1。0：识别所有声道；1：识别首个声道。
     */
    public void setFirstChannelOnly(int firstChannelOnly){
        createSpeechJobs.operation.speechRecognition.firstChannelOnly = firstChannelOnly;
    }

    /**
     * 极速ASR参数。表示是否显示词级别时间戳，默认为0。0：不显示；1：显示，不包含标点时间戳，2：显示，包含标点时间戳。
     */
    public void setWordInfo(int wordInfo){
        createSpeechJobs.operation.speechRecognition.wordInfo = wordInfo;
    }

    /**
     * 设置透传用户信息, 可打印的 ASCII 码, 长度不超过1024
     */
    public void setUserData(@NonNull String userData){
        createSpeechJobs.operation.userData = userData;
    }

    /**
     * 设置任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0
     */
    public void setJobLevel(int jobLevel){
        createSpeechJobs.operation.jobLevel = jobLevel;
    }

    /**
     * 设置任务回调地址
     * 优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调
     */
    public void setCallBack(@NonNull String callBack){
        createSpeechJobs.callBack = callBack;
    }

    /**
     * 设置任务回调格式，JSON 或 XML，默认 XML
     * 优先级高于队列的回调格式
     */
    public void setCallBackFormat(@NonNull String callBackFormat){
        createSpeechJobs.callBackFormat = callBackFormat;
    }

    /**
     * 设置任务回调类型，Url 或 TDMQ，默认 Url
     * 优先级高于队列的回调类型
     */
    public void setCallBackType(@NonNull String callBackType){
        createSpeechJobs.callBackType = callBackType;
    }

    /**
     * 设置任务回调TDMQ配置
     * 当 CallBackType 为 TDMQ 时必填
     */
    public void setCallBackMqConfig(@NonNull CallBackMqConfig callBackMqConfig){
        createSpeechJobs.callBackMqConfig = callBackMqConfig;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/asr_jobs";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        try {
            return RequestBodySerializer.string(COSRequestHeaderKey.APPLICATION_XML,
                    QCloudXml.toXml(createSpeechJobs));
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        }
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(TextUtils.isEmpty(createSpeechJobs.input.object) && TextUtils.isEmpty(createSpeechJobs.input.url)){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "input must be non-empty");
        }

        if(TextUtils.isEmpty(createSpeechJobs.queueId)){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "queueId must be non-empty");
        }

        if(TextUtils.isEmpty(createSpeechJobs.operation.output.region) ||
                TextUtils.isEmpty(createSpeechJobs.operation.output.bucket) ||
                TextUtils.isEmpty(createSpeechJobs.operation.output.object)){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "output must be non-empty");
        }

        if(TextUtils.isEmpty(createSpeechJobs.operation.speechRecognition.engineModelType)){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "engineModelType must be non-empty");
        }

        if(createSpeechJobs.operation.speechRecognition.channelNum == -1){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "channelNum must be non-empty");
        }

        if(createSpeechJobs.operation.speechRecognition.resTextFormat == -1){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "resTextFormat must be non-empty");
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
