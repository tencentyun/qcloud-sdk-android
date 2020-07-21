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

package com.tencent.cos.xml.upload;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConfigs;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PutObjectTest {


    @Test public void testPutObject() {

        CosXmlService cosXmlService = TestUtils.newDefaultTerminalService();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_1M_PATH,
                TestConfigs.LOCAL_TXT_1M_PATH);
        try {
            PutObjectResult putObjectResult = cosXmlService.putObject(putObjectRequest);
            Assert.assertTrue(true);
        } catch (CosXmlClientException clientException) {
            Assert.fail(TestUtils.mergeExceptionMessage(clientException, null));
        } catch (CosXmlServiceException serviceException) {
            Assert.fail(TestUtils.mergeExceptionMessage(null, serviceException));
        }
    }


    @Test public void testPutObjectAsync() {

        CosXmlService cosXmlService = TestUtils.newDefaultTerminalService();
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConfigs.TERMINAL_PERSIST_BUCKET, TestConfigs.COS_TXT_1M_PATH,
                TestConfigs.LOCAL_TXT_1M_PATH);
        final TestLocker locker = new TestLocker();
        cosXmlService.putObjectAsync(putObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Assert.assertTrue(true);
                locker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Assert.fail(TestUtils.mergeExceptionMessage(clientException, serviceException));
                locker.release();
            }
        });
        locker.lock();
    }

}
