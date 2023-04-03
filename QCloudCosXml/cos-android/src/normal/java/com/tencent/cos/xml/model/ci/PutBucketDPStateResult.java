package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.BucketDocumentPreviewState;
import com.tencent.cos.xml.model.tag.PutBucketDPState;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/10/14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class PutBucketDPStateResult extends CosXmlResult {

    private BucketDocumentPreviewState bucketDocumentPreviewState;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);

        PutBucketDPState putBucketDPState = QCloudXmlUtils.fromXml(response.byteStream(), PutBucketDPState.class);
        bucketDocumentPreviewState = putBucketDPState.DocBucket;
    }

    public BucketDocumentPreviewState getDocumentPreviewState() {
        return bucketDocumentPreviewState;
    }
}
