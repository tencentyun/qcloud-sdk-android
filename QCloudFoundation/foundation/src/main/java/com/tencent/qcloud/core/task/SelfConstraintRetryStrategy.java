package com.tencent.qcloud.core.task;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 *     自我维护状态的重试策略，自己维护重试间隔，并根据任务请求的结果自动调节
 * </p>
 * Created by wjielai on 2020-04-23.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public abstract class SelfConstraintRetryStrategy extends RetryStrategy {

    private AtomicLong nextDelay;

    public SelfConstraintRetryStrategy(int initBackoff, int maxBackoff, int baseAttempts) {
        super(initBackoff, maxBackoff, baseAttempts);
        nextDelay = new AtomicLong(0);
    }

    @Override
    public long getNextDelay(int attempts) {
        return nextDelay.get();
    }

    @Override
    public void onTaskEnd(boolean isTaskSuccess, Exception e) {
        if (isTaskSuccess) {
            nextDelay.set(0);
        } else if (shouldIncreaseDelay(e)){
            nextDelay.set(Math.max(Math.min(maxBackoff, nextDelay.get() * BACKOFF_MULTIPLIER),
                    initBackoff));
        }
    }

    /**
     * 是否应该增加重试间隔
     *
     * @param e
     * @return
     */
    protected abstract boolean shouldIncreaseDelay(Exception e);
}
