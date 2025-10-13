package com.tencent.cos.xml;

import static com.tencent.cos.xml.core.TestConst.PERSIST_BUCKET_BIG_OBJECT_SIZE;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * Created by jordanqin on 2025/8/15.
 * Copyright 2010-2025 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DomainSwitchingTest {
    private static final String TAG = "DomainSwitchingTest";

    private static final String PERSIST_OBJECT = "DomainSwitchingTest.txt";
    private CosXmlSimpleService cosXmlService;
    private TransferManager transferManager;

    @Before
    public void setUp() {
        cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
    }

    @Test
    public void test1Upload() {
        String localPath = TestUtils.localPath(PERSIST_OBJECT);
        try {
            TestUtils.createFile(localPath, PERSIST_BUCKET_BIG_OBJECT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        COSXMLUploadTask uploadTask = transferManager.upload(TestConst.PERSIST_BUCKET, PERSIST_OBJECT,
                localPath, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TestUtils.printError("upload success");
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(uploadTask);
    }

    @Test public void test2Download() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                PERSIST_OBJECT,
                TestUtils.localParentPath());
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(),
                getObjectRequest);
        final TestLocker testLocker = new TestLocker();
        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                File file = new File(getObjectRequest.getDownloadPath());
                TestUtils.print("download file size:"+file.length());
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
        TestUtils.assertCOSXMLTaskSuccess(downloadTask);
    }

    @Test
    public void test3Delete() {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(TestConst.PERSIST_BUCKET, PERSIST_OBJECT);
            DeleteObjectResult deleteObjectResult = cosXmlService.deleteObject(deleteObjectRequest);
            Assert.assertNotNull(deleteObjectResult);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        }
    }
}
