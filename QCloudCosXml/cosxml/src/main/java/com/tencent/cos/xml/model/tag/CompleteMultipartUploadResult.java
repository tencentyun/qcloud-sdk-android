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

/**
 * 完成分块上传结果
 */

public class CompleteMultipartUploadResult {
    /**
     * 创建的Object的外网访问域名
     */
    public String location;
    /**
     * 分块上传的目标Bucket
     */
    public String bucket;
    /**
     * Object的名称
     */
    public String key;
    /**
     * 合并后文件的 MD5 算法校验值
     */
    public String eTag;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{CompleteMultipartUploadResult:\n");
        stringBuilder.append("Location:").append(location).append("\n");
        stringBuilder.append("Bucket:").append(bucket).append("\n");
        stringBuilder.append("Key:").append(key).append("\n");
        stringBuilder.append("ETag:").append(eTag).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
