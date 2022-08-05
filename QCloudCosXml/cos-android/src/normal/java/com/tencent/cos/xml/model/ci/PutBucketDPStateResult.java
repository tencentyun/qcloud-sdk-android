package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.BucketDocumentPreviewState;
import com.tencent.cos.xml.model.tag.PutBucketDPState;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

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

        try {
            PutBucketDPState putBucketDPState = QCloudXml.fromXml(response.byteStream(), PutBucketDPState.class);
            bucketDocumentPreviewState = putBucketDPState.DocBucket;
        } catch (IOException e) {
            throw new CosXmlClientException(ClientErrorCode.SERVERERROR.getCode(), e);
        } catch (XmlPullParserException e) {
            throw new CosXmlClientException(ClientErrorCode.POOR_NETWORK.getCode(), e);
        }
    }

    public BucketDocumentPreviewState getDocumentPreviewState() {
        return bucketDocumentPreviewState;
    }
}
