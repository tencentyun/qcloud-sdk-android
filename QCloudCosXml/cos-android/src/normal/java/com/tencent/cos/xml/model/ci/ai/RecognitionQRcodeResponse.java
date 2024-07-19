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
public class RecognitionQRcodeResponse {
    
    /**
     * 二维码识别结果。0表示未识别到二维码，1表示识别到二维码
     */
    public int codeStatus;
    /**
     * 二维码识别结果，可能有多个
     */
    public RecognitionQRcodeResponseQRcodeInfo qRcodeInfo;
    /**
     * 处理后的图片 base64数据，请求参数 cover 为1时返回
     */
    public String resultImage;
    @XmlBean(name = "QRcodeInfo", method = XmlBean.GenerateMethod.FROM)
    public static class RecognitionQRcodeResponseQRcodeInfo {
        /**
         * 二维码的内容。可能识别不到内容
         */
        public String codeUrl;
        /**
         * 图中识别到的二维码位置坐标
         */
        @XmlElement(flatListNote = true, ignoreListNote = true)
        public List<RecognitionQRcodeResponseCodeLocation> codeLocation;
    }
    @XmlBean(name = "CodeLocation", method = XmlBean.GenerateMethod.FROM)
    public static class RecognitionQRcodeResponseCodeLocation {
        /**
         * 二维码坐标点（X坐标,Y坐标）
         */
        public String point;
    }

}
