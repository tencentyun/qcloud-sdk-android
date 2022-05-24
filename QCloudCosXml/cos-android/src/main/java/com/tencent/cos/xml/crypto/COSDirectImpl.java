package com.tencent.cos.xml.crypto;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.cos.xml.model.object.InitMultipartUploadRequest;
import com.tencent.cos.xml.model.object.InitMultipartUploadResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.model.object.UploadPartRequest;
import com.tencent.cos.xml.model.object.UploadPartResult;

/**
 * <p>
 * Created by rickenwang on 2021/7/6.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class COSDirectImpl implements COSDirect {

    private CryptoModuleBase cryptoModule;

    private CosXmlSimpleService cosService;

    public COSDirectImpl(CosXmlSimpleService cosService, CryptoModuleBase cryptoModule) {
        this.cosService = cosService;
        this.cryptoModule = cryptoModule;
    }

    @Override
    public PutObjectResult putObject(PutObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        if (isTransferSecurely()) {
            return cryptoModule.putObjectSecurely(request);
        } else if (cosService != null) {
            return cosService.putObject(request);
        }
        throw CosXmlClientException.internalException("Api implementation not found");
    }

    @Override
    public GetObjectResult getObject(GetObjectRequest getObjectRequest) throws CosXmlClientException, CosXmlServiceException {
        if (isTransferSecurely()) {
            return cryptoModule.getObjectSecurely(getObjectRequest);
        } else if (cosService != null) {
            return cosService.getObject(getObjectRequest);
        }
        throw CosXmlClientException.internalException("Api implementation not found");
    }

    @Override
    public CompleteMultiUploadResult completeMultipartUpload(CompleteMultiUploadRequest request) throws CosXmlClientException, CosXmlServiceException {
        if (isTransferSecurely()) {
            return cryptoModule.completeMultipartUploadSecurely(request);
        } else if (cosService != null) {
            return cosService.completeMultiUpload(request);
        }
        throw CosXmlClientException.internalException("Api implementation not found");
    }

    @Override
    public InitMultipartUploadResult initMultipartUpload(InitMultipartUploadRequest request) throws CosXmlClientException, CosXmlServiceException {
        if (isTransferSecurely()) {
            return cryptoModule.initMultipartUploadSecurely(request);
        } else if (cosService != null) {
            return cosService.initMultipartUpload(request);
        }
        throw CosXmlClientException.internalException("Api implementation not found");
    }

    @Override
    public UploadPartResult uploadPart(UploadPartRequest request) throws CosXmlClientException, CosXmlServiceException {
        if (isTransferSecurely()) {
            return cryptoModule.uploadPartSecurely(request);
        } else if (cosService != null) {
            return cosService.uploadPart(request);
        }
        throw CosXmlClientException.internalException("Api implementation not found");
    }

    @Override
    public void uploadPartAsync(UploadPartRequest request, CosXmlResultListener resultListener) {

        if (isTransferSecurely()) {
            cryptoModule.uploadPartAsyncSecurely(request, resultListener);
        } else if (cosService != null){
            cosService.uploadPartAsync(request, resultListener);
        } else {
            resultListener.onFail(request, CosXmlClientException.internalException("Api implementation not found"), null);
        }
    }

    @Override
    public HeadObjectResult headObject(HeadObjectRequest request) throws CosXmlClientException, CosXmlServiceException {
        return cosService.headObject(request);
    }

    @Override
    public boolean isTransferSecurely() {
        return cryptoModule != null;
    }

    public CosXmlSimpleService getCosService() {
        return cosService;
    }

    public CryptoModuleBase getCryptoModule() {
        return cryptoModule;
    }

    @Override
    public void cancel(CosXmlRequest request) {
        if (cosService != null) {
            cosService.cancel(request);
        }
    }
}
