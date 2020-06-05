package com.tencent.qcloud.core.http;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public final class HttpConstants {

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
