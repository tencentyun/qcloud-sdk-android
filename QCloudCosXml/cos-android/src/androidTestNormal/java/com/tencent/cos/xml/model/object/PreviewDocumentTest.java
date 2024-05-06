package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlLinkRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlLinkResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentInHtmlResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentRequest;
import com.tencent.cos.xml.model.ci.PreviewDocumentResult;

import org.junit.Assert;
public class PreviewDocumentTest {
    public static class PreviewDocumentTestAdapter extends NormalRequestTestAdapter<PreviewDocumentRequest, PreviewDocumentResult> {
        @Override
        protected PreviewDocumentRequest newRequestInstance() {
            PreviewDocumentRequest request = new PreviewDocumentRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_DOCUMENT_PATH, TestUtils.localParentPath(), 1);
            request.setSrcType("docx");
            // NOCA:authentic_check(测试用的word文件密码 不会暴露什么)
            request.setPassword("qwer1234");
            request.setDstType("jpg");
            request.setComment(1);
            request.setImageParams("imageMogr2/thumbnail/!50p|watermark/2/text/5pWw5o2u5LiH6LGh/fill/I0ZGRkZGRg==/fontsize/30/dx/20/dy/20");
            request.setQuality(90);
            request.setScale(200);
            request.setImageDpi(100);
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
            Assert.assertTrue(result.getTotalPage() > 0);
            result.getContentType();
//        Assert.assertNotNull(result.getContentType());
            Assert.assertNull(result.getErrNo());
            Assert.assertNotNull(result.getPreviewFilePath());
        }
    }

    public static class PreviewDocumentExcelTestAdapter extends NormalRequestTestAdapter<PreviewDocumentRequest, PreviewDocumentResult> {
        @Override
        protected PreviewDocumentRequest newRequestInstance() {
            PreviewDocumentRequest request = new PreviewDocumentRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_DOCUMENT_XLSX_PATH, TestUtils.localParentPath(), 1);
            request.setDstType("jpg");
            request.setComment(1);
            request.setImageParams("imageMogr2/thumbnail/!50p|watermark/2/text/5pWw5o2u5LiH6LGh/fill/I0ZGRkZGRg==/fontsize/30/dx/20/dy/20");
            request.setQuality(90);
            request.setScale(200);
            request.setImageDpi(100);
            request.setExcelSheet(1);
            request.setExcelPaperDirection(1);
            request.setExcelRow(0);
            request.setExcelCol(1);
            request.setExcelPaperSize(0);
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
            Assert.assertTrue(result.getTotalPage() > 0);
            result.getContentType();
//        Assert.assertNotNull(result.getContentType());
            result.getTotalSheet();
            result.getSheetName();
            Assert.assertNull(result.getErrNo());
            Assert.assertNotNull(result.getPreviewFilePath());
        }
    }

    public static class PreviewDocumentTxtTestAdapter extends NormalRequestTestAdapter<PreviewDocumentRequest, PreviewDocumentResult> {
        @Override
        protected PreviewDocumentRequest newRequestInstance() {
            PreviewDocumentRequest request = new PreviewDocumentRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_DOCUMENT_TXT_PATH, TestUtils.localParentPath(), 1);
            request.setSrcType("txt");
            request.setDstType("jpg");
            request.setComment(1);
            request.setImageParams("imageMogr2/thumbnail/!50p|watermark/2/text/5pWw5o2u5LiH6LGh/fill/I0ZGRkZGRg==/fontsize/30/dx/20/dy/20");
            request.setQuality(90);
            request.setScale(200);
            request.setImageDpi(100);
            request.setTxtPagination(true);
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
            Assert.assertTrue(result.getTotalPage() > 0);
            result.getContentType();
//        Assert.assertNotNull(result.getContentType());
            Assert.assertNull(result.getErrNo());
            Assert.assertNotNull(result.getPreviewFilePath());
        }
    }

    public static class PreviewDocumentInHtmlTestAdapter extends NormalRequestTestAdapter<PreviewDocumentInHtmlRequest, PreviewDocumentInHtmlResult> {
        @Override
        protected PreviewDocumentInHtmlRequest newRequestInstance() {
            PreviewDocumentInHtmlRequest request = new PreviewDocumentInHtmlRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_DOCUMENT_PATH, TestUtils.localParentPath());
            request.setSrcType("docx");
            request.setCopyable(false);
            request.setCopyable(true);
            request.setHtmlParams("{ commonOptions: { isShowTopArea: true, isShowHeader: true } }");
            try {
                request.setWatermark("Test");
                request.setWatermarkColor("rgba(192,192,192,0.6)");
                request.setWatermarkFont("bold 20px Serif");
            } catch (CosXmlClientException e) {
                e.printStackTrace();
            }
            request.setWatermarkRotate(300);
            request.setWatermarkHorizontal(60);
            request.setWatermarkVertical(90);
            return request;
        }

        @Override
        protected PreviewDocumentInHtmlResult exeSync(PreviewDocumentInHtmlRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.previewDocumentInHtml(request);
        }

        @Override
        protected void exeAsync(PreviewDocumentInHtmlRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.previewDocumentInHtmlAsync(request, resultListener);
        }

        @Override
        protected void assertResult(PreviewDocumentInHtmlResult result) {
            super.assertResult(result);
            Assert.assertNotNull(result.getPreviewFilePath());
        }
    }

    public static class PreviewDocumentInHtmlLinkTestAdapter extends NormalRequestTestAdapter<PreviewDocumentInHtmlLinkRequest, PreviewDocumentInHtmlLinkResult> {
        @Override
        protected PreviewDocumentInHtmlLinkRequest newRequestInstance() {
            PreviewDocumentInHtmlLinkRequest request = new PreviewDocumentInHtmlLinkRequest(TestConst.PERSIST_BUCKET,
                    TestConst.PERSIST_BUCKET_DOCUMENT_PATH);
            request.setSrcType("docx");
            request.setCopyable(false);
            request.setCopyable(true);
            request.setHtmlParams("{ commonOptions: { isShowTopArea: true, isShowHeader: true } }");
            try {
                request.setWatermark("Test");
                request.setWatermarkColor("rgba(192,192,192,0.6)");
                request.setWatermarkFont("bold 20px Serif");
            } catch (CosXmlClientException e) {
                e.printStackTrace();
            }
            request.setWatermarkRotate(300);
            request.setWatermarkHorizontal(60);
            request.setWatermarkVertical(90);
            return request;
        }

        @Override
        protected PreviewDocumentInHtmlLinkResult exeSync(PreviewDocumentInHtmlLinkRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
            return cosXmlService.previewDocumentInHtmlLink(request);
        }

        @Override
        protected void exeAsync(PreviewDocumentInHtmlLinkRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
            cosXmlService.previewDocumentInHtmlLinkAsync(request, resultListener);
        }

        @Override
        protected void assertResult(PreviewDocumentInHtmlLinkResult result) {
            super.assertResult(result);
            Assert.assertNotNull(result.getPreviewUrl());
        }
    }
}