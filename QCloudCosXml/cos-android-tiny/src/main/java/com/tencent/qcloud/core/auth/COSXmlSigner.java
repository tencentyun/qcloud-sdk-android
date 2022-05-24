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
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.QCloudHttpRequest;

import java.net.URL;
import java.util.Locale;

/**
 * COS签名器<br>
 * 对请求进行签名
 * 请参考文档：<a href="https://cloud.tencent.com/document/product/436/7778">请求签名</a>
 */
public class COSXmlSigner implements QCloudSigner {

    final static String COS_SESSION_TOKEN = "x-cos-security-token";

    /**
     * 使用指定的证书按照<a href="https://cloud.tencent.com/document/product/436/7778">请求签名</a>文档规则对请求进行签名
     * @param request 需要签名的请求
     * @param credentials 用于签名的证书
     * @throws QCloudClientException 客户端异常
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
                .append(sourceProvider.getRealHeaderList().toLowerCase(Locale.ROOT)).append("&")
                .append(AuthConstants.Q_URL_PARAM_LIST).append("=")
                .append(sourceProvider.getRealParameterList().toLowerCase(Locale.ROOT)).append("&")
                .append(AuthConstants.Q_SIGNATURE).append("=").append(signature);
        String auth = authorization.toString();

        if (request.isSignInUrl()) {
            addAuthInPara(request, credentials, auth);
        } else {
            addAuthInHeader(request, credentials, auth);
        }

        sourceProvider.onSignRequestSuccess(request, credentials, auth);
    }

    private void addAuthInPara(QCloudHttpRequest request, QCloudCredentials credentials, String auth) {

        URL url = request.url();
        String authQuery = auth;
        if (credentials instanceof SessionQCloudCredentials) {
            SessionQCloudCredentials sessionCredentials = (SessionQCloudCredentials) credentials;
            authQuery = authQuery.concat("&token").concat("=").concat(sessionCredentials.getToken());
        }
        String query = url.getQuery();
        String sUrl = url.toString();
        int index = sUrl.indexOf('?');
        if (index < 0) {
            sUrl = sUrl.concat("?").concat(authQuery);
        } else {
            int lastQueryIndex = index + query.length();
            sUrl = sUrl.substring(0, lastQueryIndex + 1)
                    .concat("&")
                    .concat(authQuery)
                    .concat(sUrl.substring(lastQueryIndex + 1));
        }
        request.setUrl(sUrl);
    }

    protected String getSessionTokenKey() {
        return COS_SESSION_TOKEN;
    }

    private void addAuthInHeader(QCloudHttpRequest request, QCloudCredentials credentials, String auth) {

        request.removeHeader(HttpConstants.Header.AUTHORIZATION);
        request.addHeader(HttpConstants.Header.AUTHORIZATION, auth);

        if (credentials instanceof SessionQCloudCredentials) {
            SessionQCloudCredentials sessionCredentials = (SessionQCloudCredentials) credentials;
            String sessionTokenKey = getSessionTokenKey();
            request.removeHeader(sessionTokenKey);
            request.addHeader(sessionTokenKey, sessionCredentials.getToken());
        }

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
