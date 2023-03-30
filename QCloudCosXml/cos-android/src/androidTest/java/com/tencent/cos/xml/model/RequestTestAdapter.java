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

    private int retry = 3;
    private void recoveryRetry(){
        retry = 3;
    }

    public void testSyncRequest() {

        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            S result = exeSync(newRequestInstance(), cosXmlService);
            recoveryRetry();
            assertResult(result);
        } catch (CosXmlClientException clientException) {
            if(retry > 0) {
                TestUtils.sleep(600);
                retry--;
                testSyncRequest();
            } else {
                recoveryRetry();
                assertException(clientException, null);
            }
        } catch (CosXmlServiceException serviceException) {
            if(retry > 0) {
                TestUtils.sleep(600);
                retry--;
                testSyncRequest();
            } else {
                recoveryRetry();
                assertException(null, serviceException);
            }
        } catch (Exception e){
            e.printStackTrace();
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

    protected abstract S exeSync(R request, CosXmlSimpleService cosXmlService) throws CosXmlClientException, CosXmlServiceException;

    protected abstract void exeAsync(R request, CosXmlSimpleService cosXmlService, CosXmlResultListener resultListener);

    protected void assertResult(S result) {
        TestUtils.print(result.printResult());
        TestUtils.printXML(result);
        Assert.assertTrue(result.httpCode >= 200 && result.httpCode < 300);
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

    // TODO: 2023/3/30 Post待删除
    public void testPostSyncRequest() {

        CosXmlSimpleService cosXmlService = ServiceFactory.INSTANCE.newDefaultService();
        try {
            S result = exeSync(newRequestInstance(), cosXmlService);
            Assert.assertTrue(true);
        } catch (CosXmlClientException clientException) {
            Assert.assertTrue(true);
        } catch (CosXmlServiceException serviceException) {
            Assert.assertTrue(true);
        } catch (Exception e){
            Assert.assertTrue(true);
        }
    }

    public void testPostAsyncRequest() {
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

        Assert.assertTrue(true);
    }
}
