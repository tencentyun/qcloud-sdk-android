package com.tencent.cos.xml.transfer;

import static com.tencent.cos.xml.core.TestUtils.bigPlusFilePath;
import static com.tencent.cos.xml.core.TestUtils.getContext;

import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.crypto.Headers;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.QCloudHttpRequest;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>
 * Created by jordanqin on 2023/8/10 23:56.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class SelfUploadTest {
    private static final String TAG = "SelfUploadTest";

    // 小文件上传
    @Test
    public void testUploadSmallFileByPath() {
        TransferManager transferManager = newSelfTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                TestUtils.filePath("N好.", 1024));
        final COSXMLUploadTask uploadTask = transferManager.upload(putObjectRequest, null);
        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i("QCloudTest", result.headers.toString());
                QCloudLogger.i("QCloudTest", result.printResult());
                QCloudLogger.i("QCloudTest", result.accessUrl);
                TestUtils.parseBadResponseBody(result);
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                TestUtils.printError(TestUtils.getCosExceptionMessage(clientException, serviceException));
                testLocker.release();
            }
        });
        testLocker.lock();
    }

    // 大文件上传
    @Test
    public void testUploadBigFileByUri() {
        TransferManager transferManager = newSelfTransferManager();
        File file = new File(bigPlusFilePath());
        Uri uri = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getPackageName()+".fileProvider", file);

        final COSXMLUploadTask uploadTask = transferManager.upload(
                TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH + 12,
                uri,
                null);

        final TestLocker testLocker = new TestLocker();
        uploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                // QCloudLogger.i(TestConst.UT_TAG, "transfer state is " + state);
                QCloudLogger.i(TestConst.UT_TAG, "upload id is " + uploadTask.getUploadId());
            }
        });
        uploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                QCloudLogger.i("QCloudTest", "upload success!!");
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

    public TransferManager newSelfTransferManager() {
        TransferConfig transferConfig = new TransferConfig.Builder()
                .build();
        return new TransferManager(newSelfService(), transferConfig);
    }

    public CosXmlSimpleService newSelfService() {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();

        return new CosXmlSimpleService(getContext(), cosXmlServiceConfig,
                new QCloudSelfSigner() {
                    /**
                     * 对请求进行签名
                     *
                     * @param request 需要签名的请求
                     * @throws QCloudClientException 客户端异常
                     */
                    @Override
                    public void sign(QCloudHttpRequest request) throws QCloudClientException {
                        // 1. 获取需要签名的字段(可以根据业务添加删除其他字段，对应的服务端也需要修改)
                        String httpMethod = request.method();
                        String host = request.host();
                        String coskey = request.url().getPath();
                        coskey = coskey.startsWith("/") ? coskey.substring(1) : coskey;
                        try {
                            host = URLEncoder.encode(host, "UTF-8");
                            coskey = URLEncoder.encode(coskey, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d(TAG, httpMethod);
                        Log.d(TAG, host);
                        Log.d(TAG, coskey);

                        // 2. 把 request 的签名参数传给服务端计算签名
                        String authorization = null;
                        String securityToken = null;

                        OkHttpClient client = new OkHttpClient();
                        // 服务端代码参考 https://github.com/tencentyun/cos-demo/tree/main/server/server-sign/nodejs
                        // 192.168.31.111为测试服务器，请替换成您的服务器地址
                        String url = String.format("http://192.168.31.111:3000/sts-server-sign?httpMethod=%s&host=%s&cosKey=%s",httpMethod, host, coskey);
                        Log.d(TAG, url);
                        Request signRequest = new Request.Builder()
                                .url(url)
                                .build();

                        Response response = null;
                        try {
                            response = client.newCall(signRequest).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (response == null || !response.isSuccessful())
                            throw new QCloudClientException(new IOException("Unexpected code " + response));

                        try {
                            String jsonStr = "";
                            try {
                                jsonStr = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, jsonStr);
                            JSONTokener jsonParser = new JSONTokener(jsonStr);
                            JSONObject json = (JSONObject) jsonParser.nextValue();
                            if(json.getInt("code") == 0){
                                JSONObject data = json.getJSONObject("data");
                                authorization = data.getString("authorization");
                                if(data.has("securityToken")){
                                    securityToken = data.getString("securityToken");
                                }
                            } else {
                                throw new QCloudClientException("get authorization failed");
                            }
                        } catch (JSONException ex) {
                            throw new QCloudClientException(ex);
                        }

                        // 3. 给请求添加签名
                        request.removeHeader(HttpConstants.Header.AUTHORIZATION);
                        request.addHeader(HttpConstants.Header.AUTHORIZATION, authorization);
                        if(securityToken != null) {
                            request.removeHeader(Headers.SECURITY_TOKEN);
                            request.addHeader(Headers.SECURITY_TOKEN, securityToken);
                        }
                    }
                });
    }
}
