package com.tencent.qcloud.core.http;

import java.net.InetAddress;
import java.util.List;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/5/30.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class HttpTaskMetrics {

    private long fullTaskStartTime;
    private long fullTaskTookTime;

    private long calculateMD5StartTime;
    private long calculateMD5STookTime;

    private long signRequestStartTime;
    private long signRequestTookTime;

    long dnsLookupTookTime;

    long connectTookTime;

    long secureConnectTookTime;

    long writeRequestHeaderTookTime;

    long writeRequestBodyTookTime;

    long readResponseHeaderTookTime;

    long readResponseBodyTookTime;

    String domainName;
    List<InetAddress> remoteAddress;

    void onTaskStart() {
        fullTaskStartTime = System.nanoTime();
    }

    void onTaskEnd() {
        fullTaskTookTime = System.nanoTime() - fullTaskStartTime;
        onDataReady();
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
     * 获取任务整体耗时
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
    public List<InetAddress> getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * 获取服务器域名
     * @return 服务器域名
     */
    public String getDomainName() {
        return domainName;
    }

    private double toSeconds(long nanotime) {
        return (double)nanotime / 1_000_000_000.0;
    }

    /**
     * 数据准备完毕时的回调，你可以重载此方法，实现数据完毕后的回调
     */
    public void onDataReady() {

    }

    @Override
    public String toString() {
        return new StringBuilder().append("Http Metrics: \n")
                .append("fullTaskTookTime : ").append(fullTaskTookTime()).append("\n")
                .append("calculateMD5STookTime : ").append(calculateMD5STookTime()).append("\n")
                .append("signRequestTookTime : ").append(signRequestTookTime()).append("\n")
                .append("dnsLookupTookTime : ").append(dnsLookupTookTime()).append("\n")
                .append("connectTookTime : ").append(connectTookTime()).append("\n")
                .append("secureConnectTookTime : ").append(secureConnectTookTime()).append("\n")
                .append("writeRequestHeaderTookTime : ").append(writeRequestHeaderTookTime()).append("\n")
                .append("writeRequestBodyTookTime : ").append(writeRequestBodyTookTime()).append("\n")
                .append("readResponseHeaderTookTime : ").append(readResponseHeaderTookTime()).append("\n")
                .append("readResponseBodyTookTime : ").append(readResponseBodyTookTime())
                .toString();
    }
}
