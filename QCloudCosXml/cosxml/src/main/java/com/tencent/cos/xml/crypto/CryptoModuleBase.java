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

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import static com.tencent.cos.xml.crypto.LengthCheckInputStream.EXCLUDE_SKIPPED_BYTES;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.Range;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;
import com.tencent.cos.xml.s3.Base64;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.util.ContextHolder;
import com.tencent.qcloud.core.util.QCloudUtils;
import com.tencentcloudapi.kms.v20190118.models.GenerateDataKeyRequest;
import com.tencentcloudapi.kms.v20190118.models.GenerateDataKeyResponse;

import org.json.JSONException;


/**
 * Common implementation for different COS cryptographic modules.
 */
public abstract class CryptoModuleBase implements CryptoModule {
    private static final boolean IS_MULTI_PART = true;
    protected static final int DEFAULT_BUFFER_SIZE = 1024 * 2; // 2K
    protected final EncryptionMaterialsProvider kekMaterialsProvider;
    protected final COSCryptoScheme cryptoScheme;
    protected final ContentCryptoScheme contentCryptoScheme;
    /** Map of data about in progress encrypted multipart uploads. */
    protected final Map<String, MultipartUploadCryptoContext> multipartUploadContexts =
            Collections.synchronizedMap(new HashMap<String, MultipartUploadCryptoContext>());
    protected final CosXmlSimpleService cos;
    protected final QCLOUDKMS kms;


    protected CryptoModuleBase(QCLOUDKMS kms, CosXmlSimpleService cos,
                               QCloudCredentialProvider credentialsProvider,
                               EncryptionMaterialsProvider kekMaterialsProvider) {
        
        this.kekMaterialsProvider = kekMaterialsProvider;
        this.cos = cos;
        this.cryptoScheme = COSCryptoScheme.from();
        this.contentCryptoScheme = cryptoScheme.getContentCryptoScheme();
        this.kms = kms;
    }

    /**
     * For testing purposes only.
     */
    protected CryptoModuleBase(CosXmlSimpleService cos, QCloudCredentialProvider credentialsProvider,
                               EncryptionMaterialsProvider kekMaterialsProvider) {
        this.kekMaterialsProvider = kekMaterialsProvider;
        this.cos = cos;
        this.cryptoScheme = COSCryptoScheme.from();
        this.contentCryptoScheme = cryptoScheme.getContentCryptoScheme();
        this.kms = null;
    }

    /**
     * Returns the length of the ciphertext computed from the length of the plaintext.
     *
     * @param plaintextLength a non-negative number
     * @return a non-negative number
     */
    protected abstract long ciphertextLength(long plaintextLength);

    //////////////////////// Common Implementation ////////////////////////
    @Override
    public PutObjectResult putObjectSecurely(PutObjectRequest req) throws CosXmlClientException, CosXmlServiceException {
        //
        ContentCryptoMaterial cekMaterial = createContentCryptoMaterial(req);
        // Wraps the object data with a cipher input stream
        PutObjectRequest wrappedReq = wrapWithCipher(req, cekMaterial);
        // Update the metadata
        req.setMetadata(updateMetadataWithContentCryptoMaterial(req.getMetadata(), cekMaterial));
        // Put the encrypted object into COS
        return cos.putObject(wrappedReq);
    }


    abstract MultipartUploadCryptoContext newUploadContext(InitMultipartUploadRequest req,
                                                           ContentCryptoMaterial cekMaterial);

    @Override
    public InitMultipartUploadResult initMultipartUploadSecurely(InitMultipartUploadRequest request)
            throws CosXmlClientException, CosXmlServiceException {
        // Generate a one-time use symmetric key and initialize a cipher to
        // encrypt object data
        ContentCryptoMaterial cekMaterial = createContentCryptoMaterial(request);

        ObjectMetadata metadata = request.getMetadata();
        if (metadata == null)
            metadata = new ObjectMetadata();
        // Store encryption info in metadata
        request.setMetadata(
                updateMetadataWithContentCryptoMaterial(metadata, cekMaterial));

        InitMultipartUploadResult result = cos.initMultipartUpload(request);
        MultipartUploadCryptoContext uploadContext = newUploadContext(request, cekMaterial);
        multipartUploadContexts.put(result.initMultipartUpload.uploadId, uploadContext);
        return result;
    }

