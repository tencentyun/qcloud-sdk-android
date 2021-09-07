package com.tencent.cos.xml.transfer;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.AbortMultiUploadRequest;
import com.tencent.cos.xml.model.object.AbortMultiUploadResult;

import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * <p>
 * Created by rickenwang on 2021/6/30.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class CosServiceTaskProxy {


    private CosXmlSimpleService cosXmlSimpleService;

    CosServiceTaskProxy(CosXmlSimpleService cosXmlSimpleService) {
        this.cosXmlSimpleService = cosXmlSimpleService;
    }
    

    public Task<AbortMultiUploadResult> abortMultiUpload(final AbortMultiUploadRequest abortMultiUploadRequest) {

        final TaskCompletionSource<AbortMultiUploadResult> tcs = new TaskCompletionSource<>();
        cosXmlSimpleService.abortMultiUploadAsync(abortMultiUploadRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                tcs.setResult((AbortMultiUploadResult) result);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                tcs.setError(clientException != null ? clientException : serviceException != null ? serviceException :
                        new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), "UnknownError"));
            }
        });
        return tcs.getTask();
    }
}
