package com.tencent.qcloud.track;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池
 * <p>
 * Created by jordanqin on 2023/9/6 20:06.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class TrackExecutors {
    public static final ThreadPoolExecutor COMMAND_EXECUTOR;

    static {
        COMMAND_EXECUTOR = new ThreadPoolExecutor(5, 5, 5L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                new TaskThreadFactory("Command-", Thread.NORM_PRIORITY));

        COMMAND_EXECUTOR.allowCoreThreadTimeOut(true);
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
        public Thread newThread(Runnable runnable) {
            Thread newThread = new Thread(runnable, "Track-" + tag + increment.getAndIncrement());
            newThread.setDaemon(false);
            newThread.setPriority(priority);
            return newThread;
        }
    }
}
