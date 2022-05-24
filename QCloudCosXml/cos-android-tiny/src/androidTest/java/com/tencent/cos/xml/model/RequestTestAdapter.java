package com.tencent.cos.xml.model;

import android.util.Log;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;

import org.junit.Assert;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public abstract class RequestTestAdapter<R extends CosXmlRequest, S extends CosXmlResult> {

    private int retry = 3;
    private void recoveryRetry(){
        retry = 3;
    }

    public void testAsyncRequest() {

        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        final TestLocker locker = new TestLocker();

        final COSResult cosResult = new COSResult();

        exeAsync(newRequestInstance(), cosXmlService, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.i(TestConst.UT_TAG, "onSuccess");
                cosResult.result = result;
                locker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                Log.i(TestConst.UT_TAG, "onFailed");
                cosResult.clientException = clientException;
                cosResult.serviceException = serviceException;
                locker.release();
            }
        });
        locker.lock();

        if (cosResult.result != null) {
            recoveryRetry();
            assertResult((S) cosResult.result);
        } else {
            if(retry > 0) {
                TestUtils.sleep(600);
                retry--;
                testAsyncRequest();
            } else {
                recoveryRetry();
                assertException(cosResult.clientException, cosResult.serviceException);
            }
        }
    }

    protected abstract R newRequestInstance();

    protected abstract void exeAsync(R request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener);

    protected void assertResult(S result) {
//        result.printResult();
        TestUtils.parseBadResponseBody(result);

        Assert.assertTrue(result != null);
    }

    protected void assertException(@Nullable CosXmlClientException clientException,
                                   @Nullable CosXmlServiceException serviceException) {

        Assert.fail(TestUtils.getCosExceptionMessage(this.getClass().getSimpleName(), clientException, serviceException));
    }

    static class COSResult {

        CosXmlResult result;

        CosXmlClientException clientException;

        CosXmlServiceException serviceException;
    }
}
