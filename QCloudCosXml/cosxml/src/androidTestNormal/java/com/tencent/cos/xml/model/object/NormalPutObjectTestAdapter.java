package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class NormalPutObjectTestAdapter extends NormalRequestTestAdapter<PutObjectRequest, PutObjectResult> {
    private String cospath;

    public NormalPutObjectTestAdapter(String cospath){
        this.cospath = cospath;
    }

    @Override
    protected PutObjectRequest newRequestInstance() {
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET, this.cospath,
                TestUtils.smallFilePath());
        return putObjectRequest;
    }

    @Override
    protected PutObjectResult exeSync(PutObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putObject(request);
    }

    @Override
    protected void exeAsync(PutObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putObjectAsync(request, resultListener);
    }
}