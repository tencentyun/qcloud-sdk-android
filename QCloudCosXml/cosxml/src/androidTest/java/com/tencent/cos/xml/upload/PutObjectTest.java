package com.tencent.cos.xml.upload;


import android.support.test.runner.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConfigs;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;

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
