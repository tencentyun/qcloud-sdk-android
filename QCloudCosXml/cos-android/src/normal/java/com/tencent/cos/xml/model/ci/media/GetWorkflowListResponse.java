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

package com.tencent.cos.xml.model.ci.media;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class GetWorkflowListResponse {
    
    /**
     *请求的唯一 ID
     */
    public String requestId;

    /**
     *工作流总数
     */
    public int totalCount;

    /**
     *当前页数，同请求中的 pageNumber
     */
    public int pageNumber;

    /**
     *每页个数，同请求中的 pageSize
     */
    public int pageSize;

    /**
     *工作流数组
     */
    @XmlElement(ignoreListNote = true, flatListNote = true)
    public List<GetWorkflowListResponseMediaWorkflowList> mediaWorkflowList;

    @XmlBean(name = "MediaWorkflowList", method = XmlBean.GenerateMethod.FROM)
    public static class GetWorkflowListResponseMediaWorkflowList {
        /**
         *工作流名称
         */
        public String name;

        /**
         *工作流 ID
         */
        public String workflowId;

        /**
         *工作流状态
         */
        public String state;

        /**
         *创建时间
         */
        public String createTime;

        /**
         *更新时间
         */
        public String updateTime;

        /**
         * 拓扑信息
         * 由于结构比较复杂，且类型字段不固定，因此此处返回原始的xml字符串
         */
        // TODO: 2023/7/20 待originalXmlString支持
//        @XmlElement(originalXmlString = true)
//        public String topology;
    }


}
