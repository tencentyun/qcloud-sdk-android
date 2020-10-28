package com.tencent.cos.xml.model.object;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

import org.junit.Assert;

// Generate by auto
public class OptionObjectTestAdapter extends RequestTestAdapter<OptionObjectRequest, OptionObjectResult> {
    @Override
    protected OptionObjectRequest newRequestInstance() {
        return new OptionObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                "http://cloud.tencent.com", "PUT");
    }

    @Override
    protected OptionObjectResult exeSync(OptionObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.optionObject(request);
    }

    @Override
    protected void exeAsync(OptionObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.optionObjectAsync(request, resultListener);
    }


    @Override
    protected void assertException(@Nullable CosXmlClientException clientException, @Nullable CosXmlServiceException serviceException) {

        if (serviceException != null && "AccessForbidden".equalsIgnoreCase(serviceException.getErrorCode())) {
            Assert.assertTrue(true);
        } else {
            super.assertException(clientException, serviceException);
        }
    }
}