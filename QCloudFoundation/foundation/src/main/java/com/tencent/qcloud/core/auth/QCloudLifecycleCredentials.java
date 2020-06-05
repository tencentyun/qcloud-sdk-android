package com.tencent.qcloud.core.auth;

/**
 * Created by wjielai on 2017/9/25.
 *
 * 拥有生命周期的证书，证书会在一定时间后过期。
 *
 * <p>
 * Copyright (c) 2010-2017 Tencent Cloud. All rights reserved.
 */

public interface QCloudLifecycleCredentials extends QCloudCredentials {

    /**
     * 返回临时密钥
     *
     * @return signKey
     */
    String getSignKey();

    /**
     * 返回临时密钥有效期
     *
     * @return keyTime
     */
    String getKeyTime();

    /**
     * 返回密钥是否有效
     *
     * @return true 表示有效，false 表示无效
     */
    boolean isValid();
}
