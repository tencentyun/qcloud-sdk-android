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

package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * 查询存储桶（Bucket）中正在进行中的分块上传对象的请求.
 * @see com.tencent.cos.xml.CosXml#listMultiUploads(ListMultiUploadsRequest)
 * @see com.tencent.cos.xml.CosXml#listMultiUploadsAsync(ListMultiUploadsRequest, CosXmlResultListener)
 */
final public class ListMultiUploadsRequest extends BucketRequest {

    /**
     * Delimiter is a sign.
     * If Prefix exists, the same paths between Prefix and delimiter will be grouped as the same
     * type and defined Common Prefix, then all Common Prefixes will be listed.
     * If Prefix doesn't exist, the listing process will start from the beginning of the path
     */
    private String delimiter;
    /** Indicate the encoding method of the returned value */
    private String encodingType;
    /** Prefix match, used to specify the prefix address of the returned file */
    private String prefix;
    /** Max number of entries returned each time, default is 1000 */
    private String maxUploads;
    /**
     * Used together with upload-id-marker
     * If upload-id-marker is not specified, entries whose ObjectNames are in front of key-marker
     * (according to alphabetical order) will be listed.If upload-id-marker is specified, besides
     * the above entries, those whose ObjectNames are equal to key-marker and UploadIDs are in front
     * of upload-id-marker (according to alphabetical order) will also be listed.
     */
    private String keyMarker;
    /**
     * Used together with key-marker
     * If key-marker is not specified, upload-id-marker will be ignored
     * If key-marker is specified, entries whose ObjectNames are in front of key-marker
     * (according to alphabetical order) will be listed, and entries whose ObjectNames
     * are equal to key-marker and UploadIDs are in front of upload-id-marker
     * (according to alphabetical order) will also be listed.
     */
    private String uploadIdMarker;

    public ListMultiUploadsRequest(String bucket){
        super(bucket);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("uploads", null);
        if(delimiter != null){
            queryParameters.put("delimiter",delimiter);
        }
        if(encodingType != null){
            queryParameters.put("encoding-type",encodingType);
        }
        if(prefix != null){
            queryParameters.put("prefix",prefix);
        }
        if(maxUploads != null){
            queryParameters.put("max-uploads",maxUploads);
        }
        if(keyMarker != null){
            queryParameters.put("key-marker",keyMarker);
        }
        if(uploadIdMarker != null){
            queryParameters.put("upload-id-marker",uploadIdMarker);
        }
        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() {
        return null;
    }

    /**
     * <p>
     * 设置请求的delimiter
     * </p>
     * <p>
     * 定界符为一个符号，对 Object 名字包含指定前缀且第一次出现 delimiter 字符之间的 Object 作为一组元素：common prefix。
     * 如果没有 prefix，则从路径起点开始
     * </p>
     *
     * @param delimiter 请求的delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * 获取请求的delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * <p>
     * 设置返回值的编码格式。
     * </p>
     * <p>
     * 合法值：url
     * </p>
     *
     * @param encodingType 返回值的编码格式
     */
    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    /**
     * 获取返回值的编码格式
     */
    public String getEncodingType() {
        return encodingType;
    }

    /**
     * <p>
     * 设置请求的key-marker
     * </p>
     *
     * <p>
     * 与 upload-id-marker 一起使用
     * 当 upload-id-marker 未被指定时，ObjectName 字母顺序大于 key-marker 的条目将被列出
     * 当upload-id-marker被指定时，ObjectName 字母顺序大于key-marker的条目被列出，
     * ObjectName 字母顺序等于 key-marker 同时 UploadID 大于 upload-id-marker 的条目将被列出。
     * </p>
     *
     * @param keyMarker 请求的key-marker
     */
    public void setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
    }

    /**
     * 获取请求的key-marker
     */
    public String getKeyMarker() {
        return keyMarker;
    }

    /**
     * 设置最大返回的 multipart 数量，合法取值从1到1000，默认1000
     * @param maxUploads 最大返回的 multipart 数量
     */
    public void setMaxUploads(String maxUploads) {
        this.maxUploads = maxUploads;
    }

    /**
     * 获取由用户设置的maxUploads值
     */
    public String getMaxUploads() {
        return maxUploads;
    }

    /**
     * <p>
     * 设置限定返回的 Object 的前缀
     * </p>
     *
     * @param prefix 限定返回的 Object 的前缀
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 获取用户设置的前缀prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * <p>
     * 设置请求的Upload-id-marker
     * </p>
     * <p>
     * 与 key-marker 一起使用
     * 当 key-marker 未被指定时，upload-id-marker 将被忽略
     * 当 key-marker 被指定时，ObjectName字母顺序大于 key-marker 的条目被列出，
     * ObjectName 字母顺序等于 key-marker 同时 UploadID 大于 upload-id-marker 的条目将被列出。
     * </p>
     * @param uploadIdMarker 请求的Upload-id-marker
     */
    public void setUploadIdMarker(String uploadIdMarker) {
        this.uploadIdMarker = uploadIdMarker;
    }

    /**
     * 获取设置的upload-id-marker
     */
    public String getUploadIdMarker() {
        return uploadIdMarker;
    }

}
