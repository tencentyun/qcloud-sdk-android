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

import com.tencent.qcloud.core.logger.QCloudLogger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class CallMetricsListener extends EventListener {

    private long dnsStartTime;
    private long dnsLookupTookTime;

    private long connectStartTime;
    private long connectTookTime;

    private long secureConnectStartTime;
    private long secureConnectTookTime;

    private long writeRequestHeaderStartTime;
    private long writeRequestHeaderTookTime;

    private long writeRequestBodyStartTime;
    private long writeRequestBodyTookTime;

    private long readResponseHeaderStartTime;
    private long readResponseHeaderTookTime;

    private long readResponseBodyStartTime;
    private long readResponseBodyTookTime;

    private String domainName;
    private List<InetAddress> inetAddressList;

    public CallMetricsListener(Call call) {

    }

    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        dnsStartTime = System.nanoTime();
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        StringBuffer ipList = new StringBuffer("{");
        if(inetAddressList != null){
            for(InetAddress inetAddress : inetAddressList){
                ipList.append(inetAddress.getHostAddress()).append(",");
            }
        }
        ipList.append("}");
        QCloudLogger.i(QCloudHttpClient.HTTP_LOG_TAG, "dns: " + domainName + ":" + ipList.toString());
        dnsLookupTookTime += System.nanoTime() - dnsStartTime;
        this.domainName = domainName;
        this.inetAddressList = inetAddressList;
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        connectStartTime = System.nanoTime();
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        connectTookTime += System.nanoTime() - connectStartTime;
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        connectTookTime += System.nanoTime() - connectStartTime;
    }

    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        secureConnectStartTime = System.nanoTime();
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        secureConnectTookTime += System.nanoTime() - secureConnectStartTime;
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        writeRequestHeaderStartTime = System.nanoTime();
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        writeRequestHeaderTookTime += System.nanoTime() - writeRequestHeaderStartTime;
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        writeRequestBodyStartTime = System.nanoTime();
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        writeRequestBodyTookTime += System.nanoTime() - writeRequestBodyStartTime;
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        readResponseHeaderStartTime = System.nanoTime();
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        readResponseHeaderTookTime += System.nanoTime() - readResponseHeaderStartTime;
    }

    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        readResponseBodyStartTime = System.nanoTime();
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        readResponseBodyTookTime += System.nanoTime() - readResponseBodyStartTime;
    }

    public void dumpMetrics(HttpTaskMetrics metrics) {
        metrics.domainName = domainName;
        metrics.remoteAddress = inetAddressList;
        metrics.dnsLookupTookTime += dnsLookupTookTime;
        metrics.connectTookTime += connectTookTime;
        metrics.secureConnectTookTime += secureConnectTookTime;
        metrics.writeRequestHeaderTookTime += writeRequestHeaderTookTime;
        metrics.writeRequestBodyTookTime += writeRequestBodyTookTime;
        metrics.readResponseHeaderTookTime += readResponseHeaderTookTime;
        metrics.readResponseBodyTookTime += readResponseBodyTookTime;
    }

    public List<InetAddress> dumpDns() {
        return inetAddressList;
    }
}
