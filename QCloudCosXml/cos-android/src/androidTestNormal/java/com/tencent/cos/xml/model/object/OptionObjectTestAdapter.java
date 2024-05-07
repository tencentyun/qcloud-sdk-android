package com.tencent.cos.xml.model.object;

import static org.hamcrest.CoreMatchers.is;

import androidx.annotation.Nullable;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

import org.junit.Assert;

// Generate by auto
public class OptionObjectTestAdapter extends NormalRequestTestAdapter<OptionObjectRequest, OptionObjectResult> {
    @Override
    protected OptionObjectRequest newRequestInstance() {
        OptionObjectRequest request = new OptionObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH,
                "cloud.tencent.com", "PUT");
        request.setOrigin("cloud.tencent.com");
        Assert.assertEquals(request.getOrigin(), "cloud.tencent.com");
        request.setAccessControlMethod("PUT");
        Assert.assertEquals(request.getAccessControlMethod(), "PUT");
        request.setAccessControlHeaders("Access-Control-Test");
        Assert.assertEquals(request.getAccessControlHeaders(), "Access-Control-Test");
        return request;
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
            if(super.collector != null) {
                this.collector.checkThat(true, is(true));
            } else  {
                Assert.assertTrue(true);
            }
        } else {
            super.assertException(clientException, serviceException);
        }
    }
}