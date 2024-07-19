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
import com.tencent.cos.xml.model.ci.common.NoiseReduction;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class PostNoiseReduction {

    /**
     * 创建任务的 Tag：NoiseReduction;是否必传：是
     */
    public String tag = "NoiseReduction";

    /**
     * 待操作的文件信息;是否必传：是
     */
    public PostNoiseReductionInput input;

    /**
     * 操作规则;是否必传：是
     */
    public PostNoiseReductionOperation operation;

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

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.TO)
    public static class PostNoiseReductionInput {
        /**
         * 执行音频降噪任务的文件路径目前只支持文件大小在10M之内的音频 如果输入为视频文件或者多通道的音频，只会保留单通道的音频流 目前暂不支持m3u8格式输入;是否必传：是
         */
        public String object;

    }
    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.TO)
    public static class PostNoiseReductionOperation {
        /**
         * 降噪模板ID;是否必传：否
         */
        public String templateId;

        /**
         * 降噪任务参数，同创建降噪模板接口中的 Request.NoiseReduction;是否必传：否
         */
        public NoiseReduction noiseReduction;

        /**
         * 任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否
         */
        public String jobLevel;

        /**
         * 透传用户信息，可打印的 ASCII 码，长度不超过1024;是否必传：否
         */
        public String userData;

        /**
         * 结果输出配置;是否必传：是
         */
        public PostNoiseReductionOutput output;

    }
    @XmlBean(name = "Output", method = XmlBean.GenerateMethod.ALL)
    public static class PostNoiseReductionOutput {
        /**
         * 存储桶的地域;是否必传：是
         */
        public String region;

        /**
         * 存储结果的存储桶;是否必传：是
         */
        public String bucket;

        /**
         * 输出结果的文件名;是否必传：是
         */
        public String object;

    }

   
}
