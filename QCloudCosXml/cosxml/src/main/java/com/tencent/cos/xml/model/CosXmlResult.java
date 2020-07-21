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

package com.tencent.cos.xml.model;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.qcloud.core.http.HttpResponse;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *  COS返回结果基类
 * </p>
 */

public abstract class CosXmlResult {
    /**
     * http状态码
     */
    public int httpCode;
    /**
     * http状态信息
     */
    public String httpMessage;
    /**
     * http headers
     */
    public Map<String, List<String>> headers;
    /**
     * 访问 object 的地址.
     */
    public String accessUrl;

    /**
     * 根据HttpResponse解析相关内容
     * @param response http响应
     * @throws CosXmlClientException 客户端异常
     * @throws CosXmlServiceException 服务端异常
     */
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        httpCode = response.code();
        httpMessage = response.message();
        headers = response.headers();
    }

    /**
     * 返回转换后的字符串
     */
    public String printResult(){
        return httpCode + "|" + httpMessage;
    }
}
