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
import java.util.Map;

public class DatasetSimpleQueryResponse {
    
    /**
     * 请求ID
     */
    public String requestId;
    /**
     * 文件信息列表。仅在请求的Aggregations为空时返回。
     */
    public List<FileResult> files;
    /**
     * 聚合字段信息列表。仅在请求的Aggregations不为空时返回。
     */
    public List<AggregationsResult> aggregations;
    /**
     * 翻页标记。当文件总数大于设置的MaxResults时，用于翻页的Token。符合条件的文件信息未全部返回时，此参数才有值。下一次列出文件信息时将此值作为NextToken传入，将后续的文件信息返回。
     */
    public String nextToken;
    public static class AggregationsResult {
        /**
         *  聚合字段的聚合操作符。      
         */
        public String operation;
        /**
         *  聚合的统计结果。     
         */
        public float value;
        /**
         *  分组聚合的结果列表。仅在请求的Aggregations中存在group类型的Operation时才会返回。
         */
        public List<Groups> groups;
        /**
         * 聚合字段名称。
         */
        public String field;
    }
    public static class Groups {
        /**
         *  分组聚合的总个数。      
         */
        public int count;
        /**
         * 分组聚合的值。
         */
        public String value;
    }
    public static class FileResult {
        /**
         * 对象唯一ID。      
         */
        public String objectId;
        /**
         *  元数据创建时间的时间戳，格式为RFC3339Nano 
         */
        public String createTime;
        /**
         *  元数据修改时间的时间戳，格式为RFC3339Nano创建元数据后，如果未更新过元数据，则元数据修改时间的时间戳和元数据创建时间的时间戳相同 
         */
        public String updateTime;
        /**
         *   资源标识字段，表示需要建立索引的文件地址  
         */
        public String uRI;
        /**
         *   文件路径  
         */
        public String filename;
        /**
         *    文件媒体类型。 枚举值：image：图片。other：其他。document：文档。archive：压缩包。audio：音频。video：视频。
         */
        public String mediaType;
        /**
         *   文件内容类型（MIME Type）。
         */
        public String contentType;
        /**
         *   文件存储空间类型。
         */
        public String cOSStorageClass;
        /**
         *    文件CRC64值。 
         */
        public String cOSCRC64;
        /**
         *    文件大小，单位为字节。 
         */
        public int size;
        /**
         *    指定Object被下载时网页的缓存行为。该字段需要设置COS Object HTTP属性Cache-Control。 
         */
        public String cacheControl;
        /**
         *    指定Object被下载时的名称。需要设置COS Object HTTP属性Content-Disposition。 
         */
        public String contentDisposition;
        /**
         *    指定该Object被下载时的内容编码格式。需要设置COS Object HTTP属性Content-Encoding。 
         */
        public String contentEncoding;
        /**
         *    Object内容使用的语言。需要设置COS Object HTTP属性Content-Language。 
         */
        public String contentLanguage;
        /**
         *    加密算法,需要设置x-cos-server-side-encryption。 
         */
        public String serverSideEncryption;
        /**
         *    Object生成时会创建相应的ETag ，ETag用于标识一个Object的内容。 
         */
        public String eTag;
        /**
         *   文件最近一次修改时间的时间戳， 格式为RFC3339Nano。
         */
        public String fileModifiedTime;
        /**
         *   该文件的自定义ID。该文件索引到数据集后，作为该行元数据的属性存储，用于和您的业务系统进行关联、对应。您可以根据业务需求传入该值，例如将某个URI关联到您系统内的某个ID。推荐传入全局唯一的值。
         */
        public String customId;
        /**
         *   文件自定义标签列表。储存您业务自定义的键名、键值对信息，用于在查询时可以据此为筛选项进行检索。
         */
        public Map<String, String> customLabels;
        /**
         *   cos自定义头部。储存您业务在cos object上的键名、键值对信息，用于在查询时可以据此为筛选项进行检索。
         */
        public Map<String, String> cOSUserMeta;
        /**
         *    文件访问权限属性。 
         */
        public String objectACL;
        /**
         *   cos自定义标签。储存您业务在cos object上的自定义标签的键名、键值对信息，用于在查询时可以据此为筛选项进行检索。
         */
        public Map<String, String> cOSTagging;
        /**
         *    cos自定义标签的数量。 
         */
        public int cOSTaggingCount;
        /**
         * 数据集名称。
         */
        public String datasetName;
    }

}
