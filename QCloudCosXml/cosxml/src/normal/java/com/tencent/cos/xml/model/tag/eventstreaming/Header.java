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
import java.util.Map;

/**
 * 响应报头
 */
class Header {
    private final String name;
    private final HeaderValue value;

    Header(String name, HeaderValue value) {
        this.name = name;
        this.value = value;
    }

    Header(String name, String value) {
        this(name, HeaderValue.fromString(value));
    }

    public String getName() {
        return name;
    }

    public HeaderValue getValue() {
        return value;
    }

    static Header decode(ByteBuffer buf) {
        String name = null;
        try {
            name = Utils.readShortString(buf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new Header(name, HeaderValue.decode(buf));
    }

    static void encode(Map.Entry<String, HeaderValue> header, DataOutputStream dos) throws IOException {
        new Header(header.getKey(), header.getValue()).encode(dos);
    }

    void encode(DataOutputStream dos) throws IOException {
        Utils.writeShortString(dos, name);
        value.encode(dos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Header header = (Header) o;

        if (!name.equals(header.name)) return false;
        return value.equals(header.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Header{"
            + "name='" + name + '\''
            + ", value=" + value
            + '}';
    }
}
