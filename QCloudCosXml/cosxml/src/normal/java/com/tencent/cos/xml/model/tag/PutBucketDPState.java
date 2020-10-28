package com.tencent.cos.xml.model.tag;

import com.tencent.cos.xml.model.tag.BucketDocumentPreviewState;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/10/14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

@XmlBean(name = "Response")
public class PutBucketDPState {

    @XmlElement
    public String RequestId;

    @XmlElement
    public BucketDocumentPreviewState DocBucket;

}
