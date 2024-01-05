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

package com.tencent.qcloud.quic;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class QuicImplTest {

    private QuicManager quicManager;
    private Context context;


    @Before
    public void setup() {

        context = InstrumentationRegistry.getTargetContext();

        quicManager = new QuicManager(true);
    }

    @Test
    public void testGet() throws Exception {

        String host = "stgwhttp2.kof.qq.com";
        String ip = "";
        int port = 443;
        int tcpPort = 80;
        try {
            InetAddress[] inetAddresses = InetAddress.getAllByName(host);
            if (inetAddresses != null && inetAddresses.length > 0) {
                ip = inetAddresses[0].getHostAddress();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        QuicRequest quicRequest = new QuicRequest(host, host, ip, port, tcpPort);
        quicRequest.addHeader(":scheme", "https");
        quicRequest.addHeader(":path", "/1.jpg");
        quicRequest.addHeader(":method", "GET");

        String fileName = "response-" + System.currentTimeMillis() + ".jpg";
        File output = new File(context.getExternalCacheDir(), fileName);

        try {
            QuicImpl quic = quicManager.newQuicImpl(quicRequest);
            QuicResponse quicResponse = quic.call();
            Assert.assertEquals(200, quicResponse.code);
            if (quicResponse.buffer != null) {

                FileOutputStream outputStream = new FileOutputStream(output);
                byte[] data = new byte[4 * 1024];
                int len = quicResponse.buffer.read(data, 0, data.length);
                while (len != -1) {
                    outputStream.write(data, 0, len);
                    len = quicResponse.buffer.read(data, 0, data.length);
                }
                outputStream.flush();
                outputStream.close();

                long contentLength = Long.parseLong(quicResponse.headers.get("content-length"));
                Assert.assertEquals(output.length(), contentLength);


                for (Map.Entry<String, String> header : quicResponse.headers.entrySet()) {
                    Log.d("QuicTest", "(" + header.getKey() + "|" + header.getValue() + ")");
                }
            }
        } finally {
            output.delete();
        }
    }
}
