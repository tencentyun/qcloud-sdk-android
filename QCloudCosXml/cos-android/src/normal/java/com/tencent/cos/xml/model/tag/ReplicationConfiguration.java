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
 * 跨地域配置信息
 */
public class ReplicationConfiguration {
    /**
     * 发起者身份标示：
     * qcs::cam::uin/&lt;OwnerUin>:uin/&lt;SubUin>
     */
    public String role;
    /**
     * 具体配置信息，最多支持1000个，所有策略只能指向一个目标存储桶
     */
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
        /**
         * 用来标注具体 Rule 的名称
         */
        public String id;
        /**
         * 标识 Rule 是否生效，枚举值：Enabled，Disabled
         */
        public String status;
        /**
         * 前缀匹配策略，不可重叠，重叠返回错误，前缀匹配根目录为空
         */
        public String prefix;
        /**
         * 目标存储桶信息
         */
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
        /**
         * 资源标识符：
         * qcs::cos:[region]::[bucketname-Appid]
         */
        public String bucket;
        /**
         * 存储级别，枚举值：Standard，Standard_IA，默认值：原存储桶级别
         */
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
