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

package com.tencent.cos.xml.model.ci.asr.bean;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * 当 Tag 为 SpeechRecognition 时有效，指定该任务的参数
 */
@XmlBean
public class SpeechRecognition {
    /**
     * 引擎模型类型。
     * 电话场景：
     * • 8k_zh：电话 8k 中文普通话通用（可用于双声道音频）；
     * • 8k_zh_s：电话 8k 中文普通话话者分离（仅适用于单声道音频）；
     * 非电话场景：
     * • 16k_zh：16k 中文普通话通用；
     * • 16k_zh_video：16k 音视频领域；
     * • 16k_en：16k 英语；
     * • 16k_ca：16k 粤语。
     */
    public String engineModelType;

    /**
     * 语音声道数。1：单声道；2：双声道（仅支持 8k_zh 引擎模型）
     */
    public int channelNum = -1;

    /**
     * 识别结果返回形式。0： 识别结果文本(含分段时间戳)； 1：仅支持16k中文引擎，含识别结果详情(词时间戳列表，一般用于生成字幕场景)
     */
    public int resTextFormat = -1;

    /**
     * 是否过滤脏词（目前支持中文普通话引擎）。0：不过滤脏词；1：过滤脏词；2：将脏词替换为 * 。默认值为0
     */
    public int filterDirty;

    /**
     * 是否过语气词（目前支持中文普通话引擎）。0：不过滤语气词；1：部分过滤；2：严格过滤 。默认值为 0
     */
    public int filterModal;

    /**
     * 是否进行阿拉伯数字智能转换（目前支持中文普通话引擎）。0：不转换，直接输出中文数字，1：根据场景智能转换为阿拉伯数字。默认值为1
     */
    public int convertNumMode = 1;
}
