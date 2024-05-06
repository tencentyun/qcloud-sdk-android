package com.tencent.cos.xml.model;

import static org.hamcrest.CoreMatchers.is;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestLocker;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;

import org.junit.Assert;
import org.junit.rules.ErrorCollector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created by rickenwang on 2020/10/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public abstract class NormalRequestTestAdapter<R extends CosXmlRequest, S extends CosXmlResult> {
    private ErrorCollector collector;
    public void setCollector(ErrorCollector collector) {
        this.collector = collector;
    }
    private int retry = 3;

    private void recoveryRetry(){
        retry = 3;
    }

    public void testSyncRequest() {

        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
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
        }
    }


    public void testAsyncRequest() {

        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
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

    protected abstract S exeSync(R request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException;

    protected abstract void exeAsync(R request, CosXmlService cosXmlService, CosXmlResultListener resultListener);

    protected void assertResult(S result) {
        TestUtils.print(result.printResult());

        setResultPublicFieldNull(result);
        result.printResult();

        TestUtils.printXML(result);
        if(this.collector != null) {
            this.collector.checkThat(result.httpCode >= 200 && result.httpCode < 300, is(true));
        } else  {
            Assert.assertTrue(result.httpCode >= 200 && result.httpCode < 300);
        }
    }

    protected void assertException(@Nullable CosXmlClientException clientException,
                                   @Nullable CosXmlServiceException serviceException) {
        if(this.collector != null) {
            this.collector.addError(new AssertionError(TestUtils.getCosExceptionMessage(this.getClass().getSimpleName(), clientException, serviceException)));
        } else {
            Assert.fail(TestUtils.getCosExceptionMessage(this.getClass().getSimpleName(), clientException, serviceException));
        }
    }

    private void setResultPublicFieldNull(S result) {
        if (result == null) {
            return;
        }

        Field[] fields = result.getClass().getDeclaredFields();
        List<Field> publicFields = new ArrayList<>();

        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers())) {
                publicFields.add(field);
            }
        }

        if (publicFields.size() == 1) {
            Field publicField = publicFields.get(0);
            try {
                publicField.set(result, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class COSResult {

        CosXmlResult result;

        CosXmlClientException clientException;

        CosXmlServiceException serviceException;
    }
}
