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

import java.io.Serializable;

public class JSONOutput implements Serializable {
    private String recordDelimiter;

    public JSONOutput() {
        this("\n");
    }

    public JSONOutput(String recordDelimiter) {
        this.recordDelimiter = recordDelimiter;
    }

    /**
     * The value used to separate individual records in the output.
     */
    public Character getRecordDelimiter() {
        return stringToChar(recordDelimiter);
    }

    /**
     * The value used to separate individual records in the output.
     */
    public String getRecordDelimiterAsString() {
        return recordDelimiter;
    }

    /**
     * The value used to separate individual records in the output.
     */
    public void setRecordDelimiter(String recordDelimiter) {
        validateNotEmpty(recordDelimiter, "recordDelimiter");
        this.recordDelimiter = recordDelimiter;
    }

    /**
     * The value used to separate individual records in the output.
     */
    public JSONOutput withRecordDelimiter(String recordDelimiter) {
        setRecordDelimiter(recordDelimiter);
        return this;
    }

    /**
     * The value used to separate individual records in the output.
     */
    public void setRecordDelimiter(Character recordDelimiter) {
        setRecordDelimiter(charToString(recordDelimiter));
    }

    /**
     * The value used to separate individual records in the output.
     */
    public JSONOutput withRecordDelimiter(Character recordDelimiter) {
        setRecordDelimiter(recordDelimiter);
        return this;
    }

    private String charToString(Character character) {
        return character == null ? null : character.toString();
    }

    private Character stringToChar(String string) {
        // Should not be empty string (setters should call validateNotEmpty)
        return string == null ? null : string.charAt(0);
    }

    private void validateNotEmpty(String value, String valueName) {
        if ("".equals(value)) {
            // Prevent the empty string from being used. We convert the recordDelimiter to a Character in getRecordDelimiter,
            // and the empty string doesn't have a valid Character representation. It's never a valid input anyway.
            throw new IllegalArgumentException(valueName + " must not be empty-string.");
        }
    }
}
