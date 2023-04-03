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

/**
 * 审核任务结果的详细信息
 */
@XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
public class AuditJobsDetail extends BaseAuditJobsDetail {
    /**
     * 错误码，只有State为 Failed时返回。详情请查看 <a href="https://cloud.tencent.com/document/product/460/42867#.E9.94.99.E8.AF.AF.E7.A0.81.E5.88.97.E8.A1.A8">错误码列表</a>。
     */
    public String code;
    /**
     * 错误描述，只有State为 Failed时返回。
     */
    public String message;
    /**
     * 该字段表示本次判定的审核结果，您可以根据该结果，进行后续的操作；建议您按照业务所需，对不同的审核结果进行相应处理。
     * 有效值：0（审核正常），1 （判定为违规敏感文件），2（疑似敏感，建议人工复核）
     */
    public String result;
    /**
     * 该字段用于返回检测结果中所对应的优先级最高的恶意标签，表示模型推荐的审核结果，建议您按照业务所需，对不同违规类型与建议值进行处理。 返回值：Normal：正常，Porn：色情，Ads：广告，Politics：涉政，Terrorism：暴恐
     */
    public String label;
}
