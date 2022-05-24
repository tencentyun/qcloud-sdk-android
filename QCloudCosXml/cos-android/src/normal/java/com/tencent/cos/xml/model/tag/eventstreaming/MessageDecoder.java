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

import com.tencent.cos.xml.exception.CosXmlServiceException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class MessageDecoder {

    private static int defaultDecoderCapacity = 1024 * 1024 + 1024 * 5; // 每个 chunk 一般不会超过 1M
    private static int incrementDecoderCapacity = 1024 * 512;

    private ByteBuffer buf;

    public MessageDecoder() {
        this.buf = ByteBuffer.allocate(defaultDecoderCapacity);
    }

    public boolean hasPendingContent() {
        return buf.position() != 0;
    }

    public List<Message> feed(byte[] bytes) throws CosXmlServiceException {
        return feed(bytes, 0, bytes.length);
    }

    public List<Message> feed(byte[] bytes, int offset, int length) throws CosXmlServiceException {

        buf = safePut(buf, bytes, offset, length);
        ByteBuffer readView = (ByteBuffer) buf
                .duplicate() // 拷贝 capacity、limit、position 等参数，但是共用 buffer 数据
                .flip(); // 准备数据读
        int bytesConsumed = 0;

        List<Message> result = new ArrayList<>();
        while (readView.remaining() >= 15) {
            int totalMessageLength = Utils.toIntExact(Prelude.decode(readView.duplicate()).getTotalLength());

            if (readView.remaining() >= totalMessageLength) {
                Message decoded = Message.decode(readView);
                result.add(decoded);
                bytesConsumed += totalMessageLength;
            } else {
                break;
            }
        }

        if (bytesConsumed > 0) {
            buf.flip();
            buf.position(buf.position() + bytesConsumed);
            buf.compact();
        }

        return result;
    }

    private ByteBuffer safePut(ByteBuffer byteBuffer, byte[] bytes, int offset, int length) {

        if (byteBuffer.remaining() < length) {

            ByteBuffer newByteBuffer = ByteBuffer.allocate(byteBuffer.capacity() + incrementDecoderCapacity);
            byteBufferCopy(newByteBuffer, byteBuffer);
            newByteBuffer.put(bytes, offset, length);
            return newByteBuffer;
        }

        byteBuffer.put(bytes, offset, length);
        return byteBuffer;
    }

    private void byteBufferCopy(ByteBuffer dst, ByteBuffer src) {

        System.arraycopy(src.array(), 0, dst.array(), 0, src.position());
        dst.position(src.position());
    }
}
