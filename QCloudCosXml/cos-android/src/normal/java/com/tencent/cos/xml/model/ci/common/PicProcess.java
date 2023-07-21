package com.tencent.cos.xml.model.ci.common;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * <p>
 * Created by jordanqin on 2023/6/29 10:46.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@XmlBean
public class PicProcess {
    /**
     * 是否返回原图信息;是否必传：否;
     */
    public boolean isPicInfo;

    /**
     * 图片处理规则;是否必传：是;
     * 1. 基础图片处理参见<a href="https://cloud.tencent.com/document/product/436/44879">基础图片处理</a>文档
     * 2. 图片压缩参见<a href="https://cloud.tencent.com/document/product/436/60450">图片压缩</a>文档
     * 3. 盲水印参见<a href="https://cloud.tencent.com/document/product/436/46782">盲水印</a>文档
     */
    public String processRule;
}
