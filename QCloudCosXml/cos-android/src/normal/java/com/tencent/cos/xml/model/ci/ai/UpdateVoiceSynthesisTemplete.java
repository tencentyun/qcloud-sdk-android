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

package com.tencent.cos.xml.model.ci.ai;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class UpdateVoiceSynthesisTemplete {

    /**
     * 模板类型：Tts;是否必传：是
     */
    public String tag = "Tts";

    /**
     * 模板名称，仅支持中文、英文、数字、_、-和*，长度不超过 64;是否必传：是
     */
    public String name;

    /**
     * 处理模式Asyc（异步合成）Sync（同步合成）;是否必传：否
     */
    public String mode;

    /**
     * 音频格式，支持 wav、mp3、pcm ;是否必传：否
     */
    public String codec;

    /**
     * 音色，取值和限制介绍请见下表;是否必传：否
     */
    public String voiceType;

    /**
     * 音量，取值范围 [-10,10];是否必传：否
     */
    public String volume;

    /**
     * 语速，取值范围 [50,200];是否必传：否
     */
    public String speed;

    /**
     * 情绪，不同音色支持的情绪不同，详见下表;是否必传：否
     */
    public String emotion;


   
}
