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

package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpRequest;

/**
 * 签名原料提供器<br>
 * 提供用于签名的相关字段
 */
public interface QCloudSignSourceProvider {
    /**
     * 获取请求中参与签名的字段生成的结果字符串
     * @param request 请求
     * @param <T> 相关请求类型
     * @return 参与签名字段拼接的结果字符串
     * @throws QCloudClientException 客户端异常
     */
    <T> String source(HttpRequest<T> request) throws QCloudClientException;

    /**
     * 请求签名成功
     * @param request 请求
     * @param credentials 签名证书
     * @param authorization 签名后的授权结果
     * @param <T> 相关请求类型
     * @throws QCloudClientException 客户端异常
     */
    <T> void onSignRequestSuccess(HttpRequest<T> request, QCloudCredentials credentials,
                                  String authorization) throws QCloudClientException;
}
