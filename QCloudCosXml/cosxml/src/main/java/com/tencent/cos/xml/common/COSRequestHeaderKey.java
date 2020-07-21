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

package com.tencent.cos.xml.common;

/**
 * 请求HTTP header键
 */
final public class COSRequestHeaderKey {
    public final static String CACHE_CONTROL = "Cache-Control";
    public final static String CONTENT_DISPOSITION = "Content-Disposition";
    public final static String CONTENT_ENCODING = "Content-Encoding";
    public final static String EXPIRES = "Expires";
    public final static String ORIGIN = "Origin";
    public final static String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    public final static String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    public final static String IF_MODIFIED_SINCE = "If-Modified-Since";
    public final static String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public final static String IF_MATCH = "If-Match";
    public final static String IF_NONE_MATCH = "If-None-Match";
    public final static String APPLICATION_XML = "application/xml";
    public final static String TEXT_PLAIN = "text/plain";
    public final static String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public final static String RANGE = "Range";

    /**
     * 定义目标对象的访问控制列表（ACL）属性。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/30752#.E9.A2.84.E8.AE.BE.E7.9A.84-acl">ACL 概述</a> 文档中对象的预设 ACL 部分，例如 default，private，public-read 等，默认为 default
     */
    public final static String X_COS_ACL = "x-cos-acl";
    /**
     * 赋予被授权者读取目标对象的权限，
     */
    public final static String X_COS_GRANT_READ = "x-cos-grant-read";
    /**
     * 赋予被授权者写入目标对象的权限，
     */
    public final static String X_COS_GRANT_WRITE = "x-cos-grant-write";
    /**
     * 赋予被授权者读取目标对象的访问控制列表（ACL）的权限
     */
    public final static String X_COS_GRANT_READ_ACP = "x-cos-grant-read-acp";
    /**
     * 赋予被授权者写入目标对象的访问控制列表（ACL）的权限
     */
    public final static String X_COS_GRANT_WRITE_ACP = "x-cos-grant-write-acp";
    /**
     * 赋予被授权者操作目标对象的所有权限，
     */
    public final static String X_COS_GRANT_FULL_CONTROL = "x-cos-grant-full-control";
    /**
     * 源对象的 URL，其中对象键需经过 URLEncode，可以通过 versionId 参数指定源对象的版本，例如：
     * sourcebucket-1250000001.cos.ap-shanghai.myqcloud.com/example-%E8%85%BE%E8%AE%AF%E4%BA%91.jpg
     * 或sourcebucket-1250000001.cos.ap-shanghai.myqcloud.com/example-%E8%85%BE%E8%AE%AF%E4%BA%91.jpg?versionId=MTg0NDUxNzYzMDc0NDMzNDExOTc
     */
    public final static String X_COS_COPY_SOURCE = "x-cos-copy-source";
    /**
     * 是否复制源对象的元数据信息，枚举值：Copy，Replaced，默认为 Copy：
     * 如果标记为 Copy，则复制源对象的元数据信息
     * 如果标记为 Replaced，则按本次请求的请求头中的元数据信息作为目标对象的元数据信息
     * 当目标对象和源对象为同一对象时，即用户试图修改元数据时，则标记必须为 Replaced
     */
    public final static String X_COS_METADATA_DIRECTIVE = "x-cos-metadata-directive";
    /**
     * 当对象在指定时间后被修改，则执行复制操作，否则返回 HTTP 状态码为412（Precondition Failed）
     */
    public final static String X_COS_COPY_SOURCE_IF_MODIFIED_SINCE= "x-cos-copy-source-If-Modified-Since";
    /**
     * 当对象在指定时间后未被修改，则执行复制操作，否则返回 HTTP 状态码为412（Precondition Failed）
     */
    public final static String X_COS_COPY_SOURCE_IF_UNMODIFIED_SINCE = "x-cos-copy-source-If-Unmodified-Since";
    /**
     * 当对象的 ETag 与指定的值一致，则执行复制操作，否则返回 HTTP 状态码为412（Precondition Failed）
     */
    public final static String X_COS_COPY_SOURCE_IF_MATCH = "x-cos-copy-source-If-Match";
    /**
     * 当对象的 ETag 与指定的值不一致，则执行复制操作，否则返回 HTTP 状态码为412（Precondition Failed）
     */
    public final static String X_COS_COPY_SOURCE_IF_NONE_MATCH = "x-cos-copy-source-If-None-Match";
    /**
     * 目标对象的存储类型。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/33417">存储类型</a> 文档，例如 STANDARD_IA，ARCHIVE。默认值：STANDARD
     */
    public final static String X_COS_STORAGE_CLASS_ = "x-cos-storage-class";

    /**
     * 源对象的字节范围，范围值必须使用 bytes=first-last 格式，first 和 last 都是基于 0 开始的偏移量。
     * 例如 bytes=0-9 表示您希望拷贝源对象的开头10个字节的数据，如果不指定，则表示拷贝整个对象
     */
    public final static String X_COS_COPY_SOURCE_RANGE = "x-cos-copy-source-range";
}
