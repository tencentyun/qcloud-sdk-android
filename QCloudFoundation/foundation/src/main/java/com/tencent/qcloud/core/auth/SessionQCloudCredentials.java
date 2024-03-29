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

import com.tencent.qcloud.core.http.HttpConfiguration;

import static com.tencent.qcloud.core.auth.Utils.handleTimeAccuracy;

/**
 * 服务端临时证书
 */
public class SessionQCloudCredentials implements QCloudLifecycleCredentials, QCloudRawCredentials {

    private final String secretId;
    private final String secretKey;
    private final String token;
    private final long startTime;
    private final long expiredTime;

    /**
     * Constructs a new SessionQCloudCredentials object
     *
     * @param secretId    The QCloud secretId.
     * @param secretKey   The QCloud temporary secretKey.
     * @param token       The QCloud token.
     * @param expiredTime The expired time of the key.
     */
    public SessionQCloudCredentials(String secretId, String secretKey, String token, long expiredTime) {
        this(secretId, secretKey, token, HttpConfiguration.getDeviceTimeWithOffset(), expiredTime);
    }

    /**
     * Constructs a new SessionQCloudCredentials object
     *
     * @param secretId    The QCloud secretId.
     * @param secretKey   The QCloud temporary secretKey.
     * @param token       The QCloud token.
     * @param startTime   The begin time of the key.
     * @param expiredTime The expired time of the key.
     */
    public SessionQCloudCredentials(String secretId, String secretKey, String token, long startTime, long expiredTime) {
        if (secretId == null) {
            throw new IllegalArgumentException("secretId cannot be null.");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("secretKey cannot be null.");
        }
        if (token == null) {
            throw new IllegalArgumentException("token cannot be null.");
        }
        if (startTime >= expiredTime) {
            throw new IllegalArgumentException("beginTime must be less than expiredTime.");
        }

        this.secretId = secretId;
        this.secretKey = secretKey;
        this.startTime = startTime;
        this.expiredTime = expiredTime;
        this.token = token;
    }

    /**
     * Constructs a new SessionQCloudCredentials object
     *
     * @param secretId  The QCloud secretId.
     * @param secretKey The QCloud temporary secretKey.
     * @param token     The QCloud token.
     * @param keyTime   The QCloud keyTime.
     */
    public SessionQCloudCredentials(String secretId, String secretKey, String token, String keyTime) {
        if (secretId == null) {
            throw new IllegalArgumentException("secretId cannot be null.");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("secretKey cannot be null.");
        }
        if (token == null) {
            throw new IllegalArgumentException("token cannot be null.");
        }
        if (keyTime == null) {
            throw new IllegalArgumentException("keyTime cannot be null.");
        }

        this.secretId = secretId;
        this.secretKey = secretKey;
        this.token = token;
        long[] times = Utils.parseKeyTimes(keyTime);
        this.startTime = times[0];
        this.expiredTime = times[1];
    }

    private String getKeyTime(long beginTime, long expiredTime) {
        return handleTimeAccuracy(beginTime) + ";" + handleTimeAccuracy(expiredTime);
    }

    private String getSignKey(String secretKey, String keyTime) {
        byte[] hmacSha1 = (Utils.hmacSha1(keyTime, secretKey));
        if (hmacSha1 != null) {
            return new String(Utils.encodeHex(hmacSha1)); // 用secretKey来加密keyTime
        }
        return null;
    }

    @Override
    public boolean isValid() {
        long current = HttpConfiguration.getDeviceTimeWithOffset();
        return current <= expiredTime - AuthConstants.EXPIRE_TIME_RESERVE_IN_SECONDS;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getKeyTime() {
        return handleTimeAccuracy(startTime) + ";" + handleTimeAccuracy(expiredTime);
    }

    @Override
    public String getSecretId() {
        return secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    @Override
    public String getSignKey() {
        return getSignKey(secretKey, getKeyTime());
    }

    public long getStartTime() {
        return startTime;
    }

    public long getExpiredTime() {
        return expiredTime;
    }
}
