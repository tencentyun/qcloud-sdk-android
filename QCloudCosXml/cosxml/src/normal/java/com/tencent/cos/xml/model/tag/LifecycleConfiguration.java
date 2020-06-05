package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class LifecycleConfiguration {
    public List<Rule> rules;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{LifecycleConfiguration:\n");
        if(rules != null){
            for (Rule rule : rules){
                if(rule != null) stringBuilder.append(rule.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class Rule{
        public String id;
        public Filter filter;
        public String status;
        public Transition transition;
        public Expiration expiration;
        public NoncurrentVersionExpiration noncurrentVersionExpiration;
        public NoncurrentVersionTransition noncurrentVersionTransition;
        public AbortIncompleteMultiUpload abortIncompleteMultiUpload;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Rule:\n");
            stringBuilder.append("Id:").append(id).append("\n");
            if (filter != null)stringBuilder.append(filter.toString()).append("\n");
            stringBuilder.append("Status:").append(status).append("\n");
            if(transition != null)stringBuilder.append(transition.toString()).append("\n");
            if(expiration != null)stringBuilder.append(expiration.toString()).append("\n");
            if(noncurrentVersionExpiration != null)stringBuilder.append(noncurrentVersionExpiration.toString()).append("\n");
            if(noncurrentVersionTransition != null)stringBuilder.append(noncurrentVersionTransition.toString()).append("\n");
            if(abortIncompleteMultiUpload != null)stringBuilder.append(abortIncompleteMultiUpload.toString()).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Filter{
        public String prefix;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Filter:\n");
            stringBuilder.append("Prefix:").append(prefix).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Transition{
        public int days;
        public String date;
        public String storageClass;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Transition:\n");
            stringBuilder.append("Days:").append(days).append("\n");
            stringBuilder.append("Date:").append(date).append("\n");
            stringBuilder.append("StorageClass:").append(storageClass).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Expiration{
        public String date;
        public int days;
        public String expiredObjectDeleteMarker;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Expiration:\n");
            stringBuilder.append("Days:").append(days).append("\n");
            stringBuilder.append("Date:").append(date).append("\n");
            stringBuilder.append("ExpiredObjectDeleteMarker:").append(expiredObjectDeleteMarker).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class NoncurrentVersionExpiration{
        public int  noncurrentDays;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{NoncurrentVersionExpiration:\n");
            stringBuilder.append("NoncurrentDays:").append(noncurrentDays).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class NoncurrentVersionTransition{
        public int noncurrentDays;
        public String storageClass;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{NoncurrentVersionTransition:\n");
            stringBuilder.append("NoncurrentDays:").append(noncurrentDays).append("\n");
            stringBuilder.append("StorageClass:").append(storageClass).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class AbortIncompleteMultiUpload{
        public int daysAfterInitiation;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{AbortIncompleteMultiUpload:\n");
            stringBuilder.append("DaysAfterInitiation:").append(daysAfterInitiation).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}
