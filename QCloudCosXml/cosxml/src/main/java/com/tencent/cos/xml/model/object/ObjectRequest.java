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
 * Created by bradyxiao on 2017/11/30.
 */

public abstract class ObjectRequest extends CosXmlRequest {

    protected String cosPath;

    public ObjectRequest(String bucket, String cosPath){
        this.bucket = bucket;
        this.cosPath = cosPath;
    }

    public void setCosPath(String cosPath){
        this.cosPath = cosPath;
    }

    /**
     * 支持 CSP 的 Bucket 可能在 path 上
     *
     * @param config
     * @return
     */
    @Override
    public String getPath(CosXmlServiceConfig config) {
        return config.getUrlPath(bucket, cosPath);
    }

    public String getCosPath() {
        return cosPath;
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
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

    public void setCOSServerSideEncryption(){
        this.addHeader("x-cos-server-side-encryption", "AES256");
    }

    /**
     * customerKey must be "[a-z][A-Z][0-9]", and its length = 32
     * @param customerKey
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
