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
public class TemplateTranscodeResponse {
    
    /**
     *保存模板详情的容器
     */
    public TemplateTranscodeResponseTemplate template;

    /**
     *请求的唯一 ID
     */
    public String requestId;

    @XmlBean(name = "Template", method = XmlBean.GenerateMethod.FROM)
    public static class TemplateTranscodeResponseTemplate {
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
         *模板类型，Transcode
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
        public TemplateTranscodeResponseTransTpl transTpl;

    }

    @XmlBean(name = "TransTpl", method = XmlBean.GenerateMethod.FROM)
    public static class TemplateTranscodeResponseTransTpl {
        /**
         *同请求体中的 Request.TimeInterval
         */
        public TemplateTranscode.TemplateTranscodeTimeInterval timeInterval;

        /**
         *同请求体中的 Request.Container
         */
        public TemplateTranscode.TemplateTranscodeContainer container;

        /**
         *同请求体中的 Request.Video
         */
        public TemplateTranscode.TemplateTranscodeVideo video;

        /**
         *同请求体中的 Request.Audio
         */
        public TemplateTranscode.TemplateTranscodeAudio audio;

        /**
         *同请求体中的 Request.TransConfig
         */
        public TemplateTranscode.TemplateTranscodeTransConfig transConfig;

        /**
         *同请求体中的 Request.AudioMix
         */
        public AudioMix audioMix;

        /**
         *同请求体中的Request.AudioMixArray
         */
        @XmlElement(flatListNote = true)
        public List<AudioMix> audioMixArray;

    }


}
