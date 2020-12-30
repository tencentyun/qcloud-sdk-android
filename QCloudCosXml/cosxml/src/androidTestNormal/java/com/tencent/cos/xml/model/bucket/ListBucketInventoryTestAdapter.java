 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;

public class ListBucketInventoryTestAdapter extends NormalRequestTestAdapter<ListBucketInventoryRequest, ListBucketInventoryResult> {
    @Override
    protected ListBucketInventoryRequest newRequestInstance() {
        ListBucketInventoryRequest request = new ListBucketInventoryRequest(TestConst.PERSIST_BUCKET);
        // TODO: 2020/12/30 覆盖率  缺省值：None貌似不对  设置为none后获取不到清单
        request.setContinuationToken("");
        return request;
    }

    @Override
    protected ListBucketInventoryResult exeSync(ListBucketInventoryRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.listBucketInventory(request);
    }

    @Override
    protected void exeAsync(ListBucketInventoryRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.listBucketInventoryAsync(request, resultListener);
    }
}  