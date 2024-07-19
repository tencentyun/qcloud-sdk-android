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
public class AILicenseRecResponse {
    
    /**
     * 卡证识别结果，1：识别到结果 0：未识别到结果
     */
    public int status;
    /**
     * 卡证识别信息。
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<AILicenseRecResponseIdInfo> idInfo;
    @XmlBean(name = "IdInfo", method = XmlBean.GenerateMethod.FROM)
    public static class AILicenseRecResponseIdInfo {
        /**
         * 字段的名称
         */
        public String name;
        /**
         * 字段的值
         */
        public String detectedText;
        /**
         * 字段的置信度，取值范围为[0-100]，值越高概率越大
         */
        public int score;
        /**
         * 驾驶证中识别到字段的坐标
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<AILicenseRecResponseLocation> location;
    }
    @XmlBean(name = "Location", method = XmlBean.GenerateMethod.FROM)
    public static class AILicenseRecResponseLocation {
        /**
         * 驾驶证信息 坐标点（X 坐标，Y 坐标）
         */
        public String point;
    }

}
