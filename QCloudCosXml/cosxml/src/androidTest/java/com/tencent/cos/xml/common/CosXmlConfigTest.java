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

package com.tencent.cos.xml.common;

import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.qcloud.core.http.QCloudHttpRetryHandler;
import com.tencent.qcloud.core.task.RetryStrategy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executors;

import okhttp3.Request;
import okhttp3.Response;

@RunWith(AndroidJUnit4.class)
public class CosXmlConfigTest {
    @Test
    public void test() {
        CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .dnsCache(false)
                .addHeader("testkey", "testvalue")
                .addNoSignHeaders("testkey")
                .enableQuic(false)
                .setBucketInPath(false)
                .setExecutor(Executors.newFixedThreadPool(3))
                .setObserveExecutor(Executors.newFixedThreadPool(3))
                .setPathStyle(false)
                .setRetryStrategy(RetryStrategy.DEFAULT)
                .setRetryHandler(new QCloudHttpRetryHandler() {
                    @Override
                    public boolean shouldRetry(Request request, Response response, Exception exception) {
                        return false;
                    }
                })
                .setConnectionTimeout(3000)
                .setSocketTimeout(5000)
                .builder();


        CosXmlServiceConfig config1 = config.newBuilder().builder();

        Assert.assertEquals(config.getSocketTimeout(), config1.getSocketTimeout());

        String host = config.getHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.region.myqcloud.com", host);
        String host1 = config.getHost("bucket-1250000000", Region.AP_Beijing.getRegion(),false);
        Assert.assertEquals("bucket-1250000000.cos.ap-beijing.myqcloud.com", host1);
        String host2 = config.getHost("bucket-1250000000", Region.AP_Beijing.getRegion(),false, false);
        Assert.assertEquals("bucket-1250000000.cos.ap-beijing.myqcloud.com", host2);
        String host3 = config.getHost("bucket-1250000000", Region.AP_Beijing.getRegion(), "1250000000",false, false);
        Assert.assertEquals("bucket-1250000000.cos.ap-beijing.myqcloud.com", host3);
        String host4 = config.getHost("bucket-1250000000", Region.AP_Beijing.getRegion(),"1250000000", false);
        Assert.assertEquals("bucket-1250000000.cos.ap-beijing.myqcloud.com", host4);

        String defaultRequestHost = config.getDefaultRequestHost(Region.AP_Beijing.getRegion(),"bucket-1250000000", "1250000000");
        Assert.assertEquals("bucket-1250000000.cos.ap-beijing.myqcloud.com", defaultRequestHost);

        String headerHost = config.getHeaderHost(Region.AP_Beijing.getRegion(),"bucket-1250000000");
        Assert.assertEquals("", headerHost);
    }

    @Test
    public void testIpPortHost() {

        CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
                .setHost(Uri.parse("https://127.0.0.1:8080"))
                .setHost("127.0.0.1")
                .builder();
        Assert.assertEquals(config.getRequestHost("", false),
                "127.0.0.1");
        Assert.assertEquals(config.getPort(),
                8080);
        config = new CosXmlServiceConfig.Builder()
                .setHost(Uri.parse("https://127.0.0.1"))
                .builder();
        Assert.assertEquals(config.getRequestHost("", false),
                "127.0.0.1");
    }

    @Test
    public void testCSPHostCompat() {
        CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                .builder();
        String host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.region.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(null, "region")
                .setBucketInPath(false)
                .builder();
        host = config.getRequestHost("bucket",false);
        Assert.assertEquals("bucket.cos.region.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(null, "region")
                .setBucketInPath(false)
                .builder();
        host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.region.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                .setEndpointSuffix("cos.region.yun.tce.com")
                .builder();
        host = config.getRequestHost("bucket",false);
        Assert.assertEquals("bucket-1250000000.cos.region.yun.tce.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(true)
                .setEndpointSuffix("cos.region.yun.tce.com")
                .builder();
        host = config.getRequestHost("bucket",false);
        Assert.assertEquals("cos.region.yun.tce.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(true)
                .setEndpointSuffix("cos.yun.tce.com")
                .builder();
        host = config.getRequestHost("bucket",false);
        Assert.assertEquals("cos.yun.tce.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                .setEndpointSuffix("cos.${region}.yun.tce.com")
                .builder();
        host = config.getRequestHost("bucket",true);
        Assert.assertEquals("bucket-1250000000.cos.accelerate.yun.tce.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "ap-guangzhou")
                .setBucketInPath(false)
                .setEndpointSuffix("cos.${region}.myqcloud.com")
                .builder();
        host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.ap-guangzhou.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "ap-guangzhou")
                .builder();
        Assert.assertEquals("cos.accelerate.myqcloud.com",
                config.getEndpointSuffix(config.getRegion(),true));
    }

    @Test
    public void testCSPHost() {
        CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                .builder();
        String host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.region.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(null, "region")
                .setBucketInPath(false)
                .builder();
        host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.region.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(null, "region")
                .setBucketInPath(false)
                .builder();
        host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.region.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                // .setEndpointSuffix("cos.region.yun.tce.com")
                .setHostFormat("${bucket}.cos.region.yun.tce.com")
                .builder();
        host = config.getRequestHost("bucket",false);
        Assert.assertEquals("bucket-1250000000.cos.region.yun.tce.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(true)
                //.setEndpointSuffix("cos.region.yun.tce.com")
                .setHostFormat("cos.region.yun.tce.com")
                .builder();
        host = config.getRequestHost("bucket",false);
        Assert.assertEquals("cos.region.yun.tce.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(true)
                //.setEndpointSuffix("cos.yun.tce.com")
                .setHostFormat("cos.yun.tce.com")
                .builder();
        host = config.getRequestHost("bucket",false);
        Assert.assertEquals("cos.yun.tce.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                //.setEndpointSuffix("cos.${region}.yun.tce.com")
                .setHostFormat("${bucket}.cos.accelerate.yun.tce.com")
                .builder();
        host = config.getRequestHost("bucket",true);
        Assert.assertEquals("bucket-1250000000.cos.accelerate.yun.tce.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "ap-guangzhou")
                .setBucketInPath(false)
                //.setEndpointSuffix("cos.${region}.myqcloud.com")
                .setHostFormat("${bucket}.cos.${region}.myqcloud.com")
                .builder();
        host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.ap-guangzhou.myqcloud.com", host);

//        config = new CosXmlServiceConfig.Builder()
//                .setAppidAndRegion("1250000000", "ap-guangzhou")
//                .builder();
//        Assert.assertEquals("cos.accelerate.myqcloud.com",
//                config.getEndpointSuffix(config.getRegion(),true));
    }

    @Test public void testAccelerateHost() {

        CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                .setAccelerate(true)
                .builder();
        String host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.accelerate.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(true)
                .setAccelerate(true)
                .builder();
        host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.accelerate.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                .setAccelerate(false)
                .builder();
        host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.cos.region.myqcloud.com", host);

        config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                .setAccelerate(false)
                .builder();
        host = config.getRequestHost("bucket-1250000000",true);
        Assert.assertEquals("bucket-1250000000.cos.accelerate.myqcloud.com", host);
    }

    @Test public void testCustomFormatHost() {

        CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("1250000000", "region")
                .setBucketInPath(false)
                .setAccelerate(true)
                .setHostFormat("${bucket}.${region}.tencent.com")
                .builder();
        String host = config.getRequestHost("bucket-1250000000",false);
        Assert.assertEquals("bucket-1250000000.region.tencent.com", host);
    }
}
