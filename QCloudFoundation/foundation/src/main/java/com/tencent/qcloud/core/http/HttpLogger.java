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

import com.tencent.qcloud.core.logger.FileLogAdapter;
import com.tencent.qcloud.core.logger.QCloudLogger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class HttpLogger implements HttpLoggingInterceptor.Logger {

    private boolean debuggable;
    private FileLogAdapter fileLogAdapter;
    private List<String> mRequestBufferLogs;

    private String tag;

    public HttpLogger(boolean debuggable, String tag) {

        this.debuggable = debuggable;
        this.tag = tag;
        mRequestBufferLogs = new ArrayList<>(10);
    }

    HttpLogger(boolean debuggable) {
        this(debuggable, QCloudHttpClient.HTTP_LOG_TAG);
    }

    public void setDebug(boolean debuggable) {
        this.debuggable = debuggable;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void logRequest(String message) {
        if (debuggable) {
            QCloudLogger.i(tag, message);
        }
        fileLogAdapter = QCloudLogger.getAdapter(FileLogAdapter.class);
        if (fileLogAdapter != null) {
            synchronized (mRequestBufferLogs){
                mRequestBufferLogs.add(message);
            }
        }
    }

    @Override
    public void logResponse(Response response, String message) {
        if (debuggable) {
            QCloudLogger.i(tag, message);
        }
        if (fileLogAdapter != null && response != null && !response.isSuccessful()) {
            flushRequestBufferLogs();
            fileLogAdapter.log(QCloudLogger.INFO, tag,
                    message, null);
        } else {
            synchronized (mRequestBufferLogs){
                mRequestBufferLogs.clear();
            }
        }
    }

    @Override
    public void logException(Exception exception, String message) {
        QCloudLogger.i(tag, message);
        if (fileLogAdapter != null && exception != null) {
            flushRequestBufferLogs();
            fileLogAdapter.log(QCloudLogger.INFO, tag,
                    message, exception);
        } else {
            synchronized (mRequestBufferLogs){
                mRequestBufferLogs.clear();
            }
        }
    }

    private synchronized void flushRequestBufferLogs() {
        synchronized (mRequestBufferLogs){
            if (fileLogAdapter != null && mRequestBufferLogs.size() > 0) {
                for (String requestLog : mRequestBufferLogs) {
                    fileLogAdapter.log(QCloudLogger.INFO, tag,
                            requestLog, null);
                }
                mRequestBufferLogs.clear();
            }
        }
    }
}
