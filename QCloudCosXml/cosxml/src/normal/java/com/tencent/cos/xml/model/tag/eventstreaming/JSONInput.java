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

/**
 * 描述如何格式化JSON格式的输入对象
 */
public class JSONInput implements Serializable {
    private String type;

    public JSONInput(String type) {
        this.type = type;
    }

    public JSONInput(JSONType type) {
        this.type = type.toString();
    }

    /**
     * The type of JSON. Valid values: Document, Lines.
     */
    public String getType() {
        return type;
    }

    /**
     * The type of JSON. Valid values: Document, Lines.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * The type of JSON. Valid values: Document, Lines.
     */
    public JSONInput withType(String type) {
        setType(type);
        return this;
    }

    /**
     * The type of JSON. Valid values: Document, Lines.
     */
    public void setType(JSONType type) {
        setType(type == null ? null : type.toString());
    }

    /**
     * The type of JSON. Valid values: Document, Lines.
     */
    public JSONInput withType(JSONType type) {
        setType(type);
        return this;
    }
}
