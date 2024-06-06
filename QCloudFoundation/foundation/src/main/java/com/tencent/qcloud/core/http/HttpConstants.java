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

package com.tencent.qcloud.core.http;

public final class HttpConstants {
    /**
     * 腾讯云COS服务响应标识: 接收请求并返回响应的服务器的名称
     */
    public static final String TENCENT_COS_SERVER = "tencent-cos";

    public static final class ContentType {
        
        public static final String TEXT_PLAIN = "text/plain";

        public final static String JSON = "application/json";

        public final static String XML = "application/xml";

        public final static String MULTIPART_FORM_DATA = "multipart/form-data";

        public static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    }

    public static final class Scheme {

        public final static String HTTP = "http";

        public final static String HTTPS = "https";
    }

    public static final class Header {

        public final static String AUTHORIZATION = "Authorization";

        public static final String USER_AGENT = "User-Agent";

        public final static String HOST = "Host";

        public final static String CONTENT_LENGTH = "Content-Length";

        public final static String CONTENT_DISPOSITION = "Content-Disposition";

        public final static String CONTENT_ENCODING = "Content-Encoding";

        public final static String TRANSFER_ENCODING = "Transfer-Encoding";

        public final static String CONTENT_TYPE = "Content-Type";

        public final static String CONTENT_MD5 = "Content-MD5";

        public final static String CONTENT_RANGE = "Content-Range";

        public final static String CONNECTION = "Connection";

        public final static String RANGE = "Range";

        public final static String DATE = "Date";

        public final static String EXPECT = "Expect";

        public final static String SERVER = "Server";

        public final static String ACCEPT = "Accept";
    }

    public static final class RequestMethod {

        public final static String GET = "GET";

        public final static String HEAD = "HEAD";

        public final static String PUT = "PUT";

        public final static String POST = "POST";

        public final static String TRACE = "TRACE";

        public final static String OPTIONS = "OPTIONS";

        public final static String DELETE = "DELETE";
    }
}
