package com.tencent.qcloud.core.auth;

/**
 * <p>
 *     一个静态访问凭证 Provider，访问凭证由用户提供，并且由用户自己管理更新。
 * </p>
 * Created by Tencentyun on 2018/8/23.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class StaticCredentialProvider implements QCloudCredentialProvider {

    private QCloudCredentials mCredentials;

    public StaticCredentialProvider() {

    }

    public StaticCredentialProvider(QCloudCredentials credentials) {
        mCredentials = credentials;
    }

    /**
     * 更新访问凭证，请在每次调用 API 前都更新，以免发生请求无授权的错误。
     *
     * @param credentials 腾讯云访问凭证
     */
    public void update(QCloudCredentials credentials) {
        mCredentials = credentials;
    }

    @Override
    public QCloudCredentials getCredentials() {
        return mCredentials;
    }

    @Override
    public void refresh() {

    }
}
