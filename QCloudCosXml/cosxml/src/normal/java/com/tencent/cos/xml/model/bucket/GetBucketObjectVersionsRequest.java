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

import android.text.TextUtils;

import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Map;

/**
 * 拉取存储桶内的所有对象及其历史版本信息的请求.
 * @see com.tencent.cos.xml.CosXml#getBucketObjectVersions(GetBucketObjectVersionsRequest)
 * @see com.tencent.cos.xml.CosXml#getBucketObjectVersionsAsync(GetBucketObjectVersionsRequest, CosXmlResultListener)
 */
public class GetBucketObjectVersionsRequest extends BucketRequest {

    private String prefix;

    private String delimiter;

    private String encodingType;

    private String keyMarker;

    private String versionIdMarker;

    private int maxKeys = 1000;

    public GetBucketObjectVersionsRequest(String bucket) {
        super(bucket);
    }

    /**
     * GetBucketObjectVersionsRequest构造函数
     * @param bucket 存储桶名
     * @param prefix 对象键匹配前缀，限定响应中只包含指定前缀的对象键
     * @param delimiter 一个字符的分隔符，用于对对象键进行分组
     * @param keyMarker 起始对象键标记，从该标记之后（不含）按照 UTF-8 字典序返回对象版本条目
     * @param versionIdMarker 起始版本 ID 标记，从该标记之后（不含）返回对象版本条目
     */
    public GetBucketObjectVersionsRequest(String bucket, String prefix, String delimiter, String keyMarker, String versionIdMarker) {
        super(bucket);
        this.prefix = prefix;
        this.delimiter = delimiter;
        this.keyMarker = keyMarker;
        this.versionIdMarker = versionIdMarker;
    }

    /**
     * GetBucketObjectVersionsRequest构造函数
     * @param bucket 存储桶名
     * @param delimiter 一个字符的分隔符，用于对对象键进行分组
     * @param keyMarker 起始对象键标记，从该标记之后（不含）按照 UTF-8 字典序返回对象版本条目
     * @param versionIdMarker 起始版本 ID 标记，从该标记之后（不含）返回对象版本条目
     */
    public GetBucketObjectVersionsRequest(String bucket, String delimiter, String keyMarker, String versionIdMarker) {
        this(bucket, "", delimiter, keyMarker, versionIdMarker);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("versions", null);
        addQuery("prefix", prefix);
        addQuery("delimiter", delimiter);
        addQuery("encoding-type", encodingType);
        addQuery("key-marker", keyMarker);
        addQuery("version-id-marker", versionIdMarker);
        addQuery("max-keys", String.valueOf(maxKeys));

        return super.getQueryString();
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    /**
     * 设置对象键匹配前缀
     * @param prefix 对象键匹配前缀
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 设置字符的分隔符，用于对对象键进行分组
     * @param delimiter 字符的分隔符，用于对对象键进行分组
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * 设置返回值的编码方式
     * @param encodingType 返回值的编码方式
     */
    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    /**
     * 设置起始对象键标记
     * @param keyMarker 起始对象键标记
     */
    public void setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
    }

    /**
     * 设置起始版本 ID 标记
     * @param versionIdMarker 起始版本 ID 标记
     */
    public void setVersionIdMarker(String versionIdMarker) {
        this.versionIdMarker = versionIdMarker;
    }

    /**
     * 设置单次返回最大的条目数量，默认值为1000，最大为1000
     * @param maxKeys 单次返回最大的条目数量，默认值为1000，最大为1000
     */
    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public String getVersionIdMarker() {
        return versionIdMarker;
    }

    private void addQuery(String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            queryParameters.put(key, value);
        }
    }
}
