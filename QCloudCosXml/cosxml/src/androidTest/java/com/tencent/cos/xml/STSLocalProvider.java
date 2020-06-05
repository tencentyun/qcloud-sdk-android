package com.tencent.cos.xml;

import android.util.Base64;

import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;
import com.tencent.qcloud.core.auth.SessionCredentialProvider;
import com.tencent.qcloud.core.auth.Utils;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpResult;
import com.tencent.qcloud.core.http.QCloudHttpClient;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * <p>
 * </p>
 * Created by wjielai on 2019/1/7.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class STSLocalProvider extends SessionCredentialProvider {

    private String secretId;
    private String secretKey;
    private String appId;
    private String region;
    private String policy = "{\"statement\": [{\"action\": [\"name/cos:*\"],\"effect\": \"allow\"," +
            "\"resource\":[\"qcs::cos:%s:uid/%s:prefix//%s/*\"]}],\"version\": \"2.0\"}";

    @Deprecated
    public STSLocalProvider(String secretId, String secretKey, String appId, String region) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.appId = appId;
        this.region = region;
    }

    @Override
    protected QCloudLifecycleCredentials fetchNewCredentials() throws QCloudClientException {
        HttpRequest<String> fetchSTSRequest = getRequestByKey();

        try {
            HttpResult<String> result = QCloudHttpClient.getDefault()
                    .resolveRequest(fetchSTSRequest)
                    .executeNow();
            if (result.isSuccessful()) {
                return parseServerResponse(result.content());
            } else {
                throw new QCloudClientException("fetch new credentials error ", result.asException());
            }
        } catch (QCloudServiceException e) {
            throw new QCloudClientException("fetch new credentials error ", e);
        }
    }

    private HttpRequest<String> getRequestByKey() {
        String requestHost = "sts.api.qcloud.com";
        String requestPath = "/v2/index.php";
        String requestMethod = "GET";

        Map<String, String> params = new TreeMap<>();

        params.put("policy", String.format(policy, region, appId, appId));
        params.put("name", "cos-android-sdk");
        params.put("Action", "GetFederationToken");
        params.put("SecretId", secretId);
        params.put("Nonce", "" + new Random().nextInt(Integer.MAX_VALUE));
        params.put("Timestamp", "" + System.currentTimeMillis() / 1000);
        params.put("RequestClient", "cos-android-sdk");
        params.put("durationSeconds", "1800");

        String plainText = makeSignPlainText(params, requestMethod,
                requestHost, requestPath);

        byte[] hmacSha1 = Utils.hmacSha1(plainText, secretKey);
        if (hmacSha1 != null) {
            params.put("Signature", Base64.encodeToString(hmacSha1, Base64.DEFAULT));
        }

        return new HttpRequest.Builder<String>()
                .scheme("https")
                .host(requestHost)
                .path(requestPath)
                .method(requestMethod)
                .query(params)
                .build();
    }

    private String makeSignPlainText(Map<String, String> requestParams, String requestMethod,
                                     String requestHost, String requestPath) {
        String retStr = "";
        retStr += requestMethod;
        retStr += requestHost;
        retStr += requestPath;
        retStr += buildParamStr(requestParams);

        return retStr;
    }

    private String buildParamStr(Map<String, String> requestParams) {
        StringBuilder retStr = new StringBuilder();
        for(Map.Entry<String, String> entry : requestParams.entrySet()) {
            if (retStr.length()==0) {
                retStr.append('?');
            } else {
                retStr.append('&');
            }
            retStr.append(entry.getKey().replace("_", ".")).append('=').append(entry.getValue().toString());

        }
        return retStr.toString();
    }

}
