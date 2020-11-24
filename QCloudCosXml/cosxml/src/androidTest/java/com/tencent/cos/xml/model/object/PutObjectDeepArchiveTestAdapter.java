package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class PutObjectDeepArchiveTestAdapter extends RequestTestAdapter<PutObjectRequest, PutObjectResult> {
    @Override
    protected PutObjectRequest newRequestInstance() {
        PutObjectRequest putObjectRequest = new PutObjectRequest(TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH.concat("_deepArchive"),
                TestUtils.smallFilePath());
        try {
            putObjectRequest.setRequestHeaders("x-cos-storage-class", COSStorageClass.DEEP_ARCHIVE.getStorageClass(), false);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
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