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

import com.tencent.cos.xml.s3.Base64;
import com.tencent.cos.xml.s3.Base64Codec;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static com.tencent.cos.xml.model.tag.eventstreaming.HeaderType.TIMESTAMP;
import static com.tencent.cos.xml.model.tag.eventstreaming.HeaderType.fromTypeId;
import static com.tencent.cos.xml.model.tag.eventstreaming.Utils.writeBytes;
import static com.tencent.cos.xml.model.tag.eventstreaming.Utils.writeString;

/**
 * A typed header value. The underlying value can be obtained by calling the
 * appropriate getter.
 */
public abstract class HeaderValue {
    public static HeaderValue fromBoolean(boolean value) {
        return new BooleanValue(value);
    }

    public static HeaderValue fromByte(byte value) {
        return new ByteValue(value);
    }

    public static HeaderValue fromShort(short value) {
        return new ShortValue(value);
    }

    public static HeaderValue fromInteger(int value) {
        return new IntegerValue(value);
    }

    public static HeaderValue fromLong(long value) {
        return new LongValue(value);
    }

    public static HeaderValue fromByteArray(byte[] bytes) {
        return new ByteArrayValue(bytes);
    }

    public static HeaderValue fromByteBuffer(ByteBuffer buf) {
        buf = buf.duplicate();
        byte[] bytes = new byte[buf.remaining()];
        buf.get(bytes);
        return fromByteArray(bytes);
    }

    public static HeaderValue fromString(String string) {
        return new StringValue(string);
    }

    public static HeaderValue fromTimestamp(long timstamp) {
        return new TimestampValue(new Date(timstamp));
    }

    public static HeaderValue fromDate(Date value) {
        return new TimestampValue(value);
    }

    public static HeaderValue fromUuid(UUID value) {
        return new UuidValue(value);
    }

    protected HeaderValue() {}

    public abstract HeaderType getType();

    public boolean getBoolean() {
        throw new IllegalStateException();
    }

    public byte getByte() {
        throw new IllegalStateException("Expected byte, but type was " + getType().name());
    }

    public short getShort() {
        throw new IllegalStateException("Expected short, but type was " + getType().name());
    }

    public int getInteger() {
        throw new IllegalStateException("Expected integer, but type was " + getType().name());
    }

    public long getLong() {
        throw new IllegalStateException("Expected long, but type was " + getType().name());
    }

    public byte[] getByteArray() {
        throw new IllegalStateException();
    }

    public final ByteBuffer getByteBuffer() {
        return ByteBuffer.wrap(getByteArray());
    }

    public String getString() {
        throw new IllegalStateException();
    }

    public long getTimestamp() {
        throw new IllegalStateException("Expected timestamp, but type was " + getType().name());
    }

    public Date getDate() {
        return new Date(getTimestamp());
    }

    public UUID getUuid() {
        throw new IllegalStateException("Expected UUID, but type was " + getType().name());
    }

    void encode(DataOutputStream dos) throws IOException {
        dos.writeByte(getType().headerTypeId);
        encodeValue(dos);
    }

    abstract void encodeValue(DataOutputStream dos) throws IOException;

    static HeaderValue decode(ByteBuffer buf) {
        HeaderType type = fromTypeId(buf.get());
        switch (type) {
            case TRUE:
                return new BooleanValue(true);
            case FALSE:
                return new BooleanValue(false);
            case BYTE:
                return new ByteValue(buf.get());
            case SHORT:
                return new ShortValue(buf.getShort());
            case INTEGER:
                return fromInteger(buf.getInt());
            case LONG:
                return new LongValue(buf.getLong());
            case BYTE_ARRAY:
                return fromByteArray(Utils.readBytes(buf));
            case STRING:
                try {
                    return fromString(Utils.readString(buf));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throw new IllegalStateException();
                }
            case TIMESTAMP:
                return TimestampValue.decode(buf);
            case UUID:
                return UuidValue.decode(buf);
            default:
                throw new IllegalStateException();
        }
    }

    private static final class BooleanValue extends HeaderValue {
        private final boolean value;

        private BooleanValue(boolean value) {
            this.value = value;
        }

        @Override
        public HeaderType getType() {
            if (value) {
                return HeaderType.TRUE;
            } else {
                return HeaderType.FALSE;
            }
        }

        @Override
        public boolean getBoolean() {
            return value;
        }

        @Override
        void encodeValue(DataOutputStream dos) {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BooleanValue that = (BooleanValue) o;

            return value == that.value;
        }

