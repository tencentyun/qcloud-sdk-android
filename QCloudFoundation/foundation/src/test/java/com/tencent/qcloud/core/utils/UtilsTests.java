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