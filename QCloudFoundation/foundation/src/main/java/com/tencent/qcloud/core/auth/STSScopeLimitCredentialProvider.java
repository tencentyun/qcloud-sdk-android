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

import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpResult;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.http.RequestBodySerializer;
/**
 * STS临时证书提供器
 * STS信息请参考：<a href="https://cloud.tencent.com/document/product/598/33416">STS相关接口</a>
 */

public class STSScopeLimitCredentialProvider extends BasicScopeLimitCredentialProvider {

    private HttpRequest.Builder<String> requestBuilder;

    /**
     * 实例化一个 STSScopeLimitCredentialProvider
     */
    public STSScopeLimitCredentialProvider(HttpRequest.Builder<String> requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @Override
    public SessionQCloudCredentials fetchNewCredentials(STSCredentialScope[] credentialScope) throws QCloudClientException {
        RequestBodySerializer serializer = RequestBodySerializer.string(
                HttpConstants.ContentType.JSON,
                STSCredentialScope.jsonify(credentialScope)
        );
        requestBuilder.body(serializer).method("POST");

        HttpRequest<String> fetchSTSRequest = buildRequest(requestBuilder);
        try {
            HttpResult<String> result = QCloudHttpClient.getDefault()
                    .resolveRequest(fetchSTSRequest)
                    .executeNow();
            if (result.isSuccessful()) {
                return parseServerResponse(result.content());
            } else {
                throw new QCloudClientException("fetch new credentials error ", new QCloudAuthenticationException(
                        result.asException().getMessage()));
            }
        } catch (QCloudServiceException e) {
            throw new QCloudClientException("fetch new credentials error ", new QCloudAuthenticationException(e.getMessage()));
        }
    }

    /**
     * 构建请求 Request，默认直接返回 {@link STSScopeLimitCredentialProvider#STSScopeLimitCredentialProvider(HttpRequest.Builder)} 中的参数
     *
     * @return 请求 Request
     */
    protected HttpRequest<String> buildRequest(HttpRequest.Builder<String> requestBuilder) {
        return requestBuilder.build();
    }

    /**
     * 解析服务器返回的 json 数据，默认行为是解析 CAM 的标准返回格式
     *
     * @param jsonContent 返回json数据
     * @return 临时签名
     * @throws QCloudClientException 获取签名出错的异常
     */
    protected SessionQCloudCredentials parseServerResponse(String jsonContent) throws QCloudClientException {
        return SessionCredentialProvider.parseStandardSTSJsonResponse(jsonContent);
    }
}
