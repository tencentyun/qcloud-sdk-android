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
 *
 *   &#64;XmlBean
 *   public class Book {
 *   &#64;XmlElement
 *   String title;
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
   * 忽略List节点<br/>
   * 目前仅用于toxml，适配部分不合理XML节点设计
   * @return 是否忽略List节点
   */
  boolean ignoreListNote() default false;

  /**
   * 是否是平铺的List节点<br/>
   * 仅用于fromxml<br/>
   * 如下XML
   *
   * <pre>
   * {@code
   *   <Response>
   *     <MediaBucketList>
   *           <BucketId></BucketId>
   *           <Region></Region>
   *           <CreateTime></CreateTime>
   *     </MediaBucketList>
   *     <MediaBucketList>
   *           <BucketId></BucketId>
   *           <Region></Region>
   *           <CreateTime></CreateTime>
   *     </MediaBucketList>
   * </Response>
   *
   *  @XmlBean(name = "Response")
   *  public class DescribeMediaBucketsResult {
   *     //媒体 Bucket 列表
   *     @XmlElement(flatListNote = true)
   *     public List<MediaBucketList> mediaBucketList;
   *
   *     @XmlBean
   *     public static class MediaBucketList{
   *         //存储桶 ID
   *         public String bucketId;
   *     }
   * }
   * }
   * </pre>
   * @return 是否是平铺的List节点
   */
  boolean flatListNote() default false;

  /**
   * 忽略Element名称<br/>
   * 目前仅用于toxml，用于适配以下XML
   *
   * <pre>
   * {@code
   *   <DomainList>
   *     <Domain>*.qq.com</Domain>
   *     <Domain>*.qcloud.com</Domain>
   *   </DomainList>
   *
   *   public List<Domain> domainList;
   *   @XmlBean
   *   public static class Domain{
   *       @XmlElement(ignoreName = true)
   *       public String domain;
   *   }
   *   }
   *   </pre>
   * @return 是否忽略Element名称
   */
  boolean ignoreName() default false;

  /**
   * 是否忽略0值
   * @return 是否忽略0值
   */
  boolean ignoreZero() default false;

  /**
   * 是否解析为原始的xml字符串<br/>
   * 仅用于fromxml
   * @return 原始的xml字符串
   */
  boolean originalXmlString() default false;
}
