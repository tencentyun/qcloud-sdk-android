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
import com.tencent.qcloud.core.logger.COSLogger;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


class CountingInputStream extends FilterInputStream {

    private long bytesWritten = 0;
    private long bytesTotal = 0;
    private long recentReportBytes = 0;

    private long mark = -1L;

    private QCloudProgressListener progressListener;


    public CountingInputStream(InputStream delegate, long bytesTotal, QCloudProgressListener progressListener) {
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

    void readBytesInternal(long byteCount) {
        bytesWritten += byteCount;
        reportProgress();
    }

    long getTotalTransferred() {
        return bytesWritten;
    }

    long getBytesTotal(){
        return bytesTotal;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = super.read(b, off, len);
        if (count > 0) {
            readBytesInternal(count);
        }
        return count;
    }

    @Override
    public long skip(long n) throws IOException {
        long count = super.skip(n);
        readBytesInternal(count);
        return count;
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);
        this.mark = this.bytesWritten;
    }

    @Override
    public synchronized void reset() throws IOException {
        if (!this.in.markSupported()) {
            throw new IOException("Mark not supported");
        } else if (this.mark == -1L) {
            throw new IOException("Mark not set");
        } else {
            this.in.reset();
            this.bytesWritten = this.mark;
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        COSLogger.iProcess("Test", "CountingInputStream is closed");
    }
}
