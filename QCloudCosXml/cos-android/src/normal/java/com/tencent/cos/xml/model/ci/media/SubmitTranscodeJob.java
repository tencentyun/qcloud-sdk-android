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
import com.tencent.cos.xml.model.ci.common.DigitalWatermark;
import com.tencent.cos.xml.model.tag.CallBackMqConfig;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class SubmitTranscodeJob {

    /**
     *创建任务的Tag：Transcode;是否必传：是;
     */
    public String tag = "Transcode";

    /**
     *待操作的文件信息;是否必传：是;
     */
    public SubmitTranscodeJobInput input;

    /**
     *操作规则;是否必传：是;
     */
    public SubmitTranscodeJobOperation operation;

    /**
     *任务所在的队列类型，限制为 SpeedTranscoding, 表示为开启倍速转码;是否必传：否;
     */
    public String queueType;

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
     *任务回调TDMQ配置，当 CallBackType 为 TDMQ 时必填。详情见 CallBackMqConfig;是否必传：否;
     */
    public CallBackMqConfig callBackMqConfig;

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.TO)
    public static class SubmitTranscodeJobInput {
        /**
         *文件路径;是否必传：是;
         */
        public String object;

    }

    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.TO)
    public static class SubmitTranscodeJobOperation {
        /**
         *转码模板 ID;是否必传：否;
         */
        public String templateId;

        /**
         *转码模板参数;是否必传：否;
         */
        public SubmitTranscodeJobTranscode transcode;

        /**
         *水印模板 ID，可以传多个水印模板 ID，最多传3个。;是否必传：否;
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<String> watermarkTemplateId;
        /**
         *水印模板参数，同创建水印模板接口中的 Request.Watermark  ，最多传3个。;是否必传：否;
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<TemplateWatermark.Watermark> watermark;

        /**
         *去除水印参数, H265、AV1编码暂不支持该参数;是否必传：否;
         */
        public SubmitTranscodeJobRemoveWatermark removeWatermark;

        /**
         *字幕参数, H265、AV1编码和非mkv封装 暂不支持该参数;是否必传：否;
         */
        public SubmitTranscodeJobSubtitles subtitles;

        /**
         *数字水印参数,详情见 DigitalWatermark;是否必传：否;
         */
        public DigitalWatermark digitalWatermark;

        /**
         *结果输出配置;是否必传：是;
         */
        public SubmitTranscodeJobOutput output;

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
    public static class SubmitTranscodeJobOutput {
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
    public static class SubmitTranscodeJobTranscode {
        /**
         *同创建转码模板接口中的 Request.TimeInterval;是否必传：否;
         */
        public TemplateTranscode.TemplateTranscodeTimeInterval timeInterval;

        /**
         *同创建转码模板接口中的 Request.Container;是否必传：否;
         */
        public TemplateTranscode.TemplateTranscodeContainer container;

        /**
         *同创建转码模板接口中的 Request.Video ;是否必传：否;
         */
        public TemplateTranscode.TemplateTranscodeVideo video;

        /**
         *同创建转码模板接口中的 Request.Audio;是否必传：否;
         */
        public TemplateTranscode.TemplateTranscodeAudio audio;

        /**
         *同创建转码模板接口中的 Request.TransConfig;是否必传：否;
         */
        public TemplateTranscode.TemplateTranscodeTransConfig transConfig;

        /**
         *混音参数, 详情见 AudioMix;是否必传：否;
         */
        public AudioMix audioMix;

        /**
         *混音参数数组, 最多同时传2个。详情见 AudioMixArray;是否必传：否;
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<AudioMix> audioMixArray;
    }

    @XmlBean(name = "Subtitles", method = XmlBean.GenerateMethod.TO)
    public static class SubmitTranscodeJobSubtitles {
        /**
         *字幕参数;是否必传：是;
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<SubmitTranscodeJobSubtitle> subtitle;
    }

    @XmlBean(name = "RemoveWatermark")
    public static class SubmitTranscodeJobRemoveWatermark {
        /**
         *距离左上角原点 x 偏移，范围为[1, 4096];是否必传：是;
         */
        public String dx;

        /**
         *距离左上角原点 y 偏移，范围为[1, 4096];是否必传：是;
         */
        public String dy;

        /**
         *宽，范围为[1, 4096];是否必传：是;
         */
        public String width;

        /**
         *高，范围为[1, 4096];是否必传：是;
         */
        public String height;

    }

    @XmlBean(name = "Subtitle", method = XmlBean.GenerateMethod.TO)
    public static class SubmitTranscodeJobSubtitle {
        /**
         *同 bucket 的字幕地址;是否必传：是;
         */
        public String url;

    }


   
}
