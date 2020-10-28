package com.tencent.cos.xml.model.tag;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

/**
 * <p>
 * Created by rickenwang on 2020/9/29.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@XmlBean
public class IntelligentTieringConfiguration {

    @XmlElement(name = "Status")
    public String status;

    @XmlElement(name = "Transition")
    public Transition transition;

    @XmlBean
    public static class Transition {

        @XmlElement(name = "Days")
        public int days;
    }
}
