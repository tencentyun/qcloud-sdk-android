package com.tencent.cos.xml.doc;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.NormalServiceFactory;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.ci.PreviewDocumentRequest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 *
 * @author jordanqin
 * @since 6/3/21
 * <p>
 * Copyright (c) 2010-2021 Tencent Cloud. All rights reserved.
 */
@RunWith(AndroidJUnit4.class)
public class DocTest {
    @Test
    public void testPreviewDoc() {

        PreviewDocumentRequest previewDocumentRequest = new PreviewDocumentRequest(TestConst.PERSIST_BUCKET,
                "", TestUtils.localParentPath(), 1);

        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        cosXmlService.previewDocumentAsync(previewDocumentRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {

            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {

            }
        });
    }


    @Test public void testGetSnapshot() {

        PreviewDocumentRequest previewDocumentRequest = new PreviewDocumentRequest(TestConst.PERSIST_BUCKET,
                "", TestUtils.localParentPath(), 1);

        CosXmlService cosXmlService = NormalServiceFactory.INSTANCE.newDefaultService();
        cosXmlService.previewDocumentAsync(previewDocumentRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {

            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {

            }
        });
    }
}
