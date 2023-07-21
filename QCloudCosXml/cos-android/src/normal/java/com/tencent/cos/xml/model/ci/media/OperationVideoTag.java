package com.tencent.cos.xml.model.ci.media;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * <p>
 * Created by jordanqin on 2023/6/20 17:56.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@XmlBean(name = "VideoTag")
public class OperationVideoTag {
    /**
     * 场景类型，可选择视频标签的运用场景，不同的运用场景使用的算法、输入输出等都会有所差异;是否必传：是;;限制：当前版本只适配 Stream 场景;
     */
    public String scenario = "Stream";
}
