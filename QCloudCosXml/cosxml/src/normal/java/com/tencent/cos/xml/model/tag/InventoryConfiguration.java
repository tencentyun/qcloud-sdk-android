package com.tencent.cos.xml.model.tag;

import java.io.StringWriter;
import java.util.List;
import java.util.Set;

public class InventoryConfiguration {

    public static final String SCHEDULE_FREQUENCY_DAILY = "Daily";
    public static final String SCHEDULE_FREQUENCY_WEEKLY = "Weekly";


    /** 清单的名称，与请求参数中的 id 对应 */
    public String id;

    /** 清单是否启用的标识。如果设置为 True，清单功能将生效；如果设置为 False，将不生成任何清单 */
    public boolean isEnabled;

    /** 是否在清单中包含对象版本 */
    public String includedObjectVersions;

    /** 筛选待分析对象。清单功能将分析符合 Filter 中设置的前缀的对象 */
    public Filter filter;

    /** 设置清单结果中应包含的分析项目 */
    public OptionalFields optionalFields;

    /** 配置清单任务周期 */
    public Schedule schedule;

    /** 描述存放清单结果的信息 */
    public Destination destination;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{InventoryConfiguration:\n");
        stringBuilder.append("Id").append(id).append("\n");
        stringBuilder.append("IsEnabled:").append(isEnabled).append("\n");
        if(destination != null)stringBuilder.append(destination.toString()).append("\n");
        if(schedule != null)stringBuilder.append(schedule.toString()).append("\n");
        if(filter != null)stringBuilder.append(filter.toString()).append("\n");
        stringBuilder.append("IncludedObjectVersions:").append(includedObjectVersions).append("\n");
        if(optionalFields != null)stringBuilder.append(optionalFields.toString()).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class Filter{
        public String prefix;

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{Filter:\n");
            stringBuilder.append("Prefix:").append(prefix).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class OptionalFields{
        public Set<String> fields;

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{OptionalFields:\n");
            if(fields != null)
                for(String field : fields){
                    stringBuilder.append("Field:").append(field).append("\n");
                }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Schedule{
        public String frequency;

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{Schedule:\n");
            stringBuilder.append("Frequency:").append(frequency).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Destination{
        public COSBucketDestination cosBucketDestination;

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{Destination:\n");
            if(cosBucketDestination != null) stringBuilder.append(cosBucketDestination.toString()).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class COSBucketDestination{
        public String format;
        public String accountId;
        public String bucket;
        public String prefix;
        public Encryption encryption;

        public void setBucket(String region, String bucket){
            if(region != null && bucket != null){
                this.bucket = String.format("qcs::cos:%s::%s", region, bucket);
            }
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{COSBucketDestination:\n");
            stringBuilder.append("Format:").append(format).append("\n");;
            stringBuilder.append("AccountId:").append(accountId).append("\n");;
            stringBuilder.append("Bucket:").append(bucket).append("\n");;
            stringBuilder.append("Prefix:").append(prefix).append("\n");;
            if(encryption != null){
                stringBuilder.append(encryption.toString()).append("\n");
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class Encryption{
        public String sSECOS;

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{Encryption:\n");
            stringBuilder.append("SSE-COS:").append(sSECOS).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public enum Field{
        SIZE("Size"),
        LastModified_Date("LastModifiedDate"),
        StroageClass("StorageClass"),
        ETAG("Etag"),
        IS_MULTIPARTUPLOADed("IsMultipartUploaded"),
        REPLICATION_STATUS("ReplicationStatus");

        String value;
        private Field(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }

    public enum IncludedObjectVersions{
        /** 清单中将会包含所有对象版本, 并在清单中增加 VersionId，IsLatest，DeleteMarker 这几个字段 */
        ALL("All"),
        /** 清单中不包含对象版本信息 */
        CURRENT("Current");

        private String desc;

        IncludedObjectVersions(String desc){
            this.desc = desc;
        }

        public String getDesc(){
            return this.desc;
        }
    }

    public enum Frequency{
        DAILY("Daily");
        String value;
        private Frequency(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }

}
