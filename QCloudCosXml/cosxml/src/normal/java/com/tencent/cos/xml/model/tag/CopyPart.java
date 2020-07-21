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

package com.tencent.cos.xml.model.tag;

/**
 * 分块复制结果
 */

public class CopyPart {
    /**
     * 对象的实体标签（Entity Tag），是对象被创建时标识对象内容的信息标签，可用于检查对象的内容是否发生变化。
     * 例如8e0b617ca298a564c3331da28dcb50df，此头部并不一定返回对象的 MD5 值，而是根据对象上传和加密方式而有所不同。
     */
    public String eTag;

    /**
     * 对象最后修改时间，为 ISO8601 格式，例如2019-05-24T10:56:40Z
     */
    public String lastModified;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{CopyPart:\n");
        stringBuilder.append("ETag:").append(eTag).append("\n");
        stringBuilder.append("LastModified:").append(lastModified).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
