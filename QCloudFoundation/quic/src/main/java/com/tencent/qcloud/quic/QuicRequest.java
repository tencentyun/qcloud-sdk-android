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

package com.tencent.qcloud.quic;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class QuicRequest {
    protected String url;
    protected String host;
    protected String ip;
    protected int port;
    protected int tcpPort = 80;

    protected Map<String, String> headers = new LinkedHashMap<>(); //有序

    protected RequestBody requestBody;

    public QuicRequest(String url, String host, String ip, int port, int tcpPort){
        this.url = url;
        this.host = host;
        this.ip = ip;
        this.port = port;
        this.tcpPort = tcpPort;
    }

    public void addHeader(String key, String value){
        headers.put(key, value);
    }

    public void setRequestBody(RequestBody requestBody){
        this.requestBody = requestBody;
    }

}
