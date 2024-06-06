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

public class CreateFileMetaIndex {

    /**
     * 数据集名称，同一个账户下唯一。;是否必传：是
     */
    public String datasetName;

    /**
     * 元数据索引结果（以回调形式发送至您的回调地址，支持以 http:// 或者 https:// 开头的地址，例如： http://www.callback.com。;是否必传：否
     */
    public String callback;

    /**
     * 用于建立索引的文件信息。;是否必传：是
     */
    public File file;

    public static class File {
        /**
         * 自定义ID。该文件索引到数据集后，作为该行元数据的属性存储，用于和您的业务系统进行关联、对应。您可以根据业务需求传入该值，例如将某个URI关联到您系统内的某个ID。推荐传入全局唯一的值。在查询时，该字段支持前缀查询和排序，详情请见字段和操作符的支持列表。   ;是否必传：否
         */
        public String customId;

        /**
         * 自定义标签。您可以根据业务需要自定义添加标签键值对信息，用于在查询时可以据此为筛选项进行检索，详情请见字段和操作符的支持列表。  ;是否必传：否
         */
        public Map<String, String> customLabels;

        /**
         * 自定义标签键 ;是否必传：否
         */
        public String key;

        /**
         * 自定义标签值 ;是否必传：否
         */
        public String value;

        /**
         * 可选项，文件媒体类型，枚举值： image：图片。  other：其他。 document：文档。 archive：压缩包。 video：视频。  audio：音频。  ;是否必传：否
         */
        public String mediaType;

        /**
         * 可选项，文件内容类型（MIME Type），如image/jpeg。  ;是否必传：否
         */
        public String contentType;

        /**
         * 资源标识字段，表示需要建立索引的文件地址，当前仅支持COS上的文件，字段规则：cos:///，其中BucketName表示COS存储桶名称，ObjectKey表示文件完整路径，例如：cos://examplebucket-1250000000/test1/img.jpg。 注意： 1、仅支持本账号内的COS文件 2、不支持HTTP开头的地址;是否必传：是
         */
        public String uRI;

        /**
         * 输入图片中检索的人脸数量，默认值为20，最大值为20。(仅当数据集模板 ID 为 Official:FaceSearch 有效)。;是否必传：否
         */
        public int maxFaceNum;

        /**
         * 自定义人物属性(仅当数据集模板 ID 为 Official:FaceSearch 有效)。;是否必传：否
         */
        public List<Persons> persons;
    }
    public static class Persons {
        /**
         * 自定义人物 ID。;是否必传：否
         */
        public String personId;

    }

   
}
