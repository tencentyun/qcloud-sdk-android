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

import androidx.annotation.NonNull;

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

    private long dnsStartTimestamp;
    private long dnsStartTime;
    private long dnsLookupTookTime;

    private long connectStartTimestamp;
    private long connectStartTime;
    private long connectTookTime;

    private long secureConnectStartTimestamp;
    private long secureConnectStartTime;
    private long secureConnectTookTime;

    private long writeRequestHeaderStartTimestamp;
    private long writeRequestHeaderStartTime;
    private long writeRequestHeaderTookTime;

    private long writeRequestBodyStartTimestamp;
    private long writeRequestBodyStartTime;
    private long writeRequestBodyTookTime;

    private long readResponseHeaderStartTimestamp;
    private long readResponseHeaderStartTime;
    private long readResponseHeaderTookTime;

    private long readResponseBodyStartTimestamp;
    private long readResponseBodyStartTime;
    private long readResponseBodyTookTime;

    private List<InetAddress> dnsInetAddressList;
    private InetAddress connectAddress;

    private long requestBodyByteCount;
    private long responseBodyByteCount;

    public CallMetricsListener(Call call) {

    }

    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        dnsStartTime = System.nanoTime();
        dnsStartTimestamp = System.currentTimeMillis();
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
        this.dnsInetAddressList = inetAddressList;
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        connectStartTime = System.nanoTime();
        connectStartTimestamp = System.currentTimeMillis();
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        connectTookTime += System.nanoTime() - connectStartTime;
        connectAddress = inetSocketAddress.getAddress();
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        connectTookTime += System.nanoTime() - connectStartTime;
        connectAddress = inetSocketAddress.getAddress();
    }

    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        secureConnectStartTime = System.nanoTime();
        secureConnectStartTimestamp = System.currentTimeMillis();
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
        writeRequestHeaderStartTimestamp = System.currentTimeMillis();
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
        writeRequestBodyStartTimestamp = System.currentTimeMillis();
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        writeRequestBodyTookTime += System.nanoTime() - writeRequestBodyStartTime;
        requestBodyByteCount = byteCount;
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        readResponseHeaderStartTime = System.nanoTime();
        readResponseHeaderStartTimestamp = System.currentTimeMillis();
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
        readResponseBodyStartTimestamp = System.currentTimeMillis();
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        readResponseBodyTookTime += System.nanoTime() - readResponseBodyStartTime;
        responseBodyByteCount = byteCount;
    }

    public void dumpMetrics(HttpTaskMetrics metrics) {
        metrics.recordConnectAddress(connectAddress);
        metrics.remoteAddress = dnsInetAddressList;
        metrics.dnsStartTimestamp += dnsStartTimestamp;
        metrics.dnsLookupTookTime += dnsLookupTookTime;
        metrics.connectStartTimestamp += connectStartTimestamp;
        metrics.connectTookTime += connectTookTime;
        metrics.secureConnectStartTimestamp += secureConnectStartTimestamp;
        metrics.secureConnectTookTime += secureConnectTookTime;
        metrics.writeRequestHeaderStartTimestamp += writeRequestHeaderStartTimestamp;
        metrics.writeRequestHeaderTookTime += writeRequestHeaderTookTime;
        metrics.writeRequestBodyStartTimestamp += writeRequestBodyStartTimestamp;
        metrics.writeRequestBodyTookTime += writeRequestBodyTookTime;
        metrics.readResponseHeaderStartTimestamp += readResponseHeaderStartTimestamp;
        metrics.readResponseHeaderTookTime += readResponseHeaderTookTime;
        metrics.readResponseBodyStartTimestamp += readResponseBodyStartTimestamp;
        metrics.readResponseBodyTookTime += readResponseBodyTookTime;
        metrics.requestBodyByteCount = requestBodyByteCount;
        metrics.responseBodyByteCount = responseBodyByteCount;
    }

    public List<InetAddress> dumpDns() {
        return dnsInetAddressList;
    }

    @NonNull
    @Override
    public String toString() {
        return "CallMetricsListener{" +
                "dnsStartTimestamp=" + dnsStartTimestamp +
                ", dnsLookupTookTime=" + dnsLookupTookTime +
                ", connectTimestamp=" + connectStartTimestamp +
                ", connectTookTime=" + connectTookTime +
                ", secureConnectTimestamp=" + secureConnectStartTimestamp +
                ", secureConnectTookTime=" + secureConnectTookTime +
                ", writeRequestHeaderTimestamp=" + writeRequestHeaderStartTimestamp +
                ", writeRequestHeaderTookTime=" + writeRequestHeaderTookTime +
                ", writeRequestBodyTimestamp=" + writeRequestBodyStartTimestamp +
                ", writeRequestBodyTookTime=" + writeRequestBodyTookTime +
                ", readResponseHeaderTimestamp=" + readResponseHeaderStartTimestamp +
                ", readResponseHeaderTookTime=" + readResponseHeaderTookTime +
                ", readResponseBodyTimestamp=" + readResponseBodyStartTimestamp +
                ", readResponseBodyTookTime=" + readResponseBodyTookTime +
                ", inetAddressList=" + dnsInetAddressList +
                ", connectAddress=" + connectAddress +
                ", requestBodyByteCount=" + requestBodyByteCount +
                ", responseBodyByteCount=" + responseBodyByteCount +
                '}';
    }
}
