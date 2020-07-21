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
import com.tencent.cos.xml.s3.Base64;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;

/**
 * SELECT Object Content响应块<br>
 * 由于响应体的大小无法预知，COS 将用户请求响应体以序列化形式展示，即将响应体切分成多个分块返回
 * <p>
 * 详情请参考：<a herf="https://cloud.tencent.com/document/product/436/37641#.E5.93.8D.E5.BA.94">SELECT Object Content响应</a>
 */
public class Message {
    private static final int TRAILING_CRC_LENGTH = 4;
    static final int MESSAGE_OVERHEAD = Prelude.LENGTH_WITH_CRC + TRAILING_CRC_LENGTH;

    private final Map<String, HeaderValue> headers;
    private final byte[] payload;

    public Message(Map<String, HeaderValue> headers, byte[] payload) {
        this.headers = headers;
        this.payload = payload.clone();
    }

    /**
     * 获取响应报头
     * @return 响应报头
     */
    public Map<String, HeaderValue> getHeaders() {
        return headers;
    }

    /**
     * 获取响应正文
     * @return 响应正文
     */
    public byte[] getPayload() {
        return payload.clone();
    }

    public static Message decode(ByteBuffer buf) throws CosXmlServiceException {
        Prelude prelude = Prelude.decode(buf);

        int totalLength = prelude.getTotalLength();
        validateMessageCrc(buf, totalLength);
        buf.position(buf.position() + Prelude.LENGTH_WITH_CRC);

        long headersLength = prelude.getHeadersLength();
        byte[] headerBytes = new byte[Utils.toIntExact(headersLength)];
        buf.get(headerBytes);
        Map<String, HeaderValue> headers = decodeHeaders(ByteBuffer.wrap(headerBytes));

        byte[] payload = new byte[Utils.toIntExact(totalLength - MESSAGE_OVERHEAD - headersLength)];
        buf.get(payload);
        buf.getInt(); // skip past the message CRC

        return new Message(headers, payload);
    }

    private static void validateMessageCrc(ByteBuffer buf, int totalLength) throws CosXmlServiceException {
        Checksum crc = new CRC32();

        CheckSums.update(crc, (ByteBuffer) buf.duplicate().limit(buf.position() + totalLength - 4));
        long computedMessageCrc = crc.getValue();

        long wireMessageCrc = Utils.toUnsignedLong(buf.getInt(buf.position() + totalLength - 4));

        if (wireMessageCrc != computedMessageCrc) {
            throw new CosXmlServiceException("CRC failed",
                    new ArithmeticException(String.format("Message checksum failure: expected 0x%x, computed 0x%x", wireMessageCrc, computedMessageCrc)));
        }
    }

    static Map<String, HeaderValue> decodeHeaders(ByteBuffer buf) {
        Map<String, HeaderValue> headers = new HashMap<String, HeaderValue>();

        while (buf.hasRemaining()) {
            Header header = Header.decode(buf);
            headers.put(header.getName(), header.getValue());
        }

        return Collections.unmodifiableMap(headers);
    }

    public ByteBuffer toByteBuffer() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            encode(baos);
            baos.close();
            return ByteBuffer.wrap(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void encode(OutputStream os) {
        try {
            CheckedOutputStream checkedOutputStream = new CheckedOutputStream(os, new CRC32());
            encodeOrThrow(checkedOutputStream);
            long messageCrc = checkedOutputStream.getChecksum().getValue();
            os.write((int) (0xFF & messageCrc >> 24));
            os.write((int) (0xFF & messageCrc >> 16));
            os.write((int) (0xFF & messageCrc >> 8));
            os.write((int) (0xFF & messageCrc));

            os.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void encodeOrThrow(OutputStream os) throws IOException {
        ByteArrayOutputStream headersAndPayload = new ByteArrayOutputStream();
        {
            DataOutputStream dos = new DataOutputStream(headersAndPayload);
            for (Entry<String, HeaderValue> entry : headers.entrySet()) {
                Header.encode(entry, dos);
            }
            dos.write(payload);
            dos.flush();
        }

        int totalLength = Prelude.LENGTH_WITH_CRC + headersAndPayload.size() + 4;

        {
            byte[] preludeBytes = getPrelude(totalLength);
            Checksum crc = new CRC32();
            crc.update(preludeBytes, 0, preludeBytes.length);

            DataOutputStream dos = new DataOutputStream(os);
            dos.write(preludeBytes);
            long value = crc.getValue();
            int value1 = (int) value;
            dos.writeInt(value1);
            dos.flush();
        }

        headersAndPayload.writeTo(os);
    }

    private byte[] getPrelude(int totalLength) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8);
        DataOutputStream dos = new DataOutputStream(baos);

        int headerLength = totalLength - Message.MESSAGE_OVERHEAD - payload.length;
        dos.writeInt(totalLength);
        dos.writeInt(headerLength);

        dos.close();
        return baos.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!headers.equals(message.headers)) return false;
        return Arrays.equals(payload, message.payload);
    }

    @Override
    public int hashCode() {
        int result = headers.hashCode();
        result = 31 * result + Arrays.hashCode(payload);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        for (Entry<String, HeaderValue> entry : headers.entrySet()) {
            ret.append(entry.getKey());
            ret.append(": ");
            ret.append(entry.getValue().toString());
            ret.append('\n');
        }
        ret.append('\n');

        HeaderValue contentTypeHeader = headers.get("content-type");
        if (contentTypeHeader == null) {
            contentTypeHeader = HeaderValue.fromString("application/octet-stream");
        }

        String contentType = contentTypeHeader.getString();
        if (contentType.contains("json") || contentType.contains("text")) {
            try {
                ret.append(new String(payload, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            ret.append(Base64.encodeAsString(payload));
        }
        ret.append('\n');
        return ret.toString();
    }
}
