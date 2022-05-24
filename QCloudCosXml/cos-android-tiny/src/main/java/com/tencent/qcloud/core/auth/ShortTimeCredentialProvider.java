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
import com.tencent.qcloud.core.http.HttpConfiguration;

/**
 * 本地临时证书提供器<br>
 * 非常不推荐直接使用这种方式，因为会在客户端暴露您的secretId, secretKey，导致秘钥泄露，<br>
 * 推荐使用服务端下发临时秘钥的方式实现 {@link SessionCredentialProvider}
 * <p>
 * SDK 示例：<a href="https://cloud.tencent.com/document/product/436/12159#.E5.88.9D.E5.A7.8B.E5.8C.96.E6.9C.8D.E5.8A.A1">方式三：通过永久密钥进行授权（不推荐）</a>
 */

public class ShortTimeCredentialProvider extends BasicLifecycleCredentialProvider {

    private String secretKey;
    private long duration;
    private String secretId;

    @Deprecated
    public ShortTimeCredentialProvider(String secretId, String secretKey, long keyDuration) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.duration = keyDuration;
    }

    @Override
    protected QCloudLifecycleCredentials fetchNewCredentials() throws QCloudClientException  {
        // 使用本地永久秘钥计算得到临时秘钥
        long current = HttpConfiguration.getDeviceTimeWithOffset();
        long expired = current + duration;
        String keyTime = current + ";" + expired;
        String signKey = secretKey2SignKey(secretKey, keyTime);

        return new BasicQCloudCredentials(secretId, secretKey, signKey, keyTime);
    }

    private String secretKey2SignKey(String secretKey, String keyTime) {
        byte[] hmacSha1 = (Utils.hmacSha1(keyTime, secretKey));
        if (hmacSha1 != null) {
            return new String(Utils.encodeHex(hmacSha1)); // 用secretKey来加密keyTime
        }

        return null;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getSecretId() {
        return secretId;
    }

    public long getDuration() {
        return duration;
    }
}
