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

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class TemplateAnimation {

    /**
     *模板类型：Animation;是否必传：是;
     */
    public String tag = "Animation";

    /**
     *模板名称仅支持中文、英文、数字、_、-和*;是否必传：是;
     */
    public String name;

    /**
     *容器格式;是否必传：是;
     */
    public TemplateAnimationContainer container;

    /**
     *视频信息;是否必传：否;
     */
    public TemplateAnimationVideo video;

    /**
     *时间区间;是否必传：否;
     */
    public TemplateAnimationTimeInterval timeInterval;

    @XmlBean(name = "Video")
    public static class TemplateAnimationVideo {
        /**
         *编解码格式;是否必传：是;默认值：无;限制：gif, webp;
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
         *帧率;是否必传：否;默认值：视频原始帧率;限制：值范围���(0，60]单位：fps如果不设置，那么播放速度按照原来的时间戳。这里设置 fps 为动图的播放帧率;
         */
        public String fps;

        /**
         *动图只保留关键帧 。若 AnimateOnlyKeepKeyFrame 设置为 true 时，则不考虑 AnimateTimeIntervalOfFrame、AnimateFramesPerSecond；若 AnimateOnlyKeepKeyFrame 设置为 false 时，则必须填写AnimateTimeIntervalOfFrame 或 AnimateFramesPerSecond;是否必传：否;默认值：false;限制：true、false动图保留关键帧参数优先级：AnimateFramesPerSecond 大于 AnimateOnlyKeepKeyFrame 大于 AnimateTimeIntervalOfFrame;
         */
        public String animateOnlyKeepKeyFrame;

        /**
         *动图抽帧间隔时间;是否必传：否;默认值：无;限制：（0，视频时长]动图抽帧时间间隔若设置 TimeInterval.Duration，则小于该值;
         */
        public String animateTimeIntervalOfFrame;

        /**
         *Animation 每秒抽帧帧数;是否必传：否;默认值：无;限制：（0，视频帧率）动图抽帧频率优先级AnimateFramesPerSecond 大于 AnimateOnlyKeepKeyFrame 大于 AnimateTimeIntervalOfFrame;
         */
        public String animateFramesPerSecond;

        /**
         *设置相对质量;是否必传：否;默认值：无;限制：[1, 100)webp 图像质量设定生效，gif 没有质量参数;
         */
        public String quality;

    }

    @XmlBean(name = "Container")
    public static class TemplateAnimationContainer {
        /**
         *封装格式：gif，hgif，webp  hgif 为高质量 gif，即清晰度比较高的 gif 格式图;是否必传：是;
         */
        public String format;

    }

    @XmlBean(name = "TimeInterval")
    public static class TemplateAnimationTimeInterval {
        /**
         *开始时间;是否必传：否;默认值：0;限制： [0 视频时长] 单位为秒 支持 float 格式，执行精度精确到毫秒;
         */
        public String start;

        /**
         *持续时间;是否必传：否;默认值：视频时长;限制： [0 视频时长] 单位为秒 支持 float 格式，执行精度精确到毫秒;
         */
        public String duration;

    }


   
}
