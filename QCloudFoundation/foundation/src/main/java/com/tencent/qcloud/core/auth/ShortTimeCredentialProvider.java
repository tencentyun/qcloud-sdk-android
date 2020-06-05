package com.tencent.qcloud.core.auth;


import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConfiguration;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
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
