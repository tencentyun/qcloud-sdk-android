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

@XmlBean
public class MediaInfoStreamVideo {
    /**
      * 该流的编号;是否必传：否
      */
    public String index;

    /**
      * 编解码格式名字;是否必传：否
      */
    public String codecName;

    /**
      * 编解码格式的详细名字;是否必传：否
      */
    public String codecLongName;

    /**
      * 编码时基;是否必传：否
      */
    public String codecTimeBase;

    /**
      * 编码标签名;是否必传：否
      */
    public String codecTagString;

    /**
      * 编码标签;是否必传：否
      */
    public String codecTag;

    /**
      * 色原;是否必传：否
      */
    public String colorPrimaries;

    /**
      * 色彩范围;是否必传：否
      */
    public String colorRange;

    /**
      * 色彩通道;是否必传：否
      */
    public String colorTransfer;

    /**
      * 视频编码档位;是否必传：否
      */
    public String profile;

    /**
      * 视频高，单位 px;是否必传：否
      */
    public String height;

    /**
      * 视频宽，单位 px;是否必传：否
      */
    public String width;

    /**
      * 是否有B帧。1表示有，0表示无;是否必传：否
      */
    public String hasBFrame;

    /**
      * 视频编码的参考帧个数;是否必传：否
      */
    public String refFrames;

    /**
      * 采样宽高比;是否必传：否
      */
    public String sar;

    /**
      * 显示宽高比;是否必传：否
      */
    public String dar;

    /**
      * 像素格式;是否必传：否
      */
    public String pixFormat;

    /**
      * 场的顺序;是否必传：否
      */
    public String fieldOrder;

    /**
      * 视频编码等级;是否必传：否
      */
    public String level;

    /**
      * 视频帧率;是否必传：否
      */
    public String fps;

    /**
      * 平均帧率;是否必传：否
      */
    public String avgFps;

    /**
      * 时基;是否必传：否
      */
    public String timebase;

    /**
      * 视频开始时间，单位为秒;是否必传：否
      */
    public String startTime;

    /**
      * 视频时长，单位为秒;是否必传：否
      */
    public String duration;

    /**
      * 比特率，单位为 kbps;是否必传：否
      */
    public String bitrate;

    /**
      * 总帧数;是否必传：否
      */
    public String numFrames;

    /**
      * 语言;是否必传：否
      */
    public String language;

}