package com.tencent.cos.xml;

import android.content.Context;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.DeleteBucketDPStateResult;
import com.tencent.cos.xml.model.ci.GetBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.GetBucketDPStateResult;
import com.tencent.cos.xml.model.ci.PutBucketDPStateRequest;
import com.tencent.cos.xml.model.ci.PutBucketDPStateResult;
import com.tencent.cos.xml.model.ci.QRCodeUploadRequest;
import com.tencent.cos.xml.model.ci.QRCodeUploadResult;
import com.tencent.cos.xml.model.ci.SensitiveContentRecognitionRequest;
import com.tencent.cos.xml.model.ci.SensitiveContentRecognitionResult;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.qcloud.core.auth.COSXmlSigner;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.SignerFactory;
import com.tencent.qcloud.core.common.QCloudResultListener;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/10/15.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class CIService extends CosXmlService {

    private static class CISigner extends COSXmlSigner {
        @Override
        protected String getSessionTokenKey() {
            return "x-ci-security-token";
        }
    }

    public CIService(Context context, CosXmlServiceConfig configuration, QCloudCredentialProvider qCloudCredentialProvider) {
        super(context, configuration, qCloudCredentialProvider);
        this.signerType = "CISigner";
        SignerFactory.registerSigner(signerType, new CISigner());
    }

    public GetBucketDPStateResult getBucketDocumentPreviewState(GetBucketDPStateRequest request)
            throws CosXmlClientException, CosXmlServiceException{
        return execute(request, new GetBucketDPStateResult());
    }

    public void getBucketDocumentPreviewStateAsync(GetBucketDPStateRequest request, CosXmlResultListener listener) {
        schedule(request, new GetBucketDPStateResult(), listener);
    }

    public PutBucketDPStateResult putBucketDocumentPreviewState(PutBucketDPStateRequest request)
            throws CosXmlClientException, CosXmlServiceException{
        return execute(request, new PutBucketDPStateResult());
    }

    public void putBucketDocumentPreviewStateAsync(PutBucketDPStateRequest request, CosXmlResultListener listener) {
        schedule(request, new PutBucketDPStateResult(), listener);
    }

    public DeleteBucketDPStateResult deleteBucketDocumentPreviewState(DeleteBucketDPStateRequest request)
            throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new DeleteBucketDPStateResult());
    }

    public void deleteBucketDocumentPreviewStateAsync(DeleteBucketDPStateRequest request, CosXmlResultListener listener)
            throws CosXmlClientException, CosXmlServiceException {
        schedule(request, new DeleteBucketDPStateResult(), listener);
    }

    public QRCodeUploadResult qrCodeUpload(QRCodeUploadRequest request) 
            throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new QRCodeUploadResult());
    }

    public void qrCodeUploadAsync(QRCodeUploadRequest request, CosXmlResultListener cosXmlResultListener) {
        schedule(request, new QRCodeUploadResult(), cosXmlResultListener);
    }
    
    public SensitiveContentRecognitionResult sensitiveContentRecognition(SensitiveContentRecognitionRequest request) 
        throws CosXmlClientException, CosXmlServiceException {
        return execute(request, new SensitiveContentRecognitionResult());
    }

    public void sensitiveContentRecognitionAsync(SensitiveContentRecognitionRequest request, CosXmlResultListener listener) {
        schedule(request, new SensitiveContentRecognitionResult(), listener);
    }
}
