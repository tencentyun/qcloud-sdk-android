package com.tencent.cos.xml.doc;

import android.content.Context;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSDownloadTask;
import com.tencent.cos.xml.transfer.COSUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferService;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;

import java.io.File;

/**
 * <p>
 * Created by rickenwang on 2021/11/12.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class RequestIdSnippet {


    private final String COS_REGION = TestConst.PERSIST_BUCKET_REGION;
    private Context context = TestUtils.getContext();
    private QCloudCredentialProvider credentialProvider;

    private void upload() {

        // 1. 初始化 TransferService。在相同配置的情况下，您应该复用同一个 TransferService
        TransferConfig transferConfig = new TransferConfig.Builder()
                .build();
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(COS_REGION)
                .builder();
        CosXmlService cosXmlService = new CosXmlService(context, cosXmlServiceConfig, credentialProvider);
        TransferService transferService = new TransferService(cosXmlService, transferConfig);

        // 2. 初始化 PutObjectRequest
        String bucket = "examplebucket-1250000000"; //存储桶，格式：BucketName-APPID
        String cosPath = "exampleobject"; //对象在存储桶中的位置标识符，即称对象键
        String srcPath = "examplefilepath"; //本地文件的绝对路径
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
                cosPath, srcPath);

        // 3. 调用 upload 方法上传文件
        final COSUploadTask uploadTask = transferService.upload(putObjectRequest);
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // 上传成功，可以在这里拿到 COS 上的文件 CRC64
                String requestId = result.getHeader("x-cos-request-id");
            }

            @Override
            public void onFail(CosXmlRequest request,
                               CosXmlClientException clientException,
                               CosXmlServiceException serviceException) {
                // 只有 CosXmlServiceException 异常才会有 requestId
                if (serviceException != null) {
                    String requestId = serviceException.getRequestId();
                }
            }
        });
    }

}
