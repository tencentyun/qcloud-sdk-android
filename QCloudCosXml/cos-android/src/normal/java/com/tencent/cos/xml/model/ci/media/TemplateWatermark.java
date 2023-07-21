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
public class TemplateWatermark {

    /**
     *模板类型: Watermark;是否必传：是;
     */
    public String tag = "Watermark";

    /**
     *模板名称，仅支持中文、英文、数字、_、-和*;是否必传：是;
     */
    public String name;

    /**
     *水印信息;是否必传：是;
     */
    public Watermark watermark;

    @XmlBean(name = "Watermark")
    public static class Watermark {
        /**
         *水印类型;是否必传：是;默认值：无;限制：Text：文字水印、 Image：图片水印;
         */
        public String type;

        /**
         *基准位置;是否必传：是;默认值：无;限制：TopRight、TopLeft、BottomRight、BottomLeft、Left、Right、Top、Bottom、Center;
         */
        public String pos;

        /**
         *偏移方式;是否必传：是;默认值：无;限制：Relativity：按比例，Absolute：固定位置;
         */
        public String locMode;

        /**
         *水平偏移;是否必传：是;默认值：无;限制：1. 在图片水印中，如果 Background 为 true，当 locMode 为 Relativity 时，为%，值范围：[-300 0]；当 locMode 为 Absolute 时，为 px，值范围：[-4096 0]。2.  在图片水印中，如果 Background 为 false，当 locMode 为 Relativity 时，为%，值范围：[0 100]；当 locMode 为 Absolute 时，为 px，值范围：[0 4096]。3. 在文字水印中，当 locMode 为 Relativity 时，为%，值范围：[0 100]；当 locMode 为 Absolute 时，为 px，值范围：[0 4096]。4. 当Pos为Top、Bottom和Center时，该参数无效。;
         */
        public String dx;

        /**
         *垂直偏移;是否必传：是;默认值：无;限制：1. 在图片水印中，如果 Background 为 true，当 locMode 为 Relativity 时，为%，值范围：[-300 0]；当 locMode 为 Absolute 时，为 px，值范围：[-4096 0]。2. 在图片水印中，如果 Background 为 false，当 locMode 为 Relativity 时，为%，值范围：[0 100]；当 locMode 为 Absolute 时，为 px，值范围：[0 4096]。3. 在文字水印中，当 locMode 为 Relativity 时，为%，值范围：[0 100]；当 locMode 为 Absolute 时，为 px，值范围：[0 4096]。4. 当Pos为Left、Right和Center时，该参数无效。;
         */
        public String dy;

        /**
         *水印开始时间;是否必传：否;默认值：0;限制：1. [0 视频时长]  2. 单位为秒  3. 支持 float 格式，执行精度精确到毫秒;
         */
        public String startTime;

        /**
         *水印结束时间;是否必传：否;默认值：视频结束时间;限制：1. [0 视频时长]  2. 单位为秒  3. 支持 float 格式，执行精度精确到毫秒;
         */
        public String endTime;

        /**
         *水印滑动配置，配置该参数后水印位移设置不生效，极速高清/H265转码暂时不支持该参数;是否必传：否;默认值：无;限制：无;
         */
        public TemplateWatermarkSlideConfig slideConfig;

        /**
         *图片水印节点;是否必传：否;默认值：无;限制：无;
         */
        public TemplateWatermarkImage image;

        /**
         *文本水印节点;是否必传：否;默认值：无;限制：无;
         */
        public TemplateWatermarkText text;

    }

    @XmlBean(name = "Text")
    public static class TemplateWatermarkText {
        /**
         *字体大小;是否必传：是;默认值：无;限制：值范围：[5 100]，单位 px;
         */
        public String fontSize;

        /**
         *字体类型;是否必传：是;默认值：无;限制：参考下表;
         */
        public String fontType;

        /**
         *字体颜色;是否必传：是;默认值：无;限制：格式：0xRRGGBB;
         */
        public String fontColor;

        /**
         *透明度;是否必传：是;默认值：无;限制：值范围：[1 100]，单位%;
         */
        public String transparency;

        /**
         *水印内容;是否必传：是;默认值：无;限制：长度不超过64个字符，仅支持中文、英文、数字、_、-和*;
         */
        public String text;

    }

    @XmlBean(name = "Image")
    public static class TemplateWatermarkImage {
        /**
         *水印图地址(需要 Urlencode 后传入);是否必传：是;默认值：无;限制：同 bucket 的水印图片地址;
         */
        public String url;

        /**
         *尺寸模式;是否必传：是;默认值：无;限制：1. Original：原有尺寸   2. Proportion：按比例  3. Fixed：固定大小;
         */
        public String mode;

        /**
         *宽;是否必传：否;默认值：无;限制：1. 当 Mode 为 Original 时，不支持设置水印图宽度  2. 当 Mode 为 Proportion，单位为%，背景图值范围：[100 300]；前景图值范围：[1 100]，相对于视���宽，最大不超过4096px 3. 当 Mode 为 Fixed，单位为 px，值范围：[8，4096] 4.若只设置 Width 时，按照水印图比例计算 Height;
         */
        public String width;

        /**
         *高;是否必传：否;默认值：无;限制：1. 当 Mode 为 Original 时，不支持设置水印图高度  2. 当 Mode 为 Proportion，单位为%，背景图值范围：[100 300]；前景图值范围：[1 100]，相对于视频高，最大不超过4096px 3. 当 Mode 为 Fixed，单位为 px，值范围：[8，4096] 4.若只设置 Height 时，按照水印图比例计算 Width;
         */
        public String height;

        /**
         *透明度;是否必传：是;默认值：无;限制：值范围：[1 100]，单位%;
         */
        public String transparency;

        /**
         *是否背景图;是否必传：否;默认值：false;限制：true、false;
         */
        public String background;

    }

    @XmlBean(name = "SlideConfig")
    public static class TemplateWatermarkSlideConfig {
        /**
         *滑动模式;是否必传：是;默认值：无;限制：Default: 默认不开启、ScrollFromLeft: 从左到右滚动，若设置了ScrollFromLeft模式，则Watermark.Pos参数不生效;
         */
        public String slideMode;

        /**
         *横向滑动速度;是否必传：是;默认值：无;限制：取值范围：[0,10]内的整数，默认为0;
         */
        public String xSlideSpeed;

        /**
         *纵向滑动速度;是否必传：是;默认值：无;限制：取值范围：[0,10]内的整数，默认为0;
         */
        public String ySlideSpeed;

    }


   
}
