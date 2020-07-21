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

import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * 对象跨域访问配置预请求的请求.
 * @see com.tencent.cos.xml.CosXml#optionObject(OptionObjectRequest)
 * @see com.tencent.cos.xml.CosXml#optionObjectAsync(OptionObjectRequest, CosXmlResultListener)
 */
final public class OptionObjectRequest extends ObjectRequest {
    private String origin;
    private String accessControlMethod;
    private String accessControlHeaders;

    /**
     *
     * @param bucket 存储桶名
     * @param cosPath 对象cos上的路径
     * @param origin 请求来源域名
     * @param accessControlMethod 跨域访问的请求 HTTP 方法
     */
    public OptionObjectRequest(String bucket, String cosPath, String origin, String accessControlMethod){
        super(bucket, cosPath);
        this.origin = origin;
        this.accessControlMethod = accessControlMethod;
        setOrigin(origin);
        setAccessControlMethod(accessControlMethod);
    }

    @Override
    public String getMethod() {
        return RequestMethod.OPTIONS;
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(origin == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "option request origin must not be null");
        }
        if(accessControlMethod == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "option request accessControlMethod must not be null");
        }
    }

    /**
     * <p>
     * 设置模拟跨域访问的请求来源域名。
     * </p>
     *
     * @param origin 请求来源域名
     */
    public void setOrigin(String origin) {
        this.origin = origin;
        if(origin != null){
            addHeader(COSRequestHeaderKey.ORIGIN,origin);
        }
    }

    /**
     * 获取设置的模拟跨域访问的请求来源域名。
     *
     * @return 请求来源域名
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * 设置模拟跨域访问的请求 HTTP 方法
     *
     * @param accessControlMethod 请求 HTTP 方法
     */
    public void setAccessControlMethod(String accessControlMethod) {
        if(accessControlMethod != null){
            this.accessControlMethod = accessControlMethod.toUpperCase();
            addHeader(COSRequestHeaderKey.ACCESS_CONTROL_REQUEST_METHOD,this.accessControlMethod);
        }
    }

    /**
     * 获取设置的模拟跨域访问的请求 HTTP 方法
     *
     * @return 请求 HTTP 方法
     */
    public String getAccessControlMethod() {
        return accessControlMethod;
    }

    /**
     * 设置模拟跨域访问的请求头部
     *
     * @param accessControlHeaders 模拟跨域访问的请求头部
     */
    public void setAccessControlHeaders(String accessControlHeaders) {
        this.accessControlHeaders = accessControlHeaders;
        if(accessControlHeaders != null){
            addHeader(COSRequestHeaderKey.ACCESS_CONTROL_REQUEST_HEADERS,accessControlHeaders);
        }
    }

    /**
     * 获取用户设置的模拟跨域访问的请求头部
     *
     * @return 模拟跨域访问的请求头部
     */
    public String getAccessControlHeaders() {
        return accessControlHeaders;
    }

}
