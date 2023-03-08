package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.ci.GetSnapshotRequest;
import com.tencent.cos.xml.model.ci.GetSnapshotResult;

// Generate by auto
public class GetSnapshotTestAdapter extends NormalRequestTestAdapter<GetSnapshotRequest, GetSnapshotResult> {
    @Override
    protected GetSnapshotRequest newRequestInstance() {
        GetSnapshotRequest getSnapshotRequest = new GetSnapshotRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_VIDEO_PATH, TestUtils.localParentPath(), "1.jpg", 1);
        getSnapshotRequest.setHeight(100);
        getSnapshotRequest.setWidth(100);
        getSnapshotRequest.setFormat("jpg");
        getSnapshotRequest.setRotate("off");
        getSnapshotRequest.setMode("keyframe");
        return getSnapshotRequest;
    }

    @Override
    protected GetSnapshotResult exeSync(GetSnapshotRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getSnapshot(request);
    }

    @Override
    protected void exeAsync(GetSnapshotRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getSnapshotAsync(request, resultListener);
    }
}