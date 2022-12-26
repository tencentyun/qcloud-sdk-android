package com.tencent.cos.xml.model.tag;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/10/14.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

@XmlBean(name = "Response")
public class DescribeDocProcessBuckets {
    /**
     * 请求的唯一 ID
     */
    public String requestId;

    /**
     * 队列总数
     */
    public int totalCount;

    /**
     * 当前页数，同请求中的 pageNumber
     */
    public int pageNumber;

    /**
     * 每页个数，同请求中的 pageSize
     */
    public int pageSize;

    /**
     * 队列数组
     */
    @XmlElement(flatListNote = true)
    public List<BucketDocumentPreviewState> docBucketList;
}
