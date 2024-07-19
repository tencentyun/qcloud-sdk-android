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
public class GetSearchImageResponse {
    
    /**
     * 请求的唯一 ID
     */
    public String requestId;
    /**
     * 数量
     */
    public int count;
    /**
     * 图片信息
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<GetSearchImageResponseImageInfos> imageInfos;
    @XmlBean(name = "ImageInfos", method = XmlBean.GenerateMethod.FROM)
    public static class GetSearchImageResponseImageInfos {
        /**
         * 物品 ID
         */
        public String entityId;
        /**
         * 图片自定义的内容
         */
        public String customContent;
        /**
         * 自定义标签信息，最多不超过10个，json 字符串，格式为 key:value （例 key1>=1 key1>='aa' ）对
         */
        public String tags;
        /**
         * 图片名称
         */
        public String picName;
        /**
         * 相似度
         */
        public int score;
    }

}
