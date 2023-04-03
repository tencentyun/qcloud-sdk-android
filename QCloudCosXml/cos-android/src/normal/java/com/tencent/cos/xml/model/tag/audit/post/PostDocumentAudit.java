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

package com.tencent.cos.xml.model.tag.audit.post;

import com.tencent.cos.xml.model.tag.audit.bean.AuditConf;
import com.tencent.cos.xml.model.tag.audit.bean.AuditInput;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * 文档审核的具体配置项
 */
@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class PostDocumentAudit {
    /**
     * 文档需要审核的内容
     */
    public DocumentAuditInput input;
    /**
     * 文档审核规则配置
     */
    public AuditConf conf;

    public PostDocumentAudit() {
        this.input = new DocumentAuditInput();
        this.conf = new AuditConf();
    }

    /**
     * 文档需要审核的内容
     */
    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.TO)
    public static class DocumentAuditInput extends AuditInput {
        /**
         * 指定文档文件的类型，如未指定则默认以文件的后缀为类型。
         * 如果文件没有后缀，该字段必须指定，否则会审核失败。例如：doc、docx、ppt、pptx 等。
         */
        public String type;
    }
}
