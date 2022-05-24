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

import java.util.List;

/**
 * 生命周期配置
 */
public class LifecycleConfiguration {
    /**
     * 规则列表
     */
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

    /**
     * 规则描述
     */
    public static class Rule{
        /**
         * 用于唯一地标识规则，长度不能超过255个字符
         */
        public String id;
        /**
         * Filter 用于描述规则影响的 Object 集合
         */
        public Filter filter;
        /**
         * 指明规则是否启用，枚举值：Enabled，Disabled
         */
        public String status;
        /**
         * 规则转换属性，对象何时转换为 Standard_IA 或 Archive
         */
        public Transition transition;
        /**
         * 规则过期属性
         */
        public Expiration expiration;
        /**
         * 指明非当前版本对象何时过期
         */
        public NoncurrentVersionExpiration noncurrentVersionExpiration;
        /**
         * 指明非当前版本对象何时转换为 STANDARD_IA 或 ARCHIVE
         */
        public NoncurrentVersionTransition noncurrentVersionTransition;
        /**
         * 设置允许分片上传保持运行的最长时间
         */
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
        /**
         * 指定规则所适用的前缀。匹配前缀的对象受该规则影响，Prefix 最多只能有一个
         */
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
        /**
         * 指明规则对应的动作在对象最后的修改日期过后多少天操作：
         * 如果是 Transition，该字段有效值是非负整数
         * 如果是 Expiration，该字段有效值为正整数，最大支持3650天
         */
        public int days;
        /**
         * 指明规则对应的动作在何时操作，支持2007-12-01T12:00:00.000Z和2007-12-01T00:00:00+08:00这两种格式
         */
        public String date;
        /**
         * 指定 Object 转储到的目标存储类型，枚举值： STANDARD_IA，ARCHIVE
         */
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
        /**
         * 指明规则对应的动作在何时操作，支持2007-12-01T12:00:00.000Z和2007-12-01T00:00:00+08:00这两种格式
         */
        public String date;
        /**
         * 指明规则对应的动作在对象最后的修改日期过后多少天操作：
         * 如果是 Transition，该字段有效值是非负整数
         * 如果是 Expiration，该字段有效值为正整数，最大支持3650天
         */
        public int days;
        /**
         * 删除过期对象删除标记，枚举值 true，false
         */
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
        /**
         * 指明规则对应的动作在对象变成非当前版本多少天后执行
         * 如果是 Transition，该字段有效值是非负整数
         * 如果是 Expiration，该字段有效值为正整数，最大支持3650天
         */
        public int noncurrentDays;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{NoncurrentVersionExpiration:\n");
            stringBuilder.append("NoncurrentDays:").append(noncurrentDays).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class NoncurrentVersionTransition{
        /**
         * 指明规则对应的动作在对象变成非当前版本多少天后执行
         * 如果是 Transition，该字段有效值是非负整数
         * 如果是 Expiration，该字段有效值为正整数，最大支持3650天
         */
        public int noncurrentDays;
        /**
         * 指定 Object 转储到的目标存储类型，枚举值： STANDARD_IA，ARCHIVE
         */
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
        /**
         * 指明分片上传开始后多少天内必须完成上传
         */
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
