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

package com.tencent.cos.xml.model.tag.audit.get;


import com.tencent.cos.xml.model.tag.audit.bean.AuditJobsDetail;
import com.tencent.cos.xml.model.tag.audit.bean.AuditSection;
import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import java.util.List;

/**
 * 音频审核返回的具体响应内容
 */
@XmlBean(name = "Response", method = XmlBean.GenerateMethod.FROM)
public class GetAudioAuditJobResponse {
    /**
     * 音频审核任务的详细信息
     */
    public AudioAuditJobsDetail jobsDetail;
    /**
     * 每次请求发送时，服务端将会自动为请求生成一个 ID，遇到问题时，该 ID 能更快地协助定位问题。
     */
    public String requestId;

    /**
     * 音频审核任务的详细信息
     */
    @XmlBean(name = "JobsDetail", method = XmlBean.GenerateMethod.FROM)
    public static class AudioAuditJobsDetail extends AuditJobsDetail {
        /**
         * 本次审核的文件名称，创建任务使用 Object 时返回。
         */
        public String object;
        /**
         * 本次审核的文件链接，创建任务使用 Url 时返回
         */
        public String url;
        /**
         * 该字段用于返回音频文件中已识别的对应文本内容。
         */
        public String audioText;
        /**
         * 审核场景为涉黄的审核结果信息
         */
        public AudioAuditScenarioInfo pornInfo;
        /**
         * 审核场景为涉暴恐的审核结果信息
         */
        public AudioAuditScenarioInfo terrorismInfo;
        /**
         * 审核场景为政治敏感的审核结果信息
         */
        public AudioAuditScenarioInfo politicsInfo;
        /**
         * 审核场景为广告引导的审核结果信息
         */
        public AudioAuditScenarioInfo adsInfo;
        /**
         * 当音频过长时，会对音频进行分段，该字段用于返回音频片段的审核结果，主要包括开始时间和音频审核的相应结果。
         */
        @XmlElement(flatListNote = true)
        public List<AudioSection> section;
    }

    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class AudioAuditScenarioInfo{
        /**
         * 否命中该审核分类，0表示未命中，1表示命中，2表示疑似
         */
        public int hitFlag;
        /**
         * 该分片中审核的结果分数，分数越高表示越敏感
         */
        public int score;
        /**
         * 本次审核的结果标签，如果命中了敏感的关键词，该字段返回对应的关键词。
         */
        public String label;
    }

    /**
     * 声音审核的结果。
     */
    @XmlBean(method = XmlBean.GenerateMethod.FROM)
    public static class AudioSection extends AuditSection {
        /**
         * 视频声音片段的访问地址，您可以通过该地址获取该声音片段的内容，地址格式为标准 URL 格式。
         */
        public String url;
        /**
         * 该字段用于返回当前视频声音的 ASR 文本识别的检测结果（仅在审核策略开启文本内容检测时返回），识别上限为5小时。
         */
        public String text;
        /**
         * 该字段用于返回当前声音片段位于视频中的时间，单位为毫秒，例如5000（视频开始后5000毫秒）。
         */
        public int offsetTime;

        /**
         * 当前视频声音片段的时长，单位毫秒。
         */
        public int duration;

        /**
         * 该字段用于返回检测结果中所对应的优先级最高的恶意标签，表示模型推荐的审核结果，建议您按照业务所需，对不同违规类型与建议值进行处理。 返回值：Normal：正常，Porn：色情，Ads：广告，Politics：涉政，Terrorism：暴恐。
         */
        public String label;

        /**
         * 该字段表示本次判定的审核结果，您可以根据该结果，进行后续的操作；建议您按照业务所需，对不同的审核结果进行相应处理。
         * 有效值：0（审核正常），1 （判定为违规敏感文件），2（疑似敏感，建议人工复核）。
         */
        public int result;
    }
}
