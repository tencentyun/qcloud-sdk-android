package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpResult;
import com.tencent.qcloud.core.http.QCloudHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wjielai on 2017/8/31.
 * <p>
 * Copyright (c) 2010-2017 Tencent Cloud. All rights reserved.
 */

public class SessionCredentialProvider extends BasicLifecycleCredentialProvider {

    private HttpRequest<String> httpRequest;
    private HttpRequest.Builder<String> requestBuilder;

    private StsVersion stsVersion = StsVersion.VERSION_2; // 默认解析 STS 2.0 的返回包

    public SessionCredentialProvider() {

    }

    public SessionCredentialProvider(HttpRequest<String> httpRequest) {
        this(httpRequest, StsVersion.VERSION_2);
    }

    public SessionCredentialProvider(HttpRequest.Builder<String> requestBuilder) {
        this(requestBuilder, StsVersion.VERSION_2);
    }

    public SessionCredentialProvider(HttpRequest<String> httpRequest, StsVersion stsVersion) {
        this.httpRequest = httpRequest;
        this.stsVersion = stsVersion;
    }

    public SessionCredentialProvider(HttpRequest.Builder<String> requestBuilder, StsVersion stsVersion) {
        this.requestBuilder = requestBuilder;
        this.stsVersion = stsVersion;
    }

    @Override
    protected QCloudLifecycleCredentials fetchNewCredentials() throws QCloudClientException {
        HttpRequest<String> fetchSTSRequest = null;
        if (httpRequest != null) {
            fetchSTSRequest = buildRequest(httpRequest);
        } else if (requestBuilder != null) {
            fetchSTSRequest = buildRequest(requestBuilder);
        }

        if (fetchSTSRequest != null) {
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
        } else {
            throw new QCloudClientException(new QCloudAuthenticationException("please pass http request object for fetching"));
        }
    }

    /**
     * 构建请求 Request，默认直接返回 {@link SessionCredentialProvider#SessionCredentialProvider(HttpRequest)} 中的参数
     *
     * @return 请求 Request
     */
    protected HttpRequest<String> buildRequest(HttpRequest<String> httpRequest) {
        return httpRequest;
    }

    /**
     * 构建请求 Request，默认直接返回 {@link SessionCredentialProvider#SessionCredentialProvider(HttpRequest.Builder)} 中的参数
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
        return stsVersion == StsVersion.VERSION_2 ? parseStandardSTSJsonResponse(jsonContent)
                : parseStandardSTS3JsonResponse(jsonContent);
    }

    /**
     * 解析 STS 3.0 的返回
     */
    static SessionQCloudCredentials parseStandardSTS3JsonResponse(String jsonContent) throws QCloudClientException {
        if (jsonContent != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonContent);
                JSONObject data = jsonObject.optJSONObject("Response");
                if (data == null) {
                    data = jsonObject;
                }
                JSONObject credentials = data.optJSONObject("Credentials");
                JSONObject error = data.optJSONObject("Error");

                if (credentials != null) {
                    long expiredTime = data.optLong("ExpiredTime");
                    String sessionToken = credentials.optString("Token");
                    String tmpSecretId = credentials.optString("TmpSecretId");
                    String tmpSecretKey = credentials.optString("TmpSecretKey");
                    return new SessionQCloudCredentials(tmpSecretId, tmpSecretKey, sessionToken, expiredTime);
                } else if (error != null) {
                    throw new QCloudClientException(new QCloudAuthenticationException("get credentials error : " + data.toString()));
                }
            } catch (JSONException e) {
                throw new QCloudClientException("parse sts3.0 session json fails", new QCloudAuthenticationException(e.getMessage()));
            }
        }

        throw new QCloudClientException(new QCloudAuthenticationException("fetch credential response content is null"));
    }


    /**
     * 解析 STS 2.0 的返回
     */
    static SessionQCloudCredentials parseStandardSTSJsonResponse(String jsonContent) throws QCloudClientException {
        if (jsonContent != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonContent);
                JSONObject data = jsonObject.optJSONObject("data");
                if (data == null) {
                    data = jsonObject;
                }
                JSONObject credentials = data.optJSONObject("credentials");
                int code = data.optInt("code", -1);
                if (credentials != null) {
                    long expiredTime = data.optLong("expiredTime");
                    long startTime = data.optLong("startTime");
                    String sessionToken = credentials.optString("sessionToken");
                    String tmpSecretId = credentials.optString("tmpSecretId");
                    String tmpSecretKey = credentials.optString("tmpSecretKey");
                    if (startTime > 0) {
                        return new SessionQCloudCredentials(tmpSecretId, tmpSecretKey, sessionToken, startTime, expiredTime);
                    } else {
                        return new SessionQCloudCredentials(tmpSecretId, tmpSecretKey, sessionToken, expiredTime);
                    }
                } else if (code > 0) {
                    throw new QCloudClientException(new QCloudAuthenticationException("get credentials error : " + data.toString()));
                }
            } catch (JSONException e) {
                throw new QCloudClientException("parse sts2.0 session json fails", new QCloudAuthenticationException(e.getMessage()));
            }
        }

        throw new QCloudClientException(new QCloudAuthenticationException("fetch credential response content is null"));
    }

    public enum StsVersion {

        VERSION_2,
        VERSION_3
    }
}
