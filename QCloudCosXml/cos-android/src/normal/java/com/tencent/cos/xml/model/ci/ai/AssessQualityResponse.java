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
public class AssessQualityResponse {
    
    /**
     * 取值为 TRUE 或 FALSE，TRUE 为长图，FALSE 为正常图，长图定义为长宽比大于等于3或小于等于1/3的图片。
     */
    public boolean longImage;
    /**
     * 取值为 TRUE 或 FALSE，TRUE 为黑白图，FALSE 为否。黑白图即灰度图，指红绿蓝三个通道都是以灰度色阶显示的图片，并非视觉上的“黑白图片”。
     */
    public boolean blackAndWhite;
    /**
     * 取值为 TRUE 或 FALSE，TRUE 为小图，FALSE 为否, 小图定义为最长边小于179像素的图片。当一张图片被判断为小图时，不建议做推荐和展示，其他字段统一输出为0或 FALSE。
     */
    public boolean smallImage;
    /**
     * 取值为 TRUE 或 FALSE，TRUE 为大图，FALSE 为否，定义为最短边大于1000像素的图片
     */
    public boolean bigImage;
    /**
     * 取值为 TRUE 或 FALSE，TRUE 为纯色图或纯文字图，即没有内容或只有简单内容的图片，FALSE 为正常图片。
     */
    public boolean pureImage;
    /**
     * 综合评分。图像清晰度的得分，对图片的噪声、曝光、模糊、压缩等因素进行综合评估，取值为[0, 100]，值越大，越清晰。一般大于50为较清晰图片，标准可以自行把握。
     */
    public int clarityScore;
    /**
     * 综合评分。图像美观度得分，从构图、色彩等多个艺术性维度评价图片，取值为[0, 100]，值越大，越美观。一般大于50为较美观图片，标准可以自行把握。
     */
    public int aestheticScore;
    /**
     * 
     */
    public int lowQualityScore;
    /**
     * 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
    public String requestId;

}
