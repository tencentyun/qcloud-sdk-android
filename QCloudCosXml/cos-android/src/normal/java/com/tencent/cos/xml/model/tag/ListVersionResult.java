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

import java.util.List;

/**
 * Bucket Object versions 结果信息
 */
public class ListVersionResult {
    /** 存储桶的名称，格式为&lt;BucketName-APPID&gt;，例如examplebucket-1250000000 */
    public String name;
    /** 对象键匹配前缀，对应请求中的 prefix 参数 */
    public String prefix;
    /** 起始对象键标记，从该标记之后（不含）按照 UTF-8 字典序返回对象版本条目，对应请求中的 key-marker 参数 */
    public String keyMarker;
    /** 起始版本 ID 标记，从该标记之后（不含）返回对象版本条目，对应请求中的 version-id-marker 参数 */
    public String versionIdMarker;
    /** 编码格式，对应请求中的 encoding-type 参数，且仅当请求中指定了 encoding-type 参数才会返回该节点 */
    public String encodingType;
    /** 单次响应返回结果的最大条目数量，对应请求中的 max-keys 参数 */
    public int maxKeys;
    /** 响应条目是否被截断，布尔值，例如 true 或 false */
    public boolean isTruncated;
    /** 分隔符，对应请求中的 delimiter 参数，且仅当请求中指定了 delimiter 参数才会返回该节点 */
    public String delimiter;
    /**
     * 仅当响应条目有截断（IsTruncated 为 true）才会返回该节点，
     * 该节点的值为当前响应条目中的最后一个对象键，当需要继续请求后续条目时，将该节点的值作为下一次请求的 key-marker 参数传入
     */
    public String nextKeyMarker;
    /**
     * 仅当响应条目有截断（IsTruncated 为 true）才会返回该节点，
     * 该节点的值为当前响应条目中的最后一个对象的版本 ID，当需要继续请求后续条目时，将该节点的值作为下一次请求的 version-id-marker 参数传入
     */
    public String nextVersionIdMarker;
    /**
     * 从 prefix 或从头（若未指定 prefix）到首个 delimiter 之间相同的部分，
     * 定义为 Common Prefix。仅当请求中指定了 delimiter 参数才有可能返回该节点
     */
    public List<CommonPrefixes> commonPrefixes;
    /** 对象版本条目 */
    public List<Version> versions;
    /** 对象删除标记条目 */
    public List<DeleteMarker> deleteMarkers;

    public static class CommonPrefixes {
        /** 单条 Common Prefix 的前缀 */
        public String prefix;
    }


    public static class Version {
        /** 对象键 */
        public String key;
        /** 对象的版本 ID */
        public String versionID;
        /** 当前版本是否为该对象的最新版本 */
        public boolean isLatest;
        /** 当前版本的最后修改时间，为 ISO8601 格式，例如2019-05-24T10:56:40Z */
        public String lastModified;
        /**
         * 对象的实体标签（Entity Tag），
         * 是对象被创建时标识对象内容的信息标签，可用于检查对象的内容是否发生变化，
         * 例如"8e0b617ca298a564c3331da28dcb50df"。此头部并不一定返回对象的 MD5 值，而是根据对象上传和加密方式而有所不同
         */
        public String etag;
        /** 对象大小，单位为 Byte */
        public long size;
        /** 对象存储类型 */
        public String storageClass;
        /** 对象持有者信息 */
        public Owner owner;
    }

    public static class DeleteMarker {
        /** 对象键 */
        public String key;
        /** 对象的删除标记的版本 ID */
        public String versionId;
        /** 当前删除标记是否为该对象的最新版本 */
        public boolean isLatest;
        /** 当前删除标记的删除时间，为 ISO8601 格式，例如2019-05-24T10:56:40Z */
        public String lastModified;
        /** 对象持有者信息 */
        public Owner owner;
    }

    public static class Owner {
        /** 对象持有者的 APPID */
        public String id;
        /** 对象持有者的名字 */
        public String displayName;
    }

}
