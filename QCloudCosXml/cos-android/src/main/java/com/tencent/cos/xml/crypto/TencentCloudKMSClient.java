/*
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 
 * According to cos feature, we modify some class，comment, field name, etc.
 */

package com.tencent.cos.xml.crypto;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.auth.BasicQCloudCredentials;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudCredentials;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.kms.v20190118.KmsClient;
import com.tencentcloudapi.kms.v20190118.models.DecryptRequest;
import com.tencentcloudapi.kms.v20190118.models.DecryptResponse;
import com.tencentcloudapi.kms.v20190118.models.EncryptRequest;
import com.tencentcloudapi.kms.v20190118.models.EncryptResponse;
import com.tencentcloudapi.kms.v20190118.models.GenerateDataKeyRequest;
import com.tencentcloudapi.kms.v20190118.models.GenerateDataKeyResponse;

import java.util.Locale;

/**
 * Client for accessing TencentCloud KMS.
 */
public class TencentCloudKMSClient implements QCLOUDKMS {
    private final KmsClient kmsClient;

    private QCloudCredentialProvider credentialProvider;

    public TencentCloudKMSClient(String region, QCloudCredentialProvider credentialProvider) {
        this.kmsClient = new KmsClient(null, region);
        this.credentialProvider = credentialProvider;
    }

    public void assetCredentials() throws CosXmlClientException {

        Credential kmsCredential = null;
        QCloudCredentials credentials = null;
        try {
            credentials = credentialProvider.getCredentials();
            if (credentials instanceof SessionQCloudCredentials) {
                SessionQCloudCredentials sessionQCloudCredentials = (SessionQCloudCredentials) credentials;
                String secretId = sessionQCloudCredentials.getSecretId();
                String secretKey = sessionQCloudCredentials.getSecretKey();
                String token = sessionQCloudCredentials.getToken();
                kmsCredential = new Credential(secretId, secretKey, token);
            } else if (credentials instanceof BasicQCloudCredentials) {
                BasicQCloudCredentials basicQCloudCredentials = (BasicQCloudCredentials) credentials;
                String secretId = basicQCloudCredentials.getSecretId();
                String secretKey = basicQCloudCredentials.getSecretKey();
                kmsCredential = new Credential(secretId, secretKey);
            } else {
                throw CosXmlClientException.internalException("credentials is neither SessionQCloudCredentials nor BasicQCloudCredentials ");
            }
            this.kmsClient.setCredential(kmsCredential);
        } catch (QCloudClientException e) {
            throw CosXmlClientException.internalException(e.getMessage());
        }
    }

    /**
     * Generates a unique symmetric data key for client-side encryption. This operation returns a plaintext copy of the
     * data key and a copy that is encrypted under a customer master key (CMK) that you specify. You can use the
     * plaintext key to encrypt your data outside of KMS and store the encrypted data key with the encrypted data.
     * 
     * @param generateDataKeyRequest GenerateDataKeyRequest
     * @return GenerateDataKeyResponse
     * @throws CosXmlClientException
     */
    @Override
    public GenerateDataKeyResponse generateDataKey(GenerateDataKeyRequest generateDataKeyRequest) throws CosXmlClientException {
        try {
            assetCredentials();
            GenerateDataKeyResponse generateDataKeyRes = this.kmsClient.GenerateDataKey(generateDataKeyRequest);
            return generateDataKeyRes;
        } catch (TencentCloudSDKException e) {
            throw getClientException(e, "TencentCloudKMS Service got exception while GenerateDataKey");
        }
    }


    /**
    * Encrypts plaintext into ciphertext by using a customer master key (CMK).
    *
    * @param encryptRequest EncryptRequest
    * @return EncryptResponse
    * @throws TencentCloudSDKException
    */
    @Override
    public EncryptResponse encrypt(EncryptRequest encryptRequest) throws CosXmlClientException {
        try {
            assetCredentials();
            EncryptResponse encryptResponse = this.kmsClient.Encrypt(encryptRequest);
            return encryptResponse;
        } catch (TencentCloudSDKException e) {
            throw getClientException(e, "TencentCloudKMS Service got exception while Encrypt");
        }
    }

    /**
    * Decrypts ciphertext that was encrypted by a KMS customer master key (CMK) using any of the following
    * operations:
    *
    * generateDataKey
    *
    * @param decryptRequest DecryptRequest
    * @return DecrypResponse
    * @throws CosXmlClientException
    */
    public DecryptResponse decrypt(DecryptRequest decryptRequest) throws CosXmlClientException {
        try{
            assetCredentials();
            DecryptResponse decryptResponse = this.kmsClient.Decrypt(decryptRequest);
            return decryptResponse;
        } catch (TencentCloudSDKException e) {
            throw getClientException(e, "TencentCloudKMS Service got exception while Decrypt");
        }
    }

    private CosXmlClientException getClientException(TencentCloudSDKException e, String errorMessage) {

        String errorCode = e.getErrorCode();
        String requestId = e.getRequestId();
        String message = String.format(Locale.ENGLISH, "%s: %s, error code: %s, requestId: %s", errorMessage,
                e.getMessage(), errorCode, requestId);

        return new CosXmlClientException(ClientErrorCode.KMS_ERROR.getCode(), message);
    }

    @Override
    public void shutdown() {
    }
}
