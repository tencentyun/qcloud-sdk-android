package com.tencent.cos.xml.model.ci.common;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * <p>
 * Created by jordanqin on 2023/6/29 11:37.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@XmlBean(method = XmlBean.GenerateMethod.FROM)
public class MediaResult {
    /**
     * 输出文件的基本信息
     */
    public OutputFile outputFile;

    /**
     * 输出文件的基本信息
     */
    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class OutputFile {
        /**
         * 输出文件所在的存储桶
         */
        public String bucket;
        /**
         * 输出文件所在的存储桶所在的园区
         */
        public String region;
        /**
         * 输出文件名，可能有多个
         */
        @XmlElement(flatListNote = true)
        public List<String> objectName;
        /**
         * 输出文件的 MD5 信息
         */
        @XmlElement(flatListNote = true)
        public List<Md5Info> md5Info;
    }

    /**
     * 输出文件的 MD5 信息
     */
    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class Md5Info {
        /**
         * 输出文件名
         */
        public String objectName;
        /**
         * 输出文件的 MD5 值
         */
        public String md5;
    }
}
