/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.qcloud.core.task;

import java.util.concurrent.atomic.AtomicLong;

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
