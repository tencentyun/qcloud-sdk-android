package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class ListMultipartUploads {

    public String bucket;
    public String encodingType;
    public String keyMarker;
    public String uploadIdMarker;
    public String nextKeyMarker;
    public String nextUploadIdMarker;
    public String maxUploads;
    public boolean isTruncated;
    public String prefix;
    public String delimiter;
    public List<Upload> uploads;
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

    public static class Upload{
        public String key;
        public String uploadID;
        public String storageClass;
        public Initiator initiator;
        public Owner owner;
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
        public String prefix;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{CommonPrefixes:\n");
            stringBuilder.append("Prefix:").append(prefix).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

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
