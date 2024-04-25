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
public class TemplateVideoMontage {

    /**
     *模板类型: VideoMontage;是否必传：是;限制：无;
     */
    public String tag = "VideoMontage";

    /**
     *模板名称 仅支持中文、英文、数字、_、-和*;是否必传：是;限制：无;
     */
    public String name;

    /**
     *集锦时长;是否必传：否;限制：1. 默认自动分析时长2. 单位为秒3. 支持 float 格式，执行精度精确到毫秒;
     */
    public String duration;

    /**
     *容器格式;是否必传：是;限制：无;
     */
    public TemplateVideoMontageContainer container;

    /**
     *视频信息;是否必传：是;限制：无;
     */
    public TemplateVideoMontageVideo video;

    /**
     *音频信息;是否必传：否;限制：无;
     */
    public TemplateVideoMontageAudio audio;

    /**
     *精彩集锦场景;是否必传：否;限制：取值范围：Soccer/Video，默认值为Video;
     */
    public String scene;

    /**
     *混音参数, 详情见 AudioMixSubmitVideoTagJob;是否必传：否;限制：Audio.Remove 为 false 时生效;
     */
    public AudioMix audioMix;

    /**
     *混音参数数组, 最多同时传2个。详情见 AudioMixArraySubmitVideoTagJob;是否必传：否;限制：Audio.Remove 为 false 时生效;
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<AudioMix> audioMixArray;

    @XmlBean(name = "Video")
    public static class TemplateVideoMontageVideo {
        /**
         *编解码格式;是否必传：否;默认值：H.264;限制：1. H.264 2.H.265;
         */
        public String codec;

        /**
         *宽;是否必传：否;默认值：视频原始宽度;限制：1. 值范围：[128，4096]2. 单位：px3. 若只设置 Width 时，按照视频原始比例计算 Height4. 必须为偶数;
         */
        public String width;

        /**
         *高;是否必传：否;默认值：视频原始高度;限制：1. 值范围：[128，4096]2. 单位：px3. 若只设置 Height 时，按照视频原始比例计算 Width4. 必须为偶数;
         */
        public String height;

        /**
         *帧率;是否必传：否;默认值：无;限制：1. 值范围：(0，60]2. 单位：fps;
         */
        public String fps;

        /**
         *视频输出文件的码率;是否必传：否;默认值：无;限制：1. 值范围：[10，50000]2. 单位：Kbps;
         */
        public String bitrate;

        /**
         *码率-质量控制因子;是否必传：否;默认值：无;限制：1. 值范围：(0, 51]2. 如果设置了 Crf，则 Bitrate 的设置失效3. 当 Bitrate 为空时，默认为25;
         */
        public String crf;

        /**
         *旋转角度;是否必传：否;默认值：无;限制：1. 值范围：[0, 360)2. 单位：度;
         */
        public String rotate;

    }

    @XmlBean(name = "Audio")
    public static class TemplateVideoMontageAudio {
        /**
         *编解码格式;是否必传：否;默认值：aac;限制：取值 aac、mp3;
         */
        public String codec;

        /**
         *采样率;是否必传：否;默认值：44100;限制：1. 单位：Hz2. 可选 11025、22050、32000、44100、48000、960003. 不同的封装，mp3 支持不同的采样率，如下表所示;
         */
        public String samplerate;

        /**
         *原始音频码率;是否必传：否;默认值：无;限制：1. 单位：Kbps2. 值范围：[8，1000];
         */
        public String bitrate;

        /**
         *声道数;是否必传：否;默认值：无;限制：1. 当 Codec 设置为 aac，支持1、2、4、5、6、82. 当 Codec 设置为 mp3，支持1、2;
         */
        public String channels;

        /**
         *是否删除音频流;是否必传：否;默认值：false;限制：取值 true、false;
         */
        public String remove;

    }

    @XmlBean(name = "Container")
    public static class TemplateVideoMontageContainer {
        /**
         *封装格式: mp4、flv、hls、ts、mkv;是否必传：是;
         */
        public String format;

    }


   
}
