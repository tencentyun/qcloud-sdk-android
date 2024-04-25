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

import com.tencent.cos.xml.model.tag.CallBackMqConfig;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;

@XmlBean(name = "Request", method = XmlBean.GenerateMethod.TO)
public class SubmitMediaSegmentJob {

    /**
     *创建任务的 Tag：Segment;是否必传：是;
     */
    public String tag = "Segment";

    /**
     *待操作的文件信息;是否必传：是;
     */
    public SubmitMediaSegmentJobInput input;

    /**
     *操作规则;是否必传：是;
     */
    public SubmitMediaSegmentJobOperation operation;

    /**
     *任务回调格式，JSON 或 XML，默认 XML，优先级高于队列的回调格式;是否必传：否;
     */
    public String callBackFormat;

    /**
     *任务回调类型，Url 或 TDMQ，默认 Url，优先级高于队列的回调类型;是否必传：否;
     */
    public String callBackType;

    /**
     *任务回调地址，优先级高于队列的回调地址。设置为 no 时，表示队列的回调地址不产生回调;是否必传：否;
     */
    public String callBack;

    /**
     *任务回调TDMQ配置，当 CallBackType 为 TDMQ 时必填。详情见 CallBackMqConfig;是否必传：否;
     */
    public CallBackMqConfig callBackMqConfig;

    @XmlBean(name = "Input", method = XmlBean.GenerateMethod.TO)
    public static class SubmitMediaSegmentJobInput {
        /**
         *文件路径;是否必传：是;
         */
        public String object;

    }

    @XmlBean(name = "Operation", method = XmlBean.GenerateMethod.TO)
    public static class SubmitMediaSegmentJobOperation {
        /**
         *转封装参数;是否必传：是;
         */
        public SubmitMediaSegmentJobSegment segment;

        /**
         *结果输出配置;是否必传：是;
         */
        public SubmitMediaSegmentJobOutput output;

        /**
         *任务优先级，级别限制：0 、1 、2 。级别越大任务优先级越高，默认为0;是否必传：否;
         */
        public String jobLevel;

    }

    @XmlBean(name = "Output")
    public static class SubmitMediaSegmentJobOutput {
        /**
         *存储桶的地域;是否必传：是;
         */
        public String region;

        /**
         *存储结果的存储桶;是否必传：是;
         */
        public String bucket;

        /**
         *输出结果的文件名，如果设置了Duration, 且 Format 不为 HLS 或 m3u8 时，文件名必须包含${number}参数作为自定义转封装后每一小段音/视频流的输出序号;是否必传：是;
         */
        public String object;

    }

    @XmlBean(name = "Segment")
    public static class SubmitMediaSegmentJobSegment {
        /**
         *封装格式;是否必传：是;限制：aac、mp3、flac、mp4、ts、mkv、avi、hls、m3u8;
         */
        public String format;

        /**
         *转封装时长，单位：秒;是否必传：否;限制：不小于5的整数;
         */
        public String duration;

        /**
         *处理的流编号，对应媒体信息中的 Response.MediaInfo.Stream.Video.Index 和 Response.MediaInfo.Stream.Audio.Index，详见 获取媒体信息接口;是否必传：否;限制：无;
         */
        public String transcodeIndex;

        /**
         *hls 加密配置;是否必传：否;限制：无，只有当封装格式为 hls 时生效;
         */
        public SubmitMediaSegmentJobHlsEncrypt hlsEncrypt;

    }

    @XmlBean(name = "HlsEncrypt")
    public static class SubmitMediaSegmentJobHlsEncrypt {
        /**
         *是否开启 HLS 加密;是否必传：否;默认值：false;限制：1. true/false 2. Segment.Format 为 HLS 时支持加密;
         */
        public String isHlsEncrypt;

        /**
         *HLS 加密的 key;是否必传：否;默认值：无;限制：当 IsHlsEncrypt 为 true 时，该参数才有意义;
         */
        public String uriKey;

    }


   
}
