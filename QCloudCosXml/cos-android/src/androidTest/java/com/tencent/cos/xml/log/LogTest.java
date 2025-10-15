package com.tencent.cos.xml.log;

import android.util.Log;

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
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.logger.COSLogger;
import com.tencent.qcloud.core.logger.LogEntity;
import com.tencent.qcloud.core.logger.LogLevel;
import com.tencent.qcloud.core.logger.channel.CosLogListener;
import com.tencent.qcloud.track.cls.ClsAuthenticationException;
import com.tencent.qcloud.track.cls.ClsLifecycleCredentialProvider;
import com.tencent.qcloud.track.cls.ClsSessionCredentials;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>
 * Created by rickenwang on 2021/11/12.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4.class)
public class LogTest {
    @Test
    public void testLog() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSLogger.setAppVersion("2.8.6");
        COSLogger.setDeviceID("testDeviceID");
        COSLogger.setDeviceModel("testDeviceModel");
        Map<String, String> extras = new HashMap<>();
        extras.put("userID", "1");
        COSLogger.setExtras(extras);
        COSLogger.addSensitiveRule("(x-cos-request-id:\\s*)[^\\s]+", "$1***");
//        COSLogger.removeSensitiveRule("(x-cos-request-id:\\s*)[^\\s]+");
        testUploadFileByPath(transferManager);
        Log.d("getLogRootDir", COSLogger.getLogRootDir());
        File[] files = COSLogger.getLogFiles(3);
        for(File file : files) {
            Log.d("testLog", file.getAbsolutePath());
        }
    }

    @Test
    public void testDisableLogcat() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSLogger.enableLogcat(false);
        testUploadFileByPath(transferManager);
    }

    @Test
    public void testDisableFile() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSLogger.enableLogFile(false);
        testUploadFileByPath(transferManager);
    }

    @Test
    public void testLogListener() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        CosLogListener listener1 = new CosLogListener() {
            @Override
            public void onLog(LogEntity entity) {
                Log.d("testLogListener1", entity.getLogcatString());
            }
        };
        CosLogListener listener2 = new CosLogListener() {
            @Override
            public void onLog(LogEntity entity) {
                Log.d("testLogListener2", entity.getLogcatString());
            }
        };
        COSLogger.addLogListener(listener1);
        COSLogger.addLogListener(listener2);

        // 新启动一个线程，3秒后测试移除监听器
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                COSLogger.removeLogListener(listener2);
            }
        }).start();
        testUploadFileByPath(transferManager);
    }

    @Test
    public void testMinLevel() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        COSLogger.setMinLevel(LogLevel.WARN);
        COSLogger.setFileMinLevel(LogLevel.VERBOSE);
        testUploadFileByPath(transferManager);
    }

    @Test
    public void testFileEncryption() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
        // 初始化（密钥和IV建议安全存储）
        byte[] key = new byte[] {
                // 32个字节的16进制表示（示例值，实际应使用安全随机数生成）
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
                0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F
        };
        byte[] iv ={
                0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
                0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F
        };
        COSLogger.setLogFileEncryptionKey(key, iv);
        testUploadFileByPath(transferManager);
    }

    @Test
    public void testCls() {
        TransferManager transferManager = ServiceFactory.INSTANCE.newDefaultTransferManager();
//        COSLogger.setCLsChannel("5edf1c8b-160c-43d5-8506-0a8621a3fa73", "ap-guangzhou.cls.tencentcs.com", "", "");
        COSLogger.setCLsChannel("5edf1c8b-160c-43d5-8506-0a8621a3fa73", "ap-guangzhou.cls.tencentcs.com", new ClsLifecycleCredentialProvider() {
            @Override
            protected ClsSessionCredentials fetchNewCredentials() throws ClsAuthenticationException {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://49.232.13.225:3000/sts/cls")
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response == null || !response.isSuccessful())
                    throw new ClsAuthenticationException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                try {
                    String jsonStr = "";
                    try {
                        jsonStr = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("MySessionCredentialProvider", jsonStr);
                    JSONTokener jsonParser = new JSONTokener(jsonStr);
                    JSONObject data = (JSONObject) jsonParser.nextValue();
                    JSONObject credentials = data.getJSONObject("credentials");
                    return new ClsSessionCredentials(credentials.getString("tmpSecretId"), credentials.getString("tmpSecretKey"),
                            credentials.getString("sessionToken"), data.getLong("expiredTime"));
                } catch (JSONException ex) {
                    throw new ClsAuthenticationException(ex.getMessage());
                }
            }
        });
        testUploadFileByPath(transferManager);
    }

    public void testUploadFileByPath(TransferManager transferManager) {
        String filePath = TestUtils.bigFilePath();

        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                filePath);
        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                TestUtils.parseBadResponseBody(result);
                TestUtils.sleep(10000);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                TestUtils.sleep(10000);
                testLocker.release();
            }
        });
        testLocker.lock();
    }
}
