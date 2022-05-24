/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.model.tag;

import java.util.Set;

/**
 * 清单配置参数
 */
public class InventoryConfiguration {

    public static final String SCHEDULE_FREQUENCY_DAILY = "Daily";
    public static final String SCHEDULE_FREQUENCY_WEEKLY = "Weekly";


    /** 清单的名称，与请求参数中的 id 对应 */
    public String id;

    /** 清单是否启用的标识。如果设置为 True，清单功能将生效；如果设置为 False，将不生成任何清单 */
    public boolean isEnabled;

    /**
     * 是否在清单中包含对象版本
     * 如果设置为 All ，清单中将会包含所有对象版本，并在清单中增加 VersionId， IsLatest， DeleteMarker 这几个字段
     * 如果设置为 Current，则清单中不包含对象版本信息
     */
    public String includedObjectVersions;

    /** 筛选待分析对象。清单功能将分析符合 Filter 中设置的前缀的对象 */
    public Filter filter;

    /** 清单结果中应包含的分析维度 */
    public OptionalFields optionalFields;

    /** 清单任务周期 */
    public Schedule schedule;

    /** 存放清单结果的信息 */
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
        /**
         * 需要分析的对象的前缀
         */
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
        /**
         * 清单结果中可选包含的分析维度名称，可选字段包括： Size， LastModifiedDate， StorageClass， ETag， IsMultipartUploaded， ReplicationStatus
         */
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
        /**
         * 清单任务周期，可选项为按日或者按周
         */
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
        /**
         * 清单结果导出后存放的存储桶信息
         */
        public COSBucketDestination cosBucketDestination;

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{Destination:\n");
            if(cosBucketDestination != null) stringBuilder.append(cosBucketDestination.toString()).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    /**
     * 清单结果导出后存放的存储桶信息
     */
    public static class COSBucketDestination{
        /**
         * 清单分析结果的文件形式，可选项为 CSV 格式和 ORC 格式
         */
        public String format;
        /**
         * 存储桶的所有者 ID
         */
        public String accountId;
        /**
         * 清单分析结果的存储桶名
         */
        public String bucket;
        /**
         * 清单分析结果的前缀
         */
        public String prefix;
        /**
         * 为清单结果提供服务端加密的选项
         */
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

    /**
     * 为清单结果提供服务端加密的选项
     */
    public static class Encryption{
        /**
         * COS 托管密钥的加密方式
         */
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
