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
import com.tencent.cos.xml.model.tag.audit.bean.ImageAuditScenarioInfo;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * 文档审核返回的具体响应内容
 */
@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class GetDocumentAuditJobResponse {
    /**
     * 文档审核任务的详细信息
     */
    public DocumentAuditJobsDetail jobsDetail;
    /**
     * 每次请求发送时，服务端将会自动为请求生成一个 ID，遇到问题时，该 ID 能更快地协助定位问题。
     */
    public String requestId;

    /**
     * 文档审核任务的详细信息
     */
    @XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
    public static class DocumentAuditJobsDetail extends AuditJobsDetail {
        /**
         * 该字段表示本次判定的审核结果，您可以根据该结果，进行后续的操作；建议您按照业务所需，对不同的审核结果进行相应处理。
         * 有效值：0（审核正常），1 （判定为违规敏感文件），2（疑似敏感，建议人工复核）
         */
        public int suggestion;
        /**
         * 本次审核的文件名称，创建任务使用 Object 时返回。
         */
        public String object;
        /**
         * 本次审核的文件链接，创建任务使用 Url 时返回
         */
        public String url;
        /**
         * 文档审核会将文档转换为图片进行审核，该字段表示转换的图片总数量。
         */
        public int pageCount;
        /**
         * 该字段用于返回命中的审核场景及对应的结果
         */
        public Labels labels;
        /**
         * 文档转换为图片后，具体每张图片的审核结果信息
         */
        public PageSegment pageSegment;
    }

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class Labels{
        /**
         * 审核场景为涉黄的审核结果信息
         */
        public DocumentAuditScenarioInfo pornInfo;
        /**
         * 审核场景为涉暴恐的审核结果信息
         */
        public DocumentAuditScenarioInfo terrorismInfo;
        /**
         * 审核场景为政治敏感的审核结果信息
         */
        public DocumentAuditScenarioInfo politicsInfo;
        /**
         * 审核场景为广告引导的审核结果信息
         */
        public DocumentAuditScenarioInfo adsInfo;
    }

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class DocumentAuditScenarioInfo{
        /**
         * 否命中该审核分类，0表示未命中，1表示命中，2表示疑似
         */
        public int hitFlag;
        /**
         * 该字段表示审核结果命中审核信息的置信度，取值范围：0（置信度最低）-100（置信度最高 ），越高代表该内容越有可能属于当前返回审核信息
         * 例如：色情 99，则表明该内容非常有可能属于色情内容
         */
        public int score;
    }

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class PageSegment {
        /**
         * 文档转换为图片后，每张图片的详细审核结果信息
         */
        @XmlElement(flatListNote = true)
        public List<Results> results;
    }

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class Results {
        /**
         * 文档转换成图片后，您可以通过该地址查看该图片内容，地址格式为标准 URL 格式。
         * 注意：每次查看数据的有效期为2小时，2小时后如还需查看，请重新发起查询请求。
         */
        public String url;
        /**
         * 该字段用于返回当前图片 OCR 文本识别的检测结果，仅在审核策略开启文本内容检测时返回。
         */
        public String text;
        /**
         * 该图片的页码，通常是文档的页码
         */
        public int pageNumber;
        /**
         * 如果审核的为表格文件，该字段表示表格内的 Sheet 页码
         */
        public int sheetNumber;
        /**
         * 该字段用于返回检测结果中所对应的优先级最高的恶意标签，表示模型推荐的审核结果，建议您按照业务所需，对不同违规类型与建议值进行处理。 返回值：Normal：正常，Porn：色情，Ads：广告，Politics：涉政，Terrorism：暴恐。
         */
        public String label;
        /**
         * 该字段表示本次判定的审核结果，您可以根据该结果，进行后续的操作；建议您按照业务所需，对不同的审核结果进行相应处理。
         * 有效值：0（审核正常），1 （判定为违规敏感文件），2（疑似敏感，建议人工复核）
         */
        public int suggestion;
        /**
         * 审核场景为涉黄的审核结果信息。
         */
        public ImageAuditScenarioInfo pornInfo;
        /**
         * 审核场景为涉暴恐的审核结果信息。
         */
        public ImageAuditScenarioInfo terrorismInfo;
        /**
         * 审核场景为政治敏感的审核结果信息。
         */
        public ImageAuditScenarioInfo politicsInfo;
        /**
         * 审核场景为广告引导的审核结果信息。
         */
        public ImageAuditScenarioInfo adsInfo;
    }
}