        @Override
        public int hashCode() {
            if (value) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static final class ByteValue extends HeaderValue {
        private final byte value;

        private ByteValue(byte value) {
            this.value = value;
        }

        @Override
        public HeaderType getType() {
            return HeaderType.BYTE;
        }

        @Override
        public byte getByte() {
            return value;
        }

        @Override
        void encodeValue(DataOutputStream dos) {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ByteValue that = (ByteValue) o;

            return value == that.value;
        }

        @Override
        public int hashCode() {
            return (int) value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static final class ShortValue extends HeaderValue {
        private final short value;

        private ShortValue(short value) {
            this.value = value;
        }

        @Override
        public HeaderType getType() {
            return HeaderType.SHORT;
        }

        @Override
        public short getShort() {
            return value;
        }

        @Override
        void encodeValue(DataOutputStream dos) {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ShortValue that = (ShortValue) o;

            return value == that.value;
        }

        @Override
        public int hashCode() {
            return (int) value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static final class IntegerValue extends HeaderValue {
        private final int value;

        private IntegerValue(int value) {
            this.value = value;
        }

        @Override
        public HeaderType getType() {
            return HeaderType.INTEGER;
        }

        @Override
        public int getInteger() {
            return value;
        }

        @Override
        void encodeValue(DataOutputStream dos) throws IOException {
            dos.writeInt(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IntegerValue that = (IntegerValue) o;

            return value == that.value;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static final class LongValue extends HeaderValue {
        private final long value;

        private LongValue(long value) {
            this.value = value;
        }

        @Override
        public HeaderType getType() {
            return HeaderType.LONG;
        }

        @Override
        public long getLong() {
            return value;
        }

        @Override
        void encodeValue(DataOutputStream dos) throws IOException {
            dos.writeLong(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LongValue longValue = (LongValue) o;

            return value == longValue.value;
        }

        @Override
        public int hashCode() {
            return (int) (value ^ (value >>> 32));
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static final class ByteArrayValue extends HeaderValue {
        private final byte[] value;

        private Base64Codec codec;

        private ByteArrayValue(byte[] value) {
            this.value = ValidationUtils.assertNotNull(value, "value");
        }

        @Override
        public HeaderType getType() {
            return HeaderType.BYTE_ARRAY;
        }

        @Override
        public byte[] getByteArray() {
            return value;
        }

        @Override
        void encodeValue(DataOutputStream dos) throws IOException {
            writeBytes(dos, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ByteArrayValue that = (ByteArrayValue) o;

            return Arrays.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(value);
        }

        @Override
        public String toString() {
            return Base64.encodeAsString(value);
        }

        private String toStringDirect(final byte[] bytes) {
            final char[] dest = new char[bytes.length];
            int i=0;

            for (byte b: bytes)
                dest[i++] = (char)b;

            return new String(dest);
        }
    }

    private static final class StringValue extends HeaderValue {
        private final String value;

        private StringValue(String value) {
            this.value = ValidationUtils.assertNotNull(value, "value");
        }

        @Override
        public HeaderType getType() {
            return HeaderType.STRING;
        }

        @Override
        public String getString() {
            return value;
        }

        @Override
        void encodeValue(DataOutputStream dos) throws IOException {
            writeString(dos, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StringValue that = (StringValue) o;

            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return '"' + value + '"';
        }
    }

    // TODO: 2019-11-06 modify DateTime to Date
    private static final class TimestampValue extends HeaderValue {
        private final Date value;

        private TimestampValue(Date value) {
            this.value = ValidationUtils.assertNotNull(value, "value");
        }

        static TimestampValue decode(ByteBuffer buf) {
            long epochMillis = buf.getLong();
            return new TimestampValue(new Date(epochMillis));
        }

        @Override
        public HeaderType getType() {
            return TIMESTAMP;
        }

        @Override
        public long getTimestamp() {
            return value.getTime();
        }

        @Override
        void encodeValue(DataOutputStream dos) throws IOException {
            dos.writeLong(value.getTime());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TimestampValue that = (TimestampValue) o;

            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private static final class UuidValue extends HeaderValue {
        private final UUID value;

        private UuidValue(UUID value) {
            this.value = ValidationUtils.assertNotNull(value, "value");
        }

        static UuidValue decode(ByteBuffer buf) {
            long msb = buf.getLong();
            long lsb = buf.getLong();
            return new UuidValue(new UUID(msb, lsb));
        }

        @Override
        public HeaderType getType() {
            return HeaderType.UUID;
        }

        @Override
        public UUID getUuid() {
            return value;
        }

        @Override
        void encodeValue(DataOutputStream dos) throws IOException {
            dos.writeLong(value.getMostSignificantBits());
            dos.writeLong(value.getLeastSignificantBits());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UuidValue uuidValue = (UuidValue) o;

            return value.equals(uuidValue.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
