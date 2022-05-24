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


import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 响应体转换器
 * @param <T>
 */
public abstract class ResponseBodyConverter<T> {

    /**
     * 转换方法
     * @param response  需要解析的ResponseBody
     *
     * @throws QCloudClientException 客户端异常
     * @throws QCloudServiceException 服务端异常
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

    /**
     * 获取文件转换器
     * @param filePath 文件路径
     * @return 文件转换器
     */
    public static ResponseBodyConverter<Void> file(String filePath) {
        return file(filePath, -1);
    }

    /**
     * 获取文件转换器
     * @param filePath 文件路径
     * @param offset 偏移量
     * @return 文件转换器
     */
    public static ResponseBodyConverter<Void> file(String filePath, long offset) {
        return new ResponseFileConverter<Void>(filePath, offset);
    }

    /**
     * 获取字符串转换器
     * @return 字符串转换器
     */
    public static ResponseBodyConverter<String> string() {
        return new StringConverter();
    }

    /**
     * 获取字节数组转换器
     * @return 字节数组转换器
     */
    public static ResponseBodyConverter<byte[]> bytes() {
        return new BytesConverter();
    }

    /**
     * 获取字节流转换器
     * @return 字节流转换器
     */
    public static ResponseBodyConverter<InputStream> inputStream() {
        return new InputStreamConverter();
    }
}
