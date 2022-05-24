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
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量图片审核的具体配置项
 */
@XmlBean(name = "Request")
public class PostImagesAudit {
    /**
     * 需要审核的内容，如有多个图片，请传入多个 Input 结构。
     */
    @XmlElement(ignoreListNote = true)
    public List<ImagesAuditInput> input;
    /**
     * 批量图片审核规则配置
     */
    public AuditConf conf;

    public PostImagesAudit() {
        this.input = new ArrayList<>();
        this.conf = new AuditConf();
    }

    /**
     * 批量图片需要审核的内容
     */
    @XmlBean(name = "Input")
    public static class ImagesAuditInput extends AuditInput {
        /**
         * 截帧频率，GIF 图检测专用，默认值为5，表示从第一帧（包含）开始每隔5帧截取一帧
         */
        @XmlElement(ignoreZero = true)
        public int interval;

        /**
         * 最大截帧数量，GIF 图检测专用，默认值为5，表示只截取 GIF 的5帧图片进行审核，必须大于0
         */
        @XmlElement(ignoreZero = true)
        public int maxFrames;
    }
}
