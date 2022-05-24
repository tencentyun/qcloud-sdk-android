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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

final class Utils {

    private static final String UTF8 = "UTF-8";

    private Utils() {}

    static int toIntExact(long headersLength) {
        if ((int) headersLength != headersLength) {
            throw new ArithmeticException("integer overflow");
        }
        return (int) headersLength;
    }

    static long toUnsignedLong(int x) {
        return ((long) x) & 0xffffffffL;
    }

    static String readShortString(ByteBuffer buf) throws UnsupportedEncodingException {
        int length = buf.get() & 0xFF;
        checkStringBounds(length, 255);
        byte[] bytes = new byte[length];
        buf.get(bytes);
        return new String(bytes, UTF8);
    }

    static String readString(ByteBuffer buf) throws UnsupportedEncodingException {
        int length = buf.getShort() & 0xFFFF;
        checkStringBounds(length, 32767);
        byte[] bytes = new byte[length];
        buf.get(bytes);
        return new String(bytes, UTF8);
    }

    static byte[] readBytes(ByteBuffer buf) {
        int length = buf.getShort() & 0xFFFF;
        checkByteArrayBounds(length);
        byte[] bytes = new byte[length];
        buf.get(bytes);
        return bytes;
    }

    static void writeShortString(DataOutputStream dos, String string) throws IOException {
        byte[] bytes = string.getBytes(UTF8);
        checkStringBounds(bytes.length, 255);
        dos.writeByte(bytes.length);
        dos.write(bytes);
    }

    static void writeString(DataOutputStream dos, String string) throws IOException {
        byte[] bytes = string.getBytes(UTF8);
        checkStringBounds(bytes.length, 32767);
        writeBytes(dos, bytes);
    }

    static void writeBytes(DataOutputStream dos, byte[] bytes) throws IOException {
        checkByteArrayBounds(bytes.length);
        dos.writeShort((short) bytes.length);
        dos.write(bytes);
    }

    private static void checkByteArrayBounds(int length) {
        if (length == 0) {
            throw new IllegalArgumentException("Byte arrays may not be empty");
        }
        if (length > 32767) {
            throw new IllegalArgumentException("Illegal byte array length: " + length);
        }
    }

    private static void checkStringBounds(int length, int maxLength) {
        if (length == 0) {
            throw new IllegalArgumentException("Strings may not be empty");
        }
        if (length > maxLength) {
            throw new IllegalArgumentException("Illegal string length: " + length);
        }
    }
}
