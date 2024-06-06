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

public class DatasetSimpleQuery {

    /**
     * 数据集名称，同一个账户下唯一。;是否必传：是
     */
    public String datasetName;

    /**
     * 简单查询参数条件，可自嵌套。;是否必传：是
     */
    public Query query;

    /**
     * 返回文件元数据的最大个数，取值范围为0200。 使用聚合参数时，该值表示返回分组的最大个数，取值范围为02000。 不设置此参数或者设置为0时，则取默认值100。;是否必传：否
     */
    public int maxResults;

    /**
     * 当绑定关系总数大于设置的MaxResults时，用于翻页的token。从NextToken开始按字典序返回绑定关系信息列表。第一次调用此接口时，设置为空。;是否必传：否
     */
    public String nextToken;

    /**
     * 排序字段列表。请参考字段和操作符的支持列表。 多个排序字段可使用半角逗号（,）分隔，例如：Size,Filename。 最多可设置5个排序字段。 排序字段顺序即为排序优先级顺序。;是否必传：是
     */
    public String sort;

    /**
     * 排序字段的排序方式。取值如下： asc：升序； desc（默认）：降序。 多个排序方式可使用半角逗号（,）分隔，例如：asc,desc。 排序方式不可多于排序字段，即参数Order的元素数量需小于等于参数Sort的元素数量。例如Sort取值为Size,Filename时，Order可取值为asc,desc或asc。 排序方式少于排序字段时，未排序的字段默认取值asc。例如Sort取值为Size,Filename，Order取值为asc时，Filename默认排序方式为asc，即升序排列;是否必传：是
     */
    public String order;

    /**
     * 聚合字段信息列表。 当您使用聚合查询时，仅返回聚合结果，不再返回匹配到的元信息列表。;是否必传：是
     */
    public Aggregations aggregations;

    /**
     * 仅返回特定字段的值，而不是全部已有的元信息字段。可用于降低返回的结构体大小。不填或留空则返回所有字段。;是否必传：是
     */
    public String withFields;

    public static class Aggregations {
        /**
         *  聚合字段的操作符。枚举值：min：最小值。max：最大值。average：平均数sum：求和。count：计数。distinct：去重计数。group：分组计数，按照分组计数结果从高到低排序。 ;是否必传：否
         */
        public String operation;

        /**
         * 字段名称。关于支持的字段，请参考字段和操作符的支持列表。;是否必传：否
         */
        public String field;

    }
    public static class Query {
        /**
         * 操作运算符。枚举值： not：逻辑非。 or：逻辑或。 and：逻辑与。 lt：小于。 lte：小于等于。 gt：大于。 gte：大于等于。 eq：等于。 exist：存在性查询。 prefix：前缀查询。 match-phrase：字符串匹配查询。 nested：字段为数组时，其中同一对象内逻辑条件查询。;是否必传：是
         */
        public String operation;

        /**
         * 子查询的结构体。 只有当Operations为逻辑运算符（and、or、not或nested）时，才能设置子查询条件。 在逻辑运算符为and/or/not时，其SubQueries内描述的所有条件需符合父级设置的and/or/not逻辑关系。 在逻辑运算符为nested时，其父级的Field必须为一个数组类的字段（如：Labels）。 子查询条件SubQueries组的Operation必须为and/or/not中的一个或多个，其Field必须为父级Field的子属性。;是否必传：否
         */
        public List<SubQueries> subQueries;
        /**
         * 字段名称。关于支持的字段，请参考字段和操作符的支持列表。;是否必传：否
         */
        public String field;

        /**
         * 查询的字段值。当Operations为逻辑运算符（and、or、not或nested）时，该字段无效。;是否必传：否
         */
        public String value;

    }
    public static class SubQueries {
        /**
         * 查询的字段值。当Operations为逻辑运算符（and、or、not或nested）时，该字段无效。;是否必传：否
         */
        public String value;

        /**
         *  操作运算符。枚举值：not：逻辑非。or：逻辑或。and：逻辑与。lt：小于。lte：小于等于。gt：大于。gte：大于等于。eq：等于。exist：存在性查询。prefix：前缀查询。match-phrase：字符串匹配查询。nested：字段为数组时，其中同一对象内逻辑条件查询。 ;是否必传：是
         */
        public String operation;

        /**
         * 字段名称。关于支持的字段，请参考字段和操作符的支持列表。;是否必传：否
         */
        public String field;

    }

   
}
