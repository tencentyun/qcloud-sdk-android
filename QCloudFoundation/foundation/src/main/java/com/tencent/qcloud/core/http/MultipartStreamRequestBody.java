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

import android.content.ContentResolver;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.tencent.qcloud.core.common.QCloudDigistListener;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.util.OkhttpInternalUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class MultipartStreamRequestBody extends RequestBody implements ProgressBody, QCloudDigistListener, ReactiveBody {
    private Map<String, String> bodyParameters = new LinkedHashMap<>();
    private String name;
    private String fileName;
    StreamingRequestBody streamingRequestBody;
    MultipartBody multipartBody;

    public MultipartStreamRequestBody(){
    }

    public void setBodyParameters(Map<String, String> bodyParameters){
        if (bodyParameters != null){
            this.bodyParameters.putAll(bodyParameters);
        }
    }

    public void setContent(String contentType, String name, String fileName, File file, long offset, long counts){
        if(name != null){
            this.name = name;
        }
        this.fileName = fileName;
        if (TextUtils.isEmpty(contentType)) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getPath());
            contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        streamingRequestBody = ExStreamingRequestBody.file(file, contentType, offset, counts);
    }

    public void setContent(String contentType, String name, String fileName, byte[] bytes, long offset, long counts){
        if(name != null){
            this.name = name;
        }
        this.fileName = fileName;
        streamingRequestBody = ExStreamingRequestBody.bytes(bytes, contentType, offset, counts);
    }

    public void setContent(String contentType, String name, String fileName, File tmpFile, InputStream stream, long offset, long counts) throws IOException {
        if(name != null){
            this.name = name;
        }
        this.fileName = fileName;
        streamingRequestBody = ExStreamingRequestBody.steam(stream, tmpFile, contentType, offset, counts);
    }

    public void setSign(String sign){
        if(sign != null){
            bodyParameters.put("Signature", sign);
        }
    }

    public void addMd5() throws IOException {
        try {
            bodyParameters.put(HttpConstants.Header.CONTENT_MD5, onGetMd5());
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public void prepare() {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MediaType.parse(HttpConstants.ContentType.MULTIPART_FORM_DATA));
        for (Map.Entry<String, String> entry : bodyParameters.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        builder.addFormDataPart(name, fileName, streamingRequestBody);
        multipartBody = builder.build();
    }

    @Override
    public <T> void end(HttpResult<T> httpResult) throws IOException {

    }

    @Override
    public void setProgressListener(QCloudProgressListener progressListener) {
        if(streamingRequestBody != null){
            streamingRequestBody.setProgressListener(progressListener);
        }
    }

    @Override
    public long getBytesTransferred() {
        return streamingRequestBody != null ? streamingRequestBody.getBytesTransferred() : 0;
    }

    @Override
    public MediaType contentType() {
        return multipartBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return multipartBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        multipartBody.writeTo(sink);
    }

    @Override
    public String onGetMd5() throws IOException {
        if(streamingRequestBody != null){
            String md5 = streamingRequestBody.onGetMd5();
            bodyParameters.put(HttpConstants.Header.CONTENT_MD5,md5);
            return md5;
        }
        return null;
    }

    private static class ExStreamingRequestBody extends StreamingRequestBody{

        protected ExStreamingRequestBody() {
            super();
        }

        static StreamingRequestBody file(File file, String contentType) {
            return file(file, contentType, 0, Long.MAX_VALUE);
        }

        static StreamingRequestBody file(File file, String contentType, long offset, long length) {
            StreamingRequestBody requestBody = new ExStreamingRequestBody();
            requestBody.file = file;
            requestBody.contentType = contentType;
            requestBody.offset = offset < 0 ? 0 : offset;
            requestBody.requiredLength = length;

            return requestBody;
        }

        static StreamingRequestBody bytes(byte[] bytes, String contentType, long offset, long length) {
            StreamingRequestBody requestBody = new ExStreamingRequestBody();
            requestBody.bytes = bytes;
            requestBody.contentType = contentType;
            requestBody.offset = offset < 0 ? 0 : offset;
            requestBody.requiredLength = length;

            return requestBody;
        }

        static StreamingRequestBody steam(InputStream inputStream, File tmpFile, String contentType,
                                          long offset, long length) {
            StreamingRequestBody requestBody = new ExStreamingRequestBody();
            requestBody.stream = inputStream;
            requestBody.contentType = contentType;
            requestBody.file = tmpFile;
            requestBody.offset = offset < 0 ? 0 : offset;
            requestBody.requiredLength = length;

            return requestBody;
        }

        static StreamingRequestBody url(URL url, String contentType,
                                        long offset, long length) {
            StreamingRequestBody requestBody = new ExStreamingRequestBody();
            requestBody.url = url;
            requestBody.contentType = contentType;
            requestBody.offset = offset < 0 ? 0 : offset;
            requestBody.requiredLength = length;
            return requestBody;
        }

        static StreamingRequestBody uri(Uri uri, ContentResolver contentResolver, String contentType,
                                        long offset, long length) {
            StreamingRequestBody requestBody = new ExStreamingRequestBody();
            requestBody.uri = uri;
            requestBody.contentResolver = contentResolver;
            requestBody.contentType = contentType;
            requestBody.offset = offset < 0 ? 0 : offset;
            requestBody.requiredLength = length;
            return requestBody;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            InputStream inputStream = null;
            BufferedSource source = null;
            try {
                inputStream = getStream();
                if (inputStream != null) {
                    source = Okio.buffer(Okio.source(inputStream));
                    long contentLength = contentLength();
                    countingSink = new CountingSink(sink, contentLength, progressListener);
                    BufferedSink bufferedSink = Okio.buffer(countingSink);
                    if (contentLength > 0) {
                        bufferedSink.write(source, contentLength);
                    } else {
                        bufferedSink.writeAll(source);
                    }
                    bufferedSink.flush();
                }
            } finally {

               if(inputStream != null) OkhttpInternalUtils.closeQuietly(inputStream);
               if(source != null) OkhttpInternalUtils.closeQuietly(source);
            }
        }
    }
}
