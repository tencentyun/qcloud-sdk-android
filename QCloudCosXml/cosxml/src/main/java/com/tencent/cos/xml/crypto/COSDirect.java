package com.tencent.cos.xml.crypto;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
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
public interface COSDirect {


    PutObjectResult putObject(PutObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    GetObjectResult getObject(GetObjectRequest getObjectRequest) throws CosXmlClientException, CosXmlServiceException;

    CompleteMultiUploadResult completeMultipartUpload(CompleteMultiUploadRequest request) throws CosXmlClientException, CosXmlServiceException;

    InitMultipartUploadResult initMultipartUpload(InitMultipartUploadRequest request) throws CosXmlClientException, CosXmlServiceException;

    UploadPartResult uploadPart(UploadPartRequest request) throws CosXmlClientException, CosXmlServiceException;

    HeadObjectResult headObject(HeadObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    boolean isTransferSecurely();

    CosXmlSimpleService getCosService();

    CryptoModuleBase getCryptoModule();

    void cancel(CosXmlRequest request);

}
