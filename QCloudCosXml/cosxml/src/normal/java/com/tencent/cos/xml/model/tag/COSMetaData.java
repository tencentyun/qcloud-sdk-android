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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 对象元数据 x-cos-meta-*
 */
public class COSMetaData {
    private Map<String, String> metaData;

    public COSMetaData() {
        metaData = new HashMap<>();
    }

    /**
     * 添加元数据
     * @param key header key
     * @param value header value
     */
    public void put(String key, String value) {
        String prefix = "x-cos-meta-";
        if (!key.startsWith(prefix)) {
            key = prefix + key;
        }
        metaData.put(key, value);
    }

    /**
     * 获取元数据值
     * @param key 元数据键
     * @return 元数据值
     */
    public String get(String key) {
        return metaData.get(key);
    }

    /**
     * 获取元数据集合
     * @return 元数据集合
     */
    public Set<String> keySet() {
        return metaData.keySet();
    }
}
