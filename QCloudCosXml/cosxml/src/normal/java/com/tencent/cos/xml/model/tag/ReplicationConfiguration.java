package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class ReplicationConfiguration {
    public String role;
    public List<Rule> rules;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{ReplicationConfiguration:\n");
        stringBuilder.append("Role:").append(role).append("\n");
        if(rules != null){
            for(Rule rule : rules){
                if(rule != null)stringBuilder.append(rule.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class Rule{
        public String id;
        public String status;
        public String prefix;
        public Destination destination;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Rule:\n");
            stringBuilder.append("Id:").append(id).append("\n");
            stringBuilder.append("Status:").append(status).append("\n");
            stringBuilder.append("Prefix:").append(prefix).append("\n");
            if(destination != null)stringBuilder.append(destination.toString()).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Destination{
        public String bucket;
        public String storageClass;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Destination:\n");
            stringBuilder.append("Bucket:").append(bucket).append("\n");
            stringBuilder.append("StorageClass:").append(storageClass).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

}
