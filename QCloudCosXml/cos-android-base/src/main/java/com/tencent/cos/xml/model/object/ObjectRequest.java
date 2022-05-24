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

package com.tencent.cos.xml.model.object;


import android.util.Base64;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.utils.DigestUtils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 对象相关请求基类
 */
public abstract class ObjectRequest extends CosXmlRequest {

    /**
     * 对象在cos上的路径
     */
    protected String cosPath;

    /**
     * 对象相关请求基类
     * @param bucket 存储桶名
     * @param cosPath cos上的路径
     */
    public ObjectRequest(String bucket, String cosPath){
        this.bucket = bucket;
        this.cosPath = cosPath;
    }

    /**
     * 设置对象在cos上的路径
     * @param cosPath 对象在cos上的路径
     */
    public void setCosPath(String cosPath){
        this.cosPath = cosPath;
    }

    /**
     * 支持 CSP 的 Bucket 可能在 path 上
     * @param config 服务配置
     * @return path
     */
    @Override
    public String getPath(CosXmlServiceConfig config) {
        return config.getUrlPath(bucket, cosPath);
    }

    /**
     * 获取对象在cos上的路径
     * @return 对象在cos上的路径
     */
    public String getCosPath() {
        return cosPath;
    }


    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(requestURL != null){
            return;
        }
        if(bucket == null || bucket.length() < 1){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "bucket must not be null ");
        }
        if(cosPath == null || cosPath.length() < 1){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "cosPath must not be null ");
        }
    }


    /**C
     * 对象SSE-COS服务端加密配置
     */
    public void setCOSServerSideEncryption(){
        this.addHeader("x-cos-server-side-encryption", "AES256");
    }

    /**
     * 对象SSE-C服务端加密配置
     * @param customerKey 服务端加密密钥
     * @throws CosXmlClientException 客户端异常
     */
    public void setCOSServerSideEncryptionWithCustomerKey(String customerKey) throws CosXmlClientException {
        if(customerKey != null){
            addHeader("x-cos-server-side-encryption-customer-algorithm", "AES256");
            addHeader("x-cos-server-side-encryption-customer-key", DigestUtils.getBase64(customerKey));
            String base64ForKeyMd5;
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                base64ForKeyMd5 = Base64.encodeToString(messageDigest.digest(customerKey.getBytes( Charset.forName("UTF-8"))), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
            }
            addHeader("x-cos-server-side-encryption-customer-key-MD5", base64ForKeyMd5);
        }
    }

    /**
     * 对象SSE-KMS服务端加密配置
     * @param customerKeyID 用于指定 KMS 的用户主密钥 CMK，如不指定，则使用 COS 默认创建的 CMK
     * @param json 用于指定加密上下文，值为 JSON 格式加密上下文键值对的 Base64 编码
     * @throws CosXmlClientException 客户端代理
     */
    public void setCOSServerSideEncryptionWithKMS(String customerKeyID, String json) throws CosXmlClientException {
        addHeader("x-cos-server-side-encryption", "cos/kms");
        if(customerKeyID != null){
            addHeader("x-cos-server-side-encryption-cos-kms-key-id", customerKeyID);
        }
        if(json != null){
            addHeader("x-cos-server-side-encryption-context", DigestUtils.getBase64(json));
        }
    }

    protected String getContentType() {
        List<String> contentType = requestHeaders.get("Content-Type");
        if (contentType == null || contentType.isEmpty()) {
            contentType = requestHeaders.get("content-type");
        }
        if (contentType == null || contentType.isEmpty()) {
            contentType = requestHeaders.get("Content-type");
        }

        return contentType != null && !contentType.isEmpty() ? contentType.get(0) : null;
    }
}
