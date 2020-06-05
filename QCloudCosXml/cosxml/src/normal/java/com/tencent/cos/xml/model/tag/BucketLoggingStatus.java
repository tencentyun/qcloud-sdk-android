package com.tencent.cos.xml.model.tag;

public class BucketLoggingStatus {

    public LoggingEnabled loggingEnabled;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{BucketLoggingStatus:\n");
        if(loggingEnabled != null)stringBuilder.append(loggingEnabled.toString()).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class LoggingEnabled{
        public String targetBucket;
        public String targetPrefix;

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{LoggingEnabled:\n");
            stringBuilder.append("TargetBucket:").append(targetBucket).append("\n");
            stringBuilder.append("TargetPrefix:").append(targetPrefix).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}
