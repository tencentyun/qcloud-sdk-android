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

package com.tencent.qcloud.core.auth;

import android.text.TextUtils;

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConfiguration;
import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.QCloudHttpRequest;
import com.tencent.qcloud.core.util.QCloudHttpUtils;
import com.tencent.qcloud.core.util.QCloudStringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.tencent.qcloud.core.http.HttpConstants.Header.CONTENT_LENGTH;
import static com.tencent.qcloud.core.http.HttpConstants.Header.CONTENT_TYPE;
import static com.tencent.qcloud.core.http.HttpConstants.Header.DATE;
import static com.tencent.qcloud.core.http.HttpConstants.Header.TRANSFER_ENCODING;

/**
 * 提供COS请求中参与签名的字段
 * <p>
 * 具体请参考：<a herf="https://cloud.tencent.com/document/product/436/7778#.E7.AD.BE.E5.90.8D.E6.AD.A5.E9.AA.A4">签名步骤</a>中的 步骤6：生成 StringToSign
 */

public class COSXmlSignSourceProvider implements QCloudSignSourceProvider {

    private Set<String> parametersRequiredToSign;
    private Set<String> parametersSigned;

    private Set<String> headerKeysRequiredToSign;
    private Set<String> headerKeysSigned;
    private Map<String, List<String>> headerPairs;

    private String signTime;

    public COSXmlSignSourceProvider() {
        headerKeysRequiredToSign = new HashSet<>();
        parametersRequiredToSign = new HashSet<>();
        headerKeysSigned = new HashSet<>();
        parametersSigned = new HashSet<>();
    }

    public void parameter(String key) {
        parametersRequiredToSign.add(key);
    }

    public void parameters(Set<String> keys) {
        if(keys != null){
            parametersRequiredToSign.addAll(keys);
        }
    }

    public void header(String key) {
        headerKeysRequiredToSign.add(key);
    }

    public void headers(Set<String> keys) {
        if(keys != null){
            headerKeysRequiredToSign.addAll(keys);
        }
    }

    /**
     * 设置签名使用的 headers 参数，默认读取的是 {@link QCloudHttpRequest#headers()}
     *
     * @param headerPairs 键值对，签名使用的 headers 参数
     */
    public void setHeaderPairsForSign(Map<String, List<String>> headerPairs) {
        this.headerPairs = headerPairs;
    }

    void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    @Override
    public <T> void onSignRequestSuccess(HttpRequest<T> request, QCloudCredentials credentials,
                                         String authorization) {

    }

    @Override
    public <T> String source(HttpRequest<T> request) throws QCloudClientException {
        if (request == null) {
            return null;
        }

        List<String> keysToSign = new LinkedList<>(request.headers().keySet());
        // 强制签名如下 OkHttp 自动添加的 Header
        keysToSign.add(CONTENT_TYPE);
        keysToSign.add(CONTENT_LENGTH);
        keysToSign.add(DATE);

        // 默认头部字段参与计算
        if (headerKeysRequiredToSign.size() < 1) {
            for (String headerKey : keysToSign) {

                if (request.getNoSignHeaders().contains(headerKey)) {
                    continue;
                }
                headerKeysRequiredToSign.add(headerKey);
//                if (HttpConstants.Header.CONTENT_MD5.equalsIgnoreCase(headerKey) ||
//                        HttpConstants.Header.CONTENT_DISPOSITION.equalsIgnoreCase(headerKey) ||
//                        HttpConstants.Header.CONTENT_ENCODING.equalsIgnoreCase(headerKey) ||
//                        (headerKey.startsWith("x-cos-") && !COSXmlSigner.COS_SESSION_TOKEN.equals(headerKey))) {
//                    headerKeysRequiredToSign.add(headerKey);
//                }
                //
            }
        }

        // 默认URL参数字段参与计算
        if (parametersRequiredToSign.size() < 1) {
            Map<String, List<String>> queryNameValues = QCloudHttpUtils.getQueryPair(request.url());
            parametersRequiredToSign.addAll(queryNameValues.keySet());
        }

        // 填充必要的头部信息
        if (headerKeysRequiredToSign.size() > 0) {
            Set<String> lowerCaseHeaders = toLowerCase(headerKeysRequiredToSign);

            // 1、是否存在Content-Type
            if (lowerCaseHeaders != null && lowerCaseHeaders.contains(CONTENT_TYPE.toLowerCase()) && request.getRequestBody() != null) {
                String contentType = request.contentType();
                if (contentType != null) {
                    request.addHeader(CONTENT_TYPE, contentType);
                }
            }

            // 2、是否存在Content-Length
            if (lowerCaseHeaders != null && lowerCaseHeaders.contains(CONTENT_LENGTH.toLowerCase()) && request.getRequestBody() != null) {
                long contentLength;
                try {
                    contentLength = request.contentLength();
                } catch (IOException e) {
                    throw new QCloudClientException("read content length fails", e);
                }
                if (contentLength != -1) {
                    request.addHeader(CONTENT_LENGTH, Long.toString(contentLength));
                    request.removeHeader(TRANSFER_ENCODING);
                } else {
                    request.addHeader(TRANSFER_ENCODING, "chunked");
                    request.removeHeader(CONTENT_LENGTH);
                }

            }

            // 3、是否存在Date
            if (lowerCaseHeaders != null && lowerCaseHeaders.contains(DATE.toLowerCase())) {
                request.addHeader(DATE, HttpConfiguration.getGMTDate(new Date()));
            }
        }

        // 添加method
        StringBuilder formatString = new StringBuilder(request.method().toLowerCase());
        formatString.append("\n");

        // 添加path
        String path = QCloudHttpUtils.urlDecodeString(request.url().getPath());
        formatString.append(path);
        formatString.append("\n");

        // 添加parameters
        String paraString = queryStringForKeys(request.url(), parametersRequiredToSign, parametersSigned);

        formatString.append(paraString);
        formatString.append("\n");

        // 添加header，得到最终的formatString
        String headerString = "";
        headerPairs = headerPairs != null ? headerPairs : request.headers();
        if (headerPairs != null) {
            headerString = headersStringForKeys(headerPairs, headerKeysRequiredToSign, headerKeysSigned);
        }
        formatString.append(headerString);
        formatString.append("\n");

        StringBuilder stringToSign = new StringBuilder();

        // 追加 q-sign-algorithm
        stringToSign.append(AuthConstants.SHA1);
        stringToSign.append("\n");

        // 追加q-sign-time
        stringToSign.append(signTime);
        stringToSign.append("\n");

        // 追加 sha1Hash(formatString)
        String formatStringSha1 = Utils.encodeHexString(Utils.sha1(formatString.toString()));
        stringToSign.append(formatStringSha1);
        stringToSign.append("\n");

        return stringToSign.toString();
    }

