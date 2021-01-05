package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.ci.PreviewDocumentRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentResult;

import org.junit.Assert;

public class PreviewDocumentTestAdapter extends NormalRequestTestAdapter<PreviewDocumentRequest, PreviewDocumentResult> {
    @Override
    protected PreviewDocumentRequest newRequestInstance() {
        PreviewDocumentRequest request = new PreviewDocumentRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_DOCUMENT_PATH, TestUtils.localParentPath(), "preview_document.jpg", 1);
        request.setSrcType("docx");
        return request;
    }

    @Override
    protected PreviewDocumentResult exeSync(PreviewDocumentRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.previewDocument(request);
    }

    @Override
    protected void exeAsync(PreviewDocumentRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.previewDocumentAsync(request, resultListener);
    }

    @Override
    protected void assertResult(PreviewDocumentResult result) {
        super.assertResult(result);
        Assert.assertTrue(result.getTotalPage()>0);
        result.getContentType();
//        Assert.assertNotNull(result.getContentType());
        Assert.assertNull(result.getErrNo());
        Assert.assertNotNull(result.getPreviewFilePath());
    }
}