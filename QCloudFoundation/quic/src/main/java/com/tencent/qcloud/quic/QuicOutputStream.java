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
