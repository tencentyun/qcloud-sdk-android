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

import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.common.Range;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.util.Base64Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.internal.Util;

/**
 * 编码加密工具类
 */

public class DigestUtils {

    public static String getMD5(String filePath) throws CosXmlClientException {
        if(filePath == null) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "file Path is null");
        File file = new File(filePath);
        if(!file.exists()) throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "file Path is not exist");
        String md5;
        FileInputStream fileInputStream = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 32];
            int len = -1;
            while ((len = fileInputStream.read(buffer)) != -1){
                messageDigest.update(buffer, 0, len);
            }
            md5 = StringUtils.toHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        } catch (FileNotFoundException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(), e);
        }finally {
            CloseUtil.closeQuietly(fileInputStream);
        }
        return md5;
    }

    @Nullable public static String getCOSMd5(InputStream inputStream, long size) throws IOException {
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buff = new byte[8 * 1024];
            int readLen;
            long remainLength = size;
            while (remainLength > 0L && (readLen = inputStream.read(buff, 0,
                    (buff.length > remainLength ? (int) remainLength : buff.length)))!= -1){
                messageDigest.update(buff, 0, readLen);
                remainLength -= readLen;
            }
            return "\"" + StringUtils.toHexString(messageDigest.digest()) + "\"";
        } catch (IOException e) {
            throw e;
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("unSupport Md5 algorithm", e);
        }
    }

    public static String getSha1(String content) throws CosXmlClientException {
        String sha1;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            sha1 = StringUtils.toHexString(messageDigest.digest(content.getBytes(
                    Charset.forName("UTF-8"))));
        } catch (NoSuchAlgorithmException e) {
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        }
        return sha1;
    }

    public static String getSHA1FromPath(String filePath) throws CosXmlClientException {
        String sha1;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[64 * 1024];
            int len;
            while((len = fileInputStream.read(buffer,0,buffer.length)) != -1){
                messageDigest.update(buffer,0,len);
            }
            sha1 = StringUtils.toHexString(messageDigest.digest());
        } catch (FileNotFoundException e) {
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), e);
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.IO_ERROR.getCode(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        }finally {
            CloseUtil.closeQuietly(fileInputStream);
        }
        return sha1;
    }

    public static String getSHA1FromBytes(byte[] data, int offset, int len) throws CosXmlClientException{
        String sha1;
        if(data == null || len <= 0 || offset < 0){
            throw new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "data == null | len <= 0 |" +
                    "offset < 0 |offset >= len");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(data,offset,len);
            sha1 = StringUtils.toHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        }catch (OutOfMemoryError e){
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        }
        return sha1;
    }

    public static String getHmacSha1(String content, String key) throws CosXmlClientException {
        String hmacSha1;
        try {
            byte[] byteKey = key.getBytes(Charset.forName("UTF-8"));
            SecretKey hmacKey = new SecretKeySpec(byteKey, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(hmacKey);
            hmacSha1 = StringUtils.toHexString(mac.doFinal(content.getBytes(
                    Charset.forName("UTF-8"))));
        } catch (NoSuchAlgorithmException e) {
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        } catch (InvalidKeyException e) {
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        }
        return hmacSha1;
    }

    public static String getBase64(String content) throws CosXmlClientException {
        if(TextUtils.isEmpty(content)){
            return content;
        }
        try {
            return Base64.encodeToString(content.getBytes("utf-8"), Base64.NO_WRAP); // NO_WARP
        } catch (UnsupportedEncodingException e) {
            throw new CosXmlClientException(ClientErrorCode.INTERNAL_ERROR.getCode(), e);
        }
    }
}
