package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class ListParts {
    /**
     * 分块上传的目标 Bucke
     */
    public String bucket;
    /**
     * 规定返回值的编码方式
     */
    public String encodingType;
    /**
     * Object 的名称
     */
    public String key;
    /**
     * 本次分块上传的 uploadID
     */
    public String uploadId;
    /**
     * 表示这些分块所有者的信息
     */
    public Owner owner;
    /**
     * 默认以 UTF-8 二进制顺序列出条目，所有列出条目从 marker 开始
     */
    public String partNumberMarker;
    /**
     * 表示本次上传发起者的信息
     */
    public Initiator initiator;
    /**
     * 表示这些分块的存储级别
     */
    public String storageClass;
    /**
     * 假如返回条目被截断，则返回 nextPartNumberMarker 就是下一个条目的起点
     */
    public String nextPartNumberMarker;
    /**
     * 单次返回最大的条目数量
     */
    public String maxParts;
    /**
     * 返回条目是否被截断，布尔值：TRUE，FALSE
     */
    public boolean isTruncated;
    /**
     * 表示每一个块的信息
     */
    public List<Part> parts;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{ListParts:\n");
        stringBuilder.append("Bucket:").append(bucket).append("\n");
        stringBuilder.append("Encoding-Type:").append(encodingType).append("\n");
        stringBuilder.append("Key:").append(key).append("\n");
        stringBuilder.append("UploadId:").append(uploadId).append("\n");
        if(owner != null)stringBuilder.append(owner.toString()).append("\n");
        stringBuilder.append("PartNumberMarker:").append(partNumberMarker).append("\n");
        if(initiator != null) stringBuilder.append(initiator.toString()).append("\n");
        stringBuilder.append("StorageClass:").append(storageClass).append("\n");
        stringBuilder.append("NextPartNumberMarker:").append(nextPartNumberMarker).append("\n");
        stringBuilder.append("MaxParts:").append(maxParts).append("\n");
        stringBuilder.append("IsTruncated:").append(isTruncated).append("\n");
        if(parts != null){
            for(Part part : parts){
                if(part != null)stringBuilder.append(part.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class Owner{
        /**
         * 创建者的一个唯一标识
         */
        public String id;
        /**
         * 创建者的用户名描述
         */
        public String disPlayName;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Owner:\n");
            stringBuilder.append("Id:").append(id).append("\n");
            stringBuilder.append("DisPlayName:").append(disPlayName).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Initiator{
        /**
         * 创建者的一个唯一标识
         */
        public String id;
        /**
         * 创建者的用户名描述
         */
        public String disPlayName;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Initiator:\n");
            stringBuilder.append("Id:").append(id).append("\n");
            stringBuilder.append("DisPlayName:").append(disPlayName).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Part{
        /**
         * 块的编号
         */
        public String partNumber;
        /**
         * 块最后修改时间
         */
        public String lastModified;
        /**
         * Object 块的 MD5 算法校验值
         */
        public String eTag;
        /**
         * 	块大小，单位 Byte
         */
        public String size;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Part:\n");
            stringBuilder.append("PartNumber:").append(partNumber).append("\n");
            stringBuilder.append("LastModified:").append(lastModified).append("\n");
            stringBuilder.append("ETag:").append(eTag).append("\n");
            stringBuilder.append("Size:").append(size).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

}
