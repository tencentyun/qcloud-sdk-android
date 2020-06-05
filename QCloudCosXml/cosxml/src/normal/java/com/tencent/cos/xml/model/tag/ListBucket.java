package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class ListBucket {

    public String name;
    public String encodingType;
    public String prefix;
    public String marker;
    public int maxKeys;
    public boolean isTruncated;
    public String nextMarker;
    public List<Contents> contentsList;
    public List<CommonPrefixes> commonPrefixesList;
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
        public String key;
        public String lastModified;
        public String eTag;
        public long size;
        public Owner owner;
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
