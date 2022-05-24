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

package com.tencent.cos.xml.model.tag.eventstreaming;


public class Progress {

    private Long bytesScanned;
    private Long bytesReturned;
    private Long bytesProcessed;

    public Progress(Long bytesScanned, Long bytesReturned, Long bytesProcessed) {
        this.bytesScanned = bytesScanned;
        this.bytesReturned = bytesReturned;
        this.bytesProcessed = bytesProcessed;
    }

    /**
     * Current number of object bytes scanned.
     */
    public Long getBytesScanned() {
        return bytesScanned;
    }

    /**
     * Current number of object bytes scanned.
     */
    public void setBytesScanned(Long bytesScanned) {
        this.bytesScanned = bytesScanned;
    }

    /**
     * Current number of object bytes scanned.
     */
    public Progress withBytesScanned(Long readBytes) {
        setBytesScanned(readBytes);
        return this;
    }

    /**
     * Current number of bytes of records payload data returned.
     */
    public Long getBytesReturned() {
        return bytesReturned;
    }

    /**
     * Current number of bytes of records payload data returned.
     */
    public void setBytesReturned(Long bytesReturned) {
        this.bytesReturned = bytesReturned;
    }

    /**
     * Current number of bytes of records payload data returned.
     */
    public Progress withBytesReturned(Long bytesReturned) {
        setBytesReturned(bytesReturned);
        return this;
    }

    /**
     * Current number of uncompressed object bytes processed.
     */
    public Long getBytesProcessed() {
        return bytesProcessed;
    }

    /**
     * Current number of uncompressed object bytes processed.
     */
    public void setBytesProcessed(Long bytesProcessed) {
        this.bytesProcessed = bytesProcessed;
    }

    /**
     * Current number of uncompressed object bytes processed.
     */
    public Progress withBytesProcessed(Long processedBytes) {
        setBytesProcessed(processedBytes);
        return this;
    }
}
