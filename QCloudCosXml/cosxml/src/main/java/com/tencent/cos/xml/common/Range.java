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

package com.tencent.cos.xml.common;

/**
 * 请求头 Range: bytes=x-x
 * <ul>
 *     <li>表示头500个字节：bytes=0-499 </li>
 *     <li>表示第二个500字节：bytes=500-999</li>
 *     <li>表示500字节以后的范围：bytes=500- </li>
 *     <li>表示最后500个字节：bytes=-500 :暂不支持</li>
 * </ul>
 */
public class Range {
    private long start;
    private long end;
    public Range(long start, long end){
        this.start = start;
        this.end = end;
    }

    public Range(long start){
        this(start, -1);
    }

    public long getEnd() {
        return end;
    }

    public long getStart() {
        return start;
    }

    public String getRange(){
        return String.format("bytes=%s-%s", start, (end == -1 ?"": String.valueOf(end)));
    }

}
