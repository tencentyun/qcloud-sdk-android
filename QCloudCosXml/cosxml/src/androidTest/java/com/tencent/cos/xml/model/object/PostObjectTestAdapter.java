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
public class PostObjectTestAdapter extends RequestTestAdapter<PostObjectRequest, PostObjectResult> {
    @Override
    protected PostObjectRequest newRequestInstance() {
        return new PostObjectRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_POST_OBJECT_PATH,
                "this is post object".getBytes());
    }

    @Override
    protected PostObjectResult exeSync(PostObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.postObject(request);
    }

    @Override
    protected void exeAsync(PostObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.postObjectAsync(request, resultListener);
    }


}