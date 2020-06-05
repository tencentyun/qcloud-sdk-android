package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class ListAllMyBuckets {

    public Owner owner;
    public List<Bucket> buckets;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{ListAllMyBuckets:\n");
        if(owner != null)stringBuilder.append(owner.toString()).append("\n");
        stringBuilder.append("Buckets:\n");
        for(Bucket bucket : buckets){
            if(bucket != null)stringBuilder.append(bucket.toString()).append("\n");
        }
        stringBuilder.append("}").append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class  Owner{
        public String id;
        public String disPlayName;
        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Owner:\n");
            stringBuilder.append("ID:").append(id).append("\n");
            stringBuilder.append("DisPlayName:").append(disPlayName).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Bucket{
        public String name;
        public String location;
        public String createDate;
        public String type;
        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Bucket:\n");
            stringBuilder.append("Name:").append(name).append("\n");
            stringBuilder.append("Location:").append(location).append("\n");
            stringBuilder.append("CreateDate:").append(createDate).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}
