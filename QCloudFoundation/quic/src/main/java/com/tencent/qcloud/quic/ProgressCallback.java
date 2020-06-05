package com.tencent.qcloud.quic;

public interface ProgressCallback {

    void onProgress(long current, long total);
}
