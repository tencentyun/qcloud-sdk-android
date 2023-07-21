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
public class TemplateSmartCover {

    /**
     *模板类型：SmartCover;是否必传：是;
     */
    public String tag = "SmartCover";

    /**
     *模板名称仅支持中文、英文、数字、_、-和*;是否必传：是;
     */
    public String name;

    /**
     *智能封面参数;是否必传：是;
     */
    public TemplateSmartCoverSmartCover smartCover;

    @XmlBean(name = "SmartCover")
    public static class TemplateSmartCoverSmartCover {
        /**
         *图片格式;是否必传：否;默认值：jpg;限制：jpg、png  、webp;
         */
        public String format;

        /**
         *宽;是否必传：否;默认值：视频原始宽度;限制：值范围：[128，4096]单位：px若只设置 Width 时，按照视频原始比例计算 Height;
         */
        public String width;

        /**
         *高;是否必传：否;默认值：视频原始高度;限制：值范围��[128，4096]单位：px若只设置 Height 时，按照视频原始比例计算 Width;
         */
        public String height;

        /**
         *截图数量;是否必传：否;默认值：3;限制：[1,10];
         */
        public String count;

        /**
         *封面去重;是否必传：否;默认值：false;限制：true/false;
         */
        public String deleteDuplicates;

    }


   
}
