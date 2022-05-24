package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.ci.GetMediaInfoRequest;
import com.tencent.cos.xml.model.ci.GetMediaInfoResult;

public class GetMediaInfoTestAdapter extends NormalRequestTestAdapter<GetMediaInfoRequest, GetMediaInfoResult> {
    @Override
    protected GetMediaInfoRequest newRequestInstance() {
        GetMediaInfoRequest GetMediaInfoRequest = new GetMediaInfoRequest(TestConst.PERSIST_BUCKET,
                TestConst.PERSIST_BUCKET_VIDEO_PATH);
        return GetMediaInfoRequest;
    }

    @Override
    protected GetMediaInfoResult exeSync(GetMediaInfoRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.getMediaInfo(request);
    }

    @Override
    protected void exeAsync(GetMediaInfoRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.getMediaInfoAsync(request, resultListener);
    }
}