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

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.Range;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.utils.FileUtils;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CryptoModuleAE extends CryptoModuleBase {

    public CryptoModuleAE(CosXmlSimpleService cos, QCloudCredentialProvider credentialsProvider,
                          EncryptionMaterialsProvider kekMaterialsProvider) {
        this(null, cos, credentialsProvider, kekMaterialsProvider);
    }

    public CryptoModuleAE(QCLOUDKMS kms, CosXmlSimpleService cos,
                          QCloudCredentialProvider credentialsProvider,
            EncryptionMaterialsProvider kekMaterialsProvider) {
        super(kms, cos, credentialsProvider, kekMaterialsProvider);
    }

    /**
     * Returns true if a strict encryption mode is in use in the current crypto module; false
     * otherwise.
     */
    protected boolean isStrict() {
        return false;
    }


    @Override
    public GetObjectResult getObjectSecurely(GetObjectRequest req) throws CosXmlClientException, CosXmlServiceException {
        GetObjectResult getObjectResult = cos.getObject(req);

        ObjectMetadata metadata = new ObjectMetadata(getObjectResult.headers);

        Range range = req.getRange();

        try {
            ContentCryptoMaterial cekMaterial =
                    ContentCryptoMaterial.fromObjectMetadata(metadata,
                            kekMaterialsProvider, null,
                            // range is sometimes necessary to compute the adjusted IV
                            range == null ? null : new long[] {range.getStart(), range.getEnd()},
                            false, kms);
            CipherLite cipherLite = cekMaterial.getCipherLite();
            InputStream inputStream = new CipherLiteInputStream(new FileInputStream(req.getDownloadPath()),
                    cipherLite);

            String encryptedFilePath = req.getDownloadPath();
            String decryptedFilePath = encryptedFilePath + ".decrypt";
            File decryptedFile = new File(decryptedFilePath);
            FileUtils.saveInputStreamToTmpFile(inputStream, decryptedFile, 0, -1);
            FileUtils.deleteFileIfExist(encryptedFilePath);
            if (!decryptedFile.renameTo(new File(encryptedFilePath))) {
                throw CosXmlClientException.internalException("decrypt file failed.");
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            throw CosXmlClientException.internalException(e.getMessage());
        }

        return getObjectResult;
    }


    @Override
    final MultipartUploadCryptoContext newUploadContext(InitMultipartUploadRequest req,
                                                        ContentCryptoMaterial cekMaterial) {
        return new MultipartUploadCryptoContext(req.getBucket(), req.getCosPath(), cekMaterial);
    }

    //// specific overrides for uploading parts.
    @Override
    final CipherLite cipherLiteForNextPart(MultipartUploadCryptoContext uploadContext) {
        return uploadContext.getCipherLite();
    }

    @Override
    final long computeLastPartSize(UploadPartRequest req) {
        return req.getFileLength() + (contentCryptoScheme.getTagLengthInBits() / 8);
    }



    /**
     * Asserts that the specified parameter value is not null and if it is, throws an
     * IllegalArgumentException with the specified error message.
     *
     * @param parameterValue The parameter value being checked.
     * @param errorMessage The error message to include in the IllegalArgumentException if the
     *        specified parameter is null.
     */
    private void assertParameterNotNull(Object parameterValue, String errorMessage) {
        if (parameterValue == null)
            throw new IllegalArgumentException(errorMessage);
    }

    @Override
    protected final long ciphertextLength(long originalContentLength) {
        // Add 16 bytes for the 128-bit tag length using AES/GCM
        return originalContentLength + contentCryptoScheme.getTagLengthInBits() / 8;
    }

}
