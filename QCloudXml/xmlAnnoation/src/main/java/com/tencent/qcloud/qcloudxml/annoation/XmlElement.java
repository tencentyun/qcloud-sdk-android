/*
 * Copyright (C) 2015 Hannes Dorfmann
 * Copyright (C) 2015 Tickaroo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tencent.qcloud.qcloudxml.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 子元素
 *
 * <pre>
 *   {@code
 *   <Book>
 *      <Title>Effective Java</Title>
 *   </Book>
 *   }
 * </pre>
 *
 * <pre>
 *   {@code
 *
 *   @XmlBean
 *   public class Book {
 *   @XmlElement
 *   String title;
 *   }
 *   }
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
@Inherited
public @interface XmlElement {

  /**
   * XML元素名称
   *
   * @return XML元素名称 默认为字段名且首字母大写
   */
  String name() default "";

  /**
   * 忽略List节点
   * 目前仅用于toxml，适配部分不合理XML节点设计
   * @return 是否忽略List节点
   */
  boolean ignoreListNote() default false;
}
