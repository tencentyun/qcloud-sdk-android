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

package com.tencent.cos.xml.model.tag.eventstreaming;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static java.lang.String.format;

/**
 * SELECT Object Content响应块中的预响应
 * 请参考：{@link Message}
 */
final class Prelude {
    static final int LENGTH = 8;
    static final int LENGTH_WITH_CRC = LENGTH + 4;

    private final int totalLength;
    private final long headersLength;

    private Prelude(int totalLength, long headersLength) {
        this.totalLength = totalLength;
        this.headersLength = headersLength;
    }

    static Prelude decode(ByteBuffer buf) {
        buf = buf.duplicate();

        long computedPreludeCrc = computePreludeCrc(buf);

        long totalLength = intToUnsignedLong(buf.getInt());
        long headersLength = intToUnsignedLong(buf.getInt());
        long wirePreludeCrc = intToUnsignedLong(buf.getInt());
        if (computedPreludeCrc != wirePreludeCrc) {
            throw new IllegalArgumentException(format("Prelude checksum failure: expected 0x%x, computed 0x%x",
                wirePreludeCrc, computedPreludeCrc));
        }

        if (headersLength < 0 || headersLength > 131072) {
            throw new IllegalArgumentException("Illegal headers_length value: " + headersLength);
        }

        long payloadLength = (totalLength - headersLength) - Message.MESSAGE_OVERHEAD;
        if (payloadLength < 0 || payloadLength > 16777216) {
            throw new IllegalArgumentException("Illegal payload size: " + payloadLength);
        }

        return new Prelude(toIntExact(totalLength), headersLength);
    }

    private static long intToUnsignedLong(int i) {
        return ((long) i) & 0xffffffffL;
    }

    private static int toIntExact(long value) {
        if ((int) value != value) {
            throw new ArithmeticException("integer overflow");
        }
        return (int) value;
    }

    private static long computePreludeCrc(ByteBuffer buf) {
        byte[] prelude = new byte[Prelude.LENGTH];
        buf.duplicate().get(prelude);

        Checksum crc = new CRC32();
        crc.update(prelude, 0, prelude.length);
        return crc.getValue();
    }

    int getTotalLength() {
        return totalLength;
    }

    long getHeadersLength() {
        return headersLength;
    }
}
