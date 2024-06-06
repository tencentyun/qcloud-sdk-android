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

public class DescribeDatasetResponse {
    
    /**
     * 请求ID
     */
    public String requestId;
    /**
     * 数据集信息
     */
    public Dataset dataset;
    public static class Dataset {
        /**
         *    模板ID。   
         */
        public String templateId;
        /**
         *    数据集描述信息 
         */
        public String description;
        /**
         *  数据集创建时间的时间戳，格式为RFC3339Nano 
         */
        public String createTime;
        /**
         *  数据集修改时间的时间戳，格式为RFC3339Nano创建数据集后，如果未更新过数据集，则数据集修改时间的时间戳和数据集创建时间的时间戳相同 
         */
        public String updateTime;
        /**
         *  数据集当前绑定的COS Bucket数量                               
         */
        public int bindCount;
        /**
         *  数据集当前文件数量                                           
         */
        public int fileCount;
        /**
         *  数据集中当前文件总大小，单位为字节                           
         */
        public int totalFileSize;
        /**
         * 数据集名称
         */
        public String datasetName;
    }

}
