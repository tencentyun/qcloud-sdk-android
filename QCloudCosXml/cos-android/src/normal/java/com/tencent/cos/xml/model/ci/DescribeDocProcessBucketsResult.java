package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.tag.DescribeDocProcessBuckets;
import com.tencent.cos.xml.utils.QCloudXmlUtils;
import com.tencent.qcloud.core.http.HttpResponse;

/**
 * 查询已经开通文档预览功能的 Bucket
 * Created by wjielai on 2020/10/14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class DescribeDocProcessBucketsResult extends CosXmlResult {
    /**
     * 查询已经开通文档预览功能的 Bucket
     */
    public DescribeDocProcessBuckets describeDocProcessBuckets;

    @Override
    public void parseResponseBody(HttpResponse response) throws CosXmlClientException, CosXmlServiceException {
        super.parseResponseBody(response);
        describeDocProcessBuckets = QCloudXmlUtils.fromXml(response.byteStream(), DescribeDocProcessBuckets.class);
    }
}
