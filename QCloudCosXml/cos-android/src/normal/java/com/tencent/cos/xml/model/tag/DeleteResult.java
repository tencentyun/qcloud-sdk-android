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
 * 批量删除结果
 */
public class DeleteResult {
    /**
     * 删除成功的对象列表，
     * 仅当使用 Verbose 模式才会返回该元素
     */
    public List<Deleted> deletedList;
    /**
     * 删除失败的对象列表
     */
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

    /**
     * 删除成功的对象
     */
    public static class Deleted{
        /**
         * 对象键
         */
        public String key;
        /**
         * 删除成功的版本 ID，仅当请求中指定了要删除对象的版本 ID 时才返回该元素
         */
        public String versionId;
        /**
         * 仅当对该对象的删除创建了一个删除标记，或删除的是该对象的一个删除标记时才返回该元素，布尔值，固定为 true
         */
        public boolean deleteMarker;
        /**
         * 仅当对该对象的删除创建了一个删除标记，或删除的是该对象的一个删除标记时才返回该元素，值为创建或删除的删除标记的版本 ID
         */
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

    /**
     * 删除失败的对象
     */
    public static class Error{
        /**
         * 对象键
         */
        public String key;
        /**
         * 删除失败的错误码，用来定位唯一的错误条件和确定错误场景
         */
        public String code;
        /**
         * 删除失败的具体错误信息
         */
        public String message;
        /**
         * 删除失败的版本 ID，仅当请求中指定了要删除对象的版本 ID 时才返回该元素
         */
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
