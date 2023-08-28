package com.tencent.qcloud.core.http;

import com.tencent.qcloud.core.util.OkhttpInternalUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * <p>
 * Created by rickenwang on 2020/7/17.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class OkHttpLoggingUtils {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static void logResponse(Response response, long tookMs, HttpLoggingInterceptor.Level level, HttpLoggingInterceptor.Logger logger) {

        boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS;

        ResponseBody responseBody = response.body();
        boolean hasResponseBody = responseBody != null;

        long contentLength = hasResponseBody ? responseBody.contentLength() : 0;
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logger.logResponse(response, "<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", "
                + bodySize + " body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.logResponse(response, headers.name(i) + ": " + headers.value(i));
            }

            if (!logBody || !OkhttpInternalUtils.hasBody(response) || !hasResponseBody || isContentLengthTooLarge(contentLength)) {
                logger.logResponse(response, "<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                logger.logResponse(response, "<-- END HTTP (encoded body omitted)");
            } else {
                try {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        try {
                            charset = contentType.charset(UTF8);
                        } catch (UnsupportedCharsetException e) {
                            logger.logResponse(response, "");
                            logger.logResponse(response, "Couldn't decode the response body; charset is likely malformed.");
                            logger.logResponse(response, "<-- END HTTP");

                            return ;
                        }
                    }

                    if (!isPlaintext(buffer)) {
                        logger.logResponse(response, "");
                        logger.logResponse(response, "<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                        return ;
                    }

                    if (contentLength != 0) {
                        logger.logResponse(response, "");
                        logger.logResponse(response, buffer.clone().readString(charset));
                    }

                    logger.logResponse(response, "<-- END HTTP (" + buffer.size() + "-byte body)");
                } catch (Exception e) {
                    // we don't want logger crash.
                    logger.logResponse(response, "<-- END HTTP");
                }
            }
        }
    }

    public static void logQuicRequestHeaders(Map<String, String> headers, HttpLoggingInterceptor.Logger logger) {

        if (headers == null) {
            return;
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            logger.logRequest(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void logMessage(String message, HttpLoggingInterceptor.Logger logger) {
        logger.logRequest(message);
    }


    public static void logRequest(Request request, Protocol protocol, HttpLoggingInterceptor.Level level, HttpLoggingInterceptor.Logger logger) throws IOException {

        boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logger.logRequest(requestStartMessage);

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logger.logRequest("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    logger.logRequest("Content-Length: " + requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logger.logRequest(name + ": " + headers.value(i));
                }
            }

            if (!logBody || !hasRequestBody || isContentLengthTooLarge(requestBody.contentLength())) {
                logger.logRequest("--> END " + request.method());
            } else if (bodyEncoded(request.headers())) {
                logger.logRequest("--> END " + request.method() + " (encoded body omitted)");
            } else {
                try {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);

                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }

                    logger.logRequest("");
                    if (isPlaintext(buffer)) {
                        logger.logRequest(buffer.readString(charset));
                        logger.logRequest("--> END " + request.method()
                                + " (" + requestBody.contentLength() + "-byte body)");
                    } else {
                        logger.logRequest("--> END " + request.method() + " (binary "
                                + requestBody.contentLength() + "-byte body omitted)");
                    }
                } catch (Exception e) {
                    // we don't want logger crash.
                    logger.logRequest("--> END " + request.method());
                }
            }
        }

    }

    private static boolean isContentLengthTooLarge(long contentLength) {
        return contentLength > 2 * 1024;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
