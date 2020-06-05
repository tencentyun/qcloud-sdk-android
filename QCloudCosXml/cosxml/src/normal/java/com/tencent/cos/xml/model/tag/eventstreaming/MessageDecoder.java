/*
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.

 * According to cos feature, we modify some class，comment, field name, etc.
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
