package com.tencent.cos.xml.core.test;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConfigs;
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
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.tencent.cos.xml.core.TestUtils.mergeExceptionMessage;

/**
 * <p>
 * Created by rickenwang on 2020/7/22.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class CdnDownloadTest {

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
            QCloudLogger.i("QCloud", TestUtils.getMD5(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHeadObject() {


        CosXmlService cosXmlService = TestUtils.newCdnTerminalService();
        HeadObjectRequest headObjectRequest = new HeadObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_PIC_PATH);

        String path = TestConfigs.COS_PIC_PATH;
        long timestamp = System.currentTimeMillis() / 1000;
        String rand = String.valueOf(new Random(timestamp).nextInt(10000));
        String key = TestConfigs.COS_SUB_BUCKET_CDN_SIGN;

        Map<String, String> paras = new HashMap<>();
        String sign = cdnSign(path, timestamp, rand, key);
        paras.put("sign", String.format("%d-%s-0-%s", timestamp, rand, sign));
        headObjectRequest.setQueryParameters(paras);

        try {
            headObjectRequest.setRequestHeaders("Host", "android-ut-persist-gz-1253653367.file.myqcloud.com", false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        try {
            cosXmlService.headObject(headObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
    }

    @Test public void testGetObject() {

        String bucket = TestConfigs.TERMINAL_PERSIST_BUCKET;

        CosXmlService cosXmlService = TestUtils.newCdnTerminalService();
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, TestConfigs.COS_PIC_PATH, TestConfigs.LOCAL_FILE_DIRECTORY.concat("test2/"));

        String path = TestConfigs.COS_PIC_PATH;
        long timestamp = System.currentTimeMillis() / 1000;
        String rand = String.valueOf(new Random(timestamp).nextInt(10000));
        String key = TestConfigs.COS_SUB_BUCKET_CDN_SIGN;

        Map<String, String> paras = new HashMap<>();
        String sign = cdnSign(path, timestamp, rand, key);
        paras.put("sign", String.format("%d-%s-0-%s", timestamp, rand, sign));
        getObjectRequest.setQueryParameters(paras);

        try {
            cosXmlService.getObject(getObjectRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }

    }

    @Test public void testTransferManagerDownload() {

        TransferManager transferManager = TestUtils.newCdnTerminalTransferManager();
        GetObjectRequest getObjectRequest = new GetObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET,
                TestConfigs.COS_TXT_1G_PATH, TestConfigs.LOCAL_FILE_DIRECTORY.concat("test2/"));

        String path = TestConfigs.COS_TXT_1G_PATH;
        Long timestamp = System.currentTimeMillis() / 1000;
        String rand = String.valueOf(new Random(timestamp).nextInt(10000));
        String key = TestConfigs.COS_SUB_BUCKET_CDN_SIGN;

        Map<String, String> paras = new HashMap<>();
        String sign = cdnSign(path, timestamp, rand, key);
        paras.put("sign", String.format("%d-%s-0-%s", timestamp, rand, sign));
        getObjectRequest.setQueryParameters(paras);

        try {
            getObjectRequest.setRequestHeaders("Host", "android-ut-persist-gz-1253653367.file.myqcloud.com", false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }

        COSXMLDownloadTask downloadTask = transferManager.download(TestUtils.getContext(), getObjectRequest);
        final TestLocker testLocker = new TestLocker();

        downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.assertTrue(true);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(mergeExceptionMessage(clientException, serviceException));
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
    }






}
