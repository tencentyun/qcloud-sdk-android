package com.tencent.cos.xml.model.ci.common;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * <p>
 * Created by jordanqin on 2023/6/29 11:37.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@XmlBean
public class DigitalWatermark {
    /**
     * 嵌入数字水印的水印信息
     * 长度不超过64个字符，仅支持中文、英文、数字、_、-和*
     */
    public String message;
    /**
     * 数字水印类型
     * 当前仅可设置为 Text
     */
    public String type = "Text";
    /**
     * 数字水印版本
     * 当前仅可设置为 V1
     */
    public String version = "V1";
    /**
     * 当添加水印失败是否忽略错误继续执行任务
     * 限制为 true/false
     */
    public String ignoreError;
    /**
     * 添加水印是否成功，执行中为Running，成功为 Success，失败为 Failed
     * 该字段不能主动设置，当任务提交成功时，会返回该字段
     */
    public String state;
}
