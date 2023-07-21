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

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class TemplateVoiceSeparate {

    /**
     *模板类型: VoiceSeparate;是否必传：是;限制：无;
     */
    public String tag = "VoiceSeparate";

    /**
     *模板名称 仅支持中文、英文、数字、_、-和*;是否必传：是;限制：无;
     */
    public String name;

    /**
     *输出音频;是否必传：是;限制：IsAudio：输出人声IsBackground：输出背景声AudioAndBackground：输出人声和背景声;
     */
    public String audioMode;

    /**
     *音频配置;是否必传：是;限制：无;
     */
    public AudioConfig audioConfig;

    @XmlBean(name = "Audio")
    public static class AudioConfig {
        /**
         *编解码格式;是否必传：否;默认值：aac;限制：取值 aac、mp3、flac、amr;
         */
        public String codec;

        /**
         *采样率;是否必传：否;默认值：44100;限制：1. 单位：Hz2. 可选 8000、11025、22050、32000、44100、48000、960003. 当 Codec 设置为 aac/flac 时，不支持80004. 当 Codec 设置为 mp3 时，不支持8000和960005. 5. 当 Codec 设置为 amr 时，只支持8000;
         */
        public String samplerate;

        /**
         *原始音频码率;是否必传：否;默认值：无;限制：1. 单位：Kbps2. 值范围：[8，1000];
         */
        public String bitrate;

        /**
         *声道数;是否必传：否;默认值：无;限制：1. 当 Codec 设置为 aac/flac，支持1、2、4、5、6、82. 当 Codec 设置为 mp3，支持1、2 3. 当 Codec 设置为 amr，只支持1;
         */
        public String channels;

    }


   
}
