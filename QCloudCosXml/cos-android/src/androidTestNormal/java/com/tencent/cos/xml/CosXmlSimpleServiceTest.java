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

package com.tencent.cos.xml;

import static com.tencent.cos.xml.core.TestUtils.getContext;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.object.HeadObjectRequest;
import com.tencent.cos.xml.model.object.HeadObjectResult;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.logger.FileLogAdapter;
import com.tencent.qcloud.core.logger.QCloudLogger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class CosXmlSimpleServiceTest {
    @Test
    public void cosXmlSimpleServiceTest(){
        HeadObjectRequest request = new HeadObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH);
        request.setVersionId(null);
        request.setIfModifiedSince("Wed, 21 Oct 2009 07:28:00 GMT");

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .isHttps(true)
                .setDebuggable(true)
                .setAppidAndRegion(TestConst.COS_APPID, TestConst.PERSIST_BUCKET_REGION)
                .setRegion(TestConst.PERSIST_BUCKET_REGION)
                .builder();
        CosXmlService cosXmlService = new CosXmlService(getContext(), cosXmlServiceConfig,
                new ShortTimeCredentialProvider(TestConst.SECRET_ID, TestConst.SECRET_KEY,600) );
        cosXmlService.setNetworkClient(cosXmlServiceConfig);
        String host = TestConst.PERSIST_BUCKET + ".cos." + TestConst.PERSIST_BUCKET_REGION + ".myqcloud.com";
//        try {
//            cosXmlService.addCustomerDNS(host, new String[] {"183.60.83.19"});
//        } catch (CosXmlClientException e) {
//            e.printStackTrace();
//        }
        cosXmlService.addVerifiedHost(host);
        cosXmlService.setServiceDomain(host);

        Assert.assertNotNull(cosXmlService.getAppid());
        Assert.assertNotNull(cosXmlService.getRegion());
        Assert.assertNotNull(cosXmlService.getRegion(request));

        FileLogAdapter fileLogAdapter = FileLogAdapter.getInstance(getContext(), "QLog");
        QCloudLogger.addAdapter(fileLogAdapter);
        File[] files = cosXmlService.getLogFiles(100);

        try {
            Assert.assertNotNull(cosXmlService.getPresignedURL(request));
        } catch (CosXmlClientException e) {
            Assert.fail();
        }
        Assert.assertNotNull(cosXmlService.getAccessUrl(request));

        try {
            HeadObjectResult result = cosXmlService.headObject(request);
            Assert.assertNotNull(result);
        } catch (CosXmlClientException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } catch (CosXmlServiceException e) {
            Assert.fail(TestUtils.getCosExceptionMessage(e));
        } finally {
            cosXmlService.cancelAll();
            cosXmlService.release();
        }
    }
}