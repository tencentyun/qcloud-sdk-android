package com.tencent.qcloud.core.auth;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020-04-21.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public interface QCloudRawCredentials extends QCloudCredentials {

    /**
     * 返回原始 secretKey
     *
     * @return secretId
     */
    String getSecretKey();
}
