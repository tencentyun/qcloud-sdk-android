 
package com.tencent.cos.xml.model.bucket;

import static com.tencent.cos.xml.core.TestConst.OWNER_UIN;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.InventoryConfiguration;

import org.junit.Assert;

// Generate by auto
public class PutBucketInventoryTestAdapter extends NormalRequestTestAdapter<PutBucketInventoryRequest, PutBucketInventoryResult> {
    @Override
    protected PutBucketInventoryRequest newRequestInstance() {

        PutBucketInventoryRequest putBucketInventoryRequest = new PutBucketInventoryRequest(TestConst.PERSIST_BUCKET);
        putBucketInventoryRequest.setInventoryId("inventoryId");
        putBucketInventoryRequest.isEnable(true);
        putBucketInventoryRequest.setFilter("do_not_remove/");
        putBucketInventoryRequest.setIncludedObjectVersions(InventoryConfiguration.IncludedObjectVersions.ALL);
        putBucketInventoryRequest.setScheduleFrequency(InventoryConfiguration.SCHEDULE_FREQUENCY_DAILY);
        putBucketInventoryRequest.setOptionalFields(InventoryConfiguration.Field.StroageClass);
        Assert.assertNotNull(InventoryConfiguration.Frequency.DAILY.getValue());
        putBucketInventoryRequest.setDestination("CSV", OWNER_UIN, TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_REGION, "objectPrefix");
        return putBucketInventoryRequest;
    }

    @Override
    protected PutBucketInventoryResult exeSync(PutBucketInventoryRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketInventory(request);
    }

    @Override
    protected void exeAsync(PutBucketInventoryRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketInventoryAsync(request, resultListener);
    }
}  