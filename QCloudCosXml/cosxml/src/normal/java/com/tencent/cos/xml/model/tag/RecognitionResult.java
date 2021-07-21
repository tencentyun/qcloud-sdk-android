package com.tencent.cos.xml.model.tag;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

/**
 * <p>
 * Created by rickenwang on 2021/5/17.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
@XmlBean(name = "RecognitionResult")
public class RecognitionResult {

    @XmlElement(name = "PornInfo")
    public PornInfo pornInfo;

    @XmlElement(name = "PoliticsInfo")
    public PoliticsInfo politicsInfo;

    @XmlElement(name = "TerroristInfo")
    public TerroristInfo terroristInfo;

    @XmlElement(name = "AdsInfo")
    public AdsInfo adsInfo;


    @XmlBean(name = "PornInfo")
    public static class PornInfo {

        @XmlElement(name = "Code")
        public int code;

        @XmlElement(name = "Msg")
        public String msg;

        @XmlElement(name = "HitFlag")
        public int hitFlag;

        @XmlElement(name = "Score")
        public int score;

        @XmlElement(name = "Label")
        public String label;

    }
    @XmlBean(name = "PoliticsInfo")
    public static class PoliticsInfo {

        @XmlElement(name = "Code")
        public int code;

        @XmlElement(name = "Msg")
        public String msg;

        @XmlElement(name = "HitFlag")
        public int hitFlag;

        @XmlElement(name = "Score")
        public int score;

        @XmlElement(name = "Label")
        public String label;

    }
    @XmlBean(name = "TerroristInfo")
    public static class TerroristInfo {

        @XmlElement(name = "Code")
        public int code;

        @XmlElement(name = "Msg")
        public String msg;

        @XmlElement(name = "HitFlag")
        public int hitFlag;

        @XmlElement(name = "Score")
        public int score;

        @XmlElement(name = "Label")
        public String label;

    }
    @XmlBean(name = "AdsInfo")
    public static class AdsInfo {

        @XmlElement(name = "Code")
        public int code;

        @XmlElement(name = "Msg")
        public String msg;

        @XmlElement(name = "HitFlag")
        public int hitFlag;

        @XmlElement(name = "Score")
        public int score;

        @XmlElement(name = "Label")
        public String label;

    }

}

