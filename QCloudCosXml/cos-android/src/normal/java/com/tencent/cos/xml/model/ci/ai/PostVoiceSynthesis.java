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

import com.tencent.cos.xml.model.ci.common.CallBackMqConfig;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class PostVoiceSynthesis {

    /**
     * 创建任务的 Tag：Tts;是否必传：是
     */
    public String tag = "Tts";

    /**
     * 操作规则;是否必传：是
     */
    public PostVoiceSynthesisOperation operation;

    /**
     * 任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否
     */
    public String callBackFormat;

    /**
     * 任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否
     */
    public String callBackType;

    /**
     * 任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否
     */
    public String callBack;

    /**
     * 任务回调TDMQ配置，当 CallBackType 为 TDMQ 时必填。详情见 CallBackMqConfig;是否必传：否
     */
    public CallBackMqConfig callBackMqConfig;

    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.TO)
    public static class PostVoiceSynthesisOperation {
        /**
         * 语音合成模板 ID;是否必传：否
         */
        public String templateId;

        /**
         * 语音合成参数;是否必传：否
         */
        public PostVoiceSynthesisTtsTpl ttsTpl;

        /**
         * 语音合成任务参数;是否必传：是
         */
        public PostVoiceSynthesisTtsConfig ttsConfig;

        /**
         * 结果输出配置;是否必传：是
         */
        public PostVoiceSynthesisOutput output;

        /**
         * 透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
         */
        public String userData;

        /**
         * 任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
         */
        public String jobLevel;

    }
    @XmlBean(name = "Output", method = XmlBean.GenerateMethod.ALL)
    public static class PostVoiceSynthesisOutput {
        /**
         * 存储桶的地域;是否必传：是
         */
        public String region;

        /**
         * 存储结果的存储桶;是否必传：是
         */
        public String bucket;

        /**
         * 结果文件名;是否必传：是
         */
        public String object;

    }
    @XmlBean(name = "TtsConfig", method = XmlBean.GenerateMethod.ALL)
    public static class PostVoiceSynthesisTtsConfig {
        /**
         * 输入类型，Url/Text;是否必传：是
         */
        public String inputType;

        /**
         * 当 InputType 为 Url 时， 必须是合法的 COS 地址，文件必须是utf-8编码，且大小不超过 10M。如果合成方式为同步处理，则文件内容不超过 300 个 utf-8 字符；如果合成方式为异步处理，则文件内容不超过 10000 个 utf-8 字符。当 InputType 为 Text 时, 输入必须是 utf-8 字符, 且不超过 300 个字符。;是否必传：是
         */
        public String input;

    }
    @XmlBean(name = "TtsTpl", method = XmlBean.GenerateMethod.ALL)
    public static class PostVoiceSynthesisTtsTpl {
        /**
         * 同创建语音合成模板接口中的 Request.Mode;是否必传：否
         */
        public String mode;

        /**
         * 同创建语音合成模板接口中的 Request.Codec;是否必传：否
         */
        public String codec;

        /**
         * 同创建语音合成模板接口中的 Request.VoiceType;是否必传：否
         */
        public String voiceType;

        /**
         * 同创建语音合成模板接口中的 Request.Volume;是否必传：否
         */
        public String volume;

        /**
         * 同创建语音合成模板接口中的 Request.Speed;是否必传：否
         */
        public String speed;

        /**
         * 同创建语音合成模板接口中的 Request.Emotion;是否必传：否
         */
        public String emotion;

    }

   
}
