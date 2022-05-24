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
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSRequestHeaderKey;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.MetaDataDirective;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.cos.xml.utils.URLEncodeUtils;
import com.tencent.qcloud.core.auth.STSCredentialScope;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 简单复制对象的请求.
 * @see com.tencent.cos.xml.SimpleCosXml#copyObject(CopyObjectRequest)
 * @see com.tencent.cos.xml.SimpleCosXml#copyObjectAsync(CopyObjectRequest, CosXmlResultListener)
 */
public class CopyObjectRequest extends ObjectRequest {
    private CopySourceStruct copySourceStruct;

    public CopyObjectRequest(String bucket, String cosPath, CopySourceStruct copySourceStruct){
        super(bucket, cosPath);
        this.copySourceStruct = copySourceStruct;
    }

    @Override
    public String getMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.bytes(null, new byte[0]);
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        if(copySourceStruct == null){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "copy source must not be null");
        }else {
            copySourceStruct.checkParameters();
        }
    }

    /**
     * 设置复制的目标cos路径
     * @param cosPath 复制的目标cos路径
     */
    public void setCosPath(String cosPath){
        this.cosPath = cosPath;
    }

    /**
     * 获取复制的目标cos路径
     * @return 复制的目标cos路径
     */
    public String getCosPath() {
        return cosPath;
    }

    @Override
    public STSCredentialScope[] getSTSCredentialScope(CosXmlServiceConfig config) {
        STSCredentialScope scope1 = new STSCredentialScope("name/cos:PutObject", config.getBucket(bucket),
                config.getRegion(), getPath(config));
        STSCredentialScope scope2 = new STSCredentialScope("name/cos:GetObject", copySourceStruct.bucket,
                copySourceStruct.region, copySourceStruct.cosPath);
        return STSCredentialScope.toArray(scope1, scope2);
    }

    /**
     * 设置复制源对象信息
     * @param copySource 源对象结构体
     * @param config 服务配置信息，用于获取源对象的 URL
     * @throws CosXmlClientException 客户端异常
     */
    public void setCopySource(CopySourceStruct copySource, CosXmlServiceConfig config) throws CosXmlClientException {
        this.copySourceStruct = copySource;
        if(copySourceStruct != null){
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE, copySourceStruct.getSource(config));
        }
    }

//    @Override
//    public String getHost(CosXmlServiceConfig config, boolean isSupportAccelerate, boolean isHeader) throws CosXmlClientException {
//        String host =  super.getHost(config, isSupportAccelerate, isHeader);
//        // setCopySource(copySourceStruct, config);
//        return host;
//    }

