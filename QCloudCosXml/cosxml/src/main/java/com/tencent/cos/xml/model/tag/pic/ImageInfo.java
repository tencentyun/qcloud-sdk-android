package com.tencent.cos.xml.model.tag.pic;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

/**
 * <p>
 * Created by rickenwang on 2020/11/24.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@XmlBean(name = "ImageInfo")
public class ImageInfo {

    @XmlElement(name = "Format")
    public String format;

    @XmlElement(name = "Width")
    public int width;

    @XmlElement(name = "Height")
    public int height;

    @XmlElement(name = "Quality")
    public int quality;

    @XmlElement(name = "Ave")
    public String ave;

    @XmlElement(name = "Orientation")
    public int orientation;
}
