package com.tencent.qcloud.quic.test;

public interface ResultCallback {
    void onFailure(QuicRequest request, QuicException e);
    void onResponse(QuicRequest request, QuicResponse response);
}
