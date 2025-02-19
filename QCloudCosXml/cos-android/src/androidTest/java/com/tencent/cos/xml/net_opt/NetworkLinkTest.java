package com.tencent.cos.xml.net_opt;

import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_BIG_OBJECT_SIZE;

import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.io.IOException;

/**
 * <p>
 * Created by jordanqin on 2025/1/20 22:07.
 * Copyright 2010-2025 Tencent Cloud. All Rights Reserved.
 */
public class NetworkLinkTest {
    private static final String TAG = "HttpDnsTest";
//    @Test
    public void testAccelerate() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newAccelerateTransferManager();
        testUploadBigFileByPath(transferManager);
    }

    private void testUploadBigFileByPath(TransferManager transferManager) {
        String localPath = TestUtils.localPath("1642166999131.m4a");
        try {
            TestUtils.createFile(localPath, PERSIST_BUCKET_BIG_OBJECT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH,
                localPath, null);
        final TestLocker testLocker = new TestLocker();

        uploadTask.setInitMultipleUploadListener(initiateMultipartUpload -> TestUtils.print(initiateMultipartUpload.uploadId));
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i(TestConst.UT_TAG, "transfer progress is " + complete + "/" + target);
            }
        });
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TestUtils.printXML(result);
                TestUtils.printError("upload success");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if(clientException != null) {
                    clientException.printStackTrace();
                }
                if(serviceException != null) {
                    serviceException.printStackTrace();
                }
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock(500000);
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }
}
