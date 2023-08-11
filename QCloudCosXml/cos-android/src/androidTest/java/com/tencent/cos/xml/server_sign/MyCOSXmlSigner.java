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

package com.tencent.cos.xml.server_sign;

import com.tencent.qcloud.core.auth.AuthConstants;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.auth.Utils;
import com.tencent.qcloud.core.common.QCloudAuthenticationException;
import com.tencent.qcloud.core.common.QCloudClientException;

import java.util.Locale;

/**
 * COS签名器<br>
 * 服务端实现可以参考这里
 * 对请求进行签名
 * 请参考文档：<a href="https://cloud.tencent.com/document/product/436/7778">请求签名</a>
 */
public class MyCOSXmlSigner {
    /**
     * 写死的签名
     */
    public static final SessionQCloudCredentials credentials = new SessionQCloudCredentials(
            "xxxxxxxxxxxxxxxxxxxxxxxxx",
            "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
            "xxxxxxxxxxxxxxxxxxxxxxxxxx",
            1111111111, 222222222
    );

    public static MyQCloudSelfSigner.SignResult sign(String source, String realHeaderList, String realParameterList) throws QCloudClientException{
        if (credentials == null) {
            throw new QCloudClientException(new QCloudAuthenticationException("Credentials is null."));
        }
        StringBuilder authorization = new StringBuilder();

        String keyTime = credentials.getKeyTime();
        String signature = signature(source, credentials.getSignKey());

        authorization.append(AuthConstants.Q_SIGN_ALGORITHM).append("=").append(AuthConstants.SHA1).append("&")
                .append(AuthConstants.Q_AK).append("=")
                .append(credentials.getSecretId()).append("&")
                .append(AuthConstants.Q_SIGN_TIME).append("=")
                .append(keyTime).append("&")
                .append(AuthConstants.Q_KEY_TIME).append("=")
                .append(credentials.getKeyTime()).append("&")
                .append(AuthConstants.Q_HEADER_LIST).append("=")
                .append(realHeaderList.toLowerCase(Locale.ROOT)).append("&")
                .append(AuthConstants.Q_URL_PARAM_LIST).append("=")
                .append(realParameterList.toLowerCase(Locale.ROOT)).append("&")
                .append(AuthConstants.Q_SIGNATURE).append("=").append(signature);
        String auth = authorization.toString();
        return new MyQCloudSelfSigner.SignResult(auth, credentials.getToken());
    }

    private static String signature(String source, String signKey) {
        byte[] sha1Bytes = Utils.hmacSha1(source, signKey);
        String signature = "";
        if (sha1Bytes != null) {
            signature = new String(Utils.encodeHex(sha1Bytes));
        }
        return signature;
    }
}
