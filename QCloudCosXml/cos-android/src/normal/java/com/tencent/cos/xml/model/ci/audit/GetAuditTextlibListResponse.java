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
public class GetAuditTextlibListResponse {
    
    /**
     *请求的唯一ID，排查问题时可提供该值便于快速定位问题。
     */
    public String requestId;

    /**
     *当前查询到的自定义文本库总数。
     */
    public int totalCount;
    /**
     *包含自定义文本库信息的集合。
     */
    @XmlElement(ignoreListNote = true, flatListNote = true)
    public List<Libs> libs;

    @XmlBean(name = "Libs", method = XmlBean.GenerateMethod.FROM)
    public static class Libs {
        /**
         *自定义文本库的ID。
         */
        public String libID;

        /**
         *自定义文本库的名称。
         */
        public String libName;

        /**
         *表示审核的文本命中了该文本库中的关键词时，返回的处理建议。
         */
        public String suggestion;

        /**
         * 当前自定义文本库关联的审核策略信息集合，如果未关联则不返回。
         */
        @XmlElement(ignoreListNote = true, flatListNote = true)
        public List<Strategies> strategies;

        /**
         *表示审核的内容与该文本库中关键词的匹配模式。
         */
        public String matchType;

        /**
         *自定义文本库的创建时间。
         */
        public String createTime;
    }

    @XmlBean(name = "Strategies", method = XmlBean.GenerateMethod.FROM)
    public static class Strategies {
        /**
         *当前BizType所在的存储桶。
         */
        public String bucket;

        /**
         *当前BizType所属的审核服务类型。
         */
        public String service;

        /**
         *当前自定义文本库关联的其中一个BizType值。
         */
        public String bizType;
    }
}
