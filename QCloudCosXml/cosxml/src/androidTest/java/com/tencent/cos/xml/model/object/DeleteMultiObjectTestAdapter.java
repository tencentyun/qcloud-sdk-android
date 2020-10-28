package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.RequestTestAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// Generate by auto
public class DeleteMultiObjectTestAdapter extends RequestTestAdapter<DeleteMultiObjectRequest, DeleteMultiObjectResult> {

    private List<String> keys;

    public DeleteMultiObjectTestAdapter(String ... keys) {
        this.keys = new LinkedList<>();
        Collections.addAll(this.keys, keys);
    }

    @Override
    protected DeleteMultiObjectRequest newRequestInstance() {
        return new DeleteMultiObjectRequest(TestConst.PERSIST_BUCKET, keys);
    }

    @Override
    protected DeleteMultiObjectResult exeSync(DeleteMultiObjectRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.deleteMultiObject(request);
    }

    @Override
    protected void exeAsync(DeleteMultiObjectRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.deleteMultiObjectAsync(request, resultListener);
    }
}