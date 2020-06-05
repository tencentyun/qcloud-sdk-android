package com.tencent.qcloud.quic.test;

import java.io.IOException;
import java.io.OutputStream;

public class QuicOutputStream extends OutputStream {

    QuicNative quicNative;

    public QuicOutputStream(QuicNative quicNative){
        this.quicNative = quicNative;
    }

    @Override public void write(int b) {
        throw new IllegalStateException("No implementation");
    }

    @Override
    public void write(byte[] b) throws IOException {
        quicNative.sendRequest(b, b.length, false);
    }

    @Override public void write(byte[] data, int offset, int byteCount) {
        throw new IllegalStateException("No implementation");
    }

    @Override public void flush() {
    }

    @Override public void close() {
        quicNative.sendRequest(new byte[0], 0, true);
    }

    @Override public String toString() {
        return this +  ".outputStream()";
    }
}
