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
 * 存储桶列表
 */
public class ListBucket {
    /**
     * 存储桶的名称，格式为<BucketName-APPID>，例如examplebucket-1250000000
     */
    public String name;
    /**
     * 编码格式，对应请求中的 encoding-type 参数，且仅当请求中指定了 encoding-type 参数才会返回该节点
     */
    public String encodingType;
    /**
     * 对象键匹配前缀，对应请求中的 prefix 参数
     */
    public String prefix;
    /**
     * 起始对象键标记，从该标记之后（不含）按照 UTF-8 字典序返回对象键条目，对应请求中的 marker 参数
     */
    public String marker;
    /**
     * 单次响应返回结果的最大条目数量，对应请求中的 max-keys 参数
     * 注意：该参数会限制每一次 List 操作返回的最大条目数，COS 在每次 List 操作中将返回不超过 max-keys 所设定数值的条目。
     * 如果由于您设置了 max-keys 参数，导致单次响应中未列出所有对象，COS 会返回一项 nextmarker 参数作为您下次 List 请求的入参，
     * 以便您后续进行列出对象
     */
    public int maxKeys;
    /**
     * 响应条目是否被截断，布尔值，例如 true 或 false
     */
    public boolean isTruncated;
    /**
     * 仅当响应条目有截断（IsTruncated 为 true）才会返回该节点，
     * 该节点的值为当前响应条目中的最后一个对象键，当需要继续请求后续条目时，将该节点的值作为下一次请求的 marker 参数传入
     */
    public String nextMarker;
    /**
     * 对象条目
     */
    public List<Contents> contentsList;
    /**
     * 从 prefix 或从头（如未指定 prefix）到首个 delimiter 之间相同的部分，
     * 定义为 Common Prefix。仅当请求中指定了 delimiter 参数才有可能返回该节点
     */
    public List<CommonPrefixes> commonPrefixesList;
    /**
     * 分隔符，对应请求中的 delimiter 参数，且仅当请求中指定了 delimiter 参数才会返回该节点
     */
    public String delimiter;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{ListBucket:\n");
        stringBuilder.append("Name:").append(name).append("\n");
        stringBuilder.append("Encoding-Type:").append(encodingType).append("\n");
        stringBuilder.append("Prefix:").append(prefix).append("\n");
        stringBuilder.append("Marker:").append(marker).append("\n");
        stringBuilder.append("MaxKeys:").append(maxKeys).append("\n");
        stringBuilder.append("IsTruncated:").append(isTruncated).append("\n");
        stringBuilder.append("NextMarker:").append(nextMarker).append("\n");
        if(contentsList != null){
            for (Contents contents : contentsList){
                if(contents != null)stringBuilder.append(contents.toString()).append("\n");
            }
        }
        if(commonPrefixesList != null){
            for (CommonPrefixes commonPrefixes : commonPrefixesList){
                if(commonPrefixes != null)stringBuilder.append(commonPrefixes.toString()).append("\n");
            }
        }
        stringBuilder.append("Delimiter:").append(delimiter).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class Contents{
        /**
         * 对象键
         */
        public String key;
        /**
         * 对象最后修改时间，为 ISO8601 格式，如2019-05-24T10:56:40Z
         */
        public String lastModified;
        /**
         * 对象的实体标签（Entity Tag），是对象被创建时标识对象内容的信息标签，可用于检查对象的内容是否发生变化，
         * 例如“8e0b617ca298a564c3331da28dcb50df”，此头部并不一定返回对象的 MD5 值，而是根据对象上传和加密方式而有所不同
         */
        public String eTag;
        /**
         * 对象大小，单位为 Byte
         */
        public long size;
        /**
         * 对象持有者信息
         */
        public Owner owner;
        /**
         * 对象存储类型
         */
        public String storageClass;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Contents:\n");
            stringBuilder.append("Key:").append(key).append("\n");
            stringBuilder.append("LastModified:").append(lastModified).append("\n");
            stringBuilder.append("ETag:").append(eTag).append("\n");
            stringBuilder.append("Size:").append(size).append("\n");
            if (owner != null)stringBuilder.append(owner.toString()).append("\n");
            stringBuilder.append("StorageClass:").append(storageClass).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class CommonPrefixes{
        /**
         * Common Prefix 的前缀
         */
        public String prefix;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{CommonPrefixes:\n");
            stringBuilder.append("Prefix:").append(prefix).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Owner{
        /**
         * 对象持有者的 APPID
         */
        public String id;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Owner:\n");
            stringBuilder.append("Id:").append(id).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

}