    public boolean hasMultipartUploadContext(String uploadId) {
        return multipartUploadContexts.containsKey(uploadId);
    }

    public MultipartUploadCryptoContext getCryptoContext(String uploadId) {
        return multipartUploadContexts.get(uploadId);
    }

    //// specific crypto module behavior for uploading parts.
    abstract CipherLite cipherLiteForNextPart(MultipartUploadCryptoContext uploadContext);

    abstract long computeLastPartSize(UploadPartRequest req);

    /**
     * {@inheritDoc}
     *
     * <p>
     * <b>NOTE:</b> Because the encryption process requires context from previous blocks, parts
     * uploaded with the COSEncryptionClient (as opposed to the normal COSClient) must be
     * uploaded serially, and in order. Otherwise, the previous encryption context isn't available
     * to use when encrypting the current part.
     */
    @Override
    public UploadPartResult uploadPartSecurely(UploadPartRequest req) throws CosXmlClientException, CosXmlServiceException {
        final int blockSize = contentCryptoScheme.getBlockSizeInBytes();
        final boolean isLastPart = req.isLastPart();
        final String uploadId = req.getUploadId();
        final long partSize = req.getFileLength();
        final boolean partSizeMultipleOfCipherBlockSize = 0 == (partSize % blockSize);
        if (!isLastPart && !partSizeMultipleOfCipherBlockSize) {
            throw CosXmlClientException.internalException(
                    "Invalid part size: part sizes for encrypted multipart uploads must be multiples "
                            + "of the cipher block size (" + blockSize
                            + ") with the exception of the last part.");
        }
        final MultipartUploadCryptoContext uploadContext = multipartUploadContexts.get(uploadId);
        if (uploadContext == null) {
            throw CosXmlClientException.internalException(
                    "No client-side information available on upload ID " + uploadId);
        }
        final UploadPartResult result;
        // Checks the parts are uploaded in series
        uploadContext.beginPartUpload(req.getPartNumber());
        CipherLite cipherLite = cipherLiteForNextPart(uploadContext);
        SdkFilterInputStream isCurr = null;
        try {
            CipherLiteInputStream clis = newMultipartCOSCipherInputStream(req, cipherLite);
            isCurr = clis; // so the clis will be closed (in the finally block below) upon
            // unexpected failure should we opened a file undereath
            req.setInputStream(isCurr);
            // Treat all encryption requests as input stream upload requests,
            // not as file upload requests.
            req.setSrcPath(null);
            req.setFileOffset(0);
            // The last part of the multipart upload will contain an extra
            // 16-byte mac
            if (isLastPart) {
                // We only change the size of the last part
                long lastPartSize = computeLastPartSize(req);
                if (lastPartSize > -1)
                    req.setFileContentLength(lastPartSize);
                if (uploadContext.hasFinalPartBeenSeen()) {
                    throw CosXmlClientException.internalException(
                            "This part was specified as the last part in a multipart upload, but a previous part was already marked as the last part.  "
                                    + "Only the last part of the upload should be marked as the last part.");
                }
            }

            result = cos.uploadPart(req);
        } finally {
            uploadContext.endPartUpload();
        }
        if (isLastPart)
            uploadContext.setHasFinalPartBeenSeen(true);
        return result;
    }

    public final CipherLiteInputStream newMultipartCOSCipherInputStream(UploadPartRequest req,
            CipherLite cipherLite) throws CosXmlClientException {
        final String fileOrig = req.getSrcPath();
        final Uri uri = req.getUri();
        InputStream isCurr = null;
        try {

            // isCurr = new ResettableInputStream(fileOrig);
            if (!TextUtils.isEmpty(fileOrig)) {
                isCurr = new ResettableInputStream(fileOrig);
            } else if (uri != null && ContextHolder.getAppContext() != null) {
                isCurr = ContextHolder.getAppContext().getContentResolver().openInputStream(uri);
            }

            isCurr = new InputSubstream(isCurr, req.getFileOffset(), req.getFileContentLength(),
                    req.isLastPart());
            return cipherLite.markSupported()
                    ? new CipherLiteInputStream(isCurr, cipherLite, DEFAULT_BUFFER_SIZE,
                            IS_MULTI_PART, req.isLastPart())
                    : new RenewableCipherLiteInputStream(isCurr, cipherLite, DEFAULT_BUFFER_SIZE,
                            IS_MULTI_PART, req.isLastPart());
        } catch (Exception e) {
            throw CosXmlClientException.internalException("Unable to create cipher input stream");
        }
    }


