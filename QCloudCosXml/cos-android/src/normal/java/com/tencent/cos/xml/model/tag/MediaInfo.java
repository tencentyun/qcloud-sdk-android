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

package com.tencent.cos.xml.model.tag;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

/**
 * 获取媒体文件的信息
 */
@XmlBean
public class MediaInfo {
    /**
     * 流信息
     */
    public Stream stream;
    /**
     * 格式信息
     */
    public Format format;

    /**
     * 流信息
     */
    @XmlBean
    public static class Stream {
        /**
         * 视频信息
         */
        public Video video;
        /**
         * 音频信息
         */
        public Audio audio;
        /**
         * 字幕信息
         */
        public Subtitle subtitle;
    }

    /**
     * 视频信息
     */
    @XmlBean
    public static class Video{
        /**
         * 该流的编号
         */
        public int index;
        /**
         * 编解码格式名字
         */
        public String codecName;
        /**
         * 编解码格式的详细名字
         */
        public String codecLongName;
        /**
         * 编码时基
         */
        public String codecTimeBase;
        /**
         * 编码标签名
         */
        public String codecTagString;
        /**
         * 编码标签
         */
        public String codecTag;
        /**
         * 视频编码档位
         */
        public String profile;
        /**
         * 视频高，单位 px
         */
        public int height;
        /**
         * 视频宽，单位 px
         */
        public int width;
        /**
         * 是否有B帧。1表示有，0表示无
         */
        public int hasBFrame;
        /**
         * 视频编码的参考帧个数
         */
        public int refFrames;
        /**
         * 采样宽高比
         */
        public String sar;
        /**
         * 显示宽高比
         */
        public String dar;
        /**
         * 像素格式
         */
        public String pixFormat;
        /**
         * 场的顺序
         */
        public String fieldOrder;
        /**
         * 视频编码等级
         */
        public int level;
        /**
         * 视频帧率
         */
        public float fps;
        /**
         * 平均帧率
         */
        public String avgFps;
        /**
         * 时基
         */
        public String timebase;
        /**
         * 视频开始时间，单位为秒
         */
        public float startTime;
        /**
         * 视频时长，单位为秒
         */
        public float duration;
        /**
         * 比特率，单位为 kbps
         */
        public float bitrate;
        /**
         * 总帧数
         */
        public int numFrames;
        /**
         * 语言
         */
        public String language;
    }

    /**
     * 音频信息
     */
    @XmlBean
    public static class Audio{
        /**
         * 该流的编号
         */
        public int index;
        /**
         * 编解码格式名字
         */
        public String codecName;
        /**
         * 编解码格式的详细名字
         */
        public String codecLongName;
        /**
         * 编码时基
         */
        public String codecTimeBase;
        /**
         * 编码标签名
         */
        public String codecTagString;
        /**
         * 编码标签
         */
        public String codecTag;
        /**
         * 采样格式
         */
        public String sampleFmt;
        /**
         * 采样率
         */
        public float sampleRate;
        /**
         * 通道数量
         */
        public int channel;
        /**
         * 通道格式
         */
        public String channelLayout;
        /**
         * 时基
         */
        public String timebase;
        /**
         * 视频开始时间，单位为秒
         */
        public float startTime;
        /**
         * 视频时长，单位为秒
         */
        public float duration;
        /**
         * 比特率，单位为 kbps
         */
        public float bitrate;
        /**
         * 语言
         */
        public String language;
    }

    /**
     * 字幕信息
     */
    @XmlBean
    public static class Subtitle{
        /**
         * 该流的编号
         */
        public int index;
        /**
         * 语言，und 表示无查询结果
         */
        public String language;
    }

    /**
     * 格式信息
     */
    @XmlBean
    public static class Format {
        /**
         * Stream（包含 Video、Audio、Subtitle）的数量
         */
        public int numStream;
        /**
         * 节目的数量
         */
        public int numProgram;
        /**
         * 容器格式名字
         */
        public String formatName;
        /**
         * 容器格式的详细名字
         */
        public String formatLongName;
        /**
         * 起始时间，单位为秒
         */
        public float startTime;
        /**
         * 时长，单位为秒
         */
        public float duration;
        /**
         * 比特率，单位为 kbps
         */
        public float bitrate;
        /**
         * 大小，单位为 Byte
         */
        public float size;
    }
}
