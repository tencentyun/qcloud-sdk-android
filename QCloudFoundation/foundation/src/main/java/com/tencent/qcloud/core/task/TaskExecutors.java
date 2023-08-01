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

import androidx.annotation.NonNull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskExecutors {
    // 上传线程数
    public static int UPLOAD_THREAD_COUNT = 4;
    // 下载线程数
    public static int DOWNLOAD_THREAD_COUNT = 6;

    public static final ThreadPoolExecutor COMMAND_EXECUTOR;

    public static ThreadPoolExecutor UPLOAD_EXECUTOR;

    public static ThreadPoolExecutor DOWNLOAD_EXECUTOR;

    public static final UIThreadExecutor UI_THREAD_EXECUTOR;

    public static void initExecutor(int uploadMaxThreadCount, int downloadMaxThreadCount){
        UPLOAD_THREAD_COUNT = uploadMaxThreadCount;
        DOWNLOAD_THREAD_COUNT = downloadMaxThreadCount;

        UPLOAD_EXECUTOR = new ThreadPoolExecutor(UPLOAD_THREAD_COUNT, UPLOAD_THREAD_COUNT, 5L,
                TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(),
                new TaskThreadFactory("Upload-", 3));
        DOWNLOAD_EXECUTOR = new ThreadPoolExecutor(DOWNLOAD_THREAD_COUNT, DOWNLOAD_THREAD_COUNT, 5L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE),
                new TaskThreadFactory("Download-", 3));
        UPLOAD_EXECUTOR.allowCoreThreadTimeOut(true);
        DOWNLOAD_EXECUTOR.allowCoreThreadTimeOut(true);
    }

    static {
        COMMAND_EXECUTOR = new ThreadPoolExecutor(5, 5, 5L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE),
                new TaskThreadFactory("Command-", 8));
        UPLOAD_EXECUTOR = new ThreadPoolExecutor(UPLOAD_THREAD_COUNT, UPLOAD_THREAD_COUNT, 5L,
                TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(),
                new TaskThreadFactory("Upload-", 3));
        DOWNLOAD_EXECUTOR = new ThreadPoolExecutor(DOWNLOAD_THREAD_COUNT, DOWNLOAD_THREAD_COUNT, 5L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE),
                new TaskThreadFactory("Download-", 3));
        UI_THREAD_EXECUTOR = new UIThreadExecutor();

        UPLOAD_EXECUTOR.allowCoreThreadTimeOut(true);
        COMMAND_EXECUTOR.allowCoreThreadTimeOut(true);
        DOWNLOAD_EXECUTOR.allowCoreThreadTimeOut(true);

    }

    static final class TaskThreadFactory implements ThreadFactory {
        private final AtomicInteger increment = new AtomicInteger(1);
        private final String tag;
        private final int priority;

        TaskThreadFactory(String tag, int priority) {
            this.tag = tag;
            this.priority = priority;
        }

        @Override
        public final Thread newThread(@NonNull Runnable runnable) {
            Thread newThread = new Thread(runnable, "QCloud-" + tag + increment.getAndIncrement());
            newThread.setDaemon(false);
            newThread.setPriority(priority);
            return newThread;
        }
    }
}
