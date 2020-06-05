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
 * <p>
 * STS 临时密钥提供者，临时密钥的权限被限定在操作和资源上，将不会自动重用，每次都会重新请求，您需要根据场景自行缓存密钥
 *
 * Created by wjielai on 2018/12/5.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
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
