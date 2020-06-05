package com.tencent.qcloud.core.http;


import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.util.QCloudHttpUtils;

import java.io.*;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;


/**
 * 解析下载的字节流，并保存为文本
 *
 *
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class ResponseFileConverter<T> extends ResponseBodyConverter<T> implements ProgressBody {

    private String filePath;
    private long offset;

    protected boolean isQuic = false;

    protected QCloudProgressListener progressListener;

    private CountingSink countingSink;

    public ResponseFileConverter(String filePath, long offset) {
        this.filePath = filePath;
        this.offset = offset;
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
            if(offset > 0) randomAccessFile.seek(offset);
            byte[] buffer = new byte[8192];
            countingSink = new CountingSink(new Buffer(), contentLength, progressListener);
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
        File downloadFilePath = new File(filePath);
        File parentDir = downloadFilePath.getParentFile();
        if(parentDir != null && !parentDir.exists() && !parentDir.mkdirs()){
            throw new QCloudClientException(new IOException("local file directory can not create."));
        }
        try {
            OutputStream outputStream = new FileOutputStream(downloadFilePath);
            return outputStream;
        } catch (FileNotFoundException e) {
           throw new QCloudClientException(e);
        }
    }

    @Override
    public long getBytesTransferred() {
        return countingSink != null ? countingSink.getTotalTransferred() : 0;
    }
}
