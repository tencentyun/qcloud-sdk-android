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
import java.util.zip.Checksum;

/**
 * 数据校验
 */
final class CheckSums {

    private CheckSums() {}

    static void update(Checksum checksum, ByteBuffer buffer) {
        if (buffer.hasArray()) {
            int pos = buffer.position();
            int off = buffer.arrayOffset();
            int limit = buffer.limit();
            int rem = limit - pos;
            checksum.update(buffer.array(), pos + off, rem);
            buffer.position(limit);
        } else {
            int length = buffer.remaining();
            byte[] b = new byte[length];
            buffer.get(b, 0, length);
            checksum.update(b, 0, length);
        }
    }
}
