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
 * 片段审核结果
 */
@XmlBean(method = XmlBean.GenerateMethod.FROM)
public class AuditSection {
    /**
     * 审核场景为涉黄的审核结果信息。
     */
    public TextAuditScenarioInfo pornInfo;
    /**
     * 审核场景为涉暴恐的审核结果信息。
     */
    public TextAuditScenarioInfo terrorismInfo;
    /**
     * 审核场景为政治敏感的审核结果信息。
     */
    public TextAuditScenarioInfo politicsInfo;
    /**
     * 审核场景为广告引导的审核结果信息。
     */
    public TextAuditScenarioInfo adsInfo;
    /**
     * 审核场景为未成年的审核结果信息。
     */
    public TextAuditScenarioInfo teenagerInfo;
}
