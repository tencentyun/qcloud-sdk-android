package com.tencent.cos.xml.transfer;

import java.net.InetAddress;

/**
 * <p>
 * Created by rickenwang on 2021/11/11.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class TransferTaskMetrics {

    /**
     * 上传的域名
     */
    String domain;

    /**
     * 连接的 ip
     */
    InetAddress connectAddress;

    /**
     * 本次需要上传或者下载的长度
     */
    long size;

    /**
     * 任务消耗的整体时间，包括等待的时间
     */
    private long tookTime;

    private long waitTookTime;

    /**
     * 第一次回调 inProgress 的时间，此时已经调用了 PutUploadRequest 或者 UploadPartRequest 方法来上传，
     * 注意不是回调 IN_PROGRESS 状态的时间，回调 IN_PROGRESS 时，会检查上传的一些状态，还没有调用上传
     */
    private long firstProgressTookTime;

    private long startTime = 0;
    private long progressTime = 0;
    private long firstProgressCallbackTime = 0;
    private long completeTime = 0;

//    public TransferTaskMetrics(String domain, InetAddress connectAddress,
//                               long uploadLength, long tookTime, long firstProgressTookTime) {
//        this.domain = domain;
//        this.connectAddress = connectAddress;
//        this.size = uploadLength;
//        this.tookTime = tookTime;
//        this.firstProgressTookTime = firstProgressTookTime;
//    }

    public TransferTaskMetrics() {}

    void onStart() {
        startTime = System.nanoTime();
    }

    void onInProgress() {
        progressTime = System.nanoTime();
        firstProgressCallbackTime = progressTime; // firstProgressCallbackTime 不会小于 progressTime
    }

    void onFirstProgressCallback() {
        if (firstProgressCallbackTime <= progressTime) {
            firstProgressCallbackTime = System.nanoTime();
        }
    }

    void onComplete() {
        completeTime = System.nanoTime();
        tookTime = tookTime(completeTime);
        waitTookTime = tookTime(progressTime);
        firstProgressTookTime = tookTime(firstProgressCallbackTime);
    }

    public String getDomain() {
        return domain;
    }

    public InetAddress getConnectAddress() {
        return connectAddress;
    }

    public long getSize() {
        return size;
    }

    public long getTookTime() {
        return tookTime;
    }

    public long getFirstProgressTookTime() {
        return firstProgressTookTime;
    }

    public long getWaitTookTime() {
        return waitTookTime;
    }

    private long tookTime(long time) {
        return Math.max(-1, time - startTime) / 1000000;
    }

    @Override
    public String toString() {
        return "TransferTaskMetrics{" +
                "domain='" + domain + '\'' +
                ", connectAddress=" + connectAddress +
                ", size=" + size +
                ", tookTime=" + tookTime +
                ", waitTookTime=" + waitTookTime +
                ", firstProgressTookTime=" + firstProgressTookTime +
                '}';
    }
}
