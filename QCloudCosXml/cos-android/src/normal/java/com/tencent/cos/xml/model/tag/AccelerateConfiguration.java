package com.tencent.cos.xml.model.tag;

/**
 * <p>
 * Created by rickenwang on 2020/9/16.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class AccelerateConfiguration {

    /**
     * 全球加速功能是否开启，枚举值：Suspended、Enabled
     */
    public String status;

    /**
     * 全球加速功能的类型，枚举值：COS
     */
    public String type;
}
