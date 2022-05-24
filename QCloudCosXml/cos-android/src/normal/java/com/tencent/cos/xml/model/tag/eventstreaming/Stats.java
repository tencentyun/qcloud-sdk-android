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


public class Stats {

    private Long bytesScanned;
    private Long bytesProcessed;
    private Long bytesReturned;

    public Stats(Long bytesScanned, Long bytesProcessed, Long bytesReturned) {
        this.bytesScanned = bytesScanned;
        this.bytesProcessed = bytesProcessed;
        this.bytesReturned = bytesReturned;
    }

    /**
     * Total number of object bytes scanned.
     */
    public Long getBytesScanned() {
        return bytesScanned;
    }

    /**
     * Total number of object bytes scanned.
     */
    public void setBytesScanned(Long bytesScanned) {
        this.bytesScanned = bytesScanned;
    }

    /**
     * Total number of object bytes scanned.
     */
    public Stats withBytesScanned(Long readBytes) {
        setBytesScanned(readBytes);
        return this;
    }

    /**
     * Total number of bytes of records payload data returned.
     */
    public Long getBytesReturned() {
        return bytesReturned;
    }

    /**
     * Total number of bytes of records payload data returned.
     */
    public void setBytesReturned(Long bytesReturned) {
        this.bytesReturned = bytesReturned;
    }

    /**
     * Total number of bytes of records payload data returned.
     */
    public Stats withBytesReturned(Long bytesReturned) {
        setBytesReturned(bytesReturned);
        return this;
    }

    /**
     * Total number of uncompressed object bytes processed.
     */
    public Long getBytesProcessed() {
        return bytesProcessed;
    }

    /**
     * Total number of uncompressed object bytes processed.
     */
    public void setBytesProcessed(Long bytesProcessed) {
        this.bytesProcessed = bytesProcessed;
    }

    /**
     * Total number of uncompressed object bytes processed.
     */
    public Stats withBytesProcessed(Long processedBytes) {
        setBytesProcessed(processedBytes);
        return this;
    }
}
