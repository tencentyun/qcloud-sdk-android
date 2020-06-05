package com.tencent.qcloud.core.http;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
        DnsRepository.LocalDnsCache localDnsCache = new DnsRepository.LocalDnsCache(context);
        DnsRepository.DnsFetcher dnsFetcher = new DnsRepository.DnsFetcher();
        dnsFetcher.addHost(hostGuangzhou);
        Map<String, List<InetAddress>> fetchDnsRecords = dnsFetcher.fetchAll();
        Map<String, List<InetAddress>> loadDnsRecords = localDnsCache.loadFromLocal();
        localDnsCache.save2Local(fetchDnsRecords);
        loadDnsRecords = localDnsCache.loadFromLocal();

        Assert.assertTrue(fetchDnsRecords != null && !fetchDnsRecords.isEmpty());
        Assert.assertTrue(loadDnsRecords != null && !loadDnsRecords.isEmpty());
        Assert.assertEquals(fetchDnsRecords.size(), loadDnsRecords.size());
        Assert.assertEquals(fetchDnsRecords.get(hostGuangzhou).size(), loadDnsRecords.get(hostGuangzhou).size());


        DnsRepository dnsRepository = DnsRepository.getInstance();
        List<String> prefecthHosts = new LinkedList<>();
        prefecthHosts.add(hostBeijing);
        dnsRepository.addPrefetchHosts(prefecthHosts);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        dnsRepository.init(new DnsRepository.AsyncExecuteCompleteListener() {
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
            dnsRepository.getDnsRecord(hostGuangzhou);
            dnsRepository.getDnsRecord(hostBeijing);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            final CountDownLatch countDownLatch2 = new CountDownLatch(1);
            dnsRepository.insertDnsRecordCache(hostShanghai, dnsRepository.getDnsRecord(hostBeijing), new DnsRepository.AsyncExecuteCompleteListener() {
                @Override
                public void onComplete() {
                    countDownLatch2.countDown();
                }
            });
            countDownLatch2.await();
            dnsRepository.getDnsRecord(hostShanghai);
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
            DnsRepository.getInstance().insertDnsRecordCache(hostname, addresses);
            QCloudLogger.i(tag, "system lookup " + hostname);
            return addresses;
        } catch (UnknownHostException e) {
            // e.printStackTrace();
            QCloudLogger.w(tag, "system dns failed, retry cache dns records.");
        }
        QCloudLogger.i(tag, "dns cache " + hostname);
        return DnsRepository.getInstance().getDnsRecord(hostname);
    }

    private List<InetAddress> debugDns(String host) throws UnknownHostException {

        if (new Random().nextBoolean()) {
            return Dns.SYSTEM.lookup(host);
        } else {
            throw new UnknownHostException(host);
        }
    }
}
