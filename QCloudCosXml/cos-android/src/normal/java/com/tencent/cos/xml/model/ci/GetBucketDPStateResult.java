package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.BucketDocumentPreviewState;
import com.tencent.cos.xml.model.tag.GetBucketDPState;
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

public class GetBucketDPStateResult extends CosXmlResult {

    private BucketDocumentPreviewState bucketDocumentPreviewState;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);

        try {
            GetBucketDPState getBucketDPS = QCloudXml.fromXml(response.byteStream(), GetBucketDPState.class);
            bucketDocumentPreviewState = getBucketDPS.DocBucketList;
        } catch (XmlPullParserException | IOException e) {
            throw new CosXmlServiceException("Parse xml error", e);
        }
    }

    public BucketDocumentPreviewState getDocumentPreviewState() {
        return bucketDocumentPreviewState;
    }
}
