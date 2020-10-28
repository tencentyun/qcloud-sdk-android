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

package com.tencent.qcloud.quic;

import java.io.IOException;
import java.io.OutputStream;

public class QuicOutputStream extends OutputStream {

    private boolean isClose = false;

    QuicNative quicNative;

    public QuicOutputStream(QuicNative quicNative){
        this.quicNative = quicNative;
    }

    @Override public void write(int b) {
        // QLog.d("quic outputstream : write length %d", (byte) (b&0xFF));
        quicNative.sendRequest(new byte[]{(byte) (b&0xFF)}, 1, false);
    }

    @Override
    public void write(byte[] b) throws IOException {
        // QLog.d("quic outputstream : write length %d ", b.length);
        quicNative.sendRequest(b, b.length, false);
    }

    @Override public void write(byte[] data, int offset, int byteCount) {
        // QLog.d("quic outputstream : write length %d ", byteCount);
        if(offset < 0) offset = 0;
        if(byteCount < 0) byteCount = data.length;
        if(offset + byteCount <= data.length){
            byte[] copy = new byte[byteCount];
            System.arraycopy(data, offset, copy, 0, byteCount);
            quicNative.sendRequest(copy, byteCount, false);
        }
    }

    @Override public void flush() {

    }

    @Override public void close() {
    }

    @Override public String toString() {
        return this +  ".outputStream()";
    }
}