    @Override
    public CompleteMultiUploadResult completeMultipartUploadSecurely(
            CompleteMultiUploadRequest req) throws CosXmlClientException, CosXmlServiceException {
        String uploadId = req.getUploadId();
        final MultipartUploadCryptoContext uploadContext = multipartUploadContexts.get(uploadId);

        if (uploadContext != null && !uploadContext.hasFinalPartBeenSeen()) {
            throw CosXmlClientException.internalException(
                    "Unable to complete an encrypted multipart upload without being told which part was the last.  "
                            + "Without knowing which part was the last, the encrypted data in COS is incomplete and corrupt.");
        }
        CompleteMultiUploadResult result = cos.completeMultiUpload(req);
        multipartUploadContexts.remove(uploadId);
        return result;
    }

    protected final ObjectMetadata updateMetadataWithContentCryptoMaterial(ObjectMetadata metadata, ContentCryptoMaterial instruction)
        throws CosXmlClientException {
        if (metadata == null)
            metadata = new ObjectMetadata();
        try {
            return instruction.toObjectMetadata(metadata);
        } catch (JSONException e) {
            throw CosXmlClientException.internalException(e.getMessage());
        }
    }

    /**
     * Creates and returns a non-null content crypto material for the given request.
     *
     * @throws CosXmlClientException if no encryption material can be found.
     */
    protected final ContentCryptoMaterial createContentCryptoMaterial(CosXmlRequest req) throws CosXmlClientException {
        return newContentCryptoMaterial(this.kekMaterialsProvider, null,
                req);
    }

    /**
     * Returns the content encryption material generated with the given kek material, material
     * description and security providers; or null if the encryption material cannot be found for
     * the specified description.
     */
    private ContentCryptoMaterial newContentCryptoMaterial(
            EncryptionMaterialsProvider kekMaterialProvider,
            Map<String, String> materialsDescription, Provider provider, CosXmlRequest req) throws CosXmlClientException {
        EncryptionMaterials kekMaterials =
                kekMaterialProvider.getEncryptionMaterials(materialsDescription);
        if (kekMaterials == null) {
            return null;
        }
        return buildContentCryptoMaterial(kekMaterials, provider, req);
    }

    /**
     * Returns a non-null content encryption material generated with the given kek material and
     * security providers.
     *
     * @throws CosXmlClientException if no encryption material can be found from the given encryption
     *         material provider.
     */
    private ContentCryptoMaterial newContentCryptoMaterial(
            EncryptionMaterialsProvider kekMaterialProvider, Provider provider,
            CosXmlRequest req) throws CosXmlClientException {
        EncryptionMaterials kekMaterials = kekMaterialProvider.getEncryptionMaterials();
        if (kekMaterials == null)
            throw CosXmlClientException.internalException(
                    "No material available from the encryption material provider");
        return buildContentCryptoMaterial(kekMaterials, provider, req);
    }

    /**
     * @param materials a non-null encryption material
     */
    private ContentCryptoMaterial buildContentCryptoMaterial(EncryptionMaterials materials,
            Provider provider, CosXmlRequest req) throws CosXmlClientException {
        // Randomly generate the IV
        final byte[] iv = new byte[contentCryptoScheme.getIVLengthInBytes()];
        cryptoScheme.getSecureRandom().nextBytes(iv);

        if (materials.isKMSEnabled()) {
            final Map<String, String> encryptionContext =
                    ContentCryptoMaterial.mergeMaterialDescriptions(materials, req);

            GenerateDataKeyRequest keyGenReq = new GenerateDataKeyRequest();
            try {
                keyGenReq.setEncryptionContext(JSONUtils.toJsonString(encryptionContext));
            } catch (JSONException e) {
                throw CosXmlClientException.internalException("generate datakey request set encryption context got json processing exception");
            }
            keyGenReq.setKeyId(materials.getCustomerMasterKeyId());
            keyGenReq.setKeySpec(contentCryptoScheme.getKeySpec());

            GenerateDataKeyResponse keyGenRes = kms.generateDataKey(keyGenReq);

            byte[] key = Base64.decode(keyGenRes.getPlaintext());
            final SecretKey cek = new SecretKeySpec(key,
                                  contentCryptoScheme.getKeyGeneratorAlgorithm());

            byte[] keyBlob = keyGenRes.getCiphertextBlob().getBytes();
            return ContentCryptoMaterial.wrap(cek, iv, contentCryptoScheme, provider,
                    new KMSSecuredCEK(keyBlob, encryptionContext));
        } else {
            // Generate a one-time use symmetric key and initialize a cipher to encrypt object data
            return ContentCryptoMaterial.create(generateCEK(materials, provider), iv, materials,
                    cryptoScheme, provider, kms, req);
        }
    }

