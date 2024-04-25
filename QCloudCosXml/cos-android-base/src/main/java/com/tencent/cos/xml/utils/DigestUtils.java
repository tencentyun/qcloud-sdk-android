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
import com.tencent.cos.xml.exception.CosXmlClientException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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

    public static long getBigIntFromString(String value) {
        return new BigInteger(value).longValue();
    }

    public static String getBigIntToString(long l) {

        BigInteger b0 = BigInteger.valueOf(l>>1&0x4000000000000000L);
        BigInteger b1 = BigInteger.valueOf(l&0x7FFFFFFFFFFFFFFFL);
        return b0.add(b0).add(b1).toString();
    }

    public static String getCRC64String(InputStream inputStream) {
        return getBigIntToString(getCRC64(inputStream));
    }

    public static String getCRC64String(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            return getCRC64String(fileInputStream);
        } catch (Exception exception) {
            return "";
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static long getCRC64(InputStream inputStream) {
        try {
            CRC64 crc64 = new CRC64();
            int readLen;
            byte[] buff = new byte[8 * 1024];
            while ((readLen = inputStream.read(buff)) != -1){
                crc64.update(buff, readLen);
            }
            return crc64.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static long getCRC64(InputStream inputStream, long skip, long size) {

        try {
            long skipNumber = inputStream.skip(skip);
            if (skipNumber != skip) {
                return -1;
            }
            CRC64 crc64 = new CRC64();
            byte[] buff = new byte[8 * 1024];
            int readLen;
            long remainLength = size >= 0 ? size : Long.MAX_VALUE;
            int needSize = (int) Math.min(remainLength, buff.length);

            while (remainLength > 0L && (readLen = inputStream.read(buff, 0, needSize))!= -1){
                crc64.update(buff, readLen);
                remainLength -= readLen;
            }
            return crc64.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Nullable public static String getCOSMd5(InputStream inputStream, long skip, long size) throws IOException {
        try {
            long skipped = 0L;
            while (skipped < skip) {
                long result = inputStream.skip(skip - skipped);
                if (result == 0) {
                    throw new IOException("Failed to skip requested bytes");
                }
                skipped += result;
            }

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buff = new byte[8 * 1024];
            int readLen;
            long remainLength = size >= 0 ? size : Long.MAX_VALUE;

            while (remainLength > 0L) {
                int needSize = (int) Math.min(remainLength, buff.length);
                readLen = 0;
                while (readLen < needSize) {
                    int result = inputStream.read(buff, readLen, needSize - readLen);
                    if (result == -1) {
                        throw new IOException("Unexpected end of stream");
                    }
                    readLen += result;
                }
                messageDigest.update(buff, 0, readLen);
                remainLength -= readLen;
            }

            return "\"" + StringUtils.toHexString(messageDigest.digest()) + "\"";
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Unsupported MD5 algorithm", e);
        }
    }

    public static COSMd5AndReadData getCOSMd5AndReadData(
            InputStream inputStream,
            int size
    ) throws IOException {
        try{
            byte[] readData = new byte[size];
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            int readLen;
            if ((readLen = inputStream.read(readData, 0, size))!= -1){
                if (readLen < size) {
                    return  new COSMd5AndReadData(
                            "",
                            subByte(readData, 0, readLen)
                    );
                }
                messageDigest.update(readData, 0, readLen);
            }
            return new COSMd5AndReadData(
                    "\"" + StringUtils.toHexString(messageDigest.digest()) + "\"",
                    subByte(readData, 0, readLen)
            );
        } catch (IOException e) {
            throw e;
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("unSupport Md5 algorithm", e);
        }
    }

    /**
     * 截取byte数组   不改变原数组
     * @param b 原数组
     * @param off 偏差值（索引）
     * @param length 长度
     * @return 截取后的数组
     */
    private static byte[] subByte(byte[] b,int off,int length){
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }

    public static class COSMd5AndReadData {
        public String md5;
        public byte[] readData;

        public COSMd5AndReadData(String md5, byte[] readData) {
            this.md5 = md5;
            this.readData = readData;
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

    /**
     * 获取URL安全的base64编码字符串<br>
     * 1、将普通 BASE64 编码结果中的加号（+）替换成连接号（-）
     * 2、将编码结果中的正斜线（/）替换成下划线（_）
     * 3、保留编码结果中末尾的全部等号（=）
     *
     * @param content 原始字符串
     * @return URL安全的base64编码字符串
     */
    public static String getSecurityBase64(String content) throws CosXmlClientException {
        String base64 = getBase64(content);
        if(TextUtils.isEmpty(base64)){
            return base64;
        }

        base64 = base64.replace("+","-");
        base64 = base64.replace("/","_");
        return base64;
    }
}
