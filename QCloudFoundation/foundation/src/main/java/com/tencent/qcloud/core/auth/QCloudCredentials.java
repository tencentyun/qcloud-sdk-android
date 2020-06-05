package com.tencent.qcloud.core.auth;

/**
 * Created by wjielai on 2017/9/21.
 * <p>
 * Copyright (c) 2010-2017 Tencent Cloud. All rights reserved.
 */

public interface QCloudCredentials {
    /**
     * 返回 secretId
     *
     * @return secretId
     */
    String getSecretId();
}
