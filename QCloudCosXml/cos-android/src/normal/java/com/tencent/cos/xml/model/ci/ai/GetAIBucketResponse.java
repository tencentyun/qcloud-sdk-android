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

package com.tencent.cos.xml.model.ci.ai;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class GetAIBucketResponse {
    
    /**
     * 请求的唯一 ID
     */
    public String requestId;
    /**
     * 开通AI 内容识别（异步）的 Bucket 总数
     */
    public int totalCount;
    /**
     * 当前页数，同请求中的 pageNumber
     */
    public int pageNumber;
    /**
     * 每页个数，同请求中的 pageSize
     */
    public int pageSize;
    /**
     * AI 内容识别（异步） Bucket 详情
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<GetAIBucketResponseAiBucketList> aiBucketList;
    @XmlBean(name = "AiBucketList", method = XmlBean.GenerateMethod.FROM)
    public static class GetAIBucketResponseAiBucketList {
        /**
         * 存储桶 ID
         */
        public String bucketId;
        /**
         * 存储桶名称，同 BucketId
         */
        public String name;
        /**
         * 所在的地域
         */
        public String region;
        /**
         * 创建时间
         */
        public String createTime;
    }

}
