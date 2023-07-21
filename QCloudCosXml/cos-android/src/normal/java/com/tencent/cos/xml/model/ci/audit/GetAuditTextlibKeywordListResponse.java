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
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class GetAuditTextlibKeywordListResponse {
    
    /**
     *请求的唯一ID，排查问题时可提供该值便于快速定位问题。
     */
    public String requestId;

    /**
     *当前查询到的自定义文本库关键字总数。
     */
    public int totalCount;
    /**
     *包含文本库关键词信息的集合。
     */
    @XmlElement(ignoreListNote = true, flatListNote = true)
    public List<Keywords> keywords;

    @XmlBean(name = "Keywords", method = XmlBean.GenerateMethod.FROM)
    public static class Keywords {
        /**
         *文本库关键词的ID。
         */
        public String keywordID;

        /**
         *该关键词的内容。
         */
        public String content;

        /**
         *该关键词所属的标签类型。
         */
        public String label;

        /**
         *该关键词的备注信息。
         */
        public String remark;

        /**
         *该关键词的创建时间。
         */
        public String createTime;
    }
}
