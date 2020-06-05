package com.tencent.qcloud.core.http.interceptor;

import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpTask;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.task.TaskManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.tencent.qcloud.core.http.QCloudHttpClient.HTTP_LOG_TAG;

/**
 * <p>
 *     熔断器拦截器
 *
 *     https://docs.microsoft.com/en-us/azure/architecture/patterns/circuit-breaker
 * </p>
 * Created by wjielai on 2020-01-15.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class CircuitBreakerInterceptor implements Interceptor {

    enum State {
        OPEN,
        CLOSED,
        HALF_OPENED
    }

    private static class FootprintWriter {
        private Set<String> tasks = new HashSet<>();

        boolean noRecords(HttpTask task) {
            return !tasks.contains(getResourceId(task));
        }

        void remember(HttpTask task) {
            tasks.add(getResourceId(task));
        }

        String getResourceId(HttpTask task) {
            HttpRequest request =  task.request();
            return request.method() + request.url().getHost() + "/" + request.url().getPath();
        }
    }

    private AtomicInteger failedCount = new AtomicInteger(0);   // 连续失败次数
    private AtomicInteger successCount = new AtomicInteger(0); // 连续成功次数
    private State state = State.CLOSED; // 状态
    private long entryOpenStateTimestamp; // 进入 OPEN 状态时间
    private long recentErrorTimestamp; // 最近失败时间
    private FootprintWriter footprintWriter = new FootprintWriter(); // 任务记录器

    private static final int THRESHOLD_STATE_SWITCH_FOR_CONTINUOUS_FAIL = 5; // 连续失败进入 OPEN 状态
    private static final int THRESHOLD_STATE_SWITCH_FOR_CONTINUOUS_SUCCESS = 2; // 连续成功 进入 CLOSED 状态
    private static final long TIMEOUT_FOR_OPEN_STATE = 10000; // 10000ms
    private static final long TIMEOUT_FOR_RESET_ALL = 60000; // 60000ms

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpTask task = (HttpTask) TaskManager.getInstance().get((String) request.tag());
        boolean isFreshTask;

        synchronized (CircuitBreakerInterceptor.class) {
            if (state == State.OPEN) {
                long openDuration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() -
                        entryOpenStateTimestamp);
                if (openDuration > TIMEOUT_FOR_OPEN_STATE) {
                    state = State.HALF_OPENED;
                }
            }
            if (recentErrorTimestamp > 0) {
                if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - recentErrorTimestamp) >
                    TIMEOUT_FOR_RESET_ALL) {
                    // reset all state and counter
                    state = State.CLOSED;
                    successCount.set(0);
                    failedCount.set(0);
                    recentErrorTimestamp = 0;
                }
            }
            if (isFreshTask = footprintWriter.noRecords(task)) {
                footprintWriter.remember(task);
            }
        }

        if (state == State.OPEN && (task.isDownloadTask() || task.isUploadTask()) && !isFreshTask) {
            // circuit breaker denied
            QCloudLogger.i(HTTP_LOG_TAG, "CircuitBreaker deny %s", request);
            throw new CircuitBreakerDeniedException("too many continuous errors.");
        }

        try {
            Response response = chain.proceed(request);
            synchronized (CircuitBreakerInterceptor.class) {
                if (state == State.HALF_OPENED && successCount.incrementAndGet() >=
                        THRESHOLD_STATE_SWITCH_FOR_CONTINUOUS_SUCCESS) {
                    QCloudLogger.i(HTTP_LOG_TAG, "CircuitBreaker is CLOSED.");
                    state = State.CLOSED;
                    failedCount.set(0);
                } else if (state == State.OPEN) {
                    QCloudLogger.i(HTTP_LOG_TAG, "CircuitBreaker is HALF_OPENED.");
                    state = State.HALF_OPENED;
                    successCount.set(1);
                } else if (state == State.CLOSED){
                    int f = failedCount.get();
                    if (f > 0) {
                        failedCount.set(Math.max(f - 2, 0));
                    }
                    QCloudLogger.i(HTTP_LOG_TAG, "CircuitBreaker get success");
                }
            }

            return response;
        } catch (IOException e) {
            synchronized (CircuitBreakerInterceptor.class) {
                recentErrorTimestamp = System.nanoTime();
                if (state == State.CLOSED && failedCount.incrementAndGet() >=
                    THRESHOLD_STATE_SWITCH_FOR_CONTINUOUS_FAIL) {
                    QCloudLogger.i(HTTP_LOG_TAG, "CircuitBreaker is OPEN.");
                    state = State.OPEN;
                    entryOpenStateTimestamp = System.nanoTime();
                } else if (state == State.HALF_OPENED) {
                    QCloudLogger.i(HTTP_LOG_TAG, "CircuitBreaker is OPEN.");
                    state = State.OPEN;
                    entryOpenStateTimestamp = System.nanoTime();
                } else {
                    QCloudLogger.i(HTTP_LOG_TAG, "CircuitBreaker get fail: %d",
                            failedCount.get());
                }
            }

            throw e;
        }
    }
}
