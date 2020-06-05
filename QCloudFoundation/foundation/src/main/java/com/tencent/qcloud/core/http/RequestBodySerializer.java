package com.tencent.qcloud.core.http;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.ByteString;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public abstract class RequestBodySerializer{
    /**
     * 将请求体转换成 ResponseBody
     *
     * @return ResponseBody
     */
    public abstract RequestBody body();

    private static final class BaseRequestBodyWrapper extends RequestBodySerializer {

        private final RequestBody body;

        public BaseRequestBodyWrapper(RequestBody body) {
            this.body = body;
        }

        @Override
        public RequestBody body() {
            return body;
        }
    }

    private static MediaType parseType(String contentType) {
        if (contentType != null) {
            return MediaType.parse(contentType);
        } else {
            return null;
        }
    }

    public static RequestBodySerializer wrap(RequestBody body) {
        return new BaseRequestBodyWrapper(body);
    }


    /**
     * Returns a new request body that transmits {@code content}. If {@code contentType} is non-null
     * and lacks a charset, this will use UTF-8.
     */
    public static RequestBodySerializer string(String contentType, String content) {
        return new BaseRequestBodyWrapper(RequestBody.create(parseType(contentType), content));
    }

    /** Returns a new request body that transmits {@code content}. */
    public static RequestBodySerializer string(String contentType, ByteString content) {
        return new BaseRequestBodyWrapper(RequestBody.create(parseType(contentType), content));
    }

    /** Returns a new request body that transmits {@code content}. */
    public static RequestBodySerializer bytes(final String contentType, final byte[] content) {
        return bytes(contentType, content,  0L, -1);
    }

    public static RequestBodySerializer bytes(final String contentType, final byte[] content,
                                               final long offset, final long byteCount) {
        long contentLength = byteCount < 0 ? content.length - offset : Math.min(byteCount, content.length - offset);
        if (contentLength < 1024 * 200) { // 200KB
            // small content
            return new BaseRequestBodyWrapper(RequestBody.create(parseType(contentType), content));
        } else {
            // large content
            return new BaseRequestBodyWrapper(StreamingRequestBody.bytes(content, contentType, offset, byteCount));
        }
    }

    public static RequestBodySerializer file(String contentType, File file) {
        return file(contentType, file,  0L, -1);
    }

    public static RequestBodySerializer file(String contentType, File file, long offset, long length) {
        if (TextUtils.isEmpty(contentType)) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getPath());
            contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        StreamingRequestBody fileRequestBody = StreamingRequestBody.file(file, contentType, offset, length);

        return new BaseRequestBodyWrapper(fileRequestBody);
    }

    public static RequestBodySerializer url(String contentType, URL url) {
        return url(contentType, url,  0L, -1);
    }

    public static RequestBodySerializer url(String contentType, URL url, long offset, long length) {
        if (TextUtils.isEmpty(contentType)) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(url.toString());
            contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        StreamingRequestBody fileRequestBody = StreamingRequestBody.url(url, contentType, offset, length);

        return new BaseRequestBodyWrapper(fileRequestBody);
    }

    public static RequestBodySerializer uri(String contentType, Uri uri, Context context) {
        return uri(contentType, uri, context, 0L, -1);
    }

    public static RequestBodySerializer uri(String contentType, Uri uri, Context context, long offset, long length) {
        ContentResolver contentResolver = context.getContentResolver();
        if (TextUtils.isEmpty(contentType)) {
            contentType = contentResolver.getType(uri);
        }
        StreamingRequestBody fileRequestBody = StreamingRequestBody.uri(uri, contentResolver, contentType, offset, length);

        return new BaseRequestBodyWrapper(fileRequestBody);
    }

    public static RequestBodySerializer stream(String contentType, File tmpFile, InputStream inputStream) {
        return stream(contentType, tmpFile, inputStream, 0L, -1);
    }

    public static RequestBodySerializer stream(String contentType, File tmpFile, InputStream inputStream,
                                               long offset, long length) {
        StreamingRequestBody requestBody = StreamingRequestBody.steam(inputStream, tmpFile, contentType,
                offset, length);

        return new BaseRequestBodyWrapper(requestBody);
    }

    public  static RequestBodySerializer multiPart(MultipartStreamRequestBody multipartStreamRequestBody) {
        return new BaseRequestBodyWrapper(multipartStreamRequestBody);
    }

}
