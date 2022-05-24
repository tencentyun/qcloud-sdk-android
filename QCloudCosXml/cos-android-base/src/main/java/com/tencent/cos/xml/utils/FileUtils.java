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

package com.tencent.cos.xml.utils;

import android.util.Log;

import com.tencent.cos.xml.CosXmlBaseService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.internal.Util;

/**
 * 文件工具类
 */

public class FileUtils {



    public static void saveInputStreamToTmpFile(InputStream stream, File file, long offset, long size) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            long bytesTotal = 0;
            long bytesLimit = size;
            if (bytesLimit < 0) {
                bytesLimit = Long.MAX_VALUE;
            }
            if (offset > 0) {
                long skip = stream.skip(offset);
            }
            while (bytesTotal < bytesLimit && (bytesRead = stream.read(buffer)) != -1) {
                fos.write(buffer, 0, (int) Math.min(bytesRead, bytesLimit - bytesTotal));
                bytesTotal += bytesRead;
            }
            fos.flush();
        } finally {
            if(fos != null) Util.closeQuietly(fos);
        }
    }

    public static String tempCache(InputStream inputStream) throws CosXmlClientException {
        if(inputStream == null)return null;
        FileOutputStream fileOutputStream = null;
        try {
            //String tempPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "temp.tmp";
            String tempPath = CosXmlBaseService.appCachePath + File.separator + "temp.tmp";
            Log.d("UnitTest", tempPath);
            File tempFile = new File(tempPath);
            if(tempFile.exists())tempFile.delete();
            fileOutputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024 * 64];
            int receiveLen = -1;
            while ((receiveLen = inputStream.read(buffer, 0, buffer.length))> 0){
                fileOutputStream.write(buffer, 0, receiveLen);
            }
            fileOutputStream.flush();
            return tempPath;
        }catch (IOException e){
            throw new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(), e);
        }finally {
            CloseUtil.closeQuietly(fileOutputStream);
            CloseUtil.closeQuietly(inputStream);
        }
    }
    
    public static void intercept(String filePath, long offset, long size) throws IOException {

        if (size <= 0) {
            clearFile(filePath);
        }

        File sourceFile = new File(filePath);
        File tempFile = new File(filePath.concat("." + System.currentTimeMillis() + ".temp"));
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        FileInputStream fileInputStream = new FileInputStream(sourceFile);

        if (offset > 0) {
            long skip = fileInputStream.skip(offset);
            if (skip != offset) {
                throw new IOException("skip size is not equal to offset");
            }  
        }

        long remainSize = size;
        byte[] buffer = new byte[1024 * 64];
        int pageLength = (int) Math.min(buffer.length, remainSize);
        int readLength = 0;
        while ((readLength = fileInputStream.read(buffer, 0, pageLength)) > 0) {
            fileOutputStream.write(buffer, 0, readLength);
            remainSize -= readLength;
            pageLength = (int) Math.min(buffer.length, remainSize);
        }

        deleteFileIfExist(filePath);
        boolean rename = tempFile.renameTo(sourceFile);
        
        if (!rename) {
            throw new IOException("rename to " + filePath + "failed");
        }
    }

    public static boolean deleteFileIfExist(String filePath) {

        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
    
    public static boolean clearFile(String filePath) throws IOException {
        
        if (deleteFileIfExist(filePath)) {
            return new File(filePath).createNewFile();
        }
        return false;
    }


    public static File[] listFile(File file){
        if(file != null && file.isDirectory()){
            return file.listFiles();
        }else {
            return null;
        }
    }


}
