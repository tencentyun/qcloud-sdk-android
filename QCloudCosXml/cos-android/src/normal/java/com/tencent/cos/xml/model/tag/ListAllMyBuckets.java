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


import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * GET Service 结果的所有信息
 */
@XmlBean(name = "ListAllMyBucketsResult")
public class ListAllMyBuckets {
    /**
     * 存储桶持有者信息
     */
    public Owner owner;
    /**
     * 存储桶列表
     */
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

    /**
     * 存储桶持有者
     */
    @XmlBean
    public static class Owner{
        /**
         * 存储桶持有者的完整 ID
         */
        @XmlElement(name = "ID")
        public String id;
        /**
         * 存储桶持有者的名字
         */
        @XmlElement(name = "DisplayName")
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

    /**
     * 存储桶
     */
    @XmlBean
    public static class Bucket{
        /**
         * 存储桶的名称
         */
        public String name;
        /**
         * 存储桶所在地域
         */
        public String location;
        /**
         * 存储桶的创建时间，为 ISO8601 格式，例如2019-05-24T10:56:40Z
         */
        @XmlElement(name = "CreationDate")
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
