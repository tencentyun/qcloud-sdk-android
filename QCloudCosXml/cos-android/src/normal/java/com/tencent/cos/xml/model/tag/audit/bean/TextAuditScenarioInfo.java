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
 * 审核的审核结果信息
 */
@XmlBean(method = XmlBean.GenerateMethod.FROM)
public class TextAuditScenarioInfo {
    /**
     * 否命中该审核分类，0表示未命中，1表示命中，2表示疑似
     */
    public int hitFlag;
    /**
     * 该审核的结果分数，分数越高表示越敏感
     */
    public int score;
    /**
     * 在当前审核场景下命中的关键词
     */
    public String keywords;
    /**
     * 该字段表示审核命中的具体审核类别。例如 Moan，表示色情标签中的呻吟类别。注意：该字段可能返回空。
     */
    public String category;
    /**
     * 该字段表示审核命中的具体子标签，例如：Porn 下的 SexBehavior 子标签。注意：该字段可能返回空，表示未命中具体的子标签。
     */
    public String subLabel;
    /**
     * 该字段用于返回基于风险库识别的结果。注意：未命中风险库中样本时，此字段不返回。
     */
    @XmlElement(flatListNote = true)
    public List<LibResults> libResults;
    /**
     * 该字段表示声纹的识别详细结果。注意：未开启该功能时不返回该字段。
     */
    @XmlElement(flatListNote = true)
    public List<Results> speakerResults;
    /**
     * 该字段表示未成年的识别详细结果。注意：未开启该功能时不返回该字段。
     */
    @XmlElement(flatListNote = true)
    public List<Results> recognitionResults;

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class LibResults {
        /**
         * 命中的风险库类型，取值为1（预设黑白库）和2（自定义风险库）。
         */
        @XmlElement(ignoreZero = true)
        public int libType;
        /**
         * 命中的风险库名称。
         */
        public String libName;
        /**
         * 命中的库中关键词。该参数可能会有多个返回值，代表命中的多个关键词。
         */
        @XmlElement(flatListNote = true)
        public List<String> keywords;
    }

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class Results {
        /**
         * 该字段表示对应的识别结果类型信息。
         * 取值含义：
         * cmn	普通话
         * en	英语
         * yue	粤语
         * ja	日语
         * ko	韩语
         * mn	蒙语
         * bo	藏语
         * ug	维语
         * dialect	方言
         */
        public String label;
        /**
         * 该字段表示审核结果命中审核信息的置信度，取值范围：0（置信度最低）-100（置信度最高 ），越高代表音频越有可能属于当前返回的标签。
         */
        public int score;
        /**
         * 该字段表示对应标签的片段在音频文件内的开始时间，单位为毫秒。注意：此字段可能未返回，表示取不到有效值。
         */
        public int startTime;
        /**
         * 该字段表示对应标签的片段在音频文件内的结束时间，单位为毫秒。注意：此字段可能未返回，表示取不到有效值。
         */
        public int endTime;
    }
}
