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

package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.http.HttpResponse;

import java.util.Arrays;
import java.util.List;

/**
 * 对象跨域访问配置预请求的返回结果.
 * @see com.tencent.cos.xml.CosXml#optionObject(OptionObjectRequest)
 * @see OptionObjectRequest
 */
final public class OptionObjectResult extends CosXmlResult {
    /**
     * 模拟跨域访问的请求来源域名，当来源不允许的时候，此 Header 不返回
     */
    public String accessControlAllowOrigin;
    /**
     * 模拟跨域访问的请求 HTTP 方法，当请求方法不允许的时候，此 Header 不返回
     */
    public List<String> accessControlAllowMethods;
    /**
     * 模拟跨域访问的请求头部，当模拟任何请求头部不允许的时候，此 Header 不返回
     */
    public List<String> accessControlAllowHeaders;
    /**
     * 模拟跨域访问的响应列出头部，当模拟任何响应列出头部不允许的时候，此 Header 不返回
     */
    public List<String> accessControlExposeHeaders;
    /**
     * 设置 OPTIONS 请求得到结果的有效期
     */
    public long accessControlMaxAge;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlServiceException, CosXmlClientException {
        super.parseResponseBody(response);
        accessControlAllowOrigin = response.header("Access-Control-Allow-Origin");
        if(response.header("Access-Control-Max-Age") != null){
            accessControlMaxAge = Long.parseLong(response.header("Access-Control-Max-Age"));
        }
        if(response.header("Access-Control-Allow-Methods") != null){
            accessControlAllowMethods = Arrays.asList(response.header("Access-Control-Allow-Methods").split(","));
        }
        if(response.header("Access-Control-Allow-Headers") != null){
            accessControlAllowHeaders = Arrays.asList(response.header("Access-Control-Allow-Headers").split(","));
        }
        if(response.header("Access-Control-Expose-Headers") != null){
            accessControlExposeHeaders = Arrays.asList(response.header("Access-Control-Expose-Headers").split(","));
        }
    }

    @Override
    public String printResult() {
        return super.printResult() + "\n"
                + accessControlAllowOrigin + "\n"
                + accessControlMaxAge + "\n";
    }
}
