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

package com.tencent.cos.xml.model.ci.media;

import com.tencent.cos.xml.model.ci.common.AudioMix;
import com.tencent.cos.xml.model.tag.CallBackMqConfig;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class SubmitVideoMontageJob {

    /**
     *创建任务的 Tag：VideoMontage;是否必传：是;
     */
    public String tag = "VideoMontage";

    /**
     *待操作的文件信息;是否必传：是;
     */
    public SubmitVideoMontageJobInput input;

    /**
     *操作规则;是否必传：是;
     */
    public SubmitVideoMontageJobOperation operation;

    /**
     *任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否;
     */
    public String callBackFormat;

    /**
     *任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否;
     */
    public String callBackType;

    /**
     *任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否;
     */
    public String callBack;

    /**
     *任务回调 TDMQ 配置，当 CallBackType 为 TDMQ 时必填。详情见 CallBackMqConfig;是否必传：否;
     */
    public CallBackMqConfig callBackMqConfig;

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.TO)
    public static class SubmitVideoMontageJobInput {
        /**
         *文件路径;是否必传：是，当场景为Soccer时非必选;
         */
        public String object;

        /**
         *支持公网下载的Url，与Object必须有其中一个，且当两者都传入时，优先使用Object，仅支持Soccer场景;是否必传：否;
         */
        public String url;

    }

    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.TO)
    public static class SubmitVideoMontageJobOperation {
        /**
         *精彩集锦模板参数;是否必传：否;
         */
        public SubmitVideoMontageJobVideoMontage videoMontage;

        /**
         *模板 ID;是否必传：否;
         */
        public String templateId;

        /**
         *结果输出配置;是否必传：是;
         */
        public SubmitVideoMontageJobOutput output;

        /**
         *透传用户信息, 可打印的 ASCII 码, 长度不超过1024;是否必传：否;
         */
        public String userData;

        /**
         *任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否;
         */
        public String jobLevel;

    }

    @XmlBean(name = "Output")
    public static class SubmitVideoMontageJobOutput {
        /**
         *存储桶的地域;是否必传：是;
         */
        public String region;

        /**
         *存储结果的存储桶;是否必传：是;
         */
        public String bucket;

        /**
         *输出结果的文件名;是否必传：是;
         */
        public String object;

    }

    @XmlBean(name = "Transcode")
    public static class SubmitVideoMontageJobVideoMontage {
        /**
         *同创建转码模板接口中的 Request.TimeInterval;是否必传：否;
         */
//        public SubmitVideoMontageJobTimeInterval timeInterval;

        /**
         *同创建转码模板接口中的 Request.Container;是否必传：否;
         */
        public TemplateVideoMontage.TemplateVideoMontageContainer container;

        /**
         *同创建转码模板接口中的 Request.Video;是否必传：否;
         */
        public TemplateVideoMontage.TemplateVideoMontageVideo video;

        /**
         *同创建转码模板接口中的 Request.Audio;是否必传：否;
         */
        public TemplateVideoMontage.TemplateVideoMontageAudio audio;

        /**
         *同创建转码模板接口中的 Request.TransConfig;是否必传：否;
         */
        public TemplateTranscode.TemplateTranscodeTransConfig transConfig;

        /**
         *同创建转码模板接口中的 Request.Scene;是否必传：否;
         */
//        public SubmitVideoMontageJobScene scene;

        /**
         *混音参数, 详情见 AudioMix;是否必传：否;
         */
        public AudioMix audioMix;
        /**
         *混音参数数组, 最多同时传2个。详情见AudioMixArray;是否必传：否;
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<AudioMix> audioMixArray;
    }


   
}
