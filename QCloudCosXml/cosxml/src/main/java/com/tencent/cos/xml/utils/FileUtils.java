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

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件工具类
 */

public class FileUtils {

    public static String tempCache(InputStream inputStream) throws CosXmlClientException {
        if(inputStream == null)return null;
        FileOutputStream fileOutputStream = null;
        try {
            //String tempPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "temp.tmp";
            String tempPath = CosXmlSimpleService.appCachePath + File.separator + "temp.tmp";
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

    public static boolean deleteFileIfExist(String filePath) {

        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
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
