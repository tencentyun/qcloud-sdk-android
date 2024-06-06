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

public class SearchImage {

    /**
     * 数据集名称，同一个账户下唯一。;是否必传：是
     */
    public String datasetName;

    /**
     * 指定检索方式为图片或文本，pic 为图片检索，text 为文本检索，默认为 pic。;是否必传：否
     */
    public String mode;

    /**
     * 资源标识字段，表示需要建立索引的文件地址(Mode 为 pic 时必选)。;是否必传：否
     */
    public String uRI;

    /**
     * 返回相关图片的数量，默认值为10，最大值为100。;是否必传：否
     */
    public int limit;

    /**
     * 检索语句，检索方式为 text 时必填，最多支持60个字符 (Mode 为 text 时必选)。;是否必传：否
     */
    public String text;

    /**
     * 出参 Score（相关图片匹配得分） 中，只有超过 MatchThreshold 值的结果才会返回。默认值为0，推荐值为80。;是否必传：否
     */
    public int matchThreshold;


   
}
