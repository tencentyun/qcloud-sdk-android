package com.tencent.cos.xml.crypto;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.object.CompleteMultiUploadRequest;
import com.tencent.cos.xml.model.object.CompleteMultiUploadResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
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
public interface CryptoModule {


    PutObjectResult putObjectSecurely(PutObjectRequest request) throws CosXmlClientException, CosXmlServiceException;

    GetObjectResult getObjectSecurely(GetObjectRequest getObjectRequest) throws CosXmlClientException, CosXmlServiceException;

    CompleteMultiUploadResult completeMultipartUploadSecurely(CompleteMultiUploadRequest request) throws CosXmlClientException, CosXmlServiceException;

    InitMultipartUploadResult initMultipartUploadSecurely(InitMultipartUploadRequest request) throws CosXmlClientException, CosXmlServiceException;

    UploadPartResult uploadPartSecurely(UploadPartRequest request) throws CosXmlClientException, CosXmlServiceException;

    void uploadPartAsyncSecurely(UploadPartRequest request, CosXmlResultListener resultListener);

}
