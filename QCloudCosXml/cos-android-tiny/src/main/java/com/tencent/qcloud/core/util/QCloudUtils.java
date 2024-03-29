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

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class QCloudUtils {

    public static boolean isNotEmpty(String str) {
        return !TextUtils.isEmpty(str);
    }

    public static long getUriContentLength(Uri uri, ContentResolver contentResolver) {
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = contentResolver.query(uri,
                    null, null, null, null);
            if (cursor != null) {
                try {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    if (cursor.moveToFirst()) {
                        return cursor.getLong(sizeIndex);
                    }
                    return -1;
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

    /**
     * 部分 vivo 机型
     *
     * @param uri
     * @param contentResolver
     * @return
     */
    public static long getUriContentLength2(Uri uri, ContentResolver contentResolver) {
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            return getUriContentLengthByRead(uri, contentResolver);
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
}
