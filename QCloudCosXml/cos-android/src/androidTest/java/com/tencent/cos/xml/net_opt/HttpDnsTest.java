package com.tencent.cos.xml.net_opt;

import static com.tencent.cos.xml.core.TestUtils.smallFilePath;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.BasePutObjectResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;

import org.junit.Assert;

/**
 * <p>
 * Created by jordanqin on 2025/1/17 22:07.
 * Copyright 2010-2025 Tencent Cloud. All Rights Reserved.
 */
public class HttpDnsTest {
    private static final String TAG = "HttpDnsTest";
//    @Test
    public void testBasePutObject() {
        CosXmlSimpleService cosXmlSimpleService = ServiceFactory.INSTANCE.newDnsService();
        PutObjectRequest request = new PutObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                smallFilePath());
        final TestLocker testLocker = new TestLocker();
        cosXmlSimpleService.basePutObjectAsync(request, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                BasePutObjectResult result1 = (BasePutObjectResult) result;
                TestUtils.printXML(result1);
                Assert.assertFalse(TextUtils.isEmpty(result1.eTag));
                testLocker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, @Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {
                if(clientException != null) {
                    clientException.printStackTrace();
                }
                if(serviceException != null) {
                    serviceException.printStackTrace();
                }
                Assert.fail();
                testLocker.release();
            }
        });
        testLocker.lock();
    }
}
