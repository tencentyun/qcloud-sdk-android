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

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class AIIDCardOCRResponse {
    
    /**
     * 身份证识别信息
     */
    public AIIDCardOCRResponseIdInfo idInfo;
    /**
     * 扩展信息，不请求则不返回
     */
    public AIIDCardOCRResponseAdvancedInfo advancedInfo;
    @XmlBean(name = "AdvancedInfo", method = XmlBean.GenerateMethod.FROM)
    public static class AIIDCardOCRResponseAdvancedInfo {
        /**
         * 裁剪后身份证照片的 Base64 编码，设置 Config.CropIdCard 为 true 时返回
         */
        public String idCard;
        /**
         * 身份证头像照片的 Base64 编码，设置 Config.CropPortrait 为 true 时返回
         */
        public String portrait;
        /**
         * 图片质量分数，设置  Config.Quality 为 true 时返回（取值范围：0~100，分数越低越模糊，建议阈值≥50）
         */
        public String quality;
        /**
         * 身份证边框不完整告警阈值分数，设置 Config.BorderCheckWarn 为 true 时返回（取值范围：0~100，分数越低边框遮挡可能性越低，建议阈值≥50）
         */
        public String borderCodeValue;
        /**
         * 告警信息，Code 告警码列表和释义：9100 身份证有效日期不合法告警9101 身份证边框不完整告警9102 身份证复印件告警9103 身份证翻拍告警9104 临时身份证告警9105 身份证框内遮挡告警9106 身份证 PS 告警可能存在多个 WarnInfos
         */
        public String warnInfos;
    }
    @XmlBean(name = "IdInfo", method = XmlBean.GenerateMethod.FROM)
    public static class AIIDCardOCRResponseIdInfo {
        /**
         * 姓名（人像面）
         */
        public String name;
        /**
         * 性别（人像面）
         */
        public String sex;
        /**
         * 民族（人像面）
         */
        public String nation;
        /**
         * 出生日期（人像面）
         */
        public String birth;
        /**
         * 地址（人像面）
         */
        public String address;
        /**
         * 身份证号（人像面）
         */
        public String idNum;
        /**
         * 发证机关（国徽面）
         */
        public String authority;
        /**
         * 证件有效期（国徽面）
         */
        public String validDate;
    }

}
