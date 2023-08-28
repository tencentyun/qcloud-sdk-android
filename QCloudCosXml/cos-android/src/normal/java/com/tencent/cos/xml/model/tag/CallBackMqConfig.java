package com.tencent.cos.xml.model.tag;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * @author Created by jordanqin.
 * Created in 2022/10/21 11:36
 * Copyright 2010-2022 Tencent Cloud. All Rights Reserved.
 */
@XmlBean(name = "CallBackMqConfig")
public class CallBackMqConfig {
    /**
     * 消息队列所属园区，目前支持园区 sh（上海）、bj（北京）、gz（广州）、cd（成都）、hk（香港）
     */
    public String mqRegion;
    /**
     * 消息队列使用模式，默认 Queue ：
     * 主题订阅：Topic
     * 队列服务: Queue
     */
    public String mqMode;
    /**
     * TDMQ 主题名称
     */
    public String mqName;
}
