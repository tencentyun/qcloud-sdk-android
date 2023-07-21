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

package com.tencent.cos.xml.model.ci.audit;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class PostTextAuditReport {

    /**
     *需要反馈的数据类型，错误文本样本取值为1。;是否必传：是;
     */
    public int contentType = 1;

    /**
     *文本类型的样本，需要填写 base64 的文本内容，ContentType 为1时必填。;是否必传：否;
     */
    public String text;

    /**
     *数据万象审核判定的审核结果标签，例如 Porn。;是否必传：是;
     */
    public String label;

    /**
     *您自己期望的正确审核结果的标签，例如期望是正常，则填 Normal。;是否必传：是;
     */
    public String suggestedLabel;

    /**
     *该数据样本对应的审核任务 ID，有助于定位审核记录。;是否必传：否;
     */
    public String jobId;

    /**
     *该数据样本之前审核的时间，有助于定位审核记录。 格式为 2021-08-07T12:12:12+08:00;是否必传：否;
     */
    public String moderationTime;


   
}
