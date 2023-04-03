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

package com.tencent.cos.xml.model.tag.audit.get;


import com.tencent.cos.xml.model.tag.audit.bean.AuditJobsDetail;
import com.tencent.cos.xml.model.tag.audit.bean.AuditScenarioInfo;
import com.tencent.cos.xml.model.tag.audit.bean.AuditSection;
import com.tencent.cos.xml.model.tag.audit.bean.TextAuditScenarioInfo;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * 文本审核返回的具体响应内容
 */
@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class TextAuditJobResponse {
    /**
     * 文本审核任务的详细信息
     */
    public TextAuditJobsDetail jobsDetail;
    /**
     * 每次请求发送时，服务端将会自动为请求生成一个 ID，遇到问题时，该 ID 能更快地协助定位问题。
     */
    public String requestId;

    /**
     * 文本审核任务的详细信息
     */
    @XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
    public static class TextAuditJobsDetail extends AuditJobsDetail {
        /**
         * 文本内容的 Base64 编码。
         */
        public String content;
        /**
         * 审核的文本内容分片数，固定为1。
         */
        public int sectionCount;
        /**
         * 审核场景为涉黄的审核结果信息
         */
        public AuditScenarioInfo pornInfo;
        /**
         * 审核场景为涉暴恐的审核结果信息
         */
        public AuditScenarioInfo terrorismInfo;
        /**
         * 审核场景为政治敏感的审核结果信息
         */
        public AuditScenarioInfo politicsInfo;
        /**
         * 审核场景为广告引导的审核结果信息
         */
        public AuditScenarioInfo adsInfo;
        /**
         * 审核场景为违法的审核结果信息。
         */
        public AuditScenarioInfo illegalInfo;
        /**
         * 审核场景为谩骂的审核结果信息。
         */
        public AuditScenarioInfo abuseInfo;
        /**
         * 文本审核的具体结果信息
         */
        @XmlElement(flatListNote = true)
        public List<Section> section;
    }

    /**
     * 文本审核的具体结果信息
     */
    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class Section extends AuditSection {
        /**
         * 该分片位于文本中的起始位置信息（即10代表第11个utf8文字）。从0开始。
         */
        public int startByte;
        /**
         * 该字段用于返回检测结果中所对应的优先级最高的恶意标签，表示模型推荐的审核结果，建议您按照业务所需，对不同违规类型与建议值进行处理。
         * 返回值：Normal：正常，Porn：色情，Ads：广告，以及其他不安全或不适宜的类型。
         */
        public String label;
        /**
         * 该字段表示本次判定的审核结果，您可以根据该结果，进行后续的操作；建议您按照业务所需，对不同的审核结果进行相应处理。
         * 有效值：0（审核正常），1 （判定为违规敏感文件），2（疑似敏感，建议人工复核）。
         */
        public int result;
        /**
         * 审核场景为违法的审核结果信息。
         */
        public TextAuditScenarioInfo illegalInfo;
        /**
         * 审核场景为谩骂的审核结果信息。
         */
        public TextAuditScenarioInfo abuseInfo;
    }
}
