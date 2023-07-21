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

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class TemplateAnimationResponse {
    
    /**
     *模板 ID
     */
    public String templateId;

    /**
     *模板名字
     */
    public String name;

    /**
     *模板所属存储桶
     */
    public String bucketId;

    /**
     *模板属性，Custom 或者 Official
     */
    public String category;

    /**
     *模板 Tag
     */
    public String tag;

    /**
     *更新时间
     */
    public String updateTime;

    /**
     *创建时间
     */
    public String createTime;

    /**
     *详细的模板参数
     */
    public TemplateAnimationResponseTransTpl transTpl;

    @XmlBean(name = "TransTpl", method = XmlBean.GenerateMethod.FROM)
    public static class TemplateAnimationResponseTransTpl {
        public TransTplVideo video;
        public TransTplContainer container;
        public TransTplTimeInterval timeInterval;
    }

    @XmlBean(name = "Video", method = XmlBean.GenerateMethod.FROM)
    public static class TransTplVideo {
        /**
         *编解码格式
         */
        public String codec;

        /**
         *宽
         */
        public String width;

        /**
         *高
         */
        public String height;

        /**
         *帧率
         */
        public String fps;

        /**
         *动图只保留关键帧
         */
        public String animateOnlyKeepKeyFrame;

        /**
         *动图抽帧间隔时间
         */
        public String animateTimentervalOfFrame;

        /**
         *Animation 每秒抽帧帧数
         */
        public String animateFramesPerSecond;

        /**
         *设置相对质量
         */
        public String quality;

    }

    @XmlBean(name = "Container", method = XmlBean.GenerateMethod.FROM)
    public static class TransTplContainer {
        /**
         *封装格式： gif，hgif，webp  hgif 为高质量 gif，即清晰度比较高的 gif 格式图
         */
        public String format;

    }

    @XmlBean(name = "TimeInterval", method = XmlBean.GenerateMethod.FROM)
    public static class TransTplTimeInterval {
        /**
         *开始时间
         */
        public String start;

        /**
         *持续时间
         */
        public String duration;

    }


}
