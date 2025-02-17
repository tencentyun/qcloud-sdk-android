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

import com.tencent.qcloud.core.common.QCloudDigistListener;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.util.Base64Utils;
import com.tencent.qcloud.core.util.OkhttpInternalUtils;
import com.tencent.qcloud.core.util.QCloudUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
public class StreamingRequestBody extends RequestBody implements ProgressBody, QCloudDigistListener {

    protected File file;
    protected byte[] bytes;
    protected InputStream stream;
    protected URL url;
    protected Uri uri;
    protected ContentResolver contentResolver;

    protected long offset = 0;
    protected long requiredLength = -1;
    protected long contentRawLength = -1;

    protected String contentType;

    protected QCloudProgressListener progressListener;

    protected CountingSink countingSink;

    private boolean deleteFileWhenComplete = false;

    public void setProgressListener(QCloudProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public QCloudProgressListener getProgressListener(){
        return progressListener;
    }

    protected StreamingRequestBody() {
    }

    @Override
    public long getBytesTransferred() {
        return countingSink != null ? countingSink.getTotalTransferred() : 0;
    }

    static StreamingRequestBody file(File file, String contentType) {
        return file(file, contentType, 0, Long.MAX_VALUE);
    }

    static StreamingRequestBody file(File file, String contentType, long offset, long length) {
        StreamingRequestBody requestBody = new StreamingRequestBody();
        requestBody.file = file;
        requestBody.contentType = contentType;
        requestBody.offset = offset < 0 ? 0 : offset;
        requestBody.requiredLength = length;

        return requestBody;
    }

    static StreamingRequestBody bytes(byte[] bytes, String contentType, long offset, long length) {
        StreamingRequestBody requestBody = new StreamingRequestBody();
        requestBody.bytes = bytes;
        requestBody.contentType = contentType;
        requestBody.offset = offset < 0 ? 0 : offset;
        requestBody.requiredLength = length;

        return requestBody;
    }

    static StreamingRequestBody steam(InputStream inputStream, File tmpFile, String contentType,
                                      long offset, long length) {
        StreamingRequestBody requestBody = new StreamingRequestBody();
        requestBody.stream = inputStream;
        requestBody.contentType = contentType;
        requestBody.file = tmpFile;
        requestBody.offset = offset < 0 ? 0 : offset;
        requestBody.requiredLength = length;
        requestBody.deleteFileWhenComplete = true;
        return requestBody;
    }

    static StreamingRequestBody url(URL url, String contentType,
                                    long offset, long length) {
        StreamingRequestBody requestBody = new StreamingRequestBody();
        requestBody.url = url;
        requestBody.contentType = contentType;
        requestBody.offset = offset < 0 ? 0 : offset;
        requestBody.requiredLength = length;
        return requestBody;
    }

    static StreamingRequestBody uri(Uri uri, ContentResolver contentResolver, String contentType,
                                    long offset, long length) {
        StreamingRequestBody requestBody = new StreamingRequestBody();
        requestBody.uri = uri;
        requestBody.contentResolver = contentResolver;
        requestBody.contentType = contentType;
        requestBody.offset = offset < 0 ? 0 : offset;
        requestBody.requiredLength = length;
        return requestBody;
    }

    boolean isLargeData() {
        return file != null || uri != null || stream != null;
    }

    @Override
    public MediaType contentType() {
        if (contentType != null) {
            return MediaType.parse(contentType);
        } else {
            return null;
        }
    }

    @Override
    public long contentLength() throws IOException {
        long contentMaxLength = getContentRawLength();
        long contentLength;
        if (contentMaxLength <= 0) {
            contentLength = Math.max(requiredLength, -1);
        } else if (requiredLength <= 0) {
            contentLength = Math.max(contentMaxLength - offset, -1);
        } else {
            contentLength = Math.min(contentMaxLength - offset, requiredLength);
        }

        // 此处考虑文件被修改，大小发生变化，contentMaxLength - offset为负数的情况，会导致400 Bad Request
        if(contentLength < 0){
            contentLength = -1;
        }
        return contentLength;
    }

    protected long getContentRawLength() throws IOException {
        if (contentRawLength < 0) {
            if (stream != null) {
                contentRawLength = stream.available();
            } else if (file != null) {
                contentRawLength = file.length();
            } else if (bytes != null) {
                contentRawLength = bytes.length;
            } else if (uri != null) {
                contentRawLength = QCloudUtils.getUriContentLength(uri, contentResolver);
            }
        }

        return contentRawLength;
    }

    public InputStream getStream() throws IOException {
        InputStream inputStream = null;
        if (bytes != null) {
            inputStream = new ByteArrayInputStream(bytes);
        } else if (stream != null) {
            try {
                saveInputStreamToTmpFile(stream, file);
            } finally {
                if(stream != null) OkhttpInternalUtils.closeQuietly(stream);
                stream = null;
                offset = 0;
            }
            inputStream = new FileInputStream(file);
        } else if (file != null) {
            inputStream = new FileInputStream(file);
        } else if (url != null) {
            URLConnection conn = url.openConnection();
            if(offset > 0) {
                conn.setRequestProperty("Range", "bytes=" + offset + "-" + offset + requiredLength);
            }
            inputStream = url.openStream();
        } else if (uri != null) {
            inputStream = contentResolver.openInputStream(uri);

        }
        if(url == null && inputStream != null && offset > 0) {
            long skiped = inputStream.skip(offset);
            if (skiped < offset) {
                QCloudLogger.w(QCloudHttpClient.HTTP_LOG_TAG, "skip  %d is small than offset %d",
                        skiped, offset);
            }
        }
        return inputStream;
    }

    protected void saveInputStreamToTmpFile(InputStream stream, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            long bytesTotal = 0;
            long bytesLimit = contentLength();
            if (bytesLimit < 0) {
                bytesLimit = Long.MAX_VALUE;
            }
            if (offset > 0) {
                long skip = stream.skip(offset);
            }
            while (bytesTotal < bytesLimit && (bytesRead = stream.read(buffer)) != -1) {
                fos.write(buffer, 0, (int) Math.min(bytesRead, bytesLimit - bytesTotal));
                bytesTotal += bytesRead;
            }
            fos.flush();
        } finally {
            if(fos != null) OkhttpInternalUtils.closeQuietly(fos);
        }
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

    @Override
    public String onGetMd5() throws IOException {
        InputStream inputStream = null;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            if(bytes != null){
                messageDigest.update(bytes, (int) offset, (int) contentLength());
                return Base64Utils.encode(messageDigest.digest());
            }
            inputStream = getStream();//已经 skip
            byte[] buff = new byte[8 * 1024];
            int readLen;
            long remainLength = contentLength();
            while (remainLength > 0L && (readLen = inputStream.read(buff, 0,
                    (buff.length > remainLength ? (int) remainLength : buff.length)))!= -1){
                messageDigest.update(buff, 0, readLen);
                remainLength -= readLen;
            }
            return Base64Utils.encode(messageDigest.digest());
        } catch (IOException e) {
            throw e;
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("unSupport Md5 algorithm", e);
        } finally {
            if(inputStream != null)OkhttpInternalUtils.closeQuietly(inputStream);
        }
    }

    //
    public void release() {
        if (deleteFileWhenComplete) {
            if (file != null) {
                file.delete();
            }
        }

    }
}
