package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.http.HttpConfiguration;

import static com.tencent.qcloud.core.auth.Utils.handleTimeAccuracy;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
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
        return current > startTime && current < expiredTime - AuthConstants.EXPIRE_TIME_RESERVE_IN_SECONDS;
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
