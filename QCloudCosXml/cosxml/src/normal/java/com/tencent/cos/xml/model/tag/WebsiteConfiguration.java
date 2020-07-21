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

package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * 存储桶关联的静态网站配置信息
 */
public class WebsiteConfiguration {
    /**
     * 索引文档配置
     */
    public IndexDocument indexDocument;
    /**
     * 错误文档配置
     */
    public ErrorDocument errorDocument;
    /**
     * 重定向所有请求配置
     */
    public RedirectAllRequestTo redirectAllRequestTo;
    /**
     * 重定向规则配置
     */
    public List<RoutingRule> routingRules;

    /**
     * 索引文档配置
     */
    public static class IndexDocument{
        /**
         * 指定索引文档的对象键后缀。
         * 例如指定为index.html，那么当访问到存储桶的根目录时，会自动返回 index.html 的内容，
         * 或者当访问到article/目录时，会自动返回article/index.html的内容
         */
        public String suffix;
    }

    /**
     * 错误文档配置
     */
    public static class ErrorDocument{
        /**
         * 指定通用错误文档的对象键
         */
        public String key;
    }

    /**
     * 重定向所有请求配置
     */
    public static class RedirectAllRequestTo{
        /**
         * 指定重定向所有请求的目标协议
         */
        public String protocol;
    }

    /**
     * 重定向规则配置
     */
    public static class RoutingRule{
        /**
         * 重定向规则的条件配置
         */
        public Contidion contidion;
        /**
         * 重定向规则的具体重定向目标配置
         */
        public Redirect redirect;
    }

    /**
     * 重定向规则的条件配置
     */
    public static class Contidion{
        /**
         * 指定重定向规则的错误码匹配条件
         */
        public int httpErrorCodeReturnedEquals = -1;
        /**
         * 指定重定向规则的对象键前缀匹配条件
         */
        public String keyPrefixEquals;
    }

    /**
     * 重定向规则的具体重定向目标配置
     */
    public static class Redirect{
        /**
         * 指定重定向规则的目标协议
         */
        public String protocol;
        /**
         * 指定重定向规则的具体重定向目标的对象键，替换方式为替换整个原始请求的对象键
         */
        public String replaceKeyWith;
        /**
         * 指定重定向规则的具体重定向目标的对象键，替换方式为替换原始请求中所匹配到的前缀部分
         */
        public String replaceKeyPrefixWith;
    }
}
