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

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.net.InetAddress;
import java.util.List;

public class HttpTaskMetrics {

    private long fullTaskStartTime;
    private long fullTaskTookTime;

    private long httpTaskStartTime;
    private long httpTaskTookTime;

    private long calculateMD5StartTime;
    private long calculateMD5STookTime;

    private long signRequestStartTime;
    private long signRequestTookTime;

    long dnsStartTimestamp;
    long dnsLookupTookTime;

    long connectStartTimestamp;
    long connectTookTime;

    long secureConnectStartTimestamp;
    long secureConnectTookTime;

    long writeRequestHeaderStartTimestamp;
    long writeRequestHeaderTookTime;

    long writeRequestBodyStartTimestamp;
    long writeRequestBodyTookTime;

    long readResponseHeaderStartTimestamp;
    long readResponseHeaderTookTime;

    long readResponseBodyStartTimestamp;
    long readResponseBodyTookTime;

    long requestBodyByteCount;
    long responseBodyByteCount;

    @Nullable String domainName;
    @Nullable List<InetAddress> remoteAddress;
    @Nullable InetAddress connectAddress;
    private int retryCount;
    private boolean isClockSkewedRetry;

    void onTaskStart() {
        fullTaskStartTime = System.nanoTime();
    }

    void onTaskEnd() {
        fullTaskTookTime = System.nanoTime() - fullTaskStartTime;
        onDataReady();
    }

    void onHttpTaskStart() {
        httpTaskStartTime = System.nanoTime();
    }

    void onHttpTaskEnd() {
        if(httpTaskStartTime != 0){
            httpTaskTookTime = System.nanoTime() - httpTaskStartTime;
        }
    }

    void onCalculateMD5Start() {
        calculateMD5StartTime = System.nanoTime();
    }

    void onCalculateMD5End() {
        calculateMD5STookTime += System.nanoTime() - calculateMD5StartTime;
    }

    void onSignRequestStart() {
        signRequestStartTime = System.nanoTime();
    }

    void onSignRequestEnd() {
        signRequestTookTime += System.nanoTime() - signRequestStartTime;
    }

    public long requestBodyByteCount() {
        return requestBodyByteCount;
    }

    public long responseBodyByteCount() {
        return responseBodyByteCount;
    }

    /**
     * 获取 http 请求执行时间
     *
     * @return http 请求执行时间，单位是秒
     */
    public double httpTaskFullTime() {
        return toSeconds(httpTaskTookTime);
    }

    /**
     * 获取 DNS 请求耗时
     *
     * @return DNS 请求耗时，单位是秒
     */
    public double dnsLookupTookTime() {
        return toSeconds(dnsLookupTookTime);
    }

    /**
     * 获取连接建立耗时
     *
     * @return 连接建立耗时，单位是秒
     */
    public double connectTookTime() {
        return toSeconds(connectTookTime);
    }

    /**
     * 获取 TLS 建立会话耗时
     *
     * @return TLS 会话耗时，单位是秒
     */
    public double secureConnectTookTime() {
        return toSeconds(secureConnectTookTime);
    }

    /**
     * 获取请求 BODY MD5 计算耗时
     *
     * @return 请求 BODY MD5 计算耗时，单位是秒
     */
    public double calculateMD5STookTime() {
        return toSeconds(calculateMD5STookTime);
    }

    /**
     * 获取签名计算耗时，包括获取密钥时间
     *
     * @return 签名计算耗时，单位是秒
     */
    public double signRequestTookTime() {
        return toSeconds(signRequestTookTime);
    }

    /**
     * 获取读取响应头部耗时
     *
     * @return 读取响应头部耗时，单位是秒
     */
    public double readResponseHeaderTookTime() {
        return toSeconds(readResponseHeaderTookTime);
    }

    /**
     * 获取读取响应体耗时
     *
     * @return 读取响应体耗时，单位是秒
     */
    public double readResponseBodyTookTime() {
        return toSeconds(readResponseBodyTookTime);
    }

    /**
     * 获取写请求头部耗时
     *
     * @return 写请求头部耗时，单位是秒
     */
    public double writeRequestBodyTookTime() {
        return toSeconds(writeRequestBodyTookTime);
    }

    /**
     * 获取写请求体耗时
     *
     * @return 写请求体耗时，单位是秒
     */
    public double writeRequestHeaderTookTime() {
        return toSeconds(writeRequestHeaderTookTime);
    }

    /**
     * 获取任务整体耗时，包括计算 md5 时间、计算签名时间和 http 请求时间
     *
     * @return 任务整体耗时，单位是秒
     */
    public double fullTaskTookTime() {
        return toSeconds(fullTaskTookTime);
    }

    /**
     * 获取服务器IP地址
     * @return 服务器IP地址列表
     */
    @Nullable public List<InetAddress> getRemoteAddress() {
        return remoteAddress;
    }

    @Nullable public InetAddress getConnectAddress() {
        return connectAddress;
    }

    /**
     * 获取服务器域名
     * @return 服务器域名
     */
    @Nullable public String getDomainName() {
        return domainName;
    }

