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

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.util.QCloudHttpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;


public class ResponseFileConverter<T> extends ResponseBodyConverter<T> implements ProgressBody {

    private String filePath;
    private Uri contentUri;
    private ContentResolver contentResolver;

    private long offset;

    protected boolean isQuic = false;

    protected QCloudProgressListener progressListener;

    private CountingSink countingSink;

    private InputStream inputStream;

    public ResponseFileConverter(String filePath, long offset) {
        this.filePath = filePath;
        this.offset = offset;
    }

    public ResponseFileConverter(Uri contentUri, ContentResolver contentResolver, long offset) {
        this.contentUri = contentUri;
        this.contentResolver = contentResolver;
        this.offset = offset;
    }

    public ResponseFileConverter() {
    }

    @Override
    public void setProgressListener(QCloudProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public void enableQuic(boolean isQuic){
        this.isQuic = isQuic;
    }

    public QCloudProgressListener getProgressListener(){
        return progressListener;
    }

    @Override
    public T convert(HttpResponse<T> response) throws QCloudClientException, QCloudServiceException {
        if(isQuic) return null;
        HttpResponse.checkResponseSuccessful(response);

        String contentRangeString = response.header(HttpConstants.Header.CONTENT_RANGE);
        long[] contentRange = QCloudHttpUtils.parseContentRange(contentRangeString);
        long contentLength;
        if (contentRange != null) {
            //206
            contentLength = contentRange[1] - contentRange[0] + 1;
        } else {
            //200
            contentLength = response.contentLength();
        }

        if (!TextUtils.isEmpty(filePath)) {
            return downloadToAbsolutePath(response, contentLength);
        } else if (contentUri != null) {
            return pipeToContentUri(response, contentLength);
        }

        throw new QCloudClientException(new IllegalArgumentException("filePath or ContentUri are both null"));
    }

    private T pipeToContentUri(HttpResponse<T> response, long contentLength)
            throws QCloudClientException, QCloudServiceException {
        OutputStream output = getOutputStream();
        InputStream input = response.byteStream();

        byte[] buffer = new byte[8192];
        countingSink = new CountingSink(new Buffer(), contentLength, progressListener);
        int len;
        try {
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
                countingSink.writeBytesInternal(len);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            throw new QCloudClientException("write local uri error for " + e.toString(), e);
        } finally {
            if(output != null) {
                Util.closeQuietly(output);
            }
        }
    }


    private T downloadToAbsolutePath(HttpResponse<T> response, long contentLength)
            throws QCloudClientException, QCloudServiceException {
        File downloadFilePath = new File(filePath);
        File parentDir = downloadFilePath.getParentFile();
        if(parentDir != null && !parentDir.exists() && !parentDir.mkdirs()){
            throw new QCloudClientException(new IOException("local file directory can not create."));
        }

        ResponseBody body = response.response.body();
        if (body == null) {
            throw new QCloudServiceException("response body is empty !");
        }
        try {
            writeRandomAccessFile(downloadFilePath, response.byteStream(), contentLength);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            throw new QCloudClientException("write local file error for " + e.toString(), e);
        }
    }

    private void writeRandomAccessFile(File downloadFilePath, InputStream inputStream, long contentLength)
            throws IOException, QCloudClientException {
        if (inputStream == null) {
            throw new QCloudClientException(new IOException("response body stream is null"));
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(downloadFilePath, "rws");
            //获取上一次已传输的数据长度
            long lastTimeBytes = getBytesTransferred();
            //seek值需要加上lastTimeBytes，因为重试后上一次的lastTimeBytes数据已经下载到文件中
            if(offset + lastTimeBytes > 0) randomAccessFile.seek(offset + lastTimeBytes);
            byte[] buffer = new byte[8192];
            countingSink = new CountingSink(new Buffer(), contentLength, lastTimeBytes, progressListener);
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, len);
                countingSink.writeBytesInternal(len);
            }
        } finally {
            if(randomAccessFile != null) Util.closeQuietly(randomAccessFile);
        }
    }

    public OutputStream getOutputStream() throws QCloudClientException {
        if (!TextUtils.isEmpty(filePath)) {
            File downloadFilePath = new File(filePath);
            File parentDir = downloadFilePath.getParentFile();
            if(parentDir != null && !parentDir.exists() && !parentDir.mkdirs()){
                throw new QCloudClientException(new IOException("local file directory can not create."));
            }
            try {
                return new FileOutputStream(downloadFilePath);
            } catch (FileNotFoundException e) {
                throw new QCloudClientException(e);
            }
        } else if (contentUri != null){
            try {
                return contentResolver.openOutputStream(contentUri);
            } catch (FileNotFoundException e) {
                throw new QCloudClientException(e);
            }
        }  else {
            throw new QCloudClientException(new IllegalArgumentException("filePath or ContentUri are both null"));
        }
    }

    @Override
    public long getBytesTransferred() {
        return countingSink != null ? countingSink.getTotalTransferred() : 0;
    }

    /**
     * 是否是文件路径转换器
     * @return 是否是文件路径转换器
     */
    public boolean isFilePathConverter(){
        return !TextUtils.isEmpty(filePath);
    }
}
