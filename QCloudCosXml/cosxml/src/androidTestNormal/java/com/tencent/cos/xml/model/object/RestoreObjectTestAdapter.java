package com.tencent.cos.xml.model.object;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.RestoreConfigure;

import org.junit.Assert;

public class RestoreObjectTestAdapter extends NormalRequestTestAdapter<RestoreRequest, RestoreResult> {
    @Override
    protected RestoreRequest newRequestInstance() {
        RestoreRequest request = new RestoreRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_DEEP_ARCHIVE_OBJECT_PATH);
        request.setExpireDays(1);
        request.setTier(RestoreConfigure.Tier.Expedited);
        return request;
    }

    @Override
    protected RestoreResult exeSync(RestoreRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.restoreObject(request);
    }

    @Override
    protected void exeAsync(RestoreRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.restoreObjectAsync(request, resultListener);
    }

    @Override
    protected void assertException(@Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {

        if (serviceException != null && "RestoreAlreadyInProgress".equalsIgnoreCase(serviceException.getErrorCode())) {
            Assert.assertTrue(true);
        } else {
            super.assertException(clientException, serviceException);
        }
    }
}