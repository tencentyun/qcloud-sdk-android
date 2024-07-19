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

package com.tencent.cos.xml.model.ci.common;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * <p>
 * Created by jordanqin on 2023/6/29 11:37.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

@XmlBean(name = "AudioConfig", method = XmlBean.GenerateMethod.ALL)
public class AudioConfig {
    /**
      * 编解码格式，取值 aac、mp3、flac、amr。当 Request.AudioMode 为 MusicMode 时，仅支持 mp3、wav、acc;是否必传：否
      */
    public String codec;

    /**
      * 采样率单位：Hz可选 8000、11025、22050、32000、44100、48000、96000当 Codec 设置为 aac/flac 时，不支持 8000当 Codec 设置为 mp3 时，不支持 8000 和 96000当 Codec 设置为 amr 时，只支持 8000当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
      */
    public String samplerate;

    /**
      * 音频码率单位：Kbps值范围：[8，1000]当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
      */
    public String bitrate;

    /**
      * 声道数当 Codec 设置为 aac/flac，支持1、2、4、5、6、8当 Codec 设置为 mp3，支持1、2 当 Codec 设置为 amr，只支持1当 Request.AudioMode 为 MusicMode 时，该参数无效;是否必传：否
      */
    public String channels;

}
