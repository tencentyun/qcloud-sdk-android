package com.tencent.cos.xml.core;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.transfer.COSXMLTask;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;

import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class TestUtils {


    public static void assertCOSXMLTaskSuccess(COSXMLTask cosxmlTask) {

        Assert.assertTrue(getCosExceptionMessage(cosxmlTask.getException()),
                cosxmlTask.getTaskState() == TransferState.COMPLETED);
    }

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

        RandomAccessFile accessFile = new RandomAccessFile(absolutePath, "rws");
        accessFile.setLength(fileLength);
        accessFile.seek(fileLength/2);
        accessFile.write(new Random().nextInt(200));
        accessFile.seek(fileLength - 1);
        accessFile.write(new Random().nextInt(200));
        accessFile.close();
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

        if (clientException != null) {
            return clientException.getMessage();
        }
        if (serviceException != null) {
            return serviceException.getErrorCode() + " : " + serviceException.getErrorMessage();
        }
        return "Unknown Error";
    }

    public static String getCosExceptionMessage(Exception exception) {

        if (exception instanceof QCloudClientException) {
            return getCosExceptionMessage((QCloudClientException) exception, null);
        } else if (exception instanceof QCloudServiceException) {
            return getCosExceptionMessage(null, (QCloudServiceException) exception);
        } else if (exception != null) {
            return exception.getMessage();
        }
        return "Unknown Error";
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
        return getContext().getExternalCacheDir().getAbsolutePath();
    }
}
