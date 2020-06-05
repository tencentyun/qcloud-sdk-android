package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/26.
 */

public class DeleteResult {
    public List<Deleted> deletedList;
    public List<Error> errorList;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{DeleteResult:\n");
        if(deletedList != null){
            for(Deleted deleted : deletedList){
                if(deleted != null)stringBuilder.append(deleted.toString()).append("\n");
            }
        }
        if(errorList != null){
            for(Error error : errorList){
                if(error != null)stringBuilder.append(error.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class Deleted{
        public String key;
        public String versionId;
        public boolean deleteMarker;
        public String  deleteMarkerVersionId;
        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Deleted:\n");
            stringBuilder.append("Key:").append(key).append("\n");
            stringBuilder.append("VersionId:").append(versionId).append("\n");
            stringBuilder.append("DeleteMarker:").append(deleteMarker).append("\n");
            stringBuilder.append("DeleteMarkerVersionId:").append(deleteMarkerVersionId).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Error{
        public String key;
        public String code;
        public String message;
        public String versionId;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{CosError:\n");
            stringBuilder.append("Key:").append(key).append("\n");
            stringBuilder.append("Code:").append(code).append("\n");
            stringBuilder.append("Message:").append(message).append("\n");
            stringBuilder.append("VersionId:").append(versionId).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}