    /**
     * @param kekMaterials non-null encryption materials
     */
    protected final SecretKey generateCEK(final EncryptionMaterials kekMaterials,
            final Provider providerIn) throws CosXmlClientException {
        final String keygenAlgo = contentCryptoScheme.getKeyGeneratorAlgorithm();
        KeyGenerator generator;
        try {
            generator = providerIn == null ? KeyGenerator.getInstance(keygenAlgo)
                    : KeyGenerator.getInstance(keygenAlgo, providerIn);
            generator.init(contentCryptoScheme.getKeyLengthInBits(),
                    cryptoScheme.getSecureRandom());
            // Set to true if the key encryption involves the use of BC's public key
            boolean involvesBCPublicKey = false;
            KeyPair keypair = kekMaterials.getKeyPair();
            if (keypair != null) {
                String keyWrapAlgo =
                        cryptoScheme.getKeyWrapScheme().getKeyWrapAlgorithm(keypair.getPublic());
                if (keyWrapAlgo == null) {
                    Provider provider = generator.getProvider();
                    String providerName = provider == null ? null : provider.getName();
                    involvesBCPublicKey = "BC".equals(providerName);
                }
            }
            SecretKey secretKey = generator.generateKey();
            if (!involvesBCPublicKey || secretKey.getEncoded()[0] != 0)
                return secretKey;
            for (int retry = 0; retry < 10; retry++) {
                secretKey = generator.generateKey();
                if (secretKey.getEncoded()[0] != 0)
                    return secretKey;
            }
            // The probability of getting here is 2^80, which is impossible in practice.
            throw CosXmlClientException.internalException("Failed to generate secret key");
        } catch (NoSuchAlgorithmException e) {
            throw CosXmlClientException.internalException(
                    "Unable to generate envelope symmetric key:" + e.getMessage());
        }
    }

    private @Nullable InputStream openInputStream(PutObjectRequest putObjectRequest) throws IOException {

        String filePath = putObjectRequest.getSrcPath();
        Uri uri = putObjectRequest.getUri();

        InputStream inputStream = null;
        if (filePath != null) {
            inputStream =  new FileInputStream(filePath);
        } else if (uri != null && ContextHolder.getAppContext() != null) {
            inputStream = ContextHolder.getAppContext().getContentResolver().openInputStream(uri);
        }
        return inputStream;
    }

