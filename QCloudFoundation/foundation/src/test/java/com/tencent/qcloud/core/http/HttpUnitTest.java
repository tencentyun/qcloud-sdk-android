package com.tencent.qcloud.core.http;

import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudResultListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.task.RetryStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * <p>
 * </p>
 * Created by wjielai on 2018/4/27.
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class HttpUnitTest {

    private QCloudHttpClient httpClient = null;

    @Before
    public void setupContext() throws UnknownHostException {
        httpClient = new QCloudHttpClient.Builder()
                .setInheritBuilder(new OkHttpClient.Builder().eventListener(new EventListener() {
                    @Override
                    public void dnsStart(Call call, String domainName) {
                        super.dnsStart(call, domainName);
                        System.out.println("<<<<< dns start: " + domainName);
                    }

                    @Override
                    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
                        super.dnsEnd(call, domainName, inetAddressList);
                        System.out.println(">>>>> dns end: " + inetAddressList);
                    }

                    @Override
                    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
                        super.connectStart(call, inetSocketAddress, proxy);
                        System.out.println(">>>>> connect end: " + inetSocketAddress);
                    }

                    @Override
                    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
                        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
                        System.out.println(">>>>> connect failed: " + ioe);
                    }

                    @Override
                    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
                        super.connectEnd(call, inetSocketAddress, proxy, protocol);
                        System.out.println(">>>>> connect end: " + inetSocketAddress);
                    }
                }))
                .setRetryStrategy(RetryStrategy.FAIL_FAST)
                .build();
        httpClient.setDebuggable(true);
        httpClient.addDnsRecord("www.qq.com", new String[]{"59.37.96.63"});
    }

    @Test
    public void testRetry() throws Exception {
        final Object lock = new Object();

        final QCloudHttpRequest<String> request = new QCloudHttpRequest.Builder<String>()
                .scheme("http")
                .method("GET")
                .host("www.aaabbcc.com")
                .build();
        httpClient.resolveRequest(request)
                .schedule()
                .addResultListener(new QCloudResultListener<HttpResult<String>>() {
                    @Override
                    public void onSuccess(HttpResult<String> result) {
                        synchronized (lock) {
                            lock.notify();
                        }
                    }

                    @Override
                    public void onFailure(QCloudClientException clientException, QCloudServiceException serviceException) {
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                });

        synchronized (lock) {
            lock.wait();
        }
    }

    @Test
    public void testHttpUrl() {
        String url = "http://music.163.com/share/2095551/3510133339?a=1";
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl != null) {
            String url2 = httpUrl.newBuilder()
                    .addQueryParameter("_tac_mta_appkey", "2334")
                    .addQueryParameter("_tac_mta_channel", "2334")
                    .build().toString();
            System.out.println(url2);
        }
    }

    @Test(expected = QCloudClientException.class)
    public void testSimpleGet() throws MalformedURLException, QCloudClientException, QCloudServiceException {
        String url = "http://www.qq.com";
        HttpResult<String> result = httpClient.resolveRequest(new HttpRequest.Builder<String>()
                .url(new URL(url))
                .method("GET")
                .build())
                .executeNow();
    }

    @Test
    public void testPattern() {
        Pattern pattern = Pattern.compile("<Code>(RequestTimeTooSkewed|SignatureDoesNotMatch|ExpiredToken)</Code>");
        Matcher matcher = pattern.matcher("<Code>RequestTimeTooSkewed</Code>");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("RequestTimeTooSkewed", matcher.group(1));
    }
}
