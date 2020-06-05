package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.common.QCloudClientException;

/**
 * Created by wjielai on 2017/9/22.
 * <p>
 * Copyright (c) 2010-2017 Tencent Cloud. All rights reserved.
 */

public interface QCloudCredentialProvider {
    /**
     * Returns QCloudCredentials which the caller can use to authorize an QCloud
     * request. Each implementation of QCloudCredentialsProvider can chose its own
     * strategy for loading credentials. For example, an implementation might
     * load credentials from an existing key management system, or load new
     * credentials when credentials are rotated.
     *
     * @return QCloudCredentials which the caller can use to authorize an QCloud
     *         request.
     */
    QCloudCredentials getCredentials() throws QCloudClientException;

    /**
     * Forces this credentials provider to refresh its credentials. For many
     * implementations of credentials provider, this method may simply be a
     * no-op, such as any credentials provider implementation that vends
     * static/non-changing credentials. For other implementations that vend
     * different credentials through out their lifetime, this method should
     * force the credentials provider to refresh its credentials.
     */
    void refresh() throws QCloudClientException;
}
