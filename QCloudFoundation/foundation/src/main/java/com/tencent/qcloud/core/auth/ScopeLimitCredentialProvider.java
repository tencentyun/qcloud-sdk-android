package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.common.QCloudClientException;

/**
 * <p>
 *     限定权限的临时凭证提供者
 * </p>
 * Created by wjielai on 2018/12/3.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public interface ScopeLimitCredentialProvider extends QCloudCredentialProvider {

    /**
     * 可以根据权限范围，返回一个合适的临时密钥。
     *
     * @param credentialScope 权限范围
     * @return 一个符合权限要求的临时密钥
     * @throws QCloudClientException 如果请求超时或者失败，会抛出异常
     */
    SessionQCloudCredentials getCredentials(STSCredentialScope[] credentialScope) throws QCloudClientException;
}
