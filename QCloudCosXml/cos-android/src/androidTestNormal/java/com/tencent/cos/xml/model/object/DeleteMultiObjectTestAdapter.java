package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

import org.junit.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// Generate by auto
public class DeleteMultiObjectTestAdapter extends NormalRequestTestAdapter<DeleteMultiObjectRequest, DeleteMultiObjectResult> {

    private List<String> keys;

    public DeleteMultiObjectTestAdapter(String ... keys) {
        this.keys = new LinkedList<>();
        Collections.addAll(this.keys, keys);
    }

    @Override
    protected DeleteMultiObjectRequest newRequestInstance() {
        DeleteMultiObjectRequest request = new DeleteMultiObjectRequest(TestConst.PERSIST_BUCKET, keys);
        request.setQuiet(false);
        request.setObjectList(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+3);
        request.setObjectList(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+4, null);
        //制造删除失败的对象
        request.setObjectList("doesNotExist");
        Map<String, String> objectListWithVersionId = new HashMap<>();
        objectListWithVersionId.put(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+5, null);
        objectListWithVersionId.put(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+6, null);
        //制造删除失败的对象
        objectListWithVersionId.put(TestConst.PERSIST_BUCKET_SMALL_OBJECT_PATH+10, null);
        request.setObjectList(objectListWithVersionId);
        Assert.assertNotNull(request.getDelete());
        return request;
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