/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.qcloud.core.util;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

public class QCloudUtils {

    public static boolean isNotEmpty(String str) {
        return !TextUtils.isEmpty(str);
    }

    public static long getUriContentLength(Uri uri, ContentResolver contentResolver) {
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            long length = -1;
            Cursor cursor = contentResolver.query(uri,
                    null, null, null, null);
            if (cursor != null) {
                try {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    if (cursor.moveToFirst()) {
                        length = cursor.getLong(sizeIndex);
                    }
                } finally {
                    cursor.close();
                }
            }

            //适配部分 vivo 机型
            if(length == -1){
                length = getUriContentLengthByRead(uri, contentResolver);
            }

            return length;
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            File file = new File(uri.getPath());
            return file.length();
        }

        return -1;
    }

    public static boolean doesUriFileExist(Uri uri, ContentResolver contentResolver) {

        try {
            ParcelFileDescriptor fileDescriptor = contentResolver.openFileDescriptor(uri, "r");
            fileDescriptor.close();
            return true;
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return false;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return true;
        }
    }

    private static long getUriContentLengthByRead(Uri uri, ContentResolver contentResolver) {
        InputStream inputStream = null;
        try {
            inputStream = contentResolver.openInputStream(uri);
            long length = 0;
            byte[] buff = new byte[8192];

            int readLen;
            while((readLen = inputStream.read(buff)) != -1) {
                length += readLen;
            }

            return length;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static long getUriContentLengthBySkip(Uri uri, ContentResolver contentResolver) {
        InputStream inputStream = null;
        try {
            inputStream = contentResolver.openInputStream(uri);
            long length = 0;
            int skipStep = 8192;

            long skipLen;
            while((skipLen = inputStream.skip(skipStep)) != -1L) {
                length += skipLen;
            }

            return length;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
}
