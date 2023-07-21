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
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class TemplateConcat {

    /**
     *模板类型：Concat;是否必传：是;
     */
    public String tag = "Concat";

    /**
     *模板名称仅支持中文、英文、数字、_、-和*;是否必传：是;
     */
    public String name;

    /**
     *拼接模板;是否必传：是;
     */
    public TemplateConcatConcatTemplate concatTemplate;

    @XmlBean(name = "ConcatTemplate")
    public static class TemplateConcatConcatTemplate {
        /**
         *拼接节点;是否必传：是;默认值：无;限制：无;
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<TemplateConcatConcatFragment> concatFragment;
        /**
         *音频参数;是否必传：否;默认值：媒体原始值;限制：目标文件不需要 Audio 信息，需要设置 Audio.Remove 为 true;
         */
        public TemplateConcatAudio audio;

        /**
         *视频参数;是否必传：否;默认值：媒体原始值;限制：目标文件不需要 Video 信息，需要设置 Video.Remove 为 true;
         */
        public TemplateConcatVideo video;

        /**
         *封装格式;是否必传：是;默认值：无;限制：无;
         */
        public TemplateConcatContainer container;

        /**
         *只拼接不转码;是否必传：否;默认值：false;限制：true/ false;
         */
        public String directConcat;

        /**
         *转场参数;是否必传：是;默认值：无;限制：无;
         */
        public TemplateConcatSceneChangeInfo sceneChangeInfo;

        /**
         *混音参数, 详情见 AudioMix;是否必传：否;默认值：Audio.Remove 为 false 时生效;限制：-;
         */
        public AudioMix audioMix;

        /**
         *混音参数数组, 最多同时传2个。详情见 AudioMixArray;是否必传：否;默认值：Audio.Remove 为 false 时生效;限制：-;
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<AudioMix> audioMixArray;
    }

    @XmlBean(name = "Audio")
    public static class TemplateConcatAudio {
        /**
         *编解码格式;是否必传：是;默认值：文件原编码;限制：取值 aac、mp3;
         */
        public String codec;

        /**
         *采样率;是否必传：否;默认值：文件原采样率;限制：单位：Hz可选 11025、22050、32000、44100、48000、96000不同的封装，mp3 支持不同的采样率，如下表所示;
         */
        public String samplerate;

        /**
         *音频码率;是否必传：否;默认值：文件原音频码率;限制：单位：Kbps值范围：[8，1000];
         */
        public String bitrate;

        /**
         *声道数;是否必传：否;默认值：文件原声道数;限制：当 Codec 设置为 aac，支持1、2、4、5、6、8当 Codec 设置为 mp3，支持1、2;
         */
        public String channels;

    }

    @XmlBean(name = "Video")
    public static class TemplateConcatVideo {
        /**
         *编解码格式;是否必传：是;默认值：H.264;限制：H.264;
         */
        public String codec;

        /**
         *宽;是否必传：否;默认值：视频原始宽度;限制：值范围：[128，4096]单位：px若只设置 Width 时，按照视频原始比例计算 Height;
         */
        public String width;

        /**
         *高;是否必传：否;默认值：视频原始高度;限制：值范围：[128，4096]单位：px若只设置 Height 时，按照视频原始比例计算 Width;
         */
        public String height;

        /**
         *帧率;是否必传：否;默认值：视频原始帧率;限制：值范围：(0，60]单位：fps;
         */
        public String fps;

        /**
         *视频输出文件的码率;是否必传：否;默认值：视频原始码率;限制：值范围：[10，50000]单位：Kbps    ;
         */
        public String bitrate;

        /**
         *码率-质量控制因子;是否必传：否;默认值：视频原始码率;限制：值范围：(0, 51]如果设置了 Crf，则 Bitrate 的设置失效当 Bitrate 为空时，默认为25 ;
         */
        public String crf;

        /**
         *是否删除视频流;是否必传：否;默认值：false;限制：取值 true、false;
         */
        public String remove;

        /**
         *旋转角度;是否必传：否;默认值：无;限制：1. 值范围：[0, 360)2. 单位：度;
         */
        public String rotate;
    }

    @XmlBean(name = "Container")
    public static class TemplateConcatContainer {
        /**
         *封装格式：mp4，flv，hls，ts, mp3, aac;是否必传：是;
         */
        public String format;

    }

    @XmlBean(name = "ConcatFragment")
    public static class TemplateConcatConcatFragment {
        /**
         *拼接对象地址;是否必传：是;默认值：无;限制：同 bucket 对象文件;
         */
        public String url;

        /**
         *节点类型;是否必传：是;默认值：无;限制：Start：开头End：结尾;
         */
        public String mode;

        /**
         *开始时间，单位为秒, 支持float格式，执行精度精确到毫秒，当Request.ConcatTemplate.DirectConcat 为 true 时不生效;是否必传：否;默认值：无;限制：无;
         */
        public String startTime;

        /**
         *结束时间，单位为秒, 支持float格式，执行精度精确到毫秒，当 Request.ConcatTemplate.DirectConcat 为 true 时不生效;是否必传：否;默认值：无;限制：无;
         */
        public String endTime;

    }

    @XmlBean(name = "SceneChangeInfo")
    public static class TemplateConcatSceneChangeInfo {
        /**
         *转场模式;是否必传：是;默认值：无;限制：Default：不添加转场特效FADE：淡入淡出GRADIENT：渐变;
         */
        public String mode;

        /**
         *转场时长;是否必传：否;默认值：3;限制：单位：秒(s)取值范围：(0, 5], 支持小数;
         */
        public String time;

    }


   
}
