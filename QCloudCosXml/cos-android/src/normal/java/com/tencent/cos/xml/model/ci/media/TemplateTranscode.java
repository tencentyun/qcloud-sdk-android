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

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class TemplateTranscode {

    /**
     *模板类型：Transcode;是否必传：是;限制：无;
     */
    public String tag = "Transcode";

    /**
     *模板名称，仅支持中文、英文、数字、_、-和*;是否必传：是;限制：无;
     */
    public String name;

    /**
     *容器格式;是否必传：是;限制：无;
     */
    public TemplateTranscodeContainer container;

    /**
     *视频信息;是否必传：否;限制：不传 Video，相当于删除视频信息;
     */
    public TemplateTranscodeVideo video;

    /**
     *时间区间;是否必传：否;限制：无;
     */
    public TemplateTranscodeTimeInterval timeInterval;

    /**
     *音频信息;是否必传：否;限制：不传 Audio，相当于删除音频信息;
     */
    public TemplateTranscodeAudio audio;

    /**
     *转码配置;是否必传：否;限制：无;
     */
    public TemplateTranscodeTransConfig transConfig;

    /**
     *混音参数，详情见 AudioMix;是否必传：否;限制：Audio.Remove 为 false 时生效;
     */
    public AudioMix audioMix;

    /**
     *混音参数, 最多同时传2个。详情见 AudioMixArray;是否必传：否;限制：Audio.Remove 为 false 时生效;
     */
    @XmlElement(flatListNote = true, ignoreListNote = true)
    public List<AudioMix> audioMixArray;

    @XmlBean(name = "Video")
    public static class TemplateTranscodeVideo {
        /**
         *编解码格式;是否必传：否;默认值：H.264当 format 为 WebM 时，为 VP8;限制：H.264H.265VP8VP9AV1;
         */
        public String codec;

        /**
         *宽;是否必传：否;默认值：视频原始宽度;限制：值范围：[128, 4096]单位：px若只设置 Width 时，按照视频原始比例计算 Height 必须为偶数;
         */
        public String width;

        /**
         *高;是否必传：否;默认值：视频原始高度;限制：值范围：[128, 4096]单位：px若只设置 Height 时，按照视频原始比例计算 Width必须为偶数;
         */
        public String height;

        /**
         *帧率;是否必传：否;默认值：无;限制：值范围：(0, 60] 单位：fps ;
         */
        public String fps;

        /**
         *是否删除视频流;是否必传：否;默认值：false;限制：true、false;
         */
        public String remove;

        /**
         *编码级别;是否必传：否;默认值：high;限制：支持 baseline、main、high、auto当 Pixfmt 为 auto 时，该参���仅能设置为 auto，当设置为其他选项时，参数值将被设置为 auto baseline：适合移动设备main：适合标准分辨率设备high：适合高分辨率设备仅H.264支持此参数;
         */
        public String profile;

        /**
         *视频输出文件的码率;是否必传：否;默认值：无;限制：值范围：[10, 50000], 单位：Kbpsauto 表示自适应码率;
         */
        public String bitrate;

        /**
         *码率-质量控制因子;是否必传：否;默认值：无;限制：值范围：(0, 51]如果设置了 Crf，则 Bitrate 的设置失效当 Bitrate 为空时，默认为25 ;
         */
        public String crf;

        /**
         *关键帧间最大帧数;是否必传：否;默认值：无;限制：值范围：[1, 100000];
         */
        public String gop;

        /**
         *视频算法器预置;是否必传：否;默认值：medium，当 Codec 为 VP8 时，为 good;限制：H.264支持该参数，取值 veryfast、fast、medium、slow、slowerP8 支持该参数，取值 good、realtimeAV1 支持该参数，取值 universal、mediumH.265 和 VP9 不支持该参数;
         */
        public String preset;

        /**
         *缓冲区大小;是否必传：否;默认值：无;限制：值范围：[1000, 128000]单位：KbCodec 为 VP8/VP9时不支持此参数;
         */
        public String bufsize;

        /**
         *视频码率峰值;是否必传：否;默认值：无;限制：值范围：[10, 50000]单位：KbpsCodec 为 VP8/VP9时不支持此参数;
         */
        public String maxrate;

        /**
         *视频颜色格式;是否必传：否;默认值：无;限制：H.264支持：yuv420p、yuv422p、yuv444p、yuvj420p、yuvj422p、yuvj444p、autoH.265支持：yuv420p、yuv420p10le、auto Codec 为 VP8/VP9/AV1 时不支持此参数;
         */
        public String pixfmt;

        /**
         *长短边自适应;是否必传：否;默认值：false;限制：true、falseCodec 为 VP8/VP9/AV1 时不支持此参数;
         */
        public String longShortMode;

        /**
         *旋转角度;是否必传：否;默认值：无;限制：值范围：[0, 360)单位：度 ;
         */
        public String rotate;

        /**
         *Roi 强度;是否必传：否;默认值：none;限制：none、low、medium、high Codec 为 VP8/VP9 时不支持此参数;
         */
        public String roi;

        /**
         *自由裁剪;是否必传：否;默认值：无;限制：自定义裁切: width:height:left:top。示例:1280:800:0:140width和height的值需要大于0，left和top的值需要大于等于0Codec 为 H.265/AV1 时不支持此参数。开启自适应编码时, 不支持此参数。开启roi时, 不支持此参数。;
         */
        public String crop;

        /**
         *开启���行扫描;是否必传：否;默认值：false;限制：false/trueCodec 为 H.265/AV1 时不支持此参数。开启自适应码率时, 不支持此参数。开启roi时, 不支持此参数。;
         */
        public String interlaced;

    }

    @XmlBean(name = "Audio")
    public static class TemplateTranscodeAudio {
        /**
         *编解码格式;是否必传：否;默认值：aac当 format 为 WebM 时，为 Vorbis当 format 为 wav 时，为 pcm_s16le;限制：取值 aac、mp3、flac、amr、Vorbis、opus、pcm_s16le;
         */
        public String codec;

        /**
         *采样率;是否必传：否;默认值：44100，当 Codec 为 opus 时，默认值为48000;限制：单位：Hz 可选 8000、11025、12000、16000、22050、24000、32000、44100、48000、88200、96000 不同的封装，mp3 支持不同的采样率，如下表所示当 Codec 设置为 amr 时，只支持8000 当 Codec 设置为 opus 时，支持8000，16000，24000，48000 ;
         */
        public String samplerate;

        /**
         *原始音频码率;是否必传：否;默认值：无;限制：单位：Kbps 值范围：[8，1000] ;
         */
        public String bitrate;

        /**
         *声道数;是否必传：否;默认值：无;限制：当 Codec 设置为 aac/flac，支持1、2、4、5、6、8当 Codec 设置为 mp3/opus 时，支持1、2 当 Codec 设置为 Vorbis 时，只支持2 当 Codec 设置为 amr，只支持1 当 Codec 设置为 pcm_s16le 时，只支持1、2   当封装格式为 dash 时，不支持8  ;
         */
        public String channels;

        /**
         *是否删除源音频流;是否必传：否;默认值：false;限制：取值 true、false;
         */
        public String remove;

        /**
         *保持双音轨;是否必传：否;默认值：false;限制：取值 true、false。 当 Video.Codec 为H.265时，此参数无效;
         */
        public String keepTwoTracks;

        /**
         *转换轨道;是否必传：否;默认值：false;限制：取值 true、false。 当 Video.Codec 为H.265时，此参数无效;
         */
        public String switchTrack;

        /**
         *采样位宽;是否必传：否;默认值：无;限制：当 Codec 设置为 aac，支持 fltp 当 Codec 设置为 mp3，支持 fltp、s16p、s32p 当 Codec 设置为 flac，支持s16、s32、s16p、s32p 当 Codec 设置为 amr，支持s16、s16p当 Codec 设置为 opus，支持s16 当 Codec 设置为 pcm_s16le，支持s16 当 Codec 设置为 Vorbis，支持 fltp 当 Video.Codec 为 H.265 时，此参数无效 ;
         */
        public String sampleFormat;

    }

    @XmlBean(name = "Container")
    public static class TemplateTranscodeContainer {
        /**
         *封装格式：取值见下表;是否必传：是;
         */
        public String format;

        /**
         *分片配置，当 format 为 hls 和 dash 时有效;是否必传：否;
         */
        public TemplateTranscodeClipConfig clipConfig;

    }

    @XmlBean(name = "TransConfig")
    public static class TemplateTranscodeTransConfig {
        /**
         *分辨率调整方式;是否必传：否;默认值：none;限制：取值 scale、crop、pad、none当输出视频的宽高比与原视频不等时，根据此参数做分辨率的相应调整;
         */
        public String adjDarMethod;

        /**
         *是否检查分辨率;是否必传：否;默认值：false;限制：true、false当为 false 时，按照配置参数转码  ;
         */
        public String isCheckReso;

        /**
         *分辨率调整方式;是否必传：否;默认值：0;限制：取值0、1；0表示使用原视频分辨率；1表示返回转码失败当 IsCheckReso 为 true 时生效;
         */
        public String resoAdjMethod;

        /**
         *是否检查视频码率;是否必传：否;默认值：false;限制：true、false当为 false 时，按照配置参数转码;
         */
        public String isCheckVideoBitrate;

        /**
         *视频码率调整方式;是否必传：否;默认值：0;限制：取值0、1；当输出视频码率大于原视频码率时，0表示使用原视频码率；1表示返回转码失败当 IsCheckVideoBitrate 为 true 时生效;
         */
        public String videoBitrateAdjMethod;

        /**
         *是否检查音频码率;是否必传：否;默认值：false;限制：true、false当为 false 时，按照配置参数转码 ;
         */
        public String isCheckAudioBitrate;

        /**
         *音频码率调整方式;是否必传：否;默认值：0;限制：取值0、1；当输出音频码率大于原音频码率时，0 表示使用原音频码率；1表示返回转码失败当 IsCheckAudioBitrate 为 true 时生效;
         */
        public String audioBitrateAdjMethod;

        /**
         *是否检查视频帧率;是否必传：否;默认值：false;限制：true、false 当为 false 时，按照配置参数转码;
         */
        public String isCheckVideoFps;

        /**
         *视频帧率调整方式;是否必传：否;默认值：0;限制：取值0、1；当输出视频帧率大于原视频帧率时，0表示使用原视频帧率；1表示返回转码失败当 IsCheckVideoFps 为 true 时生效;
         */
        public String videoFpsAdjMethod;

        /**
         *是否删除文件中的 MetaData 信息;是否必传：否;默认值：false;限制：true、false 当为 false 时, 保留源文件信息;
         */
        public String deleteMetadata;

        /**
         *是否开启 HDR 转 SDR;是否必传：否;默认值：false;限制：true/false;
         */
        public String isHdr2Sdr;

        /**
         *指定处理的流编号，对应媒体信息中的 Response.MediaInfo.Stream.Video.Index 和 Response.MediaInfo.Stream.Audio.Index，详见 获取媒体信息接口;是否必传：否;默认值：无;限制：无;
         */
        public String transcodeIndex;

        /**
         *hls 加密配置;是否必传：否;默认值：无;限制：无;
         */
        public TemplateTranscodeHlsEncrypt hlsEncrypt;

        /**
         *dash 加密配置;是否必传：否;默认值：无;限制：无;
         */
        public TemplateTranscodeDashEncrypt dashEncrypt;

    }

    @XmlBean(name = "TimeInterval")
    public static class TemplateTranscodeTimeInterval {
        /**
         *开始时间;是否必传：否;默认值：0;限制：[0 视频时长]单位为秒支持 float 格式，执行精度精确到毫秒;
         */
        public String start;

        /**
         *持续时间;是否必传：否;默认值：视频原始时长;限制：[0 视频时长]单位为秒支持 float 格式，执行精度精确到毫秒;
         */
        public String duration;

    }

    @XmlBean(name = "ClipConfig")
    public static class TemplateTranscodeClipConfig {
        /**
         *分片时长，默认5s;是否必传：否;
         */
        public String duration;

    }

    @XmlBean(name = "HlsEncrypt")
    public static class TemplateTranscodeHlsEncrypt {
        /**
         *是否开启 HLS 加密;是否必传：否;默认值：false;限制：true/false当 Container.Format 为 hls 时支持加密;
         */
        public String isHlsEncrypt;

        /**
         *HLS 加密的 key;是否必传：否;默认值：无;限制：当 IsHlsEncrypt 为 true 时，该参数才有意义;
         */
        public String uriKey;

    }

    @XmlBean(name = "DashEncrypt")
    public static class TemplateTranscodeDashEncrypt {
        /**
         *是否开启 DASH 加密;是否必传：否;默认值：false;限制：true/fals当 Container.Format 为 dash 时支持加密;
         */
        public String isEncrypt;

        /**
         *DASH 加密的 key;是否必传：否;默认值：无;限制：当 IsEncrypt 为 true 时，该参数才有意义;
         */
        public String uriKey;

    }


   
}
