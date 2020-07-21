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
 * 所有分块上传的信息
 */
public class ListMultipartUploads {
    /**
     * 分块上传的目标 Bucke
     */
    public String bucket;
    /**
     * 规定返回值的编码格式，合法值：url
     */
    public String encodingType;
    /**
     * 列出条目从该 key 值开始
     */
    public String keyMarker;
    /**
     * 列出条目从该 UploadId 值开始
     */
    public String uploadIdMarker;
    /**
     * 假如返回条目被截断，则返回的 NextKeyMarker 就是下一个条目的起点
     */
    public String nextKeyMarker;
    /**
     * 假如返回条目被截断，则返回的 UploadId 就是下一个条目的起点
     */
    public String nextUploadIdMarker;
    /**
     * 设置最大返回的 multipart 数量，合法取值从0 - 1000
     */
    public String maxUploads;
    /**
     * 返回条目是否被截断，布尔值：TRUE，FALSE
     */
    public boolean isTruncated;
    /**
     * 限定返回的 Objectkey 必须以 Prefix 作为前缀，
     * 注意使用 prefix 查询时，返回的 key 中仍会包含 Prefix
     */
    public String prefix;
    /**
     * 定界符为一个符号，对 object 名字包含指定前缀且第一次出现 delimiter 字符之间的 object 作为一组元素：common prefix。若无 prefix，则从路径起点开始
     */
    public String delimiter;
    /**
     * 每个 Upload 的信息
     */
    public List<Upload> uploads;
    /**
     * 将 prefix 到 delimiter 之间的相同路径归为一类，定义为 Common Prefix
     */
    public List<CommonPrefixes> commonPrefixes;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{ListMultipartUploads:\n");
        stringBuilder.append("Bucket:").append(bucket).append("\n");
        stringBuilder.append("Encoding-Type:").append(encodingType).append("\n");
        stringBuilder.append("KeyMarker:").append(keyMarker).append("\n");
        stringBuilder.append("UploadIdMarker:").append(uploadIdMarker).append("\n");
        stringBuilder.append("NextKeyMarker:").append(nextKeyMarker).append("\n");
        stringBuilder.append("NextUploadIdMarker:").append(nextUploadIdMarker).append("\n");
        stringBuilder.append("MaxUploads:").append(maxUploads).append("\n");
        stringBuilder.append("IsTruncated:").append(isTruncated).append("\n");
        stringBuilder.append("Prefix:").append(prefix).append("\n");
        stringBuilder.append("Delimiter:").append(delimiter).append("\n");
        if(uploads != null){
            for (Upload upload : uploads){
                if(upload != null)stringBuilder.append(upload.toString()).append("\n");
            }
        }
        if(commonPrefixes != null){
            for (CommonPrefixes commonPrefix : commonPrefixes){
                if(commonPrefix != null)stringBuilder.append(commonPrefix.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * Upload 的信息
     */
    public static class Upload{
        /**
         * Object 的名称
         */
        public String key;
        /**
         * 标示本次分块上传的 ID
         */
        public String uploadID;
        /**
         * 用来表示分块的存储级别，枚举值：STANDARD，STANDARD_IA，ARCHIVE
         */
        public String storageClass;
        /**
         * 用来表示本次上传发起者的信息
         */
        public Initiator initiator;
        /**
         * 用来表示这些分块所有者的信息
         */
        public Owner owner;
        /**
         * 分块上传的起始时间
         */
        public String initiated;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Upload:\n");
            stringBuilder.append("Key:").append(key).append("\n");
            stringBuilder.append("UploadID:").append(uploadID).append("\n");
            stringBuilder.append("StorageClass:").append(storageClass).append("\n");
            if(initiator != null) stringBuilder.append(initiator.toString()).append("\n");
            if(owner != null) stringBuilder.append(owner.toString()).append("\n");
            stringBuilder.append("Initiated:").append(initiated).append("\n");
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

    /**
     * 上传发起者的信息
     */
    public static class Initiator{
        public String uin;
        public String id;
        public String displayName;
        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Initiator:\n");
            stringBuilder.append("Uin:").append(uin).append("\n");
            stringBuilder.append("Id:").append(id).append("\n");
            stringBuilder.append("DisplayName:").append(displayName).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    /**
     * 分块所有者的信息
     */
    public static class Owner{
        public String uid;
        public String id;
        public String displayName;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Owner:\n");
            stringBuilder.append("Uid:").append(uid).append("\n");
            stringBuilder.append("Id:").append(id).append("\n");
            stringBuilder.append("DisplayName:").append(displayName).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}
