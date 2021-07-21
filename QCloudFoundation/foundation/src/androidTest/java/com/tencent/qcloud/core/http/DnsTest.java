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

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.util.ContextHolder;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import okhttp3.Dns;

@RunWith(AndroidJUnit4.class)
public class DnsTest {

    private final String tag = "DnsTest";

    private String hostGuangzhou = "cos.ap-guangzhou.myqcloud.com";
    private String hostBeijing = "cos.ap-beijing.myqcloud.com";
    private String hostShanghai = "cos.ap-shanghai.myqcloud.com";

    @BeforeClass public static void setUp() {

        Context context = InstrumentationRegistry.getContext();
        ContextHolder.setContext(context);
    }

    @Test
    public void testDNS() {

        Context context = InstrumentationRegistry.getContext();
        ConnectionRepository.LocalDnsCache localDnsCache = new ConnectionRepository.LocalDnsCache(context);
        ConnectionRepository.DnsFetcher dnsFetcher = new ConnectionRepository.DnsFetcher();
        dnsFetcher.addHost(hostGuangzhou);
        Map<String, List<InetAddress>> fetchDnsRecords = dnsFetcher.fetchAll();
        Map<String, List<InetAddress>> loadDnsRecords = localDnsCache.loadFromLocal();
        localDnsCache.save2Local(fetchDnsRecords);
        loadDnsRecords = localDnsCache.loadFromLocal();

        Assert.assertTrue(fetchDnsRecords != null && !fetchDnsRecords.isEmpty());
        Assert.assertTrue(loadDnsRecords != null && !loadDnsRecords.isEmpty());
        Assert.assertEquals(fetchDnsRecords.size(), loadDnsRecords.size());
        Assert.assertEquals(fetchDnsRecords.get(hostGuangzhou).size(), loadDnsRecords.get(hostGuangzhou).size());


        ConnectionRepository connectionRepository = ConnectionRepository.getInstance();
        List<String> prefecthHosts = new LinkedList<>();
        prefecthHosts.add(hostBeijing);
        connectionRepository.addPrefetchHosts(prefecthHosts);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        connectionRepository.init(new ConnectionRepository.AsyncExecuteCompleteListener() {
            @Override
            public void onComplete() {
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            connectionRepository.getDnsRecord(hostGuangzhou);
            connectionRepository.getDnsRecord(hostBeijing);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            final CountDownLatch countDownLatch2 = new CountDownLatch(1);
            connectionRepository.insertDnsRecordCache(hostShanghai, connectionRepository.getDnsRecord(hostBeijing), new ConnectionRepository.AsyncExecuteCompleteListener() {
                @Override
                public void onComplete() {
                    countDownLatch2.countDown();
                }
            });
            countDownLatch2.await();
            connectionRepository.getDnsRecord(hostShanghai);
        } catch (UnknownHostException | InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test public void testDnsLookUp() {

        int count = 100;
        int failedCount = 0;
        while (count-- > 0) {

            try {
                lookup(hostBeijing);
                Thread.sleep(1000);
            } catch (UnknownHostException | InterruptedException e) {
                e.printStackTrace();
                failedCount++;
                QCloudLogger.w(tag, "dns failed!!!");
            }
        }
        Assert.assertTrue(failedCount <= 1);
    }

    private List<InetAddress> lookup(String hostname) throws UnknownHostException {

        try {
            List<InetAddress> addresses = debugDns(hostname);
            ConnectionRepository.getInstance().insertDnsRecordCache(hostname, addresses);
            QCloudLogger.i(tag, "system lookup " + hostname);
            return addresses;
        } catch (UnknownHostException e) {
            // e.printStackTrace();
            QCloudLogger.w(tag, "system dns failed, retry cache dns records.");
        }
        QCloudLogger.i(tag, "dns cache " + hostname);
        return ConnectionRepository.getInstance().getDnsRecord(hostname);
    }

    private List<InetAddress> debugDns(String host) throws UnknownHostException {

        if (new Random().nextBoolean()) {
            return Dns.SYSTEM.lookup(host);
        } else {
            throw new UnknownHostException(host);
        }
    }
}
