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

/**
 * 是否需要返回查询进度 QueryProgress 信息，如果开启 COS Select 将周期性返回查询进度
 */
public class RequestProgress {

    private Boolean enabled;

    public RequestProgress(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Specifies whether periodic {@link SelectObjectContentEvent.ProgressEvent}s should be sent.
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Specifies whether periodic {@link SelectObjectContentEvent.ProgressEvent}s should be sent.
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Specifies whether periodic {@link SelectObjectContentEvent.ProgressEvent}s should be sent.
     */
    public RequestProgress withEnabled(Boolean enabled) {
        setEnabled(enabled);
        return this;
    }
}
