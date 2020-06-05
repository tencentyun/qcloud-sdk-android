package com.tencent.qcloud.core.http;

import com.tencent.qcloud.core.task.SelfConstraintRetryStrategy;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020-04-23.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class HttpSelConstraintRetryStrategy extends SelfConstraintRetryStrategy {

    public HttpSelConstraintRetryStrategy(int initBackoff, int maxBackoff, int baseAttempts) {
        super(initBackoff, maxBackoff, baseAttempts);
    }

    @Override
    protected boolean shouldIncreaseDelay(Exception e) {
        return HttpUtil.isNetworkTimeoutError(e);
    }
}
