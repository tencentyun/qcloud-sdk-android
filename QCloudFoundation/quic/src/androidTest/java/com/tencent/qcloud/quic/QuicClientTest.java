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

import com.tencent.qcloud.core.http.HttpRequest;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.core.http.HttpResult;
import com.tencent.qcloud.core.http.QCloudHttpClient;
import com.tencent.qcloud.core.http.ResponseFileConverter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

@RunWith(AndroidJUnit4.class)
public class QuicClientTest {

    private QCloudHttpClient quicHttpClient;
    private QuicClientImpl clientImpl;
    private Context context;


    @Before
    public void setup() {

        context = InstrumentationRegistry.getTargetContext();

        clientImpl = new QuicClientImpl();
        quicHttpClient = new QCloudHttpClient.Builder()
                .setNetworkClient(clientImpl)
                .enableDebugLog(true)
                .build();
    }

    @After
    public void teardown() {
        clientImpl.destroy();
    }

    @Test
    public void testClientGet() throws Exception {

        String fileName = "response-" + System.currentTimeMillis() + ".jpg";
        File output = new File(context.getExternalCacheDir(), fileName);

        try {
            HttpRequest<Void> httpRequest = new HttpRequest.Builder<Void>()
                    .method("GET")
                    .scheme("https")
                    .host("stgwhttp2.kof.qq.com")
                    .path("/1.jpg")
                    .converter(new ResponseFileConverter<Void>(output.getAbsolutePath(), 0))
                    .build();
            HttpResult result = quicHttpClient.resolveRequest(httpRequest).executeNow();
            Assert.assertEquals(200, result.code());
            List<String> headers = (List<String>) result.headers().get("content-length");
            Assert.assertEquals(1, headers.size());
            long contentLength = Long.parseLong(headers.get(0));
            Assert.assertEquals(output.length(), contentLength);
        } finally {
            output.delete();
        }

    }
}
