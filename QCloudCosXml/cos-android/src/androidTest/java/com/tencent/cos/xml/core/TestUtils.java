package com.tencent.cos.xml.core;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import androidx.test.platform.app.InstrumentationRegistry;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSTransferTask;
import com.tencent.cos.xml.transfer.COSXMLTask;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;

import org.junit.Assert;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class TestUtils {


    public static void assertCOSXMLTaskSuccess(COSXMLTask cosxmlTask) {
        Assert.assertTrue(cosxmlTask.getException() == null?cosxmlTask.getTaskState().name():getCosExceptionMessage(cosxmlTask.getException()),
                cosxmlTask.getTaskState() == TransferState.COMPLETED);
    }

    public static void assertCOSXMLTaskSuccess(COSTransferTask cosxmlTask) {
        if(cosxmlTask.getClientException() == null && cosxmlTask.getServiceException() == null){
            Assert.assertTrue(cosxmlTask.getTaskState().name(),
                    cosxmlTask.getTaskState() == TransferState.COMPLETED);
        } else {
            Assert.assertTrue(getCosExceptionMessage(cosxmlTask.getClientException(), cosxmlTask.getServiceException()),
                    cosxmlTask.getTaskState() == TransferState.COMPLETED);
        }
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

    private static boolean isXmlBean(Object object){
        Class<?> clazz = object.getClass();
        String targetClassName = clazz.getName();
        try {
            Class.forName(targetClassName + "$$XmlAdapter");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static <T> void printXML(T object) {
        if(isXmlBean(object)) {
            try {
                Log.i(TestConst.UT_TAG, "printXML");
                TestUtils.print(QCloudXmlUtils.toXml(object));
            } catch (CosXmlClientException e) {
                Assert.fail(e.getMessage());
            } catch (IllegalStateException e){
                if(!e.getMessage().equals("The toXml method is not implemented")){
                    Assert.fail(e.getMessage());
                } else {
                    TestUtils.print(objectToXml(object));
                }
            }
        } else {
            TestUtils.print(objectToXml(object));
        }
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

    public static String big60mFilePath() {

        String filePath = localPath(TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_PATH);
        try {
            boolean success = createFile(filePath, TestConst.PERSIST_BUCKET_BIG_60M_OBJECT_SIZE);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bigPlusFilePath() {

        String filePath = localPath(TestConst.PERSIST_BUCKET_BIG_OBJECT_PATH);
        try {
            boolean success = createFile(filePath, TestConst.PERSIST_BUCKET_BIG_OBJECT_SIZE+369);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String filePath(String name, int size) {

        String filePath = localPath(name);
        try {
            boolean success = createFile(filePath, size);
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
        return getContext().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
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
        TestUtils.print(objectToXml(result));
    }

    public static String objectToXml(Object object) {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true); // 设置缩进特性
            serializer.startDocument("UTF-8", true);
            serializeObject(serializer, object);
            serializer.endDocument();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return writer.toString();
    }

    private static void serializeObject(XmlSerializer serializer, Object object) throws IllegalAccessException, IllegalArgumentException, IOException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        serializer.startTag("", clazz.getSimpleName());
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue = field.get(object);
            if (fieldValue != null) {
                if (field.getType().isPrimitive() || field.getType().equals(String.class)) {
                    serializer.startTag("", fieldName);
                    serializer.text(fieldValue.toString());
                    serializer.endTag("", fieldName);
                } else if (field.getType().equals(List.class)) {
                    List<?> list = (List<?>) fieldValue;
                    for (Object item : list) {
                        serializeObject(serializer, item);
                    }
                } else {
                    serializeObject(serializer, fieldValue);
                }
            }
        }
        serializer.endTag("", clazz.getSimpleName());
    }
}
