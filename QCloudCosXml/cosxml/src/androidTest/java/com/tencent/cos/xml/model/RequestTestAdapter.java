package com.tencent.cos.xml.model;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.core.ServiceFactory;
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

    public void testSyncRequest() {

        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            S result = exeSync(newRequestInstance(), cosXmlService);
            assertResult(result);
        } catch (CosXmlClientException clientException) {
            assertException(clientException, null);
        } catch (CosXmlServiceException serviceException) {
            assertException(null, serviceException);
        }
    }


    public void testAsyncRequest() {

        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        final TestLocker locker = new TestLocker();

        final COSResult cosResult = new COSResult();

        exeAsync(newRequestInstance(), cosXmlService, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                cosResult.result = result;
                locker.release();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                cosResult.clientException = clientException;
                cosResult.serviceException = serviceException;
                locker.release();
            }
        });
        locker.lock();
        assertCOSResult(cosResult);
    }

    protected abstract R newRequestInstance();

    protected abstract S exeSync(R request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException;

    protected abstract void exeAsync(R request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener);

    private void assertCOSResult(COSResult cosResult) {

        if (cosResult.result != null) {
            assertResult((S) cosResult.result);
        } else {
            assertException(cosResult.clientException, cosResult.serviceException);
        }
    }

    protected void assertResult(S result) {
        result.printResult();
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
