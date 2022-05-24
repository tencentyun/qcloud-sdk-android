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
 * 音频审核的具体配置项
 */
@XmlBean(name = "Request")
public class PostAudioAudit {
    /**
     * 需要审核的内容
     */
    public AuditInput input;
    /**
     * 音频审核规则配置
     */
    public AudioAuditConf conf;

    public PostAudioAudit() {
        this.input = new AuditInput();
        this.conf = new AudioAuditConf();
    }

    /**
     * 音频审核规则配置
     */
    @XmlBean(name = "Conf")
    public static class AudioAuditConf extends AuditConf {
        /**
         * 回调内容的结构，有效值：Simple（回调内容包含基本信息）、Detail（回调内容包含详细信息）。默认为 Simple
         */
        public String callbackVersion;
    }
}
