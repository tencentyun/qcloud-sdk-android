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
public class RecognizeLogoResponse {
    
    /**
     * Logo识别结果，可能有多个
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<RecognizeLogoResponseLogoInfo> logoInfo;
    /**
     * Logo识别结果。0表示未识别到，1表示识别到
     */
    public int status;
    @XmlBean(name = "LogoInfo", method = XmlBean.GenerateMethod.FROM)
    public static class RecognizeLogoResponseLogoInfo {
        /**
         * Logo的名称
         */
        public String name;
        /**
         * Logo的置信度，取值范围为[0-100]。值越高概率越大。
         */
        public int score;
        /**
         * 图中识别到Logo的坐标
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<RecognizeLogoResponseLocation> location;
    }
    @XmlBean(name = "Location", method = XmlBean.GenerateMethod.FROM)
    public static class RecognizeLogoResponseLocation {
        /**
         * Logo坐标点（X坐标,Y坐标）
         */
        public String point;
    }

}
