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

import android.net.Uri;

import androidx.annotation.NonNull;

import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.crypto.ObjectMetadata;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.model.tag.pic.PicOperations;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * 简单上传的请求.
 * @see SimpleCosXml#putObject(PutObjectRequest)
 * @see SimpleCosXml#putObjectAsync(PutObjectRequest, CosXmlResultListener)
 */
public class PutObjectRequest extends BasePutObjectRequest implements TransferRequest {
    private ObjectMetadata metadata;

    protected PutObjectRequest(String bucket, String cosPath) {
        super(bucket, cosPath);
    }

    public PutObjectRequest(String bucket, String cosPath, String srcPath) {
        super(bucket, cosPath, srcPath);
    }

    public PutObjectRequest(String bucket, String cosPath, Uri uri) {
        super(bucket, cosPath, uri);
    }

    public PutObjectRequest(String bucket, String cosPath, byte[] data) {
        super(bucket, cosPath, data);
    }

    public PutObjectRequest(String bucket, String cosPath, StringBuilder stringBuilder) {
        super(bucket, cosPath, stringBuilder);
    }

    public PutObjectRequest(String bucket, String cosPath, InputStream inputStream) {
        super(bucket, cosPath, inputStream);
    }

    public PutObjectRequest(String bucket, String cosPath, URL url) {
        super(bucket, cosPath, url);
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if (metadata != null) {
            Map<String, Object> rawMetadata = metadata.getRawMetadata();
            Map<String, String> useMetadata = metadata.getUserMetadata();

            for (Map.Entry<String, Object> entry : rawMetadata.entrySet()) {
                addHeader(entry.getKey(), entry.getValue().toString());
            }
            for (Map.Entry<String, String> entry : useMetadata.entrySet()) {
                addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    public void setMetadata(ObjectMetadata metadata) {
        this.metadata = metadata;
    }


    public ObjectMetadata getMetadata() {
        return metadata;
    }

    /**
     * <p>
     * 设置Cache-Control头部
     * </p>
     * @param cacheControl Cache-Control头部
     */
    public void setCacheControl(@NonNull String cacheControl) {
        addHeader(COSRequestHeaderKey.CACHE_CONTROL,cacheControl);
    }

    /**
     * <p>
     * 设置Content-Disposition头部部
     * </p>
     * @param contentDisposition Content-Disposition头部
     */
    public void setContentDisposition(@NonNull String contentDisposition) {
       addHeader(COSRequestHeaderKey.CONTENT_DISPOSITION,contentDisposition);
    }

    /**
     * <p>
     * 设置Content-Encoding头部
     * </p>
     * @param contentEncoding Content-Encoding头部
     */
    public void setContentEncodeing(@NonNull String contentEncoding) {
        addHeader(COSRequestHeaderKey.CONTENT_ENCODING,contentEncoding);
    }

    /**
     * <p>
     * 设置Expires头部
     * </p>
     * @param expires Expires头部
     */
    public void setExpires(@NonNull String expires) {
        addHeader(COSRequestHeaderKey.EXPIRES,expires);
    }

    /**
     * 设置用户自定义头部信息
     * @param key 自定义头部信息的key值，需要以 x-cos-meta- 开头
     * @param value 自定义头部信息的value值。
     */
    public void setXCOSMeta(String key, String value){
        if(key != null && value != null){
            addHeader(key,value);
        }
    }

    /**
     * 定义对象的访问控制列表（ACL）属性。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/30752#.E9.A2.84.E8.AE.BE.E7.9A.84-acl">ACL 概述</a> 文档中对象的预设 ACL 部分，例如 default，private，public-read 等，默认为 default
     * @param cosacl COS 访问权限
     */
    public void setXCOSACL(COSACL cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl.getAcl());
        }
    }

    /**
     * 同{@link #setXCOSACL(COSACL)}
     * @param cosacl COS 访问权限
     */
    public void setXCOSACL(String cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL,cosacl);
        }
    }

    /**
     * <p>
     * 赋予被授权者读权限
     * </p>
     * @param aclAccount 读权限用户列表 {@link ACLAccount}
     */
    public void setXCOSGrantRead(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_READ, aclAccount.getAccount());
        }
    }


    /**
     * 赋予被授权者操作对象的读取权限
     * @param aclAccount ACL授权账号列表
     */
    public void setXCOSGrantWrite(ACLAccount aclAccount){

        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_WRITE, aclAccount.getAccount());
        }
    }

    /**
     * 赋予被授权者操作对象的写入权限
     * @param aclAccount ACL授权账号列表
     */
    public void setXCOSReadWrite(ACLAccount aclAccount){

        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_FULL_CONTROL, aclAccount.getAccount());
        }
    }

    /**
     * 设置对象的存储类型。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/33417">存储类型</a> 文档，例如 STANDARD_IA，ARCHIVE。默认值：STANDARD
     * @param stroageClass COS存储类型
     */
    public void setStroageClass(COSStorageClass stroageClass)
    {
        addHeader(COSRequestHeaderKey.X_COS_STORAGE_CLASS_, stroageClass.getStorageClass());
    }

    /**
     * 设置启动盲水印参数
     * @param operations 盲水印参数
     */
    public void setPicOperations(@NonNull PicOperations operations) {
        addHeader("Pic-Operations", operations.toJsonStr());
    }
}