    String getRealHeaderList() {
        return sortAndJoinSemicolon(headerKeysSigned);
    }

    String getRealParameterList() {
        return sortAndJoinSemicolon(parametersSigned);
    }

    private String sortAndJoinSemicolon(Set<String> values) {
        if (values == null) {
            return "";
        }

        // 这里也需要先按字典顺序进行排序
        Set<String> set = new TreeSet<>(values);

        StringBuilder str  = new StringBuilder();
        for (String value : set) {
            if (!QCloudStringUtils.isEmpty(str.toString())) {
                str.append(";");
            }
            str.append(value);
        }

        return str.toString();
    }

    /**
     * 将set中所有的值转化为小写
     *
     * @param set 原始集合
     * @return 小写的集合
     */
    private Set<String> toLowerCase(Set<String> set) {
        if (set != null && set.size() > 0) {
            Set<String> lowerSet = new HashSet<>();
            for (String key : set) {
                if (key != null) {
                    lowerSet.add(key.toLowerCase());
                }
            }
            return lowerSet;
        }

        return null;
    }

    private String queryStringForKeys(URL httpUrl, Set<String> keys, Set<String> realKeys) {
        StringBuilder out = new StringBuilder();
        boolean isFirst = true;

        // 1、将所有的key值转化为小写，并进行排序
        List<String> orderKeys = new LinkedList<>();
        for (String key : keys) {
            orderKeys.add(key.toLowerCase());
        }
        Collections.sort(orderKeys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        // 2、获得query所有的name，并进行小写映射
        Map<String, List<String>> queryNameValues = QCloudHttpUtils.getDecodedQueryPair(httpUrl);
        Set<String> queryNames = queryNameValues.keySet();
        Map<String, String> maps = new HashMap<>();
        for (String name : queryNames) {
            maps.put(name.toLowerCase(), name);
        }

        // 3、取出需要的参数
        for (String key : orderKeys) {
            List<String> values = queryNameValues.get(maps.get(key));
            if (values != null) {
                for (String value : values) {
                    if (!isFirst) {
                        out.append('&');
                    }
                    isFirst = false;
                    realKeys.add(key.toLowerCase());
                    out.append(key.toLowerCase()).append('=');
                    if (!TextUtils.isEmpty(value)) {
                        out.append(QCloudHttpUtils.urlEncodeString(value));
                    }
                }
            }
        }
        return out.toString();
    }

    private String headersStringForKeys(Map<String, List<String>> headers, Set<String> keys, Set<String> realKeys) {
        StringBuilder out = new StringBuilder();
        boolean isFirst = true;

        // 1、将所有的key值转化为小写，并进行排序
        List<String> orderKeys = new LinkedList<>();
        for (String key : keys) {
            orderKeys.add(key.toLowerCase());
        }
        Collections.sort(orderKeys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        // 2、获得headers所有的name，并进行小写映射
        Set<String> headerNames = headers.keySet();
        Map<String, String> maps = new HashMap<>();
        for (String name : headerNames) {
            maps.put(name.toLowerCase(), name);
        }

        // 3、取出需要的参数
        for (String key : orderKeys) {
            List<String> values = headers.get(maps.get(key));
            if (values != null) {
                for (String value : values) {
                    if (!isFirst) {
                        out.append('&');
                    }
                    isFirst = false;
                    realKeys.add(key.toLowerCase());
                    out.append(key.toLowerCase()).append('=');
                    if (!TextUtils.isEmpty(value)) {
                        out.append(QCloudHttpUtils.urlEncodeString(value));
                    }
                }
            }
        }

        return out.toString();
    }
}