    public void setDomainName(@Nullable String domainName) {
        this.domainName = domainName;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isClockSkewedRetry() {
        return isClockSkewedRetry;
    }

    public void setClockSkewedRetry(boolean clockSkewedRetry) {
        isClockSkewedRetry = clockSkewedRetry;
    }

    private double toSeconds(long nanotime) {
        return (double)nanotime / 1_000_000_000.0;
    }

    public static HttpTaskMetrics createMetricsWithHost(String host) {
        HttpTaskMetrics taskMetrics = new HttpTaskMetrics();
        taskMetrics.domainName = host;
        return taskMetrics;
    }

    public void recordConnectAddress(InetAddress address) {
        if (address != null) {
            // 耗时操作  要去掉
//            domainName = address.getHostName();
            connectAddress = address;
        }
    }

    /**
     * 数据准备完毕时的回调，你可以重载此方法，实现数据完毕后的回调
     */
    public void onDataReady() {

    }

    synchronized public HttpTaskMetrics merge(HttpTaskMetrics taskMetrics) {

        if (!TextUtils.isEmpty(domainName) && !TextUtils.isEmpty(taskMetrics.domainName)
            && !domainName.equals(taskMetrics.domainName)) {
            return this;
        }

        if (TextUtils.isEmpty(domainName) && taskMetrics.domainName != null) {
            domainName = taskMetrics.domainName;
        }

        dnsLookupTookTime = Math.max(taskMetrics.dnsLookupTookTime, dnsLookupTookTime);
        connectTookTime = Math.max(taskMetrics.connectTookTime, connectTookTime);
        secureConnectTookTime = Math.max(taskMetrics.secureConnectTookTime, secureConnectTookTime);
        writeRequestHeaderTookTime += taskMetrics.writeRequestHeaderTookTime;
        writeRequestBodyTookTime += taskMetrics.writeRequestBodyTookTime;
        readResponseHeaderTookTime += taskMetrics.readResponseHeaderTookTime;
        readResponseBodyTookTime += taskMetrics.readResponseBodyTookTime;
        requestBodyByteCount += taskMetrics.requestBodyByteCount;
        responseBodyByteCount += taskMetrics.responseBodyByteCount;
        fullTaskTookTime += taskMetrics.fullTaskTookTime;
        httpTaskTookTime += taskMetrics.httpTaskTookTime;
        calculateMD5STookTime += taskMetrics.calculateMD5STookTime;
        signRequestTookTime += taskMetrics.signRequestTookTime;
        if (taskMetrics.getRemoteAddress() != null) {
            remoteAddress = taskMetrics.getRemoteAddress();
        }
        if (taskMetrics.connectAddress != null) {
            connectAddress = taskMetrics.getConnectAddress();
        }
        retryCount += taskMetrics.retryCount;
        if(!isClockSkewedRetry){
            isClockSkewedRetry = taskMetrics.isClockSkewedRetry;
        }
        return this;
    }


    @Override
    public String toString() {
        return new StringBuilder().append("Http Metrics: \n")
                .append("domain : ").append(domainName).append("\n")
                .append("connectAddress : ").append(connectAddress != null ? connectAddress.getHostAddress() : "null").append("\n")
                .append("retryCount : ").append(retryCount).append("\n")
                .append("isClockSkewedRetry : ").append(isClockSkewedRetry).append("\n")
                .append("dns : ").append(remoteAddress != null ? remoteAddress : "null").append("\n")
                .append("fullTaskTookTime : ").append(fullTaskTookTime()).append("\n")
                .append("httpTaskTookTime : ").append(httpTaskFullTime()).append("\n")
                .append("calculateMD5STookTime : ").append(calculateMD5STookTime()).append("\n")
                .append("signRequestTookTime : ").append(signRequestTookTime()).append("\n")
                .append("dnsStartTimestamp : ").append(dnsStartTimestamp).append("\n")
                .append("dnsLookupTookTime : ").append(dnsLookupTookTime()).append("\n")
                .append("connectStartTimestamp : ").append(connectStartTimestamp).append("\n")
                .append("connectTookTime : ").append(connectTookTime()).append("\n")
                .append("secureConnectStartTimestamp : ").append(secureConnectStartTimestamp).append("\n")
                .append("secureConnectTookTime : ").append(secureConnectTookTime()).append("\n")
                .append("writeRequestHeaderStartTimestamp : ").append(writeRequestHeaderStartTimestamp).append("\n")
                .append("writeRequestHeaderTookTime : ").append(writeRequestHeaderTookTime()).append("\n")
                .append("writeRequestBodyStartTimestamp : ").append(writeRequestBodyStartTimestamp).append("\n")
                .append("writeRequestBodyTookTime : ").append(writeRequestBodyTookTime()).append("\n")
                .append("readResponseHeaderStartTimestamp : ").append(readResponseHeaderStartTimestamp).append("\n")
                .append("readResponseHeaderTookTime : ").append(readResponseHeaderTookTime()).append("\n")
                .append("readResponseBodyStartTimestamp : ").append(readResponseBodyStartTimestamp)
                .append("readResponseBodyTookTime : ").append(readResponseBodyTookTime())
                .toString();
    }
}
