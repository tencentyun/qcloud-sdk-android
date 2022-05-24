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

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

public class QCloudHttpUtils {
    // 对非 '/' 字符进行URLEncoder编码
    public static String urlEncodeWithSlash(String fileId) {

        if (fileId != null && fileId.length() > 0 && !fileId.equals("/")) {
            String[] paras = fileId.split("/");
            for (int i = 0; i < paras.length; i++) {
                paras[i] = urlEncodeString(paras[i]);
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < paras.length; i++) {
                stringBuilder.append(paras[i]);
                stringBuilder.append("/");
            }
            if (!fileId.endsWith("/")) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            fileId = stringBuilder.toString();
        }

        return fileId;
    }

    public static Map<String, List<String>> getDecodedQueryPair(URL url) {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
        if (url.getQuery() != null) {
            final String[] pairs = url.getQuery().split("&");
            for (String pair : pairs) {
                final int idx = pair.indexOf("=");
                final String key = idx > 0 ? urlDecodeString(pair.substring(0, idx)) : pair;
                if (!query_pairs.containsKey(key)) {
                    query_pairs.put(key, new LinkedList<String>());
                }
                final String value = idx > 0 && pair.length() > idx + 1 ? urlDecodeString(pair.substring(idx + 1)) : null;
                query_pairs.get(key).add(value);
            }
        }
        return query_pairs;
    }

    public static Map<String, List<String>> getQueryPair(URL url) {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
        if (url.getQuery() != null) {
            final String[] pairs = url.getQuery().split("&");
            for (String pair : pairs) {
                final int idx = pair.indexOf("=");
                final String key = idx > 0 ? pair.substring(0, idx) : pair;
                if (!query_pairs.containsKey(key)) {
                    query_pairs.put(key, new LinkedList<String>());
                }
                final String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;
                query_pairs.get(key).add(value);
            }
        }
        return query_pairs;
    }

    public static Map<String, List<String>> transformToMultiMap(Map<String, String> map) {
        final Map<String, List<String>> multiMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            List<String> valueList = new ArrayList<>(1);
            valueList.add(entry.getValue());
            multiMap.put(entry.getKey(), valueList);
        }

        return multiMap;
    }

    public static long[] parseContentRange(String contentRange) {
        if (QCloudStringUtils.isEmpty(contentRange)) {
            return null;
        }
        int lastBlankIndex = contentRange.lastIndexOf(" ");
        int acrossIndex = contentRange.indexOf("-");
        int slashIndex = contentRange.indexOf("/");
        if (lastBlankIndex == -1 || acrossIndex == -1 || slashIndex == -1) {
            return null;
        }

        long start = Long.parseLong(contentRange.substring(lastBlankIndex + 1, acrossIndex));
        long end = Long.parseLong(contentRange.substring(acrossIndex + 1, slashIndex));
        long max = Long.parseLong(contentRange.substring(slashIndex + 1));

        return new long[] {start, end, max};
    }

    public static String urlEncodeString(String source) {
        try {
            if(TextUtils.isEmpty(source))return source;
            StringBuilder encoded = new StringBuilder();
            String[] spaceSegments = source.split(" ", -1);
            for(int i = 0, size = spaceSegments.length; i < size;  i++){
                if(i == 0 && "".equals(spaceSegments[i])){
                    encoded.append("%20");
                    continue;
                }
                if(size > 1 && i == size -1 && "".equals(spaceSegments[i])){
                    break;
                }
                encoded.append(URLEncoder.encode(spaceSegments[i], "UTF-8"));
                if(i != size -1)encoded.append("%20");
            }
            // cos 后台需要对 * 做转义，而标准的 Java encode 不需要
            return encoded.toString().replaceAll("\\*", "%2A");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String urlDecodeString(String source) {
        try {
            return URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String queryParametersString(Map<String, String> keyValues) {

        if (keyValues == null) {
            return null;
        }
        StringBuilder source = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {

            if (!first) {
                source.append("&");
            }
            source.append(entry.getKey()+"="+entry.getValue());
            first = false;
        }
        return source.toString();
    }

    public static boolean isNetworkConditionException(Throwable ex) {
        return ex instanceof UnknownHostException ||
                ex instanceof SocketTimeoutException ||
                ex instanceof ConnectException ||
                ex instanceof HttpRetryException ||
                ex instanceof NoRouteToHostException ||
                (ex instanceof SSLHandshakeException && !(ex.getCause() instanceof CertificateException));
    }
}
