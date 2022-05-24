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

public enum HeaderType {
    TRUE(0),
    FALSE(1),
    BYTE(2),
    SHORT(3),
    INTEGER(4),
    LONG(5),
    BYTE_ARRAY(6),
    STRING(7),
    TIMESTAMP(8),
    UUID(9);

    final byte headerTypeId;

    HeaderType(int headerTypeId) {
        this.headerTypeId = (byte) headerTypeId;
    }

    static HeaderType fromTypeId(byte headerTypeId) {
        switch (headerTypeId) {
            case 0: return TRUE;
            case 1: return FALSE;
            case 2: return BYTE;
            case 3: return SHORT;
            case 4: return INTEGER;
            case 5: return LONG;
            case 6: return BYTE_ARRAY;
            case 7: return STRING;
            case 8: return TIMESTAMP;
            case 9: return UUID;
            default:
                throw new IllegalArgumentException("Got unknown headerTypeId " + headerTypeId);
        }
    }
}
