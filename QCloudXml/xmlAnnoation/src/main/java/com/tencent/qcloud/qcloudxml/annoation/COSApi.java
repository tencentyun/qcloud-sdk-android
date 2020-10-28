package com.tencent.qcloud.qcloudxml.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * COS Api 注解，会生成对应的 Request 和 Result 类
 *
 * <p>
 * Created by rickenwang on 2020/9/29.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface COSApi {


}
