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

package com.tencent.qcloud.track.cls;

/**
 * CLS 服务端临时证书
 */
public class ClsSessionCredentials {

    private final String secretId;
    private final String secretKey;
    private final String token;
    private final long expiredTime;

    /**
     * Constructs a new SessionQCloudCredentials object
     *
     * @param secretId    The QCloud secretId.
     * @param secretKey   The QCloud temporary secretKey.
     * @param token       The QCloud token.
     * @param expiredTime The expired time of the key.
     */
    public ClsSessionCredentials(String secretId, String secretKey, String token, long expiredTime) {
        if (secretId == null) {
            throw new IllegalArgumentException("secretId cannot be null.");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("secretKey cannot be null.");
        }
        if (token == null) {
            throw new IllegalArgumentException("token cannot be null.");
        }

        this.secretId = secretId;
        this.secretKey = secretKey;
        this.expiredTime = expiredTime;
        this.token = token;
    }

    public boolean isValid() {
        long current = System.currentTimeMillis() / 1000;
        // 提前量60s
        return current <= expiredTime - 60;
    }

    public String getToken() {
        return token;
    }

    public String getSecretId() {
        return secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public long getExpiredTime() {
        return expiredTime;
    }
}
