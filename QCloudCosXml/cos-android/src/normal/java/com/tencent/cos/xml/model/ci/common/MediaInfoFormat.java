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
public class MediaInfoFormat {
    /**
      * Stream（包含 Video、Audio、Subtitle）的数量;是否必传：否
      */
    public String numStream;

    /**
      * 节目的数量;是否必传：否
      */
    public String numProgram;

    /**
      * 容器格式名字;是否必传：否
      */
    public String formatName;

    /**
      * 容器格式的详细名字;是否必传：否
      */
    public String formatLongName;

    /**
      * 起始时间，单位为秒;是否必传：否
      */
    public String startTime;

    /**
      * 时长，单位为秒;是否必传：否
      */
    public String duration;

    /**
      * 比特率，单位为 kbps;是否必传：否
      */
    public String bitrate;

    /**
      * 大小，单位为 Byte;是否必传：否
      */
    public String size;

}
