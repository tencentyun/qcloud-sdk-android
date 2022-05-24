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
import com.tencent.qcloud.core.common.QCloudProgressListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.util.QCloudHttpUtils;
import java.io.InputStream;


public class ResponseInputStreamConverter<T> extends ResponseBodyConverter<T> implements ProgressBody {

    protected boolean isQuic = false;

    protected QCloudProgressListener progressListener;

    private CountingInputStream countingInputStream;

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
        countingInputStream = new CountingInputStream(response.byteStream(), contentLength, progressListener);
        return null;
    }

    @Override
    public long getBytesTransferred() {
        return countingInputStream != null ? countingInputStream.getTotalTransferred() : 0;
    }

    public InputStream getInputStream() {
        return countingInputStream;
    }

    public long getBytesTotal() {
        return countingInputStream.getBytesTotal();
    }
}