    /**
     * Returns the given <code>PutObjectRequest</code> but has the content as input stream wrapped
     * with a cipher, and configured with some meta data and user metadata.
     */
    protected final PutObjectRequest wrapWithCipher(final PutObjectRequest request,
            ContentCryptoMaterial cekMaterial) throws CosXmlClientException {
        // Create a new metadata object if there is no metadata already.
        ObjectMetadata metadata = request.getMetadata();
        if (metadata == null) {
            metadata = new ObjectMetadata();
        }

        // Record the original Content MD5, if present, for the unencrypted data
        String md5 = metadata.getContentMD5();
        if (TextUtils.isEmpty(md5)) {
            try {
                InputStream inputStream = openInputStream(request);
                if (inputStream != null) {
                    md5 = DigestUtils.getCOSMd5(inputStream, 0, -1);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if (md5 != null) {
            metadata.addUserMetadata(Headers.UNENCRYPTED_CONTENT_MD5, md5);
        }

        // Removes the original content MD5 if present from the meta data.
        metadata.setContentMD5(null);

        // Record the original, unencrypted content-length so it can be accessed
        // later
        final long plaintextLength = plaintextLength(request, metadata);
        if (plaintextLength >= 0) {
            metadata.addUserMetadata(Headers.UNENCRYPTED_CONTENT_LENGTH,
                    Long.toString(plaintextLength));
            // Put the ciphertext length in the metadata
            metadata.setContentLength(ciphertextLength(plaintextLength));
        }
        request.setMetadata(metadata);
        request.setInputStream(newCOSCipherLiteInputStream(request, cekMaterial, plaintextLength));
        // Treat all encryption requests as input stream upload requests, not as
        // file upload requests.
        request.setSrcPath(null);
        return request;
    }

    private CipherLiteInputStream newCOSCipherLiteInputStream(PutObjectRequest req,
            ContentCryptoMaterial cekMaterial, long plaintextLength) throws CosXmlClientException {
        final String fileOrig = req.getSrcPath();
        InputStream isCurr = null;
        try {

            isCurr = new ResettableInputStream(fileOrig);
            if (plaintextLength > -1) {
                // COS allows a single PUT to be no more than 5GB, which
                // therefore won't exceed the maximum length that can be
                // encrypted either using any cipher such as CBC or GCM.

                // This ensures the plain-text read from the underlying data
                // stream has the same length as the expected total.
                isCurr = new LengthCheckInputStream(isCurr, plaintextLength, EXCLUDE_SKIPPED_BYTES);
            }
            final CipherLite cipherLite = cekMaterial.getCipherLite();

            if (cipherLite.markSupported()) {
                return new CipherLiteInputStream(isCurr, cipherLite, DEFAULT_BUFFER_SIZE);
            } else {
                return new RenewableCipherLiteInputStream(isCurr, cipherLite, DEFAULT_BUFFER_SIZE);
            }
        } catch (Exception e) {
            throw CosXmlClientException.internalException("Unable to create cipher input stream");
        }
    }

    /**
     * Returns the plaintext length from the request and metadata; or -1 if unknown.
     */
    protected final long plaintextLength(PutObjectRequest request,
            ObjectMetadata metadata) {

        String filePath = request.getSrcPath();
        Uri uri = request.getUri();

        if (!TextUtils.isEmpty(filePath)) {
            return  new File(filePath).length();
        } else if (uri != null) {
            Context context = ContextHolder.getAppContext();
            if (context != null) {
                return QCloudUtils.getUriContentLength(uri, context.getContentResolver());
            }
        }
        return -1;
    }

    public final COSCryptoScheme getCOSCryptoScheme() {
        return cryptoScheme;
    }

    static Range getAdjustedCryptoRange(Range range) {

        if (range == null) {
            return null;
        }

        long rangeStart = getCipherBlockLowerBound(range.getStart());
        long rangeEnd = range.getEnd() == -1 ? -1 :
                getCipherBlockUpperBound(range.getEnd());
        return new Range(rangeStart, rangeEnd);
    }

    static long[] getAdjustedCryptoRange(long[] range) {
        // If range is invalid, then return null.
        if (range == null || range[0] > range[1]) {
            return null;
        }
        long[] adjustedCryptoRange = new long[2];
        adjustedCryptoRange[0] = getCipherBlockLowerBound(range[0]);
        adjustedCryptoRange[1] = getCipherBlockUpperBound(range[1]);
        return adjustedCryptoRange;
    }

    private static long getCipherBlockLowerBound(long leftmostBytePosition) {
        long cipherBlockSize = JceEncryptionConstants.SYMMETRIC_CIPHER_BLOCK_SIZE;
        long offset = leftmostBytePosition % cipherBlockSize;
        long lowerBound = leftmostBytePosition - offset - cipherBlockSize;
        return lowerBound < 0 ? 0 : lowerBound;
    }

    /**
     * Takes the position of the rightmost desired byte of a user specified range and returns the
     * position of the end of the following cipher block; or {@value Long#MAX_VALUE} if the
     * resultant position has a value that exceeds {@value Long#MAX_VALUE}.
     */
    private static long getCipherBlockUpperBound(final long rightmostBytePosition) {
        long cipherBlockSize = JceEncryptionConstants.SYMMETRIC_CIPHER_BLOCK_SIZE;
        long offset = cipherBlockSize - (rightmostBytePosition % cipherBlockSize);
        long upperBound = rightmostBytePosition + offset + cipherBlockSize;
        return upperBound < 0 ? Long.MAX_VALUE : upperBound;
    }
}
