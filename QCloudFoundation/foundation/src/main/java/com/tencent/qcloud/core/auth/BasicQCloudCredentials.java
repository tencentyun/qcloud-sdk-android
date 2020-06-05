package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.http.HttpConfiguration;

import static com.tencent.qcloud.core.auth.Utils.handleTimeAccuracy;

/**
 * Created by wjielai on 2017/9/21.
 * <p>
 * Copyright (c) 2010-2017 Tencent Cloud. All rights reserved.
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
