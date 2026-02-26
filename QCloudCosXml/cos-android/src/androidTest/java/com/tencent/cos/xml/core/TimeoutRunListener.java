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

package com.tencent.cos.xml.core;

import android.util.Log;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 全局测试超时监听器
 * 为所有测试方法设置13分钟超时限制
 */
public class TimeoutRunListener extends RunListener {

    private static final String TAG = "TimeoutRunListener";
    private static final long TIMEOUT_MINUTES = 13;

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> timeoutTask;
    private volatile Thread testThread;
    private volatile boolean isTestRunning = false;

    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        testThread = Thread.currentThread();
        isTestRunning = true;
        scheduler = Executors.newSingleThreadScheduledExecutor();

        Log.i(TAG, "Test started: " + description.getMethodName() + ", timeout set to " + TIMEOUT_MINUTES + " minutes");

        // 设置超时任务
        timeoutTask = scheduler.schedule(() -> {
            if (isTestRunning && testThread != null) {
                Log.e(TAG, "Test TIMEOUT after " + TIMEOUT_MINUTES + " minutes: " + description.getMethodName());
                System.err.println("=== TEST TIMEOUT === " + description.getMethodName() + " exceeded " + TIMEOUT_MINUTES + " minutes");

                // 中断测试线程
                testThread.interrupt();

                // 如果 interrupt 不起作用，尝试更强制的方式
                try {
                    Thread.sleep(5000); // 等待5秒
                    if (isTestRunning && testThread != null && testThread.isAlive()) {
                        Log.e(TAG, "Force stopping test thread: " + description.getMethodName());
                        // 使用 stop 方法（虽然已废弃，但在超时场景下是最后手段）
                        testThread.stop();
                    }
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }, TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        Log.i(TAG, "Test finished: " + description.getMethodName());
        cancelTimeout();
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        Log.e(TAG, "Test failed: " + failure.getDescription().getMethodName() + ", reason: " + failure.getMessage());
        cancelTimeout();
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
        Log.i(TAG, "Test ignored: " + description.getMethodName());
        cancelTimeout();
    }

    private void cancelTimeout() {
        isTestRunning = false;
        testThread = null;

        if (timeoutTask != null && !timeoutTask.isDone()) {
            timeoutTask.cancel(true);
            timeoutTask = null;
        }

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }
}
