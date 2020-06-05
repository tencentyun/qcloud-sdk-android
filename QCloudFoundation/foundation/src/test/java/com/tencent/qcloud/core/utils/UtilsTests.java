package com.tencent.qcloud.core.utils;

import com.tencent.qcloud.core.util.QCloudUtils;

import org.junit.Test;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UtilsTests {

    @Test
    public void testObjectSerializer() throws Exception {

        String host = "bucket.cos.ap-beijing.myqcloud.com";
        List<InetAddress> inetAddresses = new LinkedList<>();
        inetAddresses.add(InetAddress.getByAddress(host, new byte[]{1 ,2 ,3 ,4}));
        DnsRecord dnsRecord = new DnsRecord(host, inetAddresses);
        byte[] bytes = QCloudUtils.toBytes(dnsRecord);
        DnsRecord newDnsRecord = (DnsRecord) QCloudUtils.toObject(bytes);
        System.out.println(newDnsRecord.getHost());

    }
}