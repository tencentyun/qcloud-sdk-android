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

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import com.tencent.cos.xml.common.RequestMethod;

import java.util.Map;

/**
 * 获取存储桶（Bucket）所有或者部分对象的版本信息的请求.
 * @see com.tencent.cos.xml.CosXml#listBucketVersions(ListBucketVersionsRequest)
 * @see com.tencent.cos.xml.CosXml#listBucketVersionsAsync(ListBucketVersionsRequest, CosXmlResultListener)
 */
@Deprecated
public class ListBucketVersionsRequest extends BucketRequest {

    private String prefix;
    private String keyMarker;
    private String versionIdMarker;
    private String delimiter;
    private String encodingType; // 目前支持 "url", url Encoding
    private String maxKeys = "1000";


    public ListBucketVersionsRequest(String bucket) {
        super(bucket);
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    /**
     * 设置对象键匹配前缀，
     * 限定响应中只包含指定前缀的对象键
     * @param prefix 对象键匹配前缀
     */
    public void setPrefix(String prefix){
        if(prefix != null){
            this.prefix = prefix;
        }
    }

    /**
     * 设置起始对象键标记，
     * 从该标记之后（不含）按照 UTF-8 字典序返回对象版本条目
     * @param keyMarker 起始对象键标记
     */
    public void setKeyMarker(String keyMarker){
        if(keyMarker != null){
            this.keyMarker = keyMarker;
        }
    }

    /**
     * 设置起始版本 ID 标记，
     * 从该标记之后（不含）返回对象版本条目
     * @param versionIdMarker 起始版本 ID 标记
     */
    public void setVersionIdMarker(String versionIdMarker){
        if(versionIdMarker != null){
            this.versionIdMarker = versionIdMarker;
        }
    }

    /**
     * 设置字符分隔符，用于对对象键进行分组。
     * 所有对象键中从 prefix 或从头（若未指定 prefix）到首个 delimiter 之间相同的部分将作为 CommonPrefixes 下的一个 Prefix 节点。被分组的对象键不再出现在后续对象列表中。
     * @param delimiter 字符的分隔符
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * 设置规定返回值的编码方式，
     * 可选值：url，代表返回的对象键为 URL 编码（百分号编码）后的值
     * @param encodingType 规定返回值的编码方式
     */
    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    /**
     * 设置单次返回最大的条目数量，
     * 默认值为1000，最大为1000
     * @param maxKeys 单次返回最大的条目数量
     */
    public void setMaxKeys(int maxKeys) {
        this.maxKeys = String.valueOf(maxKeys);
    }

    @Override
    public Map<String, String> getQueryString() {
        queryParameters.put("versions", null);
        if(prefix != null){
            queryParameters.put("prefix", prefix);
        }
        if(keyMarker != null){
            queryParameters.put("key-marker", keyMarker);
        }
        if(versionIdMarker != null){
            queryParameters.put("version-id-marker", versionIdMarker);
        }
        if(delimiter != null){
            queryParameters.put("delimiter", delimiter);
        }
        if(encodingType != null){
            queryParameters.put("encoding-type", encodingType);
        }
        if(!maxKeys.equals("1000")){
            queryParameters.put("max-keys", maxKeys);
        }
        return super.getQueryString();
    }
}
