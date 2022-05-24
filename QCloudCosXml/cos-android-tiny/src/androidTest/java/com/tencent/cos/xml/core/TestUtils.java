package com.tencent.cos.xml.core;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpResponse;

import org.junit.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class TestUtils {
    public static void assertErrorMessageNull(StringBuilder builder) {
        Assert.assertTrue(builder.toString(), TextUtils.isEmpty(builder.toString()));
    }

    public static void printError(String message) {

        Log.e(TestConst.UT_TAG, message);
    }

    public static void print(String message) {

        Log.i(TestConst.UT_TAG, message);
    }

    public static boolean isQuicSupportDevice() {
        for (String abi : Build.SUPPORTED_ABIS) {
            if ("arm64-v8a".equalsIgnoreCase(abi) || "armeabi-v7a".equalsIgnoreCase(abi)) {
                return true;
            }
        }
        return false;
    }

    public static Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getContext();
    }

    public static String localPath(String subPath) {

        if (!subPath.startsWith("/")) {
            subPath = "/".concat(subPath);
        }
    
        return localParentPath().concat(subPath);
    }

    public static String smallFilePath() {

        String filePath = localPath(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        try {
            boolean success = createFile(filePath, TestConst.PERSIST_BUCKET_SMALL_OBJECT_SIZE);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bigFilePath() {

        String filePath = localPath(TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        try {
            boolean success = createFile(filePath, TestConst.PERSIST_BUCKET_BIG_OBJECT_SIZE);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean removeLocalFile(String filePath) {

        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean createFile(String absolutePath, long fileLength) throws IOException {

        File file = new File(absolutePath);
        File parentFile = file.getParentFile();

        if (file.exists() && file.length() == fileLength) {
            return true;
        }

        file.delete();

        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        long length = 0;
        Random random = new Random(System.currentTimeMillis());
        while (length < fileLength) {

            int pageSize = (int) Math.min(1024, fileLength - length);
            byte[] bytes = new byte[pageSize];
            random.nextBytes(bytes);
            fileOutputStream.write(bytes);
            length += pageSize;
        }
        fileOutputStream.close();
        return true;
    }

    public static boolean createRandomComplexFile(String absolutePath, long fileLength) throws IOException {

        File file = new File(absolutePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        RandomAccessFile accessFile = new RandomAccessFile(absolutePath, "rws");
        accessFile.setLength(fileLength);

        for (int i = 0; i < fileLength; i += 1024) {
            accessFile.seek(i);
            accessFile.write(new Random().nextInt(200));
        }
        accessFile.seek(fileLength - 1);
        accessFile.write(new Random().nextInt(200));
        accessFile.close();
        return true;
    }

    public static String getCosExceptionMessage(QCloudClientException clientException, QCloudServiceException serviceException) {
        return getCosExceptionMessage(null, clientException, serviceException);
    }

    public static String getCosExceptionMessage(String source, QCloudClientException clientException, QCloudServiceException serviceException) {
        if (TextUtils.isEmpty(source)) {
            source = "";
        } else {
            source = source + " : ";
        }

        if (clientException != null) {
            return source + clientException.getMessage();
        }
        if (serviceException != null) {
            return source + serviceException.getErrorCode() + " : " + serviceException.getErrorMessage() + " " + serviceException.getRequestId();
        }
        return source + "Unknown Error";
    }

    public static String getCosExceptionMessage(Exception exception) {
        return getCosExceptionMessage(null, exception);
    }

    public static String getCosExceptionMessage(String source, Exception exception) {
        if (TextUtils.isEmpty(source)) {
            source = "";
        } else {
            source = source + " : ";
        }

        if (exception instanceof QCloudClientException) {
            return getCosExceptionMessage(source, (QCloudClientException) exception, null);
        } else if (exception instanceof QCloudServiceException) {
            return getCosExceptionMessage(source, null, (QCloudServiceException) exception);
        } else if (exception != null) {
            return source + exception.getMessage();
        }
        return source + "Unknown Error";
    }

    public static String getMD5(String str) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String localParentPath() {
        return getContext().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    public static void clearDir(File dir) {

        File[] files = dir.listFiles();
        for (File file : files) {
            removeLocalFile(file.getAbsolutePath());
        }
    }

    public static String extractName(String path) {

        if (path.endsWith("/")) {
            return "";
        }

        int index = path.lastIndexOf("/");
        if (index < 0) {
            return path;
        } else {
            return path.substring(index + 1);
        }
    }

    public static void parseBadResponseBody(CosXmlResult result){
        HttpRequest httpRequest = null;
        try {
            httpRequest = new HttpRequest.Builder()
                    .url(new URL("https://www.qq.com"))
                    .method("get").build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url("http://www.qq.com")
                .build();
        Response.Builder responseBuilder = new Response.Builder()
                .protocol(Protocol.HTTP_1_1)
                .message("error")
                .request(request);

        String badBody = "badxml<Error>\n" +
                "  <Code>RequestTimeTooSkewed</Code>\n" +
                "  <Message>[错误信息]</Message>\n" +
                "  <Resource>[资源地址]</Resource>\n" +
                "  <RequestId>[请求ID]</RequestId>\n" +
                "  <TraceId>[错误ID]</TraceId>\n" +
                "</Error>";
        Response response = responseBuilder
                .code(403)
                .body(ResponseBody.create(MediaType.parse(HttpConstants.ContentType.XML), badBody))
                .build();
        HttpResponse httpResponse = new HttpResponse(httpRequest, response);
        try {
            result.parseResponseBody(httpResponse);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
    }
}
