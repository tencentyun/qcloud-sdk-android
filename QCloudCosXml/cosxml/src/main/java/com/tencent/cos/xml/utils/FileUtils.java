package com.tencent.cos.xml.utils;

import android.os.Environment;
import android.util.Log;


import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bradyxiao on 2017/12/14.
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

    public static File[] listFile(File file){
        if(file != null && file.isDirectory()){
            return file.listFiles();
        }else {
            return null;
        }
    }


}
