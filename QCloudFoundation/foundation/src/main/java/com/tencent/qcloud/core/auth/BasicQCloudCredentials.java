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
 * 基础证书
 */

public class BasicQCloudCredentials implements QCloudLifecycleCredentials, QCloudRawCredentials {

    private final String secretId;
    private final String signKey;
    private final String secretKey;
    private final String keyTime;

    /**
     * Constructs a new BasicQCloudCredentials object
     *
     * @param secretId The QCloud secretId.
     * @param signKey The QCloud signKey.
     * @param beginTime The begin time of the key.
     * @param expiredTime The expired time of the key.
     */
    public BasicQCloudCredentials(String secretId, String secretKey, String signKey, long beginTime, long expiredTime) {
        if (secretId == null) {
            throw new IllegalArgumentException("secretId cannot be null.");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("secretKey cannot be null.");
        }
        if (signKey == null) {
            throw new IllegalArgumentException("signKey cannot be null.");
        }
        if (beginTime >= expiredTime) {
            throw new IllegalArgumentException("beginTime must be less than expiredTime.");
        }

        this.secretId = secretId;
        this.secretKey = secretKey;
        this.signKey = signKey;
        this.keyTime = handleTimeAccuracy(beginTime) + ";" + handleTimeAccuracy(expiredTime);
    }

    /**
     * Constructs a new BasicQCloudCredentials object
     *
     * @param secretId The QCloud secretId.
     * @param signKey The QCloud signKey.
     * @param keyTime The QCloud keyTime.
     */
    public BasicQCloudCredentials(String secretId, String secretKey, String signKey, String keyTime) {
        if (secretId == null) {
            throw new IllegalArgumentException("secretId cannot be null.");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("secretKey cannot be null.");
        }
        if (signKey == null) {
            throw new IllegalArgumentException("signKey cannot be null.");
        }
        if (keyTime == null) {
            throw new IllegalArgumentException("keyTime cannot be null.");
        }

        this.secretId = secretId;
        this.secretKey = secretKey;
        this.signKey = signKey;
        this.keyTime = keyTime;
    }

    @Override
    public boolean isValid() {
        long current = HttpConfiguration.getDeviceTimeWithOffset();
        long[] times = Utils.parseKeyTimes(keyTime);
        return current > times[0] && current < times[1] - AuthConstants.EXPIRE_TIME_RESERVE_IN_SECONDS;
    }

    @Override
    public String getKeyTime() {
        return keyTime;
    }

    @Override
    public String getSignKey() {
        return signKey;
    }

    @Override
    public String getSecretId() {
        return secretId;
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }
}
