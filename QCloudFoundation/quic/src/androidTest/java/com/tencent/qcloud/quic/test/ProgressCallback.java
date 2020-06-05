package com.tencent.qcloud.quic.test;

public interface ProgressCallback {

    void onProgress(long current, long total);
}
