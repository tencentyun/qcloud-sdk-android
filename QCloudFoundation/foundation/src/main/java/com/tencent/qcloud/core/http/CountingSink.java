package com.tencent.qcloud.core.http;

import android.util.Log;

import com.tencent.qcloud.core.common.QCloudProgressListener;

import java.io.IOException;

import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

/**
 * <p>
 * </p>
 * Created by wjielai on 2017/12/8.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

class CountingSink extends ForwardingSink {

    private long bytesWritten = 0;
    private long bytesTotal = 0;
    private long recentReportBytes = 0;

    private QCloudProgressListener progressListener;

    public CountingSink(Sink delegate, long bytesTotal, QCloudProgressListener progressListener) {
        super(delegate);
        this.bytesTotal = bytesTotal;
        this.progressListener = progressListener;
    }

    private void reportProgress() {
        if (progressListener == null) {
            return;
        }
        long delta = bytesWritten - recentReportBytes;
        boolean enoughDelta =  delta > 50 * 1024 || delta * 10 > bytesTotal || bytesWritten == bytesTotal;

        if (enoughDelta) {
            recentReportBytes = bytesWritten;
            progressListener.onProgress(bytesWritten, bytesTotal);
        }
    }

    void writeBytesInternal(long byteCount) {
        bytesWritten += byteCount;
        reportProgress();
    }

    long getTotalTransferred() {
        return bytesWritten;
    }

    @Override
    public void write(Buffer source, long byteCount) throws IOException {
        super.write(source, byteCount);
        writeBytesInternal(byteCount);
    }

}