//        @Override
//    public Map<String, List<String>> getRequestHeaders() {
//        if(copySourceStruct != null){
//            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE, copySourceStruct.getSource(domainSuffix));
//        }
//        return super.getRequestHeaders();
//    }

    /**
     * 获取源对象结构体
     * @return 源对象结构体
     */
    public CopySourceStruct getCopySource(){
        return copySourceStruct;
    }

    /**
     * 是否复制源对象的元数据信息，枚举值：Copy，Replaced，默认为 Copy：
     * 如果标记为 Copy，则复制源对象的元数据信息
     * 如果标记为 Replaced，则按本次请求的请求头中的元数据信息作为目标对象的元数据信息
     * 当目标对象和源对象为同一对象时，即用户试图修改元数据时，则标记必须为 Replaced
     * @param metaDataDirective 是否拷贝元数据
     */
    public void setCopyMetaDataDirective(MetaDataDirective metaDataDirective){
        if(metaDataDirective != null){
            addHeader(COSRequestHeaderKey.X_COS_METADATA_DIRECTIVE, metaDataDirective.getMetaDirective());
        }
    }

    /**
     * 当对象在指定时间后被修改，则执行复制操作，否则返回 HTTP 状态码为412（Precondition Failed）
     * @param sourceIfModifiedSince 指定的时间
     */
    public void setCopyIfModifiedSince(String sourceIfModifiedSince){
        if(sourceIfModifiedSince != null){
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_IF_MODIFIED_SINCE, sourceIfModifiedSince);
        }
    }

    /**
     * 当对象在指定时间后未被修改，则执行复制操作，否则返回 HTTP 状态码为412（Precondition Failed）
     * @param sourceIfUnmodifiedSince 指定的时间
     */
    public void setCopyIfUnmodifiedSince(String sourceIfUnmodifiedSince){
        if(sourceIfUnmodifiedSince != null){
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_IF_UNMODIFIED_SINCE, sourceIfUnmodifiedSince);
        }
    }

    /**
     * 当对象的 ETag 与指定的值一致，则执行复制操作，否则返回 HTTP 状态码为412（Precondition Failed）
     * @param eTag 指定eTag
     */
    public void setCopyIfMatch(String eTag){
        if(eTag != null){
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_IF_MATCH, eTag);
        }
    }

    /**
     * 当对象的 ETag 与指定的值不一致，则执行复制操作，否则返回 HTTP 状态码为412（Precondition Failed）
     * @param eTag 指定eTag
     */
    public void setCopyIfNoneMatch(String eTag){
        if(eTag != null){
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_IF_NONE_MATCH, eTag);
        }
    }

    /**
     * 源对象SSE-C服务端加密配置
     * @param sourceKey 服务端加密密钥
     * @throws CosXmlClientException 客户端异常
     */
    public void setCopySourceServerSideEncryptionCustomerKey(String sourceKey) throws CosXmlClientException {
        if(sourceKey != null){
            addHeader("x-cos-copy-source-server-side-encryption-customer-algorithm", "AES256");
            addHeader("x-cos-copy-source-server-side-encryption-customer-key", DigestUtils.getBase64(sourceKey));
            String base64ForKeyMd5;
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                base64ForKeyMd5 = Base64.encodeToString(messageDigest.digest(sourceKey.getBytes( Charset.forName("UTF-8"))), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
            }
            addHeader("x-cos-copy-source-server-side-encryption-customer-key-MD5", base64ForKeyMd5);
        }
    }

    /**
     * 源对象SSE-KMS服务端加密配置
     * @param customerKeyID 用于指定 KMS 的用户主密钥 CMK，如不指定，则使用 COS 默认创建的 CMK
     * @param json 用于指定加密上下文，值为 JSON 格式加密上下文键值对的 Base64 编码
     * @throws CosXmlClientException 客户端代理
     */
    public void setCopySourceServerSideEncryptionKMS(String customerKeyID, String json) throws CosXmlClientException {
        addHeader("'x-cos-copy-source-server-side-encryption", "cos/kms");
        if(customerKeyID != null){
            addHeader("x-cos-copy-source-server-side-encryption-cos-kms-key-id", customerKeyID);
        }
        if(json != null){
            addHeader("x-cos-copy-source-server-side-encryption-context", DigestUtils.getBase64(json));
        }
    }

    /**
     * 目标对象的存储类型。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/33417">存储类型</a> 文档，例如 STANDARD_IA，ARCHIVE。默认值：STANDARD
     * @param cosStorageClass COS存储类型
     * @throws CosXmlClientException 客户端异常
     */
    public void setCosStorageClass(COSStorageClass cosStorageClass) {
        if(cosStorageClass != null){
            addHeader(COSRequestHeaderKey.X_COS_STORAGE_CLASS_, cosStorageClass.getStorageClass());
        }
    }

    /**
     * 定义目标对象的访问控制列表（ACL）属性。
     * 枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/30752#.E9.A2.84.E8.AE.BE.E7.9A.84-acl">ACL 概述</a> 文档中对象的预设 ACL 部分，例如 default，private，public-read 等，默认为 default
     * @param cosacl COS 访问权限
     */
    public void setXCOSACL(COSACL cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL, cosacl.getAcl());
        }
    }

    /**
     * 同{@link #setXCOSACL(COSACL)}
     * @param cosacl COS 访问权限
     */
    public void setXCOSACL(String cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL, cosacl);
        }
    }

    /**
     * 赋予被授权者操作目标对象的读取权限
     * @param aclAccount ACL授权账号列表
     */
    public void setXCOSGrantRead(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_READ, aclAccount.getAccount());
        }
    }

    /**
     * 赋予被授权者操作目标对象的写入权限
     * @param aclAccount ACL授权账号列表
     */
    public void setXCOSGrantWrite(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_WRITE, aclAccount.getAccount());
        }
    }

    /**
     * 赋予被授权者操作目标对象的所有权限
     * @param aclAccount ACL授权账号列表
     */
    public void setXCOSReadWrite(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_FULL_CONTROL, aclAccount.getAccount());
        }
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
     * 复制源结构体
     */
    public static class CopySourceStruct{

        public String bucket;
        public String region;
        public String cosPath;
        public String versionId;

        public CopySourceStruct(String bucket, String region, String cosPath){
            this.bucket = bucket;
            this.region = region;
            this.cosPath = cosPath;
        }

        @Deprecated
        public CopySourceStruct(String appid, String bucket, String region, String cosPath){
            this.bucket = bucket.concat("-").concat(appid);
            this.region = region;
            this.cosPath = cosPath;
        }

        public CopySourceStruct(String appid, String bucket, String region, String cosPath, String versionId){
            this(appid, bucket, region, cosPath);
            this.versionId = versionId;
        }

        /**
         * 检查参数
         * @throws CosXmlClientException 客户端异常
         */
        public void checkParameters() throws CosXmlClientException {
            if(bucket == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "copy source bucket must not be null");
            }
            if(cosPath == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "copy source cosPath must not be null");
            }
//            if(appid == null){
//                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "copy source appid must not be null");
//            }
            if(region == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "copy source region must not be null");
            }
            cosPath = URLEncodeUtils.cosPathEncode(cosPath);
        }

        /**
         * 根据服务配置信息得到源url
         * @param config 服务配置信息
         * @return 源url
         */
        public String getSource(CosXmlServiceConfig config) {
            if(cosPath != null){
                if(!cosPath.startsWith("/")){
                    cosPath = "/" + cosPath;
                }
            }
            String host = config.getDefaultRequestHost(region, bucket);
            String url = host + cosPath;
            if(versionId != null){
                url += "?versionId=" + versionId;
            }
            return url;
        }
    }
}
