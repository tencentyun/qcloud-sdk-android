package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2018/1/11.
 */

public class ListBucketVersions {

    public String name;
    public String prefix;
    public String keyMarker;
    public String versionIdMarker;
    public long maxKeys;
    public boolean isTruncated;
    public String nextKeyMarker;
    public String nextVersionIdMarker;

    public List<ObjectVersion> objectVersionList;


    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{ListVersionsResult:\n");
        stringBuilder.append("Name:").append(name).append("\n");
        stringBuilder.append("Prefix:").append(prefix).append("\n");
        stringBuilder.append("KeyMarker:").append(keyMarker).append("\n");
        stringBuilder.append("VersionIdMarker:").append(versionIdMarker).append("\n");
        stringBuilder.append("MaxKeys:").append(maxKeys).append("\n");
        stringBuilder.append("IsTruncated:").append(isTruncated).append("\n");
        stringBuilder.append("NextKeyMarker:").append(nextKeyMarker).append("\n");
        stringBuilder.append("NextVersionIdMarker:").append(nextVersionIdMarker).append("\n");
        if(objectVersionList != null){
            for(ObjectVersion objectVersion : objectVersionList){
                stringBuilder.append(objectVersion.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class ObjectVersion{
        public String key;
        public String versionId;
        public boolean isLatest;
        public String lastModified;
        public Owner owner;
    }

    public static class DeleteMarker extends ObjectVersion{

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{DeleteMarker:\n");
            stringBuilder.append("Key:").append(key).append("\n");
            stringBuilder.append("VersionId:").append(versionId).append("\n");
            stringBuilder.append("IsLatest:").append(isLatest).append("\n");
            stringBuilder.append("LastModified:").append(lastModified).append("\n");
            if(owner != null){
                stringBuilder.append(owner.toString()).append("\n");
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Owner{
        public String uid;
        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Owner:\n");
            stringBuilder.append("Uid:").append(uid).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Version extends ObjectVersion{
        public String eTag;
        public long size;
        public String storageClass;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Version:\n");
            stringBuilder.append("Key:").append(key).append("\n");
            stringBuilder.append("VersionId:").append(versionId).append("\n");
            stringBuilder.append("IsLatest:").append(isLatest).append("\n");
            stringBuilder.append("LastModified:").append(lastModified).append("\n");
            stringBuilder.append("ETag:").append(eTag).append("\n");
            stringBuilder.append("Size:").append(size).append("\n");
            stringBuilder.append("StorageClass:").append(storageClass).append("\n");
            if(owner != null){
                stringBuilder.append(owner.toString()).append("\n");
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

    }
}
