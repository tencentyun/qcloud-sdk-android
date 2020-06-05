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
import com.tencent.cos.xml.model.tag.ACLAccount;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.cos.xml.utils.URLEncodeUtils;
import com.tencent.qcloud.core.auth.STSCredentialScope;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by bradyxiao on 2017/9/20.
 * author bradyxiao
 *
 * Copy Object request is used to copy a file from source path to the destination path.
 * The recommended file size is 1M-5G. For any file above 5G, please use multipart upload (Upload - Copy).
 * In the process of copying, file meta-attributes and ACLs can be modified.
 * Users can use this API to move or rename a file, modify file attributes and create a copy.
 *
 */
public class CopyObjectRequest extends ObjectRequest {

    // copy source struct
    private CopySourceStruct copySourceStruct;

    public CopyObjectRequest(String bucket, String cosPath, CopySourceStruct copySourceStruct){
        super(bucket, cosPath);
        this.copySourceStruct = copySourceStruct;
        //setCopySource(copySourceStruct);
    }

    public CopyObjectRequest(){
        super(null, null);
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
     * set cosPath for Abort Multi upload.
     * @param cosPath
     */
    public void setCosPath(String cosPath){
        this.cosPath = cosPath;
    }

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
     * Indicate absolute path of source file URL from CopySourceStruct.
     * You can specify the history version with the versionid sub-resource
     * @param copySource
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

    public CopySourceStruct getCopySource(){
        return copySourceStruct;
    }
    /**
     * Indicate whether to copy metadata.
     * Enumerated values: Copy, Replaced. The default is Copy.
     * If it is marked as Copy, the copying action will be performed directly,
     * with the user metadata in the Header ignored ; if it is marked as Replaced,
     * the metadata will be modified based on the Header information.
     * If the destination path and the source path are the same, that is,
     * the user attempts to modify the metadata, the value must be Replaced
     * @param metaDataDirective
     */
    public void setCopyMetaDataDirective(MetaDataDirective metaDataDirective){
        if(metaDataDirective != null){
            addHeader(COSRequestHeaderKey.X_COS_METADATA_DIRECTIVE, metaDataDirective.getMetaDirective());
        }
    }

    /**
     * The action is performed if the Object has been modified since the specified time,
     * otherwise error code 412 will be returned.
     * It can be used with x-cos-copy-source-If-None-Match.
     * Using it with other conditions can cause a conflict.
     * @param sourceIfModifiedSince
     */
    public void setCopyIfModifiedSince(String sourceIfModifiedSince){
        if(sourceIfModifiedSince != null){
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_IF_MODIFIED_SINCE, sourceIfModifiedSince);
        }
    }

    /**
     * The action is performed if the Object has not been modified since the specified time,
     * otherwise error code 412 will be returned.
     * It can be used with x-cos-copy-source-If-Match.
     * it with other conditions can cause a conflict.
     * @param sourceIfUnmodifiedSince
     */
    public void setCopyIfUnmodifiedSince(String sourceIfUnmodifiedSince){
        if(sourceIfUnmodifiedSince != null){
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_IF_UNMODIFIED_SINCE, sourceIfUnmodifiedSince);
        }
    }

    /**
     * The action is performed if the Etag of Object is the same as the given one,
     * otherwise error code 412 will be returned.
     * It can be used with x-cos-copy-source-If-Unmodified-Since.
     * Using it with other conditions can cause a conflict.
     * @param eTag
     */
    public void setCopyIfMatch(String eTag){
        if(eTag != null){
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_IF_MATCH, eTag);
        }
    }

    /**
     * The action is performed if the Etag of Object is different from the given one,
     * otherwise error code 412 will be returned.
     * It can be used with x-cos-copy-source-If-Modified-Since.
     * Using it with other conditions can cause a conflict.
     * @param eTag
     */
    public void setCopyIfNoneMatch(String eTag){
        if(eTag != null){
            addHeader(COSRequestHeaderKey.X_COS_COPY_SOURCE_IF_NONE_MATCH, eTag);
        }
    }

    /**
     *  copy encryption object
     * @param sourceKey
     * @throws CosXmlClientException
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
     * copy encryption object
     * @param customerKeyID
     * @param json
     * @throws CosXmlClientException
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
     * storage class. Enumerated values: Standard, Standard_IA, Nearline; the default is Standard
     * @param cosStorageClass
     */
    public void setCosStorageClass(COSStorageClass cosStorageClass) throws CosXmlClientException {
        if(cosStorageClass != null){
            if(cosStorageClass.getStorageClass().equals(COSStorageClass.ARCHIVE)) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(),
                    "copy nonsupport archive");
            addHeader(COSRequestHeaderKey.X_COS_STORAGE_CLASS_, cosStorageClass.getStorageClass());
        }
    }

    /**
     * Allow users to define file permissions.
     * valid values: private, public-read. The default is private.
     */
    public void setXCOSACL(COSACL cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL, cosacl.getAcl());
        }
    }

    public void setXCOSACL(String cosacl){
        if(cosacl != null){
            addHeader(COSRequestHeaderKey.X_COS_ACL, cosacl);
        }
    }

    /**
     * Grant read permission to the authorized user
     * @param aclAccount
     */
    public void setXCOSGrantRead(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_READ, aclAccount.getAccount());
        }
    }

    /**
     * Grant write permission to the authorized user
     * @param aclAccount
     */
    public void setXCOSGrantWrite(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_WRITE, aclAccount.getAccount());
        }
    }

    /**
     * Grant read and write permissions to the authorized user
     * @param aclAccount
     */
    public void setXCOSReadWrite(ACLAccount aclAccount){
        if (aclAccount != null) {
            addHeader(COSRequestHeaderKey.X_COS_GRANT_FULL_CONTROL, aclAccount.getAccount());
        }
    }

    /**
     * custom file requestHeaders
     * @param key
     * @param value
     */
    public void setXCOSMeta(String key, String value){
        if(key != null && value != null){
            addHeader(key,value);
        }
    }

    public static class CopySourceStruct{
        public String appid;
        public String bucket;
        public String region;
        public String cosPath;
        public String versionId;

        public CopySourceStruct(String appid, String bucket, String region, String cosPath){
            this.appid = appid;
            this.bucket = bucket;
            this.region = region;
            this.cosPath = cosPath;
        }

        public CopySourceStruct(String appid, String bucket, String region, String cosPath, String versionId){
            this(appid, bucket, region, cosPath);
            this.versionId = versionId;
        }

        public void checkParameters() throws CosXmlClientException {
            if(bucket == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "copy source bucket must not be null");
            }
            if(cosPath == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "copy source cosPath must not be null");
            }
            if(appid == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "copy source appid must not be null");
            }
            if(region == null){
                throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "copy source region must not be null");
            }
            cosPath = URLEncodeUtils.cosPathEncode(cosPath);
        }

        public String getSource(CosXmlServiceConfig config) throws CosXmlClientException {
            if(cosPath != null){
                if(!cosPath.startsWith("/")){
                    cosPath = "/" + cosPath;
                }
            }
            String host = config.getDefaultRequestHost(region, bucket, appid);
            String url = host + cosPath;
            if(versionId != null){
                url += "?versionId=" + versionId;
            }
            return url;
        }
    }
}
