/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.model.ci.media;

import com.tencent.cos.xml.model.ci.common.AudioMix;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class TemplateVideoMontageResponse {
    
    /**
     *模板 ID
     */
    public String templateId;

    /**
     *模板名称
     */
    public String name;

    /**
     *模板所属存储桶
     */
    public String bucketId;

    /**
     *模板属性，Custom 或者 Official
     */
    public String category;

    /**
     *模板类型，VideoMontage
     */
    public String tag;

    /**
     *更新时间
     */
    public String updateTime;

    /**
     *创建时间
     */
    public String createTime;

    /**
     *详细的模板参数
     */
    public TemplateVideoMontageResponseVideoMontage videoMontage;

    @XmlBean(name = "VideoMontage", method = XmlBean.GenerateMethod.FROM)
    public static class TemplateVideoMontageResponseVideoMontage {
        /**
         *同请求体中的 Request.Duration
         */
        public String duration;

//        /**
//         *同请求体中的 Request.TimeInterval
//         */
//        public undefined timeInterval;

        /**
         *同请求体中的 Request.Container
         */
        public TemplateVideoMontage.TemplateVideoMontageContainer container;

        /**
         *同请求体中的 Request.Video
         */
        public TemplateVideoMontage.TemplateVideoMontageVideo video;

        /**
         *同请求体中的 Request.Audio
         */
        public TemplateVideoMontage.TemplateVideoMontageAudio audio;

        /**
         *同请求体中的 Request.Scene
         */
        public String scene;

        /**
         *同请求体中的 Request.AudioMix
         */
        public AudioMix audioMix;

        /**
         *同请求体中的 Request.AudioMixArray
         */
        @XmlElement(flatListNote = true)
        public List<AudioMix> audioMixArray;
    }


}
