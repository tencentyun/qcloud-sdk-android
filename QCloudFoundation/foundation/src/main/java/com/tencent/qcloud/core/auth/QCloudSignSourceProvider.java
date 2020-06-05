package com.tencent.qcloud.core.auth;

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpRequest;

/**
 * Created by wjielai on 2017/9/21.
 * <p>
 * Copyright (c) 2010-2017 Tencent Cloud. All rights reserved.
 */

public interface QCloudSignSourceProvider {

    <T> String source(HttpRequest<T> request) throws QCloudClientException;

    <T> void onSignRequestSuccess(HttpRequest<T> request, QCloudCredentials credentials,
                                  String authorization) throws QCloudClientException;
}
