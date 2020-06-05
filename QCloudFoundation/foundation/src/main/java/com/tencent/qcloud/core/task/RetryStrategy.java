package com.tencent.qcloud.core.task;

import com.tencent.qcloud.core.http.QCloudHttpRetryHandler;
/**
 * <p>
 * </p>
 * Created by wjielai on 2018/4/27.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class RetryStrategy {

    // 重试的指数退避
    protected static final int BACKOFF_MULTIPLIER = 2;

    // 重试的初始间隔
    private static final int DEFAULT_INIT_BACKOFF = 1000;

    // 普通任务最少重试次数
    private static final int DEFAULT_ATTEMPTS = 3;

    // 普通任务最长重试间隔
    private static final int DEFAULT_MAX_BACKOFF = 2000;

    protected final int initBackoff;
    protected final int maxBackoff;
    protected final int baseAttempts;

    public static RetryStrategy DEFAULT = new RetryStrategy(DEFAULT_INIT_BACKOFF,
            DEFAULT_MAX_BACKOFF, 0);

    public static RetryStrategy FAIL_FAST = new RetryStrategy(0,
            0, Integer.MIN_VALUE);

    private QCloudHttpRetryHandler qCloudHttpRetryHandler = QCloudHttpRetryHandler.DEFAULT;

    public RetryStrategy(int initBackoff, int maxBackoff, int baseAttempts) {
        this.initBackoff = initBackoff;
        this.maxBackoff = maxBackoff;
        this.baseAttempts = baseAttempts;
    }

    public long getNextDelay(int attempts) {
        if (attempts < 1) {
            return 0;
        }
        return Math.min(maxBackoff, initBackoff * (int) Math.pow(BACKOFF_MULTIPLIER, (attempts - 1)));
    }

//    public boolean shouldRetry(int attempts, long millstook) {
//        return shouldRetry(attempts, millstook, 0);
//    }

    /**
     * @param addition 可以为负值，表示减少重试
     */
    public boolean shouldRetry(int attempts, long millstook, int addition) {
        return attempts < baseAttempts + addition;
    }

    public void onTaskEnd(boolean isTaskSuccess, Exception e) {

    }

    public void setRetryHandler(QCloudHttpRetryHandler qCloudHttpRetryHandler){
        this.qCloudHttpRetryHandler = qCloudHttpRetryHandler;
    }

    public QCloudHttpRetryHandler getQCloudHttpRetryHandler(){
        return qCloudHttpRetryHandler;
    }


    public static class WeightAndReliableAddition {

        private final int maxWeight = 2;
        private final int minWeight = 0;
        private final int minReliable = 0;
        private final int maxReliable = 4;

        private final int[][] addTable = new int[][] {
                {0, 1, 2, 2, 2},
                {0, 1, 2, 3, 3},
                {0, 1, 2, 3, 4}
        };

        public int getRetryAddition(int weight, int reliable) {

            weight = regular(weight, maxWeight, minWeight);
            reliable = regular(reliable, maxReliable, minReliable);
            return 1 + addTable[weight][reliable];
        }

        private int regular(int r, int max, int min) {

            if (r > max) {
                return max;
            } else if (r < min) {
                return min;
            } else {
                return r;
            }
        }
    }

}
