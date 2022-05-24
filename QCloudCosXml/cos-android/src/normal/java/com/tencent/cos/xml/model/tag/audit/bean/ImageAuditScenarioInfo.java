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

package com.tencent.cos.xml.model.tag.audit.bean;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * 图片审核的审核结果信息
 */
@XmlBean
public class ImageAuditScenarioInfo {
    /**
     * 否命中该审核分类，0表示未命中，1表示命中，2表示疑似
     */
    public int hitFlag;
    /**
     * 该字段表示审核结果命中审核信息的置信度，取值范围：0（置信度最低）-100（置信度最高 ），越高代表该内容越有可能属于当前返回审核信息。
     * 例如：色情 99，则表明该内容非常有可能属于色情内容。
     */
    public int score;
    /**
     * 该字段表示审核命中的具体子标签，例如：Porn 下的 SexBehavior 子标签。
     * 注意：该字段可能返回空，表示未命中具体的子标签。
     */
    public String subLabel;
    /**
     * 该字段表示 OCR 文本识别的详细检测结果，包括文本坐标信息、文本识别结果等信息
     */
    @XmlElement(flatListNote = true)
    public List<AuditOcrResults> ocrResults;
    /**
     * 该字段表示审核到的一些具体结果，例如政治人物名称。注意：该字段仅在 PoliticsInfo 中返回。
     */
    @XmlElement(flatListNote = true)
    public List<ObjectResults> objectResults;

    /**
     * 该字段表示审核到的一些具体结果，例如政治人物名称。注意：该字段仅在 PoliticsInfo 中返回。
     */
    @XmlBean
    public static class ObjectResults{
        /**
         * 该标签用于返回所识别出的实体名称，例如人名。
         * 注意：此字段可能返回 null，表示取不到有效值
         */
        public String name;
        /**
         * 该参数用于返回 OCR 检测框在图片中的位置（左上角 xy 坐标、长宽、旋转角度），以方便快速定位识别文字的相关信息。
         */
        public AuditOcrLocation location;
    }
}