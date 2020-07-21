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
 * 跨域资源共享配置
 */
public class CORSConfiguration {
    /**
     * 跨域资源共享配置
     * 最多可以包含100条 CORSRule
     */
    public List<CORSRule> corsRules;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{CORSConfiguration:\n");
        if(corsRules != null){
            for(CORSRule corsRule : corsRules){
                if(corsRule != null) stringBuilder.append(corsRule.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class CORSRule{
        /**
         * 配置规则的 ID，是否包含该字段取决于 PUT Bucket cors 时是否指定 ID 字段
         */
        public String id;
        /**
         * 允许的访问来源，支持通配符*
         * 格式为：协议://域名[:端口]，例如：http://www.qq.com
         */
        public String allowedOrigin;
        /**
         * 允许的 HTTP 操作，枚举值：GET，PUT，HEAD，POST，DELETE
         */
        public List<String> allowedMethod;
        /**
         * 在发送 OPTIONS 请求时告知服务端，接下来的请求可以使用哪些自定义的 HTTP 请求头部，支持通配符*
         */
        public List<String> allowedHeader;
        /**
         * 设置浏览器可以接收到的来自服务器端的自定义头部信息
         */
        public List<String> exposeHeader;
        /**
         * 设置 OPTIONS 请求得到结果的有效期
         */
        public int maxAgeSeconds;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{CORSRule:\n");
            stringBuilder.append("ID:").append(id).append("\n");
            stringBuilder.append("AllowedOrigin:").append(allowedOrigin).append("\n");
            if(allowedMethod != null){
                for (String method : allowedMethod){
                    if(method != null)stringBuilder.append("AllowedMethod:").append(method).append("\n");
                }
            }
            if(allowedHeader != null){
                for (String header : allowedHeader){
                    if(header != null)stringBuilder.append("AllowedHeader:").append(header).append("\n");
                }
            }
            if(exposeHeader != null){
                for (String exposeHeader : exposeHeader){
                    if(exposeHeader != null)stringBuilder.append("ExposeHeader:").append(exposeHeader).append("\n");
                }
            }
            stringBuilder.append("MaxAgeSeconds:").append(maxAgeSeconds).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}
