package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

// Generate by auto
public class ListPartsTestAdapter extends RequestTestAdapter<ListPartsRequest, ListPartsResult> {
    @Override
    protected ListPartsRequest newRequestInstance() {
        return null;
    }

    @Override
    protected ListPartsResult exeSync(ListPartsRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.listParts(request);
    }

    @Override
    protected void exeAsync(ListPartsRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.listPartsAsync(request, resultListener);
    }
}