package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.QCloudHttpRequest;

/**
 * Created by wjielai on 2017/9/21.
 * <p>
 * Copyright (c) 2010-2017 Tencent Cloud. All rights reserved.
 */

public class COSXmlSigner implements QCloudSigner {

    final static String COS_SESSION_TOKEN = "x-cos-security-token";

    /**
     * 计算签名
     *
     * @throws QCloudClientException
     */
    @Override
    public void sign(QCloudHttpRequest request, QCloudCredentials credentials) throws QCloudClientException{
        if (credentials == null) {
            throw new QCloudClientException(new QCloudAuthenticationException("Credentials is null."));
        }
        COSXmlSignSourceProvider sourceProvider = (COSXmlSignSourceProvider) request.getSignProvider();
        if (sourceProvider == null) {
            throw new QCloudClientException(new QCloudAuthenticationException("No sign provider for cos xml signer."));
        }

        StringBuilder authorization = new StringBuilder();

        QCloudLifecycleCredentials lifecycleCredentials = (QCloudLifecycleCredentials) credentials;

        String keyTime = lifecycleCredentials.getKeyTime();
        sourceProvider.setSignTime(keyTime);
        String signature = signature(sourceProvider.source(request), lifecycleCredentials.getSignKey());

        authorization.append(AuthConstants.Q_SIGN_ALGORITHM).append("=").append(AuthConstants.SHA1).append("&")
                .append(AuthConstants.Q_AK).append("=")
                .append(credentials.getSecretId()).append("&")
                .append(AuthConstants.Q_SIGN_TIME).append("=")
                .append(keyTime).append("&")
                .append(AuthConstants.Q_KEY_TIME).append("=")
                .append(lifecycleCredentials.getKeyTime()).append("&")
                .append(AuthConstants.Q_HEADER_LIST).append("=")
                .append(sourceProvider.getRealHeaderList().toLowerCase()).append("&")
                .append(AuthConstants.Q_URL_PARAM_LIST).append("=")
                .append(sourceProvider.getRealParameterList().toLowerCase()).append("&")
                .append(AuthConstants.Q_SIGNATURE).append("=").append(signature);
        String auth = authorization.toString();

        request.removeHeader(HttpConstants.Header.AUTHORIZATION);
        request.addHeader(HttpConstants.Header.AUTHORIZATION, auth);

        if (credentials instanceof SessionQCloudCredentials) {
            SessionQCloudCredentials sessionCredentials = (SessionQCloudCredentials) credentials;
            request.removeHeader(COS_SESSION_TOKEN);
            request.addHeader(COS_SESSION_TOKEN, sessionCredentials.getToken());
        }

        sourceProvider.onSignRequestSuccess(request, credentials, auth);
    }

    private String signature(String source, String signKey) {
        byte[] sha1Bytes = Utils.hmacSha1(source, signKey);
        String signature = "";
        if (sha1Bytes != null) {
            signature = new String(Utils.encodeHex(sha1Bytes));
        }
        return signature;
    }
}
