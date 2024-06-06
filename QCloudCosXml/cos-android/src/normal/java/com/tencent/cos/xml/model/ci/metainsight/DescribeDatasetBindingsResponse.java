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

package com.tencent.cos.xml.model.ci.metainsight;

import java.util.List;

public class DescribeDatasetBindingsResponse {
    
    /**
     * 请求ID
     */
    public String requestId;
    /**
     * 当绑定关系总数大于设置的MaxResults时，用于翻页的token。下一次列出绑定关系信息时以此值为NextToken，将未返回的结果返回。当绑定关系未全部返回时，此参数才有值。
     */
    public String nextToken;
    /**
     * 数据集和 COS Bucket 绑定关系信息的列表。
     */
    public List<Binding> bindings;
    public static class Binding {
        /**
         *  资源标识字段，表示需要与数据集绑定的资源，当前仅支持COS存储桶，字段规则：cos://，其中BucketName表示COS存储桶名称，例如：cos://examplebucket-1250000000 
         */
        public String uRI;
        /**
         *  数据集和 COS Bucket绑定关系的状态。取值范围如下：Running：绑定关系运行中。 
         */
        public String state;
        /**
         *  数据集和 COS Bucket绑定关系创建时间的时间戳，格式为RFC3339Nano。 
         */
        public String createTime;
        /**
         *  数据集和 COS Bucket的绑定关系修改时间的时间戳，格式为RFC3339Nano。创建绑定关系后，如果未暂停或者未重启过绑定关系，则绑定关系修改时间的时间戳和绑定关系创建时间的时间戳相同。 
         */
        public String updateTime;
        /**
         * 数据集名称。
         */
        public String datasetName;
        /**
         * 详情
         */
        public String detail;
    }

}
