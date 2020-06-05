package com.tencent.qcloud.core.http;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.tencent.qcloud.core.logger.QCloudLogger;
import com.tencent.qcloud.core.util.ContextHolder;
import com.tencent.qcloud.core.util.QCloudUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Dns;

/**
 * 管理 Dns 的预取和缓存
 */
public class DnsRepository {

    private Map<String, List<InetAddress>> dnsRecords;

    private static volatile DnsRepository instance;

    private LocalDnsCache localDnsCache;

    private DnsFetcher dnsFetcher;

    private Executor singleExecutor;

    public static DnsRepository getInstance() {

        if (instance == null) {
            synchronized (DnsRepository.class) {
                if (instance == null) {
                    instance = new DnsRepository();
                }
            }
        }
        return instance;
    }

    private DnsRepository() {

        localDnsCache = new LocalDnsCache(ContextHolder.getAppContext());
        dnsFetcher = new DnsFetcher();
        dnsRecords = new ConcurrentHashMap<>();
        singleExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * 添加预取 Host，cos 存储桶的 host 一般格式为 bucket.cos.region.myqcloud.com。
     * 添加后，会在调用 {@link DnsRepository#init()} 方法后进行预解析。
     *
     * @param hosts 需要预解析的 host 列表
     */
    public void addPrefetchHosts(List<String> hosts) {
         dnsFetcher.addHosts(hosts);
    }


    /**
     * 初始化，会拉取本地缓存和预取 DNS 到内存中
     */
    public void init() {
        init(null);
    }

    /**
     * 异步缓存 DNS 解析记录
     */
    public void insertDnsRecordCache(final String host, final List<InetAddress> inetAddresses) {
        insertDnsRecordCache(host, inetAddresses, null);
    }

    // public void deleteDnsRecordCache

    /**
     * 获取已经缓存的 DNS 记录
     * @param host
     * @return
     * @throws UnknownHostException
     */
    public List<InetAddress> getDnsRecord(String host) throws UnknownHostException {

        if (!dnsRecords.containsKey(host)) {
            throw new UnknownHostException(host);
        }
        return dnsRecords.get(host);
    }

    // private
    void init(final AsyncExecuteCompleteListener listener) {

        singleExecutor.execute(new Runnable() {
            @Override
            public void run() {
                addDnsRecordsMap(localDnsCache.loadFromLocal());
                addDnsRecordsMap(dnsFetcher.fetchAll());
                localDnsCache.save2Local(dnsRecords);
                if (listener != null) {
                    listener.onComplete();
                }
            }
        });
    }

    // private
    void insertDnsRecordCache(final String host, final List<InetAddress> inetAddresses, final AsyncExecuteCompleteListener listener) {

        singleExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<InetAddress> currentAddresses = dnsRecords.get(host);
                if (!sameInetAddresses(currentAddresses, inetAddresses)) {
                    dnsRecords.put(host, inetAddresses); // 若存在，直接 update
                    localDnsCache.save2Local(dnsRecords);
                }
                if (listener != null) {
                    listener.onComplete();
                }
            }
        });
    }

    private void addDnsRecordsMap(Map<String, List<InetAddress>> dnsRecords) {

        if (dnsRecords != null) {
            this.dnsRecords.putAll(dnsRecords);
        }
    }

    private boolean sameInetAddresses(List<InetAddress> inetAddresses1, List<InetAddress> inetAddresses2) {

        if (inetAddresses1 == null || inetAddresses2 == null || inetAddresses1.size() != inetAddresses2.size()) {
            return false;
        }

        for (int i = 0; i < inetAddresses1.size(); i++) {
            if (!inetAddresses1.get(i).getHostAddress()
                    .equals(inetAddresses2.get(i).getHostAddress())) {
                return false;
            }
        }
        return true;
    }



    static class LocalDnsCache {

        private String cacheFilePath;

        LocalDnsCache(Context context) {
            if (context != null) {
                cacheFilePath = context.getCacheDir().getAbsolutePath().concat("/cosSdkDnsCache.db");
            } else {

            }
        }

        void save2Local(Map<String, List<InetAddress>> dnsRecords) {

            if (cacheFilePath == null) {
                return;
            }

            byte[] bytes = QCloudUtils.toBytes(dnsRecords);
            QCloudUtils.writeToFile(cacheFilePath, bytes);
        }


        Map<String, List<InetAddress>> loadFromLocal() {

            if (cacheFilePath == null) {
                return null;
            }

            byte[] bytes = QCloudUtils.readBytesFromFile(cacheFilePath);

            if (bytes != null) {
                Object object = QCloudUtils.toObject(bytes);
                if (object instanceof Map) {
                    return (Map<String, List<InetAddress>>) object;
                }
            }
            return null;
        }

    }

    static class DnsFetcher {

        private int maxRetry = 2;

        private List<String> hosts;

        DnsFetcher() {
            hosts = new LinkedList<>();
        }

        void addHosts(List<String> hosts) {
            this.hosts.addAll(hosts);
        }

        void addHost(String host) {
            this.hosts.add(host);
        }

        Map<String, List<InetAddress>> fetchAll()  {

            Map<String, List<InetAddress>> dnsRecords = new HashMap<>();

            for (String host : hosts) {

                List<InetAddress> ips;
                if (!TextUtils.isEmpty(host) && ((ips = fetch(host, maxRetry)) != null)) {
                    dnsRecords.put(host, ips);
                }
            }

            return dnsRecords;
        }

        private List<InetAddress> fetch(String host, int maxRetry) {

            if (maxRetry < 0) {
                return null;
            }

            try {
                return Dns.SYSTEM.lookup(host);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return fetch(host, maxRetry - 1);
            }
        }
    }

    interface AsyncExecuteCompleteListener {

        void onComplete();
    }
}
