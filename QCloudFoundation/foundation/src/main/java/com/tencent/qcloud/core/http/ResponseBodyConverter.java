package com.tencent.qcloud.core.http;


import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public abstract class ResponseBodyConverter<T> {

    /**
     *
     * @param response  需要解析的ResponseBody
     *
     * @throws QCloudClientException
     * @throws QCloudServiceException
     */
    public abstract T convert(HttpResponse<T> response) throws QCloudClientException, QCloudServiceException;

    private static final class StringConverter extends ResponseBodyConverter<String> {
        @Override
        public String convert(HttpResponse<String> response) throws QCloudClientException, QCloudServiceException {
            try {
                return response.string();
            } catch (IOException e) {
                throw new QCloudClientException(e);
            }
        }
    }

    private static final class BytesConverter extends ResponseBodyConverter<byte[]> {

        @Override
        public byte[] convert(HttpResponse<byte[]> response) throws QCloudClientException, QCloudServiceException {

            try {
                return response.bytes();
            } catch (IOException e) {
                throw new QCloudClientException(e);
            }
        }
    }

    private static final class InputStreamConverter extends ResponseBodyConverter<InputStream> {

        @Override
        public InputStream convert(HttpResponse<InputStream> response) throws QCloudClientException, QCloudServiceException {
            return response.byteStream();
        }
    }

    public static ResponseBodyConverter<Void> file(String filePath) {
        return file(filePath, -1);
    }

    public static ResponseBodyConverter<Void> file(String filePath, long offset) {
        return new ResponseFileConverter<Void>(filePath, offset);
    }

    public static ResponseBodyConverter<String> string() {
        return new StringConverter();
    }

    public static ResponseBodyConverter<byte[]> bytes() {
        return new BytesConverter();
    }

    public static ResponseBodyConverter<InputStream> inputStream() {
        return new InputStreamConverter();
    }
}
