package com.tencent.qcloud.core.util;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/6/1.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class QCloudUtils {

    public static long getUriContentLength(Uri uri, ContentResolver contentResolver) {
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = contentResolver.query(uri,
                    null, null, null, null);
            if (cursor != null) {
                try {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    cursor.moveToFirst();
                    return cursor.getLong(sizeIndex);
                } finally {
                    cursor.close();
                }
            }
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            File file = new File(uri.getPath());
            return file.length();
        }

        return -1;
    }

    public static byte[] toBytes(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public static void writeToFile(String fileName, byte[] bytes) {

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] readBytesFromFile(String fileName) {

        FileInputStream fileInputStream = null;
        byte[] data = null;
        try {
            File file = new File(fileName);
            data = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static boolean isNetworkConnected() {

        Context context = ContextHolder.getAppContext();
        if (context == null) {
            return true;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return true;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
