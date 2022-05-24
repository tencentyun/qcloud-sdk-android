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

package com.tencent.cos.xml.transfer;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;

/**
 * 传输监听接口
 */
public interface TransferListener {
    /**
     * Called when the state of the transfer is changed.
     *
     * @param id The id of the transfer record.
     * @param state The new state of the transfer.
     */
    void onStateChanged(String id, TransferState state);

    /**
     * Called when more bytes are transferred.
     *
     * @param id The id of the transfer record.
     * @param bytesCurrent Bytes transferred currently.
     * @param bytesTotal The total bytes to be transferred.
     */
    void onProgressChanged(String id, long bytesCurrent, long bytesTotal);

    /**
     * Called when an exception happens.
     *
     * @param id The id of the transfer record.
     * @param clientException An client exception object.
     * @param serviceException An Service exception object.
     */
    void onError(String id, CosXmlClientException clientException, CosXmlServiceException serviceException);
}

