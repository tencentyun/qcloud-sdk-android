package com.tencent.cos.xml.cdn;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlSimpleService;
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
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * Created by rickenwang on 2020/7/22.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class CdnDownloadTest {

    private boolean testCdn = true;
    @After
    public void clearDownloadFiles() {
        TestUtils.clearDir(new File(TestUtils.localParentPath()));
    }


    private String cdnSign(String path, long timestamp, String rand, String key) {

        try {
            return TestUtils.getMD5(String.format("%s-%d-%s-%d-%s", path, timestamp, rand, 0, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Test
    public void testMd5() {

        String str = "/test.jpg-1595236205-c2iglbgtdni-0-b8ejrafcq7ax6y1pn1iw84cgu6";
        try {
            Assert.assertEquals("d36876815a07df86a37176b2a15289e6", TestUtils.getMD5(str));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testHeadObject() {
        if (!testCdn) {
            return;
        }

        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newCDNService();

        String path = TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH;
        HeadObjectRequest headObjectRequest = new HeadObjectRequest(TestConst.PERSIST_BUCKET, path);


        long timestamp = System.currentTimeMillis() / 1000;
        String rand = String.valueOf(new Random(timestamp).nextInt(10000));
        String key = TestConst.PERSIST_BUCKET_CDN_SIGN;

        Map<String, String> paras = new HashMap<>();
        String sign = cdnSign(path, timestamp, rand, key);
        paras.put("sign", String.format("%d-%s-0-%s", timestamp, rand, sign));
        headObjectRequest.setQueryParameters(paras);

        //重试三次 有些时候会出现AssertionError: null (Service: null; Status Code: 403; Status Message: ; Error Code: null; Request ID: null)服务端错误
        try {
            cosXmlService.headObject(headObjectRequest);
        } catch (Exception e) {
            TestUtils.sleep(1000);
            try {
                cosXmlService.headObject(headObjectRequest);
            } catch (Exception e1) {
                TestUtils.sleep(1000);
                try {
                    cosXmlService.headObject(headObjectRequest);
                } catch (Exception e2) {
                    Assert.fail(e.getMessage());
                }
            }
        }

        Assert.assertTrue(true);
    }

    @Test public void testGetObject() {
        if (!testCdn) {
            return;
        }

        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newCDNService();

        String bucket = TestConst.PERSIST_BUCKET;
        String path = TestConst.PERSIST_BUCKET_PIC_PATH;

        ///String path = TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH;

        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                path, TestUtils.localParentPath());

        long timestamp = System.currentTimeMillis() / 1000;
        String rand = String.valueOf(new Random(timestamp).nextInt(10000));
        String key = TestConst.PERSIST_BUCKET_CDN_SIGN;


        Map<String, String> paras = new HashMap<>();
        String sign = cdnSign(path, timestamp, rand, key);
        paras.put("sign", String.format("%d-%s-0-%s", timestamp, rand, sign));
        getObjectRequest.setQueryParameters(paras);

        try {
            cosXmlService.getObject(getObjectRequest);
        } catch (Exception e) {
            TestUtils.sleep(1000);
            try {
                cosXmlService.getObject(getObjectRequest);
            } catch (Exception e1) {
                TestUtils.sleep(1000);
                try {
                    cosXmlService.getObject(getObjectRequest);
                } catch (Exception e2) {
                    Assert.fail(e.getMessage());
                }
            }
        }

        Assert.assertTrue(true);
    }

    private int retry = 3;
    @Test public void testTransferManagerDownload() {

        if (!testCdn) {
            return;
        }

        String path = TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH;

        TransferManager transferManager = ServiceFactory.INSTANCE.newCdnTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConst.PERSIST_BUCKET,
                path, TestUtils.localParentPath());

        long timestamp = System.currentTimeMillis() / 1000;
        String rand = String.valueOf(new Random(timestamp).nextInt(10000));
        String key = TestConst.PERSIST_BUCKET_CDN_SIGN;


        Map<String, String> paras = new HashMap<>();
        String sign = cdnSign(path, timestamp, rand, key);
        paras.put("sign", String.format("%d-%s-0-%s", timestamp, rand, sign));
        getObjectRequest.setQueryParameters(paras);
        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(), getObjectRequest);
        final TestLocker testLocker = new TestLocker();
        final StringBuilder stringBuilder = new StringBuilder();

        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                stringBuilder.append(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });

        downloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                QCloudLogger.i("QCloudHttp", "complete = " + complete + ",target = " + target);
            }
        });

        testLocker.lock();

        //重试三次 有些时候会出现AssertionError: null (Service: null; Status Code: 403; Status Message: ; Error Code: null; Request ID: null)服务端错误
        if(downloadTask.getTaskState() != TransferState.COMPLETED){
            if(retry > 0) {
                TestUtils.sleep(600);
                retry--;
                testTransferManagerDownload();
            } else {
                TestUtils.assertCOSXMLTaskSuccess(downloadTask);
            }
        } else {
            Assert.assertTrue(true);
        }
    }
}
