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

package com.tencent.qcloud.core.http;

import com.tencent.qcloud.core.common.QCloudProgressListener;

import java.io.IOException;

import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

class CountingSink extends ForwardingSink {

    //上一次已传输的长度（例如：文件路径下载重试时）
    private long lastTimeBytesWritten = 0;
    private long bytesWritten = 0;
    private long bytesTotal = 0;
    private long recentReportBytes = 0;

    private QCloudProgressListener progressListener;

    public CountingSink(Sink delegate, long bytesTotal, QCloudProgressListener progressListener) {
        super(delegate);
        this.bytesTotal = bytesTotal;
        this.progressListener = progressListener;
    }

    public CountingSink(Sink delegate, long bytesTotal, long lastTimeBytesWritten, QCloudProgressListener progressListener) {
        super(delegate);
        this.bytesTotal = bytesTotal;
        this.lastTimeBytesWritten = lastTimeBytesWritten;
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
            progressListener.onProgress(lastTimeBytesWritten + bytesWritten, lastTimeBytesWritten + bytesTotal);
        }
    }

    void writeBytesInternal(long byteCount) {
        bytesWritten += byteCount;
        reportProgress();
    }

    long getTotalTransferred() {
        return bytesWritten + lastTimeBytesWritten;
    }

    long getBytesWritten() {
        return bytesWritten;
    }

    @Override
    public void write(Buffer source, long byteCount) throws IOException {
        super.write(source, byteCount);
        writeBytesInternal(byteCount);
    }

}
