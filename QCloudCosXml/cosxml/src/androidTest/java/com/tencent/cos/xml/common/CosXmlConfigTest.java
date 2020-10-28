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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CosXmlConfigTest {

    @Test
    public void testIpPortHost() {

        CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
                .setHost(Uri.parse("https://127.0.0.1:8080"))
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
