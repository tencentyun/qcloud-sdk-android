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

import com.tencent.qcloud.core.util.QCloudHttpUtils;

import java.util.Map;

/**
 * 字符串工具类
 */
public class StringUtils {

    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'};
    public static String toHexString(byte[] data){
        StringBuilder result = new StringBuilder(data.length * 2);
        for(byte b : data){
            result.append(StringUtils.HEX_DIGITS[(b & 0xf0) >>> 4]);
            result.append(StringUtils.HEX_DIGITS[(b & 0x0f)]);
        }
        return result.toString();
    }

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }


    public static String flat(Map<String, String> paras) {

        if (paras == null) {
            return "";
        }

        StringBuilder flat = new StringBuilder();
        boolean isFirst = true;

        for (Map.Entry<String, String> entry : paras.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!isFirst) {
                flat.append("&");
            }
            isFirst = false;
            flat.append(key);
            if (!TextUtils.isEmpty(value)) {
                flat.append("=").append(QCloudHttpUtils.urlEncodeString(value));
            }
        }

        return flat.toString();
    }

    /**
     * 提取一个完整路径的文件名，比如：
     * <p>
     * resource/image/logo.png  -&gt; logo.png
     * <p>
     * resource/image/          -&gt; image/
     * <p>
     * resource/image           -&gt; image
     * <p>
     * image/                   -&gt; image/
     * <p>
     * image                    -&gt; image
     */
    public static String extractFileName(String absolutePath) {

        if (TextUtils.isEmpty(absolutePath)) {
            return "";
        }

        int lastSlashIndex = absolutePath.lastIndexOf('/');
        if (lastSlashIndex < 0) {
            return absolutePath;
        }

        if (lastSlashIndex == absolutePath.length() - 1) {
            lastSlashIndex = absolutePath.substring(0, absolutePath.length()-1).lastIndexOf('/');
        }
        return absolutePath.substring(lastSlashIndex + 1);
    }

    /**
     *  提取一个路径或者文件名中的名称（不含后缀），比如：
     * <p>
     *  resource/image/logo.png   -&gt; logo
     * <p>
     *  logo.png                  -&gt; logo
     * <p>
     *  logo                      -&gt; logo
     */
    public static String extractNameNoSuffix(String absolutePath) {
        String fileName = extractFileName(absolutePath);

        if (TextUtils.isEmpty(fileName)) {
            return "";
        }

        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return fileName.substring(0, lastIndexOfDot);
        } else {
            return fileName;
        }
    }

    /**
     *  提取一个路径或者文件名中的后缀，比如：
     * <p>
     *  resource/image/logo.png   -&gt; .png
     * <p>
     *  logo.png                  -&gt; .png
     * <p>
     *  logo                      -&gt;
     */
    public static String extractSuffix(String absolutePath) {
        if (TextUtils.isEmpty(absolutePath)) {
            return "";
        }

        if (absolutePath.endsWith("/")) {
            return "/";
        }

        int lastIndexOfDot = absolutePath.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return absolutePath.substring(lastIndexOfDot);
        }
        return "";
    }
}
